/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.common.security;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
//import sun.security.pkcs.PKCS8Key;

/**
 *
 * @author geoleite
 */
public class I9Key {

    public static final String ALGORITHM_CRIPTO = "AES";
    public static final String ALGORITHM = "RSA";
    public static final String ALGORITHM_SIGN = "MD5WithRSA";
    private static PrivateKey privateKeyServer;
    private static PublicKey publicKeyServer;
    private static final String sinal = "synchro";

    public I9Key() throws Exception {
    }

    /**
     * Create SecretKey para encriptação das mensagens
     *
     * @return Chave
     * @throws Exception
     */
    public SecretKey createSecretKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128);
        SecretKey sk = kg.generateKey();
        //byte[] key = sk.getEncoded();
        return sk;
    }

    /**
     * Cria chaves assincronas
     *
     * @return
     * @throws Exception
     */
    public KeyPair createKeys() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
        SecureRandom secureRandon = new SecureRandom();
        kpg.initialize(1024, secureRandon);
        return kpg.generateKeyPair();
    }

    public void saveKeyGenerate(Key key, File file) throws Exception {
        FileOutputStream fos = new FileOutputStream(file);
        saveKeyGenerate(key, fos);
    }

    public void saveSecretKeyGenerate(SecretKey key, File file) throws Exception {
        FileOutputStream fos = new FileOutputStream(file);
        saveSecretKeyGenerate(key, fos);
    }

    public void saveSecretKeyGenerate(SecretKey key, OutputStream oos) throws Exception {
        ObjectOutputStream objos = new ObjectOutputStream(oos);
        try {
            objos.writeObject(key);
        } finally {
            objos.flush();
            objos.close();
        }
    }

    public void saveKeyGenerate(Key key, OutputStream os) throws Exception {
        //PKCS8Key p = new PKCS8Key();

        //PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(key.getEncoded());
//        fos.write(spec.getEncoded());
        os.write(key.getEncoded());
        os.flush();
        os.close();
        //KeyFactory.getInstance(I9Key.ALGORITHM).
    }

    public static PrivateKey loadPrivateKeyServer() throws Exception {
        synchronized (sinal) {
            if (privateKeyServer == null) {
                privateKeyServer = new I9Key().loadPrivateKey(new File("server_privatekey.der"));
            }
            return privateKeyServer;
        }
    }

    public static PrivateKey loadSecretKeyServer() throws Exception {
        synchronized (sinal) {
            if (privateKeyServer == null) {
                privateKeyServer = new I9Key().loadPrivateKey(new File("server_privatekey.der"));
            }
            return privateKeyServer;
        }
    }

    public static PublicKey loadPublicKeyServer() throws Exception {
        synchronized (sinal) {
            if (publicKeyServer == null) {
                publicKeyServer = new I9Key().loadPublicKey(new File("server_publickey.der"));
            }
            return publicKeyServer;
        }
    }

    public static PublicKey loadPublicKeyServer(String path) throws Exception {
        synchronized (sinal) {
            if (publicKeyServer == null) {
                publicKeyServer = new I9Key().loadPublicKey(new File(path));
            }
            return publicKeyServer;
        }
    }

    public PrivateKey loadPrivateKey(File file) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(file);
        return loadPrivateKey(fileInputStream, (int) file.length());
    }

    public PrivateKey loadPrivateKey(InputStream is, int length) throws Exception {

        DataInputStream dis = new DataInputStream(is);
        byte[] keyBytes = new byte[length];
        dis.readFully(keyBytes);
        dis.close();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
        return kf.generatePrivate(spec);
    }

    public SecretKey loadSecretKey(InputStream is) throws Exception {
        SecretKey key;
        ObjectInputStream ois = new ObjectInputStream(is);
        try {
            key = (SecretKey) ois.readObject();
        } finally {
            ois.close();
        }
//        DataInputStream dis = new DataInputStream(is);
//        byte[] keyBytes = new byte[length];
//        dis.readFully(keyBytes);
//        dis.close();        
//        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
//        KeyFactory kf = KeyFactory.getInstance(ALGORITHM_CRIPTO);
//        return null;
        return key;
    }

    public SecretKey loadSecretKey(File file) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(file);
        return loadSecretKey(fileInputStream);
    }

    public PublicKey loadPublicKey(File file) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(file);
        return loadPublicKey(fileInputStream, (int) file.length());
    }

    public PublicKey loadPublicKey(InputStream is, int length) throws Exception {
        DataInputStream dis = new DataInputStream(is);
        byte[] keyBytes = new byte[length];
        dis.readFully(keyBytes);
        dis.close();
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
        return kf.generatePublic(spec);
    }

}
