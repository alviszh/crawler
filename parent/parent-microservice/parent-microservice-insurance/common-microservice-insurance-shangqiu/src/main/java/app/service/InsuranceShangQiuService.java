package app.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;


@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.shangqiu"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.shangqiu"})
public class InsuranceShangQiuService extends InsuranceNanYangLoginTemplateService  implements InsuranceLogin{
	public static final Logger log = LoggerFactory.getLogger(InsuranceShangQiuService.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceShangQiuCrawlerService insuranceShangQiuCrawlerService;
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance = commonLogin(insuranceRequestParameters, taskInsurance);
		return taskInsurance;
	}
	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		Map<String, Future<String>> listfuture = new HashMap<>();   //判断异步爬取是否完成
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId().trim());
		try {
			Future<String> future=insuranceShangQiuCrawlerService.getUserInfo(taskInsurance);
			listfuture.put("getUserInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
		}
		try {
			Future<String> future=insuranceShangQiuCrawlerService.getPension(taskInsurance);
			listfuture.put("getPension", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getPension.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
//		最终状态的更新
		try {
			while (true) {
				for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
//					 判断是否执行完毕
					if (entry.getValue().isDone()) { 
						tracer.addTag(taskInsurance.getTaskid() + entry.getKey() + "---get", entry.getValue().get());
						tracer.addTag(taskInsurance.getTaskid() + entry.getKey() + "---isDone", entry.getValue().get());
						listfuture.remove(entry.getKey());
						break;
					}
				}
				if (listfuture.size() == 0) {
					break;
				}
			}
			changeOtherCrawlerStatusTrue(taskInsurance);
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		} catch (Exception e) {
			System.out.println("更新最终状态时候出现了异常："+e.toString());
			tracer.addTag("listfuture--ERROR", taskInsurance.getTaskid() + "---ERROR:" + e);
			changeOtherCrawlerStatusTrue(taskInsurance);
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
		return taskInsurance;
	}
	
	//由于商丘社保只能够爬取养老保险信息，故决定将这种信息爬取完成后，也更新其他类型保险的状态码
	public void changeOtherCrawlerStatusTrue(TaskInsurance taskInsurance){
		// 生育
		insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 201,
				taskInsurance);
		// 工伤
		insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 201,
				taskInsurance);
		// 失业
		insuranceService.changeCrawlerStatus(
				InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 201,
				taskInsurance);
		//医疗
		insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
				InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
				 201, taskInsurance);

	}
	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
