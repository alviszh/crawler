package app.service;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

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
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.fujian.TelecomFujianCallHistory;
import com.microservice.dao.entity.crawler.telecom.fujian.TelecomFujianMessageHistory;
import com.microservice.dao.entity.crawler.telecom.fujian.TelecomFujianMonthBillHistory;
import com.microservice.dao.entity.crawler.telecom.fujian.TelecomFujianPayMsg;
import com.microservice.dao.entity.crawler.telecom.fujian.TelecomFujianTaocanMsg;
import com.microservice.dao.entity.crawler.telecom.fujian.TelecomFujianUserInfo;
import com.microservice.dao.entity.crawler.telecom.fujian.TelecomFujianhtml;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.fujian.TelecomFujianCallHistoryRepository;
import com.microservice.dao.repository.crawler.telecom.fujian.TelecomFujianMessageHistoryRepository;
import com.microservice.dao.repository.crawler.telecom.fujian.TelecomFujianMonthBillHistoryRepository;
import com.microservice.dao.repository.crawler.telecom.fujian.TelecomFujianPayMsgRepository;
import com.microservice.dao.repository.crawler.telecom.fujian.TelecomFujianTaocanMsgRepository;
import com.microservice.dao.repository.crawler.telecom.fujian.TelecomFujianUserInfoRepository;
import com.microservice.dao.repository.crawler.telecom.fujian.TelecomFujianhtmlRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.ISms;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.fujian")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.fujian")
public class TelecomCommon implements ISms {
	public static final Logger log = LoggerFactory.getLogger(TelecomCommon.class);
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomFujianUserInfoRepository telecomFujianUserInfoRepository;
	@Autowired
	private TelecomFujianhtmlRepository telecomFujianhtmlRepository;
	@Autowired
	private TelecomFujianMonthBillHistoryRepository telecomFujianMonthBillHistoryRepository;
	@Autowired
	private TelecomFujianTaocanMsgRepository telecomFujianTaocanMsgRepository;
	@Autowired
	private TelecomFujianPayMsgRepository telecomFujianPayMsgRepository;
	@Autowired
	private TelecomFujianCallHistoryRepository telecomFujianCallHistoryRepository;
	@Autowired
	private TelecomFujianMessageHistoryRepository telecomFujianMessageHistoryRepository;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private TracerLog tracer;

	public HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
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

	public void crawlerWdyue(MessageLogin messageLogin) {
		// 更新task表
		taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(), 200, "月账单信息采集中！");
		tracer.addTag("service.crawlerWdyue.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());
		System.out.println("2222222"+taskMobile.toString());
		
		taskMobile.setAccountMsgStatus(200);
		taskMobileRepository.save(taskMobile);
		System.out.println("2222222"+taskMobile.toString());
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			String wdzlurl1 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01420648&cityCode=fj";
			WebRequest webRequestwdzl1;
			webRequestwdzl1 = new WebRequest(new URL(wdzlurl1), HttpMethod.GET);
			HtmlPage wdzl1 = webClient.getPage(webRequestwdzl1);
			webClient = wdzl1.getWebClient();

			String wdzlurl = "http://fj.189.cn/service/bill/realtime_new.jsp?URLPATH=/service/bill/realtime.jsp&fastcode=01420648&cityCode=fj";
			WebRequest webRequestwdzl;
			webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.GET);
			Page wdzl = webClient.getPage(webRequestwdzl);
			String base = wdzl.getWebResponse().getContentAsString();
			String commonremaining = "";

			String phone = messageLogin.getName();

