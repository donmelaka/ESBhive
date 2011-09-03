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

package org.wso2.carbon.core.feed;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.AxisServiceGroup;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.description.ParameterInclude;
import org.apache.axis2.description.ParameterIncludeImpl;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.AxisEvent;
import org.apache.axis2.engine.AxisObserver;
import org.wso2.carbon.core.feed.atom.AtomFeedBuilder;
import org.wso2.carbon.core.feed.rss.RSSFeedBuilder;
import org.wso2.carbon.core.util.SystemFilter;

import java.util.ArrayList;

public class FeedListener implements AxisObserver {

    public static int MAX_ARRAY_SIZE = 10;

    private ParameterInclude parameterInclude = new ParameterIncludeImpl();
    private RSSFeedBuilder rssFeedBuilder;
    private AtomFeedBuilder atomFeedBuilder;
    private ArrayList services;

    // The initialization code will go here
    public void init(AxisConfiguration axisConfig) {
        rssFeedBuilder = new RSSFeedBuilder(axisConfig);
        atomFeedBuilder = new AtomFeedBuilder(axisConfig);
        services = new ArrayList();
    }

    public void serviceUpdate(AxisEvent event, AxisService service) {
        AxisServiceGroup axisServiceGroup = (AxisServiceGroup) service.getParent();
        String itemHome = FeedConstants.ITEM_URL_VALUE;
        if (getParameter(FeedConstants.ITEM_URL_KEY) != null) {
            itemHome = getParameter(FeedConstants.ITEM_URL_KEY).getValue().toString();
        }
        if (SystemFilter.isFilteredOutService(axisServiceGroup)) {
            return;
        }
        if (services.contains(service)) {
            rssFeedBuilder.serviceUpdate(service, itemHome);
            atomFeedBuilder.serviceUpdate(service, itemHome);
        } else {
            if (AxisEvent.SERVICE_DEPLOY == event.getEventType()) {
                rssFeedBuilder.addService(service, itemHome);
                atomFeedBuilder.addService(service, itemHome);
            } else if (AxisEvent.SERVICE_REMOVE == event.getEventType()) {
                rssFeedBuilder.removeService(service, itemHome);
                atomFeedBuilder.removeService(service, itemHome);
            } else if (AxisEvent.SERVICE_START == event.getEventType()) {
                rssFeedBuilder.startService(service, itemHome);
                atomFeedBuilder.startService(service, itemHome);
            } else if (AxisEvent.SERVICE_STOP == event.getEventType()) {
                rssFeedBuilder.stopService(service, itemHome);
                atomFeedBuilder.stopService(service, itemHome);
            }
        }
    }

    public void serviceGroupUpdate(AxisEvent event,
                                   AxisServiceGroup serviceGroup) {
        //TODO Fill the service group update
    }

    public void moduleUpdate(AxisEvent event, AxisModule module) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addParameter(Parameter param) throws AxisFault {
        if ("maxSize".equals(param.getName())) {
            String value = (String) param.getValue();
            MAX_ARRAY_SIZE = Integer.parseInt(value.trim());
        }
        parameterInclude.addParameter(param);
    }

    public void removeParameter(Parameter param) throws AxisFault {
        parameterInclude.removeParameter(param);
    }

    public void deserializeParameters(OMElement parameterElement) throws AxisFault {
        parameterInclude.deserializeParameters(parameterElement);
    }

    public Parameter getParameter(String name) {
        return parameterInclude.getParameter(name);
    }

    public ArrayList getParameters() {
        return parameterInclude.getParameters();
    }

    public boolean isParameterLocked(String parameterName) {
        return parameterInclude.isParameterLocked(parameterName);
    }
}
