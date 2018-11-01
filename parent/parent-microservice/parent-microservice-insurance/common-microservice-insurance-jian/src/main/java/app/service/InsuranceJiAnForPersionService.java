package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceJiAnParser;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.jian"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.jian"})
public class InsuranceJiAnForPersionService implements InsuranceLogin{
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceJiAnParser insuranceJiAnParser;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private AsyncJiAnPersionService  asyncJiAnPersionService;
	@Autowired
	private TracerLog tracer;
	/**
	 * @Des 登录 养老
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceJiAnForPersionService.login", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		if (null != taskInsurance) {
			WebParam webParam=null;
			try {
				webParam = insuranceJiAnParser.loginForPersion(insuranceRequestParameters);
			} catch (Exception e) {				
				e.printStackTrace();
				tracer.addTag("InsuranceJiAnForPersionService.login", e.getMessage());
			}	
			String html = webParam.getHtml();
			tracer.addTag("InsuranceJiAnForPersionService.login",
					insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
			if (html.contains("个人网上查询系统")) {
				tracer.addTag(insuranceRequestParameters.getTaskId(),"登陆成功");
				taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance, webParam.getPage());
				return taskInsurance;
			}else if(html.contains("身份证、密码错误")){
				tracer.addTag(insuranceRequestParameters.getTaskId(),"身份证、密码错误! 或还未注册！");
				taskInsurance = insuranceService.changeLoginStatusPwdError(taskInsurance);					
				return taskInsurance;
			}else{
				tracer.addTag("InsuranceJiAnForPersionService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");						
				taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
				return taskInsurance;
			}
		}
		return taskInsurance;
	}
	/**
	 * @Des 爬取总方法
	 * @param insuranceRequestParameters
	 * @throws Exception 
	 */
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("getAllData", insuranceRequestParameters.getTaskId());	
		TaskInsurance taskInsurance = null;
		try {
			asyncJiAnPersionService.getUserInfo(insuranceRequestParameters);
			asyncJiAnPersionService.getPersionInfo(insuranceRequestParameters);
			taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());	
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return taskInsurance;
	}
	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
