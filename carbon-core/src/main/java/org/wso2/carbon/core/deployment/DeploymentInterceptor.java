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

package org.wso2.carbon.core.deployment;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.description.*;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.AxisEvent;
import org.apache.axis2.engine.AxisObserver;
import org.apache.axis2.util.JavaUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.Axis2ModuleNotFound;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.application.deployer.AppDeployerUtils;
import org.wso2.carbon.core.RegistryResources;
import org.wso2.carbon.core.internal.CarbonCoreServiceComponent;
import org.wso2.carbon.core.persistence.PersistenceFactory;
import org.wso2.carbon.core.util.SystemFilter;
import org.wso2.carbon.core.util.Utils;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.jdbc.utils.Transaction;
import org.wso2.carbon.utils.CarbonUtils;

import java.util.*;

/**
 * This deployment interceptor will be called whenever before a module is initialized or service is
 * deployed.
 *
 * @see AxisObserver
 */
public class DeploymentInterceptor implements AxisObserver {
    private static final Log log = LogFactory.getLog(DeploymentInterceptor.class);

    private final Map<String, Parameter> paramMap = new HashMap<String, Parameter>();

    private final HashMap<String, HashMap<String, AxisDescription>> faultyServicesDueToModules = new HashMap<String,
            HashMap<String, AxisDescription>>();

    private PersistenceFactory pf;

    private Registry registry;

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public void init(AxisConfiguration axisConfig) {
        pf = new PersistenceFactory(axisConfig);

        try {
            if (registry == null) {
                registry = CarbonCoreServiceComponent
                        .getRegistryService().getConfigSystemRegistry();
            }
        } catch (Exception e) {
            log.error("Error while registry instance for the deployment interceptor", e);
        }
    }

    public void serviceGroupUpdate(AxisEvent axisEvent, AxisServiceGroup axisServiceGroup) {
        // We do not persist Admin service events
        if (SystemFilter.isFilteredOutService(axisServiceGroup)) {
            return;
        }

        int eventType = axisEvent.getEventType();
        if (eventType == AxisEvent.SERVICE_DEPLOY) {
            if (log.isDebugEnabled()) {
                log.debug("Deploying service group : " + axisServiceGroup.getServiceGroupName());
            }

            Resource serviceGroup = null;
            try {
                serviceGroup = pf.getServiceGroupPM()
                        .getServiceGroup(axisServiceGroup.getServiceGroupName());
            } catch (Exception e) {
                log.error("Couldn't read service group resource", e);
            }

            if (serviceGroup == null) {
                addServiceGroup(axisServiceGroup);
            } else {
                String hashFromFile = CarbonUtils.computeServiceHash(axisServiceGroup);

                // Check whether the artifact has been updated, if so we need to purge all
                // database entries and treat this as a new service group addition
                String hashFromRegistry = serviceGroup.getProperty(RegistryResources
                        .ServiceGroupProperties.HASH_VALUE);

                if (hashFromFile != null && hashFromRegistry != null &&
                        !hashFromRegistry.equals(hashFromFile)) {
                    log.warn("The service artifact of the " +
                             axisServiceGroup.getServiceGroupName() +
                             " service group has changed. Removing all registry entries and " +
                             "handling this as a new service addition.");
                    try {
                        registry.beginTransaction();
                        deleteServiceGroup(axisServiceGroup);
                        addServiceGroup(axisServiceGroup);
                        if (Transaction.isStarted()) {
                            // This is a safety measure. We ideally shouldn't be getting here
                            // since the Persistence Manager running underneath should commit
                            // the transaction.
                            registry.commitTransaction();
                        }
                    } catch (Exception e) {
                        String msg = "Unable to remove all registry entries and handle new" +
                                "service addition [" + axisServiceGroup.getServiceGroupName() + "]";
                        try {
                            registry.rollbackTransaction();
                            // We need to catch the exception that is generated by
                            // rollbackTransaction(), should there be any, as this method doesn't
                            // throw exceptions.
                            log.error(msg, e);
                        } catch (Exception ex) {
                            msg += ". Unable to rollback transaction.";
                            log.error(msg, ex);
                        }
                    }
                } else {
                    try {
                        pf.getServiceGroupPM()
                                .handleExistingServiceGroupInit(serviceGroup, axisServiceGroup);

                    } catch(Axis2ModuleNotFound e) {
                        addFaultyServiceDuetoModule(e.getModuleName(), axisServiceGroup);
                        stopServiceGroup(axisServiceGroup, axisServiceGroup.getAxisConfiguration());
                        log.warn("ServiceGroup: " + axisServiceGroup.getServiceGroupName() +
                                "is stopped due to the missing module : " + e.getModuleName());
                    } catch (Exception e) {
                        String msg = "Could not handle initialization of existing service group [" +
                                     axisServiceGroup.getServiceGroupName() + "]";
                        log.error(msg, e);
                    }
                }
                serviceGroup.discard();
            }

            linkServiceGroupWithCapp(axisServiceGroup);
        } else if (eventType == AxisEvent.SERVICE_REMOVE) {
            Parameter svcHistoryParam = axisServiceGroup.getParameter(
                    CarbonConstants.KEEP_SERVICE_HISTORY_PARAM);
            if (svcHistoryParam == null || svcHistoryParam.getValue() == null ||
                    JavaUtils.isFalse(svcHistoryParam.getValue())) {
                if (log.isDebugEnabled()) {
                    log.debug("Removing service group : " + axisServiceGroup.getServiceGroupName());
                }

                deleteServiceGroup(axisServiceGroup);
            }
        }
    }

