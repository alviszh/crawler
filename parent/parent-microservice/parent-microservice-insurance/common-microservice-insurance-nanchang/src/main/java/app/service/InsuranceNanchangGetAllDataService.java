package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceCrawler;

@Component
public class InsuranceNanchangGetAllDataService implements InsuranceCrawler {

	@Autowired
	private InsuranceNanchangService insuranceNanchangService;

	@Autowired
	private TracerLog tracer;

	/**
	 * 爬取总方法
	 * 
	 * @param insuranceRequestParameters
	 */

	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("AsyncNanchangGetAllDataService.crawler.getAllData", insuranceRequestParameters.getTaskId());

		tracer.addTag("parser.crawler.taskid", insuranceRequestParameters.getTaskId());
		tracer.addTag("parser.crawler.auth", insuranceRequestParameters.getUsername());
		insuranceNanchangService.getList(insuranceRequestParameters);
		
		return null;

	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
