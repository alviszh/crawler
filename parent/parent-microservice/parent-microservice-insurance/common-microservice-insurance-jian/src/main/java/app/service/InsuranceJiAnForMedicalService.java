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
public class InsuranceJiAnForMedicalService implements InsuranceLogin{
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceJiAnParser insuranceJiAnParser;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private AsyncJiAnMedicalService  asyncJiAnMedicalService;
	@Autowired
	private TracerLog tracer;	
	private static int errorCount=0;	
	/**
	 * @Des 登录 医疗
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("InsuranceJiAnForMedicalService.login", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		if (null != taskInsurance) {
			WebParam webParam=null;
			try {
				webParam = insuranceJiAnParser.loginForMedical(insuranceRequestParameters);
			} catch (Exception e) {			
				e.printStackTrace();
				tracer.addTag("InsuranceJiAnForMedicalService.login", e.getMessage());
			}
			String html = webParam.getHtml();
			tracer.addTagWrap("InsuranceJiAnForMedicalService.login",
					insuranceRequestParameters.getTaskId() + html);
			if (html.contains("厦门中软社会保险信息管理系统")) {
				tracer.addTag(insuranceRequestParameters.getTaskId(), "登陆成功");
				taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance, webParam.getPage());
				return taskInsurance;
			} else if (html.contains("您输入的用户名不存在或者密码")) {
				tracer.addTag(insuranceRequestParameters.getTaskId(), "您输入的用户名不存在或者用户名，密码有误");
				taskInsurance = insuranceService.changeLoginStatusPwdError(taskInsurance);
				return taskInsurance;
			} else if (html.contains("验证码不正确")) {
				errorCount++;
				tracer.addTag(insuranceRequestParameters.getTaskId() + "超级鹰对登录验证码解析错误的次数为：", errorCount + "次");
				if (errorCount <= 2) {
					tracer.addTag("parser.login", "检验码失败" + errorCount + "次，重新执行登录方法");
					login(insuranceRequestParameters);
				} else {
					errorCount = 0;
					taskInsurance = insuranceService.changeLoginStatusCaptError(taskInsurance);
					return taskInsurance;
				}
			} else {
				tracer.addTag("InsuranceJiAnForMedicalService.login",
						insuranceRequestParameters.getTaskId() + "登录页获取超时！");
				taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
				return taskInsurance;
			}
		}
		return taskInsurance;
	}
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("InsuranceJiAnForMedicalService.getAllData", insuranceRequestParameters.getTaskId());		
		TaskInsurance taskInsurance=taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			taskInsurance = asyncJiAnMedicalService.getMedicalInfo(insuranceRequestParameters);
		} catch (Exception e) {	
			e.printStackTrace();
			tracer.addTag("InsuranceJiAnForMedicalService.getAllData.Exception", e.getMessage());	
		}
		return taskInsurance;
	}
	
	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
