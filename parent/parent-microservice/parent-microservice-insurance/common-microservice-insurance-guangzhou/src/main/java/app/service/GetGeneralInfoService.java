/**
 * 
 */
package app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.guangzhou.InsuranceGuangZhouGeneral;
import com.microservice.dao.entity.crawler.insurance.guangzhou.InsuranceGuangzhouHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.guangzhou.InsuranceGuangZhouGeneralRepository;
import com.microservice.dao.repository.crawler.insurance.guangzhou.InsuranceGuangzhouHtmlRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceGuangzhouParser;

/**
 * @author Administrator
 *
 */
@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.guangzhou"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.guangzhou",})
public class GetGeneralInfoService {

	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceGuangzhouParser insuranceGuangzhouParser;
	@Autowired
	private InsuranceGuangzhouHtmlRepository insuranceGuangzhouHtmlRepository;
	@Autowired
	private InsuranceGuangZhouGeneralRepository insuranceGuangZhouGeneralRepository;
	@Autowired
	private TracerLog tracer;
	
	/**
	 * @Des 获取社保综合缴费历史信息
	 * @throws Exception 
	 */
	@Async
	public void getGeneralInfo(TaskInsurance taskInsurance) {
		tracer.addTag("parser.crawler.getPension",taskInsurance.getTaskid());
		tracer.addTag("parser.crawler.getInjury",taskInsurance.getTaskid());
		tracer.addTag("parser.crawler.getBear",taskInsurance.getTaskid());
		tracer.addTag("parser.crawler.getUnemployment",taskInsurance.getTaskid());
		
		taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
		tracer.addTag("parser.crawler.getGeneral.taskInsurance社保综合信息", taskInsurance.toString());
		InsuranceGuangzhouHtml insuranceGuangzhouHtml = new InsuranceGuangzhouHtml();
		
		try {
			WebParam webParam = insuranceGuangzhouParser.getInsuranceGeneralInfo(taskInsurance);
			List<InsuranceGuangZhouGeneral> insuranceGuangZhouGenerals = webParam.getInsuranceGuangZhouGenerals();
			System.out.println(webParam.getPage().asXml());
			if(null != webParam.getPage()){
				insuranceGuangzhouHtml.setHtml(webParam.getPage().asXml());
				insuranceGuangzhouHtml.setTaskid(taskInsurance.getTaskid());
				insuranceGuangzhouHtml.setType("insuranceGeneral");
				insuranceGuangzhouHtml.setPageCount(1);
				insuranceGuangzhouHtml.setUrl(webParam.getUrl());
				insuranceGuangzhouHtmlRepository.save(insuranceGuangzhouHtml);
				
				if(webParam.getPage().asXml().contains("您今天的缴费历史查询已经达到5次")){
					tracer.addTag("parser.crawler.getGeneral.taskInsurance社保综合信息", "您今天的缴费历史查询已经达到5次，请明天再查。");
					taskInsurance.setPhase("CRAWLER");
					taskInsurance.setPhase_status("ERROR");
					taskInsurance.setDescription("您今天的缴费历史查询已经达到5次，请明天再查。");
					taskInsuranceRepository.save(taskInsurance);
				}else{
					if(null != insuranceGuangZhouGenerals){
						insuranceGuangZhouGeneralRepository.saveAll(insuranceGuangZhouGenerals);
						taskInsurance.setGongshangStatus(webParam.getCode());
						taskInsurance.setShengyuStatus(webParam.getCode());
						taskInsurance.setShiyeStatus(webParam.getCode());
						taskInsurance.setYanglaoStatus(webParam.getCode());
					}else{
						taskInsurance.setYanglaoStatus(201);
						taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getDescription());

						taskInsurance.setShengyuStatus(201);
						taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getDescription());

						taskInsurance.setShiyeStatus(201);
						taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_FAILUE.getDescription());
						
						taskInsurance.setGongshangStatus(201);
						taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_FAILUE.getDescription());
						
						taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_FAILUE.getPhase());
						
					}
					
					tracer.addTag("parser.crawler.getGeneral.taskInsurance社保综合信息 ", taskInsurance.toString());
					taskInsuranceRepository.save(taskInsurance);
				}
			}else{
				taskInsurance.setYanglaoStatus(404);
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getDescription());

				taskInsurance.setShengyuStatus(404);
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getDescription());

				taskInsurance.setShiyeStatus(404);
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_FAILUE.getDescription());
				
				taskInsurance.setGongshangStatus(404);
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_FAILUE.getDescription());
				
				taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.crawler.getGeneralInfo.Exception",e.toString());
			taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
			
			taskInsurance.setYanglaoStatus(404);
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getDescription());

			taskInsurance.setShengyuStatus(404);
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getDescription());

			taskInsurance.setShiyeStatus(404);
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_FAILUE.getDescription());
			
			taskInsurance.setGongshangStatus(404);
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_FAILUE.getDescription());
			
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_FAILUE.getPhase());
			
			taskInsuranceRepository.save(taskInsurance);
		} finally {
			taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
			insuranceService.changeCrawlerStatusSuccess(taskInsurance);
		}
		
	}
}
