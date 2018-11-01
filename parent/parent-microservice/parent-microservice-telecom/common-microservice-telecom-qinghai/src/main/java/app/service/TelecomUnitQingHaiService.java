package app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeClass;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.mobile.MobileDataErrRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.qinghai.TelecomQingHaiIntergentResult;
import com.microservice.dao.entity.crawler.telecom.qinghai.TelecomQingHaiPayResult;
import com.microservice.dao.entity.crawler.telecom.qinghai.TelecomQingHaiBillResult;
import com.microservice.dao.entity.crawler.telecom.qinghai.TelecomQingHaiCallThremResult;
import com.microservice.dao.entity.crawler.telecom.qinghai.TelecomQingHaiSMSThremResult;
import com.microservice.dao.entity.crawler.telecom.qinghai.TelecomQingHaiUserInfoResult;
import com.microservice.dao.repository.crawler.mobile.MobileDataErrRecRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.qinghai.TelecomQingHaiIntergentResultRepository;
import com.microservice.dao.repository.crawler.telecom.qinghai.TelecomQingHaiPayResultRepository;
import com.microservice.dao.repository.crawler.telecom.qinghai.TelecomQingHaiBillResultRepository;
import com.microservice.dao.repository.crawler.telecom.qinghai.TelecomQingHaiCallThremResultRepository;
import com.microservice.dao.repository.crawler.telecom.qinghai.TelecomQingHaiSMSThremResultRepository;
import com.microservice.dao.repository.crawler.telecom.qinghai.TelecomQingHaiUserInfoResultRepository;

import app.bean.WebParamTelecom;
import app.commontracerlog.TracerLog;
import app.crawler.telecom.htmlparse.TelecomParseQingHai;
import app.unit.GetThemUnitTelecom;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.qinghai")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.qinghai")
public class TelecomUnitQingHaiService {

	public static final Logger log = LoggerFactory.getLogger(TelecomUnitQingHaiService.class);

	@Autowired
	private TelecomQingHaiUserInfoResultRepository telecomQingHaiUserInfoResultRepository;

	@Autowired
	private TelecomQingHaiBillResultRepository telecomQingHaiBillResultRepository;

	@Autowired
	private TelecomQingHaiPayResultRepository telecomQingHaiPayResultRepository;

	@Autowired
	private TelecomQingHaiIntergentResultRepository telecomQingHaiIntergentResultRepository;

	@Autowired
	private TelecomQingHaiCallThremResultRepository telecomQingHaiCallThremResultRepository;

	@Autowired
	private TelecomQingHaiSMSThremResultRepository telecomQingHaiSMSThremResultRepository;

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private MobileDataErrRecRepository mobileDataErrRecRepository;

	// 获取青海用户 缴费信息
	public Object getpayResult(MessageLogin messageLogin, TaskMobile taskMobile, int i) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国电信抓取客户青海用户  缴费信息", messageLogin.getTask_id());
		String html = null;
		try {
			html = GetThemUnitTelecom.getpayResult(messageLogin, taskMobile, i, 0);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("中国电信抓取客户青海用户  缴费信息 e", e.getMessage());
		}

