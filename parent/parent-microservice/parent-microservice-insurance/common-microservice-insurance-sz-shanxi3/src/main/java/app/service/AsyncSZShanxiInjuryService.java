package app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi3.InsuranceShanXi3Injury;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi3.InsuranceShanXi3InjuryPerson;
import com.microservice.dao.repository.crawler.insurance.sz.shanxi3.InsuranceShanXi3InjuryPersonRepository;
import com.microservice.dao.repository.crawler.insurance.sz.shanxi3.InsuranceShanXi3InjuryRepository;

import app.bean.ResponseBean;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceSZShanxiParser;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sz.shanxi3"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sz.shanxi3"})
public class AsyncSZShanxiInjuryService {
	
	@Autowired
	private TracerLog tracer; 
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceSZShanxiService insuranceSZShanxiService;
	@Autowired
	private InsuranceSZShanxiParser insuranceSZShanxiParser;
	@Autowired
	private InsuranceShanXi3InjuryPersonRepository insuranceShanXi3InjuryPersonRepository;
	@Autowired
	private InsuranceShanXi3InjuryRepository insuranceShanXi3InjuryRepository;

	/**
	 * 获取工伤保险
	 * @param insuranceRequestParameters
	 */
	public void getInjury(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("parser.login.service.getInjury",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = insuranceService.getTaskInsurance(insuranceRequestParameters);
		//城镇职工失业保险
		ResponseBean responseBean = insuranceSZShanxiService.uniteLogin("http://117.36.52.39/bjgsLogin.jsp",insuranceRequestParameters,taskInsurance);
		
		if(null != responseBean && responseBean.getWebClient() != null){
			
			String personUrl = "http://117.36.52.39/jsp/bjgs/bjgsPersonInfoQuery.jsp";
			HtmlPage personPage = (HtmlPage) insuranceSZShanxiService.getHtml(personUrl,responseBean.getWebClient());
			if(null != personPage){
				InsuranceShanXi3InjuryPerson insuranceShanXi3InjuryPerson = insuranceSZShanxiParser.parserInjuryPerson(personPage.asXml());
				insuranceShanXi3InjuryPerson.setTaskid(taskInsurance.getTaskid());
				insuranceShanXi3InjuryPersonRepository.save(insuranceShanXi3InjuryPerson);
				tracer.addTag("城镇职工工伤保险个人信息：", "爬取入库完成。");
			}
			String mingxiUrl = "http://117.36.52.39/bjgsPaymentQuery.do";
			HtmlPage mingxiPage = (HtmlPage) insuranceSZShanxiService.getHtml(mingxiUrl,responseBean.getWebClient());
			if(null != mingxiPage){
				List<InsuranceShanXi3Injury> insuranceShanXi3Injurys = insuranceSZShanxiParser.parserInjury(mingxiPage.asXml(),taskInsurance.getTaskid());
				insuranceShanXi3InjuryRepository.saveAll(insuranceShanXi3Injurys);
				tracer.addTag("城镇职工工伤保险缴纳明细：", "爬取入库完成。");
			}
			
			insuranceService.changeCrawlerStatus("【个人社保-工伤保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
					200, responseBean.getTaskInsurance());
			insuranceService.changeCrawlerStatus("【个人社保-生育保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
					201, responseBean.getTaskInsurance());
			insuranceService.changeCrawlerStatus("【个人社保-用户信息】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					201, responseBean.getTaskInsurance());
			//判断是否爬取完成
			insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());
		}else if(500 == responseBean.getCode()){
			tracer.addTag("账户或者密码错误", "error");
		}else{
			tracer.addTag("发生未知错误。", "获取失业失败！");
			insuranceService.changeCrawlerStatus("【个人社保-工伤保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
					500, responseBean.getTaskInsurance());
			insuranceService.changeCrawlerStatus("【个人社保-生育保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
					201, responseBean.getTaskInsurance());
			insuranceService.changeCrawlerStatus("【个人社保-用户信息】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					201, responseBean.getTaskInsurance());
			//判断是否爬取完成
			insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());
		}
	}

}
