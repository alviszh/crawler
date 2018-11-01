package app.service;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceCrawler;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.jiangmen"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.jiangmen"})
public class InsuranceJiangMenFutureService implements InsuranceLogin,InsuranceCrawler{
	public static final Logger log = LoggerFactory.getLogger(InsuranceJiangMenFutureService.class);
	@Autowired
	private LoginAndGetService loginAndGetService;
	@Autowired
	private InsuranceJiangMenService insuranceJiangMenService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	protected TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private TracerLog tracer;
//	public static String html = null;
	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception 
	 */
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		// TODO Auto-generated method stub
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		String url = "http://sbj.jiangmen.gov.cn/WsSever.aspx?mode=log";
		Page htmlpage = null;
		try {
			htmlpage = loginAndGetService.loginByIDNUM(webClient, url, insuranceRequestParameters.getUserIDNum().trim(), insuranceRequestParameters.getPassword().trim(), insuranceRequestParameters.getMessage().trim());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracer.addTag("登陆异常",e.toString());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhasestatus());
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
			
		}
		if(htmlpage == null){
			tracer.addTag("登陆html", null);
		}else{
			tracer.addTag("登陆html", htmlpage.getWebResponse().getContentAsString());
			if(htmlpage.getWebResponse().getContentAsString().contains("个人基本信息")){
				//String html = htmlpage.getWebResponse().getContentAsString();
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());

				String cookies = CommonUnit.transcookieToJson(webClient);
				taskInsurance.setCookies(cookies);
				//taskInsurance.setWebdriverHandle(html);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
				
				
				
//				return taskHousing;
			}else{
				taskInsurance = insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
//				return taskHousing;
			}
		}
		
		
		return taskInsurance;
	}
	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		// TODO Auto-generated method stub
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//		String html = taskInsurance.getWebdriverHandle();
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		try {
			Future<String> future = insuranceJiangMenService.getResult(insuranceRequestParameters, taskInsurance,
					webClient);
		} catch (Exception e) {
			// TODO: handle exception
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
