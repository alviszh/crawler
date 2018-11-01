package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawler;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.xinjiang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.xinjiang")
public class TelecomXinJiangCrawlerService implements ICrawler{
	
	@Autowired
	private TaskMobileRepository taskMobileRepository;
//	@Autowired
//	private TelecomXinJiangHtmlUnit  telecomXinJiangHtmlUnit;
//	@Autowired
//	private TelecomXinJiangService telecomXinJiangService;
	@Autowired
	private TelecomXinJiangService telecomXinJiangService;
	@Autowired 
	private TracerLog tracer;	
	public void updateStatus(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getDescription());
		taskMobileRepository.save(taskMobile);
	} 

	@Async
	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		tracer.addTag("@Async getAllData", messageLogin.toString());
		tracer.addTag("taskid",messageLogin.getTask_id());	
		try {
			tracer.addTag("TelecomXinJiangAspectService getAllData", messageLogin.getTask_id());
			telecomXinJiangService.getVoiceRecordData(messageLogin);// 语音详单
			telecomXinJiangService.getUserInfo(messageLogin);// 用户信息
			telecomXinJiangService.getAccountInfo(messageLogin);
			telecomXinJiangService.getRealtimefee(messageLogin);// 实时话费情况
			telecomXinJiangService.getSmsRecord(messageLogin);// 短信详单
			telecomXinJiangService.getAddvalueItems(messageLogin);// 增值服务详单
			telecomXinJiangService.getRechargeRecord(messageLogin);// 充值记录
			telecomXinJiangService.getPaymonths(messageLogin);// 月账单记录
			//telecomXinJiangService.getPoint(messageLogin);// 积分生成记录
		}catch(Exception e){
			tracer.addTag("TelecomXinJiangAspectService getAllData",e.getMessage());
			updateStatus(messageLogin);
		}		
		TaskMobile taskMobile=taskMobileRepository.findByTaskid(messageLogin.getTask_id().trim());
		tracer.addTag("TelecomXinJiangAspectService getAllData",taskMobile.toString());
		return taskMobile;
	}
//	/**
//	 * @Description: 发送登录短信随机码
//	 * @param messageLogin
//	 * @return String
//	 */
//	public TaskMobile sendSms(MessageLogin mssageLogin) {
//	    TaskMobile taskMobile=taskMobileRepository.findByTaskid(mssageLogin.getTask_id());
//		tracer.addTag("sendSms", taskMobile.getTaskid());
//		try {	
//			String smsCode = telecomXinJiangHtmlUnit.getphonecode(mssageLogin,taskMobile);
//			tracer.addTag("验证码发送返回字符串",smsCode);
//			if("1".equals(smsCode)){
//				tracer.addTag(mssageLogin.getTask_id(),"验证码发送成功");
//				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
//				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
//				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
//				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_SUCESS.getCode());
//				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_SUCESS.getMessage());
//				// 发送验证码状态更新
//				taskMobileRepository.save(taskMobile);				
//				telecomXinJiangService.getAccountInfo(mssageLogin, taskMobile);
//				return taskMobile;
//			}else{
//				tracer.addTag(mssageLogin.getTask_id(),"验证码发送失败");
//				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
//				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
//				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
//				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
//				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
//				// 发送验证码状态更新
//				taskMobileRepository.save(taskMobile);
//				return taskMobile;
//			}			
//		} catch (Exception e) {
//			tracer.addTag(mssageLogin.getTask_id(),"验证码发送失败");
//			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
//			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
//			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
//			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
//			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
//			// 发送验证码状态更新
//			taskMobileRepository.save(taskMobile);
//			return taskMobile;
//		}
//
//	}
	
//	@Override
//	public TaskMobile verifySms(MessageLogin messageLogin) {		
//		tracer.addTag("setPhoneCode", messageLogin.getTask_id());
//		TaskMobile taskMobile= taskMobileRepository.findByTaskid(messageLogin.getTask_id());
//		try {
//			WebParam  webParam = telecomXinJiangHtmlUnit.setphonecode(messageLogin, taskMobile);	
//			String smsResult=webParam.getSmsResult();
//			tracer.addTag("setPhoneCode",smsResult);	
//			if (smsResult !=null && smsResult.contains("success")) {
//				JSONObject jsonInsurObjs = JSONObject.fromObject(webParam.getSmsResult());
//			    String success = jsonInsurObjs.getString("success");		
//			 	tracer.addTag("setPhoneCode success",success);	
//			     if("true".equals(success)){
//					tracer.addTag(messageLogin.getTask_id(), "手机验证码验证成功！");		
//					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
//					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
//					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
//					String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
//	                taskMobile.setCookies(cookies);           
//					// 手机验证码验证成功状态更新
//					taskMobileRepository.save(taskMobile);		
//				}else{
//				    tracer.addTag(messageLogin.getTask_id(), "手机验证码验证失败！");		
//					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
//					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
//					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
//					taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
//					taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());			
//					// 手机验证码验证失败状态更新
//					taskMobileRepository.save(taskMobile);
//				}
//			}else{
//				  tracer.addTag(messageLogin.getTask_id(), "手机验证码验证失败！");		
//				  taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
//				  taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
//				  taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
//				  taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
//				  taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());			
//				  // 手机验证码验证失败状态更新
//				 taskMobileRepository.save(taskMobile);
//			}
//		} catch (Exception e) {		
//			tracer.addTag("verifySms.Exception", e.toString());
//		}
//		return taskMobile;
//	}
//

	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
