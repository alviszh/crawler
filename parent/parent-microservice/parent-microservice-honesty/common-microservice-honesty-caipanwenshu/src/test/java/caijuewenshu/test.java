package caijuewenshu;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;


public class test {

	public static void main(String[] args) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		//参数1：guid
		String url7 = "http://wenshu.court.gov.cn/List/List?"
				+ "sorttype=1&conditions=searchWord+1++刑事案件+案件类型:刑事案件";
		getHtml(url7, webClient);
		String guid = encryptedPhone("guid.js","ref","");
		System.out.println(guid);
		
		
		//[vjkl5=a68b89bff958d6d016018edd8e2217dc46e9a256;domain=wenshu.court.gov.cn;path=/;expires=Thu Jul 12 17:22:30 CST 2018, _gscu_2116842793=31383763lqbqtf38;domain=.wenshu.court.gov.cn;path=/;expires=Sat Jul 11 16:22:43 CST 2020, _gscs_2116842793=31383763cidizm38|pv:1;domain=.wenshu.court.gov.cn;path=/;expires=Thu Jul 12 16:52:43 CST 2018, _gscbrs_2116842793=1;domain=.wenshu.court.gov.cn;path=/, HMACCOUNT=3410EF62FB1FF444;domain=.hm.baidu.com;path=/;expires=Mon Jan 18 08:00:00 CST 2038, Hm_lvt_d2caefee2de09b8a6ea438d74fd98db2=1531383767;domain=.wenshu.court.gov.cn;path=/;expires=Fri Jul 12 16:22:46 CST 2019, Hm_lpvt_d2caefee2de09b8a6ea438d74fd98db2=1531383767;domain=.wenshu.court.gov.cn;path=/, GRIDSUMID=f473067ee4b54560a3cba9a3c792d01e;domain=.webdissector.com;path=/;expires=Fri Jan 01 07:55:55 CST 2038, ARRAffinity=8e3ec074eb50841a9dcc0927f6f82e829fedc7da5c3afaf4b80242336bd18df1;domain=.www.webdissector.com;path=/;httpOnly, GRIDSUMID=3ca0652ab5c7996a98fa95ade2bf5f37;domain=.gridsumdissector.com;path=/;expires=Thu Dec 31 00:00:00 CST 2037]
		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		//参数2：vl5x
		String vjkl5 = null;
		for (Cookie cookie : cookies) {
			if(cookie.getName().equals("vjkl5")){
				vjkl5 = cookie.getValue();
				break;
			}
		}
		
		String vl5x = encryptedPhone("vl5x.js","getKey",vjkl5);
		System.out.println(vl5x);
		
		//参数3：number
		String url2 = "http://wenshu.court.gov.cn/ValiCode/GetCode?"
				+ "guid="+guid;
		webClient.addRequestHeader("Accept", "*/*");
		webClient.addRequestHeader("Host", "wenshu.court.gov.cn");
		webClient.addRequestHeader("Origin", "http://wenshu.court.gov.cn");
		webClient.addRequestHeader("Referer", "http://wenshu.court.gov.cn/List/List?sorttype=1&conditions=searchWord+1++%E5%88%91%E4%BA%8B%E6%A1%88%E4%BB%B6+%E6%A1%88%E4%BB%B6%E7%B1%BB%E5%9E%8B:%E5%88%91%E4%BA%8B%E6%A1%88%E4%BB%B6");
		webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");
		Page page2 = gethtmlPost(webClient, null, url2);
		String number = page2.getWebResponse().getContentAsString();
		
	
		
		
		
		String url = "http://wenshu.court.gov.cn/List/ListContent?"
				+ "Param=%E6%A1%88%E4%BB%B6%E7%B1%BB%E5%9E%8B%3A%E5%88%91%E4%BA%8B%E6%A1%88%E4%BB%B6"
				+ "&Index=1"
				+ "&Page=20"
				+ "&Order=%E6%B3%95%E9%99%A2%E5%B1%82%E7%BA%A7"
				+ "&Direction=asc"
				+ "&vl5x="+vl5x
				+ "&number="+number
				+ "&guid="+guid;
		webClient.addRequestHeader("Accept", "*/*");
		webClient.addRequestHeader("Host", "wenshu.court.gov.cn");
		webClient.addRequestHeader("Origin", "http://wenshu.court.gov.cn");
		webClient.addRequestHeader("Referer", "http://wenshu.court.gov.cn/List/List?sorttype=1&conditions=searchWord+1++%E5%88%91%E4%BA%8B%E6%A1%88%E4%BB%B6+%E6%A1%88%E4%BB%B6%E7%B1%BB%E5%9E%8B:%E5%88%91%E4%BA%8B%E6%A1%88%E4%BB%B6");
		webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");
		
		Page page = gethtmlPost(webClient, null, url);
		String html = page.getWebResponse().getContentAsString();
		System.out.println(html);
		String replace = html.replace("\\", "");
		System.out.println(replace);
		String substring = replace.substring(2, replace.length()-2);
		System.out.println(substring);
		
		
	}
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	
	public static String encryptedPhone(String jsname,String wayname,String vjkl5) throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = readResource(jsname, Charsets.UTF_8);
		//System.out.println(path);
		//FileReader reader1 = new FileReader(path); // 执行指定脚本
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction(wayname,vjkl5);
		return data.toString(); 
	}
	
	public static String readResource(final String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
}
}
