package app.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;


@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.liaoning")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.liaoning")
public class TelecomLiaoNingService {
	public static final Logger log = LoggerFactory.getLogger(TelecomLiaoNingService.class);
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TelecomUnitLiaoNingService telecomUnitLiaoNingService;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	
	Map<String, Future<String>> listfuture = new HashMap<>();
	//基本信息
	@Async
	public Future<String> getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile)  {
		log.info("====================爬取结果集===========================");

		tracer.addTag("中国电信抓取客户信息", messageLogin.getTask_id());

		try {
			Future<String> userInfo = telecomUnitLiaoNingService.getUserInfo(messageLogin,taskMobile);
			System.out.println(userInfo.toString());;
			return userInfo;
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.crawler.auth", "getUserInfo"+e.toString());
		}

		return new AsyncResult<String>("200");
	}

	//余额
	@Async
	public Future<String> getChargesResult(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		log.info("==============爬取余额=========");
		Future<String> ChargesResult = telecomUnitLiaoNingService.getChargesResult(messageLogin, taskMobile, 0);
		return ChargesResult; 
	}


	//通话记录
	@Async
	public Map<String, Future<String>> getCallThrem(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {

		log.info("==============爬取详单=========");

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = telecomUnitLiaoNingService.getInitMy189homeWebClient(messageLogin, taskMobile);

		if(webClient == null){
			return listfuture;
		}
		//webClient = LoginAndGetLiaoNing.addcoolieByLiaoNing(webClient);

		for(int i = 0; i < 6; i++){
			try {
				Future<String> callThrem = telecomUnitLiaoNingService.getCallThrem(webClient, messageLogin, taskMobile, i);
				listfuture.put(i+"getCallThrem", callThrem);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		
		}
		return listfuture;

	}

	//缴费详单
	@Async
	public Map<String, Future<String>> getpayThrem(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		
		log.info("==============爬取详单=========");

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = telecomUnitLiaoNingService.getInitMy189homeWebClient(messageLogin, taskMobile);

		if(webClient == null){
			return null;
		}
		//webClient = LoginAndGetLiaoNing.addcoolieByLiaoNing(webClient);

		for(int i = 0; i < 6; i++){
			try {
				Future<String> payResult = telecomUnitLiaoNingService.getpayResult(webClient, messageLogin, taskMobile, i);
				listfuture.put(i+"getpayResult", payResult);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
		}
		
		return listfuture;
	}

	//话费详单
	@Async
	public Map<String, Future<String>> getphoneBill(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {

		log.info("==============爬取详单=========");
		Future<String> phoneBill = new AsyncResult<String>("200");
		for(int i = 0; i < 6; i++){
			try {
				phoneBill = telecomUnitLiaoNingService.getphoneBill(messageLogin,taskMobile,i);
				listfuture.put(i+"getphoneBill", phoneBill);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			try {
				Thread.sleep(5000);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		

		return listfuture;

	}

	//短信
	@Async
	public Map<String, Future<String>> getSMSThrem(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {

		log.info("==============爬取详单=========");

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = telecomUnitLiaoNingService.getInitMy189homeWebClient(messageLogin, taskMobile);

		if(webClient == null){
			return null;
		}
		//webClient = LoginAndGetLiaoNing.addcoolieByLiaoNing(webClient);
		Future<String> smsThrem =new AsyncResult<String>("200");
		for(int i = 0; i < 6; i++){
			try {
				smsThrem = telecomUnitLiaoNingService.getSMSThrem(webClient, messageLogin,taskMobile, i);
				listfuture.put("getSMSThrem", smsThrem);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			try {
				Thread.sleep(5000);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return listfuture;
	}
	//套餐详情
	@Async
	public Future<String> getphoneschemes(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {

		log.info("==============爬取详单=========");

		Future<String> getphoneschemes = telecomUnitLiaoNingService.getphoneschemes(messageLogin,taskMobile,0);
		return getphoneschemes;

	}

	//积分
	@Async
	public Future<String> getpointValue(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {

		log.info("==============爬取积分=========");
		Future<String> getpointValue = telecomUnitLiaoNingService.getpointValue(messageLogin,taskMobile,0);
		return getpointValue;
	}


	public void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}
	public TaskMobile findtaskMobile(String taskid) {

		return taskMobileRepository.findByTaskid(taskid);
	}
	
}
