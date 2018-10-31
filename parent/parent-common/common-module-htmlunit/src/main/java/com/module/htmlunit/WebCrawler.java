package com.module.htmlunit;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.module.domain.WebPageResponse;
import com.module.htmlunit.definition.UserAgent;
import com.module.htmlunit.definition.UtilDefinition;
import com.module.ocr.utils.OCRConnectUtils;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebCrawler {

	private static final Logger log = LoggerFactory.getLogger(WebCrawler.class);

	//private static WebClient webClient = null;

	private static WebCrawler instance;

	private static String alertMsg = "";

	public static final int DEFAULT_PAGE_TIME_OUT = 20000; // ms
	public static final int DEFAULT_JS_TIME_OUT = 50000;


	public synchronized static String getAlertMsg() {

		String msg = alertMsg;
		alertMsg = "";
		return msg;

	}

	public WebCrawler() {
		/*
		if (webClient == null) {
			webClient = getWebClient();
		}*/
	}

	public static WebCrawler getInstance() {
		if (instance == null) {
			synchronized (WebCrawler.class) {
				if (instance == null) {
					instance = new WebCrawler();
				}
			}
		}
		return instance;
	}

	public WebClient getWebClient() {
		//if (webClient == null) {
			WebClient webClient = new WebClient(BrowserVersion.CHROME);
			webClient.setRefreshHandler(new ThreadedRefreshHandler());
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.getOptions().setPrintContentOnFailingStatusCode(false);
			webClient.getOptions().setRedirectEnabled(true);
			webClient.getOptions().setTimeout(DEFAULT_PAGE_TIME_OUT); // 15->60
			webClient.waitForBackgroundJavaScript(DEFAULT_JS_TIME_OUT); // 5s
			webClient.setAjaxController(new NicelyResynchronizingAjaxController());
			webClient.getOptions().setUseInsecureSSL(true); //
/*			webClient.getCookieManager().setCookiesEnabled(true);//开启cookie管理
*/
			//webClient.setJavaScriptTimeout(5000);
			// webClient.getJavaScriptEngine().getContextFactory().enterContext().setOptimizationLevel(9);
			webClient.setAlertHandler(new AlertHandler() {
				@Override
				public void handleAlert(Page page, String message) {
					alertMsg = message;
				}
			});
			/*webClient.setJavaScriptErrorListener(new JavaScriptErrorListener() {
				@Override
				public void scriptException(HtmlPage htmlPage, ScriptException scriptException) {
					log.info(htmlPage.getUrl().toString() + "---scriptException----" + scriptException.getMessage());
				}

				@Override
				public void timeoutError(HtmlPage htmlPage, long allowedTime, long executionTime) {
					log.info(htmlPage.getUrl().toString() + "---timeoutError----" + allowedTime
							+ "-----executionTime----" + executionTime);
				}

				@Override
				public void malformedScriptURL(HtmlPage htmlPage, String url,
						MalformedURLException malformedURLException) {
					log.info(htmlPage.getUrl().toString() + "---malformedScriptURL----" + url + "-----executionTime----"
							+ malformedURLException.getMessage());
				}

				@Override
				public void loadScriptError(HtmlPage htmlPage, URL scriptUrl, Exception exception) {
					log.info(htmlPage.getUrl().toString() + "---loadScriptError----" + scriptUrl.toString()
							+ "-----executionTime----" + exception.getMessage());
				}
			});*/
		//}
		return webClient;
	}
	

	public WebClient getNewWebClient() {
		//webClient = null; 
		//if (webClient == null) {
			WebClient webClient = new WebClient(BrowserVersion.CHROME);
			webClient.setRefreshHandler(new ThreadedRefreshHandler());
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.getOptions().setPrintContentOnFailingStatusCode(false);
			webClient.getOptions().setRedirectEnabled(true);
			webClient.getOptions().setTimeout(DEFAULT_PAGE_TIME_OUT); // 15->60
			webClient.waitForBackgroundJavaScript(DEFAULT_JS_TIME_OUT); // 5s
			try{
				webClient.setAjaxController(new NicelyResynchronizingAjaxController());
			}catch(Exception e){
				log.info("setAjaxController exception:"+e.toString());
			}
			
			webClient.getOptions().setUseInsecureSSL(true); //
			webClient.getCookieManager().setCookiesEnabled(true);//开启cookie管理

			//webClient.setJavaScriptTimeout(5000);
			// webClient.getJavaScriptEngine().getContextFactory().enterContext().setOptimizationLevel(9);
			webClient.setAlertHandler(new AlertHandler() {
				@Override
				public void handleAlert(Page page, String message) {
					alertMsg = message;
				}
			});
			/*webClient.setJavaScriptErrorListener(new JavaScriptErrorListener() {
				@Override
				public void scriptException(HtmlPage htmlPage, ScriptException scriptException) {
					log.info(htmlPage.getUrl().toString() + "---scriptException----" + scriptException.getMessage());
				}

				@Override
				public void timeoutError(HtmlPage htmlPage, long allowedTime, long executionTime) {
					log.info(htmlPage.getUrl().toString() + "---timeoutError----" + allowedTime
							+ "-----executionTime----" + executionTime);
				}

				@Override
				public void malformedScriptURL(HtmlPage htmlPage, String url,
						MalformedURLException malformedURLException) {
					log.info(htmlPage.getUrl().toString() + "---malformedScriptURL----" + url + "-----executionTime----"
							+ malformedURLException.getMessage());
				}

				@Override
				public void loadScriptError(HtmlPage htmlPage, URL scriptUrl, Exception exception) {
					log.info(htmlPage.getUrl().toString() + "---loadScriptError----" + scriptUrl.toString()
							+ "-----executionTime----" + exception.getMessage());
				}
			});*/
		//}
		return webClient;
	}

	public synchronized WebPageResponse getPageInCookiesJsoup(String url, Map<String, String> cookies,
			Map<String, String> params) throws IOException {

		Response response;
		WebPageResponse webPageResponse = null;

		Connection conn = getConnection(url);
		if (cookies != null) {
			conn.cookies(cookies);
		}
		response = conn.method(Method.GET).execute();

		int iscodepage = response.body().toString().indexOf("近期异常访问较多，网站压力大，请输");
		// successfully responsed
		int num = 0;
		while (iscodepage != -1) {
			num++;
			if (num > 9) {
				break;
			}
			String codeurl = "http://epub.sipo.gov.cn/vci.jpg?" + Math.random();

			Response resultImageResponse = Jsoup.connect(codeurl).ignoreContentType(true).execute();
			Map<String, String> cookiesCode = resultImageResponse.cookies();

			String propFileName = "topology.properties";
			Properties properties = new Properties();
			try {
				InputStream inputStream = WebCrawler.class.getClassLoader().getResourceAsStream(propFileName);
				properties.load(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}

			String imageName = UUID.randomUUID() + ".jpg";

			FileOutputStream out = (new FileOutputStream(
					new java.io.File(properties.getProperty("nfs.filepath") + "/" + imageName)));

			out.write(resultImageResponse.bodyAsBytes()); // //
			out.close();
			String imageUrl = "http://" + properties.getProperty("nfs.nginx.server") + "/" + imageName;
			String verifycode = OCRConnectUtils.getVerifycode("/cjy/getVerifycode", imageUrl, "1005");

			/*FileOutputStream out = (new FileOutputStream(new java.io.File("D:/aaa.jpg")));

			out.write(resultImageResponse.bodyAsBytes()); // //
			// resultImageResponse.body() is where the image's contents are.
			out.close();

			System.out.println("=========请输入============");
			Scanner str = new Scanner(System.in);

			String verifycode = str.next();*/

			log.info("===========================================================================================");
			log.info("解析验证码：" + verifycode);
			String company = params.get("company");
			Map<String, String> canshu = SetMap(company, verifycode);
			Connection conn2 = getConnection("http://epub.sipo.gov.cn/patentoutline.parser");
			conn2.cookies(cookiesCode);
			response = conn2.method(Method.POST).data(canshu).execute();
			iscodepage = response.body().toString().indexOf("近期异常访问较多，网站压力大，请输");
			log.info("============解析结果为：" + iscodepage + "=======================");
		}
		log.info(response.body());

		webPageResponse = new WebPageResponse();

		String[] preDefinedCharset = new String[] { "utf-8", "big5-hkscs", "big5", "gbk", "gb2312" };
		String defaultCharset = preDefinedCharset[0];

		Elements metaTags = Jsoup.parse(response.body()).select("meta[http-equiv=Content-Type],meta[charset]");
		log.info(metaTags.toString());
		if (metaTags.size() > 0) {
			Element meta = metaTags.get(0);
			for (String cs : preDefinedCharset) {
				if (meta.hasAttr("content") && meta.attr("content").toLowerCase().indexOf("charset=" + cs) > 0
						|| meta.hasAttr("charset") && meta.attr("charset").toLowerCase().indexOf(cs) > 0) {
					defaultCharset = cs;
					break;
				}
			}
		}
		System.out.println("defaultCharset-----------" + defaultCharset);
		webPageResponse.setHtml(new String(response.bodyAsBytes(), defaultCharset));
		webPageResponse.setUnit(UtilDefinition.JSOUP);
		webPageResponse.setHttpStatusCode(response.statusCode());
		webPageResponse.setUrl(response.url().toString());

		return webPageResponse;
	}

	public synchronized WebPageResponse getPageInCookiesHtmlunit(String url, List<Cookie> cookies) throws Exception {
		log.info("getPageInlogin: {}", url);
		WebClient wc = getWebClient();
		wc = insertCookie(wc, cookies);
		log.info("=============  注入cookies   =============");
		WebPageResponse response = getPage(wc, url);
		return response;
	}

	public synchronized WebPageResponse getPage(WebClient webClient, String url) throws Exception {
		URL link = new URL(url);
		return getPage(webClient, link);
	}

	public WebClient insertCookie(WebClient webClient, List<Cookie> cookies) {
		for (Cookie e : cookies) {
			webClient.getCookieManager().addCookie(e);
		}
		return webClient;
	}

	public synchronized WebPageResponse getPage(WebClient webClient, URL url) throws Exception {
		log.info("Using Htmlunit to Send GET Request: {}", url);
		WebPageResponse wr = new WebPageResponse();
		HtmlPage page = null;
		String result = null;
		WebResponse response = null;
		try {
			page = webClient.getPage(url);
			response = page.getWebResponse();
			result = response.getContentAsString();
			int httpStatusCode = response.getStatusCode();
			wr.setHtml(result);
			wr.setHttpStatusCode(httpStatusCode);
			wr.setUrl(page.getUrl() + "");
			wr.setCookies(webClient.getCookieManager().getCookies());
			wr.setUnit(UtilDefinition.HTMLUNIT);
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} finally {
			if (webClient != null) {
				// webClient.getCache().clear();
				// webClient.getCookieManager().clearCookies();
				//webClient.close();closeAllWindows();
			}
		}
		return wr;
	}

	public synchronized WebPageResponse getPage(String url) throws Exception {
		return getPage(getWebClient(), url);

	}

	public WebPageResponse getPageJsoup(String url, Map<String, String> params) throws IOException {
		log.info("Using Jsoup to Send GET Request: {}", url);
		return getPageInCookiesJsoup(url, null, params);
	}

	private Connection getConnection(String url) {
		return Jsoup.connect(url).followRedirects(true).ignoreContentType(false).ignoreHttpErrors(false)
				.maxBodySize(2000000).timeout(30000).userAgent(UserAgent.getRandomUserAgent().toString());
	}

	private Map<String, String> SetMap(String company, String code) {
		Map<String, String> canshu = new HashMap<String, String>();
		canshu.put("vct", code);
		canshu.put("Submit", "继续");
		canshu.put("Submit", "继续");
		canshu.put("numIg", "");
		canshu.put("numDgc", "");
		canshu.put("numIgc", "");
		canshu.put("numIp", "0");
		canshu.put("pageSize", "3");
		canshu.put("strWhere", "PA='%" + company + "%'");
		canshu.put("numDg", "");
		canshu.put("numIpc", "");
		canshu.put("numUgc", "");
		canshu.put("numUgd", "");
		canshu.put("strLicenseCode", "");
		canshu.put("numIgd", "");
		canshu.put("showType", "1");
		canshu.put("numSortMethod", "");
		canshu.put("strSources", "");
		canshu.put("numUg", "");
		canshu.put("pageNow", "1");

		return canshu;
	}

}
