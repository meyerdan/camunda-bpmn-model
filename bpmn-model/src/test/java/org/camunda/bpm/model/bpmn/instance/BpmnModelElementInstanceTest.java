/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camunda.bpm.model.bpmn.instance;

import org.camunda.bpm.model.bpmn.util.GetBpmnModelElementTypeRule;
import org.camunda.bpm.model.xml.Model;
import org.camunda.bpm.model.xml.ModelInstance;
import org.camunda.bpm.model.xml.impl.type.ModelElementTypeImpl;
import org.camunda.bpm.model.xml.impl.util.ModelTypeException;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.test.assertions.AttributeAssert;
import org.camunda.bpm.model.xml.test.assertions.ChildElementAssert;
import org.camunda.bpm.model.xml.test.assertions.ModelElementTypeAssert;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.w3c.dom.DOMException;

import java.util.Collection;

import static org.camunda.bpm.model.bpmn.impl.BpmnModelConstants.BPMN20_NS;
import static org.camunda.bpm.model.xml.test.assertions.ModelAssertions.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Sebastian Menski
 */
public abstract class BpmnModelElementInstanceTest {

  protected class TypeAssumption {

    public final ModelElementType extendsType;
    public final boolean isAbstract;

    public TypeAssumption(boolean isAbstract) {
      this.extendsType = null;
      this.isAbstract = isAbstract;
    }

    public TypeAssumption(Class<? extends ModelElementInstance> extendsType, boolean isAbstract) {
      this.extendsType = model.getType(extendsType);
      this.isAbstract = isAbstract;
    }
  }

  protected class ChildElementAssumption {

    public final ModelElementType childElementType;
    public final int minOccurs;
    public final int maxOccurs;

    public ChildElementAssumption(Class<? extends ModelElementInstance> childElementType) {
      this(childElementType, 0, -1);
    }

    public ChildElementAssumption(Class<? extends ModelElementInstance> childElementType, int minOccurs) {
      this(childElementType, minOccurs, -1);
    }

    public ChildElementAssumption(Class<? extends ModelElementInstance> childElementType, int minOccurs, int maxOccurs) {
      this.childElementType = model.getType(childElementType);
      this.minOccurs = minOccurs;
      this.maxOccurs = maxOccurs;
    }
  }

  protected class AttributeAssumption {

    public final String attributeName;
    public final String namespace;
    public final boolean isIdAttribute;
    public final boolean isRequired;
    public final Object defaultValue;

    public AttributeAssumption(String attributeName) {
      this(attributeName, null, false, false, null);
    }

    public AttributeAssumption(String attributeName, String namespace) {
      this(attributeName, namespace, false, false, null);
    }

    public AttributeAssumption(String attributeName, boolean isIdAttribute) {
      this(attributeName, null, isIdAttribute, false, null);
    }

    public AttributeAssumption(String attributeName, String namespace, boolean isIdAttribute) {
      this(attributeName, namespace, isIdAttribute, false, null);
    }

    public AttributeAssumption(String attributeName, boolean isIdAttribute, boolean isRequired) {
      this(attributeName, null, isIdAttribute, isRequired, null);
    }

    public AttributeAssumption(String attributeName, String namespace, boolean isIdAttribute, boolean isRequired) {
      this(attributeName, namespace, isIdAttribute, isRequired, null);
    }

    public AttributeAssumption(String attributeName, boolean isIdAttribute, boolean isRequired, Object defaultValue) {
      this(attributeName, null, isIdAttribute, isRequired, defaultValue);
    }

    public AttributeAssumption(String attributeName, String namespace, boolean isIdAttribute, boolean isRequired, Object defaultValue) {
      this.attributeName = attributeName;
      this.namespace = namespace;
      this.isIdAttribute = isIdAttribute;
      this.isRequired = isRequired;
      this.defaultValue = defaultValue;
    }
  }

  @ClassRule
  public static final GetBpmnModelElementTypeRule bpmnModelElementTypeRule = new GetBpmnModelElementTypeRule();

