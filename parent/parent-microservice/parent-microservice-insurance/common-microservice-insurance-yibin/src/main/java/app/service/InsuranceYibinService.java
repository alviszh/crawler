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
import app.parser.InsuranceYibinParser;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.yibin"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.yibin"})
public class InsuranceYibinService implements InsuranceLogin{
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceYibinParser insuranceYibinParser;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired 
	private AsyncYibinPersionService asyncYibinPersionService;
	@Autowired 
	private  AsyncYibinMedicalService asyncYibinMedicalService;
	@Autowired
	private AsyncYibinUserInfoService asyncYibinUserInfoService;
	@Autowired
	private TracerLog tracer;	
	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceYibinService.login", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		if (null != taskInsurance) {
			WebParam webParam=null;
			try {
				webParam = insuranceYibinParser.login(insuranceRequestParameters);
			} catch (Exception e) {			
				e.printStackTrace();
				tracer.addTag("InsuranceYibinService.login.Exception", e.getMessage());
			}
			String alertMsg = webParam.getAlertMsg();
			String html=webParam.getHtml();
			tracer.addTag("InsuranceYibinService.login",
					insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
			if (alertMsg.contains("登录成功")) {
				tracer.addTag(insuranceRequestParameters.getTaskId(),"登陆成功");
				taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance, webParam.getPage());
				return taskInsurance;
			}else if(alertMsg.contains("请正确输入身份证号和密码") ){
				tracer.addTag(insuranceRequestParameters.getTaskId(),"登陆的身份证号有误");
				taskInsurance = insuranceService.changeLoginStatusPwdError(taskInsurance);					
				return taskInsurance;
			}else if(alertMsg.contains("请正确输入身份证号和密码") ){
				tracer.addTag(insuranceRequestParameters.getTaskId(),"登陆的身份证号,姓名有误或者密码有误");
				taskInsurance = insuranceService.changeLoginStatusPwdError(taskInsurance);					
				return taskInsurance;
			}else{
				tracer.addTag("InsuranceYibinService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");						
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
	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("getAllData", insuranceRequestParameters.getTaskId());
		try {
			asyncYibinUserInfoService.getUserInfo(insuranceRequestParameters);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		try {
			asyncYibinPersionService.getPersionInfo(insuranceRequestParameters);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			asyncYibinMedicalService.getMedicalInfo(insuranceRequestParameters);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		return taskInsurance;
	}
	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
