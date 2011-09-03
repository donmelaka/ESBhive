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
package org.wso2.carbon.core.security;

import java.util.Collections;

import javax.management.remote.JMXAuthenticator;
import javax.management.remote.JMXPrincipal;
import javax.security.auth.Subject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.UserStoreManager;

/**
 * JMX Authenticator for WSAS
 */
public class CarbonJMXAuthenticator implements JMXAuthenticator {

    private static Log log = LogFactory.getLog(CarbonJMXAuthenticator.class);
    private static UserRealm userRealm;

    public static void setUserRealm(UserRealm userRealm) {
        CarbonJMXAuthenticator.userRealm = userRealm;
    }

    public Subject authenticate(Object credentials) {
        // Verify that credentials is of type String[].
        //
        if (!(credentials instanceof String[])) {
            // Special case for null so we get a more informative message
            if (credentials == null) {
                throw new SecurityException("Credentials required");
            }
            throw new SecurityException("Credentials should be String[]");
        }

        // Verify that the array contains username/password
        //
        final String[] aCredentials = (String[]) credentials;
        if (aCredentials.length < 2) {
            throw new SecurityException("Credentials should have at least username & password");
        }

        // Perform authentication
        //
        String username = aCredentials[0];
        String password = aCredentials[1];

        UserStoreManager authenticator;
        try {
            authenticator = userRealm.getUserStoreManager();
        } catch (UserStoreException e) {
            String msg = "Cannot get authenticator from Realm";
            log.error(msg, e);
            throw new SecurityException(msg, e);
        }

        try {
            if(authenticator.authenticate(username, password)){
                return new Subject(true,
                                   Collections.singleton(new JMXPrincipal(username)),
                                   Collections.EMPTY_SET,
                                   Collections.EMPTY_SET);
            } else {
                throw new SecurityException("Login failed. Invalid username or password.");
            }
        } catch (Exception e) {
            String msg = "Cannot authenticate";
            log.error(msg, e);
            throw new SecurityException(msg, e);
        }
    }
}
