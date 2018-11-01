package app.crawler.telecom.htmlparse;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.shanxi1.TelecomShanxi1Account;
import com.microservice.dao.entity.crawler.telecom.shanxi1.TelecomShanxi1Bill;
import com.microservice.dao.entity.crawler.telecom.shanxi1.TelecomShanxi1CallRecord;
import com.microservice.dao.entity.crawler.telecom.shanxi1.TelecomShanxi1Flow;
import com.microservice.dao.entity.crawler.telecom.shanxi1.TelecomShanxi1Message;
import com.microservice.dao.entity.crawler.telecom.shanxi1.TelecomShanxi1Order;
import com.microservice.dao.entity.crawler.telecom.shanxi1.TelecomShanxi1PayInfo;
import com.microservice.dao.entity.crawler.telecom.shanxi1.TelecomShanxi1Starlevel;
import com.microservice.dao.entity.crawler.telecom.shanxi1.TelecomShanxi1UserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.bean.WebParam;
import app.exceptiondetail.EUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class TelecomShanxi1Parser {

	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private EUtils eutils;

	/**
	 * 通过url获取 Page
	 * 
	 * @param taskMobile
	 * @param url
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Page getPage(WebClient webClient, TaskMobile taskMobile, String url, HttpMethod type) throws Exception {
		tracer.addTag("TelecomShanxiParser.getPage---url:", url + "taskId:" + taskMobile.getTaskid());

		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);
		Page searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		tracer.addTag("TelecomShanxiParser.getPage.statusCode:" + statusCode, "---taskid:" + taskMobile.getTaskid());

		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("TelecomShanxiParser.getPage---taskid:",
					taskMobile.getTaskid() + "---url:" + url + "<xmp>" + html + "</xmp>");
			return searchPage;
		}

		return null;
	}

	public HtmlPage getHtml(String url, WebClient webClient, TaskMobile taskMobile) throws Exception {
		tracer.addTag("TelecomShanxiParser.getHtml---url:" + url + " ", taskMobile.getTaskid());

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		tracer.addTag("TelecomShanxiParser.getHtml.statusCode:" + statusCode, "---taskid:" + taskMobile.getTaskid());
		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("TelecomShanxiParser.getHtml---url:" + url + "---taskid:" + taskMobile.getTaskid(),
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
	 * 获取个人信息
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception
	 */
	public WebParam getUserInfo(TaskMobile taskMobile) throws Exception {

		tracer.addTag("TelecomShanxiParser.getUserinfo", taskMobile.getTaskid());

		WebParam webParam = new WebParam();
		
//		String urlData = "http://www.189.cn/dqmh/ssoLink.do?method=linkTo&platNo=10007&toStUrl=http://sx.189.cn/service/manage/modifyUserInfo.parser";
//		String urlData = "http://sx.189.cn/service/manage/modifyUserInfo.action";
		String urlData = "http://www.189.cn/dqmh/ssoLink.do?method=linkTo&platNo=10007&toStUrl=http://sx.189.cn/service/manage/modifyUserInfo.action";
		try {
			WebClient webClient = addcookie(taskMobile);
			Page page = getPage(webClient, taskMobile, urlData, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomShanxiParser.getUserinfo 个人信息" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				TelecomShanxi1UserInfo telecomShanxi1UserInfo = htmlParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setTelecomShanxi1UserInfo(telecomShanxi1UserInfo);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomShanxiParser.getUserInfo---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return null;
	}

	/**
	 * 解析个人信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private TelecomShanxi1UserInfo htmlParser(String html, TaskMobile taskMobile) {

		Document doc = Jsoup.parse(html);

		String name = getNextLabelByKeyword(doc, "客户名称", "th");
		String customerType = getNextLabelByKeyword(doc, "客户类型", "th");
		String telephone = getNextLabelByKeyword(doc, "联系电话", "th");
		String documenType = getNextLabelByKeyword(doc, "证件类型", "th");
		String idNum = getNextLabelByKeyword(doc, "证件号码", "th");
		String address = getNextLabelByKeyword(doc, "通信地址", "th");
		String postcode = getNextLabelByKeyword(doc, "邮政编码", "th");
		String email = getNextLabelByKeyword(doc, "E-mail", "th");
		String sex = getNextLabelByKeyword(doc, "客户性别", "th");
		String birthdate = getNextLabelByKeyword(doc, "出生日期", "th");
		String occupation = getNextLabelByKeyword(doc, "职业类别", "th");
		String education = getNextLabelByKeyword(doc, "学历", "th");
		String hobby = getNextLabelByKeyword(doc, "客户爱好", "th");
		String habit = getNextLabelByKeyword(doc, "客户习惯", "th");
		String familyCode = getNextLabelByKeyword(doc, "家庭邮编", "th");
		String homePhone = getNextLabelByKeyword(doc, "住宅电话", "th");
		String createDate = getNextLabelByKeyword(doc, "创建日期", "th");
		String totalBalance = "";
		String comboBalance = "";
		String specialUseBalance = "";
		String arrearsSum = "";
		String sumCharge = "";
		String integral = "";
		try {
			WebClient webClient = addcookie(taskMobile);
			try {
				// 山西电信个人积分
//				String url1 = "http://www.189.cn//dqmh/ssoLink.do?method=linkTo&platNo=10007&toStUrl=http://sx.189.cn/service/jf/integralSearch.parser";
				String url1 = "http://sx.189.cn/service/jf/integralSearch.action";
				HtmlPage searchPage1 = getHtml(url1, webClient, taskMobile);
				webClient = searchPage1.getWebClient();
				tracer.addTag("TelecomShanxiParser.htmlParser山西电信个人积分" + taskMobile.getTaskid(),
						"<xmp>" + searchPage1.getWebResponse().getContentAsString() + "</xmp>");

				HtmlSpan inputUserName = (HtmlSpan) searchPage1.querySelector("span.font-orange");
				if (null != inputUserName) {
					integral = inputUserName.asText();
				}
			} catch (Exception e) {
				tracer.addTag("TelecomShanxiParser.htmlParser山西电信个人积分---ERROR---"+
						taskMobile.getTaskid() ,eutils.getEDetail(e));
			}

			try {
				// 获得账户余额信息

				String urlBalance = "http://www.189.cn/dqmh/homogeneity/queryBalance.do";

				Page searchPageBalance = getPage(webClient, taskMobile, urlBalance, null);
				String balanceStr = searchPageBalance.getWebResponse().getContentAsString();

				tracer.addTag("TelecomShanxiParser.htmlParser山西电信账户余额信息" + taskMobile.getTaskid(),
						"<xmp>" + balanceStr + "</xmp>");
				JSONObject jsonObj = JSONObject.fromObject(balanceStr);
				JSONObject obj = jsonObj.getJSONObject("obj");
				if (null != obj) {
					totalBalance = obj.getString("totalBalance");
					comboBalance = obj.getString("comboBalance");
					specialUseBalance = obj.getString("specialUseBalance");
				}

			} catch (Exception e) {
				tracer.addTag("TelecomShanxiParser.htmlParser山西电信账户余额信息---ERROR---"+
						taskMobile.getTaskid() ,eutils.getEDetail(e));
			}

			try {
				// 获得账户欠费金额
				String url = "http://www.189.cn/dqmh/homogeneity/queryArrears.do";

				Page searchPage = getPage(webClient, taskMobile, url, null);
				String str = searchPage.getWebResponse().getContentAsString();

				tracer.addTag("TelecomShanxiParser.htmlParser山西电信账户欠费金额" + taskMobile.getTaskid(),
						"<xmp>" + str + "</xmp>");

				JSONObject jsonObj = JSONObject.fromObject(str);
				JSONObject obj = jsonObj.getJSONObject("obj");
				if (null != obj) {
					arrearsSum = obj.getString("arrearsSum");
				}
			} catch (Exception e) {
				tracer.addTag("TelecomShanxiParser.htmlParser山西电信账户欠费金额---ERROR---"+
						taskMobile.getTaskid() ,eutils.getEDetail(e));
			}

			try {
				// 获得本月已产生话费
				String url = "http://www.189.cn/dqmh/homogeneity/queryProducedCall.do";

				Page searchPage = getPage(webClient, taskMobile, url, null);
				String str = searchPage.getWebResponse().getContentAsString();

				tracer.addTag("TelecomShanxiParser.htmlParser山西电信本月已产生话费" + taskMobile.getTaskid(),
						"<xmp>" + str + "</xmp>");
				JSONObject jsonObj = JSONObject.fromObject(str);
				JSONObject obj = jsonObj.getJSONObject("obj");
				if (null != obj) {
					sumCharge = obj.getString("sumCharge");
				}

			} catch (Exception e) {
				tracer.addTag("TelecomShanxiParser.htmlParser山西电信本月已产生话费---ERROR--" + taskMobile.getTaskid(),
						eutils.getEDetail(e));
			}

		} catch (Exception e) {
			tracer.addTag("TelecomShanxiParser.htmlParser---ERROR--"+taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		TelecomShanxi1UserInfo teShanxi1UserInfo = new TelecomShanxi1UserInfo(taskMobile.getTaskid(), name,
				customerType, telephone, documenType, idNum, address, postcode, email, sex, birthdate, occupation,
				education, hobby, habit, familyCode, homePhone, createDate, totalBalance, comboBalance,
				specialUseBalance, arrearsSum, sumCharge, integral);
		return teShanxi1UserInfo;
	}

	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Document document, String keyword, String label) {
		Elements es = document.select(label + ":contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}

	/**
	 * 获取缴费信息
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomShanxi1PayInfo> getPayInfo(TaskMobile taskMobile, String startDate, String endDate)
			throws Exception {

		tracer.addTag("TelecomShanxiParser.getPayInfo", taskMobile.getTaskid());

		WebParam<TelecomShanxi1PayInfo> webParam = new WebParam<TelecomShanxi1PayInfo>();
		String urlData = "http://sx.189.cn/service/pay/queryPaymentRecord.action?paymentHistoryQueryIn.startDate="
				+ startDate + "&paymentHistoryQueryIn.endDate=" + endDate;
		try {
			WebClient webClient = addcookie(taskMobile);
//			try {
//				String url1 = "http://www.189.cn//dqmh/ssoLink.do?method=linkTo&platNo=10007&toStUrl=http://sx.189.cn/service/jf/integralSearch.parser";
//				HtmlPage html1 = getHtml(url1, webClient, taskMobile);
//				webClient = html1.getWebClient();
//			} catch (Exception e) {
//				tracer.addTag("TelecomShanxiParser.getPayInfo缴费信息---ERROR---" + taskMobile.getTaskid(),
//						eutils.getEDetail(e));
//			}
			Page page = getPage(webClient, taskMobile, urlData, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomShanxiParser.getPayInfo缴费信息" + taskMobile.getTaskid(), "<xmp>" + html + "</xmp>");
				List<TelecomShanxi1PayInfo> list = htmlPayInfoParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomShanxiParser.getPayInfo---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return null;
	}

	/**
	 * 解析缴费信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomShanxi1PayInfo> htmlPayInfoParser(String html, TaskMobile taskMobile) {

		List<TelecomShanxi1PayInfo> list = new ArrayList<TelecomShanxi1PayInfo>();

		try {

			TelecomShanxi1PayInfo telecomShanxi1PayInfo = null;

			JSONObject jsonObj = JSONObject.fromObject(html);

			JSONArray jsonarray = jsonObj.getJSONArray("listPaymentHistory");
			if (null != jsonarray && jsonarray.size() > 0) {
				for (Object object : jsonarray) {
					JSONObject jsonObj1 = JSONObject.fromObject(object);
					String accountName = jsonObj1.getString("accountName");
					String accNbr = jsonObj1.getString("accNbr");
					String paymentDate = jsonObj1.getString("paymentDate");
					String fee = jsonObj1.getString("fee");
					String paymentType = jsonObj1.getString("paymentType");
					String regionDesc = jsonObj1.getString("regionDesc");
					telecomShanxi1PayInfo = new TelecomShanxi1PayInfo(taskMobile.getTaskid(), accountName, accNbr,
							paymentDate, fee, "元", paymentType, "已支付", regionDesc);
					list.add(telecomShanxi1PayInfo);
				}

			}
		} catch (Exception e) {
			tracer.addTag("TelecomShanxiParser.htmlPayInfoParser---ERROR---"+
					taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return list;

	}

	/**
	 * 山西用户产品信息
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomShanxi1Order> getProduct(TaskMobile taskMobile) throws Exception {

		tracer.addTag("TelecomShanxiParser.getProduct", taskMobile.getTaskid());

		WebParam<TelecomShanxi1Order> webParam = new WebParam<TelecomShanxi1Order>();
		String urlData = "http://sx.189.cn/service/manage/packageListQuery.action?requestFlag=asynchronism";
		try {
			WebClient webClient = addcookie(taskMobile);
//			try {
//				String url1 = "http://www.189.cn//dqmh/ssoLink.do?method=linkTo&platNo=10007&toStUrl=http://sx.189.cn/service/jf/integralSearch.action";
//				HtmlPage html1 = getHtml(url1, webClient, taskMobile);
//				webClient = html1.getWebClient();
//			} catch (Exception e) {
//				tracer.addTag("TelecomShanxiParser.getProduct---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
//			}
			Page page = getPage(webClient, taskMobile, urlData, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomShanxiParser.getProduct山西用户产品信息" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomShanxi1Order> list = htmlOrderParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomShanxiParser.getProduct---ERROR---" + taskMobile.getTaskid(), eutils.getEDetail(e));
		}
		return null;
	}

	/**
	 * 解析山西用户产品信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomShanxi1Order> htmlOrderParser(String html, TaskMobile taskMobile) {

		List<TelecomShanxi1Order> list = new ArrayList<TelecomShanxi1Order>();

		try {

			TelecomShanxi1Order telecomShanxi1Order = null;

			JSONObject jsonObj = JSONObject.fromObject(html);

			JSONArray jsonarray = jsonObj.getJSONArray("proList");
			if (null != jsonarray && jsonarray.size() > 0) {
				for (Object object : jsonarray) {
					JSONObject jsonObj1 = JSONObject.fromObject(object);
					String offerDesc = jsonObj1.getString("offerDesc");
					String offerName = jsonObj1.getString("offerName");
					String offerType = jsonObj1.getString("offerType");
					telecomShanxi1Order = new TelecomShanxi1Order(taskMobile.getTaskid(), offerType, offerName,
							offerDesc);
					list.add(telecomShanxi1Order);
				}

			}
		} catch (Exception e) {
			tracer.addTag("TelecomShanxiParser.htmlOrderParser---ERROR---"+
					taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return list;

	}

	/**
	 * 山西用户账户信息
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomShanxi1Account> getAccount(TaskMobile taskMobile, MessageLogin messageLogin)
			throws Exception {

		tracer.addTag("TelecomShanxiParser.getAccount", taskMobile.getTaskid());

		WebParam<TelecomShanxi1Account> webParam = new WebParam<TelecomShanxi1Account>();
		String urlData = "http://sx.189.cn/service/manage/productQuery.action?prodNumber=" + messageLogin.getName()
				+ "&prodType=4";
		try {
			WebClient webClient = addcookie(taskMobile);
			try {
				String url1 = "http://www.189.cn//dqmh/ssoLink.do?method=linkTo&platNo=10007&toStUrl=http://sx.189.cn/service/jf/integralSearch.parser";
				HtmlPage html1 = getHtml(url1, webClient, taskMobile);
				webClient = html1.getWebClient();
			} catch (Exception e) {
				tracer.addTag("TelecomShanxiParser.getAccount---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
			}
			Page page = getPage(webClient, taskMobile, urlData, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomShanxiParser.getAccount山西用户账户信息" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomShanxi1Account> list = htmlAccountParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomShanxiParser.getAccount---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return null;
	}

	/**
	 * 解析山西用户账户信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomShanxi1Account> htmlAccountParser(String html, TaskMobile taskMobile) {

		List<TelecomShanxi1Account> list = new ArrayList<TelecomShanxi1Account>();
		try {

			TelecomShanxi1Account telecomShanxi1Account = null;

			JSONObject jsonObj = JSONObject.fromObject(html);

			JSONObject memberProdSet = jsonObj.getJSONObject("memberProdSet");

			if (null != memberProdSet) {
				String accNbr = memberProdSet.getString("accNbr");
				String productName = memberProdSet.getString("productName");
				String address = memberProdSet.getString("address");
				String servCreateDate = memberProdSet.getString("servCreateDate");
				String productStatusCdName = memberProdSet.getString("productStatusCdName");
				JSONArray jsonArray = memberProdSet.getJSONArray("payAcctNbr");
				String payAcctNbr = "";
				if (jsonArray.size() > 0) {
					payAcctNbr = jsonArray.getString(0);
				}
				telecomShanxi1Account = new TelecomShanxi1Account(taskMobile.getTaskid(), accNbr, productName, address,
						servCreateDate, productStatusCdName, payAcctNbr);
				list.add(telecomShanxi1Account);

			}
		} catch (Exception e) {
			tracer.addTag("TelecomShanxiParser.htmlAccountParser---ERROR---"+
					taskMobile.getTaskid() ,eutils.getEDetail(e));
		}

		return list;

	}

	/**
	 * 山西电信获取我的短信详单
	 * 
	 * @param taskMobile
	 * @param messageLogin
	 * @param starttime
	 * @param endtime
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomShanxi1Message> getTelecomShanxi1Message(TaskMobile taskMobile, MessageLogin messageLogin,
			String starttime, String endtime) throws Exception {

		tracer.addTag("TelecomShanxiParser.getTelecomShanxi1Message", taskMobile.getTaskid());

		WebParam<TelecomShanxi1Message> webParam = new WebParam<TelecomShanxi1Message>();
		String urlData = "http://www.189.cn/bss/billing/provincebillrecord.do?&callback=jQuery1112005879917732313844_1503539936060&accounttype=1&account="
				+ messageLogin.getName() + "&type=2&needRandomCodeFlag=0&starttime=" + starttime + "&endtime=" + endtime
				+ "&_=1503539936075";
		try {
			WebClient webClient = addcookie(taskMobile);
			Page page = getPage(webClient, taskMobile, urlData, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomShanxiParser.getTelecomShanxi1Message山西电信获取我的短信详单" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomShanxi1Message> list = htmlTelecomShanxi1MessageParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomShanxiParser.getTelecomShanxi1Message---ERROR---"+
					taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return null;
	}

	/**
	 * 解析山西电信获取我的短信详单
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomShanxi1Message> htmlTelecomShanxi1MessageParser(String html, TaskMobile taskMobile) {

		List<TelecomShanxi1Message> list = new ArrayList<TelecomShanxi1Message>();
		try {

			TelecomShanxi1Message telecomShanxi1Message = null;

			html = html.substring(html.lastIndexOf("(") + 1, html.lastIndexOf(")"));

			// Gson gson = new Gson();
			// TelecomList<TelecomShanxi1Message> fromJson = gson.fromJson(html,
			// TelecomList.class);

			JSONObject jsonObj = JSONObject.fromObject(html);

			JSONObject dataObject = jsonObj.getJSONObject("dataObject");

			if (null != dataObject) {
				JSONArray jsonArray = dataObject.getJSONArray("resultObject");
				if (null != jsonArray && jsonArray.size() > 0) {
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject json = JSONObject.fromObject(jsonArray.getString(i));
						String smsMobile = json.getString("smsMobile");
						String smsTime = json.getString("smsTime");
						String smsCost = json.getString("smsCost");
						String smsType = json.getString("smsType");
						String smsStyle = json.getString("smsStyle");
						String smsArea = json.getString("smsArea");
						telecomShanxi1Message = new TelecomShanxi1Message(taskMobile.getTaskid(), smsMobile, smsTime,
								smsCost, smsType, smsStyle, smsArea);
						list.add(telecomShanxi1Message);
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("TelecomShanxiParser.htmlTelecomShanxi1MessageParser---ERROR---"+
					taskMobile.getTaskid() ,eutils.getEDetail(e));
		}

		return list;

	}

	/**
	 * 山西电信获取我的流量详单
	 * 
	 * @param taskMobile
	 * @param messageLogin
	 * @param starttime
	 * @param endtime
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomShanxi1Flow> getTelecomShanxi1Flow(TaskMobile taskMobile, MessageLogin messageLogin,
			String starttime, String endtime) throws Exception {

		tracer.addTag("TelecomShanxiParser.getTelecomShanxi1Flow", taskMobile.getTaskid());

		WebParam<TelecomShanxi1Flow> webParam = new WebParam<TelecomShanxi1Flow>();
		String urlData = "http://www.189.cn/bss/billing/provincebillrecord.do?&callback=jQuery1112005879917732313844_1503539936060&accounttype=1&account="
				+ messageLogin.getName() + "&type=4&needRandomCodeFlag=1&randomCode=" + messageLogin.getSms_code()
				+ "&starttime=" + starttime + "&endtime=" + endtime + "&_=1503539936080";
		try {
			WebClient webClient = addcookie(taskMobile);
			Page page = getPage(webClient, taskMobile, urlData, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomShanxiParser.getTelecomShanxi1Flow山西电信获取我的流量详单" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomShanxi1Flow> list = htmlTelecomShanxi1FlowParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomShanxiParser.getTelecomShanxi1Flow---ERROR---"+
					taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return null;
	}

	/**
	 * 解析山西电信获取我的流量详单
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomShanxi1Flow> htmlTelecomShanxi1FlowParser(String html, TaskMobile taskMobile) {

		List<TelecomShanxi1Flow> list = new ArrayList<TelecomShanxi1Flow>();

		try {
			TelecomShanxi1Flow telecomShanxi1Flow = null;

			html = html.substring(html.lastIndexOf("(") + 1, html.lastIndexOf(")"));

			// Gson gson = new Gson();
			// TelecomList<TelecomShanxi1Flow> fromJson = gson.fromJson(html,
			// TelecomList.class);

			JSONObject jsonObj = JSONObject.fromObject(html);

			JSONObject dataObject = jsonObj.getJSONObject("dataObject");

			if (null != dataObject) {
				JSONArray jsonArray = dataObject.getJSONArray("resultObject");
				if (null != jsonArray && jsonArray.size() > 0) {
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject json = JSONObject.fromObject(jsonArray.getString(i));
						String netType = json.getString("netType");
						String netTime = json.getString("netTime");
						String netFlow = json.getString("netFlow");
						String netFee = json.getString("netFee");
						String netTimeCost = json.getString("netTimeCost");
						String netArea = json.getString("netArea");
						String netBusiness = json.getString("netBusiness");
						telecomShanxi1Flow = new TelecomShanxi1Flow(taskMobile.getTaskid(), netType, netTime, netFlow,
								netFee, netTimeCost, netArea, netBusiness);
						list.add(telecomShanxi1Flow);

					}
				}
			}

		} catch (Exception e) {
			tracer.addTag("TelecomShanxiParser.htmlTelecomShanxi1FlowParser---ERROR---"+
					taskMobile.getTaskid() ,eutils.getEDetail(e));
		}

		return list;

	}

	/**
	 * 山西电信获取我的通话记录详单
	 * 
	 * @param taskMobile
	 * @param messageLogin
	 * @param starttime
	 * @param endtime
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomShanxi1CallRecord> getTelecomShanxi1CallRecord(TaskMobile taskMobile,
			MessageLogin messageLogin, String starttime, String endtime) throws Exception {

		tracer.addTag("TelecomShanxiParser.getTelecomShanxi1CallRecord", taskMobile.getTaskid());

		WebParam<TelecomShanxi1CallRecord> webParam = new WebParam<TelecomShanxi1CallRecord>();
		String urlData = "http://www.189.cn/bss/billing/provincebillrecord.do?&callback=jQuery1112005879917732313844_1503539936060&accounttype=1&account="
				+ messageLogin.getName() + "&type=1&needRandomCodeFlag=1&randomCode=" + messageLogin.getSms_code()
				+ "&starttime=" + starttime + "&endtime=" + endtime + "&_=1503539936068";
		try {
			WebClient webClient = addcookie(taskMobile);
			Page page = getPage(webClient, taskMobile, urlData, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomShanxiParser.getTelecomShanxi1CallRecord山西电信获取我的通话记录详单" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomShanxi1CallRecord> list = htmlTelecomShanxi1CallRecordParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomShanxiParser.getTelecomShanxi1CallRecord---ERROR---"+
					taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return null;
	}

	/**
	 * 解析山西电信获取我的通话记录详单
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomShanxi1CallRecord> htmlTelecomShanxi1CallRecordParser(String html, TaskMobile taskMobile) {

		List<TelecomShanxi1CallRecord> list = new ArrayList<TelecomShanxi1CallRecord>();

		try {

			TelecomShanxi1CallRecord telecomShanxi1CallRecord = null;

			html = html.substring(html.lastIndexOf("(") + 1, html.lastIndexOf(")"));

			// Gson gson = new Gson();
			// TelecomList<TelecomShanxi1CallRecord> fromJson =
			// gson.fromJson(html,
			// TelecomList.class);

			JSONObject jsonObj = JSONObject.fromObject(html);

			JSONObject dataObject = jsonObj.getJSONObject("dataObject");

			if (null != dataObject) {
				JSONArray jsonArray = dataObject.getJSONArray("resultObject");
				if (null != jsonArray && jsonArray.size() > 0) {
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject json = JSONObject.fromObject(jsonArray.getString(i));
						String callType = json.getString("callType");
						String callMobile = json.getString("callMobile");
						String callTime = json.getString("callTime");
						String callTimeCost = json.getString("callTimeCost");
						String callStyle = json.getString("callStyle");
						String callArea = json.getString("callArea");
						String callFee = json.getString("callFee");
						telecomShanxi1CallRecord = new TelecomShanxi1CallRecord(taskMobile.getTaskid(), callType,
								callMobile, callTime, callTimeCost, callStyle, callArea, callFee);
						list.add(telecomShanxi1CallRecord);
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("TelecomShanxiParser.htmlTelecomShanxi1CallRecordParser---ERROR---"+
					taskMobile.getTaskid() ,eutils.getEDetail(e));
			return null;
		}

		return list;

	}

	/**
	 * 山西用户月账单消费
	 * 
	 * @param taskMobile
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomShanxi1Bill> getBill(TaskMobile taskMobile, String yearMonth) throws Exception {

		tracer.addTag("TelecomShanxiParser.getBill", taskMobile.getTaskid());

		WebParam<TelecomShanxi1Bill> webParam = new WebParam<TelecomShanxi1Bill>();
		String urlData = "http://www.189.cn/dqmh/homogeneity/balanceOutDetailQuery.do?billingcycle=" + yearMonth;
		try {
			WebClient webClient = addcookie(taskMobile);

			Page page = getPage(webClient, taskMobile, urlData, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomShanxiParser.getBill 缴费信息" + taskMobile.getTaskid(), "<xmp>" + html + "</xmp>");
				List<TelecomShanxi1Bill> list = htmlBillParser(html, taskMobile, yearMonth);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomShanxiParser.getBill---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return null;
	}

	/**
	 * 解析山西用户月账单消费
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomShanxi1Bill> htmlBillParser(String html, TaskMobile taskMobile, String yearMonth) {

		List<TelecomShanxi1Bill> list = new ArrayList<TelecomShanxi1Bill>();

		try {

			TelecomShanxi1Bill telecomShanxi1Bill = null;
			Document doc = Jsoup.parse(html);
			Elements link1 = doc.getElementsByTag("li");

			for (Element element : link1) {
				Elements elspan = element.getElementsByTag("span");
				String fee = elspan.text();
				for (Element element2 : elspan) {
					element2.empty();
				}
				String paymentType = element.text();

				telecomShanxi1Bill = new TelecomShanxi1Bill(taskMobile.getTaskid(), paymentType, fee, yearMonth);
				list.add(telecomShanxi1Bill);
			}

		} catch (Exception e) {
			tracer.addTag("TelecomShanxiParser.htmlPayInfoParser---ERROR---"+
					taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return list;

	}

	/**
	 * 山西用户星级服务信息
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomShanxi1Starlevel> getStarlevel(TaskMobile taskMobile) throws Exception {

		tracer.addTag("TelecomShanxiParser.getStarlevel", taskMobile.getTaskid());

		WebParam<TelecomShanxi1Starlevel> webParam = new WebParam<TelecomShanxi1Starlevel>();

		String transactionId = "";
		String token = "";
		String mobile = "";
		String sign = "";
		try {
			WebClient webClient = addcookie(taskMobile);
			try {
				// 跳转星级服务
				String urlT = "http://www.189.cn/dqmh/ssoLink.do?method=linkTo&platNo=93510&toStUrl=http://xjfw.189.cn/tykfh5/modules/starService/medalWall/indexPC.html?intaid=jt-sy-hxfw-01-";
				HtmlPage htmlT = getHtml(urlT, webClient, taskMobile);
				String jsonT = htmlT.getWebResponse().getContentAsString();
				webClient = htmlT.getWebClient();

			} catch (Exception e) {
				tracer.addTag("TelecomShanxiParser.urlT---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
			}
			try {
				// 通过此接口获取手机号与token
				String url1 = "http://xjfw.189.cn/tykf-itr-services/services/login/bySessionId";
				Page html = getPage(webClient, taskMobile, url1, null);
				String json = html.getWebResponse().getContentAsString();

				tracer.addTag("TelecomShanxiParser.getStarlevel---山西用户星级服务token与mobile信息" + taskMobile.getTaskid(),
						"<xmp>" + json + "</xmp>");

				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				int radomInt = new Random().nextInt(999999);
				transactionId = "1000010017" + df.format(new Date()) + radomInt;
				JSONObject jsonObj = JSONObject.fromObject(json);
				token = jsonObj.getString("token");
				mobile = jsonObj.getString("mobile");

			} catch (Exception e) {
				tracer.addTag("TelecomShanxiParser.bySessionId---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
			}

			try {
				/**
				 * 通过 transactionId , token 与 mobile 获取签名
				 */
				String urlData = "http://xjfw.189.cn/tykf-itr-services/services/dispatch.jsp?&dispatchUrl=ClientUni/clientuni/services/user/getSign?reqParam={\"transactionId\":\""
						+ transactionId + "\",\"channelCode\":\"H5002018\",\"token\":\"" + token + "\",\"type\":2}";
				Page page = getPage(webClient, taskMobile, urlData, HttpMethod.POST);
				String json = page.getWebResponse().getContentAsString();

				tracer.addTag("TelecomShanxiParser.getStarlevel---山西用户星级服务签名信息" + taskMobile.getTaskid(),
						"<xmp>" + json + "</xmp>");

				JSONObject jsonObj2 = JSONObject.fromObject(json);
				sign = jsonObj2.getString("sign");

			} catch (Exception e) {
				tracer.addTag("TelecomShanxiParser.sign---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
			}

			String urlStarlevel = "http://xjfw.189.cn/tykf-itr-services/services/dispatch.jsp?&dispatchUrl=ClientUni/clientuni/services/starLevel/custStarLevelQuery?reqParam={\"transactionId\":\""
					+ transactionId + "\",\"clientNbr\":\"" + mobile
					+ "\",\"channelCode\":\"H5002018\",\"deviceType\":\"1\",\"prvince\":\"%25E5%25B1%25B1%25E8%25A5%25BF\",\"sign\":\""
					+ sign + "\"}";

			Page page = getPage(webClient, taskMobile, urlStarlevel, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomShanxiParser.getStarlevel---山西用户星级服务信息" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomShanxi1Starlevel> list = htmlStarlevelParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomShanxiParser.getStarlevel---ERROR---"+ taskMobile.getTaskid(),eutils.getEDetail(e));
		}
		return null;
	}

	/**
	 * 解析山西用户星级服务信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomShanxi1Starlevel> htmlStarlevelParser(String html, TaskMobile taskMobile) {

		List<TelecomShanxi1Starlevel> list = new ArrayList<TelecomShanxi1Starlevel>();
		try {
			TelecomShanxi1Starlevel telecomShanxi1Starlevel = null;

			JSONObject jsonObj = JSONObject.fromObject(html);

			JSONObject custInfo = jsonObj.getJSONObject("custInfo");

			if (null != custInfo) {
				String custName = custInfo.getString("custName");
				String membershipLevel = custInfo.getString("membershipLevel");
				String growthpoint = custInfo.getString("growthpoint");
				telecomShanxi1Starlevel = new TelecomShanxi1Starlevel(taskMobile.getTaskid(), custName, membershipLevel,
						growthpoint);
				list.add(telecomShanxi1Starlevel);
			}
		} catch (Exception e) {
			tracer.addTag("TelecomShanxiParser.htmlStarlevelParser---ERROR---"+
					taskMobile.getTaskid() ,eutils.getEDetail(e));
		}

		return list;

	}

	/**
	 * 获取手机验证码
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public HtmlPage getphonecode(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {

		try {

			WebClient webClient = addcookie(taskMobile);
			String urlData = "http://www.189.cn/bss/sms/sendcode.do?flag=0&callback=jQuery111204100412069723336_1539072059786&_=1539072059789";
//			String urlData = "http://www.189.cn/bss/sms/sendcode.do?phoneNum=" + messageLogin.getName()
//					+ "&callback=jQuery111208221578666113154_1503569715993&_=1503569715997";

			Page page = getPage(webClient, taskMobile, urlData, null);

			tracer.addTag("TelecomShanxiParser.getphonecode---验证码返回数据:",
					taskMobile.getTaskid() + "---html:" + page.getWebResponse().getContentAsString());

			String url = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01841174";
			HtmlPage htmlpage = getHtml(url, webClient, taskMobile);


			return htmlpage;

		} catch (Exception e) {
			tracer.addTag("TelecomShanxiParser.getphonecode---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}

		return null;

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
	public String verificationcode(TaskMobile taskMobile, MessageLogin messageLogin, String starttime, String endtime)
			throws Exception {

		tracer.addTag("TelecomShanxiParser.getTelecomShanxi1CallRecord", taskMobile.getTaskid());
		String urlData = "http://www.189.cn/bss/billing/provincebillrecord.do?&callback=jQuery1112005879917732313844_1503539936060&accounttype=1&account="
				+ messageLogin.getName() + "&type=1&needRandomCodeFlag=1&randomCode=" + messageLogin.getSms_code()
				+ "&starttime=" + starttime + "&endtime=" + endtime + "&_=1503539936068";
		try {
			WebClient webClient = addcookie(taskMobile);
			Page page = getPage(webClient, taskMobile, urlData, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomShanxiParser.getTelecomShanxi1CallRecord 验证验证码---通话记录详单" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				html = html.substring(html.lastIndexOf("(") + 1, html.lastIndexOf(")"));

				JSONObject jsonObj = JSONObject.fromObject(html);

				String dataObject = jsonObj.getString("errorDescription");

				return dataObject;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomShanxiParser.getTelecomShanxi1CallRecord---ERROR---"+
					taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return null;
	}

}
