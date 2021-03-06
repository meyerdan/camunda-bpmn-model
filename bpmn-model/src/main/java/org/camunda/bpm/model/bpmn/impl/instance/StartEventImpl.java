/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.model.bpmn.impl.instance;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.builder.StartEventBuilder;
import org.camunda.bpm.model.bpmn.instance.CatchEvent;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.xml.ModelBuilder;
import org.camunda.bpm.model.xml.impl.instance.ModelTypeInstanceContext;
import org.camunda.bpm.model.xml.type.ModelElementTypeBuilder;
import org.camunda.bpm.model.xml.type.attribute.Attribute;

import static org.camunda.bpm.model.bpmn.impl.BpmnModelConstants.*;

/**
 * The BPMN startEvent element
 *
 * @author Sebastian Menski
 */
public class StartEventImpl extends CatchEventImpl implements StartEvent {

  private static Attribute<Boolean> isInterruptingAttribute;
  private static Attribute<String> formKeyAttribute;

  public static void registerType(ModelBuilder modelBuilder) {

    ModelElementTypeBuilder typeBuilder = modelBuilder.defineType(StartEvent.class, BPMN_ELEMENT_START_EVENT)
      .namespaceUri(BPMN20_NS)
      .extendsType(CatchEvent.class)
      .instanceProvider(new ModelElementTypeBuilder.ModelTypeInstanceProvider<StartEvent>() {
        public StartEvent newInstance(ModelTypeInstanceContext instanceContext) {
          return new StartEventImpl(instanceContext);
        }
      });

    isInterruptingAttribute = typeBuilder.booleanAttribute(BPMN_ATTRIBUTE_IS_INTERRUPTING)
      .defaultValue(true)
      .build();

    formKeyAttribute = typeBuilder.stringAttribute(ACTIVITI_ATTRIBUTE_FORM_KEY)
      .namespace(ACTIVITI_NS)
      .build();

    typeBuilder.build();
  }

  public StartEventImpl(ModelTypeInstanceContext context) {
    super(context);
  }

  @Override
  @SuppressWarnings("unchecked")
  public StartEventBuilder builder() {
    return new StartEventBuilder((BpmnModelInstance) modelInstance, this);
  }

  public boolean isInterrupting() {
    return isInterruptingAttribute.getValue(this);
  }

  public void setInterrupting(boolean isInterrupting) {
    isInterruptingAttribute.setValue(this, isInterrupting);
  }

  public String getFormKey() {
    return formKeyAttribute.getValue(this);
  }

  public void setFormKey(String formKey) {
    formKeyAttribute.setValue(this, formKey);
  }
}
