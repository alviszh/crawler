package app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.shenzhen.InsuranceShenzhenPayDetail;
import com.microservice.dao.repository.crawler.insurance.shenzhen.InsuranceShenzhenPayDetailRepository;

import app.commontracerlog.TracerLog;

/**
 * 解析失业保险Service
 * @author rongshengxu
 *
 */
@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.shenzhen"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.shenzhen"})
public class ParserUnemploymentInsuranceService {
	
	@Autowired 
	private TracerLog tracer;
	
	@Autowired
	private ShenzhenCrawlerParser shenzhenCrawlerParser;
	
	@Autowired
	private InsuranceShenzhenPayDetailRepository insuranceShenzhenPayDetailRepository;
	
	@Autowired
	private InsuranceService insuranceService;
	
	/**
	 * 解析失业保险
	 * @param baseInfoPage
	 * @return
	 */
	public WebParam<List<InsuranceShenzhenPayDetail>> parserUnemploymentInsurance(Map<String,WebParam<HtmlPage>> resultMap,TaskInsurance taskInsurance){
		
		HtmlPage unemploymentPage = resultMap.get("unemploymentInsurance").getData();
		
		tracer.addTag("ParserUnemploymentInsuranceService.parserUnemploymentInsurance:开始解析失业保险", unemploymentPage.asText());
		
		//开始解析
		WebParam<List<InsuranceShenzhenPayDetail>> payDetailWebParam = shenzhenCrawlerParser.parserUnemploymentPayDetailsInsurance(unemploymentPage, taskInsurance.getTaskid());
		tracer.addTag("ParserUnemploymentInsuranceService.parserUnemploymentInsurance:解析完成,准备入库", payDetailWebParam.toString());
		
		//缴费明细入库
		if(InsuranceShenzhenCrawlerResult.SUCCESS.getCode().equals(payDetailWebParam.getCode()) &&
				payDetailWebParam.getData() != null){
			List<InsuranceShenzhenPayDetail> payDetails = payDetailWebParam.getData();
			insuranceShenzhenPayDetailRepository.saveAll(payDetails);
			
			//更新Task表为 失业保险数据解析成功
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_SUCCESS);
		}else{
			//更新Task表为 失业保险数据解析失败
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_FAILUE);
		}
		//生育保险 不存在
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_NOT_FOUND);
		return payDetailWebParam;
	}

}
