package app.service.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.common.TelecomCommonHtml;
import com.microservice.dao.entity.crawler.telecom.common.TelecomCommonPointsAndCharges;
import com.microservice.dao.entity.crawler.telecom.common.TelecomStarlevel;

import app.bean.WebParamTelecomByChrome;
import app.crawler.telecom.htmlparse.TelecomParseCommon;
import app.service.aop.ICrawlerLogin;
import app.service.common.LoginAndGetService;
import app.service.common.TelecomBasicService;

@Component
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.common")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.common")
public class TelecomLoginAsyncService extends TelecomBasicService implements ICrawlerLogin{

	public static final Logger log = LoggerFactory.getLogger(TelecomLoginAsyncService.class);


	@Autowired
	private LoginAndGetService loginAndGetService;
	
	@Autowired
	private TelecomLoginService telecomLoginService;
	

	// 模拟登陆并获取手机短信验证码
	@Async
	@Override
	public TaskMobile login(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());

		tracerLog.addTag("parser.login.taskid", taskMobile.getTaskid());
		tracerLog.addTag("parser.login.auth", messageLogin.getName());

//		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhase());
//		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhasestatus());
//		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getDescription());
//		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getError_code());
//		taskMobile.setError_message(null);
		taskMobile.setTesthtml(messageLogin.getPassword().trim());
		save(taskMobile);
		taskMobile = findtaskMobile(messageLogin.getTask_id().trim());
		WebParamTelecomByChrome<?> webParamTelecomByChrome = new WebParamTelecomByChrome<Object>();
		try {
			webParamTelecomByChrome = telecomLoginService.login(messageLogin);
			if (webParamTelecomByChrome.getErrormessage() != null) {

				taskMobile.setPhase(StatusCodeEnum.MESSAGE_LOGIN_ERROR_TWO.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.MESSAGE_LOGIN_ERROR_TWO.getPhasestatus());
				taskMobile.setDescription(webParamTelecomByChrome.getErrormessage());

				taskMobile.setError_code(StatusCodeEnum.MESSAGE_LOGIN_ERROR_TWO.getError_code());
				taskMobile.setError_message(webParamTelecomByChrome.getErrormessage());
				taskMobile.setTesthtml(messageLogin.toString());

				// 登录失败状态存储
				save(taskMobile);
				return taskMobile;
			}
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getDescription());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getError_code());
			String cookieString = CommonUnit.transcookieToJson(webParamTelecomByChrome.getWebClient());
			taskMobile.setCookies(cookieString);
			taskMobile.setTesthtml(messageLogin.getPassword().trim());
			taskMobile.setNexturl(webParamTelecomByChrome.getCityCodeForHeBei());

			// 登录成功状态更新r
			save(taskMobile);
			try{
				telecomLoginService.quit(messageLogin);

			}catch(Exception e) {
				e.printStackTrace();
			}
			try {
				getPointsAndCharges(messageLogin, taskMobile);// 成功
			} catch (Exception e) {
				tracerLog.addTag("parser.crawler.auth", "getPointsAndCharges" + e.toString());
			}
			
			try {
				getStarlevel(messageLogin, taskMobile);// 成功
			} catch (Exception e) {
				tracerLog.addTag("parser.crawler.auth", "getStarlevel" + e.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_Nine.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_Nine.getMessage());
			taskMobile.setTesthtml(messageLogin.getPassword().trim());
			tracerLog.addTag("parser.login.auth", e.getMessage());
			// 登录失败状态更新
			save(taskMobile);
		}
		return taskMobile;
	}

	// 抓取用户余额
	@Async
	public String getPointsAndCharges(MessageLogin messageLogin, TaskMobile taskMobile) {
		tracerLog.qryKeyValue("==============>中国电信抓取客户余额和积<===============", messageLogin.getTask_id());
		String html = null;
		try {
			html = loginAndGetService.getPointsAndCharges(messageLogin, taskMobile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		tracerLog.addTag("==============>中国电信抓取客余额和积 <===============", "<xmp>" + html + "</xmp>");

		tracerLog.addTag("==============>中国电信抓取客户余额和积月  <===============", taskMobile.toString());
		System.out.println("<xmp>" + html + "</xmp>");

		if (html == null) {
			return null;
		}

		tracerLog.qryKeyValue("==============>中国电信抓取客户用户余额和积  存储客户账单信息详单<===============", messageLogin.getTask_id());
		TelecomCommonPointsAndCharges result = TelecomParseCommon.pointsAndCharges_parse(html);
		if (result == null) {
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}
		result.setUserid(messageLogin.getUser_id());
		result.setTaskid(taskMobile.getTaskid());
		save(result);
		tracerLog.qryKeyValue("==============>中国电信抓取客户用户余额和积  存储客户账单信息详单<===============", messageLogin.getTask_id());
		return "sucess";
	}

	/**
	 * 用户星级服务信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public void getStarlevel(MessageLogin messageLogin,TaskMobile taskMobile) {
		tracerLog.qryKeyValue("TelecomShanxi1Service.getStarlevel", taskMobile.getTaskid());
		try {
			WebParamTelecomByChrome<TelecomStarlevel> webParam = loginAndGetService.getStarlevel(taskMobile,0);

			if (null != webParam) {

				if (null != webParam.getList()) {

					telecomCommonStarlevelRepository.saveAll(webParam.getList());

					tracerLog.qryKeyValue("TelecomShanxi1Service.getStarlevel---用户星级服务信息","用户星级服务信息已入库!" + taskMobile.getTaskid());
				}

				TelecomCommonHtml telecomCommonHtml = new TelecomCommonHtml(taskMobile.getTaskid(),
						"telecom_starlevel", "1", webParam.getUrl(), webParam.getHtml());
				telecomCommonHtmlRepository.save(telecomCommonHtml);

				tracerLog.qryKeyValue("用户星级服务信息源码", "用户星级服务信息源码表入库!" + taskMobile.getTaskid());

			} else {
				tracerLog.qryKeyValue("webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {

			e.printStackTrace();
			tracerLog.qryKeyValue("TelecomShanxi1Service.getStarlevel---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}
	}

	public boolean isDoing(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		tracerLog.addTag("正在进行上次未完成的爬取任务。。。", taskMobile.toString());
		if ("CRAWLER".equals(taskMobile.getPhase()) && "DOING".equals(taskMobile.getPhase_status())) {
			return true;
		}
		return false;
	}

	protected void save(TaskMobile taskMobile) {
		System.out.println(taskMobile.toString());
		taskMobileRepository.save(taskMobile);
	}

	protected TaskMobile findtaskMobile(String taskid) {
		return taskMobileRepository.findByTaskid(taskid);
	}

	protected void save(TelecomCommonPointsAndCharges result) {
		telecomCommonPointsAndChargesRepository.save(result);
	}
	
	protected void save(TelecomCommonHtml result) {
		telecomCommonHtmlRepository.save(result);
	}


	@Override
	public TaskMobile getAllDataDone(String taskId) {
		return null;
	}

	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		return null;
	}

}