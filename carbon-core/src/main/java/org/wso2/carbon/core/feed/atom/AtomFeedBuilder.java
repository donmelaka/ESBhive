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

import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.core.feed.FeedConstants;
import org.wso2.carbon.core.feed.FeedFactory;
import org.wso2.carbon.core.feed.rss.ServiceElement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AtomFeedBuilder {

    private static Log log = LogFactory.getLog(AtomFeedBuilder.class);
    private AtomFeed atomFeed;

    public AtomFeedBuilder(AxisConfiguration axisConfig) {
        atomFeed = FeedFactory.getAtomFeed(FeedConstants.WSO2WSAS_ATOM_FEED, axisConfig);
    }

    public void addService(AxisService serviceDescription, String itemDefaultLink) {
        try {
            ServiceElement serviceElement = new ServiceElement();
            serviceElement.setDescription(serviceDescription.getDocumentation());
            serviceElement.setName(serviceDescription.getName());
            List operations = new ArrayList();
            for (Iterator ops = serviceDescription.getOperations();
                 ops.hasNext();) {
                AxisOperation axisOperation = (AxisOperation) ops.next();
                operations.add(axisOperation.getName().getLocalPart());
            }
            serviceElement.setOperations(operations);
            List modules = new ArrayList();
            for (Iterator mdits = serviceDescription.getEngagedModules().iterator();
                 mdits.hasNext();) {
                AxisModule axisModule = (AxisModule) mdits.next();
                modules.add(axisModule.getName());
            }
            serviceElement.setModules(modules);

            serviceElement.setEventType(ServiceElement.SERVICE_ADD);
            atomFeed.addAtomElement(serviceElement);
        } catch (Exception e) {
            log.error("Exception occurred while appending add service " +
                      serviceDescription.getName() + " event to the Atom feed", e);
        }
    }

    public void removeService(AxisService serviceDescription, String itemDefaultLink) {
        try {
            ServiceElement serviceElement = new ServiceElement();
            serviceElement.setDescription(serviceDescription.getDocumentation());
            serviceElement.setName(serviceDescription.getName());
            serviceElement.setUpdate(true);
            serviceElement.setEventType(ServiceElement.SERVICE_REMOVE);
            atomFeed.addAtomElement(serviceElement);
        } catch (Exception e) {
            log.error("Exception occurred while appending the remove service " +
                      serviceDescription.getName() + " event to the Atom feed", e);
        }
    }

    public void stopService(AxisService serviceDescription, String itemDefaultLink) {
        try {
            ServiceElement serviceElement = new ServiceElement();
            serviceElement.setDescription(serviceDescription.getDocumentation());
            serviceElement.setName(serviceDescription.getName());
            serviceElement.setUpdate(true);
            serviceElement.setEventType(ServiceElement.SERVICE_STOP);
            atomFeed.addAtomElement(serviceElement);
        } catch (Exception e) {
            log.error("Exception occurred while appending the stop service " +
                      serviceDescription.getName() + " event to the Atom feed", e);
        }
    }

    public void startService(AxisService serviceDescription, String itemDefaultLink) {
        try {
            ServiceElement serviceElement = new ServiceElement();
            serviceElement.setDescription(serviceDescription.getDocumentation());
            serviceElement.setName(serviceDescription.getName());
            serviceElement.setUpdate(true);
            serviceElement.setEventType(ServiceElement.SERVICE_START);
            atomFeed.addAtomElement(serviceElement);
        } catch (Exception e) {
            log.error("Exception occurred while appending the start service " +
                      serviceDescription.getName() + " event to the Atom feed", e);
        }
    }

    public void serviceUpdate(AxisService serviceDescription, String itemDefaultLink) {
        try {
            ServiceElement serviceElement = new ServiceElement();
            serviceElement.setDescription(serviceDescription.getDocumentation());
            serviceElement.setName(serviceDescription.getName());
            serviceElement.setUpdate(true);

            List operations = new ArrayList();

            for (Iterator ops = serviceDescription.getOperations();
                 ops.hasNext();) {
                AxisOperation axisOperation = (AxisOperation) ops.next();
                operations.add(axisOperation.getName().getLocalPart());
            }
            serviceElement.setOperations(operations);

            List modules = new ArrayList();
            for (Iterator mdits = serviceDescription.getEngagedModules().iterator();
                 mdits.hasNext();) {
                AxisModule axisModule = (AxisModule) mdits.next();
                modules.add(axisModule.getName());
            }
            serviceElement.setModules(modules);
            serviceElement.setEventType(ServiceElement.SERVICE_UPDATE);
            atomFeed.addAtomElement(serviceElement);
        } catch (Exception e) {
            log.error("Exception occurred while appending the update service " +
                      serviceDescription.getName() + " event to the Atom feed", e);
        }
    }
}
