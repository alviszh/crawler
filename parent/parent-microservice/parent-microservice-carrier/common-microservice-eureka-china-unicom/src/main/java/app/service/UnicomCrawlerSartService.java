package app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog; 

@Component
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.unicom")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.unicom")
public class UnicomCrawlerSartService {

	public static final String KEY = "秘钥就是不告诉你，自己猜去吧哈哈";

	public static final Logger log = LoggerFactory.getLogger(UnicomCrawlerSartService.class);

	@Autowired
	LoginAndGetService loginAndGetService;

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private UnicomCrawlerService unicomCrawlerService;
	
	@Autowired
	private UnicomCrawlerAsyncService unicomCrawlerAsyncService;
	
	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;

	
	

	// 获取通话详单
	@Async
	public Object getCallThemhtml(MessageLogin messageLogin, TaskMobile taskMobile) {

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
//		if (taskMobile.getCallRecordStatus() != null) {
//			return null;
//		}
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);
		List<Future<String>> listfuture = new ArrayList<Future<String>>();

		for (int i = 0; i <= 6; i++) {
			try {
				Future<String> future = unicomCrawlerService.getCallThemhtml(messageLogin, taskMobile, i);
				listfuture.add(future);

			} catch (Exception e) {
				e.printStackTrace();
				tracerLog.addTag("parser.crawler.auth", e.getMessage());
			}
			
			try {
				Thread.sleep(5000);
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
		taskMobile.setCallRecordStatus(StatusCodeRec.CRAWLER_CallRecordStatus_SUCESS.getCode());
		save(taskMobile);

		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());

		return null;

	}

	// 获取历史详单
	public Object getHistoryThem(MessageLogin messageLogin, TaskMobile taskMobile) {

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

		if (taskMobile.getAccountMsgStatus() != null) {
			return null;
		}
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);

		List<Future<String>> listfuture = new ArrayList<Future<String>>();

		for (int i = 0; i <= 6; i++) {
			try {
				Future<String> future = unicomCrawlerAsyncService.getHistoryThem(messageLogin, taskMobile, i);

				listfuture.add(future);

			} catch (Exception e) {
				tracerLog.addTag("parser.crawler.auth", e.getMessage());
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

		taskMobile.setAccountMsgStatus(StatusCodeRec.CRAWLER_AccountMsgStatus_SUCESS.getCode());

		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return null;

	}

	// 获取积分详单
	public Object getIntegraThem(MessageLogin messageLogin, TaskMobile taskMobile) {

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

		if (taskMobile.getIntegralMsgStatus() != null) {
			return null;
		}
		long startTime = System.currentTimeMillis(); // 获取开始时间
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);

		List<Future<String>> listfuture = new ArrayList<Future<String>>();

		for (int i = 0; i <= 6; i++) {
			try {
				Future<String> future = unicomCrawlerAsyncService.getIntegraThem(messageLogin, taskMobile, i);
				listfuture.add(future);

			} catch (Exception e) {
				tracerLog.addTag("parser.crawler.auth", e.getMessage());
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

		taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_SUCESS.getCode());

		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());

		long endtime = System.currentTimeMillis(); // 获取开始时间

		tracerLog.addTag("耗费时间", (endtime - startTime) + "ms");
		return null;
	}

	// 获取积分详单2
	public Object getIntegraThem2(MessageLogin messageLogin, TaskMobile taskMobile) {

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

		if (taskMobile.getIntegralMsgStatus() != null) {
			return null;
		}
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);

		save(taskMobile);

		unicomCrawlerService.getIntegraThem2Production(messageLogin, taskMobile);
		unicomCrawlerService.getIntegraThem2Total(messageLogin, taskMobile);

		return null;
	}

	// 获取短信详单
	@Async
	public Object getNoteThem(MessageLogin messageLogin, TaskMobile taskMobile) {

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

		if (taskMobile.getSmsRecordStatus() != null) {
			return null;
		}
		long startTime = System.currentTimeMillis(); // 获取开始时间
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);
		List<Future<String>> listfuture = new ArrayList<Future<String>>();

		for (int i = 0; i <= 6; i++) {
			try {
				Future<String> future = unicomCrawlerAsyncService.getNoteThem(messageLogin, taskMobile, i);
				listfuture.add(future);

			} catch (Exception e) {
				tracerLog.addTag("parser.crawler.auth", e.getMessage());
			}
			
			try {
				Thread.sleep(5000);
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
		taskMobile.setSmsRecordStatus(StatusCodeRec.CRAWLER_SMSRecordStatus_SUCESS.getCode());
		taskMobile.setFamilyMsgStatus(StatusCodeRec.CRAWLER_FamilyMsgStatus_SUCESS.getCode());
		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		long endtime = System.currentTimeMillis(); // 获取开始时间

		tracerLog.addTag("耗费时间", (endtime - startTime) + "ms");
		return null;

	}

	// 获取缴费详单
	public Object getPayMsgStatusThem(MessageLogin messageLogin, TaskMobile taskMobile) {
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

		if (taskMobile.getPayMsgStatus() != null) {
			return null;
		}
		long startTime = System.currentTimeMillis(); // 获取开始时间
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);
		List<Future<String>> listfuture = new ArrayList<Future<String>>();

		for (int i = 0; i <= 6; i++) {
			try {
				Future<String> future = unicomCrawlerAsyncService.getPayMsgStatusThem(messageLogin, taskMobile, i);
				listfuture.add(future);

			} catch (Exception e) {
				tracerLog.addTag("parser.crawler.auth", e.getMessage());
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
		taskMobile.setPayMsgStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		long endtime = System.currentTimeMillis(); // 获取开始时间

		tracerLog.addTag("耗费时间", (endtime - startTime) + "ms");
		return null;
	}

	// 获取账户余额
	@Async
	public Object getBalanceThem(MessageLogin messageLogin, TaskMobile taskMobile) {

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

//		if (taskMobile.getUserMsgStatus() != null) {
//			return null;
//		}
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);

		unicomCrawlerService.getBalanceThem(messageLogin, taskMobile);

		return null;

	}

	// 获取用户详单
	@Async
	public Object getUserinfoThem(MessageLogin messageLogin, TaskMobile taskMobile) {

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

		if (taskMobile.getUserMsgStatus() != null) {
			return null;
		}
		long startTime = System.currentTimeMillis(); // 获取开始时间

		try {
			unicomCrawlerService.getUserInfoThem(messageLogin, taskMobile);
		} catch (Exception e) {
			e.printStackTrace();
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setUserMsgStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
			taskMobile.setBusinessMsgStatus(StatusCodeRec.CRAWLER_BusinessMsgStatus_ERROR.getCode());
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
		long endtime = System.currentTimeMillis(); // 获取开始时间

		tracerLog.addTag("耗费时间", (endtime - startTime) + "ms");
		return null;

	}

	public TaskMobile findtaskMobile(String taskid) {
		return taskMobileRepository.findByTaskid(taskid);
	}

	public void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}

}