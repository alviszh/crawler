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
 * 爬取基本信息Service
 * @author rongshengxu
 *
 */
@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.shenzhen"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.shenzhen"})
public class CrawlerBaseInfoService {
	
	@Autowired 
	private TracerLog tracer;
	
	@Autowired
	private ShenzhenCrawler shenzhenCrawler;
	
	@Autowired
	private InsuranceShenzhenHtmlRepository insuranceShenzhenHtmlRepository;
	
	@Autowired
	private InsuranceService insuranceService;
	
	@Autowired
	private CrawlerAgedInsuranceService crawlerAgedInsuranceService;
	
	
	/**
	 * 爬取基本信息
	 * @param parameter
	 * @param cookies
	 * @param pid
	 * @return
	 */
	public WebParam<HtmlPage> crawlerBaseInfo(InsuranceRequestParameters parameter,TaskInsurance taskInsurance,HtmlPage inHomePage,Map<String,WebParam<HtmlPage>> resultMap){
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取基本信息", parameter.toString());
		
		//开始爬取
		WebParam<HtmlPage> webParam = shenzhenCrawler.crawlerBaseInfo(parameter,inHomePage);
		
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:爬取完成", webParam.toString());
		//Html入库
		if(InsuranceShenzhenCrawlerResult.SUCCESS.getCode().equals(webParam.getCode()) &&
				webParam.getData() != null){
			HtmlPage htmlPage = webParam.getData();
			InsuranceShenzhenHtml insuranceShenzhenHtml = new InsuranceShenzhenHtml();
			insuranceShenzhenHtml.setHtml(htmlPage.asText());
			insuranceShenzhenHtml.setTaskId(taskInsurance.getTaskid());
			insuranceShenzhenHtml.setType(InsuranceShenzhenCrawlerType.BASE_INFO.getCode());
			insuranceShenzhenHtml.setUrl(ShenzhenCrawler.HOME_URL);
			insuranceShenzhenHtmlRepository.save(insuranceShenzhenHtml);
			
			//更新Task表为 基本信息数据采集成功
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS);
			
		}else{
			//更新Task表为 基本信息数据采集失败
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE);
		}
		
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:爬取完成,调用爬取养老保险", webParam.toString());
		//保存爬取结果,调用爬取 养老保险
		resultMap.put("baseInfo", webParam);
		crawlerAgedInsuranceService.crawlerAgedInsurance(parameter, taskInsurance, inHomePage, resultMap);
		return webParam;
	}

}
