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
import com.microservice.dao.entity.crawler.insurance.guangzhou.GuangzhouMedicalInsurance;
import com.microservice.dao.entity.crawler.insurance.guangzhou.GuangzhouUserInfo;
import com.microservice.dao.entity.crawler.insurance.guangzhou.InsuranceGuangzhouHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.guangzhou.GuangZhouMedicalInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.guangzhou.GuangZhouUserInfoRepository;
import com.microservice.dao.repository.crawler.insurance.guangzhou.InsuranceGuangzhouHtmlRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceGuangzhouParser;

/**
 * @author 王培阳
 *
 */
@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.guangzhou"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.guangzhou"})
public class GetMedicalInfoService {

	@Autowired
	private InsuranceGuangzhouParser insuranceGuangzhouParser;
	@Autowired
	private GuangZhouUserInfoRepository guangZhouUserInfoRepository;
	@Autowired
	private GuangZhouMedicalInsuranceRepository guangZhouMedicalInsuranceRepository;
	@Autowired
	private GetUserInfoPartThreeService getUserInfoPartFourService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceGuangzhouHtmlRepository insuranceGuangzhouHtmlRepository;
	@Autowired
	private TracerLog tracer;
	/**
	 * @Des 获取个人信息以及医保信息
	 * @throws Exception 
	 */
	@Async
	public void getMedicalInfo(TaskInsurance taskInsurance) {
		try {
			tracer.addTag("parser.crawler.getMedical",taskInsurance.getTaskid());
			taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
			tracer.addTag("parser.crawler.getUserinfo个人信息以及医保信息", taskInsurance.toString());
			tracer.addTag("parser.crawler.getMedical个人信息以及医保信息", taskInsurance.toString());
			GuangzhouUserInfo userInfo = guangZhouUserInfoRepository.findByTaskid(taskInsurance.getTaskid()).get(0);
			
			WebParam webParam = insuranceGuangzhouParser.getMedicalInfo(taskInsurance);
			
			InsuranceGuangzhouHtml insuranceGuangzhouHtml = new InsuranceGuangzhouHtml();
			
			userInfo = webParam.getGuangzhouUserInfo();
			List<GuangzhouMedicalInsurance> guangzhouMedicalInsurances = webParam.getGuangzhouMedicalInsurances();
			
			if(null != webParam.getPage()){
				tracer.addTag("parser.crawler.getUserinfo个人信息以及医保信息", webParam.getPage().asXml());
				insuranceGuangzhouHtml.setHtml(webParam.getPage().asXml());
				insuranceGuangzhouHtml.setTaskid(taskInsurance.getTaskid());
				insuranceGuangzhouHtml.setType("medical/userinfo-医保缴费历史");
				insuranceGuangzhouHtml.setPageCount(1);
				insuranceGuangzhouHtml.setUrl(webParam.getUrl());
				insuranceGuangzhouHtmlRepository.save(insuranceGuangzhouHtml);
				
				if(webParam.getPage().asXml().contains("您今天的缴费历史查询已经达到5次")){
					tracer.addTag("parser.crawler.getUserinfo个人信息以及医保信息", "您今天的缴费历史查询已经达到5次，请明天再查。");
					taskInsurance.setPhase("CRAWLER");
					taskInsurance.setPhase_status("ERROR");
					taskInsurance.setDescription("您今天的缴费历史查询已经达到5次，请明天再查。");
					taskInsuranceRepository.save(taskInsurance);
					
				}else {
					if(null != userInfo){
						guangZhouUserInfoRepository.save(userInfo);
					}
					//网页错误
					if(500 == webParam.getMedicalPageCode()){
						taskInsurance.setYiliaoStatus(201);
						taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getPhase());
						taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getDescription());
					}
					if(null != guangzhouMedicalInsurances){
						guangZhouMedicalInsuranceRepository.saveAll(guangzhouMedicalInsurances);
						taskInsurance.setYiliaoStatus(webParam.getMedicalPageCode());
					}else{
						taskInsurance.setYiliaoStatus(201);
						taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_FAILUE.getDescription());
					}
					taskInsurance.setUserInfoStatus(webParam.getCode());
					
					tracer.addTag("parser.crawler.getMedical.taskInsurance", taskInsurance.toString());
					taskInsuranceRepository.save(taskInsurance);
				}
			}else{
				taskInsurance.setPhase_status("ERROR");
				taskInsurance.setDescription("数据采集时发生异常。");
				taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.crawler.getUserinfo2.Exception",e.toString());
			tracer.addTag("parser.crawler.getMedical.Exception",e.toString());
			taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
			taskInsurance.setYiliaoStatus(404);
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_FAILUE.getDescription());
			taskInsuranceRepository.save(taskInsurance);
		} finally {
			getUserInfoPartFourService.getUserInfo(taskInsurance);
		}
	}
}
