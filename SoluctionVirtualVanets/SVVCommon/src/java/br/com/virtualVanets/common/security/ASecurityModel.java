/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.common.security;

import br.com.virtualVanets.DefaultLoadClassFactory;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.TreeMap;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 *
 * @author geoleite
 */
public abstract class ASecurityModel {

    public final static String ENCRIPTED = "Encripted";
    public final static String SECURITY_MODEL = "SecurityModel";
    private static ASecurityModel aSecurity;
    private static TreeMap<String, byte[]> mapSecretyKey = new TreeMap<String, byte[]>();
    private static boolean encripted = false;

    private static DefaultLoadClassFactory<ASecurityModel> defaultLoadClassFactory;

    public static ASecurityModel getInstance() throws Exception {
        if (defaultLoadClassFactory == null) {
            defaultLoadClassFactory = new DefaultLoadClassFactory<ASecurityModel>();
        }
        String value = defaultLoadClassFactory.getValueForNameParameter(ENCRIPTED);
        if (value == null || value.trim().length() == 0) {
            value = "false";
        }
        encripted = Boolean.parseBoolean(value);
        return defaultLoadClassFactory.getObjectForIdClass(SECURITY_MODEL);
    }

    /**
     * @return the encripted
     */
    public boolean isEncripted() {
        return encripted;
    }

    public abstract boolean verifySign(PublicKey pubKey, byte[] signature, byte[] data) throws Exception;

    /**
     * Assina uma informação com a chave privada
     *
     * @param privatekey
     * @param data
     * @return
     * @throws Exception
     */
    public abstract byte[] sign(PrivateKey privatekey, byte[] data) throws Exception;

    /**
     * Criptografa a informação com a chave que pode ser a pública ou a privada
     *
     * @param key
     * @param data
     * @return
     */
    public abstract byte[] encrypt(Key key, byte[] data) throws Exception;

    /**
     * Descriptografa uma informação com a chave que pode ser pública ou privada
     *
     * @param key
     * @param data
     * @return
     */
    public abstract byte[] decrypt(Key key, byte[] data) throws Exception;

    public abstract SecretKey getSecretKey() throws Exception;
//    public void addSecretyKey(String id, byte[] secretyKey) {
//        if (!mapSecretyKey.containsKey(id)) {
//            mapSecretyKey.put(id, secretyKey);
//        }        
//    }
//    
//    public byte[] getSecretKey(String id) {
//        return mapSecretyKey.get(id);
//    }
//    
//    public void removeSecretKey(String id) {
//        mapSecretyKey.remove(id);
//    }
}
