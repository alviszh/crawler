package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.ganzhou.InsuranceGanZhouHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.ganzhou.InsuranceGanZhouHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.ganzhou.InsuranceGanZhouPersionRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceGanZhouParser;
import app.service.aop.InsuranceCrawler;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.ganzhou"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.ganzhou"})
public class InsuranceGanZhouService implements InsuranceCrawler{
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceGanZhouParser insuranceGanZhouParser;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceGanZhouHtmlRepository insuranceGanZhouHtmlRepository;
	@Autowired
	private InsuranceGanZhouPersionRepository  insuranceGanZhouPersionRepository;
	@Autowired
	private TracerLog tracer;	

	
	/**
	 * @Des 获取养老
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceGanZhouService.persion", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			WebParam webParam = insuranceGanZhouParser.getForPersion(insuranceRequestParameters);
			String html = webParam.getHtml();
			tracer.addTag("InsuranceGanZhouService.persion",
					insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
			InsuranceGanZhouHtml insuranceGanZhouHtml = new InsuranceGanZhouHtml();
			insuranceGanZhouHtml.setPageCount(1);
			insuranceGanZhouHtml.setType("persionInfo");
			insuranceGanZhouHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceGanZhouHtml.setUrl(webParam.getUrl());
			insuranceGanZhouHtml.setHtml(webParam.getHtml());
			insuranceGanZhouHtmlRepository.save(insuranceGanZhouHtml);
			if (webParam.getInsuranceGanZhouPersion() != null) {
				insuranceGanZhouPersionRepository.save(webParam.getInsuranceGanZhouPersion());
				tracer.addTag("getPersionInfo", insuranceRequestParameters.getTaskId() + "养老信息已入库"
						+ webParam.getInsuranceGanZhouPersion().toString());
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
				insuranceService.changeCrawlerStatusSuccess(taskInsurance);
			} else {
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				tracer.addTag("getPersionInfo", insuranceRequestParameters.getTaskId() + "没有当前条件所对应的养老信息");
			}
		} catch (Exception e) {
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 201, taskInsurance);
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			tracer.addTag("getPersionInfo.Exception", e.toString());
			e.printStackTrace();
		}
		return taskInsurance;
	}
	
	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}	
}
