package com.soundify.server.account.infrastructure.security;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
public class KeyPairLoader {
    private String jwtPublicKeyPath;
    private String jwtPrivateKeyPath;
    @Value("${application.security.jwt.keys-dir}")
    private String keysDir;
    private KeyPair keyPair;


    public KeyPair getKeyPair() {


        if (keyPair == null){
            File d = new File(keysDir);
            if (!d.exists()) {
                d.mkdir();
            }
            this.jwtPublicKeyPath = keysDir + "/public.pem";
            this.jwtPrivateKeyPath = keysDir + "/private.pem";
            keyPair = getKeyPair(jwtPublicKeyPath, jwtPrivateKeyPath);
        }
        return keyPair;
    }

    public KeyPairLoader(String jwtPublicKeyPath, String jwtPrivateKeyPath) {
        this.jwtPublicKeyPath = jwtPublicKeyPath;
        this.jwtPrivateKeyPath = jwtPrivateKeyPath;
    }

    public KeyPairLoader() {

    }

    public RSAPublicKey getJWTPublicKey(){
        return (RSAPublicKey) getKeyPair().getPublic();
    }

    public RSAPrivateKey getJWTPrivateKey(){
        return (RSAPrivateKey) getKeyPair().getPrivate();
    }

    private KeyPair getKeyPair(String publicKeyPath, String privateKeyPath) {
        File publicKeyFile = new File(publicKeyPath);
        File privateKeyFile = new File(privateKeyPath);
        if (publicKeyFile.exists() && privateKeyFile.exists()){
           try {
               KeyFactory keyFactory = KeyFactory.getInstance("RSA");
               byte[] publicBytes = readPublicKeyBytes(publicKeyFile);
               EncodedKeySpec publicEncodedKeySpec =   new X509EncodedKeySpec(publicBytes);
               PublicKey publicKey = keyFactory.generatePublic(publicEncodedKeySpec);

               byte[] privateBytes = readPrivateKeyBytes(privateKeyFile);
               EncodedKeySpec privateEncodedKeySpec =   new PKCS8EncodedKeySpec(privateBytes);
               PrivateKey privateKey = keyFactory.generatePrivate(privateEncodedKeySpec);
               return new KeyPair(publicKey, privateKey);
           } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
               throw new RuntimeException(e);
           }

        } else {
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048);
                KeyPair keyPair = keyPairGenerator.generateKeyPair();
                EncodedKeySpec publicEncodedKeySpec =   new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
                writePublicKey(publicKeyFile, publicEncodedKeySpec.getEncoded());

                EncodedKeySpec privateEncodedKeySpec =   new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
                writePrivateKey(privateKeyFile, privateEncodedKeySpec.getEncoded());
                return keyPair;
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }
    }


    private byte[] readEncoded(File file, String begin, String end) throws IOException {
        String key = Files.readString(file.toPath(), Charset.defaultCharset());
        String keyPEM = key
                .replace(begin, "")
                .replaceAll(System.lineSeparator(), "")
                .replace(end, "");

        return  Base64.decodeBase64(keyPEM);

    }
    private byte[] readPublicKeyBytes(File file) throws IOException {
        return readEncoded(file, PUBLIC_KEY_BEGIN, PUBLIC_KEY_END);

    }
    private byte[] readPrivateKeyBytes(File file) throws IOException {
        return readEncoded(file, PRIVATE_KEY_BEGIN, PRIVATE_KEY_END);

    }

    private void writeEncoded(File file, byte[] encoded, String begin, String end) throws IOException {
        String base64Key = Base64.encodeBase64String(encoded);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(begin + "\n");
            for (int i = 0; i < base64Key.length(); i += 64) {
                writer.write(base64Key.substring(i, Math.min(i + 64, base64Key.length())));
                writer.write("\n");
            }
            writer.write(end + "\n");
        }

    }

    private void writePublicKey(File file, byte[] encoded) throws IOException {
        writeEncoded(file, encoded, PUBLIC_KEY_BEGIN, PUBLIC_KEY_END);

    }

    private void writePrivateKey(File file, byte[] encoded) throws IOException {
        writeEncoded(file, encoded, PRIVATE_KEY_BEGIN, PRIVATE_KEY_END);

    }




    private static final String  PRIVATE_KEY_BEGIN = "-----BEGIN RSA PRIVATE KEY-----";
    private static final String  PRIVATE_KEY_END = "-----END RSA PRIVATE KEY-----";
    private static final String  PUBLIC_KEY_BEGIN = "-----BEGIN PUBLIC KEY-----";
    private static final String  PUBLIC_KEY_END = "-----END PUBLIC KEY-----";
}
