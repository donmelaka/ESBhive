package org.esbhive.demoapp;

import java.io.File;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import samples.services.ExceptionException;
import samples.services.SimpleStockQuoteServiceStub;
import samples.services.xsd.GetQuote;

/**
 * Hello world!
 *
 */
public class App {

  public static void main(String[] args) {
    try {
      String esb_home = "/home/pubudu/L4S1/project/wso2esb-3.0.1";
      System.setProperty("javax.net.ssl.trustStore", esb_home + File.separator + "resources" + File.separator + "security" + File.separator + "wso2carbon.jks");
      System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
      GetQuote gq = new GetQuote();
      gq.setSymbol("IBM");
      SimpleStockQuoteServiceStub sqps = new SimpleStockQuoteServiceStub("http://localhost:8280/services/StockQuoteProxy.StockQuoteProxyHttpSoap12Endpoint");
      sqps.getQuote(gq);
    } catch (ExceptionException ex) {
      Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
    } catch (RemoteException ex) {
      Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
