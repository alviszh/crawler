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
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.commontracerlog.TracerLog;
import app.service.common.TelecomCommonLoginService;

@RestController
@RequestMapping("/carrier")
public class TelecomCommonController extends TelecomBasicController{

	public static final Logger log = LoggerFactory.getLogger(TelecomCommonController.class);

	@Autowired
	private TelecomCommonLoginService telecomCommonLoginService;

	@Autowired
	private TracerLog tracerLog;

	@RequestMapping(value = "/login", method = { RequestMethod.POST })
	public ResultData<TaskMobile> login(@RequestBody MessageLogin messageLogin) {
		tracerLog.addTag("taskid", messageLogin.getTask_id());
		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id());
		telecomCommonLoginService.login(messageLogin);

		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}
}
