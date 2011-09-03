package org.wso2.carbon.core.persistence;

import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.AxisFault;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.AxisDescription;
import org.apache.commons.logging.Log;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.Collection;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.utils.WSO2Constants;
import org.wso2.carbon.core.RegistryResources;
import org.wso2.carbon.core.util.ParameterUtil;
import org.wso2.carbon.CarbonException;

import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.XMLOutputFactory;
import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayOutputStream;

/**
 * Provides common logics for all extending PersistenceManager classes.
 */
public abstract class AbstractPersistenceManager {

    protected static Log log;

    /**
     * The Configuration registry instance in which all configuration data are stored.
     */
    protected Registry configRegistry;

    protected AxisConfiguration axisConfig;

    /**
     * Constructor gets the axis config and create reference to the config registry instances.
     *
     * @param axisConfig - AxisConfiguration
     * @throws AxisFault - if the config registry is not found
     */
    protected AbstractPersistenceManager(AxisConfiguration axisConfig) throws AxisFault {
        this.axisConfig = axisConfig;
        try {
            configRegistry = (Registry) axisConfig
                    .getParameterValue(WSO2Constants.CONFIG_SYSTEM_REGISTRY_INSTANCE);
        } catch (Exception e) {
            log.error("Error while retrieving config registry from Axis configuration", e);
        }
        if (configRegistry == null) {
            throw new AxisFault("Configuration Registry is not available");
        }
    }

    /**
     * If the parameter already exists, persist new value. Otherwise create a parameter resource
     * and set the value
     *
     * @param path      - param path to store
     * @param parameter - parameter instance
     * @throws RegistryException - on registry error
     */
    protected void updateParameter(String path, Parameter parameter) throws RegistryException {
        
        String paramResourcePath = path + RegistryResources.PARAMETERS + parameter.getName();
        String paramName = parameter.getName();
        int paramType = parameter.getParameterType();

        if (paramName != null && paramName.trim().length() != 0) {
            if (parameter.getParameterElement() == null && parameter.getValue() != null
                    && parameter.getValue() instanceof String) {
                try {
                    parameter = ParameterUtil.createParameter(paramName.trim(),
                            (String) parameter.getValue());
                } catch (AxisFault ignore) {}
            }
            
            if (parameter.getParameterElement() != null) {
                configRegistry.beginTransaction();
                Resource paramResource;
                if (!configRegistry.resourceExists(paramResourcePath)) {
                    paramResource = configRegistry.newResource();
                } else {
                    paramResource = configRegistry.get(paramResourcePath);
                }

                paramResource.addProperty(RegistryResources.ParameterProperties.NAME, paramName);
                paramResource.addProperty(RegistryResources.ParameterProperties.TYPE, Integer
                        .toString(paramType));
                paramResource.setContent(parameter.getParameterElement().toString());
                configRegistry.put(paramResourcePath, paramResource);
                paramResource.discard();
                configRegistry.commitTransaction();
            }
        }
            
    }

    /**
     * Remove a parameter at the given path from the registry
     *
     * @param path      - parameter path
     * @param paramName - parameter name
     * @throws Exception - on error
     */
    protected void removeParameter(String path, String paramName) throws Exception {
        removeResource(path + RegistryResources.PARAMETERS + paramName);
    }

    /**
     * Remove a resource at the given path
     *
     * @param path - resource path
     * @throws Exception - on registry error
     */
    protected void removeResource(String path) throws Exception {
        try {
            configRegistry.beginTransaction();
            if (configRegistry.resourceExists(path)) {
                configRegistry.delete(path);
            }
            configRegistry.commitTransaction();
        } catch (Throwable e) {
            handleExceptionWithRollback("Unable to remove the resource at " + path, e);
        }
    }

