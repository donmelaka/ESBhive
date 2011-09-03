package org.wso2.carbon.core.util;

import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Enumeration;

import org.wso2.carbon.core.RegistryResources;
import org.wso2.carbon.utils.ServerConfiguration;

public class SignatureUtil {

    private static final String THUMB_DIGEST_ALGORITHM = "SHA-1";

    private static String signatureAlgorithm = "SHA1withRSA";
    private static String provider = "BC";
 
    public static void init() throws Exception{
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     * Retrieves the thumbprint for alias.
     * @param alias The alias
     * @return Thumbprint is returned.
     * @throws Exception
     */
    public static byte[] getThumbPrintForAlias(String alias) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(THUMB_DIGEST_ALGORITHM);
        sha.reset();
        Certificate cert = getCertificate(alias);
        sha.update(cert.getEncoded());
        byte[] thumb = sha.digest();
        return thumb;
    }

    /**
     * Validates the signature with the given thumbprint
     * @param thumb Thumbprint of the certificate
     * @param data Data on which the signature is performed
     * @param signature The signature.
     * @return
     * @throws Exception
     */
    public static boolean validateSignature(byte[] thumb, String data, byte[] signature) throws Exception {
        Signature signer = Signature.getInstance(signatureAlgorithm, provider);
        signer.initVerify(getPublicKey(thumb));
        signer.update(data.getBytes());
        boolean isVerified = signer.verify(signature);
        return isVerified;
    }
    
    /**
     * Validate the signature with the default thumbprint.
     * @param data The data which is used to perfrom the signature.
     * @param signature The signature to be validated.
     * @return True is returned if singature is valid.
     * @throws Exception
     */
    public static boolean validateSignature(String data, byte[] signature) throws Exception {
        Signature signer = Signature.getInstance(signatureAlgorithm, provider);
        signer.initVerify(getDefaultPublicKey());
        signer.update(data.getBytes());
        boolean isVerified = signer.verify(signature);
        return isVerified;
    }

    /**
     * Performs the signature with the default private key in the system.
     * @param data Data to be signed.
     * @return The signature is returned.
     * @throws Exception
     */
    public static byte[] doSignature(String data) throws Exception {       
        Signature signer = Signature.getInstance(signatureAlgorithm, provider);
        signer.initSign(getDefaultPrivateKey());
        signer.update(data.getBytes());
        byte[] signature = signer.sign();
        return signature;
    }
    
    private static PrivateKey getDefaultPrivateKey() throws Exception {
        KeyStoreManager keyStoreMan = KeyStoreManager.getInstance();
        KeyStore keyStore = keyStoreMan.getPrimaryKeyStore();
        ServerConfiguration config = ServerConfiguration.getInstance();
        String password = config
                .getFirstProperty(RegistryResources.SecurityManagement.SERVER_PRIMARY_KEYSTORE_PASSWORD);
        String alias = config.getFirstProperty(RegistryResources.SecurityManagement.SERVER_PRIMARY_KEYSTORE_KEY_ALIAS);
        PrivateKey privateKey = (PrivateKey)keyStore.getKey(alias, password.toCharArray());
        return privateKey;
    }
    
    private static PublicKey getDefaultPublicKey() throws Exception {
        KeyStoreManager keyStoreMan = KeyStoreManager.getInstance();
        KeyStore keyStore = keyStoreMan.getPrimaryKeyStore();
        ServerConfiguration config = ServerConfiguration.getInstance();
        String alias = config
                .getFirstProperty(RegistryResources.SecurityManagement.SERVER_PRIMARY_KEYSTORE_KEY_ALIAS);
        PublicKey publicKey = (PublicKey) keyStore.getCertificate(alias).getPublicKey();
        return publicKey;

    }

    private static PublicKey getPublicKey(byte[] thumb) throws Exception {
        KeyStoreManager keyStoreMan = KeyStoreManager.getInstance();
        KeyStore keyStore = keyStoreMan.getPrimaryKeyStore();
        PublicKey pubKey = null;
        Certificate cert = null;
        MessageDigest sha = MessageDigest.getInstance(THUMB_DIGEST_ALGORITHM);
        sha.reset();
        for (Enumeration<String> e = keyStore.aliases(); e.hasMoreElements();) {
            String alias = e.nextElement();
            cert = getCertificate(alias);
            sha.update(cert.getEncoded());
            byte[] data = sha.digest();

            if (Arrays.equals(data, thumb)) {
                pubKey = cert.getPublicKey();
                break;
            }
        }
        return pubKey;
    }

    private static Certificate getCertificate(String alias) throws Exception {
        KeyStoreManager keyStoreMan = KeyStoreManager.getInstance();
        KeyStore keyStore = keyStoreMan.getPrimaryKeyStore();
        Certificate cert = null;
        Certificate[] certs = keyStore.getCertificateChain(alias);
        if (certs == null || certs.length == 0) {
            cert = keyStore.getCertificate(alias);
        } else {
            cert = certs[0];
        }
        if (cert == null || !(cert instanceof X509Certificate)) {
            throw new Exception("Please check alias. Cannot retrieve valid certificate");
        }
        return cert;
    }

}
