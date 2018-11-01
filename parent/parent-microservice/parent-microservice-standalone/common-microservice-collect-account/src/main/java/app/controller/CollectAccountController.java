package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.microservice.dao.entity.crawler.collect.account.InsuranceHousingCollectAccount;
import app.service.CollectAccountService;

@RestController
@Configuration
public class CollectAccountController {
	
	@Autowired
	private Tracer tracer;
	@Autowired
	private CollectAccountService collectAccountService;

	
	@PostMapping(value="/collect/account")
	public String collect(@RequestBody InsuranceHousingCollectAccount insuranceHousingCollectAccount){
		
		tracer.addTag("CollectAccountController.collect", insuranceHousingCollectAccount.getTaskid());
		
		collectAccountService.collect(insuranceHousingCollectAccount);
		return "SUCCESS";
	}
}
