package app.service;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

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
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.neimenggu.TelecomNeimengguCallHistory;
import com.microservice.dao.entity.crawler.telecom.neimenggu.TelecomNeimengguMessageHistory;
import com.microservice.dao.entity.crawler.telecom.neimenggu.TelecomNeimengguMonthBillHistory;
import com.microservice.dao.entity.crawler.telecom.neimenggu.TelecomNeimengguPayMsg;
import com.microservice.dao.entity.crawler.telecom.neimenggu.TelecomNeimengguUserInfo;
import com.microservice.dao.entity.crawler.telecom.neimenggu.TelecomNeimengguhtml;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.neimenggu.TelecomNeimengguCallHistoryRepository;
import com.microservice.dao.repository.crawler.telecom.neimenggu.TelecomNeimengguMessageHistoryRepository;
import com.microservice.dao.repository.crawler.telecom.neimenggu.TelecomNeimengguMonthBillHistoryRepository;
import com.microservice.dao.repository.crawler.telecom.neimenggu.TelecomNeimengguPayMsgRepository;
import com.microservice.dao.repository.crawler.telecom.neimenggu.TelecomNeimengguUserInfoRepository;
import com.microservice.dao.repository.crawler.telecom.neimenggu.TelecomNeimengguhtmlRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.ISms;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.neimenggu")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.neimenggu")
public class TelecomCommonService implements ISms {

	public static final Logger log = LoggerFactory.getLogger(TelecomCommonService.class);

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private TelecomNeimengguUserInfoRepository telecomHuhehaoteUserInfoRepository;

	@Autowired
	private TelecomNeimengguMonthBillHistoryRepository telecomHuhehaoteMonthBillHistoryRepository;

	@Autowired
	private TelecomNeimengguPayMsgRepository telecomHuhehaotePayMsgRepository;

	@Autowired
	private TelecomNeimengguhtmlRepository telecomHuhehaotehtmlRepository;

	@Autowired
	private TelecomNeimengguCallHistoryRepository telecomHuhehaoteCallHistoryRepository;

	@Autowired
	private TelecomNeimengguMessageHistoryRepository telecomHuhehaoteMessageHistoryRepository;

	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;

	@Autowired
	private TracerLog tracer;

	public HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

