package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.service.TelecomGuangxiCommonService;


@RestController
@RequestMapping("/carrier") 
public class TelecomGuangxiController {

	
	@Autowired
	private TelecomGuangxiCommonService telecomGuangxiCommonService;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TracerLog tracer;
	//登陆
	@RequestMapping(value = "/guangxi/login", method = { RequestMethod.POST })
	public ResultData<TaskMobile> login(@RequestBody MessageLogin messageLogin) throws Exception {
		tracer.addTag("parser.login.ready", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		telecomGuangxiCommonService.login(messageLogin);
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}
	
	// 2 爬取详单
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskMobile> crawler(@RequestBody MessageLogin messageLogin)throws Exception {
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		tracer.addTag("parser.crawler.taskid", taskMobile.getTaskid());
		tracer.addTag("parser.crawler.auth", messageLogin.getName());
		telecomGuangxiCommonService.getAllData(messageLogin);
		result.setData(taskMobile);
		return result;
	}
	
//	//第一次爬取
//	@RequestMapping(value = "/crawlerFirst", method = RequestMethod.POST)
//	public ResultData<TaskMobile> crawlerFirst(@RequestBody MessageLogin messageLogin)throws Exception {
//		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
//		boolean isCrawler = isDoing(messageLogin);
//		TaskMobile taskMobile = telecomGuangxiCommonService.findtaskMobile(messageLogin.getTask_id());
//		if (isCrawler) {
//			tracer.addTag("正在进行上次未完成的爬取任务。。。", messageLogin.toString());
//			result.setData(taskMobile);
//			return result;
//		} else {
////			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
////			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
////			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
////			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
//			tracer.addTag("parser.crawler.taskid", taskMobile.getTaskid());
//			tracer.addTag("parser.crawler.auth", messageLogin.getName());
//			telecomGuangxiCommonService.save(taskMobile);
//			telecomGuangxiCommonService.getCrawlerFirst(messageLogin,taskMobile);
//			result.setData(taskMobile);
//			return result;
//		}
//
//	}
	
	
	
	//第2次获取验证码
	@RequestMapping(value = "/getphonecodeTwo", method = RequestMethod.POST)
	public ResultData<TaskMobile> getphonecodeTwo(@RequestBody   MessageLogin messageLogin) {
		tracer.addTag("parser.crawler.taskid.getphonecodeTwo",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.taskid",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth",messageLogin.getName());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		telecomGuangxiCommonService.sendSmsTwice(messageLogin);
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}
	
	
	//第1次获取验证码
	@RequestMapping(value = "/getphonecodeFirst", method = RequestMethod.POST)
	public ResultData<TaskMobile> getphonecodeFirst(@RequestBody   MessageLogin messageLogin) {
		tracer.addTag("parser.crawler.taskid.getphonecodeFirst",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.taskid",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth",messageLogin.getName());
//		TaskMobile taskMobile = telecomGuangxiCommonService.findtaskMobile(messageLogin.getTask_id());
		telecomGuangxiCommonService.sendSms(messageLogin);
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
//		taskMobile = iSms.sendSms(messageLogin);
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}
	
	//第2次验证
	@RequestMapping(value = "/setphonecodeTwo", method = RequestMethod.POST)
	public ResultData<TaskMobile> setphonecodeTwo(@RequestBody   MessageLogin messageLogin) {
		tracer.addTag("parser.crawler.taskid.setphonecodeTwo",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.taskid",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth",messageLogin.getName());
//		TaskMobile taskMobile = telecomGuangxiCommonService.findtaskMobile(messageLogin.getTask_id());
		telecomGuangxiCommonService.verifySmsTwice(messageLogin);
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
//		taskMobile = iSmsTwice.verifySmsTwice(messageLogin);
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}
	
	
	//第1次验证
	@RequestMapping(value = "/setphonecodeFirst", method = RequestMethod.POST)
	public ResultData<TaskMobile> setphonecodeFirst(@RequestBody   MessageLogin messageLogin) {
		tracer.addTag("parser.crawler.taskid.setphonecodeFirst",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.taskid",messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth",messageLogin.getName());
//		TaskMobile taskMobile = telecomGuangxiCommonService.findtaskMobile(messageLogin.getTask_id());
		telecomGuangxiCommonService.verifySms(messageLogin);
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
//		taskMobile = iSms.verifySms(messageLogin);
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}
	
//	//实名认证
//	@RequestMapping(value ="/realName" , method = RequestMethod.POST)
//	public ResultData<TaskMobile> RealName(@RequestBody MessageLogin messageLogin){
//		TaskMobile taskMobile = telecomGuangxiCommonService.findtaskMobile(messageLogin.getTask_id());
//		telecomGuangxiCommonService.realName(messageLogin,taskMobile);
//		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
//		result.setData(taskMobile);
//		return result;
//	}
	

	public boolean isDoing(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
//		System.out.println(taskMobile.toString());
		if (null == taskMobile) {
			return false;
		}
		if ("CRAWLER".equals(taskMobile.getPhase()) && "DOING".equals(taskMobile.getPhase_status())) {
			return true;
		}
		return false;
	}
}
