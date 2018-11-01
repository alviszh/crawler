package Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class TestUrl {

	public static void main(String[] args) throws UnsupportedEncodingException {
		String decode = URLDecoder.decode("ad5b729ba49949f793822da70556f95a", "UTF-8");
//		System.out.println(decode);
		String md5 = MD5("891453lili");
		System.out.println(md5.toLowerCase());
	}
	 
	public static String MD5(String s) {
		    try {
		        MessageDigest md = MessageDigest.getInstance("MD5");
		        byte[] bytes = md.digest(s.getBytes("utf-8"));
		        return toHex(bytes);
		    }
		    catch (Exception e) {
		        throw new RuntimeException(e);
		    }
		}

	public static String toHex(byte[] bytes) {

		    final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
		    StringBuilder ret = new StringBuilder(bytes.length * 2);
		    for (int i=0; i<bytes.length; i++) {
		        ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
		        ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
		    }
		    return ret.toString();
		}
}
