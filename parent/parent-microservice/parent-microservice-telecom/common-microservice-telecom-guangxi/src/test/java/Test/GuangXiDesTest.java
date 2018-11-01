/**
 * 
 */
package Test;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.module.htmlunit.WebCrawler;

/**
 * @author sln
 * @date 2018年8月14日下午4:13:36
 * @Description: 广西登陆
 */
public class GuangXiDesTest {
	public static void main(String[] args) throws Exception {
			//通过如下连接获取参数
			String url="http://gx.189.cn/chaxun/iframe/user_center.jsp";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			if(null!=hPage){
				String html=hPage.asXml();
				Document doc = Jsoup.parse(html);
				String codeId = doc.getElementById("IDNum").text();
				String key1=html.substring(html.indexOf("key1=")+6, html.indexOf("key1=")+12);
				String key2=html.substring(html.indexOf("key2=")+6, html.indexOf("key2=")+12);
				String key3=html.substring(html.indexOf("key3=")+6, html.indexOf("key3=")+12);
				//加密登陆密码
				String logon_passwd = strEnc("741258",key1,key2,key3);
			
				url="http://gx.189.cn/public/login.jsp";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				String requestBody="LOGIN_TYPE=21&RAND_TYPE=001"
						+ "&codeId="+codeId+""
						+ "&AREA_CODE="
						+ "&logon_name=18172055939"
						+ "&password_type_ra=1"
						+ "&logon_passwd=__"+logon_passwd+""
						+ "&logon_valid=%E8%AF%B7%E8%BE%93%E5%85%A5%E9%AA%8C%E8%AF%81%E7%A0%81";
				webRequest.setRequestBody(requestBody);
				Page page=webClient.getPage(webRequest);
				if(null!=page){
					html=page.getWebResponse().getContentAsString();
					Page page2 = webClient.getPage("http://gx.189.cn/chaxun/iframe/user_center.jsp");
					System.out.println("验证登陆信息返回的页面是："+page2.getWebResponse().getContentAsString());
					
					String string = page2.getWebResponse().getContentAsString();
					int indexOf = string.indexOf("PRODTYPE");
					String substring = string.substring(indexOf);
					String substring2 = substring.substring(10,17);
					System.out.println(substring2);
				}
			}
	}
	
	
	
	public static String splitData(String str, String strStart, String strEnd) {  
		int i = str.indexOf(strStart); 
		int j = str.indexOf(strEnd, i); 
		String tempStr=str.substring(i+strStart.length(), j); 
        return tempStr;  
	}
	public static String strEnc(String str,String key1,String key2,String key3) throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = readResource("des.js", Charsets.UTF_8);
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction("strEnc",str,key1,key2,key3);
		return data.toString(); 
	}
	
	public static String readResource(final String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
    }
}