    private void addServiceGroup(AxisServiceGroup axisServiceGroup) {
        try {
            pf.getServiceGroupPM().handleNewServiceGroupAddition(axisServiceGroup);
        } catch (Exception e) {
            String msg = "Could not handle initialization of new service group [" +
                         axisServiceGroup.getServiceGroupName() + "]";
            log.error(msg, e);
        }
    }

    private void deleteServiceGroup(AxisServiceGroup axisServiceGroup) {
        try {
            pf.getServiceGroupPM().deleteServiceGroup(axisServiceGroup);
        } catch (Exception e) {
            log.error("Could not delete service group " + axisServiceGroup.getServiceGroupName(),
                      e);
        }
    }


    public void serviceUpdate(AxisEvent axisEvent, AxisService axisService) {
        // We do not persist Admin service events
        if (SystemFilter.isFilteredOutService((AxisServiceGroup) axisService.getParent())) {
            return;
        }

        if (axisService.isClientSide()) {
            return;
        }
        int eventType = axisEvent.getEventType();
        String serviceName = axisService.getName();
        try {
            Resource service = pf.getServicePM().getService(axisService);

            // if (eventType == AxisEvent.SERVICE_STOP) do nothing

            if (eventType == AxisEvent.SERVICE_DEPLOY) {
                if (!JavaUtils.isTrue(axisService.getParameterValue("hiddenService"))) {
                    log.info("Deploying Axis2 service : " + serviceName);
                } else if (log.isDebugEnabled()) {
                    log.debug("Deploying hidden Axis2 service : " + serviceName);    
                }

                if (service == null) {
                    pf.getServicePM().handleNewServiceAddition(axisService);
                } else {
                    pf.getServicePM().handleExistingServiceInit(service, axisService);
                }
            } else if (eventType == AxisEvent.SERVICE_START) {
                service.setProperty(RegistryResources.ServiceProperties.ACTIVE, "true");
            } else if (eventType == AxisEvent.SERVICE_STOP) {
                service.setProperty(RegistryResources.ServiceProperties.ACTIVE, "false");
            } else if (eventType == AxisEvent.SERVICE_REMOVE) {
                if (service != null) {
                    try {
                        Parameter svcHistoryParam = axisService.getParameter(
                                CarbonConstants.KEEP_SERVICE_HISTORY_PARAM);
                        if (svcHistoryParam == null || svcHistoryParam.getValue() == null ||
                                JavaUtils.isFalse(svcHistoryParam.getValue())) {
                            pf.getServicePM().deleteService(axisService);
                        }
                    } catch (Exception e) {
                        String msg = "Cannot delete service [" + serviceName + "]";
                        log.error(msg, e);
                    }
                }
            }

            if (service != null) {
                service.discard();
            }

        } catch(Axis2ModuleNotFound e) {
            addFaultyServiceDuetoModule(e.getModuleName(), axisService);
            stopService(axisService, axisService.getAxisConfiguration());
            log.warn("Service " + axisService.getName() + 
                                " is stopped due to the missing module: " + e.getModuleName());
        } catch (Exception e) {
            String msg = "Exception occurred while handling service update event. Service ";
            log.error(msg, e);
        }
    }

