package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.commontracerlog.TracerLog;

/**
 * 
 * 项目名称：common-microservice-telecom-heilongjiang 类名称：TelecomControllerService
 * 类描述： 创建人：hyx 创建时间：2018年10月25日 上午10:28:08
 * 
 * @version
 */
@Component
@EnableAsync
public class TelecomControllerService {

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private TelecomCrawlerService telecomCrawlerService;

	@Autowired
	private TelecomAsyncService telecomAsyncService;

	@Async
	public TaskMobile sendSms(MessageLogin messageLogin) {

		tracerLog.output("parser.crawler.taskid 发送手机验证码", messageLogin.getTask_id());

		tracerLog.output("taskid", messageLogin.getTask_id());
		tracerLog.output("parser.crawler.auth", messageLogin.getName());
		TaskMobile taskMobile = telecomCrawlerService.findtaskMobile(messageLogin.getTask_id());

		telecomCrawlerService.sendSms(messageLogin);
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);

		return taskMobile;

	}

	@Async
	public TaskMobile verifySms(MessageLogin messageLogin) {

		tracerLog.output("taskid", messageLogin.getTask_id());
		tracerLog.output("parser.crawler.auth", messageLogin.getName());
		tracerLog.output("parser.crawler", messageLogin.toString());

		TaskMobile taskMobile = telecomCrawlerService.verifySms(messageLogin);
		// taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
		if (taskMobile.getPhase().equals(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase())
				&& taskMobile.getPhase_status().equals(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus())) {
			telecomAsyncService.getAllData(messageLogin);
		}

		return taskMobile;

	}
}
