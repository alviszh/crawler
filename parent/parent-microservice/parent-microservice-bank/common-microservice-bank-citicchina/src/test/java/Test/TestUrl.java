package Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;


	 public class TestUrl {
		     private final static String[] strDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	
		
		    // return Hexadecimal
		    private static String byteToArrayString(byte bByte) {
		        int iRet = bByte;
		        if (iRet < 0) {
		            iRet += 256;
		        }
		        int iD1 = iRet / 16;
		        int iD2 = iRet % 16;
		        return strDigits[iD1] + strDigits[iD2];
		    }
	
		    // 转换字节数组为16进制字串
		     private static String byteToString(byte[] bByte) {
		         StringBuffer sBuffer = new StringBuffer();
		         for (int i = 0; i < bByte.length; i++) {
		            sBuffer.append(byteToArrayString(bByte[i]));
		        }
		         return sBuffer.toString().toUpperCase();
		     }
		 
		     public static String GetMD5Code(String strObj) {
		         String resultString = null;
		         try {
		             resultString = new String(strObj);
		            MessageDigest md = MessageDigest.getInstance("MD5");
		             // md.digest() 该函数返回值为存放哈希值结果的byte数组
		             resultString = byteToString(md.digest(strObj.getBytes()));
		         } catch (NoSuchAlgorithmException ex) {
		             ex.printStackTrace();
		         }
		         return resultString;
		     }
		 
		     public static String outString(String string)throws Exception{
		 		String string2 = string.replaceAll("\"\"", "");
		 		String string3 = string2.replaceAll("\"", "");
		 		return string3;
		 	}
		     
		     public static void main(String[] args) throws Exception {
		        // System.out.println(TestUrl.GetMD5Code("12qwaszx"));
		         
//		         String a="asd12";
//		         String lowerCase = a.toUpperCase();
//		         System.out.println(lowerCase);
//		    	 String outString = outString("2014\"\"");
//		    	 System.out.println(outString);
		    	 String str=DigestUtils.md5Hex("北京******研发部"); 
		    	 System.out.println(str);
		    }
		 }
	
	
