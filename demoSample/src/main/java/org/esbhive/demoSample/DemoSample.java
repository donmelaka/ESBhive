package org.esbhive.demoSample;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import org.apache.axis2.AxisFault;
import org.esbhive.node.mgt.xsd.ESBNode;
import org.esbhive.nodelistservice.NodeListServiceStub;

/**
 * @scr.component name="demo.sample" immediate="true"

 * 
 */
public class DemoSample {

	List<ESBNode> nodeList = Collections.synchronizedList(new ArrayList());
	static NodeListServiceStub stub;

	public static void main(String[] args) throws RemoteException {
	}

	public void doWork() throws AxisFault, RemoteException {
		String esb_home = "/home/guest/Desktop/wso2esb-3.0.1";
		System.setProperty("javax.net.ssl.trustStore", esb_home + File.separator
			+ "resources" + File.separator + "security" + File.separator + "wso2carbon.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
		Scanner in = new Scanner(System.in);
		String input = System.console().readLine("How often should the list be feteched(in seconds)?");
		String input2 = System.console().readLine("What is the ip:port to first fetch the list?");
		ListFetcher lf = new ListFetcher(Integer.parseInt(input), nodeList);
		lf.fetchList(input2);
		lf.start();
		for(int i=0;i<nodeList.size();i++){
			System.out.println(nodeList.get(i).getIp());
		}
	}
}
