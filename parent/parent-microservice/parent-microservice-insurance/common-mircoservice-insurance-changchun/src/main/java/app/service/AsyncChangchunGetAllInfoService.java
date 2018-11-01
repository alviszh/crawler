package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;

@Component
public class AsyncChangchunGetAllInfoService {
	
	
	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private InsuranceChangchunService insuranceChangchunService;
	
	
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	
	
	@Async
	public void getAllInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		
		tracer.addTag("AsyncChangchunGetAllInfoService.crawler.getAllInfo", insuranceRequestParameters.getTaskId());
		
		tracer.addTag("parser.crawler.taskid",insuranceRequestParameters.getTaskId());
		tracer.addTag("parser.crawler.auth",insuranceRequestParameters.getUsername());
		insuranceService.changeCrawlerStatus("数据采集中，【医疗保险信息】已采集完成,无数据", "CRAWLER_YILIAO_MSG", 201, taskInsurance);
		insuranceService.changeCrawlerStatus("数据采集中，【生育保险信息】已采集完成,无数据", "CRAWLER_SHENGYU_MSG", 201, taskInsurance);
		insuranceService.changeCrawlerStatus("数据采集中，【工伤保险信息】已采集完成,无数据", "CRAWLER_GONGSHANG_MSG", 201, taskInsurance);
		try {
			//获取个人账户信息
			insuranceChangchunService.getAccountInfo(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		try {
//			//获取失业保险信息
////			insuranceChangchunService.getEndowmentInfo(insuranceRequestParameters);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		try {
//			//获取养老保险信息
//			insuranceChangchunService.getUnemploymentInfo(insuranceRequestParameters);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		try {
//			//获取个人信息
//			insuranceChangchunService.getUserInfo(insuranceRequestParameters);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	
}

}
