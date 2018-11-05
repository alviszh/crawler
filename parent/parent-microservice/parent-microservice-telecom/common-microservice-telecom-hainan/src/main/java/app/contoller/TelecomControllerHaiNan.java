package app.contoller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.service.TelecomCrawlerService;

@RestController
@RequestMapping("/carrier")
public class TelecomControllerHaiNan extends TelecomBasicController {

	public static final Logger log = LoggerFactory.getLogger(TelecomControllerHaiNan.class);

	@Autowired
	private TelecomCrawlerService telecomCrawlerService;
	
	@RequestMapping(value = "/loginhainan", method = { RequestMethod.POST })
	public ResultData<TaskMobile> login(@RequestBody MessageLogin messageLogin) {
		log.info("中国电信登录  ");
		log.info(messageLogin.toString());

		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id().trim());
//		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getPhase());
//		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getPhasestatus());
//		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getDescription());
//		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getError_code());
//
//		telecomCrawlerService.login(messageLogin);
		
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGINTWO_SUCCESS.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGINTWO_SUCCESS.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGINTWO_SUCCESS.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_LOGINTWO_SUCCESS.getError_code());
//		String cookieString = CommonUnit.transcookieToJson(webParamTelecom.getWebClient());
//		taskMobile.setCookies(cookieString);
		taskMobileRepository.save(taskMobile);
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}
	


	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskMobile> telecom(@RequestBody MessageLogin messageLogin) {
		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id());

		ResultData<TaskMobile> result = new ResultData<TaskMobile>();

		telecomCrawlerService.getAllData(messageLogin);

		result.setData(taskMobile);
		return result;
	}

	@RequestMapping(value = "/getphonecode", method = RequestMethod.POST)
	public ResultData<TaskMobile> telecomgetcode(@RequestBody MessageLogin messageLogin) {
		tracerLog.addTag("taskid 发送手机验证码", messageLogin.getTask_id());

		tracerLog.addTag("parser.crawler.auth", messageLogin.getName());
		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id());

		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getError_code());

		telecomCrawlerService.sendSms(messageLogin);
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}

	@RequestMapping(value = "/setphonecode", method = RequestMethod.POST)
	public ResultData<TaskMobile> telecomsetcode(@RequestBody MessageLogin messageLogin) {
		tracerLog.addTag("parser.crawler.taskid 验证手机验证码", messageLogin.getTask_id());
		tracerLog.addTag("taskid", messageLogin.getTask_id());
		tracerLog.addTag("parser.crawler.auth", messageLogin.getName());
		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id());

		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getError_code());
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		

		telecomCrawlerService.verifySms(messageLogin);
		
		return result;
	}


}
