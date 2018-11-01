package app.service;

import java.net.URL;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeClass;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.mobile.MobileDataErrRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.beijing.TelecomBeijingCallThremResult;
import com.microservice.dao.entity.crawler.telecom.beijing.TelecomBeijingChargesResult;
import com.microservice.dao.entity.crawler.telecom.beijing.TelecomBeijingIntegraResult;
import com.microservice.dao.entity.crawler.telecom.beijing.TelecomBeijingPayResult;
import com.microservice.dao.entity.crawler.telecom.beijing.TelecomBeijingPhoneBill;
import com.microservice.dao.entity.crawler.telecom.beijing.TelecomBeijingSMSThremResult;
import com.microservice.dao.entity.crawler.telecom.beijing.TelecomBeijingUserInfo;
import com.microservice.dao.repository.crawler.mobile.MobileDataErrRecRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.beijing.TelecomBeijingCallThremResultRepository;
import com.microservice.dao.repository.crawler.telecom.beijing.TelecomBeijingChargesResultRepository;
import com.microservice.dao.repository.crawler.telecom.beijing.TelecomBeijingIntegraResultRepository;
import com.microservice.dao.repository.crawler.telecom.beijing.TelecomBeijingPayResultRepository;
import com.microservice.dao.repository.crawler.telecom.beijing.TelecomBeijingPhoneBillRepository;
import com.microservice.dao.repository.crawler.telecom.beijing.TelecomBeijingSMSThremResultRepository;
import com.microservice.dao.repository.crawler.telecom.beijing.TelecomBeijingUserInfoRepository;

import app.bean.CallThremBean;
import app.bean.SMSThremBean;
import app.commontracerlog.TracerLog;
import app.service.crawler.telecom.htmlparse.TelecomParseBeijingService;

@Component
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.beijing")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.beijing")
public class TelecomUnitBeiJingService {

	@Autowired
	private TelecomBeijingUserInfoRepository telecomBeijingUserInfoRepository;

	@Autowired
	private TelecomBeijingPhoneBillRepository telecomBeijingPhoneBillRepository;

	@Autowired
	private TelecomBeijingPayResultRepository telecomBeijingPayResultRepository;

	@Autowired
	private TelecomBeijingIntegraResultRepository telecomBeijingIntegraResultRepository;

	@Autowired
	private TelecomBeijingCallThremResultRepository telecomBeijingCallThremResultRepository;

	@Autowired
	private TelecomBeijingChargesResultRepository telecomBeijingChargesResultRepository;

	@Autowired
	private TelecomBeijingSMSThremResultRepository telecomBeijingSMSThremResultRepository;

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	TelecomParseBeijingService telecomParseBeijingService;
	
	@Autowired
	private MobileDataErrRecRepository mobileDataErrRecRepository;
	
	@Autowired
	private TelecomHtmlService telecomHtmlService;
	
	
	

