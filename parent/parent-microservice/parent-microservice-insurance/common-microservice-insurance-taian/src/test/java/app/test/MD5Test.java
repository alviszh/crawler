package app.test;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @description:将密码 md5加密     32位小写
 * @author: sln 
 * @date: 2017年12月22日 上午11:53:02 
 */
public class MD5Test {
	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String result = "";  
		String str = "37040519870324134X";  
		  
		MessageDigest md5 = MessageDigest.getInstance("MD5");  
		md5.update((str).getBytes("UTF-8"));  
		byte b[] = md5.digest();  
		  
		int i;  
		StringBuffer buf = new StringBuffer("");  
		  
		for(int offset=0; offset<b.length; offset++){  
		    i = b[offset];  
		    if(i<0){  
		        i+=256;  
		    }  
		    if(i<16){  
		        buf.append("0");  
		    }  
		    buf.append(Integer.toHexString(i));  
		}  
		  
		result = buf.toString();  
		System.out.println("result = " + result);  
	}
}
