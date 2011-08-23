/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.esbhive.demoSample;

import java.util.List;
import org.esbhive.node.mgt.xsd.ESBNode;
import samples.services.SimpleStockQuoteServiceStub;

/**
 *
 * @author melaka
 */
public class Client {

	List<ESBNode> esbNodes;
	ListFetcher lf;

	public Client(List<ESBNode> esbNodes, ListFetcher lf) {
		this.esbNodes = esbNodes;
		this.lf = lf;
	}

	public void doWork() {
		ESBNode chosen = null;
		while (true) {
			chosen = esbNodes.get((int) Math.floor(Math.random() * (esbNodes.size() - 1)));
			SimpleStockQuoteServiceStub ssqss = new 

		}
	}
}
