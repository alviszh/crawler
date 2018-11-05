package app.service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.bean.TelecomHaiNanUserIdBean;
import app.bean.WebParamTelecom;
import app.bean.error.ErrorException;
import app.crawler.telecom.htmlparse.TelecomParseHaiNan;
import app.crawler.telecomhtmlunit.LognAndGetHaiNan;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISms;
import app.service.common.TelecomBasicService;

@Component
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.hainan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.hainan")
public class TelecomCrawlerService extends TelecomBasicService implements ISms, ICrawlerLogin {

	@Autowired
	private TelecomHaiNanService telecomHaiNanService;

	@Async
	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {

		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());

		String html_UserId = null;

		try {

			html_UserId = LognAndGetHaiNan.readyGetUserId(taskMobile);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			taskMobile = findtaskMobile(taskMobile.getTaskid());

			taskMobile.setFinished(true);
			taskMobile.setCallRecordStatus(StatusCodeRec.CRAWLER_CallRecordStatus_SUCESS.getCode());
			taskMobile.setError_message("海南电信网络波动，请稍后重试,网速慢，导致关键页超时");
			taskMobile.setDescription("海南电信网络波动，请稍后重试");
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			tracerLog.addTag("中国跳转页", "<xmp>" + e.getMessage() + "</xmp>");
			return null;
		}
		TelecomHaiNanUserIdBean telecomHaiNanUserIdBean = TelecomParseHaiNan.readyforUserId(html_UserId);

