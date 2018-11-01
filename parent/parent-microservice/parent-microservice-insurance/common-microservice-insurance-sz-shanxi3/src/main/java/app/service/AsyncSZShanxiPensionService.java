package app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi3.InsuranceShanXi3Pension;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi3.InsuranceShanXi3PensionPerson;
import com.microservice.dao.repository.crawler.insurance.sz.shanxi3.InsuranceShanXi3PensionPersonRepository;
import com.microservice.dao.repository.crawler.insurance.sz.shanxi3.InsuranceShanXi3PensionRepository;

import app.bean.ResponseBean;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceSZShanxiParser;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sz.shanxi3"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sz.shanxi3"})
public class AsyncSZShanxiPensionService implements InsuranceLogin{
	
	@Autowired
	private TracerLog tracer; 
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceSZShanxiService insuranceSZShanxiService;
	@Autowired
	private InsuranceSZShanxiParser insuranceSZShanxiParser;
	@Autowired
	private InsuranceShanXi3PensionPersonRepository insuranceShanXi3PensionPersonRepository;
	@Autowired
	private InsuranceShanXi3PensionRepository insuranceShanXi3PensionRepository;
	@Autowired
	private AsyncSZShanxiMedicalService asyncSZShanxiMedicalService;
	
	private ResponseBean responseBean = null;
	
	
	/**
	 * 养老信息爬取
	 * @param insuranceRequestParameters
	 * @param taskInsurance
	 */
	@Async
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = insuranceService.findTaskInsurance(insuranceRequestParameters.getTaskId());
		tracer.addTag("parser.login.service.getPension",insuranceRequestParameters.getTaskId());
		
		//城镇职业养老保险
//		ResponseBean responseBean = insuranceSZShanxiService.uniteLogin("http://117.36.52.39/sxlssLogin.jsp",insuranceRequestParameters,taskInsurance);
		
//		if(null != responseBean && responseBean.getWebClient() != null){
//			insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase(),
//					InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus(),
//					InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription(),responseBean.getTaskInsurance());
//			
//			insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
			
			String personUrl = "http://117.36.52.39/jsp/personInfoQuery.jsp";
			HtmlPage personPage = (HtmlPage) insuranceSZShanxiService.getHtml(personUrl,responseBean.getWebClient());
			if(null != personPage){
				InsuranceShanXi3PensionPerson insuranceShanXi3PensionPerson = insuranceSZShanxiParser.parserPensionPerson(personPage.asXml());
				insuranceShanXi3PensionPerson.setTaskid(taskInsurance.getTaskid());
				insuranceShanXi3PensionPersonRepository.save(insuranceShanXi3PensionPerson);
				tracer.addTag("养老保险个人信息：", "爬取入库完成。");
			}
			String mingxiUrl = "http://117.36.52.39/personAccountQuery.do";
			HtmlPage mingxiPage = (HtmlPage) insuranceSZShanxiService.getHtml(mingxiUrl,responseBean.getWebClient());
			if(null != mingxiPage){
				List<InsuranceShanXi3Pension> insuranceShanXi3Pensions = insuranceSZShanxiParser.parserPension(mingxiPage.asXml(),taskInsurance.getTaskid());
				insuranceShanXi3PensionRepository.saveAll(insuranceShanXi3Pensions);
				tracer.addTag("养老保险缴纳明细：", "爬取入库完成。");
			}
			
			insuranceService.changeCrawlerStatus("【个人社保-养老保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
					200, responseBean.getTaskInsurance());
			//执行医疗保险爬取
			asyncSZShanxiMedicalService.getMedical(insuranceRequestParameters);
//		}else if(500 == responseBean.getCode()){
//			tracer.addTag("账户或者密码错误", "error");
//		}else{
//			tracer.addTag("发生未知错误。", "获取养老失败！");
//			insuranceService.changeCrawlerStatus("【个人社保-养老保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
//					500, responseBean.getTaskInsurance());
//			//执行医疗保险爬取
//			asyncSZShanxiMedicalService.getMedical(insuranceRequestParameters);
//		}
			return taskInsurance;
	}
	
	
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.addTag("insurance.SZ.shanxi.login",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = insuranceService.findTaskInsurance(insuranceRequestParameters.getTaskId());
		
		responseBean = insuranceSZShanxiService.uniteLogin("http://117.36.52.39/sxlssLogin.jsp",insuranceRequestParameters,taskInsurance);
		
		if(null != responseBean && responseBean.getWebClient() != null){
			insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase(),
					InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus(),
					InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription(),responseBean.getTaskInsurance());
		}else{
			insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase(),
					InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus(),
					InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getDescription(),responseBean.getTaskInsurance());
		}
		return taskInsurance;
		
	}


//	@Override
//	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
//		// TODO Auto-generated method stub
//		return null;
//	}


	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
