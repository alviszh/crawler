package app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.service.HousingfundtanshanService;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;
import app.service.common.aop.ICrawlerLogin;
import app.service.common.aop.ISms;
@RestController
@Configuration
@RequestMapping("/housing/tangshan") 
public class HousingfundTangShanController extends HousingBasicController{

	public static final Logger log = LoggerFactory.getLogger(HousingfundTangShanController.class);
	@Autowired
	private HousingfundtanshanService housingfundtanshanService;
	@Autowired
	private ICrawlerLogin iCrawler;
	@Autowired
	private ISms iSms;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResultData<TaskHousing> login(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("parser.login.taskid", messageLoginForHousing.getTask_id());
		tracer.addTag("parser.login.auth", messageLoginForHousing.getNum());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		TaskHousing taskHousing = iSms.sendSms(messageLoginForHousing);
		result.setData(taskHousing);
		return result;
		
	}
	
	@RequestMapping(value = "/setphonecode", method = RequestMethod.POST )
	public ResultData<TaskHousing> setcode(@RequestBody MessageLoginForHousing messageLogin){
		TaskHousing taskHousing = findTaskHousing(messageLogin.getTask_id());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		tracer.addTag("parser.crawler.taskid 验证手机验证码", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getNum());
		taskHousing = iSms.verifySms(messageLogin);		
		result.setData(taskHousing);
		return result;
	}
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST )
	public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLogin){
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		TaskHousing taskHousing = iCrawler.getAllData(messageLogin);
		result.setData(taskHousing);
		return result;
	} 
}
