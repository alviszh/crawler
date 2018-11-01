package com.test;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.UnsupportedEncodingException;  
import org.apache.commons.codec.binary.Base64; 
public class RSATest {

//	public static void main(String[] args) throws Exception {
//		RSATest rsaTest = new RSATest();
//		String str = rsaTest.encryptedPhone("402366");
//		System.out.println(str);
//	}
//
//	public String encryptedPhone(String phonenum) throws Exception{    
//		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
//		String path = this.readResource("base64.js", Charsets.UTF_8);
//		System.out.println(path);
//		FileReader reader1 = new FileReader(path); // 执行指定脚本
//		engine.eval(path);
//		final Invocable invocable = (Invocable) engine;  
//		Object data = invocable.invokeFunction("encryptedString",phonenum);
//		return data.toString();
//	}
//	
//	public String readResource(final String fileName, Charset charset) throws IOException {
//        return Resources.toString(Resources.getResource(fileName), charset);
//}
	
	  
	
	    public static void main(String[] args){  
	        String str = "485513";  
	        try{  
	            byte[] encodeBase64 = Base64.encodeBase64(str.getBytes("UTF-8"));  
	            System.out.println("RESULT: " + new String(encodeBase64));  
	        } catch(Exception e){  
	            e.printStackTrace();  
	        }  
	    }  
	

}
