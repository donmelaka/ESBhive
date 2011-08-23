/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.esbhive.nodeListService;

import org.esbhive.node.mgt.ESBNode;
import org.esbhive.node.mgt.NodeManagerInterface;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @scr.component name="nodeList.service" immediate="true"
 * @scr.reference name="esbhive.node.service" interface="org.esbhive.node.mgt.NodeManagerInterface"
 * cardinality="1..1" policy="dynamic" bind="setNodeManager"  unbind="unsetNodeManager"
 * 
 */
public class NodeListService {

    private static NodeManagerInterface nodeManager;
    private static final Log log = LogFactory.getLog("org.wso2.carbon.NodeListService");
    
    public synchronized void setNodeManager(NodeManagerInterface r) {
        nodeManager = r;
    }

    public synchronized void unsetNodeManager(NodeManagerInterface r) {
        nodeManager = null;
    }

    public ESBNode[] getNodes() {
        ESBNode[] nodeList = null;
        if (nodeManager != null) {
            nodeList = nodeManager.getNodes();

        } else {
        }
        return nodeList;
    }

   
}
