package app.service;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.ningxia.TelecomNingxiaBusinessMsg;
import com.microservice.dao.entity.crawler.telecom.ningxia.TelecomNingxiaMonthBillHistory;
import com.microservice.dao.entity.crawler.telecom.ningxia.TelecomNingxiaPayMsg;
import com.microservice.dao.entity.crawler.telecom.ningxia.TelecomNingxiaUserInfo;
import com.microservice.dao.entity.crawler.telecom.ningxia.TelecomNingxiahtml;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.ningxia.TelecomNingxiaBusinessMsgRepository;
import com.microservice.dao.repository.crawler.telecom.ningxia.TelecomNingxiaMonthBillHistoryRepository;
import com.microservice.dao.repository.crawler.telecom.ningxia.TelecomNingxiaPayMsgRepository;
import com.microservice.dao.repository.crawler.telecom.ningxia.TelecomNingxiaUserInfoRepository;
import com.microservice.dao.repository.crawler.telecom.ningxia.TelecomNingxiahtmlRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.ISms;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.ningxia")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.ningxia")
public class TelecomCommonNingXiaService implements ISms {

	public static final Logger log = LoggerFactory.getLogger(TelecomCommonNingXiaService.class);

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private TelecomNingxiahtmlRepository telecomNingxiahtmlRepository;

	@Autowired
	private TelecomNingxiaUserInfoRepository telecomNingxiaUserInfoRepository;

	@Autowired
	private TelecomNingxiaBusinessMsgRepository telecomNingxiaBusinessMsgRepository;

	@Autowired
	private TelecomNingxiaPayMsgRepository telecomNingxiaPayMsgRepository;

	@Autowired
	private TelecomNingxiaMonthBillHistoryRepository telecomNingxiaMonthBillHistoryRepository;

	@Autowired
	private TracerLog tracer;

	////////////////////////////////////////////////// 用户信息的爬取和采集///////////////////////////////////////////////////////////

