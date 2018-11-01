package app.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.commontracerlog.TracerLog;
import app.contoller.TelecomBasicController;
import app.service.CrawlerStatusMobileService;
import app.service.TelecomShanghaiCrawlerService;
import app.service.TelecomShanghaiService;
import app.service.aop.ICrawler;
import app.service.aop.ISmsTwice;
import app.service.common.TelecomCommonLoginService;

@RestController
@RequestMapping("/carrier")
public class TelecomShanghaiController extends TelecomBasicController{
	
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TelecomShanghaiCrawlerService telecomShanghaiCrawlerService;
	@Autowired
	private TelecomShanghaiService telecomShanghaiService;
//	@Autowired
//	private TelecomCommonLoginService telecomCommonService;
//	@Autowired
//	private CrawlerStatusMobileService crawlerStatusMobileService;
	
	@RequestMapping(value = "/sendsms", method = RequestMethod.POST)
	public ResultData<TaskMobile> sendSms(@RequestBody MessageLogin messageLogin) {
		
		tracer.addTag("cralwer.telecom.shanghai.sendsms", messageLogin.getTask_id());
		ResultData<TaskMobile> data = new ResultData<TaskMobile>();
//		
//		//task表改为正在准备发送短信的状态
//		TaskMobile taskMobile = telecomShanghaiService.changeTaskMobileStauts(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhase(),
//				StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhasestatus(),
//				StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getDescription(),messageLogin.getTask_id());
		
		TaskMobile taskMobile = null;
		try {
			taskMobile = telecomShanghaiService.sendSmsTwice(messageLogin);
//			telecomShanghaiService.sendSms(messageLogin,taskMobile);
		} catch (Exception e) {
			telecomShanghaiService.changeTaskMobileStauts(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase(),StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus(),
					StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription(),messageLogin.getTask_id());
		}
		data.setData(taskMobile);
		return data;
		
	}
	
	
	@RequestMapping(value = "/validate", method = RequestMethod.POST)
	public ResultData<TaskMobile> validate(@RequestBody MessageLogin messageLogin) {
		
		tracer.addTag("cralwer.telecom.shanghai.validate", messageLogin.getTask_id());
		ResultData<TaskMobile> data = new ResultData<TaskMobile>();
		
		//task表改为正在校验的状态
//		TaskMobile taskMobile = telecomShanghaiService.changeTaskMobileStauts(StatusCodeEnum.TASKMOBILE_QUERY_LOADING.getPhase(),
//				StatusCodeEnum.TASKMOBILE_QUERY_LOADING.getPhasestatus(),
//				StatusCodeEnum.TASKMOBILE_QUERY_LOADING.getDescription(),messageLogin.getTask_id());
//		
		TaskMobile taskMobile = null;
		try {
			taskMobile = telecomShanghaiService.verifySmsTwice(messageLogin);
//			telecomShanghaiService.validate(messageLogin,taskMobile);
		} catch (Exception e) {
			telecomShanghaiService.changeTaskMobileStauts(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase(),StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus(),
					"验证码已失效！或验证码不正确！",messageLogin.getTask_id());
		}
		
		data.setData(taskMobile);		
		return data;
		
	}

	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskMobile> crawler(@RequestBody MessageLogin messageLogin) {
		
		tracer.addTag("cralwer.telecom.shanghai.crawler", messageLogin.getTask_id());
		ResultData<TaskMobile> data = new ResultData<TaskMobile>();
		
		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id());		
//		boolean isCrawler = telecomCommonService.isDoing(messageLogin);
//		
//		if(isCrawler){
//			tracer.addTag("正在进行上次未完成的爬取任务。。。", messageLogin.toString());
//			data.setData(taskMobile);
//			return data;
//		}
//		
//		tracer.addTag("parser.crawler.shanghai.taskid", taskMobile.getTaskid());
//		tracer.addTag("parser.crawler.shanghai.auth", messageLogin.getName());
//		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
//		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
//		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
//		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
//		save(taskMobile);
		
		telecomShanghaiCrawlerService.getAllData(messageLogin);
		
		//用户信息
//		try {
//			telecomShanghaiService.getUserInfo(messageLogin);
//		} catch (Exception e) {
//			tracer.addTag("crawler.telecom.shanghai.userinfo.error", e.getMessage());
//			taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 404, "【用户信息】采集超时！");	
//			taskMobileRepository.updateFamilyMsgStatus(taskMobile.getTaskid(), 404, "无亲情套餐！");
//			taskMobileRepository.updateIntegralMsgStatus(taskMobile.getTaskid(), 201, "无积分详情！");
//			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
//		}
//		
//		//缴费信息
//		try {
//			telecomShanghaiService.getPayfee(messageLogin);
//		} catch (Exception e) {
//			tracer.addTag("crawler.telecom.shanghai.payfee.error", e.getMessage());
//			taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(), 404, "【缴费信息】采集超时！");			
//			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
//		}
//		
//		//账户信息
//		try {
//			telecomShanghaiService.getAccount(messageLogin);
//		} catch (Exception e) {
//			tracer.addTag("crawler.telecom.shanghai.account.error", e.getMessage());
//			taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 200, "【账户信息】采集超时！");		
//			taskMobileRepository.updateBusinessMsgStatus(taskMobile.getTaskid(), 200, "【套餐信息】采集超时！");
//			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
//		}
//		
//		try {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
//			Calendar c = Calendar.getInstance();
//			for(int i=0;i<7;i++){			
//				c.setTime(new Date());
//				c.add(Calendar.MONTH, -i);
//				Date date = c.getTime();
//				String mon = sdf.format(date);	
//				//通话信息
//				telecomShanghaiService.getCallRec(messageLogin,mon);
//				//短信信息
//				telecomShanghaiService.getMsgRec(messageLogin,mon);
//			}
//		} catch (Exception e) {
//			tracer.addTag("crawler.telecom.shanghai.callAndMsg.error", e.getMessage());
//			taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 404, "【通讯信息】采集超时！");			
//			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
//			taskMobileRepository.updateSMSRecordStatus(taskMobile.getTaskid(), 200, "【短信信息】采集超时！");			
//			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
//		}
		data.setData(taskMobile);
		return data;
		
	}

}