	// 用户信息的爬取和采集-----------我的余额
	public String crawlerWdyue(MessageLogin messageLogin) {
		tracer.addTag("service.crawlerWdyue.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		String remaining = "";

		try {
			// 获取账户余额请求的入参
			String packurl1 = "http://nm.189.cn/selfservice/cust/queryAllProductInfo";
			String requestPayloadSend1 = "{\"qryAccNbrType\":\"\"}";
			WebRequest webRequestpack1 = new WebRequest(new URL(packurl1), HttpMethod.POST);
			webRequestpack1.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
			webRequestpack1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequestpack1.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequestpack1.setAdditionalHeader("Connection", "keep-alive");
			webRequestpack1.setAdditionalHeader("Content-Type", "application/json");
			webRequestpack1.setAdditionalHeader("Host", "nm.189.cn");
			webRequestpack1.setAdditionalHeader("Origin", "http://nm.189.cn");
			webRequestpack1.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
			webRequestpack1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequestpack1.setRequestBody(requestPayloadSend1);
			Page page = webClient.getPage(webRequestpack1);
			// [{"accNbr":"18947109133","areaCode":"0471","prodSpecId":"378","prodSpecName":"??","acctNbr":"20000000001824039","productType":"4"}]
			String check = page.getWebResponse().getContentAsString();
			JSONArray firstStep = JSONArray.fromObject(check);
			String accNbr = firstStep.getJSONObject(0).getString("accNbr");
			String accNbrType = firstStep.getJSONObject(0).getString("productType");
			String areaCode = firstStep.getJSONObject(0).getString("areaCode");
			String prodSpecId = firstStep.getJSONObject(0).getString("prodSpecId");
			String prodSpecName = firstStep.getJSONObject(0).getString("prodSpecName");

			// 组装成获取余额请求的参数
			String requestPayload = "{\"accNbr\":\"" + accNbr + "\",\"accNbrType\":\"" + accNbrType
					+ "\",\"areaCode\":\"" + areaCode + "\",\"prodSpecId\":\"" + prodSpecId
					+ "\",\"smsCode\":\"\",\"prodSpecName\":\"" + prodSpecName + "\"}";
			String yueUrl = "http://nm.189.cn/selfservice/bill/hfShowSGW";
			WebRequest webRequestyue = new WebRequest(new URL(yueUrl), HttpMethod.POST);
			webRequestyue.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
			webRequestyue.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequestyue.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequestyue.setAdditionalHeader("Connection", "keep-alive");
			webRequestyue.setAdditionalHeader("Content-Type", "application/json");
			webRequestyue.setAdditionalHeader("Host", "nm.189.cn");
			webRequestyue.setAdditionalHeader("Origin", "http://nm.189.cn");
			webRequestyue.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
			webRequestyue.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequestyue.setRequestBody(requestPayload);
			Page pageyue = webClient.getPage(webRequestyue);
			// {"balance":{"resultMsg":"查询成功","tongyongBalance":"496","resultCode":"0","zhuanyongBalance":"0"}}
			String yueinfo = pageyue.getWebResponse().getContentAsString();
			System.out.println("用户基本信息-我的余额-----" + yueinfo);

			if (yueinfo.contains("服务忙，请稍后再试")) {
System.out.println("服务忙，请稍后再试-----");
			} else {

				JSONObject firstStep1 = JSONObject.fromObject(yueinfo);
				String balance = firstStep1.getString("balance");
				JSONObject firstStep2 = JSONObject.fromObject(balance);
				// 账户余额
				String tongyongBalance = firstStep2.getString("tongyongBalance");
				int remaining2 = Integer.parseInt(tongyongBalance) / 100;
				remaining = remaining2 + "";

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return remaining;

	}

	// 用户信息的爬取和采集-----------其他信息
	public void crawlerWdother(MessageLogin messageLogin) {
		tracer.addTag("service.crawlerWdother.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		
		try {
			// 获取其他信息请求的入参
			String packurl1 = "http://nm.189.cn/selfservice/cust/queryAllProductInfo";
			String requestPayloadSend1 = "{\"qryAccNbrType\":\"\"}";
			WebRequest webRequestpack1 = new WebRequest(new URL(packurl1), HttpMethod.POST);
			webRequestpack1.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
			webRequestpack1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequestpack1.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequestpack1.setAdditionalHeader("Connection", "keep-alive");
			webRequestpack1.setAdditionalHeader("Content-Type", "application/json");
			webRequestpack1.setAdditionalHeader("Host", "nm.189.cn");
			webRequestpack1.setAdditionalHeader("Origin", "http://nm.189.cn");
			webRequestpack1.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
			webRequestpack1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequestpack1.setRequestBody(requestPayloadSend1);
			Page page = webClient.getPage(webRequestpack1);
			// [{"accNbr":"18947109133","areaCode":"0471","prodSpecId":"378","prodSpecName":"??","acctNbr":"20000000001824039","productType":"4"}]
			String check = page.getWebResponse().getContentAsString();
			JSONArray firstStep = JSONArray.fromObject(check);
			String accNbr = firstStep.getJSONObject(0).getString("accNbr");
			String accNbrType = firstStep.getJSONObject(0).getString("productType");
			String areaCode = firstStep.getJSONObject(0).getString("areaCode");

			// 组装成获取基本信息和程控功能信息的请求的参数
			String requestPayload = "{\"accNbr\":\"" + accNbr + "\",\"accNbrType\":\"" + accNbrType
					+ "\",\"areaCode\":\"" + areaCode + "\"}";
			String url = "http://nm.189.cn/selfservice/cust/querypinfo";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "application/json");
			webRequest.setAdditionalHeader("Host", "nm.189.cn");
			webRequest.setAdditionalHeader("Origin", "http://nm.189.cn");
			webRequest.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest.setRequestBody(requestPayload);
			Page pageyue = webClient.getPage(webRequest);
			String yueinfo = pageyue.getWebResponse().getContentAsString();
			System.out.println("用户基本信息-其他信息-----" + yueinfo);

			// 用户基本信息的爬取
			TelecomNeimengguhtml html = new TelecomNeimengguhtml();
			html.setHtml(yueinfo);
			html.setPageCount(1);
			html.setTaskid(messageLogin.getTask_id());
			html.setType("用户基本信息-其他信息");
			html.setUrl(url);
			telecomHuhehaotehtmlRepository.save(html);

			JSONObject step = JSONObject.fromObject(yueinfo);
			String basicInfo = step.getString("BasicInfo");
			JSONObject firstStep2 = JSONObject.fromObject(basicInfo);
			String resultMsg = firstStep2.getString("resultMsg");
			if ("查询成功".equals(resultMsg)) {
				// 解析response对象
				String prodRecords = step.getString("prodRecords");
				JSONArray array = JSONArray.fromObject(prodRecords);
				String custInfo = array.getJSONObject(0).getString("custInfo");
				JSONObject other = JSONObject.fromObject(custInfo);
				// 运营商
				String operator = other.getString("areaName");
				System.out.println("运营商-----" + operator);
				// 身份证号
				String cardid = other.getString("indentNbr");
				System.out.println("身份证号-----" + cardid);
				// 证件类型
				String paperstype = other.getString("indentNbrTypeName");
				System.out.println("证件类型-----" + paperstype);
				// 地址
				String addr = other.getString("mailAddr");
				System.out.println("地址-----" + addr);
				// 姓名
				String name = other.getString("name");
				System.out.println("姓名-----" + name);

				String productInfo = array.getJSONObject(0).getString("productInfo");
				JSONObject other2 = JSONObject.fromObject(productInfo);
				// 电话号码
				String phone = other2.getString("accNbr");
				System.out.println("电话号码-----" + phone);
				// 账户状态
				String accountstatus = other2.getString("productStatusName");
				System.out.println("账户状态-----" + accountstatus);
				// 入网时间
				String nettime = other2.getString("servCreateDate");
				System.out.println("入网时间-----" + nettime);
				// 产品名称
				String proname = other2.getString("prodSpecName");
				System.out.println("产品名称-----" + proname);
				// 余额
				String remaining = crawlerWdyue(messageLogin);
				System.out.println("余额-----" + remaining);

				TelecomNeimengguUserInfo telecomNeimengguUserInfo = new TelecomNeimengguUserInfo();
				telecomNeimengguUserInfo.setTaskid(taskMobile.getTaskid());
				telecomNeimengguUserInfo.setRemaining(remaining);
				telecomNeimengguUserInfo.setProname(proname);
				telecomNeimengguUserInfo.setNettime(nettime);
				telecomNeimengguUserInfo.setAccountstatus(accountstatus);
				telecomNeimengguUserInfo.setPhone(phone);
				telecomNeimengguUserInfo.setName(name);
				telecomNeimengguUserInfo.setAddr(addr);
				telecomNeimengguUserInfo.setPaperstype(paperstype);
				telecomNeimengguUserInfo.setCardid(cardid);
				telecomNeimengguUserInfo.setOperator(operator);
				
				telecomHuhehaoteUserInfoRepository.save(telecomNeimengguUserInfo);
				
				// 更新task表
				taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 200, "基本信息-其他信息采集中！");
				taskMobile.setUserMsgStatus(200);
				taskMobileRepository.save(taskMobile);
			} else {
				// 更新task表
				taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 404, "基本信息-其他信息采集中！");
				taskMobile.setUserMsgStatus(404);
				taskMobileRepository.save(taskMobile);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// 更新task表
			taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 404, "基本信息-其他信息采集中！");
			taskMobile.setUserMsgStatus(404);
			taskMobileRepository.save(taskMobile);
		}
	}

	// 当前月账单信息的爬取和采集
	public void crawlerMonthBill(MessageLogin messageLogin) {
		tracer.addTag("service.crawlerMonthBill.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			// 获取账户信息请求的入参
			String packurl1 = "http://nm.189.cn/selfservice/cust/queryAllProductInfo";
			String requestPayloadSend1 = "{\"qryAccNbrType\":\"\"}";
			WebRequest webRequestpack1 = new WebRequest(new URL(packurl1), HttpMethod.POST);
			webRequestpack1.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
			webRequestpack1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequestpack1.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequestpack1.setAdditionalHeader("Connection", "keep-alive");
			webRequestpack1.setAdditionalHeader("Content-Type", "application/json");
			webRequestpack1.setAdditionalHeader("Host", "nm.189.cn");
			webRequestpack1.setAdditionalHeader("Origin", "http://nm.189.cn");
			webRequestpack1.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
			webRequestpack1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequestpack1.setRequestBody(requestPayloadSend1);
			Page page = webClient.getPage(webRequestpack1);
			// [{"accNbr":"18947109133","areaCode":"0471","prodSpecId":"378","prodSpecName":"??","acctNbr":"20000000001824039","productType":"4"}]
			String check = page.getWebResponse().getContentAsString();
			JSONArray firstStep = JSONArray.fromObject(check);
			String accNbr = firstStep.getJSONObject(0).getString("accNbr");
			String accNbrType = firstStep.getJSONObject(0).getString("productType");
			String areaCode = firstStep.getJSONObject(0).getString("areaCode");
			String prodSpecId = firstStep.getJSONObject(0).getString("prodSpecId");
			String prodSpecName = firstStep.getJSONObject(0).getString("prodSpecName");

			String url = "http://nm.189.cn/selfservice/bill/dyzdQuery";
			String requestPayload = "{\"accNbr\":\"" + accNbr + "\",\"accNbrType\":\"" + accNbrType
					+ "\",\"areaCode\":\"" + areaCode + "\",\"prodSpecId\":\"" + prodSpecId
					+ "\",\"smsCode\":\"\",\"prodSpecName\":\"" + prodSpecName + "\"}";

			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "application/json");
			webRequest.setAdditionalHeader("Host", "nm.189.cn");
			webRequest.setAdditionalHeader("Origin", "http://nm.189.cn");
			webRequest.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest.setRequestBody(requestPayload);
			Page pageyue = webClient.getPage(webRequest);
			String yueinfo = pageyue.getWebResponse().getContentAsString();
			System.out.println("月账单信息-当月信息-----" + yueinfo);

			TelecomNeimengguhtml html = new TelecomNeimengguhtml();
			html.setHtml(yueinfo);
			html.setPageCount(1);
			html.setTaskid(messageLogin.getTask_id());
			html.setType("月账单信息-当月信息");
			html.setUrl(url);
			telecomHuhehaotehtmlRepository.save(html);

			JSONObject chengStep = JSONObject.fromObject(yueinfo);
			String resultMsg = chengStep.getString("resultMsg");
			System.out.println(resultMsg);
			if ("成功".equals(resultMsg)) {
				System.out.println("获取数据成功！");
				String pointInfo = chengStep.getString("resultSet");
				JSONObject array = JSONObject.fromObject(pointInfo);
				// 月份
				String month = array.getString("billingMonth");
				// 月份总计
				String monthall = array.getString("total");

				String acctItemDto = array.getString("AcctItemDto");
				JSONArray acctItem = JSONArray.fromObject(acctItemDto);
				for (int i = 0; i < acctItem.size() - 1; i++) {
					// 项目名称
					String chargeType = acctItem.getJSONObject(i).getString("chargeType");
					// 项目费用
					String charge = acctItem.getJSONObject(i).getString("charge");

					TelecomNeimengguMonthBillHistory telecomHuhehaoteMonthBillHistory = new TelecomNeimengguMonthBillHistory();
					telecomHuhehaoteMonthBillHistory.setTaskid(messageLogin.getTask_id().trim());
					telecomHuhehaoteMonthBillHistory.setMonth(month);
					telecomHuhehaoteMonthBillHistory.setMonthall(monthall);
					telecomHuhehaoteMonthBillHistory.setXmmc(chargeType);
					telecomHuhehaoteMonthBillHistory.setXmfy(charge);
					telecomHuhehaoteMonthBillHistoryRepository.save(telecomHuhehaoteMonthBillHistory);
				}
				// 更新task表
				taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 200, "月账单当月信息采集中！");
				taskMobile.setAccountMsgStatus(200);
				taskMobileRepository.save(taskMobile);

			} else {
				// 更新task表
				taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 404, "月账单当月信息采集中！");
				taskMobile.setAccountMsgStatus(404);
				taskMobileRepository.save(taskMobile);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// 更新task表
			taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 404, "月账单当月信息采集中！");
			taskMobile.setAccountMsgStatus(404);
			taskMobileRepository.save(taskMobile);
		}
	}

	// 历史月账单信息的爬取和采集
	public void crawlerMonthBillHistory(MessageLogin messageLogin, int year) {
		tracer.addTag("service.crawlerMonthBillHistory.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			// 获取账户信息请求的入参
			String packurl1 = "http://nm.189.cn/selfservice/cust/queryAllProductInfo";
			String requestPayloadSend1 = "{\"qryAccNbrType\":\"\"}";
			WebRequest webRequestpack1 = new WebRequest(new URL(packurl1), HttpMethod.POST);
			webRequestpack1.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
			webRequestpack1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequestpack1.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequestpack1.setAdditionalHeader("Connection", "keep-alive");
			webRequestpack1.setAdditionalHeader("Content-Type", "application/json");
			webRequestpack1.setAdditionalHeader("Host", "nm.189.cn");
			webRequestpack1.setAdditionalHeader("Origin", "http://nm.189.cn");
			webRequestpack1.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
			webRequestpack1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequestpack1.setRequestBody(requestPayloadSend1);
			Page page = webClient.getPage(webRequestpack1);
			// [{"accNbr":"18947109133","areaCode":"0471","prodSpecId":"378","prodSpecName":"??","acctNbr":"20000000001824039","productType":"4"}]
			String check = page.getWebResponse().getContentAsString();
			JSONArray firstStep = JSONArray.fromObject(check);
			String accNbr = firstStep.getJSONObject(0).getString("accNbr");
			String accNbrType = firstStep.getJSONObject(0).getString("productType");
			String areaCode = firstStep.getJSONObject(0).getString("areaCode");
			String prodSpecId = firstStep.getJSONObject(0).getString("prodSpecId");
			String prodSpecName = firstStep.getJSONObject(0).getString("prodSpecName");
			String billingCycle = year + "".trim();
			// 对应的参数在上面
			String request = "{\"accNbr\":\"" + accNbr + "\",\"accNbrType\":\"" + accNbrType + "\",\"areaCode\":\""
					+ areaCode + "\",\"prodSpecId\":\"" + prodSpecId + "\",\"smsCode\":\"\",\"prodSpecName\":\""
					+ prodSpecName + "\",\"billingCycle\":\"" + billingCycle + "\"}";
			String urlhistory = "http://nm.189.cn/selfservice/bill/hfzczdQuery";
			WebRequest webRequestHistory = new WebRequest(new URL(urlhistory), HttpMethod.POST);
			webRequestHistory.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
			webRequestHistory.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequestHistory.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequestHistory.setAdditionalHeader("Connection", "keep-alive");
			webRequestHistory.setAdditionalHeader("Content-Type", "application/json");
			webRequestHistory.setAdditionalHeader("Host", "nm.189.cn");
			webRequestHistory.setAdditionalHeader("Origin", "http://nm.189.cn");
			webRequestHistory.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
			webRequestHistory.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequestHistory.setRequestBody(request);
			Page pagehistory = webClient.getPage(webRequestHistory);
			String historyinfo = pagehistory.getWebResponse().getContentAsString();
			System.out.println("月账单信息-历史月信息-----" + historyinfo);

			TelecomNeimengguhtml html = new TelecomNeimengguhtml();
			html.setHtml(historyinfo);
			html.setPageCount(1);
			html.setTaskid(messageLogin.getTask_id());
			html.setType("月账单信息-历史月信息");
			html.setUrl(urlhistory);
			telecomHuhehaotehtmlRepository.save(html);

			JSONObject chengStep = JSONObject.fromObject(historyinfo);
			String resultMsg = chengStep.getString("resultCode");
			System.out.println(resultMsg);
			if ("POR-0000".equals(resultMsg)) {
				System.out.println("数据获取成功！");
				String resultSet = chengStep.getString("resultSet");
				JSONObject base = JSONObject.fromObject(resultSet);
				// 合计
				String monthall = base.getString("sumCharge");
				// 月份
				String month = base.getString("time");
				String base7 = base.getString("hmtl");
				// 套餐费
				String mealfee = "";
				// 上网通讯费
				String netphonefee = "";
				// 代收费
				String collectionfee = "";
				// 短信费
				String msgfee = "";
				// 语音通话费
				String inlandfee = "";
				if (base7.contains("套餐费")) {
					String[] splitt = base7.split("<td>套餐费</td>");
					String[] split2t = splitt[1].split("元");
					String[] split3t = split2t[0].split("<td>");
					mealfee = split3t[1];
					System.out.println("套餐费" + mealfee);
				}
				if (base7.contains("上网通讯费")) {
					String[] splitt = base7.split("<td>上网通讯费</td>");
					String[] split2t = splitt[1].split("元");
					String[] split3t = split2t[0].split("<td>");
					netphonefee = split3t[1];
					System.out.println("上网通讯费" + netphonefee);
				}
				if (base7.contains("代收费")) {
					String[] splitt = base7.split("<td>代收费</td>");
					String[] split2t = splitt[1].split("元");
					String[] split3t = split2t[0].split("<td>");
					collectionfee = split3t[1];
					System.out.println("代收费" + collectionfee);
				}
				if (base7.contains("短信费")) {
					String[] split = base7.split("<td>短信费</td>");
					String[] split2 = split[1].split("元");
					String[] split3 = split2[0].split("<td>");
					msgfee = split3[1];
					System.out.println("短信费" + msgfee);
				}

				if (base7.contains("语音通话费")) {
					String[] splity = base7.split("<td>语音通话费</td>");
					String[] split2y = splity[1].split("元");
					String[] split3y = split2y[0].split("<td>");
					inlandfee = split3y[1];
					System.out.println("语音通话费" + inlandfee);
				}
				TelecomNeimengguMonthBillHistory telecomHuhehaoteMonthBillHistory = new TelecomNeimengguMonthBillHistory();
				telecomHuhehaoteMonthBillHistory.setTaskid(messageLogin.getTask_id().trim());
				telecomHuhehaoteMonthBillHistory.setMonth(month);
				telecomHuhehaoteMonthBillHistory.setMonthall(monthall);
				telecomHuhehaoteMonthBillHistoryRepository.save(telecomHuhehaoteMonthBillHistory);

				// 更新task表
				taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 200, "月账单历史月信息采集中！");
				taskMobile.setAccountMsgStatus(200);
				taskMobileRepository.save(taskMobile);
			} else {
				// 更新task表
				taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 404, "月账单历史月信息采集中！");
				taskMobile.setAccountMsgStatus(404);
				taskMobileRepository.save(taskMobile);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// 更新task表
			taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 404, "月账单历史月信息采集中！");
			taskMobile.setAccountMsgStatus(404);
			taskMobileRepository.save(taskMobile);
		}
	}

	// 缴费信息的爬取和采集
	public void crawlerPaymsg(MessageLogin messageLogin, String year) {
		tracer.addTag("service.crawlerPaymsg.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			// 获取账户信息请求的入参
			String packurl1 = "http://nm.189.cn/selfservice/cust/queryAllProductInfo";
			String requestPayloadSend1 = "{\"qryAccNbrType\":\"\"}";
			WebRequest webRequestpack1 = new WebRequest(new URL(packurl1), HttpMethod.POST);
			webRequestpack1.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
			webRequestpack1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequestpack1.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequestpack1.setAdditionalHeader("Connection", "keep-alive");
			webRequestpack1.setAdditionalHeader("Content-Type", "application/json");
			webRequestpack1.setAdditionalHeader("Host", "nm.189.cn");
			webRequestpack1.setAdditionalHeader("Origin", "http://nm.189.cn");
			webRequestpack1.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
			webRequestpack1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequestpack1.setRequestBody(requestPayloadSend1);
			Page page = webClient.getPage(webRequestpack1);
			// [{"accNbr":"18947109133","areaCode":"0471","prodSpecId":"378","prodSpecName":"??","acctNbr":"20000000001824039","productType":"4"}]
			String check = page.getWebResponse().getContentAsString();
			JSONArray firstStep = JSONArray.fromObject(check);
			String accNbr = firstStep.getJSONObject(0).getString("accNbr");
			String accNbrType = firstStep.getJSONObject(0).getString("productType");
			String areaCode = firstStep.getJSONObject(0).getString("areaCode");
			String prodSpecId = firstStep.getJSONObject(0).getString("prodSpecId");
			// String prodSpecName =
			// firstStep.getJSONObject(0).getString("prodSpecName");

			String url = "http://nm.189.cn/selfservice/bill/jfjlQuerySGW";

			String requestPayload = "{\"accNbr\":\"" + accNbr + "\",\"accNbrType\":\"" + accNbrType
					+ "\",\"areaCode\":\"" + areaCode + "\",\"prodSpecId\":\"" + prodSpecId + "\",\"queryDate\":\""
					+ year.trim() + "\"}";

			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "application/json");
			webRequest.setAdditionalHeader("Host", "nm.189.cn");
			webRequest.setAdditionalHeader("Origin", "http://nm.189.cn");
			webRequest.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest.setRequestBody(requestPayload);
			Page pageyue = webClient.getPage(webRequest);
			String yueinfo = pageyue.getWebResponse().getContentAsString();
			System.out.println("缴费信息-----" + yueinfo);

			TelecomNeimengguhtml html = new TelecomNeimengguhtml();
			html.setHtml(yueinfo);
			html.setPageCount(1);
			html.setTaskid(messageLogin.getTask_id());
			html.setType("缴费信息");
			html.setUrl(url);
			telecomHuhehaotehtmlRepository.save(html);

			// 解析缴费数据
			JSONObject chengStep = JSONObject.fromObject(yueinfo);
			String resultMsg = chengStep.getString("resultCode");
			System.out.println(resultMsg);
			if ("0".equals(resultMsg)) {
				System.out.println("获取数据成功！");
				String resultSet = chengStep.getString("information");
				JSONArray history = JSONArray.fromObject(resultSet);
				for (int i = 0; i < history.size(); i++) {
					// 交费金额
					String paymoney = history.getJSONObject(i).getString("amount");
					// 付款时间
					String paydate = history.getJSONObject(i).getString("paymentDate");
					// 交费地点
					String payaddr = history.getJSONObject(i).getString("staffId");
					// 销账金额
					String xzmoney = history.getJSONObject(i).getString("charge");
					// 违约金
					String breakmoney = history.getJSONObject(i).getString("due");
					// 交费方式
					String payway = history.getJSONObject(i).getString("paymentMethod");
					TelecomNeimengguPayMsg telecomHuhehaotePayMsg = new TelecomNeimengguPayMsg();
					telecomHuhehaotePayMsg.setTaskid(messageLogin.getTask_id().trim());
					telecomHuhehaotePayMsg.setPaymoney(paymoney);
					telecomHuhehaotePayMsg.setPaydate(paydate);
					telecomHuhehaotePayMsg.setPayaddr(payaddr);
					telecomHuhehaotePayMsg.setXzmoney(xzmoney);
					telecomHuhehaotePayMsg.setBreakmoney(breakmoney);
					telecomHuhehaotePayMsg.setPayway(payway);
					telecomHuhehaotePayMsgRepository.save(telecomHuhehaotePayMsg);
				}

				// 更新task表
				taskMobileRepository.updatePayMsgStatus(taskMobile.getTaskid(), 200, "缴费信息采集中！");
				taskMobile.setPayMsgStatus(200);
				taskMobileRepository.save(taskMobile);
			} else {
				// 更新task表
				taskMobileRepository.updatePayMsgStatus(taskMobile.getTaskid(), 404, "缴费信息采集中！");
				taskMobile.setPayMsgStatus(404);
				taskMobileRepository.save(taskMobile);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// 更新task表
			taskMobileRepository.updatePayMsgStatus(taskMobile.getTaskid(), 404, "缴费信息采集中！");
			taskMobile.setPayMsgStatus(404);
			taskMobileRepository.save(taskMobile);
		}
	}

	// 发送验证码方法
	public TaskMobile sendSms(MessageLogin messageLogin) {
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {

			String phone = messageLogin.getName();
			String sendurl2 = "http://nm.189.cn/selfservice/service/userLogin";
			String sendResquestPayload2 = "{\"number\":\"" + phone
					+ "\",\"intLoginType\":\"4\",\"areaCode\":\"0471\",\"isBusinessCustType\":\"N\",\"identifyType\":\"B\",\"userLoginType\":\"4\",\"password\":\"\",\"randomPass\":\"\",\"noCheck\":\"N\",\"isSSOLogin\":\"Y\",\"sRand\":\"SSOLogin\"}";
			WebRequest sendResquest2 = new WebRequest(new URL(sendurl2), HttpMethod.POST);
			sendResquest2.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
			sendResquest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			sendResquest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			sendResquest2.setAdditionalHeader("Connection", "keep-alive");
			sendResquest2.setAdditionalHeader("Content-Type", "application/json");
			sendResquest2.setAdditionalHeader("Host", "nm.189.cn");
			sendResquest2.setAdditionalHeader("Origin", "http://nm.189.cn");
			sendResquest2.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36");
			sendResquest2.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			sendResquest2.setRequestBody(sendResquestPayload2);
			Page sendPage2 = webClient.getPage(sendResquest2);
			String sendString2 = sendPage2.getWebResponse().getContentAsString();
			System.out.println(sendString2);

			String sendurl = "http://nm.189.cn/selfservice/bill/xdQuerySMS";
			String sendResquestPayload = "{\"phone\":\"" + phone + "\"}";
			WebRequest sendResquest = new WebRequest(new URL(sendurl), HttpMethod.POST);
			sendResquest.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
			sendResquest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			sendResquest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			sendResquest.setAdditionalHeader("Connection", "keep-alive");
			sendResquest.setAdditionalHeader("Content-Type", "application/json");
			sendResquest.setAdditionalHeader("Host", "nm.189.cn");
			sendResquest.setAdditionalHeader("Origin", "http://nm.189.cn");
			sendResquest.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
			sendResquest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			sendResquest.setRequestBody(sendResquestPayload);
			Page sendPage = webClient.getPage(sendResquest);
			String sendString = sendPage.getWebResponse().getContentAsString();
			System.out.println(sendString);

			// 用户基本信息的爬取
			TelecomNeimengguhtml html = new TelecomNeimengguhtml();
			html.setHtml(sendString);
			html.setPageCount(1);
			html.setTaskid(messageLogin.getTask_id());
			html.setType("发送验证码");
			html.setUrl(sendurl);
			telecomHuhehaotehtmlRepository.save(html);

			JSONObject jsonObj = JSONObject.fromObject(sendString);
			String jsonObj2 = jsonObj + "";
			if ("用户未登录".equals(jsonObj2)) {
				System.out.println("验证码发送失败！");
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getError_code());
				// 保存状态
				save(taskMobile);
			} else {
				String yesOrno = jsonObj.getString("flag");
				if ("0".equals(yesOrno)) {
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
			// 保存发送验证码之后的cookies
			String cookieString = CommonUnit.transcookieToJson(webClient);
			taskMobile.setCookies(cookieString);
			// 登录成功状态更新
			save(taskMobile);

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
			String yzm = messageLogin.getSms_code();
			String checkurl = "http://nm.189.cn/selfservice/bill/xdQuerySMSCheck";
			String checkResquestPayload = "{\"code\":\"" + yzm + "\"}";
			WebRequest checkResquest = new WebRequest(new URL(checkurl), HttpMethod.POST);
			checkResquest.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
			checkResquest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			checkResquest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			checkResquest.setAdditionalHeader("Connection", "keep-alive");
			checkResquest.setAdditionalHeader("Content-Type", "application/json");
			checkResquest.setAdditionalHeader("Host", "nm.189.cn");
			checkResquest.setAdditionalHeader("Origin", "http://nm.189.cn");
			checkResquest.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
			checkResquest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			checkResquest.setRequestBody(checkResquestPayload);
			Page checkPage = webClient.getPage(checkResquest);
			String checkString = checkPage.getWebResponse().getContentAsString();
			System.out.println(checkString);

			// 用户基本信息的爬取
			TelecomNeimengguhtml html = new TelecomNeimengguhtml();
			html.setHtml(checkString);
			html.setPageCount(1);
			html.setTaskid(messageLogin.getTask_id());
			html.setType("验证验证码");
			html.setUrl(checkurl);
			telecomHuhehaotehtmlRepository.save(html);

			JSONObject checkjsonObj = JSONObject.fromObject(checkString);
			// 不发直接验证短信验证码的情况
			if (checkString.contains("用户未登录")) {

				System.out.println("验证码验证失败！");
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getError_code());
				// 保存状态
				save(taskMobile);
			}

			String checkyesOrno = checkjsonObj.getString("flag");
			if ("1".equals(checkyesOrno)) {
				System.out.println("验证码验证成功！");
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
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

	// 语音信息的爬取和解析
	public void crawlerCall(MessageLogin messageLogin, String year) {
		tracer.addTag("service.crawlerCall.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			// 获取账户信息请求的入参
			String packurl1 = "http://nm.189.cn/selfservice/cust/queryAllProductInfo";
			String requestPayloadSend1 = "{\"qryAccNbrType\":\"\"}";
			WebRequest webRequestpack1 = new WebRequest(new URL(packurl1), HttpMethod.POST);
			webRequestpack1.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
			webRequestpack1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequestpack1.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequestpack1.setAdditionalHeader("Connection", "keep-alive");
			webRequestpack1.setAdditionalHeader("Content-Type", "application/json");
			webRequestpack1.setAdditionalHeader("Host", "nm.189.cn");
			webRequestpack1.setAdditionalHeader("Origin", "http://nm.189.cn");
			webRequestpack1.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
			webRequestpack1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequestpack1.setRequestBody(requestPayloadSend1);
			Page page = webClient.getPage(webRequestpack1);
			// [{"accNbr":"18947109133","areaCode":"0471","prodSpecId":"378","prodSpecName":"??","acctNbr":"20000000001824039","productType":"4"}]
			String check = page.getWebResponse().getContentAsString();
			JSONArray firstStep = JSONArray.fromObject(check);
			String accNbr = firstStep.getJSONObject(0).getString("accNbr");
			String accNbrType = firstStep.getJSONObject(0).getString("productType");
			String areaCode = firstStep.getJSONObject(0).getString("areaCode");
			String prodSpecId = firstStep.getJSONObject(0).getString("prodSpecId");

			String yuyinurl = "http://nm.189.cn/selfservice/bill/xdQuery";

			String requestPayloadyuyin = "{\"accNbr\":\"" + accNbr + "\",\"billingCycle\":\"" + year.trim()
					+ "\",\"pageRecords\":1000,\"pageNo\":1,\"qtype\":\"0\",\"prodSpecId\":\"" + prodSpecId
					+ "\",\"accNbrType\":\"" + accNbrType + "\",\"areaCode\":\"" + areaCode + "\",\"smsCode\":\""
					+ messageLogin.getSms_code() + "\"}";

			WebRequest webRequestyuyin = new WebRequest(new URL(yuyinurl), HttpMethod.POST);
			webRequestyuyin.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
			webRequestyuyin.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequestyuyin.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequestyuyin.setAdditionalHeader("Connection", "keep-alive");
			webRequestyuyin.setAdditionalHeader("Content-Type", "application/json");
			webRequestyuyin.setAdditionalHeader("Host", "nm.189.cn");
			webRequestyuyin.setAdditionalHeader("Origin", "http://nm.189.cn");
			webRequestyuyin.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
			webRequestyuyin.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequestyuyin.setRequestBody(requestPayloadyuyin);
			Page pageyuyin = webClient.getPage(webRequestyuyin);
			// 语音信息
			String yuyininfo = pageyuyin.getWebResponse().getContentAsString();
			System.out.println("+语音信息-----+" + yuyininfo);

			// 语音信息的爬取
			TelecomNeimengguhtml html2 = new TelecomNeimengguhtml();
			html2.setHtml(yuyininfo);
			html2.setPageCount(1);
			html2.setTaskid(messageLogin.getTask_id());
			html2.setType("语音信息");
			html2.setUrl(yuyinurl);
			telecomHuhehaotehtmlRepository.save(html2);

			JSONObject yuyin = JSONObject.fromObject(yuyininfo);
			String resultCode = yuyin.getString("resultCode");
			System.out.println(resultCode);
			if ("POR-0000".equals(resultCode)) {
				System.out.println("语音数据获取数据成功！");
				String items = yuyin.getString("items");
				JSONArray itemsyuyin = JSONArray.fromObject(items);
				for (int j = 0; j < itemsyuyin.size(); j++) {
					// 呼叫类型
					String calltype = itemsyuyin.getJSONObject(j).getString("callType");
					// 对方号码
					String oppositephone = itemsyuyin.getJSONObject(j).getString("callingNbr");
					// 通话地点
					String communicateaddr = itemsyuyin.getJSONObject(j).getString("converseAddr");
					// 通话时间
					String communicatetime2 = itemsyuyin.getJSONObject(j).getString("converseDate")
							+ itemsyuyin.getJSONObject(j).getString("converseTime");
					// 通话时长
					String communicatetime = itemsyuyin.getJSONObject(j).getString("converseDuration");
					// 漫游类型
					String correspondencetype = itemsyuyin.getJSONObject(j).getString("converseType");
					// 费用合计
					String costtotal = itemsyuyin.getJSONObject(j).getString("fee");
					TelecomNeimengguCallHistory telecomHuhehaoteCallHistory = new TelecomNeimengguCallHistory();
					telecomHuhehaoteCallHistory.setTaskid(messageLogin.getTask_id().trim());
					telecomHuhehaoteCallHistory.setCalltype(calltype);
					telecomHuhehaoteCallHistory.setOppositephone(oppositephone);
					telecomHuhehaoteCallHistory.setCommunicateaddr(communicateaddr);
					telecomHuhehaoteCallHistory.setCommunicatetime2(communicatetime2);
					telecomHuhehaoteCallHistory.setCommunicatetime(communicatetime);
					telecomHuhehaoteCallHistory.setCorrespondencetype(correspondencetype);
					telecomHuhehaoteCallHistory.setCosttotal(costtotal);
					telecomHuhehaoteCallHistoryRepository.save(telecomHuhehaoteCallHistory);
				}

				// 更新task表
				taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 200, "语音信息采集中！");
				taskMobile.setCallRecordStatus(200);
				taskMobileRepository.save(taskMobile);
			} else {

				if (yuyininfo.contains("无查询结果")) {
					System.out.println("语音数据获取成功！没有数据！");
					// 更新task表
					taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 201, "语音信息采集中！");
					taskMobile.setCallRecordStatus(201);
					taskMobileRepository.save(taskMobile);
				} else {
					System.out.println("语音数据获取数据失败！");
					// 更新task表
					taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 404, "语音信息采集中！");
					taskMobile.setCallRecordStatus(404);
					taskMobileRepository.save(taskMobile);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("语音数据获取数据失败！");
			// 更新task表
			taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 404, "语音信息采集中！");
			taskMobile.setCallRecordStatus(404);
			taskMobileRepository.save(taskMobile);
		}
	}

	// 短信信息爬取和采集
	public void crawlerMessage(MessageLogin messageLogin, String year) {
		tracer.addTag("service.crawlerMessage.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			// 获取账户信息请求的入参
			String packurl1 = "http://nm.189.cn/selfservice/cust/queryAllProductInfo";
			String requestPayloadSend1 = "{\"qryAccNbrType\":\"\"}";
			WebRequest webRequestpack1 = new WebRequest(new URL(packurl1), HttpMethod.POST);
			webRequestpack1.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
			webRequestpack1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequestpack1.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequestpack1.setAdditionalHeader("Connection", "keep-alive");
			webRequestpack1.setAdditionalHeader("Content-Type", "application/json");
			webRequestpack1.setAdditionalHeader("Host", "nm.189.cn");
			webRequestpack1.setAdditionalHeader("Origin", "http://nm.189.cn");
			webRequestpack1.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
			webRequestpack1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequestpack1.setRequestBody(requestPayloadSend1);
			Page page = webClient.getPage(webRequestpack1);
			// [{"accNbr":"18947109133","areaCode":"0471","prodSpecId":"378","prodSpecName":"??","acctNbr":"20000000001824039","productType":"4"}]
			String check = page.getWebResponse().getContentAsString();
			JSONArray firstStep = JSONArray.fromObject(check);
			String accNbr = firstStep.getJSONObject(0).getString("accNbr");
			String accNbrType = firstStep.getJSONObject(0).getString("productType");
			String areaCode = firstStep.getJSONObject(0).getString("areaCode");
			String prodSpecId = firstStep.getJSONObject(0).getString("prodSpecId");

			String yuyinurl = "http://nm.189.cn/selfservice/bill/xdQuery";

			String requestPayloadyuyin = "{\"accNbr\":\"" + accNbr + "\",\"billingCycle\":\"" + year.trim()
					+ "\",\"pageRecords\":100000,\"pageNo\":1,\"qtype\":\"1\",\"prodSpecId\":\"" + prodSpecId
					+ "\",\"accNbrType\":\"" + accNbrType + "\",\"areaCode\":\"" + areaCode + "\",\"smsCode\":\""
					+ messageLogin.getSms_code() + "\"}";

			WebRequest webRequestyuyin = new WebRequest(new URL(yuyinurl), HttpMethod.POST);
			webRequestyuyin.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
			webRequestyuyin.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequestyuyin.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequestyuyin.setAdditionalHeader("Connection", "keep-alive");
			webRequestyuyin.setAdditionalHeader("Content-Type", "application/json");
			webRequestyuyin.setAdditionalHeader("Host", "nm.189.cn");
			webRequestyuyin.setAdditionalHeader("Origin", "http://nm.189.cn");
			webRequestyuyin.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
			webRequestyuyin.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequestyuyin.setRequestBody(requestPayloadyuyin);
			Page pageyuyin = webClient.getPage(webRequestyuyin);
			// 短信信息
			String yuyininfo = pageyuyin.getWebResponse().getContentAsString();
			System.out.println("短信信息-----" + yuyininfo);

			// 短信信息的爬取
			TelecomNeimengguhtml html2 = new TelecomNeimengguhtml();
			html2.setHtml(yuyininfo);
			html2.setPageCount(1);
			html2.setTaskid(messageLogin.getTask_id());
			html2.setType("短信信息");
			html2.setUrl(yuyinurl);
			telecomHuhehaotehtmlRepository.save(html2);

			JSONObject yuyin = JSONObject.fromObject(yuyininfo);
			String resultCode = yuyin.getString("resultCode");
			System.out.println(resultCode);
			if ("POR-0000".equals(resultCode)) {
				System.out.println("短信数据获取数据成功！");
				String items = yuyin.getString("items");
				JSONArray itemsyuyin = JSONArray.fromObject(items);
				for (int j = 0; j < itemsyuyin.size(); j++) {

					// 发送时间
					String sendtime = itemsyuyin.getJSONObject(j).getString("sendDate")
							+ itemsyuyin.getJSONObject(j).getString("sendTime");
					// 对方号码
					String oppositephone = itemsyuyin.getJSONObject(j).getString("callingNbr");
					// 收发类型
					String messagetype = itemsyuyin.getJSONObject(j).getString("rcvSendType");
					// 业务类型
					String bustype = itemsyuyin.getJSONObject(j).getString("bussType");
					// 费用
					String messageaddr = itemsyuyin.getJSONObject(j).getString("sendFee");

					TelecomNeimengguMessageHistory telecomHuhehaoteMessageHistory = new TelecomNeimengguMessageHistory();
					telecomHuhehaoteMessageHistory.setTaskid(messageLogin.getTask_id().trim());
					telecomHuhehaoteMessageHistory.setSendtime(sendtime);
					telecomHuhehaoteMessageHistory.setOppositephone(oppositephone);
					telecomHuhehaoteMessageHistory.setMessagetype(messagetype);
					telecomHuhehaoteMessageHistory.setBustype(bustype);
					telecomHuhehaoteMessageHistory.setMessageaddr(messageaddr);
					telecomHuhehaoteMessageHistoryRepository.save(telecomHuhehaoteMessageHistory);

				}
				taskMobile.setDescription("短信信息采集中！");
				taskMobile.setSmsRecordStatus(200);
				taskMobileRepository.save(taskMobile);
			} else {

				if (yuyininfo.contains("无查询结果")) {
					System.out.println("短信数据获取数据成功！没有数据！");
					taskMobile.setDescription("短信信息采集中！");
					taskMobile.setSmsRecordStatus(201);
					taskMobileRepository.save(taskMobile);
				} else {
					System.out.println("短信数据获取数据失败！");
					taskMobile.setDescription("短信信息采集中！");
					taskMobile.setSmsRecordStatus(404);
					taskMobileRepository.save(taskMobile);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("短信数据获取数据失败！");
			taskMobile.setSmsRecordStatus(404);
			taskMobile.setDescription("短信信息采集中！");
			taskMobileRepository.save(taskMobile);
		}
		// 更新最终的状态
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());

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