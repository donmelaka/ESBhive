package org.esbhive.demoapp;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Hello world!
 *
 */
public class App {

  static String[] url_list = null;

  public static void main(String[] args) throws InvalidDataException, MalformedURLException, IOException {
    CodeSource src = App.class.getProtectionDomain().getCodeSource();
    if (args.length == 0) {
      new App().runClients(src);
    } else {
      File data = new File(args[0]);
      if (data.exists() && data.isDirectory()) {
        new App().processData(data);
      }
    }
  }

  public void processData(File data) throws InvalidDataException, MalformedURLException, IOException {
    ChartBuilder jb = new ChartBuilder();
    File[] files = data.listFiles();
    for(int i=0;i<files.length;i++){
      ResponseDataCalculator rdc = new ResponseDataCalculator(files[i]);
      jb.addCalculatedDataItem(rdc);
    }
    jb.createChart();
  }

  public void runClients(CodeSource src) {
    String esb_home = null;
    int numReqestsPerClient = 0;
    int numTotalClients = 0;
    int numClientMachines = 0;
    URL url = null;

    try {
      if (src != null) {
        url = new URL(src.getLocation(), "options.properties");
      }
      Configuration config = new PropertiesConfiguration(url);
      esb_home = config.getString("esb.home");
      url_list = config.getStringArray("url.list");
      numTotalClients = config.getInt("num.total.clients");
      numClientMachines = config.getInt("num.client.machines");
      numReqestsPerClient = config.getInt("requests.perclient");
    } catch (ConfigurationException ex) {
      Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
    } catch (MalformedURLException ex) {
      Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
    }
    System.setProperty("javax.net.ssl.trustStore", esb_home + File.separator
            + "resources" + File.separator + "security" + File.separator + "wso2carbon.jks");
    System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");

    CountDownLatch doneSignal = new CountDownLatch(numTotalClients / numClientMachines * numReqestsPerClient);
    File dir = null;
    try {
      String dirName = "numesbs-" + url_list.length + ".totalclients-" + numTotalClients
              + ".requestsperclient-" + numReqestsPerClient;
      url = new URL(src.getLocation(), "data");
      dir = new File(new File(url.toURI()), dirName);
      System.out.println(dir.getAbsolutePath());
      dir.mkdirs();
    } catch (URISyntaxException ex) {
      Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
    } catch (MalformedURLException ex) {
      Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
    }
    for (int i = 0; i < numTotalClients / numClientMachines; i++) {
      new Client(numReqestsPerClient, doneSignal, dir).start();
    }
    try {
      doneSignal.await();
    } catch (InterruptedException ex) {
      Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
