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
 * 爬取失业保险 Service
 * @author rongshengxu
 *
 */
@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.shenzhen"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.shenzhen"})
public class CrawlerUnemploymentInsuranceService {
	
	@Autowired 
	private TracerLog tracer;
	
	@Autowired
	private ShenzhenCrawler shenzhenCrawler;
	
	@Autowired
	private InsuranceShenzhenHtmlRepository insuranceShenzhenHtmlRepository;
	
	@Autowired
	private InsuranceService insuranceService;
	
	@Autowired
	private ParserCrawledBaseInfoService parserCrawledBaseInfoService;
	
	/**
	 * 爬取失业保险
	 * @param parameter
	 * @param taskInsurance
	 * @return
	 */
	public WebParam<HtmlPage> crawlerUnemploymentInsurance(InsuranceRequestParameters parameter,TaskInsurance taskInsurance,HtmlPage inHomePage,Map<String,WebParam<HtmlPage>> resultMap){
		tracer.addTag("CrawlerUnemploymentInsuranceService.crawlerUnemploymentInsurance:开始爬取失业保险", parameter.toString());
		
		//开始爬取
		WebParam<HtmlPage> webParam = shenzhenCrawler.crawlerUnemploymentInsurance(parameter,inHomePage);
		
		tracer.addTag("CrawlerUnemploymentInsuranceService.crawlerUnemploymentInsurance:爬取完成,准备入库", webParam.toString());
		//Html入库
		if(InsuranceShenzhenCrawlerResult.SUCCESS.getCode().equals(webParam.getCode()) &&
				webParam.getData() != null){
			HtmlPage htmlPage = webParam.getData();
			InsuranceShenzhenHtml insuranceShenzhenHtml = new InsuranceShenzhenHtml();
			insuranceShenzhenHtml.setHtml(htmlPage.asText());
			insuranceShenzhenHtml.setTaskId(taskInsurance.getTaskid());
			insuranceShenzhenHtml.setType(InsuranceShenzhenCrawlerType.UNEMPLOYMENT_INSURANCE.getCode());
			insuranceShenzhenHtml.setUrl(ShenzhenCrawler.HOME_URL);
			insuranceShenzhenHtmlRepository.save(insuranceShenzhenHtml);
			
			//更新Task表为 失业保险数据采集成功
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS);
		}else{
			//更新Task表为 失业保险数据采集失败
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_FAILUE);
		}
		
		tracer.addTag("CrawlerUnemploymentInsuranceService.crawlerUnemploymentInsurance:爬取完成,调用解析 基本信息", webParam.toString());
		//保存爬取结果,调用解析 基本信息
		resultMap.put("unemploymentInsurance", webParam);
		parserCrawledBaseInfoService.parserCrawledBaseInfo(resultMap, taskInsurance);
		return webParam;
	}
	
}
