package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawler;
import app.service.crawler.telecom.htmlunit.LognAndGetBeijingService;
import app.service.crawler.telecom.htmlunit.LognAndGetBeijingUnitService;

/**
 * 
 * 项目名称：common-microservice-telecom-beijing 类名称：TelecomBeijongCrawlerService
 * 类描述： 创建人：hyx 创建时间：2018年3月18日 下午5:24:32
 * 
 * @version
 */
@Component
@EnableAsync
public class TelecomBeijongControllerService implements ICrawler{
	@Autowired
	private TelecomBeiJingCrawlerService telecomBeiJingCrawlerService;

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	
	@Autowired
	private LognAndGetBeijingService lognAndGetBeijingService;
	
	@Autowired
	private LognAndGetBeijingUnitService lognAndGetBeijingUnitService;
	
	@Async
	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = lognAndGetBeijingService.addcookie(webClient, taskMobile);
		webClient = lognAndGetBeijingUnitService.ready(webClient, 0);

		webClient = lognAndGetBeijingUnitService.readyForCallThrem(webClient, 0, messageLogin);
		String smsCode = lognAndGetBeijingUnitService.getSms(webClient, 0, messageLogin);
		webClient = lognAndGetBeijingUnitService.smsForCall(webClient, 0, messageLogin, smsCode);

		try {
			telecomBeiJingCrawlerService.getCallThrem(webClient, messageLogin, taskMobile,smsCode);// 成功
		} catch (Exception e) {
			tracerLog.output("parser.crawler.auth", "getCallThrem" + e.toString());
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

			taskMobile.setCallRecordStatus(StatusCodeRec.CRAWLER_CallRecordStatus_ERROR.getCode());
			taskMobileRepository.save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}

		try {
			telecomBeiJingCrawlerService.getSMSThrem(webClient, messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			tracerLog.output("parser.crawler.auth", "getSMSThrem" + e.toString());
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setSmsRecordStatus(StatusCodeRec.CRAWLER_SMSRecordStatus_ERROR.getCode());
			taskMobileRepository.save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}

//		try {
//			telecomCommonService.getPointsAndCharges(messageLogin, taskMobile);// 成功
//		} catch (Exception e) {
//			tracerLog.output("parser.crawler.auth", "getPointsAndCharges" + e.toString());
//		}

		try {
			telecomBeiJingCrawlerService.getUserInfo(messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			tracerLog.output("parser.crawler.auth", "getUserInfo" + e.toString());

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

			taskMobile.setUserMsgStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());

			taskMobileRepository.save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}

		try {
			telecomBeiJingCrawlerService.getpayResult(webClient, messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			tracerLog.output("parser.crawler.auth", "getpayResult" + e.toString());

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

			taskMobile.setPayMsgStatus(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getCode());

			taskMobileRepository.save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
		try {
			telecomBeiJingCrawlerService.getphoneBill(webClient, messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			tracerLog.output("parser.crawler.auth", "getphoneBill" + e.toString());
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setAccountMsgStatus(StatusCodeRec.CRAWLER_AccountMsgStatus_ERROR.getCode());
			taskMobile.setBusinessMsgStatus(StatusCodeRec.CRAWLER_BusinessMsgStatus_SUCESS.getCode());
			taskMobile.setFamilyMsgStatus(StatusCodeRec.CRAWLER_FamilyMsgStatus_SUCESS.getCode());
			taskMobileRepository.save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}

		try {
			telecomBeiJingCrawlerService.getChargesResult(webClient, messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			tracerLog.output("parser.crawler.auth", "getChargesResult" + e.toString());

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_Balance_ERROR.getCode());
			taskMobileRepository.save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}

		try {
			telecomBeiJingCrawlerService.getintegraResult(webClient, messageLogin, taskMobile);
		} catch (Exception e) {
			tracerLog.output("parser.crawler.auth", "getintegraResult" + e.toString());

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

			taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			taskMobileRepository.save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
		return taskMobile;
	}



	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
