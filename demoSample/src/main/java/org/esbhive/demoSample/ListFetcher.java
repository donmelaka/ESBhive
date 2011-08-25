/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.esbhive.demoSample;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.esbhive.node.mgt.xsd.ESBNode;
import org.esbhive.nodelistservice.NodeListServiceStub;

/**
 *
 * @author guest
 */
public class ListFetcher extends Thread {

	int howOftenSeconds;
	List<ESBNode> esbNodes;
	UIInterface ui;

	public ListFetcher(int howOftenSeconds, List<ESBNode> esbNodes, UIInterface ui) {
		this.howOftenSeconds = howOftenSeconds;
		this.esbNodes = esbNodes;
		this.ui = ui;
	}

	@Override
	public void run() {
		ESBNode chosen = null;
		while(true){
			try {
				Thread.sleep(howOftenSeconds*1000);
				chosen = esbNodes.get((int)Math.floor(Math.random()*(esbNodes.size()-1)));
				fetchList(chosen.getIpAndPort());
			} catch (Exception ex) {
				List old = new ArrayList(esbNodes);
//				Logger.getLogger(ListFetcher.class.getName()).log(Level.SEVERE, null, ex);
				esbNodes.remove(chosen);
				ui.nodeRemoved(old, esbNodes);
			}
		}
	}

	public void fetchList(String ipAndPort) throws AxisFault, RemoteException {
		ConfigurationContext ctx = null;
		ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);

		String serviceEPR4 = "https://" + ipAndPort + "/services/" + "NodeListService";

		NodeListServiceStub stub = null;
		stub = new NodeListServiceStub(ctx, serviceEPR4);
		ServiceClient client4 = stub._getServiceClient();
		Options option = client4.getOptions();
		option.setManageSession(true);
		org.esbhive.node.mgt.xsd.ESBNode[] nodes = stub.getNodes();
		esbNodes.clear();
		esbNodes.addAll(Arrays.asList(nodes));
		ui.nodesFetched(esbNodes);
	}
}
