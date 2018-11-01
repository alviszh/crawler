package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.microservice.unit.CommonUnit;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.ganzhou.InsuranceGanZhouUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.ganzhou.InsuranceGanZhouUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceGanZhouParser;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.jian"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.jian"})
public class InsuranceGanZhouMedicalCrawlerService implements InsuranceLogin{
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceGanZhouMedicalService insuranceGanZhouMedicalService;
	@Autowired
	private InsuranceGanZhouParser insuranceGanZhouParser;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceGanZhouUserInfoRepository insuranceGanZhouUserInfoRepository;
	@Autowired
	private TracerLog tracer;	
	
	/**
	 * @Des 登录 医疗
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceGanZhouMedicalCrawlerService.medical.login", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			WebParam webParam = insuranceGanZhouParser.login(insuranceRequestParameters);
			String html = webParam.getHtml();
			tracer.addTag("InsuranceGanZhouMedicalCrawlerService.medical.login",
					insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
			if (html.contains("赣州市医保网上申报平台")) {
				tracer.addTag(insuranceRequestParameters.getTaskId(),"登陆成功");
				String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
				taskInsurance.setCookies(cookies);
				taskInsuranceRepository.save(taskInsurance);
				taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance);
				return taskInsurance;
			}else if(html.contains("密码错误")){
				tracer.addTag(insuranceRequestParameters.getTaskId(),"用户名或者密码错误");
				taskInsurance = insuranceService.changeLoginStatusPwdError(taskInsurance);					
				return taskInsurance;
			}else{
				tracer.addTag("InsuranceGanZhouMedicalCrawlerService.medical.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");						
				taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
				return taskInsurance;
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("InsuranceGanZhouMedicalCrawlerService.medical.login.Exception", insuranceRequestParameters.getTaskId());
		}		
		return taskInsurance;
	}
	
	
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceGanZhouMedicalCrawlerService.medical.getAllData", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance.setShiyeStatus(201);
		taskInsurance.setShengyuStatus(201);
		taskInsurance.setGongshangStatus(201);
		taskInsurance.setYanglaoStatus(201);
		taskInsurance.setYiliaoStatus(201);
		taskInsuranceRepository.save(taskInsurance);
		insuranceGanZhouMedicalService.getUserInfo(insuranceRequestParameters);
		InsuranceGanZhouUserInfo userInfo = insuranceGanZhouUserInfoRepository.findTopByTaskid(insuranceRequestParameters.getTaskId());
		if (null != userInfo) {
			insuranceGanZhouMedicalService.getBasicinfo(insuranceRequestParameters, userInfo.getUseraccount());
		}
		taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		return taskInsurance;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
