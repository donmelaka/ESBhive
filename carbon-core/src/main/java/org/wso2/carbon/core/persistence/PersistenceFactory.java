package org.wso2.carbon.core.persistence;

import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Factory to create different PersistenceManager instances
 */
public class PersistenceFactory {

    private static Log log = LogFactory.getLog(PersistenceFactory.class);

    private ServicePersistenceManager spm;
    private ServiceGroupPersistenceManager sgpm;
    private ModulePersistenceManager mpm;
    private OperationPersistenceManager opm;

    private AxisConfiguration axisConfig;

    public PersistenceFactory(AxisConfiguration axisConfig) {
        this.axisConfig = axisConfig;
    }

    public ServicePersistenceManager getServicePM() {
        if (spm == null) {
            try {
                spm = new ServicePersistenceManager(axisConfig);
            } catch (AxisFault axisFault) {
                log.error("Error while initializing " +
                        "the ServicePersistenceManager instance", axisFault);
            }
        }
        return spm;
    }

    public ServiceGroupPersistenceManager getServiceGroupPM() {
        if (sgpm == null) {
            try {
                sgpm = new ServiceGroupPersistenceManager(axisConfig);
            } catch (AxisFault axisFault) {
                log.error("Error while initializing the " +
                        "ServiceGroupPersistenceManager instance", axisFault);
            }
        }
        return sgpm;
    }

    public OperationPersistenceManager getOperationPM() {
        if (opm == null) {
            try {
                opm = new OperationPersistenceManager(axisConfig);
            } catch (AxisFault axisFault) {
                log.error("Error while initializing the " +
                        "OperationServicePersistenceManager instance", axisFault);
            }
        }
        return opm;
    }

    public ModulePersistenceManager getModulePM() {
        if (mpm == null) {
            try {
                mpm = new ModulePersistenceManager(axisConfig);
            } catch (AxisFault axisFault) {
                log.error("Error while initializing the " +
                        "ModulePersistenceManager instance", axisFault);
            }
        }
        return mpm;
    }
}
