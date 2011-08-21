
package org.esbhive.demoSample;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esbhive.nodelistservice.NodeListServiceStub;

/**
 * @scr.component name="demo.sample" immediate="true"

 * 
 */
public class DemoSample {
   
    private static final Log log = LogFactory.getLog("org.wso2.carbon.DemoSample");
 
public void consumeList(){
        NodeListServiceStub stub = this.createProxyNodeListServiceStub("localhost:9443");
        try {
            org.esbhive.node.mgt.xsd.ESBNode[] nodes = stub.getNodes();
            for(org.esbhive.node.mgt.xsd.ESBNode node:nodes){
                log.info("#####################"+node.getIpAndPort());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(DemoSample.class.getName()).log(Level.SEVERE, null, ex);
        }
}

private NodeListServiceStub createProxyNodeListServiceStub(String ipAndPort) {
		ConfigurationContext ctx = null;
		try {
			ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
		} catch (AxisFault ex) {
			log.error("Error in DemoSample", ex);
		}

		String serviceEPR4 = "https://" + ipAndPort + "/services/" + "NodeListService";

		NodeListServiceStub stub4 = null;
		try {
			stub4 = new NodeListServiceStub(ctx, serviceEPR4);
		} catch (AxisFault ex) {
			log.error("Error in DemoSample", ex);
		}
		ServiceClient client4 = stub4._getServiceClient();
		Options option = client4.getOptions();
		option.setManageSession(true);
		//option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, loginData.getCookie());
		return stub4;
	}
   
}
