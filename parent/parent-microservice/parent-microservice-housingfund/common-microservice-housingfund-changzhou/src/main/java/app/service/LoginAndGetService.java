package app.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

import app.bean.LoginRootBean;
import app.bean.WebParamHousing;
import app.htmlparse.HousingCZParse;
import app.service.common.HousingBasicService;
import app.service.common.LoginAndGetCommon;

@Component
@Service
@EnableAsync
public class LoginAndGetService extends HousingBasicService {

	// 根据身份证登录
	public WebParamHousing<?> login(String username, String password) throws Exception {
		WebParamHousing<?> webParamHousing = new WebParamHousing<>();
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		String url = "http://www.czgjj.com/hfmis_wt/login";

		HtmlPage htmlPage = (HtmlPage) LoginAndGetCommon.getHtml(url, webClient);

		webClient = htmlPage.getWebClient();

		HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='captcha_img1']");
		String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "5000");

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

		webRequest.setAdditionalHeader("Accept", "*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		webRequest.setAdditionalHeader("Host", "www.czgjj.com");
		webRequest.setAdditionalHeader("Referer", "http://www.czgjj.com/hfmis_wt/login");
		webRequest.setAdditionalHeader("Origin", "http://www.czgjj.com");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");

		paramsList.add(new NameValuePair("username", username.trim()));
		paramsList.add(new NameValuePair("password", password.trim()));
		paramsList.add(new NameValuePair("captcha", valicodeStr.trim()));
		paramsList.add(new NameValuePair("captcha", ""));
		paramsList.add(new NameValuePair("captcha", ""));
		paramsList.add(new NameValuePair("usertype", "2"));
		paramsList.add(new NameValuePair("captchaType", "9"));
		webRequest.setRequestParameters(paramsList);

		Page page2 = LoginAndGetCommon.gethtmlWebRequest(webClient, webRequest);

		tracer.addTag("公积金登录   login", "<xmp>" + page2.getWebResponse().getContentAsString() + "</xmp>");

		System.out.println("强制登录结果为===" + page2.getWebResponse().getContentAsString());
		LoginRootBean loginRootBean = HousingCZParse.login_parse(page2.getWebResponse().getContentAsString());
		if (loginRootBean.getCode() != 0) {
			webParamHousing.setStatusCode(404);
			webParamHousing.setErrormessage(loginRootBean.getMessage());
		}
		webParamHousing.setPage(htmlPage);

		webParamHousing.setWebClient(webClient);

		return webParamHousing;

	}

	/**
	 * 获取 项目名称：common-microservice-housingfund-changzhou 所属包名：app.service 类描述：
	 * 创建人：hyx 创建时间：2018年2月27日
	 * 
	 * @version 1 返回值 void
	 * @return 
	 */
	public WebParamHousing<?> getBasic(WebClient webClient, String mannum) throws Exception {
		String url = "http://www.czgjj.com/hfmis_wt/common/zhfw/invoke/020113" + "?spcode=" + mannum.trim()
				+ "&ishj=0&appid=02" + "&hjstatus_start=0" + "&bthjstatus_start=0";

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		Page page = webClient.getPage(webRequest);
		System.out.println(page.getWebResponse().getContentAsString());

		WebParamHousing<?> webParamHousing = new WebParamHousing<>();
		webParamHousing.setPage(page);

		webParamHousing.setWebClient(webClient);

		return webParamHousing;
	}
	
	public WebParamHousing<?> getTranFlow(WebClient webClient, String mannum,String date) throws Exception {
		String url = "http://www.czgjj.com/hfmis_wt/common/zhfw/invoke/020114";

		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

		paramsList.add(new NameValuePair("filterscount", "0"));
		paramsList.add(new NameValuePair("groupscount", "0"));
		paramsList.add(new NameValuePair("pagenum", "0"));
		paramsList.add(new NameValuePair("pagesize", "20"));
		paramsList.add(new NameValuePair("recordstartindex", ""));
		paramsList.add(new NameValuePair("recordendindex", "20"));
		paramsList.add(new NameValuePair("ywlx", "1"));
		paramsList.add(new NameValuePair("jcnf", date.trim()));
		paramsList.add(new NameValuePair("spcode", mannum.trim()));
		webRequest.setRequestParameters(paramsList);
		
		Page page = webClient.getPage(webRequest);
		System.out.println("date="+date+"  :"+page.getWebResponse().getContentAsString());
		
		WebParamHousing<?> webParamHousing = new WebParamHousing<>();
		webParamHousing.setPage(page);

		webParamHousing.setWebClient(webClient);
		
		return webParamHousing;
	}


	public String getWorkerAccount(WebClient webClient) {
		String url = "http://www.czgjj.com/hfmis_wt/personal/ace/main/grcx_index";
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.setJavaScriptTimeout(50000);
			webClient.getOptions().setTimeout(50000); // 15->60
			Page page = webClient.getPage(webRequest);
			System.out.println(page.getWebResponse().getContentAsString());

			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());

			Elements scrEle = doc.select("body > script");
			System.out.println("==============================");
			System.out.println(scrEle);

			String rgex = "varspcode=(.*?)varbds";
			String txt = scrEle.toString().replaceAll("\\s", "");
			String num = getSubUtilSimple(txt, rgex).replaceAll("\"", "").replaceAll(";", "");

			System.out.println("num==" + num);

			return num;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 返回单个字符串，若匹配到多个的话就返回第一个，方法与getSubUtil一样
	 * 
	 * @param soap
	 * @param rgex
	 * @return
	 */
	public String getSubUtilSimple(String soap, String rgex) {
		System.out.println("======================");
		System.out.println(soap);
		Pattern pattern = Pattern.compile(rgex);// 匹配的模式
		Matcher m = pattern.matcher(soap);
		while (m.find()) {
			return m.group(1);
		}
		return "";
	}

}