	// 用户信息的爬取和采集 我的资料组
	public void crawlerWdzl(MessageLogin messageLogin) {
		System.out.println("用户信息的爬取和采集 我的资料组");
		tracer.addTag("service.crawlerWdzl.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());
		// 正在爬取基本信息，请耐心等待...
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_GATHER_BASE_CRAWLING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_GATHER_BASE_CRAWLING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_GATHER_BASE_CRAWLING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_GATHER_BASE_CRAWLING.getError_code());
		taskMobile.setError_message(null);
		// 本类中的方法
		save(taskMobile);

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		try {
			// String loginurl = "http://login.189.cn/login";
			// WebClient webclientlogin =
			// WebCrawler.getInstance().getNewWebClient();
			// WebRequest webRequestlogin;
			// webRequestlogin = new WebRequest(new URL(loginurl),
			// HttpMethod.GET);
			// HtmlPage pagelogin = webclientlogin.getPage(webRequestlogin);
			// // 获取对应的输入框
			// HtmlTextInput username = (HtmlTextInput)
			// pagelogin.getFirstByXPath("//input[@id='txtAccount']");
			// HtmlPasswordInput passwordInput = (HtmlPasswordInput) pagelogin
			// .getFirstByXPath("//input[@id='txtPassword']");
			// HtmlElement button = (HtmlElement)
			// pagelogin.getFirstByXPath("//a[@id='loginbtn']");
			// username.setText(messageLogin.getName());
			// passwordInput.setText(messageLogin.getPassword());
			// HtmlPage htmlpage = button.click();
			// String asXml = htmlpage.asXml();
			// if (asXml.indexOf("登录失败") != -1) {
			// System.out.println("登录失败！");
			// } else {
			// System.out.println("登录成功！");
			String wdzlurl = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=10000523&cityCode=nx";
			WebRequest webRequestwdzl;
			webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.GET);
			HtmlPage wdzl = webClient.getPage(webRequestwdzl);
			webClient = wdzl.getWebClient();
			int statusCode = wdzl.getWebResponse().getStatusCode();
			if (statusCode == 200) {
				System.out.println("我的资料组请求通过！");
				// 爬取数据
				String url = "http://nx.189.cn/bfapp/buffalo/CtQryService";
				WebRequest webRequest;
				String requestPayload = "<buffalo-call><method>getCustAndContInfo</method><string>1</string></buffalo-call>";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);

				webRequest.setAdditionalHeader("Accept", "*/*");
				webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
				webRequest.setAdditionalHeader("Connection", "keep-alive");
				webRequest.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
				webRequest.setAdditionalHeader("Host", "nx.189.cn");
				webRequest.setAdditionalHeader("Origin", "http://nx.189.cn");
				webRequest.setAdditionalHeader("Referer", "http://nx.189.cn/jt/zl/wdzl/?fastcode=10000523&cityCode=nx");
				webRequest.setAdditionalHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
				webRequest.setAdditionalHeader("X-Buffalo", "2.0");
				webRequest.setRequestBody(requestPayload);
				Page page = webClient.getPage(webRequest);
				String baseinfo = page.getWebResponse().getContentAsString();
				System.out.println("-----------------------用户基本信息的爬取 我的资料----------------------" + baseinfo);
				// 判断是否有数据
				if (!"<buffalo-reply><null></null></buffalo-reply>".equals(baseinfo)) {
					// 用户基本信息的爬取 我的资料
					TelecomNingxiahtml html = new TelecomNingxiahtml();
					html.setHtml(baseinfo);
					html.setPageCount(1);
					html.setTaskid(messageLogin.getTask_id());
					html.setType("用户基本信息");
					html.setUrl(url);
					telecomNingxiahtmlRepository.save(html);

					// 用户基本信息 我的资料组

					// 用户信息实体类 进行解析
					TelecomNingxiaUserInfo telecomNingxiaUserInfo = new TelecomNingxiaUserInfo();
					// 证件类型
					String paperstype = "";
					String[] splitpaperstype = baseinfo.split("<string>_CERT_TYPE</string>");
					String substringpaperstype = splitpaperstype[1].substring(0, 6);
					if ("<null>".equals(substringpaperstype)) {
						paperstype = "无信息";
					} else {
						String[] split2addr = splitpaperstype[1].split("</string>");
						paperstype = split2addr[0].substring(8, split2addr[0].length());
					}
					System.out.println("证件类型-----" + paperstype);
					// 身份证号
					String cardid = "";
					String[] splitcardid = baseinfo.split("<string>_CERT_NUMBER</string>");
					String substringcardid = splitcardid[1].substring(0, 6);
					if ("<null>".equals(substringcardid)) {
						cardid = "无信息";
					} else {
						String[] split2cardid = splitcardid[1].split("</string>");
						cardid = split2cardid[0].substring(8, split2cardid[0].length());
					}
					System.out.println("身份证号-----" + cardid);
					// 注册该号码地址
					String addr = "";
					String[] splitaddr = baseinfo.split("<string>_CERT_ADDRESS</string>");
					String substringaddr = splitaddr[1].substring(0, 6);
					if ("<null>".equals(substringaddr)) {
						addr = "无信息";
					} else {
						String[] split2addr = splitaddr[1].split("</string>");
						addr = split2addr[0].substring(8, split2addr[0].length());
					}
					System.out.println("注册该号码地址-----" + addr);
					// 姓名
					String name = "";
					String[] splitname = baseinfo.split("<string>_CUST_NAME</string>");
					String substringname = splitname[1].substring(0, 6);
					if ("<null>".equals(substringname)) {
						name = "无信息";
					} else {
						String[] split2name = splitname[1].split("</string>");
						name = split2name[0].substring(8, split2name[0].length());
					}
					System.out.println("姓名-----" + name);
					String operator = "电信";
					System.out.println("运营商-----" + operator);

					TelecomNingxiaUserInfo findByTaskid = telecomNingxiaUserInfoRepository
							.findByTaskid(taskMobile.getTaskid().trim());
					if (findByTaskid == null) {
						telecomNingxiaUserInfo.setTaskid(taskMobile.getTaskid());
						telecomNingxiaUserInfo.setAddr(addr);
						telecomNingxiaUserInfo.setCardid(cardid);
						telecomNingxiaUserInfo.setName(name);
						telecomNingxiaUserInfo.setOperator(operator);
						telecomNingxiaUserInfo.setPaperstype(paperstype);
						telecomNingxiaUserInfoRepository.save(telecomNingxiaUserInfo);
					} else {
						telecomNingxiaUserInfoRepository.updatewdzl(addr, cardid, name, operator, paperstype,
								findByTaskid.getTaskid());
					}

				} else {
					System.out.println("未爬取到我的资料组的基本信息！");
				}
			}

