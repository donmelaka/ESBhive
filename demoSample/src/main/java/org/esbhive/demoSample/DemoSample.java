package org.esbhive.demoSample;

import java.io.File;
import java.rmi.RemoteException;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.esbhive.nodelistservice.NodeListServiceStub;

/**
 * @scr.component name="demo.sample" immediate="true"

 * 
 */
public class DemoSample {

	static NodeListServiceStub stub;

	public static void main(String[] args) throws RemoteException {
		String esb_home = "/home/guest/Desktop/wso2esb-3.0.1";
		System.setProperty("javax.net.ssl.trustStore", esb_home + File.separator
			+ "resources" + File.separator + "security" + File.separator + "wso2carbon.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");


		stub = createProxyNodeListServiceStub("localhost:9444");
		org.esbhive.node.mgt.xsd.ESBNode[] nodes = stub.getNodes();
		System.out.println(nodes.length);
		System.out.println(nodes[nodes.length-1]);
		
	}

	private static NodeListServiceStub createProxyNodeListServiceStub(String ipAndPort) throws AxisFault {
		ConfigurationContext ctx = null;
		ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);

		String serviceEPR4 = "https://" + ipAndPort + "/services/" + "NodeListService";

		NodeListServiceStub stub4 = null;
		stub4 = new NodeListServiceStub(ctx, serviceEPR4);
		ServiceClient client4 = stub4._getServiceClient();
		Options option = client4.getOptions();
		option.setManageSession(true);
		//option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, loginData.getCookie());
		return stub4;
	}
}
