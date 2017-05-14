/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.security;

import br.com.virtualVanets.DefaultLoadClassFactory;
import static br.com.virtualVanets.DefaultLoadClassFactory.CLASS_LOAD_PROPERTIES_FILE;
import br.com.virtualVanets.common.security.ASecurityModel;
import br.com.virtualVanets.common.security.I9Key;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.TreeMap;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.*;
import java.security.cert.*;

/**
 *
 * @author geoleite
 */
public class I9Security extends ASecurityModel {
 
    //Deve ser definido uma estrutura com armazenamento em memória da chave secreta
    //Enquanto isso será utilizado uma única chave secreta pra todos os veículos
    
    public SecretKey getSecretKey() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("secretkey.der");
        return new I9Key().loadSecretKey(is);
    }
    
    /**
     * Verifica a assinatura da informação
     * @param pubKey
     * @param signature
     * @param data
     * @return
     * @throws Exception 
     */
    public boolean verifySign(PublicKey pubKey, byte[] signature, byte[] data) throws Exception {
        Signature sign = Signature.getInstance(I9Key.ALGORITHM_SIGN);
        sign.initVerify(pubKey);
        sign.update(data);
        return sign.verify(signature);
    }
    
    /**
     * Assina uma informação com a chave privada
     * @param privatekey
     * @param data
     * @return
     * @throws Exception 
     */
    public byte[] sign(PrivateKey privatekey, byte[] data) throws Exception {
        Signature signature = Signature.getInstance(I9Key.ALGORITHM_SIGN);
        signature.initSign(privatekey);
        signature.update(data);
        byte[] assinatura = signature.sign();
        return assinatura;
    }
    
    /**
     * Criptografa a informação com a chave que pode ser a pública ou a privada
     * @param key
     * @param data
     * @return 
     */
    public byte[] encrypt(Key key, byte[] data) {
        byte[] cipherText = null;
        try {
            final Cipher cipher = Cipher.getInstance(I9Key.ALGORITHM_CRIPTO);
            // Criptografa o texto puro usando a chave Púlica
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;   
    }
    
    /**
     * Descriptografa uma informação com a chave que pode ser pública ou privada
     * @param key
     * @param data
     * @return 
     */
    public byte[]  decrypt(Key key, byte[] data) {
        byte[] dectyptedText = null;

        try {
            final Cipher cipher = Cipher.getInstance(I9Key.ALGORITHM_CRIPTO);
            // Decriptografa o texto puro usando a chave Privada
            cipher.init(Cipher.DECRYPT_MODE, key);
            dectyptedText = cipher.doFinal(data);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return dectyptedText;
    }
    
    public static void insert(byte[] pubKey, String id) throws Exception {
        String driver = "org.postgresql.Driver";
        String url = "jdbc:postgresql://geoleite.com.br:5432/i9vanetsdb";
        String user = "postgres";
        String pass = "postgres";
        Class.forName(driver);
        Connection con = DriverManager.getConnection(url, user, pass);
        String sql = "update dev_device set dev_bt_publickey=? where dev_tx_identification=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1, pubKey);
        ps.setObject(2, id);
        ps.execute();
        ps.close();
        con.close();
    }
    

        
/*    
class Cifrador {

    public byte[][] cifra(PublicKey pub, byte[] textoClaro) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException {
        byte[] textoCifrado = null;
        byte[] chaveCifrada = null;
        //-- A) Gerando uma chave simétrica de 128 bits
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128);
        SecretKey sk = kg.generateKey();
        byte[] chave = sk.getEncoded();
        //-- B) Cifrando o texto com a chave simétrica gerada
        Cipher aescf = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivspec = new IvParameterSpec(new byte[16]);
        aescf.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(chave, "AES"), ivspec);
        textoCifrado = aescf.doFinal(textoClaro);
        //-- C) Cifrando a chave com a chave pública
        Cipher rsacf = Cipher.getInstance("RSA");
        rsacf.init(Cipher.ENCRYPT_MODE, pub);
        chaveCifrada = rsacf.doFinal(chave);
        return new byte[][]{textoCifrado, chaveCifrada};
    }
}

class Decifrador {

    public byte[] decifra(PrivateKey pvk, byte[] textoCifrado, byte[] chaveCifrada) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException {
        byte[] textoDecifrado = null;
        //-- A) Decifrando a chave simétrica com a chave privada
        Cipher rsacf = Cipher.getInstance("RSA");
        rsacf.init(Cipher.DECRYPT_MODE, pvk);
        byte[] chaveDecifrada = rsacf.doFinal(chaveCifrada);
        //-- B) Decifrando o texto com a chave simétrica decifrada
        Cipher aescf = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivspec = new IvParameterSpec(new byte[16]);
        aescf.init(Cipher.DECRYPT_MODE, new SecretKeySpec(chaveDecifrada, "AES"), ivspec);
        textoDecifrado = aescf.doFinal(textoCifrado);
        return textoDecifrado;
    }
}   
*/
    
    public static void main(String[] p) {
        try {
            
            
            String id = "2";
            I9Key i9Key = new I9Key();
            SecretKey secretKey = i9Key.createSecretKey();
            
            I9Security i9Security = new I9Security();
            
            byte[] textoClaro = "{\"operationCode\":10,\"type\":\"OBU\",\"device\":{\"speed\":0.21687925979495049,\"direction\":10.0,\"idDB\":0,\"latitude\":-10.906181511598998,\"longitude\":-37.061850152271525,\"altitude\":0.0,\"type\":\"OBU\"},\"date\":\"15/12/2016 08:08:31.073\",\"message\":\"1481800111073\"}".getBytes("UTF8");
            byte[] dadosCript = i9Security.encrypt(secretKey, textoClaro);
            File fSecretKey = new File("secretkey.der");
            i9Key.saveSecretKeyGenerate(secretKey, fSecretKey);
            secretKey = i9Key.loadSecretKey(new File("secretkey.der"));
            textoClaro = i9Security.decrypt(secretKey, dadosCript);
            System.out.println(new String(textoClaro));
            
//            KeyPair kp = i9Key.createKeys();
//            PrivateKey priK = kp.getPrivate();
//            PublicKey pubK = kp.getPublic();
//            File fPriK = new File("prik.der");
//            i9Key.saveKeyGenerate(priK, fPriK);
//            File fPubK = new File("pubk.der");
//            i9Key.saveKeyGenerate(pubK, fPubK);
//            priK = i9Key.loadPrivateKey(fPriK);
//            
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            i9Key.saveKeyGenerate(pubK, baos);
//            
//            insert(baos.toByteArray(), id);
            //File fPriK = new File("private_key.der");
            //File fPubK = new File("public_key.der");
            //PrivateKey priK = i9Key.loadPrivateKey(fPriK);
            //PublicKey pubK = i9Key.loadPublicKey(fPubK);
            //System.out.println(priK.getFormat());

            
//            String conteudo = "Projeto I9Vanets definindo a plataforma de Segurança";
//            I9Security i9Security = new I9Security();
//            byte[] assinatura = i9Security.sign(priK, conteudo.getBytes("UTF8"));
//            System.out.println(new String(assinatura));
//            boolean checaAssinatura = i9Security.verifySign(pubK, assinatura, conteudo.getBytes("UTF8"));
//            System.out.println("checado: " + checaAssinatura);
//            byte[] encriptado = i9Security.encrypt(pubK, conteudo.getBytes("UTF8"));
//            byte[] decritptado = i9Security.decrypt(priK, encriptado);
//            System.out.println(new String(decritptado));
//            encriptado = i9Security.encrypt(priK, conteudo.getBytes("UTF8"));
//            decritptado = i9Security.decrypt(pubK, encriptado);
//            System.out.println(new String(decritptado));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
}
