package app.service;

import java.time.LocalDate;
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
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.mobile.MobileDataErrRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.common.TelecomCommonHtml;
import com.microservice.dao.entity.crawler.telecom.yunnan.TelecomYunNanBalanceResult;
import com.microservice.dao.entity.crawler.telecom.yunnan.TelecomYunNanBillHtmlResult;
import com.microservice.dao.entity.crawler.telecom.yunnan.TelecomYunNanBusinessResult;
import com.microservice.dao.entity.crawler.telecom.yunnan.TelecomYunNanCallThremResult;
import com.microservice.dao.entity.crawler.telecom.yunnan.TelecomYunNanIntegraChangeHtmlResult;
import com.microservice.dao.entity.crawler.telecom.yunnan.TelecomYunNanIntegraChangeResult;
import com.microservice.dao.entity.crawler.telecom.yunnan.TelecomYunNanPayResult;
import com.microservice.dao.entity.crawler.telecom.yunnan.TelecomYunNanSMSThremResult;
import com.microservice.dao.entity.crawler.telecom.yunnan.TelecomYunNanUserInfoResult;
import com.microservice.dao.repository.crawler.mobile.MobileDataErrRecRepository;
import com.microservice.dao.repository.crawler.telecom.yunnan.TelecomYunNanBalanceResultRepository;
import com.microservice.dao.repository.crawler.telecom.yunnan.TelecomYunNanBillHtmlResultRepository;
import com.microservice.dao.repository.crawler.telecom.yunnan.TelecomYunNanBusinessResultRepository;
import com.microservice.dao.repository.crawler.telecom.yunnan.TelecomYunNanCallThremResultRepository;
import com.microservice.dao.repository.crawler.telecom.yunnan.TelecomYunNanIntegraChangeHtmlResultRepository;
import com.microservice.dao.repository.crawler.telecom.yunnan.TelecomYunNanIntegraChangeResultRepository;
import com.microservice.dao.repository.crawler.telecom.yunnan.TelecomYunNanPayResultRepository;
import com.microservice.dao.repository.crawler.telecom.yunnan.TelecomYunNanSMSThremResultRepository;
import com.microservice.dao.repository.crawler.telecom.yunnan.TelecomYunNanUserInfoResultRepository;

import app.bean.TelecomYunNanCanShuAccidBean;
import app.bean.TelecomYunNanCanUserIdShuBean;
import app.bean.WebParamTelecom;
import app.crawler.telecom.htmlparse.TelecomParseYunNan;
import app.service.common.TelecomBasicService;
import app.unit.GetThemUnitTelecom;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.yunnan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.yunnan")
public class TelecomAsyncYunNaniService extends TelecomBasicService {

	public static final Logger log = LoggerFactory.getLogger(TelecomAsyncYunNaniService.class);

	@Autowired
	private TelecomYunNanUserInfoResultRepository telecomYunNanUserInfoResultRepository;

	@Autowired
	private TelecomYunNanBusinessResultRepository telecomYunNanBusinessResultRepository;

	@Autowired
	private TelecomYunNanBillHtmlResultRepository telecomYunNanBillHtmlResultRepository;

	@Autowired
	private TelecomYunNanPayResultRepository telecomYunNanPayResultRepository;

	@Autowired
	private TelecomYunNanBalanceResultRepository telecomYunNanBalanceResultRepository;

	@Autowired
	private TelecomYunNanIntegraChangeResultRepository telecomYunNanIntegraChangeResultRepository;

	@Autowired
	private TelecomYunNanIntegraChangeHtmlResultRepository telecomYunNanIntegraChangeHtmlResultRepository;

	@Autowired
	private TelecomYunNanCallThremResultRepository telecomYunNanCallThremResultRepository;

	@Autowired
	private TelecomYunNanSMSThremResultRepository telecomYunNanSMSThremResultRepository;

	@Autowired
	private MobileDataErrRecRepository mobileDataErrRecRepository;
	// 获取用户 缴费信息
	@Async
	public Future<String> getpayResult(MessageLogin messageLogin, TaskMobile taskMobile,
			TelecomYunNanCanUserIdShuBean telecomYunNanCanUserIdShuBean) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国电信抓取客户  缴费信息", messageLogin.getTask_id());
		String html = null;
		try {
			html = GetThemUnitTelecom.getpayResult(messageLogin, taskMobile, telecomYunNanCanUserIdShuBean, 0);
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
		WebParamTelecom<TelecomYunNanPayResult> webParam = TelecomParseYunNan.payResult_parse(html);

		if (webParam.getErrormessage() != null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getDescription());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}

