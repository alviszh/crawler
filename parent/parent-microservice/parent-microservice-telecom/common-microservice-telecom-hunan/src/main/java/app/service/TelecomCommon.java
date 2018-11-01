package app.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
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
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.hunan.TelecomHunanCallHistory;
import com.microservice.dao.entity.crawler.telecom.hunan.TelecomHunanChengkongMsg;
import com.microservice.dao.entity.crawler.telecom.hunan.TelecomHunanMessageHistory;
import com.microservice.dao.entity.crawler.telecom.hunan.TelecomHunanMonthBillHistory;
import com.microservice.dao.entity.crawler.telecom.hunan.TelecomHunanPayMsg;
import com.microservice.dao.entity.crawler.telecom.hunan.TelecomHunanTaocanMsg;
import com.microservice.dao.entity.crawler.telecom.hunan.TelecomHunanUserInfo;
import com.microservice.dao.entity.crawler.telecom.hunan.TelecomHunanZengzhiMsg;
import com.microservice.dao.entity.crawler.telecom.hunan.TelecomHunanhtml;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.hunan.TelecomHunanCallHistoryRepository;
import com.microservice.dao.repository.crawler.telecom.hunan.TelecomHunanChengkongMsgRepository;
import com.microservice.dao.repository.crawler.telecom.hunan.TelecomHunanMessageHistoryRepository;
import com.microservice.dao.repository.crawler.telecom.hunan.TelecomHunanMonthBillHistoryRepository;
import com.microservice.dao.repository.crawler.telecom.hunan.TelecomHunanPayMsgRepository;
import com.microservice.dao.repository.crawler.telecom.hunan.TelecomHunanTaocanMsgRepository;
import com.microservice.dao.repository.crawler.telecom.hunan.TelecomHunanUserInfoRepository;
import com.microservice.dao.repository.crawler.telecom.hunan.TelecomHunanZengzhiMsgRepository;
import com.microservice.dao.repository.crawler.telecom.hunan.TelecomHunanhtmlRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.ISms;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.hunan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.hunan")
public class TelecomCommon implements ISms{

