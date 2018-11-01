package app.service.common;



import java.io.IOException;
import java.nio.charset.Charset;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import app.commontracerlog.TracerLog;

/**
 * @Description
 * @author sln
 * @date 2017年9月4日 下午5:43:54
 */
@Component
public class RSA {
	@Autowired
	private TracerLog tracer;
//	public static void main(String[] args) throws Exception {
//		RSA rsa = new RSA();
//		String str = rsa.encryptedPhone("18092316191");
//		System.out.println(str);
//	}

	public String encryptedPhone(String phonenum) throws Exception{
		tracer.addTag("RSA.phonenum", "需要加密的手机号是："+phonenum);
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
//		ScriptEngine javascript = new ScriptEngineManager().getEngineByName("javascript");
//		ScriptEngine rhino = new ScriptEngineManager().getEngineByName("rhino");
		String path = this.readResource("telecom.js", Charsets.UTF_8);
		//System.out.println(path);
		//FileReader reader1 = new FileReader(path); // 执行指定脚本
		if(engine==null){
			tracer.addTag("engine is null", "需要加密的手机号是："+phonenum);
		}else{
			tracer.addTag("执行engine", "需要加密的手机号是："+phonenum);
			engine.eval(path); 
		}
		
	    Invocable invocable = (Invocable) engine;  
	    if(invocable==null){
	    	tracer.addTag("invocable is null", "需要加密的手机号是："+phonenum);
	    	return null; 
	    }else{
	    	tracer.addTag("执行invokeFunction", "需要加密的手机号是："+phonenum);
	    	Object data = invocable.invokeFunction("encryptedString",phonenum);
	    	tracer.addTag("执行invokeFunctiondata.toString()", data.toString());
	    	return data.toString(); 
	    }
	}
	
	public String readResource(final String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
	}
}

