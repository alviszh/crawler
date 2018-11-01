package app.contoller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.service.common.AgentService;
import app.service.login.TelecomLoginAsyncService;
import app.service.login.TelecomLoginService;

@RestController
@RequestMapping("/carrier")
public class TelecomLoginController extends TelecomBasicController {

	public static final Logger log = LoggerFactory.getLogger(TelecomLoginController.class);

	@Autowired
	private TelecomLoginAsyncService telecomLoginAsyncService;
	
	@Autowired
	private TelecomLoginService telecomLoginService;
	

	@Autowired
	private AgentService agentService;
	
	@PostMapping(path = "/loginAgent")
	public TaskMobile loginAgent(@RequestBody MessageLogin messageLogin)
			throws Exception {

		tracerLog.qryKeyValue("中国电信登录集群的调用...", messageLogin.getTask_id());

		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id().trim());

		try {
			taskMobile = agentService.postAgent(messageLogin, "/carrier/login", 300000L);
		} catch (Exception e) {
			e.printStackTrace();
			taskMobile.setError_message(e.getMessage());
			taskMobile.setTesthtml(messageLogin.toString());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_AGENT_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_AGENT_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_AGENT_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_AGENT_ERROR.getError_code());
			taskMobile = taskMobileRepository.save(taskMobile);
	
			tracerLog.addTag("BeijingBankDebitCardController.loginAgent.exception", e.getMessage());
			return taskMobile;
		}

		return taskMobile;
	}
	

	@RequestMapping(value = "/login", method = { RequestMethod.POST })
	public ResultData<TaskMobile> login(@RequestBody MessageLogin messageLogin) {
		tracerLog.addTag("中国电信登录  ", messageLogin.toString());

		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id().trim());
		
		System.out.println(taskMobile.toString());
//		try {
//			taskMobile = agentService.postAgent(messageLogin, "/carrier/login");
//		} catch (Exception e) {
//
//			ResultData<TaskMobile> result = new ResultData<TaskMobile>();
//			taskMobile.setError_message("NoIdleInstance 没有闲置可用的实例 ");
//			result.setData(taskMobile);
//			return result;
//
//		}

		telecomLoginAsyncService.login(messageLogin);

		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}

	@PostMapping(path = "/quit")
	public void quit(@RequestBody MessageLogin messageLogin) {
		telecomLoginService.quit(messageLogin);
	}

//	@RequestMapping(value = "/login/test", method = { RequestMethod.POST })
//	public ResultData<TaskMobile> logintest(@RequestBody MessageLogin messageLogin) {
//		tracerLog.addTag("中国电信登录  ", messageLogin.toString());
//		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id().trim());
//		taskMobile = agentService.postAgent(messageLogin, "/carrier/login");
//		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getPhase());
//		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getPhasestatus());
//		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getDescription());
//		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getError_code());
//		taskMobile = agentService.postAgent(messageLogin, "/carrier//login/test");
//		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
//		return result;
//	}
}
