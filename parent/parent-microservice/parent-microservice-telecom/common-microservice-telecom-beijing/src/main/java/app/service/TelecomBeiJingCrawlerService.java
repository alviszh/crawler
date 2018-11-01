package app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;

@Component
@EnableAsync
public class TelecomBeiJingCrawlerService {

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private TelecomUnitBeiJingService telecomUnitBeiJingService;

	@Autowired
	private TelecomBeiJingPageService telecomBeiJingPageService;
	
	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;

	public String getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile) {

		telecomUnitBeiJingService.getUserInfo(messageLogin, taskMobile, 0);

		return null;
	}

	// 获取通话详单
	@Async
	public TaskMobile getCallThrem(WebClient webClient,MessageLogin messageLogin, TaskMobile taskMobile,String smsCode) {

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);

		if (webClient == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

			taskMobile.setCallRecordStatus(StatusCodeRec.CRAWLER_CallRecordStatus_ERROR.getCode());
			tracerLog.output("getCallThrem  taskMobile", taskMobile.toString());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}
		
		List<Future<String>> listfuture = new ArrayList<Future<String>>();

		for (int i = 0; i <= 6; i++) {
			try {
				Future<String> future = telecomBeiJingPageService.getcallbypage(webClient, messageLogin, taskMobile, i,smsCode);
				listfuture.add(future);

			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		boolean istrue = true;
		while (istrue) { /// 这里使用了循环判断，等待获取结果信息
			for (Future<String> future : listfuture) {

				if (future.isDone()) { // 判断是否执行完毕
					listfuture.remove(future);
					break;
				}
				
			}
			if (listfuture.size() <= 0) {
				istrue = false;
			}
		}

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

		if (taskMobile.getCallRecordStatus() == null) {
			taskMobile.setCallRecordStatus(StatusCodeRec.CRAWLER_CallRecordStatus_SUCESS.getCode());
		}
		save(taskMobile);
		tracerLog.output("getCallThrem  taskMobile", taskMobile.toString());
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return taskMobile;
	}

	

	// 获取短信详单
	public Object getSMSThrem(WebClient webClient,MessageLogin messageLogin, TaskMobile taskMobile) {

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);

		if (webClient == null) {
			return null;
		}
		for (int i = 0; i < 6; i++) {
			try {
				telecomBeiJingPageService.getSMSThrembypage(webClient, messageLogin, taskMobile, i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setSmsRecordStatus(StatusCodeRec.CRAWLER_SMSRecordStatus_SUCESS.getCode());
		save(taskMobile);
		tracerLog.output("getSMSThrem  taskMobile", taskMobile.toString());
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return null;
	}

	// 获取话费详单
	@Async
	public Object getphoneBill(WebClient webClient,MessageLogin messageLogin, TaskMobile taskMobile) {

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);

		telecomUnitBeiJingService.getphoneBill(webClient,messageLogin, taskMobile, 0);

		return null;
	}

	// 获取缴费详单
	@Async
	public Object getpayResult(WebClient webClient,MessageLogin messageLogin, TaskMobile taskMobile) {

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);
		for (int i = 0; i < 6; i++) {
			try {
				telecomUnitBeiJingService.getpayResult( webClient,messageLogin, taskMobile, i);
			} catch (Exception e) {
				continue;
			}
		}

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

		taskMobile.setPayMsgStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());

		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return null;
	}

	// 获取话费余额
	@Async
	public Object getChargesResult(WebClient webClient,MessageLogin messageLogin, TaskMobile taskMobile) {

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);

		telecomUnitBeiJingService.getChargesResult( webClient,messageLogin, taskMobile, 0);

		return null;
	}

	// 获取积分变化
	public Object getintegraResult(WebClient webClient,MessageLogin messageLogin, TaskMobile taskMobile) {

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);

		telecomUnitBeiJingService.getintegraResult( webClient,messageLogin, taskMobile, 0);

		return null;
	}

	public boolean isDoing(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		tracerLog.output("正在进行上次未完成的爬取任务。。。", taskMobile.toString());
		if ("CRAWLER".equals(taskMobile.getPhase()) && "DOING".equals(taskMobile.getPhase_status())) {
			return true;
		}
		return false;
	}

	public void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}

	public TaskMobile findtaskMobile(String taskid) {
		return taskMobileRepository.findByTaskid(taskid);
	}
}