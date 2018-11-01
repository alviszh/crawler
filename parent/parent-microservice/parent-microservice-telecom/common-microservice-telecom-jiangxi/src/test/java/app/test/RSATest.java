package app.test;

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
		String str = rsaTest.encryptedPhone("111111","010001","009eccbc4005f8e7fe7b9dd45bf050fc652f6e4af19443cdb8dfa64c93ae67cb76f58fd34b0e15ca779922986871ffefdff60cee793aedb97d6a14afbf4e9741c17ae42d3dabf76e796f7b4750b7b104b6836d3c16271d9d76d84f32aa48965bc377e242c28606187ee63f6e9d040c1029b2348439e1cafc4a5f4829436138c275");
		System.out.println(str);
	}

	public String encryptedPhone(String phonenum,String exponent,String modulus) throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = this.readResource("telecomJiangxi.js", Charsets.UTF_8);
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction("encryptedString",phonenum,exponent,modulus);
		return data.toString(); 
	}
	
	public String readResource(final String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
}

}
