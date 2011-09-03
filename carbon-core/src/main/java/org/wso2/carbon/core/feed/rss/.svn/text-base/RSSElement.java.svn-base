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

package org.wso2.carbon.core.feed.rss;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
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

public class RSSElement {

    private static Log log = LogFactory.getLog(RSSElement.class);

    private OMElement channelElement;
    private OMElement rssElement;

    public RSSElement(OMFactory fac, AxisConfiguration axisConfig) {
        rssElement = fac.createOMElement("rss", null);
        rssElement.addAttribute("version", "2.0", null);

        channelElement = fac.createOMElement("channel", null);
        OMElement title = fac.createOMElement("title", null);
        title.setText("WSO2 WSAS RSS");
        channelElement.addChild(title);

        OMElement link = fac.createOMElement("link", null);
        try {
            String ip = NetworkUtils.getLocalHostname();
            ip = "http://" + ip + ":" + CarbonUtils.getTransportPort(axisConfig, "http");
            link.setText(ip);
        } catch (SocketException e) {
            log.warn("NetworkUtils.getLocalHostname()");
        }
        channelElement.addChild(link);

        SimpleDateFormat zulu =
                new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");

        OMElement description = fac.createOMElement("description", null);
        description.setText("WSO2 WSAS RSS for service publication");
        channelElement.addChild(description);

        OMElement pubDate = fac.createOMElement("pubDate", null);
        Date currentTime = Calendar.getInstance().getTime();
        pubDate.setText(zulu.format(currentTime));
        channelElement.addChild(pubDate);

        OMElement lastBuildDate = fac.createOMElement("lastBuildDate", null);
        lastBuildDate.setText(zulu.format(currentTime));
        channelElement.addChild(lastBuildDate);

        OMElement category = fac.createOMElement("category", null);
        category.setText("WEB SERVICE FEEDS");
        channelElement.addChild(category);

        OMElement generator = fac.createOMElement("generator", null);
        generator.setText("WSO2 WSAS RSS Feed Generator 1.1");
        channelElement.addChild(generator);

        OMElement language = fac.createOMElement("language", null);
        language.setText("en-us");
        channelElement.addChild(language);
        rssElement.addChild(channelElement);
    }

    public OMElement getElement() {
        return rssElement;
    }

    public OMElement getChannelElement() {
        return channelElement;
    }
}