			if (base.contains("客户名称")) {
				Document doc = Jsoup.parse(base);
				Elements tab_common_2 = doc.getElementsByClass("tab_common_2");
				Element element = tab_common_2.get(0);
				Elements orange = element.getElementsByClass("orange");
				Element element2 = orange.get(0);
				commonremaining = element2.text();

				System.out.println("余额-----"+commonremaining);
				
				// 获得数据进行处理
				TelecomFujianhtml html = new TelecomFujianhtml();
				html.setHtml(base);
				html.setPageCount(1);
				html.setTaskid(messageLogin.getTask_id());
				html.setType("基本信息-余额和电话号码");
				html.setUrl(wdzlurl);
				telecomFujianhtmlRepository.save(html);

				TelecomFujianUserInfo findByTaskid = telecomFujianUserInfoRepository
						.findByTaskid(messageLogin.getTask_id());
				telecomFujianUserInfoRepository.updatewdyue(commonremaining, phone, findByTaskid.getTaskid());

				// 更新task表
				taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 200, "基本信息-我的余额和手机号码采集中！");
				System.out.println("1111111111111"+taskMobile.toString());
				
			} else {
				System.out.println("获取用户余额失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		System.out.println("333333"+taskMobile.toString());
		
	}

	public void crawlerMonthBillHistory(MessageLogin messageLogin, int year, int i) {
		tracer.addTag("service.crawlerMonthBillHistory.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {

			String wdzlurl1 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01420648&cityCode=fj";
			WebRequest webRequestwdzl1;
			webRequestwdzl1 = new WebRequest(new URL(wdzlurl1), HttpMethod.GET);
			HtmlPage wdzl1 = webClient.getPage(webRequestwdzl1);
			webClient = wdzl1.getWebClient();

			// 电话号码
			String phone = messageLogin.getName().trim();
			// 月份
			String month = year + "".trim();

			String wdzlurl = "http://fj.189.cn/service/bill/custbill.jsp?ck_month=" + month + "&citycode=595&PRODNO="
					+ phone + "&PRODTYPE=50";
			WebRequest webRequestwdzl;
			webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.POST);
			HtmlPage wdzl = webClient.getPage(webRequestwdzl);
			String base = wdzl.getWebResponse().getContentAsString();
			// 获得数据进行处理
			TelecomFujianhtml html2 = new TelecomFujianhtml();
			html2.setHtml(base);
			html2.setPageCount(1);
			html2.setTaskid(messageLogin.getTask_id());
			html2.setType("月账单信息");
			html2.setUrl(wdzlurl);
			telecomFujianhtmlRepository.save(html2);

			Document doc = Jsoup.parse(base);

			String docString = doc + "";
			// 当月合计
			String monthall = null;
			if (docString.contains("本期费用合计")) {
				String[] split = docString.split("本期费用合计");
				String[] split2 = split[1].split("元");
				monthall = split2[0];
			}
			// 月使用费
			String monthfee = null;
			if (docString.contains("套餐月基本费")) {
				String[] split = docString.split("套餐月基本费");
				String[] split2 = split[0].split("<span>");
				String a = split2[split2.length - 1];
				String[] split3 = a.split("</span>");
				monthfee = split3[0];
			}
			// 上网费
			String interfee = null;
			if (docString.contains("宽带使用费")) {
				String[] split = docString.split("宽带使用费");
				String[] split2 = split[0].split("<span>");
				String a = split2[split2.length - 1];
				String[] split3 = a.split("</span>");
				interfee = split3[0];
			}

			System.out.println(monthfee);
			System.out.println(interfee);
			System.out.println(monthall);
			TelecomFujianMonthBillHistory telecomFujianMonthBillHistory = new TelecomFujianMonthBillHistory();
			telecomFujianMonthBillHistory.setTaskid(messageLogin.getTask_id().trim());
			telecomFujianMonthBillHistory.setMonth(month);
			telecomFujianMonthBillHistory.setMonthfee(monthfee);
			telecomFujianMonthBillHistory.setInterfee(interfee);
			telecomFujianMonthBillHistory.setMonthall(monthall);
			telecomFujianMonthBillHistoryRepository.save(telecomFujianMonthBillHistory);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 发送验证码方法
	public TaskMobile sendSms(MessageLogin messageLogin) {
		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id().trim());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		// 基本信息-我的积分
		crawlerWdjifen(messageLogin);

		// 业务信息-套餐业务信息
		crawlerWdbuss(messageLogin);

		// 缴费信息
		SimpleDateFormat dfjiaofei = new SimpleDateFormat("yyyyMM");// 设置日期格式
		int endmonth = Integer.parseInt(dfjiaofei.format(new Date()));
		int firstmonth = endmonth - 5;
		crawlerPaymsg(messageLogin, firstmonth, endmonth);

		try {
			String wdzlurl1 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01420648&cityCode=fj";
			WebRequest webRequestwdzl1;
			webRequestwdzl1 = new WebRequest(new URL(wdzlurl1), HttpMethod.GET);
			HtmlPage wdzl1 = webClient.getPage(webRequestwdzl1);
			webClient = wdzl1.getWebClient();

			// 电话号码
			String phone = messageLogin.getName().trim();

			// 发送短信验证码请求
			String wdzlurl4 = "http://fj.189.cn/BUFFALO/buffalo/QueryAllAjax";
			WebRequest webRequestwdzl4;
			String requestbody = "<buffalo-call><method>getCDMASmsCode</method><map><type>java.util.HashMap</type><string>PHONENUM</string><string>"
					+ phone
					+ "</string><string>PRODUCTID</string><string>50</string><string>CITYCODE</string><string>0591</string><string>I_ISLIMIT</string><string>1</string><string>QUERYTYPE</string><string>BILL</string></map></buffalo-call>";
			webRequestwdzl4 = new WebRequest(new URL(wdzlurl4), HttpMethod.POST);
			webRequestwdzl4.setAdditionalHeader("Accept", "*/*");
			webRequestwdzl4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequestwdzl4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequestwdzl4.setAdditionalHeader("Connection", "keep-alive");
			webRequestwdzl4.setAdditionalHeader("Content-Type", "text/xml;charset=UTF-8");
			webRequestwdzl4.setAdditionalHeader("Host", "fj.189.cn");
			webRequestwdzl4.setAdditionalHeader("Origin", "http://fj.189.cn");
			webRequestwdzl4.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36");
			webRequestwdzl4.setAdditionalHeader("X-Buffalo-Version", "2.0");
			webRequestwdzl4.setRequestBody(requestbody);
			Page wdzl4 = webClient.getPage(webRequestwdzl4);
			String contentAsString = wdzl4.getWebResponse().getContentAsString();

			// 获得数据进行处理
			TelecomFujianhtml html = new TelecomFujianhtml();
			html.setHtml(contentAsString);
			html.setPageCount(1);
			html.setTaskid(messageLogin.getTask_id());
			html.setType("发送验证码信息");
			html.setUrl(wdzlurl4);
			telecomFujianhtmlRepository.save(html);

			if (contentAsString.contains("短信随机密码已经发到您的手机,请查收")) {

				System.out.println("验证码发送成功！");
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getError_code());
				// 保存发送验证码之后的cookies
				String cookieString = CommonUnit.transcookieToJson(webClient);
				taskMobile.setCookies(cookieString);
				// 登录成功状态更新
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

		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskMobile;
	}

	// 验证验证码方法
	public TaskMobile verifySms(MessageLogin messageLogin) {
		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id().trim());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			// 手机号
			String phone = messageLogin.getName().trim();
			// 月份
			SimpleDateFormat df = new SimpleDateFormat("yyyyMM");// 设置日期格式
			String format = df.format(new Date());

			// 获得验证验证码的入参
			/*
			 * 0591：福州 0592：厦门 0595：泉州
			 */
			String url = "http://fj.189.cn/service/bill/tanChu.jsp?PRODNO=" + phone
					+ "&PRODTYPE=50&CITYCODE=0591&MONTH=" + format + "&SELTYPE=1";
			WebRequest webRequestq;
			webRequestq = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage wdzlyuyinq = webClient.getPage(webRequestq);
			String contentAsString21 = wdzlyuyinq.getWebResponse().getContentAsString();

			Document doc = Jsoup.parse(contentAsString21);
			// 50
			String prodtype = doc.getElementById("PRODTYPE").val().trim();
			System.out.println(prodtype);
			// 0591
			String citycode = doc.getElementById("CITYCODE").val().trim();
			System.out.println(citycode);
			// 1
			String seltype = doc.getElementById("SELTYPE").val().trim();
			System.out.println(seltype);
			// 0
			String purid = doc.getElementById("PURID").val().trim();
			System.out.println(purid);
			// 10001
			String empoent = doc.getElementById("email_empoent").val().trim();
			System.out.println(empoent);
			// 863581c5892cdfe8a67b95c7abb47ead8b102e9620994ae95637f637fa22acac173b91015574507362816b30a884632d8562bf20de621d31d745291aaec7ca6f
			String module = doc.getElementById("email_module").val().trim();
			System.out.println(module);

			// 密码
			String password = encryptedPhone(messageLogin.getPassword().trim()).trim();
			System.out.println(password);
			// 验证码
			String code = messageLogin.getSms_code().trim();
			System.out.println(code);

			String wdzlurlyuyin = "http://fj.189.cn/service/bill/trans.jsp?PRODNO=" + phone + "&PRODTYPE=" + prodtype
					+ "&CITYCODE=" + citycode + "&SELTYPE=" + seltype + "&MONTH=" + format + "&PURID=" + purid
					+ "&email_empoent=" + empoent + "&email_module=" + module + "&serPwd50=" + password + "&randomPwd="
					+ code;

			WebRequest webRequestwdzlyuyin;
			webRequestwdzlyuyin = new WebRequest(new URL(wdzlurlyuyin), HttpMethod.POST);
			HtmlPage wdzlyuyin = webClient.getPage(webRequestwdzlyuyin);
			String contentAsString2 = wdzlyuyin.getWebResponse().getContentAsString();
			System.out.println(contentAsString2);

			// 获得数据进行处理
			TelecomFujianhtml html = new TelecomFujianhtml();
			html.setHtml(contentAsString2);
			html.setPageCount(1);
			html.setTaskid(messageLogin.getTask_id());
			html.setType("验证验证码信息");
			html.setUrl(wdzlurlyuyin);
			telecomFujianhtmlRepository.save(html);
			if (contentAsString2.contains("您的短信随机码不正确或已失效")) {
				System.out.println("验证码验证失败！");
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getError_code());
				// 保存状态
				save(taskMobile);
			} else {
				Document doc22 = Jsoup.parse(contentAsString2);
				Elements tablelist = doc22.select("table");
				if (tablelist.size() == 0) {
					System.out.println("当月的-----获取语音信息失败！");
				} else {
					System.out.println("当月的-----获取语音信息成功！");
					Elements tr = tablelist.get(1).select("tr");
					for (int i = 1; i < tr.size(); i++) {
						Elements select = tr.get(i).select("td");
						for (int j = 0; j < select.size(); j += 7) {
							// 通话起始时间
							String communicatetime2 = select.get(j + 1).html();
							// 通话时长
							String communicatetime = select.get(j + 2).html();
							// 呼叫类型
							String calltype = select.get(j + 3).html();
							// 通信地点
							String communicateaddr = select.get(j + 4).html();
							// 对方号码
							String oppositephone = select.get(j + 5).html();
							// 费用（元）
							String costtotal = select.get(j + 6).html();
							TelecomFujianCallHistory telecomFujianCallHistory = new TelecomFujianCallHistory();
							telecomFujianCallHistory.setTaskid(messageLogin.getTask_id().trim());
							telecomFujianCallHistory.setCommunicateaddr(communicateaddr);
							telecomFujianCallHistory.setCommunicatetime(communicatetime);
							telecomFujianCallHistory.setCommunicatetime2(communicatetime2);
							telecomFujianCallHistory.setCalltype(calltype);
							telecomFujianCallHistory.setOppositephone(oppositephone);
							telecomFujianCallHistory.setCosttotal(costtotal);
							telecomFujianCallHistoryRepository.save(telecomFujianCallHistory);
						}
					}

					// 更新task表
					taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 200, "语音信息采集中！");

				}

				// 语音信息
				SimpleDateFormat df22 = new SimpleDateFormat("yyyyMM");// 设置日期格式
				for (int k = 1; k <= 5; k++) {
					int year = Integer.parseInt(df22.format(new Date())) - k;
					// 语音信息
					crawlerCall(messageLogin, year);
				}

				// 短信信息
				SimpleDateFormat df2 = new SimpleDateFormat("yyyyMM");// 设置日期格式
				for (int f = 0; f <= 5; f++) {
					int year = Integer.parseInt(df2.format(new Date())) - f;
					// 短信信息
					crawlerMessage(messageLogin, year);
				}

				System.out.println("验证码验证成功！");
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getError_code());
				// 保存状态
				save(taskMobile);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskMobile;
	}

	public void crawlerPaymsg(MessageLogin messageLogin, int firstmonth, int endmonth) {
		tracer.addTag("service.crawlerPaymsg.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			String wdzlurl1 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01420648&cityCode=fj";
			WebRequest webRequestwdzl1;
			webRequestwdzl1 = new WebRequest(new URL(wdzlurl1), HttpMethod.GET);
			HtmlPage wdzl1 = webClient.getPage(webRequestwdzl1);
			webClient = wdzl1.getWebClient();

			String phone = messageLogin.getName().trim();
			String reqfirstmonth = firstmonth + "".trim();
			String reqendmonth = endmonth + "".trim();

			String wdzlurl = "http://fj.189.cn/service/pay/query/payAllQuery_Result.jsp?isRequest=yes&PRODTYPE=50&PRODNO="
					+ phone + "&STARTTIME=" + reqfirstmonth + "&STARTH=" + reqfirstmonth + "&OVERTIME=" + reqendmonth
					+ "&OVERH=" + reqendmonth;
			WebRequest webRequestwdzl;
			webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.POST);
			HtmlPage wdzl = webClient.getPage(webRequestwdzl);
			String base = wdzl.getWebResponse().getContentAsString();

			Document doc = Jsoup.parse(base);

			Elements elementsByClass = doc.getElementsByClass("listtable");
			if (elementsByClass.size() == 0) {

				System.out.println("第一次没有查到缴费信息！再试一遍");

				WebClient webClient22 = WebCrawler.getInstance().getNewWebClient();
				String cook22 = taskMobile.getCookies();
				Set<Cookie> cookies22 = CommonUnit.transferJsonToSet(cook22);
				for (Cookie cookie22 : cookies22) {
					webClient22.getCookieManager().addCookie(cookie22);
				}

				String wdzlurl2 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01420648&cityCode=fj";
				WebRequest webRequestwdzl2;
				webRequestwdzl2 = new WebRequest(new URL(wdzlurl2), HttpMethod.GET);
				HtmlPage wdzl2 = webClient22.getPage(webRequestwdzl2);
				webClient22 = wdzl2.getWebClient();

				String phone2 = messageLogin.getName().trim();
				String reqfirstmonth2 = firstmonth + "".trim();
				String reqendmonth2 = endmonth + "".trim();

				String wdzlurl3 = "http://fj.189.cn/service/pay/query/payAllQuery_Result.jsp?isRequest=yes&PRODTYPE=50&PRODNO="
						+ phone2 + "&STARTTIME=" + reqfirstmonth2 + "&STARTH=" + reqfirstmonth2 + "&OVERTIME="
						+ reqendmonth2 + "&OVERH=" + reqendmonth2;
				WebRequest webRequestwdzl3;
				webRequestwdzl3 = new WebRequest(new URL(wdzlurl3), HttpMethod.POST);
				HtmlPage wdzl3 = webClient22.getPage(webRequestwdzl3);
				String base3 = wdzl3.getWebResponse().getContentAsString();

				// 获得数据进行处理

				TelecomFujianhtml html3 = new TelecomFujianhtml();
				html3.setHtml(base3);
				html3.setPageCount(1);
				html3.setTaskid(messageLogin.getTask_id());
				html3.setType("缴费信息");
				html3.setUrl(wdzlurl3);
				telecomFujianhtmlRepository.save(html3);

				Document doc3 = Jsoup.parse(base3);

				Elements elementsByClass3 = doc3.getElementsByClass("listtable");
				if (elementsByClass3.size() == 0) {
					System.out.println("第二次获取---缴费信息---失败！放弃");
					// 更新task表
					taskMobileRepository.updatePayMsgStatus(taskMobile.getTaskid(), 404, "缴费信息采集中！");
				} else {
					System.out.println("第二次---缴费信息---获取成功！");

					Element table = elementsByClass3.get(0);

					Elements element0 = table.select("tr");
					for (int i = 1; i < element0.size(); i++) {
						Elements select = element0.get(i).select("td");
						for (int j = 0; j < select.size(); j += 5) {
							// 交易类型
							String payditch = select.get(j + 1).html();
							// 交易时间
							String paydate = select.get(j + 2).html();
							// 交易方式
							String payway = select.get(j + 3).html();
							// 交易金额
							String paymoney = select.get(j + 4).html();
							System.out.println(payditch);
							System.out.println(paydate);
							System.out.println(payway);
							System.out.println(paymoney);
							TelecomFujianPayMsg telecomFujianPayMsg = new TelecomFujianPayMsg();
							telecomFujianPayMsg.setTaskid(messageLogin.getTask_id().trim());
							telecomFujianPayMsg.setPayditch(payditch);
							telecomFujianPayMsg.setPaydate(paydate);
							telecomFujianPayMsg.setPayway(payway);
							telecomFujianPayMsg.setPaymoney(paymoney);
							telecomFujianPayMsgRepository.save(telecomFujianPayMsg);
						}
					}
					// 更新task表
					taskMobileRepository.updatePayMsgStatus(taskMobile.getTaskid(), 200, "缴费信息采集中！");
				}
			} else {

				// 获得数据进行处理

				TelecomFujianhtml html = new TelecomFujianhtml();
				html.setHtml(base);
				html.setPageCount(1);
				html.setTaskid(messageLogin.getTask_id());
				html.setType("缴费信息");
				html.setUrl(wdzlurl);
				telecomFujianhtmlRepository.save(html);

				System.out.println("第一次---缴费信息---获取成功！");

				Element table = elementsByClass.get(0);

				Elements element0 = table.select("tr");
				for (int i = 1; i < element0.size(); i++) {
					Elements select = element0.get(i).select("td");
					for (int j = 0; j < select.size(); j += 5) {
						// 交易类型
						String payditch = select.get(j + 1).html();
						// 交易时间
						String paydate = select.get(j + 2).html();
						// 交易方式
						String payway = select.get(j + 3).html();
						// 交易金额
						String paymoney = select.get(j + 4).html();
						System.out.println(payditch);
						System.out.println(paydate);
						System.out.println(payway);
						System.out.println(paymoney);
						TelecomFujianPayMsg telecomFujianPayMsg = new TelecomFujianPayMsg();
						telecomFujianPayMsg.setTaskid(messageLogin.getTask_id().trim());
						telecomFujianPayMsg.setPayditch(payditch);
						telecomFujianPayMsg.setPaydate(paydate);
						telecomFujianPayMsg.setPayway(payway);
						telecomFujianPayMsg.setPaymoney(paymoney);
						telecomFujianPayMsgRepository.save(telecomFujianPayMsg);
					}
				}
				// 更新task表
				taskMobileRepository.updatePayMsgStatus(taskMobile.getTaskid(), 200, "缴费信息采集中！");

			}
		} catch (Exception e) {

		}

	}

	public void crawlerWdjifen(MessageLogin messageLogin) {
		tracer.addTag("service.crawlerWdjifen.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {

			String wdzlurl1 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01420648&cityCode=fj";
			WebRequest webRequestwdzl1;
			webRequestwdzl1 = new WebRequest(new URL(wdzlurl1), HttpMethod.GET);
			HtmlPage wdzl1 = webClient.getPage(webRequestwdzl1);
			webClient = wdzl1.getWebClient();

			////////////////////////////////////////////////////// 姓名和用户状态///////////////////////////////////////////////////////
			String wdzlurl2 = "http://fj.189.cn/service/club/queryIntegral.jsp";
			WebRequest webRequestwdzl2;
			webRequestwdzl2 = new WebRequest(new URL(wdzlurl2), HttpMethod.GET);
			webClient.getPage(webRequestwdzl2);
			HtmlPage wdzl2 = webClient.getPage(webRequestwdzl2);
			String base2 = wdzl2.getWebResponse().getContentAsString();

			Document doc2 = Jsoup.parse(base2);
			Elements elementsByClass = doc2.getElementsByClass("points_box");
			if (elementsByClass.size() == 0) {
				System.out.println("用户信息中的姓名和账户状态----未获取到！");
				System.out.println("用户信息中的姓名和账户状态----从新获取！");

				String wdzlurl122 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01420648&cityCode=fj";
				WebRequest webRequestwdzl122;
				webRequestwdzl122 = new WebRequest(new URL(wdzlurl122), HttpMethod.GET);
				HtmlPage wdzl122 = webClient.getPage(webRequestwdzl122);
				webClient = wdzl122.getWebClient();

				////////////////////////////////////////////////////// 姓名和用户状态///////////////////////////////////////////////////////
				String wdzlurl222 = "http://fj.189.cn/service/club/queryIntegral.jsp";
				WebRequest webRequestwdzl222;
				webRequestwdzl222 = new WebRequest(new URL(wdzlurl222), HttpMethod.GET);
				HtmlPage wdzl222 = webClient.getPage(webRequestwdzl222);
				String base222 = wdzl222.getWebResponse().getContentAsString();

				Document doc222 = Jsoup.parse(base222);
				Elements elementsByClass22 = doc222.getElementsByClass("points_box");
				if (elementsByClass22.size() == 0) {
					System.out.println("用户信息中的姓名和账户状态-----第二次-----获取数据失败！");
					taskMobileRepository.updateIntegralMsgStatus(taskMobile.getTaskid(), 404, "基本信息-我的积分采集中！");
				} else {

					Element element = elementsByClass22.get(0);
					String name1 = element.select("p").get(0).text();
					String name = "";
					String accountstatus = "";
					if (name1.contains("当前状态")) {
						String[] split = name1.split("当前状态");
						// 姓名
						name = split[0];
						System.out.println(name);
						// 账户状态
						accountstatus = element.select("span").get(0).html();
						System.out.println(accountstatus);
					} else {
						String[] split = name1.split("：");
						// 姓名
						name = split[1];
						System.out.println("姓名-----"+name);
						// 获得数据进行处理
						TelecomFujianhtml html2 = new TelecomFujianhtml();
						html2.setHtml(base2);
						html2.setPageCount(1);
						html2.setTaskid(messageLogin.getTask_id());
						html2.setType("基本信息-姓名和用户状态");
						html2.setUrl(wdzlurl2);
						telecomFujianhtmlRepository.save(html2);

						// 用户信息实体类
						TelecomFujianUserInfo telecomFujianUserInfo = new TelecomFujianUserInfo();

						telecomFujianUserInfo.setTaskid(taskMobile.getTaskid());
						telecomFujianUserInfo.setUseintegral("");
						telecomFujianUserInfo.setExpireintegral("");
						telecomFujianUserInfo.setName(name);
						telecomFujianUserInfo.setAccountstatus(accountstatus);
						telecomFujianUserInfoRepository.save(telecomFujianUserInfo);

						// 更新task表
						taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 200, "基本信息-我的积分采集中！");
						taskMobileRepository.updateIntegralMsgStatus(taskMobile.getTaskid(), 200, "基本信息-我的积分采集中！");
					}
				}
			} else {
				Element element = elementsByClass.get(0);

				String name1 = element.select("p").get(0).text();
				String name = "";
				String accountstatus = "";
				if (name1.contains("当前状态")) {
					String[] split = name1.split("当前状态");
					// 姓名
					name = split[0];
					System.out.println(name);
					// 账户状态
					accountstatus = element.select("span").get(0).html();
					System.out.println(accountstatus);
				} else {
					String[] split = name1.split("：");
					// 姓名
					name = split[1];
					// 获得数据进行处理
					TelecomFujianhtml html2 = new TelecomFujianhtml();
					html2.setHtml(base2);
					html2.setPageCount(1);
					html2.setTaskid(messageLogin.getTask_id());
					html2.setType("基本信息-姓名和用户状态");
					html2.setUrl(wdzlurl2);
					telecomFujianhtmlRepository.save(html2);

					// 用户信息实体类
					TelecomFujianUserInfo telecomFujianUserInfo = new TelecomFujianUserInfo();

					telecomFujianUserInfo.setTaskid(taskMobile.getTaskid());
					telecomFujianUserInfo.setUseintegral("");
					telecomFujianUserInfo.setExpireintegral("");
					telecomFujianUserInfo.setName(name);
					telecomFujianUserInfo.setAccountstatus(accountstatus);
					telecomFujianUserInfoRepository.save(telecomFujianUserInfo);

					// 更新task表
					taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 200, "基本信息-我的积分采集中！");
					taskMobileRepository.updateIntegralMsgStatus(taskMobile.getTaskid(), 200, "基本信息-我的积分采集中！");
				}
			}
		} catch (Exception e) {
		}
	}

	public void crawlerWdbuss(MessageLogin messageLogin) {
		tracer.addTag("service.crawlerWdbuss.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		try {

			String wdzlurl1 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01420648&cityCode=fj";
			WebRequest webRequestwdzl1;
			webRequestwdzl1 = new WebRequest(new URL(wdzlurl1), HttpMethod.GET);
			HtmlPage wdzl1 = webClient.getPage(webRequestwdzl1);
			webClient = wdzl1.getWebClient();

			// 电话号码/账号
			String phone = messageLogin.getName().trim();

			String wdzlurl = "http://fj.189.cn/service2/actions/Package.action?showPackageAttr=&OBJECTNUM=" + phone
					+ "&OBJECTTYPE=50";
			WebRequest webRequestwdzl;
			webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.POST);
			webClient.getPage(webRequestwdzl);
			Page wdzl = webClient.getPage(webRequestwdzl);
			String base = wdzl.getWebResponse().getContentAsString();

			// 获得数据进行处理
			TelecomFujianhtml html2 = new TelecomFujianhtml();
			html2.setHtml(base);
			html2.setPageCount(1);
			html2.setTaskid(messageLogin.getTask_id());
			html2.setType("业务信息");
			html2.setUrl(wdzlurl);
			telecomFujianhtmlRepository.save(html2);

			System.out.println("套餐数据的格式：" + base);
			if (base.contains("FunInon")) {

				JSONObject json = JSONObject.fromObject(base);
				String FunInon = json.getString("FunInon");
				Document doc = Jsoup.parse(FunInon);
				Elements yhbTd = doc.getElementsByClass("yhbTd");
				Elements yhb = doc.getElementsByClass("yhb");
				for (int i = 0; i < yhbTd.size(); i++) {
					// 套餐名称
					String business_name = yhbTd.get(i).text();

					String time = yhb.get(i).html();
					String[] split = time.split("<br/>");
					// 订购时间
					String order_time = split[0];
					// 生效时间
					String effect_time = split[0];
					// 失效时间
					String lose_time = split[0];

					TelecomFujianTaocanMsg telecomFujianTaocanMsg = new TelecomFujianTaocanMsg();
					telecomFujianTaocanMsg.setTaskid(messageLogin.getTask_id().trim());
					telecomFujianTaocanMsg.setPhone(phone);
					telecomFujianTaocanMsg.setBusinessname(business_name);
					telecomFujianTaocanMsg.setOrdertime(order_time);
					telecomFujianTaocanMsg.setEffecttime(effect_time);
					telecomFujianTaocanMsg.setLosetime(lose_time);
					telecomFujianTaocanMsgRepository.save(telecomFujianTaocanMsg);

					// 更新task表
					taskMobileRepository.updateBusinessMsgStatus(taskMobile.getTaskid(), 200, "业务信息采集中！");

				}

			} else {
				System.out.println("套餐信息---获取数据失败！");
				// 更新task表
				taskMobileRepository.updateBusinessMsgStatus(taskMobile.getTaskid(), 404, "业务信息采集中！");
			}

		} catch (Exception e) {
			// 更新task表
			taskMobileRepository.updateBusinessMsgStatus(taskMobile.getTaskid(), 404, "业务信息采集中！");
			System.out.println("获取套餐数据----获取数据失败!");
			e.printStackTrace();
		}
	}

	public void crawlerCall(MessageLogin messageLogin, int year) {
		tracer.addTag("service.crawlerCall.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id().trim());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			// 手机号
			String phone = messageLogin.getName().trim();
			// 月份
			SimpleDateFormat df = new SimpleDateFormat("yyyyMM");// 设置日期格式
			String format = df.format(new Date());

			// 获得验证验证码的入参
			String url = "http://fj.189.cn/service/bill/tanChu.jsp?PRODNO=" + phone
					+ "&PRODTYPE=50&CITYCODE=0591&MONTH=" + format + "&SELTYPE=1";
			WebRequest webRequestq;
			webRequestq = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage wdzlyuyinq = webClient.getPage(webRequestq);
			String contentAsString21 = wdzlyuyinq.getWebResponse().getContentAsString();
			Document doc1 = Jsoup.parse(contentAsString21);
			// 50
			String prodtype = doc1.getElementById("PRODTYPE").val().trim();
			System.out.println(prodtype);
			// 0591
			String citycode = doc1.getElementById("CITYCODE").val().trim();
			System.out.println(citycode);
			// 1
			String seltype = doc1.getElementById("SELTYPE").val().trim();
			System.out.println(seltype);
			// 0
			String purid = doc1.getElementById("PURID").val().trim();
			System.out.println(purid);
			// 10001
			String empoent = doc1.getElementById("email_empoent").val().trim();
			System.out.println(empoent);
			// 863581c5892cdfe8a67b95c7abb47ead8b102e9620994ae95637f637fa22acac173b91015574507362816b30a884632d8562bf20de621d31d745291aaec7ca6f
			String module = doc1.getElementById("email_module").val().trim();
			System.out.println(module);
			// 密码
			String password = encryptedPhone(messageLogin.getPassword().trim()).trim();
			System.out.println(password);
			// 验证码
			String code = messageLogin.getSms_code().trim();
			System.out.println(code);
			// 语音数据的月份
			String yearmonth = year + "".trim();
			System.out.println(yearmonth);
			String wdzlurlyuyin = "http://fj.189.cn/service/bill/trans.jsp?PRODNO=" + phone + "&PRODTYPE=" + prodtype
					+ "&CITYCODE=" + citycode + "&SELTYPE=" + seltype + "&MONTH=" + yearmonth + "&PURID=" + purid
					+ "&email_empoent=" + empoent + "&email_module=" + module + "&serPwd50=" + password + "&randomPwd="
					+ code;
			WebRequest webRequestwdzlyuyin;
			webRequestwdzlyuyin = new WebRequest(new URL(wdzlurlyuyin), HttpMethod.POST);
			HtmlPage wdzlyuyin = webClient.getPage(webRequestwdzlyuyin);
			String contentAsString2 = wdzlyuyin.getWebResponse().getContentAsString();

			Document doc = Jsoup.parse(contentAsString2);

			Elements tablelist = doc.select("table");
			if (tablelist.size() == 0) {
				System.out.println("第一次----获取语音信息失败！重试一次！");
				System.out.println(contentAsString2);

				String wdzlurlyuyin2 = "http://fj.189.cn/service/bill/trans.jsp?PRODNO=" + phone + "&PRODTYPE="
						+ prodtype + "&CITYCODE=" + citycode + "&SELTYPE=" + seltype + "&MONTH=" + yearmonth + "&PURID="
						+ purid + "&email_empoent=" + empoent + "&email_module=" + module + "&serPwd50=" + password
						+ "&randomPwd=" + code;
				WebRequest webRequestwdzlyuyin2;
				webRequestwdzlyuyin2 = new WebRequest(new URL(wdzlurlyuyin2), HttpMethod.POST);
				HtmlPage wdzlyuyin2 = webClient.getPage(webRequestwdzlyuyin2);
				String contentAsString22 = wdzlyuyin2.getWebResponse().getContentAsString();

				Document doc2 = Jsoup.parse(contentAsString22);

				Elements tablelist2 = doc2.select("table");
				if (tablelist2.size() == 0) {
					System.out.println("第二次----获取语音信息失败！放弃");
				} else {
					System.out.println("第二次----获取语音信息成功！");
					// 获得数据进行处理
					TelecomFujianhtml html = new TelecomFujianhtml();
					html.setHtml(contentAsString2);
					html.setPageCount(1);
					html.setTaskid(messageLogin.getTask_id());
					html.setType("语音信息");
					html.setUrl(wdzlurlyuyin);
					telecomFujianhtmlRepository.save(html);

					System.out.println("第一次----获取语音信息成功！");
					Elements tr = tablelist.get(1).select("tr");
					for (int i = 1; i < tr.size(); i++) {
						Elements select = tr.get(i).select("td");
						for (int j = 0; j < select.size(); j += 7) {
							// 通话起始时间
							String communicatetime2 = select.get(j + 1).html();
							// 通话时长
							String communicatetime = select.get(j + 2).html();
							// 呼叫类型
							String calltype = select.get(j + 3).html();
							// 通信地点
							String communicateaddr = select.get(j + 4).html();
							// 对方号码
							String oppositephone = select.get(j + 5).html();
							// 费用（元）
							String costtotal = select.get(j + 6).html();
							TelecomFujianCallHistory telecomFujianCallHistory = new TelecomFujianCallHistory();
							telecomFujianCallHistory.setTaskid(messageLogin.getTask_id().trim());
							telecomFujianCallHistory.setCommunicateaddr(communicateaddr);
							telecomFujianCallHistory.setCommunicatetime(communicatetime);
							telecomFujianCallHistory.setCommunicatetime2(communicatetime2);
							telecomFujianCallHistory.setCalltype(calltype);
							telecomFujianCallHistory.setOppositephone(oppositephone);
							telecomFujianCallHistory.setCosttotal(costtotal);
							telecomFujianCallHistoryRepository.save(telecomFujianCallHistory);

						}
					}
					// 更新task表
					taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 200, "语音信息采集中！");
				}

			} else {

				// 获得数据进行处理
				TelecomFujianhtml html = new TelecomFujianhtml();
				html.setHtml(contentAsString2);
				html.setPageCount(1);
				html.setTaskid(messageLogin.getTask_id());
				html.setType("语音信息");
				html.setUrl(wdzlurlyuyin);
				telecomFujianhtmlRepository.save(html);

				System.out.println("第一次----获取语音信息成功！");
				Elements tr = tablelist.get(1).select("tr");
				for (int i = 1; i < tr.size(); i++) {
					Elements select = tr.get(i).select("td");
					for (int j = 0; j < select.size(); j += 7) {
						// 通话起始时间
						String communicatetime2 = select.get(j + 1).html();
						// 通话时长
						String communicatetime = select.get(j + 2).html();
						// 呼叫类型
						String calltype = select.get(j + 3).html();
						// 通信地点
						String communicateaddr = select.get(j + 4).html();
						// 对方号码
						String oppositephone = select.get(j + 5).html();
						// 费用（元）
						String costtotal = select.get(j + 6).html();
						TelecomFujianCallHistory telecomFujianCallHistory = new TelecomFujianCallHistory();
						telecomFujianCallHistory.setTaskid(messageLogin.getTask_id().trim());
						telecomFujianCallHistory.setCommunicateaddr(communicateaddr);
						telecomFujianCallHistory.setCommunicatetime(communicatetime);
						telecomFujianCallHistory.setCommunicatetime2(communicatetime2);
						telecomFujianCallHistory.setCalltype(calltype);
						telecomFujianCallHistory.setOppositephone(oppositephone);
						telecomFujianCallHistory.setCosttotal(costtotal);
						telecomFujianCallHistoryRepository.save(telecomFujianCallHistory);

					}
				}
				// 更新task表
				taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 200, "语音信息采集中！");
			}

		} catch (Exception e) {
		}
	}

	public void crawlerMessage(MessageLogin messageLogin, int year) {
		tracer.addTag("service.crawlerMessage.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id().trim());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			// 手机号
			String phone = messageLogin.getName().trim();
			// 月份
			SimpleDateFormat df = new SimpleDateFormat("yyyyMM");// 设置日期格式
			String format = df.format(new Date());

			// 获得验证验证码的入参
			String url = "http://fj.189.cn/service/bill/tanChu.jsp?PRODNO=" + phone
					+ "&PRODTYPE=50&CITYCODE=0591&MONTH=" + format + "&SELTYPE=3";
			WebRequest webRequestq;
			webRequestq = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage wdzlyuyinq = webClient.getPage(webRequestq);
			String contentAsString21 = wdzlyuyinq.getWebResponse().getContentAsString();

			Document doc1 = Jsoup.parse(contentAsString21);
			// 50
			String prodtype = doc1.getElementById("PRODTYPE").val().trim();
			System.out.println(prodtype);
			// 0591
			String citycode = doc1.getElementById("CITYCODE").val().trim();
			System.out.println(citycode);
			// 3
			String seltype = doc1.getElementById("SELTYPE").val().trim();
			System.out.println(seltype);
			// 0
			String purid = doc1.getElementById("PURID").val().trim();
			System.out.println(purid);
			// 10001
			String empoent = doc1.getElementById("email_empoent").val().trim();
			System.out.println(empoent);
			// 863581c5892cdfe8a67b95c7abb47ead8b102e9620994ae95637f637fa22acac173b91015574507362816b30a884632d8562bf20de621d31d745291aaec7ca6f
			String module = doc1.getElementById("email_module").val().trim();
			System.out.println(module);

			// 密码
			String password = encryptedPhone(messageLogin.getPassword().trim()).trim();
			System.out.println(password);
			// 验证码
			String code = messageLogin.getSms_code().trim();
			System.out.println(code);

			String yearmonth = year + "".trim();
			System.out.println(yearmonth);

			String wdzlurlyuyin = "http://fj.189.cn/service/bill/trans.jsp?PRODNO=" + phone + "&PRODTYPE=" + prodtype
					+ "&CITYCODE=" + citycode + "&SELTYPE=" + seltype + "&MONTH=" + yearmonth + "&PURID=" + purid
					+ "&email_empoent=" + empoent + "&email_module=" + module + "&serPwd50=" + password + "&randomPwd="
					+ code;
			WebRequest webRequestwdzlyuyin;
			webRequestwdzlyuyin = new WebRequest(new URL(wdzlurlyuyin), HttpMethod.POST);
			HtmlPage wdzlyuyin = webClient.getPage(webRequestwdzlyuyin);
			String contentAsString2 = wdzlyuyin.getWebResponse().getContentAsString();

			Document doc = Jsoup.parse(contentAsString2);

			// 归属城市
			Elements tablelist = doc.select("table");
			if (tablelist.size() == 0) {
				System.out.println("第一次-----获取客户归属城市失败！");
				System.out.println(contentAsString2);

				String wdzlurlyuyin22 = "http://fj.189.cn/service/bill/trans.jsp?PRODNO=" + phone + "&PRODTYPE="
						+ prodtype + "&CITYCODE=" + citycode + "&SELTYPE=" + seltype + "&MONTH=" + yearmonth + "&PURID="
						+ purid + "&email_empoent=" + empoent + "&email_module=" + module + "&serPwd50=" + password
						+ "&randomPwd=" + code;
				WebRequest webRequestwdzlyuyin22;
				webRequestwdzlyuyin22 = new WebRequest(new URL(wdzlurlyuyin22), HttpMethod.POST);
				HtmlPage wdzlyuyin22 = webClient.getPage(webRequestwdzlyuyin22);
				String contentAsString222 = wdzlyuyin22.getWebResponse().getContentAsString();

				Document doc22 = Jsoup.parse(contentAsString222);

				// 归属城市
				Elements tablelist22 = doc22.select("table");
				if (tablelist22.size() == 0) {
					System.out.println("第二次-----获取客户归属城市失败！");
				} else {
					System.out.println("第二次-----获取客户归属城市成功！");
					// 获得数据进行处理
					TelecomFujianhtml html = new TelecomFujianhtml();
					html.setHtml(contentAsString2);
					html.setPageCount(1);
					html.setTaskid(messageLogin.getTask_id());
					html.setType("短信信息");
					html.setUrl(wdzlurlyuyin);
					telecomFujianhtmlRepository.save(html);

					Element table = tablelist22.get(0);
					Element td = table.select("td").get(5);
					String city = td.text().trim();

					TelecomFujianUserInfo findByTaskid = telecomFujianUserInfoRepository
							.findByTaskid(messageLogin.getTask_id());

					telecomFujianUserInfoRepository.updatewdcity(city, findByTaskid.getTaskid());

					// 更新task表
					taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 200, "基本信息-归属城市采集中！");

					Elements tr = doc.select("table").get(1).select("tr");
					for (int i = 1; i < tr.size(); i++) {
						Elements select = tr.get(i).select("td");
						for (int j = 0; j < select.size(); j += 7) {
							// 业务类型
							String bustype = select.get(j + 1).html();
							// 发送时间
							String sendtime = select.get(j + 2).html();
							// 发送号码
							String sendphone = select.get(j + 3).html();
							// 对方号码
							String oppositephone = select.get(j + 4).html();
							// 收发类型
							String messagetype = select.get(j + 5).html();
							// 费用（元）
							String messageaddr = select.get(j + 6).html();
							TelecomFujianMessageHistory telecomFujianMessageHistory = new TelecomFujianMessageHistory();
							telecomFujianMessageHistory.setTaskid(messageLogin.getTask_id().trim());
							telecomFujianMessageHistory.setBustype(bustype);
							telecomFujianMessageHistory.setSendtime(sendtime);
							telecomFujianMessageHistory.setSendphone(sendphone);
							telecomFujianMessageHistory.setOppositephone(oppositephone);
							telecomFujianMessageHistory.setMessagetype(messagetype);
							telecomFujianMessageHistory.setMessageaddr(messageaddr);
							telecomFujianMessageHistoryRepository.save(telecomFujianMessageHistory);

						}
					}
					// 更新task表
					taskMobileRepository.updateSMSRecordStatus(taskMobile.getTaskid(), 200, "短信信息采集中！");
				}

			} else {
				System.out.println("第一次-----获取客户归属城市成功！");

				// 获得数据进行处理
				TelecomFujianhtml html = new TelecomFujianhtml();
				html.setHtml(contentAsString2);
				html.setPageCount(1);
				html.setTaskid(messageLogin.getTask_id());
				html.setType("短信信息");
				html.setUrl(wdzlurlyuyin);
				telecomFujianhtmlRepository.save(html);

				Element table = tablelist.get(0);
				Element td = table.select("td").get(5);
				String city = td.text().trim();

				TelecomFujianUserInfo findByTaskid = telecomFujianUserInfoRepository
						.findByTaskid(messageLogin.getTask_id());
				telecomFujianUserInfoRepository.updatewdcity(city, findByTaskid.getTaskid());

				// 更新task表
				taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 200, "基本信息-归属城市采集中！");

				Elements tr = doc.select("table").get(1).select("tr");
				for (int i = 1; i < tr.size(); i++) {
					Elements select = tr.get(i).select("td");
					for (int j = 0; j < select.size(); j += 7) {
						// 业务类型
						String bustype = select.get(j + 1).html();
						// 发送时间
						String sendtime = select.get(j + 2).html();
						// 发送号码
						String sendphone = select.get(j + 3).html();
						// 对方号码
						String oppositephone = select.get(j + 4).html();
						// 收发类型
						String messagetype = select.get(j + 5).html();
						// 费用（元）
						String messageaddr = select.get(j + 6).html();
						TelecomFujianMessageHistory telecomFujianMessageHistory = new TelecomFujianMessageHistory();
						telecomFujianMessageHistory.setTaskid(messageLogin.getTask_id().trim());
						telecomFujianMessageHistory.setBustype(bustype);
						telecomFujianMessageHistory.setSendtime(sendtime);
						telecomFujianMessageHistory.setSendphone(sendphone);
						telecomFujianMessageHistory.setOppositephone(oppositephone);
						telecomFujianMessageHistory.setMessagetype(messagetype);
						telecomFujianMessageHistory.setMessageaddr(messageaddr);
						telecomFujianMessageHistoryRepository.save(telecomFujianMessageHistory);

					}
				}
				// 更新task表
				taskMobileRepository.updateSMSRecordStatus(taskMobile.getTaskid(), 200, "短信信息采集中！");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String encryptedPhone(String phonenum) throws Exception {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = this.readResource("telecom.js", Charsets.UTF_8);
		engine.eval(path);
		final Invocable invocable = (Invocable) engine;
		Object data = invocable.invokeFunction("encryptedString", phonenum);
		return data.toString();
	}

	public String readResource(final String fileName, Charset charset) throws IOException {
		return Resources.toString(Resources.getResource(fileName), charset);
	}
}