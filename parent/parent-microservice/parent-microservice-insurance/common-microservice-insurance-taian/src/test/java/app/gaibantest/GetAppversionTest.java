/**
 * 
 */
package app.gaibantest;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.io.Charsets;

import com.google.common.io.Resources;

/**
 * @author sln
 * @date 2018年8月23日下午5:14:29
 * @Description: 泰安社保登陆，参数需要appversion
 */
public class GetAppversionTest {
	public static void main(String[] args) throws Exception {
//		String encryptedPwd = encrypted("1.1.11","370902199112251848");
		String encryptedPwd = encrypted("98533987291166723","1.1.11");
		System.out.println(encryptedPwd);
		
	}
	public static String encrypted(String appverson,String yhm) throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = readResource("login.js", Charsets.UTF_8);
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction("onLogin","",appverson,yhm);
		return data.toString(); 
	}
	
	public static String readResource(final String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
	}
}
