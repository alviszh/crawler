package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import com.microservice.dao.entity.crawler.collect.account.InsuranceHousingCollectAccount;
import com.microservice.dao.repository.crawler.collect.account.InsuranceHousingCollectAccountRepository;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.collect.account"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.collect.account"})
public class CollectAccountService {
	
	@Autowired
	private Tracer tracer;
	@Autowired
	private InsuranceHousingCollectAccountRepository insuranceHousingCollectAccountRepository;
	
	public void collect(InsuranceHousingCollectAccount insuranceHousingCollectAccount) {
		
		tracer.addTag("登录信息： ", insuranceHousingCollectAccount.toString());
		insuranceHousingCollectAccountRepository.save(insuranceHousingCollectAccount);
			
	}

}
