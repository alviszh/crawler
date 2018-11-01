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
import app.service.CrawlerStatusMobileService;
import app.service.TelecomCommonNingXiaService;
import app.service.TelecomUnitCommomNingXiaService;

@RestController
@RequestMapping("/carrier")
public class TelecomNingXiaController {
	@Autowired
	private TelecomCommonNingXiaService telecomCommonNingXiaService;
	@Autowired
	private TelecomUnitCommomNingXiaService telecomUnitCommomNingXiaService;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private TracerLog tracer;

	////////////////////////////////////////////////// 用户信息的爬取和采集///////////////////////////////////////////////////////////
	@RequestMapping(value = "/crawler", method = { RequestMethod.POST })
	public ResultData<TaskMobile> crawler(@RequestBody MessageLogin messageLogin) {
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getName());
		TaskMobile taskMobile = telecomCommonNingXiaService.findtaskMobile(messageLogin.getTask_id().trim());
		// 准备开始
		taskMobile.setFamilyMsgStatus(200);
		taskMobile.setIntegralMsgStatus(200);
		telecomCommonNingXiaService.save(taskMobile);
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);

		// 所有爬取业务的入口
		telecomUnitCommomNingXiaService.getAllData(messageLogin);

		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());

		return result;
	}

	@RequestMapping(value = "/getphonecode", method = { RequestMethod.POST })
	public ResultData<TaskMobile> getYzm(@RequestBody MessageLogin messageLogin) {
		System.out.println(messageLogin.toString());
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getName());
		TaskMobile taskMobile = telecomCommonNingXiaService.findtaskMobile(messageLogin.getTask_id().trim());

		telecomCommonNingXiaService.sendSms(messageLogin);

		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}

	@RequestMapping(value = "/setphonecode", method = { RequestMethod.POST })
	public ResultData<TaskMobile> setYzm(@RequestBody MessageLogin messageLogin) {
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getName());
		TaskMobile taskMobile = telecomCommonNingXiaService.findtaskMobile(messageLogin.getTask_id().trim());

		telecomCommonNingXiaService.verifySms(messageLogin);

		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}

}
