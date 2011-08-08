/**
 * Copyright (c) 2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.proxyadmin;

import org.apache.axiom.om.OMElement;
import org.wso2.carbon.mediation.initializer.ServiceBusConstants;
import org.wso2.carbon.mediation.initializer.persistence.MediationPersistenceManager;

/**
 *
 */
public class ProxyServiceDeployer extends org.apache.synapse.deployers.ProxyServiceDeployer {

    MediationPersistenceManager mpm;

    public ProxyServiceDeployer() {
        super();
        this.mpm = MediationPersistenceManager.getInstance();
    }

    @Override
    public String deploySynapseArtifact(OMElement artifactConfig, String fileName) {
        String proxyName = super.deploySynapseArtifact(artifactConfig, fileName);
        mpm.saveItemToRegistry(proxyName, ServiceBusConstants.ITEM_TYPE_PROXY_SERVICE);
        return proxyName;
    }

    @Override
    public String updateSynapseArtifact(OMElement artifactConfig, String fileName,
                                        String existingArtifactName) {
        String proxyName = super.updateSynapseArtifact(
                artifactConfig, fileName, existingArtifactName);
        mpm.saveItemToRegistry(proxyName, ServiceBusConstants.ITEM_TYPE_PROXY_SERVICE);
        return proxyName;
    }

    @Override
    public void undeploySynapseArtifact(String artifactName) {
        super.undeploySynapseArtifact(artifactName);
        mpm.deleteItemFromRegistry(artifactName, ServiceBusConstants.ITEM_TYPE_PROXY_SERVICE);
    }

    @Override
    public void restoreSynapseArtifact(String artifactName) {
        super.restoreSynapseArtifact(artifactName);
        mpm.saveItemToRegistry(artifactName, ServiceBusConstants.ITEM_TYPE_PROXY_SERVICE);
    }
}
