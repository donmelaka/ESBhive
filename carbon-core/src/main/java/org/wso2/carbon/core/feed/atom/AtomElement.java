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

package org.wso2.carbon.core.feed.atom;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.util.UUIDGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.axis2.engine.AxisConfiguration;
import org.wso2.carbon.utils.NetworkUtils;
import org.wso2.carbon.utils.ServerConstants;
import org.wso2.carbon.utils.CarbonUtils;

import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * The OMElement representation of the Atom AbstractFeed
 */
public class AtomElement {

    private static Log log = LogFactory.getLog(AtomElement.class);
    private OMElement atomElement;

    public AtomElement(OMFactory fac, AxisConfiguration axisConfig) {
        atomElement = fac.createOMElement("feed", null);
        atomElement.addAttribute("xmlns", "http://www.w3.org/2005/Atom", null);
//        atomElement.addAttribute("version", "0.3", ns);
        OMElement titileElement = fac.createOMElement("title", null);
        titileElement.setText("WSO2 WSAS Atom");
        atomElement.addChild(titileElement);
        OMElement link = fac.createOMElement("link", null);
        String ip = null;
        try {
            ip = NetworkUtils.getLocalHostname();
            ip = "http://" + ip + ":" + CarbonUtils.getTransportPort(axisConfig, "http");
        } catch (SocketException e) {
            log.warn("NetworkUtils.getLocalHostname()");
        }
        OMElement modified = fac.createOMElement("updated", null);
        SimpleDateFormat zulu =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date currentTime = Calendar.getInstance().getTime();
        modified.setText(zulu.format(currentTime));
        atomElement.addChild(modified);
        link.addAttribute("rel", "alternate", null);
        link.addAttribute("type", "text/html", null);
        link.addAttribute("href", ip, null);
        atomElement.addChild(link);
        OMElement author = fac.createOMElement("author", null);
        OMElement name = fac.createOMElement("name", null);
        OMElement uri = fac.createOMElement("uri", null);
        uri.setText("http://www.wso2.org");
        OMElement emal = fac.createOMElement("email", null);
        emal.setText("info@wso2.org");
        name.setText("WSO2 WSAS");
        author.addChild(name);
        author.addChild(uri);
        author.addChild(emal);
        OMElement id = fac.createOMElement("id", null);
        id.setText(UUIDGenerator.getUUID());
        atomElement.addChild(id);

        atomElement.addChild(author);
    }

    public OMElement getElement() {
        return atomElement;
    }
}
