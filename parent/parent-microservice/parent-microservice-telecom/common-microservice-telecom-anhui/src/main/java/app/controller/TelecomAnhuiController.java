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
import app.service.TelecomCommonAnhuiService;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISms;
    
@RestController     
@RequestMapping("/carrier")
public class TelecomAnhuiController {    
	@Autowired  
	private TelecomCommonAnhuiService telecomCommonAnhuiService;
	@Autowired
	private TracerLog tracer;
	@Autowired 
	private ICrawlerLogin iCrawlerLogin;
	@Autowired 
	private ISms iSms;
		// 登陆wap
		@RequestMapping(value = "/anhui/login", method = { RequestMethod.POST })
		public ResultData<TaskMobile> login(@RequestBody MessageLogin messageLogin) throws Exception {

			TaskMobile taskMobile = telecomCommonAnhuiService.findtaskMobile(messageLogin.getTask_id());
			tracer.addTag("parser.login.ready", messageLogin.getTask_id());
			telecomCommonAnhuiService.login(messageLogin);
//			iCrawlerLogin.login(messageLogin);
			tracer.addTag("login.login.ready.start", taskMobile.toString());
			ResultData<TaskMobile> result = new ResultData<TaskMobile>();
			result.setData(taskMobile);
			return result;
	    }
		
		
//	    // 登陆Net
//		@RequestMapping(value = "/anhui/loginNet", method = { RequestMethod.POST })
//		public ResultData<TaskMobile> loginNet(@RequestBody MessageLogin messageLogin) throws Exception {
//
//			TaskMobile taskMobile = telecomCommonAnhuiService.findtaskMobile(messageLogin.getTask_id());
//
//			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getPhase());
//			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getPhasestatus());
//			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getDescription());
//			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_READY_SUCESSES.getError_code());
//			tracer.addTag("parser.login.ready", messageLogin.getTask_id());
//
//			telecomCommonAnhuiService.loginNet(messageLogin, taskMobile);
//
//			ResultData<TaskMobile> result = new ResultData<TaskMobile>();
//			result.setData(taskMobile);
//			return result;
//		}
//	// 第一次爬取
//	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
//	public ResultData<TaskMobile> crawler(@RequestBody MessageLogin messageLogin) throws Exception {
//		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
//		boolean isCrawler = isDoing(messageLogin);
//		TaskMobile taskMobile = telecomCommonAnhuiService.findtaskMobile(messageLogin.getTask_id());
//		if (isCrawler) {
//			tracer.addTag("正在进行上次未完成的爬取任务。。。", messageLogin.toString());
//			result.setData(taskMobile);
//			return result;
//		} else {
//
//			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
//			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
//			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
//			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
//			tracer.addTag("parser.crawler.taskid", taskMobile.getTaskid());
//			tracer.addTag("parser.crawler.auth", messageLogin.getName());
//
//			telecomCommonAnhuiService.save(taskMobile);
//
//			telecomCommonAnhuiService.getAllData(messageLogin, taskMobile);
//			result.setData(taskMobile);
//			return result;
//		}
//
//	}
	
	
	    // 第2次爬取 wap
		@RequestMapping(value = "/crawler", method = RequestMethod.POST)
		public ResultData<TaskMobile> crawlerTwo(@RequestBody MessageLogin messageLogin) throws Exception {
			ResultData<TaskMobile> result = new ResultData<TaskMobile>();
			TaskMobile taskMobile = telecomCommonAnhuiService.findtaskMobile(messageLogin.getTask_id());
			tracer.addTag("parser.crawler.taskid", taskMobile.getTaskid());
			tracer.addTag("parser.crawler.auth", messageLogin.getName());
			telecomCommonAnhuiService.save(taskMobile);
//			telecomCommonAnhuiService.getAllData(messageLogin);
			taskMobile = iCrawlerLogin.getAllData(messageLogin);
			result.setData(taskMobile);
			return result;
	
		}
		
//		// 第3次爬取 wap
//		@RequestMapping(value = "/crawler", method = RequestMethod.POST)
//		public ResultData<TaskMobile> crawlerThree(@RequestBody MessageLogin messageLogin) throws Exception {
//			ResultData<TaskMobile> result = new ResultData<TaskMobile>();
//			boolean isCrawler = isDoing(messageLogin);
//			TaskMobile taskMobile = telecomCommonAnhuiService.findtaskMobile(messageLogin.getTask_id());
//			if (isCrawler) {
//				tracer.addTag("正在进行上次未完成的爬取任务。。。", messageLogin.toString());
//				result.setData(taskMobile);
//				return result;
//			} else {
//
//				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
//				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
//				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
//				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
//				tracer.addTag("parser.crawler.taskid", taskMobile.getTaskid());
//				tracer.addTag("parser.crawler.auth", messageLogin.getName());
//
//				telecomCommonAnhuiService.save(taskMobile);
//
//				telecomCommonAnhuiService.getThreeData(messageLogin, taskMobile);
//				result.setData(taskMobile);
//				return result;
//			}
//		}
		
//
//	    //第1次获取验证码
//		@RequestMapping(value = "/getphonecodeFirst", method = RequestMethod.POST)
//		public ResultData<TaskMobile> getphonecodeFirst(@RequestBody   MessageLogin messageLogin) {
//			tracer.addTag("parser.crawler.taskid 发送手机验证码",messageLogin.getTask_id());
//
//			tracer.addTag("parser.crawler.taskid",messageLogin.getTask_id());
//			tracer.addTag("parser.crawler.auth",messageLogin.getName());
//			TaskMobile taskMobile = telecomCommonAnhuiService.findtaskMobile(messageLogin.getTask_id());
//			
//			telecomCommonAnhuiService.getPhoneCodeFirst(messageLogin,taskMobile);
//			
//			ResultData<TaskMobile> result = new ResultData<TaskMobile>();
//			result.setData(taskMobile);
//			return result;
//		}
//		
//		
//   	 //net端第3次获取验证码  用于爬取详单的
//		@RequestMapping(value = "/getphonecodeThird", method = RequestMethod.POST)
//		public ResultData<TaskMobile> getphonecodeThird(@RequestBody   MessageLogin messageLogin) {
//			tracer.addTag("parser.crawler.taskid 发送手机验证码",messageLogin.getTask_id());
//
//			tracer.addTag("parser.crawler.taskid",messageLogin.getTask_id());
//			tracer.addTag("parser.crawler.auth",messageLogin.getName());
//			TaskMobile taskMobile = telecomCommonAnhuiService.findtaskMobile(messageLogin.getTask_id());
//			
//			telecomCommonAnhuiService.getphonecodeThird(messageLogin,taskMobile);
//			
//			ResultData<TaskMobile> result = new ResultData<TaskMobile>();
//			result.setData(taskMobile);
//			return result;
//		}
//	
		 //wap获取验证码
		@RequestMapping(value = "/getphonecode", method = RequestMethod.POST)
		public ResultData<TaskMobile> getphonecode(@RequestBody   MessageLogin messageLogin) {
			tracer.addTag("parser.crawler.taskid 发送手机验证码",messageLogin.getTask_id());
			tracer.addTag("parser.crawler.auth",messageLogin.getName());
			TaskMobile taskMobile = telecomCommonAnhuiService.findtaskMobile(messageLogin.getTask_id());
//			telecomCommonAnhuiService.sendSms(messageLogin);
			taskMobile = iSms.sendSms(messageLogin);
			ResultData<TaskMobile> result = new ResultData<TaskMobile>();
			result.setData(taskMobile);
			return result;
		}
		
		
		//验证验证码
		@RequestMapping(value = "/setphonecode", method = RequestMethod.POST)
		public ResultData<TaskMobile> setphonecode(@RequestBody   MessageLogin messageLogin) {
			tracer.addTag("parser.crawler.taskid 验证验证码",messageLogin.getTask_id());
			tracer.addTag("parser.crawler.auth",messageLogin.getName());
			TaskMobile taskMobile = telecomCommonAnhuiService.findtaskMobile(messageLogin.getTask_id());
//			telecomCommonAnhuiService.verifySms(messageLogin);
			taskMobile = iSms.verifySms(messageLogin);
			ResultData<TaskMobile> result = new ResultData<TaskMobile>();
			result.setData(taskMobile);
			return result;
		}
		
}
