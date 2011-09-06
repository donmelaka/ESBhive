/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.esbhive.node.mgt;

/**
 * This interface exposes the current nodes in the hive. Any component that consumes
 * the node list will use this interface.
 * @author pubudu
 */
public interface NodeManagerInterface {
  public org.esbhive.node.mgt.ESBNode[] getNodes();
}
