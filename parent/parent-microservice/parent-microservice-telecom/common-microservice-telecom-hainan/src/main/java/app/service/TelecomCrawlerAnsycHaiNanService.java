package app.service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
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
import com.crawler.mobile.json.StatusCodeRec;
import com.microservice.dao.entity.crawler.mobile.MobileDataErrRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.common.TelecomCommonHtml;
import com.microservice.dao.entity.crawler.telecom.hainan.TelecomHaiNanBalanceResult;
import com.microservice.dao.entity.crawler.telecom.hainan.TelecomHaiNanBusinessResult;
import com.microservice.dao.entity.crawler.telecom.hainan.TelecomHaiNanCallThremResult;
import com.microservice.dao.entity.crawler.telecom.hainan.TelecomHaiNanIntegraChangeResult;
import com.microservice.dao.entity.crawler.telecom.hainan.TelecomHaiNanPayResult;
import com.microservice.dao.entity.crawler.telecom.hainan.TelecomHaiNanSMSThremResult;
import com.microservice.dao.entity.crawler.telecom.hainan.TelecomHaiNanUserInfoResult;
import com.microservice.dao.repository.crawler.mobile.MobileDataErrRecRepository;
import com.microservice.dao.repository.crawler.telecom.hainan.TelecomHaiNanBalanceResultRepository;
import com.microservice.dao.repository.crawler.telecom.hainan.TelecomHaiNanBusinessResultRepository;
import com.microservice.dao.repository.crawler.telecom.hainan.TelecomHaiNanCallThremResultRepository;
import com.microservice.dao.repository.crawler.telecom.hainan.TelecomHaiNanIntegraChangeResultRepository;
import com.microservice.dao.repository.crawler.telecom.hainan.TelecomHaiNanPayResultRepository;
import com.microservice.dao.repository.crawler.telecom.hainan.TelecomHaiNanSMSThremResultRepository;
import com.microservice.dao.repository.crawler.telecom.hainan.TelecomHaiNanUserInfoResultRepository;


import app.bean.TelecomHaiNanUserIdBean;
import app.bean.WebParamTelecom;
import app.crawler.telecom.htmlparse.TelecomParseHaiNan;
import app.service.common.TelecomBasicService;
import app.unit.GetThemUnitTelecom;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.hainan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.hainan")
public class TelecomCrawlerAnsycHaiNanService extends TelecomBasicService {

	public static final Logger log = LoggerFactory.getLogger(TelecomCrawlerAnsycHaiNanService.class);


	@Autowired
	private TelecomHaiNanBusinessResultRepository telecomHaiNanBusinessResultRepository;

	@Autowired
	private TelecomHaiNanPayResultRepository telecomHaiNanPayResultRepository;
	
	@Autowired
	private TelecomHaiNanBalanceResultRepository telecomHaiNanBalanceResultRepository;
	
	@Autowired
	private TelecomHaiNanCallThremResultRepository telecomHaiNanCallThremResultRepository;
	
	@Autowired
	private TelecomHaiNanIntegraChangeResultRepository telecomHaiNanIntegraChangeResultRepository;
	
	@Autowired
	private TelecomHaiNanSMSThremResultRepository telecomHaiNanSMSThremResultRepository;
	
	@Autowired
	private TelecomHaiNanUserInfoResultRepository telecomHaiNanUserInfoResultRepository;
	
	@Autowired
	private MobileDataErrRecRepository mobileDataErrRecRepository;

	// 获取用户 缴费信息
	@Async
	public Future<String> getpayResult(MessageLogin messageLogin, TaskMobile taskMobile, int i,
			TelecomHaiNanUserIdBean telecomHaiNanUserIdBean) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国电信抓取客户  缴费信息", messageLogin.getTask_id());
		String html = null;
		try {
			html = GetThemUnitTelecom.getpayResult(messageLogin, taskMobile, telecomHaiNanUserIdBean, i, 0);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("中国电信抓取客户  缴费信息", e.getMessage());
		}

