package org.esbhive.node.addition;

import org.esbhive.node.mgt.ESBNode;

/**
 *
 * @author Piumi
 */

public interface NodeAdditionInterface {

    public String setNode(ESBNode newNode,ESBNode[] nodeList);
    public String DeployProxies();

}
