package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceHuangShiParser;
import app.service.aop.InsuranceCrawler;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.huangshi"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.huangshi"})
public class InsuranceHuangShiService implements InsuranceLogin, InsuranceCrawler{

	
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceHuangShiParser insuranceHuangShiParser;
	@Autowired
	private AsyncGetDataService asyncGetDataService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	
	@Override
	@Async
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("crawler.login.service.begin.taskid", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			WebParam webParam = insuranceHuangShiParser.login(insuranceRequestParameters);
			if(null != webParam.getHtmlPage()){
				tracer.addTag("crawler.login.service.success", "登陆成功！");
				insuranceService.changeLoginStatusSuccess(taskInsurance, webParam.getHtmlPage());
			}else{
				tracer.addTag("crawler.login.service.fail", webParam.getHtml());
				insuranceService.changeLoginStatus("LOGIN", "ERROR", webParam.getHtml(), taskInsurance);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.login.service.Exception", e.toString());
			insuranceService.changeLoginStatusException(taskInsurance);
		}
		taskInsurance = insuranceService.findTaskInsurance(taskInsurance.getTaskid());
		return taskInsurance;		
	}
	
	@Override
	@Async
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("crawler.service.begin.taskid", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		
		asyncGetDataService.getUserInfo(taskInsurance);
		asyncGetDataService.getPensionInfo(taskInsurance);
		asyncGetDataService.getMedicalInfo(taskInsurance);
		asyncGetDataService.getBearInfo(taskInsurance);
		asyncGetDataService.getInjuryInfo(taskInsurance);
		asyncGetDataService.getUnemploymentInfo(taskInsurance);
		
		return taskInsurance;
	}
	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
