package app.service.aop;

import com.crawler.taxation.json.TaxationRequestParameters;
import com.microservice.dao.entity.crawler.taxation.basic.TaskTaxation;

public interface ILogin extends ICrawler{

	/**
	 * 开始登陆
	 * */
	public TaskTaxation login(TaxationRequestParameters taxationRequestParameters);
}