		tracerLog.addTag("中国电信抓取客户青海用户  缴费信息", "<xmp>" + html + "</xmp>");

		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());

			
			return new AsyncResult<String>("erroe");
		}
		List<TelecomQingHaiPayResult> rootresult = null;

		try {
			rootresult = TelecomParseQingHai.payResult_parse(html);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("中国电信抓取客户青海用户  缴费信息 e", e.getMessage());
		}

		if (rootresult == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());

			save(taskMobile);
			return new AsyncResult<String>("erroe");
		}
		tracerLog.addTag("中国电信抓取客户青海用户 缴费信息", messageLogin.getTask_id());

		// 保存前做去重处理
		if (rootresult != null) {
			for (TelecomQingHaiPayResult resultset : rootresult) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				save(resultset);
			}
		}
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());
		save(taskMobile);
		return rootresult;

	}

	// 获取青海用户 账单信息
	@Async
	public Future<String> getBill(MessageLogin messageLogin, TaskMobile taskMobile, int i) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国电信抓取客户青海用户   账单信息", messageLogin.getTask_id());

		String html = null;
		try {
			html = GetThemUnitTelecom.getBill(messageLogin, taskMobile, i, 0);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("中国电信抓取客户青海用户   账单信息 ", e.getMessage());
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());
			save(taskMobile);
			return new AsyncResult<String>("error");
		}

		tracerLog.addTag("中国电信抓取客户青海用户   账单信息", "<xmp>" + html + "</xmp>");

		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());

			save(taskMobile);
			return new AsyncResult<String>("error");
		}
		List<TelecomQingHaiBillResult> rootresult = new ArrayList<>();
		try {
			rootresult = TelecomParseQingHai.bill_parse(html);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("中国电信抓取客户青海用户 账单信息 error" + i, e.getMessage());
		}

		tracerLog.addTag("中国电信抓取客户青海用户 账单信息", messageLogin.getTask_id());

		for (TelecomQingHaiBillResult resultset : rootresult) {
			resultset.setUserid(messageLogin.getUser_id());
			resultset.setTaskid(taskMobile.getTaskid());
			resultset.setHtml(html);
			save(resultset);
		}

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());

		save(taskMobile);
		return new AsyncResult<String>("sucess");

	}

	// 获取青海用户 积分生成记录
	@Async
	public Future<String> getintegraResult(MessageLogin messageLogin, TaskMobile taskMobile, int i) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国电信抓取客户青海用户  积分生成记录", messageLogin.getTask_id());

		String html = null;
		try {
			html = GetThemUnitTelecom.getintegraResult(messageLogin, taskMobile, i, 0);

		} catch (Exception e) {
			e.printStackTrace();

			tracerLog.addTag("中国电信抓取客户青海用户  积分生成记录 ", e.getMessage());
		}
		tracerLog.addTag("中国电信抓取客户青海用户  积分生成记录", "<xmp>" + html + "</xmp>");

		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());

			save(taskMobile);
			return new AsyncResult<String>("erroe");
		}

		WebParamTelecom<TelecomQingHaiIntergentResult> webParam = TelecomParseQingHai.integraResult_parse(html);

		if (webParam.getErrormessage() != null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getDescription());

			save(taskMobile);
			return new AsyncResult<String>("erroe");
		}

		List<TelecomQingHaiIntergentResult> rootresult = webParam.getList();

		if (rootresult == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getDescription());
			save(taskMobile);
			return new AsyncResult<String>("erroe");
		}
		tracerLog.addTag("中国电信抓取客户青海用户 积分生成记录", messageLogin.getTask_id());

		// 保存前做去重处理
		if (rootresult != null) {
			for (TelecomQingHaiIntergentResult resultset : rootresult) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());

				save(resultset);
			}
		}

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getDescription());

		taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_SUCESS.getCode());
		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return new AsyncResult<String>("sucess");
	}

	// 获取青海用户 通话详单
	@Async
	public Future<String> getPhoneBillResult(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile,
			int i) {

		tracerLog.addTag("中国电信抓取客户青海用户   通话详单", messageLogin.getTask_id());

		String html = null;
		try {
			html = GetThemUnitTelecom.getPhoneBill(webClient, messageLogin, taskMobile, i, 0);

		} catch (Exception e) {
			e.printStackTrace();

			tracerLog.addTag("中国电信抓取客户青海用户   通话详单 ", e.getMessage());
		}
		LocalDate nowdate = LocalDate.now();
		String monthint = nowdate.plusMonths(-i).getMonthValue() + "";
		if (monthint.length() < 2) {
			monthint = "0" + monthint;
		}
		String error_date = nowdate.plusMonths(-i).getYear() + monthint;
		tracerLog.addTag("中国电信抓取客户青海用户   通话详单", "<xmp>" + html + "</xmp>");

		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getDescription());
			save(taskMobile);

			MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录", error_date,
					taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE", "未获取html", 0);
			mobileDataErrRecRepository.save(dataErrRec);

			return new AsyncResult<String>("error");
		}
		
		WebParamTelecom<TelecomQingHaiCallThremResult> webParam = new WebParamTelecom<TelecomQingHaiCallThremResult>();
		try{
			webParam = TelecomParseQingHai.callThrem_parse(html);

		}catch(Exception e){
			
		}

		if (webParam.getErrormessage() != null) {

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getDescription());

			MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录", error_date,
					taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE", webParam.getErrormessage(), 0);
			mobileDataErrRecRepository.save(dataErrRec);
			save(taskMobile);
			tracerLog.addTag("解析错误", "<xmp>" + webParam.getHtml() + "</xmp>");
			tracerLog.addTag("解析错误", webParam.getErrormessage());
			return new AsyncResult<String>("error");
		}

		List<TelecomQingHaiCallThremResult> rootresult = webParam.getList();

		if (rootresult == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getDescription());

			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			tracerLog.addTag("解析错误", "<xmp>" + webParam.getHtml() + "</xmp>");
			tracerLog.addTag("解析错误", webParam.getErrormessage());
			return new AsyncResult<String>("error");
		}

		// 保存前做去重处理
		if (rootresult != null) {
			for (TelecomQingHaiCallThremResult resultset : rootresult) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				save(resultset);
			}
		}

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getDescription());

		save(taskMobile);

		return new AsyncResult<String>("sucess");
		// return new AsyncResult<String>("erroe");

	}

	// 获取青海用户 短信详单
	@Async
	public Future<String> getSMSBill(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国电信抓取客户青海用户  短信详单", messageLogin.getTask_id());
		String html = null;
		try {
			html = GetThemUnitTelecom.getSMSBill(webClient, messageLogin, taskMobile, i, 0);
		} catch (Exception e) {
			e.printStackTrace();

			tracerLog.addTag("中国电信抓取客户青海用户  短信详单", e.getMessage());
		}

		tracerLog.addTag("中国电信抓取客户青海用户  短信详单", "<xmp>" + html + "</xmp>");

		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());

			save(taskMobile);
			return new AsyncResult<String>("erroe");
		}

		WebParamTelecom<TelecomQingHaiSMSThremResult> webParam = TelecomParseQingHai.SMSThrem_parse(html);

		if (webParam.getErrormessage() != null) {

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());

			save(taskMobile);
			tracerLog.addTag("解析错误", "<xmp>" + webParam.getHtml() + "</xmp>");
			tracerLog.addTag("解析错误", webParam.getErrormessage());
			return new AsyncResult<String>("error");

		}

		List<TelecomQingHaiSMSThremResult> rootresult = webParam.getList();

		if (rootresult == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getDescription());
			save(taskMobile);

			tracerLog.addTag("解析错误", "<xmp>" + webParam.getHtml() + "</xmp>");
			tracerLog.addTag("解析错误", webParam.getErrormessage());
			return new AsyncResult<String>("error");
		}
		tracerLog.addTag("中国电信抓取客户青海用户 短信详单", messageLogin.getTask_id());

		// 保存前做去重处理
		if (rootresult != null) {
			for (TelecomQingHaiSMSThremResult resultset : rootresult) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				save(resultset);
			}
		}

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getDescription());

		save(taskMobile);
		return new AsyncResult<String>("sucess");
	}

	/*
	 * 获取青海用户 信息，余额，业务等
	 * 
	 * @param webClient
	 * 
	 * @param messageLogin
	 * 
	 * @param taskMobile
	 * 
	 * @param i
	 * 
	 * @return
	 */
	@Async
	public void getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile) {
		TelecomQingHaiUserInfoResult telecomQingHaiUserInfoResult = new TelecomQingHaiUserInfoResult();
		try {
			telecomQingHaiUserInfoResult = getFeeBalance(messageLogin, taskMobile, telecomQingHaiUserInfoResult);

		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("中国电信 青海 错误1", e.getMessage());
		}
		try {
			telecomQingHaiUserInfoResult = getUseablePoint(messageLogin, taskMobile, telecomQingHaiUserInfoResult);

		} catch (Exception e) {
			e.printStackTrace();

			tracerLog.addTag("中国电信 青海 错误2", e.getMessage());

		}
		try {
			telecomQingHaiUserInfoResult = getHandledBiz(messageLogin, taskMobile, telecomQingHaiUserInfoResult);

		} catch (Exception e) {
			e.printStackTrace();

			tracerLog.addTag("中国电信 青海 错误3", e.getMessage());

		}

		try {
			telecomQingHaiUserInfoResult = getUserName(messageLogin, taskMobile, telecomQingHaiUserInfoResult);

		} catch (Exception e) {
			e.printStackTrace();

			tracerLog.addTag("中国电信 青海 错误4", e.getMessage());

		}

		try {
			telecomQingHaiUserInfoResult.setUserid(messageLogin.getUser_id());
			telecomQingHaiUserInfoResult.setTaskid(taskMobile.getTaskid());
			save(telecomQingHaiUserInfoResult);
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_USER_MSG().getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_USER_MSG().getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_USER_MSG().getDescription());

			taskMobile.setUserMsgStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		} catch (Exception e) {
			e.printStackTrace();

			tracerLog.addTag("中国电信 青海 错误telecomQingHaiUserInfoResult", e.getMessage());
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_USER_MSG().getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_USER_MSG().getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_USER_MSG().getDescription());

			taskMobile.setUserMsgStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}

	}

	// 用户信息
	public TelecomQingHaiUserInfoResult getFeeBalance(MessageLogin messageLogin, TaskMobile taskMobile,
			TelecomQingHaiUserInfoResult telecomQingHaiUserInfoResult) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国电信抓取客户青海用户 用户信息 getFeeBalance", messageLogin.getTask_id());

		String html = null;
		try {
			html = GetThemUnitTelecom.getFeeBalance(messageLogin, taskMobile, 0);

		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("中国电信抓取客户青海用户 用户信息  getFeeBalance", e.getMessage());
		}

		tracerLog.addTag("中国电信抓取客户青海用户  用户信息 getFeeBalance", "<xmp>" + html + "</xmp>");
		if (html == null) {
			return telecomQingHaiUserInfoResult;
		}

		telecomQingHaiUserInfoResult = TelecomParseQingHai.userinfo_parse(html, 0, telecomQingHaiUserInfoResult);

		return telecomQingHaiUserInfoResult;
	}

	// 获取 积分信息
	public TelecomQingHaiUserInfoResult getUseablePoint(MessageLogin messageLogin, TaskMobile taskMobile,
			TelecomQingHaiUserInfoResult telecomQingHaiUserInfoResult) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国电信抓取客户青海用户   积分", messageLogin.getTask_id());

		String html = null;
		try {
			html = GetThemUnitTelecom.getUseablePoint(messageLogin, taskMobile, 0);
		} catch (Exception e) {
			e.printStackTrace();

			tracerLog.addTag("中国电信抓取客户青海用户    积分", e.getMessage());
		}

		tracerLog.addTag("中国电信抓取客户青海用户    积分", "<xmp>" + html + "</xmp>");
		if (html == null) {
			return telecomQingHaiUserInfoResult;
		}
		telecomQingHaiUserInfoResult = TelecomParseQingHai.userinfo_parse(html, 3, telecomQingHaiUserInfoResult);

		System.out.println(telecomQingHaiUserInfoResult.toString());

		return telecomQingHaiUserInfoResult;
	}

	// 获取 套餐
	public TelecomQingHaiUserInfoResult getHandledBiz(MessageLogin messageLogin, TaskMobile taskMobile,
			TelecomQingHaiUserInfoResult telecomQingHaiUserInfoResult) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国电信抓取客户青海用户  套餐 ", messageLogin.getTask_id());

		String html = null;
		try {
			html = GetThemUnitTelecom.getHandledBiz(messageLogin, taskMobile, 0);
		} catch (Exception e) {
			tracerLog.addTag("中国电信抓取客户青海用户    套餐", e.getMessage());
			return null;
		}

		tracerLog.addTag("中国电信抓取客户青海用户    套餐", "<xmp>" + html + "</xmp>");

		if (html == null) {
			return telecomQingHaiUserInfoResult;
		}

		telecomQingHaiUserInfoResult = TelecomParseQingHai.userinfo_parse(html, 2, telecomQingHaiUserInfoResult);

		System.out.println(telecomQingHaiUserInfoResult.toString());
		return telecomQingHaiUserInfoResult;
	}

	// 获取 姓名
	public TelecomQingHaiUserInfoResult getUserName(MessageLogin messageLogin, TaskMobile taskMobile,
			TelecomQingHaiUserInfoResult telecomQingHaiUserInfoResult) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国电信抓取客户青海用户  姓名 ", messageLogin.getTask_id());

		String html = null;
		try {
			html = GetThemUnitTelecom.getUserName(messageLogin, taskMobile, 0);
		} catch (Exception e) {
			tracerLog.addTag("中国电信抓取客户青海用户    姓名", e.getMessage());
		}

		tracerLog.addTag("中国电信抓取客户青海用户    姓名 ", "<xmp>" + html + "</xmp>");

		if (html == null) {
			return telecomQingHaiUserInfoResult;
		}

		String rootresult = TelecomParseQingHai.userinfo_parseName(html);

		telecomQingHaiUserInfoResult.setName(rootresult);

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_USER_MSG().getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_USER_MSG().getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_USER_MSG().getDescription());

		taskMobile.setUserMsgStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());

		return telecomQingHaiUserInfoResult;
	}

	private void save(TelecomQingHaiSMSThremResult result) {
		telecomQingHaiSMSThremResultRepository.save(result);
	}

	private void save(TelecomQingHaiCallThremResult result) {
		telecomQingHaiCallThremResultRepository.save(result);
	}

	private void save(TelecomQingHaiIntergentResult result) {
		telecomQingHaiIntergentResultRepository.save(result);
	}

	private void save(TelecomQingHaiPayResult result) {
		telecomQingHaiPayResultRepository.save(result);
	}

	private void save(TelecomQingHaiBillResult result) {
		telecomQingHaiBillResultRepository.save(result);
	}

	private void save(TelecomQingHaiUserInfoResult result) {
		telecomQingHaiUserInfoResultRepository.save(result);
	}

	private void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}

}