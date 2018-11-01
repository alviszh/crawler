package TestUrl;

import java.io.InputStream;
import java.net.URL;

import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;

/**
 * @description:
 * @author: sln 
 * @date: 2018年3月29日 上午9:27:12 
 */
public class HtmlunitTest {
	public static void main(String[] args) throws Exception {
		String html="";
		String url="https://www.renshenet.org.cn/sionline/loginControler";//登录链接
		WebClient webClient = WebCrawler.getInstance().getWebClient(); 
		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest); 
		if(null!=page){
			int statusCode = page.getWebResponse().getStatusCode();
			if(200==statusCode){
				html = page.getWebResponse().getContentAsString();
			}
		}
		System.out.println("获取的登录响应页面是："+html);
		String md5Result = HelperUtils.getMd5Result(html);
		System.out.println("本次登录页面加密后的MD5结果是："+md5Result);
		Document doc = Jsoup.parse(html);
		
		//如下信息获取该登录页面相关的所有js的全路径
		Elements eles = doc.select("script[src]");
		for(Element jsElement:eles){
//			String absJsPath = jsElement.attr("abs:src");    //返回js的绝对路径
			String absJsPath = jsElement.attr("src");    //返回js的绝对路径
			System.out.println("返回的js绝对路径是："+absJsPath);
			URL url2 = new URL(absJsPath);     
  		    InputStream ins = url2.openStream();    
  		    String jsmd5 = DigestUtils.md5Hex(ins); 
  		    System.out.println(absJsPath+"-------jsmd5-------"+jsmd5);
		}
	}
}
