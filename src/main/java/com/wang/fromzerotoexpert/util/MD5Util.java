package com.wang.fromzerotoexpert.util;

import org.springframework.util.DigestUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class MD5Util {


    public static String generateSalt(int length) {
        byte[] salt = new byte[length];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        // 使用十六进制编码将字节数组转换为字符串
        BigInteger bigInt = new BigInteger(1, salt);
        StringBuilder generatedSalt = new StringBuilder(bigInt.toString(16));

        // 确保字符串长度满足要求
        while (generatedSalt.length() < length * 2) {
            generatedSalt.insert(0, "0");
        }

        return generatedSalt.toString();
    }


//    public static String montage(String password, String salt) {
//        return password + salt;
//    }

    public static String generateMD5Password(String rawPassword, String salt) {
        String password = rawPassword + salt;
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        return md5Password;
    }
}
