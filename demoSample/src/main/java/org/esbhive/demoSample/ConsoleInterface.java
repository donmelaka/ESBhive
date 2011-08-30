/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.esbhive.demoSample;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.esbhive.node.mgt.xsd.ESBNode;

/**
 *
 * @author guest
 */
public class ConsoleInterface implements UIInterface{

	@Override
	public void nodesFetched(List<ESBNode> esbNodes) {
		System.out.println("Node list feteched. Nodes are : "+this.ipPortPairs(esbNodes));
	}

	@Override
	public void requestFailed(ESBNode esbNode) {
		System.out.println("Requet sent to "+esbNode.getIpAndPort()+ " did not recieve a response.");
	}

	@Override
	public void nodeRemoved(List<ESBNode> oldEsbNodes, List<ESBNode> newEsbNodes) {
		System.out.println("Node removed from list. Old nodes: "+ this.ipPortPairs(oldEsbNodes)+"\n"
			+ "New nodes: "+this.ipPortPairs(newEsbNodes));
	}

	@Override
	public void responseRecieved(ESBNode node, String value) {
		System.out.println("Response recieved from  node: "+ node.getIpAndPort()+ ". Value is : "+ value);
	}
        
        public List<String> ipPortPairs(List<ESBNode> esbNodes){
		List ipPortPairs = new ArrayList();
		for(Iterator<ESBNode> i = esbNodes.iterator();i.hasNext();){
			ipPortPairs.add(i.next().getIpAndPort());
		}
		return ipPortPairs;
	}

	

	public void currentLeader(String ipAndPort) {
		System.out.println("Nodes changed. Current leader is "+ipAndPort+".");
	}

	public void sendingRequest(String ipAndPort) {
		System.out.println("Sending request to "+ipAndPort+".");
		
	}
}
