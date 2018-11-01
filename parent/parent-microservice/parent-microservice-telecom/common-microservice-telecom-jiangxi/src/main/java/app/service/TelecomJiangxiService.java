package app.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.domain.TempBean;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISmsTwice;
import app.service.common.CalendarParamService;
import app.service.common.CrawlerGetAndSetService;
import app.service.common.LoginGetAndSetService;



@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.jiangxi")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.jiangxi")
public class TelecomJiangxiService implements ICrawlerLogin,ISmsTwice {
	@Autowired
	private AsyncJiangxiGetAllDataService asyncJiangxiGetAllDataService;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private CrawlerGetAndSetService crawlerGetAndSetService;
	@Autowired
	private LoginGetAndSetService loginGetAndSetService;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	
	public TaskMobile findtaskMobile(String taskid) {
		return taskMobileRepository.findByTaskid(taskid);
	}
	public void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}
	//===========================相关信息爬取部分   start======================================
	@Async
	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id());
		Map<String, Future<String>> listfuture = new HashMap<>();   //判断异步爬取是否完成
		//用户信息
		try {
			Future<String> future =asyncJiangxiGetAllDataService.getUserInfo(taskMobile);
			listfuture.put("getUserInfo", future);
			//用调研账号调研的时候，亲情号没有相关数据或者是可以将相关数据存储到别的实体，
			//故为了最终的状态更新，在此处将亲情号信息的状态先设置为默认值：200
			crawlerStatusMobileService.updateFamilyMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，亲情号信息无可采集记录或此账号未设置相关信息");
		} catch (Exception e) {
			crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(),404, "数据采集中，用户信息已采集完成");
			tracer.addTag("action.crawler.getUserInfo.e",e.toString());
		}
		//月账单
		try {
			List<String> monthList = CalendarParamService.getMonth();
			for(int i=0;i<6;i++){
				String yearmonth = monthList.get(i);
				Future<String> future =asyncJiangxiGetAllDataService.getMonthBill(messageLogin,taskMobile,yearmonth);
				listfuture.put(yearmonth+"getMonthBill", future);
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getMonthBill.e",e.toString());
			
		}
		//积分信息
		try {
			//爬取从上个月开始的6个月
			List<String> monthlist = CalendarParamService.getMonth();
			for(int i=0;i<6;i++){
				String qryMonth=monthlist.get(i);
				Future<String> future =asyncJiangxiGetAllDataService.getIntegra(messageLogin,taskMobile,qryMonth);
				listfuture.put(qryMonth+"getIntegra", future);
			}
		} catch (Exception e) {
			crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 404, "数据采集中，无积分记录或此时处于月初或月末，无法查询指定月积分");
			tracer.addTag("action.crawler.getIntegra.e",e.toString());
		}
		
		//爬取业务信息
		try {
			Future<String> future =asyncJiangxiGetAllDataService.getBusiness(taskMobile);
			listfuture.put("getBusiness", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getBusiness.e",e.toString());
			crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(),404, "数据采集中，业务信息已采集完成");
			
		}
		//爬取我的现状信息
		try {
			Future<String> future =asyncJiangxiGetAllDataService.getCurrentSituation(taskMobile);
			listfuture.put("getCurrentSituation", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getCurrentSituation.e",e.toString());
			
		}
		//爬取余额记录  (在此处更新账户信息爬取状态，虽然账户信息并无数据可供采集)
		try {
			Future<String> future =asyncJiangxiGetAllDataService.getBalance(messageLogin,taskMobile);
			listfuture.put("getBalance", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getBalance.e",e.toString());
			crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(),404, "数据采集中，余额信息已采集完成");
			
		}
		//爬取充值记录
		try {
			//爬取从本月开始的6个月
			List<String> monthlist = CalendarParamService.getMonthIncludeThis();
			for(int i=0;i<6;i++){
				String qryMonth=monthlist.get(i);
				Future<String> future =asyncJiangxiGetAllDataService.getChargeInfo(messageLogin,taskMobile,qryMonth);
				listfuture.put(qryMonth+"getChargeInfo", future);
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getChargeInfo.e",e.toString());
			crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(),404, "数据采集中,充值记录已采集完成");
		}
		//语音通话详单
		try {
			//爬取从本个月开始的6个月
			List<String> monthlist = CalendarParamService.getMonthIncludeThis();
			for(int i=0;i<6;i++){
				String qryMonth=monthlist.get(i);
				Future<String> future =asyncJiangxiGetAllDataService.getCallRecord(messageLogin,taskMobile,qryMonth);
				listfuture.put(qryMonth+"getCallRecord", future);
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getCallRecord.e",e.toString());
			crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 404, "数据采集中,通话记录已采集完成");
			
		}
		//短信详单
		try {
			//爬取从本个月开始的6个月
			List<String> monthlist = CalendarParamService.getMonthIncludeThis();
			for(int i=0;i<6;i++){
				String qryMonth=monthlist.get(i);
				Future<String> future =asyncJiangxiGetAllDataService.getSMSRecord(messageLogin,taskMobile,qryMonth);
				listfuture.put(qryMonth+"getSMSRecord", future);
			}
		}  catch (Exception e) {
			tracer.addTag("action.crawler.getSMSRecord.e",e.toString());
			crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 404, "数据采集中,短信记录已采集完成");
		}
		//最终状态的更新
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
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		} catch (Exception e) {
			tracer.addTag("listfuture--ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
		return taskMobile;
	}
	//===========================相关信息爬取部分   end======================================
	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	//===========================二次登录所需短信验证码发送和验证  start======================================
	@Override
	@Async
	public TaskMobile sendSms(MessageLogin messageLogin) {
		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id());
		TempBean tempBean = new TempBean();
		try {
			tempBean = loginGetAndSetService.login(messageLogin,tempBean,taskMobile);
			if (tempBean.getHtmlpage() == null) { //二次登录短信验证码发送失败
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR1.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR1.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR1.getDescription());
				taskMobile.setTesthtml(messageLogin.getPassword().trim());
				// 登录失败状态存储
				save(taskMobile);
			}else{//二次登录短信验证码发送成功
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_SUCCESS1.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_SUCCESS1.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_SUCCESS1.getDescription());
				taskMobile.setTesthtml(messageLogin.getPassword().trim());
				save(taskMobile);
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR1.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR1.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR1.getDescription());
			taskMobile.setTesthtml(messageLogin.getPassword().trim());
			// 二次登录短信验证码发送失败状态更新
			save(taskMobile);
		}
		return taskMobile;
		
	}
	@Override
	@Async
	public TaskMobile verifySms(MessageLogin messageLogin) {
		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id());
		TempBean tempBean = loginGetAndSetService.setloginphonecode(messageLogin, taskMobile);
		if (tempBean.getErrormessage() != null) {  //不管后台如何提示
			if(tempBean.getErrormessage().contains("短信服务密码连续输入错误的次数超过三次")){
				tracer.addTag("二次登录电信验证验证码 错误次数超过3次 明日再尝试登录", tempBean.getErrormessage());
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR6.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR6.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR6.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_FIVE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_FIVE.getMessage());
				// 手机验证码验证失败状态更新
				save(taskMobile);
				return taskMobile;
			}
			
			if(tempBean.getErrormessage().contains("短信密码错误")){
				tracer.addTag("二次登录电信验证验证码 输入错误", tempBean.getErrormessage());
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getDescription());
				taskMobile.setError_code(StatusCodeRec.MOBILE_LOGIN_ERROR_SMS.getCode());
				taskMobile.setError_message(StatusCodeRec.MOBILE_LOGIN_ERROR_SMS.getMessage());
				// 手机验证码验证失败状态更新
				save(taskMobile);
				return taskMobile;
			}
			if(tempBean.getErrormessage().contains("不存在此用户")){
				tracer.addTag("二次登录电信验证验证码 登录失败，不存在此用户", tempBean.getErrormessage());
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR3.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR3.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR3.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_SIX.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_SIX.getMessage());
				// 手机验证码验证失败状态更新
				save(taskMobile);
				return taskMobile;
			}
			if(tempBean.getErrormessage().contains("您输入的短信密码已失效")){
				tracer.addTag("您输入的短信密码已失效,请重新获取短信验证码！", tempBean.getErrormessage());
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR4.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR4.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR4.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				// 手机验证码验证失败状态更新
				save(taskMobile);
				return taskMobile;
			}
			if(tempBean.getErrormessage().contains("未知错误")){
				tracer.addTag("未知错误！", tempBean.getErrormessage());
				//未知错误暂时按照短信验证码错误对待
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getDescription());
				taskMobile.setError_code(StatusCodeRec.MOBILE_LOGIN_ERROR_SMS.getCode());
				taskMobile.setError_message(StatusCodeRec.MOBILE_LOGIN_ERROR_SMS.getMessage());
				// 手机验证码验证失败状态更新
				save(taskMobile);
				return taskMobile;
			}
		}
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGINTWO_SUCCESS.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGINTWO_SUCCESS.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGINTWO_SUCCESS.getDescription());
		//将验证验证码成功的cookie存储
		String cookieString = CommonUnit.transcookieToJson(tempBean.getWebClient());
		taskMobile.setCookies(cookieString);
		// 手机验证码验证成功状态更新
		save(taskMobile);
		tracer.addTag("手机验证码验证完成", tempBean.getHtml());
		return taskMobile;
	}
	//===========================二次登录所需短信验证码发送和验证  end======================================
	//=======================爬取数据 验证码部分    start==========================================
	@Override
	@Async
	public TaskMobile sendSmsTwice(MessageLogin messageLogin) {
		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id());
		TempBean tempBean = crawlerGetAndSetService.getphonecode(taskMobile);
		String errormessage=tempBean.getErrormessage();
		System.out.println("获取到的errormessage是："+errormessage);
		if (errormessage!=null) {
			if(tempBean.getErrormessage().contains("短信验证码发送失败")){
				tracer.addTag("电信获取验证码  获取错误，错误信息是：", "=="+tempBean.getErrormessage());
				System.out.println("电信获取验证码  获取错误，错误信息是："+tempBean.getErrormessage());
				
				tracer.addTag("电信获取验证码 获取错误，html页面是：","=="+ tempBean.getHtml());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				// 发送验证码状态更新
				save(taskMobile);
				return taskMobile;
			}else if(tempBean.getErrormessage().contains("您发送的短信验证码超过了当日最大发送次数")){
				System.out.println("您发送的短信验证码超过了当日最大发送次数：=====================");

				tracer.addTag("电信获取验证码  获取错误，错误信息是：", "=="+tempBean.getErrormessage());
				tracer.addTag("电信获取验证码 获取错误，html页面是：","=="+ tempBean.getHtml());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR2.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR2.getDescription());
				taskMobile.setError_code(StatusCodeRec.MOBILE_VERIFY_SMS_TOOMANY_TODAY.getCode());
				taskMobile.setError_message(StatusCodeRec.MOBILE_VERIFY_SMS_TOOMANY_TODAY.getMessage());
				// 发送验证码状态更新
				save(taskMobile);
				return taskMobile;
			}else if(tempBean.getErrormessage().contains("未查询到您的验证号码信息")){
				System.out.println("未查询到您的验证号码信息：=====================");

				tracer.addTag("电信获取验证码  获取错误，错误信息是：", "=="+tempBean.getErrormessage());
				tracer.addTag("电信获取验证码 获取错误，html页面是：","=="+ tempBean.getHtml());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription("短信验证码获取失败，未查询到您的验证号码信息");
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				// 发送验证码状态更新
				save(taskMobile);
				return taskMobile;
			}else if(errormessage.contains("您发送的太快了")){
				tracer.addTag("电信获取验证码  获取错误，错误信息是：", "=="+tempBean.getErrormessage());
				System.out.println("电信获取验证码  获取错误，错误信息是："+tempBean.getErrormessage());
				tracer.addTag("电信获取验证码 获取错误，html页面是：","=="+ tempBean.getHtml());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription("短信验证码获取失败，发送操作过于频繁");
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				// 发送验证码状态更新
				save(taskMobile);
				return taskMobile;
			}else if(errormessage.contains("获取短信验证码过程中网站出现其他错误")){
				tracer.addTag("电信获取验证码  获取错误，错误信息是：", "=="+tempBean.getErrormessage());
				System.out.println("电信获取验证码  获取错误，错误信息是："+tempBean.getErrormessage());
				
				tracer.addTag("电信获取验证码 获取错误，html页面是：","=="+ tempBean.getHtml());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription("短信验证码获取失败，该过程中电信网站系统异常");
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				// 发送验证码状态更新
				save(taskMobile);
				return taskMobile;
			}else{
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				// 发送验证码状态更新
				save(taskMobile);
				return taskMobile;
			}
		}
		String cookieString = CommonUnit.transcookieToJson(tempBean.getWebClient());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
		taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_SUCESS.getCode());
		taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_SUCESS.getMessage());
		taskMobile.setCookies(cookieString);
		// 发送验证码状态更新
		save(taskMobile);    //将发送验证码成功后的cookie存储到taskmobile表中 
		return taskMobile;
	}
	@Override
	@Async
	public TaskMobile verifySmsTwice(MessageLogin messageLogin) {
		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id());
		TempBean tempBean = crawlerGetAndSetService.setphonecode(messageLogin, taskMobile);
		if (tempBean.getErrormessage() != null) {
			tracer.addTag("电信验证验证码  错误", tempBean.getErrormessage());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_TWO.getMessage());
			// 手机验证码验证失败状态更新
			save(taskMobile);
			return taskMobile;
		}
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
		String cookieString = CommonUnit.transcookieToJson(tempBean.getWebClient());
		taskMobile.setCookies(cookieString);
		// 手机验证码验证成功状态更新
		save(taskMobile);
		tracer.addTag("手机验证码验证完成", tempBean.getHtml());
		return taskMobile;
	}
	//=======================爬取数据 验证码部分    end============================================
	@Override
	public TaskMobile login(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		return null;
	}
}
