package app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.daqing.InsuranceDaQingMedical;
import com.microservice.dao.entity.crawler.insurance.daqing.InsuranceDaQingUserInfo;
import com.microservice.dao.repository.crawler.insurance.daqing.InsuranceDaQingMedicalRepository;
import com.microservice.dao.repository.crawler.insurance.daqing.InsuranceDaQingUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceDaQingParser;

/**
 * 该网站经常打不开页面，显示维护中，所以开发登陆的时候，要注意这个问题
 */

@Component
@SuppressWarnings("all")
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic",
        "com.microservice.dao.entity.crawler.insurance.daqing" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic",
        "com.microservice.dao.repository.crawler.insurance.daqing" })
public class InsuranceDaQingCrawlerService{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceDaQingParser daQingParser;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceChangeCrawlerStatus changeCrawlerStatus;
	@Autowired
	private InsuranceDaQingUserInfoRepository userInfoRepository;
    @Autowired
    private InsuranceDaQingMedicalRepository medicalRepository;
	
	//爬取个人基本信息以及医疗信息(由于已经存储了相关源码页，此处不再重复保存)
	@Async
	public void getAllInfo(TaskInsurance taskInsurance, String html) {
		//先爬取基本信息
		try {
			InsuranceDaQingUserInfo userInfo = daQingParser.userInfoParser(taskInsurance.getTaskid(),html);
			if(null!=userInfo){
				userInfoRepository.save(userInfo);
				tracer.addTag("个人信息", "爬取成功，已经入库");
				insuranceService.changeCrawlerStatus(
	                		InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
	                        InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("爬取个人信息过程中出现异常", e.toString());
			insuranceService.changeCrawlerStatus(
              		InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
                      InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 201, taskInsurance);
		}
		//再爬取医疗信息
		/*try {
			List<InsuranceDaQingMedical> list = daQingParser.medicalInfoParser(html);
			if(list!=null && list.size()>0){
				medicalRepository.saveAll(list);
				tracer.addTag("医疗保险缴费信息", "爬取成功，已经入库");
				insuranceService.changeCrawlerStatus(
	            		InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
	                    InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);
			}else{
				insuranceService.changeCrawlerStatus(
	            		InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
	                    InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("爬取医疗保险信息过程中出现异常", e.toString());
			insuranceService.changeCrawlerStatus(
            		InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
                    InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
		}*/
		//更新其他保险信息的爬取状态（由于官网一直显示维护中，所以无法爬取五险信息）
		changeCrawlerStatus.changeOtherCrawlerStatusTrue(taskInsurance, 201);
		
		//更新最终的爬取状态
		insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
	}
}
