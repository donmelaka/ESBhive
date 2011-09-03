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

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.engine.AxisConfiguration;
import org.wso2.carbon.core.feed.AbstractFeed;
import org.wso2.carbon.core.feed.FeedListener;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class RSSFeed extends AbstractFeed {

    private List elementList;
    private OMFactory fac;

    public RSSFeed(AxisConfiguration axisConfig) {
        super(axisConfig);
        this.elementList = new ArrayList();
        fac = OMAbstractFactory.getOMFactory();
    }

    public void addRSSElement(ServiceElement element) {
        if (elementList.size() == FeedListener.MAX_ARRAY_SIZE) {
            elementList.remove(FeedListener.MAX_ARRAY_SIZE - 1);
        }
        elementList.add(0, element);
    }

    public OMElement getFeedElement(String serviceContextPath) throws SocketException {
        RSSElement rssElement = new RSSElement(fac, axisConfig);
        OMElement atomElement = rssElement.getElement();
        OMElement channelElement = rssElement.getChannelElement();
        for (int i = 0; i < elementList.size(); i++) {
            ServiceElement serviceElement = (ServiceElement) elementList.get(i);
            serviceElement.setUrl(getUrl(serviceElement.getName(), serviceContextPath));
            channelElement.addChild(serviceElement.getOMElement(fac));
        }
        return atomElement;
    }
}