    /**
     * Engage or disenage module at the given resource path.
     *
     * @param module       - AxisModule instance
     * @param resourcePath - registry path of a service group, service, operation etc
     * @param engage       - engage or disengage
     * @throws Exception - on registry transaction errors
     */
    protected void handleModuleForAxisDescription(AxisModule module, String resourcePath,
                                                  boolean engage) throws Exception {
        configRegistry.beginTransaction();
        String moduleResourcePath = PersistenceUtils.getResourcePath(module);
        if (engage) {
            configRegistry.addAssociation(resourcePath, moduleResourcePath,
                    RegistryResources.Associations.ENGAGED_MODULES);
        } else {
            configRegistry.removeAssociation(resourcePath, moduleResourcePath,
                    RegistryResources.Associations.ENGAGED_MODULES);
        }
        configRegistry.commitTransaction();
    }

    /**
     * Load parameters for the given AxisDescription instance from registry
     *
     * @param ad     - AxisDescription instance
     * @param adPath - corresponding registry path
     * @throws Exception - on error
     */
    protected void loadParameters(AxisDescription ad, String adPath) throws Exception {
        String adParamPath = adPath + RegistryResources.PARAMETERS;
        if (configRegistry.resourceExists(adParamPath)) {
            Collection adParameters = (Collection) configRegistry.get(adParamPath);
            for (String paramPath : adParameters.getChildren()) {
                Resource param = configRegistry.get(paramPath);
                if (!(param instanceof Collection)) {
                    StAXOMBuilder builder = new StAXOMBuilder(param.getContentStream());
                    Parameter parameter = ParameterUtil.createParameter(builder
                            .getDocumentElement());
                    Parameter p = ad
                            .getParameter(param.getProperty(RegistryResources.NAME));
                    // don't overide the param if it alread exists and locked..
                    if (!(p != null && p.isLocked())) {
                        ad.addParameter(parameter);
                    }
                }
                param.discard();
            }
            adParameters.discard();
        }
    }

    /**
     * Write list of parameters to the registry
     *
     * @param paramList - Parameter list
     * @param adPath    - resource path to be written
     * @throws Exception - on error
     */
    protected void writeParameters(ArrayList<Parameter> paramList, String adPath) throws Exception {
        for (Object o : paramList) {
            Parameter parameter = (Parameter) o;
            String paramName = parameter.getName();
            if (paramName != null && paramName.trim().length() != 0) {
                if (parameter.getParameterElement() == null && parameter.getValue() != null
                        && parameter.getValue() instanceof String) {
                    parameter = ParameterUtil.createParameter(paramName.trim(),
                            (String) parameter.getValue());
                }
                if (parameter.getParameterElement() != null) {
                    Resource paramResource = configRegistry.newResource();
                    paramResource.setContent(parameter.getParameterElement().toString());
                    paramResource.addProperty(RegistryResources.NAME, parameter.getName());
                    configRegistry.put(adPath + RegistryResources.PARAMETERS
                            + parameter.getName(), paramResource);
                    paramResource.discard();
                }
            }
        }
    }

    /**
     * Writes any AxisDescription instance to the registry with it's documentation and name as
     * properties
     *
     * @param ad           - AxisDescription instance
     * @param nameProperty - name to be set
     * @param path         - path to store AxisDescription
     * @throws RegistryException - error on registry operations
     */
    protected void writeAxisDescription(AxisDescription ad, String nameProperty,
                                        String path) throws RegistryException {
        Collection collection = configRegistry.newCollection();
        String doc = ad.getDocumentation();
        if (doc != null) {
            collection.setProperty(RegistryResources.ServiceProperties.DOCUMENTATION, doc);
        }
        collection.setProperty(RegistryResources.NAME, nameProperty);
        configRegistry.put(path, collection);
    }

    /**
     * Get all the values against a property of a given resource path
     *
     * @param resourcePath - resource path
     * @param property     - property name
     * @return - list of values
     * @throws RegistryException - on registry error
     */
    protected List getPropertyValues(String resourcePath, String property) throws RegistryException {
        Resource resource = configRegistry.get(resourcePath);
        List values = resource.getPropertyValues(property);
        resource.discard();
        return values;
    }

