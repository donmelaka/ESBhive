package org.wso2.carbon.core.persistence;

import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.AxisFault;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.Parameter;
import org.apache.commons.logging.LogFactory;

public class OperationPersistenceManager extends AbstractPersistenceManager {

    // initialize log variable for this class
    static {
        log = LogFactory.getLog(OperationPersistenceManager.class);
    }

    /**
     * Constructor gets the axis config and calls the super constructor.
     *
     * @param axisConfig - AxisConfiguration
     * @throws AxisFault - if the config registry is not found
     */
    public OperationPersistenceManager(AxisConfiguration axisConfig) throws AxisFault {
        super(axisConfig);
    }

    /**
     * Handle the engagement of the module to operation at the registry level
     *
     * @param module    - AxisModule instance
     * @param operation - AxisOperation instance
     * @throws Exception - on error
     */
    public void engageModuleForOperation(AxisModule module, AxisOperation operation)
            throws Exception {
        try {
            handleModuleForAxisDescription(module,
                    PersistenceUtils.getResourcePath(operation), true);
            if (log.isDebugEnabled()) {
                log.debug("Successfully engaged " + module.getName() +
                        " module for " + operation.getName() + " operation");
            }
        } catch (Throwable e) {
            handleExceptionWithRollback("Unable to engage " + module.getName() +
                    " module to " + module.getOperations() + " operation ", e);
        }
    }

    /**
     * Handle the dis-engagement of the module to operation at the registry level
     *
     * @param module    - AxisModule instance
     * @param operation - AxisOperation instance
     * @throws Exception - on error
     */
    public void disengageModuleForOperation(AxisModule module, AxisOperation operation)
            throws Exception {
        try {
            handleModuleForAxisDescription(module,
                    PersistenceUtils.getResourcePath(operation), false);
            if (log.isDebugEnabled()) {
                log.debug("Successfully disengaged " + module.getName() +
                        " module from " + operation.getName() + " operation");
            }
        } catch (Throwable e) {
            handleExceptionWithRollback("Unable to disengage " + module.getName() +
                    " module from " + module.getOperations() + " operation ", e);
        }
    }

    /**
     * Remove the specified parameter from the given operation
     *
     * @param operation - AxisOperation instance
     * @param parameter - parameter to remove
     * @throws Exception - on error
     */
    public void removeOperationParameter(AxisOperation operation, Parameter parameter)
            throws Exception {
        removeParameter(PersistenceUtils.getResourcePath(operation), parameter.getName());
    }

    /**
     * Persist the given operation parameter. If the parameter already exists in registry, update
     * it. Otherwise, create a new parameter.
     *
     * @param operation - AxisOperation instance
     * @param parameter - parameter to persist
     * @throws Exception - on registry call errors
     */
    public void updateOperationParameter(AxisOperation operation, Parameter parameter)
            throws Exception {
        try {
            updateParameter(PersistenceUtils.getResourcePath(operation), parameter);
        } catch (Throwable e) {
            handleExceptionWithRollback("Unable to update the operation parameter " +
                    parameter.getName() + " of operation " + operation.getName(), e);
        }
    }
}
