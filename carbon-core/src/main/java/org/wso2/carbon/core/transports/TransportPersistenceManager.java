/*
 *  Copyright (c) 2005-2008, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.core.transports;

import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.Collection;
import org.wso2.carbon.core.internal.CarbonCoreServiceComponent;
import org.wso2.carbon.core.RegistryResources;
import org.wso2.carbon.CarbonException;
import org.apache.axis2.description.TransportInDescription;
import org.apache.axis2.description.TransportOutDescription;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.deployment.DeploymentConstants;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.net.URL;

public class TransportPersistenceManager {

    private static final String TRANSPORT_LISTENER = "listener";
    private static final String TRANSPORT_SENDER = "sender";

    private static final Log log = LogFactory.getLog(TransportPersistenceManager.class);

    private static Registry getRegistry() throws Exception {
        return CarbonCoreServiceComponent.getRegistryService().getConfigSystemRegistry();
    }

    public static void saveTransportListener(TransportInDescription transportIn,
                                             boolean enabled) throws Exception {

        if (log.isDebugEnabled()) {
            log.debug("Saving the " + transportIn.getName() + " listener configuration");
        }
        OMElement element = TransportBuilderUtils.serializeTransportListener(transportIn);
        saveTransport(element, enabled);
    }

    public static void saveTransportSender(TransportOutDescription transportOut,
                                             boolean enabled) throws Exception {

        if (log.isDebugEnabled()) {
            log.debug("Saving the " + transportOut.getName() + " sender configuration");
        }
        OMElement element = TransportBuilderUtils.serializeTransportSender(transportOut);
        saveTransport(element, enabled);
    }

    public static void saveTransport(OMElement element, boolean enabled) throws Exception {

        boolean listener = false;
        if (DeploymentConstants.TAG_TRANSPORT_RECEIVER.equals(element.getLocalName())) {
            listener = true;
        } else if (DeploymentConstants.TAG_TRANSPORT_SENDER.equals(element.getLocalName())) {
            listener = false;
        } else {
            handleFault("Invalid transport configuration element");
        }

        String name = element.getAttributeValue(new QName(DeploymentConstants.ATTRIBUTE_NAME));
        if (name == null || "".equals(name)) {
            handleFault("Transport configuration does not specify the name attribute");
        }

        String path = RegistryResources.TRANSPORTS + name + "/";
        if (listener) {
            path += TRANSPORT_LISTENER;
        } else {
            path += TRANSPORT_SENDER;
        }

        Registry registry = getRegistry();
        Resource resource = registry.newResource();
        resource.setContent(element.toString());
        resource.setProperty(RegistryResources.Transports.PROTOCOL_NAME, name);
        resource.setProperty(RegistryResources.Transports.IS_ENABLED, String.valueOf(enabled));
        registry.put(path, resource);
        resource.discard();
    }

    public static OMElement getTransportElement(String name, boolean listener) throws Exception {
        Registry registry = getRegistry();
        Resource resource = getTransportResource(registry, name, listener);

        if (resource != null) {
            ByteArrayInputStream in = new ByteArrayInputStream((byte[]) resource.getContent());
            resource.discard();
            StAXOMBuilder builder = new StAXOMBuilder(in);
            return builder.getDocumentElement();
        }
        return null;
    }

    public static TransportInDescription getTransportListener(String name,
                                                              boolean init) throws Exception {

        OMElement element = getTransportElement(name, true);
        if (element != null) {
            return TransportBuilderUtils.processTransportReceiver(element, init);
        }
        return null;
    }

    public static TransportOutDescription getTransportSender(String name, 
                                                             boolean init) throws Exception {

        OMElement element = getTransportElement(name, false);
        if (element != null) {
            return TransportBuilderUtils.processTransportSender(element, init);
        }
        return null;
    }

    public static void addParameter(String name, boolean listener, boolean enabled,
                                    Parameter p) throws Exception {

        OMElement element = getTransportElement(name, listener);
        if (element != null) {
            element.addChild(TransportBuilderUtils.serializeParameter(p,
                OMAbstractFactory.getOMFactory()));
            saveTransport(element, enabled);
        }
    }

    public static void removeParameter(String name, boolean listener, boolean enabled,
                                       String paramName) throws Exception {

        OMElement element = getTransportElement(name, listener);
        if (element != null) {
            Iterator params = element.getChildrenWithLocalName(DeploymentConstants.TAG_PARAMETER);
            OMElement p = null;
            while (params.hasNext()) {
                p = (OMElement) params.next();
                if (paramName.equals(p.getAttributeValue(new QName(
                        DeploymentConstants.ATTRIBUTE_NAME)))) {
                    break;
                }
            }
            if (p != null) {
                p.detach();
            }
            saveTransport(element, enabled);
        }
    }

    public static String[] getEnabledTransports(boolean listener) throws Exception {
        Registry registry = getRegistry();
        if (!registry.resourceExists(RegistryResources.TRANSPORTS)) {
            return null;
        }

        Collection transports = (Collection) registry.get(RegistryResources.TRANSPORTS);
        String[] childResources = transports.getChildren();
        if (childResources == null || childResources.length == 0) {
            return null;
        }

        List<String> enabledTransports = new ArrayList<String>();
        for (String childPath : childResources) {
            if (!childPath.endsWith("/")) {
                childPath += "/";
            }

            if (listener) {
                childPath += TRANSPORT_LISTENER;
            } else {
                childPath += TRANSPORT_SENDER;
            }

            if (registry.resourceExists(childPath)) {
                Resource resource = registry.get(childPath);
                if (Boolean.valueOf(resource.getProperty(RegistryResources.Transports.IS_ENABLED))) {
                    enabledTransports.add(resource.getProperty(RegistryResources.
                            Transports.PROTOCOL_NAME));
                }
            }
        }

        if (enabledTransports.size() > 0) {
            return enabledTransports.toArray(new String[enabledTransports.size()]);
        }

        return null;
    }

    public static Resource getTransportResource(String name) throws Exception {
        Registry registry = getRegistry();
        return getTransportResource(registry, name, true);
    }

    public static void setTransportEnabled(String name, boolean listener,
                                           boolean enabled) throws Exception {

        Registry registry = getRegistry();
        Resource resource = getTransportResource(registry, name, listener);
        if (resource != null) {
            resource.setProperty(RegistryResources.Transports.IS_ENABLED, String.valueOf(enabled));
            registry.put(resource.getPath(), resource);
            resource.discard();
        }
    }

    public static void updateEnabledTransports(
            java.util.Collection<TransportInDescription> listeners,
            java.util.Collection<TransportOutDescription> senders) throws Exception {

        Registry registry = getRegistry();
        for (TransportInDescription listener : listeners) {
            if (getTransportResource(registry, listener.getName(), true) == null) {
                saveTransportListener(listener, true);
            }
        }

        for (TransportOutDescription sender : senders) {
            if (getTransportResource(registry, sender.getName(), false) == null) {
                saveTransportSender(sender, true);
            }
        }
    }

    /**
     * This method checks whether the configuration of a particular transport is available in
     * the registry. If it does then the method will simply return. Otherwise it will attempt
     * to load the transport configurations from the specified transport configuration file.
     * Once loaded the transport configurations will be permanently stored in the registry so
     * the subsequent calls to this method will always find the transport configurations in
     * the registry.
     *
     * @param transport Name of the transport
     * @param configFileURL URL to the transport configuration file available in the transport bundle
     * @throws Exception on error
     */
    public static void saveTransportConfiguration(String transport,
                                                  URL configFileURL) throws Exception {

        if (log.isDebugEnabled()) {
            log.debug("Persiting the initial transport configuration for " + transport);
        }

        if (TransportPersistenceManager.getTransportElement(transport, true) == null) {
            OMElement conf = TransportBuilderUtils.parseTransportConfiguration(
                    transport, configFileURL, true);
            if (conf != null) {
                saveTransport(conf, false);
            } else {
                log.warn("No transport listener configuration found for : " + transport);
            }
        }

        if (TransportPersistenceManager.getTransportElement(transport, false) == null) {
            OMElement conf = TransportBuilderUtils.parseTransportConfiguration(
                    transport, configFileURL, false);
            if (conf != null) {
                saveTransport(conf, false);
            } else {
                log.warn("No transport sender configuration found for : " + transport);
            }
        }
    }

    private static Resource getTransportResource(Registry registry, String name,
                                                 boolean listener) throws Exception {

        String path = RegistryResources.TRANSPORTS + name + "/";
        if (listener) {
            path += TRANSPORT_LISTENER;
        } else {
            path += TRANSPORT_SENDER;
        }

        if (registry.resourceExists(path)) {
            return registry.get(path);
        }
        return null;
    }

    private static void handleFault(String msg) throws CarbonException {
        log.error(msg);
        throw new CarbonException(msg);
    }

}
