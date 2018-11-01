package Test;

import java.net.URL;
import java.net.URLDecoder;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;

public class Testdate {

	public static void main(String[] args) throws Exception {
		String url="http://gx.189.cn/chaxun/iframe/user_center.jsp";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Page page = webClient.getPage(url);
		
		String url1="http://gx.189.cn/public/login.jsp";
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
		webRequest.setRequestBody("LOGIN_TYPE=21&RAND_TYPE=001&AREA_CODE=&logon_name=18172055939&password_type_ra=1&logon_passwd=__292F671761606C896E4DE63361A587A9&logon_valid=%E8%AF%B7%E8%BE%93%E5%85%A5%E9%AA%8C%E8%AF%81%E7%A0%81");
		Page page2 = webClient.getPage(webRequest);
//		System.out.println(page2.getWebResponse().getContentAsString());
		
		webRequest = new WebRequest(new URL("http://gx.189.cn/chaxun/iframe/user_center.jsp"), HttpMethod.GET);
		Page page3 = webClient.getPage(webRequest);
		System.out.println(page3.getWebResponse().getContentAsString());
	}
}
