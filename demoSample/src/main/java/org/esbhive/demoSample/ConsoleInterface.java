/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.esbhive.demoSample;

import java.util.List;
import org.esbhive.node.mgt.xsd.ESBNode;

/**
 *
 * @author guest
 */
public class ConsoleInterface extends UIInterface{

	@Override
	public void nodesFetched(List<ESBNode> esbNodes) {
		System.out.println("Node list feteched. New nodes are : "+esbNodes);
	}

	@Override
	public void requestFailed(ESBNode esbNode) {
		System.out.println("Requet sent to "+esbNode.getIpAndPort()+ " did not recieve a response.");
	}

	@Override
	public void nodeRemoved(List<ESBNode> oldEsbNodes, List<ESBNode> newEsbNodes) {
		System.out.println("Node removed from list. Old nodes: "+ oldEsbNodes+"\n"
			+ "New nodes: "+newEsbNodes);
	}

	@Override
	public void responseRecieved(ESBNode node, String value) {
		System.out.println("Response recieved from  node: "+ node.getIpAndPort()+ ". Value is : "+ value);
	}
}
