package main.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Md5Util {

    private Md5Util() { }

    /**
     * 计算字符串的 MD5，返回小写 32 位十六进制；若 input 为 null 则返回 null。
     */
    public static String md5Hex(String input) {
        if (input == null) return null;
        return md5HexInternal(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 使用盐值计算 MD5（简单将盐拼接在原文后面），若 input 为 null 则返回 null。
     */
    public static String md5Hex(String input, String salt) {
        if (input == null) return null;
        String combined = (salt == null) ? input : (input + salt);
        return md5HexInternal(combined.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 校验原文（可带盐）和已知 MD5 是否匹配。
     */
    public static boolean matches(String input, String md5) {
        if (input == null || md5 == null) return false;
        return md5.equals(md5Hex(input));
    }

    public static boolean matches(String input, String salt, String md5) {
        if (input == null || md5 == null) return false;
        return md5.equals(md5Hex(input, salt));
    }

    private static String md5HexInternal(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(data);
            return toHexLower(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }

    private static String toHexLower(byte[] bytes) {
        char[] hexDigits = "0123456789abcdef".toCharArray();
        char[] result = new char[bytes.length * 2];
        int idx = 0;
        for (byte b : bytes) {
            int unsigned = b & 0xFF;
            result[idx++] = hexDigits[unsigned >>> 4];
            result[idx++] = hexDigits[unsigned & 0x0F];
        }
        return new String(result);
    }
}
