package app.test;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class Base64Test {

	public static void main(String[] args) throws Exception {
		Base64Test rsaTest = new Base64Test();
//		String str = rsaTest.encrypt("321183199006190339");  //MzIxMTgzMTk5MDA2MTkwMzM5
		String str = rsaTest.encrypt("123456");  //MTIzNDU2
		System.out.println(str);
	}

	public String encrypt(String str) throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = this.readResource("base64.js", Charsets.UTF_8);
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction("encode",str);
		return data.toString(); 
	}
	
	public String readResource(final String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
	}
}
