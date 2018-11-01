package app.test;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

/**
 * @description: 测试加密密码
 * @author: sln 
 * @date: 2018年1月19日 下午3:09:46 
 */
public class RSATest {
	public static void main(String[] args) throws Exception {
		RSATest rsaTest = new RSATest();
		String str = rsaTest.encryptedPhone("nyq1006");
		System.out.println(str);
	}
	public String encryptedPhone(String phonenum) throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = this.readResource("puyang.js", Charsets.UTF_8);
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		//加密
		Object data = invocable.invokeFunction("encryptedString",phonenum);
		return data.toString(); 
	}
	
	public String readResource(final String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
	}
}	
