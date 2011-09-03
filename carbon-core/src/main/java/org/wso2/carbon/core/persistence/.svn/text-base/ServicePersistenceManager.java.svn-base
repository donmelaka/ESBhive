package org.wso2.carbon.core.persistence;

import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.axis2.util.JavaUtils;
import org.apache.axis2.util.PolicyUtil;
import org.apache.axis2.description.*;
import org.apache.commons.logging.LogFactory;
import org.apache.neethi.PolicyComponent;
import org.apache.neethi.Policy;
import org.apache.axiom.om.util.UUIDGenerator;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.Collection;
import org.wso2.carbon.registry.core.Association;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.core.RegistryResources;
import org.wso2.carbon.core.RegistryRepoHandler;
import org.wso2.carbon.core.transports.TransportPersistenceManager;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.CarbonException;
import org.wso2.carbon.application.deployer.AppDeployerConstants;
import org.wso2.carbon.utils.CarbonUtils;

import javax.xml.namespace.QName;
import java.util.*;

public class ServicePersistenceManager extends AbstractPersistenceManager {

    private static final String ADDRESSING_MODULE = "addressing";

    // initialize log variable for this class
    static {
        log = LogFactory.getLog(ServicePersistenceManager.class);
    }

    /**
     * Constructor gets the axis config and calls the super constructor.
     *
     * @param axisConfig - AxisConfiguration
     * @throws AxisFault - if the config registry is not found
     */
    public ServicePersistenceManager(AxisConfiguration axisConfig) throws AxisFault {
        super(axisConfig);
    }

