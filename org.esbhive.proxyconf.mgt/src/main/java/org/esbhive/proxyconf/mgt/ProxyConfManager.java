package org.esbhive.proxyconf.mgt;

/**
 *
 * @author jeewantha
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.esbhive.node.mgt.ESBNode;
import org.wso2.carbon.proxyadmin.ProxyData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ProxyConfManager {

   private static Map<ProxyData, ESBNode>   nodes = new HashMap<ProxyData, ESBNode>();
  //private static List<ProEsb>   nodes = new ArrayList<ProEsb>();
    private static final Log log2 = LogFactory.getLog("org.wso2.carbon.ProxyConfManager");

    public ProxyConfManager() {
      
    }

    public void addNodeToMap(ProxyData proxyData, ESBNode esbNode) {
     nodes.put(proxyData, esbNode);

    }

    public void deleteNode(String proxyname) {
     ProxyData proxydata = null;
            ProxyData [] proxyarray= new ProxyData[nodes.size()];
                    nodes.keySet().toArray(proxyarray);
            for(int i=0;i<nodes.size();i++){
                if(proxyarray[i].getName().equals(proxyname)){
                    proxydata=proxyarray[i];
                    break;
                }
            }
                    nodes.remove(proxydata);
    
    }

    public ESBNode[] getEsbNodeList() {
        ESBNode[] esbnodes = new ESBNode[1];
        esbnodes[0] = new ESBNode();
        return esbnodes;
    }

   public ProxyData[] getProxyDataList() {
        ProxyData[] prodata = new ProxyData[nodes.size()];
       // prodata = nodes.keySet().toArray(prodata);
        return  prodata;
    }


    public ProEsb[] getlist() {
	   ProEsb[] proesb = new ProEsb[nodes.size()];
	   ProEsb temProEsb=new ProEsb();
                ProxyData[] prodata = new ProxyData[nodes.size()];
                ESBNode[] esbnodes = new ESBNode[nodes.size()];

		prodata=nodes.keySet().toArray(prodata);
                esbnodes=nodes.values().toArray(esbnodes);

                for(int i=0;i<proesb.length;i++){
                    temProEsb.setProxyData(prodata[i]);
                    temProEsb.setESBNode(esbnodes[i]);
		    proesb[i]=temProEsb;
                }

            return proesb;
    }
/*
        ProEsb[] proesb = new ProEsb[nodes.size()];
        ProxyData[] prodata = new ProxyData[nodes.size()];
        ESBNode[] esbnodes = new ESBNode[nodes.size()];
 *
 */

     //   prodata = nodes.keySet().toArray(prodata);
       // esbnodes = nodes.values().toArray(esbnodes);


    /*    for (int i = 0; i < proesb.length; i++) {
            proesb[i].setProxyData(prodata[i]);
            proesb[i].setESBNode(esbnodes[i]);
        }
     
         return new ProEsb[2];



    }

    *
     */
}
