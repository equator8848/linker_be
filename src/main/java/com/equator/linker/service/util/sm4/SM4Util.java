package com.equator.linker.service.util.sm4;

import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Hex;

import java.security.SecureRandom;

/**
 * 本类主要提供国密SM4算法使用样例，
 * 此样例包含国密SM4算法的(ECB模式|CBC模式)两种模式下的加密和解密方法的使用；
 * <p>
 * *********注意事项*********
 * 1) 所使用的包名为com.sgitg.sgcc.sm.*;
 * 2) 此算法使用只能依赖bcprov-jdk16-1.46.jar,项目中不能同时含有bouncy castle的其他版本的jar，
 * 否则使用过程中算法运算会抛出异常
 * 3) 以下SM4Util针对字符串进行算法操作，
 * 若项目组需要进行文件加密操作请使用SM4Utils对外方法进行byte数组算法运算处理
 */
public class SM4Util extends SM4Utils {

    private static final String key = "6a0150d920232858462c94bdd0a967a6";

    /**
     * generate a secret key.
     *
     * @return a random key or iv with hex code type.
     */
    public String generateKeyOrIV() {
        byte[] key = new byte[16];
        new SecureRandom().nextBytes(key);
        return Strings.fromByteArray(Hex.encode(key));
    }

    /**
     * 使用国密SM4算法ECB模式进行加密数据
     *
     * @param plainText 需要加密的明文数据
     * @param key       加密需要的hex编码的秘钥
     * @return 返回加密后的hex编码的密文
     */
    public static final String encryptBySM4ECB(String plainText, String key) {
        //调用国密SM4算法的ecb模式对明文数据进行加密
        byte[] sg_EncECBData = SG_EncECBData(Hex.decode(key), Strings.toUTF8ByteArray(plainText));
        return Strings.fromByteArray(Hex.encode(sg_EncECBData));
    }

    /**
     * 使用国密SM4算法ECB模式进行加密数据
     *
     * @param plainText 需要加密的明文数据
     * @return 返回加密后的hex编码的密文
     */
    public static final String encryptBySM4ECB(String plainText) {
        //调用国密SM4算法的ecb模式对明文数据进行加密
        byte[] sg_EncECBData = SG_EncECBData(Hex.decode(key), Strings.toUTF8ByteArray(plainText));
        return Strings.fromByteArray(Hex.encode(sg_EncECBData));
    }

    /**
     * 使用国密	SM4算法ECB模式进行解密数据
     *
     * @param cipherText 需要进行解密hex编码密文
     * @return 返回解密后的明文信息
     */
    public static final String decryptBySM4ECB(String cipherText) {
        //调用国密SM4算法ECB模式对密文进行解密
        byte[] sg_EncECBData = SG_DecECBData(Hex.decode(key), Hex.decode(cipherText));
        return Strings.fromUTF8ByteArray(sg_EncECBData);
    }


    /**
     * 使用国密	SM4算法ECB模式进行解密数据
     *
     * @param cipherText 需要进行解密hex编码密文
     * @param key        解密需要的hex编码的秘钥
     * @return 返回解密后的明文信息
     */
    public static final String decryptBySM4ECB(String cipherText, String key) {
        //调用国密SM4算法ECB模式对密文进行解密
        byte[] sg_EncECBData = SG_DecECBData(Hex.decode(key), Hex.decode(cipherText));
        return Strings.fromUTF8ByteArray(sg_EncECBData);
    }

    /**
     * 使用国密SM4算法CBC模式进行加密数据
     *
     * @param plainText 需要加密的明文数据
     * @param key       加密需要的hex编码的秘钥
     * @param iv        加密需要的hex编码的初始向量
     * @return 返回加密后的hex编码的密文字符
     */
    public final String encryptBySM4CBC(String plainText, String key, String iv) {
        //调用国密SM4算法的CBC模式对明文数据进行加密
        byte[] encryptData_CBC = encryptData_CBC(Hex.decode(iv), Hex.decode(key), Strings.toUTF8ByteArray(plainText));
        return Strings.fromByteArray(Hex.encode(encryptData_CBC));
    }

    /**
     * 使用国密SM4算法的CBC模式进行解密数据
     *
     * @param cipherText 加密后的hex编码密文数据
     * @param key        解密需要的hex编码的秘钥
     * @param iv         解密需要的hex编码的初始向量
     * @return 返回解密后明文字符串
     */
    public final String decryptBySM4CBC(String cipherText, String key, String iv) {
        //调用国密SM4算法CBC模式对密文进行解密
        byte[] decryptData_CBC = decryptData_CBC(Hex.decode(iv), Hex.decode(key), Hex.decode(cipherText));
        return Strings.fromUTF8ByteArray(decryptData_CBC);
    }

    public static void main(String[] args) {
        SM4Util sm4Util = new SM4Util();
        System.out.println("************************SM4 ECB模式加密/解密***************************");
        String plainText = "test";

        String encryptBySM4ECB1 = SM4Util.encryptBySM4ECB(plainText);
        System.out.println("国密SM4算法ECB模式加密后为：" + encryptBySM4ECB1);
        String decryptBySM4ECB1 = SM4Util.decryptBySM4ECB(encryptBySM4ECB1);
        System.out.println("国密SM4算法ECB模式解密后为：" + decryptBySM4ECB1);


        System.out.println("************************SM4 CBC模式加密/解密***************************");
        //国密SM4算法CBC加密解密使用
        String keyCBC = sm4Util.generateKeyOrIV();
        System.out.println("随机获取的SM4算法CBC模式加解密的秘钥为：" + keyCBC);
        String iv = sm4Util.generateKeyOrIV();
        System.out.println("随机获取的SM4算法CBC模式加解密的初始向量为：" + iv);
        String encryptBySM4CBC = sm4Util.encryptBySM4CBC(plainText, keyCBC, iv);
        System.out.println("国密SM4算法CBC模式加密后为：" + encryptBySM4CBC);
        String decryptBySM4CBC = sm4Util.decryptBySM4CBC(encryptBySM4CBC, keyCBC, iv);
        System.out.println("国密SM4算法CBC模式解密后为：" + decryptBySM4CBC);
    }

}

