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
import com.microservice.dao.entity.crawler.insurance.sz.shanxi3.InsuranceShanXi3Medical;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi3.InsuranceShanXi3MedicalPerson;
import com.microservice.dao.repository.crawler.insurance.sz.shanxi3.InsuranceShanXi3MedicalPersonRepository;
import com.microservice.dao.repository.crawler.insurance.sz.shanxi3.InsuranceShanXi3MedicalRepository;

import app.bean.ResponseBean;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceSZShanxiParser;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sz.shanxi3"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sz.shanxi3"})
public class AsyncSZShanxiMedicalService {
	
	@Autowired
	private TracerLog tracer; 
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceSZShanxiService insuranceSZShanxiService;
	@Autowired
	private InsuranceSZShanxiParser insuranceSZShanxiParser;
	@Autowired
	private InsuranceShanXi3MedicalPersonRepository insuranceShanXi3MedicalPersonRepository;
	@Autowired
	private InsuranceShanXi3MedicalRepository insuranceShanXi3MedicalRepository;
	@Autowired
	private AsyncSZShanxiUnemploymentService asyncSZShanxiUnemploymentService;

	/**
	 * 获取医疗保险数据
	 * @param insuranceRequestParameters
	 */
	public void getMedical(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("parser.login.service.getMedical",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = insuranceService.getTaskInsurance(insuranceRequestParameters);
		//城镇职工医疗保险
		ResponseBean responseBean = insuranceSZShanxiService.uniteLogin("http://117.36.52.39/zgylLogin.jsp",insuranceRequestParameters,taskInsurance);
		
		if(null != responseBean && responseBean.getWebClient() != null){
			
			String personUrl = "http://117.36.52.39/jsp/zgyl/zgylAccountCalQueryPage_notitle.jsp";
			HtmlPage personPage = (HtmlPage) insuranceSZShanxiService.getHtml(personUrl,responseBean.getWebClient());
			if(null != personPage){
				InsuranceShanXi3MedicalPerson insuranceShanXi3MedicalPerson = insuranceSZShanxiParser.parserMedicalPerson(personPage.asXml());
				insuranceShanXi3MedicalPerson.setTaskid(taskInsurance.getTaskid());
				insuranceShanXi3MedicalPersonRepository.save(insuranceShanXi3MedicalPerson);
				tracer.addTag("城镇职工医疗保险个人信息：", "爬取入库完成。");
			}
			String mingxiUrl = "http://117.36.52.39/zgylAccountCalQuery.do?title.display=true";
			HtmlPage mingxiPage = (HtmlPage) insuranceSZShanxiService.getHtml(mingxiUrl,responseBean.getWebClient());
			if(null != mingxiPage){
				List<InsuranceShanXi3Medical> insuranceShanXi3Medicals = insuranceSZShanxiParser.parserMedical(mingxiPage.asXml(),taskInsurance.getTaskid());
				insuranceShanXi3MedicalRepository.saveAll(insuranceShanXi3Medicals);
				tracer.addTag("城镇职工医疗保险缴纳明细：", "爬取入库完成。");
			}
			
			insuranceService.changeCrawlerStatus("【个人社保-医疗保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
					200, responseBean.getTaskInsurance());
			//执行失业保险爬取
			asyncSZShanxiUnemploymentService.getUnemployment(insuranceRequestParameters);
		}else if(500 == responseBean.getCode()){
			tracer.addTag("账户或者密码错误", "error");
		}else{
			tracer.addTag("发生未知错误。", "获取养老失败！");
			insuranceService.changeCrawlerStatus("【个人社保-医疗保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
					500, responseBean.getTaskInsurance());
			//执行失业保险爬取
			asyncSZShanxiUnemploymentService.getUnemployment(insuranceRequestParameters);
		}
	}

}
