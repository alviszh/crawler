package app.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;

import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.commontracerlog.TracerLog;
import app.service.TelecomBeiJingPageService;
import app.service.TelecomBeijongControllerService;

@RestController
@RequestMapping("/carrier")
public class TelecomBeiJingController {

	@Autowired
	private TelecomBeiJingPageService telecomBeiJingPageService;

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private TelecomBeijongControllerService telecomBeijongControllerService;

	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskMobile> telecom(@RequestBody MessageLogin messageLogin) throws Exception {
		tracerLog.output("taskid", messageLogin.getTask_id());

		TaskMobile taskMobile = telecomBeiJingPageService.findtaskMobile(messageLogin.getTask_id());
		tracerLog.output("parser.crawler.taskid", taskMobile.getTaskid());
		tracerLog.output("parser.crawler.auth", messageLogin.getName());

		telecomBeiJingPageService.save(taskMobile);
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		telecomBeijongControllerService.getAllData(messageLogin);
		result.setData(taskMobile);
		return result;

	}


}
