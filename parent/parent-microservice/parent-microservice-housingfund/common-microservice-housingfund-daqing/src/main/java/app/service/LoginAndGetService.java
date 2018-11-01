package app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.daqing.HousingDaQingUserinfo;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamHousing;
import app.service.common.HousingBasicService;
import app.service.common.LoginAndGetCommon;
import app.unit.RSA;

/**
 * 
 * 项目名称：common-microservice-housingfund-daqing 类名称：LoginAndGetService 类描述：
 * 创建人：hyx 创建时间：2017年11月8日 下午3:24:08
 * 
 * @version
 */
@Component
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.daqing")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.daqing")
public class LoginAndGetService extends HousingBasicService {

	public static final Logger log = LoggerFactory.getLogger(ChaoJiYingOcrService.class);

	private String OCR_FILE_PATH = "/home/pdf";
	private String uuid = UUID.randomUUID().toString();

	/**
	 * 
	 * 项目名称：common-microservice-housingfund-daqing 所属包名：app.service 类描述： 创建人：hyx
	 * 创建时间：2017年11月8日
	 * 
	 * @version 1 返回值 WebParamHousing
	 */
	public WebParamHousing<?> login(MessageLoginForHousing messageLoginForHousing, int num) throws Exception {
		WebParamHousing<?> webParamHousing = new WebParamHousing<>();

		String url = "http://wt.dqgjj.cn:8080/wt-web/grlogin";
		String username = messageLoginForHousing.getNum();

		String password = messageLoginForHousing.getPassword();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage htmlpage = (HtmlPage) LoginAndGetCommon.getHtml(url, webClient);
		HtmlImage valiCodeImg = htmlpage.getFirstByXPath("//*[@id='captcha_img']");

		String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "5000");

		System.out.println(valicodeStr);

		Document docx = Jsoup.parse(htmlpage.asXml());

		String exponent = docx.select("input#exponent").attr("value");

		String modulus = docx.select("input#modulus").attr("value");
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

		Page page2 = LoginAndGetCommon.gethtmlWebRequest(webClient, webRequest);
		System.out.println("username = " + username);
		System.out.println("password = " + password);
		System.out.println("password_rsa = " + password_rsa);
		Document doc = Jsoup.parse(page2.getWebResponse().getContentAsString());
		String ip_text = doc.select("div#qzdl_div").select("p>span").text();
		String error_text = doc.select("div#yzm_tip,div#username_tip,div#pwd_tip").text();
		System.out.println("===========ip_text=========" + doc.select("div#qzdl_div").select("p>span"));
		System.out.println("===========ip_text=========" + ip_text);
		System.out.println("===========error_text=========" + doc.select("div#yzm_tip,div#username_tip,div#pwd_tip"));
		if (error_text != null && !error_text.isEmpty()) {
			System.out.println("登录失败 错误信息为" + error_text);
			if (error_text.contains("密码格式不正确")) {
				num++;
				if (num < 3) {
					return login(messageLoginForHousing, num);
				}
			}
			webParamHousing.setErrormessage(error_text);
			return webParamHousing;
		}
		if (ip_text != null && !ip_text.isEmpty()) {
			webParamHousing = loginForcedByClient(page2, webParamHousing);
		}

