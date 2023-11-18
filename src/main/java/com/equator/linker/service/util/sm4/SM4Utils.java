package com.equator.linker.service.util.sm4;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

public class SM4Utils {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] SG_EncECBData(byte[] keyBytes, byte[] plainText) {
        if (keyBytes != null && keyBytes.length == 16) {
            if (plainText != null && plainText.length > 0) {
                try {
                    SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "SM4");
                    Cipher cipher = Cipher.getInstance("SM4/ECB/PKCS5Padding");
                    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
                    return cipher.doFinal(plainText);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static byte[] SG_DecECBData(byte[] keyBytes, byte[] cipherText) {
        if (keyBytes != null && keyBytes.length == 16) {
            if (cipherText != null && cipherText.length > 0 && cipherText.length % 16 == 0) {
                try {
                    SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "SM4");
                    Cipher cipher = Cipher.getInstance("SM4/ECB/PKCS5Padding");
                    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
                    return cipher.doFinal(cipherText);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    protected byte[] encryptData_CBC(byte[] ivBytes, byte[] keyBytes, byte[] plainText) {
        if (keyBytes != null && keyBytes.length != 0 && keyBytes.length % 16 == 0) {
            if (plainText != null && plainText.length > 0) {
                if (ivBytes != null && ivBytes.length > 0) {
                    try {
                        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "SM4");
                        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
                        Cipher cipher = Cipher.getInstance("SM4/CBC/PKCS5Padding");
                        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
                        return cipher.doFinal(plainText);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    protected byte[] decryptData_CBC(byte[] ivBytes, byte[] keyBytes, byte[] cipherText) {
        if (keyBytes != null && keyBytes.length != 0 && keyBytes.length % 16 == 0) {
            if (cipherText != null && cipherText.length > 0) {
                if (ivBytes != null && ivBytes.length > 0) {
                    try {
                        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "SM4");
                        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
                        Cipher cipher = Cipher.getInstance("SM4/CBC/PKCS5Padding");
                        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
                        return cipher.doFinal(cipherText);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
