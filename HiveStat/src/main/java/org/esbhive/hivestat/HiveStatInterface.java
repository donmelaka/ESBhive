package org.esbhive.hivestat;

import org.esbhive.node.mgt.ESBNode;


public interface HiveStatInterface {

    public String giveStat(String name);
    public ESBNode[] selectBestNodes();
}
