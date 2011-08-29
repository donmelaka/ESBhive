package org.esbhive.demoSample;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JFrame;
import org.apache.axis2.AxisFault;
import org.esbhive.node.mgt.xsd.ESBNode;

/**
 * @scr.component name="demo.sample" immediate="true"

 * 
 */
public class DemoSample {

    List<ESBNode> nodeList = Collections.synchronizedList(new ArrayList());
    DemoUI ui = new DemoUI();

    public static void main(String[] args) throws RemoteException {

        new DemoSample().doWork();
    }

    public void doWork() throws RemoteException {
        ui.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ui.setResizable(false);
        ui.setLocationRelativeTo(null);
        ui.setVisible(true);
        String esb_home = "/home/melaka/Desktop/wso2esb-9445";
        System.setProperty("javax.net.ssl.trustStore", esb_home + File.separator
                + "resources" + File.separator + "security" + File.separator + "wso2carbon.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        String input = System.console().readLine("How often should the list be feteched(in seconds)?");
        String input2 = System.console().readLine("What is the ip:port to first fetch the list?");
        ListFetcher lf = new ListFetcher(Integer.parseInt(input), nodeList, ui);
        Client client = new Client(nodeList, lf, ui);
        lf.fetchList(input2);
        lf.start();
        client.doWork();
    }
}
