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
import app.parser.InsuranceWeiFangParser;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.weifang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.weifang"})
public class InsuranceWeiFangCrawleService implements InsuranceLogin{
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceWeiFangParser insuranceWeiFangParser;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceWeiFangService insuranceWeiFangService;
	@Autowired 
	private AsyncWeiFangPersionService asyncWeiFangPersionService;
	@Autowired 
	private  AsyncWeiFangMedicalService asyncWeiFangMedicalService;
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
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceWeiFangService.login", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		if (null != taskInsurance) {
			WebParam webParam = null;
			try {
				webParam = insuranceWeiFangParser.login(insuranceRequestParameters);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (null == webParam) {
			    tracer.addTag("InsuranceWeiFangService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
				taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
				return taskInsurance;
			} else {
				String html = webParam.getHtml();
				tracer.addTag("InsuranceWeiFangService.login",
						insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
				if (html.contains("个人办事")) {
					tracer.addTag(insuranceRequestParameters.getTaskId(),"登陆成功");
					taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance, webParam.getPage());
					return taskInsurance;
				}else if(html.contains("用户名或密码错误")){
					tracer.addTag(insuranceRequestParameters.getTaskId(),"账号或者登陆密码错误");
					taskInsurance = insuranceService.changeLoginStatusPwdError(taskInsurance);					
					return taskInsurance;
				}else if(html.contains("验证码不正确")){
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
					tracer.addTag("InsuranceWeiFangService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");						
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
			taskInsurance.setShiyeStatus(201);
			taskInsurance.setShengyuStatus(201);
			taskInsurance.setGongshangStatus(201);
			taskInsuranceRepository.save(taskInsurance);
			insuranceWeiFangService.getUserInfo(insuranceRequestParameters);	
			asyncWeiFangPersionService.getPersionInfo(insuranceRequestParameters);
			asyncWeiFangMedicalService.getMedicalInfo(insuranceRequestParameters);
		} catch (Exception e) {			
			tracer.addTag("parser.crawler.getAllData.Exception", e.getMessage());
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
