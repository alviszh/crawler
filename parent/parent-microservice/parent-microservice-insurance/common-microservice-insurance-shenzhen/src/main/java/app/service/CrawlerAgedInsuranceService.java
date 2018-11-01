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
 * 爬取养老保险 Service
 * @author rongshengxu
 *
 */
@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.shenzhen"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.shenzhen"})
public class CrawlerAgedInsuranceService {
	
	@Autowired 
	private TracerLog tracer;
	
	@Autowired
	private ShenzhenCrawler shenzhenCrawler;
	
	@Autowired
	private InsuranceShenzhenHtmlRepository insuranceShenzhenHtmlRepository;
	
	@Autowired
	private CrawlerMedicalInsuranceService crawlerMedicalInsuranceService;
	
	@Autowired
	private InsuranceService insuranceService;
	
	/**
	 * 爬取养老保险
	 * @param parameter
	 * @param taskInsurance
	 * @return
	 */
	public WebParam<HtmlPage> crawlerAgedInsurance(InsuranceRequestParameters parameter,TaskInsurance taskInsurance,HtmlPage inHomePage,Map<String,WebParam<HtmlPage>> resultMap){
		tracer.addTag("CrawlerAgedInsuranceService.crawlerAgedInsurance:开始爬取养老保险", parameter.toString());

		//开始爬取
		WebParam<HtmlPage> webParam = shenzhenCrawler.crawlerAgedInsurance(parameter,inHomePage);
		
		tracer.addTag("CrawlerAgedInsuranceService.crawlerAgedInsurance:爬取完成", webParam.toString());
		//Html入库
		if(InsuranceShenzhenCrawlerResult.SUCCESS.getCode().equals(webParam.getCode()) &&
				webParam.getData() != null){
			HtmlPage htmlPage = webParam.getData();
			InsuranceShenzhenHtml insuranceShenzhenHtml = new InsuranceShenzhenHtml();
			insuranceShenzhenHtml.setHtml(htmlPage.asText());
			insuranceShenzhenHtml.setTaskId(taskInsurance.getTaskid());
			insuranceShenzhenHtml.setType(InsuranceShenzhenCrawlerType.AGED_INSURANCE.getCode());
			insuranceShenzhenHtml.setUrl(ShenzhenCrawler.HOME_URL);
			insuranceShenzhenHtmlRepository.save(insuranceShenzhenHtml);
			
			//更新Task表为 养老保险数据采集成功
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS);
		}else{
			//更新Task表为 养老保险数据采集失败
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE);
		}
		
		tracer.addTag("CrawlerAgedInsuranceService.crawlerAgedInsurance:爬取完成,调用爬取医疗保险", webParam.toString());
		//保存爬取结果,调用爬取 医疗保险
		resultMap.put("agedInsurance", webParam);
		crawlerMedicalInsuranceService.crawlerMedicalInsurance(parameter, taskInsurance, inHomePage, resultMap);
		return webParam;
	}
	
}
