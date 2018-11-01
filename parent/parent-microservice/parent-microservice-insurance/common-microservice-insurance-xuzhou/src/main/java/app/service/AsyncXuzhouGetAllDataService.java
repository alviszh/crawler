package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;

@Component
public class AsyncXuzhouGetAllDataService {
	
	@Autowired
	private InsuranceXuzhouService insuranceXuzhouService;
	
	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private InsuranceService insuranceService;
	
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	/**
	 * @Des 爬取总方法
	 * @param insuranceRequestParameters
	 */
	@Async
	public void getAllData(InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("AsyncXuzhouGetAllDataService.crawler.getAllData", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceService.changeCrawlerStatus("数据采集中，【工伤保险信息】,无数据", "CRAWLER_GONGSHANG_MSG", 201, taskInsurance);
		insuranceService.changeCrawlerStatus("数据采集中，【生育保险信息】,无数据", "CRAWLER_SHENGYU_MSG", 201, taskInsurance);
		insuranceService.changeCrawlerStatus("数据采集中，【失业保险信息】,无数据", "CRAWLER_SHIYE_MSG", 201, taskInsurance);
		insuranceService.changeCrawlerStatus("数据采集中，【用户信息】,无数据", "CRAWLER_USER_MSG", 201, taskInsurance);
		try {
			//获取养老保险信息
			insuranceXuzhouService.getEndowmentAccount(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			//获取医疗保险信息
			insuranceXuzhouService.getMedicalAccount(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


}
