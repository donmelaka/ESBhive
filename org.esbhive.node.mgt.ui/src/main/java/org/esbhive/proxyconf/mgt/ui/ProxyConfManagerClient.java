/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.esbhive.proxyconf.mgt.ui;

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
import org.esbhive.node.mgt.client.ESBNode;
//import org.esbhive.node.mgt.client.EsbNodeManagerStub;
import org.esbhive.proxyconf.mgt.client.ProEsb;
import org.esbhive.proxyconf.mgt.client.ProxyConfManagerStub;
//import org.esbhive.proxyconf.mgt.client.ProxyData;
import org.esbhive.proxyconf.mgt.client.ProxyDataList;
import org.wso2.carbon.proxyadmin.xsd.ProxyData;

public class ProxyConfManagerClient {

private  ProxyConfManagerStub stub;

  public ProxyConfManagerClient(ConfigurationContext configCtx, String backendServerURL, String cookie) throws AxisFault{
    String serviceURL = backendServerURL + "ProxyConfManager";


    stub = new ProxyConfManagerStub(configCtx, serviceURL);
    ServiceClient client = stub._getServiceClient();
    Options options = client.getOptions();
    options.setManageSession(true);
    options.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, cookie);
  }


   public ProEsb getProEsb(String proxyname) throws java.lang.Exception{
     try{
        ProEsb proesb= stub.getProEsb(proxyname);
        return proesb;
     }
     catch (RemoteException ex) {
      String message = "Error when getting proesb";
      return null;
    }
   }

   public String[] getKeySet() throws java.lang.Exception{
     try{
        String[] arr= stub.getKeySet();
        return arr;
     }
     catch (RemoteException ex) {
      String message = "Error when getting KeySet";
      return null;
    }
   }

   public ProxyDataList getProxyDataList(String ipandport) throws java.lang.Exception{
     try{
        ProxyDataList prodatalist=stub.getProxyDataList(ipandport);
        //String test=prodatalist[0].getName();
         //System.out.println(test);
        return prodatalist;

     }
     catch (RemoteException ex) {
      String message = "Error when getting proxydatalist";
      return null;
    }
   }
   

/*
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
 * 
 */


}