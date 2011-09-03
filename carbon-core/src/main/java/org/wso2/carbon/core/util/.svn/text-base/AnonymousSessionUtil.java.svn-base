package org.wso2.carbon.core.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.CarbonException;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.user.core.util.UserCoreUtil;
import org.wso2.carbon.utils.TenantUtils;

/**
 * When there is no authenticated session please use this calss to get the
 * registry and realm.
 * 
 * Usage : Password Callback Handler
 */
public class AnonymousSessionUtil {

    private static Log log = LogFactory.getLog(AnonymousSessionUtil.class);
    
    /**
     * @deprecated 
     */
    public static UserRegistry getSystemRegistryByUserName(RegistryService registryService,
            RealmService realmService, String userName) throws CarbonException {
        try {
            String tenantDomain = UserCoreUtil.getTenantDomain(realmService, userName);
            return AnonymousSessionUtil.getSystemRegistryByDomainName(registryService, realmService,
                    tenantDomain);
        } catch (UserStoreException e) {
            throw new CarbonException(e.getMessage(), e);
        }
    }
    
    /**
     * @deprecated 
     */
    public static UserRegistry getUserRegistryByUserName(RegistryService registryService,
            RealmService realmService, String userName) throws CarbonException {
        try {
            return registryService.getUserRegistry(userName);
        } catch (RegistryException e) {
            log.error(e.getMessage(), e);
            throw new CarbonException(e.getMessage(), e);
        }
    
    }

    /**
     * @deprecated 
     */
    public static UserRegistry getSystemRegistryByDomainName(RegistryService registryService,
            RealmService realmService, String domainName) throws CarbonException {
        try {
            int tenantId = realmService.getTenantManager().getTenantId(domainName);
            if (tenantId < 0) {
                throw new CarbonException("Invalid domain name");
            }
            if (!realmService.getTenantManager().isTenantActive(tenantId)) {
                throw new CarbonException("Inactive tenant");
            }
            return registryService.getConfigSystemRegistry(tenantId);
        } catch (UserStoreException e) {
            log.error(e.getMessage(), e);
            throw new CarbonException(e.getMessage(), e);
        } catch (RegistryException e) {
            log.error(e.getMessage(), e);
            throw new CarbonException(e.getMessage(), e);
        }
    }

    /**
     * @deprecated 
     */
    public static UserRegistry getUserRegistry(RegistryService registryService, String userName)
            throws CarbonException {
        try {
            return registryService.getUserRegistry(userName);
        } catch (RegistryException e) {
            log.error(e.getMessage(), e);
            throw new CarbonException(e.getMessage(), e);
        }
    }

    public static UserRealm getRealmByUserName(RegistryService registryService,
            RealmService realmService, String userName) throws CarbonException {
        try {
            String tenantDomain = UserCoreUtil.getTenantDomain(realmService, userName);
            return AnonymousSessionUtil.getRealmByTenantDomain(registryService, realmService,
                    tenantDomain);
        } catch (UserStoreException e) {
            throw new CarbonException(e.getMessage(), e);
        }
    }

    public static UserRealm getRealmByTenantDomain(RegistryService registryService,
            RealmService realmService, String tenantDomain) throws CarbonException {
        try {
            int tenantId = realmService.getTenantManager().getTenantId(tenantDomain);
            if (tenantId < 0) {
                throw new CarbonException("Invalid domain name");
            }
            if (!realmService.getTenantManager().isTenantActive(tenantId)) {
                throw new CarbonException("Inactive tenant");
            }
            return registryService.getUserRealm(tenantId);
        } catch (UserStoreException e) {
            log.error(e.getMessage(), e);
            throw new CarbonException(e.getMessage(), e);
        } catch (RegistryException e) {
            log.error(e.getMessage(), e);
            throw new CarbonException(e.getMessage(), e);
        }
    }

}
