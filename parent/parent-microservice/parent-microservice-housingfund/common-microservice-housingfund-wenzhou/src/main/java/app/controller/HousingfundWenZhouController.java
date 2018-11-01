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
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.service.HousingfundWenZhouService;

@RestController
@Configuration
@RequestMapping("/housing/wenzhou") 
public class HousingfundWenZhouController extends HousingBasicController{

	public static final Logger log = LoggerFactory.getLogger(HousingfundWenZhouController.class);
	@Autowired
	private HousingfundWenZhouService housingfundWenZhouService;

	@RequestMapping(value = "/getphonecode", method = RequestMethod.POST )
	public ResultData<TaskHousing> getcode(@RequestBody MessageLoginForHousing messageLogin){
		tracer.addTag("parser.crawler.taskid 发送手机验证码", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getNum());
		TaskHousing taskHousing = findTaskHousing(messageLogin.getTask_id());
		
		try {
			housingfundWenZhouService.getPhoneCode(messageLogin,taskHousing);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.getphonecode.auth", e.toString());
		}
		
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
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
		try {
			housingfundWenZhouService.setPhoneCode(messageLogin,taskHousing);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.setphonecode.auth", e.toString());
		}
		
		result.setData(taskHousing);
		return result;
	}
	@RequestMapping(value = "/crawler", method = RequestMethod.POST )
	public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLogin){
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getNum());
		TaskHousing taskHousing = findTaskHousing(messageLogin.getTask_id());
		System.out.println("------crawler----------");
		try {
			housingfundWenZhouService.getuserinfo(messageLogin,taskHousing);
		} catch (Exception e) {
			e.printStackTrace();
			
			tracer.addTag("parser.getphonecode.auth", e.toString());
		}
		
		try {
			housingfundWenZhouService.PayStatus(messageLogin,taskHousing);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.getphonecode.auth", e.toString());
		}
		
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		result.setData(taskHousing);
		return result;
	}
	
}
