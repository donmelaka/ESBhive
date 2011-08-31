/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.esbhive.demoSample;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axis2.transport.http.HTTPConstants;
import org.esbhive.node.mgt.xsd.ESBNode;
import samples.services.SimpleStockQuoteServiceStub;
import samples.services.xsd.GetQuote;
import samples.services.xsd.GetQuoteResponse;

/**
 *
 * @author melaka
 */
public class Client {

    List<ESBNode> esbNodes;
    ListFetcher lf;
    UIInterface ui;

    public Client(List<ESBNode> esbNodes, ListFetcher lf, UIInterface ui) {
        this.esbNodes = esbNodes;
        this.lf = lf;
        this.ui = ui;
    }


    public void doWork() {
        ESBNode chosen = null;
        while (true) {
            chosen = esbNodes.get((int) Math.floor(Math.random() * (esbNodes.size())));
            try {
                Thread.sleep(500);
                String url = "http://" + chosen.getIpAndPort().split(":")[0] + ":" + chosen.getSynapsePort() + "/services/StockQuoteProxy.StockQuoteProxyHttpSoap12Endpoint";
                SimpleStockQuoteServiceStub ssqss = new SimpleStockQuoteServiceStub(url);
                GetQuote gq = new GetQuote();
                gq.setSymbol("IBM");
                GetQuoteResponse response = ssqss.getQuote(gq);
                int timeOutInMilliSeconds =1000*5; // Two minutes
                ssqss._getServiceClient().getOptions().setProperty(
                        HTTPConstants.SO_TIMEOUT, new Integer(timeOutInMilliSeconds));
                ssqss._getServiceClient().getOptions().setProperty(
                        HTTPConstants.CONNECTION_TIMEOUT, new Integer(timeOutInMilliSeconds));
                ui.responseRecieved(chosen, "" + response.getLast());
            } catch (Exception ex) {
//				Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                List old = new ArrayList(esbNodes);
                esbNodes.remove(chosen);
                ui.nodeRemoved(old, esbNodes);
            }


        }
    }
}
