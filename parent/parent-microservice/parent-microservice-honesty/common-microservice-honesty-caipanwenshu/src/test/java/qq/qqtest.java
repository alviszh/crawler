package qq;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class qqtest {

		
		public static void main(String[] args) throws FailingHttpStatusCodeException, IOException {
			
//			double random = Math.random();
//			System.out.println(random);
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String url1 = "http://pccz.court.gov.cn/pcajxxw/pctzr/tzrdh?lx=1";
			Page gethtmlPost = getHtml(url1, webClient);
			String statusMessage2 = gethtmlPost.getWebResponse().getStatusMessage();
			System.out.println(statusMessage2);
			
			String url2 = "http://pccz.court.gov.cn/pcajxxw/pctzr/tzr?sjlx=1";
			Page page2 = gethtmlPost(webClient, null, url2);
			String contentAsString = page2.getWebResponse().getContentAsString();
			System.out.println(contentAsString);

			webClient.addRequestHeader("Accept", "text/html, */*; q=0.01");
			webClient.addRequestHeader("Host", "pccz.court.gov.cn");
			webClient.addRequestHeader("Origin", "http://pccz.court.gov.cn");
			webClient.addRequestHeader("Referer", "http://pccz.court.gov.cn/pcajxxw/pctzr/tzrdh?lx=1");
			webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");
			String url = "http://pccz.court.gov.cn/pcajxxw/pctzr/tzrlb?"
					+ "param={"+"zcd"+":[999],%22hy%22:[999],%22gsxz%22:[999],%22zczb%22:[999],%22zgsl%22:[999]}"
							+ "&pageNum=2"
							+ "&sjlx=1"
							+ "&qymc=";
			
//			List<NameValuePair> paramsList2 = new ArrayList<>();
//			paramsList2.add(new NameValuePair("param", "{%22zcd%22:[999],%22hy%22:[999],%22gsxz%22:[999],%22zczb%22:[999],%22zgsl%22:[999]}"));
//			paramsList2.add(new NameValuePair("pageNum", "2"));
//			paramsList2.add(new NameValuePair("sjlx", "1"));
//			paramsList2.add(new NameValuePair("qymc", ""));
			
			Page page = gethtmlPost(webClient, null, url);
			String statusMessage = page.getWebResponse().getStatusMessage();
			System.out.println(statusMessage);
		}
		public static Page getHtml(String url, WebClient webClient) {
			WebRequest webRequest;
			try {
				webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

				// webClient.get
				webClient.getOptions().setJavaScriptEnabled(false);
				webRequest = new WebRequest(new URL(url), HttpMethod.GET);
				Page searchPage = webClient.getPage(webRequest);
				return searchPage;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}}
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
	}
