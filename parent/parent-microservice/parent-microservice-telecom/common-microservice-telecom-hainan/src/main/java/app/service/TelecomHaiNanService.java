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
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.bean.TelecomHaiNanUserIdBean;
import app.service.common.TelecomBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.hainan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.hainan")
public  class TelecomHaiNanService extends TelecomBasicService {

	public static final Logger log = LoggerFactory.getLogger(TelecomHaiNanService.class);

	@Autowired
	private TelecomCrawlerAnsycHaiNanService telecomCrawlerAnsycHaiNanService;


	// 用户积分生成记录
	@Async
	public String getintegraChangeResult(MessageLogin messageLogin, TaskMobile taskMobile,
			TelecomHaiNanUserIdBean telecomHaiNanUserIdBean) {
		log.info("====================爬取结果集===========================");

		try {

			List<Future<String>> listfuture = new ArrayList<>();
			for (int i = 0; i < 6; i++) {
				Future<String> future = telecomCrawlerAnsycHaiNanService.getintegraChangeResult(messageLogin, taskMobile,
						telecomHaiNanUserIdBean, i);
				listfuture.add(future);
			}

			boolean istrue = true;
			while (istrue) { /// 这里使用了循环判断，等待获取结果信息
				if(listfuture.size()<=0){
					istrue = false;
				}
				for (Future<String> future : listfuture) {
					
					if (future.isDone()) { // 判断是否执行完毕
						listfuture.remove(future);
						System.out.println(
								"Result getintegraChangeResult - " + future.toString() + ":::" + future.isDone());
						break;
					}
				}
			}
			taskMobile = findtaskMobile(taskMobile.getTaskid());
			taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_SUCESS.getCode());

			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 余额
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Object getBalance(MessageLogin messageLogin, TaskMobile taskMobile,
			TelecomHaiNanUserIdBean telecomHaiNanUserIdBean) {

		taskMobile = findtaskMobile(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);
		log.info("==============爬取详单=========");

		List<Future<String>> listfuture = new ArrayList<>();
		//for (int i = 0; i < 6; i++) {
			Future<String> future = telecomCrawlerAnsycHaiNanService.getbalance(messageLogin, taskMobile, 0,
					telecomHaiNanUserIdBean);
			listfuture.add(future);
		//}

		boolean istrue = true;
		while (istrue) { /// 这里使用了循环判断，等待获取结果信息

				if (future.isDone()) { // 判断是否执行完毕
					listfuture.remove(future);

					System.out.println("Result getBalance - " + future.toString() + ":::" + future.isDone());
					istrue = false;
					break;
				}
			
		}
		taskMobile = findtaskMobile(taskMobile.getTaskid());

		taskMobile.setFamilyMsgStatus(StatusCodeRec.CRAWLER_FamilyMsgStatu_SUCESS.getCode());
		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return null;
	}

	/**
	 * 获取套餐信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Object getBussiness(MessageLogin messageLogin, TaskMobile taskMobile,
			TelecomHaiNanUserIdBean telecomHaiNanUserIdBean) {

		taskMobile = findtaskMobile(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);
		log.info("==============爬取详单=========");

		Future<String> future = telecomCrawlerAnsycHaiNanService.getBussiness(messageLogin, taskMobile,
				telecomHaiNanUserIdBean);

		boolean istrue = true;
		while (istrue) { /// 这里使用了循环判断，等待获取结果信息}
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
	@Async
	public Object getpayResult(MessageLogin messageLogin, TaskMobile taskMobile,
			TelecomHaiNanUserIdBean telecomHaiNanUserIdBean) {

		taskMobile = findtaskMobile(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);
		log.info("==============爬取详单=========");
		List<Future<String>> listfuture = new ArrayList<>();

		for (int i = 0; i < 6; i++) {
			Future<String> future = telecomCrawlerAnsycHaiNanService.getpayResult(messageLogin, taskMobile, i,
					telecomHaiNanUserIdBean);

			listfuture.add(future);
		}
		boolean istrue = true;
		while (istrue) { /// 这里使用了循环判断，等待获取结果信息
			if(listfuture.size()<=0){
				istrue = false;
			}
			for (Future<String> future : listfuture) {
				
				if (future.isDone()) { // 判断是否执行完毕
					listfuture.remove(future);
					System.out.println("Result getBill - " + future.toString() + ":::" + future.isDone());
					break;
				}
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
	public Object getBill(MessageLogin messageLogin, TaskMobile taskMobile,
			TelecomHaiNanUserIdBean telecomHaiNanUserIdBean) {

		taskMobile = findtaskMobile(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
		taskMobile.setError_message(null);
		save(taskMobile);
		log.info("==============爬取详单=========");

		List<Future<String>> listfuture = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			Future<String> future = telecomCrawlerAnsycHaiNanService.getBill(messageLogin, taskMobile, telecomHaiNanUserIdBean,
					i);

			listfuture.add(future);
		}
		boolean istrue = true;
		int i = 0;
		while (istrue) { /// 这里使用了循环判断，等待获取结果信息
			if(listfuture.size()<=0){
				istrue = false;
			}
			for (Future<String> future : listfuture) {
				
				if (future.isDone()) { // 判断是否执行完毕
					System.out.println("Result getBill - " + future.toString() + ":::" + future.isDone());
					i++;
					listfuture.remove(future);
					break;
				}
			}
		}
		taskMobile = findtaskMobile(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());

		taskMobile.setAccountMsgStatus(StatusCodeRec.CRAWLER_AccountMsgStatus_SUCESS.getCode());
		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return null;
	}

	// 获取通话详单
	public Object getphoneBill(MessageLogin messageLogin, TaskMobile taskMobile,
			TelecomHaiNanUserIdBean telecomHaiNanUserIdBean) {
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
				Future<String> future = telecomCrawlerAnsycHaiNanService.getPhoneBill(telecomHaiNanUserIdBean, messageLogin,
						taskMobile, i);
				listfuture.add(future);
			}
			boolean istrue = true;
			int i = 0;
			while (istrue) { /// 这里使用了循环判断，等待获取结果信息
				if(listfuture.size()<=0){
					istrue = false;
				}
				for (Future<String> future : listfuture) {
					
					if (future.isDone()) { // 判断是否执行完毕
						listfuture.remove(future);
						System.out.println(
								"Result getphoneBill - " + i + ":" + future.toString() + ":::" + future.isDone());
						i++;
						listfuture.remove(future);

						break;
					}
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
	public Object getSMSBill(TelecomHaiNanUserIdBean telecomHaiNanUserIdBean, MessageLogin messageLogin,
			TaskMobile taskMobile) {
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
				Future<String> future = telecomCrawlerAnsycHaiNanService.getSMSBill(telecomHaiNanUserIdBean, messageLogin,
						taskMobile, i);
				listfuture.add(future);
			}

			boolean istrue = true;
			int i = 0;
			while (istrue) { /// 这里使用了循环判断，等待获取结果信息
				if(listfuture.size()<=0){
					istrue = false;
				}
				for (Future<String> future : listfuture) {
					if (future.isDone()) { // 判断是否执行完毕
						listfuture.remove(future);
						System.out.println(
								"Result getSMSBill - " + i + ":" + future.toString() + ":::" + future.isDone());
						i++;
						break;
					}

				}
			}

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

			taskMobile.setSmsRecordStatus(StatusCodeRec.CRAWLER_SMSRecordStatus_SUCESS.getCode());

			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 200,
					StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
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

		Future<String> future = telecomCrawlerAnsycHaiNanService.getUserInfo(messageLogin, taskMobile);

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