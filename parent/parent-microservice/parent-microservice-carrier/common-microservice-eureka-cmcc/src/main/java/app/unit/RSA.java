package app.unit;



import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

/**
 * @Description
 * @author zz
 * @date 2017年9月4日 下午5:43:54
 */
@Component
public class RSA {
	
//	@Autowired
//	private Tracer tracer;
	
	public static void main(String[] args) throws Exception {
		RSA rsa = new RSA();
		String str = rsa.encryptedPwd("001314");
		System.out.println(str);
	}

	public String encryptedPwd(String password) throws Exception{
		System.out.println(("RSA.pwd"+ "需要加密的密码是："+password));
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
//		ScriptEngine javascript = new ScriptEngineManager().getEngineByName("javascript");
//		ScriptEngine rhino = new ScriptEngineManager().getEngineByName("rhino");
//		String encrypted = this.readResource("jsencrypt.js", Charsets.UTF_8);
		String path = this.readResource("encrypt.js", Charsets.UTF_8);
		System.out.println("RSA.encryptedPhone.path"+ "  加密类中的path是："+path);
		//System.out.println(path);
		//FileReader reader1 = new FileReader(path); // 执行指定脚本
		if(engine==null){
			System.out.println("engine is null"+" 需要加密的密码是："+password);
		}else{
			System.out.println("执行engine"+" 需要加密的密码是："+password);
			engine.eval(path); 
		}
		
	    Invocable invocable = (Invocable) engine;  
	    if(invocable==null){
	    	System.out.println("invocable is null"+" 需要加密的密码是："+password);
	    	return null; 
	    }else{
	    	System.out.println("执行invokeFunction"+" 需要加密的密码是："+password);
	    	Object data = invocable.invokeFunction("encrypt",password);
	    	System.out.println("执行invokeFunctiondata.toString()  "+ data.toString());
	    	return data.toString(); 
	    }
	}
	
	public String readResource(final String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
	}
	
	
}

