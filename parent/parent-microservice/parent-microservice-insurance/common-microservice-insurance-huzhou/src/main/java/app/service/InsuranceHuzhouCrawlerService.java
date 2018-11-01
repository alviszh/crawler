package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.htmlunit.InsuranceHuzhouHtmlunit;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.huzhou"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.huzhou"})
public class InsuranceHuzhouCrawlerService implements InsuranceLogin{
	
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceHuzhouHtmlunit insuranceHuzhouHtmlunit;
	@Autowired
	private InsuranceHuzhouService insuranceHuzhouService;
	@Autowired
	private InsuranceHuzhouGetAllDataService insuranceHuzhouGetAllDataService;
	@Autowired
	private InsuranceService insuranceService;	
	@Autowired 
	private TracerLog tracer;			
	private static int errorCount=0;
	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceHuzhouService.login", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		if (null != taskInsurance) {
			WebParam webParam=null;
			try {
				webParam = insuranceHuzhouHtmlunit.login(insuranceRequestParameters);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				tracer.addTag("InsuranceHuzhouService.login.Exception e", e.getMessage());
			}
			String html = webParam.getHtml();
			tracer.addTagWrap("InsuranceHuzhouService.login",
					insuranceRequestParameters.getTaskId() + html);
			String errMsg=webParam.getErrMsg();
			if (errMsg.contains("登录成功")) {
				tracer.addTag(insuranceRequestParameters.getTaskId(), "登陆成功");
				String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
				taskInsurance = changeLoginStatusSuccess(taskInsurance, cookies);
				return taskInsurance;
			}else if(errMsg.contains("您输入的身份证和姓名不存在")) {
				tracer.addTag(insuranceRequestParameters.getTaskId(), "账号或者登陆密码错误");
				taskInsurance = insuranceService.changeLoginStatusIdnumError(taskInsurance);
				return taskInsurance;
			} else if (errMsg.contains("请输入身份证") || errMsg.contains("请输入姓名")
					|| errMsg.contains("请输入密码") || errMsg.contains("请输入验证码")) {
				tracer.addTag(insuranceRequestParameters.getTaskId(), "输入框不能为空");
				taskInsurance = insuranceService.changeLoginStatusPwdError(taskInsurance);
				return taskInsurance;
			} else if (errMsg.contains("验证码不正确")) {
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
			}else {
				tracer.addTag("InsuranceWeiFangService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
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
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			insuranceHuzhouService.getUserInfo(insuranceRequestParameters);
			insuranceHuzhouGetAllDataService.getAllData(insuranceRequestParameters);
			insuranceHuzhouService.getBasicinfo(insuranceRequestParameters);
		} catch (Exception e) {		
			e.printStackTrace();
		}
		taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		return taskInsurance;
	}
	
	  private TaskInsurance changeLoginStatusSuccess(TaskInsurance taskInsurance, String cookie) {
	        taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
	        taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
	        taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());	
	        taskInsurance.setCookies(cookie);	
	        taskInsurance = taskInsuranceRepository.save(taskInsurance);
	        return taskInsurance;
	    }

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
	
		return null;
	}
	
}
