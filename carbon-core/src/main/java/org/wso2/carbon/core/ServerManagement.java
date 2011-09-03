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
package org.wso2.carbon.core;

import org.apache.axis2.description.TransportInDescription;
import org.apache.axis2.transport.TransportListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.utils.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.Query;
import javax.management.QueryExp;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class ServerManagement {

    private static final Log log = LogFactory.getLog(ServerManagement.class);
    private static final long TIMEOUT = 60 * 1000;
    private Map inTransports;

    public ServerManagement(Map inTransports) {
        this.inTransports = inTransports;
    }

    public ServerManagement() {
    }

    /**
     * Method to switch a node to maintenance mode.
     * <p/>
     * Here is the sequence of events:
     * <p/>
     * <oll>
     * <li>Client calls this method</li>
     * <li>The server stops accepting new requests/connections, but continues to stay alive so
     * that old requests & connections can be served</li>
     * <li>Once all requests have been processed, the method returns</li
     * </ol>
     */
    public void startMaintenance() throws Exception {
        log.info("Starting to switch to maintenance mode...");
        for (Iterator iter = inTransports.values().iterator(); iter.hasNext();) {
            TransportInDescription tinDesc = (TransportInDescription) iter.next();
            TransportListener transport = tinDesc.getReceiver();
            transport.stop();
        }
        log.info("Stopped all transport listeners");
        log.info("Waiting for request service completion...");

        waitForRequestCompletion();
        log.info("All requests have been served.");
    }

    /**
     * Wait till all service requests have been serviced. This method will only wait for a maximum
     * of {@link ServerManagement#TIMEOUT}
     *
     * @throws Exception If an error occurs while trying to connect to the Tomcat MBean
     */
    public void waitForRequestCompletion() throws Exception {
        
        /**
         * Get all MBeans with names such as Catalina:type=RequestProcessor,worker=http-9762,name=HttpRequest<n>
         * & Catalina:type=RequestProcessor,worker=http-9762,name=HttpsRequest<n>
         */
        MBeanServer mbs = ManagementFactory.getMBeanServer();
        boolean areRequestsInService;
        long start = System.currentTimeMillis();
        do {
            /*QueryExp q = Query.eq(Query.attr("stage"),
                                  Query.value(org.apache.coyote.Constants.STAGE_SERVICE));*/
            /* TODO Core should not have an dependency to any app server. */
            QueryExp q = Query.eq(Query.attr("stage"), Query.value(3));
            Set set = mbs.queryNames(new ObjectName("Catalina:type=RequestProcessor,*"), q);
            if (set.size() > 0) {
                areRequestsInService = true;
                if (System.currentTimeMillis() - start > TIMEOUT) {
                    log.warn("Timeout occurred even though there are active connections.");
                    break;
                }
                Thread.sleep(2000);
            } else {
                areRequestsInService = false;
            }
        } while (areRequestsInService);
    }

    /**
     * Method to change the state of a node from "maintenance" to "normal"
     *
     * @throws Exception If an error occurs while trying to connect to the Tomcat MBean
     */
    public void endMaintenance() throws Exception {
        log.info("Switching to normal mode...");
        for (Iterator iter = inTransports.values().iterator(); iter.hasNext();) {
            TransportInDescription tinDesc = (TransportInDescription) iter.next();
            TransportListener transport = tinDesc.getReceiver();
            transport.start();
        }
        log.info("Switched to normal mode");
    }
}
