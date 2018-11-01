package app.unit;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

/**
 * @Description
 * @author sln
 * @date 2017年9月4日 下午5:43:54
 */
public class RSA {
	// public static void main(String[] args) throws Exception {
	// RSA rsa = new RSA();
	// String str = rsa.encryptedPhone("18092316191");
	// System.out.println(str);
	// }
	
	private  ScriptEngineManager scriptEngineManager ;
	private   ScriptEngine engine ;
	
	public RSA(){
		scriptEngineManager = new ScriptEngineManager();
		engine = scriptEngineManager.getEngineByName("nashorn");
	}

	public  String encryptedPassword(String password, String exponent, String modulus) throws Exception {

		// ScriptEngine javascript = new
		// ScriptEngineManager().getEngineByName("javascript");
		// ScriptEngine rhino = new
		// ScriptEngineManager().getEngineByName("rhino");

		System.out.println(exponent + "==========" + modulus);
		String path = readResource("telecom.js", Charsets.UTF_8);
		System.out.println("==============================================");
		// FileReader reader1 = new FileReader(path); // 执行指定脚本
		// ScriptEngine engine = new
		// ScriptEngineManager().getEngineByName("nashorn");
		// ScriptEngineManager factory = new ScriptEngineManager();
		// ScriptEngine engine = factory.getEngineByName("javascript");

		
		if (path == null || path.isEmpty()) {
			System.out.println("path == null ");
		} else {
			if (engine == null) {
				System.out.println("engine == null ");
				
			} else {
				engine.eval(path);
			}

		}

		Invocable invocable = (Invocable) engine;

		Object data = invocable.invokeFunction("encryptedString", password, exponent, modulus);
		return data.toString();

	}

	// public static void main(String[] args) throws Exception {
	// System.out.println(encryptedPassword("930311"));
	// }

	public  String readResource(final String fileName, Charset charset) throws IOException {
		return Resources.toString(Resources.getResource(fileName), charset);
	}
}
