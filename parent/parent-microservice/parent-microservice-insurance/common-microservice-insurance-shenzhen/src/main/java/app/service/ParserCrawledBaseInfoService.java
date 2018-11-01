package app.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.shenzhen.InsuranceShenzhenBaseInfo;
import com.microservice.dao.repository.crawler.insurance.shenzhen.InsuranceShenzhenBaseInfoRepository;

import app.commontracerlog.TracerLog;

/**
 * 解析基本信息Service
 * @author rongshengxu
 *
 */
@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.shenzhen"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.shenzhen"})
public class ParserCrawledBaseInfoService {
	
	@Autowired 
	private TracerLog tracer;
	
	@Autowired
	private ShenzhenCrawlerParser shenzhenCrawlerParser;
	
	@Autowired
	private InsuranceShenzhenBaseInfoRepository insuranceShenzhenBaseInfoRepository;
	
	@Autowired
	private InsuranceService insuranceService;
	
	@Autowired
	private ParserAgedInsuranceService parserAgedInsuranceService;
	
	/**
	 * 解析基本信息
	 * @param baseInfoPage
	 * @return
	 */
	public WebParam<InsuranceShenzhenBaseInfo> parserCrawledBaseInfo(Map<String,WebParam<HtmlPage>> resultMap, TaskInsurance taskInsurance){
		HtmlPage baseInfoPage = resultMap.get("baseInfo").getData();
		
		tracer.addTag("ParserCrawledBaseInfoService.parserCrawledBaseInfo:开始解析基本信息", baseInfoPage.asText());
		
		//开始解析
		WebParam<InsuranceShenzhenBaseInfo> webParam = shenzhenCrawlerParser.parserBaseInfo(baseInfoPage);
		tracer.addTag("ParserCrawledBaseInfoService.parserCrawledBaseInfo:解析完成", webParam.toString());
		
		//基本信息 入库
		if(InsuranceShenzhenCrawlerResult.SUCCESS.getCode().equals(webParam.getCode()) &&
				webParam.getData() != null){
			InsuranceShenzhenBaseInfo baseInfo = webParam.getData();
			baseInfo.setTaskId(taskInsurance.getTaskid());
			insuranceShenzhenBaseInfoRepository.save(baseInfo);
			
			//更新Task表为 基本信息数据解析成功
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_SUCCESS);
		}else{
			//更新Task表为 基本信息数据解析失败
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_FAILUE);
		}
		
		tracer.addTag("ParserCrawledBaseInfoService.parserCrawledBaseInfo:解析完成,调用解析养老保险", webParam.toString());
		//解析完成,调用解析养老保险
		parserAgedInsuranceService.parserAgedInsurance(resultMap, taskInsurance);
		return webParam;
	}

}
