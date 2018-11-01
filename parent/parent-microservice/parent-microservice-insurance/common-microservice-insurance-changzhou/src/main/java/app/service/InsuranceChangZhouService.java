package app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;


@Component
@Service
@EnableAsync
public class InsuranceChangZhouService extends InsuranceService {

	public static final Logger log = LoggerFactory.getLogger(InsuranceChangZhouService.class);

	@Autowired
	private InsuranceChangZhouCrawlerService insuranceChangZhouCrawlerService;

	@Async
	public TaskInsurance crawler(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = insuranceChangZhouCrawlerService.login(insuranceRequestParameters);

		if (taskInsurance.getPhase().indexOf(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase()) != -1
				&& taskInsurance.getPhase_status()
						.indexOf(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus()) != -1) {
			insuranceChangZhouCrawlerService.getAllData(insuranceRequestParameters);
		}

		return taskInsurance;
	}
}