package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.ResultData;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.commontracerlog.TracerLog;
import app.service.common.aop.ICrawlerLogin;
import app.service.common.aop.ISms;
@RestController
@Configuration
@RequestMapping("/housing/leshan") 
public class HousingfundLeShanController extends HousingBasicController{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private ISms iSms;
	@Autowired
	private ICrawlerLogin iCrawlerLogin;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResultData<TaskHousing> telecom(@RequestBody MessageLoginForHousing messageLoginForHousing) {
//		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		TaskHousing taskHousing = iCrawlerLogin.login(messageLoginForHousing);
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		result.setData(taskHousing);
		return result;
		
		
	}
	
	@RequestMapping(value = "/setphonecode", method = RequestMethod.POST )
	public ResultData<TaskHousing> sendSms(@RequestBody MessageLoginForHousing messageLogin){
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		tracer.addTag("parser.crawler.taskid 验证手机验证码", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getNum());
		tracer.addTag("parser.login.sendSMS", messageLogin.getTask_id());
		tracer.addTag("parser.login.sendSMS.auth", messageLogin.getNum());
		TaskHousing taskHousing = iSms.verifySms(messageLogin);
		result.setData(taskHousing);
		return result;
	}
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST )
	public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLogin){
//		TaskHousing taskHousing = findTaskHousing(messageLogin.getTask_id());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		TaskHousing taskHousing = iCrawlerLogin.getAllData(messageLogin);
		result.setData(taskHousing);
		return result;
	} 
}
