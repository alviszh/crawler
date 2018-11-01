package app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.mobile.MobileDataErrRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.MobileDataErrRecRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.impl.CrawlerImpl; 

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.mobile"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.mobile"})
public class CrawlerStatusMobileService{ 

	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private MobileDataErrRecRepository mobileDataErrRecRepository;
	@Autowired
	private CrawlerImpl crawlerImpl;
	
	@Autowired
	private TracerLog tracerLog;

	/**
	 * @Description 爬取完成后更新taskmobile
	 * @param taskId
	 */
	public TaskMobile updateTaskMobile(String taskId) {

		tracerLog.addTag("updateTaskMobile", "CrawlerStatusMobileService updateTaskMobile");
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(taskId);
		
		tracerLog.addTag("updateTaskMobile taskMobile to String", taskMobile.toString());
		if (null != taskMobile.getUserMsgStatus() && null != taskMobile.getAccountMsgStatus()
				&& null != taskMobile.getBusinessMsgStatus()
				&& null != taskMobile.getCallRecordStatus() && null != taskMobile.getFamilyMsgStatus()
				&& null != taskMobile.getIntegralMsgStatus() && null != taskMobile.getPayMsgStatus()
				&& null != taskMobile.getSmsRecordStatus()) {
						
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_SUCCESS.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_SUCCESS.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_SUCCESS.getDescription());
			taskMobile.setFinished(true);
			taskMobile = taskMobileRepository.save(taskMobile);
			tracerLog.addTag("updateTaskMobile", "updateTaskMobile success");
			
			//add by meidi  20180718
			crawlerImpl.getAllDataDone(taskId);
			
		}else{ 
			tracerLog.addTag("updateTaskMobile", "此时还有数据没有爬取完成，故暂未更新最终爬取状态"); 
		}
		
		return taskMobile;

	}
	/**
	 * @param description
	 * @param phase
	 * @param phaseStatus
	 * @param code
	 * @param taskMobile
	 * @return
	 * @return
	 * @Des 改变taskMobile表状态
	 */
	@Transactional(isolation = Isolation.REPEATABLE_READ)//这种事务隔离级别可以防止脏读，不可重复读。但是可能出现幻像读。
	public TaskMobile changeCrawlerStatus(String description, String phase, Integer code,
			String taskId) {
		
		tracerLog.addTag("changeCrawlerStatus", "CrawlerStatusMobileService changeCrawlerStatus");
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(taskId);
		tracerLog.addTag("changeCrawlerStatus.begin", taskMobile.toString());
		
		taskMobile.setDescription(description);
		if (phase.equals(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getPhase())) {
			taskMobile.setUserMsgStatus(code);
		}
		if (phase.equals(StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getPhase())) {
			taskMobile.setAccountMsgStatus(code);
		}
		if (phase.equals(StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getPhase())) {
			taskMobile.setSmsRecordStatus(code);
		}
		if (phase.equals(StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getPhase())) {
			taskMobile.setCallRecordStatus(code);
		}
		if (phase.equals(StatusCodeEnum.TASKMOBILE_CRAWLER_CHECK_MSG_SUCCESS.getPhase())) {
			taskMobile.setPayMsgStatus(code);
		}
		if (phase.equals(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getPhase())) {
			taskMobile.setPayMsgStatus(code);
		}
		if (phase.equals(StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getPhase())) {
			taskMobile.setBusinessMsgStatus(code);
		}
		if (phase.equals(StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getPhase())) {
			taskMobile.setIntegralMsgStatus(code);
		}		
		taskMobile = taskMobileRepository.save(taskMobile);
		
		tracerLog.addTag("changeCrawlerStatus.end", taskMobile.toString());
		return taskMobile;
	}
	
	

	public void updateUserMsgStatus(String taskId, Integer status,String description) {
		taskMobileRepository.updateUserMsgStatus(taskId, status,description);
//		updateTaskMobile(taskId);
	}

	public void updateAccountMsgStatus(String taskId, Integer status,String description) {
		taskMobileRepository.updateAccountMsgStatus(taskId, status,description);
//		updateTaskMobile(taskId);
	}

	public void updateSMSRecordStatus(String taskId, Integer status,String description) {
		taskMobileRepository.updateSMSRecordStatus(taskId, status,description);
//		updateTaskMobile(taskId);
	}

	public void updateBusinessMsgStatus(String taskId, Integer status,String description) {
		taskMobileRepository.updateBusinessMsgStatus(taskId, status,description);
//		updateTaskMobile(taskId);
	}

	public void updatePayMsgStatus(String taskId, Integer status,String description) {
		taskMobileRepository.updatePayMsgStatus(taskId, status,description);
//		updateTaskMobile(taskId);
	}


	public void updateFamilyMsgStatus(String taskId, Integer status,String description) {
		taskMobileRepository.updateFamilyMsgStatus(taskId, status,description);
//		updateTaskMobile(taskId);
	}
	
	public void updateIntegralMsgStatus(String taskId, Integer status,String description) {
		taskMobileRepository.updateIntegralMsgStatus(taskId, status,description);
//		updateTaskMobile(taskId);
	}
	
	public void updateCallRecordStatus(String taskId, Integer status,String description) {
		taskMobileRepository.updateCallRecordStatus(taskId, status,description);
//		updateTaskMobile(taskId);
	}
	
	public void saveMobileDataErrRec(List<MobileDataErrRec> recs){
		if(null != recs){
			mobileDataErrRecRepository.saveAll(recs);
		}
		
	}

}
