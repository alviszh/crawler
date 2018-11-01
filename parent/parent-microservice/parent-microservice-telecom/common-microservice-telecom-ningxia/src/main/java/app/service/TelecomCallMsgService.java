package app.service;

import java.net.URL;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.ningxia.TelecomNingxiaCallHistory;
import com.microservice.dao.entity.crawler.telecom.ningxia.TelecomNingxiaMessageHistory;
import com.microservice.dao.entity.crawler.telecom.ningxia.TelecomNingxiahtml;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.ningxia.TelecomNingxiaCallHistoryRepository;
import com.microservice.dao.repository.crawler.telecom.ningxia.TelecomNingxiaMessageHistoryRepository;
import com.microservice.dao.repository.crawler.telecom.ningxia.TelecomNingxiahtmlRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.ningxia")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.ningxia")
public class TelecomCallMsgService {
	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private TelecomNingxiahtmlRepository telecomNingxiahtmlRepository;

	@Autowired
	private TelecomNingxiaCallHistoryRepository telecomNingxiaCallHistoryRepository;

	@Autowired
	private TelecomNingxiaMessageHistoryRepository telecomNingxiaMessageHistoryRepository;

	@Autowired
	private TracerLog tracer;

	// 通话记录信息的爬取和采集
	public void crawlerCallMessageHistory(MessageLogin messageLogin, String monthFrist, String monthEnd) {
		System.out.println("通话记录信息的爬取和采集");
		tracer.addTag("service.crawlerCallMessageHistory.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());
		// 准备开始爬取通话记录信息
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_GATHER_CALL_CRAWLING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_GATHER_CALL_CRAWLING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_GATHER_CALL_CRAWLING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_GATHER_CALL_CRAWLING.getError_code());
		// 保存通话记录信息爬取时的状态
		save(taskMobile);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			// 爬取和解析 通话记录
			// 获得数据的接口
			String url = "http://nx.189.cn/bfapp/buffalo/CtQryService";
			String requestPayload = "<buffalo-call><method>qry_sj_yuyinfeiqingdan</method><string>" + monthFrist
					+ "</string><string>" + monthEnd + "</string></buffalo-call>";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setAdditionalHeader("Accept", "*/*");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
			webRequest.setAdditionalHeader("Host", "nx.189.cn");
			webRequest.setAdditionalHeader("Referer", "http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
			webRequest.setRequestBody(requestPayload);
			Page page = webClient.getPage(webRequest);
			String contentAsString = page.getWebResponse().getContentAsString();
			System.out.println(contentAsString);
			if ("<buffalo-reply><null></null></buffalo-reply>".equals(contentAsString)) {
				System.out.println("没有查到对应的数据！");
			} else {
				System.out.println("数据获取成功！");
				Document doc = Jsoup.parse(contentAsString);
				Elements link1 = doc.getElementsByTag("list");
				if (contentAsString.contains("<length>0</length>")) {
					System.out.println("当前月没有数据！");
				} else {
					TelecomNingxiahtml html = new TelecomNingxiahtml();
					html.setHtml(contentAsString);
					html.setPageCount(1);
					html.setTaskid(messageLogin.getTask_id());
					html.setType("通话记录信息");
					html.setUrl(url);
					telecomNingxiahtmlRepository.save(html);

					Elements elementsByTag = link1.get(0).getElementsByTag("map");
					for (Element element : elementsByTag) {
						TelecomNingxiaCallHistory telecomNingxiaCallHistory = new TelecomNingxiaCallHistory();

						Elements link2 = element.getElementsByTag("string");
						// 通信类型
						String correspondencetype = link2.get(17).text();
						System.out.println(correspondencetype);
						telecomNingxiaCallHistory.setCorrespondencetype(correspondencetype);
						// 通话时长
						String communicatetime = link2.get(11).text();
						System.out.println(communicatetime);
						telecomNingxiaCallHistory.setCommunicatetime(communicatetime);
						// 通话时间
						String communicatetime2 = link2.get(3).text();
						System.out.println(communicatetime2);
						telecomNingxiaCallHistory.setCommunicatetime2(communicatetime2);
						// 通话地点
						String communicateaddr = link2.get(13).text();
						System.out.println(communicateaddr);
						telecomNingxiaCallHistory.setCommunicateaddr(communicateaddr);
						// 对方号码
						String oppositephone = link2.get(7).text();
						System.out.println(oppositephone);
						telecomNingxiaCallHistory.setOppositephone(oppositephone);
						// 呼叫类型
						String calltype = link2.get(9).text();
						System.out.println(calltype);
						telecomNingxiaCallHistory.setCalltype(calltype);
						// 通话周期 目前和 通话时长 一样
						String callperiod = link2.get(3).text();
						System.out.println(callperiod);
						telecomNingxiaCallHistory.setCallperiod(callperiod);
						// 费用合计
						String costtotal = link2.get(15).text();
						System.out.println(costtotal);
						telecomNingxiaCallHistory.setCosttotal(costtotal);
						// taskid
						telecomNingxiaCallHistory.setTaskid(messageLogin.getTask_id());
						// 执行保存
						telecomNingxiaCallHistoryRepository.save(telecomNingxiaCallHistory);

					}
				}
			}
			// 语音信息采集完成
			taskMobile.setCallRecordStatus(200);
			taskMobile.setTaskid(messageLogin.getTask_id().trim());
			// 本类中的方法
			save(taskMobile);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//短信记录信息的爬取和采集
	public void crawlerSmsMessageHistory(MessageLogin messageLogin, String monthFrist, String monthEnd) {
		System.out.println("短信记录信息的爬取和采集");
		tracer.addTag("service.crawlerCallMessageHistory.taskid", messageLogin.getTask_id().trim());
		TaskMobile taskMobile = this.findtaskMobile(messageLogin.getTask_id().trim());
		// 准备开始爬取通话记录信息
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_GATHER_CALL_CRAWLING2.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_GATHER_CALL_CRAWLING2.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_GATHER_CALL_CRAWLING2.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_GATHER_CALL_CRAWLING2.getError_code());
		// 保存通话记录信息爬取时的状态
		save(taskMobile);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskMobile.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			// 解析和爬取短信记录信息
			// 获得数据的接口
			String urlMsg = "http://nx.189.cn/bfapp/buffalo/CtQryService";
			String requestPayloadMsg = "<buffalo-call><method>qry_sj_cxclxd</method><string>" + monthFrist
					+ "</string><string>" + monthEnd + "</string></buffalo-call>";
			WebRequest webRequestMsg = new WebRequest(new URL(urlMsg), HttpMethod.POST);
			webRequestMsg.setAdditionalHeader("Accept", "*/*");
			webRequestMsg.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequestMsg.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequestMsg.setAdditionalHeader("Connection", "keep-alive");
			webRequestMsg.setAdditionalHeader("Content-Type", "text/plain;charset=UTF-8");
			webRequestMsg.setAdditionalHeader("Host", "nx.189.cn");
			webRequestMsg.setAdditionalHeader("Referer", "http://nx.189.cn/jt/bill/xd/?fastcode=20000776&cityCode=nx");
			webRequestMsg.setRequestBody(requestPayloadMsg);
			Page pageMsg = webClient.getPage(webRequestMsg);
			String contentAsStringMsg = pageMsg.getWebResponse().getContentAsString();
			System.out.println(contentAsStringMsg);
			if ("<buffalo-reply><null></null></buffalo-reply>".equals(contentAsStringMsg)) {
				System.out.println("没有查到对应的数据！");
			} else {
				System.out.println("数据获取成功！");
				Document doc = Jsoup.parse(contentAsStringMsg);
				Elements link1 = doc.getElementsByTag("list");
				if (contentAsStringMsg.contains("<length>0</length>")) {
					System.out.println("当前月没有数据！");
				} else {
					
					TelecomNingxiahtml html = new TelecomNingxiahtml();
					html.setHtml(contentAsStringMsg);
					html.setPageCount(1);
					html.setTaskid(messageLogin.getTask_id());
					html.setType("短信记录信息");
					html.setUrl(urlMsg);
					telecomNingxiahtmlRepository.save(html);
					
					Elements elementsByTag = link1.get(0).getElementsByTag("map");
					for (Element element : elementsByTag) {
						TelecomNingxiaMessageHistory telecomNingxiaMessageHistory = new TelecomNingxiaMessageHistory();
						
						Elements link2 = element.getElementsByTag("string");
						// 发送时间
						String sendtime = link2.get(7).text();
						System.out.println(sendtime);
						telecomNingxiaMessageHistory.setSendtime(sendtime);
						// 对方号码
						String oppositephone = link2.get(5).text();
						System.out.println(oppositephone);
						telecomNingxiaMessageHistory.setOppositephone(oppositephone);
						// 信息类型
						String messagetype = link2.get(3).text();
						System.out.println(messagetype);
						telecomNingxiaMessageHistory.setMessagetype(messagetype);
						// 费用
						String messageaddr = link2.get(3).text();
						System.out.println(messageaddr);
						telecomNingxiaMessageHistory.setMessageaddr(messageaddr);
						// taskid
						telecomNingxiaMessageHistory.setTaskid(messageLogin.getTask_id());
						// 执行保存
						telecomNingxiaMessageHistoryRepository.save(telecomNingxiaMessageHistory);
					}
					
				}
			}
			// 短信信息采集完成
			taskMobile.setSmsRecordStatus(200);
			taskMobile.setTaskid(messageLogin.getTask_id().trim());
			// 本类中的方法
			save(taskMobile);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public TaskMobile findtaskMobile(String taskid) {
		return taskMobileRepository.findByTaskid(taskid);
	}

	public void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}
}