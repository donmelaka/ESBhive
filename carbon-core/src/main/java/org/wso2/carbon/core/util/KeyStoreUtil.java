package org.wso2.carbon.core.util;

import java.io.File;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Enumeration;

import org.apache.axis2.AxisFault;
import org.wso2.carbon.core.RegistryResources;
import org.wso2.carbon.utils.ServerConfiguration;

public class KeyStoreUtil {

    /**
     * KeyStore name will be here.
     * 
     * @param store - keyStore
     * @return
     */
    public static String getPrivateKeyAlias(KeyStore store) throws Exception {
        String alias = null;
        Enumeration<String> enums = store.aliases();
        while(enums.hasMoreElements()){
            String name = enums.nextElement();
            if(store.isKeyEntry(name)){
                alias = name;
                break;
            }
        }
        return alias;
    }

    public static String getKeyStoreFileName(String fullName) {
        ServerConfiguration config = ServerConfiguration.getInstance();
        String fileName = config
                .getFirstProperty(RegistryResources.SecurityManagement.SERVER_PRIMARY_KEYSTORE_FILE);
        String name = null;
        int index = fileName.lastIndexOf("/");
        if (index != -1) {
            name = fileName.substring(index + 1);
        } else {
            index = fileName.lastIndexOf(File.separatorChar);
            if (index != -1) {
                name = fileName.substring(fileName.lastIndexOf(File.separatorChar));
            } else {
                name = fileName;
            }
        }
        return name;
    }

    public static boolean isPrimaryStore(String id) {
        ServerConfiguration config = ServerConfiguration.getInstance();
        String fileName = config
                .getFirstProperty(RegistryResources.SecurityManagement.SERVER_PRIMARY_KEYSTORE_FILE);
        int index = fileName.lastIndexOf("/");
        if (index != -1) {
            String name = fileName.substring(index + 1);
            if (name.equals(id)) {
                return true;
            }
        } else {
            index = fileName.lastIndexOf(File.separatorChar);
            String name = null;
            if (index != -1) {
                name = fileName.substring(fileName.lastIndexOf(File.separatorChar));
            } else {
                name = fileName;
            }

            if (name.equals(id)) {
                return true;
            }
        }
        return false;
    }

    public static Certificate getCertificate(String alias, KeyStore store) throws AxisFault {
        try {
            Enumeration enumeration = store.aliases();
            while (enumeration.hasMoreElements()) {
                String itemAlias = (String) enumeration.nextElement();
                if (itemAlias.equals(alias)) {
                    return store.getCertificate(alias);
                }
            }
            return null;
        } catch (Exception e) {
            String msg = "Could not read certificates from keystore file. ";
            throw new AxisFault(msg + e.getMessage());
        }
    }

}