		if (webParamHousing.getErrormessage() != null) {
			if (webParamHousing.getErrormessage().indexOf("强制登录可能会造成已登录用户未完成的操作无效") != -1) {
				page2 = LoginAndGetCommon.gethtmlWebRequest(webClient, webRequest);
				webParamHousing = loginForcedByClient(page2, webParamHousing);
				// webParamHousing.setErrormessage(null);
			} else {
				return webParamHousing;
			}

		}
		webParamHousing.setWebClient(webClient);
		webParamHousing = loginWelcom(webParamHousing);
		// getpdf(webClient);
		webParamHousing.setWebClient(webClient);
		return webParamHousing;
	}

	/**
	 * 
	 * 项目名称：common-microservice-housingfund-daqing 所属包名：app.service 类描述： 创建人：hyx
	 * 创建时间：2017年11月8日
	 * 
	 * @version 1 返回值 WebParamHousing
	 */
	private WebParamHousing<?> loginWelcom(WebParamHousing<?> webParamHousing) throws Exception {
		String url = "http://wt.dqgjj.cn:8080/wt-web/home?logintype=1";
		WebClient webClient = webParamHousing.getWebClient();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		webClient.getOptions().setJavaScriptEnabled(false);
		LoginAndGetCommon.gethtmlWebRequest(webClient, webRequest);
		webParamHousing.setWebClient(webClient);
		return webParamHousing;
	}

	/**
	 * 
	 * 项目名称：common-microservice-housingfund-daqing 所属包名：app.service 类描述： 创建人：hyx
	 * 创建时间：2017年11月8日
	 * 
	 * @version 1 返回值 WebParamHousing
	 */
	private WebParamHousing<?> loginForcedByClient(Page page, WebParamHousing<?> webParamHousing) throws Exception {
		HtmlPage htmlpage = (HtmlPage) page;
		// String url = "http://wt.dqgjj.cn:8080/wt-web/captcha_chk";

		HtmlImage valiCodeImg = htmlpage.getFirstByXPath("/html/body/div[1]/div[2]/div/div/div/div[2]/div/div[1]/img");
		String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "5000");

		HtmlTextInput forcedtext = (HtmlTextInput) htmlpage.getFirstByXPath("//*[@id='force_captcha']");
		forcedtext.setText(valicodeStr);
		HtmlElement button = (HtmlElement) htmlpage.getFirstByXPath("//*[@id='qzdl']");

		HtmlPage htmlpage2 = button.click();
		System.out.println("=======================================");
		System.out.println(htmlpage2.asXml());
		System.out.println("=======================================");
		if (htmlpage2.asXml().indexOf("强制登录可能会造成已登录用户未完成的操作无效") != -1) {
			webParamHousing.setErrormessage("强制登录可能会造成已登录用户未完成的操作无效");
			return webParamHousing;
		}
		webParamHousing.setErrormessage(null);
		webParamHousing.setWebClient(htmlpage2.getWebClient());
		return webParamHousing;
	}

	/**
	 * 
	 * 项目名称：common-microservice-housingfund-daqing 所属包名：app.service 类描述：
	 * 获取用户个人信息 创建人：hyx 创建时间：2017年11月9日
	 * 
	 * @version 1 返回值 WebParamHousing
	 */
	public WebParamHousing<?> getUserInfo(WebParamHousing<?> webParamHousing, WebClient webClient) throws Exception {
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

		Page page = LoginAndGetCommon.gethtmlWebRequest(webClient, webRequest);
		System.out.println(page.getWebResponse().getContentAsString());
		webParamHousing.setPage(page);
		return webParamHousing;
	}

	/**
	 * 
	 * 项目名称：common-microservice-housingfund-daqing 所属包名：app.service 类描述：
	 * 下载五年流水的pdf 创建人：hyx 创建时间：2017年11月9日
	 * 
	 * @version 1 返回值 WebParamHousing
	 */
	public WebParamHousing<?> getTranFlowByPDF(WebParamHousing<?> webParamHousing, WebClient webClient,
			HousingDaQingUserinfo housingDaQingUserinfo) throws Exception {
		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		LocalDate enddate = LocalDate.now();
		LocalDate startdate = enddate.plusYears(-5);

		String enddate_string = (enddate + "").replaceAll("-", "/");
		String startdate_string = (startdate + "").replaceAll("-", "/");

		String url = "http://wt.dqgjj.cn:8080/wt-web/jcr/jcrxxcx_zhtzmxcx_prt.service?" + "ksrq=" + startdate_string
				+ "&jsrq=" + enddate_string + "&grxx=" + housingDaQingUserinfo.getGrzh().trim() + "&dwzh="
				+ housingDaQingUserinfo.getDwzh().trim() + "&ffbm=01&ywfl=01&ywlb=99&blqd=wt_02";
		tracer.addTag("下载pdf耗时的url", url);
		System.out.println("===================getpdf========================");
		long starttime = System.currentTimeMillis();
		Map<String, String> cookieMap = new HashMap<String, String>();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie.getValue());
			}
		}

		Connection con = Jsoup.connect(url).header("Content-Type", "application/pdf");

		String imgagePath = null;
		try {
			Response response = con.cookies(cookieMap).ignoreContentType(true).execute();
			File codeImageFile = getImageLocalPath();

			imgagePath = codeImageFile.getAbsolutePath();
			FileOutputStream out = (new FileOutputStream(new java.io.File(imgagePath)));

			out.write(response.bodyAsBytes());
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		long endtime = System.currentTimeMillis();
		tracer.addTag("下载pdf耗时", "下载pdf耗时" + (endtime - starttime) + "ms");
		System.out.println("下载pdf耗时" + (endtime - starttime) + "ms");
		webParamHousing.setUrl(imgagePath);
		System.out.println("===================getpdf========================");
		webParamHousing.setUrl(imgagePath);
		return webParamHousing;

	}

	/**
	 * @Description: 本地路径
	 * @return File
	 */
	public File getImageLocalPath() {

		File parentDirFile = new File(OCR_FILE_PATH);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //

		if (!parentDirFile.exists()) {
			parentDirFile.mkdirs();
		}

		String imageName = uuid + ".pdf";
		File codeImageFile = new File(OCR_FILE_PATH + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true); //

		return codeImageFile;

	}

}