	// 抓取北京用户个人信息
	@Async
	public String getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile, int i) {

		String html = telecomHtmlService.getUserInfo(messageLogin, taskMobile, i);
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		tracerLog.output2("中国电信抓取客户北京用户基本信息", "<xmp>" + html + "</xmp>");

		tracerLog.output("中国电信抓取客户北京用户基本信息 taskMobile",taskMobile.toString());
		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_PAY_MSG(5).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_PAY_MSG(5).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_PAY_MSG(5).getDescription());
			taskMobile.setUserMsgStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
			taskMobile = taskMobileRepository.save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}

		TelecomBeijingUserInfo rootresultcall = null;
		try {
			rootresultcall = telecomParseBeijingService.userinfo_parse(html);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (rootresultcall == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getDescription());
			taskMobile.setUserMsgStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());

			taskMobile = taskMobileRepository.save(taskMobile);
			return null;
		}

		rootresultcall.setUserid(messageLogin.getUser_id());
		rootresultcall.setTaskid(taskMobile.getTaskid());
		save(rootresultcall);
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getDescription());

		taskMobile.setUserMsgStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());

		taskMobile = taskMobileRepository.save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		tracerLog.output("中国电信抓取客户北京用户基本信息 存储完成", messageLogin.getTask_id());
		return "sucess";
	}

	// 获取北京用户 往月话费账单
	@Async
	public Object getphoneBill(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i) {

		tracerLog.output("中国电信抓取客户北京用户 往月话费账单", messageLogin.getTask_id());

		String html = telecomHtmlService.getphoneBill(webClient, messageLogin, taskMobile);// 获取通话详单html

		tracerLog.output2("中国电信抓取客户北京用户 往月话费账单", "<xmp>" + html + "</xmp>");

		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());

			taskMobile.setAccountMsgStatus(StatusCodeRec.CRAWLER_AccountMsgStatus_ERROR.getCode());

			taskMobile.setBusinessMsgStatus(StatusCodeRec.CRAWLER_BusinessMsgStatus_SUCESS.getCode());
			taskMobile.setFamilyMsgStatus(StatusCodeRec.CRAWLER_FamilyMsgStatus_SUCESS.getCode());
			taskMobile = taskMobileRepository.save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}
		List<TelecomBeijingPhoneBill> rootresult = null;
		try {
			rootresult = telecomParseBeijingService.phoneBill_parse(html);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (rootresult == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());
			taskMobile.setAccountMsgStatus(StatusCodeRec.CRAWLER_AccountMsgStatus_ERROR.getCode());

			taskMobile.setBusinessMsgStatus(StatusCodeRec.CRAWLER_BusinessMsgStatus_SUCESS.getCode());
			taskMobile.setFamilyMsgStatus(StatusCodeRec.CRAWLER_FamilyMsgStatus_SUCESS.getCode());
			taskMobile = taskMobileRepository.save(taskMobile);
			tracerLog.output("getphoneBill  taskMobile", taskMobile.toString());
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}

		// 保存前做去重处理
		if (rootresult != null) {
			tracerLog.output("中国电信抓取客户北京用户 往月话费账单 sucess", rootresult.size() + "条");
			for (TelecomBeijingPhoneBill resultset : rootresult) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());

				save(resultset);
			}
		}

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setAccountMsgStatus(StatusCodeRec.CRAWLER_AccountMsgStatus_SUCESS.getCode());
		taskMobile.setBusinessMsgStatus(StatusCodeRec.CRAWLER_BusinessMsgStatus_SUCESS.getCode());
		taskMobile.setFamilyMsgStatus(StatusCodeRec.CRAWLER_FamilyMsgStatus_SUCESS.getCode());
		taskMobile = taskMobileRepository.save(taskMobile);
		tracerLog.output("getphoneBill  taskMobile",taskMobile.toString());
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return rootresult;

	}

	// 获取北京用户 缴费信息
	public Object getpayResult(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i) {

		tracerLog.output("中国电信抓取客户北京用户  缴费信息", messageLogin.getTask_id());

		String html = telecomHtmlService.getpayResult(webClient, messageLogin, taskMobile, i, 0);

		tracerLog.output2("中国电信抓取客户北京用户  缴费信息", "<xmp>" + html + "</xmp>");

		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());

			taskMobile.setPayMsgStatus(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getCode());

			taskMobile = taskMobileRepository.save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			tracerLog.output("getpayResult  taskMobile",taskMobile.toString());
			return null;
		}
		List<TelecomBeijingPayResult> rootresult = null;
		try {
			rootresult = telecomParseBeijingService.payResult_parse(html);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (rootresult == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());
			taskMobile = taskMobileRepository.save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}
		tracerLog.output("中国电信抓取客户北京用户 缴费信息", messageLogin.getTask_id());

		// 保存前做去重处理
		if (rootresult != null) {
			for (TelecomBeijingPayResult resultset : rootresult) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());

				save(resultset);
			}
		}
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());
		taskMobile = taskMobileRepository.save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return rootresult;

	}

	// 获取北京用户 积分增长
	@Async
	public Object getintegraResult(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i) {

		tracerLog.output("中国电信抓取客户北京用户   积分增长", messageLogin.getTask_id());

		String html = telecomHtmlService.getintegraResult(webClient, messageLogin, taskMobile, 0);

		tracerLog.output2("中国电信抓取客户北京用户   积分增长", "<xmp>" + html + "</xmp>");

		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());
			taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			taskMobile = taskMobileRepository.save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}

		List<TelecomBeijingIntegraResult> rootresult = null;
		try {
			rootresult = telecomParseBeijingService.integraResult_parse(html);
		} catch (Exception e) {
			e.printStackTrace();
		}


		tracerLog.output("getintegraResult  taskMobile", taskMobile.toString());
		if (rootresult == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());
			taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());

			taskMobile = taskMobileRepository.save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}
		tracerLog.output("中国电信抓取客户北京用户  积分增长", messageLogin.getTask_id());

		// 保存前做去重处理
		if (rootresult != null) {
			for (TelecomBeijingIntegraResult resultset : rootresult) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				save(resultset);
			}
		}

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_SUCESS.getCode());
		taskMobile = taskMobileRepository.save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return rootresult;

	}

	// 获取北京用户话费余额
	@Async
	public Object getChargesResult(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i) {

		tracerLog.output("中国电信抓取获取北京用户话费余额", messageLogin.getTask_id());

		String html = telecomHtmlService.getChargesResult(webClient, messageLogin, taskMobile, 0);

		tracerLog.output2("中国电信抓取获取北京用户话费余额", "<xmp>" + html + "</xmp>");

		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());

			taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_Balance_ERROR.getCode());
			taskMobile = taskMobileRepository.save(taskMobile);
			tracerLog.output("getChargesResult  taskMobile",taskMobile.toString());
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}

		TelecomBeijingChargesResult rootresult = null;
		try {
			rootresult = telecomParseBeijingService.chargesResult_parse(html);
		} catch (Exception e) {
			e.printStackTrace();
		}

		tracerLog.output("getChargesResult  taskMobile",taskMobile.toString());
		if (rootresult == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());
			if (taskMobile.getIntegralMsgStatus() != null && taskMobile.getIntegralMsgStatus() != 200) {
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_Balance_ERROR.getCode());
			} else {
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_Balance_ERROR.getCode());
			}
			taskMobile = taskMobileRepository.save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}
		tracerLog.output("中国电信抓取获取北京用户话费余额", messageLogin.getTask_id());

		// 保存前做去重处理
		if (rootresult != null) {
			rootresult.setUserid(messageLogin.getUser_id());
			rootresult.setTaskid(taskMobile.getTaskid());
			save(rootresult);
		}

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		if (taskMobile.getIntegralMsgStatus() != null && taskMobile.getIntegralMsgStatus() != 200) {
			taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_Balance_SUCESS.getCode());
		} else {
			taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_Balance_SUCESS.getCode());
		}
		taskMobile = taskMobileRepository.save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return rootresult;

	}

	// 获取北京用户 通话详单
	public String getCallThrem(WebClient webClientCookies, MessageLogin messageLogin, TaskMobile taskMobile, String stardate,String enddate,String month,
			int pagenum) {
		tracerLog.output("中国电信抓取客户北京用户   通话详单", messageLogin.getTask_id());

		String html = null;
		try{
			 html =  telecomHtmlService.getCallThrem(webClientCookies, messageLogin, taskMobile, stardate,enddate, month, pagenum);
			tracerLog.output2("中国电信抓取客户北京用户"+month+"通话详单", "<xmp>" + html + "</xmp>");

		}catch(Exception e){
			tracerLog.output("中国电信抓取客户北京用户"+month+"通话详单", e.getMessage());
			html = null;
		}
		if (html == null) {
			tracerLog.output("中国电信抓取客户北京用户"+month+"通话详单", "html == null");
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
//			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
//			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription("数据采集中，通话详单信息"+month+"月已采集完成");
			taskMobile = taskMobileRepository.save(taskMobile);
			MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录", month,
					taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE","当月返回html为null", 0);
			mobileDataErrRecRepository.save(dataErrRec);
			return null;
		}

		CallThremBean rootresultcall = null;
		try {
			rootresultcall = telecomParseBeijingService.callThrem_parse(html);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (rootresultcall == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setDescription("数据采集中，通话详单信息"+month+"月已采集完成");
			taskMobile = taskMobileRepository.save(taskMobile);
			return null;
		}

		List<TelecomBeijingCallThremResult> rootresult = rootresultcall.getResult();
		if (rootresult == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		
			taskMobile.setDescription("数据采集中，通话详单信息"+month+"月已采集完成");
			taskMobile = taskMobileRepository.save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}
		tracerLog.output("中国电信抓取客户北京用户 通话详单", messageLogin.getTask_id());

		// 保存前处理
		if (rootresult != null) {
			for (TelecomBeijingCallThremResult resultset : rootresult) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				save(resultset);
			}
		}

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
//		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
//		taskMobile.setPhase_status("CRAWLER_CHECK_MSG");
		taskMobile.setDescription("数据采集中，通话详单信息"+month+"月已采集完成");
		taskMobile = taskMobileRepository.save(taskMobile);
//		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return rootresultcall.getPagenum() + "";

	}

	// 获取北京用户 通话详单
	@Async
	public Future<String> getCallThremByAsync(WebClient webClientCookies, MessageLogin messageLogin, TaskMobile taskMobile,
			String stardate,String enddate,String month, int pagenum,String smsCode) {
		tracerLog.output("中国电信抓取客户北京用户"+month+"通话详单", messageLogin.getTask_id());
	
		
//		http://bj.189.cn/iframe/feequery/detailValidCode.action
//		webClientCookies = lognAndGetBeijingUnitService.smsForCall(webClientCookies, 0, messageLogin, smsCode);

		String html = null;
		try{
			 html =  telecomHtmlService.getCallThrem(webClientCookies, messageLogin, taskMobile,stardate,enddate,month, pagenum);
			tracerLog.output2("中国电信抓取客户北京用户"+month+"通话详单", "<xmp>" + html + "</xmp>");

		}catch(Exception e){
			tracerLog.output("中国电信抓取客户北京用户"+month+"通话详单 抛出异常", e.getMessage());
			html = null;
		}


		if (html == null) {
			tracerLog.output("中国电信抓取客户北京用户"+month+"通话详单", "html == null");
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setDescription("数据采集中，通话详单信息"+month+"月已采集完成");
			taskMobile = taskMobileRepository.save(taskMobile);
			MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录", month,
					taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE","未获取html", 0);
			mobileDataErrRecRepository.save(dataErrRec);
			return new AsyncResult<String>("error");	

		}

		CallThremBean rootresultcall = null;
		try {
			rootresultcall = telecomParseBeijingService.callThrem_parse(html);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (rootresultcall == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setDescription("数据采集中，通话详单信息"+month+"月已采集完成");
			taskMobile = taskMobileRepository.save(taskMobile);
			return new AsyncResult<String>("error");	


		}

		List<TelecomBeijingCallThremResult> rootresult = rootresultcall.getResult();
		if (rootresult == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setDescription("数据采集中，通话详单信息"+month+"月已采集完成");
			taskMobile = taskMobileRepository.save(taskMobile);
			return new AsyncResult<String>("error");	

		}
		tracerLog.output("中国电信抓取客户北京用户 通话详单", messageLogin.getTask_id());

		// 保存前做去重处理
		if (rootresult != null) {
			for (TelecomBeijingCallThremResult resultset : rootresult) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				save(resultset);
			}
		}

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setDescription("数据采集中，通话详单信息"+month+"月已采集完成");
		taskMobile = taskMobileRepository.save(taskMobile);
		return new AsyncResult<String>("sucess");	

	}

	// 获取北京用户 短信详单
	public String getSMSThrem(WebClient webClientCookies, MessageLogin messageLogin, TaskMobile taskMobile, int i,
			int pagenum) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.output("中国电信抓取客户北京用户   短信详单", messageLogin.getTask_id());

		String html = null;
		try {
			html = telecomHtmlService.getSMSThrem(webClientCookies, messageLogin, taskMobile, i, pagenum);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		tracerLog.output2("中国电信抓取客户北京用户 短信详单", "<xmp>" + html + "</xmp>");

		if (html == null) {
			tracerLog.output("中国电信抓取客户北京用户  短信详单", "html == null");
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getDescription());
			taskMobile = taskMobileRepository.save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());

			return null;
		}

		SMSThremBean rootresultcall = null;
		try {
			rootresultcall = telecomParseBeijingService.sms_parse(html);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (rootresultcall == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getDescription());
			taskMobile = taskMobileRepository.save(taskMobile);
			taskMobile = taskMobileRepository.save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;

		}

		List<TelecomBeijingSMSThremResult> rootresult = rootresultcall.getResult();
		if (rootresult == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getDescription());
			taskMobile = taskMobileRepository.save(taskMobile);
			taskMobile = taskMobileRepository.save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}
		tracerLog.output("中国电信抓取客户北京用户  短信详单", messageLogin.getTask_id());

		// 保存前做去重处理
		if (rootresult != null) {
			for (TelecomBeijingSMSThremResult resultset : rootresult) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				save(resultset);
			}
		}

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getDescription());
		taskMobile = taskMobileRepository.save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());

		return rootresultcall.getPagenum() + "";

	}

	private void save(TelecomBeijingSMSThremResult result) {
		telecomBeijingSMSThremResultRepository.save(result);
	}

	private void save(TelecomBeijingChargesResult result) {
		telecomBeijingChargesResultRepository.save(result);
	}

	private void save(TelecomBeijingCallThremResult result) {
		telecomBeijingCallThremResultRepository.save(result);
	}

	private void save(TelecomBeijingIntegraResult result) {
		telecomBeijingIntegraResultRepository.save(result);
	}

	private void save(TelecomBeijingPayResult result) {
		telecomBeijingPayResultRepository.save(result);
	}

	private void save(TelecomBeijingPhoneBill result) {
		telecomBeijingPhoneBillRepository.save(result);
	}

	private void save(TelecomBeijingUserInfo result) {
		telecomBeijingUserInfoRepository.save(result);
	}


	public HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
}