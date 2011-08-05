/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.esbhive.proxyconf.mgt;

import org.esbhive.node.mgt.ESBNode;
import org.wso2.carbon.proxyadmin.ProxyData;

/**
 *
 * @author jeewantha
 */
public interface ProxyConfManagerInterface {
  public void addProxyConf(ProxyData prodata, ESBNode[] esbarray);

  public ProEsb getProEsb(String proxyname);

  public ProxyDataList getProxyDataList(String ipandport);

  public void deleteESB(String ipandport);

  public void deleteProxy(String proxyname);

}