		if (telecomHaiNanUserIdBean == null) {
			taskMobile = findtaskMobile(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_ERROR.getPhasestatus());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_ERROR.getError_code());
			taskMobile.setError_message("海南电信网络波动，请稍后重试,网速慢，导致关键页超时");
			taskMobile.setDescription("海南电信网络波动，请稍后重试");
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}

		try {
			telecomHaiNanService.getSMSBill(telecomHaiNanUserIdBean, messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			tracerLog.addTag("parser.crawler.auth", "getSMSThrem" + e.toString());
		}

		try {
			telecomHaiNanService.getphoneBill(messageLogin, taskMobile, telecomHaiNanUserIdBean);
		} catch (Exception e) {
			tracerLog.addTag("parser.crawler.auth", "getCallThrem" + e.toString());
		}

		try {
			telecomHaiNanService.getBussiness(messageLogin, taskMobile, telecomHaiNanUserIdBean);// 成功
		} catch (Exception e) {
			tracerLog.addTag("parser.crawler.auth", "getBussiness" + e.toString());
		}

		try {
			telecomHaiNanService.getUserInfo(messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("parser.crawler.auth", "getUserInfo" + e.toString());
		}

		try {
			telecomHaiNanService.getBill(messageLogin, taskMobile, telecomHaiNanUserIdBean);// 成功
		} catch (Exception e) {
			e.printStackTrace();

			tracerLog.addTag("parser.crawler.auth", "getBill" + e.toString());
		}

		try {
			telecomHaiNanService.getintegraChangeResult(messageLogin, taskMobile, telecomHaiNanUserIdBean);
		} catch (Exception e) {
			tracerLog.addTag("parser.crawler.auth", "getintegraResult" + e.toString());
		}

		try {
			telecomHaiNanService.getpayResult(messageLogin, taskMobile, telecomHaiNanUserIdBean);// 成功
		} catch (Exception e) {
			tracerLog.addTag("parser.crawler.auth", "getpayResult" + e.toString());
		}

		try {
			telecomHaiNanService.getBalance(messageLogin, taskMobile, telecomHaiNanUserIdBean);// 成功
		} catch (Exception e) {
			tracerLog.addTag("parser.crawler.auth", "getpayResult" + e.toString());
		}
		return null;
	}

	// 模拟登陆
	@Async
	@Override
	public TaskMobile login(MessageLogin messageLogin) {

//		tracerLog.addTag("taskid", messageLogin.getTask_id());
//		tracerLog.addTag("parser.login.auth", messageLogin.getName());
//		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
//
//		taskMobile.setCookies(null);
//		save(taskMobile);
//
//		try {
//			WebParamTelecom<?> webParamTelecom = telecomLoginService.loginHaiNan(messageLogin, taskMobile, 0);
//			if (webParamTelecom.getWebClient() == null) {
//
//				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getPhase());
//				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getPhasestatus());
//				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getDescription());
//
//				taskMobile.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
//				taskMobile.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
//				taskMobile.setTesthtml(messageLogin.getPassword().trim());
//				tracerLog.addTag("parser.login.auth", webParamTelecom.getStatusCodeRec().toString());
//
//				// 登录失败状态存储
//				save(taskMobile);
//				return null;
//			}
//			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGINTWO_SUCCESS.getPhase());
//			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGINTWO_SUCCESS.getPhasestatus());
//			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGINTWO_SUCCESS.getDescription());
//			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_LOGINTWO_SUCCESS.getError_code());
//			String cookieString = CommonUnit.transcookieToJson(webParamTelecom.getWebClient());
//			taskMobile.setCookies(cookieString);
//			taskMobile.setTesthtml(messageLogin.getPassword().trim());
//
//			// 登录成功状态更新
//			save(taskMobile);
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getPhase());
//			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getPhasestatus());
//			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getDescription());
//			taskMobile.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_Nine.getCode());
//			taskMobile.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_Nine.getMessage());
//			taskMobile.setTesthtml(messageLogin.getPassword().trim());
//			tracerLog.addTag("parser.login.auth", e.getMessage());
//			// 登录失败状态更新
//			save(taskMobile);
//		}
		return null;
	}

	// 获取手机验证码
	@Async
	@Override
	public TaskMobile sendSms(MessageLogin messageLogin) {

		tracerLog.addTag("taskid", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());

		// 发送验证码状态更新
		WebParamTelecom<?> webParamTelecom = LognAndGetHaiNan.getPhonecode(messageLogin, taskMobile);
		if (webParamTelecom.getErrormessage() != null) {
			taskMobile = findtaskMobile(taskMobile.getTaskid());
			tracerLog.addTag("电信获取验证码 getPhoneCode  错误", "==" + webParamTelecom.getErrormessage());
			tracerLog.addTag("电信获取验证码  getPhoneCode html", "==" + webParamTelecom.getHtml());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setDescription(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
			taskMobile.setError_message(webParamTelecom.getErrormessage());
			// 发送验证码状态更新
			save(taskMobile);
			return null;
		}

		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
		taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_SUCESS.getCode());
		taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_SUCESS.getMessage());
		taskMobile.setCookies(webParamTelecom.getWebClient());
		
		// 发送验证码状态更新
		save(taskMobile);

		tracerLog.addTag("手机验证码获取完成", webParamTelecom.getHtml());

		return taskMobile;

	}

	// 手机短信验证码验证
	@Async
	@Override
	// 根据当日期获取六个月通话详单
	@Retryable(value = ErrorException.class, maxAttempts = 3)
	public TaskMobile verifySms(MessageLogin messageLogin) {
		tracerLog.addTag("taskid", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		String html_UserId = null;

		try {
			html_UserId = LognAndGetHaiNan.readyGetUserId(taskMobile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ErrorException("获取关键字错误");
		} 

		tracerLog.addTag("setphonecode telecomHaiNanUserIdBean html ", html_UserId);
		TelecomHaiNanUserIdBean telecomHaiNanUserIdBean = TelecomParseHaiNan.readyforUserId(html_UserId);
		if (telecomHaiNanUserIdBean == null) {
			taskMobile = findtaskMobile(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhasestatus());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getError_code());
			taskMobile.setError_message("海南电信网络波动，请稍后重试,网速慢，导致关键页超时");
			taskMobile.setDescription("海南电信网络波动，请稍后重试");
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}
		tracerLog.addTag("setphonecode telecomHaiNanUserIdBean ", telecomHaiNanUserIdBean.toString());

		LocalDate today = LocalDate.now();

		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-0);

		String month = enddate.getMonthValue() + "";
		if (month.length() < 2) {
			month = "0" + month;
		}

		String stardate = today.getYear() + month;

		WebParamTelecom<?> webParamTelecom = null;
		try {
			webParamTelecom = LognAndGetHaiNan.setphonecode(telecomHaiNanUserIdBean, messageLogin, taskMobile,
					stardate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			tracerLog.addTag("电信获取验证码  setPhoneCode catch", e.getMessage());

			taskMobile = findtaskMobile(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getError_code());
			taskMobile.setError_message(webParamTelecom.getErrormessage());
			taskMobile.setPhase_status(webParamTelecom.getErrormessage());
			taskMobile.setDescription(webParamTelecom.getErrormessage());

			// 手机验证码验证失败状态更新
			save(taskMobile);
		}

		tracerLog.addTag("yanzheng html ", webParamTelecom.toString());
		if (webParamTelecom.getErrormessage() != null) {
			tracerLog.addTag("电信获取验证码  getErrormessage 错误", webParamTelecom.getErrormessage());
			tracerLog.addTag("电信获取验证码  html 错误", webParamTelecom.getHtml());
			taskMobile = findtaskMobile(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getError_code());
			taskMobile.setError_message(webParamTelecom.getErrormessage());
			taskMobile.setDescription(webParamTelecom.getErrormessage());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus());

			// 手机验证码验证失败状态更新
			save(taskMobile);
			return null;
		}
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());

		// 手机验证码验证成功状态更新
		save(taskMobile);
		tracerLog.addTag("手机验证码验证完成", webParamTelecom.getHtml());
		return null;

	}

	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