    public void moduleUpdate(AxisEvent axisEvent, AxisModule axisModule) {
        //TODO AZEEZ: Check whether we can ignore AdminModules - SystemFilter.isFilteredOutModule
        // We ignore admin module events
        String moduleName = axisModule.getName();
        /*if (moduleName.equals(ServerConstants.ADMIN_MODULE) ||
            moduleName.equals(ServerConstants.TRACER_MODULE) ||
            moduleName.equals(ServerConstants.STATISTICS_MODULE)) {
            return;
        }*/

        // Handle.MODULE_DEPLOY event. This may be a new or existing module
        if (axisEvent.getEventType() == AxisEvent.MODULE_DEPLOY) {
            String moduleVersion;
            if (axisModule.getVersion() == null) {
                log.warn("A valid Version not found for the module : '" + moduleName + "'");
                moduleVersion = RegistryResources.ModuleProperties.UNDEFINED;
            } else {
                moduleVersion = axisModule.getVersion().toString();
            }
            if (!SystemFilter.isFilteredOutModule(axisModule)) {
                log.info("Deploying Axis2 module : " + axisModule.getArchiveName());
            }

            Resource module = null;
            try {
                module = pf.getModulePM().getModule(moduleName, moduleVersion);
            } catch (Exception e) {
                log.error("Couldn't read the module resource", e);
            }

            if (module != null) {
                try {
                    pf.getModulePM().handleExistingModuleInit(module, axisModule);
                } catch (Exception e) {
                    log.error("Could not handle initialization of existing module", e);
                }
                module.discard();
            } else { // this is a new module which has not been registered in the DB yet
                try {
                    pf.getModulePM().handleNewModuleAddition(axisModule, moduleName, moduleVersion);
                } catch (Exception e) {
                    log.error("Could not handle addition of new module", e);
                }
            }

            synchronized (faultyServicesDueToModules) {
                //Check whether there are faulty services due to this module
                HashMap<String, AxisDescription> faultyServices = getFaultyServicesDuetoModule(moduleName);
                //noinspection unchecked
                faultyServices = (HashMap<String, AxisDescription>) faultyServices.clone();

                // Here iterating a cloned hashmap and modifying the original hashmap.
                // To avoid the ConcurrentModificationException.
                for (AxisDescription axisDescription : faultyServices.values()) {
                    removeFaultyServiceDuetoModule(moduleName, (String) axisDescription.getKey());

                    Resource axisDescritionResource;
                    try {
                        //Recover the faulty serviceGroup or service.
                        if (axisDescription instanceof AxisServiceGroup) {
                            AxisServiceGroup axisServiceGroup = (AxisServiceGroup) axisDescription;
                            axisDescritionResource = pf.getServiceGroupPM().getServiceGroup(axisServiceGroup.getServiceGroupName());
                            pf.getServiceGroupPM().handleExistingServiceGroupInit(axisDescritionResource, axisServiceGroup);

                            //Start all the services in this serviceGroup and remove the special parameter
                            startServiceGroup(axisServiceGroup, axisServiceGroup.getAxisConfiguration());
                            log.info("Recovered and Deployed axis2 service group : " +
                                    axisServiceGroup.getServiceGroupName());

                        } else if (axisDescription instanceof AxisService) {
                            AxisService axisService = (AxisService) axisDescription;
                            axisDescritionResource = pf.getServicePM().getService(axisService);
                            pf.getServicePM().handleExistingServiceInit(axisDescritionResource, axisService);

                            //Start this axisService and remove the special paramter.
                            startService(axisService, axisService.getAxisConfiguration());
                            log.info("Recovered and Deployed axis2 service : " + axisService.getName());
                        }

                    } catch (Axis2ModuleNotFound e) {
                        addFaultyServiceDuetoModule(e.getModuleName(), axisDescription);
                    } catch (Exception e) {
                        String msg = "Could not handle initialization of existing service group [" +
                                axisDescription.getKey() + "]";
                        log.error(msg, e);
                    }
                }
            }
        }
    }

    public void addParameter(Parameter parameter) throws AxisFault {
        paramMap.put(parameter.getName(), parameter);
    }

    public void removeParameter(Parameter param) throws AxisFault {
        paramMap.remove(param.getName());
    }

    public void deserializeParameters(OMElement omElement) throws AxisFault {
        //No need to do anything here
    }

    public Parameter getParameter(String paramName) {
        return paramMap.get(paramName);
    }

