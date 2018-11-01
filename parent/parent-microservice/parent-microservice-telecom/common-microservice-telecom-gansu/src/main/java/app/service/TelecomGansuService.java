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
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.mobile.MobileDataErrRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuBusiness;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuCall;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuHtml;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuMessage;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuPay;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuPhoneBill;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuPhoneMonthBill;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuScore;
import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuUserInfo;
import com.microservice.dao.repository.crawler.mobile.MobileDataErrRecRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.gansu.TelecomGansuBusinessRepository;
import com.microservice.dao.repository.crawler.telecom.gansu.TelecomGansuCallRepository;
import com.microservice.dao.repository.crawler.telecom.gansu.TelecomGansuHtmlRepository;
import com.microservice.dao.repository.crawler.telecom.gansu.TelecomGansuMessageRepository;
import com.microservice.dao.repository.crawler.telecom.gansu.TelecomGansuPayRepository;
import com.microservice.dao.repository.crawler.telecom.gansu.TelecomGansuPhoneBillRepository;
import com.microservice.dao.repository.crawler.telecom.gansu.TelecomGansuPhoneMonthBillRepository;
import com.microservice.dao.repository.crawler.telecom.gansu.TelecomGansuScoreRepository;
import com.microservice.dao.repository.crawler.telecom.gansu.TelecomGansuUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.TelecomGansuParser;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.gansu")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.gansu")
public class TelecomGansuService {
	@Autowired
	private CrawlerStatusMobileService c;
	@Autowired
	private TelecomGansuParser telecomGansuParser;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomGansuCallRepository telecomGansuCallRepository;
	@Autowired
	private TelecomGansuPayRepository telecomGansuPayRepository;
	@Autowired
	private TelecomGansuMessageRepository telecomGansuMessageRepository;
	@Autowired
	private TelecomGansuPhoneBillRepository telecomGansuPhoneBillRepository;
	@Autowired
	private TelecomGansuPhoneMonthBillRepository telecomGansuPhoneMonthBillRepository;
	@Autowired
	private TelecomGansuUserInfoRepository telecomGansuUserInfoRepository;
	@Autowired
	private TelecomGansuScoreRepository telecomGansuScoreRepository;
	@Autowired
	private TelecomGansuHtmlRepository telecomGansuHtmlRepository;
	@Autowired
	private TelecomGansuBusinessRepository telecomGansuBusinessRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private MobileDataErrRecRepository mobileDataErrRecRepository;

