package app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.bean.WebParamUnicom;
import app.commontracerlog.TracerLog;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISms;
import app.service.aop.ISmsTwice;

@Component
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.unicom")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.unicom")
public class UnicomService implements ISms, ICrawlerLogin, ISmsTwice {

	public static final String KEY = "秘钥就是不告诉你，自己猜去吧哈哈";

	public static final Logger log = LoggerFactory.getLogger(UnicomService.class);

	@Autowired
	LoginAndGetService loginAndGetService;

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private UnicomCrawlerSartService unicomCrawlerSartService;

	@Async
	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {

		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());

		tracerLog.addTag("taskid", taskMobile.getTaskid());
		tracerLog.addTag("parser.crawler.auth", messageLogin.toString());

		try {
			// 获取通话详单t
			unicomCrawlerSartService.getCallThemhtml(messageLogin, taskMobile);
		} catch (Exception e) {
			tracerLog.addTag("通话详单", e.getMessage());
		}
		try {
			// 获取账户余额
			tracerLog.addTag("获取账户余额", taskMobile.toString());
			unicomCrawlerSartService.getBalanceThem(messageLogin, taskMobile);
		} catch (Exception e) {
			tracerLog.addTag("短信详单", e.getMessage());
		}

		try {
			// 获取缴费详单
			unicomCrawlerSartService.getPayMsgStatusThem(messageLogin, taskMobile);
		} catch (Exception e) {
			tracerLog.addTag("缴费详单", e.getMessage());
		}
		try {
			// 获取历史详单
			unicomCrawlerSartService.getHistoryThem(messageLogin, taskMobile);

		} catch (Exception e) {
			tracerLog.addTag("历史详单", e.getMessage());
		}

		try {
			// 获取积分详单
			unicomCrawlerSartService.getIntegraThem(messageLogin, taskMobile);
		} catch (Exception e) {
			tracerLog.addTag("积分详单", e.getMessage());
		}

		try {
			// 获取积分详单
			unicomCrawlerSartService.getIntegraThem2(messageLogin, taskMobile);
		} catch (Exception e) {
			tracerLog.addTag("积分详单2", e.getMessage());
		}

		try {
			// 获取用户信息 业务套餐
			unicomCrawlerSartService.getUserinfoThem(messageLogin, taskMobile);
		} catch (Exception e) {
			tracerLog.addTag("用户信息 业务套餐", e.getMessage());
		}

		try {
			// 获取短信详单
			unicomCrawlerSartService.getNoteThem(messageLogin, taskMobile);
		} catch (Exception e) {
			tracerLog.addTag("短信详单", e.getMessage());
		}

