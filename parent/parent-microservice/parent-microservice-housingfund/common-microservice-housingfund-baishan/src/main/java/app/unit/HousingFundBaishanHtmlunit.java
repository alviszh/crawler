package app.unit;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.baishan.HousingBaishanPaydetails;
import com.microservice.dao.entity.crawler.housing.baishan.HousingBaishanUserInfo;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.InfoParam;
import app.crawler.domain.WebParam;
import app.parser.HousingFundBaishanParser;

@Component
public class HousingFundBaishanHtmlunit {
	public static final Logger log = LoggerFactory.getLogger(HousingFundBaishanHtmlunit.class);
	@Autowired
	private HousingFundBaishanParser housingFundBaishanParser;
	@Autowired
	private TracerLog tracer;
	/**
	 * 登录
	 * 
	 * @param messageLoginForHousing
	 * @param taskHousing
	 * @return
	 */
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		tracer.addTag("HousingFundBaishanHtmlunit.login", taskHousing.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam = new WebParam();
		try {			
			String loginUrl="http://www.bssgjj.com/wscx/zfbzgl/zfbzsq/login_hidden.jsp?password="+messageLoginForHousing.getPassword()+"&sfzh="+messageLoginForHousing.getNum()+"&cxyd="+URLEncoder.encode("当前年度", "GBK")+"&dbname=gjjmx12";
			Page searchPage = getPage(webClient, taskHousing, loginUrl, null, null, "GBK", null, null);
			if (null != searchPage) {
				String searchPageString = searchPage.getWebResponse().getContentAsString();
				Document doc = Jsoup.parse(searchPageString);
				// 职工账号
				Elements selectzgzh = doc.select("input[name=zgzh]");
				String zgzh = selectzgzh.get(0).val();
				// 身份证号
				Elements selectsfzh = doc.select("input[name=sfzh]");
				String sfzh = selectsfzh.get(0).val();
				// 职工姓名
				Elements selectzgxm = doc.select("input[name=zgxm]");
				String zgxm = selectzgxm.get(0).val();
				// 感觉像“单位编码”
				Elements selectdwbm = doc.select("input[name=dwbm]");
				String dwbm = selectdwbm.get(0).val();
				// 弹框
				String alert = WebCrawler.getAlertMsg();
				InfoParam infoParam = new InfoParam(zgzh, sfzh, zgxm, dwbm);
				webParam.setInfoParam(infoParam);
				webParam.setText(alert);
				String cookies = CommonUnit.transcookieToJson(webClient);
				webParam.setCookies(cookies);
				webParam.setHtml(searchPageString);
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("HousingFundBaishanHtmlunit.login:",
					messageLoginForHousing.getTask_id() + "---ERROR:" + e.toString());
			e.printStackTrace();
		}
		return webParam;
	}
	/**
	 * 用户信息
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam getUserInfo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing, InfoParam infoParam) throws Exception {
		tracer.addTag("HousingFundBaishanHtmlunit.getUserInfo", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		try {
			 WebClient webClient=addcookie(taskHousing);
			String url = "http://www.bssgjj.com/wscx/zfbzgl/zfbzsq/main_menu.jsp";            
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);	
			String body="zgzh="+infoParam.getZgzh()+"&sfzh="+infoParam.getSfzh()+"&zgxm="+ URLEncoder.encode(infoParam.getZgxm(), "GBK")+"&dwbm="+infoParam.getDwbm()+"&cxyd="+URLEncoder.encode("当前年度", "GBK")+"&dbname=gjjmx12&pass1="+messageLoginForHousing.getPassword();
	    	webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest.setAdditionalHeader("Host", "www.bssgjj.com");
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest.setRequestBody(body);
			HtmlPage page = webClient.getPage(webRequest);
			if (null != page) {
				String html = page.asXml();
				tracer.addTag("HousingFundBaishanHtmlunit.getUserInfo---用户信息" + taskHousing.getTaskid(),
						"<xmp>" + html + "</xmp>");
				if (html.contains("职工姓名")) {
					HousingBaishanUserInfo housingBaishanUserInfo =housingFundBaishanParser.htmlUserInfoParser(taskHousing, html);	
					webParam.setUserInfo(housingBaishanUserInfo);
				}
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("HousingFundBaishanHtmlunit.getUserInfo---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
	/**
	 * 缴费信息
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam getPay(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,
			InfoParam infoParam) throws Exception {

		tracer.addTag("HousingFundBaishanHtmlunit.getPay", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		try {
			WebClient webClient = addcookie(taskHousing);
			String url = "http://www.bssgjj.com/wscx/zfbzgl/gjjmxcx/gjjmx_cx.jsp";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("sfzh", infoParam.getSfzh()));
			paramsList.add(new NameValuePair("zgxm", infoParam.getZgxm()));
			paramsList.add(new NameValuePair("zgzh", infoParam.getZgzh()));
			paramsList.add(new NameValuePair("dwbm", infoParam.getDwbm()));
			paramsList.add(new NameValuePair("cxyd", URLEncoder.encode("当前年度", "GBK")));	
			Page page = getPage(webClient, taskHousing, url, HttpMethod.POST, paramsList, "GBK", null, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingFundBaishanHtmlunit.getPay---缴费信息第一次请求" + taskHousing.getTaskid(),
						"<xmp>" + html + "</xmp>");
			
				List<HousingBaishanPaydetails> paydetails = housingFundBaishanParser.htmlPayParser(html, taskHousing);
				webParam.setPage(page);
				webParam.setHtml(page.getWebResponse().getContentAsString());
				webParam.setPaydetails(paydetails);			
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("HousingFundBaishanHtmlunit.getPay---ERROR:", taskHousing.getTaskid() + "---ERROR:" + e.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 通过url获取 Page
	 * 
	 * @param taskMobile
	 * @param url
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Page getPage(WebClient webClient, TaskHousing taskHousing, String url, HttpMethod type,
			List<NameValuePair> paramsList, String code, String body, Map<String, String> map) throws Exception {
		tracer.addTag("HousingFundBaishanHtmlunit.getPage---url:", url + "---taskId:" + taskHousing.getTaskid());

		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);

		if (null != map) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				webRequest.setAdditionalHeader(entry.getKey(), entry.getValue());
			}
		}

		if (null != body && !"".equals(body)) {
			webRequest.setRequestBody(body);
		}

		if (null != code && !"".equals(code)) {
			webRequest.setCharset(Charset.forName(code));
		}

		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		tracer.addTag("HousingFundBaishanHtmlunit.getPage.statusCode:" + statusCode, "---taskid:" + taskHousing.getTaskid());

		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("HousingFundBaishanHtmlunit.getPage---taskid:",
					taskHousing.getTaskid() + "---url:" + url + "<xmp>" + html + "</xmp>");
			return searchPage;
		}

		return null;
	}
	
	public WebClient addcookie(TaskHousing taskHousing) {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}

		return webClient;
	}
	public  WebClient addcookie(WebClient webclient, TaskHousing taskHousing) {
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		 for(Cookie cookie : cookies){
			 webclient.getCookieManager().addCookie(cookie);
		  }
		return webclient;
	}
}