			// 基本信息采集完成
			taskMobile.setUserMsgStatus(200);
			taskMobile.setTaskid(messageLogin.getTask_id().trim());
			// 本类中的方法
			save(taskMobile);

			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 用户信息的爬取和采集 话费及余额组
	public void crawlerRemainMoney(MessageLogin messageLogin) {
		System.out.println("用户信息的爬取和采集 话费及余额组");
		tracer.addTag("service.crawlerRemainMoney.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());
		// 正在爬取基本信息，请耐心等待...
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_GATHER_BASE_CRAWLING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_GATHER_BASE_CRAWLING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_GATHER_BASE_CRAWLING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_GATHER_BASE_CRAWLING.getError_code());
		taskMobile.setError_message(null);
		// 本类中的方法
		save(taskMobile);

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		try {
			
			String url111 = "http://www.189.cn/dqmh/homogeneity/queryBalance.do";
			WebRequest webRequest111;
			webRequest111 = new WebRequest(new URL(url111), HttpMethod.POST);
			webRequest111.setRequestParameters(new ArrayList<NameValuePair>());
			webRequest111.getRequestParameters().add(new NameValuePair("queryitemtype", "1"));
			webRequest111.setAdditionalHeader("CACHE", "TCP_MISS");
			webRequest111.setAdditionalHeader("Cache-Control", "private,no-store,no-cache,must-revalidate");
			webRequest111.setAdditionalHeader("CC_CACHE", "TCP_MISS");
			webRequest111.setAdditionalHeader("Content-Type", "application/json;charset=utf-8");
			webRequest111.setAdditionalHeader("Date", "Thu, 18 Oct 2018 09:02:25 GMT");
			webRequest111.setAdditionalHeader("Pragma", "no-cache");

			webRequest111.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			webRequest111.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest111.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest111.setAdditionalHeader("Connection", "keep-alive");
			webRequest111.setAdditionalHeader("Host", "www.189.cn");
			webRequest111.setAdditionalHeader("Origin", "http://www.189.cn");
			webRequest111.setAdditionalHeader("Referer",
					"http://www.189.cn/dqmh/homogeneity/initCost.do?menuType=callandbalance&fastcode=20000777&cityCode=nx");
			webRequest111.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
			webRequest111.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			Page page = webClient.getPage(webRequest111);
			String contentAsString = page.getWebResponse().getContentAsString();
			System.out.println("用户基本信息的爬取 话费及余额组==========================" + contentAsString);

			// 用户基本信息的爬取 话费及余额组
			TelecomNingxiahtml html = new TelecomNingxiahtml();
			html.setHtml(contentAsString);
			html.setPageCount(1);
			html.setTaskid(messageLogin.getTask_id());
			html.setType("用户基本信息");
			html.setUrl(url111);
			telecomNingxiahtmlRepository.save(html);

			JSONObject jsonObj = JSONObject.fromObject(contentAsString);
			String string = jsonObj.getString("obj");
			JSONObject jsonObj2 = JSONObject.fromObject(string);
			// 余额
			String remainMoney = jsonObj2.getString("totalBalance");
			System.out.println("账户余额-----" + remainMoney);

			TelecomNingxiaUserInfo tlecomNingxiaUserInfo = new TelecomNingxiaUserInfo();
			TelecomNingxiaUserInfo findByTaskid = telecomNingxiaUserInfoRepository
					.findByTaskid(taskMobile.getTaskid().trim());
			if (findByTaskid == null) {
				tlecomNingxiaUserInfo.setTaskid(taskMobile.getTaskid());
				tlecomNingxiaUserInfo.setRemaining(remainMoney);
				telecomNingxiaUserInfoRepository.save(tlecomNingxiaUserInfo);
			} else {
				telecomNingxiaUserInfoRepository.updatewdyue(remainMoney, findByTaskid.getTaskid());
			}

			// 基本信息采集完成
			taskMobile.setUserMsgStatus(200);
			taskMobile.setTaskid(messageLogin.getTask_id().trim());
			// 本类中的方法
			save(taskMobile);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 业务信息的爬取和采集
	public void crawlerBusinessMsg(MessageLogin messageLogin) {
		System.out.println("业务信息的爬取和采集");
		tracer.addTag("service.crawlerBusinessMsg.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());
		// 正在爬取业务信息，请耐心等待...
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_GATHER_BASINESS_CRAWLING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_GATHER_BASINESS_CRAWLING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_GATHER_BASINESS_CRAWLING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_GATHER_BASINESS_CRAWLING.getError_code());
		taskMobile.setError_message(null);
		// 本类中的方法
		save(taskMobile);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			String wdzlurl = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=10000506&cityCode=nx";
			WebRequest webRequestwdzl;
			webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.POST);
			HtmlPage wdzl = webClient.getPage(webRequestwdzl);
			webClient = wdzl.getWebClient();
			int statusCode = wdzl.getWebResponse().getStatusCode();
			if (statusCode == 200) {
				String url = "http://nx.189.cn/bfapp/buffalo/CtProdService";
				WebRequest webRequest;
				String requestPayload = "<buffalo-call><method>qryVsop</method><string>" + messageLogin.getName()
						+ "</string></buffalo-call>";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				webRequest.setAdditionalHeader("Accept", "*/*");
				webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
				webRequest.setAdditionalHeader("Connection", "keep-alive");
				webRequest.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
				webRequest.setAdditionalHeader("Host", "nx.189.cn");
				webRequest.setAdditionalHeader("Origin", "http://nx.189.cn");
				webRequest.setAdditionalHeader("Referer", "http://nx.189.cn/jt/zz/td/?fastcode=10000506&cityCode=nx");
				webRequest.setAdditionalHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
				webRequest.setAdditionalHeader("X-Buffalo", "2.0");
				webRequest.setRequestBody(requestPayload);
				Page page = webClient.getPage(webRequest);
				String contentAsString = page.getWebResponse().getContentAsString();
				System.out.println("业务信息的爬取 增值业务组-----" + contentAsString);

				// 业务信息的爬取 增值业务组
				TelecomNingxiahtml html = new TelecomNingxiahtml();
				html.setHtml(contentAsString);
				html.setPageCount(1);
				html.setTaskid(messageLogin.getTask_id());
				html.setType("业务信息");
				html.setUrl(url);
				telecomNingxiahtmlRepository.save(html);

				// 遍历xml数据
				Document doc = Jsoup.parse(contentAsString);
				Elements link1 = doc.getElementsByTag("map");
				for (Element element : link1) {
					TelecomNingxiaBusinessMsg telecomNingxiaBusinessMsg = new TelecomNingxiaBusinessMsg();
					Elements link2 = element.getElementsByTag("string");
					// 业务名称
					String businessname = link2.get(3).text();
					System.out.println("业务名称-----" + businessname);
					// 品牌
					String brand = link2.get(7).text();
					System.out.println("品牌-----" + brand);
					// 费用周期
					String costperiod = link2.get(15).text();
					System.out.println("费用周期-----" + costperiod);
					// 业务费用
					String businesscost = link2.get(15).text();
					System.out.println("业务费用-----" + businesscost);
					// 生效时间
					String effecttime = link2.get(19).text();
					effecttime = effecttime.substring(0, 8);
					String netintime1 = effecttime.substring(0, 4);
					String netintime2 = effecttime.substring(4, 6);
					String netintime3 = effecttime.substring(6, 8);
					effecttime = netintime1 + "-" + netintime2 + "-" + netintime3;
					System.out.println("生效时间-----" + effecttime);
					telecomNingxiaBusinessMsg.setTaskid(messageLogin.getTask_id().trim());
					telecomNingxiaBusinessMsg.setBusinessname(businessname);
					telecomNingxiaBusinessMsg.setBrand(brand);
					telecomNingxiaBusinessMsg.setCostperiod(costperiod);
					telecomNingxiaBusinessMsg.setBusinesscost(businesscost);
					telecomNingxiaBusinessMsg.setEffecttime(effecttime);
					telecomNingxiaBusinessMsgRepository.save(telecomNingxiaBusinessMsg);

				}
				// 业务信息采集完成
				taskMobile.setBusinessMsgStatus(200);
				taskMobile.setTaskid(messageLogin.getTask_id().trim());
				// 本类中的方法
				save(taskMobile);

			}
		} catch (Exception e) {
			e.printStackTrace();
			// 业务信息采集完成
			taskMobile.setBusinessMsgStatus(404);
			taskMobile.setTaskid(messageLogin.getTask_id().trim());
			// 本类中的方法
			save(taskMobile);
		}
	}

