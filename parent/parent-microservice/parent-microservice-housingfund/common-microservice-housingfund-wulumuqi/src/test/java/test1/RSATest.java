package test1;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class RSATest {

	public static void main(String[] args) throws Exception {
		RSATest rsaTest = new RSATest();
//		String str = rsaTest.encrypt("321183199006190339");  //MzIxMTgzMTk5MDA2MTkwMzM5
//		String str = rsaTest.encrypt("123456");  //MTIzNDU2
//		System.out.println(str);
	}

	public String encrypt(String str,String modulus,String exponent) throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = this.readResource("wulumuqi.js", Charsets.UTF_8);
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction("encryptedString",str,modulus,exponent);
		return data.toString(); 
	}
	
	public String readResource(final String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
	}
}
