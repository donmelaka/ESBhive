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

package org.wso2.carbon.core.persistence;

import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.Association;
import org.wso2.carbon.utils.CarbonUtils;
import org.wso2.carbon.core.RegistryResources;
import org.wso2.carbon.CarbonConstants;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.AxisFault;
import org.apache.axis2.util.JavaUtils;
import org.apache.axis2.description.*;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;

public class ServiceGroupPersistenceManager extends AbstractPersistenceManager {

    // initialize log variable for this class
    static {
        log = LogFactory.getLog(ServiceGroupPersistenceManager.class);
    }

    /**
     * Constructor gets the axis config and calls the super constructor.
     *
     * @param axisConfig - AxisConfiguration
     * @throws AxisFault - if the config registry is not found
     */
    public ServiceGroupPersistenceManager(AxisConfiguration axisConfig) throws AxisFault {
        super(axisConfig);
    }

    /**
     * Returns the registry Resource for the specified Service group name
     *
     * @param serviceGroupId - Service Group name
     * @return - Service Group resource
     * @throws Exception - on registry transaction error
     */
    public Resource getServiceGroup(String serviceGroupId) throws Exception {
        try {
            String resourcePath = RegistryResources.SERVICE_GROUPS + serviceGroupId;
            if (configRegistry.resourceExists(resourcePath)) {
                Resource resource = configRegistry.get(resourcePath);
                if (resource.getProperty(RegistryResources.SUCCESSFULLY_ADDED) != null) {
                    return resource;
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("Successfully retrieved resource for " +
                        serviceGroupId + " Service Group");
            }
        } catch (Throwable e) {
            handleException("Could not get the Service Group resource from Config Registry", e);
        }
        return null;
    }

    /**
     * Deletes the registry resource of the specified service group
     *
     * @param serviceGroup - AxisServiceGroup instance
     * @throws Exception - on error
     */
    public void deleteServiceGroup(AxisServiceGroup serviceGroup) throws Exception {
        Iterator services = serviceGroup.getServices();
        Parameter param = serviceGroup.getParameter(CarbonConstants.PRESERVE_SERVICE_HISTORY_PARAM);
        if (services.hasNext() && ((AxisService) services.next()).isClientSide()) {
            return;
        }
        try {
            if (param == null || !JavaUtils.isTrue(param.getValue().toString())) {
                configRegistry.delete(PersistenceUtils.getResourcePath(serviceGroup));
            }
            if (log.isDebugEnabled()) {
                log.debug("Successfully deleted resource for " +
                        serviceGroup.getServiceGroupName() + " Service Group");
            }
        } catch (Throwable e) {
            handleExceptionWithRollback("Could not delete Service Group " +
                    "resource from Config Registry", e);
        }
    }

    /**
     * Handle the engagement of the module to service group at the registry level
     *
     * @param module       - AxisModule instance
     * @param serviceGroup - AxisServiceGroup instance
     * @throws Exception - on error
     */
    public void engageModuleForServiceGroup(AxisModule module, AxisServiceGroup serviceGroup)
            throws Exception {
        try {
            handleModuleForAxisDescription(module,
                    PersistenceUtils.getResourcePath(serviceGroup), true);
            if (log.isDebugEnabled()) {
                log.debug("Successfully engaged " + module.getName() + " module to " +
                        serviceGroup.getServiceGroupName() + " service group ");
            }
        } catch (Throwable e) {
            handleExceptionWithRollback("Unable to engage " + module.getName() + " module to " +
                    serviceGroup.getServiceGroupName() + " service group ", e);
        }
    }

    /**
     * Handle the dis-engagement of the module to service group at the registry level
     *
     * @param module       - AxisModule instance
     * @param serviceGroup - AxisServiceGroup instance
     * @throws Exception - on error
     */
    public void disengageModuleForServiceGroup(AxisModule module, AxisServiceGroup serviceGroup)
            throws Exception {
        try {
            handleModuleForAxisDescription(module,
                    PersistenceUtils.getResourcePath(serviceGroup), false);
            if (log.isDebugEnabled()) {
                log.debug("Successfully disengaged " + module.getName() + " module from " +
                        serviceGroup.getServiceGroupName() + " service group ");
            }
        } catch (Throwable e) {
            handleExceptionWithRollback("Unable to disengage " + module.getName() +
                    " module from " + serviceGroup.getServiceGroupName() + " service group ", e);
        }
    }

    /**
     * Handle initialization of an already existing service group in regsitry. Loads all parameters
     * and engaged modules into the service group instance.
     *
     * @param sgResource   - Service group resource
     * @param serviceGroup - AxisServiceGroup instance
     * @throws Exception - on error
     */
    public synchronized void handleExistingServiceGroupInit(Resource
            sgResource, AxisServiceGroup serviceGroup) throws Exception {
        try {
            configRegistry.beginTransaction();
            // Add the Service Group Parameters
            loadParameters(serviceGroup, sgResource.getPath());

            // Disengage all the statically engaged modules (i.e. those module
            // engaged from the services.xml file)
            serviceGroup.getEngagedModules().clear();

            // Engage modules to service group
            Association[] associations = configRegistry.getAssociations(sgResource.getPath(),
                    RegistryResources.Associations.ENGAGED_MODULES);
            for (Association association : associations) {
                Resource moduleResource = configRegistry.get(association.getDestinationPath());
                AxisModule axisModule = PersistenceUtils.getAxisModule(moduleResource, axisConfig);
                if (!isGloballyEngaged(association.getDestinationPath())) {
                    serviceGroup.disengageModule(axisModule);
                    serviceGroup.engageModule(axisModule);
                }
                moduleResource.discard();
            }
            configRegistry.commitTransaction();

            if (log.isDebugEnabled()) {
                log.debug("Initialized Service Group - " + serviceGroup.getServiceGroupName());
            }
        } catch (Throwable e) {
            handleExceptionWithRollback("Unable to handle service group init. Service group: " +
                    serviceGroup.getServiceGroupName(), e);
        }
    }

    /**
     * Handle initialization of a new service group in regsitry. Writes all parameters
     * and engaged modules into the registry.
     *
     * @param serviceGroup - AxisServiceGroup instance
     * @throws Exception - on error
     */
    public synchronized void handleNewServiceGroupAddition(AxisServiceGroup
            serviceGroup) throws Exception {
        Iterator services = serviceGroup.getServices();
        if (services.hasNext() && ((AxisService) services.next()).isClientSide()) {
            return;
        }

        try {
            configRegistry.beginTransaction();
            Resource serviceGroupResource = configRegistry.newCollection();
            String hashValue = CarbonUtils.computeServiceHash(serviceGroup);
            if (hashValue != null) {
                serviceGroupResource.addProperty(RegistryResources
                        .ServiceGroupProperties.HASH_VALUE, hashValue);
            }
            String sgName = serviceGroup.getServiceGroupName();
            String sgResourcePath = RegistryResources.SERVICE_GROUPS + sgName;
            configRegistry.put(sgResourcePath, serviceGroupResource);

            // Handle ServiceGroup-Module engagement
            for (Object o : serviceGroup.getEngagedModules()) {
                AxisModule axisModule = (AxisModule) o;
                if (!axisConfig.isEngaged(axisModule.getName())) {
                    String moduleResourcePath = PersistenceUtils.getResourcePath(axisModule);
                    configRegistry.addAssociation(sgResourcePath, moduleResourcePath,
                            RegistryResources.Associations.ENGAGED_MODULES);
                }
            }
            // Handle Service Group Parameters
            writeParameters(serviceGroup.getParameters(), sgResourcePath);

            serviceGroupResource.addProperty(RegistryResources.SUCCESSFULLY_ADDED, "true");
            configRegistry.put(sgResourcePath, serviceGroupResource);
            configRegistry.commitTransaction();

            if (log.isDebugEnabled()) {
                log.debug("Added new service group - " + sgName);
            }
        } catch (Throwable e) {
            handleExceptionWithRollback("Unable to handle new service group addition. " +
                    "Service group: " + serviceGroup.getServiceGroupName(), e);
        }
    }

    /**
     * Set the given property to the service group resource in the registry
     *
     * @param serviceGroup  - AxisServiceGroup instance
     * @param propertyName  - name of the property to set
     * @param propertyValue - value to set
     * @throws Exception - on error
     */
    public void setServiceGroupProperty(AxisServiceGroup serviceGroup, String propertyName,
                                        String propertyValue) throws Exception {
        try {
            String serviceResourcePath = RegistryResources.SERVICE_GROUPS
                    + serviceGroup.getServiceGroupName();
            configRegistry.beginTransaction();
            if (configRegistry.resourceExists(serviceResourcePath)) {
                Resource serviceResource = configRegistry.get(serviceResourcePath);
                serviceResource.setProperty(propertyName, propertyValue);
                configRegistry.put(serviceResourcePath, serviceResource);
                serviceResource.discard();
            }
            configRegistry.commitTransaction();
        } catch (Throwable e) {
            handleExceptionWithRollback("Unable to set property " + propertyName
                    + " to service group " + serviceGroup.getServiceGroupName(), e);
        }
    }

    /**
     * Persist the given service group parameter. If the parameter already exists in registry,
     * update it. Otherwise, create a new parameter.
     *
     * @param serviceGroup - AxisServiceGroup instance
     * @param parameter    - parameter to persist
     * @throws Exception - on registry call errors
     */
    public void updateServiceGroupParameter(AxisServiceGroup serviceGroup, Parameter parameter)
            throws Exception {
        try {
            updateParameter(PersistenceUtils.getResourcePath(serviceGroup), parameter);
        } catch (Throwable e) {
            handleExceptionWithRollback("Unable to update the service group parameter "
                    + parameter.getName() + " of service group "
                    + serviceGroup.getServiceGroupName(), e);
        }
    }
}

