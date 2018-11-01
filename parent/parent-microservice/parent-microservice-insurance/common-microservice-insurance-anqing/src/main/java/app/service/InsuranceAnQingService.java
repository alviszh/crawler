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
import app.parser.InsuranceAnQingParser;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.anqing"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.anqing"})
public class InsuranceAnQingService implements InsuranceLogin{
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceAnQingParser insuranceAnQingParser;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceAnQingPersionService  insuranceAnQingPersionService;
	@Autowired
	private TracerLog tracer;	
	private static int errorCount=0;
	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Async
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceHouAnQingService.login", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		if (null != taskInsurance) {
			WebParam webParam = insuranceAnQingParser.login(insuranceRequestParameters);
			if (null == webParam) {
			    tracer.addTag("InsuranceHouAnQingService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
				taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
				return taskInsurance;
			} else {
				String html = webParam.getHtml();
				tracer.addTag("InsuranceHouAnQingService.login",
						insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
				if (html.contains("安庆市人力资源和社会保障局网上办事大厅")) {
					tracer.addTag(insuranceRequestParameters.getTaskId(),"登陆成功");
					taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance, webParam.getPage());
					return taskInsurance;
				}else if(html.contains("用户名密码错误")){
					tracer.addTag(insuranceRequestParameters.getTaskId(),"账号或者登陆密码错误");
					taskInsurance = insuranceService.changeLoginStatusPwdError(taskInsurance);					
					return taskInsurance;
				}else if(html.contains("验证码错误")){
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
					tracer.addTag("InsuranceHouAnQingService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");						
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
			insuranceAnQingPersionService.getUserInfo(insuranceRequestParameters);
			insuranceAnQingPersionService.getInsuranceInfo(insuranceRequestParameters);
			insuranceAnQingPersionService.getPaydetails(insuranceRequestParameters);
			
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
