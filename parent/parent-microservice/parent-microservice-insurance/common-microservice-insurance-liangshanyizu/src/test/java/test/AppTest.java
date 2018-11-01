package test;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONObject;

public class AppTest {
	public static void main(String[] args) {
		try {
			WebClient webClient = WebCrawler.getInstance().getWebClient();
			// getRSAKey
			String loginurl2 = "http://118.122.8.171:8003/lsui/getRSAKey.jspx";
			WebRequest webRequest = new WebRequest(new URL(loginurl2), HttpMethod.POST);
			Page page00 = webClient.getPage(webRequest);
			String page000 = page00.getWebResponse().getContentAsString();

			JSONObject json = JSONObject.fromObject(page000);
			String success = json.getString("success").trim();
			if ("true".equals(success)) {
				System.out.println("获取加密参数成功！");
				String fieldData = json.getString("fieldData").trim();
				JSONObject jsonfieldData = JSONObject.fromObject(fieldData);
				// 加密需要的数据
				String publicKeyModulus = jsonfieldData.getString("publicKeyModulus").trim();
				// 9f3ea10fbcb6f2fae83d5c78b2bbe65ca5a777327233201209ffa9e8628965571a9895b180dc703be56fbfcd24df4af3cae14ae699615b70fd5fe630db684f15c59e833e4caba63016bb44b5e38917504f8a6991c9988b404fa2ff7bbe4f80b46355db1abe56bf9a0636ab71a1bac52cf3b70cb9178796e6ac1633b9323f8f7b
				System.out.println("publicKeyModulus:" + publicKeyModulus);
				String publicKeyExponent = jsonfieldData.getString("publicKeyExponent").trim();
				// 10001
				System.out.println("publicKeyExponent:" + publicKeyExponent);
				String RSAkey = getKeyPair(publicKeyExponent,"",publicKeyModulus);
				System.out.println("RSAkey:"+RSAkey);
				//key
				String[] data={"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
				String key="";
				for(int i=0;i<16;i++){
					int id=(int) Math.ceil(Math.random()*35);
					key+=data[id];
				}
				//登录入参
				String netysku = encryptedString(RSAkey,key);
				System.out.println("netysku:"+netysku);
			} else {
				System.out.println("获取加密参数失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String encryptedString(String RSAkey,String key) throws Exception {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = readResource("zaozhuang.js", Charsets.UTF_8);
		engine.eval(path);
		final Invocable invocable = (Invocable) engine;
		Object data = invocable.invokeFunction("encryptedString", RSAkey,key);
		return data.toString();
	}
	public static String getKeyPair(String publicKeyExponent,String pub,String publicKeyModulus) throws Exception {
		 ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");  
	        //加载并执行javascript脚本  
	        engine.eval(new FileReader("src/main/resources/zaozhuang.js"));  
	          
	        Invocable invocable = (Invocable) engine;  
	        //调用javascript函数  
	        String result = invocable.invokeFunction("getKeyPair", publicKeyExponent,"",publicKeyModulus).toString();  
	        return result;
	}

	public static String readResource(final String fileName, Charset charset) throws IOException {
		return Resources.toString(Resources.getResource(fileName), charset);
	}
}
