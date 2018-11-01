package app.util;

import java.security.MessageDigest;

public class MD5Utils {

    /**
     * 大写字母的MD5加密
     *
     * @param s
     * @return
     */
    public static String capitalMD5(String s) {
        return MD5(s, false);
    }

    /**
     * 小写字母的MD5加密
     *
     * @param s
     * @return
     */
    public static String lowercaseMD5(String s) {
        return MD5(s, true);
    }

    public static String MD5(String s, boolean isLowercase) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            if (isLowercase) {
                return toLowercaseHex(bytes);
            } else {
                return toCapitalHex(bytes);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 小写字母的16进制字节转换
     *
     * @param bytes
     * @return
     */
    private static String toLowercaseHex(byte[] bytes) {
        final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
        return toHex(HEX_DIGITS, bytes);
    }

    /**
     * 大写字母的16进制字节转换
     *
     * @param bytes
     * @return
     */
    private static String toCapitalHex(byte[] bytes) {
        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        return toHex(HEX_DIGITS, bytes);
    }

    private static String toHex(final char[] HEX_DIGITS, byte[] bytes) {
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

}
