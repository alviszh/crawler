package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.foshan.InsuranceFoshanHtml;
import com.microservice.dao.entity.crawler.insurance.foshan.InsuranceFoshanInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.foshan.InsuranceFoshanHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.foshan.InsuranceFoshanInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceFoshanParser;

/**
 * @author 王培阳
 *
 */
@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.foshan"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.foshan"})
public class GetMedicalInfoService {

	
	@Autowired
	private InsuranceFoshanInfoRepository insuranceFoshanInfoRepository;
	@Autowired
	private InsuranceFoshanHtmlRepository insuranceFoshanHtmlRepository;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceFoshanParser insuranceFoshanParser;
	@Autowired
	private GetInjuryInfoService getInjuryInfoService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	/**
	 * @Des 爬取医疗社保信息
	 * @param taskInsurance
	 */
	@Async
	public void getInfo(TaskInsurance taskInsurance) {
		try {
			tracer.addTag("parser.crawler.getMedical",taskInsurance.getTaskid());
			taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
			tracer.addTag("parser.crawler.getMedicalinfo.taskInsurance", taskInsurance.toString());
			WebParam<InsuranceFoshanInfo> webParam = insuranceFoshanParser.getMedicalInfo(taskInsurance);
			if(webParam.getCode() == 100){
				insuranceService.changeCrawlerStatus("没有登录或者连结超时,无法获取数据", "CRAWLER_YILIAO_MSG", 100, taskInsurance);
				tracer.addTag("parser.crawler.getMedicalinfo", "没有登录或者连结超时,无法获取数据");
			}
			if(null != webParam.getPage()){
				InsuranceFoshanHtml insuranceFoshanHtml = new InsuranceFoshanHtml();
				insuranceFoshanHtml.setTaskid(taskInsurance.getTaskid());
				insuranceFoshanHtml.setType("medicalInfo");
				insuranceFoshanHtml.setPageCount(1);
				insuranceFoshanHtml.setUrl(webParam.getUrl());
				insuranceFoshanHtml.setHtml(webParam.getPage().asXml());
				
				insuranceFoshanHtmlRepository.save(insuranceFoshanHtml);
				tracer.addTag("parser.crawler.getMedicalinfo.html", "医疗社保页面已入库");
			}
			if(null != webParam.getList()){
				insuranceFoshanInfoRepository.saveAll(webParam.getList());
				tracer.addTag("parser.crawler.getMedicalinfo", "医疗信息已入库");
				insuranceService.changeCrawlerStatus("数据采集中，【医疗保险信息】已采集完成","CRAWLER_YILIAO_MSG",200, taskInsurance);
			}else{
				insuranceService.changeCrawlerStatus("数据采集中，【医疗保险信息】数据采集失败","CRAWLER_YILIAO_MSG",201, taskInsurance);
				tracer.addTag("parser.crawler.getMedicalinfo", "医疗信息未爬取到，无数据");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.crawler.getMedicalinfo.service.Exception", e.toString());
			taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
			insuranceService.changeCrawlerStatus("数据采集中，【医疗保险信息】数据采集完成","CRAWLER_YILIAO_MSG",404, taskInsurance);
		} finally {
			getInjuryInfoService.getInfo(taskInsurance);
		}
	}
	
	
}
