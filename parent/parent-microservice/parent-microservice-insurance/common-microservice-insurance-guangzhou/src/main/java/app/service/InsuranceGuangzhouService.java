package app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.guangzhou.GuangzhouMedicalInsurance;
import com.microservice.dao.entity.crawler.insurance.guangzhou.GuangzhouUserInfo;
import com.microservice.dao.entity.crawler.insurance.guangzhou.InsuranceGuangZhouGeneral;
import com.microservice.dao.entity.crawler.insurance.guangzhou.InsuranceGuangzhouHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.guangzhou.GuangZhouMedicalInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.guangzhou.GuangZhouUserInfoRepository;
import com.microservice.dao.repository.crawler.insurance.guangzhou.InsuranceGuangZhouGeneralRepository;
import com.microservice.dao.repository.crawler.insurance.guangzhou.InsuranceGuangzhouHtmlRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceGuangzhouParser;

/**
 * @author 王培阳
 */
@EnableAsync
@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.guangzhou"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.guangzhou"})
public class InsuranceGuangzhouService {

	@Autowired
	private InsuranceGuangZhouGeneralRepository insuranceGuangZhouGeneralRepository;
	@Autowired
	private GuangZhouMedicalInsuranceRepository guangZhouMedicalInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceGuangzhouParser insuranceGuangzhouParser;
	@Autowired
	private GuangZhouUserInfoRepository guangZhouUserInfoRepository;
	@Autowired
	private GetUserInfoPartTwoService getUserInfoPartTwoService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceGuangzhouHtmlRepository insuranceGuangzhouHtmlRepository;
	@Autowired
	private TracerLog tracer;
	/**
	 * @Des 获取个人信息
	 * @throws Exception 
	 */
	@Async
	public void getUserInfo(TaskInsurance taskInsurance) {
		try {
			tracer.addTag("parser.crawler.getUserinfo",taskInsurance.getTaskid());
			taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
			tracer.addTag("parser.crawler.getUserinfo.taskInsurance", taskInsurance.toString());
			InsuranceGuangzhouHtml insuranceGuangzhouHtml = new InsuranceGuangzhouHtml();
			
			WebParam webParam = insuranceGuangzhouParser.getUserInfoOne(taskInsurance);
			GuangzhouUserInfo guangzhouUserInfo = webParam.getGuangzhouUserInfo();
			if(null != guangzhouUserInfo){
				guangZhouUserInfoRepository.save(guangzhouUserInfo);
				tracer.addTag("parser.crawler.getUserinfo.success", "个人信息已入库");
				taskInsurance = insuranceService.changeCrawlerStatusUserInfo(taskInsurance, 200);
			}else{
				tracer.addTag("parser.crawler.getUserinfo.fail", "个人信息未入库");
				taskInsurance = insuranceService.changeCrawlerStatusUserInfo(taskInsurance, 201);
			}
			if(null != webParam.getPage()){
				insuranceGuangzhouHtml.setHtml(webParam.getPage().asXml());
				insuranceGuangzhouHtml.setTaskid(taskInsurance.getTaskid());
				insuranceGuangzhouHtml.setType("userinfo-个人基础信息查询页面");
				insuranceGuangzhouHtml.setPageCount(1);
				insuranceGuangzhouHtml.setUrl(webParam.getUrl());
				insuranceGuangzhouHtmlRepository.save(insuranceGuangzhouHtml);
			}
			tracer.addTag("parser.crawler.getUserinfo.taskInsurance个人基础信息查询页面", taskInsurance.toString());
			taskInsuranceRepository.save(taskInsurance);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.crawler.getUserinfo.Exception",e.toString());
			taskInsurance = insuranceService.changeCrawlerStatusUserInfo(taskInsurance, 404);
		}
		insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
	}
	
	/**
	 * @Des 获取个人信息以及医保信息
	 * @throws Exception 
	 */
	@Async
	public void getMedicalInfo(TaskInsurance taskInsurance) {
		try {
			tracer.addTag("parser.crawler.getMedical",taskInsurance.getTaskid());
			taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
			tracer.addTag("parser.crawler.getMedical医保信息", taskInsurance.toString());
			WebParam webParam = insuranceGuangzhouParser.getMedicalInfo(taskInsurance);
			
			InsuranceGuangzhouHtml insuranceGuangzhouHtml = new InsuranceGuangzhouHtml();
			List<GuangzhouMedicalInsurance> guangzhouMedicalInsurances = webParam.getGuangzhouMedicalInsurances();
			if(null != webParam.getPage()){
				tracer.addTag("parser.crawler.getUserinfo个人信息以及医保信息", webParam.getPage().asXml());
				insuranceGuangzhouHtml.setHtml(webParam.getPage().asXml());
				insuranceGuangzhouHtml.setTaskid(taskInsurance.getTaskid());
				insuranceGuangzhouHtml.setType("medical");
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
					if(null != guangzhouMedicalInsurances && guangzhouMedicalInsurances.size() > 0){
						guangZhouMedicalInsuranceRepository.saveAll(guangzhouMedicalInsurances);
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
								InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(),
								200, taskInsurance);
						tracer.addTag("parser.crawler.getMedical.success", "医保数据采集成功");
					}else{
						tracer.addTag("parser.crawler.getMedical.fail", "医保数据采集失败");
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
								InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(),
								201, taskInsurance);
					}
				}
			}else{
				tracer.addTag("parser.crawler.getMedical.fail2", "医保数据采集失败");
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(),
						201, taskInsurance);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.crawler.getUserinfo2.Exception",e.toString());
			tracer.addTag("parser.crawler.getMedical.Exception",e.toString());
			taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(),
					404, taskInsurance);
		} 
		insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
	}
	
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
					if(null != insuranceGuangZhouGenerals && insuranceGuangZhouGenerals.size() > 0){
						insuranceGuangZhouGeneralRepository.saveAll(insuranceGuangZhouGenerals);
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
								InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(),
								200, taskInsurance);
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
								InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(),
								200, taskInsurance);
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
								InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(),
								200, taskInsurance);
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
								InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(),
								200, taskInsurance);
						tracer.addTag("parser.crawler.getGeneral.success", "社保综合信息采集完成已入库");
					}else{
						tracer.addTag("parser.crawler.getGeneral.fail", "社保综合信息采集失败，无数据");
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
								InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(),
								201, taskInsurance);
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
								InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(),
								201, taskInsurance);
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
								InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(),
								201, taskInsurance);
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
								InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(),
								201, taskInsurance);
					}
					tracer.addTag("parser.crawler.getGeneral.taskInsurance社保综合信息 ", taskInsurance.toString());
				}
			}else{
				tracer.addTag("parser.crawler.getGeneral.fail2", "社保综合信息采集失败，无数据");
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(),
						201, taskInsurance);
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(),
						201, taskInsurance);
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(),
						201, taskInsurance);
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(),
						201, taskInsurance);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.crawler.getGeneralInfo.Exception",e.toString());
			taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(),
					404, taskInsurance);
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(),
					404, taskInsurance);
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(),
					404, taskInsurance);
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(),
					404, taskInsurance);
		}
		insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
	}
}
