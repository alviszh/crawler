package test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
 
public class HmacTest {

	public static void main(String[] args) {
		
		String payload = "{\"taskId\":\"aopshijiazhuang001\",\"username\":\"130103198805052124\",\"password\":\"234721\"}";
		
		String sss = base64Hmac256(payload,"27c7e4bc518c48d095d9caf544771876");
		
		System.out.println(sss);

	}
	
	
	public static String base64Hmac256(String payload, String secret) {
	    try {
	        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
	        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256");
	        sha256Hmac.init(secretKey);
	        return Base64.encodeBase64String(sha256Hmac.doFinal(payload.getBytes()));
	    } catch (Exception ignored) {
	        return "";
	    }
	}

}
