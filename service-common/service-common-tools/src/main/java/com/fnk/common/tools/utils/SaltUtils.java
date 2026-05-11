package com.fnk.common.tools.utils;

/**
 * 加盐工具类
 * @author Enigma
 */
public class SaltUtils {

    // 生成盐
    public static String getSalt(int n) {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(chars[(int) (Math.random() * chars.length)]);
        }
        return sb.toString();
    }

}
