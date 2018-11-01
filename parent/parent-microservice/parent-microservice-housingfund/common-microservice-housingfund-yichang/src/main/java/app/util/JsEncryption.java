package app.util;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class JsEncryption {

	public static String encrypted(String sfzhm,String key1) throws Exception{
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = readResource("securityutil.js", Charsets.UTF_8);
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction("encryptedString",sfzhm,key1);
		return data.toString(); 
	}
	
	public static String readResource(final String fileName, Charset charset) throws IOException {
		return Resources.toString(Resources.getResource(fileName), charset);
	}
	
	
}
