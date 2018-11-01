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
 * 解析工伤保险 Service
 * @author rongshengxu
 *
 */
@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.shenzhen"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.shenzhen"})
public class ParserInjuryInsuranceService {
	
	@Autowired 
	private TracerLog tracer;
	
	@Autowired
	private ShenzhenCrawlerParser shenzhenCrawlerParser;
	
	@Autowired
	private InsuranceShenzhenPayDetailRepository insuranceShenzhenPayDetailRepository;
	
	@Autowired
	private InsuranceService insuranceService;
	
	@Autowired
	private ParserUnemploymentInsuranceService parserUnemploymentInsuranceService;
	
	/**
	 * 解析工伤保险
	 * @param baseInfoPage
	 * @return
	 */
	public WebParam<List<InsuranceShenzhenPayDetail>> parserInjuryInsurance(Map<String,WebParam<HtmlPage>> resultMap,TaskInsurance taskInsurance){
		
		HtmlPage injuryPage = resultMap.get("injuryInsurance").getData();
		
		tracer.addTag("ParserInjuryInsuranceService.parserInjuryInsurance:开始解析工伤保险", injuryPage.asText());
		
		//开始解析
		WebParam<List<InsuranceShenzhenPayDetail>> payDetailWebParam = shenzhenCrawlerParser.parserInjuryPayDetailsInsurance(injuryPage, taskInsurance.getTaskid());
		tracer.addTag("ParserInjuryInsuranceService.parserInjuryInsurance:解析完成,准备入库", payDetailWebParam.toString());
		
		//缴费明细入库
		if(InsuranceShenzhenCrawlerResult.SUCCESS.getCode().equals(payDetailWebParam.getCode()) &&
				payDetailWebParam.getData() != null){
			List<InsuranceShenzhenPayDetail> payDetails = payDetailWebParam.getData();
			insuranceShenzhenPayDetailRepository.saveAll(payDetails);
			
			//更新Task表为 工伤保险数据解析成功
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_INJURY_SUCCESS);
		}else{
			//更新Task表为 工伤保险数据解析失败
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE);
		}
		
		tracer.addTag("ParserInjuryInsuranceService.parserInjuryInsurance:解析完成,调用解析失业保险", payDetailWebParam.toString());
		//解析完成,调用解析失业保险
		parserUnemploymentInsuranceService.parserUnemploymentInsurance(resultMap, taskInsurance);
		return payDetailWebParam;
	}

}
