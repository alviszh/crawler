package app.service.aop.impl;

import org.springframework.beans.factory.annotation.Autowired;
import com.crawler.car.insurance.bean.CarInsuranceRequestBean;
import com.microservice.dao.entity.crawler.car.insurance.TaskCarInsurance;
import com.microservice.dao.repository.crawler.car.insurance.TaskCarInsuranceRepository;
import app.commontracerlog.TracerLog;
import app.service.aop.CarInsuranceCrawler;

public class CarInsuranceCrawlerImpl implements CarInsuranceCrawler{
	
	@Autowired 
	private TracerLog tracer;
	
	@Autowired
	private TaskCarInsuranceRepository taskCarInsuranceRepository;

	@Override
	public TaskCarInsurance getAllData(CarInsuranceRequestBean carInsuranceRequestBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskCarInsurance getAllDataDone(String taskid) {
		tracer.addTag("AbstractCrawlerTracer getAllDataDone",taskid);
		TaskCarInsurance taskCarInsurance = taskCarInsuranceRepository.findByTaskid(taskid); 
		return taskCarInsurance;
	}

}
