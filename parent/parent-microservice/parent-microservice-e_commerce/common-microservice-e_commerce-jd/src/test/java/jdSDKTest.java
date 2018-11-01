

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
public class jdSDKTest {
	// public static void main(String[] args) throws Exception {
	// RSA rsa = new RSA();
	// String str = rsa.encryptedPhone("18092316191");
	// System.out.println(str);
	// }
	
	private  static ScriptEngineManager scriptEngineManager ;
	private static   ScriptEngine engine ;
	

	public static  String encryptedPassword() throws Exception {

		scriptEngineManager = new ScriptEngineManager();
		engine = scriptEngineManager.getEngineByName("nashorn");
		String path = readResource("jd2.js", Charsets.UTF_8);
		System.out.println("==============================================");
		

		
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

		Object data = invocable.invokeFunction("","");
		return data.toString();

	}

	// public static void main(String[] args) throws Exception {
	// System.out.println(encryptedPassword("930311"));
	// }

	public static String readResource(final String fileName, Charset charset) throws IOException {
		return Resources.toString(Resources.getResource(fileName), charset);
	}
	
	public static void main(String[] args) throws Exception {
		encryptedPassword();
	}
}