    /**
     * Returns the registry Resource for the specified AxisService
     *
     * @param axisService - AxisService instance
     * @return - service resource
     * @throws Exception - on registry transaction error
     */
    public Resource getService(AxisService axisService) throws Exception {
        try {
            String serviceResourcePath = PersistenceUtils.getResourcePath(axisService);
            if (configRegistry.resourceExists(serviceResourcePath)) {
                Resource resource = configRegistry.get(serviceResourcePath);
                if (resource.getProperty(RegistryResources.SUCCESSFULLY_ADDED) != null) {
                    return resource;
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("Successfully retrieved resource for " +
                        axisService.getName() + " Service");
            }
        } catch (Throwable e) {
            handleException("Could not get the Service resource from Config Registry", e);
        }
        return null;
    }

    /**
     * Deletes the registry resource of the specified service
     *
     * @param axisService - AxisService instance
     * @throws Exception - on registry transaction error
     */
    public void deleteService(AxisService axisService) throws Exception {
        Parameter param = axisService.getParameter(CarbonConstants.PRESERVE_SERVICE_HISTORY_PARAM);
        try {
            String serviceResourcePath = PersistenceUtils.getResourcePath(axisService);
            configRegistry.beginTransaction();
            if (configRegistry.resourceExists(serviceResourcePath)
                    && (param == null || !JavaUtils.isTrue(param.getValue().toString()))) {
                configRegistry.delete(serviceResourcePath);
            }
            configRegistry.commitTransaction();
        } catch (RegistryException e) {
            handleExceptionWithRollback("Could not delete Service " +
                    "resource from Config Registry", e);
        }

        //If the repository is configRegistry based, remove the artifact from configRegistry
        if (axisService.getFileName() != null) {
            String filePath = axisService.getFileName().getPath();
            if (filePath != null && CarbonUtils.getRegistryRepoPath() != null) {
                RegistryRepoHandler.deleteArtifactFromRegistry(filePath);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Successfully deleted " + axisService.getName() + " Service");
        }
    }

    /**
     * When a new service is deployed, persist all it's contents (operations, policies etc.)
     * into the registry
     *
     * @param axisService - AxisService instance
     * @throws Exception - on error
     */
    public synchronized void handleNewServiceAddition(AxisService axisService) throws Exception {
        if (axisService.isClientSide()) {
            return;
        }

        try {
            configRegistry.beginTransaction();

            // Add Service
            Collection service = configRegistry.newCollection();
            service.setProperty(RegistryResources.ServiceProperties.DOCUMENTATION, axisService
                    .getDocumentation());
            service.setProperty(RegistryResources.ServiceProperties.EXPOSED_ON_ALL_TANSPORTS,
                    String.valueOf(axisService.isEnableAllTransports()));

            String serviceResourcePath = PersistenceUtils.getResourcePath(axisService);
            configRegistry.put(serviceResourcePath, service);

            // Add Service Operations
            for (Iterator iter = axisService.getOperations(); iter.hasNext();) {
                AxisOperation axisOperation = (AxisOperation) iter.next();
                writeAxisDescription(axisOperation, axisOperation.getName().getLocalPart(),
                        PersistenceUtils.getResourcePath(axisOperation));
                writeParameters(axisOperation.getParameters(),
                        PersistenceUtils.getResourcePath(axisOperation));
            }

            // Add Service Bindings
            Map endPointMap = axisService.getEndpoints();
            for (Object o : endPointMap.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                AxisBinding axisBinding = ((AxisEndpoint) entry.getValue()).getBinding();
                handleNewBindingAddition(serviceResourcePath, axisBinding);
            }

            // Add the Service Policies
            List<Resource> servicePolicies = getServicePolicies(axisService);
            for (Resource servicePolicy : servicePolicies) {
                configRegistry.put(serviceResourcePath + RegistryResources.POLICIES +
                        servicePolicy.getProperty(RegistryResources.ModuleProperties.POLICY_UUID),
                        servicePolicy);
            }

            // If the service scope='soapsession', engage addressing if not already engaged.
            if (axisService.getScope().equals(Constants.SCOPE_SOAP_SESSION)) {
                if (!axisService.isEngaged(ADDRESSING_MODULE)) {
                    axisService.engageModule(axisService.getAxisConfiguration().getModule(
                            ADDRESSING_MODULE));
                }
            }

            // Add the Modules Engaged to this service
            for (Object module : axisService.getEngagedModules()) {
                AxisModule axisModule = (AxisModule) module;
                String moduleResourcePath = PersistenceUtils.getResourcePath(axisModule);
                if (!isGloballyEngaged(moduleResourcePath)
                        && !axisService.getParent().isEngaged(axisModule.getName())) {
                    configRegistry.addAssociation(serviceResourcePath, moduleResourcePath,
                            RegistryResources.Associations.ENGAGED_MODULES);
                }
            }

            // Save the operation-module engagements
            for (Iterator iter = axisService.getOperations(); iter.hasNext();) {
                AxisOperation axisOperation = (AxisOperation) iter.next();
                for (Object o : axisOperation.getEngagedModules()) {
                    AxisModule axisModule = (AxisModule) o;
                    String moduleResourcePath = PersistenceUtils.getResourcePath(axisModule);
                    if (!isGloballyEngaged(moduleResourcePath)
                            && !axisService.getParent().isEngaged(axisModule.getName())
                            && !axisService.isEngaged(axisModule.getName())) {
                        configRegistry.addAssociation(PersistenceUtils
                                .getResourcePath(axisOperation), moduleResourcePath,
                                RegistryResources.Associations.ENGAGED_MODULES);
                    }
                }
            }

            // add the service parameters
            writeParameters(axisService.getParameters(), serviceResourcePath);

            // add transport associations
            if (!axisService.isEnableAllTransports()) {
                List<String> exposedTransports = axisService.getExposedTransports();
                for (String exposedTransport : exposedTransports) {
                    Resource transportResource = TransportPersistenceManager.
                            getTransportResource(exposedTransport);
                    if (transportResource == null) {
                        throw new CarbonException("The configuration resource " +
                                "for " + exposedTransport + " transport does not exist");
                    }
                    configRegistry.addAssociation(serviceResourcePath, transportResource.getPath(),
                            RegistryResources.Associations.EXPOSED_TRANSPORTS);
                }
            }
            service = (Collection) configRegistry.get(serviceResourcePath);
            service.addProperty(RegistryResources.SUCCESSFULLY_ADDED, "true");
            configRegistry.put(serviceResourcePath, service);

            configRegistry.commitTransaction();

            if (log.isDebugEnabled()) {
                log.debug("Added new service - " + axisService.getName());
            }
        } catch (Throwable e) {
            handleExceptionWithRollback("Unable to handle new service addition. Service: " +
                    axisService.getName(), e);
        }
    }

    private void handleNewBindingAddition(String serviceResourcePath,
                                          AxisBinding axisBinding) throws RegistryException {
        writeAxisDescription(axisBinding, axisBinding.getName().getLocalPart(),
                serviceResourcePath + RegistryResources.ServiceProperties.BINDINGS
                        + axisBinding.getName().getLocalPart());

        // Add binding operations
        Iterator operations = axisBinding.getChildren();
        while (operations.hasNext()) {
            AxisBindingOperation bo = (AxisBindingOperation) operations.next();
            writeAxisDescription(bo, bo.getName().getLocalPart(),
                    PersistenceUtils.getBindingOperationPath(serviceResourcePath, bo));
        }
    }

    /**
     * Handle initialization of an already existing service in regsitry. Loads all parameters,
     * policies etc. into the service instance.
     *
     * @param service     - Service resource
     * @param axisService - AxisService instance
     * @throws Exception - on error
     */
    public synchronized void handleExistingServiceInit(Resource service, AxisService axisService)
            throws Exception {

        boolean isProxyService = PersistenceUtils.isProxyService(axisService);
        boolean wsdlChangeDetected = false;

        try {
            configRegistry.beginTransaction();
            String serviceResourcePath = service.getPath();

            // Fetch and attach Service policies
            loadPolicies(axisService, getPropertyValues(serviceResourcePath,
                    RegistryResources.ServiceProperties.POLICY_UUID), serviceResourcePath);

            for (Iterator iter = axisService.getOperations(); iter.hasNext();) {
                AxisOperation axisOperation = (AxisOperation) iter.next();
                String operationPath = PersistenceUtils.getResourcePath(axisOperation);

                // check whether the operation exists in the registry
                if (configRegistry.resourceExists(operationPath)) {
                    // Fetch and attach Operation and Message Policies
                    if (!axisOperation.isControlOperation()) {
                        // First load operation policies
                        loadPolicies(axisOperation, getPropertyValues(operationPath,
                                RegistryResources.ServiceProperties.POLICY_UUID),
                                serviceResourcePath);

                        // Fetch and attach MessageIn policies for this operation
                        if (!(axisOperation instanceof OutOnlyAxisOperation)) {
                            loadPolicies(axisOperation.getMessage(
                                    WSDLConstants.MESSAGE_LABEL_IN_VALUE), getPropertyValues(
                                    operationPath, RegistryResources.ServiceProperties
                                            .MESSAGE_IN_POLICY_UUID), serviceResourcePath);
                        }

                        // Fetch and attach MessageOut policies for this operation
                        if (!(axisOperation instanceof InOnlyAxisOperation)) {
                            loadPolicies(axisOperation.getMessage(
                                    WSDLConstants.MESSAGE_LABEL_OUT_VALUE), getPropertyValues(
                                    operationPath, RegistryResources.ServiceProperties
                                            .MESSAGE_OUT_POLICY_UUID), serviceResourcePath);
                        }

                        // Disengage all the statically engaged modules (i.e. those module engaged
                        // from the services.xml file)
                        axisOperation.getEngagedModules().clear();

                        Association[] associations = configRegistry.getAssociations(operationPath,
                                RegistryResources.Associations.ENGAGED_MODULES);
                        for (Association association : associations) {
                            Resource mod = configRegistry.get(association.getDestinationPath());
                            AxisModule axisModule = PersistenceUtils.getAxisModule(mod, axisConfig);
                            if (!isGloballyEngaged(association.getDestinationPath())) {
                                if (!axisService.isEngaged(axisModule)) {
                                    axisOperation.engageModule(axisModule);
                                }
                            }
                            mod.discard();
                        }
                        // Handle operation parameters
                        loadParameters(axisOperation, operationPath);

                        // Handle operation documentation
                        loadDocumentation(axisOperation, operationPath);
                    }
                } else {
                    wsdlChangeDetected = true;
                    writeAxisDescription(axisOperation, axisOperation.getName().getLocalPart(),
                            PersistenceUtils.getResourcePath(axisOperation));
                    writeParameters(axisOperation.getParameters(),
                            PersistenceUtils.getResourcePath(axisOperation));

                    for (Object o : axisOperation.getEngagedModules()) {
                        AxisModule axisModule = (AxisModule) o;
                        String moduleResourcePath = PersistenceUtils.getResourcePath(axisModule);
                        if (!isGloballyEngaged(moduleResourcePath)
                                && !axisService.getParent().isEngaged(axisModule.getName())
                                && !axisService.isEngaged(axisModule.getName())) {
                            configRegistry.addAssociation(PersistenceUtils
                                    .getResourcePath(axisOperation), moduleResourcePath,
                                    RegistryResources.Associations.ENGAGED_MODULES);
                        }
                    }
                }
            }

            // sync up the operations, required by the proxy services : Ruwan
            String operationsPath = service.getPath() + RegistryResources.OPERATIONS;
            if(configRegistry.resourceExists(operationsPath)){
                Collection operationsCollection = (Collection) configRegistry.get(operationsPath);
                for (String opPath : operationsCollection.getChildren()) {
                    String opName = opPath.substring(operationsPath.length()).replace("/", "");
                    if (axisService.getOperation(new QName(opName)) == null) {
                        wsdlChangeDetected = true;
                        // new service do not have the operation
                        configRegistry.delete(opPath);
                    }
                }            	
            }

            // Fetch and attach Binding, Binding operation and their Message policies
            Map endPointMap = axisService.getEndpoints();
            for (Object o : endPointMap.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                AxisEndpoint point = (AxisEndpoint) entry.getValue();
                AxisBinding currentAxisBinding = point.getBinding();

                // Fetch binding policies
                String bindingPath = serviceResourcePath + RegistryResources.ServiceProperties
                        .BINDINGS + currentAxisBinding.getName().getLocalPart();
                if (configRegistry.resourceExists(bindingPath)) {
                    loadPolicies(currentAxisBinding, getPropertyValues(bindingPath,
                            RegistryResources.ServiceProperties.POLICY_UUID), serviceResourcePath);
                    Iterator operationsItr = currentAxisBinding.getChildren();
                    while (operationsItr.hasNext()) {
                        AxisBindingOperation bindingOp = (AxisBindingOperation) operationsItr.next();

                        // Fetch and attach binding operation policies
                        String bindingOpPath = PersistenceUtils
                                .getBindingOperationPath(serviceResourcePath, bindingOp);
                        if (configRegistry.resourceExists(bindingOpPath)) {

                            loadPolicies(bindingOp, getPropertyValues(bindingOpPath,
                                    RegistryResources.ServiceProperties.POLICY_UUID),
                                    serviceResourcePath);
                            // Fetch and attach MessageIn policies for this operation
                            loadPolicies(bindingOp.getChild(
                                    WSDLConstants.MESSAGE_LABEL_IN_VALUE), getPropertyValues(
                                    bindingOpPath, RegistryResources.ServiceProperties
                                            .MESSAGE_IN_POLICY_UUID), serviceResourcePath);
                            // Fetch and attach MessageOut policies for this operation
                            loadPolicies(bindingOp.getChild(
                                    WSDLConstants.MESSAGE_LABEL_OUT_VALUE), getPropertyValues(
                                    bindingOpPath, RegistryResources.ServiceProperties
                                            .MESSAGE_OUT_POLICY_UUID), serviceResourcePath);
                        } else {
                            writeAxisDescription(bindingOp, bindingOp.getName().getLocalPart(),
                                    PersistenceUtils.getBindingOperationPath(
                                            serviceResourcePath, bindingOp));
                        }
                    }
                } else {
                    handleNewBindingAddition(serviceResourcePath, currentAxisBinding);
                }

            }
            // Disengage all the statically engaged modules (i.e. those module
            // engaged from the services.xml file)
            axisService.getEngagedModules().clear();

            // Engage modules to service
            Association[] engModules = configRegistry.getAssociations(serviceResourcePath,
                    RegistryResources.Associations.ENGAGED_MODULES);
            for (Association association : engModules) {
                Resource mod = configRegistry.get(association.getDestinationPath());
                AxisModule axisModule = PersistenceUtils.getAxisModule(mod, axisConfig);
                if (!isGloballyEngaged(association.getDestinationPath())) {
                    axisService.disengageModule(axisModule);
                    axisService.engageModule(axisModule);
                }
                mod.discard();
            }

            // add description
            if (wsdlChangeDetected) {
                service.setProperty(RegistryResources.ServiceProperties.DOCUMENTATION,
                        axisService.getDocumentation());
            } else {
                loadDocumentation(axisService, serviceResourcePath);
            }

            // If the current service is proxy service, write existing params into registry, because the proxy
            // editing also supports parameter editing, to which we should give the precedence
            if (isProxyService) {
                ArrayList<Parameter> availableParameters = axisService.getParameters();
                // Adding the parameters to the configRegistry
                ListIterator<Parameter> ite2 = availableParameters.listIterator();
                while (ite2.hasNext()) {
                    Parameter serviceParameter = ite2.next();
                    if (serviceParameter.getParameterType() != Parameter.ANY_PARAMETER) {
                        updateServiceParameter(axisService, serviceParameter);
                    }
                }
            }
            loadParameters(axisService, serviceResourcePath);
            
            // Handle existing transports
            if (isProxyService) {
                List<String> availableTransports = axisService.getExposedTransports();
                ListIterator<String> transportItr = availableTransports.listIterator();

                // Removing transports from the configRegistry
                Association[] associations = configRegistry.getAssociations(serviceResourcePath,
                        RegistryResources.Associations.EXPOSED_TRANSPORTS);
                for (Association a : associations) {
                    configRegistry.removeAssociation(a.getSourcePath(), a.getDestinationPath(),
                            a.getAssociationType());
                }
                // Adding the transports to the configRegistry
                while (transportItr.hasNext()) {
                    String transport = transportItr.next();
                    Resource transportResource = TransportPersistenceManager
                            .getTransportResource(transport);
                    if (transportResource == null) {
                        throw new CarbonException("The configuration resource for " + transport +
                            " transport does not exist");
                    }
                    service.setProperty(RegistryResources.ServiceProperties
                            .EXPOSED_ON_ALL_TANSPORTS, String.valueOf(false));
                    configRegistry.addAssociation(serviceResourcePath, transportResource.getPath(),
                            RegistryResources.Associations.EXPOSED_TRANSPORTS);
                    configRegistry.put(transportResource.getPath(), transportResource);
                    if (log.isDebugEnabled()) {
                        log.debug("Added " + transport + " transport binding for " +
                                axisService.getName() + " service");
                    }
                }
            } else {
                if (!Boolean.valueOf(service.getProperty(RegistryResources
                        .ServiceProperties.EXPOSED_ON_ALL_TANSPORTS))) {
                    axisService.setExposedTransports(new ArrayList());
                    Association[] associations = configRegistry.getAssociations(service.getPath(),
                            RegistryResources.Associations.EXPOSED_TRANSPORTS);
                    for (Association association : associations) {
                        Resource resource = configRegistry.get(association.getDestinationPath());
                        String transportProtocol = resource
                                .getProperty(RegistryResources.Transports.PROTOCOL_NAME);
                        axisService.addExposedTransport(transportProtocol);
                        resource.discard();
                    }
                }
            }
            // Activate/Deactivate service
            String serviceState = service.getProperty(RegistryResources.ServiceProperties.ACTIVE);
            if (serviceState == null || serviceState.trim().length() == 0) {
                serviceState = "true";
            }
            axisService.setActive(Boolean.parseBoolean(serviceState));

            configRegistry.put(serviceResourcePath, service);
            configRegistry.commitTransaction();

            if (log.isDebugEnabled()) {
                log.debug("Initialized service - " + axisService.getName());
            }
        } catch (Throwable e) {
            handleExceptionWithRollback("Unable to handle service initialization. Service: " +
                    axisService.getName(), e);
        }
    }

    /**
     * Handle the engagement of the module to service at the registry level
     *
     * @param module  - AxisModule instance
     * @param service - AxisService instance
     * @throws Exception - on error
     */
    public void engageModuleForService(AxisModule module, AxisService service) throws Exception {
        try {
            handleModuleForAxisDescription(module,
                    PersistenceUtils.getResourcePath(service), true);
            if (log.isDebugEnabled()) {
                log.debug("Successfully engaged " + module.getName() + " module for " +
                        service.getName() + "service");
            }
        } catch (Throwable e) {
            handleExceptionWithRollback("Unable to engage " + module.getName() +
                    " module to " + service.getName() + " service", e);
        }
    }

    /**
     * Handle the dis-engagement of the module to service at the registry level
     *
     * @param module  - AxisModule instance
     * @param service - AxisService instance
     * @throws Exception - on error
     */
    public void disengageModuleForService(AxisModule module, AxisService service) throws Exception {
        try {
            handleModuleForAxisDescription(module,
                    PersistenceUtils.getResourcePath(service), false);
            if (log.isDebugEnabled()) {
                log.debug("Successfully disengaged " + module.getName() + " module from " +
                        service.getName() + "service");
            }
        } catch (Throwable e) {
            handleExceptionWithRollback("Unable to disengage " + module.getName() +
                    " module from " + service.getName() + " service", e);
        }
    }

    /**
     * Remove the specified parameter from the given service
     *
     * @param service   - AxisService instance
     * @param parameter - parameter to remove
     * @throws Exception - on error
     */
    public void removeServiceParameter(AxisService service, Parameter parameter) throws Exception {
        removeParameter(PersistenceUtils.getResourcePath(service), parameter.getName());
    }

    /**
     * Set the given property to the service resource in the registry
     *
     * @param service       - AxisService instance
     * @param propertyName  - name of the property to set
     * @param propertyValue - value to set
     * @throws Exception - on error
     */
    public void setServiceProperty(AxisService service, String propertyName,
                                   String propertyValue) throws Exception {
        try {
            String serviceResourcePath = PersistenceUtils.getResourcePath(service);
            configRegistry.beginTransaction();
            if (configRegistry.resourceExists(serviceResourcePath)) {
                Resource serviceResource = configRegistry.get(serviceResourcePath);
                serviceResource.setProperty(propertyName, propertyValue);
                configRegistry.put(serviceResourcePath, serviceResource);
                serviceResource.discard();
            }
            configRegistry.commitTransaction();
            if (log.isDebugEnabled()) {
                log.debug("Successfully set " + propertyName + " property for " +
                        service.getName() + "service");
            }
        } catch (Throwable e) {
            handleExceptionWithRollback("Unable to set property " + propertyName +
                    " to service " + service.getName(), e);
        }
    }

    /**
     * Persist the given service parameter. If the parameter already exists in registry, update
     * it. Otherwise, create a new parameter.
     *
     * @param service   - AxisService instance
     * @param parameter - parameter to persist
     * @throws Exception - on registry call errors
     */
    public void updateServiceParameter(AxisService service, Parameter parameter) throws Exception {
        try {
            updateParameter(PersistenceUtils.getResourcePath(service), parameter);
        } catch (Throwable e) {
            handleExceptionWithRollback("Unable to update the service parameter " +
                    parameter.getName() + " of service " + service.getName(), e);
        }
    }

    /**
     * Check whether the specified parameter already exists. If yes, return the value of it. If
     * not, write a new parameter with the specified value
     *
     * @param service    - AxisService instance
     * @param paramName  - name of the parameter to check
     * @param paramValue - value to be set
     * @return - if param found, stored value. Otherwise the original value
     * @throws Exception - on error
     */
    public String getExistingValueOrUpdateParameter(AxisService service, String
            paramName, String paramValue) throws Exception {
        String serviceResourcePath = PersistenceUtils.getResourcePath(service);
        String serviceParamResourcePath = serviceResourcePath + RegistryResources.PARAMETERS
                + paramName;

        String returnValue = paramValue;
        try {
            configRegistry.beginTransaction();
            Resource serviceParamResource;
            if (!configRegistry.resourceExists(serviceParamResourcePath)) {
                serviceParamResource = configRegistry.newResource();
                serviceParamResource
                        .addProperty(RegistryResources.ParameterProperties.NAME, paramName);
                serviceParamResource
                        .addProperty(RegistryResources.ParameterProperties.VALUE, paramValue);
                serviceParamResource.setContent("<parameter name=\"" + paramName
                        + "\">" + paramValue + "</parameter>");
                configRegistry.put(serviceParamResourcePath, serviceParamResource);
                serviceParamResource.discard();
            } else {
                serviceParamResource = configRegistry.get(serviceParamResourcePath);
                returnValue = serviceParamResource
                        .getProperty(RegistryResources.ParameterProperties.VALUE);
            }
            configRegistry.commitTransaction();
        } catch (Throwable e) {
            handleExceptionWithRollback("Unable to update the service parameter " +
                    paramName + " to the service " + service.getName(), e);
        }
        return returnValue;
    }

    /**
     * Removes an exposed transport from a given service.
     *
     * @param serviceName       - Name of the service where new transport to be removed.
     * @param transportProtocol - Name of the transport to be removed.
     * @throws Exception - on error
     */
    public void removeExposedTransports(String serviceName,
                                        String transportProtocol) throws Exception {
        try {
            AxisService axisService = axisConfig.getServiceForActivation(serviceName);
            if (axisService == null) {
                handleException("No service found for the provided service name : " + serviceName);
                return;
            }
            Resource serviceResource = getService(axisService);
            Resource transportResource = TransportPersistenceManager
                    .getTransportResource(transportProtocol);

            configRegistry.beginTransaction();

            if (transportResource != null) {
                configRegistry.removeAssociation(serviceResource.getPath(),
                        transportResource.getPath(),
                        RegistryResources.Associations.EXPOSED_TRANSPORTS);
                transportResource.discard();
            }

            List<String> exposedTrps = axisService.getExposedTransports();
            for (String transport : exposedTrps) {
                transportResource = TransportPersistenceManager.getTransportResource(transport);
                if (transportResource == null) {
                    throw new CarbonException("The configuration resource for " + transport +
                            " transport does not exist");
                }
                configRegistry.addAssociation(serviceResource.getPath(), transportResource
                        .getPath(), RegistryResources.Associations.EXPOSED_TRANSPORTS);
                transportResource.discard();
            }

            serviceResource.setProperty(RegistryResources.ServiceProperties
                    .EXPOSED_ON_ALL_TANSPORTS, String.valueOf(false));
            configRegistry.put(serviceResource.getPath(), serviceResource);
            serviceResource.discard();
            configRegistry.commitTransaction();

            if (log.isDebugEnabled()) {
                log.debug("Successfully removed " + transportProtocol + " transport from " +
                        serviceName + "service");
            }
        } catch (Exception e) {
            handleExceptionWithRollback("Error while removing exposed transport : " +
                    transportProtocol, e);
        }
    }

    /**
     * Extract all the policies from the AxisService and create registry Resources for them.
     *
     * @param axisService Service to get policies
     * @return policies list of policies
     * @throws Exception on error
     */
    private List<Resource> getServicePolicies(AxisService axisService) throws Exception {
        // List of policy resources to be retuned
        List<Resource> policyResources = new ArrayList<Resource>();
        String serviceResourcePath = PersistenceUtils.getResourcePath(axisService);

        // Get Service Policy
        List<PolicyComponent> servicePolicyList = new ArrayList<PolicyComponent>(axisService
                .getPolicySubject().getAttachedPolicyComponents());
        Policy servicePolicy = PolicyUtil.getMergedPolicy(servicePolicyList, axisService);

        if (servicePolicy != null) {
            // Add this policy as a resource to the list
            addPolicyResource(policyResources, servicePolicy, PolicyInclude.AXIS_SERVICE_POLICY);
            // Refer this policy from the service
            setResourcePolicyId(serviceResourcePath, servicePolicy.getId());
        }

        // Get Service Operation Policies
        Iterator serviceOperations = axisService.getOperations();
        while (serviceOperations.hasNext()) {
            AxisOperation axisOperation = (AxisOperation) serviceOperations.next();
            Resource operationResource = configRegistry
                    .get(PersistenceUtils.getResourcePath(axisOperation));

            // Get the operation policy
            List<PolicyComponent> opPolicyList = new ArrayList<PolicyComponent>(axisOperation
                    .getPolicySubject().getAttachedPolicyComponents());
            Policy operationPolicy = PolicyUtil.getMergedPolicy(opPolicyList, axisOperation);

            if (operationPolicy != null) {
                // Add this policy as a resource to the list
                addPolicyResource(policyResources,
                        operationPolicy, PolicyInclude.AXIS_OPERATION_POLICY);
                // Refer this policy from the operation resource
                operationResource.setProperty(RegistryResources.ServiceProperties.POLICY_UUID,
                        operationPolicy.getId());
            }

            if (!(axisOperation instanceof OutOnlyAxisOperation)) {
                // Get Service Operation Message Policies
                AxisMessage axisInMessage = axisOperation
                        .getMessage(WSDLConstants.MESSAGE_LABEL_IN_VALUE);

                // Get the message in policy
                List<PolicyComponent> messageInPolicyList = new ArrayList<PolicyComponent>(
                        axisInMessage.getPolicySubject().getAttachedPolicyComponents());
                Policy messageInPolicy = PolicyUtil.getMergedPolicy(messageInPolicyList, axisInMessage);

                if (messageInPolicy != null) {
                    // Add this policy as a resource to the list
                    addPolicyResource(policyResources,
                            messageInPolicy, PolicyInclude.AXIS_MESSAGE_POLICY);
                    // Refer this policy from the operation resource
                    operationResource.setProperty(RegistryResources.ServiceProperties
                            .MESSAGE_IN_POLICY_UUID, messageInPolicy.getId());
                }
            }

            // Get the message out policy
            if (!(axisOperation instanceof InOnlyAxisOperation)) {
                AxisMessage axisOutMessage = axisOperation
                        .getMessage(WSDLConstants.MESSAGE_LABEL_OUT_VALUE);
                List<PolicyComponent> messageOutPolicyList = new ArrayList<PolicyComponent>(
                        axisOutMessage.getPolicySubject().getAttachedPolicyComponents());
                Policy messageOutPolicy = PolicyUtil
                        .getMergedPolicy(messageOutPolicyList, axisOutMessage);

                if (messageOutPolicy != null) {
                    // Add this policy as a resource to the list
                    addPolicyResource(policyResources,
                            messageOutPolicy, PolicyInclude.AXIS_MESSAGE_POLICY);
                    // Refer this policy from the operation resource
                    operationResource.setProperty(RegistryResources.ServiceProperties
                            .MESSAGE_OUT_POLICY_UUID, messageOutPolicy.getId());
                }
            }

            // Update the operation resource in configRegistry
            configRegistry.put(serviceResourcePath + RegistryResources.ServiceProperties.OPERATIONS
                    + axisOperation.getName().getLocalPart(), operationResource);
            operationResource.discard();
        }

        // Get binding policies
        Map endPointMap = axisService.getEndpoints();

        /**
         * We don't have a way of accessing all bindings directly from axis service. Therefore,
         * we have to access those trough endpoints. So the same binding can be found again and
         * again. To remove that overhead, we memorize the treated bindings.
         */
        ArrayList<String> bindingsList = new ArrayList<String>();
        for (Object o : endPointMap.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            AxisBinding currentAxisBinding = ((AxisEndpoint) entry.getValue()).getBinding();

            if (bindingsList.contains(currentAxisBinding.getName().getLocalPart())) {
                continue;
            }
            // If we process this binding, add it's name to our list
            bindingsList.add(currentAxisBinding.getName().getLocalPart());

            // Get current binding Policy
            List<PolicyComponent> bindingPolicyList = new ArrayList<PolicyComponent>(
                    currentAxisBinding.getPolicySubject().getAttachedPolicyComponents());
            Policy bindingPolicy = PolicyUtil
                    .getMergedPolicy(bindingPolicyList, currentAxisBinding);

            if (bindingPolicy != null) {
                // Add this policy as a resource to the list
                addPolicyResource(policyResources, bindingPolicy, PolicyInclude.BINDING_POLICY);
                // Refer this policy from the binding resource
                setResourcePolicyId(serviceResourcePath + RegistryResources
                        .ServiceProperties.BINDINGS + currentAxisBinding.getName().getLocalPart(),
                        bindingPolicy.getId());
            }

            // Get Binding Operation Policies
            Iterator operations = currentAxisBinding.getChildren();
            while (operations.hasNext()) {
                AxisBindingOperation currentOperation = (AxisBindingOperation) operations.next();
                Resource bindingOperationResource = configRegistry.get(PersistenceUtils
                        .getBindingOperationPath(serviceResourcePath, currentOperation));

                // Get current binding operation policy
                List<PolicyComponent> boPolicyList = new ArrayList<PolicyComponent>(
                        currentOperation.getPolicySubject().getAttachedPolicyComponents());
                Policy boPolicy = PolicyUtil.getMergedPolicy(boPolicyList, currentOperation);

                if (boPolicy != null) {
                    // Add this policy as a resource to the list
                    addPolicyResource(policyResources,
                            boPolicy, PolicyInclude.BINDING_OPERATION_POLICY);
                    // Refer this policy from the binding operation
                    bindingOperationResource.setProperty(RegistryResources
                            .ServiceProperties.POLICY_UUID, boPolicy.getId());
                }

                // Get Binding Operation Message Policies
                AxisDescription boMessageIn = currentOperation
                        .getChild(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                if (boMessageIn != null) {
                    List<PolicyComponent> boMessageInPolicyList = new ArrayList<PolicyComponent>(
                            boMessageIn.getPolicySubject().getAttachedPolicyComponents());
                    Policy boMessageInPolicy = PolicyUtil
                            .getMergedPolicy(boMessageInPolicyList, boMessageIn);

                    if (boMessageInPolicy != null) {
                        // Add this policy as a resource to the list
                        addPolicyResource(policyResources,
                                boMessageInPolicy, PolicyInclude.BINDING_INPUT_POLICY);
                        // Refer this policy from the binding operation
                        bindingOperationResource.setProperty(RegistryResources.ServiceProperties
                                .MESSAGE_IN_POLICY_UUID, boMessageInPolicy.getId());
                    }
                }

                // Get binding operaion out policy
                AxisDescription boMessageOut = currentOperation
                        .getChild(WSDLConstants.MESSAGE_LABEL_OUT_VALUE);
                if (boMessageOut != null) {
                    List<PolicyComponent> boMessageOutPolicyList = new ArrayList<PolicyComponent>(
                            boMessageOut.getPolicySubject().getAttachedPolicyComponents());
                    Policy boMessageOutPolicy = PolicyUtil
                            .getMergedPolicy(boMessageOutPolicyList, boMessageOut);

                    if (boMessageOutPolicy != null) {
                        // Add this policy as a resource to the list
                        addPolicyResource(policyResources,
                                boMessageOutPolicy, PolicyInclude.BINDING_OUTPUT_POLICY);
                        // Refer this policy from the binding operation
                        bindingOperationResource.setProperty(RegistryResources.ServiceProperties
                                .MESSAGE_OUT_POLICY_UUID, boMessageOutPolicy.getId());
                    }
                }

                // Update binding operation resource in configRegistry
                configRegistry.put(PersistenceUtils.getBindingOperationPath(serviceResourcePath,
                        currentOperation), bindingOperationResource);
                bindingOperationResource.discard();
            }
        }
        return policyResources;
    }

    /**
     * Sets the policy Id for a resource of a service, operation, binding etc..
     *
     * @param resourcePath - registry path
     * @param policyId     - policy Id to be set
     * @throws RegistryException - error on updating resource
     */
    private void setResourcePolicyId(String resourcePath,
                                     String policyId) throws RegistryException {
        Resource resource = configRegistry.get(resourcePath);
        resource.setProperty(RegistryResources.ServiceProperties.POLICY_UUID, policyId);

        // Update the service resource in configRegistry
        configRegistry.put(resourcePath, resource);
        resource.discard();
    }

    /**
     * Add the given Policy as a resource to the given policy list if it doesn't already exist
     *
     * @param policyResources - list of policy resource
     * @param policy          - Policy instance
     * @param policyType      - policy type
     * @throws Exception - on creating policy resource
     */
    private void addPolicyResource(List<Resource> policyResources,
                                     Policy policy, int policyType) throws Exception {
        if (policy.getId() == null) {
            // Generate an ID
            policy.setId(UUIDGenerator.getUUID());
            policyResources.add(createPolicyResource(policy, policy.getId(), policyType));
        } else if (PersistenceUtils.getPolicyResourceFromList(policy.getId(),
                policyResources) == null) {
            policyResources.add(createPolicyResource(policy, policy.getId(), policyType));
        }
    }
}