  public static ModelInstance modelInstance;
  public static Model model;
  public static ModelElementType modelElementType;

  @BeforeClass
  public static void getModelElementType() {
    modelInstance = bpmnModelElementTypeRule.getModelInstance();
    model = bpmnModelElementTypeRule.getModel();
    modelElementType = bpmnModelElementTypeRule.getModelElementType();
  }

  public abstract TypeAssumption getTypeAssumption();
  public abstract Collection<ChildElementAssumption> getChildElementAssumptions();
  public abstract Collection<AttributeAssumption> getAttributesAssumptions();


  public ModelElementTypeAssert assertThatType() {
    return assertThat(modelElementType);
  }

  public AttributeAssert assertThatAttribute(String attributeName) {
    return assertThat(modelElementType.getAttribute(attributeName));
  }

  public ChildElementAssert assertThatChildElement(ModelElementType childElementType) {
    ModelElementTypeImpl modelElementTypeImpl = (ModelElementTypeImpl) modelElementType;
    return assertThat(modelElementTypeImpl.getChildElementCollection(childElementType));
  }

  public ModelElementType getType(Class<? extends ModelElementInstance> instanceClass) {
    return model.getType(instanceClass);
  }

  @Test
  public void testType() {
    assertThatType().isPartOfModel(model);
    assertThatType().hasTypeNamespace(BPMN20_NS);

    TypeAssumption assumption = getTypeAssumption();

    if (assumption.isAbstract) {
      assertThatType().isAbstract();
    }
    else {
      assertThatType().isNotAbstract();
    }
    if (assumption.extendsType == null) {
       assertThatType().extendsNoType();
    }
    else {
      assertThatType().extendsType(assumption.extendsType);
    }

    if (assumption.isAbstract) {
      try {
        modelInstance.newInstance(modelElementType);
        fail("Element type " + modelElementType.getTypeName() + " is abstract.");
      }
      catch (DOMException e) {
        // expected exception
      }
      catch (ModelTypeException e) {
        // expected exception
      }
      catch (Exception e) {
        fail("Unexpected exception " + e.getMessage());
      }
    }
    else {
      ModelElementInstance modelElementInstance = modelInstance.newInstance(modelElementType);
      assertThat(modelElementInstance).isNotNull();
    }
  }

  @Test
  public void testChildElements() {
    Collection<ChildElementAssumption> childElementAssumptions = getChildElementAssumptions();
    if (childElementAssumptions == null) {
      assertThatType().hasNoChildElements();
    }
    else {
      for (ChildElementAssumption assumption : childElementAssumptions) {
        assertThatType().hasChildElements(assumption.childElementType);
        assertThatChildElement(assumption.childElementType)
          .occursMinimal(assumption.minOccurs)
          .occursMaximal(assumption.maxOccurs);
      }
    }
  }

  @Test
  public void testAttributes() {
    Collection<AttributeAssumption> attributesAssumptions = getAttributesAssumptions();
    if (attributesAssumptions == null) {
      assertThatType().hasNoAttributes();
    }
    else {
      for (AttributeAssumption assumption : attributesAssumptions) {
        assertThatType().hasAttributes(assumption.attributeName);
        AttributeAssert attributeAssert = assertThatAttribute(assumption.attributeName);

        attributeAssert.hasOwningElementType(modelElementType);

        if (assumption.namespace != null) {
          attributeAssert.hasNamespaceUri(assumption.namespace);
        }
        else {
          attributeAssert.hasNoNamespaceUri();
        }

        if (assumption.isIdAttribute) {
          attributeAssert.isIdAttribute();
        }
        else {
          attributeAssert.isNotIdAttribute();
        }

        if (assumption.isRequired) {
          attributeAssert.isRequired();
        }
        else {
          attributeAssert.isOptional();
        }

        if (assumption.defaultValue == null) {
          attributeAssert.hasNoDefaultValue();
        }
        else {
          attributeAssert.hasDefaultValue(assumption.defaultValue);
        }

      }
    }
  }

}
