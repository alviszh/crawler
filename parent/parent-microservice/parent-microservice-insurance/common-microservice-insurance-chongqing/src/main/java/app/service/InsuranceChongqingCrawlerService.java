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
import app.parser.InsuranceChongqingParser;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.chongqing"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.chongqing"})
public class InsuranceChongqingCrawlerService implements InsuranceLogin{
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceChongqingParser insuranceChongqingParser;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceChongqingService insuranceChongqingService;
	@Autowired 
	private AsyncChongqingLostworkService  asyncChongqingLostworkService;
	@Autowired 
	private  AsyncChongqingBirthService asyncChongqingBirthService;
	@Autowired 
	private  AsyncChongqingInjuryService asyncChongqingInjuryService;
	@Autowired
	private AsyncChongqingPersionService asyncChongqingPersionService;
	@Autowired
	private AsyncChongqingMedicalService  asyncChongqingMedicalService;
	
	private static int errorCount=0;
	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 */
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceChongqingCrawlerService.login", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		if (null != taskInsurance) {
			WebParam webParam = null;
			try {
				webParam = insuranceChongqingParser.login(insuranceRequestParameters);
			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("InsuranceChongqingCrawlerService.login", e.getMessage());
			}
			if (null == webParam) {
			    tracer.addTag("InsuranceChongqingCrawlerService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
				taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
				return taskInsurance;
			} else {
				String html = webParam.getPage().getWebResponse().getContentAsString();
				tracer.addTag("InsuranceChongqingCrawlerService.login",
						insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
				Integer code=webParam.getCode();
				if (1001 == code) {
					tracer.addTag(insuranceRequestParameters.getTaskId(),"登陆成功");
					taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance, webParam.getPage());
					return taskInsurance;
				}else if(1002 == code){
					tracer.addTag(insuranceRequestParameters.getTaskId(),"账号或者登陆密码错误");
					taskInsurance = insuranceService.changeLoginStatusPwdError(taskInsurance);					
					return taskInsurance;
				}else if(1003 == code){
					errorCount++;
					tracer.addTag(insuranceRequestParameters.getTaskId()+"超级鹰对登录验证码解析错误的次数为：",errorCount+"次");
					if (errorCount<=2){
						tracer.addTag("parser.login","检验码失败"+errorCount+"次，重新执行登录方法");
						login(insuranceRequestParameters);
					} else {
						errorCount=0;
						taskInsurance = insuranceService.changeLoginStatusCaptError(taskInsurance);					
						return taskInsurance;
					}		 		
				}else{
					tracer.addTag("InsuranceChongqingService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");						
					taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
					return taskInsurance;
				}
			}
		}
		return taskInsurance;
	}
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("parser.crawler.getAllData", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			insuranceChongqingService.getUserInfo(insuranceRequestParameters);
			asyncChongqingBirthService.getBirthInfo(insuranceRequestParameters);	
			asyncChongqingInjuryService.getInjuryInfo(insuranceRequestParameters);
			asyncChongqingLostworkService.getLostworkInfo(insuranceRequestParameters);
			asyncChongqingPersionService.getPersionInfo(insuranceRequestParameters);
			asyncChongqingMedicalService.getMedicalInfo(insuranceRequestParameters);	
		} catch (Exception e) {			
			e.printStackTrace();
			tracer.addTag("parser.crawler.getAllData.Exception", e.getMessage());
		}
		taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		return taskInsurance;
	}
	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		
		return null;
	}

}
