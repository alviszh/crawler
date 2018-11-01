package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.foshan.InsuranceFoshanHtml;
import com.microservice.dao.entity.crawler.insurance.foshan.InsuranceFoshanUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.foshan.InsuranceFoshanHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.foshan.InsuranceFoshanUserInfoRepository;

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
public class GetUserInfoService {
	
	@Autowired
	private InsuranceFoshanUserInfoRepository insuranceFoshanUserInfoRepository;
	@Autowired
	private InsuranceFoshanHtmlRepository insuranceFoshanHtmlRepository;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceFoshanParser insuranceFoshanParser;
	@Autowired
	private GetPensionInfoService getPensionInfoService;
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
			WebParam<InsuranceFoshanUserInfo> webParam = insuranceFoshanParser.getUserInfo(taskInsurance);
			if(webParam.getCode() == 100){
				insuranceService.changeCrawlerStatus("没有登录或者连结超时,无法获取数据", "CRAWLER_USER_MSG", 100, taskInsurance);
				tracer.addTag("parser.crawler.getUserinfo", "没有登录或者连结超时,无法获取数据");
			}
			if(null != webParam.getPage()){
				InsuranceFoshanHtml insuranceFoshanHtml = new InsuranceFoshanHtml();
				insuranceFoshanHtml.setTaskid(taskInsurance.getTaskid());
				insuranceFoshanHtml.setType("userInfo");
				insuranceFoshanHtml.setPageCount(1);
				insuranceFoshanHtml.setUrl(webParam.getUrl());
				insuranceFoshanHtml.setHtml(webParam.getPage().asXml());
				
				insuranceFoshanHtmlRepository.save(insuranceFoshanHtml);
				tracer.addTag("parser.crawler.getUserinfo.html", "个人信息页面已入库");
			}
			if(null != webParam.getList()){
				insuranceFoshanUserInfoRepository.saveAll(webParam.getList());
				tracer.addTag("parser.crawler.getUserinfo", "个人信息已入库");
				
				insuranceService.changeCrawlerStatusUserInfo(taskInsurance, 200);
			}else{
				insuranceService.changeCrawlerStatusUserInfo(taskInsurance, 201);
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
