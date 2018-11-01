package app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.InsuranceService;
import app.service.InsuranceYangZhouService;

@RestController
@Configuration
@RequestMapping("/insurance/yangzhou") 
public class InsuranceYangZhouController {
public static final Logger log = LoggerFactory.getLogger(InsuranceYangZhouController.class);	
	@Autowired
	private InsuranceYangZhouService insuranceYangZhouService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TaskInsuranceRepository  taskInsuranceRepository;
	@Autowired
	private TracerLog tracer;
	/**
	 * 登录
	 * @param insuranceRequestParameters
	 * @return
	 */
	@PostMapping(value="/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){		
	    tracer.addTag("InsuranceYangZhouController.login",insuranceRequestParameters.getTaskId());	
	    TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);	
		try {
			taskInsurance=insuranceYangZhouService.login(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskInsurance;
	}	
	@PostMapping(value="/crawler")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{	
		tracer.addTag("InsuranceYangZhouController.crawler", insuranceRequestParameters.getTaskId());
		boolean isCrawler = insuranceService.isDoing(insuranceRequestParameters);
		TaskInsurance taskInsurance = null;
		if(isCrawler){
			tracer.addTag("正在进行上次未完成的爬取任务。。。",insuranceRequestParameters.toString());
		}else{
			taskInsurance = insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);		
			taskInsurance.setShiyeStatus(201);
			taskInsurance.setShengyuStatus(201);
			taskInsurance.setGongshangStatus(201);
			taskInsurance.setYanglaoStatus(201);
			taskInsuranceRepository.save(taskInsurance);
			insuranceYangZhouService.getUserInfo(insuranceRequestParameters);
			insuranceYangZhouService.getMedicalList(insuranceRequestParameters);
	   }
		return taskInsurance;
		
	}
}
