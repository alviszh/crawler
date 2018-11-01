package app.crawler.htmlparse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeLogin;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.bayanzhuoer.HousingBayanzhuoerUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.bean.InfoParam;
import app.crawler.bean.WebParam;

@Component
public class HousingBayanzhuoerParse {

	@Autowired
	private TracerLog tracer;
	/**
	 * 登录
	 * 
	 * @param messageLoginForHousing
	 * @param taskHousing
	 * @return
	 * @throws IOException 
	 * @throws FailingHttpStatusCodeException 
	 * @throws InterruptedException 
	 */
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		tracer.addTag("HousingBayanzhuoerParse.login", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String url = "http://zh.bynegjj.com/wscx/zfbzgl/zfbzsq/login_hidden.jsp";
			String loginType = "01";
			if (StatusCodeLogin.SUNSHINE_NUM.equals(messageLoginForHousing.getLogintype())) {
				loginType = "02";
			}
			String body = "password=" + messageLoginForHousing.getPassword() + "&sfzh=" + messageLoginForHousing.getNum()
					+ "&cxyd=" + URLEncoder.encode("当前年度", "GBK") + "&dbname=gjjmx9&dlfs=" + loginType;
			Page loginPage = getPage(webClient, taskHousing, url, HttpMethod.POST, null, "GBK", body, null);
			if (null != loginPage) {
				String pageString = loginPage.getWebResponse().getContentAsString();
				System.out.println(loginPage);
				Document doc = Jsoup.parse(pageString);
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
				// 当前状态
				Elements selectzgzt = doc.select("input[name=zgzt]");
				String zgzt = selectzgzt.get(0).val();
				// 弹框
				String alert = WebCrawler.getAlertMsg();
				InfoParam infoParam = new InfoParam(zgzh, sfzh, zgxm, dwbm, zgzt);
				webParam.setInfoParam(infoParam);
				webParam.setText(alert);
				String cookies = CommonUnit.transcookieToJson(webClient);
				webParam.setCookies(cookies);
				webParam.setHtml(pageString);
				webParam.setLoginType(loginType);
				return webParam;
			}	
		} catch (Exception e) {
			tracer.addTag("HousingBayanzhuoerParse.login.Exception",e.toString());
			e.printStackTrace();
		}
	
		return null;
	}

	/**
	 * 用户信息
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,
			InfoParam infoParam, String loginType) throws Exception {
		tracer.addTag("HousingBayanzhuoerParse.getUserInfo", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = addcookie(taskHousing);
		String url = "http://zh.bynegjj.com/wscx/zfbzgl/zfbzsq/main_menu.jsp";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("zgzh", infoParam.getZgzh()));
		paramsList.add(new NameValuePair("sfzh", infoParam.getSfzh()));
		paramsList.add(new NameValuePair("zgxm", infoParam.getZgxm()));
		paramsList.add(new NameValuePair("dwbm", infoParam.getDwbm()));
		paramsList.add(new NameValuePair("zgzt", URLEncoder.encode(infoParam.getZgzt(), "GBK")));
		paramsList.add(new NameValuePair("cxyd", URLEncoder.encode("当前年度", "GBK")));
		paramsList.add(new NameValuePair("dbname", "gjjmx9"));
		Page searchPage = getPage(webClient, taskHousing, url, null, paramsList, "GBK", null, null);
		if (null != searchPage) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("HousingBayanzhuoerParse.getUserInfo---用户信息" + taskHousing.getTaskid(),
					"<xmp>" + html + "</xmp>");
			HousingBayanzhuoerUserInfo housingBayanzhuoerUserInfo = htmlUserInfoParser(taskHousing, html);
			webParam.setPage(searchPage);
			webParam.setHtml(html);
			webParam.setHousingBayanzhuoerUserInfo(housingBayanzhuoerUserInfo);
			webParam.setUrl(searchPage.getUrl().toString());
			webParam.setCode(searchPage.getWebResponse().getStatusCode());
			return webParam;
		}
		return null;
	}

	/**
	 * 解析用户信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	public HousingBayanzhuoerUserInfo htmlUserInfoParser(TaskHousing taskHousing, String html) throws Exception {
		tracer.addTag("HousingBayanzhuoerParse.htmlUserInfoParser---info:" + taskHousing.getTaskid(),
				"<xmp>" + html + "</xmp>");
		Document doc = Jsoup.parse(html);
		String companyNum = getNextLabelByKeyword(doc, "单位账号");
		String companyName = getNextLabelByKeyword(doc, "单位名称");
		String staffAccount = getNextLabelByKeyword(doc, "职工账号");
		String username = getNextLabelByKeyword(doc, "职工姓名");
		String idnum = getNextLabelByKeyword(doc, "身份证号");
		String openingDate = getNextLabelByKeyword(doc, "开户日期");
		String lastPayMoney = getNextLabelByKeyword(doc, "上年汇补缴累计");
		String lastInterest = getNextLabelByKeyword(doc, "上年利息");
		String yearPayMoney = getNextLabelByKeyword(doc, "本年汇补缴累计");// 缴存比例
		String yearDrawMoney = getNextLabelByKeyword(doc, "本年支取累计");
		String balance = getNextLabelByKeyword(doc, "公积金余额");
		String monthlyPay = getNextLabelByKeyword(doc, "月缴额");
		String companyMonthlyPay = getNextLabelByKeyword(doc, "单位月缴额");
		String personalMonthlyPay = getNextLabelByKeyword(doc, "个人月缴额");
		String financeMonthPay = getNextLabelByKeyword(doc, "财政月缴额");
		String yearMonth = getNextLabelByKeyword(doc, "缴至年月");
		String state = getNextLabelByKeyword(doc, "账户状态");
		String idcard = getNextLabelByKeyword(doc, "联名卡号");
		HousingBayanzhuoerUserInfo housingBayanzhuoerUserInfo = new HousingBayanzhuoerUserInfo(companyNum, companyName,
				staffAccount, username, idnum, openingDate, lastPayMoney, lastInterest, yearPayMoney, yearDrawMoney,
				balance, monthlyPay, companyMonthlyPay, personalMonthlyPay, financeMonthPay, yearMonth, state, idcard,
				taskHousing.getTaskid());
		return housingBayanzhuoerUserInfo;
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
		tracer.addTag("HousingBayanzhuoerParse.getPage---url:", url + "---taskId:" + taskHousing.getTaskid());
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
		Thread.sleep(2500);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		tracer.addTag("HousingBayanzhuoerParse.getPage.statusCode:" + statusCode, "---taskid:" + taskHousing.getTaskid());
		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("HousingBayanzhuoerParse.getPage---taskid:",
					taskHousing.getTaskid() + "---url:" + url + "<xmp>" + html + "</xmp>");
			return searchPage;
		}
		return null;
	}

	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("tr[class=jtpsoft] td:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
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
}