	public static final Logger log = LoggerFactory.getLogger(TelecomCommon.class);

	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomHunanUserInfoRepository telecomHunanUserInfoRepository;
	@Autowired
	private TelecomHunanhtmlRepository telecomHunanhtmlRepository;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private TelecomHunanTaocanMsgRepository telecomHunanTaocanMsgRepository;
	@Autowired
	private TelecomHunanZengzhiMsgRepository telecomHunanZengzhiMsgRepository;
	@Autowired
	private TelecomHunanChengkongMsgRepository telecomHunanChengkongMsgRepository;
	@Autowired
	private TelecomHunanMonthBillHistoryRepository telecomHunanMonthBillHistoryRepository;
	@Autowired
	private TelecomHunanPayMsgRepository telecomHunanPayMsgRepository;
	@Autowired
	private TelecomHunanCallHistoryRepository telecomHunanCallHistoryRepository;
	@Autowired
	private TelecomHunanMessageHistoryRepository telecomHunanMessageHistoryRepository;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private RetryService retryService;
	@Value("${filesavepath}")
	String filesavepath;
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
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM-dd");
		Calendar calendar = Calendar.getInstance();
		// 某月的第一天
		calendar.add(Calendar.MONTH, -i);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return format.format(calendar.getTime());
	}

	// 某个月的最后一天
	public String getLastMonthdate(int i) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -i);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return format.format(calendar.getTime());
	}

	// 我的账户状态
	@Async
	public void crawlerWdstatus(MessageLogin messageLogin) {
		tracer.addTag("service.crawlerWdstatus.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			String wdzlurl1 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000801&&cityCode=hn";
			WebRequest webRequestwdzl1;
			webRequestwdzl1 = new WebRequest(new URL(wdzlurl1), HttpMethod.GET);
			HtmlPage wdzl1 = webClient.getPage(webRequestwdzl1);
			webClient = wdzl1.getWebClient();

			String wdzlurl = "http://hn.189.cn/webportal-wt/hnselfservice/customerInfo/customer-info!queryCustInStatus.action?_z=1&fastcode=10000284&cityCode=hn";
			WebRequest webRequestwdzl;
			webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.GET);
			webClient.getPage(webRequestwdzl);
			HtmlPage wdzl = webClient.getPage(webRequestwdzl);
			String asXml2 = wdzl.asXml();
			tracer.addTag("基本信息---我的账户状态", asXml2);
			// 获得数据进行处理

			TelecomHunanhtml html = new TelecomHunanhtml();
			html.setHtml(asXml2);
			html.setPageCount(1);
			html.setTaskid(messageLogin.getTask_id());
			html.setType("基本信息-账户状态");
			html.setUrl(wdzlurl);
			telecomHunanhtmlRepository.save(html);

			Document doc = Jsoup.parse(asXml2);
			Elements trs = doc.getElementsByClass("taoc_table");
			// 基本信息
			Elements element0 = trs.get(0).select("tr");
			Elements select = element0.get(1).select("td");
			// 产品名称
			String proname = select.get(0).html();
			// 当前状态
			String accountstatus = select.get(2).html();
			System.out.println(proname);
			System.out.println(accountstatus);

			// 用户信息实体类
			TelecomHunanUserInfo telecomHunanUserInfo = new TelecomHunanUserInfo();

			TelecomHunanUserInfo findByTaskid = telecomHunanUserInfoRepository.findByTaskid(messageLogin.getTask_id());
			if (findByTaskid == null) {
				telecomHunanUserInfo.setTaskid(taskMobile.getTaskid());
				telecomHunanUserInfo.setProname(proname);
				telecomHunanUserInfo.setAccountstatus(accountstatus);
				telecomHunanUserInfoRepository.save(telecomHunanUserInfo);

				// 更新task表
				taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 200, "基本信息-账户状态采集中！");

			} else {
				telecomHunanUserInfoRepository.updatewdStatus(proname, accountstatus, findByTaskid.getTaskid());

				// 更新task表
				taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 200, "基本信息-账户状态采集中！");

			}
			// 基本信息-我的余额 测试通过
			crawlerWdyue(messageLogin);
			// 更新最终的状态
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());

		} catch (Exception e) {
			// 更新最终的状态
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			e.printStackTrace();
		}
	}

	// 我的余额
	@Async
	public void crawlerWdyue(MessageLogin messageLogin) {
		tracer.addTag("service.crawlerWdyue.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			String wdzlurl1 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000801&&cityCode=hn";
			WebRequest webRequestwdzl1;
			webRequestwdzl1 = new WebRequest(new URL(wdzlurl1), HttpMethod.GET);
			HtmlPage wdzl1 = webClient.getPage(webRequestwdzl1);
			webClient = wdzl1.getWebClient();

			// 整理入参
			String requestphone = messageLogin.getName();
			SimpleDateFormat df = new SimpleDateFormat("yyyyMM");// 设置日期格式
			String format = df.format(new Date());// new Date()为获取当前系统时间

			String wdzlurl = "http://hn.189.cn/webportal-wt/hnselfservice/billquery/queryBalanceRecord.action?_z=1&fastcode=20000801&cityCode=hn";
			WebRequest webRequestwdzl;
			webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.POST);
			webClient.getPage(webRequestwdzl);
			HtmlPage wdzl = webClient.getPage(webRequestwdzl);
			String asXml2 = wdzl.asXml();

			tracer.addTag("基本信息---我的余额", asXml2);

			Document doc = Jsoup.parse(asXml2);
			Element trs = doc.select("table").get(0).select("tr").get(1).select("td").get(4);
			// 余额
			String remaing = trs.html();
			System.out.println(remaing);
			Elements select = doc.getElementById("queryNum2").select("option");
			// 电话号码
			String phone = select.get(0).html();
			System.out.println(phone);

			// 获得数据进行处理

			TelecomHunanhtml html = new TelecomHunanhtml();
			html.setHtml(asXml2);
			html.setPageCount(1);
			html.setTaskid(messageLogin.getTask_id());
			html.setType("基本信息-余额和电话号码");
			html.setUrl(wdzlurl);
			telecomHunanhtmlRepository.save(html);

			// 用户信息实体类
			TelecomHunanUserInfo telecomHunanUserInfo = new TelecomHunanUserInfo();

			TelecomHunanUserInfo findByTaskid = telecomHunanUserInfoRepository.findByTaskid(messageLogin.getTask_id());
			if (findByTaskid == null) {
				telecomHunanUserInfo.setTaskid(taskMobile.getTaskid());
				telecomHunanUserInfo.setCommonremaining(remaing);
				telecomHunanUserInfo.setPhone(phone);
				telecomHunanUserInfoRepository.save(telecomHunanUserInfo);

				// 更新task表
				taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 200, "基本信息-我的余额和手机号码采集中！");

			} else {
				telecomHunanUserInfoRepository.updatewdyue(remaing, phone, findByTaskid.getTaskid());

				// 更新task表
				taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 200, "基本信息-我的余额和手机号码采集中！");

			}
			// 更新最终的状态
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());

		} catch (Exception e) {
			// 更新最终的状态
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			e.printStackTrace();
		}
	}


	// 业务信息 三个
	@Async
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
			String wdzlurl1 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000801&&cityCode=hn";
			WebRequest webRequestwdzl1;
			webRequestwdzl1 = new WebRequest(new URL(wdzlurl1), HttpMethod.GET);
			HtmlPage wdzl1 = webClient.getPage(webRequestwdzl1);
			webClient = wdzl1.getWebClient();

			String wdzlurl = "http://hn.189.cn/webportal-wt/hnselfservice/businessquery/business-query!userBusinessList.action?_z=1&fastcode=10000279&cityCode=hn";
			WebRequest webRequestwdzl;
			webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.GET);
			webClient.getPage(webRequestwdzl);
			HtmlPage wdzl = webClient.getPage(webRequestwdzl);
			String asXml2 = wdzl.asXml();

			System.out.println("业务信息的界面信息-----" + asXml2);

			tracer.addTag("业务信息", asXml2);

			// 获得数据进行处理

			TelecomHunanhtml html = new TelecomHunanhtml();
			html.setHtml(asXml2);
			html.setPageCount(1);
			html.setTaskid(messageLogin.getTask_id());
			html.setType("业务信息");
			html.setUrl(wdzlurl);
			telecomHunanhtmlRepository.save(html);

			Document doc = Jsoup.parse(asXml2);
			Elements trs = doc.select("table");

			// 套餐业务
			String taocantable = trs.get(0) + "";
			if (taocantable.contains("无任何数据")) {
			} else {
				Elements element0 = trs.get(0).select("tr");
				for (int i = 1; i < element0.size(); i++) {
					Elements select = element0.get(i).select("td");
					for (int j = 0; j < select.size(); j += 4) {
						// 套餐名称
						String businessname = select.get(j).html();
						// 说明
						String explain = select.get(j + 1).html();
						// 生效日期
						String effecttime = select.get(j + 2).html();
						// 失效日期
						String losetime = select.get(j + 3).html();
						System.out.println(businessname);
						System.out.println(explain);
						System.out.println(effecttime);
						System.out.println(losetime);

						TelecomHunanTaocanMsg telecomHunanTaocanMsg = new TelecomHunanTaocanMsg();
						telecomHunanTaocanMsg.setTaskid(taskMobile.getTaskid());
						telecomHunanTaocanMsg.setBusinessname(businessname);
						telecomHunanTaocanMsg.setExplain(explain);
						telecomHunanTaocanMsg.setEffecttime(effecttime);
						telecomHunanTaocanMsg.setLosetime(losetime);
						telecomHunanTaocanMsg.setPhone(messageLogin.getName());
						telecomHunanTaocanMsgRepository.save(telecomHunanTaocanMsg);
					}
				}
			}

			// 增值业务
			Elements element1 = trs.get(1).select("tr");
			for (int i = 1; i < element1.size(); i++) {
				Elements select = element1.get(i).select("td");
				for (int j = 0; j < select.size(); j += 6) {
					// 业务名称
					String businessname = select.get(j).html();
					// SP名称
					String businessSPname = select.get(j + 1).html();
					// 业务费用
					String businessfee = select.get(j + 2).html();
					// 生效日期
					String effecttime = select.get(j + 3).html();
					// 失效日期
					String losetime = select.get(j + 4).html();
					System.out.println(businessname);
					System.out.println(businessSPname);
					System.out.println(businessfee);
					System.out.println(effecttime);
					System.out.println(losetime);

					TelecomHunanZengzhiMsg telecomHunanZengzhiMsg = new TelecomHunanZengzhiMsg();
					telecomHunanZengzhiMsg.setTaskid(taskMobile.getTaskid());
					telecomHunanZengzhiMsg.setBusinessname(businessname);
					telecomHunanZengzhiMsg.setBusinessSPname(businessSPname);
					telecomHunanZengzhiMsg.setBusinessfee(businessfee);
					telecomHunanZengzhiMsg.setEffecttime(effecttime);
					telecomHunanZengzhiMsg.setLosetime(losetime);
					telecomHunanZengzhiMsg.setPhone(messageLogin.getName());
					telecomHunanZengzhiMsgRepository.save(telecomHunanZengzhiMsg);
				}
			}

			// 程控业务
			Elements element2 = trs.get(2).select("tr");
			for (int i = 1; i < element2.size(); i++) {
				Elements select = element2.get(i).select("td");
				for (int j = 0; j < select.size(); j += 4) {
					// 套餐名称
					String businessname = select.get(j).html();
					// 说明
					String businessintroduce = select.get(j + 1).html();
					// 生效日期
					String effecttime = select.get(j + 2).html();
					System.out.println(businessname);
					System.out.println(businessintroduce);
					System.out.println(effecttime);

					TelecomHunanChengkongMsg telecomHunanChengkongMsg = new TelecomHunanChengkongMsg();
					telecomHunanChengkongMsg.setTaskid(taskMobile.getTaskid());
					telecomHunanChengkongMsg.setBusinessname(businessname);
					telecomHunanChengkongMsg.setBusinessintroduce(businessintroduce);
					telecomHunanChengkongMsg.setEffecttime(effecttime);
					telecomHunanChengkongMsg.setPhone(messageLogin.getName());
					telecomHunanChengkongMsgRepository.save(telecomHunanChengkongMsg);
				}
			}
			// 更新task表
			taskMobileRepository.updateBusinessMsgStatus(taskMobile.getTaskid(), 200, "业务信息采集中！");
		} catch (Exception e) {
			// 更新task表
			taskMobileRepository.updateBusinessMsgStatus(taskMobile.getTaskid(), 200, "业务信息采集中！");
			e.printStackTrace();
		}
		// 更新最终的状态
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
	}

	// 历史月账单
	@Async
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
			String wdzlurl1 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000801&&cityCode=hn";
			WebRequest webRequestwdzl1;
			webRequestwdzl1 = new WebRequest(new URL(wdzlurl1), HttpMethod.GET);
			HtmlPage wdzl1 = webClient.getPage(webRequestwdzl1);
			webClient = wdzl1.getWebClient();

			// 整理入参
			String requestphone = messageLogin.getName();
			String format = year + "";
			String wdzlurl = "http://hn.189.cn/webportal-wt/hnselfservice/billquery/queryBillDetailQuery.do?queryFlag=2&queryMonth="
					+ format + "&productId=" + requestphone;
			WebRequest webRequestwdzl;
			webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.GET);
			webClient.getPage(webRequestwdzl);
			HtmlPage wdzl = webClient.getPage(webRequestwdzl);
			String asXml2 = wdzl.asXml();

			int m = 0;
			m = m++;
			tracer.addTag("历史月账单第" + m + "次请求返回 的报文", asXml2);

			// 获得数据进行处理
			TelecomHunanhtml html = new TelecomHunanhtml();
			html.setHtml(asXml2);
			html.setPageCount(1);
			html.setTaskid(messageLogin.getTask_id());
			html.setType("历史月账单信息");
			html.setUrl(wdzlurl);
			telecomHunanhtmlRepository.save(html);

			if (asXml2.contains("查不到相应月份的账户级账单")) {
				System.out.println("查不到相应月份的账户级账单");
			} else {

				TelecomHunanMonthBillHistory telecomHunanMonthBillHistory = new TelecomHunanMonthBillHistory();

				Document doc = Jsoup.parse(asXml2);

				Elements trs = doc.select("table").get(1).select("table");
				// 第一部分
				Elements element1 = trs.get(1).select("td");
				// 姓名--用户信息中的
				String name = element1.get(2).html();

				// 用户信息实体类
				TelecomHunanUserInfo telecomHunanUserInfo = new TelecomHunanUserInfo();

				TelecomHunanUserInfo findByTaskid = telecomHunanUserInfoRepository
						.findByTaskid(messageLogin.getTask_id());
				if (findByTaskid == null) {
					telecomHunanUserInfo.setTaskid(taskMobile.getTaskid());
					telecomHunanUserInfo.setName(name);
					telecomHunanUserInfoRepository.save(telecomHunanUserInfo);

					// 更新task表
					taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 200, "基本信息-姓名采集中！");

				} else {
					telecomHunanUserInfoRepository.updatewdname(name, findByTaskid.getTaskid());

					// 更新task表
					taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 200, "基本信息-姓名采集中！");

				}

				// 月份
				String month = element1.get(4).html();
				// 总计
				String monthall = element1.get(5).html();
				System.out.println(name);
				System.out.println(month);
				System.out.println(monthall);
				telecomHunanMonthBillHistory.setTaskid(messageLogin.getTask_id().trim());
				telecomHunanMonthBillHistory.setMonth(month);
				telecomHunanMonthBillHistory.setMonthall(monthall);

				if (!asXml2.contains("有线宽带")) {
					if (trs.size() == 5) {
						// 月使用费
						Elements element4 = trs.get(4).select("td");
						String monthfee = element4.get(element4.size() - 1).html();
						System.out.println(monthfee);
						telecomHunanMonthBillHistory.setMonthfee(monthfee);
					}
					if (trs.size() == 6) {
						// 月使用费
						Elements element4 = trs.get(4).select("td");
						String monthfee = element4.get(element4.size() - 1).html();
						System.out.println(monthfee);
						// 短信彩信费
						Elements element5 = trs.get(5).select("td");
						String msgfee = element5.get(element5.size() - 1).html();
						System.out.println(msgfee);
						telecomHunanMonthBillHistory.setMonthfee(monthfee);
						telecomHunanMonthBillHistory.setMsgfee(msgfee);
					}
					if (trs.size() == 7) {
						// 月使用费
						Elements element4 = trs.get(4).select("td");
						String monthfee = element4.get(element4.size() - 1).html();
						System.out.println(monthfee);
						// 短信彩信费
						Elements element5 = trs.get(5).select("td");
						String msgfee = element5.get(element5.size() - 1).html();
						System.out.println(msgfee);
						// 代收费
						Elements element6 = trs.get(6).select("td");
						String daishoufee = element6.get(element6.size() - 1).html();
						System.out.println(daishoufee);
						telecomHunanMonthBillHistory.setMonthfee(monthfee);
						telecomHunanMonthBillHistory.setMsgfee(msgfee);
						telecomHunanMonthBillHistory.setDaishoufee(daishoufee);
					}
				} else {
					// 月使用量
					String monthfee = monthall;
					telecomHunanMonthBillHistory.setMonthfee(monthfee);
				}

				String table4 = doc.getElementById("Table4") + "";
				if (table4.contains("月基本费")) {
					String[] split = table4.split("月基本费 </td>");
					String[] split2 = split[1].split("元");
					String[] split3 = split2[0].split("<td width=\"50%\">");
					String monthfee = split3[1].trim();
					telecomHunanMonthBillHistory.setMonthfee(monthfee);
				}
				if (table4.contains("短信彩信费")) {
					String[] split = table4.split("短信彩信费 </td>");
					String[] split2 = split[1].split("元");
					String[] split3 = split2[0].split("<td width=\"50%\">");
					String msgfee = split3[1].trim();
					telecomHunanMonthBillHistory.setMsgfee(msgfee);
				}
				if (table4.contains("套外优惠")) {
					String[] split = table4.split("套外优惠 </td>");
					String[] split2 = split[1].split("元");
					String[] split3 = split2[0].split("<td width=\"50%\">");
					String twyhfee = split3[1].trim();
					telecomHunanMonthBillHistory.setTwyhfee(twyhfee);
				}
				if (table4.contains("红包返还")) {
					String[] split = table4.split("红包返还 </td>");
					String[] split2 = split[1].split("元");
					String[] split3 = split2[0].split("<td width=\"50%\">");
					String hbfhfee = split3[1].trim();
					telecomHunanMonthBillHistory.setHbfhfee(hbfhfee);
				}

				telecomHunanMonthBillHistoryRepository.save(telecomHunanMonthBillHistory);
				// 更新task表
				taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 200, "月账单信息采集中！");

				// 更新最终的状态
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}
		} catch (Exception e) {
			// 更新task表
			taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 200, "月账单信息采集中！");

			// 更新最终的状态
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			e.printStackTrace();
		}
	}

	// 缴费信息
	@Async
	public void crawlerPaymsg(MessageLogin messageLogin, int year, int qzb) {

		tracer.addTag("service.crawlerPaymsg.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {

			String wdzlurl1 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000801&&cityCode=hn";
			WebRequest webRequestwdzl1;
			webRequestwdzl1 = new WebRequest(new URL(wdzlurl1), HttpMethod.GET);
			HtmlPage wdzl1 = webClient.getPage(webRequestwdzl1);
			webClient = wdzl1.getWebClient();

			// 整理入参
			String format = year + "";

			String wdzlurl = "http://hn.189.cn/webportal-wt/hnselfservice/billquery/queryPaymentRecord.action?_z=1&fastcode=20000802&cityCode=hn";
			// String wdzlurl =
			// "http://hn.189.cn/webportal-wt/hnselfservice/billquery/queryPaymentRecord.parser?queryNumType=80000045&accNbrType=80000045&queryMonth="
			// + format;
			WebRequest webRequestwdzl;
			webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.POST);
			webClient.getPage(webRequestwdzl);
			HtmlPage wdzl = webClient.getPage(webRequestwdzl);
			String asXml2 = wdzl.asXml();

			// 获得数据进行处理
			TelecomHunanhtml html = new TelecomHunanhtml();
			html.setHtml(asXml2);
			html.setPageCount(1);
			html.setTaskid(messageLogin.getTask_id());
			html.setType("缴费信息");
			html.setUrl(wdzlurl);
			telecomHunanhtmlRepository.save(html);
			if (asXml2.contains("未查询到相关数据")) {
				System.out.println("未查询到相关数据");
			} else {
				Document doc = Jsoup.parse(asXml2);
				Elements trs = doc.getElementsByClass("taoc_table");
				// 缴费信息
				Elements element0 = trs.get(0).select("tr");
				for (int i = 1; i < element0.size(); i++) {
					Elements select = element0.get(i).select("td");
					if (select.size() == 5) {
						for (int j = 0; j < select.size(); j += 5) {
							// 入账时间
							String paydate = select.get(j).html();
							// 入账金额
							String paymoney = select.get(j + 1).html();
							// 交费渠道
							String payditch = select.get(j + 2).html();
							// 交费方式
							String payway = select.get(j + 3).html();
							// 使用范围
							String scope = select.get(j + 4).html();
							System.out.println(paydate);
							System.out.println(paymoney);
							System.out.println(payditch);
							System.out.println(payway);
							System.out.println(scope);
							TelecomHunanPayMsg telecomHunanPayMsg = new TelecomHunanPayMsg();
							telecomHunanPayMsg.setTaskid(messageLogin.getTask_id().trim());
							telecomHunanPayMsg.setPaydate(paydate);
							telecomHunanPayMsg.setPaymoney(paymoney);
							telecomHunanPayMsg.setPayditch(payditch);
							telecomHunanPayMsg.setPayway(payway);
							telecomHunanPayMsg.setScope(scope);
							telecomHunanPayMsgRepository.save(telecomHunanPayMsg);
						}
					}
				}
			}
			// 更新task表
			taskMobileRepository.updatePayMsgStatus(taskMobile.getTaskid(), 200, "缴费信息采集中！");
			// 更新最终的状态
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		} catch (Exception e) {
			e.printStackTrace();
			// 更新task表
			taskMobileRepository.updatePayMsgStatus(taskMobile.getTaskid(), 200, "缴费信息采集中！");
			// 更新最终的状态
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}

	}
	public void crawlerCall(MessageLogin messageLogin, String month, String lastday) {
		tracer.addTag("service.crawlerCall.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			// 验证码
			String code = messageLogin.getSms_code().trim();
			// 年月
			String yearMonth = month.trim();
			// 最后一天
			String last = lastday.trim();

			Thread.sleep(2000);

			String wdzlurl = "http://hn.189.cn/webportal-wt/hnselfservice/billquery/bill-query!queryBillx.action?tm=2038%E4%B8%8B%E5%8D%885:03:57&tabIndex=2&queryMonth="
					+ yearMonth + "&patitype=2&startDay=1&endDay=" + last + "&valicode=" + code + "&code=" + code
					+ "&accNbr=";
			WebRequest requestSettings = new WebRequest(new URL(wdzlurl), HttpMethod.GET);
			HtmlPage htmlpage = webClient.getPage(requestSettings);
			String asXml = htmlpage.asXml();
			tracer.addTag("获取语音返回的报文-----" + yearMonth, asXml);
			if (asXml.contains("没有查询到相关数据")) {
				System.out.println("你所查询的时间没有数据！");
			} else if (asXml.contains("对不起您的号码未通过实名校验，请先完成实名核对，并审核通过后再来访问此功能！")) {
				System.out.println("对不起您的号码未通过实名校验，请先完成实名核对，并审核通过后再来访问此功能！");
				HtmlPage retry = retryService.retry(wdzlurl, webClient, yearMonth);
				String asXml2 = retry.asXml();
				tracer.addTag("重试成功返回的报文-----" + yearMonth, asXml2);
				DomElement elementById = retry.getElementById("exp_b_list_btn");
				Page click = elementById.click();
//				save1(contentAsStream, "D:\\" + messageLogin.getName() + "---" + yearMonth + ".pdf");
//				readFdf("D:\\" + messageLogin.getName() + "---" + yearMonth + ".pdf");
//				File file1 = new File("D:\\" + messageLogin.getName() + "---" + yearMonth + ".txt");
//				save1(contentAsStream, filesavepath+ "/" + messageLogin.getName() + "---" + yearMonth + ".pdf");
				
				String url = filesavepath + "/" + messageLogin.getName() + "---" + yearMonth + ".pdf";
				System.out.println(url);
				getImagePath(click,url);
				readFdf(url);
				File file1 = new File(filesavepath+ "/" + messageLogin.getName() + "---" + yearMonth + ".txt");
				
				String readTxtFile = readTxtFile(file1);
				String[] split = readTxtFile.split("\r\n");
				List<String> base = new ArrayList<String>();
				for (int i = 4; i < split.length; i++) {
					String string = split[i].toString();
					if (string.contains("被叫") || string.contains("主叫")) {
						String string1 = split[i - 1].toString();
						String string2 = split[i - 2].toString();
						base.add("第" + string2 + "-" + string1 + " " + string);
					}
				}
				tracer.addTag("解析图片获取的语音数据总数-----" + yearMonth, base.size() + "");
				for (int i = 0; i < base.size(); i++) {
					String string = base.get(i).toString();
					String[] split2 = string.split(" ");
					// 通话时间
					String communicatetime2 = split2[1].trim();
					// 类型
					String calltype = split2[2].trim();
					// 对方号码
					String oppositephone = split2[3].trim();
					// 时长
					String communicatetime = split2[4].trim();
					// 通话地区
					String communicateaddr = split2[5].trim();
					// 费用(元)
					String costtotal = split2[6].trim();
					// 话单类型
					String correspondencetype = split2[7].trim();
					System.out.println(communicatetime2);
					System.out.println(calltype);
					System.out.println(oppositephone);
					System.out.println(communicatetime);
					System.out.println(communicateaddr);
					System.out.println(costtotal);
					System.out.println(correspondencetype);

					TelecomHunanCallHistory telecomHunanCallHistory = new TelecomHunanCallHistory();
					telecomHunanCallHistory.setTaskid(messageLogin.getTask_id().trim());
					telecomHunanCallHistory.setCommunicateaddr(communicateaddr);
					telecomHunanCallHistory.setCommunicatetime(communicatetime);
					telecomHunanCallHistory.setCommunicatetime2(communicatetime2);
					telecomHunanCallHistory.setOppositephone(oppositephone);
					telecomHunanCallHistory.setCosttotal(costtotal);
					telecomHunanCallHistory.setCalltype(calltype);
					telecomHunanCallHistory.setCorrespondencetype(correspondencetype);
					telecomHunanCallHistoryRepository.save(telecomHunanCallHistory);
				}
			} else {
				DomElement elementById = htmlpage.getElementById("exp_b_list_btn");
				Page click = elementById.click();
//				String url ="D:\\img\\" + messageLogin.getName() + "---" + yearMonth + ".pdf";
//				getImagePath(click,url);
//				readFdf("D:\\" + messageLogin.getName() + "---" + yearMonth + ".pdf");
//				File file1 = new File("D:\\" + messageLogin.getName() + "---" + yearMonth + ".txt");
				String url = filesavepath + "/" + messageLogin.getName() + "---" + yearMonth + ".pdf";
				System.out.println(url);
				getImagePath(click,url);
				readFdf(url);
				File file1 = new File(filesavepath + "/" + messageLogin.getName() + "---" + yearMonth + ".txt");
				
				String readTxtFile = readTxtFile(file1);
				String[] split = readTxtFile.split("\r\n");
				List<String> base = new ArrayList<String>();
				for (int i = 4; i < split.length; i++) {
					String string = split[i].toString();
					if (string.contains("被叫") || string.contains("主叫")) {
						String string1 = split[i - 1].toString();
						String string2 = split[i - 2].toString();
						base.add("第" + string2 + "-" + string1 + " " + string);
					}
				}
				tracer.addTag("解析图片获取的语音数据总数-----" + yearMonth, base.size() + "");
				for (int i = 0; i < base.size(); i++) {
					String string = base.get(i).toString();
					String[] split2 = string.split(" ");
					// 通话时间
					String communicatetime2 = split2[1].trim();
					// 类型
					String calltype = split2[2].trim();
					// 对方号码
					String oppositephone = split2[3].trim();
					// 时长
					String communicatetime = split2[4].trim();
					// 通话地区
					String communicateaddr = split2[5].trim();
					// 费用(元)
					String costtotal = split2[6].trim();
					// 话单类型
					String correspondencetype = split2[7].trim();
					System.out.println(communicatetime2);
					System.out.println(calltype);
					System.out.println(oppositephone);
					System.out.println(communicatetime);
					System.out.println(communicateaddr);
					System.out.println(costtotal);
					System.out.println(correspondencetype);

					TelecomHunanCallHistory telecomHunanCallHistory = new TelecomHunanCallHistory();
					telecomHunanCallHistory.setTaskid(messageLogin.getTask_id().trim());
					telecomHunanCallHistory.setCommunicateaddr(communicateaddr);
					telecomHunanCallHistory.setCommunicatetime(communicatetime);
					telecomHunanCallHistory.setCommunicatetime2(communicatetime2);
					telecomHunanCallHistory.setOppositephone(oppositephone);
					telecomHunanCallHistory.setCosttotal(costtotal);
					telecomHunanCallHistory.setCalltype(calltype);
					telecomHunanCallHistory.setCorrespondencetype(correspondencetype);
					telecomHunanCallHistoryRepository.save(telecomHunanCallHistory);
				}
			}

			// 更新task表
			taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 200, "语音通话信息采集中！");
			// 更新最终的状态
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		} catch (Exception e) {
			// 更新task表
			taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 200, "语音通话信息采集中！");
			// 更新最终的状态
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			e.printStackTrace();
		}
	}

	@Async
	public void crawlerMessage(MessageLogin messageLogin, String month, String lastday) {
		tracer.addTag("service.crawlerMessage.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {

			// 验证码
			String code = messageLogin.getSms_code().trim();
			// 年月
			String yearMonth = month.trim();
			// 最后一天
			String last = lastday.trim();

			Thread.sleep(2000);
			String wdzlurl = "http://hn.189.cn/webportal-wt/hnselfservice/billquery/bill-query!queryBillx.action?tm=2038%E4%B8%8B%E5%8D%884:44:42&tabIndex=2&queryMonth="
					+ yearMonth + "&patitype=12&startDay=1&endDay=" + last + "&valicode=" + code + "&code=" + code
					+ "&accNbr=";
			WebRequest webRequestwdzl;
			webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.GET);
			webClient.getPage(webRequestwdzl);
			HtmlPage wdzl = webClient.getPage(webRequestwdzl);
			String contentAsString = wdzl.getWebResponse().getContentAsString();
			System.out.println(contentAsString);

			tracer.addTag("短信第1次请求返回 的报文---" + yearMonth, contentAsString);

			TelecomHunanhtml html = new TelecomHunanhtml();
			html.setHtml(contentAsString);
			html.setPageCount(1);
			html.setTaskid(messageLogin.getTask_id());
			html.setType("短信信息");
			html.setUrl(wdzlurl);
			telecomHunanhtmlRepository.save(html);

			if (contentAsString.contains("没有查询到相关数据")) {
				System.out.println("你所查询的时间没有数据！");

			} else if (contentAsString.contains("对不起您的号码未通过实名校验，请先完成实名核对，并审核通过后再来访问此功能！")) {
				System.out.println("对不起您的号码未通过实名校验，请先完成实名核对，并审核通过后再来访问此功能！");
			} else {
				Document doc = Jsoup.parse(contentAsString);
				Elements element0 = doc.select("tr");
				for (int i = 0; i < element0.size(); i++) {
					Elements select = element0.get(i).select("td");
					if (select.size() == 7) {
						// 发送开始时间
						String sendtime = select.get(1).text();
						// 本机号码
						String myphone = select.get(2).text();
						// 对方号码
						String oppositephone = select.get(3).text();
						// 费用(元)
						String messageaddr = select.get(4).text();
						// 话单类型
						String bustype = select.get(5).text();
						// 帐目类型
						String zmlx = select.get(6).text();

						System.out.println(sendtime);
						System.out.println(myphone);
						System.out.println(oppositephone);
						System.out.println(messageaddr);
						System.out.println(bustype);
						System.out.println(zmlx);

						TelecomHunanMessageHistory telecomHunanMessageHistory = new TelecomHunanMessageHistory();
						telecomHunanMessageHistory.setTaskid(messageLogin.getTask_id().trim());
						telecomHunanMessageHistory.setSendtime(sendtime);
						telecomHunanMessageHistory.setMyphone(myphone);
						telecomHunanMessageHistory.setOppositephone(oppositephone);
						telecomHunanMessageHistory.setMessageaddr(messageaddr);
						telecomHunanMessageHistory.setBustype(bustype);
						telecomHunanMessageHistory.setZmlx(zmlx);
						telecomHunanMessageHistoryRepository.save(telecomHunanMessageHistory);
					}
				}

				// 获取所有的数据
				Element element = doc.getElementsByClass("selser_ye").get(0);
				String trim = element.text().trim();
				String[] split = trim.split("：");
				String[] split2 = split[1].split("总");
				int pagenum = Integer.parseInt(split2[0].trim());
				if (pagenum <= 20) {
					System.out.println("不需要分页！");
				} else {
					System.out.println("需要分页！");
					int page = pagenum / 20;
					for (int j = 2; j <= page + 1; j++) {
						String wdzlurl11 = "http://hn.189.cn/webportal-wt/hnselfservice/billquery/bill-query!queryBillx.action?tm=2038%E4%B8%8B%E5%8D%884:44:42&tabIndex=2&queryMonth="
								+ yearMonth + "&patitype=12&startDay=1&endDay=" + last + "&valicode=" + code + "&code="
								+ code + "&accNbr=&pageNo=" + j;
						WebRequest webRequestwdzl11;
						webRequestwdzl11 = new WebRequest(new URL(wdzlurl11), HttpMethod.GET);
						webClient.getPage(webRequestwdzl11);
						HtmlPage wdzl11 = webClient.getPage(webRequestwdzl11);
						String contentAsString11 = wdzl11.getWebResponse().getContentAsString();
						System.out.println(contentAsString11);

						tracer.addTag("短信第" + j + "次请求返回 的报文", contentAsString11);

						Document doc2 = Jsoup.parse(contentAsString11);

						Elements element01 = doc2.select("tr");
						for (int i = 0; i < element01.size(); i++) {
							Elements select = element01.get(i).select("td");
							if (select.size() == 7) {
								// 发送开始时间
								String sendtime = select.get(1).text();
								// 本机号码
								String myphone = select.get(2).text();
								// 对方号码
								String oppositephone = select.get(3).text();
								// 费用(元)
								String messageaddr = select.get(4).text();
								// 话单类型
								String bustype = select.get(5).text();
								// 帐目类型
								String zmlx = select.get(6).text();

								System.out.println(sendtime);
								System.out.println(myphone);
								System.out.println(oppositephone);
								System.out.println(messageaddr);
								System.out.println(bustype);
								System.out.println(zmlx);

								TelecomHunanMessageHistory telecomHunanMessageHistory = new TelecomHunanMessageHistory();
								telecomHunanMessageHistory.setTaskid(messageLogin.getTask_id().trim());
								telecomHunanMessageHistory.setSendtime(sendtime);
								telecomHunanMessageHistory.setMyphone(myphone);
								telecomHunanMessageHistory.setOppositephone(oppositephone);
								telecomHunanMessageHistory.setMessageaddr(messageaddr);
								telecomHunanMessageHistory.setBustype(bustype);
								telecomHunanMessageHistory.setZmlx(zmlx);
								telecomHunanMessageHistoryRepository.save(telecomHunanMessageHistory);
							}
						}

					}
				}
			}
			// 更新task表
			taskMobileRepository.updateSMSRecordStatus(taskMobile.getTaskid(), 200, "短信信息采集中！");
			// 更新最终的状态
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		} catch (Exception e) {
			// 更新task表
			taskMobileRepository.updateSMSRecordStatus(taskMobile.getTaskid(), 200, "短信信息采集中！");
			// 更新最终的状态
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			e.printStackTrace();
		}
	}

	// 实名认证-发送验证码方法
	public TaskMobile sendSms(MessageLogin messageLogin) {
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		try {
			String wdzlurl1 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=20000801&&cityCode=hn";
			WebRequest webRequestwdzl1;
			webRequestwdzl1 = new WebRequest(new URL(wdzlurl1), HttpMethod.GET);
			HtmlPage wdzl1 = webClient.getPage(webRequestwdzl1);
			webClient = wdzl1.getWebClient();

			String wdzlurl = "http://hn.189.cn/webportal-wt/hnselfservice/billquery/bill-query!queryBill.action?_z=1&fastcode=10000280&cityCode=hn";
			WebRequest webRequestwdzl;
			webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.GET);
			webClient.getPage(webRequestwdzl);
			HtmlPage wdzl = webClient.getPage(webRequestwdzl);

			// 获取对应的输入框
			HtmlTextInput cardId = (HtmlTextInput) wdzl.getFirstByXPath("//input[@id='cardId']");
			HtmlTextInput userName = (HtmlTextInput) wdzl.getFirstByXPath("//input[@id='userName']");
			HtmlElement wdzlbutton = (HtmlElement) wdzl.getFirstByXPath("//a[@class='btn_big']");
//			cardId.setText("230833199411240338");
//			userName.setText("齐忠斌");
			 cardId.setText(taskMobile.getBasicUser().getIdnum());
			 userName.setText(taskMobile.getBasicUser().getName());
			// 发送验证码的界面
			HtmlPage wdzlpage = wdzlbutton.click();

			// 获得数据进行处理
			TelecomHunanhtml html = new TelecomHunanhtml();
			html.setHtml(wdzlpage.asXml());
			html.setPageCount(1);
			html.setTaskid(messageLogin.getTask_id());
			html.setType("实名认证-发送验证码信息");
			html.setUrl(wdzlurl);
			telecomHunanhtmlRepository.save(html);

			// 图片验证码输入框
			HtmlTextInput test = (HtmlTextInput) wdzlpage.getFirstByXPath("//input[@id='randQuery']");
			System.out.println(test);
			if (null == test) {
				System.out.println("实名认证失败！");
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_QUERY_ERROR_AUTONYM.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_QUERY_ERROR_AUTONYM.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_QUERY_ERROR_AUTONYM.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_QUERY_ERROR_AUTONYM.getError_code());
				save(taskMobile);
			} else {
				System.out.println("实名认证成功！");
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhasestatus());
				taskMobile.setDescription("实名认证成功！");
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getError_code());
				save(taskMobile);

				// 发送验证码
				Thread.sleep(5000);
				// 图片验证码输入框
				HtmlTextInput randQuery = (HtmlTextInput) wdzlpage.getFirstByXPath("//input[@id='randQuery']");
				// 图片验证码
				HtmlImage validationCode4 = (HtmlImage) wdzlpage.getFirstByXPath("//img[@id='validationCode4']");
				String code = chaoJiYingOcrService.getVerifycode(validationCode4, "1004").trim();

				String wdzlurlsend = "http://hn.189.cn/webportal-wt/hnselfservice/billquery/bill-query!queryBilly.action?number="
						+ messageLogin.getName().trim() + "&randQuery=" + code;
				WebRequest webRequestwdzlsend;
				webRequestwdzlsend = new WebRequest(new URL(wdzlurlsend), HttpMethod.POST);
				Page pagesend = webClient.getPage(webRequestwdzlsend);
				String pagesendstring = pagesend.getWebResponse().getContentAsString();
				System.out.println(pagesendstring);
				if (pagesendstring.contains("success")) {
					System.out.println("验证码发送成功！");
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
					taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getError_code());
					// 数据库中的cookies更新
					String cookieString = CommonUnit.transcookieToJson(webClient);
					taskMobile.setCookies(cookieString);
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

			String code = messageLogin.getSms_code().trim();
			String wdzlurl = "http://hn.189.cn/webportal-wt/hnselfservice/billquery/bill-query!queryBillx.action";
			String body = "tm=2038%E4%B8%8B%E5%8D%884:41:54&tabIndex=2&queryMonth=2018-05&patitype=2&startDay=1&endDay=28&valicode="
					+ code + "&code=" + code + "&accNbr=";
			WebRequest webRequestwdzl;
			webRequestwdzl = new WebRequest(new URL(wdzlurl), HttpMethod.POST);
			webRequestwdzl.setRequestBody(body);
			webClient.getPage(webRequestwdzl);
			HtmlPage wdzl = webClient.getPage(webRequestwdzl);
			String asXml2 = wdzl.asXml();

			if (asXml2.contains("对不起您的号码未通过实名校验")) {
				System.out.println("验证码验证失败！");
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR_RETRY.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR_RETRY.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR_RETRY.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR_RETRY.getError_code());
				// 保存状态
				save(taskMobile);
			} else {

				// 获得数据进行处理
				TelecomHunanhtml html = new TelecomHunanhtml();
				html.setHtml(asXml2);
				html.setPageCount(1);
				html.setTaskid(messageLogin.getTask_id());
				html.setType("验证验证码信息");
				html.setUrl(wdzlurl);
				telecomHunanhtmlRepository.save(html);

				Document doc = Jsoup.parse(asXml2);
				Element label = doc.getElementById("fail");
				if (null == label) {

					System.out.println("验证码验证成功！");
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
					taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getError_code());
					// 保存状态
					save(taskMobile);

				} else {
					System.out.println("验证码验证失败！");
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR_RETRY.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR_RETRY.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR_RETRY.getDescription());
					taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR_RETRY.getError_code());
					// 保存状态
					save(taskMobile);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskMobile;
	}

	//利用IO流保存验证码成功后，返回验证码图片保存路径 
		public static String getImagePath(Page page,String url) throws Exception{ 
			File imageFile = getImageCustomPath(url); 
			String imgagePath = imageFile.getAbsolutePath(); 
			InputStream inputStream = page.getWebResponse().getContentAsStream(); 
			FileOutputStream outputStream = (new FileOutputStream(new java.io.File(imgagePath))); 
			if (inputStream != null && outputStream != null) { 
			int temp = 0; 
			while ((temp = inputStream.read()) != -1) { // 开始拷贝 
			outputStream.write(temp); // 边读边写 
			} 
			outputStream.close(); 
			inputStream.close(); // 关闭输入输出流 
			} 
			return imgagePath; 
		} 
		//创建验证码图片保存路径 
		public static File getImageCustomPath(String url) { 
			File parentDirFile = new File("/opt/image"); 
			parentDirFile.setReadable(true); // 
			parentDirFile.setWritable(true); // 
			if (!parentDirFile.exists()) { 
			System.out.println("==========创建文件夹=========="); 
			parentDirFile.mkdirs(); 
			} 
			File codeImageFile = new File(url); 
			codeImageFile.setReadable(true); // 
			codeImageFile.setWritable(true, false); // 
			return codeImageFile; 
		}


	public static void readFdf(String file) throws Exception {
		// 是否排序
		boolean sort = false;
		// pdf文件名
		String pdfFile = file;
		// 输入文本文件名称
		String textFile = null;
		// 编码方式
		String encoding = "UTF-8";
		// 开始提取页数
		int startPage = 1;
		// 结束提取页数
		int endPage = Integer.MAX_VALUE;
		// 文件输入流，生成文本文件
		Writer output = null;
		// 内存中存储的PDF Document
		PDDocument document = null;
		try {
			try {
				// 首先当作一个URL来装载文件，如果得到异常再从本地文件系统//去装载文件
				URL url = new URL(pdfFile);
				// 注意参数已不是以前版本中的URL.而是File。
				document = PDDocument.load(pdfFile);
				// 获取PDF的文件名
				String fileName = url.getFile();
				// 以原来PDF的名称来命名新产生的txt文件
				if (fileName.length() > 4) {
					File outputFile = new File(fileName.substring(0, fileName.length() - 4) + ".txt");
					textFile = outputFile.getName();
				}
			} catch (MalformedURLException e) {
				// 如果作为URL装载得到异常则从文件系统装载
				// 注意参数已不是以前版本中的URL.而是File。
				document = PDDocument.load(pdfFile);
				if (pdfFile.length() > 4) {
					textFile = pdfFile.substring(0, pdfFile.length() - 4) + ".txt";
				}
			}
			// 文件输入流，写入文件倒textFile
			output = new OutputStreamWriter(new FileOutputStream(textFile), encoding);
			// PDFTextStripper来提取文本
			PDFTextStripper stripper = null;
			stripper = new PDFTextStripper();
			// 设置是否排序
			stripper.setSortByPosition(sort);
			// 设置起始页
			stripper.setStartPage(startPage);
			// 设置结束页
			stripper.setEndPage(endPage);
			// 调用PDFTextStripper的writeText提取并输出文本
			stripper.writeText(document, output);
		} finally {
			if (output != null) {
				// 关闭输出流
				output.close();
			}
			if (document != null) {
				// 关闭PDF Document
				document.close();
			}
		}
	}

	public static String readTxtFile(File fileName) throws Exception {
		String result = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);
			try {
				String read = null;
				while ((read = bufferedReader.readLine()) != null) {
					result = result + read + "\r\n";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (fileReader != null) {
				fileReader.close();
			}
		}

		return result;
	}
}