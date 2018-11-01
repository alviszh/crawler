package app.test;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
/**
 * 改的生成图片验证码的js，但后来发现该网站用post请求登录可以跳过图片验证码
 * @author sln
 *
 */
public class ValidateCodeTest {
	public static void main(String[] args) throws Exception {
		String encrypt = encrypt();
		System.out.println(encrypt);
		String validate = validate(encrypt);
		System.out.println(validate);
	}

	public static String encrypt() throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path =readResource("validateCode.js", Charsets.UTF_8);
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction("createCode");
		return data.toString(); 
	}
	public static String readResource(final String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
	}
	public static String validate(String code) throws Exception{
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path =readResource("validateCode.js", Charsets.UTF_8);
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction("validate",code);
		return data.toString(); 
	}
}