    public ArrayList<Parameter> getParameters() {
        Collection<Parameter> collection = paramMap.values();
        ArrayList<Parameter> arr = new ArrayList<Parameter>();
        for (Parameter aCollection : collection) {
            arr.add(aCollection);
        }
        return arr;
    }

    public boolean isParameterLocked(String paramName) {
        return (paramMap.get(paramName)).isLocked();
    }

    /**
     * Updates the map that keeps track of faulty services due to modules
     * @param moduleName This service has become faulty due this module.
     * @param axisDescription  Data that are required when recovering the faulty service.
     */
    private void addFaultyServiceDuetoModule(String moduleName, AxisDescription axisDescription) {
        HashMap<String, AxisDescription> faultyServicesMap;
        synchronized (faultyServicesDueToModules) {
            if (faultyServicesDueToModules.containsKey(moduleName)) {
                faultyServicesMap = faultyServicesDueToModules.get(moduleName);
                faultyServicesMap.put((String) axisDescription.getKey(), axisDescription);
            } else {
                faultyServicesMap = new HashMap<String, AxisDescription>();
                faultyServicesMap.put((String) axisDescription.getKey(), axisDescription);
                faultyServicesDueToModules.put(moduleName, faultyServicesMap);
            }
        }
    }

    private HashMap<String, AxisDescription> getFaultyServicesDuetoModule(String moduleName) {
        if (faultyServicesDueToModules.containsKey(moduleName)) {
            return faultyServicesDueToModules.get(moduleName);
        }
        return new HashMap<String, AxisDescription>(1);
    }


    private void removeFaultyServiceDuetoModule(String moduleName, String serviceGroupName) {
        synchronized (faultyServicesDueToModules) {
            HashMap<String, AxisDescription> faultyServices = faultyServicesDueToModules.get(moduleName);
            if (faultyServices != null) {
                faultyServices.remove(serviceGroupName);
                if (faultyServices.isEmpty()) {
                    faultyServicesDueToModules.remove(moduleName);
                }
            }
        }
    }

    public void startServiceGroup(AxisServiceGroup servicGroup, AxisConfiguration axisConfiguration) {
        for (Iterator itr = servicGroup.getServices(); itr.hasNext();) {
            startService((AxisService) itr.next(), axisConfiguration);
        }
    }

    public void stopServiceGroup(AxisServiceGroup servicGroup, AxisConfiguration axisConfiguration) {
        for (Iterator itr = servicGroup.getServices(); itr.hasNext();) {
            stopService((AxisService) itr.next(), axisConfiguration);
        }
    }

    public void startService(AxisService axisService, AxisConfiguration axisConfiguration) {
        String serviceName = axisService.getName();

        if (log.isDebugEnabled()) {
            log.debug("Activating service : " + serviceName);
        }

        try {
            axisConfiguration.startService(serviceName);
            //Removing the special special property
            Parameter param = axisService.getParameter(CarbonConstants.CARBON_FAULTY_SERVICE);
            if(param != null){
                axisService.removeParameter(param);
            }
        } catch (AxisFault e) {
            String msg = "Cannot start service : " + serviceName;
            log.error(msg, e);
        }
    }

    public void stopService(AxisService axisService, AxisConfiguration axisConfiguration) {
        String serviceName = axisService.getName();
        
        if (log.isDebugEnabled()) {
            log.debug("Deactivating service : " + serviceName);
        }

        try {
            axisConfiguration.stopService(serviceName);
            axisService.addParameter(CarbonConstants.CARBON_FAULTY_SERVICE,
                    CarbonConstants.CARBON_FAULTY_SERVICE_DUE_TO_MODULE);
        } catch (AxisFault e) {
            String msg = "Cannot stop service : " + serviceName;
            log.error(msg, e);
        }
    }

    private void linkServiceGroupWithCapp(AxisServiceGroup sg) {
        AxisService axisService = null;
        for (Iterator<AxisService> itr = sg.getServices() ; itr.hasNext() ;) {
            AxisService temp = itr.next();
            if (temp.getFileName() != null) {
                axisService = temp;
            }
        }
        try {
            if (axisService != null) {
                String filePath = axisService.getFileName().getPath();
                String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
                String artifactType = Utils.getArtifactTypeFromService(axisService);
                AppDeployerUtils.attachArtifactToOwnerApp(CarbonCoreServiceComponent
                        .getAppManager(), fileName, artifactType, sg.getServiceGroupName());
            }
        } catch (Exception e) {
            log.error("Error while accessing ApplicationManager", e);
        }
    }
}