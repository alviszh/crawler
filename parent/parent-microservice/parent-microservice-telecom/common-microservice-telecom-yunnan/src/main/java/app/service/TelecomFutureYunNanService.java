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
import com.crawler.mobile.json.StatusCodeClass;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.bean.TelecomYunNanCanShuAccidBean;
import app.bean.TelecomYunNanCanUserIdShuBean;
import app.commontracerlog.TracerLog;
import app.crawler.telecomhtmlunit.LognAndGetYunNanUnit;
import app.service.common.TelecomBasicService;

@Component
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.yunnan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.yunnan")
public class TelecomFutureYunNanService extends TelecomBasicService {

	public static final Logger log = LoggerFactory.getLogger(TelecomFutureYunNanService.class);


	@Autowired
	private TelecomAsyncYunNaniService telecomAsyncYunNaniService;

	@Autowired
	private TracerLog tracerLog;

	// 用户积分生成记录
	@Async
	public String getintegraChangeResult(MessageLogin messageLogin, TaskMobile taskMobile) {
		log.info("====================爬取结果集===========================");

		try {
			TelecomYunNanCanUserIdShuBean telecomYunNanCanUserIdShuBean = LognAndGetYunNanUnit
					.getUserIdunit(messageLogin, taskMobile);
			telecomAsyncYunNaniService.getintegraChangeResult(messageLogin, taskMobile, telecomYunNanCanUserIdShuBean,
					0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**余额
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Object getBalance(MessageLogin messageLogin, TaskMobile taskMobile) {

		taskMobile = findtaskMobile(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);
		log.info("==============爬取详单=========");
		TelecomYunNanCanShuAccidBean telecomYunNanCanShuAccidBean = LognAndGetYunNanUnit.getAccid(messageLogin,
				taskMobile);

		Future<String> future = telecomAsyncYunNaniService.getbalance(messageLogin, taskMobile,
				telecomYunNanCanShuAccidBean);
		boolean istrue = true;
		while (istrue) { /// 这里使用了循环判断，等待获取结果信息

			if (future.isDone()) { // 判断是否执行完毕
				System.out.println("Result getBalance - " + future.toString() + ":::" + future.isDone());	
				istrue = false;
				break;
			}

		}
		taskMobile = findtaskMobile(taskMobile.getTaskid());
		taskMobile.setPayMsgStatus(StatusCodeRec.CRAWLER_Balance_SUCESS.getCode());

		taskMobile.setFamilyMsgStatus(StatusCodeRec.CRAWLER_FamilyMsgStatu_SUCESS.getCode());
		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return null;
	}
	
	/**获取套餐信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Object getBussiness(MessageLogin messageLogin, TaskMobile taskMobile) {

		taskMobile = findtaskMobile(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);
		log.info("==============爬取详单=========");

		Future<String> future = telecomAsyncYunNaniService.getBussiness(messageLogin, taskMobile);
		boolean istrue = true;

		while (istrue) { /// 这里使用了循环判断，等待获取结果信息

			if (future.isDone()) { // 判断是否执行完毕
				System.out.println("Result getBussiness - " + future.toString() + ":::" + future.isDone());	
				istrue = false;
				break;
			}

		}
		taskMobile = findtaskMobile(taskMobile.getTaskid());
		taskMobile.setBusinessMsgStatus(StatusCodeRec.CRAWLER_BusinessMsgStatus_SUCESS.getCode());

		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return null;
	}
	

	// 获取缴费详单（完成）
	public Object getpayResult(MessageLogin messageLogin, TaskMobile taskMobile) {

		taskMobile = findtaskMobile(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);
		log.info("==============爬取详单=========");
		TelecomYunNanCanUserIdShuBean telecomYunNanCanUserIdShuBean = LognAndGetYunNanUnit.getUserIdunit(messageLogin,
				taskMobile);

		Future<String> future = telecomAsyncYunNaniService.getpayResult(messageLogin, taskMobile,
				telecomYunNanCanUserIdShuBean);

		boolean istrue = true;
		while (istrue) { /// 这里使用了循环判断，等待获取结果信息
			if (future.isDone()) { // 判断是否执行完毕
				System.out.println("Result getpayResult - " + future.toString() + ":::" + future.isDone());	
				istrue = false;
				break;
			}

		}
		taskMobile = findtaskMobile(taskMobile.getTaskid());
		taskMobile.setPayMsgStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());

		taskMobile.setFamilyMsgStatus(StatusCodeRec.CRAWLER_FamilyMsgStatu_SUCESS.getCode());
		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return null;
	}

	// 获取账单详单（完成）
	@Async
	public Object getBill(MessageLogin messageLogin, TaskMobile taskMobile) {

		taskMobile = findtaskMobile(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);
		log.info("==============爬取详单=========");
		
		TelecomYunNanCanUserIdShuBean telecomYunNanCanUserIdShuBean = LognAndGetYunNanUnit.getUserIdunit(messageLogin,
				taskMobile);
		List<Future<String>> listfuture = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			Future<String> future = telecomAsyncYunNaniService.getBill(messageLogin, taskMobile,
					telecomYunNanCanUserIdShuBean, i);

			listfuture.add(future);
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
		taskMobile = findtaskMobile(taskMobile.getTaskid());

	    taskMobile.setAccountMsgStatus(StatusCodeRec.CRAWLER_AccountMsgStatus_SUCESS.getCode());
		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return null;
	}

	// 获取通话详单
	public Object getphoneBill(WebClient webClient,MessageLogin messageLogin, TaskMobile taskMobile) {
		List<Future<String>> listfuture = new ArrayList<>();
		taskMobile = findtaskMobile(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);
		try {

			for (int i = 0; i < 6; i++) {
				Future<String> future = telecomAsyncYunNaniService.getPhoneBill(webClient, messageLogin, taskMobile, i);
				listfuture.add(future);
				Thread.sleep(5000);
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
			taskMobile = findtaskMobile(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CALL_MSG(0).getPhase());

			taskMobile.setCallRecordStatus(StatusCodeRec.CRAWLER_CallRecordStatus_SUCESS.getCode());

			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			tracerLog.addTag("中国电信抓取客户用户   通话详单", "<xmp>" + e.getMessage() + "</xmp>");
			taskMobile = findtaskMobile(taskMobile.getTaskid());

			if (taskMobile.getCallRecordStatus() != null && taskMobile.getCallRecordStatus() != 200) {
				taskMobile.setCallRecordStatus(StatusCodeRec.CRAWLER_CallRecordStatus_ERROR.getCode());
			} else {
				taskMobile.setCallRecordStatus(StatusCodeRec.CRAWLER_CallRecordStatus_ERROR.getCode());
			}
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}

		// telecomUnitQingHaiService.getPhoneBillResult(webClient, messageLogin,
		// taskMobile, 1);
		log.info("==============爬取详单=========");
		return null;
	}

	// 获取短信详单
	public Object getSMSBill(WebClient webClient,MessageLogin messageLogin, TaskMobile taskMobile) {
		List<Future<String>> listfuture = new ArrayList<>();

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);
		try {
			
			for (int i = 0; i < 6; i++) {
				Future<String> future = telecomAsyncYunNaniService.getSMSBill(webClient, messageLogin, taskMobile, i);
				listfuture.add(future);
				Thread.sleep(5000);
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

			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			tracerLog.addTag("中国电信抓取客户用户   通话详单", "<xmp>" + e.getMessage() + "</xmp>");
			taskMobile = findtaskMobile(taskMobile.getTaskid());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}

		log.info("==============爬取详单========="); //
		return null;
	}

	/**
	 * 获取用户信息（完成）
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Object getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile) {

		taskMobile = findtaskMobile(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);

		Future<String> future = telecomAsyncYunNaniService.getUserInfo(messageLogin, taskMobile);

		boolean istrue = true;
		while (istrue) { /// 这里使用了循环判断，等待获取结果信息

			if (future.isDone()) { // 判断是否执行完毕
				System.out.println("Result getUserInfo - " + future.toString() + ":::" + future.isDone());
				istrue = false;
				break;
			}

		}
		taskMobile = findtaskMobile(taskMobile.getTaskid());
		taskMobile.setUserMsgStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return null;
	}

	

}