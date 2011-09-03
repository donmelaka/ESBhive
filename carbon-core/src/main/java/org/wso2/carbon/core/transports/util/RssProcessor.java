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
import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.core.feed.FeedConstants;
import org.wso2.carbon.core.feed.FeedFactory;
import org.wso2.carbon.core.feed.rss.RSSFeed;
import org.wso2.carbon.core.transports.HttpGetRequestProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 */
public class RssProcessor implements HttpGetRequestProcessor {
    private static Log log = LogFactory.getLog(RequestProcessorUtil.class);

    public void process(HttpServletRequest request, HttpServletResponse response,
                        ConfigurationContext configurationContext) throws Exception {
        try {
            response.setContentType("text/xml; charset=utf-8");
            RSSFeed rssFeed = FeedFactory.getRSSFeed(FeedConstants.WSO2WSAS_RSS_FEED,
                                                     configurationContext.getAxisConfiguration());
            if (rssFeed != null) {
                XMLStreamWriter writer =
                        XMLOutputFactory.newInstance()
                                .createXMLStreamWriter(response.getOutputStream());
                writer.writeProcessingInstruction("xml-stylesheet",
                                                  "  type=\"text/xsl\" href=\"" +
                                                  (configurationContext.getContextRoot().equals("/") ?
                                                   "" : configurationContext.getContextRoot()) +
                                                                                               "/styles/rss.xsl\"");
                OMElement feedElement =
                        rssFeed.getFeedElement(configurationContext.getServiceContextPath());
                feedElement.serialize(writer);
                writer.flush();
            }
        } catch (Exception e) {
            log.error("Could not process RSS feed", e);
        }
    }
}
