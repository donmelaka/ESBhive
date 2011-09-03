/*
 * Copyright 2005-2007 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.core.transports.util;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 *
 */
public class RequestProcessorUtil {
    private static Log log = LogFactory.getLog(RequestProcessorUtil.class);

    /**
     * @param byteArrayOutStream
     * @param out
     * @param annotatedXsl
     * @param contextRoot
     * @param annotation         : If annotation is false PI would not be attached.
     */
    public static void writeDocument(ByteArrayOutputStream byteArrayOutStream,
                                     OutputStream out,
                                     String annotatedXsl,
                                     String contextRoot, boolean annotation) {
        XMLStreamWriter writer;
        try {
            ByteArrayInputStream bais =
                    new ByteArrayInputStream(byteArrayOutStream.toByteArray());
            XMLStreamReader reader =
                    XMLInputFactory.newInstance().createXMLStreamReader(bais);
            StAXOMBuilder builder = new StAXOMBuilder(reader);
            OMElement docElem = builder.getDocumentElement();
            writer = XMLOutputFactory.newInstance().createXMLStreamWriter(out);
            if (annotatedXsl != null && annotation) {
                writer.writeProcessingInstruction("xml-stylesheet",
                                                  "  type=\"text/xsl\" href=\"" +
                                                  (contextRoot.equals("/") ? "" : contextRoot) +
                                                  "/styles/" +
                                                  annotatedXsl + "\"");
            }
            docElem.serialize(writer);
            writer.flush();
        } catch (XMLStreamException e) {
            log.error("Error occurred while trying to write processing instruction for attaching " +
                      "annotated style sheet", e);
        }
    }

    public static String getServiceContextPath(ConfigurationContext configCtx) {
        String serviceContextPath = configCtx.getServiceContextPath();
        if (!configCtx.getContextRoot().equals("/")
            && !serviceContextPath.startsWith("/")) {
            serviceContextPath = "/" + serviceContextPath;
        }
        return serviceContextPath;
    }
}
