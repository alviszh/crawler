package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.commontracerlog.TracerLog;
import app.service.AsyncjiangsuGetAllDataService;
import app.service.TelecomjiangsuService;

@RestController
@RequestMapping("/carrier") 
public class TelecomjiangsuController {

	@Autowired
	private AsyncjiangsuGetAllDataService asyncjiangsuGetAllDataService;
	
	@Autowired
	private TelecomjiangsuService telecomjiangsuService;
	
	@Autowired
	private TracerLog tracer;

	@RequestMapping(value = "/jiangsu/login", method = { RequestMethod.POST })
	public ResultData<TaskMobile> login(@RequestBody MessageLogin messageLogin) {
		
		tracer.qryKeyValue("parser.login.taskid",messageLogin.getTask_id());
		tracer.addTag("parser.login.auth",messageLogin.getName());
		
//		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGINTWO_LOADING.getPhase());
//		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGINTWO_LOADING.getPhasestatus());
//		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGINTWO_LOADING.getDescription());
//		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_LOGINTWO_LOADING.getError_code());
//		taskMobile.setError_message(null);
//		telecomjiangsuService.save(taskMobile);

		TaskMobile taskMobile = telecomjiangsuService.findtaskMobile(messageLogin.getTask_id());
		
		telecomjiangsuService.login(messageLogin);
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
		
	}
	
	/**
	 * 可跳过验证码
	 * 发送手机验证码
	 * @param messageLogin
	 * @return
	 */
//	@RequestMapping(value = "/getphonecode", method = RequestMethod.POST)
//	public ResultData<TaskMobile> telecomgetcode(@RequestBody   MessageLogin messageLogin) {
//		tracer.addTag("parser.crawler.taskid 发送手机验证码",messageLogin.getTask_id());
//
//		tracer.addTag("parser.crawler.taskid",messageLogin.getTask_id());
//		tracer.addTag("parser.crawler.auth",messageLogin.getName());
//		TaskMobile taskMobile = telecomService.findtaskMobile(messageLogin.getTask_id());
//		
//		telecomService.getPhoneCode(messageLogin,taskMobile);
//		
//		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
//		result.setData(taskMobile);
//		return result;
//	}
	
	
	/**
	 * 可跳过验证码
	 * 验证手机验证码
	 * @param messageLogin
	 * @return
	 */
//	@RequestMapping(value = "/setphonecode", method = RequestMethod.POST)
//	public ResultData<TaskMobile> verificationcode(@RequestBody   MessageLogin messageLogin) {
//		tracer.addTag("parser.crawler.taskid 验证手机验证码",messageLogin.getTask_id());
//
//		tracer.addTag("parser.crawler.taskid",messageLogin.getTask_id());
//		tracer.addTag("parser.crawler.auth",messageLogin.getName());
//		TaskMobile taskMobile = telecomService.findtaskMobile(messageLogin.getTask_id());
//		
//		telecomService.verificationcode(messageLogin,taskMobile);
//		
//		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
//		result.setData(taskMobile);
//		return result;
//	}
	
	
	/**
	 * 爬取数据接口
	 * @param messageLogin
	 * @return
	 */
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskMobile> telecom(@RequestBody  MessageLogin messageLogin) {
		tracer.qryKeyValue("parser.crawler.taskid",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth",messageLogin.getName());
		
		asyncjiangsuGetAllDataService.getAllData(messageLogin);
		
//		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
//		boolean isCrawler = telecomService.isDoing(messageLogin);
//		if(isCrawler){
//			tracer.addTag("正在进行上次未完成的爬取任务。。。",messageLogin.toString());
//		}else{
//			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
//			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
//			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
//			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
//			telecomService.save(taskMobile);
//			tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
//			tracer.addTag("parser.crawler.auth", messageLogin.getName());
//			asyncjiangsuGetAllDataService.getAllData(taskMobile,messageLogin);
//			result.setData(taskMobile);
//			return result;
//		}
		
		return null;
		
	}
	

}
