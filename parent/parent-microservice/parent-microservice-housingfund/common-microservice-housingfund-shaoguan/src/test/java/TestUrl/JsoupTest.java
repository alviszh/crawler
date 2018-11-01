package TestUrl;

import java.io.InputStream;
import java.net.URL;

import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @description:
 * @author: sln 
 * @date: 2018年3月29日 上午9:27:35 
 */
//绝大部分网站用这个方法就可以，该测试类是首选测试类，如果该测试类不行，就用htmlunit的方法
public class JsoupTest {
	public static void main(String[] args) throws Exception {
		String loginUrl="http://www.fj12333.gov.cn:268/fwpt/";
		
		URL url = new URL(loginUrl);   
		Document doc = Jsoup.parse(url, 10*1000);    
		String html = doc.html();
		System.out.println("获取的登录响应页面是："+html);
		String md5Result = HelperUtils.getMd5Result(html);
		System.out.println("本次登录页面加密后的MD5结果是："+md5Result);
		//如下信息获取该登录页面相关的所有js的全路径
		Elements eles = doc.select("script[src]");
		for(Element jsElement:eles){
			String absJsPath = jsElement.attr("abs:src");    //返回js的绝对路径
			System.out.println("返回的js绝对路径是："+absJsPath);
			URL url2 = new URL(absJsPath);     
  		    InputStream ins = url2.openStream();    
  		    String jsmd5 = DigestUtils.md5Hex(ins); 
  		    System.out.println(absJsPath+"-------jsmd5-------"+jsmd5);
		}
	}
}
