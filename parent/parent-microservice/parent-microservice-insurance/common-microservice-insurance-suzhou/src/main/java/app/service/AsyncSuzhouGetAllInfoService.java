package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;

@Component
public class AsyncSuzhouGetAllInfoService {
	
	
	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private InsuranceSuzhouService insuranceSuzhouService;
	
	
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	
	@Async
	public void getAllInfo(InsuranceRequestParameters insuranceRequestParameters) {
		
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		
		tracer.addTag("AsyncSuzhouGetAllInfoService.crawler.getAllInfo", insuranceRequestParameters.getTaskId());
		
		
		
		try {
			//获取企业养老信息
			insuranceSuzhouService.getEnterprisePensionInfo(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			//获取城镇职工医保信息
			insuranceSuzhouService.getTownsWorkersInfo(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			insuranceSuzhouService.getWorkInjuryInfo(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			insuranceSuzhouService.getUnmploymentInfo(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			insuranceSuzhouService.getBirthInfo(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try{
			insuranceSuzhouService.getUserInfo(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