		tracerLog.addTag("中国电信抓取客户  缴费信息", "<xmp>" + html + "</xmp>");

		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getDescription());

			save(taskMobile);
			return null;
		}
		WebParamTelecom<TelecomHaiNanPayResult> webParam = TelecomParseHaiNan.payResult_parse(html);

		if (webParam.getErrormessage() != null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getDescription());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}

		List<TelecomHaiNanPayResult> rootresult = webParam.getList();

		if (rootresult == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getDescription());
			save(taskMobile);
			return null;
		}
		tracerLog.addTag("中国电信抓取客户 缴费信息", messageLogin.getTask_id());
		// 保存前做去重处理
		if (rootresult != null) {
			for (TelecomHaiNanPayResult resultset : rootresult) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				save(resultset);
			}
		}
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getDescription());

		save(taskMobile);
		return null;

	}

	@Async
	public Future<String> getbalance(MessageLogin messageLogin, TaskMobile taskMobile, int i,
			TelecomHaiNanUserIdBean telecomHaiNanUserIdBean) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国电信抓取客户  余额信息", messageLogin.getTask_id());
		String html = null;
		try {
			html = GetThemUnitTelecom.getBalance(messageLogin, taskMobile, i, 0);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("中国电信抓取客户  余额信息", e.getMessage());
		}

		tracerLog.addTag("中国电信抓取客户  余额信息", "<xmp>" + html + "</xmp>");

		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getDescription());

			save(taskMobile);
			return null;
		}
		WebParamTelecom<TelecomHaiNanBalanceResult> webParam = TelecomParseHaiNan.balance_parse(html);

		if (webParam.getErrormessage() != null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getDescription());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}

		List<TelecomHaiNanBalanceResult> rootresult = webParam.getList();

		if (rootresult == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getDescription());
			save(taskMobile);
			return null;
		}
		tracerLog.addTag("中国电信抓取客户 缴费信息", messageLogin.getTask_id());
		// 保存前做去重处理
		LocalDate today = LocalDate.now();

		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);

		String month = enddate.getMonthValue() + "";
		if (month.length() < 2) {
			month = "0" + month;
		}

		String stardate = today.getYear()+ month;
		if (rootresult != null) {
			for (TelecomHaiNanBalanceResult resultset : rootresult) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				resultset.setDate(stardate);
				save(resultset);
			}
		}
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getDescription());

		save(taskMobile);
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
	public Future<String> getBussiness(MessageLogin messageLogin, TaskMobile taskMobile,
			TelecomHaiNanUserIdBean telecomHaiNanUserIdBean) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国电信抓取客户  套餐信息", messageLogin.getTask_id());
		String html = null;
		try {
			html = GetThemUnitTelecom.getBussiness(telecomHaiNanUserIdBean, messageLogin, taskMobile, 0);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("中国电信抓取客户  套餐信息", e.getMessage());
		}

		tracerLog.addTag("中国电信抓取客户  套餐信息", "<xmp>" + html + "</xmp>");

		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_Buss_MSG().getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_Buss_MSG().getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_Buss_MSG().getDescription());

			save(taskMobile);
			return null;
		}
		WebParamTelecom<TelecomHaiNanBusinessResult> webParam = TelecomParseHaiNan.business_parse(html);

		if (webParam.getErrormessage() != null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_Buss_MSG().getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_Buss_MSG().getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_Buss_MSG().getDescription());
			save(taskMobile);
		}

		List<TelecomHaiNanBusinessResult> rootresult = webParam.getList();

		if (rootresult == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_Buss_MSG().getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_Buss_MSG().getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_Buss_MSG().getDescription());
			save(taskMobile);
			return null;
		}
		tracerLog.addTag("中国电信抓取客户 套餐信息", messageLogin.getTask_id());
		// 保存前做去重处理
		if (rootresult != null) {
			for (TelecomHaiNanBusinessResult resultset : rootresult) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				save(resultset);
			}
		}
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_Buss_MSG().getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_Buss_MSG().getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_Buss_MSG().getDescription());

		save(taskMobile);
		return null;

	}

	// 获取用户 账单信息(完成)
	@Async
	public Future<String> getBill(MessageLogin messageLogin, TaskMobile taskMobile,
			TelecomHaiNanUserIdBean telecomHaiNanUserIdBean, int i) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国电信抓取用户   账单信息", messageLogin.getTask_id());

		String html = null;
		try {
			html = GetThemUnitTelecom.getBill(messageLogin, taskMobile, i, 0);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("中国电信抓取用户   账单信息 ", e.getMessage());
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());
			save(taskMobile);
			return null;
		}

		tracerLog.addTag("中国电信抓取用户   账单信息", "<xmp>" + html + "</xmp>");

		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());
			save(taskMobile);
			return null;
		}

		// List<TelecomQingHaiBillResult> rootresult =
		// TelecomParseYunNan.bill_parse(html);
		tracerLog.addTag("中国电信抓取客户用户 账单信息", messageLogin.getTask_id());

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());
		taskMobile.setAccountMsgStatus(StatusCodeRec.CRAWLER_AccountMsgStatus_SUCESS.getCode());
		save(taskMobile);
		return null;

	}

	// 获取用户 积分生成记录(完成)
	@Async
	public Future<String> getintegraChangeResult(MessageLogin messageLogin, TaskMobile taskMobile,
			TelecomHaiNanUserIdBean telecomHaiNanUserIdBean, int i) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国电信抓取客户用户  积分生成记录", messageLogin.getTask_id());

		String html = null;
		try {
			html = GetThemUnitTelecom.getintegraChangeResult(messageLogin, taskMobile, telecomHaiNanUserIdBean, i, 0);

		} catch (Exception e) {
			e.printStackTrace();

			tracerLog.addTag("中国电信抓取客户用户  积分生成记录 ", e.getMessage());
		}
		tracerLog.addTag("中国电信抓取客户用户  积分生成记录", "<xmp>" + html + "</xmp>");

		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getDescription());
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());

			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}

		WebParamTelecom<TelecomHaiNanIntegraChangeResult> webParam = TelecomParseHaiNan.integraChangeResult_parse(html);

		if (webParam.getErrormessage() != null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getDescription());
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}

		List<TelecomHaiNanIntegraChangeResult> rootresult = webParam.getList();

		if (rootresult == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getDescription());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}
		tracerLog.addTag("中国电信抓取客户用户 积分生成记录", messageLogin.getTask_id());

		// 保存前做去重处理
		if (rootresult != null) {
			for (TelecomHaiNanIntegraChangeResult resultset : rootresult) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());

				save(resultset);
			}
		}

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getDescription());
		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return null;

	}

	// 获取用户 通话详单
	@Async
	public Future<String> getPhoneBill(TelecomHaiNanUserIdBean telecomHaiNanUserIdBean, MessageLogin messageLogin,
			TaskMobile taskMobile, int i) {

		tracerLog.addTag("中国电信抓取客户用户   通话详单", messageLogin.getTask_id());
		LocalDate nowdate = LocalDate.now();
		String monthint = nowdate.plusMonths(-i).getMonthValue() + "";
		if (monthint.length() < 2) {
			monthint = "0" + monthint;
		}
		String error_date = nowdate.plusMonths(-i).getYear()+monthint;
		String html = null;
		try {
			html = GetThemUnitTelecom.getPhoneBill(telecomHaiNanUserIdBean, messageLogin, taskMobile, i, 0);

		} catch (Exception e) {
			e.printStackTrace();

			tracerLog.addTag("中国电信抓取客户用户   通话详单 ", e.getMessage());
		}

		tracerLog.addTag("中国电信抓取客户用户   通话详单", "<xmp>" + html + "</xmp>");

		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getDescription());
			save(taskMobile);
			MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录", error_date,
					taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE","未获取html", 0);
			mobileDataErrRecRepository.save(dataErrRec);
			return null;
		}
		WebParamTelecom<TelecomHaiNanCallThremResult> webParam = TelecomParseHaiNan.callThrem_parse(html);

		if (webParam.getErrormessage() != null) {

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getDescription());
			save(taskMobile);
			MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录", error_date,
					taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE",webParam.getErrormessage(), 0);
			mobileDataErrRecRepository.save(dataErrRec);			tracerLog.addTag("解析错误" + i, "<xmp>" + webParam.getHtml() + "</xmp>");
			tracerLog.addTag("解析错误" + i, webParam.getErrormessage());
		}

		List<TelecomHaiNanCallThremResult> rootresult = webParam.getList();

		if (rootresult == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getDescription());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			tracerLog.addTag("解析错误" + i, "<xmp>" + webParam.getHtml() + "</xmp>");
			tracerLog.addTag("解析错误" + i, webParam.getErrormessage());
			return null;
		}

		// 保存前做去重处理
		if (rootresult != null) {
			for (TelecomHaiNanCallThremResult resultset : rootresult) {
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
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return null;
		// return null;

	}

	// 获取用户 短信详单
	@Async
	public Future<String> getSMSBill(TelecomHaiNanUserIdBean telecomHaiNanUserIdBean, MessageLogin messageLogin,
			TaskMobile taskMobile, int i) {
		tracerLog.addTag("中国电信抓取客户用户  短信详单", messageLogin.getTask_id());
		String html = null;
		try {

			html = GetThemUnitTelecom.getSMSBill(telecomHaiNanUserIdBean, messageLogin, taskMobile, i, 0);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("中国电信抓取客户用户  短信详单", e.getMessage());
		}

		tracerLog.addTag("中国电信抓取客户用户  短信详单", "<xmp>" + html + "</xmp>");

		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getDescription());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}

		TelecomCommonHtml telecomCommonHtml = new TelecomCommonHtml(taskMobile.getTaskid(), "telecom_yunnan_sms_html",
				"20004", null, html);
		save(telecomCommonHtml);

		WebParamTelecom<TelecomHaiNanSMSThremResult> webParam = TelecomParseHaiNan.SMSThrem_parse(html);

		if (webParam.getErrormessage() != null) {

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getDescription());
			save(taskMobile);
			tracerLog.addTag("解析错误", "<xmp>" + webParam.getHtml() + "</xmp>");
			tracerLog.addTag("解析错误", webParam.getErrormessage());
		}

		List<TelecomHaiNanSMSThremResult> rootresult = webParam.getList();

		if (rootresult == null) {
			System.out.println("===========null " + i + "===========");
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getDescription());
			save(taskMobile);
			tracerLog.addTag("解析错误", "<xmp>" + webParam.getHtml() + "</xmp>");
			tracerLog.addTag("解析错误", webParam.getErrormessage());

			return null;
		}
		tracerLog.addTag("中国电信抓取客户用户 短信详单", messageLogin.getTask_id());

		// 保存前做去重处理
		if (rootresult != null) {
			for (TelecomHaiNanSMSThremResult resultset : rootresult) {
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
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());

		System.out.println("==============结束" + i + "=======================");
		return null;

	}

	/*
	 * 获取用户 信息(完成)
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
	public Future<String> getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile) {
		String html = null;
		try {
			html = GetThemUnitTelecom.getUserInfo(messageLogin, taskMobile, 0);

		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("中国电信 用户信息 ", e.getMessage());
		}

		tracerLog.addTag("中国电信 用户信息", "<xmp>" + html + "</xmp>");
		if (html == null) {
			return null;
		}

		WebParamTelecom<TelecomHaiNanUserInfoResult> webParam = TelecomParseHaiNan.userinfo_parse(html);

		if (webParam.getErrormessage() != null) {

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_USER_MSG().getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_USER_MSG().getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_USER_MSG().getDescription());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			tracerLog.addTag("解析错误", "<xmp>" + webParam.getHtml() + "</xmp>");
			tracerLog.addTag("解析错误", webParam.getErrormessage());
		}
		List<TelecomHaiNanUserInfoResult> rootresult = webParam.getList();

		if (rootresult == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_USER_MSG().getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_USER_MSG().getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_USER_MSG().getDescription());

			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());

			tracerLog.addTag("解析错误", "<xmp>" + webParam.getHtml() + "</xmp>");
			tracerLog.addTag("解析错误", webParam.getErrormessage());
			return null;
		}

		for (TelecomHaiNanUserInfoResult resultset : rootresult) {
			resultset.setUserid(messageLogin.getUser_id());
			resultset.setTaskid(taskMobile.getTaskid());
			save(resultset);
		}

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_USER_MSG().getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_USER_MSG().getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_USER_MSG().getDescription());
		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());

		return null;
	}

	private void save(TelecomHaiNanSMSThremResult result) {
		telecomHaiNanSMSThremResultRepository.save(result);
	}

	private void save(TelecomHaiNanCallThremResult result) {
		telecomHaiNanCallThremResultRepository.save(result);
	}

	private void save(TelecomHaiNanPayResult result) {
		telecomHaiNanPayResultRepository.save(result);
	}

	private void save(TelecomHaiNanUserInfoResult result) {
		telecomHaiNanUserInfoResultRepository.save(result);
	}

	private void save(TelecomHaiNanBusinessResult result) {
		telecomHaiNanBusinessResultRepository.save(result);
	}

	private void save(TelecomHaiNanBalanceResult result) {
		telecomHaiNanBalanceResultRepository.save(result);
	}

	private void save(TelecomHaiNanIntegraChangeResult result) {
		telecomHaiNanIntegraChangeResultRepository.save(result);
	}

}