		return null;

	}

	@Async
	@Override
	public TaskMobile login(MessageLogin messageLogin) {
		tracerLog.addTag("auth", messageLogin.toString());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());

		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getError_code());
		taskMobile.setTesthtml(messageLogin.toString());
		taskMobile.setError_message(null);
		taskMobileRepository.save(taskMobile);
		WebParamUnicom<?> webParamUnicom = null;
		try {
			webParamUnicom = loginAndGetService.login(messageLogin);// 模拟登陆
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("parser.login.error", e.getMessage());
			taskMobile.setPhase(webParamUnicom.getStatusCodeEnum().getPhase());
			taskMobile.setPhase_status(webParamUnicom.getStatusCodeEnum().getPhasestatus());
			taskMobile.setDescription(webParamUnicom.getStatusCodeEnum().getDescription());
			taskMobile.setError_code(webParamUnicom.getStatusCodeEnum().getError_code());
			taskMobile.setError_message(taskMobile.getDescription());
			taskMobileRepository.save(taskMobile);
			return taskMobile;
		}
		if (webParamUnicom.getWebClient() == null) {
			tracerLog.addTag("parser.login.error", "webParamUnicom.getWebClient() is null");
			tracerLog.addTag("parser.login.auth", messageLogin.toString());

			taskMobile.setPhase(webParamUnicom.getStatusCodeEnum().getPhase());
			taskMobile.setPhase_status(webParamUnicom.getStatusCodeEnum().getPhasestatus());
			taskMobile.setDescription(webParamUnicom.getStatusCodeEnum().getDescription());
			taskMobile.setError_code(webParamUnicom.getStatusCodeEnum().getError_code());
			taskMobile.setError_message(webParamUnicom.getStatusCodeEnum().getDescription());
			// 登录失败状态存储
			taskMobileRepository.save(taskMobile);
			return taskMobile;
		} else {
			taskMobile.setPhase(webParamUnicom.getStatusCodeEnum().getPhase());
			taskMobile.setPhase_status(webParamUnicom.getStatusCodeEnum().getPhasestatus());
			taskMobile.setDescription(webParamUnicom.getStatusCodeEnum().getDescription());
			taskMobile.setError_code(webParamUnicom.getStatusCodeEnum().getError_code());
			taskMobile.setError_message(webParamUnicom.getStatusCodeEnum().getDescription());
			String cookieString = CommonUnit.transcookieToJson(webParamUnicom.getWebClient());
			taskMobile.setCookies(cookieString);
			// 数据爬取状态存储
			taskMobileRepository.save(taskMobile);
		}
		return taskMobile;
	}

	// 获取手机验证码
	@Async
	@Override
	public TaskMobile sendSms(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		tracerLog.addTag("taskid", taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getDescription());
		// 发送验证码状态更新
		taskMobileRepository.save(taskMobile);
		WebParamUnicom<?> webParamUnicom = loginAndGetService.getPhonecode(messageLogin, taskMobile);
		if (webParamUnicom.getErrormessage() != null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			tracerLog.addTag("电信获取验证码 getPhoneCode  错误", "==" + webParamUnicom.getErrormessage());
			tracerLog.addTag("电信获取验证码  getPhoneCode html", "==" + webParamUnicom.getHtml());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(webParamUnicom.getErrormessage());
			taskMobile.setDescription(webParamUnicom.getErrormessage());

			// 发送验证码状态更新
			taskMobileRepository.save(taskMobile);
			webParamUnicom.getWebClient().close();

			return taskMobile;
		}

		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
		taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_SUCESS.getCode());
		taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_SUCESS.getMessage());
		String cookieString = CommonUnit.transcookieToJson(webParamUnicom.getWebClient());
		taskMobile.setCookies(cookieString);
		// 发送验证码状态更新
		taskMobileRepository.save(taskMobile);
		tracerLog.addTag("手机验证码获取完成", webParamUnicom.getHtml());

		log.info("======================手机验证码获取完成====================");
		webParamUnicom.getWebClient().close();
		return taskMobile;

	}

	// 手机短信验证码验证
	@Async
	@Override
	public TaskMobile verifySms(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());

		tracerLog.addTag("taskid", taskMobile.getTaskid());
		WebParamUnicom<?> webParamUnicom = null;
		try {
			webParamUnicom = loginAndGetService.verifySms(messageLogin, taskMobile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhasestatus());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getError_code());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getDescription());
			// 手机验证码验证失败状态更新
			taskMobileRepository.save(taskMobile);
			webParamUnicom.getWebClient().close();

			return taskMobile;
		}

		if (webParamUnicom.getErrormessage() != null) {
			tracerLog.addTag("获取验证码  getErrormessage 错误", webParamUnicom.getErrormessage());
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(webParamUnicom.getStatusCodeEnum().getPhase());
			taskMobile.setPhase_status(webParamUnicom.getStatusCodeEnum().getPhasestatus());
			taskMobile.setError_code(webParamUnicom.getStatusCodeEnum().getError_code());
			taskMobile.setError_message(webParamUnicom.getErrormessage());
			taskMobile.setDescription(webParamUnicom.getErrormessage());

			// 手机验证码验证失败状态更新
			taskMobileRepository.save(taskMobile);
			webParamUnicom.getWebClient().close();

			return taskMobile;
		}

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(webParamUnicom.getStatusCodeEnum().getPhase());
		taskMobile.setPhase_status(webParamUnicom.getStatusCodeEnum().getPhasestatus());
		taskMobile.setError_code(webParamUnicom.getStatusCodeEnum().getError_code());
		taskMobile.setDescription(webParamUnicom.getStatusCodeEnum().getDescription());
		String cookieString = CommonUnit.transcookieToJson(webParamUnicom.getWebClient());
		taskMobile.setCookies(cookieString);
		// 手机验证码验证成功状态更新
		taskMobileRepository.save(taskMobile);
		webParamUnicom.getWebClient().close();

		return taskMobile;

	}

	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskMobile sendSmsTwice(MessageLogin messageLogin) {
		// TODO Auto-generated method stub

		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());

		tracerLog.addTag("taskid", taskMobile.getTaskid());
		WebParamUnicom<?> webParamUnicom = null;
		try {
			webParamUnicom = loginAndGetService.sendSmsTwice(messageLogin, taskMobile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR1.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR1.getPhasestatus());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR1.getError_code());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR1.getDescription());
			taskMobileRepository.save(taskMobile);
			
			return taskMobile;
		}

		if (webParamUnicom.getErrormessage() != null) {
			tracerLog.addTag("获取验证码  getErrormessage 错误", webParamUnicom.getErrormessage());
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(webParamUnicom.getStatusCodeEnum().getPhase());
			taskMobile.setPhase_status(webParamUnicom.getStatusCodeEnum().getPhasestatus());
			taskMobile.setError_code(webParamUnicom.getStatusCodeEnum().getError_code());
			taskMobile.setError_message(webParamUnicom.getErrormessage());
			taskMobile.setDescription(webParamUnicom.getErrormessage());

			taskMobileRepository.save(taskMobile);
			
			webParamUnicom.getWebClient().close();

			return taskMobile;
		}

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(webParamUnicom.getStatusCodeEnum().getPhase());
		taskMobile.setPhase_status(webParamUnicom.getStatusCodeEnum().getPhasestatus());
		taskMobile.setError_code(webParamUnicom.getStatusCodeEnum().getError_code());
		taskMobile.setDescription(webParamUnicom.getStatusCodeEnum().getDescription());
		String cookieString = CommonUnit.transcookieToJson(webParamUnicom.getWebClient());
		taskMobile.setCookies(cookieString);
		taskMobileRepository.save(taskMobile);
		webParamUnicom.getWebClient().close();

		return taskMobile;
	}

	@Override
	public TaskMobile verifySmsTwice(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());

		tracerLog.addTag("taskid", taskMobile.getTaskid());
		WebParamUnicom<?> webParamUnicom = null;
		try {
			webParamUnicom = loginAndGetService.verifySmsTwice(messageLogin, taskMobile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR1.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR1.getPhasestatus());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR1.getError_code());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SECOND_ERROR1.getDescription());
			taskMobileRepository.save(taskMobile);
			return taskMobile;
		}

		if (webParamUnicom.getErrormessage() != null) {
			tracerLog.addTag("获取验证码  getErrormessage 错误", webParamUnicom.getErrormessage());
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(webParamUnicom.getStatusCodeEnum().getPhase());
			taskMobile.setPhase_status(webParamUnicom.getStatusCodeEnum().getPhasestatus());
			taskMobile.setError_code(webParamUnicom.getStatusCodeEnum().getError_code());
			taskMobile.setError_message(webParamUnicom.getErrormessage());
			taskMobile.setDescription(webParamUnicom.getErrormessage());

			taskMobileRepository.save(taskMobile);
			return taskMobile;
		}

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(webParamUnicom.getStatusCodeEnum().getPhase());
		taskMobile.setPhase_status(webParamUnicom.getStatusCodeEnum().getPhasestatus());
		taskMobile.setError_code(webParamUnicom.getStatusCodeEnum().getError_code());
		String cookieString = CommonUnit.transcookieToJson(webParamUnicom.getWebClient());
		taskMobile.setCookies(cookieString);
		taskMobileRepository.save(taskMobile);
		return taskMobile;
		
	}

}