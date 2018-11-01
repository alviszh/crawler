package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;

/**
 * 德州社保爬取Service
 * 
 * @author qizhongbin
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.fuyang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.fuyang" })
public class InsuranceHaErBinService implements InsuranceLogin {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private CrawlerBaseInfoService crawlerBaseInfoService;
	@Autowired
	private InsuranceService insuranceService;
	
	
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance = crawlerBaseInfoService.login(insuranceRequestParameters);
		return taskInsurance;
	}
	public TaskInsurance getAllData(InsuranceRequestParameters parameter) {
		tracer.addTag("InsuranceHaErBinService.crawler:开始执行爬取", parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		// 爬取解析基本信息
		String id = crawlerBaseInfoService.crawlerBaseInfo(parameter);
		System.out.println("id-----------" + id);

		// 爬取解析工伤
		crawlerBaseInfoService.crawlerGongshangInsurance(parameter, id);
		// 爬取解析生育
		crawlerBaseInfoService.crawlerShengyuInsurance(parameter, id);
		// 爬取解析失业
		crawlerBaseInfoService.crawlerShiyeInsurance(parameter, id);

		// 更新最终的状态
		insuranceService.changeCrawlerStatusSuccess(parameter.getTaskId());

		return taskInsurance;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
