/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.esbhive.node.consumer;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.esbhive.node.mgt.ESBNode;
import org.esbhive.node.mgt.NodeManagerInterface;

/**
 * @scr.component name="node.manager" immediate="true"
 * @scr.service interface="org.eclipse.osgi.framework.console.CommandProvider"
 * @scr.reference name="esbhive.node.service" interface="org.esbhive.node.mgt.NodeManagerInterface"
 * cardinality="1..1" policy="dynamic" bind="setNodeManager"  unbind="unsetNodeManager"
 * 
 */
public class ListConsumer implements CommandProvider{
  private NodeManagerInterface nodeManager;
  

  public synchronized void setNodeManager(NodeManagerInterface r) {
		nodeManager = r;
	}
	public synchronized void unsetNodeManager(NodeManagerInterface r) {
		nodeManager = null;
	}

  public synchronized void _listnodes(CommandInterpreter ci) {
		if(nodeManager != null) {
      ESBNode[]	nodeList = nodeManager.getNodes();
      for(int i=0 ; i<nodeList.length ; i++){
        ci.println(nodeList[i].getIpAndPort()+" "+nodeList[i].getUsername());
      }
		} else {
			ci.println("Error, no NodeManager available");
		}
	}



  public String getHelp() {
    return "\tlistnodes - list the currently available nodes";
  }
  
  
}
