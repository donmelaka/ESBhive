package org.wso2.carbon.core.util;

import org.wso2.carbon.CarbonException;
import org.wso2.carbon.core.RegistryResources;
import org.wso2.carbon.core.internal.CarbonCoreServiceComponent;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.utils.ServerConfiguration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * The purpose of this class is to centrally manage the key stores.
 * Load key stores only once.
 * Reloading them over and over result a in a performance penalty.
 *  
 */
public class KeyStoreManager {

    private static KeyStore primaryKeyStore = null;

    private static KeyStoreManager instance = null;

    /**
     * Chosen for thread safety 
     */
    private static ConcurrentHashMap<String, KeyStore> loadedKeyStores = null;

    private KeyStoreManager() {
        loadedKeyStores = new ConcurrentHashMap<String, KeyStore>();
    }

    public static KeyStoreManager getInstance() {
        if (instance == null) {
            instance = new KeyStoreManager();
        }
        return instance;
    }

    public KeyStore getKeyStore(String keyStoreName) throws Exception {

        if (KeyStoreUtil.isPrimaryStore(keyStoreName)) {
            return getPrimaryKeyStore();
        }

        KeyStore keyStore = loadedKeyStores.get(keyStoreName);

        if (keyStore == null) {

            String path = RegistryResources.SecurityManagement.KEY_STORES + "/" + keyStoreName;

            RegistryService registryService = CarbonCoreServiceComponent.getRegistryService();
            Registry registry = registryService.getConfigSystemRegistry();

            Resource resource = registry.get(path);

            if (resource != null) {
                byte[] bytes = (byte[]) resource.getContent();
                keyStore = KeyStore.getInstance(resource
                        .getProperty(RegistryResources.SecurityManagement.PROP_TYPE));
                CryptoUtil cryptoUtil = CryptoUtil.getDefaultCryptoUtil();
                String encryptedPassword = resource
                        .getProperty(RegistryResources.SecurityManagement.PROP_PASSWORD);
                String password = new String(cryptoUtil.base64DecodeAndDecrypt(encryptedPassword));
                ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
                keyStore.load(stream, password.toCharArray());
            }
            resource.discard();
        }

        if (keyStore == null) {
            throw new CarbonException("Failed to load keystore");
        }

        return keyStore;
    }

    public String getPassword(Resource resource) throws Exception {
        ;

        CryptoUtil cryptoUtil = CryptoUtil.getDefaultCryptoUtil();
        String encryptedPassword = resource
                .getProperty(RegistryResources.SecurityManagement.PROP_PRIVATE_KEY_PASS);
        String password = new String(cryptoUtil.base64DecodeAndDecrypt(encryptedPassword));
        return password;

    }

    public void updateKeyStore(String name, KeyStore keyStore) throws Exception {
        ServerConfiguration config = ServerConfiguration.getInstance();

        if (KeyStoreUtil.isPrimaryStore(name)) {
            String file = new File(
                    config
                            .getFirstProperty(RegistryResources.SecurityManagement.SERVER_PRIMARY_KEYSTORE_FILE))
                    .getAbsolutePath();
            FileOutputStream out = new FileOutputStream(file);
            String password = config
                    .getFirstProperty(RegistryResources.SecurityManagement.SERVER_PRIMARY_KEYSTORE_PASSWORD);
            keyStore.store(out, password.toCharArray());
            return;
        }

        String path = RegistryResources.SecurityManagement.KEY_STORES + "/" + name;

        RegistryService registryService = CarbonCoreServiceComponent.getRegistryService();
        Registry registry = registryService.getConfigSystemRegistry();

        Resource resource = registry.get(path);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CryptoUtil cryptoUtil = CryptoUtil.getDefaultCryptoUtil();
        String encryptedPassword = resource
                .getProperty(RegistryResources.SecurityManagement.PROP_PASSWORD);
        String password = new String(cryptoUtil.base64DecodeAndDecrypt(encryptedPassword));
        keyStore.store(outputStream, password.toCharArray());
        outputStream.flush();
        outputStream.close();

        resource.setContent(outputStream.toByteArray());

        registry.put(path, resource);
        resource.discard();
    }

    public KeyStore getPrimaryKeyStore() throws Exception {
        if (primaryKeyStore == null) {

            ServerConfiguration config = ServerConfiguration.getInstance();
            String file = new File(
                    config
                            .getFirstProperty(RegistryResources.SecurityManagement.SERVER_PRIMARY_KEYSTORE_FILE))
                    .getAbsolutePath();
            KeyStore store = KeyStore
                    .getInstance(config
                            .getFirstProperty(RegistryResources.SecurityManagement.SERVER_PRIMARY_KEYSTORE_TYPE));
            String password = config
                    .getFirstProperty(RegistryResources.SecurityManagement.SERVER_PRIMARY_KEYSTORE_PASSWORD);
            FileInputStream in = new FileInputStream(file);
            store.load(in, password.toCharArray());
            primaryKeyStore = store;
        }
        return primaryKeyStore;
    }

    public static PrivateKey getDefaultPrivateKey() throws Exception {
        ServerConfiguration config = ServerConfiguration.getInstance();
        String password = config
                .getFirstProperty(RegistryResources.SecurityManagement.SERVER_PRIMARY_KEYSTORE_PASSWORD);
        String alias = config
                .getFirstProperty(RegistryResources.SecurityManagement.SERVER_PRIMARY_KEYSTORE_KEY_ALIAS);
        PrivateKey privateKey = (PrivateKey) primaryKeyStore.getKey(alias, password.toCharArray());
        return privateKey;
    }

    public static PublicKey getDefaultPublicKey() throws Exception {
        ServerConfiguration config = ServerConfiguration.getInstance();
        String alias = config
                .getFirstProperty(RegistryResources.SecurityManagement.SERVER_PRIMARY_KEYSTORE_KEY_ALIAS);
        PublicKey publicKey = (PublicKey) primaryKeyStore.getCertificate(alias).getPublicKey();
        return publicKey;
    }

    public String getPrimaryPrivateKeyPasssword() {
        ServerConfiguration config = ServerConfiguration.getInstance();
        String password = config
                .getFirstProperty(RegistryResources.SecurityManagement.SERVER_PRIMARY_KEYSTORE_PASSWORD);
        return password;
    }

}
