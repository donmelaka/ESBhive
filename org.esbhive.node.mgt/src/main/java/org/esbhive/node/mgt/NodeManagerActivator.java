/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.esbhive.node.mgt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import java.util.Properties;

/**
 *
 * @author Melaka
 */
public class NodeManagerActivator implements BundleActivator {

     private static final Log log = LogFactory.getLog(NodeManagerActivator.class);

    public void start(BundleContext bc) {

        if (log.isDebugEnabled()) {
            log.debug("Starting the node manager component ...");
        }

        Properties props = new Properties();
        bc.registerService(
                NodeManager.class.getName(), new NodeManager(), props);

        if (log.isDebugEnabled()) {
            log.debug("Successfully registered the property mediator service");
        }
    }

    public void stop(BundleContext bc) {
        if (log.isDebugEnabled()) {
            log.debug("Stopped the NodeManager component ...");
        }
    }

}
