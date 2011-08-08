/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.esbhive.proconf.mgt;

/**
 *
 * @author jeewantha
 */

import java.rmi.RemoteException;
import java.util.Map;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.esbhive.node.mgt.xsd.ESBNode;
import org.esbhive.node.mgt.client.EsbNodeManagerStub;
import org.esbhive.proconf.mgt.client.ProConfManagerStub;
import org.wso2.carbon.proxyadmin.xsd.ProxyData;

public class ProConfManagerClient {
private  ProConfManagerStub stub;

  public ProConfManagerClient(ConfigurationContext configCtx, String backendServerURL, String cookie) throws AxisFault{
    String serviceURL = backendServerURL + "ProConfManager";


    stub = new ProConfManagerStub(configCtx, serviceURL);
    ServiceClient client = stub._getServiceClient();
    Options options = client.getOptions();
    options.setManageSession(true);
    options.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, cookie);
  }

  public void addToOthers(ProxyData data, ESBNode esbnode) throws RemoteException, Exception{
    try{
      stub.addToOthers(data,esbnode);
    } catch (RemoteException ex) {
      String message = "Error when adding node to map";
      throw new java.lang.Exception(message,ex);
    }
  }

  public void deleteNode(String proxyname) throws Exception{
    try{
      stub.deleteNode(proxyname);
    } catch (RemoteException ex) {
      String message = "Error when deleting node";
      throw new java.lang.Exception(message,ex);
    }
  }

  public  org.esbhive.proconf.mgt.client.ProEsb[] getlist() throws RemoteException, Exception{
    try{
      return stub.getlist();
    } catch (RemoteException ex) {
      String message = "Error when getting list";
      throw new java.lang.Exception(message,ex);
    }
  }


}
