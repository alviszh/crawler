package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceLiuZhouParser;
import app.service.aop.InsuranceCrawler;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic",
		"com.microservice.dao.entity.crawler.insurance.liuzhou" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic",
		"com.microservice.dao.repository.crawler.insurance.liuzhou" })
public class InsuranceLiuZhouService implements InsuranceLogin,InsuranceCrawler{
	@Autowired
	private InsuranceLiuZhouParser insuranceLiuZhouParser;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	protected TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private TracerLog tracer;
//	public static String html;
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		// TODO Auto-generated method stub
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		HtmlPage page = null;
		try {
			page = insuranceLiuZhouParser.login(insuranceRequestParameters);
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
			if(html.contains("个 人 参 保 基 本 信 息")){
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());

				String cookies = CommonUnit.transcookieToJson(page.getWebClient());
				taskInsurance.setCookies(cookies);
				taskInsurance.setWebdriverHandle(html);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
				
			}else if(html.contains("修 改 电 子 邮 箱")||html.contains("绑定手机修改")){
				taskInsurance.setDescription("请完善个人信息");
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_NOTFIND_ERROR.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_NOTFIND_ERROR.getPhasestatus());
//				taskInsurance.setFinished(true);
				taskInsuranceRepository.save(taskInsurance);
			}else{					
				insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
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
		String html = taskInsurance.getWebdriverHandle();
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
//		insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
		try {
			insuranceLiuZhouParser.crawler(taskInsurance,webClient,html);
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
