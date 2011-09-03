package org.wso2.carbon.core.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.CarbonException;
import org.wso2.carbon.registry.core.RegistryConstants;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.utils.WSO2Constants;

/**
 * This is the class should be used by Admin service authors to get the Registry
 * and Realms.
 */
public class AdminServicesUtil {

    private static Log log = LogFactory.getLog(AdminServicesUtil.class);

    public static boolean isSuperTenant() throws CarbonException {
        MessageContext messageContext = MessageContext.getCurrentMessageContext();
        if (messageContext == null) {
            String msg = "Could not get the user's Registry session. Message context not found.";
            log.error(msg);
            throw new CarbonException(msg);
        }

        HttpServletRequest request = (HttpServletRequest) messageContext
                .getProperty("transport.http.servletRequest");

        HttpSession session = request.getSession();
        int tenantId = (Integer) session.getAttribute(RegistryConstants.TENANT_ID);
        if (tenantId == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @deprecated
     */
    public static UserRegistry getSystemRegistry() throws CarbonException {

        MessageContext messageContext = MessageContext.getCurrentMessageContext();
        if (messageContext == null) {
            String msg = "Could not get the user's Registry session. Message context not found.";
            log.error(msg);
            throw new CarbonException(msg);
        }

        HttpServletRequest request = (HttpServletRequest) messageContext
                .getProperty("transport.http.servletRequest");

        UserRegistry registry = (UserRegistry) request.getSession().getAttribute(
                RegistryConstants.SYSTEM_REGISTRY);

        if (registry == null) {
            String msg = "System Registry instance is not found. "
                    + "Users have to login to retrieve a system registry instance for the tenant. ";
            log.error(msg);
            throw new CarbonException(msg);
        }

        return registry;
    }

    /**
     * @deprecated
     */
    public static UserRegistry getUserRegistry() throws CarbonException {

        MessageContext messageContext = MessageContext.getCurrentMessageContext();
        if (messageContext == null) {
            String msg = "Could not get the user's Registry session. Message context not found.";
            log.error(msg);
            throw new CarbonException(msg);
        }

        HttpServletRequest request = (HttpServletRequest) messageContext
                .getProperty("transport.http.servletRequest");

        UserRegistry registry = (UserRegistry) request.getSession().getAttribute(
                RegistryConstants.USER_REGISTRY);

        if (registry == null) {
            String msg = "User Registry instance is not found. "
                    + "Users have to login to retrieve a user registry instance for the tenant. ";
            log.error(msg);
            throw new CarbonException(msg);
        }

        return registry;
    }

    public static UserRealm getUserRealm() throws CarbonException {
        UserRealm userRealm;
        UserRegistry userRegistry = (UserRegistry) getHttpSession().getAttribute(
                WSO2Constants.CONFIG_USER_REGISTRY_INSTANCE);
        userRealm = userRegistry.getUserRealm();
        return userRealm;
    }
    
    private static HttpSession getHttpSession() {
        MessageContext msgCtx = MessageContext.getCurrentMessageContext();
        HttpSession httpSession = null;
        if (msgCtx != null) {
            HttpServletRequest request =
                    (HttpServletRequest) msgCtx.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
            httpSession = request.getSession();
        }
        return httpSession;
    }
    


}
