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
import com.microservice.dao.entity.crawler.insurance.sz.shanxi3.InsuranceShanXi3Unemployment;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi3.InsuranceShanXi3UnemploymentPerson;
import com.microservice.dao.repository.crawler.insurance.sz.shanxi3.InsuranceShanXi3UnemploymentPersonRepository;
import com.microservice.dao.repository.crawler.insurance.sz.shanxi3.InsuranceShanXi3UnemploymentRepository;

import app.bean.ResponseBean;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceSZShanxiParser;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sz.shanxi3"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sz.shanxi3"})
public class AsyncSZShanxiUnemploymentService {
	
	@Autowired
	private TracerLog tracer; 
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceSZShanxiService insuranceSZShanxiService;
	@Autowired
	private InsuranceSZShanxiParser insuranceSZShanxiParser;
	@Autowired
	private InsuranceShanXi3UnemploymentPersonRepository insuranceShanXi3UnemploymentPersonRepository;
	@Autowired
	private InsuranceShanXi3UnemploymentRepository insuranceShanXi3UnemploymentRepository;
	@Autowired
	private AsyncSZShanxiInjuryService asyncSZShanxiInjuryService;
	
	
	/**
	 * 获取失业保险
	 * @param insuranceRequestParameters
	 */
	public void getUnemployment(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("parser.login.service.getUnemployment",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = insuranceService.getTaskInsurance(insuranceRequestParameters);
		//城镇职工失业保险
		ResponseBean responseBean = insuranceSZShanxiService.uniteLogin("http://117.36.52.39/sylssLogin.jsp",insuranceRequestParameters,taskInsurance);
		
		if(null != responseBean && responseBean.getWebClient() != null){
			
			String personUrl = "http://117.36.52.39/sylssPersonInfoQuery.do";
			HtmlPage personPage = (HtmlPage) insuranceSZShanxiService.getHtml(personUrl,responseBean.getWebClient());
			if(null != personPage){
				InsuranceShanXi3UnemploymentPerson insuranceShanXi3UnemploymentPerson = insuranceSZShanxiParser.parserUnemploymentPerson(personPage.asXml());
				insuranceShanXi3UnemploymentPerson.setTaskid(taskInsurance.getTaskid());
				insuranceShanXi3UnemploymentPersonRepository.save(insuranceShanXi3UnemploymentPerson);
				tracer.addTag("城镇职工失业保险个人信息：", "爬取入库完成。");
			}
			String mingxiUrl = "http://117.36.52.39/sylssPaymentQuery.do";
			HtmlPage mingxiPage = (HtmlPage) insuranceSZShanxiService.getHtml(mingxiUrl,responseBean.getWebClient());
			if(null != mingxiPage){
				List<InsuranceShanXi3Unemployment> insuranceShanXi3Unemployments = insuranceSZShanxiParser.parserUnemployment(mingxiPage.asXml(),taskInsurance.getTaskid());
				insuranceShanXi3UnemploymentRepository.saveAll(insuranceShanXi3Unemployments);
				tracer.addTag("城镇职工失业保险缴纳明细：", "爬取入库完成。");
			}
			
			insuranceService.changeCrawlerStatus("【个人社保-失业保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
					200, responseBean.getTaskInsurance());
			//执行失业保险爬取
			asyncSZShanxiInjuryService.getInjury(insuranceRequestParameters);
		}else if(500 == responseBean.getCode()){
			tracer.addTag("账户或者密码错误", "error");
		}else{
			tracer.addTag("发生未知错误。", "获取失业失败！");
			insuranceService.changeCrawlerStatus("【个人社保-失业保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
					500, responseBean.getTaskInsurance());
			//执行失业保险爬取
			asyncSZShanxiInjuryService.getInjury(insuranceRequestParameters);
		}
	}

}
