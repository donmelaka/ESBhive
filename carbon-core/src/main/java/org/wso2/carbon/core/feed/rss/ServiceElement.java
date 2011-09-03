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
import org.apache.axiom.om.util.UUIDGenerator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ServiceElement {

    public static final int SERVICE_ADD = 0;
    public static final int SERVICE_REMOVE = 1;
    public static final int SERVICE_UPDATE = 2;
    public static final int SERVICE_STOP = 3;
    public static final int SERVICE_START = 4;

    private static final SimpleDateFormat dateFormat =
            new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");

    private String name;
    private String description;
    private List operations;
    private List modules;
    private String url;
    private boolean update;
    private int eventType;
    private boolean rssType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List getOperations() {
        return operations;
    }

    public void setOperations(List operations) {
        this.operations = operations;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List getModules() {
        return modules;
    }

    public void setModules(List modules) {
        this.modules = modules;
    }

    private OMElement getServiceRemovedRSS(OMFactory fac) {
        OMElement item = fac.createOMElement("item", null);
        OMElement title = fac.createOMElement("title", null);
        if (!isUpdate()) {
            title.setText(name);
        } else {
            title.setText(name + " - removed");
        }
        item.addChild(title);
        OMElement decriptionElement = fac.createOMElement("description", null);
        decriptionElement.setText("  " + name + " : service removed from the system");
        item.addChild(decriptionElement);

        OMElement pubDate = fac.createOMElement("pubDate", null);
        Date currentTime = Calendar.getInstance().getTime();
        pubDate.setText(dateFormat.format(currentTime));
        item.addChild(pubDate);

        OMElement guid = fac.createOMElement("guid", null);
        guid.addAttribute("isPermaLink", "false", null);
        guid.setText(UUIDGenerator.getUUID());
        item.addChild(guid);
        return item;
    }

    private OMElement stopService(OMFactory fac) {
        OMElement item = fac.createOMElement("item", null);
        OMElement title = fac.createOMElement("title", null);
        if (!isUpdate()) {
            title.setText(name);
        } else {
            title.setText(name + " - deactivated");
        }
        item.addChild(title);
        OMElement decriptionElement = fac.createOMElement("description", null);
        decriptionElement.setText("  " + name + " : service marked as inactive");
        item.addChild(decriptionElement);

        OMElement pubDate = fac.createOMElement("pubDate", null);
        Date currentTime = Calendar.getInstance().getTime();
        pubDate.setText(dateFormat.format(currentTime));
        item.addChild(pubDate);

        OMElement guid = fac.createOMElement("guid", null);
        guid.addAttribute("isPermaLink", "false", null);
        guid.setText(UUIDGenerator.getUUID());
        item.addChild(guid);
        return item;
    }

    private OMElement startService(OMFactory fac) {
        OMElement item = fac.createOMElement("item", null);
        OMElement title = fac.createOMElement("title", null);
        if (!isUpdate()) {
            title.setText(name);
        } else {
            title.setText(name + " - activated");
        }
        item.addChild(title);
        OMElement decriptionElement = fac.createOMElement("description", null);
        decriptionElement.setText("  " + name + ": service marked as active");
        item.addChild(decriptionElement);

        OMElement pubDate = fac.createOMElement("pubDate", null);
        Date currentTime = Calendar.getInstance().getTime();
        pubDate.setText(dateFormat.format(currentTime));
        item.addChild(pubDate);

        OMElement guid = fac.createOMElement("guid", null);
        guid.addAttribute("isPermaLink", "false", null);
        guid.setText(UUIDGenerator.getUUID());
        item.addChild(guid);
        return item;
    }

    private OMElement getRSSServiceElement(OMFactory fac) {
        OMElement item = fac.createOMElement("item", null);
        OMElement title = fac.createOMElement("title", null);
        if (!isUpdate()) {
            title.setText(name);
        } else {
            title.setText("  " + name + " - updated");
        }
        item.addChild(title);

        OMElement epr = fac.createOMElement("link", null);
        epr.setText(url + "?info");
        item.addChild(epr);

        OMElement decriptionElement = fac.createOMElement("description", null);

        decriptionElement.setText(description);


        item.addChild(decriptionElement);

        OMElement pubDate = fac.createOMElement("pubDate", null);
        Date currentTime = Calendar.getInstance().getTime();
        pubDate.setText(dateFormat.format(currentTime));
        item.addChild(pubDate);

        OMElement guid = fac.createOMElement("guid", null);
        //isPermaLink="false"
        guid.addAttribute("isPermaLink", "false", null);
        guid.setText(UUIDGenerator.getUUID());
        item.addChild(guid);
        return item;
    }

    private OMElement getServiceRemovedAtom(OMFactory fac) {
        OMElement item = fac.createOMElement("entry", null);
        OMElement title = fac.createOMElement("title", null);
        if (!isUpdate()) {
            title.setText(name);
        } else {
            title.setText("  " + name + " - removed");
        }
        OMElement epr = fac.createOMElement("link", null);
        epr.addAttribute("rel", "alternate", null);
        epr.addAttribute("type", "text/html", null);
        epr.addAttribute("href", url + "?info", null);
        item.addChild(epr);
        item.addChild(title);
        OMElement summary = fac.createOMElement("summary", null);
        summary.setText("  " + name + " removed from the system");
        item.addChild(summary);
        SimpleDateFormat zulu =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        OMElement pubDate = fac.createOMElement("published", null);
        Date currentTime = Calendar.getInstance().getTime();
        pubDate.setText(zulu.format(currentTime));
        item.addChild(pubDate);

        OMElement modified = fac.createOMElement("updated", null);
        modified.setText(zulu.format(currentTime));
        item.addChild(modified);
        //modified

        OMElement guid = fac.createOMElement("id", null);
        guid.setText(UUIDGenerator.getUUID());
        item.addChild(guid);
        return item;
    }

    private OMElement stopServiceAtom(OMFactory fac) {
        OMElement item = fac.createOMElement("entry", null);
        OMElement title = fac.createOMElement("title", null);
        if (!isUpdate()) {
            title.setText(name);
        } else {
            title.setText(name + " - deactivated");
        }

        OMElement epr = fac.createOMElement("link", null);
        epr.addAttribute("rel", "alternate", null);
        epr.addAttribute("type", "text/html", null);
        epr.addAttribute("href", url + "?info", null);
        item.addChild(epr);
        item.addChild(title);
        OMElement summary = fac.createOMElement("summary", null);
        summary.setText("  " + name + " : service marked as inactive");
        item.addChild(summary);
        SimpleDateFormat zulu =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        OMElement pubDate = fac.createOMElement("published", null);
        Date currentTime = Calendar.getInstance().getTime();
        pubDate.setText(zulu.format(currentTime));
        item.addChild(pubDate);

        OMElement modified = fac.createOMElement("updated", null);
        modified.setText(zulu.format(currentTime));
        item.addChild(modified);
        //modified

        OMElement guid = fac.createOMElement("id", null);
        guid.setText(UUIDGenerator.getUUID());
        item.addChild(guid);

        return item;
    }

    private OMElement startServiceAtom(OMFactory fac) {
        OMElement item = fac.createOMElement("entry", null);
        OMElement title = fac.createOMElement("title", null);
        if (!isUpdate()) {
            title.setText(name);
        } else {
            title.setText(name + " - activated");
        }
        OMElement epr = fac.createOMElement("link", null);
        epr.addAttribute("rel", "alternate", null);
        epr.addAttribute("type", "text/html", null);
        epr.addAttribute("href", url + "?info", null);
        item.addChild(epr);

        item.addChild(title);
        OMElement summary = fac.createOMElement("summary", null);
        summary.setText("  " + name + " : service marked as active");
        item.addChild(summary);

        SimpleDateFormat zulu =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        OMElement pubDate = fac.createOMElement("published", null);
        Date currentTime = Calendar.getInstance().getTime();
        pubDate.setText(zulu.format(currentTime));
        item.addChild(pubDate);

        OMElement modified = fac.createOMElement("updated", null);
        modified.setText(zulu.format(currentTime));
        item.addChild(modified);
        //modified

        OMElement guid = fac.createOMElement("id", null);
        guid.setText(UUIDGenerator.getUUID());
        item.addChild(guid);

        return item;
    }

    private OMElement getAtomServiceElement(OMFactory fac) {
        OMElement item = fac.createOMElement("entry", null);
        OMElement title = fac.createOMElement("title", null);
        if (eventType == SERVICE_UPDATE) {
            title.setText(name + " - updated");
        } else {
            title.setText(name);
        }
        item.addChild(title);

        OMElement epr = fac.createOMElement("link", null);
        epr.addAttribute("rel", "alternate", null);
        epr.addAttribute("type", "text/html", null);
        epr.addAttribute("href", url + "?info", null);
        item.addChild(epr);
        OMElement pubDate = fac.createOMElement("published", null);
        SimpleDateFormat zulu =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date currentTime = Calendar.getInstance().getTime();
        pubDate.setText(zulu.format(currentTime));
        item.addChild(pubDate);

        OMElement modified = fac.createOMElement("updated", null);
        modified.setText(zulu.format(currentTime));
        item.addChild(modified);
        //modified

        OMElement guid = fac.createOMElement("id", null);
        guid.setText(UUIDGenerator.getUUID());
        item.addChild(guid);
        return item;
    }

    public OMElement getOMElement(OMFactory fac) {
        if (rssType) {
            switch (eventType) {
                case SERVICE_ADD: {
                    return getRSSServiceElement(fac);
                }
                case SERVICE_REMOVE: {
                    return getServiceRemovedRSS(fac);
                }
                case SERVICE_UPDATE: {
                    return getRSSServiceElement(fac);
                }
                case SERVICE_START: {
                    return startService(fac);
                }
                case SERVICE_STOP: {
                    return stopService(fac);
                }
            }
        } else {
            switch (eventType) {
                case SERVICE_ADD: {
                    return getAtomServiceElement(fac);
                }
                case SERVICE_REMOVE: {
                    return getServiceRemovedAtom(fac);
                }
                case SERVICE_UPDATE: {
                    return getAtomServiceElement(fac);
                }
                case SERVICE_START: {
                    return startServiceAtom(fac);
                }
                case SERVICE_STOP: {
                    return stopServiceAtom(fac);
                }
            }
        }
        return null;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public void setRssType(boolean rssType) {
        this.rssType = rssType;
    }
}