    /**
     * Load the documentation of an AxisDescription instance
     *
     * @param ad           - AxisDescription instance to set the documentation
     * @param resourcePath - resource path of the AxisDescription
     * @throws RegistryException - on registry error
     */
    protected void loadDocumentation(AxisDescription ad, String resourcePath)
            throws RegistryException {
        Resource resource = configRegistry.get(resourcePath);
        String documentation = resource
                .getProperty(RegistryResources.ServiceProperties.DOCUMENTATION);
        if (documentation != null) {
            try {
                // tries to build an OMNode from the documentation
                ad.setDocumentation(AXIOMUtil.stringToOM(documentation));
            } catch (Exception e) {
                ad.setDocumentation(documentation);
            }
        }
    }

    /**
     * Load policies from the registry and attach to the AxisDescription instance.
     *
     * @param ad           - AxisDescription instance
     * @param policyIdList - list of policy uuids
     * @param servicePath  - all policies are stored at service level. Therefore, fetch the
     *                     actual policy from service level
     * @throws RegistryException - registry transaction errors
     */
    protected void loadPolicies(AxisDescription ad, List policyIdList,
                                String servicePath) throws RegistryException {
        // if AxisDescription is null, return
        if (ad == null) {
            return;
        }

        ad.getPolicySubject().clear();

        if (policyIdList != null) {
            for (Object servicePolicy : policyIdList) {
                String currentPolicyUUID = (String) servicePolicy;
                String policyResourcePath = servicePath + RegistryResources.POLICIES
                        + currentPolicyUUID;
                if (configRegistry.resourceExists(policyResourcePath)) {
                    Resource policyResource = configRegistry.get(policyResourcePath);
                    if (!(policyResource instanceof Collection)) {
                        Policy policy = PolicyEngine.getPolicy(policyResource.getContentStream());
                        ad.getPolicySubject().attachPolicy(policy);
                    }
                    policyResource.discard();
                } else {
                    log.error("Failed to load Policy with ID " + currentPolicyUUID
                            + ". The Policy does not exist.");
                }
            }
        }
    }

    /**
     * Checks whether the provided module is globally engaged. This is done using a property in
     * the module resource
     *
     * @param resourcePath - module resource path
     * @return - true if globally engaged, else false
     * @throws RegistryException - error on reading registry
     */
    protected boolean isGloballyEngaged(String resourcePath) throws RegistryException {
        if (configRegistry.resourceExists(resourcePath)) {
            Resource resource = configRegistry.get(resourcePath);
            boolean globallyEngaged = Boolean.parseBoolean(resource
                    .getProperty(RegistryResources.ModuleProperties.GLOBALLY_ENGAGED));
            resource.discard();
            return globallyEngaged;
        }
        return false;
    }

    /**
     * Creates a registry Resource for a given Policy
     *
     * @param policy     - Policy instance
     * @param policyId   - policy uuid
     * @param policyType - policy type
     * @return - created policy resource
     * @throws Exception - error on serialization
     */
    protected Resource createPolicyResource(Policy policy,
                                            String policyId, int policyType) throws Exception {
        Resource policyResource = configRegistry.newResource();
        policyResource.setProperty(RegistryResources.ServiceProperties.POLICY_UUID, policyId);

        // Set the policy as a string in the resource
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(baos);
        policy.serialize(writer);
        writer.flush();
        policyResource.setContent(baos.toString());

        policyResource.setProperty(RegistryResources.ServiceProperties.POLICY_TYPE,
                "" + policyType);
        return policyResource;
    }

    /**
     * Handles exception and rollbacks an already started transaction. Don't use this method if
     * you haven't already started a registry transaction
     *
     * @param msg - Message to log
     * @param e   - original exception
     * @throws CarbonException - carbon exception to throw
     */
    protected void handleExceptionWithRollback(String msg, Throwable e) throws Exception {
        log.error(msg, e);
        try {
            configRegistry.rollbackTransaction();
        } catch (RegistryException re) {
            log.error("Transaction rollback failed", re);
        }
        throw new CarbonException(msg, e);
    }

    protected void handleException(String msg, Throwable e) throws Exception {
        log.error(msg, e);
        throw new CarbonException(msg, e);
    }

    protected void handleException(String msg) throws Exception {
        log.error(msg);
        throw new CarbonException(msg);
    }
}
