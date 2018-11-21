package app;

import java.net.MalformedURLException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;

public class Test {

	public static void main(String[] args) throws Exception {
		String url = "https://ipcrs.pbccrc.org.cn/page/login/loginreg.jsp";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
//		if (httpProxyRes != null) {
			ProxyConfig proxyConfig = webClient.getOptions().getProxyConfig();
//			System.err.println("代理："+httpProxyRes.getIp()+Integer.parseInt(httpProxyRes.getPort()));
			proxyConfig.setProxyHost("101.64.14.89"); 
			proxyConfig.setProxyPort(4528);
		try {
			//
			//        }
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage searchPage1 = webClient.getPage(webRequest);
			String html = searchPage1.getWebResponse().getContentAsString();
			System.err.println(html);
			if (html.contains("请输入登录密码")) {
				System.err.println("代理ip可用");
			} 
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("代理ip不可用");
		}
	}
}
