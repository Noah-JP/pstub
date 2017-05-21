package com.gt.stub.web.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by noah on 2017. 5. 20..
 */
@Component
public class TokenEncryptor {

    public static final String ENCRYPT_KEY = "1234567890123456";
    public static final String ENCRYPT_METHOD = "AES/CBC/PKCS5Padding";
    public static final String ENCRYPT_PADDING_DATE_FORMAT = "yyyyMMddHHmmss";

    private Cipher encryptor;
    private Cipher decryptor;

    @PostConstruct
    private void initialize() throws Exception {
        this.encryptor = Cipher.getInstance(ENCRYPT_METHOD);
        this.encryptor.init(Cipher.ENCRYPT_MODE, getAESKey(), getIvKey());

        this.decryptor = Cipher.getInstance(ENCRYPT_METHOD);
        this.decryptor.init(Cipher.DECRYPT_MODE, getAESKey(), getIvKey());
    }

    public String encrypt(String target) {
        Assert.hasText(target, "Encrypt target must be not empty.");
        String result;
        try {
            byte[] encryptedBytes = encryptor.doFinal(target.concat(getNowToString()).getBytes());
            result = new String(Base64.encodeBase64String(encryptedBytes));
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
        return result;
    }

    public String decrypt(String target) {
        Assert.hasText(target, "Decrypt target must be not empty.");
        String decrypted;
        try {
            byte[] targetBytes = target.getBytes();
            decrypted = new String(decryptor.doFinal(Base64.decodeBase64(targetBytes)));
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
        return decrypted.substring(0, decrypted.length() - ENCRYPT_PADDING_DATE_FORMAT.length());
    }

    private String getNowToString() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(ENCRYPT_PADDING_DATE_FORMAT));
    }

    private IvParameterSpec getIvKey() throws Exception {
        return new IvParameterSpec(ENCRYPT_KEY.getBytes());
    }

    private SecretKeySpec getAESKey() throws Exception {
        return new SecretKeySpec(ENCRYPT_KEY.getBytes(), "AES");
    }
}
