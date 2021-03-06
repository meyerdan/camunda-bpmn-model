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
package org.camunda.bpm.model.bpmn.impl;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.xml.ModelValidationException;
import org.camunda.bpm.model.xml.impl.ModelImpl;
import org.camunda.bpm.model.xml.impl.parser.AbstractModelParser;
import org.camunda.bpm.model.xml.impl.util.ReflectUtil;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.SchemaFactory;
import java.io.InputStream;

/**
 * <p>The parser used when parsing BPMN Files</p>
 *
 * @author Daniel Meyer
 *
 */
public class BpmnParser extends AbstractModelParser {

 private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
 private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

  private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

  public BpmnParser() {
    this.schemaFactory = SchemaFactory.newInstance(W3C_XML_SCHEMA);
    try {
      this.schema = schemaFactory.newSchema(ReflectUtil.getResource(BpmnModelConstants.BPMN_20_SCHEMA_LOCATION));
    } catch (SAXException e) {
      throw new ModelValidationException("Unable to parse schema:" + ReflectUtil.getResource(BpmnModelConstants.BPMN_20_SCHEMA_LOCATION));
    }
  }

  @Override
  protected void configureFactory(DocumentBuilderFactory dbf) {
    dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
    dbf.setAttribute(JAXP_SCHEMA_SOURCE, ReflectUtil.getResource(BpmnModelConstants.BPMN_20_SCHEMA_LOCATION).toString());
    super.configureFactory(dbf);
  }

  @Override
  protected BpmnModelInstanceImpl createModelInstance(Document document) {
    return new BpmnModelInstanceImpl((ModelImpl) Bpmn.INSTANCE.getBpmnModel(), Bpmn.INSTANCE.getBpmnModelBuilder(), document);
  }

  @Override
  public BpmnModelInstanceImpl parseModelFromStream(InputStream inputStream) {
    return (BpmnModelInstanceImpl) super.parseModelFromStream(inputStream);
  }

  @Override
  public BpmnModelInstanceImpl getEmptyModel() {
    return (BpmnModelInstanceImpl) super.getEmptyModel();
  }

}
