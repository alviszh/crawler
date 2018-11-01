package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceGuiLinParser;
import app.service.aop.InsuranceCrawler;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic",
		"com.microservice.dao.entity.crawler.insurance.guilin" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic",
		"com.microservice.dao.repository.crawler.insurance.guilin" })
public class InsuranceGuiLinService implements InsuranceLogin,InsuranceCrawler{
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceGuiLinParser insuranceGuiLinParser;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private TracerLog tracer;
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		// TODO Auto-generated method stub
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			HtmlPage page = insuranceGuiLinParser.login(insuranceRequestParameters);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
//				System.out.println(html);
				tracer.addTag("登陆html", html);
				if (html.contains("全部功能")) {
					System.out.println("全部功能");
					insuranceService.changeLoginStatusSuccess(taskInsurance, page);
					
				} else if (html.contains("个人用户")) {
					insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
				} else {
					System.out.println("错误");
					insuranceService.changeLoginStatusException(taskInsurance);
				}

			} else {
				insuranceService.changeLoginStatusTimeOut(taskInsurance);
			} 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tracer.addTag("登陆异常",e.toString());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhasestatus());
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
		}
		return taskInsurance;
	}
	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		// TODO Auto-generated method stub
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
//		insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
		try {
			insuranceGuiLinParser.crawler(taskInsurance, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracer.addTag("登陆异常",e.toString());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhasestatus());
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
		}
		return taskInsurance;
	}
	
	
	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		
		return null;
	}
	
	
	
	
}
