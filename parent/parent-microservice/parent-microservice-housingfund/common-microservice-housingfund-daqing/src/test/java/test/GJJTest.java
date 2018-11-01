package test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageReader;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

import app.crawler.htmlparse.HousingDQParse;
import app.unit.RSA;

public class GJJTest {

	// private static final String LEN_MIN = "0";
	// private static final String TIME_ADD = "0";
	// private static final String STR_DEBUG = "a";
	// private static final String OCR_FILE_PATH = "/home/pdf";
	// private static String uuid = UUID.randomUUID().toString();

	static String url = "http://wt.dqgjj.cn:8080/wt-web/grlogin";

	public static void main(String[] args) throws Exception {
		login();
		// HousingDQParse.tranflowPDF_parse("/home/pdf/13c2f27e-cd94-422c-9d07-e9482b9d3328.pdf");
	}

	private static Object login() throws Exception {
		String username = "230623199303110444";

		String password = "930311";
		// *[@id="captcha_img"]
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		// String url2 = "http://wt.dqgjj.cn:8080/wt-web/captcha_chk";
		HtmlPage htmlpage = (HtmlPage) getHtml(url, webClient);
		HtmlImage valiCodeImg = htmlpage.getFirstByXPath("//*[@id='captcha_img']");
		ImageReader imageReader = valiCodeImg.getImageReader();
		BufferedImage bufferedImage = imageReader.read(0);

		JFrame f2 = new JFrame();
		JLabel l = new JLabel();
		l.setIcon(new ImageIcon(bufferedImage));
		f2.getContentPane().add(l);
		f2.setSize(100, 100);
		f2.setTitle("验证码");
		f2.setVisible(true);

		Document docx = Jsoup.parse(htmlpage.asXml());

		String exponent = docx.select("input#exponent").attr("value");

		String modulus = docx.select("input#modulus").attr("value");

		System.out.println(exponent + "=========" + modulus);

		String valicodeStr = JOptionPane.showInputDialog("请输入验证码：");

		RSA rsa = new RSA();

		String password_rsa = rsa.encryptedPassword(password.trim(), exponent, modulus);

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

		paramsList.add(new NameValuePair("force_and_dxyz", "1"));
		paramsList.add(new NameValuePair("grloginDxyz", "0"));
		paramsList.add(new NameValuePair("username", username.trim()));
		paramsList.add(new NameValuePair("password", password_rsa.trim()));
		paramsList.add(new NameValuePair("force", ""));
		paramsList.add(new NameValuePair("captcha", valicodeStr.trim()));
		webRequest.setRequestParameters(paramsList);

		webRequest.setAdditionalHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");

		webRequest.setAdditionalHeader("Host", "wt.dqgjj.cn:8080");
		webRequest.setAdditionalHeader("Origin", "http://wt.dqgjj.cn:8080");
		webRequest.setAdditionalHeader("Referer", "http://wt.dqgjj.cn:8080/wt-web/grlogin");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");

		Page page2 = gethtmlWebRequest(webClient, webRequest);

		System.out.println("==========================================================");
		System.out.println(page2.getWebResponse().getContentAsString());
		System.out.println("==========================================================");
		System.out.println("username = " + username);
		System.out.println("password = " + password);
		System.out.println("password_rsa = " + password_rsa);
		Document doc = Jsoup.parse(page2.getWebResponse().getContentAsString());
		String ip_text = doc.select("div#qzdl_div").select("p>span").text();
		String error_text = doc.select("div#yzm_tip,div#username_tip,div#pwd_tip").text();
		System.out.println("===========ip_text=========" + doc.select("div#qzdl_div").select("p>span"));
		System.out.println("===========ip_text=========" + ip_text);
		System.out.println("===========error_text=========" + doc.select("div#yzm_tip,div#username_tip,div#pwd_tip"));

		System.out.println("===========error_text=========" + error_text);
		if (error_text != null && !error_text.isEmpty()) {
			System.out.println("登录失败 错误信息为" + error_text);
			return null;
		}

		if (doc.toString().indexOf("强制登录可能会造成已登录用户未完成的操作无效") != -1) {
			valiCodeImg = htmlpage.getFirstByXPath("//*[@id='captcha_div']/div[1]/img");
			imageReader = valiCodeImg.getImageReader();
			bufferedImage = imageReader.read(0);

			f2 = new JFrame();
			l = new JLabel();
			l.setIcon(new ImageIcon(bufferedImage));
			f2.getContentPane().add(l);
			f2.setSize(100, 100);
			f2.setTitle("验证码");
			f2.setVisible(true);

			valicodeStr = JOptionPane.showInputDialog("请输入验证码第二次：");

			paramsList.add(new NameValuePair("force", "force"));
			paramsList.add(new NameValuePair("captcha", valicodeStr.trim()));
			webRequest.setRequestParameters(paramsList);

			page2 = gethtmlWebRequest(webClient, webRequest);
		}
		/*
		 * Set<Cookie> cookies = webClient.getCookieManager().getCookies(); for
		 * (Cookie cookie : cookies) { System.out.println(cookie.getName() +
		 * "===" + cookie.getValue()); } if (ip_text != null &&
		 * !ip_text.isEmpty()) {
		 * 
		 * webClient = loginForcedByClient(page2); } loginWelcom(webClient);
		 */
		// getpdf(webClient);
		return null;
	}

	public static void getUserInfo(WebClient webClient) throws Exception {
		String url = "http://wt.dqgjj.cn:8080/wt-web/jcr/jcrkhxxcx_mh.service";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		webClient.getOptions().setJavaScriptEnabled(false);

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

		paramsList.add(new NameValuePair("ffbm", "01"));
		paramsList.add(new NameValuePair("ywfl", "01"));
		paramsList.add(new NameValuePair("ywlb", "99"));
		paramsList.add(new NameValuePair("cxlx", "01"));
		webRequest.setRequestParameters(paramsList);

		Page page = gethtmlWebRequest(webClient, webRequest);
		System.out.println("===================getUserInfo========================");
		System.out.println(page.getWebResponse().getContentAsString());

		HousingDQParse.userinfo_parse(page.getWebResponse().getContentAsString());
		System.out.println("===================getUserInfo========================");
	}

	public static Page gethtmlWebRequest(WebClient webClient, WebRequest webRequest)
			throws FailingHttpStatusCodeException, IOException {
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		webClient.waitForBackgroundJavaScript(50000);
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}

	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;

	}

}
