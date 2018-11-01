package app.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.mobile.MobileDataErrRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiBill;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiBusiness;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiCall;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiHtml;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiMessage;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiPay;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiScore;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiUserInfo;
import com.microservice.dao.repository.crawler.mobile.MobileDataErrRecRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.anhui.TelecomAnhuiRepositoryBill;
import com.microservice.dao.repository.crawler.telecom.anhui.TelecomAnhuiRepositoryBusiness;
import com.microservice.dao.repository.crawler.telecom.anhui.TelecomAnhuiRepositoryCall;
import com.microservice.dao.repository.crawler.telecom.anhui.TelecomAnhuiRepositoryHtml;
import com.microservice.dao.repository.crawler.telecom.anhui.TelecomAnhuiRepositoryMessage;
import com.microservice.dao.repository.crawler.telecom.anhui.TelecomAnhuiRepositoryPay;
import com.microservice.dao.repository.crawler.telecom.anhui.TelecomAnhuiRepositoryScore;
import com.microservice.dao.repository.crawler.telecom.anhui.TelecomAnhuiRepositoryUserInfo;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.parser.TelecomAnhuiParser;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.anhui")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.anhui")
public class TelecomAnhuiService {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TelecomAnhuiParser telecomAnhuiParser;
	@Autowired
	private CrawlerStatusMobileService c;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomAnhuiRepositoryUserInfo telecomAnhuiRepositoryUserInfo;
	@Autowired
	private TelecomAnhuiRepositoryBill telecomAnhuiRepositoryBill;
	@Autowired
	private TelecomAnhuiRepositoryBusiness telecomAnhuiRepositoryBusiness;
	@Autowired
	private TelecomAnhuiRepositoryScore telecomAnhuiRepositoryScore;
	@Autowired
	private TelecomAnhuiRepositoryCall telecomAnhuiRepositoryCall;
	@Autowired
	private TelecomAnhuiRepositoryMessage telecomAnhuiRepositoryMessage;
	@Autowired
	private TelecomAnhuiRepositoryPay telecomAnhuiRepositoryPay;
	@Autowired
	private TelecomAnhuiRepositoryHtml telecomAnhuiRepositoryHtml;
	@Autowired
	private MobileDataErrRecRepository mobileDataErrRecRepository;
	
	
	public String getFirstDay(String fmt,int i) throws Exception{
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -i);  
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}
	
	//短信
	@Async
	public void getSMS(MessageLogin messageLogin, TaskMobile taskMobile){
		int j = 0;
		try {
			int time=0;
			for (j = 0; j < 6; j++) {
				WebParam<TelecomAnhuiMessage> webParam = telecomAnhuiParser.getSMS(messageLogin,taskMobile,j);
				if(null != webParam)
				{
					if(webParam.getList()==null)
					{
						tracer.addTag("parser.telecom.crawler.getSMS", messageLogin.getTask_id());
						taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
						tracer.addTag("parser.telecom.crawler.getSMS.taskmobile", j+taskMobile.toString());
//						MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), "短信记录",getFirstDay("yyyyMM",j), taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "无数据", 1);
//						mobileDataErrRecRepository.save(m);
//						taskMobileRepository.updateSMSRecordStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
//						c.updateTaskMobile(messageLogin.getTask_id()); 
					}
					else
					{
						TelecomAnhuiHtml telecomAnhuiHtml = new TelecomAnhuiHtml();
						telecomAnhuiHtml.setHtml(webParam.getHtml());
						telecomAnhuiHtml.setTaskid(messageLogin.getTask_id());
						telecomAnhuiHtml.setPageCount(1);
						telecomAnhuiHtml.setType("getSMS");
						telecomAnhuiHtml.setUrl(webParam.getUrl());
						telecomAnhuiRepositoryHtml.save(telecomAnhuiHtml);
//						taskMobileRepository.updateSMSRecordStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
						telecomAnhuiRepositoryMessage.saveAll(webParam.getList());
						tracer.addTag("parser.telecom.crawler.getSMS.SUCCESS",j+ messageLogin.getTask_id());
//						c.updateTaskMobile(messageLogin.getTask_id());
						time++;
					}
				}
				else
				{
					MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), "短信记录",getFirstDay("yyyyMM",j), taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时", 1);
					mobileDataErrRecRepository.save(m);
					tracer.addTag("parser.telecom.crawler.getSMS.ERROR1",j+taskMobile.getTaskid());
//					taskMobileRepository.updateSMSRecordStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
//					c.updateTaskMobile(messageLogin.getTask_id());
				}
			}
			if(time==0)
			{
				tracer.addTag("parser.telecom.crawler.getSMS.ERROR2",j+taskMobile.getTaskid());
				taskMobileRepository.updateSMSRecordStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
//				c.updateTaskMobile(messageLogin.getTask_id());
			}
			else
			{
				taskMobileRepository.updateSMSRecordStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getSMS.SUCCESS",j+ messageLogin.getTask_id());
//				c.updateTaskMobile(messageLogin.getTask_id());
			}
		} catch (Exception e) {
			MobileDataErrRec m = null;
			try {
				m = new MobileDataErrRec(taskMobile.getTaskid(), "短信记录",getFirstDay("yyyyMM",j), taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时", 1);
			} catch (Exception e1) {
				tracer.addTag("parser.telecom.crawler.getSMS.ERRREC",j+taskMobile.getTaskid());
				e1.printStackTrace();
			}
			mobileDataErrRecRepository.save(m);
			tracer.addTag("parser.telecom.crawler.getSMS.ERROR3",j+taskMobile.getTaskid());
			taskMobileRepository.updateSMSRecordStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
//			c.updateTaskMobile(messageLogin.getTask_id());
			e.printStackTrace();
		}
		c.updateTaskMobile(messageLogin.getTask_id());
	}

	
	//通讯
	@Async
	public void getAllCall(MessageLogin messageLogin, TaskMobile taskMobile){
		int j = 0;
		try {
			int time=0;
			for (j = 0; j < 6; j++) {
				WebParam<TelecomAnhuiCall> webParam = telecomAnhuiParser.getAllCall(messageLogin,taskMobile,j);
				if(null != webParam)
				{
					if(webParam.getList()==null)
					{
						tracer.addTag("parser.telecom.crawler.getAllCall", messageLogin.getTask_id());
						taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
						tracer.addTag("parser.telecom.crawler.getAllCall.taskmobile", taskMobile.toString());
//						MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录",getFirstDay("yyyyMM",j),taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时", 1);
//						mobileDataErrRecRepository.save(m);
//						taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
//						c.updateTaskMobile(messageLogin.getTask_id()); 
					}
					else
					{
						TelecomAnhuiHtml telecomAnhuiHtml = new TelecomAnhuiHtml();
						telecomAnhuiHtml.setHtml(webParam.getHtml());
						telecomAnhuiHtml.setTaskid(messageLogin.getTask_id());
						telecomAnhuiHtml.setPageCount(1);
						telecomAnhuiHtml.setType("getAllCall");
						telecomAnhuiHtml.setUrl(webParam.getUrl());
						telecomAnhuiRepositoryHtml.save(telecomAnhuiHtml);
//						taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
						telecomAnhuiRepositoryCall.saveAll(webParam.getList());
						tracer.addTag("parser.telecom.crawler.getAllCall.SUCCESS",j+ messageLogin.getTask_id());
//						c.updateTaskMobile(messageLogin.getTask_id()); 
						time++;
					}
				}
				else
				{
					MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录",getFirstDay("yyyyMM",j),taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时", 1);
					mobileDataErrRecRepository.save(m);
					tracer.addTag("parser.telecom.crawler.getAllCall.ERROR1",j+taskMobile.getTaskid());
//					taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
//					c.updateTaskMobile(messageLogin.getTask_id());
				}
			}
			if(time==0)
			{
				tracer.addTag("parser.telecom.crawler.getAllCall.ERROR2",j+taskMobile.getTaskid());
				taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
//				c.updateTaskMobile(messageLogin.getTask_id());
			}
			else
			{
				tracer.addTag("parser.telecom.crawler.getAllCall.SUCCESS",j+ messageLogin.getTask_id());
				taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
//				c.updateTaskMobile(messageLogin.getTask_id()); 
			}
		} catch (Exception e) {
			MobileDataErrRec m = null;
			try {
				m = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录",getFirstDay("yyyyMM",j),taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时", 1);
			} catch (Exception e1) {
				tracer.addTag("parser.telecom.crawler.getAllCall.ERRREC",j+taskMobile.getTaskid());
				e1.printStackTrace();
			}
			mobileDataErrRecRepository.save(m);
			tracer.addTag("parser.telecom.crawler.getAllCall.ERROR3",j+taskMobile.getTaskid());
			taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
