/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.esbhive.proxyconf.mgt;

import java.util.ArrayList;
import org.esbhive.node.mgt.ESBNode;
import org.wso2.carbon.proxyadmin.ProxyData;

/**
 *
 * @author jeewantha
 */
public class ProEsb {

    private ProxyData pdata;
    private ESBNode node;
    ArrayList<ESBNode> esbnodes = new ArrayList<ESBNode>();

    public ProEsb() {
    
    }

    public void addArrayToList(ESBNode[] list){ //can have duplicate entries
        for(int i=0;i<list.length;i++){
            esbnodes.add(list[i]);
        }

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

    public ArrayList<ESBNode> getESBNodes() {
        return esbnodes;
    }
    
}
