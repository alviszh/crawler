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
public class TelecomYunNanController extends TelecomBasicController{

	public static final Logger log = LoggerFactory.getLogger(TelecomYunNanController.class);

	@Autowired
	private TelecomCrawlerService telecomCrawlerService;
	
	
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
		tracerLog.addTag("parser.crawler.taskid 发送手机验证码", messageLogin.getTask_id());

		tracerLog.addTag("parser.crawler.taskid", messageLogin.getTask_id());
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
		tracerLog.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracerLog.addTag("parser.crawler.auth", messageLogin.getName());
		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id());

		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getError_code());

		telecomCrawlerService.verifySms(messageLogin);
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}
	

}
