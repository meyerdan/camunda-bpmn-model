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
package org.camunda.bpm.model.xml.impl.instance;

import org.camunda.bpm.model.xml.impl.ModelInstanceImpl;
import org.camunda.bpm.model.xml.impl.type.ModelElementTypeImpl;
import org.w3c.dom.Element;

/**
 * @author Daniel Meyer
 * @author Sebastian Menski
 *
 */
public final class ModelTypeInstanceContext {

  private final ModelInstanceImpl model;
  private final Element domElement;
  private final ModelElementTypeImpl modelType;

  public ModelTypeInstanceContext(Element domElement, ModelInstanceImpl model, ModelElementTypeImpl modelType) {
    this.domElement = domElement;
    this.model = model;
    this.modelType = modelType;
  }

  /**
   * @return the dom element
   */
  public Element getDomElement() {
    return domElement;
  }

  /**
   * @return the model
   */
  public ModelInstanceImpl getModel() {
    return model;
  }

  /**
   * @return the modelType
   */
  public ModelElementTypeImpl getModelType() {
    return modelType;
  }

}
