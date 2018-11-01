package app.service.aop;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

public interface InsuranceLogin extends InsuranceCrawler{

	/**
	 * 开始登陆
	 * */
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters);
	
}
