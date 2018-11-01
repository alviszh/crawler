package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.zibo.InsuranceZiboHtml;
import com.microservice.dao.entity.crawler.insurance.zibo.InsuranceZiboInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.zibo.InsuranceZiboHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.zibo.InsuranceZiboInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceZiboParser;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.zibo"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.zibo"})
public class GetMedicalInfoService {

	
	@Autowired
	private InsuranceZiboInfoRepository insuranceZiboInfoRepository;
	@Autowired
	private InsuranceZiboHtmlRepository insuranceZiboHtmlRepository;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private GetInjuryInfoService getInjuryInfoService;
	@Autowired
	private InsuranceZiboParser insuranceZiboParser;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	
	/**
	 * @Des 爬取医疗信息
	 * @param taskInsurance
	 */
	@Async
	public void getInfo(TaskInsurance taskInsurance) {
		tracer.addTag("parser.crawler.getMedicalinfo",taskInsurance.getTaskid());
		taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
		tracer.addTag("parser.crawler.getMedicalinfo.taskInsurance", taskInsurance.toString());
		try {
			WebParam<InsuranceZiboInfo> webParam = insuranceZiboParser.getMedicalInfo(taskInsurance);
			
			if(null != webParam.getPage()){
				InsuranceZiboHtml insuranceZiboHtml = new InsuranceZiboHtml();
				insuranceZiboHtml.setTaskid(taskInsurance.getTaskid());
				insuranceZiboHtml.setType("medical");
				insuranceZiboHtml.setPageCount(1);
				insuranceZiboHtml.setUrl(webParam.getUrl());
				insuranceZiboHtml.setHtml(webParam.getPage().asXml());
				
				insuranceZiboHtmlRepository.save(insuranceZiboHtml);
				tracer.addTag("parser.crawler.getMedicalinfo.html", "医保信息页面已入库");
			}
			if(null != webParam.getList()){
				insuranceZiboInfoRepository.saveAll(webParam.getList());
				tracer.addTag("parser.crawler.getMedicalinfo", "医保信息已入库");
				
				insuranceService.changeCrawlerStatus("数据采集中，【医疗保险信息】已采集完成", "CRAWLER_YILIAO_MSG", 200, taskInsurance);
			}else{
				insuranceService.changeCrawlerStatus("数据采集中，【医疗保险信息】已采集完成", "CRAWLER_YILIAO_MSG", 201, taskInsurance);
				tracer.addTag("parser.crawler.getMedicalinfo", "医保信息未爬取到，无数据");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.crawler.getMedicalinfo.service.Exception", e.toString());
			insuranceService.changeCrawlerStatus("数据采集中，【医疗保险信息】已采集完成", "CRAWLER_YILIAO_MSG", 404, taskInsurance);
		} finally {
			getInjuryInfoService.getInfo(taskInsurance);
		}
	}
}
