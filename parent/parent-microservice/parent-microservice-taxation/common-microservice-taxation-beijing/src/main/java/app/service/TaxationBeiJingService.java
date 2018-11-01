package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.taxation.json.TaxationRequestParameters;
import com.microservice.dao.entity.crawler.taxation.basic.TaskTaxation;
import com.microservice.dao.repository.crawler.taxation.basic.TaskTaxationRepository;

import app.service.aop.ILogin;
@Component
@Service
@EnableAsync
@EntityScan(basePackages = {"com.microservice.dao.entity.crawler.taxation.beijing","com.microservice.dao.entity.crawler.taxation.basic"})
@EnableJpaRepositories(basePackages = {"com.microservice.dao.repository.crawler.taxation.beijing","com.microservice.dao.repository.crawler.taxation.basic"} )
public class TaxationBeiJingService implements ILogin{

	@Autowired
	private TaxationBeiJingCommonService taxationBeiJingCommonService;
	@Autowired
	private TaskTaxationRepository taskTaxationRepository;
	
	@Async
	@Override
	public TaskTaxation login(TaxationRequestParameters taxationRequestParameters) {
		TaskTaxation taskTaxation = taskTaxationRepository.findByTaskid(taxationRequestParameters.getTaskId());
		taxationBeiJingCommonService.login(taxationRequestParameters,taskTaxation);
		return taskTaxation;
	}

	@Async
	@Override
	public TaskTaxation getAllData(TaxationRequestParameters taxationRequestParameters) {
		TaskTaxation taskTaxation = taskTaxationRepository.findByTaskid(taxationRequestParameters.getTaskId());
		taxationBeiJingCommonService.crawlerUserInfo(taxationRequestParameters,taskTaxation);
		taxationBeiJingCommonService.crawlerAccount(taxationRequestParameters,taskTaxation);
		return taskTaxation;
	}

	@Override
	public TaskTaxation getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