	// 缴费信息的爬取和采集
	public void crawlerPaymsg(MessageLogin messageLogin) {
		System.out.println("缴费信息的爬取和采集");
		tracer.addTag("service.crawlerPaymsg.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());
		// 正在爬取缴费信息，请耐心等待...
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_GATHER_PAYMSG_CRAWLING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_GATHER_PAYMSG_CRAWLING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_GATHER_PAYMSG_CRAWLING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_GATHER_PAYMSG_CRAWLING.getError_code());
		taskMobile.setError_message(null);
		// 本类中的方法
		save(taskMobile);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			String wdzlurl = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000137&cityCode=nx";
			WebRequest webRequestwdzl;
			webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.GET);
			HtmlPage wdzl = webClient.getPage(webRequestwdzl);
			webClient = wdzl.getWebClient();
			int statusCode = wdzl.getWebResponse().getStatusCode();
			if (statusCode == 200) {
				String url = "http://nx.189.cn/bfapp/buffalo/CtQryService";
				WebRequest webRequest;
				String requestPayload = "<buffalo-call><method>qry_Order_jiaoyi_new</method><string>"
						+ messageLogin.getName()
						+ "</string><string>20150101</string><string>20350101</string></buffalo-call>";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);

				webRequest.setAdditionalHeader("Accept", "*/*");
				webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
				webRequest.setAdditionalHeader("Connection", "keep-alive");
				webRequest.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
				webRequest.setAdditionalHeader("Host", "nx.189.cn");
				webRequest.setAdditionalHeader("Origin", "http://nx.189.cn");
				webRequest.setAdditionalHeader("Referer", "http://nx.189.cn/jt/dd/jyd/?fastcode=20000137&cityCode=nx");
				webRequest.setAdditionalHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
				webRequest.setAdditionalHeader("X-Buffalo", "2.0");
				webRequest.setRequestBody(requestPayload);
				Page page = webClient.getPage(webRequest);
				String contentAsString = page.getWebResponse().getContentAsString();
				System.out.println(contentAsString);

