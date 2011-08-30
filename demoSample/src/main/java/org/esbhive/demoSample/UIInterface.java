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
public interface UIInterface {
	abstract public void nodesFetched(List<ESBNode> esbNodes);
	abstract public void requestFailed(ESBNode esbNode);
	abstract public void nodeRemoved(List<ESBNode> oldEsbNodes, List<ESBNode> newEsbNodes);
	abstract public void responseRecieved(ESBNode node, String value);
	public void currentLeader(String ipAndPort);
	abstract public List<String> ipPortPairs(List<ESBNode> esbNodes);
	public void sendingRequest(String ipAndPort);
	
}
