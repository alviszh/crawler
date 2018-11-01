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
 * 爬取 工伤保险 Service
 * @author rongshengxu
 *
 */
@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.shenzhen"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.shenzhen"})
public class CrawlerInjuryInsuranceService {

	@Autowired 
	private TracerLog tracer;
	
	@Autowired
	private ShenzhenCrawler shenzhenCrawler;
	
	@Autowired
	private InsuranceShenzhenHtmlRepository insuranceShenzhenHtmlRepository;
	
	@Autowired
	private InsuranceService insuranceService;
	
	@Autowired
	private CrawlerUnemploymentInsuranceService crawlerUnemploymentInsuranceService;
	
	/**
	 * 爬取工伤保险
	 * @param parameter
	 * @param taskInsurance
	 * @return
	 */
	public WebParam<HtmlPage> crawlerInjuryInsurance(InsuranceRequestParameters parameter,TaskInsurance taskInsurance,HtmlPage inHomePage,Map<String,WebParam<HtmlPage>> resultMap){
		tracer.addTag("CrawlerInjuryInsuranceService.crawlerInjuryInsurance:开始爬取工伤保险", parameter.toString());

		//开始爬取
		WebParam<HtmlPage> webParam = shenzhenCrawler.crawlerInjuryInsurance(parameter,inHomePage);
		
		tracer.addTag("CrawlerInjuryInsuranceService.crawlerInjuryInsurance:爬取完成", webParam.toString());
		//Html入库
		if(InsuranceShenzhenCrawlerResult.SUCCESS.getCode().equals(webParam.getCode()) &&
				webParam.getData() != null){
			HtmlPage htmlPage = webParam.getData();
			InsuranceShenzhenHtml insuranceShenzhenHtml = new InsuranceShenzhenHtml();
			insuranceShenzhenHtml.setHtml(htmlPage.asText());
			insuranceShenzhenHtml.setTaskId(taskInsurance.getTaskid());
			insuranceShenzhenHtml.setType(InsuranceShenzhenCrawlerType.INJURY_INSURANCE.getCode());
			insuranceShenzhenHtml.setUrl(ShenzhenCrawler.HOME_URL);
			insuranceShenzhenHtmlRepository.save(insuranceShenzhenHtml);
			
			//更新Task表为 工伤保险数据采集成功
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS);
		}else{
			//更新Task表为 工伤保险数据采集失败
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_FAILUE);
		}
		
		tracer.addTag("CrawlerInjuryInsuranceService.crawlerInjuryInsurance:爬取完成,调用爬取 失业保险", webParam.toString());
		//保存爬取结果,调用爬取 失业保险
		resultMap.put("injuryInsurance", webParam);
		crawlerUnemploymentInsuranceService.crawlerUnemploymentInsurance(parameter, taskInsurance, inHomePage, resultMap);
		return webParam;
	}
	
	
}