				// 缴费信息的爬取 交易单查询
				TelecomNingxiahtml html = new TelecomNingxiahtml();
				html.setHtml(contentAsString);
				html.setPageCount(1);
				html.setTaskid(messageLogin.getTask_id());
				html.setType("缴费信息");
				html.setUrl(url);
				telecomNingxiahtmlRepository.save(html);

				// 解析缴费信息
				Document doc = Jsoup.parse(contentAsString);
				Elements link1 = doc.getElementsByTag("list");
				Elements elementsByTag = link1.get(0).getElementsByTag("map");
				for (Element element : elementsByTag) {
					TelecomNingxiaPayMsg telecomNingxiaPayMsg = new TelecomNingxiaPayMsg();
					Elements link2 = element.getElementsByTag("string");
					// 缴费时间
					String paydate = link2.get(3).text();
					System.out.println(paydate);
					// 缴费方式
					String payway = link2.get(5).text();
					System.out.println(payway);
					// 缴费金额
					Elements link = element.getElementsByTag("int");
					String paymoney = link.get(0).text();
					paymoney = paymoney.substring(0, paymoney.length() - 2) + "元";
					System.out.println(paymoney);
					// 缴费地点
					String payaddr = "地址一：" + link2.get(8).text() + "  地址二：" + link2.get(12).text();
					System.out.println(payaddr);
					telecomNingxiaPayMsg.setTaskid(messageLogin.getTask_id());
					telecomNingxiaPayMsg.setPaydate(paydate);
					telecomNingxiaPayMsg.setPayway(payway);
					telecomNingxiaPayMsg.setPaymoney(paymoney);
					telecomNingxiaPayMsg.setPayaddr(payaddr);
					telecomNingxiaPayMsgRepository.save(telecomNingxiaPayMsg);

				}
				// 缴费信息采集完成
				taskMobile.setPayMsgStatus(200);
				taskMobile.setTaskid(messageLogin.getTask_id().trim());
				// 本类中的方法
				save(taskMobile);

			}
		} catch (Exception e) {
			// 缴费信息采集完成
			taskMobile.setPayMsgStatus(404);
			taskMobile.setTaskid(messageLogin.getTask_id().trim());
			// 本类中的方法
			save(taskMobile);
			e.printStackTrace();
		}
	}

	// 月账单信息的爬取和采集
	public String crawlerMonthBill(MessageLogin messageLogin, int year) {
		System.out.println("月账单信息的爬取和采集");
		tracer.addTag("service.crawlerMonthBill.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());
		// 正在爬取月账单信息，请耐心等待...
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_GATHER_MONTHBILL_CRAWLING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_GATHER_MONTHBILL_CRAWLING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_GATHER_MONTHBILL_CRAWLING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_GATHER_MONTHBILL_CRAWLING.getError_code());
		taskMobile.setError_message(null);
		// 本类中的方法
		save(taskMobile);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			
			String yearString = "";
			yearString = year + "";
			
			String url11111 = "http://www.189.cn/dqmh/homogeneity/balanceOutDetailQuery.do";
			WebRequest webRequest11111;
			webRequest11111 = new WebRequest(new URL(url11111), HttpMethod.POST);
			webRequest11111.setRequestParameters(new ArrayList<NameValuePair>());
			webRequest11111.getRequestParameters().add(new NameValuePair("billingcycle", yearString));
			HtmlPage page = webClient.getPage(webRequest11111);
			// 月账单信息的爬取
			String asXml = page.asXml();
			System.out.println("月账单信息的爬取 -----" + asXml);
			Document doc = Jsoup.parse(asXml);

			// 月份
			String month = yearString;
			System.out.println("月份-----" + month);

			// 本月已产生话费
			Elements elementsByClass = doc.getElementsByClass("fs24");
			if (elementsByClass == null) {
				System.out.println("月账单获取失败！-----"+month);
			} else {
				String byycshf = elementsByClass.get(0).text();
				System.out.println("本月已产生话费-----" + byycshf);

				// 本期已付费用
				Elements by_right_01 = doc.getElementsByClass("by_right_01");
				String bqyffy = by_right_01.get(0).text();
				System.out.println("本期已付费用-----" + bqyffy);

				// 本期欠费
				String bqqf = by_right_01.get(1).text();
				System.out.println("本期欠费-----" + bqqf);

				// 本月总话费
				String byzhf = "";
				SimpleDateFormat df = new SimpleDateFormat("yyyyMM");// 设置日期格式
				String format = df.format(new Date());// new Date()为获取当前系统时间
				if (month.equals(format)) {
					// 当前月没有本月总话费
					byzhf = "无";
				}

				// 月账单信息的爬取
				TelecomNingxiahtml html = new TelecomNingxiahtml();
				String asXml3 = page.asXml();
				html.setHtml(asXml3);
				html.setPageCount(1);
				html.setTaskid(messageLogin.getTask_id());
				html.setType("月账单信息");
				html.setUrl(url11111);
				telecomNingxiahtmlRepository.save(html);

				TelecomNingxiaMonthBillHistory telecomNingxiaMonthBillHistory = new TelecomNingxiaMonthBillHistory();
				telecomNingxiaMonthBillHistory.setTaskid(messageLogin.getTask_id());
				telecomNingxiaMonthBillHistory.setMonth(month);
				telecomNingxiaMonthBillHistory.setByycshf(byycshf);
				telecomNingxiaMonthBillHistory.setBqyffy(bqyffy);
				telecomNingxiaMonthBillHistory.setBqqf(bqqf);
				telecomNingxiaMonthBillHistory.setByzhf(byzhf);
				telecomNingxiaMonthBillHistoryRepository.save(telecomNingxiaMonthBillHistory);

				// 月账单信息采集完成
				taskMobile.setAccountMsgStatus(200);
				taskMobile.setTaskid(messageLogin.getTask_id().trim());
				// 本类中的方法
				save(taskMobile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 月账单信息采集完成
			taskMobile.setAccountMsgStatus(404);
			taskMobile.setTaskid(messageLogin.getTask_id().trim());
			// 本类中的方法
			save(taskMobile);
		}
		return null;
	}

	// 得到验证码方法
	public TaskMobile sendSms(MessageLogin messageLogin) {
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		System.out.println("登陆的cookies：" + cook);
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			String wdzlurl = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000776";
			WebRequest webRequestwdzl;
			webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.GET);
			HtmlPage wdzl = webClient.getPage(webRequestwdzl);
			webClient = wdzl.getWebClient();
			int statusCode = wdzl.getWebResponse().getStatusCode();
			if (statusCode == 200) {

				String packurl7 = "http://nx.189.cn/bfapp/buffalo/CtQryService";
				String requestPayloadSend7 = "<buffalo-call><method>getCustOfBillByCustomerCode</method><string>2951121277270000__giveup</string></buffalo-call>";
				WebRequest webRequestpack7 = new WebRequest(new URL(packurl7), HttpMethod.POST);
				webRequestpack7.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
				webRequestpack7.setAdditionalHeader("Host", "nx.189.cn");
				webRequestpack7.setAdditionalHeader("Origin", "http://nx.189.cn");
				webRequestpack7.setAdditionalHeader("Referer",
						"http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
				webRequestpack7.setAdditionalHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
				webRequestpack7.setRequestBody(requestPayloadSend7);
				webClient.getPage(webRequestpack7);

				String packurl8 = "http://nx.189.cn/bfapp/buffalo/CtQryService";
				String requestPayloadSend8 = "<buffalo-call><method>getFeeNumByHT</method><string>10732369</string><string>201708</string></buffalo-call>";
				WebRequest webRequestpack8 = new WebRequest(new URL(packurl8), HttpMethod.POST);
				webRequestpack8.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
				webRequestpack8.setAdditionalHeader("Host", "nx.189.cn");
				webRequestpack8.setAdditionalHeader("Origin", "http://nx.189.cn");
				webRequestpack8.setAdditionalHeader("Referer",
						"http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
				webRequestpack8.setAdditionalHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
				webRequestpack8.setRequestBody(requestPayloadSend8);
				webClient.getPage(webRequestpack8);

				String packurl9 = "http://nx.189.cn/bfapp/buffalo/CtQryService";
				String requestPayloadSend9 = "<buffalo-call><method>getSelectedFeeProdNum</method><string>undefined</string><string>"
						+ messageLogin.getName().trim() + "</string><string>2</string></buffalo-call>";
				WebRequest webRequestpack9 = new WebRequest(new URL(packurl9), HttpMethod.POST);
				webRequestpack9.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
				webRequestpack9.setAdditionalHeader("Host", "nx.189.cn");
				webRequestpack9.setAdditionalHeader("Origin", "http://nx.189.cn");
				webRequestpack9.setAdditionalHeader("Referer",
						"http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
				webRequestpack9.setAdditionalHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
				webRequestpack9.setRequestBody(requestPayloadSend9);
				webClient.getPage(webRequestpack9);

				// 发送验证码接口
				String sendurl = "http://nx.189.cn/bfapp/buffalo/CtSubmitService";
				String requestPayloadSend = "<buffalo-call><method>sendDXYzmForBill</method></buffalo-call>";
				WebRequest webRequestSend = new WebRequest(new URL(sendurl), HttpMethod.POST);
				webRequestSend.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
				webRequestSend.setAdditionalHeader("Host", "nx.189.cn");
				webRequestSend.setAdditionalHeader("Origin", "http://nx.189.cn");
				webRequestSend.setAdditionalHeader("Referer",
						"http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
				webRequestSend.setAdditionalHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
				webRequestSend.setRequestBody(requestPayloadSend);
				Page pageSend = webClient.getPage(webRequestSend);

				// 保存发送验证码之后的cookies
				String cookieString = CommonUnit.transcookieToJson(webClient);
				taskMobile.setCookies(cookieString);
				// 登录成功状态更新
				save(taskMobile);

				String send = pageSend.getWebResponse().getContentAsString();
				System.out.println("发送验证码接口" + send);
				if (send.contains("base.Success")) {
					System.out.println("验证码发送成功！");
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
					taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getError_code());
					// 保存状态
					save(taskMobile);
				} else {
					System.out.println("验证码发送失败！");
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
					taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getError_code());
					// 保存状态
					save(taskMobile);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskMobile;
	}

	// 验证验证码方法
	public TaskMobile verifySms(MessageLogin messageLogin) {
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			String check = "";
			String name = messageLogin.getName().trim();
			String code = messageLogin.getSms_code().trim();
			String checkurl = "http://nx.189.cn/bfapp/buffalo/CtQryService";
			String requestPayloadCheck = "<buffalo-call><method>validBillSMS</method><string>" + name
					+ "</string><string>" + code + "</string></buffalo-call>";
			WebRequest webRequestCheck = new WebRequest(new URL(checkurl), HttpMethod.POST);
			webRequestCheck.setAdditionalHeader("Accept", "*/*");
			webRequestCheck.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequestCheck.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequestCheck.setAdditionalHeader("Connection", "keep-alive");
			webRequestCheck.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
			webRequestCheck.setAdditionalHeader("Host", "nx.189.cn");
			webRequestCheck.setAdditionalHeader("Referer",
					"http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
			webRequestCheck.setRequestBody(requestPayloadCheck);
			Page pageCheck = webClient.getPage(webRequestCheck);
			check = pageCheck.getWebResponse().getContentAsString();
			System.out.println("验证验证码的接口" + check);
			if (check.contains("base.Success")) {
				System.out.println("验证码验证成功！");
				taskMobile.setPhase("PASSWORD");
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getError_code());
				// 保存状态
				save(taskMobile);
			} else {
				System.out.println("验证码验证失败！");
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getError_code());
				// 保存状态
				save(taskMobile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskMobile;
	}

	public void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}

	public TaskMobile findtaskMobile(String taskid) {
		return taskMobileRepository.findByTaskid(taskid);
	}

	// 某个月的第一天
	public String getFirstMonthdate(int i) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		// 某月的第一天
		calendar.add(Calendar.MONTH, -i);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return format.format(calendar.getTime());
	}

	// 某个月的最后一天
	public String getLastMonthdate(int i) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -i);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return format.format(calendar.getTime());
	}

}