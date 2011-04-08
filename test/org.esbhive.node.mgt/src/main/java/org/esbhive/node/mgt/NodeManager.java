package org.esbhive.node.mgt;

import java.rmi.RemoteException;
import java.util.ArrayList;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.transport.http.HTTPConstants;
import org.esbhive.node.mgt.data.ESBNode;
import org.esbhive.node.mgt.authenticator.proxy.AuthenticationAdminStub;
import org.esbhive.node.mgt.authenticator.proxy.AuthenticationExceptionException;



/**
 * Hello world!
 *
 */
public class NodeManager {

  private ArrayList<ESBNode> nodes;


  public NodeManager() {
    nodes = new ArrayList<ESBNode>();
  }


  //this is called by me to add myself to one esb in the hive
  public ESBNode[] addNode(ESBNode me, ESBNode addto) throws RemoteException, AuthenticationExceptionException {
//    String esb_home = "/home/pubudu/L4S1/project/wso2esb-3.0.1";
//
//    System.setProperty("javax.net.ssl.trustStore",
//            esb_home + File.separator + "resources" + File.separator + "security" + File.separator + "wso2carbon.jks");
//    System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
    ConfigurationContext ctx =
            ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);

    String serviceEPR = "https://"+addto.getIp()+"/services/" + "AuthenticationAdmin";
    AuthenticationAdminStub stub = new AuthenticationAdminStub(ctx, serviceEPR);

    ServiceClient client = stub._getServiceClient();
    Options options = client.getOptions();
    options.setManageSession(true);

    boolean isLogged = stub.login(addto.getUsername(),addto.getPassword(), addto.getIp());

    String cookie = (String) stub._getServiceClient().getServiceContext().getProperty(
            HTTPConstants.COOKIE_STRING);

    String serviceEPR4 = "https://"+addto.getIp()+"/services/" + "EsbNodeManager";

    EsbNodeManagerStub stub4 = new EsbNodeManagerStub(ctx, serviceEPR4);

    ServiceClient client4 = stub4._getServiceClient();
    Options option = client4.getOptions();
    option.setManageSession(true);
    option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, cookie);



    org.esbhive.node.mgt.data.xsd.ESBNode meInOtherFormat
            = new org.esbhive.node.mgt.data.xsd.ESBNode();
    meInOtherFormat.setIp(me.getIp());
    meInOtherFormat.setUsername(me.getUsername());
    meInOtherFormat.setPassword(me.getPassword());
    org.esbhive.node.mgt.data.xsd.ESBNode[] a = stub4.addNodeAndGetNodes(meInOtherFormat);

    for(int i=0;i<a.length;i++){
      nodes.add(new ESBNode(a[i].getIp(),a[i].getUsername(),a[i].getPassword()));
    }

    ESBNode[] nodeArray = new ESBNode[nodes.size()];
    nodes.toArray(nodeArray);
    return nodeArray;

  }


  //this is called by another backend to add him to me(this is called in the addnode method)
  public ESBNode[] addNodeAndGetNodes(ESBNode node) {
    if (node != null) {
      if (!nodes.contains(node)) {
        nodes.add(node);
      }
    }
    ESBNode[] nodeArray = new ESBNode[nodes.size()];
    nodes.toArray(nodeArray);
    return nodeArray;
  }

  public ESBNode[] getNodes() {    
    ESBNode[] nodeArray = new ESBNode[nodes.size()];
    nodes.toArray(nodeArray);
    return nodeArray;
  }
}