		List<TelecomYunNanPayResult> rootresult = webParam.getList();

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
			for (TelecomYunNanPayResult resultset : rootresult) {
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
	public Future<String> getbalance(MessageLogin messageLogin, TaskMobile taskMobile,
			TelecomYunNanCanShuAccidBean telecomYunNanCanShuAccidBean) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国电信抓取客户  余额信息", messageLogin.getTask_id());
		String html = null;
		try {
			html = GetThemUnitTelecom.getBalance(messageLogin, taskMobile, telecomYunNanCanShuAccidBean, 0);
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
		WebParamTelecom<TelecomYunNanBalanceResult> webParam = TelecomParseYunNan.balance_parse(html);

		if (webParam.getErrormessage() != null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getDescription());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}

		List<TelecomYunNanBalanceResult> rootresult = webParam.getList();

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
			for (TelecomYunNanBalanceResult resultset : rootresult) {
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

	/**
	 * 获取套餐信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Future<String> getBussiness(MessageLogin messageLogin, TaskMobile taskMobile) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国电信抓取客户  套餐信息", messageLogin.getTask_id());
		String html = null;
		try {
			html = GetThemUnitTelecom.getBussiness(messageLogin, taskMobile, 0);
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
		WebParamTelecom<TelecomYunNanBusinessResult> webParam = TelecomParseYunNan.business_parse(html);

		if (webParam.getErrormessage() != null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_Buss_MSG().getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_Buss_MSG().getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_Buss_MSG().getDescription());
			save(taskMobile);
		}

		List<TelecomYunNanBusinessResult> rootresult = webParam.getList();

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
			for (TelecomYunNanBusinessResult resultset : rootresult) {
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
			TelecomYunNanCanUserIdShuBean telecomYunNanCanUserIdShuBean, int i) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国电信抓取用户   账单信息", messageLogin.getTask_id());

		String html = null;
		try {
			html = GetThemUnitTelecom.getBill(messageLogin, taskMobile, telecomYunNanCanUserIdShuBean, i, 0);
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

		TelecomYunNanBillHtmlResult resultset = new TelecomYunNanBillHtmlResult();
		resultset.setUserid(messageLogin.getUser_id());
		resultset.setTaskid(taskMobile.getTaskid());
		resultset.setHtml(html);
		save(resultset);

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
	public Object getintegraChangeResult(MessageLogin messageLogin, TaskMobile taskMobile,
			TelecomYunNanCanUserIdShuBean telecomYunNanCanUserIdShuBean, int i) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国电信抓取客户用户  积分生成记录", messageLogin.getTask_id());

		String html = null;
		try {
			html = GetThemUnitTelecom.getintegraChangeResult(messageLogin, taskMobile, telecomYunNanCanUserIdShuBean, i,
					0);

		} catch (Exception e) {
			e.printStackTrace();

			tracerLog.addTag("中国电信抓取客户用户  积分生成记录 ", e.getMessage());
		}
		tracerLog.addTag("中国电信抓取客户用户  积分生成记录", "<xmp>" + html + "</xmp>");

		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());

			if (taskMobile.getIntegralMsgStatus() != null && taskMobile.getIntegralMsgStatus() != 200) {
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			} else {
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			}
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}

		TelecomYunNanIntegraChangeHtmlResult telecomYunNanIntegraChangeHtmlResult = new TelecomYunNanIntegraChangeHtmlResult();

		telecomYunNanIntegraChangeHtmlResult.setUserid(messageLogin.getUser_id());
		telecomYunNanIntegraChangeHtmlResult.setTaskid(taskMobile.getTaskid());
		telecomYunNanIntegraChangeHtmlResult.setHtml(html);
		save(telecomYunNanIntegraChangeHtmlResult);

		WebParamTelecom<TelecomYunNanIntegraChangeResult> webParam = TelecomParseYunNan.integraChangeResult_parse(html);

		if (webParam.getErrormessage() != null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getDescription());
			if (taskMobile.getIntegralMsgStatus() != null && taskMobile.getIntegralMsgStatus() != 200) {
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			} else {
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			}
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}

		List<TelecomYunNanIntegraChangeResult> rootresult = webParam.getList();

		if (rootresult == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getDescription());
			if (taskMobile.getIntegralMsgStatus() != null && taskMobile.getIntegralMsgStatus() != 200) {
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			} else {
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			}
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}
		tracerLog.addTag("中国电信抓取客户用户 积分生成记录", messageLogin.getTask_id());

		// 保存前做去重处理
		if (rootresult != null) {
			for (TelecomYunNanIntegraChangeResult resultset : rootresult) {
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
		return rootresult;

	}

	// 获取用户 通话详单
	@Async
	public Future<String> getPhoneBill(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i) {

		tracerLog.addTag("中国电信抓取客户用户   通话详单", messageLogin.getTask_id());

		String html = null;
		try {
			html = GetThemUnitTelecom.getPhoneBill(webClient, messageLogin, taskMobile, i, 0);

		} catch (Exception e) {
			e.printStackTrace();

			tracerLog.addTag("中国电信抓取客户用户   通话详单 ", e.getMessage());
		}
		
		LocalDate nowdate = LocalDate.now();
		String monthint = nowdate.plusMonths(-i).getMonthValue() + "";
		if (monthint.length() < 2) {
			monthint = "0" + monthint;
		}
		String error_date = nowdate.plusMonths(-i).getYear()+monthint;

		tracerLog.addTag("中国电信抓取客户用户   通话详单", "<xmp>" + html + "</xmp>");

		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getDescription());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			
			MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录", error_date,
					taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE","未获取html", 0);
			mobileDataErrRecRepository.save(dataErrRec);
			return null;
		}
		WebParamTelecom<TelecomYunNanCallThremResult> webParam = TelecomParseYunNan.callThrem_parse(html);

		if (webParam.getErrormessage() != null) {

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getDescription());
			save(taskMobile);
			tracerLog.addTag("解析错误" + i, "<xmp>" + webParam.getHtml() + "</xmp>");
			tracerLog.addTag("解析错误" + i, webParam.getErrormessage());
			
			MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录", error_date,
					taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE",webParam.getErrormessage(), 0);
			mobileDataErrRecRepository.save(dataErrRec);
		}

		List<TelecomYunNanCallThremResult> rootresult = webParam.getList();

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
			for (TelecomYunNanCallThremResult resultset : rootresult) {
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
	public Future<String> getSMSBill(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i) {
		tracerLog.addTag("中国电信抓取客户用户  短信详单", messageLogin.getTask_id());
		String html = null;
		try {

			html = GetThemUnitTelecom.getSMSBill(webClient, messageLogin, taskMobile, i, 0);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("中国电信抓取客户用户  短信详单", e.getMessage());
		}

		tracerLog.addTag("中国电信抓取客户用户  短信详单", "<xmp>" + html + "</xmp>");

		if (html == null) {
			System.out.println("==========null============");
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

		WebParamTelecom<TelecomYunNanSMSThremResult> webParam = TelecomParseYunNan.SMSThrem_parse(html);

		if (webParam.getErrormessage() != null) {

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getDescription());
			save(taskMobile);
			tracerLog.addTag("解析错误", "<xmp>" + webParam.getHtml() + "</xmp>");
			tracerLog.addTag("解析错误", webParam.getErrormessage());
		}

		List<TelecomYunNanSMSThremResult> rootresult = webParam.getList();

		if (rootresult == null) {
			System.out.println("===========null " + i + "===========");
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getDescription());
			save(taskMobile);
			tracerLog.addTag("解析错误", "<xmp>" + webParam.getHtml() + "</xmp>");
			tracerLog.addTag("解析错误", webParam.getErrormessage());
			System.out.println("==============结束" + i + "=======================");

			return null;
		}
		tracerLog.addTag("中国电信抓取客户用户 短信详单", messageLogin.getTask_id());

		// 保存前做去重处理
		if (rootresult != null) {
			for (TelecomYunNanSMSThremResult resultset : rootresult) {
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

		WebParamTelecom<TelecomYunNanUserInfoResult> webParam = TelecomParseYunNan.userinfo_parse(html);

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
		List<TelecomYunNanUserInfoResult> rootresult = webParam.getList();

		if (rootresult == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_USER_MSG().getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_USER_MSG().getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_USER_MSG().getDescription());

			save(taskMobile);
			tracerLog.addTag("解析错误", "<xmp>" + webParam.getHtml() + "</xmp>");
			tracerLog.addTag("解析错误", webParam.getErrormessage());
			return null;
		}

		for (TelecomYunNanUserInfoResult resultset : rootresult) {
			resultset.setUserid(messageLogin.getUser_id());
			resultset.setTaskid(taskMobile.getTaskid());
			save(resultset);
		}

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_USER_MSG().getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_USER_MSG().getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_USER_MSG().getDescription());
		save(taskMobile);
		return null;
	}

	private void save(TelecomYunNanSMSThremResult result) {
		telecomYunNanSMSThremResultRepository.save(result);
	}

	private void save(TelecomYunNanCallThremResult result) {
		telecomYunNanCallThremResultRepository.save(result);
	}

	private void save(TelecomYunNanIntegraChangeResult result) {
		telecomYunNanIntegraChangeResultRepository.save(result);
	}

	private void save(TelecomYunNanPayResult result) {
		telecomYunNanPayResultRepository.save(result);
	}

	private void save(TelecomYunNanBillHtmlResult result) {
		telecomYunNanBillHtmlResultRepository.save(result);
	}

	private void save(TelecomYunNanUserInfoResult result) {
		telecomYunNanUserInfoResultRepository.save(result);
	}

	private void save(TelecomYunNanBusinessResult result) {
		telecomYunNanBusinessResultRepository.save(result);
	}

	private void save(TelecomYunNanBalanceResult result) {
		telecomYunNanBalanceResultRepository.save(result);
	}

	private void save(TelecomYunNanIntegraChangeHtmlResult result) {
		telecomYunNanIntegraChangeHtmlResultRepository.save(result);
	}

}