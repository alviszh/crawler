package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;

import app.commontracerlog.TracerLog;

@Component
public class AsyncZhongshanGetAllDataService {
	
	@Autowired
	private InsuranceZhongshanService insuranceZhongshanService;
	
	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private InsuranceService insuranceService;
	
	
	/**
	 * @Des 爬取总方法
	 * @param insuranceRequestParameters
	 */
	@Async
	public void getAllData(InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("AsyncZhongshanGetAllDataService.crawler.getAllData", insuranceRequestParameters.getTaskId());
		
		tracer.addTag("parser.crawler.taskid",insuranceRequestParameters.getTaskId());
		
		
		try {
			//获取个人养老账户
			insuranceZhongshanService.getPensionaccount(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			//大病补充医疗保险缴费明细
			insuranceZhongshanService.getMedicalcare(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			//医疗账户消费明细
			insuranceZhongshanService.getPensionDetail(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