//			c.updateTaskMobile(messageLogin.getTask_id());
			e.printStackTrace();
		}
		c.updateTaskMobile(messageLogin.getTask_id());
	}

	
	//业务 
	@Async
	public void getBusiness(MessageLogin messageLogin, TaskMobile taskMobile) {
		try {
			WebParam<TelecomAnhuiBusiness> webParam = telecomAnhuiParser.getBusiness(messageLogin,taskMobile);
			if(null != webParam)
			{
				if(webParam.getList()==null)
				{
					tracer.addTag("parser.telecom.crawler.getBusiness", messageLogin.getTask_id());
					taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
					tracer.addTag("parser.telecom.crawler.getBusiness.html", "<xmp>" + webParam.getHtml() + "</xmp>");
					tracer.addTag("parser.telecom.crawler.getBusiness.taskmobile", taskMobile.toString());
					taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getDescription());
					c.updateTaskMobile(messageLogin.getTask_id()); 
				}
				else
				{
					TelecomAnhuiHtml telecomAnhuiHtml = new TelecomAnhuiHtml();
					telecomAnhuiHtml.setHtml(webParam.getHtml());
					telecomAnhuiHtml.setTaskid(messageLogin.getTask_id());
					telecomAnhuiHtml.setPageCount(1);
					telecomAnhuiHtml.setType("getBusiness");
					telecomAnhuiHtml.setUrl(webParam.getUrl());
					telecomAnhuiRepositoryHtml.save(telecomAnhuiHtml);
					taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getDescription());
					telecomAnhuiRepositoryBusiness.saveAll(webParam.getList());
					tracer.addTag("parser.telecom.crawler.getBusiness.SUCCESS", messageLogin.getTask_id());
					c.updateTaskMobile(messageLogin.getTask_id()); 
				}
			}
			else
			{
				tracer.addTag("parser.telecom.crawler.getBusiness.ERROR1",taskMobile.getTaskid());
				taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getDescription());
				c.updateTaskMobile(messageLogin.getTask_id());
			}
		} catch (Exception e) {
			tracer.addTag("parser.telecom.crawler.getBusiness.ERROR2",taskMobile.getTaskid());
			taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getDescription());
			c.updateTaskMobile(messageLogin.getTask_id());
			e.printStackTrace();
		}
		
	}

	
	//积分
	@Async
	public void getScore(MessageLogin messageLogin, TaskMobile taskMobile) {
		try {
			int time=0;
			for (int i = 0; i < 6; i++) {
				WebParam<TelecomAnhuiScore> webParam = telecomAnhuiParser.getScore(messageLogin,taskMobile,i);
				if(null != webParam)
				{
					if(webParam.getList()==null)
					{
						tracer.addTag("parser.telecom.crawler.getScore", messageLogin.getTask_id());
						taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
						tracer.addTag("parser.telecom.crawler.getScore.taskmobile", taskMobile.toString());
					}
					else if(webParam.getHtml().contains("暂无查到相关记录"))
					{
						TelecomAnhuiHtml telecomAnhuiHtml = new TelecomAnhuiHtml();
						telecomAnhuiHtml.setHtml(webParam.getHtml());
						telecomAnhuiHtml.setTaskid(messageLogin.getTask_id());
						telecomAnhuiHtml.setPageCount(1);
						telecomAnhuiHtml.setType("getScore");
						telecomAnhuiHtml.setUrl(webParam.getUrl());
						telecomAnhuiRepositoryHtml.save(telecomAnhuiHtml);
						tracer.addTag("parser.telecom.crawler.getScore.SUCCESS.NOData1", messageLogin.getTask_id());
						time=9;
					}
					else
					{
						TelecomAnhuiHtml telecomAnhuiHtml = new TelecomAnhuiHtml();
						telecomAnhuiHtml.setHtml(webParam.getHtml());
						telecomAnhuiHtml.setTaskid(messageLogin.getTask_id());
						telecomAnhuiHtml.setPageCount(1);
						telecomAnhuiHtml.setType("getScore");
						telecomAnhuiHtml.setUrl(webParam.getUrl());
						System.out.println(telecomAnhuiHtml);
						telecomAnhuiRepositoryHtml.save(telecomAnhuiHtml);
						taskMobileRepository.updateIntegralMsgStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
						telecomAnhuiRepositoryScore.saveAll(webParam.getList());
						tracer.addTag("parser.telecom.crawler.getScore.SUCCESS", messageLogin.getTask_id());
						time++;
					}
				}else
				{
					tracer.addTag("parser.telecom.crawler.getScore.ERROR1",taskMobile.getTaskid());
					taskMobileRepository.updateIntegralMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
				}
			}
			if(time==0)
			{
				tracer.addTag("parser.telecom.crawler.getScore.ERROR2",taskMobile.getTaskid());
				taskMobileRepository.updateIntegralMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
				c.updateTaskMobile(messageLogin.getTask_id());
			}
			else
			{
				taskMobileRepository.updateIntegralMsgStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getScore.SUCCESS3", messageLogin.getTask_id());
				c.updateTaskMobile(messageLogin.getTask_id()); 
			}
		} catch (Exception e) {
			tracer.addTag("parser.telecom.crawler.getScore.ERROR3",taskMobile.getTaskid());
			taskMobileRepository.updateIntegralMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
			c.updateTaskMobile(messageLogin.getTask_id());
			e.printStackTrace();
		}
	}

	//亲情号
	@Async
	public void getfamily(MessageLogin messageLogin, TaskMobile taskMobile) {
		taskMobileRepository.updateFamilyMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
		tracer.addTag("parser.telecom.crawler.getfamily.ERROR", messageLogin.getTask_id());
		c.updateTaskMobile(messageLogin.getTask_id()); 
	}

	//账单
	@Async
	public void getBill(MessageLogin messageLogin, TaskMobile taskMobile) {
		try {
			int time=0;
			for (int j = 1; j <7; j++) {
				WebParam<TelecomAnhuiBill> webParam = telecomAnhuiParser.getBill(messageLogin,taskMobile,j);
				if(null != webParam)
				{
					if(webParam.getList()==null)
					{
						tracer.addTag("parser.telecom.crawler.getBill", messageLogin.getTask_id());
						taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
						tracer.addTag("parser.telecom.crawler.getBill.taskmobile", taskMobile.toString());
						taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
					}
					else
					{
						TelecomAnhuiHtml telecomAnhuiHtml = new TelecomAnhuiHtml();
						telecomAnhuiHtml.setHtml(webParam.getHtml());
						telecomAnhuiHtml.setTaskid(messageLogin.getTask_id());
						telecomAnhuiHtml.setPageCount(1);
						telecomAnhuiHtml.setType("getBill");
						telecomAnhuiHtml.setUrl(webParam.getUrl());
						telecomAnhuiRepositoryHtml.save(telecomAnhuiHtml);
						taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
						telecomAnhuiRepositoryBill.saveAll(webParam.getList());
						tracer.addTag("parser.telecom.crawler.getBill.SUCCESS", messageLogin.getTask_id());
						time++;
					}
				}
				else
				{
					tracer.addTag("parser.telecom.crawler.getBill.ERROR1",taskMobile.getTaskid());
					taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
				}
			}
			if(time>0)
			{
				taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getBill.SUCCESS", messageLogin.getTask_id());
				c.updateTaskMobile(messageLogin.getTask_id()); 
			}
			else
			{
				tracer.addTag("parser.telecom.crawler.getBill.ERROR2",taskMobile.getTaskid());
				taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
				c.updateTaskMobile(messageLogin.getTask_id());
				
			}
		} catch (Exception e) {
			tracer.addTag("parser.telecom.crawler.getBill.ERROR3",taskMobile.getTaskid());
			taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
			c.updateTaskMobile(messageLogin.getTask_id());
			e.printStackTrace();
		}
	}

	
	//缴费信息
	@Async
	public void getPay(MessageLogin messageLogin, TaskMobile taskMobile) {
		try {
			int time=0;
			for (int i = 0; i < 6; i++) {
				WebParam<TelecomAnhuiPay> webParam = telecomAnhuiParser.getPay(messageLogin,taskMobile,i);
				if(null != webParam)
				{
					if(webParam.getList()==null)
					{
						tracer.addTag("parser.telecom.crawler.getPay", messageLogin.getTask_id());
						taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
						tracer.addTag("parser.telecom.crawler.getPay.taskmobile", taskMobile.toString());
						taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
					}
					else if(webParam.getCode()==203)
					{
						taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(),200,"数据采集中，【支付信息】无记录");
					}
					else
					{
						TelecomAnhuiHtml telecomAnhuiHtml = new TelecomAnhuiHtml();
						telecomAnhuiHtml.setHtml(webParam.getHtml());
						telecomAnhuiHtml.setTaskid(messageLogin.getTask_id());
						telecomAnhuiHtml.setPageCount(1);
						telecomAnhuiHtml.setType("getPay");
						telecomAnhuiHtml.setUrl(webParam.getUrl());
						System.out.println(telecomAnhuiHtml);
						telecomAnhuiRepositoryHtml.save(telecomAnhuiHtml);
						taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
						telecomAnhuiRepositoryPay.saveAll(webParam.getList());
						tracer.addTag("parser.telecom.crawler.getPay.SUCCESS", messageLogin.getTask_id());
						time++;
					}
					
				}
				else
				{
					tracer.addTag("parser.telecom.crawler.getPay.ERROR1",taskMobile.getTaskid());
					taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_ERROR.getDescription());
					//c.updateTaskMobile(messageLogin.getTask_id());
				}
			}
			if(time==0)
			{
				tracer.addTag("parser.telecom.crawler.getPay.ERROR2",taskMobile.getTaskid());
				taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
				c.updateTaskMobile(messageLogin.getTask_id());
			}
			else
			{
				taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getPay.SUCCESS", messageLogin.getTask_id());
				c.updateTaskMobile(messageLogin.getTask_id());
			}
		} catch (Exception e) {
			tracer.addTag("parser.telecom.crawler.getPay.ERROR3",taskMobile.getTaskid());
			taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
			c.updateTaskMobile(messageLogin.getTask_id());
			e.printStackTrace();
		}
	}

	
	//个人信息
	@Async
	public void getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile) {
		try {
			WebParam<TelecomAnhuiUserInfo> webParam = telecomAnhuiParser.getUserInfo(messageLogin,taskMobile);
			if(null != webParam)
			{
				if(webParam.getTelecomAnhuiUserInfo()==null)
				{
					tracer.addTag("parser.telecom.crawler.getUserInfo", messageLogin.getTask_id());
					taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
					tracer.addTag("parser.telecom.crawler.getUserInfo.html", "<xmp>" + webParam.getHtml() + "</xmp>");
					tracer.addTag("parser.telecom.crawler.getUserInfo.taskmobile", taskMobile.toString());
					taskMobileRepository.updateUserMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
				}
				else
				{
					TelecomAnhuiHtml telecomAnhuiHtml = new TelecomAnhuiHtml();
					telecomAnhuiHtml.setHtml(webParam.getHtml());
					telecomAnhuiHtml.setTaskid(messageLogin.getTask_id());
					telecomAnhuiHtml.setPageCount(1);
					telecomAnhuiHtml.setType("getUserInfo");
					telecomAnhuiHtml.setUrl(webParam.getUrl());
					System.out.println(telecomAnhuiHtml);
					telecomAnhuiRepositoryHtml.save(telecomAnhuiHtml);
					taskMobileRepository.updateUserMsgStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
					telecomAnhuiRepositoryUserInfo.save(webParam.getTelecomAnhuiUserInfo());
					tracer.addTag("parser.telecom.crawler.getSMS.SUCCESS", messageLogin.getTask_id());
				}
			}
			else
			{
				tracer.addTag("parser.telecom.crawler.getUserInfo.ERROR1",taskMobile.getTaskid());
				taskMobileRepository.updateUserMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
			}
		} catch (Exception e) {
			tracer.addTag("parser.telecom.crawler.getUserInfo.ERROR2",taskMobile.getTaskid());
			taskMobileRepository.updateUserMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
			e.printStackTrace();
		}
		c.updateTaskMobile(messageLogin.getTask_id());
	}


	//验证验证码
	public void setphonecode(MessageLogin messageLogin, TaskMobile taskMobile) {
		try {
//			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getDescription());
//			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhase());
//			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhasestatus());
//			taskMobileRepository.save(taskMobile);
			WebParam webParam = telecomAnhuiParser.getDetailCall(messageLogin,taskMobile,1);
			if(null != webParam)
			{
				if(webParam.getHtml()==null)
				{
					taskMobile.setDescription("短信验证码发送失败,您今天内获取取随机密码已超出限制");
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus());
					taskMobileRepository.save(taskMobile);
				}
				else
				{
					if(webParam.getHtml().contains("fmList")==true)
					{
						//taskMobile.setDescription("验证码输入成功");
						taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS1.getPhase());
						taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS1.getPhasestatus());
						taskMobile.setDescription("账号验证成功！");
						taskMobileRepository.save(taskMobile);
					}
					else if(webParam.getHtml().contains("验证失败网络有误，请重新尝试"))
					{
						taskMobile.setDescription("验证失败网络有误，请重新尝试");
						taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
						taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus());
						taskMobileRepository.save(taskMobile);
					}
					else
					{
						taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getDescription());
						taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
						taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus());
						taskMobileRepository.save(taskMobile);
					}
				}
			}
			else
			{
				System.out.println("=================");
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getDescription());
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus());
				taskMobileRepository.save(taskMobile);
			}
			
		} catch (Exception e) {
			taskMobile.setDescription("网站异常，暂停服务，请您关注安徽电信官网");
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus());
			taskMobileRepository.save(taskMobile);
			e.printStackTrace();
		}
		
	}


	//个人信息.
	@Async
	public void getWapUserInfo(MessageLogin messageLogin, TaskMobile taskMobile) {
		try {
			WebParam<TelecomAnhuiUserInfo> webParam = telecomAnhuiParser.getWapUserInfo(messageLogin,taskMobile);
			if(null != webParam)
			{
				if(webParam.getTelecomAnhuiUserInfo()==null)
				{
					tracer.addTag("parser.telecom.crawler.getUserInfo", messageLogin.getTask_id());
					taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
					tracer.addTag("parser.telecom.crawler.getUserInfo.html", "<xmp>" + webParam.getHtml() + "</xmp>");
					tracer.addTag("parser.telecom.crawler.getUserInfo.taskmobile", taskMobile.toString());
					taskMobileRepository.updateUserMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
				}
				else
				{
					TelecomAnhuiHtml telecomAnhuiHtml = new TelecomAnhuiHtml();
					telecomAnhuiHtml.setHtml(webParam.getHtml());
					telecomAnhuiHtml.setTaskid(messageLogin.getTask_id());
					telecomAnhuiHtml.setPageCount(1);
					telecomAnhuiHtml.setType("getUserInfo");
					telecomAnhuiHtml.setUrl(webParam.getUrl());
					System.out.println(telecomAnhuiHtml);
					telecomAnhuiRepositoryHtml.save(telecomAnhuiHtml);
					taskMobileRepository.updateUserMsgStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
					telecomAnhuiRepositoryUserInfo.save(webParam.getTelecomAnhuiUserInfo());
					tracer.addTag("parser.telecom.crawler.getSMS.SUCCESS", messageLogin.getTask_id());
				}
			}
			else
			{
				tracer.addTag("parser.telecom.crawler.getUserInfo.ERROR1",taskMobile.getTaskid());
				taskMobileRepository.updateUserMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
			}
		} catch (Exception e) {
			tracer.addTag("parser.telecom.crawler.getUserInfo.ERROR2",taskMobile.getTaskid());
			taskMobileRepository.updateUserMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
			e.printStackTrace();
		}
		c.updateTaskMobile(messageLogin.getTask_id());
		
	}

	//缴费
	@Async
	public void getWapPay(MessageLogin messageLogin, TaskMobile taskMobile) {
		try {
			int time=0;
			for (int i = 0; i < 6; i++) {
				WebParam<TelecomAnhuiPay> webParam = telecomAnhuiParser.getWapPay(messageLogin,taskMobile,i);
				if(null != webParam)
				{
					
				    if(webParam.getList() !=null)
					{
						TelecomAnhuiHtml telecomAnhuiHtml = new TelecomAnhuiHtml();
						telecomAnhuiHtml.setHtml(webParam.getHtml());
						telecomAnhuiHtml.setTaskid(messageLogin.getTask_id());
						telecomAnhuiHtml.setPageCount(1);
						telecomAnhuiHtml.setType("getWapPay");
						telecomAnhuiHtml.setUrl(webParam.getUrl());
						System.out.println(telecomAnhuiHtml);
						telecomAnhuiRepositoryHtml.save(telecomAnhuiHtml);
//						taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
						telecomAnhuiRepositoryPay.saveAll(webParam.getList());
						tracer.addTag("parser.telecom.crawler.getPay.SUCCESS", messageLogin.getTask_id());
						time++;
					}
					else if(webParam.getTelecomAnhuiPay() !=null)
					{
						TelecomAnhuiHtml telecomAnhuiHtml = new TelecomAnhuiHtml();
						telecomAnhuiHtml.setHtml(webParam.getHtml());
						telecomAnhuiHtml.setTaskid(messageLogin.getTask_id());
						telecomAnhuiHtml.setPageCount(1);
						telecomAnhuiHtml.setType("getWapPay");
						telecomAnhuiHtml.setUrl(webParam.getUrl());
						System.out.println(telecomAnhuiHtml);
						telecomAnhuiRepositoryHtml.save(telecomAnhuiHtml);
//						taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
						telecomAnhuiRepositoryPay.save(webParam.getTelecomAnhuiPay());
						tracer.addTag("parser.telecom.crawler.getPay.SUCCESS", messageLogin.getTask_id());
						time++;
					}
					else
					{
						tracer.addTag("parser.telecom.crawler.getPay", messageLogin.getTask_id());
						taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
						tracer.addTag("parser.telecom.crawler.getPay.taskmobile", taskMobile.toString());
//						taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(),201, "数据采集中，【支付信息】无记录");
					}
					
				}
				else
				{
					tracer.addTag("parser.telecom.crawler.getPay.ERROR1",taskMobile.getTaskid());
//					taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_ERROR.getDescription());
					//c.updateTaskMobile(messageLogin.getTask_id());
				}
			}
			if(time==0)
			{
				tracer.addTag("parser.telecom.crawler.getPay.ERROR2",taskMobile.getTaskid());
				taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
				c.updateTaskMobile(messageLogin.getTask_id());
			}
			else
			{
				taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getPay.SUCCESS", messageLogin.getTask_id());
				c.updateTaskMobile(messageLogin.getTask_id());
			}
		} catch (Exception e) {
			tracer.addTag("parser.telecom.crawler.getPay.ERROR3",taskMobile.getTaskid());
			taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
			c.updateTaskMobile(messageLogin.getTask_id());
			e.printStackTrace();
		}
		
	}


	//账单
	@Async
	public void getWapBill(MessageLogin messageLogin, TaskMobile taskMobile) {
		try {
			int time=0;
			for (int i = 0; i < 6; i++) {
				WebParam<TelecomAnhuiBill> webParam = telecomAnhuiParser.getWapBill(messageLogin,taskMobile,i);
				if(null != webParam)
				{
					if(webParam.getTelecomAnhuiBill() != null)
					{
						TelecomAnhuiHtml telecomAnhuiHtml = new TelecomAnhuiHtml();
						telecomAnhuiHtml.setHtml(webParam.getHtml());
						telecomAnhuiHtml.setTaskid(messageLogin.getTask_id());
						telecomAnhuiHtml.setPageCount(1);
						telecomAnhuiHtml.setType("getWapBill");
						telecomAnhuiHtml.setUrl(webParam.getUrl());
						System.out.println(telecomAnhuiHtml);
						telecomAnhuiRepositoryHtml.save(telecomAnhuiHtml);
//						taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
						telecomAnhuiRepositoryBill.save(webParam.getTelecomAnhuiBill());
						tracer.addTag("parser.telecom.crawler.getWapBill.SUCCESS", messageLogin.getTask_id());
						time++;
					}
					else if(webParam.getList() != null)
					{
						TelecomAnhuiHtml telecomAnhuiHtml = new TelecomAnhuiHtml();
						telecomAnhuiHtml.setHtml(webParam.getHtml());
						telecomAnhuiHtml.setTaskid(messageLogin.getTask_id());
						telecomAnhuiHtml.setPageCount(1);
						telecomAnhuiHtml.setType("getWapBill");
						telecomAnhuiHtml.setUrl(webParam.getUrl());
						System.out.println(telecomAnhuiHtml);
						telecomAnhuiRepositoryHtml.save(telecomAnhuiHtml);
//						taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
						telecomAnhuiRepositoryBill.saveAll(webParam.getList());
						tracer.addTag("parser.telecom.crawler.getWapBill.SUCCESS", messageLogin.getTask_id());
						time++;
					}
					else
					{
						tracer.addTag("parser.telecom.crawler.getWapBill", messageLogin.getTask_id());
						taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
						tracer.addTag("parser.telecom.crawler.getWapBill.taskmobile", taskMobile.toString());
//						taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
					}
					
				}
				else
				{
					tracer.addTag("parser.telecom.crawler.getWapBill.ERROR1",taskMobile.getTaskid());
//					taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
					//c.updateTaskMobile(messageLogin.getTask_id());
				}
			}
			if(time==0)
			{
				tracer.addTag("parser.telecom.crawler.getWapBill.ERROR2",taskMobile.getTaskid());
				taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
				c.updateTaskMobile(messageLogin.getTask_id());
			}
			else
			{
				taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getWapBill.SUCCESS", messageLogin.getTask_id());
				c.updateTaskMobile(messageLogin.getTask_id());
			}
		} catch (Exception e) {
			tracer.addTag("parser.telecom.crawler.getWapBill.ERROR3",taskMobile.getTaskid());
			taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
			c.updateTaskMobile(messageLogin.getTask_id());
			e.printStackTrace();
		}
		
	}


	//积分
	@Async
	public void getWapScore(MessageLogin messageLogin, TaskMobile taskMobile) {
		try {
			int time=0;
			for (int i = 1; i < 7; i++) {
				WebParam<TelecomAnhuiScore> webParam = telecomAnhuiParser.getWapScore(messageLogin,taskMobile,i);
				if(null != webParam)
				{
					if(webParam.getHtml().contains("客户未评上星级,禁查禁兑"))
					{
//						taskMobileRepository.updateIntegralMsgStatus(messageLogin.getTask_id(),201, "数据采集中，【积分信息】无记录");
						tracer.addTag("parser.telecom.crawler.getWapScore.null", messageLogin.getTask_id());
					}
					else if(webParam.getTelecomAnhuiScore()!= null)
					{
						TelecomAnhuiHtml telecomAnhuiHtml = new TelecomAnhuiHtml();
						telecomAnhuiHtml.setHtml(webParam.getHtml());
						telecomAnhuiHtml.setTaskid(messageLogin.getTask_id());
						telecomAnhuiHtml.setPageCount(1);
						telecomAnhuiHtml.setType("getWapScore");
						telecomAnhuiHtml.setUrl(webParam.getUrl());
						System.out.println(telecomAnhuiHtml);
						telecomAnhuiRepositoryHtml.save(telecomAnhuiHtml);
//						taskMobileRepository.updateIntegralMsgStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
						telecomAnhuiRepositoryScore.save(webParam.getTelecomAnhuiScore());
						tracer.addTag("parser.telecom.crawler.getWapScore.SUCCESS", messageLogin.getTask_id());
						time++;
					}
					else
					{
						tracer.addTag("parser.telecom.crawler.getWapScore", messageLogin.getTask_id());
						taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
						tracer.addTag("parser.telecom.crawler.getWapScore.taskmobile", taskMobile.toString());
//						taskMobileRepository.updateIntegralMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
					}
					
				}
				else
				{
					tracer.addTag("parser.telecom.crawler.getWapScore.ERROR1",taskMobile.getTaskid());
//					taskMobileRepository.updateIntegralMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
					//c.updateTaskMobile(messageLogin.getTask_id());
				}
			}
			if(time==0)
			{
				tracer.addTag("parser.telecom.crawler.getWapScore.ERROR2",taskMobile.getTaskid());
				taskMobileRepository.updateIntegralMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
				c.updateTaskMobile(messageLogin.getTask_id());
			}
			else
			{
				taskMobileRepository.updateIntegralMsgStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getWapScore.SUCCESS", messageLogin.getTask_id());
				c.updateTaskMobile(messageLogin.getTask_id());
			}
		} catch (Exception e) {
			tracer.addTag("parser.telecom.crawler.getWapScore.ERROR3",taskMobile.getTaskid());
			taskMobileRepository.updateIntegralMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
			c.updateTaskMobile(messageLogin.getTask_id());
			e.printStackTrace();
		}
		
	}

	//业务
	@Async
	public void getWapBusiness(MessageLogin messageLogin, TaskMobile taskMobile) {
		try {
				WebParam<TelecomAnhuiBusiness> webParam = telecomAnhuiParser.getWapBusiness(messageLogin,taskMobile);
				if(null != webParam)
				{
					if(webParam.getTelecomAnhuiBusiness() != null)
					{
						TelecomAnhuiHtml telecomAnhuiHtml = new TelecomAnhuiHtml();
						telecomAnhuiHtml.setHtml(webParam.getHtml());
						telecomAnhuiHtml.setTaskid(messageLogin.getTask_id());
						telecomAnhuiHtml.setPageCount(1);
						telecomAnhuiHtml.setType("getWapBusiness");
						telecomAnhuiHtml.setUrl(webParam.getUrl());
						System.out.println(telecomAnhuiHtml);
						telecomAnhuiRepositoryHtml.save(telecomAnhuiHtml);
						taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getDescription());
						telecomAnhuiRepositoryBusiness.save(webParam.getTelecomAnhuiBusiness());
						tracer.addTag("parser.telecom.crawler.getWapBusiness.SUCCESS", messageLogin.getTask_id());
					}
					else if(webParam.getList() != null)
					{
						TelecomAnhuiHtml telecomAnhuiHtml = new TelecomAnhuiHtml();
						telecomAnhuiHtml.setHtml(webParam.getHtml());
						telecomAnhuiHtml.setTaskid(messageLogin.getTask_id());
						telecomAnhuiHtml.setPageCount(1);
						telecomAnhuiHtml.setType("getWapBusiness");
						telecomAnhuiHtml.setUrl(webParam.getUrl());
						System.out.println(telecomAnhuiHtml);
						telecomAnhuiRepositoryHtml.save(telecomAnhuiHtml);
						taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(),200, StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getDescription());
						telecomAnhuiRepositoryBusiness.saveAll(webParam.getList());
						tracer.addTag("parser.telecom.crawler.getWapBusiness.SUCCESS", messageLogin.getTask_id());
					}
					else{
						tracer.addTag("parser.telecom.crawler.getWapBusiness", messageLogin.getTask_id());
						taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
						tracer.addTag("parser.telecom.crawler.getWapBusiness.taskmobile", taskMobile.toString());
						taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getDescription());
					}
					
				}
				else
				{
					tracer.addTag("parser.telecom.crawler.getWapBusiness.ERROR1",taskMobile.getTaskid());
					taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getDescription());
				}
		} catch (Exception e) {
			tracer.addTag("parser.telecom.crawler.getWapBusiness.ERROR3",taskMobile.getTaskid());
			taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(),201, StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getDescription());
			e.printStackTrace();
		}
		c.updateTaskMobile(messageLogin.getTask_id());
	}

}
