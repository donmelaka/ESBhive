package org.wso2.carbon.core.persistence;

import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.AxisFault;
import org.apache.axis2.util.Utils;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.description.PolicySubject;
import org.apache.axis2.description.PolicyInclude;
import org.apache.commons.logging.LogFactory;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.util.UUIDGenerator;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.neethi.PolicyComponent;
import org.apache.neethi.PolicyReference;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.Collection;
import org.wso2.carbon.core.RegistryResources;
import org.wso2.carbon.core.CarbonAxisConfigurator;
import org.wso2.carbon.core.util.ParameterUtil;

import java.util.List;
import java.util.ArrayList;

public class ModulePersistenceManager extends AbstractPersistenceManager {

    // initialize log variable for this class
    static {
        log = LogFactory.getLog(ModulePersistenceManager.class);
    }

    /**
     * Constructor gets the axis config and calls the super constructor.
     *
     * @param axisConfig - AxisConfiguration
     * @throws AxisFault - if the config registry is not found
     */
    public ModulePersistenceManager(AxisConfiguration axisConfig) throws AxisFault {
        super(axisConfig);
    }

    /**
     * Returns the registry Resource for the specified Axis2 Module
     *
     * @param moduleName    - Module name
     * @param moduleVersion - Module version
     * @return - module resource
     * @throws Exception - on registry transaction error
     */
    public Resource getModule(String moduleName, String moduleVersion) throws Exception {
        try {
            String modulePath = RegistryResources.MODULES + moduleName + "/" + moduleVersion;
            if (configRegistry.resourceExists(modulePath)) {
                Resource resource = configRegistry.get(modulePath);
                if (resource.getProperty(RegistryResources.SUCCESSFULLY_ADDED) != null) {
                    return resource;
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("Successfully retrieved " + moduleName +
                        " module resource from registry");
            }
        } catch (Throwable e) {
            handleException("Could not get the Module resource from Config Registry", e);
        }
        return null;
    }

    /**
     * Update the module resource with a 'globlally engaged' property set to true
     *
     * @param module - AxisModule instance
     * @throws Exception - on error
     */
    public void globallyEngageModule(AxisModule module) throws Exception {
        try {
            handleGlobalModule(module, true);
            if (log.isDebugEnabled()) {
                log.debug(module.getName() + " is globally engaged");
            }
        } catch (Throwable e) {
            handleExceptionWithRollback("Unable to engage " + module.getName() + " globally", e);
        }
    }

    /**
     * Update the module resource with a 'globlally engaged' property set to false
     *
     * @param module - AxisModule instance
     * @throws Exception - on error
     */
    public void globallyDisengageModule(AxisModule module) throws Exception {
        try {
            handleGlobalModule(module, false);
            if (log.isDebugEnabled()) {
                log.debug(module.getName() + " is globally disengaged");
            }
        } catch (Throwable e) {
            handleExceptionWithRollback("Unable to disengage " + module.getName() + " globally", e);
        }
    }

    /**
     * Handle initialization of a new module in regsitry. Writes all parameters and engaged
     * policies into the registry.
     *
     * @param axisModule    - AxisModule instance to be persisted
     * @param moduleName    - module name
     * @param moduleVersion - module version
     * @throws Exception - on error
     */
    public synchronized void handleNewModuleAddition(AxisModule axisModule, String moduleName,
                                                     String moduleVersion) throws Exception {
        try {
            configRegistry.beginTransaction();
            Resource module = configRegistry.newCollection();
            module.addProperty(RegistryResources.ModuleProperties.NAME, moduleName);
            if (!moduleVersion.equals(RegistryResources.ModuleProperties.UNDEFINED)) {
                module.addProperty(RegistryResources.ModuleProperties.VERSION, moduleVersion);
            }
            module.addProperty(RegistryResources.ModuleProperties.GLOBALLY_ENGAGED, String
                    .valueOf(CarbonAxisConfigurator.getInstance().isGlobalyEngaged(axisModule)));

            String registryResourcePath = RegistryResources.MODULES + moduleName
                    + "/" + moduleVersion;
            configRegistry.put(registryResourcePath, module);

            // add the module parameters
            writeParameters(axisModule.getParameters(), registryResourcePath);

            // Persist module policies
            List<Resource> modulePolicies = getModulePolicies(axisModule);
            for (Resource modulePolicy : modulePolicies) {
                configRegistry.put(RegistryResources.MODULES + moduleName + "/" + moduleVersion
                        + RegistryResources.POLICIES
                        + modulePolicy.getProperty(RegistryResources.ModuleProperties.POLICY_UUID),
                        modulePolicy);
            }
            module.addProperty(RegistryResources.SUCCESSFULLY_ADDED, "true");
            configRegistry.put(registryResourcePath, module);

            configRegistry.commitTransaction();
            if (log.isDebugEnabled()) {
                log.debug("Added new module - " + axisModule.getName() + "-" +
                        axisModule.getVersion().toString());
            }
        } catch (Throwable e) {
            handleExceptionWithRollback("Unable to handle new module addition. Module: " +
                    Utils.getModuleName(moduleName, moduleVersion), e);
            PersistenceUtils.markFaultyModule(axisModule);
        }
    }

    /**
     * Handle initialization of an already existing module in regsitry. Writes all parameters
     * and engaged policies into the registry.
     *
     * @param moduleResource - resource for the module
     * @param axisModule     - AxisModule instance
     * @throws Exception - on registry transaction error
     */
    public synchronized void handleExistingModuleInit(Resource moduleResource,
                                                      AxisModule axisModule) throws Exception {
        try {
            String paramResource = moduleResource.getPath() + RegistryResources.PARAMETERS;
            configRegistry.beginTransaction();

            // Add the Module Parameters
            if (configRegistry.resourceExists(paramResource)) {
                Collection parameters = (Collection) configRegistry.get(paramResource);
                for (String param : parameters.getChildren()) {
                    Resource resource = configRegistry.get(param);
                    if (!(resource instanceof Collection)) {
                        StAXOMBuilder builder = new StAXOMBuilder(resource.getContentStream());
                        Parameter parameter = ParameterUtil.createParameter(builder
                                .getDocumentElement());
                        Parameter p = axisModule.getParameter(resource
                                .getProperty(RegistryResources.NAME));
                        // don't overide the param if it alread exists and locked..
                        if (!(p != null && p.isLocked())) {
                            axisModule.addParameter(parameter);
                        }
                    }
                    resource.discard();
                }
                parameters.discard();
            }
            axisModule.getPolicySubject().clear();

            // Load policies from registry into AxisModule.
            String modulePath = PersistenceUtils.getResourcePath(axisModule);
            String policiesResource = modulePath + RegistryResources.POLICIES;
            if (configRegistry.resourceExists(policiesResource)) {
                Collection policies = (Collection) configRegistry.get(policiesResource);
                for (String policyResource : policies.getChildren()) {
                    Resource resource = configRegistry.get(policyResource);
                    if (!(resource instanceof Collection)) {
                        Policy policy = PolicyEngine.getPolicy(resource.getContentStream());
                        axisModule.getPolicySubject().attachPolicy(policy);
                    }
                    resource.discard();
                }
                policies.discard();
            }

            PersistenceUtils.handleGlobalParams(axisModule, moduleResource);
            configRegistry.commitTransaction();

            if (log.isDebugEnabled()) {
                log.debug("Initialized module - " + Utils.getModuleName(axisModule.getName(),
                        axisModule.getVersion().toString()));
            }
        } catch (Throwable e) {
            handleExceptionWithRollback("Unable to handle module init. Module: " + Utils
                    .getModuleName(axisModule.getName(), axisModule.getVersion().toString()), e);
            PersistenceUtils.markFaultyModule(axisModule);
        }
    }

    /**
     * Remove the specified parameter from the given module
     *
     * @param module    - AxisModule instance
     * @param parameter - parameter to remove
     * @throws Exception - on error
     */
    public void removeModuleParameter(AxisModule module, Parameter parameter) throws Exception {
        removeParameter(PersistenceUtils.getResourcePath(module), parameter.getName());
    }

    /**
     * Persist the given module parameter. If the parameter already exists in registry,
     * update it. Otherwise, create a new parameter.
     *
     * @param module    - AxisModule instance
     * @param parameter - parameter to persist
     * @throws Exception - on registry call errors
     */
    public void updateModuleParameter(AxisModule module, Parameter parameter) throws Exception {
        try {
            updateParameter(PersistenceUtils.getResourcePath(module), parameter);
        } catch (Throwable e) {
            handleExceptionWithRollback("Unable to update module parameter " +
                    parameter.getName() + " of module " + module.getName(), e);
        }
    }

    /**
     * Delete the module from the registry
     *
     * @param module - AxisModule instance
     * @throws Exception - on error
     */
    public void removeModule(AxisModule module) throws Exception {
        removeResource(PersistenceUtils.getResourcePath(module));
    }

    /**
     * Extract all the service policies from the AxisService.
     *
     * @param axisModule the Axis2 module
     * @return module policies
     * @throws Exception on error
     */
    private List<Resource> getModulePolicies(AxisModule axisModule) throws Exception {
        // List of policies to return
        List<Resource> modulePolicies = new ArrayList<Resource>();

        PolicySubject modulePolicySubject = axisModule.getPolicySubject();
        List<PolicyComponent> policyList = new ArrayList<PolicyComponent>(modulePolicySubject
                .getAttachedPolicyComponents());

        // Get the merged module policy
        Policy policy = null;
        for (Object policyElement : policyList) {
            if (policyElement instanceof Policy) {
                policy = (policy == null) ?
                        (Policy) policyElement : policy.merge((Policy) policyElement);
            } else {
                PolicyReference policyReference = (PolicyReference) policyElement;
                String key = policyReference.getURI();
                int pos = key.indexOf("#");
                if (pos == 0) {
                    key = key.substring(1);
                } else if (pos > 0) {
                    key = key.substring(0, pos);
                }

                PolicyComponent attachedPolicyComponent = modulePolicySubject
                        .getAttachedPolicyComponent(key);

                if (attachedPolicyComponent != null && attachedPolicyComponent instanceof Policy) {
                    policy = (Policy) attachedPolicyComponent;
                }
            }
        }

        if (policy != null) {
            if (policy.getId() == null) {
                // Generate an ID
                policy.setId(UUIDGenerator.getUUID());
            }
            // Create a configRegistry resource from the merged module policy
            Resource policyResource = createPolicyResource(policy, policy.getId(),
                    PolicyInclude.AXIS_MODULE_POLICY);
            policyResource.setProperty(RegistryResources.ModuleProperties.VERSION, axisModule
                    .getVersion().toString());
            modulePolicies.add(policyResource);
        }

        return modulePolicies;
    }

    /**
     * Engage or disengage module globally
     *
     * @param module - AxisModule instance
     * @param engage - whether to engage or disengage the given module
     * @throws Exception - on registry transaction errors
     */
    private void handleGlobalModule(AxisModule module, boolean engage) throws Exception {
        String resourcePath = PersistenceUtils.getResourcePath(module);
        configRegistry.beginTransaction();
        if (configRegistry.resourceExists(resourcePath)) {
            Resource resource = configRegistry.get(resourcePath);
            resource.setProperty(RegistryResources.ModuleProperties.GLOBALLY_ENGAGED,
                    String.valueOf(engage));
            configRegistry.put(resourcePath, resource);
            resource.discard();
        } else {
            handleException("Trying to engage or disengage unavailable module " + module.getName());
        }
        configRegistry.commitTransaction();
    }
}
