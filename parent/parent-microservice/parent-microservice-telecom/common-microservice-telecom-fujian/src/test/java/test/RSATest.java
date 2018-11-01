package test;

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
		String str = rsaTest.encryptedPhone("054246");
		System.out.println(str);
	}

	public String encryptedPhone(String phonenum) throws Exception {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = this.readResource("telecom.js", Charsets.UTF_8);
		engine.eval(path);
		final Invocable invocable = (Invocable) engine;
		Object data = invocable.invokeFunction("encryptedString", phonenum);
		return data.toString();
	}

	public String readResource(final String fileName, Charset charset) throws IOException {
		return Resources.toString(Resources.getResource(fileName), charset);
	}

}
