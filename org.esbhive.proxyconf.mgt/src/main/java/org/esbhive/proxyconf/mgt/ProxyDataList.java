/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.esbhive.proxyconf.mgt;

import java.util.ArrayList;
import org.wso2.carbon.proxyadmin.ProxyData;

/**
 *
 * @author jeewantha
 */
public class ProxyDataList {

public ProxyDataList(){
}

ArrayList<ProxyData> PDArr = new ArrayList<ProxyData>();

public void addToList(ProxyData pd){
    PDArr.add(pd);
}

    public ArrayList<ProxyData> getProxyDataList() {
        return PDArr;
    }
}
