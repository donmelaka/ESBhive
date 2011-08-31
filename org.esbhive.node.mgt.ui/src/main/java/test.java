/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jeewantha
 */
import org.apache.axis2.context.ConfigurationContext;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.ui.CarbonUIUtil;
import org.wso2.carbon.utils.ServerConstants;
import org.wso2.carbon.ui.CarbonUIMessage;
import org.esbhive.node.mgt.ui.NodeManagerClient;
import org.esbhive.node.mgt.client.ESBNode;
//import org.esbhive.node.mgt.xsd.ESBNode;
import org.wso2.carbon.proxyadmin.xsd.ProxyData;
import org.esbhive.proxyconf.mgt.ui.ProxyConfManagerClient;
import org.esbhive.proxyconf.mgt.client.ProxyDataList;
import org.esbhive.proxyconf.mgt.client.ProEsb;

public class test {

    org.esbhive.node.mgt.xsd.ESBNode[] esbNodes=new org.esbhive.node.mgt.xsd.ESBNode[0];
    String[] keylist=new String[10];
    boolean a=true;
   public void ttt(){
    for(int i=0;i<keylist.length;i++){
            System.out.println("test");
    }

   }
}
