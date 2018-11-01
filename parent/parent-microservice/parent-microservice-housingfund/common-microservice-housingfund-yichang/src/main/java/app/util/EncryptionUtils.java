package app.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


/**
 * @description: 给源码进行加密
 */
public class EncryptionUtils {

	// md5加密
	public static String StringToMd5(String html) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(html.getBytes("UTF-8"));
			byte[] encryption = md5.digest();

			StringBuffer strBuf = new StringBuffer();
			for (int i = 0; i < encryption.length; i++) {
				if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
					strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
				} else {
					strBuf.append(Integer.toHexString(0xff & encryption[i]));
				}
			}
			return strBuf.toString();
		} catch (NoSuchAlgorithmException e) {
			return "";
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	//Base64
	public static String Base64(String str){
		final Base64.Encoder encoder = Base64.getEncoder();
		try {
			byte[] timeByte = str.getBytes("UTF-8");
			//编码
			str = encoder.encodeToString(timeByte);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
