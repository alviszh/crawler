package app.parser;

import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jettison.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuBusiness;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuCall;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuMessage;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuPay;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuPhoneBill;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuPhoneMonthBill;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuScore;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuUserInfo;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.TelecomRetryGanSuService;
import net.sf.json.JSONObject;

@Component
public class TelecomGansuParser {

	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomRetryGanSuService telecomRetryGanSuService;
	@Autowired
	private TracerLog tracer;

	/**
	 * 登陆
	 * 
	 * @param mobileLogin
	 * @return
	 * @throws Exception
	 */
	public WebParam login(MessageLogin mobileLogin) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://login.189.cn/login";
		HtmlPage html = getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
		username.setText(mobileLogin.getName());
		passwordInput.setText(mobileLogin.getPassword());

		HtmlPage htmlpage = button.click();
		WebParam webParam = new WebParam();
		int statusCode = htmlpage.getWebResponse().getStatusCode();
		if (statusCode != 200) {
			if (htmlpage.asXml().indexOf("登录失败") != -1) {
				tracer.addTag("parser.login.error", mobileLogin.getName());
				webParam.setHtml(htmlpage.asXml());
			}
		}
		webParam.setPage(htmlpage);
		webParam.setWebClient(webClient);
		return webParam;
	}

	private void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}

	public HtmlPage getHtml(String url, WebClient webClient) {
		webClient.getCache().setMaxSize(0);

		HtmlPage searchPage = null;
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			searchPage = webClient.getPage(webRequest);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.crawler.ERROR", "ERROR");
		}

		return searchPage;
	}

	public Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			webClient.getCache().setMaxSize(0);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			if (searchPage == null) {
				return null;
			}
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public WebClient addcookie(WebClient webclient, TaskMobile taskMobile) {

		Type founderSetType = new TypeToken<HashSet<CookieJson>>() {
		}.getType();

		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getNexturl());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webclient.getCookieManager().addCookie(i.next());
		}

		return webclient;
	}

	public WebClient getWebClient(Set<Cookie> cookies) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		return webClient;
	}

	// 获得通用的cookie来实现不许用验证码
	public WebClient getMyWeclient(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		// webClient.getCache().setMaxSize(0);
		webClient = addcookie(webClient, taskMobile);
		String url = "http://www.189.cn/dqmh/my189/initMy189home.do";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);
		int code = page.getWebResponse().getStatusCode();
		WebClient weClientmy89 = null;
		if (code == 200) {
			String cookie = CommonUnit.transcookieToJson(page.getWebClient());
			Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookie);
			weClientmy89 = getWebClient(cookies);
		}
		return weClientmy89;

	}

	public WebClient getMyWebclientThree(MessageLogin messageLogin, TaskMobile taskMobile) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getCache().setMaxSize(0);
		webClient = addcookie(webClient, taskMobile);
		String url = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000071";

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage page = webClient.getPage(webRequest);
			int code = page.getWebResponse().getStatusCode();
			// WebClient weClientmy89 = null;
			if (code == 200 && null != page) {
				String cookie = CommonUnit.transcookieToJson(page.getWebClient());
				Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookie);
				WebClient weClientmy89 = getWebClient(cookies);
				// 缴费信息
				String url1 = "http://gs.189.cn/web/pay2/dealCheckV7.action?numberType=4%3A" + messageLogin.getName()
						+ "&beginTime=" + getDateBefore("yyyyMM", 1) + "&endTime=" + getDateBefore("yyyyMM", 1)
						+ "&productInfo=4%3A" + messageLogin.getName()
						+ "&validatecode=&busitype=3&mobilenum=&rand=Name";
				// WebClient webClient2 = page.getWebClient();
				webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
				HtmlPage page2 = weClientmy89.getPage(webRequest);
				WebClient webClient3 = page2.getWebClient();
				int code1 = page.getWebResponse().getStatusCode();
				if (null != page2 && code1 == 200) {
					return webClient3;
				}
			}
		} catch (Exception e) {
			tracer.addTag("parser.crawler.getTY", messageLogin.getTask_id());
		}

		return null;
	}

	/**
	 * 解析个人信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @param i
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomGansuUserInfo> getUserTelecomInfo(MessageLogin messageLogin, TaskMobile taskMobile,
			WebClient webClient) throws Exception {
		TelecomGansuUserInfo telecomGansuUserInfo = new TelecomGansuUserInfo();
		WebParam<TelecomGansuUserInfo> webParam = new WebParam<TelecomGansuUserInfo>();
		 String url = "http://gs.189.cn/web/self/showMyProfileV7.action";
//		String url = "http://gs.189.cn/web/jsonV6/getUserInfo.action";
		// WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page3 = webClient.getPage(url);
		Thread.sleep(5000);
//		if (null != page3 && page3.getWebResponse().getContentAsString().contains("user")) {
			if (null != page3 && page3.getWebResponse().getContentAsString().contains("用户信息")) {
			 Document doc =Jsoup.parse(page3.getWebResponse().getContentAsString());
			 System.out.println(page3.getWebResponse().getContentAsString());
			 String name = getNextLabelByKeyword(doc, "联系人");
			 String addr = getNextLabelByKeyword(doc, "联系地址");
			 String postalcode = getNextLabelByKeyword(doc, "邮箱");
			 String phone = doc.select("span.orgff8200").get(0).text();
			 String lineNum =
			 doc.select("table.colum4").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(1).getElementsByTag("td").get(1).text();
			 String cardType =
			 doc.getElementsByClass("colum4").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(1).getElementsByTag("td").get(2).text();
			 String cardNumber =
			 doc.getElementsByClass("colum4").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(1).getElementsByTag("td").get(3).text();
			 telecomGansuUserInfo.setPhone(phone.substring(1,phone.length()-1));
			 telecomGansuUserInfo.setName(name);
			 telecomGansuUserInfo.setCardNum(cardNumber);
			 telecomGansuUserInfo.setAddr(addr);
			 telecomGansuUserInfo.setCardType(cardType);
			 telecomGansuUserInfo.setLineNum(lineNum);
			 telecomGansuUserInfo.setPostalcode(postalcode);
//			JSONObject fromObject = JSONObject.fromObject(page3.getWebResponse().getContentAsString());
//			String string = fromObject.getString("user");
//			// System.out.println(string);
//			JSONObject fromObject2 = JSONObject.fromObject(string);
//			String cardNumber = fromObject2.getString("cardNumber");
//			String cardType = fromObject2.getString("cardType");
//			String contactphone = fromObject2.getString("contactphone");
//			String custname = fromObject2.getString("custname");
//			String orgname = fromObject2.getString("orgname");
//			String orgcode = fromObject2.getString("orgcode");
//			telecomGansuUserInfo.setPhone(contactphone);
//			telecomGansuUserInfo.setName(custname);
//			telecomGansuUserInfo.setCardNum(cardNumber);
//			if (cardType.contains("1")) {
//				telecomGansuUserInfo.setCardType("身份证");
//			} else {
//				telecomGansuUserInfo.setCardType("其他证件");
//			}
//			telecomGansuUserInfo.setPostalcode(orgcode);
//			telecomGansuUserInfo.setCustName(orgname);
			telecomGansuUserInfo.setTaskid(messageLogin.getTask_id());
			webParam.setTelecomGansuUserInfo(telecomGansuUserInfo);
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			return webParam;
		}
		return null;
	}

	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("td:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}

	// 开始时间
	public String getTime() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM-dd hh:mm:ss");
		// 当前时间和十年前时间
		String hehe = dateFormat.format(now);
		String endtime = hehe.substring(0, 6);
		int startint = Integer.parseInt(endtime);
		int start1 = (startint - 1000);
		String starttime = start1 + "";
		return starttime;
	}

	// 结束时间
	public String getOldTime() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");

		String hehe = dateFormat.format(now);
		return hehe;
	}

	// 交费时间
	public static String getPayTime(int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}

	// 积分
	public WebParam<TelecomGansuScore> getJiFenInfo(MessageLogin messageLogin, TaskMobile taskMobile,
			WebClient webClient) throws Exception {
		String url = "http://gs.189.cn/web/self/scoreSearchV7.action?productGroup=4%3A" + messageLogin.getName()
				+ "&beginDate=" + getDateBefore("yyyyMM", 6) + "&endDate=" + getDateBefore("yyyyMM", 0)
				+ "&fastcode=10000595&cityCode=gs";
		String url1 = "http://gs.189.cn/web/self/scoreSearchV7.action?productGroup=4%3A" + messageLogin.getName()
				+ "&beginDate=" + getDateBefore("yyyyMM", 6) + "&endDate=" + getDateBefore("yyyyMM", 0);

		WebParam<TelecomGansuScore> webParam = new WebParam<TelecomGansuScore>();
		WebClient client = WebCrawler.getInstance().getNewWebClient();
		WebClient client2 = addcookie(client, taskMobile);
		Page page = null;
		int code = 0;
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.GET);
		if (webClient != null) {
			page = webClient.getPage(webRequest);
			code = page.getWebResponse().getStatusCode();
			System.out.println(page.getWebResponse().getContentAsString());
			if (page.getWebResponse().getContentAsString().contains("当前可用积分")) {
				Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
				Elements elementById = doc.getElementsByClass("feesearch_result");
				Elements elementsByTag = elementById.get(0).getElementsByTag("td");
				String text = elementsByTag.get(0).text();
				String[] split = text.split("年底到期积分");
				Pattern pattern = Pattern.compile("[^0-9]");
				Matcher matcher = pattern.matcher(split[0]);
				String all = matcher.replaceAll("");
				TelecomGansuScore t = new TelecomGansuScore();
				t.setTaskid(taskMobile.getTaskid());
				t.setMonth("半年");
				t.setSumScore(all);
				webParam.setTelecomGansuScore(t);
				webParam.setHtml(page.getWebResponse().getContentAsString());
				webParam.setUrl(url);
			}
			else if (null != page && code == 200) {
				String[] split = page.getWebResponse().getContentAsString().split("detailStr");
				// for (int i = 1; i < split.length; i++) {
				// System.out.println(split[i]);
				// }
				TelecomGansuScore telecomGansuScore = null;
				StringBuilder sb = new StringBuilder();
				for (String string : split) {
					sb.append(string);
				}
				String sb1 = sb.toString();
				String[] split2 = sb1.split("\\+=");
				// for (int i = 1; i < split2.length; i++) {
				// System.out.println(split2[i]);
				// }
				StringBuilder sb2 = new StringBuilder();
				for (String string1 : split2) {
					sb2.append(string1);
				}
				String sb3 = sb2.toString();
				String[] split3 = sb3.split("<p>");

				StringBuilder sb4 = new StringBuilder();
				for (int i = 1; i < split3.length; i++) {
					// System.out.println(split3[i]);
					sb4.append(split3[i]);
				}
				String sb5 = sb4.toString();
				String[] split4 = sb5.split("</p>");

				StringBuilder sb6 = new StringBuilder();
				for (int i = 0; i < split4.length - 1; i++) {
					// System.out.println(split4[i]);
					sb6.append(split4[i]);
				}
				String sb7 = sb6.toString();
				String replaceAll = sb7.replaceAll("</td>+", "").replaceAll("\"\\+\"", "")
						.replaceAll("<td align='center'>", "").replaceAll("\\s*", "").replaceAll(";", "")
						.replaceAll("\"\"", "").replaceAll("</tr><tr>", "").replaceAll("\\+", "");
				String[] split5 = replaceAll.split("\"\"");
				List<TelecomGansuScore> list = new ArrayList<TelecomGansuScore>();
				StringBuilder sb8 = new StringBuilder();
				for (int i = 0; i < split5.length - 1; i++) {
					if (split5[i].trim().length() > 0) {
						sb8.append(split5[i] + ",");
					}
				}
				String string8 = sb8.toString();
				String[] split6 = string8.split(",");
				for (int i = 0; i < split6.length - 4; i = i + 4) {
					telecomGansuScore = new TelecomGansuScore();
					telecomGansuScore.setMonth(split6[i]);
					telecomGansuScore.setSendScore(split6[i + 1]);
					telecomGansuScore.setRewardScore(split6[i + 2]);
					telecomGansuScore.setSumScore(split6[i + 3]);
					telecomGansuScore.setTaskid(messageLogin.getTask_id());
					list.add(telecomGansuScore);
				}
				webParam.setList(list);
				webParam.setHtml(page.getWebResponse().getContentAsString());
				// webParam.setPage(page);
				webParam.setUrl(url);
				webParam.setCode(code);
			}
		}
		return webParam;
	}

	// 业务
	public WebParam<TelecomGansuBusiness> getBusinessInfo(MessageLogin messageLogin, TaskMobile taskMobile,
			WebClient webClient) throws Exception {
		WebParam<TelecomGansuBusiness> webParam = new WebParam<TelecomGansuBusiness>();
		TelecomGansuBusiness telecomGansuBusiness = null; // 手机：双层加密
		String url1 = "http://gs.189.cn/web/change/functionChoiceUnsubscribeQueryV7.action?productInfo=4%3A"
				+ messageLogin.getName() + "&checkedProductName=%25E6%2589%258B%25E6%259C%25BA%25EF%25BC%259A"
				+ messageLogin.getName() + "&functionEnterId=4&businessId=null";
		if (null != webClient) {
			// HtmlPage page2 = webClient.getPage(url1);
			HtmlPage page2 = telecomRetryGanSuService.getBusinessRetry(taskMobile, messageLogin, url1, webClient);
			// System.out.println(page2.getWebResponse().getContentAsString());
			int code2 = page2.getWebResponse().getStatusCode();
			if (null != page2 & code2 == 200) {
				HtmlDivision object = page2.getFirstByXPath("//div[@class='feesearch_con']");
				Document doc = Jsoup.parse(object.asXml());
				// String thead =
				// doc.getElementsByTag("table").get(0).getElementsByTag("thead").text();//thead
				Elements trs = doc.getElementsByTag("table").get(0).getElementsByTag("tbody").get(0)
						.getElementsByTag("tr");// 第一个 tbody
				List<TelecomGansuBusiness> txt = new ArrayList<TelecomGansuBusiness>();
				for (Element element : trs) {
					Elements tds = element.getElementsByTag("td");
					if (tds.size() > 2) {
						String businessName = tds.get(0).text();
						String businessTime = tds.get(1).text();
						String businessInfo = tds.get(2).text();

						telecomGansuBusiness = new TelecomGansuBusiness(businessName, businessTime, businessInfo,
								messageLogin.getTask_id());
						txt.add(telecomGansuBusiness);
					}
				}

				Elements trs1 = doc.getElementsByTag("table").get(0).getElementsByTag("tbody").get(1)
						.getElementsByTag("tr");// 第二个tbody
				for (Element element : trs1) {
					Elements tds = element.getElementsByTag("td");
					if (tds.size() > 2) {
						String businessName = tds.get(0).text();
						String businessTime = tds.get(1).text();
						String businessInfo = tds.get(2).text();

						telecomGansuBusiness = new TelecomGansuBusiness(businessName, businessTime, businessInfo,
								messageLogin.getTask_id());
						txt.add(telecomGansuBusiness);
					}
				}
				webParam.setTelecomGansuBusiness(telecomGansuBusiness);
				webParam.setList(txt);
				webParam.setHtml(page2.getWebResponse().getContentAsString());
				webParam.setPage(page2);
				webParam.setUrl(url1);
				webParam.setCode(code2);
				return webParam;
			}
		}
		return null;
	}

	/**
	 * 缴费信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomGansuPay> getPayMentInfo(MessageLogin messageLogin, TaskMobile taskMobile,
			WebClient webClient, int i) throws Exception {
		TelecomGansuPay telecomGansuPay = null;
		WebParam<TelecomGansuPay> webParam = new WebParam<TelecomGansuPay>();
		// 缴费信息
		// String u ="http://gs.189.cn/web/pay2/dealTurnV7.action";
		// Page page2 = webClient.getPage(u);
		// Document doc1 =
		// Jsoup.parse(page2.getWebResponse().getContentAsString());
		// System.out.println(page2.getWebResponse().getContentAsString());
		// Element elementById = doc1.getElementById("select_option");
		// Elements elementsByTag = elementById.getElementsByTag("input");
		// String numberType11 = URLEncoder.encode(elementsByTag.get(0).val(),
		// "UTF-8");
		// String numberType12 = URLEncoder.encode(elementsByTag.get(1).val(),
		// "UTF-8");
		// String numberType13 = URLEncoder.encode(elementsByTag.get(2).val(),
		// "UTF-8");
		// String numberType14 = URLEncoder.encode(elementsByTag.get(3).val(),
		// "UTF-8");
		String url = "http://gs.189.cn/web/pay2/dealCheckV7.action?numberType=4%3A" + messageLogin.getName()
				+ "&beginTime=" + getPayTime(i) + "&endTime=" + getPayTime(i) + "&productInfo=4%3A"
				+ messageLogin.getName() + "&validatecode=&busitype=3&mobilenum=&rand=Name";
		// String url
		// ="http://gs.189.cn/web/pay2/dealCheckV7.action?numberType1="+numberType11+"&numberType1="+numberType12+"&numberType1="+numberType13+"&numberType1="+numberType14+"&numberType="+numberType11+"&beginTime="+getPayTime(i)+"&endTime="+getPayTime(i)+"&productInfo=4%3A"+messageLogin.getName()+"&validatecode=&busitype=3&mobilenum=&rand=";
		// WebClient myWeclient = getMyWeclient(messageLogin, taskMobile);
		// WebRequest webRequest = new WebRequest(new URL(url),
		// HttpMethod.POST);
		// Page page = myWeclient.getPage(webRequest);
		Page page = webClient.getPage(url);
		Thread.sleep(1000);
		// System.out.println(page.getWebResponse().getContentAsString());
		if (page.getWebResponse().getContentAsString().contains("付款方式")) {
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			Element div = doc.select("#hiddenresult").first();
			if (doc.toString().contains("tr")) {
				Elements trs = div.select("tr");
				List<TelecomGansuPay> list = new ArrayList<TelecomGansuPay>();
				for (Element tr : trs) {
					Elements tds = tr.select("td");
					telecomGansuPay = new TelecomGansuPay();
					telecomGansuPay.setTime(tds.get(1).text());
					telecomGansuPay.setMoney(tds.get(2).text());
					telecomGansuPay.setPayRoad(tds.get(3).text());
					telecomGansuPay.setPayType(tds.get(4).text());
					telecomGansuPay.setTaskid(messageLogin.getTask_id());
					list.add(telecomGansuPay);
				}
				webParam.setList(list);
				webParam.setHtml(page.getWebResponse().getContentAsString());
				// webParam.setPage(page3);
				webParam.setUrl(url);
				// System.out.println(list.toString()+"缴费信息555555555555555555555555555555555555555555555555");
				return webParam;
			}
		}
		return null;
	}

	// 短信记录
	public WebParam<TelecomGansuMessage> getMessageInfo(MessageLogin messageLogin, TaskMobile taskMobile,
			WebClient client, int a) throws Exception {
		Thread.sleep(1000);
		String dateBefore = getDateBefore("yyyyMM", a);
		// //1
		String url = "http://gs.189.cn/web/json/clearSession.action";
		Page page3 = client.getPage(url);
//		System.out.println(page3.getWebResponse().getContentAsString() + "1+getMessageInfo" + dateBefore);
		// 2
		String url1 = "http://gs.189.cn/web/json/searchDetailedFeeNew.action?productGroup=4:" + messageLogin.getName()
				+ "&orderDetailType=8&queryMonth=" + dateBefore + "&flag=1";
		Page page2 = client.getPage(url1);
//		System.out.println(page2.getWebResponse().getContentAsString() + "2+getMessageInfo" + dateBefore);
		if (page2.getWebResponse().getContentAsString().contains("{\"result\":1}")) {
			// 3		
			String url2 = "http://gs.189.cn/web/json/ifPopWinShow.action?productType=4&accessNumber="
					+ messageLogin.getName();
			Page page5 = client.getPage(url2);
//			System.out.println(page5.getWebResponse().getContentAsString() + "3+getMessageInfo" + dateBefore);
			// 4
			String url3 = "http://gs.189.cn/web/json/getAncillaryInfo.action?timestamp=" + System.currentTimeMillis()
					+ "&productGroup=4:" + messageLogin.getName();
			Page page4 = client.getPage(url3);
//			System.out.println(page4.getWebResponse().getContentAsString() + "4+getMessageInfo" + dateBefore);
			// 5
			String url6 = "http://gs.189.cn/web/json/searchDetailedFee.action?timestamp=" + System.currentTimeMillis()
					+ "&productGroup=4:" + messageLogin.getName() + "&orderDetailType=8&queryMonth=" + dateBefore;
//			System.out.println(url6);
			WebParam<TelecomGansuMessage> webParam = new WebParam<TelecomGansuMessage>();
			TelecomGansuMessage telecomGansuMessage = null;
			Page page = null;
			if (client != null) {
				// page = client.getPage(url6);
				try {
					page = telecomRetryGanSuService.getMessageRetry(taskMobile, messageLogin, url6, client);
				} catch (Exception e) {
					tracer.addTag("retry.getMessageRetry.FAIL", taskMobile.getTaskid());
				}

				if (null != page && null != page.getWebResponse().getContentAsString()) {
					System.out.println(dateBefore + page.getWebResponse().getContentAsString());
					JSONObject json = JSONObject.fromObject(page.getWebResponse().getContentAsString());
					if (page.getWebResponse().getContentAsString().contains("trList")) {
						String string = json.getString("jsonResult").replace("\\", "");
						if (string.contains("trList")) {
							JSONObject json1 = JSONObject.fromObject(string);
							String string2 = json1.getString("trList");
							JSONArray json2 = new JSONArray(string2);
							List<TelecomGansuMessage> list = new ArrayList<TelecomGansuMessage>();
							for (int i = 0; i < json2.length(); i++) {
								JSONObject j = JSONObject.fromObject(json2.get(i) + "");
								telecomGansuMessage = new TelecomGansuMessage();
								telecomGansuMessage.setHisNum(j.getString("val2"));
								telecomGansuMessage.setMoney(j.getString("val3"));
								telecomGansuMessage.setSendDate(j.getString("val0"));
								telecomGansuMessage.setSendType(j.getString("val1"));
								telecomGansuMessage.setTaskid(messageLogin.getTask_id());
								list.add(telecomGansuMessage);
							}
							webParam.setList(list);
							webParam.setHtml(page.getWebResponse().getContentAsString());
							webParam.setWebClient(client);
							webParam.setUrl(url6);
							return webParam;
						}
					}

				}
			}
		}
		return null;
	}

	// 通话记录
	public WebParam<TelecomGansuCall> getCallInfo(MessageLogin messageLogin, TaskMobile taskMobile, WebClient client,
			int a) throws Exception {
		Thread.sleep(1000);
		String dateBefore = getDateBefore("yyyyMM", a);
		// 1
		String url = "http://gs.189.cn/web/json/clearSession.action";
		Page page3 = client.getPage(url);
		System.out.println(page3.getWebResponse().getContentAsString() + "1+getCallInfo" + dateBefore);
		// 2
		String url1 = "http://gs.189.cn/web/json/searchDetailedFeeNew.action?productGroup=4:" + messageLogin.getName()
				+ "&orderDetailType=6&queryMonth=" + dateBefore + "&flag=1";
		Page page2 = client.getPage(url1);
		System.out.println(page2.getWebResponse().getContentAsString() + "2+getCallInfo" + dateBefore);
		if (page2.getWebResponse().getContentAsString().contains("{\"result\":1}")) {
			// 3
			String url2 = "http://gs.189.cn/web/json/ifPopWinShow.action?productType=4&accessNumber="
					+ messageLogin.getName();
			Page page5 = client.getPage(url2);
			System.out.println(page5.getWebResponse().getContentAsString() + "3+getCallInfo" + dateBefore);
			// 4
			String url6 = "http://gs.189.cn/web/json/getAncillaryInfo.action?timestamp=" + System.currentTimeMillis()
					+ "productGroup=4:" + messageLogin.getName();
			Page page4 = client.getPage(url6);
			System.out.println(page4.getWebResponse().getContentAsString() + "4+getCallInfo" + dateBefore);
			// 5
			String url7 = "http://gs.189.cn/web/json/searchDetailedFee.action?timestamp=" + System.currentTimeMillis()
					+ "&productGroup=4:" + messageLogin.getName() + "&orderDetailType=6&queryMonth=" + dateBefore;
			WebParam<TelecomGansuCall> webParam = new WebParam<TelecomGansuCall>();
			TelecomGansuCall telecomGansuCall = null;
			// Page page = client.getPage(url7);
			Page page = null;
			try {
				page = telecomRetryGanSuService.getCallRetry(taskMobile, messageLogin, url7, client);
			} catch (Exception e) {
				tracer.addTag("retry.getCallRetry.FAIL", taskMobile.getTaskid());
			}

			if (null != page) {
				System.out.println(dateBefore + page.getWebResponse().getContentAsString());
				JSONObject json = JSONObject.fromObject(page.getWebResponse().getContentAsString());
				String string = json.getString("jsonResult").replace("\\", "");
				if (null != string) {
					JSONObject json1 = JSONObject.fromObject(string);
					if (json1.toString().contains("trList") == false) {
						return webParam;
					} else {
						String string2 = json1.getString("trList");
						List<TelecomGansuCall> list = new ArrayList<TelecomGansuCall>();
						JSONArray json2 = new JSONArray(string2);
						for (int i = 0; i < json2.length(); i++) {
							JSONObject j = JSONObject.fromObject(json2.get(i) + "");
							telecomGansuCall = new TelecomGansuCall();
							telecomGansuCall.setCallDate(j.getString("val0"));
							telecomGansuCall.setCallStatus(j.getString("val1"));
							telecomGansuCall.setHisNum(j.getString("val2"));
							telecomGansuCall.setCallTime(j.getString("val3"));
							telecomGansuCall.setCallMoney(j.getString("val4"));
							telecomGansuCall.setCallPlace(j.getString("val5"));
							telecomGansuCall.setCallType(j.getString("val6"));
							telecomGansuCall.setTaskid(messageLogin.getTask_id());
							list.add(telecomGansuCall);
						}
						webParam.setList(list);
						webParam.setHtml(page.getWebResponse().getContentAsString());
						webParam.setUrl(url7);
						return webParam;
					}
				}
			}
		}
		return null;
	}

	// 账单信息
	public WebParam<TelecomGansuPhoneBill> getPhoneBillInfo(MessageLogin messageLogin, TaskMobile taskMobile,
			WebClient webClient) throws Exception {
		String url1 = "http://gs.189.cn/web/v7/fee/getBillPay.action?productInfo=4%3A" + messageLogin.getName()
				+ "&checkedProductName=%E6%89%8B%E6%9C%BA%3A" + messageLogin.getName();
		WebParam<TelecomGansuPhoneBill> webParam = new WebParam<TelecomGansuPhoneBill>();
		TelecomGansuPhoneBill telecomGansuPhoneBill = null;

		Page page2 = webClient.getPage(url1);
		Thread.sleep(1000);
		if (null != page2) {
			int code2 = page2.getWebResponse().getStatusCode();
			if (code2 == 200) {
				if (page2.getWebResponse().getContentAsString().contains("billPayList")) {
					JSONObject json = JSONObject.fromObject(page2.getWebResponse().getContentAsString());
					String string = json.getString("billPayList");
					JSONArray json1 = new JSONArray(string);
					List<TelecomGansuPhoneBill> list = new ArrayList<TelecomGansuPhoneBill>();
					for (int i = 1; i < json1.length(); i++) {
						Object json3 = json1.get(i);
						JSONObject json4 = JSONObject.fromObject(json3 + "");
						telecomGansuPhoneBill = new TelecomGansuPhoneBill();
						telecomGansuPhoneBill.setTaskid(messageLogin.getTask_id());
						telecomGansuPhoneBill.setMonth(json4.getString("month"));
						telecomGansuPhoneBill.setMoney(json4.getString("money"));
						telecomGansuPhoneBill.setPayState(json4.getString("payState"));
						telecomGansuPhoneBill.setStylepx(json4.getString("stylepx"));
						list.add(telecomGansuPhoneBill);
					}
					webParam.setList(list);
					webParam.setHtml(page2.getWebResponse().getContentAsString());
					webParam.setUrl(url1);
					webParam.setCode(code2);
					return webParam;
				}
			}
		}
		return null;
	}

	/*
	 * @Des 获取当前月 的前i个月的 时间
	 */
	public String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}

	// 月账单
	public WebParam<TelecomGansuPhoneMonthBill> getPhoneMonthBillInfo(MessageLogin messageLogin, TaskMobile taskMobile,
			int i, WebClient webClient) throws Exception {
		WebParam<TelecomGansuPhoneMonthBill> webParam = new WebParam<TelecomGansuPhoneMonthBill>();
		// Thread.sleep(1000);
		String dateBefore = getDateBefore("yyyyMM", i);
		String url88 = "http://gs.189.cn/web/fee/getBillPayDetail.action?t=" + System.currentTimeMillis()
				+ "&detailMonth=" + dateBefore + "&productInfo=4%3A" + messageLogin.getName();
		// http://gs.189.cn/web/v7/fee/getBillPay.action?productInfo=4%3A17344146694&checkedProductName=%E6%89%8B%E6%9C%BA%3A17344146694
		Page page3 = webClient.getPage(url88);
		// Thread.sleep(1000);
		// System.out.println(page3.getWebResponse().getContentAsString());
		if (null != page3) {
			int code = page3.getWebResponse().getStatusCode();
			TelecomGansuPhoneMonthBill telecomGansuPhoneMonthBill = null;
			List<TelecomGansuPhoneMonthBill> list = new ArrayList<TelecomGansuPhoneMonthBill>();
			if (code == 200) {
				// HtmlTable object =
				// page3.getFirstByXPath("//table[@class='bill_list']");
				if (page3.getWebResponse().getContentAsString().contains("bill_list")) {
					Document doc = Jsoup.parse(page3.getWebResponse().getContentAsString());
					String netMoney = getNextLabelByKeyword(doc, "流量包费");
					// System.out.println(netMoney+"________________________________________________________");
					String tianYiMoney = getNextLabelByKeyword(doc, "天翼套餐功能费");
					String messageMoney = getNextLabelByKeyword(doc, "短信通信费");
					String calllocalMoney = getNextLabelByKeyword(doc, "本地通话费");
					String callLongMoney = getNextLabelByKeyword(doc, "国内长途通话费");
					telecomGansuPhoneMonthBill = new TelecomGansuPhoneMonthBill();
					telecomGansuPhoneMonthBill.setTaskid(messageLogin.getTask_id());
					telecomGansuPhoneMonthBill.setNetMoney(netMoney);
					telecomGansuPhoneMonthBill.setTianYiMoney(tianYiMoney);
					telecomGansuPhoneMonthBill.setMessageMoney(messageMoney);
					telecomGansuPhoneMonthBill.setCalllocalMoney(calllocalMoney);
					telecomGansuPhoneMonthBill.setCallLongMoney(callLongMoney);
					telecomGansuPhoneMonthBill.setMonth(dateBefore);
					list.add(telecomGansuPhoneMonthBill);
					webParam.setList(list);
					webParam.setHtml(page3.getWebResponse().getContentAsString());
					webParam.setUrl(url88);
					webParam.setCode(code);
					return webParam;
				}
			}
		}
		return null;
	}
}
