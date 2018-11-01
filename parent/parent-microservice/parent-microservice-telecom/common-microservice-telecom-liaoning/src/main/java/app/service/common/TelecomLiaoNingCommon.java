package app.service.common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.CrawlerStatusMobileService;
import app.service.TelecomLiaoNingService;
import app.service.TelecomUnitLiaoNingService;
import app.service.aop.ICrawler;
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.liaoning")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.liaoning")
public class TelecomLiaoNingCommon implements ICrawler{

	@Autowired
	private TelecomLiaoNingService telecomLiaoNingService;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TelecomUnitLiaoNingService telecomUnitLiaoNingService;
	Map<String, Future<String>> listfuture = new HashMap<>();
	@Async
	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		tracer.addTag("@Async getAllData", messageLogin.toString());
		tracer.addTag("taskid",messageLogin.getTask_id());	
		TaskMobile taskMobile = telecomLiaoNingService.findtaskMobile(messageLogin.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		try {
			webClient = telecomUnitLiaoNingService.getInitMy189homeWebClient(messageLogin, taskMobile);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
		}
		
		/**
		 * 功能
		 */
		//客户信息
		try {
			Future<String> userInfo = telecomUnitLiaoNingService.getUserInfo(messageLogin,taskMobile); //成功
			listfuture.put("getUserInfo", userInfo);
		} catch (Exception e) {
			tracer.addTag("parser.crawler.auth", "getUserInfo" + e.toString());
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
		}
    	
		//账户余额信息
		try {
			Future<String> ChargesResult = telecomUnitLiaoNingService.getChargesResult(messageLogin,taskMobile,0); //成功
			listfuture.put("getChargesResult", ChargesResult);
		} catch (Exception e) {
			tracer.addTag("parser.crawler.auth", "getChargesResult" + e.toString());
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
		}
		
		//账户短信信息
		try {
			

			if(webClient == null){
			}
			for(int i = 0; i < 6; i++){
				try {
					Future<String> smsthrem = telecomUnitLiaoNingService.getSMSThrem(webClient, messageLogin, taskMobile, i);
					listfuture.put(i+"smsthrem", smsthrem);
				} catch (Exception e) {
					e.printStackTrace();
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
					continue;
				}
			
			}//成功
		} catch (Exception e) {
			tracer.addTag("parser.crawler.auth", "getSMSThrem" + e.toString());
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
		}
		
		//业务详情
		try {
			Future<String> getphoneschemes = telecomLiaoNingService.getphoneschemes(messageLogin,taskMobile);//成功
			listfuture.put("getphoneschemes", getphoneschemes);
		} catch (Exception e) {
			tracer.addTag("parser.crawler.auth", "getphoneschemes" + e.toString());
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
		}
		
		//积分
		try {
			Future<String> getpointValue = telecomLiaoNingService.getpointValue(messageLogin,taskMobile);//成功
			listfuture.put("getpointValue", getpointValue);
		} catch (Exception e) {
			tracer.addTag("parser.crawler.auth", "getpointValue" + e.toString());
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
		}
	   
		//月账单
		try {
			for(int i = 0; i < 6; i++){
				try {
					Future<String> getphoneBill = telecomUnitLiaoNingService.getphoneBill(messageLogin,taskMobile,i);
					listfuture.put(i+"getphoneBill", getphoneBill);
				} catch (Exception e) {
					e.printStackTrace();
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
					continue;
				}
				
			}
		} catch (Exception e) {
			tracer.addTag("parser.crawler.auth", "getphoneBill" + e.toString());
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
		}
		
		//缴费详单
		try {
			for(int i = 0; i < 6; i++){
				try {
					Future<String> payResult = telecomUnitLiaoNingService.getpayResult(webClient, messageLogin, taskMobile, i);
					listfuture.put(i+"getpayResult", payResult);
				} catch (Exception e) {
					e.printStackTrace();
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
					continue;
				}
				
			}
		} catch (Exception e) {
			tracer.addTag("parser.crawler.auth", "getpayThrem" + e.toString());
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
		}
		
		//账户通话信息
		try {
			for(int i = 0; i < 6; i++){
				try {
					Future<String> callThrem = telecomUnitLiaoNingService.getCallThrem(webClient, messageLogin, taskMobile, i);
					listfuture.put(i+"getCallThrem", callThrem);
				} catch (Exception e) {
					e.printStackTrace();
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
					continue;
				}
			
			}
		} catch (Exception e) {
			tracer.addTag("parser.crawler.auth", "getCallThrem" + e.toString());
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
		}
		System.out.println("1111");
		try {
			while (true) {
				for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
					if (entry.getValue().isDone()) { // 判断是否执行完毕
						tracer.addTag(taskMobile.getTaskid() + entry.getKey() + "---get", entry.getValue().get());
						tracer.addTag(taskMobile.getTaskid() + entry.getKey() + "---isDone", entry.getValue().get());
						listfuture.remove(entry.getKey());
						break;
					}
				}
				if (listfuture.size() == 0) {
					break;
				}
			}
			TaskMobile taskMobile2 = crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			return taskMobile2;
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("inTelecomjiangsuService-listfuture--ERROR", taskMobile.getTaskid());
			TaskMobile taskMobile2 = crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			return taskMobile2;
		}
		
	}
	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
