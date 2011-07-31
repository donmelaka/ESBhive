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
public class ProEsb {

    private ProxyData pdata;
    private ESBNode node;

    public ProEsb() {
    
    }

    public ProEsb(ProxyData pd, ESBNode esbnode) {
    pdata=pd;
    node=esbnode;
    }

    public void setProxyData(ProxyData data) {
        this.pdata = data;
    }

    public void setESBNode(ESBNode enode) {
        this.node = enode;
    }

    public ProxyData getProxyData() {
        return pdata;
    }

    public ESBNode getESBNode() {
        return node;
    }
    
}