	// 获取个人界面
	@Async
	public void getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile, WebClient webClient) {
		try {
			tracer.addTag("parser.telecom.crawler.getUserInfo.start", messageLogin.getTask_id());
			WebParam<TelecomGansuUserInfo> webParam = telecomGansuParser.getUserTelecomInfo(messageLogin, taskMobile,webClient);
			if (null != webParam) {
				taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
				if (webParam.getTelecomGansuUserInfo() == null) {
					taskMobileRepository.updateUserMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
					tracer.addTag("parser.telecom.crawler.getUserInfo.ERROR1", messageLogin.getTask_id());
				} else {
					TelecomGansuHtml telecomGansuHtml = new TelecomGansuHtml();
					telecomGansuHtml.setUrl(webParam.getUrl());
					telecomGansuHtml.setPageCount(1);
					telecomGansuHtml.setType("getUserInfo");
					telecomGansuHtml.setHtml(webParam.getHtml());
					telecomGansuHtml.setTaskid(taskMobile.getTaskid());
					telecomGansuHtmlRepository.save(telecomGansuHtml);
					tracer.addTag("parser.telecom.crawler.getHtml", messageLogin.getTask_id());
					taskMobileRepository.updateUserMsgStatus(messageLogin.getTask_id(), 200,StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
					telecomGansuUserInfoRepository.save(webParam.getTelecomGansuUserInfo());
					tracer.addTag("parser.telecom.crawler.getUserInfo.SUCCESS", messageLogin.getTask_id());
				}
			} else {
				taskMobileRepository.updateUserMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getUserInfo.ERROR2", messageLogin.getTask_id());
			}

		} catch (Exception e) {
			taskMobileRepository.updateUserMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
			tracer.addTag("parser.telecom.crawler.getUserInfo.ERROR3", messageLogin.getTask_id());
			e.printStackTrace();
		}
		c.updateTaskMobile(messageLogin.getTask_id());
	}

	// 积分信息
	@Async
	public void getJiFen(MessageLogin messageLogin, TaskMobile taskMobile,WebClient webClient) {
		try {
			tracer.addTag("parser.telecom.crawler.jiFenInfo.start", messageLogin.getTask_id());
			WebParam<TelecomGansuScore> webParam = telecomGansuParser.getJiFenInfo(messageLogin, taskMobile,webClient);
			if (null != webParam) {
				taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
				if(webParam.getHtml().contains("当前可用积分"))
				{
					TelecomGansuHtml telecomGansuHtml = new TelecomGansuHtml();
					telecomGansuHtml.setUrl(webParam.getUrl());
					telecomGansuHtml.setPageCount(1);
					telecomGansuHtml.setType("jiFenInfo");
					telecomGansuHtml.setHtml(webParam.getHtml());
					telecomGansuHtml.setTaskid(taskMobile.getTaskid());
					telecomGansuHtmlRepository.save(telecomGansuHtml);
					tracer.addTag("parser.telecom.crawler.getHtml.sum", messageLogin.getTask_id());
					taskMobileRepository.updateIntegralMsgStatus(messageLogin.getTask_id(), 200,StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
					telecomGansuScoreRepository.save(webParam.getTelecomGansuScore());
					tracer.addTag("parser.telecom.crawler.jiFenInfo.SUCCESS.sum", messageLogin.getTask_id());
				}
				else if (webParam.getList() == null) {
					taskMobileRepository.updateIntegralMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
					tracer.addTag("parser.telecom.crawler.jiFenInfo.ERROR1", messageLogin.getTask_id());
				}
				else {
					TelecomGansuHtml telecomGansuHtml = new TelecomGansuHtml();
					telecomGansuHtml.setUrl(webParam.getUrl());
					telecomGansuHtml.setPageCount(1);
					telecomGansuHtml.setType("jiFenInfo");
					telecomGansuHtml.setHtml(webParam.getHtml());
					telecomGansuHtml.setTaskid(taskMobile.getTaskid());
					telecomGansuHtmlRepository.save(telecomGansuHtml);
					tracer.addTag("parser.telecom.crawler.getHtml", messageLogin.getTask_id());
					taskMobileRepository.updateIntegralMsgStatus(messageLogin.getTask_id(), 200,StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
					telecomGansuScoreRepository.saveAll(webParam.getList());
					tracer.addTag("parser.telecom.crawler.jiFenInfo.SUCCESS", messageLogin.getTask_id());
				}
			} else {
				taskMobileRepository.updateIntegralMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.jiFenInfo.ERROR2", messageLogin.getTask_id());
			}

		} catch (Exception e) {
			taskMobileRepository.updateIntegralMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getDescription());
			tracer.addTag("parser.telecom.crawler.jiFenInfo.ERROR3", messageLogin.getTask_id());
			e.printStackTrace();
		}
		c.updateTaskMobile(messageLogin.getTask_id());
	}

	// 业务信息
	@Async
	public void getBusiness(MessageLogin messageLogin, TaskMobile taskMobile,WebClient webClient) {
		try {
			tracer.addTag("parser.telecom.crawler.getBusiness.start", messageLogin.getTask_id());
			WebParam<TelecomGansuBusiness> webParam = telecomGansuParser.getBusinessInfo(messageLogin, taskMobile,webClient);
			if (null != webParam) {
				taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
				if (webParam.getList() == null) {
					taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getDescription());
					tracer.addTag("parser.telecom.crawler.getBusiness.ERROR1", messageLogin.getTask_id());
				} else {
					TelecomGansuHtml telecomGansuHtml = new TelecomGansuHtml();
					telecomGansuHtml.setUrl(webParam.getUrl());
					telecomGansuHtml.setPageCount(1);
					telecomGansuHtml.setType("getBusiness");
					telecomGansuHtml.setHtml(webParam.getHtml());
					telecomGansuHtml.setTaskid(taskMobile.getTaskid());
					telecomGansuHtmlRepository.save(telecomGansuHtml);
					tracer.addTag("parser.telecom.crawler.getHtml", messageLogin.getTask_id());
					taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(), 200,StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getDescription());
					telecomGansuBusinessRepository.saveAll(webParam.getList());
					tracer.addTag("parser.telecom.crawler.getBusiness.SUCCESS", messageLogin.getTask_id());
				}
			} else {
				taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getBusiness.ERROR2", messageLogin.getTask_id());
			}
		} catch (Exception e) {
			taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getDescription());
			tracer.addTag("parser.telecom.crawler.getBusiness.ERROR3", messageLogin.getTask_id());
			e.printStackTrace();
		}
		c.updateTaskMobile(messageLogin.getTask_id());
	}

	
	// 缴费信息 和 账单
	@Async
	public void getPayMent(MessageLogin messageLogin, TaskMobile taskMobile, WebClient webClient) {
		int time = 0;
		try {
			for (int i = 1; i < 7; i++) {
				WebParam<TelecomGansuPay> webParam = telecomGansuParser.getPayMentInfo(messageLogin, taskMobile,webClient, i);
				tracer.addTag("parser.telecom.crawler.getPayMent", messageLogin.getTask_id());
				if (null != webParam) {
					if (webParam.getList()!= null) {
						TelecomGansuHtml telecomGansuHtml = new TelecomGansuHtml();
						telecomGansuHtml.setUrl(webParam.getUrl());
						telecomGansuHtml.setPageCount(1);
						telecomGansuHtml.setType("getPayMent");
						telecomGansuHtml.setHtml(webParam.getHtml());
						telecomGansuHtml.setTaskid(taskMobile.getTaskid());
						telecomGansuHtmlRepository.save(telecomGansuHtml);
						tracer.addTag("parser.telecom.crawler.getHtml", messageLogin.getTask_id());
						telecomGansuPayRepository.saveAll(webParam.getList());
						time++;
					} else {
						tracer.addTag("parser.telecom.crawler.getPayMent.ERROR1", messageLogin.getTask_id());
					}
				} else {
					tracer.addTag("parser.telecom.crawler.getPayMent.ERROR2", messageLogin.getTask_id());
				}
			}
			if (time>0) {
				taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(), 200,StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getPayMent.SUCCESS" + time, messageLogin.getTask_id());
			} else {
				taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getPayMent.ERROR3" + time, messageLogin.getTask_id());
			}
		} catch (Exception e) {
			taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
			tracer.addTag("parser.telecom.crawler.getPayMent.TIMEOUT" + time, messageLogin.getTask_id());
			e.printStackTrace();
		}
		c.updateTaskMobile(messageLogin.getTask_id());
	}

	// 短信账单
	@Async
	public void getMessage(MessageLogin messageLogin, TaskMobile taskMobile, WebClient client) {
		int a = 0;
		int i = 0;
			try {
				for (i = 0; i < 6; i++) {
				WebParam<TelecomGansuMessage> webParam = telecomGansuParser.getMessageInfo(messageLogin, taskMobile,
						client, i);
				if (null != webParam) {
					if (webParam.getList() == null) {
						
//						MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), "短信记录",getFirstDay("yyyyMM",i),taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时", 1);
//						mobileDataErrRecRepository.save(m);
						// taskMobileRepository.updateSMSRecordStatus(messageLogin.getTask_id(),
						// 201,
						// StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
						tracer.addTag("parser.telecom.crawler.getMessage.ERROR1", messageLogin.getTask_id());
					} else {
						a++;
						TelecomGansuHtml telecomGansuHtml = new TelecomGansuHtml();
						telecomGansuHtml.setUrl(webParam.getUrl());
						telecomGansuHtml.setPageCount(1);
						telecomGansuHtml.setType("getMessage");
						telecomGansuHtml.setHtml(webParam.getHtml());
						telecomGansuHtml.setTaskid(taskMobile.getTaskid());
						telecomGansuHtmlRepository.save(telecomGansuHtml);
						tracer.addTag("parser.telecom.crawler.getHtml", messageLogin.getTask_id());
						// taskMobileRepository.updateSMSRecordStatus(messageLogin.getTask_id(),200,
						// StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
						telecomGansuMessageRepository.saveAll(webParam.getList());
						tracer.addTag("parser.telecom.crawler.getMessage.SUCCESS",i+webParam.getHtml()+ messageLogin.getTask_id());
					}
				} else {
					// taskMobileRepository.updateSMSRecordStatus(messageLogin.getTask_id(),201,
					// StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
					MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), "短信记录",getFirstDay("yyyyMM",i),taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时", 1);
					mobileDataErrRecRepository.save(m);
					tracer.addTag("parser.telecom.crawler.getMessage.ERROR2", i+messageLogin.getTask_id());
				  }
				}
				if (a>0) {
					taskMobileRepository.updateSMSRecordStatus(messageLogin.getTask_id(), 200,StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
					tracer.addTag("parser.telecom.crawler.getMessage.SUCCESS",i+messageLogin.getTask_id());
				} else {
					taskMobileRepository.updateSMSRecordStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
					tracer.addTag("parser.telecom.crawler.getMessage.ERROR3",i+ messageLogin.getTask_id());
				}
			} catch (Exception e) {
				MobileDataErrRec m = null;
				try {
					m = new MobileDataErrRec(taskMobile.getTaskid(), "短信记录",getFirstDay("yyyyMM",i),taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时", 1);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mobileDataErrRecRepository.save(m);
				// taskMobileRepository.updateSMSRecordStatus(messageLogin.getTask_id(),201,
				// StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
				taskMobileRepository.updateSMSRecordStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getMessage.TIMEOUT",i+ messageLogin.getTask_id());
				e.printStackTrace();
			}
		    c.updateTaskMobile(messageLogin.getTask_id());
	}

	
	public String getFirstDay(String fmt,int i) throws Exception{
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -i);  
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}
	
	// 语音账单
	@Async
	public void getCall(MessageLogin messageLogin, TaskMobile taskMobile, WebClient client) {
		
		int a = 0;
		int j = 0;
		tracer.addTag("parser.telecom.crawler.getCall.start", messageLogin.getTask_id());
			try {
				for (j = 0; j < 6; j++) {
				WebParam<TelecomGansuCall> webParam = telecomGansuParser.getCallInfo(messageLogin, taskMobile, client,j);
				if (null != webParam) {
					if (webParam.getList() == null) {
						// taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(),
						// 201,
						// StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
//						MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录",getFirstDay("yyyyMM",j), taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时", 1);
//						mobileDataErrRecRepository.save(m);
						tracer.addTag("parser.telecom.crawler.getCall.ERROR1"+j, messageLogin.getTask_id());
					} else {
						TelecomGansuHtml telecomGansuHtml = new TelecomGansuHtml();
						telecomGansuHtml.setUrl(webParam.getUrl());
						telecomGansuHtml.setPageCount(1);
						telecomGansuHtml.setType("getCall");
						telecomGansuHtml.setHtml(webParam.getHtml());
						telecomGansuHtml.setTaskid(taskMobile.getTaskid());
						telecomGansuHtmlRepository.save(telecomGansuHtml);
						tracer.addTag("parser.telecom.crawler.getHtml", messageLogin.getTask_id());
						// taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(),200,
						// StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
						
						telecomGansuCallRepository.saveAll(webParam.getList());
						tracer.addTag("parser.telecom.crawler.getCall.SUCCESS。single", j+messageLogin.getTask_id());
						a++;
					}
				} else {
					// taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(),
					// 201,
					// StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
					MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录",getFirstDay("yyyyMM",j), taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时", 1);
					mobileDataErrRecRepository.save(m);
					tracer.addTag("parser.telecom.crawler.getCall.ERROR2", j+messageLogin.getTask_id());
				}
				}if (a>0) {
					taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 200,StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
					tracer.addTag("parser.telecom.crawler.getCall.SUCCESS", j+messageLogin.getTask_id());
				} else {
					taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
					tracer.addTag("parser.telecom.crawler.getCall.ERROR.3", j+messageLogin.getTask_id());
				}
			} catch (Exception e) {
				MobileDataErrRec m = null;
				try {
					m = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录",getFirstDay("yyyyMM",j), taskMobile.getCarrier(),taskMobile.getCity(), "INCOMPLETE", "系统超时", 1);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mobileDataErrRecRepository.save(m);
				// taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(),
				// 201,
				// StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
				taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getCall.TIMEOUT",j+ messageLogin.getTask_id());
				e.printStackTrace();
			}
		    c.updateTaskMobile(messageLogin.getTask_id());
	}

	// 账单
	@Async
	public void getPhoneBillInfo(MessageLogin messageLogin, TaskMobile taskMobile, WebClient webClient) {
		try {
			tracer.addTag("parser.telecom.crawler.getPhoneBillInfo.start", messageLogin.getTask_id());
			WebParam<TelecomGansuPhoneBill> webParam = telecomGansuParser.getPhoneBillInfo(messageLogin, taskMobile,webClient);
			if (null != webParam) {
				if (webParam.getList() == null) {
					taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
					tracer.addTag("parser.telecom.crawler.getPhoneBillInfo.ERROR1", messageLogin.getTask_id());
				} else {
					TelecomGansuHtml telecomGansuHtml = new TelecomGansuHtml();
					telecomGansuHtml.setUrl(webParam.getUrl());
					telecomGansuHtml.setPageCount(1);
					telecomGansuHtml.setType("getPhoneBillInfo");
					telecomGansuHtml.setHtml(webParam.getHtml());
					telecomGansuHtml.setTaskid(taskMobile.getTaskid());
					telecomGansuHtmlRepository.save(telecomGansuHtml);
					tracer.addTag("parser.telecom.crawler.getHtml", messageLogin.getTask_id());
					taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(), 200,StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
					telecomGansuPhoneBillRepository.saveAll(webParam.getList());
					tracer.addTag("parser.telecom.crawler.getPhoneBillInfo.SUCCESS", messageLogin.getTask_id());
				}
			} else {
				taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
				tracer.addTag("parser.telecom.crawler.getPhoneBillInfo.ERROR2", messageLogin.getTask_id());
			}
		} catch (Exception e) {
			taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
			tracer.addTag("parser.telecom.crawler.getPhoneBillInfo.ERROR3", messageLogin.getTask_id());
			e.printStackTrace();
		}
		c.updateTaskMobile(messageLogin.getTask_id());
	}

	// 月账单
	@Async
	public void getPhoneMonthBillInfo(MessageLogin messageLogin, TaskMobile taskMobile, WebClient webClient) {
		    int a = 0;
			try {
				for (int i = 1; i < 6; i++) {
				tracer.addTag("parser.telecom.crawler.getPhoneMonthBillInfo.start", messageLogin.getTask_id());
				WebParam<TelecomGansuPhoneMonthBill> webParam = telecomGansuParser.getPhoneMonthBillInfo(messageLogin,taskMobile, i, webClient);
				if (null != webParam) {
					if (webParam.getList() == null) {
						// taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(),201,
						// "数据采集中，【月账单明细】采集完成");
						tracer.addTag("parser.telecom.crawler.getPhoneMonthBillInfo.ERROR1", messageLogin.getTask_id());
					} else {
						a++;
						TelecomGansuHtml telecomGansuHtml = new TelecomGansuHtml();
						telecomGansuHtml.setUrl(webParam.getUrl());
						telecomGansuHtml.setPageCount(1);
						telecomGansuHtml.setType("getPhoneMonthBillInfo");
						telecomGansuHtml.setHtml(webParam.getHtml());
						telecomGansuHtml.setTaskid(taskMobile.getTaskid());
						telecomGansuHtmlRepository.save(telecomGansuHtml);
						tracer.addTag("parser.telecom.crawler.getHtml", messageLogin.getTask_id());
						// taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(),200,
						// "数据采集中，【月账单明细】采集完成");
						telecomGansuPhoneMonthBillRepository.saveAll(webParam.getList());
						tracer.addTag("parser.telecom.crawler.getPhoneMonthBillInfo.SUCCESS",messageLogin.getTask_id());
					}
					
				} else {
					// taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(),201,
					// "数据采集中，【月账单明细】采集完成");
					tracer.addTag("parser.telecom.crawler.getPhoneMonthBillInfo.ERROR2", messageLogin.getTask_id());
				}
				}if (a>0) {
					// taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(),
					tracer.addTag("parser.telecom.crawler.getPhoneMonthBillInfoIng.SUCCESS" + a, messageLogin.getTask_id());
				} else {
					tracer.addTag("parser.telecom.crawler.getPhoneMonthBillInfo.ERROR3" + a, messageLogin.getTask_id());
					// taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(),200,
				}
			} catch (Exception e) {
				// taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(),201,
				// "数据采集中，【月账单明细】采集完成");
				e.printStackTrace();
			}
		    c.updateTaskMobile(messageLogin.getTask_id());
	}

	//亲情号码
	@Async
	public void getFamilyMsg(MessageLogin messageLogin, TaskMobile taskMobile, WebClient webClient) {
		 taskMobile.setFamilyMsgStatus(201);
		 taskMobileRepository.updateFamilyMsgStatus(messageLogin.getTask_id(), 201,StatusCodeEnum.TASKMOBILE_CRAWLER_FAMILY_MSG_SUCCESS.getDescription());
		 c.updateTaskMobile(messageLogin.getTask_id());
	}
}
