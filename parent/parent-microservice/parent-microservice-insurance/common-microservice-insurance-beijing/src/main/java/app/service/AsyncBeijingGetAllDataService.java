package app.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceCrawler;

@Component
public class AsyncBeijingGetAllDataService{
	
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceBeijingService insuranceBeijingService;
	@Autowired
	private AsyncBeijingPensionService asyncBeijingPensionService;
	@Autowired
	private AsyncBeijingUnemploymentService asyncBeijingUnemploymentService;
	@Autowired
	private AsyncBeijingInjuryService asyncBeijingInjuryService;
	@Autowired
	private AsyncBeijingMaternityService asyncBeijingMaternityService;
	@Autowired
	private AsyncBeijingMedicalService asyncBeijingMedicalService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	
	/**
	 * @Des 爬取总方法
	 * @param insuranceRequestParameters
	 */
//	@Async
//	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters){
//		tracer.addTag("parser.crawler.getAllData", insuranceRequestParameters.getTaskId());
//		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//		try {
//			insuranceBeijingService.getUserInfo(insuranceRequestParameters);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		Calendar calendar = Calendar.getInstance();
//		for(int i=0;i<10;i++){
//			String searchYear = String.valueOf(calendar.get(Calendar.YEAR)-i);
//			asyncBeijingPensionService.getPensionMsg(insuranceRequestParameters,searchYear);
//		}
//		
//		for(int i=0;i<10;i++){
//			String searchYear = String.valueOf(calendar.get(Calendar.YEAR)-i);
//			asyncBeijingUnemploymentService.getUnemployment(insuranceRequestParameters,searchYear);
//		}
//		
//		for(int i=0;i<10;i++){
//			String searchYear = String.valueOf(calendar.get(Calendar.YEAR)-i);
//			asyncBeijingInjuryService.getInjury(insuranceRequestParameters,searchYear);
//		}
//		
//		for(int i=0;i<10;i++){
//			String searchYear = String.valueOf(calendar.get(Calendar.YEAR)-i);
//			asyncBeijingMaternityService.getBear(insuranceRequestParameters,searchYear);
//		}
//		
//		for(int i=0;i<10;i++){
//			String searchYear = String.valueOf(calendar.get(Calendar.YEAR)-i);
//			asyncBeijingMedicalService.getMedical(insuranceRequestParameters,searchYear);
//		}
////		taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//		return taskInsurance;
//		
//	}



}
