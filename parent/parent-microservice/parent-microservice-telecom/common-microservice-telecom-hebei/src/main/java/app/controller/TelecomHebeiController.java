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
import app.service.TelecomHebeiCrawlerService;
import app.service.TelecomHebeiService;
import app.service.aop.ICrawler;
import app.service.aop.ISmsTwice;
import app.service.common.TelecomCommonLoginService;

@RestController
@RequestMapping("/carrier")
public class TelecomHebeiController extends TelecomBasicController{
	
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TelecomHebeiService crawlerSmsTwiceTracer;	
	@Autowired
	private TelecomHebeiCrawlerService ICrawlerTracer;
//	@Autowired
//	private TelecomHebeiService telecomHebeiService;
////	@Autowired
////	private TelecomCommonLoginService telecomCommonService;
//	@Autowired
//	private CrawlerStatusMobileService crawlerStatusMobileService;
	
	@RequestMapping(value = "/sendsms", method = RequestMethod.POST)
	public ResultData<TaskMobile> sendSMS(@RequestBody MessageLogin messageLogin ){
		
		tracer.addTag("cralwer.telecom.hebei.sendsms", messageLogin.getTask_id());
		ResultData<TaskMobile> data = new ResultData<TaskMobile>();
//		
		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id());
//		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhase());
//		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhasestatus());
//		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getDescription());
//		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getError_code());
//		save(taskMobile);
		
//		try {
//			//更新cookie，获取城市代码
//			taskMobile = telecomHebeiService.updateCookies(taskMobile,messageLogin);
//		} catch (Exception e1) {
//			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_INTERMEDIATE_ERROR.getPhase());
//			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_INTERMEDIATE_ERROR.getPhasestatus());
//			taskMobile.setDescription("电信网站波动，请重新再试。");
//			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_INTERMEDIATE_ERROR.getError_code());
//			taskMobile.setFinished(false);
//			save(taskMobile);
//		}
		
		if(null != taskMobile.getNexturl()){
			//发送短信
			taskMobile = crawlerSmsTwiceTracer.sendSmsTwice(messageLogin);			
		}else{
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getError_code());
			save(taskMobile);
		}
		data.setData(taskMobile);
		return data;
	}
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskMobile> crawler(@RequestBody MessageLogin messageLogin) {
		
		tracer.addTag("cralwer.telecom.hebei.crawler", messageLogin.getTask_id());
		ResultData<TaskMobile> data = new ResultData<TaskMobile>();
//		
//		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id());		
//		boolean isCrawler = telecomCommonService.isDoing(messageLogin);
//		
//		if(isCrawler){
//			tracer.addTag("正在进行上次未完成的爬取任务。。。", messageLogin.toString());
//			data.setData(taskMobile);
//			return data;
//		}
//		
//		tracer.addTag("parser.crawler.taskid", taskMobile.getTaskid());
//		tracer.addTag("parser.crawler.auth", messageLogin.getName());
//		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
//		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
//		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
//		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
//		save(taskMobile);
		
		TaskMobile taskMobile = ICrawlerTracer.getAllData(messageLogin);
		
//		//用户信息
//		try {
//			telecomHebeiService.getUserInfo(messageLogin);
//		} catch (Exception e) {
//			tracer.addTag("crawler.telecom.hebei.userinfo.error", e.getMessage());
//			taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 404, "【用户信息】采集超时！");	
//			taskMobileRepository.updateFamilyMsgStatus(taskMobile.getTaskid(), 201, "无亲情套餐！");
//			taskMobileRepository.updateIntegralMsgStatus(taskMobile.getTaskid(), 201, "无积分详情！");
//			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
//		}
//		
//		//套餐详情
//		try {
//			telecomHebeiService.getPackage(messageLogin);
//		} catch (Exception e) {
//			tracer.addTag("crawler.telecom.hebei.package.error", e.getMessage());
//			taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(), 404, "【业务信息】采集超时！");			
//			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
//		}
//		
//		//缴费信息
//		try {
//			telecomHebeiService.getPayfee(messageLogin);
//		} catch (Exception e) {
//			tracer.addTag("crawler.telecom.hebei.payfee.error", e.getMessage());
//			taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(), 404, "【缴费信息】采集超时！");			
//			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	
//		}
//		
//		try {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
//			Calendar c1 = Calendar.getInstance();
//			String startYear = telecomHebeiService.getCurrentYearStartTime();
//			String endYear = telecomHebeiService.getCurrentYearEndTime();
//			for(int i=0;i<7;i++){			
//				c1.setTime(new Date());
//				c1.add(Calendar.MONTH, -i);
//				Date date1 = c1.getTime();
//				String currentmon = sdf.format(date1);
//				//账单信息
//				telecomHebeiService.getAccount(messageLogin,currentmon);
//			}
//			
//			
//			//通话信息
//			telecomHebeiService.getCallRec(messageLogin);
//			//短信信息
//			telecomHebeiService.getMsgRec(messageLogin,startYear,endYear);
//		} catch (Exception e) {
//			tracer.addTag("crawler.telecom.hebei.getAccount.error", e.getMessage());
//			taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 404, "【账单信息】采集超时！");	
//			taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 404, "【通讯信息】采集超时！");	
//			taskMobileRepository.updateSMSRecordStatus(taskMobile.getTaskid(), 404, "【短信信息】采集超时！");			
//			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
//		}
		data.setData(taskMobile);
		return data;
		
	}
	

}
