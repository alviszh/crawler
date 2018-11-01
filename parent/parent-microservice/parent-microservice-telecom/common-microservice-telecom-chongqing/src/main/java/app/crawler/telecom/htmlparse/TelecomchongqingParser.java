package app.crawler.telecom.htmlparse;

import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingBalance;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingBill;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingBusiness;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingCallRecord;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingComboUse;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingFlow;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingIncrement;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingIntegral;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingMessage;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingPay;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingStarlevel;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.bean.WebParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class TelecomchongqingParser {

	@Autowired
	private TracerLog tracer;


	/**
	 * 通过url获取 Page
	 * 
	 * @param taskMobile
	 * @param url
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Page getPage(WebClient webClient, TaskMobile taskMobile, String url, HttpMethod type,
			List<NameValuePair> paramsList, Boolean code) throws Exception {
//		tracer.addTag("TelecomChongqingParser.getPage---url:", url + "---taskId:" + taskMobile.getTaskid());

		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);

		if (code) {
			webRequest.setCharset(Charset.forName("UTF-8"));
		}

		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
//		tracer.addTag("TelecomChongqingParser.getPage.statusCode:" + statusCode, "---taskid:" + taskMobile.getTaskid());

		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("TelecomChongqingParser.getPage---taskid:",
					taskMobile.getTaskid() + "---url:" + url + "<xmp>" + html + "</xmp>");
			return searchPage;
		}

		return null;
	}

	public HtmlPage getHtml(String url, WebClient webClient, TaskMobile taskMobile) throws Exception {
//		tracer.addTag("TelecomChongqingParser.getHtml---url:" + url + " ", taskMobile.getTaskid());

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
//		tracer.addTag("TelecomChongqingParser.getHtml.statusCode:" + statusCode, "---taskid:" + taskMobile.getTaskid());
		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("TelecomChongqingParser.getHtml---url:" + url + "---taskid:" + taskMobile.getTaskid(),
					"<xmp>" + html + "</xmp>");
			return searchPage;
		}
		return null;
	}

	public WebClient addcookie(TaskMobile taskMobile) {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}

		return webClient;
	}
	
	/**
	 * 实名认证姓名
	 * 
	 * @param mobileLogin
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public Boolean verificationName(TaskMobile taskMobile) throws Exception {

		tracer.addTag("TelecomChongqingParser.verificationName", taskMobile.getTaskid());

		try {
			WebClient webClient = addcookie(taskMobile);
			try {

				String url0203 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0203";
				HtmlPage html1 = getHtml(url0203, webClient, taskMobile);
				String json1 = html1.getWebResponse().getContentAsString();

			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("TelecomChongqingParser.verificationName.url0203---ERROR:",
						taskMobile.getTaskid() + "---ERROR:" + e);
			}

			String url = "http://cq.189.cn/new-bill/bill_SMZ";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			String tname = taskMobile.getBasicUser().getName();
			tname = tname.substring(1);
			paramsList.add(new NameValuePair("tname", tname));

			Page page = getPage(webClient, taskMobile, url, HttpMethod.POST, paramsList, true);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomChongqingParser.verificationName---实名认证姓名" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				String json = page.getWebResponse().getContentAsString();

				JSONObject jsonObj = JSONObject.fromObject(json);
				String sm = "0";
				String xm = "0";
				if (jsonObj.has("sm")) {
					sm = jsonObj.getString("sm");
				}
				if (jsonObj.has("xm")) {
					xm = jsonObj.getString("xm");
				}
				webClient.close();
				if (sm.equals("1") && xm.equals("1")) {
					return false;
				} else {
					return true;
				}
			}
		} catch (Exception e) {
			tracer.addTag("TelecomChongqingParser.verificationName---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
			e.printStackTrace();
			return false;
		}
		return false;

	}

	/**
	 * 实名认证证件号码
	 * 
	 * @param mobileLogin
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public Boolean verificationIdcard(TaskMobile taskMobile) throws Exception {

		tracer.addTag("TelecomChongqingParser.verificationIdcard", taskMobile.getTaskid());

		try {
			WebClient webClient = addcookie(taskMobile);
			try {

				String url0203 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0203";
				HtmlPage html1 = getHtml(url0203, webClient, taskMobile);
				String json1 = html1.getWebResponse().getContentAsString();

			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("TelecomChongqingParser.verificationIdcard.url0203---ERROR:",
						taskMobile.getTaskid() + "---ERROR:" + e);
			}

			String url = "http://cq.189.cn/new-bill/bill_SMZ";

			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			String idcard = taskMobile.getBasicUser().getIdnum();
			if (idcard.length() >= 6) {
				idcard = idcard.substring(idcard.length() - 6);
			}
			paramsList.add(new NameValuePair("idcard", idcard));

			Page page = getPage(webClient, taskMobile, url, HttpMethod.POST, paramsList, true);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomChongqingParser.verificationIdcard---实名认证姓名" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				String json = page.getWebResponse().getContentAsString();

				String sm = "0";
				String sfz = "0";

				JSONObject jsonObj = JSONObject.fromObject(json);
				if (jsonObj.has("sm")) {
					sm = jsonObj.getString("sm");
				}

				if (jsonObj.has("sfz")) {
					sfz = jsonObj.getString("sfz");
				}

				if (sm.equals("2") && sfz.equals("2")) {
					return false;
				} else {
					return true;
				}
			}
		} catch (Exception e) {
			tracer.addTag("TelecomChongqingParser.verificationIdcard---ERROR:",
					taskMobile.getTaskid() + "---ERROR:" + e);
			e.printStackTrace();
			return false;
		}
		return false;

	}

	/**
	 * 验证验证码
	 * 
	 * @param taskMobile
	 * @param messageLogin
	 * @param starttime
	 * @param endtime
	 * @return
	 * @throws Exception
	 */
	public String verifySms(TaskMobile taskMobile, MessageLogin messageLogin) throws Exception {

		tracer.addTag("TelecomchongqingParser.verificationcode", taskMobile.getTaskid());

		try {
			WebClient webClient = addcookie(taskMobile);
			String tname = taskMobile.getBasicUser().getName();
			String idcard = taskMobile.getBasicUser().getIdnum();
			if (tname.length() > 0) {
				tname = tname.substring(1);
			}
			if (idcard.length() >= 6) {
				idcard = idcard.substring(idcard.length() - 6);
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
			String beginTime = format.format(calendar.getTime());
			calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
			String endTime = format.format(calendar.getTime());
			String month = beginTime.substring(0, beginTime.length() - 3);
//			String url3 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02031273";
//			HtmlPage html3 = getHtml(url3, webClient, taskMobile);
//			String json3 = html3.getWebResponse().getContentAsString();

			String url1 = "http://cq.189.cn/new-bill/bill_XDCXNR";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("accNbr", messageLogin.getName()));
			paramsList.add(new NameValuePair("productId", "208511296"));
			paramsList.add(new NameValuePair("month", month));
			paramsList.add(new NameValuePair("callType", "00"));
			paramsList.add(new NameValuePair("listType", "300001"));
			paramsList.add(new NameValuePair("beginTime", beginTime));
			paramsList.add(new NameValuePair("endTime", endTime));
			paramsList.add(new NameValuePair("rc", messageLogin.getSms_code()));
			paramsList.add(new NameValuePair("tname", tname));
			paramsList.add(new NameValuePair("idcard", idcard));
			paramsList.add(new NameValuePair("zq", "2"));

			Page page = getPage(webClient, taskMobile, url1, HttpMethod.POST, paramsList, true);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomchongqingParser.verificationcode 验证验证码" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				webClient.close();
				return html;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomchongqingParser.verificationcode---ERROR:",
					taskMobile.getTaskid() + "---ERROR:" + e.toString());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 用户星级服务信息
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomChongqingStarlevel> getStarlevel(TaskMobile taskMobile) throws Exception {

		tracer.addTag("TelecomChongqingParser.getStarlevel", taskMobile.getTaskid());

		WebParam<TelecomChongqingStarlevel> webParam = new WebParam<TelecomChongqingStarlevel>();

		String transactionId = "";
		String token = "";
		String mobile = "";
		String sign = "";
		try {
			WebClient webClient = addcookie(taskMobile);
			try {
				// 跳转星级服务
				String urlT = "http://www.189.cn/dqmh/ssoLink.do?method=linkTo&platNo=93510&toStUrl=http://xjfw.189.cn/tykfh5/modules/starService/medalWall/indexPC.html?intaid=jt-sy-hxfw-01-";
				// HtmlPage htmlT = getHtml(urlT, webClient, taskMobile);
				Page htmlT = getPage(webClient, taskMobile, urlT, null, null, false);
				String jsonT = htmlT.getWebResponse().getContentAsString();
				// webClient = htmlT.getWebClient();

			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("TelecomChongqingParser.urlT---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
			}
			try {
				// 通过此接口获取手机号与token
				String url1 = "http://xjfw.189.cn/tykf-itr-services/services/login/bySessionId";
				Page html = getPage(webClient, taskMobile, url1, null, null, false);
				String json = html.getWebResponse().getContentAsString();

				tracer.addTag("TelecomChongqingParser.getStarlevel---用户星级服务token与mobile信息" + taskMobile.getTaskid(),
						"<xmp>" + json + "</xmp>");

				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				int radomInt = new Random().nextInt(999999);
				transactionId = "1000010017" + df.format(new Date()) + radomInt;
				JSONObject jsonObj = JSONObject.fromObject(json);
				token = jsonObj.getString("token");
				mobile = jsonObj.getString("mobile");

			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("TelecomChongqingParser.bySessionId---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
			}

			try {
				/**
				 * 通过 transactionId , token 与 mobile 获取签名
				 */
				String urlData = "http://xjfw.189.cn/tykf-itr-services/services/dispatch.jsp?&dispatchUrl=ClientUni/clientuni/services/user/getSign?reqParam={\"transactionId\":\""
						+ transactionId + "\",\"channelCode\":\"H5002018\",\"token\":\"" + token + "\",\"type\":2}";
				Page page = getPage(webClient, taskMobile, urlData, HttpMethod.POST, null, false);
				String json = page.getWebResponse().getContentAsString();

				tracer.addTag("TelecomChongqingParser.getStarlevel---用户星级服务签名信息" + taskMobile.getTaskid(),
						"<xmp>" + json + "</xmp>");

				JSONObject jsonObj2 = JSONObject.fromObject(json);
				sign = jsonObj2.getString("sign");

			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("TelecomChongqingParser.sign---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
			}

			String urlStarlevel = "http://xjfw.189.cn/tykf-itr-services/services/dispatch.jsp?&dispatchUrl=ClientUni/clientuni/services/starLevel/custStarLevelQuery?reqParam={\"transactionId\":\""
					+ transactionId + "\",\"clientNbr\":\"" + mobile
					+ "\",\"channelCode\":\"H5002018\",\"deviceType\":\"1\",\"prvince\":\"%25E9%2587%258D%25E5%25BA%2586\",\"sign\":\""
					+ sign + "\"}";

			Page page = getPage(webClient, taskMobile, urlStarlevel, null, null, false);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomChongqingParser.getStarlevel---用户星级服务信息" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomChongqingStarlevel> list = htmlStarlevelParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomChongqingParser.getStarlevel---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析用户星级服务信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomChongqingStarlevel> htmlStarlevelParser(String html, TaskMobile taskMobile) {

		List<TelecomChongqingStarlevel> list = new ArrayList<TelecomChongqingStarlevel>();
		try {
			TelecomChongqingStarlevel telecomChongqingStarlevel = null;

			JSONObject jsonObj = JSONObject.fromObject(html);

			JSONObject custInfo = jsonObj.getJSONObject("custInfo");

			if (null != custInfo) {
				String custName = custInfo.getString("custName");
				String membershipLevel = custInfo.getString("membershipLevel");
				String growthpoint = custInfo.getString("growthpoint");
				telecomChongqingStarlevel = new TelecomChongqingStarlevel(taskMobile.getTaskid(), custName,
						membershipLevel, growthpoint);
				list.add(telecomChongqingStarlevel);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomChongqingParser.htmlStarlevelParser---ERROR:",
					taskMobile.getTaskid() + "---ERROR:" + e.toString());
		}

		return list;

	}

	/**
	 * 余额查询
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomChongqingBalance> getBalance(TaskMobile taskMobile, MessageLogin messageLogin)
			throws Exception {

		tracer.addTag("TelecomChongqingParser.getBalance", taskMobile.getTaskid());

		WebParam<TelecomChongqingBalance> webParam = new WebParam<TelecomChongqingBalance>();

		try {
			WebClient webClient = addcookie(taskMobile);
			try {
				// 跳转余额信息
				String url0203 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0203";
				HtmlPage html1 = getHtml(url0203, webClient, taskMobile);
				String json1 = html1.getWebResponse().getContentAsString();

			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("TelecomChongqingParser.url0203---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
			}

			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("accNbr", messageLogin.getName()));
			paramsList.add(new NameValuePair("productId", "208511296"));

			String url = "http://cq.189.cn/new-bill/getNewYe";
			Page page = getPage(webClient, taskMobile, url, HttpMethod.POST, paramsList, false);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomChongqingParser.getBalance---余额信息" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomChongqingBalance> list = htmlBalanceParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				webClient.close();
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomChongqingParser.getBalance---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 实时话费查询
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomChongqingBalance> getBalanceRealtime(TaskMobile taskMobile, MessageLogin messageLogin)
			throws Exception {

		tracer.addTag("TelecomChongqingParser.getBalanceRealtime", taskMobile.getTaskid());

		WebParam<TelecomChongqingBalance> webParam = new WebParam<TelecomChongqingBalance>();

		try {
			WebClient webClient = addcookie(taskMobile);
			try {
				// 跳转余额信息
				String urlRealtime = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0203";
				HtmlPage html1 = getHtml(urlRealtime, webClient, taskMobile);
				String json1 = html1.getWebResponse().getContentAsString();

			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("TelecomChongqingParser.url0203---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
			}

			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("accNbr", messageLogin.getName()));
			paramsList.add(new NameValuePair("productId", "208511296"));

			String url = "http://cq.189.cn/new-bill/getSSHF";
			Page page = getPage(webClient, taskMobile, url, HttpMethod.POST, paramsList, false);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomChongqingParser.getBalanceRealtime---实时话费查询" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomChongqingBalance> list = htmlBalanceParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomChongqingParser.getBalanceRealtime---ERROR:",
					taskMobile.getTaskid() + "---ERROR:" + e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析余额信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomChongqingBalance> htmlBalanceParser(String html, TaskMobile taskMobile) {

		List<TelecomChongqingBalance> list = new ArrayList<TelecomChongqingBalance>();
		try {
			TelecomChongqingBalance telecomChongqingBalance = null;

			String time = "";
			String money = "";
			String type = "";
			JSONArray jsonarray = JSONArray.fromObject(html);
			for (Object object : jsonarray) {
				JSONObject jsonObj1 = JSONObject.fromObject(object);
				if(jsonObj1.has("lastLogin")){
					time = jsonObj1.getString("lastLogin");
				}
				if(jsonObj1.has("BalanceAvailable")){
					money = jsonObj1.getString("BalanceAvailable");
				}
			}
			telecomChongqingBalance = new TelecomChongqingBalance(taskMobile.getTaskid(), time, money, type);
			list.add(telecomChongqingBalance);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomChongqingParser.htmlBalanceParser---ERROR:",
					taskMobile.getTaskid() + "---ERROR:" + e.toString());
		}

		return list;

	}
	
	
	/**
	 * 账单中转链接
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public TaskMobile transferBill(TaskMobile taskMobile) throws Exception {

		tracer.addTag("TelecomChongqingParser.transferBill", taskMobile.getTaskid());

		WebClient webClient = addcookie(taskMobile);
		try {
			// 跳转账单
			String url1 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02031272";
			HtmlPage html1 = getHtml(url1, webClient, taskMobile);
			String json1 = html1.getWebResponse().getContentAsString();
			String cookies = CommonUnit.transcookieToJson(html1.getWebClient());
			taskMobile.setCookies(cookies);
			
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomChongqingParser.url02031272---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
		}
		return taskMobile;
	}
	

	/**
	 * 账单
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomChongqingBill> getBill(TaskMobile taskMobile, MessageLogin messageLogin, String yearmonth)
			throws Exception {

		tracer.addTag("TelecomChongqingParser.getBill---" + yearmonth, taskMobile.getTaskid());

		WebParam<TelecomChongqingBill> webParam = new WebParam<TelecomChongqingBill>();

		try {
			WebClient webClient = addcookie(taskMobile);
//			try {
//				// 跳转账单
//				String url1 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02031272";
//				HtmlPage html1 = getHtml(url1, webClient, taskMobile);
//				String json1 = html1.getWebResponse().getContentAsString();
//
//			} catch (Exception e) {
//				e.printStackTrace();
//				tracer.addTag("TelecomChongqingParser.url02031272---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
//			}

			try {
				// 跳转查询某月
				String url2 = "http://cq.189.cn/new-bill/bill_ZZDCX";
				List<NameValuePair> paramsList2 = new ArrayList<NameValuePair>();
				paramsList2.add(new NameValuePair("accNbr", messageLogin.getName()));
				paramsList2.add(new NameValuePair("productId", "208511296"));
				paramsList2.add(new NameValuePair("month", yearmonth));
				paramsList2.add(new NameValuePair("queryType", "2"));
				Page html2 = getPage(webClient, taskMobile, url2, HttpMethod.POST, paramsList2, false);
				String json2 = html2.getWebResponse().getContentAsString();
				tracer.addTag("TelecomChongqingParser.bill_ZZDCX---SUCCESS:" + taskMobile.getTaskid(), json2);
			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("TelecomChongqingParser.bill_ZZDCX---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
			}
			String url = "http://cq.189.cn/new-bill/bill_ZDCX";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("page", "1"));
			paramsList.add(new NameValuePair("rows", String.valueOf(Integer.MAX_VALUE)));
			Page page = getPage(webClient, taskMobile, url, HttpMethod.POST, paramsList, false);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomChongqingParser.getBill---账单" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomChongqingBill> list = htmlBillParser(html, taskMobile, yearmonth);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				webClient.close();
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomChongqingParser.getBill---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析账单
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomChongqingBill> htmlBillParser(String html, TaskMobile taskMobile, String yearmonth) {

		List<TelecomChongqingBill> list = new ArrayList<TelecomChongqingBill>();
		try {
			TelecomChongqingBill telecomChongqingBill = null;
			JSONObject jsonObj = JSONObject.fromObject(html);
			String rows = jsonObj.getString("rows");
			JSONArray jsonarray = JSONArray.fromObject(rows);
			for (Object object : jsonarray) {
				JSONObject jsonObj1 = JSONObject.fromObject(object);
				String billItem = jsonObj1.getString("billItem");
				String billAmount = jsonObj1.getString("billAmount");
				telecomChongqingBill = new TelecomChongqingBill(taskMobile.getTaskid(), billItem, billAmount,
						yearmonth);
				list.add(telecomChongqingBill);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomChongqingParser.htmlBillParser---ERROR:",
					taskMobile.getTaskid() + "---ERROR:" + e.toString());
		}

		return list;

	}

	/**
	 * 积分查询
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomChongqingIntegral> getIntegral(TaskMobile taskMobile, MessageLogin messageLogin)
			throws Exception {

		tracer.addTag("TelecomChongqingParser.getIntegral", taskMobile.getTaskid());

		WebParam<TelecomChongqingIntegral> webParam = new WebParam<TelecomChongqingIntegral>();

		try {
			WebClient webClient = addcookie(taskMobile);
			try {
				String urlhome = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02061287";
				HtmlPage html = getHtml(urlhome, webClient, taskMobile);
				String json = html.getWebResponse().getContentAsString();

			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("TelecomChongqingParser.urlhome---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
			}

			String url = "http://cq.189.cn/new-integral/index.htm?fastcode=02061287&cityCode=cq";

			Page page = getPage(webClient, taskMobile, url, null, null, false);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomChongqingParser.getIntegral---积分查询" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomChongqingIntegral> list = htmlIntegralParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				webClient.close();
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomChongqingParser.getIntegral---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析积分查询
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomChongqingIntegral> htmlIntegralParser(String html, TaskMobile taskMobile) {

		List<TelecomChongqingIntegral> list = new ArrayList<TelecomChongqingIntegral>();
		try {

			Document doc = Jsoup.parse(html);
			Elements link1 = doc.getElementsByTag("table");
			if (link1.size() > 0) {
				Elements link2 = link1.get(0).getElementsByTag("tr");
				if (link2.size() > 1) {
					// 当前可使用积分
					String type = link2.get(0).getElementsByTag("th").get(0).text();
					String integral = link2.get(1).getElementsByTag("td").get(0).text();
					TelecomChongqingIntegral telecomChongqingIntegral = new TelecomChongqingIntegral(
							taskMobile.getTaskid(), type, integral);

					// 历史累计积分
					String type1 = link2.get(0).getElementsByTag("th").get(1).text();
					String integral1 = link2.get(1).getElementsByTag("td").get(1).text();
					TelecomChongqingIntegral telecomChongqingIntegral1 = new TelecomChongqingIntegral(
							taskMobile.getTaskid(), type1, integral1);

					// 本年度到期积分
					String type2 = link2.get(0).getElementsByTag("th").get(2).text();
					String integral2 = link2.get(1).getElementsByTag("td").get(2).text();
					TelecomChongqingIntegral telecomChongqingIntegral2 = new TelecomChongqingIntegral(
							taskMobile.getTaskid(), type2, integral2);

					list.add(telecomChongqingIntegral);
					list.add(telecomChongqingIntegral1);
					list.add(telecomChongqingIntegral2);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomChongqingParser.htmlIntegralParser---ERROR:",
					taskMobile.getTaskid() + "---ERROR:" + e.toString());
		}

		return list;

	}
	
	/**
	 * 套餐使用情况中转链接
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public TaskMobile transferComboUse(TaskMobile taskMobile) throws Exception {

		tracer.addTag("TelecomChongqingParser.transferComboUse", taskMobile.getTaskid());

		WebClient webClient = addcookie(taskMobile);
		try {
			// 跳转套餐使用情况
			String url2 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0202";
			HtmlPage html2 = getHtml(url2, webClient, taskMobile);
			String json2 = html2.getWebResponse().getContentAsString();
			String cookies = CommonUnit.transcookieToJson(html2.getWebClient());
			taskMobile.setCookies(cookies);
			
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomChongqingParser.fastcode=0202---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
		}
		return taskMobile;
	}
	

	/**
	 * 套餐使用情况
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomChongqingComboUse> getComboUse(TaskMobile taskMobile, MessageLogin messageLogin,
			String yearmonth) throws Exception {

		tracer.addTag("TelecomChongqingParser.getComboUse", taskMobile.getTaskid());

		WebParam<TelecomChongqingComboUse> webParam = new WebParam<TelecomChongqingComboUse>();

		try {
			WebClient webClient = addcookie(taskMobile);
//			try {
//				// 跳转套餐使用情况
//				String url2 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0202";
//				HtmlPage html2 = getHtml(url2, webClient, taskMobile);
//				String json2 = html2.getWebResponse().getContentAsString();
//
//			} catch (Exception e) {
//				e.printStackTrace();
//				tracer.addTag("TelecomChongqingParser.fastcode=0202---ERROR:",
//						taskMobile.getTaskid() + "---ERROR:" + e);
//			}
			String url = "http://cq.189.cn/new-bill/getSYTC";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("accNbr", messageLogin.getName()));
			paramsList.add(new NameValuePair("productId", "208511296"));
			paramsList.add(new NameValuePair("queryMonth", yearmonth));

			Page page = getPage(webClient, taskMobile, url, HttpMethod.POST, paramsList, false);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomChongqingParser.getComboUse---套餐使用情况" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomChongqingComboUse> list = htmlComboUseParser(html, taskMobile, yearmonth);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				webClient.close();
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomChongqingParser.getComboUse---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析套餐使用情况
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomChongqingComboUse> htmlComboUseParser(String html, TaskMobile taskMobile, String yearmonth) {

		List<TelecomChongqingComboUse> list = new ArrayList<TelecomChongqingComboUse>();
		try {
			TelecomChongqingComboUse telecomChongqingComboUse = null;

			JSONArray jsonarray = JSONArray.fromObject(html);

			if (null != jsonarray && jsonarray.size() > 0) {
				for (Object object : jsonarray) {
					JSONObject jsonObj1 = JSONObject.fromObject(object);
					String totalDesc = jsonObj1.getString("totalDesc");
					String disctDealedAmount = jsonObj1.getString("disctDealedAmount");
					String disctUndealedAmount = jsonObj1.getString("disctUndealedAmount");
					String disctAmount = jsonObj1.getString("disctAmount");
					String disctUnit = jsonObj1.getString("disctUnit");
					String expDate = jsonObj1.getString("expDate");
					String carryoverAmount = "";
					String month = "";
					String sm = "";
					try {
						if (jsonObj1.has("carryoverAmount")) {
							carryoverAmount = jsonObj1.getString("carryoverAmount");
							month = jsonObj1.getString("month");
							sm = jsonObj1.getString("sm");
						}
					} catch (Exception e) {
						tracer.addTag("TelecomChongqingParser.htmlComboUseParser.carryoverAmount---ERROR:",
								taskMobile.getTaskid() + "---ERROR:" + e.toString());
					}
					telecomChongqingComboUse = new TelecomChongqingComboUse(taskMobile.getTaskid(), yearmonth,
							totalDesc, disctDealedAmount, disctUndealedAmount, disctAmount, disctUnit, expDate, month,
							carryoverAmount, sm);
					list.add(telecomChongqingComboUse);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomChongqingParser.htmlComboUseParser---ERROR:",
					taskMobile.getTaskid() + "---ERROR:" + e.toString());
		}

		return list;

	}

	/**
	 * 在用业务情况-----套餐信息
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomChongqingBusiness> getBusinessT(TaskMobile taskMobile, MessageLogin messageLogin)
			throws Exception {

		tracer.addTag("TelecomChongqingParser.getBusinessT", taskMobile.getTaskid());

		WebParam<TelecomChongqingBusiness> webParam = new WebParam<TelecomChongqingBusiness>();

		try {
			WebClient webClient = addcookie(taskMobile);
			try {

				String urlfastcode = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0202";
				HtmlPage html2 = getHtml(urlfastcode, webClient, taskMobile);
				String json2 = html2.getWebResponse().getContentAsString();

			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("TelecomChongqingParser.getBusinessT.urlfastcode---ERROR:",
						taskMobile.getTaskid() + "---ERROR:" + e);
			}

			String url = "http://cq.189.cn/new-bill/getAccept";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("accNbr", messageLogin.getName()));
			paramsList.add(new NameValuePair("productId", "208511296"));

			Page page = getPage(webClient, taskMobile, url, HttpMethod.POST, paramsList, false);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomChongqingParser.getBusinessT---在用业务情况-----套餐信息" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomChongqingBusiness> list = htmlBusinessTParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				webClient.close();
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomChongqingParser.getBusinessT---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析在用业务情况-----套餐信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomChongqingBusiness> htmlBusinessTParser(String html, TaskMobile taskMobile) {

		List<TelecomChongqingBusiness> list = new ArrayList<TelecomChongqingBusiness>();
		try {
			TelecomChongqingBusiness telecomChongqingBusiness = null;

			JSONArray jsonarray = JSONArray.fromObject(html);

			if (null != jsonarray && jsonarray.size() > 0) {
				for (Object object : jsonarray) {
					JSONObject jsonObj1 = JSONObject.fromObject(object);
					String offerCompName = jsonObj1.getString("offerCompName");
					JSONArray ywxq = JSONArray.fromObject(jsonObj1.getString("ywxq"));
					for (Object object2 : ywxq) {
						String favourName = JSONObject.fromObject(object2).getString("favourName");
						String favourEffTime = JSONObject.fromObject(object2).getString("favourEffTime");
						telecomChongqingBusiness = new TelecomChongqingBusiness(taskMobile.getTaskid(), offerCompName,
								favourName, favourEffTime);
						list.add(telecomChongqingBusiness);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomChongqingParser.htmlBusinessTParser---ERROR:",
					taskMobile.getTaskid() + "---ERROR:" + e.toString());
		}

		return list;

	}

	/**
	 * 在用业务----已订购的增值业务
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomChongqingBusiness> getBusinessZ(TaskMobile taskMobile, MessageLogin messageLogin)
			throws Exception {

		tracer.addTag("TelecomChongqingParser.getBusinessZ", taskMobile.getTaskid());

		WebParam<TelecomChongqingBusiness> webParam = new WebParam<TelecomChongqingBusiness>();

		try {
			WebClient webClient = addcookie(taskMobile);
			try {

				String urlfastcode = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0202";
				HtmlPage html2 = getHtml(urlfastcode, webClient, taskMobile);
				String json2 = html2.getWebResponse().getContentAsString();

			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("TelecomChongqingParser.getBusinessZ.urlfastcode---ERROR:",
						taskMobile.getTaskid() + "---ERROR:" + e);
			}

			String url = "http://cq.189.cn/new-bill/getKXB";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("accNbr", messageLogin.getName()));
			paramsList.add(new NameValuePair("productId", "208511296"));
			paramsList.add(new NameValuePair("type", "kxb"));
			paramsList.add(new NameValuePair("page", "1"));
			paramsList.add(new NameValuePair("rows", String.valueOf(Integer.MAX_VALUE)));

			Page page = getPage(webClient, taskMobile, url, HttpMethod.POST, paramsList, false);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomChongqingParser.getBusinessZ---在用业务情况-----已订购的增值业务" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomChongqingBusiness> list = htmlBusinessParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				webClient.close();
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomChongqingParser.getBusinessZ---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 在用业务----已订购的基础功能
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomChongqingBusiness> getBusinessJ(TaskMobile taskMobile, MessageLogin messageLogin)
			throws Exception {

		tracer.addTag("TelecomChongqingParser.getBusinessJ", taskMobile.getTaskid());

		WebParam<TelecomChongqingBusiness> webParam = new WebParam<TelecomChongqingBusiness>();

		try {
			WebClient webClient = addcookie(taskMobile);
			try {

				String urlfastcode = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=0202";
				HtmlPage html2 = getHtml(urlfastcode, webClient, taskMobile);
				String json2 = html2.getWebResponse().getContentAsString();

			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("TelecomChongqingParser.getBusinessJ.urlfastcode---ERROR:",
						taskMobile.getTaskid() + "---ERROR:" + e);
			}

			String url = "http://cq.189.cn/new-bill/getZZYW";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("accNbr", messageLogin.getName()));
			paramsList.add(new NameValuePair("productId", "208511296"));
			paramsList.add(new NameValuePair("type", "zzyw"));
			paramsList.add(new NameValuePair("page", "1"));
			paramsList.add(new NameValuePair("rows", String.valueOf(Integer.MAX_VALUE)));

			Page page = getPage(webClient, taskMobile, url, HttpMethod.POST, paramsList, false);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomChongqingParser.getBusinessJ---在用业务情况-----已订购的基础功能" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomChongqingBusiness> list = htmlBusinessParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				webClient.close();
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomChongqingParser.getBusinessJ---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析在用业务----已订购的增值业务 解析在用业务----已订购的基础功能
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomChongqingBusiness> htmlBusinessParser(String html, TaskMobile taskMobile) {

		List<TelecomChongqingBusiness> list = new ArrayList<TelecomChongqingBusiness>();
		try {
			TelecomChongqingBusiness telecomChongqingBusiness = null;

			JSONObject jsonObj = JSONObject.fromObject(html);
			String rows = jsonObj.getString("rows");
			JSONArray jsonarray = JSONArray.fromObject(rows);
			for (Object object : jsonarray) {
				JSONObject jsonObj1 = JSONObject.fromObject(object);
				String serv_product_name = jsonObj1.getString("serv_product_name");
				telecomChongqingBusiness = new TelecomChongqingBusiness(taskMobile.getTaskid(), serv_product_name, "",
						"");
				list.add(telecomChongqingBusiness);
			}

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomChongqingParser.htmlBusinessParser---ERROR:",
					taskMobile.getTaskid() + "---ERROR:" + e.toString());
		}

		return list;

	}

	/**
	 * 充值缴费
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomChongqingPay> getPay(TaskMobile taskMobile, MessageLogin messageLogin, String starttime,
			String endtime) throws Exception {

		tracer.addTag("TelecomChongqingParser.getPay", taskMobile.getTaskid());

		WebParam<TelecomChongqingPay> webParam = new WebParam<TelecomChongqingPay>();

		try {
			WebClient webClient = addcookie(taskMobile);
			try {
				String urlfastcode = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02051285";
				HtmlPage html2 = getHtml(urlfastcode, webClient, taskMobile);
				String json2 = html2.getWebResponse().getContentAsString();

			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("TelecomChongqingParser.getPay.urlfastcode---ERROR:",
						taskMobile.getTaskid() + "---ERROR:" + e);
			}

			String url = "http://cq.189.cn/new-pay/allPayQueryPage";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("sTime", starttime));
			paramsList.add(new NameValuePair("eTime", endtime));
			paramsList.add(new NameValuePair("page", "1"));
			paramsList.add(new NameValuePair("rows", String.valueOf(Integer.MAX_VALUE)));

			Page page = getPage(webClient, taskMobile, url, HttpMethod.GET, paramsList, false);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomChongqingParser.getPay---充值缴费" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomChongqingPay> list = htmlPayParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				webClient.close();
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomChongqingParser.getPay---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析充值缴费
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomChongqingPay> htmlPayParser(String html, TaskMobile taskMobile) {

		List<TelecomChongqingPay> list = new ArrayList<TelecomChongqingPay>();
		try {
			TelecomChongqingPay telecomChongqingPay = null;

			JSONObject jsonObj = JSONObject.fromObject(html);
			String rows = jsonObj.getString("rows");
			JSONArray jsonarray = JSONArray.fromObject(rows);
			for (Object object : jsonarray) {
				JSONObject jsonObj1 = JSONObject.fromObject(object);
				String time = jsonObj1.getString("time");
				String order = jsonObj1.getString("order");
				String state = jsonObj1.getString("state");
				String money = jsonObj1.getString("money");
				String from = jsonObj1.getString("from");
				String type = jsonObj1.getString("type");
				telecomChongqingPay = new TelecomChongqingPay(taskMobile.getTaskid(), time, order, state, money, from,
						type);
				list.add(telecomChongqingPay);
			}

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomChongqingParser.htmlPayParser---ERROR:",
					taskMobile.getTaskid() + "---ERROR:" + e.toString());
		}

		return list;

	}

	/**
	 * 通话详单
	 * @param taskMobile
	 * @param messageLogin
	 * @param yearmonth
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws InterruptedException 
	 * @throws Exception
	 */
	//maxAttempts表示重试次数，multiplier即指定延迟倍数，比如delay=5000l,multiplier=2,则第一次重试为5秒，第二次为10秒，第三次为20秒
	//backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为1000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为1秒，第二次为1.5秒，第三次为2.25秒。
	@Retryable(value={RuntimeException.class,},maxAttempts=3,backoff = @Backoff(delay = 1500l,multiplier = 1.5))
	public WebParam<TelecomChongqingCallRecord> getCallRecord(TaskMobile taskMobile, MessageLogin messageLogin,
			String yearmonth, String beginTime, String endTime){
		
		tracer.addTag("TelecomChongqingParser.getCallRecord" + yearmonth, taskMobile.getTaskid());

		WebParam<TelecomChongqingCallRecord> webParam = new WebParam<TelecomChongqingCallRecord>();

//		try {
		WebClient webClient = addcookie(taskMobile);
		String tname = taskMobile.getBasicUser().getName();
		String idcard = taskMobile.getBasicUser().getIdnum();
		if (tname.length() > 0) {
			tname = tname.substring(1);
		}
		if (idcard.length() >= 6) {
			idcard = idcard.substring(idcard.length() - 6);
		}
		try {
			String url1 = "http://cq.189.cn/new-bill/bill_XDCXNR";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("accNbr", messageLogin.getName()));
			paramsList.add(new NameValuePair("productId", "208511296"));
			paramsList.add(new NameValuePair("month", yearmonth));
			paramsList.add(new NameValuePair("callType", "00"));
			paramsList.add(new NameValuePair("listType", "300001"));
			paramsList.add(new NameValuePair("beginTime", beginTime));
			paramsList.add(new NameValuePair("endTime", endTime));
			paramsList.add(new NameValuePair("rc", messageLogin.getSms_code()));
			paramsList.add(new NameValuePair("tname", tname));
			paramsList.add(new NameValuePair("idcard", idcard));
			paramsList.add(new NameValuePair("zq", "2"));
			Page html1 = getPage(webClient, taskMobile, url1, HttpMethod.POST, paramsList, true);
			String json1 = html1.getWebResponse().getContentAsString();
			if (json1.contains("跳转中") || json1.contains("没有查到您的清单数据")) {
				webParam.setPage(html1);
				webParam.setHtml(json1);
				webParam.setUrl(url1);
				webParam.setCode(201);
				webClient.close();
				return webParam;
			}

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomChongqingParser.getCallRecord.bill_XDCXNR---ERROR:" + yearmonth,
					taskMobile.getTaskid() + "---ERROR:" + e);
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		String url = "http://cq.189.cn/new-bill/bill_XDCX_Page";
		List<NameValuePair> paramsList2 = new ArrayList<NameValuePair>();
		paramsList2.add(new NameValuePair("page", "1"));
		paramsList2.add(new NameValuePair("rows", String.valueOf(Integer.MAX_VALUE)));
		Page page = null;
		try {
			page = getPage(webClient, taskMobile, url, HttpMethod.POST, paramsList2, true);
		} catch (Exception e) {
			tracer.addTag("TelecomChongqingParser.getCallRecord.bill_XDCX_Page---ERROR:" + yearmonth,
					taskMobile.getTaskid() + "---ERROR:" + e);
		}

		if (null != page) {
			String html = page.getWebResponse().getContentAsString();
			tracer.addTag("TelecomChongqingParser.getCallRecord---通话详单" + yearmonth + taskMobile.getTaskid(),
					"<xmp>" + html + "</xmp>");
			List<TelecomChongqingCallRecord> list = htmlCallRecordParser(html, taskMobile);
			webParam.setPage(page);
			webParam.setHtml(html);
			webParam.setList(list);
			webParam.setUrl(page.getUrl().toString());
			webParam.setCode(page.getWebResponse().getStatusCode());
			webClient.close();
			return webParam;
		} else {
			tracer.addTag("通话记录重试月份" + yearmonth, " 重试机制触发 URL" + url);
			throw new RuntimeException("重试机制触发！");
		}
//		} catch (Exception e) {
//			tracer.addTag("TelecomChongqingParser.getCallRecord---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e.toString());
//			e.printStackTrace();
//		}
//		return null;
	}

	/**
	 * 解析通话详单
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomChongqingCallRecord> htmlCallRecordParser(String html, TaskMobile taskMobile) {

		List<TelecomChongqingCallRecord> list = new ArrayList<TelecomChongqingCallRecord>();
		try {
			TelecomChongqingCallRecord telecomChongqingCallRecord = null;

			JSONObject jsonObj = JSONObject.fromObject(html);
			String rows = jsonObj.getString("rows");
			JSONArray jsonarray = JSONArray.fromObject(rows);
			for (Object object : jsonarray) {
				JSONObject jsonObj1 = JSONObject.fromObject(object);
				String callMobile = jsonObj1.getString("对方号码");
				String callType = jsonObj1.getString("呼叫类型");
				String callTime = jsonObj1.getString("起始时间");
				String callTimeCost = jsonObj1.getString("通话时长（秒）");
				String callFee = jsonObj1.getString("费用（元）");
				String callStyle = jsonObj1.getString("通话类型");
				String callArea = jsonObj1.getString("使用地点");
				String inpackage = jsonObj1.getString("是否在套餐内");
				if(!callMobile.equals("合计")){
					telecomChongqingCallRecord = new TelecomChongqingCallRecord(taskMobile.getTaskid(), callMobile, callType, callTime,
							callTimeCost, callFee, callStyle, callArea, inpackage);
					list.add(telecomChongqingCallRecord);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomChongqingParser.htmlCallRecordParser---ERROR:",
					taskMobile.getTaskid() + "---ERROR:" + e.toString());
		}

		return list;

	}
	
	
	
	/**
	 * 短信详单
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	//maxAttempts表示重试次数，multiplier即指定延迟倍数，比如delay=5000l,multiplier=2,则第一次重试为5秒，第二次为10秒，第三次为20秒
	//backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为1000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为1秒，第二次为1.5秒，第三次为2.25秒。
	@Retryable(value={RuntimeException.class,},maxAttempts=3,backoff = @Backoff(delay = 1500l,multiplier = 1.5))
	public WebParam<TelecomChongqingMessage> getMessage(TaskMobile taskMobile, MessageLogin messageLogin,
			String yearmonth, String beginTime, String endTime) {
		
		tracer.addTag("TelecomChongqingParser.getMessage"+yearmonth, taskMobile.getTaskid());

		WebParam<TelecomChongqingMessage> webParam = new WebParam<TelecomChongqingMessage>();
		WebClient webClient = addcookie(taskMobile);
//		try {
//			try {
//				String url3 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02031273";
//				HtmlPage html3 = getHtml(url3, webClient, taskMobile);
//				String json3 = html3.getWebResponse().getContentAsString();
//
//			} catch (Exception e) {
//				e.printStackTrace();
//				tracer.addTag("TelecomChongqingParser.getMessage.url3---ERROR:",
//						taskMobile.getTaskid() + "---ERROR:" + e.toString());
//			}

		String tname = taskMobile.getBasicUser().getName();
		String idcard = taskMobile.getBasicUser().getIdnum();
		if (tname.length() > 0) {
			tname = tname.substring(1);
		}
		if (idcard.length() >= 6) {
			idcard = idcard.substring(idcard.length() - 6);
		}
		try {
			String url1 = "http://cq.189.cn/new-bill/bill_XDCXNR";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("accNbr", messageLogin.getName()));
			paramsList.add(new NameValuePair("productId", "208511296"));
			paramsList.add(new NameValuePair("month", yearmonth));
			paramsList.add(new NameValuePair("callType", "01"));
			paramsList.add(new NameValuePair("listType", "300002"));
			paramsList.add(new NameValuePair("beginTime", beginTime));
			paramsList.add(new NameValuePair("endTime", endTime));
			paramsList.add(new NameValuePair("rc", messageLogin.getSms_code()));
			paramsList.add(new NameValuePair("tname", tname));
			paramsList.add(new NameValuePair("idcard", idcard));
			paramsList.add(new NameValuePair("zq", "2"));
			Page html1 = getPage(webClient, taskMobile, url1, HttpMethod.POST, paramsList, true);
			String json1 = html1.getWebResponse().getContentAsString();
			if (json1.contains("跳转中") || json1.contains("没有查到您的清单数据")) {
				webParam.setPage(html1);
				webParam.setHtml(json1);
				webParam.setUrl(url1);
				webParam.setCode(201);
				webClient.close();
				return webParam;
			}

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomChongqingParser.getMessage.bill_XDCXNR---ERROR:" + yearmonth,
					taskMobile.getTaskid() + "---ERROR:" + e.toString());
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String url = "http://cq.189.cn/new-bill/bill_XDCX_Page";
		List<NameValuePair> paramsList2 = new ArrayList<NameValuePair>();
		paramsList2.add(new NameValuePair("page", "1"));
		paramsList2.add(new NameValuePair("rows", String.valueOf(Integer.MAX_VALUE)));

		Page page = null;
		try {
			page = getPage(webClient, taskMobile, url, HttpMethod.POST, paramsList2, true);
		} catch (Exception e) {
			tracer.addTag("TelecomChongqingParser.getMessage.bill_XDCX_Page---ERROR:" + yearmonth,
					taskMobile.getTaskid() + "---ERROR:" + e);
		}

		if (null != page) {
			String html = page.getWebResponse().getContentAsString();
			tracer.addTag("TelecomChongqingParser.getMessage---短信详单" + yearmonth + taskMobile.getTaskid(),
					"<xmp>" + html + "</xmp>");
			List<TelecomChongqingMessage> list = htmlMessageParser(html, taskMobile);
			webParam.setPage(page);
			webParam.setHtml(html);
			webParam.setList(list);
			webParam.setUrl(page.getUrl().toString());
			webParam.setCode(page.getWebResponse().getStatusCode());
			webClient.close();
			return webParam;
		} else {
			tracer.addTag("短信记录重试月份" + yearmonth, " 重试机制触发 URL" + url);
			throw new RuntimeException("重试机制触发！");
		}
//		} catch (Exception e) {
//			tracer.addTag("TelecomChongqingParser.getMessage---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e.toString());
//			e.printStackTrace();
//		}
//		return null;
	}

	/**
	 * 解析短信详单
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomChongqingMessage> htmlMessageParser(String html, TaskMobile taskMobile) {

		List<TelecomChongqingMessage> list = new ArrayList<TelecomChongqingMessage>();
		try {
			TelecomChongqingMessage telecomChongqingMessage = null;

			JSONObject jsonObj = JSONObject.fromObject(html);
			String rows = jsonObj.getString("rows");
			JSONArray jsonarray = JSONArray.fromObject(rows);
			for (Object object : jsonarray) {
				JSONObject jsonObj1 = JSONObject.fromObject(object);
				String smsType = jsonObj1.getString("业务类型");
				String smsMobile = jsonObj1.getString("对方号码");
				String smsTime = jsonObj1.getString("发送时间");
				String smsCost = jsonObj1.getString("费用（元）");
				String roam = jsonObj1.getString("漫游状态");
				String inpackage = jsonObj1.getString("是否在套餐内");
				if(!smsType.equals("合计")){
					telecomChongqingMessage = new TelecomChongqingMessage(taskMobile.getTaskid(), smsType, smsMobile,
							smsTime, smsCost, roam, inpackage);
					list.add(telecomChongqingMessage);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomChongqingParser.htmlMessageParser---ERROR:",
					taskMobile.getTaskid() + "---ERROR:" + e.toString());
		}

		return list;

	}
	
	
	/**
	 * 上网详单
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomChongqingFlow> getFlow(TaskMobile taskMobile, MessageLogin messageLogin,
			String yearmonth, String beginTime, String endTime) throws Exception {
		
		tracer.addTag("TelecomChongqingParser.getFlow", taskMobile.getTaskid());

		WebParam<TelecomChongqingFlow> webParam = new WebParam<TelecomChongqingFlow>();

		try {
			WebClient webClient = addcookie(taskMobile);
			
//			try {
//				String url3 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02031273";
//				HtmlPage html3 = getHtml(url3, webClient, taskMobile);
//				String json3 = html3.getWebResponse().getContentAsString();
//
//			} catch (Exception e) {
//				e.printStackTrace();
//				tracer.addTag("TelecomChongqingParser.getFlow.url3---ERROR:",
//						taskMobile.getTaskid() + "---ERROR:" + e.toString());
//			}

			String tname = taskMobile.getBasicUser().getName();
			String idcard = taskMobile.getBasicUser().getIdnum();
			if (tname.length() > 0) {
				tname = tname.substring(1);
			}
			if (idcard.length() >= 6) {
				idcard = idcard.substring(idcard.length() - 6);
			}
			try {
				String url1 = "http://cq.189.cn/new-bill/bill_XDCXNR";
				List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
				paramsList.add(new NameValuePair("accNbr", messageLogin.getName()));
				paramsList.add(new NameValuePair("productId", "208511296"));
				paramsList.add(new NameValuePair("month", yearmonth));
				paramsList.add(new NameValuePair("callType", "01"));
				paramsList.add(new NameValuePair("listType", "300003"));
				paramsList.add(new NameValuePair("beginTime", beginTime));
				paramsList.add(new NameValuePair("endTime", endTime));
				paramsList.add(new NameValuePair("rc", messageLogin.getSms_code()));
				paramsList.add(new NameValuePair("tname", tname));
				paramsList.add(new NameValuePair("idcard", idcard));
				paramsList.add(new NameValuePair("zq", "2"));
				Page html1 = getPage(webClient, taskMobile, url1, HttpMethod.POST, paramsList, true);
				String json1 = html1.getWebResponse().getContentAsString();
				if(json1.contains("跳转中") || json1.contains("没有查到您的清单数据")){
					webParam.setPage(html1);
					webParam.setHtml(json1);
					webParam.setUrl(url1);
					webParam.setCode(201);
					return webParam;
				}

			} catch (Exception e) {
				
				e.printStackTrace();
				tracer.addTag("TelecomChongqingParser.getFlow.bill_XDCXNR--1次---ERROR:",
						taskMobile.getTaskid() + "---ERROR:" + e.toString());
			}
			
			Thread.sleep(1000);
			String url = "http://cq.189.cn/new-bill/bill_XDCX_Page";
			List<NameValuePair> paramsList2 = new ArrayList<NameValuePair>();
			paramsList2.add(new NameValuePair("page", "1"));
			paramsList2.add(new NameValuePair("rows", String.valueOf(Integer.MAX_VALUE)));

			Page page = getPage(webClient, taskMobile, url, HttpMethod.POST, paramsList2, true);
			
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomChongqingParser.getFlow---上网详单" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomChongqingFlow> list = htmlFlowParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomChongqingParser.getFlow---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e.toString());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析上网详单
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomChongqingFlow> htmlFlowParser(String html, TaskMobile taskMobile) {

		List<TelecomChongqingFlow> list = new ArrayList<TelecomChongqingFlow>();
		try {
			TelecomChongqingFlow telecomChongqingFlow = null;

			JSONObject jsonObj = JSONObject.fromObject(html);
			String rows = jsonObj.getString("rows");
			JSONArray jsonarray = JSONArray.fromObject(rows);
			for (Object object : jsonarray) {
				JSONObject jsonObj1 = JSONObject.fromObject(object);
				String netTime = jsonObj1.getString("开始时间");
				String netTimeCost = jsonObj1.getString("上网时长（秒）");
				String netFlow = jsonObj1.getString("流量（KB）");
				String netType = jsonObj1.getString("网络类型");
				String netArea = jsonObj1.getString("通信地点");
				String netBusiness = jsonObj1.getString("业务类型");
				String netFee = jsonObj1.getString("费用（元）");
				if(!netTime.equals("合计")){
					telecomChongqingFlow = new TelecomChongqingFlow(taskMobile.getTaskid(), netTime, netTimeCost, netFlow,
							netType, netArea, netBusiness, netFee);
					list.add(telecomChongqingFlow);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomChongqingParser.htmlFlowParser---ERROR:",
					taskMobile.getTaskid() + "---ERROR:" + e.toString());
		}

		return list;

	}
	
	
	
	/**
	 * 增值详单
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomChongqingIncrement> getIncrementBusiness(TaskMobile taskMobile, MessageLogin messageLogin,
			String yearmonth, String beginTime, String endTime) throws Exception {
		
		tracer.addTag("TelecomChongqingParser.getIncrementBusiness", taskMobile.getTaskid());

		WebParam<TelecomChongqingIncrement> webParam = new WebParam<TelecomChongqingIncrement>();

		try {
			WebClient webClient = addcookie(taskMobile);
			
//			try {
//				String url3 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02031273";
//				HtmlPage html3 = getHtml(url3, webClient, taskMobile);
//				String json3 = html3.getWebResponse().getContentAsString();
//
//			} catch (Exception e) {
//				e.printStackTrace();
//				tracer.addTag("TelecomChongqingParser.getIncrementBusiness.url3---ERROR:",
//						taskMobile.getTaskid() + "---ERROR:" + e.toString());
//			}

			String tname = taskMobile.getBasicUser().getName();
			String idcard = taskMobile.getBasicUser().getIdnum();
			if (tname.length() > 0) {
				tname = tname.substring(1);
			}
			if (idcard.length() >= 6) {
				idcard = idcard.substring(idcard.length() - 6);
			}
			try {
				String url1 = "http://cq.189.cn/new-bill/bill_XDCXNR";
				List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
				paramsList.add(new NameValuePair("accNbr", messageLogin.getName()));
				paramsList.add(new NameValuePair("productId", "208511296"));
				paramsList.add(new NameValuePair("month", yearmonth));
				paramsList.add(new NameValuePair("callType", "02"));
				paramsList.add(new NameValuePair("listType", "300004"));
				paramsList.add(new NameValuePair("beginTime", beginTime));
				paramsList.add(new NameValuePair("endTime", endTime));
				paramsList.add(new NameValuePair("rc", messageLogin.getSms_code()));
				paramsList.add(new NameValuePair("tname", tname));
				paramsList.add(new NameValuePair("idcard", idcard));
				paramsList.add(new NameValuePair("zq", "2"));
				Page html1 = getPage(webClient, taskMobile, url1, HttpMethod.POST, paramsList, true);
				String json1 = html1.getWebResponse().getContentAsString();
				if(json1.contains("跳转中") || json1.contains("没有查到您的清单数据")){
					webParam.setPage(html1);
					webParam.setHtml(json1);
					webParam.setUrl(url1);
					webParam.setCode(201);
					webClient.close();
					return webParam;
				}

			} catch (Exception e) {
				
				e.printStackTrace();
				tracer.addTag("TelecomChongqingParser.getIncrementBusiness.bill_XDCXNR--1次---ERROR:",
						taskMobile.getTaskid() + "---ERROR:" + e.toString());
			}
			
			Thread.sleep(1000);
			String url = "http://cq.189.cn/new-bill/bill_XDCX_Page";
			List<NameValuePair> paramsList2 = new ArrayList<NameValuePair>();
			paramsList2.add(new NameValuePair("page", "1"));
			paramsList2.add(new NameValuePair("rows", String.valueOf(Integer.MAX_VALUE)));

			Page page = getPage(webClient, taskMobile, url, HttpMethod.POST, paramsList2, true);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomChongqingParser.getIncrementBusiness---增值详单" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomChongqingIncrement> list = htmlIncrementBusinessParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				webClient.close();
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomChongqingParser.getIncrementBusiness---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e.toString());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析增值详单
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomChongqingIncrement> htmlIncrementBusinessParser(String html, TaskMobile taskMobile) {

		List<TelecomChongqingIncrement> list = new ArrayList<TelecomChongqingIncrement>();
		try {
			TelecomChongqingIncrement telecomChongqingIncrementBusiness = null;

			JSONObject jsonObj = JSONObject.fromObject(html);
			String rows = jsonObj.getString("rows");
			JSONArray jsonarray = JSONArray.fromObject(rows);
			for (Object object : jsonarray) {
				JSONObject jsonObj1 = JSONObject.fromObject(object);
				String businessName = jsonObj1.getString("业务名称");
				String businessCode = jsonObj1.getString("业务代码/号码");
				String chargetype = jsonObj1.getString("计费类型");
				String useTime = jsonObj1.getString("使用时间");
				String callTimeCost = jsonObj1.getString("通话时长（秒）");
				String fee = jsonObj1.getString("费用（元）");
				if(!businessName.equals("合计")){
					telecomChongqingIncrementBusiness = new TelecomChongqingIncrement(
							taskMobile.getTaskid(), businessName, businessCode, chargetype, useTime, callTimeCost, fee);
					list.add(telecomChongqingIncrementBusiness);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomChongqingParser.htmlIncrementBusinessParser---ERROR:",
					taskMobile.getTaskid() + "---ERROR:" + e.toString());
		}

		return list;

	}

}
