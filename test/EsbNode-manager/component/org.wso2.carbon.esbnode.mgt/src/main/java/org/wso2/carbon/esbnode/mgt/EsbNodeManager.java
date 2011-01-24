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

    private ArrayList<EsbNode> esbNodeList;

    public EsbNodeManager() {
        esbNodeList = new ArrayList<EsbNode>();

        EsbNode esb1 = new EsbNode();
        esb1.setIpAddress("10.8.192.12");

        EsbNode esb2 = new EsbNode();
        esb1.setIpAddress("10.8.192.13");

    }

    public void addEsbNode(EsbNode esbNode) {
        if (esbNode == null) {
            return;
        }
        esbNodeList.add(esbNode);
    }

    public EsbNode[] getEsbNodes() {
        EsbNode[] esbNodes = new EsbNode[esbNodeList.size()];
        esbNodeList.toArray(esbNodes);
        return esbNodes;
    }
}
