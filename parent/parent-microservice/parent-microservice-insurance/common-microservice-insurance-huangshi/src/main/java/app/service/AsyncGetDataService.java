package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.huangshi.InsuranceHuangShiHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.huangshi.InsuranceHuangShiHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.huangshi.InsuranceHuangShiPayinfoRepository;
import com.microservice.dao.repository.crawler.insurance.huangshi.InsuranceHuangShiUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceHuangShiParser;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.huangshi"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.huangshi"})
public class AsyncGetDataService {

	@Autowired
	private InsuranceHuangShiUserInfoRepository insuranceHuangShiUserInfoRepository;
	@Autowired
	private InsuranceHuangShiPayinfoRepository insuranceHuangShiPayinfoRepository;
	@Autowired
	private InsuranceHuangShiHtmlRepository insuranceHuangShiHtmlRepository;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceHuangShiParser insuranceHuangShiParser;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	
	@Async
	public void getUserInfo(TaskInsurance taskInsurance) {
		tracer.addTag("crawler.service.AsyncGetDataService.getUserInfo.taskid", taskInsurance.getTaskid());
		try {
			WebParam webParam = insuranceHuangShiParser.getUserInfo(taskInsurance);
			InsuranceHuangShiHtml html = new InsuranceHuangShiHtml();
			html.setTaskid(taskInsurance.getTaskid());
			html.setUrl(webParam.getUrl());
			html.setPageCount(1);
			html.setType("userInfo");
			html.setHtml(webParam.getHtml());
			insuranceHuangShiHtmlRepository.save(html);
			if(null != webParam.getList()){
				insuranceHuangShiUserInfoRepository.saveAll(webParam.getList());
				tracer.addTag("crawler.service.AsyncGetDataService.getUserInfo.success", "用户信息数据采集成功！");
				insuranceService.changeCrawlerStatus("用户信息采集成功！", "CRAWLER_USER_MSG", 200, taskInsurance);
			}else{
				tracer.addTag("crawler.service.AsyncGetDataService.getUserInfo.fail", "用户信息数据采集失败，无数据！");
				insuranceService.changeCrawlerStatus("用户信息采集完成！", "CRAWLER_USER_MSG", 201, taskInsurance);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.service.AsyncGetDataService.getUserInfo.Exception", e.toString());
			insuranceService.changeCrawlerStatus("用户信息采集完成！", "CRAWLER_USER_MSG", 404, taskInsurance);
		}
		insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
	}
	
	@Async
	public void getPensionInfo(TaskInsurance taskInsurance) {
		tracer.addTag("crawler.service.AsyncGetDataService.getPensionInfo.taskid", taskInsurance.getTaskid());
		try {
			WebParam webParam = insuranceHuangShiParser.getPensionInfo(taskInsurance);
			InsuranceHuangShiHtml html = new InsuranceHuangShiHtml();
			html.setTaskid(taskInsurance.getTaskid());
			html.setUrl(webParam.getUrl());
			html.setPageCount(1);
			html.setType("pension");
			html.setHtml(webParam.getHtml());
			insuranceHuangShiHtmlRepository.save(html);
			if(null != webParam.getList() && webParam.getList().size() > 0){
				insuranceHuangShiPayinfoRepository.saveAll(webParam.getList());
				tracer.addTag("crawler.service.AsyncGetDataService.getPensionInfo.success", "养老信息数据采集成功！");
				insuranceService.changeCrawlerStatus("养老信息采集成功！", "CRAWLER_YANGLAO_MSG", 200, taskInsurance);
			}else{
				tracer.addTag("crawler.service.AsyncGetDataService.getPensionInfo.fail", "养老信息数据采集失败，无数据！");
				insuranceService.changeCrawlerStatus("养老信息采集完成！", "CRAWLER_YANGLAO_MSG", 201, taskInsurance);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.service.AsyncGetDataService.getPensionInfo.Exception", e.toString());
			insuranceService.changeCrawlerStatus("养老信息采集完成！", "CRAWLER_YANGLAO_MSG", 404, taskInsurance);
		}
		insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
	}

	@Async
	public void getMedicalInfo(TaskInsurance taskInsurance) {
		tracer.addTag("crawler.service.AsyncGetDataService.getMedicalInfo.taskid", taskInsurance.getTaskid());
		try {
			WebParam webParam = insuranceHuangShiParser.getMedicalInfo(taskInsurance);
			InsuranceHuangShiHtml html = new InsuranceHuangShiHtml();
			html.setTaskid(taskInsurance.getTaskid());
			html.setUrl(webParam.getUrl());
			html.setPageCount(1);
			html.setType("medical");
			html.setHtml(webParam.getHtml());
			insuranceHuangShiHtmlRepository.save(html);
			if(null != webParam.getList() && webParam.getList().size() > 0){
				insuranceHuangShiPayinfoRepository.saveAll(webParam.getList());
				tracer.addTag("crawler.service.AsyncGetDataService.getMedicalInfo.success", "医疗信息数据采集成功！");
				insuranceService.changeCrawlerStatus("医疗信息采集成功！", "CRAWLER_YILIAO_MSG", 200, taskInsurance);
			}else{
				tracer.addTag("crawler.service.AsyncGetDataService.getMedicalInfo.fail", "医疗信息数据采集失败，无数据！");
				insuranceService.changeCrawlerStatus("医疗信息采集完成！", "CRAWLER_YILIAO_MSG", 201, taskInsurance);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.service.AsyncGetDataService.getMedicalInfo.Exception", e.toString());
			insuranceService.changeCrawlerStatus("医疗信息采集完成！", "CRAWLER_YILIAO_MSG", 404, taskInsurance);
		}
		insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
	}

	@Async
	public void getBearInfo(TaskInsurance taskInsurance) {
		tracer.addTag("crawler.service.AsyncGetDataService.getBearInfo.taskid", taskInsurance.getTaskid());
		try {
			WebParam webParam = insuranceHuangShiParser.getBearInfo(taskInsurance);
			InsuranceHuangShiHtml html = new InsuranceHuangShiHtml();
			html.setTaskid(taskInsurance.getTaskid());
			html.setUrl(webParam.getUrl());
			html.setPageCount(1);
			html.setType("bear");
			html.setHtml(webParam.getHtml());
			insuranceHuangShiHtmlRepository.save(html);
			if(null != webParam.getList() && webParam.getList().size() > 0){
				insuranceHuangShiPayinfoRepository.saveAll(webParam.getList());
				tracer.addTag("crawler.service.AsyncGetDataService.getBearInfo.success", "生育信息数据采集成功！");
				insuranceService.changeCrawlerStatus("生育信息采集成功！", "CRAWLER_SHENGYU_MSG", 200, taskInsurance);
			}else{
				tracer.addTag("crawler.service.AsyncGetDataService.getBearInfo.fail", "生育信息数据采集失败，无数据！");
				insuranceService.changeCrawlerStatus("生育信息采集完成！", "CRAWLER_SHENGYU_MSG", 201, taskInsurance);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.service.AsyncGetDataService.getBearInfo.Exception", e.toString());
			insuranceService.changeCrawlerStatus("生育信息采集完成！", "CRAWLER_SHENGYU_MSG", 404, taskInsurance);
		}
		insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
	}
	
	@Async
	public void getInjuryInfo(TaskInsurance taskInsurance) {
		tracer.addTag("crawler.service.AsyncGetDataService.getInjuryInfo.taskid", taskInsurance.getTaskid());
		try {
			WebParam webParam = insuranceHuangShiParser.getInjuryInfo(taskInsurance);
			InsuranceHuangShiHtml html = new InsuranceHuangShiHtml();
			html.setTaskid(taskInsurance.getTaskid());
			html.setUrl(webParam.getUrl());
			html.setPageCount(1);
			html.setType("injury");
			html.setHtml(webParam.getHtml());
			insuranceHuangShiHtmlRepository.save(html);
			if(null != webParam.getList() && webParam.getList().size() > 0){
				insuranceHuangShiPayinfoRepository.saveAll(webParam.getList());
				tracer.addTag("crawler.service.AsyncGetDataService.getInjuryInfo.success", "工伤信息数据采集成功！");
				insuranceService.changeCrawlerStatus("工伤信息采集成功！", "CRAWLER_GONGSHANG_MSG", 200, taskInsurance);
			}else{
				tracer.addTag("crawler.service.AsyncGetDataService.getInjuryInfo.fail", "工伤信息数据采集失败，无数据！");
				insuranceService.changeCrawlerStatus("工伤信息采集完成！", "CRAWLER_GONGSHANG_MSG", 201, taskInsurance);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.service.AsyncGetDataService.getInjuryInfo.Exception", e.toString());
			insuranceService.changeCrawlerStatus("工伤信息采集完成！", "CRAWLER_GONGSHANG_MSG", 404, taskInsurance);
		}
		insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
	}

	@Async
	public void getUnemploymentInfo(TaskInsurance taskInsurance) {
		tracer.addTag("crawler.service.AsyncGetDataService.getUnemploymentInfo.taskid", taskInsurance.getTaskid());
		try {
			WebParam webParam = insuranceHuangShiParser.getUnemploymentInfo(taskInsurance);
			InsuranceHuangShiHtml html = new InsuranceHuangShiHtml();
			html.setTaskid(taskInsurance.getTaskid());
			html.setUrl(webParam.getUrl());
			html.setPageCount(1);
			html.setType("unemployment");
			html.setHtml(webParam.getHtml());
			insuranceHuangShiHtmlRepository.save(html);
			if(null != webParam.getList() && webParam.getList().size() > 0){
				insuranceHuangShiPayinfoRepository.saveAll(webParam.getList());
				tracer.addTag("crawler.service.AsyncGetDataService.getUnemploymentInfo.success", "失业信息数据采集成功！");
				insuranceService.changeCrawlerStatus("失业信息采集成功！", "CRAWLER_SHIYE_MSG", 200, taskInsurance);
			}else{
				tracer.addTag("crawler.service.AsyncGetDataService.getUnemploymentInfo.fail", "失业信息数据采集失败，无数据！");
				insuranceService.changeCrawlerStatus("失业信息采集完成！", "CRAWLER_SHIYE_MSG", 201, taskInsurance);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.service.AsyncGetDataService.getUnemploymentInfo.Exception", e.toString());
			insuranceService.changeCrawlerStatus("失业信息采集完成！", "CRAWLER_SHENGYU_MSG", 404, taskInsurance);
		}
		insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
	}

}
