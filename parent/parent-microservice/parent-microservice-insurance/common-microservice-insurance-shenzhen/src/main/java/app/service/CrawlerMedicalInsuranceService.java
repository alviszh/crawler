package app.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.shenzhen.InsuranceShenzhenHtml;
import com.microservice.dao.repository.crawler.insurance.shenzhen.InsuranceShenzhenHtmlRepository;

import app.commontracerlog.TracerLog;

/**
 * 爬取医疗保险 Service
 * @author rongshengxu
 *
 */
@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.shenzhen"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.shenzhen"})
public class CrawlerMedicalInsuranceService {

	@Autowired 
	private TracerLog tracer;
	
	@Autowired
	private ShenzhenCrawler shenzhenCrawler;
	
	@Autowired
	private InsuranceShenzhenHtmlRepository insuranceShenzhenHtmlRepository;
	
	@Autowired
	private InsuranceService insuranceService;
	
	@Autowired
	private CrawlerInjuryInsuranceService crawlerInjuryInsuranceService;
	
	/**
	 * 爬取医疗保险
	 * @param parameter
	 * @param taskInsurance
	 * @return
	 */
	public WebParam<HtmlPage> crawlerMedicalInsurance(InsuranceRequestParameters parameter,TaskInsurance taskInsurance,HtmlPage inHomePage,Map<String,WebParam<HtmlPage>> resultMap){
		tracer.addTag("CrawlerMedicalInsuranceService.crawlerMedicalInsurance:开始爬取医疗保险", parameter.toString());

		//开始爬取
		WebParam<HtmlPage> webParam = shenzhenCrawler.crawlerMedicalInsurance(parameter,inHomePage);
		tracer.addTag("CrawlerMedicalInsuranceService.crawlerMedicalInsurance:爬取完成", webParam.toString());
		
		//Html入库
		if(InsuranceShenzhenCrawlerResult.SUCCESS.getCode().equals(webParam.getCode()) &&
				webParam.getData() != null){
			HtmlPage htmlPage = webParam.getData();
			InsuranceShenzhenHtml insuranceShenzhenHtml = new InsuranceShenzhenHtml();
			insuranceShenzhenHtml.setHtml(htmlPage.asText());
			insuranceShenzhenHtml.setTaskId(taskInsurance.getTaskid());
			insuranceShenzhenHtml.setType(InsuranceShenzhenCrawlerType.MEDICAL_INSURANCE.getCode());
			insuranceShenzhenHtml.setUrl(ShenzhenCrawler.HOME_URL);
			insuranceShenzhenHtmlRepository.save(insuranceShenzhenHtml);
			
			//更新Task表为 医疗保险数据采集成功
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS);
		}else{
			//更新Task表为 医疗保险数据采集失败
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_FAILUE);
		}
		
		tracer.addTag("CrawlerMedicalInsuranceService.crawlerMedicalInsurance:爬取完成,调用爬取工伤保险", webParam.toString());
		//保存爬取结果,调用爬取工伤保险
		resultMap.put("medicalInsurance", webParam);
		crawlerInjuryInsuranceService.crawlerInjuryInsurance(parameter, taskInsurance, inHomePage, resultMap);
		return webParam;
	}
	
}
