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
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeClass;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.qinghai")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.qinghai")
public class TelecomQingHaiService {

	public static final Logger log = LoggerFactory.getLogger(TelecomQingHaiService.class);

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private TelecomUnitQingHaiService telecomUnitQingHaiService;

	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private TelecomUnitService telecomUnitService;

	// 青海用户积分生成记录
	@Async
	public String getintegraResult(MessageLogin messageLogin, TaskMobile taskMobile) {

		try {

			List<Future<String>> futures = new ArrayList<>();

			for (int i = 1; i < 6; i++) {
				Future<String> future = telecomUnitQingHaiService.getintegraResult(messageLogin, taskMobile, i);

				futures.add(future);

			}
			
			boolean istrue = true;
			while (istrue) { /// 这里使用了循环判断，等待获取结果信息
				for (Future<String> entry : futures) {

					if (entry.isDone()) { // 判断是否执行完毕
						futures.remove(entry);
						break;
					}
				}

				if (futures.size() <= 0) {
					System.out.println("is true");
					istrue = false;
				}

			}
			
			taskMobile.setIntegralMsgStatus(200);
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			
		} catch (Exception e) {
			e.printStackTrace();
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

			if (taskMobile.getIntegralMsgStatus() != null && taskMobile.getIntegralMsgStatus() != 200) {
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			} else {
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			}
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}

		

		return null;
	}

	// 获取缴费详单
	public Object getpayResult(MessageLogin messageLogin, TaskMobile taskMobile) {

		try {
			telecomUnitQingHaiService.getpayResult(messageLogin, taskMobile, 0);
		} catch (Exception e) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(0).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(0).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(0).getDescription());

			taskMobile.setPayMsgStatus(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getCode());

			taskMobile.setFamilyMsgStatus(StatusCodeRec.CRAWLER_FamilyMsgStatu_SUCESS.getCode());

			taskMobile.setBusinessMsgStatus(StatusCodeRec.CRAWLER_BusinessMsgStatus_SUCESS.getCode());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
		
		taskMobile.setPayMsgStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());

		taskMobile.setFamilyMsgStatus(StatusCodeRec.CRAWLER_FamilyMsgStatu_SUCESS.getCode());

		taskMobile.setBusinessMsgStatus(StatusCodeRec.CRAWLER_BusinessMsgStatus_SUCESS.getCode());
		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());

		return null;
	}

	// 获取账单详单
	public Object getBill(MessageLogin messageLogin, TaskMobile taskMobile) {

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);

		List<Future<String>> futures = new ArrayList<>();

		try{
			for (int i = 0; i < 6; i++) {
				try {
					Future<String> future = telecomUnitQingHaiService.getBill(messageLogin, taskMobile, i);

					futures.add(future);
				} catch (Exception e) {
					tracerLog.output("getbill 错误 第" + i + "个", e.getMessage());
				}

			}
		}catch(Exception e){
			e.printStackTrace();
			
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(0).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(0).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(0).getDescription());

			taskMobile.setAccountMsgStatus(StatusCodeRec.CRAWLER_AccountMsgStatus_ERROR.getCode());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			
			return null;

		}
		
		boolean istrue = true;
		while (istrue) { /// 这里使用了循环判断，等待获取结果信息
			for (Future<String> entry : futures) {

				if (entry.isDone()) { // 判断是否执行完毕
					futures.remove(entry);
					break;
				}
			}

			if (futures.size() <= 0) { 
				System.out.println("bill   is true");
				istrue = false;
			}

		}
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(0).getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(0).getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(0).getDescription());

		taskMobile.setAccountMsgStatus(StatusCodeRec.CRAWLER_AccountMsgStatus_SUCESS.getCode());
		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());


		return null;
	}

	// 获取通话详单
	public Object getphoneBill(MessageLogin messageLogin, TaskMobile taskMobile) {

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);
		
		try{
			WebClient webClient = telecomUnitService.getWebClientForTeleComQingHai(taskMobile);
			List<Future<String>> futures = new ArrayList<>();
			for (int i = 0; i < 6; i++) {
				try {
					Future<String> future = telecomUnitQingHaiService.getPhoneBillResult(webClient, messageLogin,
							taskMobile, i);

					futures.add(future);
				} catch (Exception e) {
					tracerLog.output("getphoneBill 错误 第" + i + "个", e.getMessage());
				}

			}
			boolean istrue = true;
			while (istrue) { /// 这里使用了循环判断，等待获取结果信息
				for (Future<String> entry : futures) {
					if (entry.isDone()) { // 判断是否执行完毕
						futures.remove(entry);
						break;
					}
				}

				if (futures.size() <= 0) {
					istrue = false;
				}

			}

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

			taskMobile.setCallRecordStatus(200);
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}catch(Exception e){
			e.printStackTrace();
		}

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

		taskMobile.setCallRecordStatus(200);
		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		

		return null;
	}

	// 获取短信详单
	public Object getSMSBill(MessageLogin messageLogin, TaskMobile taskMobile) {

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);
		try {
			WebClient webClient = telecomUnitService.getWebClientForTeleComQingHai(taskMobile);

			List<Future<String>> futures = new ArrayList<>();
			int errori = 404;

			for (int i = 0; i < 6; i++) {

				try {
					Future<String> future = telecomUnitQingHaiService.getSMSBill(webClient, messageLogin, taskMobile,
							i);
					futures.add(future);
				} catch (Exception e) {
					tracerLog.output("getSMSBill 错误 第" + i + "个", e.getMessage());
				}

			}

			boolean istrue = true;
			while (istrue) { /// 这里使用了循环判断，等待获取结果信息
				for (Future<String> entry : futures) {

					if (entry.isDone()) { // 判断是否执行完毕
						futures.remove(entry);
						break;
					}
				}

				if (futures.size() <= 0) {
					System.out.println("is true");
					istrue = false;
				}

			}
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

			taskMobile.setSmsRecordStatus(errori);
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());

			return null;
		} catch (Exception e) {
			e.printStackTrace();

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

			taskMobile.setSmsRecordStatus(404);
			save(taskMobile);

			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}

	}

	/**
	 * 获取用户信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	public Object getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile) {

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);

		try {
			telecomUnitQingHaiService.getUserInfo(messageLogin, taskMobile);
		} catch (Exception e) {
			e.printStackTrace();

			tracerLog.output("userinfo error", e.getMessage());

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_USER_MSG().getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_USER_MSG().getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_USER_MSG().getDescription());

			taskMobile.setUserMsgStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

		taskMobile.setUserMsgStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());


		return null;
	}

	public void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}

	public TaskMobile findtaskMobile(String taskid) {
		return taskMobileRepository.findByTaskid(taskid);
	}
}