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
import app.parser.InsuranceWuZhouParser;
import app.service.aop.InsuranceCrawler;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic",
		"com.microservice.dao.entity.crawler.insurance.wuzhou" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic",
		"com.microservice.dao.repository.crawler.insurance.wuzhou" })
public class InsuranceWuZhouService implements InsuranceLogin,InsuranceCrawler{
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceWuZhouParser insuranceWuZhouParser;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private TracerLog tracer;
	
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		// TODO Auto-generated method stub
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		HtmlPage page = null;
		try {
			page = insuranceWuZhouParser.login(insuranceRequestParameters);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracer.addTag("登陆异常",e.toString());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhasestatus());
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
		}
		if(null != page){
			String html = page.getWebResponse().getContentAsString();
			tracer.addTag("登陆html", html);
			System.out.println(html);
			if(html.contains("全部功能")){
				insuranceService.changeLoginStatusSuccess(taskInsurance,page);
				
			}else if(html.contains("资料下载")){
				insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
			}else{					
				insuranceService.changeLoginStatusException(taskInsurance);	
			}
			
		}else{
			insuranceService.changeLoginStatusTimeOut(taskInsurance);	
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
			insuranceWuZhouParser.crawler(taskInsurance,webClient);
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

