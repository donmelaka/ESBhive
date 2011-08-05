package org.esbhive.demoapp;

import java.io.File;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import samples.services.SimpleStockQuoteServiceCallbackHandler;
import samples.services.SimpleStockQuoteServiceStub;
import samples.services.xsd.GetQuote;

/**
 * Hello world!
 *
 */
public class App {
  static String[] url_list= null;
  public static void main(String[] args) {
    String esb_home = null ;
    int numReqestsPerClient = 0;
    try {
      Configuration config = new PropertiesConfiguration("options.properties");
      esb_home = config.getString("esb.home");
      url_list = config.getStringArray("url.list");
      numReqestsPerClient = config.getInt("requests.perclient");
    } catch (ConfigurationException ex) {
      Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
    }
    try {
      System.setProperty("javax.net.ssl.trustStore", esb_home + File.separator + "resources" + File.separator + "security" + File.separator + "wso2carbon.jks");
      System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
      GetQuote gq = new GetQuote();
      gq.setSymbol("IBM");
      for (int i = 0; i < numReqestsPerClient; i++) {
        Long curTime = System.nanoTime();
        SimpleStockQuoteServiceCallbackHandler ssqscbh =
                new SimpleStockQuoteServiceCallbackHandler(curTime) {

                  @Override
                  public void receiveResultgetQuote(samples.services.xsd.GetQuoteResponse result) {
                    long time = System.nanoTime() - ((Long)this.clientData).longValue();
                    System.out.println(time);
                  }
                };
        int next = (int)Math.rint(Math.random() * (App.url_list.length-1));
        SimpleStockQuoteServiceStub sqps = new SimpleStockQuoteServiceStub(App.url_list[next]+"/services/StockQuoteProxy.StockQuoteProxyHttpSoap12Endpoint");
        sqps.startgetQuote(gq, ssqscbh);
      }
      System.console().readLine();
    } catch (RemoteException ex) {
      Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
