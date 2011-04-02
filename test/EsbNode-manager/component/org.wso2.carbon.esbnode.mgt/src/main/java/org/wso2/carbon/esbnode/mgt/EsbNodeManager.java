/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Melaka
 */
package org.wso2.carbon.esbnode.mgt;

import java.util.ArrayList;

import org.wso2.carbon.esbnode.mgt.data.EsbNode;
//import org.wso2.carbon.student.mgt.data.Student;

public class EsbNodeManager {

  /*  public static void main(String[] args) {
        EsbNode esb1 = new EsbNode();
        esb1.setIpAddress("10.8.192.12");

        EsbNode esb2 = new EsbNode();
        esb2.setIpAddress("10.8.192.13");
        EsbNodeManager esbMan = new EsbNodeManager();
        //esbMan.addEsbNode(esb2);
        esbMan.addEsbNodeWithReturn(esb1);
        EsbNode[] ebsNodes = esbMan.addEsbNodeWithReturn(esb2);

        for (int i = 0; i < ebsNodes.length; i++) {
            System.out.println(ebsNodes[i].getIpAddress());
        }
    }*/

    private ArrayList<EsbNode> esbNodeList;

    public EsbNodeManager() {
        esbNodeList = new ArrayList<EsbNode>();

        EsbNode esb1 = new EsbNode();
        esb1.setIpAddress("10.8.192.12");

        EsbNode esb2 = new EsbNode();
        esb2.setIpAddress("10.8.192.13");

        this.addEsbNode(esb2);
        this.addEsbNode(esb1);

    }

    public void addEsbNode(EsbNode esbNode) {
        if (esbNode == null) {
            return;
        }
        esbNodeList.add(esbNode);
    }

    public EsbNode[] addEsbNodeWithReturn(EsbNode esbNode) {
        if (esbNode == null) {
            return this.getEsbNodes();
        }
        esbNodeList.add(esbNode);
        return this.getEsbNodes();
    }

    public EsbNode[] getEsbNodes() {
        EsbNode[] esbNodes = new EsbNode[esbNodeList.size()];
        esbNodeList.toArray(esbNodes);
        return esbNodes;
    }
}
