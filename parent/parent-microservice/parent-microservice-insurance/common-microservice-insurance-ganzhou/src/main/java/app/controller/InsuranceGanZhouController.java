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
import app.service.InsuranceGanZhouMedicalCrawlerService;
import app.service.InsuranceGanZhouService;

@RestController
@Configuration
@RequestMapping("/insurance/ganzhou") 
public class InsuranceGanZhouController {
public static final Logger log = LoggerFactory.getLogger(InsuranceGanZhouController.class);
	
	@Autowired
	private InsuranceGanZhouService insuranceGanZhouService;
	@Autowired
	private InsuranceGanZhouMedicalCrawlerService insuranceGanZhouMedicalCrawlerService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private TracerLog tracer;
	/**
	 * 医保登录
	 * @param insuranceRequestParameters
	 * @return
	 */
	@PostMapping(value="/loginForMedical")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){		
	    tracer.addTag("InsuranceGanZhouController.medical.login",insuranceRequestParameters.getTaskId());		
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());	
		try {
			taskInsurance=insuranceGanZhouMedicalCrawlerService.login(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskInsurance;
	}	
	
	@PostMapping(value="/crawlerForMedical")
	public TaskInsurance crawlerForMedical(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("InsuranceGanZhouController.medical.crawler", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance = insuranceGanZhouMedicalCrawlerService.getAllData(insuranceRequestParameters);
		return taskInsurance;
	}
	
	@PostMapping(value="/crawlerForYanglao")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{		
		tracer.addTag("InsuranceGanZhouController.crawlerForYanglao.crawler", insuranceRequestParameters.getTaskId());
	    TaskInsurance  taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());	    
		taskInsurance.setUserInfoStatus(201);
		taskInsurance.setShiyeStatus(201);
		taskInsurance.setShengyuStatus(201);
		taskInsurance.setGongshangStatus(201);
		taskInsurance.setYiliaoStatus(201);
		taskInsuranceRepository.save(taskInsurance);
		taskInsurance=insuranceGanZhouService.getAllData(insuranceRequestParameters);	
		return taskInsurance;	
	}
}
