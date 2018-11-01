package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.zibo.InsuranceZiboHtml;
import com.microservice.dao.entity.crawler.insurance.zibo.InsuranceZiboUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.zibo.InsuranceZiboHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.zibo.InsuranceZiboUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceZiboParser;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.zibo"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.zibo"})
public class GetUserInfoService {

	
	@Autowired
	private InsuranceZiboUserInfoRepository insuranceZiboUserInfoRepository;
	@Autowired
	private InsuranceZiboHtmlRepository insuranceZiboHtmlRepository;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private GetPensionInfoService getPensionInfoService;
	@Autowired
	private InsuranceZiboParser insuranceZiboParser;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	
	/**
	 * @Des 爬取个人信息
	 * @param taskInsurance
	 */
	public void getUserInfo(TaskInsurance taskInsurance) {
		try {
			tracer.addTag("parser.crawler.getUserinfo",taskInsurance.getTaskid());
			taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
			tracer.addTag("parser.crawler.getUserinfo.taskInsurance", taskInsurance.toString());
			
			WebParam<InsuranceZiboUserInfo> webParam = insuranceZiboParser.getUserInfo(taskInsurance);
			
			if(null != webParam.getPage()){
				InsuranceZiboHtml insuranceZiboHtml = new InsuranceZiboHtml();
				insuranceZiboHtml.setTaskid(taskInsurance.getTaskid());
				insuranceZiboHtml.setType("userInfo");
				insuranceZiboHtml.setPageCount(1);
				insuranceZiboHtml.setUrl(webParam.getUrl());
				insuranceZiboHtml.setHtml(webParam.getPage().asXml());
				
				insuranceZiboHtmlRepository.save(insuranceZiboHtml);
				tracer.addTag("parser.crawler.getUserinfo.html", "个人信息页面已入库");
			}
			if(null != webParam.getList()){
				insuranceZiboUserInfoRepository.saveAll(webParam.getList());
				tracer.addTag("parser.crawler.getUserinfo", "个人信息已入库");
				
				insuranceService.changeCrawlerStatusUserInfo(taskInsurance, 200);
			}else{
				insuranceService.changeCrawlerStatusUserInfo(taskInsurance, 201);
				tracer.addTag("parser.crawler.getUserinfo", "个人信息未爬取到，无数据");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.crawler.getUserinfo.service.Exception", e.toString());
			taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
			insuranceService.changeCrawlerStatusUserInfo(taskInsurance, 404);
		} finally {
			getPensionInfoService.getInfo(taskInsurance);
		}
	}
	
}
