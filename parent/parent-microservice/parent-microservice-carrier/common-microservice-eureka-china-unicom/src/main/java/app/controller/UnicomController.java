package app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.StatusCodeEnum;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.bean.UnicomChangePasswordResult;
import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.LoginAndGetService;
import app.service.UnicomChangePassWordService;
import app.service.UnicomService;

@Component
@RestController
@RequestMapping("/carrier")
public class UnicomController {
	public static final Logger log = LoggerFactory.getLogger(UnicomController.class);
	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Autowired
	private UnicomService unicomService;

	@Autowired
	private UnicomChangePassWordService unicomChangePassWordService;
	@Autowired
	private TracerLog tracerLog;
	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private AgentService agentService;

	@Autowired
	private LoginAndGetService loginAndGetService;

	

	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public TaskMobile unicomcrawler(@RequestBody MessageLogin messageLogin) {
		tracerLog.addTag("taskid",messageLogin.getTask_id());
		tracerLog.addTag("------------UnicomController getAllData-------------", messageLogin.toString());

		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());

		unicomService.getAllData(messageLogin);

		return taskMobile;

	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public TaskMobile unicomlogin(@RequestBody MessageLogin messageLogin) {
		tracerLog.addTag("中国联通登录 ", messageLogin.toString());
		tracerLog.addTag("taskid", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());

		unicomService.login(messageLogin);
		ResultData<TaskMobile> resultmessagedatat = new ResultData<>();
		resultmessagedatat.setData(taskMobile);
		return taskMobile;
	}

	@RequestMapping(value = "/getphonecode", method = RequestMethod.POST)
	public ResultData<TaskMobile> telecomgetcode(@RequestBody MessageLogin messageLogin) {
		tracerLog.addTag("中国联通获取验证码 ", messageLogin.toString());
		tracerLog.addTag("taskid", messageLogin.getTask_id());
		tracerLog.addTag("auth", messageLogin.getName());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getError_code());
		taskMobile = taskMobileRepository.save(taskMobile);

		unicomService.sendSms(messageLogin);
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}

	
	@PostMapping(path = "/setphonecodeAgent")
	public TaskMobile loginAgent(@RequestBody MessageLogin messageLogin) {

		tracerLog.addTag("中国联通登录集群的调用...", messageLogin.getTask_id());

		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());

		try {
			taskMobile = agentService.postAgent(messageLogin, "/carrier/setphonecode", 300000L);
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

	
	@RequestMapping(value = "/setphonecode", method = RequestMethod.POST)
	public ResultData<TaskMobile> telecomsetcode(@RequestBody MessageLogin messageLogin) {
		tracerLog.addTag("中国联通验证验证码 ", messageLogin.toString());
		tracerLog.addTag("taskid", messageLogin.getTask_id());
		tracerLog.addTag("telecomsetcode", messageLogin.toString());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());


		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getError_code());
		taskMobileRepository.save(taskMobile);

		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);

		unicomService.verifySms(messageLogin);

		return result;
	}
	
	
	@RequestMapping(value = "/verifySmsTwice", method = RequestMethod.POST)
	public ResultData<TaskMobile> verifySmsTwice(@RequestBody MessageLogin messageLogin) {
		tracerLog.addTag("中国联通验证验证码 ", messageLogin.toString());
		tracerLog.addTag("taskid", messageLogin.getTask_id());
		tracerLog.addTag("telecomsetcode", messageLogin.toString());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());


		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getError_code());
		taskMobileRepository.save(taskMobile);

		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);

		unicomService.verifySmsTwice(messageLogin);

		return result;
	}
	
	@RequestMapping(value = "/sendSmsTwice", method = RequestMethod.POST)
	public ResultData<TaskMobile> sendSmsTwice(@RequestBody MessageLogin messageLogin) {
		tracerLog.addTag("中国联通验证验证码 ", messageLogin.toString());
		tracerLog.addTag("taskid", messageLogin.getTask_id());
		tracerLog.addTag("telecomsetcode", messageLogin.toString());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());


		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_READY_CODE_SECOND_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_READY_CODE_SECOND_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_READY_CODE_SECOND_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_READY_CODE_SECOND_DONING.getError_code());
		taskMobileRepository.save(taskMobile);

		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);

		unicomService.sendSmsTwice(messageLogin);

		return result;
	}
	

	@RequestMapping(value = "/passwordlogin", method = RequestMethod.POST)
	public TaskMobile unicompasswordlonin(@RequestBody UnicomChangePasswordResult result) {
		tracerLog.addTag("中国联通修改验证码 ", result.toString());
		tracerLog.addTag("taskid", result.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(result.getTask_id());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getError_code());
		unicomChangePassWordService.passwordlongin(result, taskMobile);
		ResultData<TaskMobile> resultmessagedatat = new ResultData<>();
		resultmessagedatat.setData(taskMobile);
		return taskMobile;
	}

	@RequestMapping(value = "/passwordgetCode", method = RequestMethod.POST)
	public TaskMobile unicompasswordgetCode(@RequestBody UnicomChangePasswordResult result) {
		log.info("==============>中国联通修改密码验证 获取验证码<====================  ");
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(result.getTask_id());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getError_code());
		taskMobile.setError_message(null);
		unicomChangePassWordService.getPhoneCode(result, taskMobile);
		ResultData<TaskMobile> resultmessagedatat = new ResultData<>();
		resultmessagedatat.setData(taskMobile);
		return taskMobile;
	}

	@RequestMapping(value = "/passwordsetCode", method = RequestMethod.POST)
	public TaskMobile unicompasswordsetCode(@RequestBody UnicomChangePasswordResult result) {
		log.info("==============>中国联通修改密码验证 set验证码<====================  ");
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(result.getTask_id());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_DONING.getError_code());
		taskMobile.setError_message(null);
		unicomChangePassWordService.setPhoneCode(result, taskMobile);
		ResultData<TaskMobile> resultmessagedatat = new ResultData<>();
		resultmessagedatat.setData(taskMobile);
		return taskMobile;
	}

	@RequestMapping(value = "/passwordchange", method = RequestMethod.POST)
	public TaskMobile unicompasswordchange(@RequestBody UnicomChangePasswordResult result) {
		log.info("==============>中国联通修改密码<====================  ");
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(result.getTask_id());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_DONING.getError_code());
		taskMobile.setError_message(null);
		unicomChangePassWordService.passwordchange(result, taskMobile);
		ResultData<TaskMobile> resultmessagedatat = new ResultData<>();
		resultmessagedatat.setData(taskMobile);
		return taskMobile;
	}

	@PostMapping(path = "/quit")
	public void quit(@RequestBody MessageLogin messageLogin) {
		loginAndGetService.quit(messageLogin);
	}
}