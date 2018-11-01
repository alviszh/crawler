package test.insurance.yichang;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Base64;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class RSATest {

	public static void main(String[] args) throws Exception {
		RSATest rsaTest = new RSATest();
		String sfzhm = "420529198610174513";
		sfzhm = sfzhm.toUpperCase();
		String key1 = "123456789";
		String str = rsaTest.encryptedPhone(sfzhm,key1);
//		System.out.println(str);
//		final Base64.Encoder encoder = Base64.getEncoder();
//		try {
//			byte[] timeByte = str.getBytes("UTF-8");
//			//编码
//			str = encoder.encodeToString(timeByte);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		System.out.println(str);
	}

	public String encryptedPhone(String sfzhm,String key1) throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = this.readResource("securityutil.js", Charsets.UTF_8);
		//System.out.println(path);
		//FileReader reader1 = new FileReader(path); // 执行指定脚本
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction("encryptedString",sfzhm,key1);
		return data.toString(); 
	}
	
	public String readResource(final String fileName, Charset charset) throws IOException {
		return Resources.toString(Resources.getResource(fileName), charset);
	}
	
}
