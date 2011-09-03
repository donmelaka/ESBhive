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

import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.core.feed.FeedConstants;
import org.wso2.carbon.core.feed.FeedFactory;

import java.util.ArrayList;
import java.util.Iterator;

public class RSSFeedBuilder {
    private static Log log = LogFactory.getLog(RSSFeedBuilder.class);
    private RSSFeed rssFeed;

    public RSSFeedBuilder(AxisConfiguration axisConfig) {
        rssFeed = FeedFactory.getRSSFeed(FeedConstants.WSO2WSAS_RSS_FEED, axisConfig);
    }

    public void addService(AxisService serviceDescription, String itemDefaultLink) {
        try {
            ServiceElement serviceElement = new ServiceElement();
            serviceElement.setDescription(serviceDescription.getDocumentation());
            serviceElement.setName(serviceDescription.getName());

            ArrayList operations = new ArrayList();
            for (Iterator ops = serviceDescription.getOperations(); ops.hasNext();) {
                AxisOperation axisOperation = (AxisOperation) ops.next();
                operations.add(axisOperation.getName().getLocalPart());
            }
            serviceElement.setOperations(operations);
            ArrayList modules = new ArrayList();
            for (Iterator engModIter = serviceDescription.getEngagedModules().iterator();
                 engModIter.hasNext();) {
                AxisModule axisModule = (AxisModule) engModIter.next();
                modules.add(axisModule.getName());
            }
            serviceElement.setModules(modules);
            serviceElement.setRssType(true);
            serviceElement.setEventType(ServiceElement.SERVICE_ADD);
            rssFeed.addRSSElement(serviceElement);
        } catch (Exception e) {
            log.error("Exception occurred while appending the add service " +
                      serviceDescription.getName() + " event to the RSS feed", e);
        }
    }

    public void removeService(AxisService serviceDescription, String itemDefaultLink) {
        try {
            ServiceElement serviceElement = new ServiceElement();
            serviceElement.setRssType(true);
            serviceElement.setDescription(serviceDescription.getDocumentation());
            serviceElement.setName(serviceDescription.getName());
            serviceElement.setEventType(ServiceElement.SERVICE_REMOVE);
            rssFeed.addRSSElement(serviceElement);
        } catch (Exception e) {
            log.error("Exception occurred while appending the remove service " +
                      serviceDescription.getName() + " event to the RSS feed", e);
        }
    }

    public void stopService(AxisService serviceDescription, String itemDefaultLink) {
        try {
            ServiceElement serviceElement = new ServiceElement();
            serviceElement.setRssType(true);
            serviceElement.setDescription(serviceDescription.getDocumentation());
            serviceElement.setName(serviceDescription.getName());
            serviceElement.setEventType(ServiceElement.SERVICE_STOP);
            rssFeed.addRSSElement(serviceElement);
        } catch (Exception e) {
            log.error("Exception occurred while appending the stop service " +
                      serviceDescription.getName() + " event to the RSS feed", e);
        }
    }

    public void startService(AxisService serviceDescription, String itemDefaultLink) {
        try {
            ServiceElement serviceElement = new ServiceElement();
            serviceElement.setRssType(true);
            serviceElement.setDescription(serviceDescription.getDocumentation());
            serviceElement.setName(serviceDescription.getName());
            serviceElement.setEventType(ServiceElement.SERVICE_START);
            rssFeed.addRSSElement(serviceElement);
        } catch (Exception e) {
            log.error("Exception occurred while appending the start service " +
                      serviceDescription.getName() + " event to the RSS feed", e);
        }
    }

    public void serviceUpdate(AxisService serviceDescription, String itemDefaultLink) {
        try {
            ServiceElement serviceElement = new ServiceElement();
            serviceElement.setDescription(serviceDescription.getDocumentation());
            serviceElement.setName(serviceDescription.getName());
            serviceElement.setUpdate(true);

            ArrayList oprations = new ArrayList();
            Iterator ops = serviceDescription.getOperations();
            while (ops.hasNext()) {
                AxisOperation axisOperation = (AxisOperation) ops.next();
                oprations.add(axisOperation.getName().getLocalPart());
            }
            serviceElement.setOperations(oprations);

            ArrayList modules = new ArrayList();
            for (Iterator engModIter = serviceDescription.getEngagedModules().iterator();
                 engModIter.hasNext();) {
                AxisModule axisModule = (AxisModule) engModIter.next();
                modules.add(axisModule.getName());
            }
            serviceElement.setModules(modules);
            serviceElement.setEventType(ServiceElement.SERVICE_UPDATE);
            serviceElement.setRssType(true);
            rssFeed.addRSSElement(serviceElement);
        } catch (Exception e) {
            log.error("Exception occurred while appending the update service " +
                      serviceDescription.getName() + " event to the RSS feed", e);
        }
    }
}
