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

package org.camunda.bpm.model.bpmn.impl.instance;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.builder.ServiceTaskBuilder;
import org.camunda.bpm.model.bpmn.instance.Operation;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.Task;
import org.camunda.bpm.model.xml.ModelBuilder;
import org.camunda.bpm.model.xml.impl.instance.ModelTypeInstanceContext;
import org.camunda.bpm.model.xml.type.ModelElementTypeBuilder;
import org.camunda.bpm.model.xml.type.attribute.Attribute;
import org.camunda.bpm.model.xml.type.reference.AttributeReference;

import static org.camunda.bpm.model.bpmn.impl.BpmnModelConstants.*;
import static org.camunda.bpm.model.xml.type.ModelElementTypeBuilder.ModelTypeInstanceProvider;

/**
 * The BPMN serviceTask element
 *
 * @author Sebastian Menski
 */
public class ServiceTaskImpl extends TaskImpl implements ServiceTask {

  private static Attribute<String> implementationAttribute;
  private static AttributeReference<Operation> operationRefAttribute;
  private static Attribute<String> classAttribute;

  public static void registerType(ModelBuilder modelBuilder) {
    ModelElementTypeBuilder typeBuilder = modelBuilder.defineType(ServiceTask.class, BPMN_ELEMENT_SERVICE_TASK)
      .namespaceUri(BPMN20_NS)
      .extendsType(Task.class)
      .instanceProvider(new ModelTypeInstanceProvider<ServiceTask>() {
        public ServiceTask newInstance(ModelTypeInstanceContext instanceContext) {
          return new ServiceTaskImpl(instanceContext);
        }
      });

    implementationAttribute = typeBuilder.stringAttribute(BPMN_ATTRIBUTE_IMPLEMENTATION)
      .defaultValue("##WebService")
      .build();

    operationRefAttribute = typeBuilder.stringAttribute(BPMN_ATTRIBUTE_OPERATION_REF)
      .qNameAttributeReference(Operation.class)
      .build();

    classAttribute = typeBuilder.stringAttribute(ACTIVITI_ATTRIBUTE_CLASS)
      .namespace(ACTIVITI_NS)
      .build();

    typeBuilder.build();
  }

  public ServiceTaskImpl(ModelTypeInstanceContext context) {
    super(context);
  }

  @Override
  @SuppressWarnings("unchecked")
  public ServiceTaskBuilder builder() {
    return new ServiceTaskBuilder((BpmnModelInstance) modelInstance, this);
  }

  public String getImplementation() {
    return implementationAttribute.getValue(this);
  }

  public void setImplementation(String implementation) {
    implementationAttribute.setValue(this, implementation);
  }

  public Operation getOperation() {
    return operationRefAttribute.getReferenceTargetElement(this);
  }

  public void setOperation(Operation operation) {
    operationRefAttribute.setReferenceTargetElement(this, operation);
  }

  public String getClassName() {
    return classAttribute.getValue(this);
  }

  public void setClassName(String className) {
    classAttribute.setValue(this, className);
  }
}
