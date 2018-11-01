package app.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamTelecom;
import app.crawler.telecomhtmlunit.LognAndGetYunNan;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISms;
import app.service.common.TelecomBasicService;
import app.unit.TeleComCommonUnit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.yunnan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.yunnan")
public class TelecomCrawlerService extends TelecomBasicService implements ICrawlerLogin, ISms {

	@Autowired
	private TelecomFutureYunNanService telecomFutureYunNanService;

	@Autowired
	private TelecomUnitService telecomUnitService;

	public WebClient getWebClientForTeleComYunNan(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = TeleComCommonUnit.addcookie(webClient, taskMobile);

		String url = "http://yn.189.cn/service/jt/bill/actionjt/ifr_bill_detailslist_new.jsp";
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("NUM", messageLogin.getName().trim()));
		paramsList.add(new NameValuePair("AREA_CODE", "0871"));
		paramsList.add(new NameValuePair("PROD_NO", "4217"));
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Referer",
				"http://yn.189.cn/service/jt/bill/qry_mainjt.jsp?SERV_NO=SHQD1&fastcode=01941229&cityCode=yn");
		// webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setAdditionalHeader("Host", "yn.189.cn");
		webRequest.setAdditionalHeader("Pragma", "no-cache");
		webRequest.setAdditionalHeader("Origin", "http://yn.189.cn");
		webRequest.setRequestParameters(paramsList);
		TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);
		return webClient;

	}

	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {

		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());

		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());

		try {
			webClient = telecomUnitService.getWebClientForTeleComYunNan(messageLogin, taskMobile);
			webClient = telecomUnitService.addcookieForSMSAndCall(webClient);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			telecomFutureYunNanService.getSMSBill(webClient, messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			tracerLog.addTag("parser.crawler.auth", "getSMSThrem" + e.toString());
		}

		try {
			telecomFutureYunNanService.getphoneBill(webClient, messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			tracerLog.addTag("parser.crawler.auth", "getCallThrem" + e.toString());
		}

		try {
			telecomFutureYunNanService.getBussiness(messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			tracerLog.addTag("parser.crawler.auth", "getBussiness" + e.toString());
		}

		try {
			telecomFutureYunNanService.getUserInfo(messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("parser.crawler.auth", "getUserInfo" + e.toString());
		}

		try {
			telecomFutureYunNanService.getBill(messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			e.printStackTrace();

			tracerLog.addTag("parser.crawler.auth", "getBill" + e.toString());
		}

		try {
			telecomFutureYunNanService.getintegraChangeResult(messageLogin, taskMobile);
		} catch (Exception e) {
			tracerLog.addTag("parser.crawler.auth", "getintegraResult" + e.toString());
		}

		try {
			telecomFutureYunNanService.getpayResult(messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			tracerLog.addTag("parser.crawler.auth", "getpayResult" + e.toString());
		}

		try {
			telecomFutureYunNanService.getBalance(messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			tracerLog.addTag("parser.crawler.auth", "getpayResult" + e.toString());
		}
		
		webClient.close();
		return null;
	}

	// 获取手机验证码
	@Async
	@Override
	public TaskMobile sendSms(MessageLogin messageLogin) {
		tracerLog.addTag("taskid", messageLogin.getTask_id());

		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());

		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());

		String url = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01941226";
		try {
			TeleComCommonUnit.getHtml(url, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			tracerLog.addTag("电信获取验证码  错误", "==" + e.getMessage());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
			// 发送验证码状态更新
			save(taskMobile);
			return taskMobile;
		}
		String cookieString = CommonUnit.transcookieToJson(webClient);
		taskMobile.setCookies(cookieString);
		save(taskMobile);
		WebParamTelecom<?> webParamTelecom = LognAndGetYunNan.getphonecode(messageLogin, taskMobile);
		if (webParamTelecom.getErrormessage() != null) {
			taskMobile = findtaskMobile(taskMobile.getTaskid());
			tracerLog.addTag("电信获取验证码  错误", "==" + webParamTelecom.getErrormessage());
			tracerLog.addTag("电信获取验证码  错误", "==" + webParamTelecom.getHtml());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
			if (webParamTelecom.getErrormessage().indexOf("您今天内获取取随机密码已超出限制") != -1) {
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR2.getDescription());

			}
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(webParamTelecom.getErrormessage());
			// 发送验证码状态更新
			save(taskMobile);
			webClient.close();
			return null;
		}

		cookieString = CommonUnit.transcookieToJson(webParamTelecom.getWebClient());

		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
		taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_SUCESS.getCode());
		taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_SUCESS.getMessage());
		taskMobile.setCookies(cookieString);
		// 发送验证码状态更新
		save(taskMobile);
		tracerLog.addTag("手机验证码获取完成", webParamTelecom.getHtml());
		webClient.close();

		return taskMobile;

	}

	// 手机短信验证码验证
	@Async
	@Override
	public TaskMobile verifySms(MessageLogin messageLogin) {
		tracerLog.addTag("taskid", messageLogin.getTask_id());

		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());

		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());

		String url = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=01941226";
		try {
			TeleComCommonUnit.getHtml(url, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			tracerLog.addTag("电信获取验证码  错误", "==" + e.getMessage());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
			// 发送验证码状态更新
			save(taskMobile);
			webClient.close();

			return null;
		}
		WebParamTelecom<?> webParamTelecom = LognAndGetYunNan.setphonecode(messageLogin, taskMobile);
		if (webParamTelecom.getErrormessage() != null) {
			tracerLog.addTag("电信获取验证码  错误", webParamTelecom.getErrormessage());
			tracerLog.addTag("电信获取验证码  html", webParamTelecom.getHtml());
			taskMobile = findtaskMobile(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
			if (webParamTelecom.getErrormessage().indexOf("证件姓名或证件号码不正确，请仔细核对您的客户信息") != -1) {
				taskMobile.setDescription(webParamTelecom.getErrormessage());
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());

			}
			if (webParamTelecom.getErrormessage().indexOf("随机验证码错误") != -1) {
				taskMobile.setDescription(webParamTelecom.getErrormessage());
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus());
			}
			if (webParamTelecom.getErrormessage().indexOf("网络连接错误") != -1) {
				taskMobile.setDescription(webParamTelecom.getErrormessage());
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhasestatus());
			}

			// 手机验证码验证失败状态更新
			save(taskMobile);
			webParamTelecom.getWebClient().close();
			return null;
		}
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
		String cookieString = CommonUnit.transcookieToJson(webParamTelecom.getWebClient());
		taskMobile.setCookies(cookieString);
		// 手机验证码验证成功状态更新
		save(taskMobile);
		webParamTelecom.getWebClient().close();

		tracerLog.addTag("手机验证码验证完成", webParamTelecom.getHtml());
		return null;

	}

	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskMobile login(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		return null;
	}

}
