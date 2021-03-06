/*                                                                             
 * Copyright 2004,2005 The Apache Software Foundation.                         
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
package org.wso2.carbon.core.multitenancy.transports;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.SessionContext;
import org.apache.axis2.description.TransportInDescription;
import org.apache.axis2.transport.TransportListener;

/**
 *
 */
public class DummyTransportListener implements TransportListener {

    private String tenant;
    private TransportListener mainTransportListener;

    public DummyTransportListener(TransportListener mainTransportListener, String tenant) {
        this.mainTransportListener = mainTransportListener;
        this.tenant = tenant;
    }

    public void init(ConfigurationContext axisConf, TransportInDescription transprtIn)
            throws AxisFault {
        // Nothing to implement
    }

    public void start() throws AxisFault {
        // Nothing to implement
    }

    public void stop() throws AxisFault {
        // Nothing to implement
    }

    public EndpointReference getEPRForService(String serviceName,
                                              String ip) throws AxisFault {
        String fullyQualifiedServiceName = "t/" + tenant + "/" + serviceName;
        EndpointReference epr =
                mainTransportListener.getEPRForService(fullyQualifiedServiceName, ip);
        return epr;
    }

    public EndpointReference[] getEPRsForService(String serviceName,
                                                 String ip) throws AxisFault {
        EndpointReference[] eprs = new EndpointReference[]{getEPRForService(serviceName, ip)};
        return eprs;
    }

    public SessionContext getSessionContext(MessageContext messageContext) {
        // Nothing to implement
        return null;
    }

    public void destroy() {
        // Nothing to implement
    }
}
