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
import com.microservice.dao.entity.crawler.insurance.shenzhen.InsuranceShenzhenCompany;
import com.microservice.dao.entity.crawler.insurance.shenzhen.InsuranceShenzhenPayDetail;
import com.microservice.dao.repository.crawler.insurance.shenzhen.InsuranceShenzhenCompanyRepository;
import com.microservice.dao.repository.crawler.insurance.shenzhen.InsuranceShenzhenPayDetailRepository;

import app.commontracerlog.TracerLog;

/**
 * 解析养老保险Service
 * @author rongshengxu
 *
 */
@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.shenzhen"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.shenzhen"})
public class ParserAgedInsuranceService {
	
	@Autowired 
	private TracerLog tracer;
	
	@Autowired
	private ShenzhenCrawlerParser shenzhenCrawlerParser;
	
	@Autowired
	private InsuranceService insuranceService;
	
	@Autowired
	private InsuranceShenzhenCompanyRepository insuranceShenzhenCompanyRepository;
	
	@Autowired
	private InsuranceShenzhenPayDetailRepository insuranceShenzhenPayDetailRepository;
	
	@Autowired
	private ParserMedicalInsuranceService parserMedicalInsuranceService;
	
	/**
	 * 解析养老保险
	 * @param baseInfoPage
	 * @return
	 */
	public WebParam<List<InsuranceShenzhenPayDetail>> parserAgedInsurance(Map<String,WebParam<HtmlPage>> resultMap,TaskInsurance taskInsurance){
		
		HtmlPage agedInsurancePage = resultMap.get("agedInsurance").getData();
		
		tracer.addTag("ParserAgedInsuranceService.parserAgedInsurance:开始解析养老保险", agedInsurancePage.asText());
		
		//开始解析
		WebParam<List<InsuranceShenzhenCompany>> companyWebParam = shenzhenCrawlerParser.parserInsuranceCompanys(agedInsurancePage,taskInsurance.getTaskid());
		WebParam<List<InsuranceShenzhenPayDetail>> payDetailWebParam = shenzhenCrawlerParser.parserAgedPayDetailsInsurance(agedInsurancePage,taskInsurance.getTaskid());
		tracer.addTag("ParserAgedInsuranceService.parserAgedInsurance:解析完成", companyWebParam.toString());
		tracer.addTag("ParserAgedInsuranceService.parserAgedInsurance:解析完成", payDetailWebParam.toString());
		
		//单位信息 入库
		if(InsuranceShenzhenCrawlerResult.SUCCESS.getCode().equals(companyWebParam.getCode()) &&
				companyWebParam.getData() != null){
			List<InsuranceShenzhenCompany> companys = companyWebParam.getData();
			insuranceShenzhenCompanyRepository.saveAll(companys);
		}
		//缴费明细入库
		if(InsuranceShenzhenCrawlerResult.SUCCESS.getCode().equals(payDetailWebParam.getCode()) &&
				payDetailWebParam.getData() != null){
			List<InsuranceShenzhenPayDetail> payDetails = payDetailWebParam.getData();
			tracer.addTag("InsuranceShenzhenService.parserAgedInsurance:缴费明细 开始入库", payDetails.toString());
			insuranceShenzhenPayDetailRepository.saveAll(payDetails);
			
			//更新Task表为 养老保险数据解析成功
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_AGED_SUCCESS);
		}else{
			//更新Task表为 养老保险数据解析失败
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
		}
		
		tracer.addTag("ParserAgedInsuranceService.parserAgedInsurance:解析完成,调用解析医疗保险", payDetailWebParam.toString());
		//解析完成,调用解析医疗保险
		parserMedicalInsuranceService.parserMedicalInsurance(resultMap, taskInsurance);
		return payDetailWebParam;
	}
	
}
