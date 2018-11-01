package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.xuzhou.InsuranceXuzhouHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.xuzhou.HtmlInsuranceXuzhouRepository;
import com.microservice.dao.repository.crawler.insurance.xuzhou.InsuranceXuzhouEndowmentAccountRepository;
import com.microservice.dao.repository.crawler.insurance.xuzhou.InsuranceXuzhouMedicalAccountRepository;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.parser.InsuranceXuzhouParser;


@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.xuzhou" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.xuzhou" })
public class InsuranceXuzhouService {

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceXuzhouParser insuranceXuzhouParser;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceXuzhouEndowmentAccountRepository xuzhouEndowmentAccountRepository;
	@Autowired
	private InsuranceXuzhouMedicalAccountRepository xuzhouMedicalAccountRepository;
	@Autowired
	private HtmlInsuranceXuzhouRepository  xuzhouHtmlRepository;
	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Async
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters, Integer count)
			throws Exception {

		tracer.addTag("InsuranceChengduService.login", insuranceRequestParameters.getTaskId());

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());


		return null;
	}

	/**
	 * 更新task表（doing 正在登录状态）
	 * 
	 * @param insuranceRequestParameters
	 * @return
	 */
	public TaskInsurance changeStatus(InsuranceRequestParameters insuranceRequestParameters) {

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);
		return taskInsurance;
	}

	/**
	 * @Des 更新task表（doing 正在采集）
	 * @param insuranceRequestParameters
	 */
	public TaskInsurance updateTaskInsurance(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
		return taskInsurance;
	}
	
//	@Async
//	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters)throws Exception {
//		tracer.addTag("InsuranceXuzhouService.login", insuranceRequestParameters.getTaskId());
//		//先通过taskid在数据控充查询当次任务
//		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//		if(null != taskInsurance){
//			WebParam webParam = insuranceXuzhouParser.login(insuranceRequestParameters);
//			
//			if(null == webParam){
//				tracer.addTag("InsuranceChangchunService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
//				taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
//				return taskInsurance;			
//			}else{
//				String html = webParam.getPage().getWebResponse().getContentAsString();
//				tracer.addTag("InsuranceChangchunService.login",
//						insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
//			}
//		}
//		return taskInsurance;		
//	}

	@Async
	public void getEndowmentAccount(InsuranceRequestParameters insuranceRequestParameters)throws Exception  {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		
		WebParam webParam = insuranceXuzhouParser.getEndowmentAccount(insuranceRequestParameters,taskInsurance);
		if(null!=webParam){
			xuzhouEndowmentAccountRepository.save(webParam.getXuzhouEndowmentInfo());
			insuranceService.changeCrawlerStatus("数据采集中，【养老保险个人账户信息】已采集完成", "CRAWLER_YANGLAO_MSG", 200, taskInsurance);
			tracer.addTag("InsuranceXuzhouService.getEndowmentAccount 养老保险个人账户信息", "养老保险个人账户信息已入库!");
			tracer.addTag("InsuranceXuzhouService.getEndowmentAccount:SUCCESS", insuranceRequestParameters.getTaskId());
		
			InsuranceXuzhouHtml insuranceXuzhouHtml = new InsuranceXuzhouHtml();
			insuranceXuzhouHtml.setDescription("endowmentAccountInfo");
			insuranceXuzhouHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceXuzhouHtml.setUrl(webParam.getUrl());
			insuranceXuzhouHtml.setHtml(webParam.getHtml());
			xuzhouHtmlRepository.save(insuranceXuzhouHtml);
			tracer.addTag("InsuranceXuzhouService.getEndowmentAccount","养老账户信息源码表入库!");
			insuranceService.changeCrawlerStatus("数据采集中，【医疗保险信息】已采集完成", "CRAWLER_YANGLAO_MSG", 200, taskInsurance);
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}else{
			tracer.addTag("InsuranceXuzhouService.getEndowmentAccount","认证失败，请确认身份证号码和个人代码");
			tracer.addTag("InsuranceXuzhouService.getEndowmentAccount:养老保险认证失败", insuranceRequestParameters.getTaskId());
		}
	}
	
	
	@Async
	public void getMedicalAccount(InsuranceRequestParameters insuranceRequestParameters)throws Exception {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		
		WebParam webParam = insuranceXuzhouParser.getMedicalAccount(insuranceRequestParameters,taskInsurance);
	
		if(null !=webParam){
			xuzhouMedicalAccountRepository.save(webParam.getXuzhouMedicalInfo());
			insuranceService.changeCrawlerStatus("数据采集中，【医疗保险个人账户信息】已采集完成", "CRAWLER_YILIAO_MSG", 200, taskInsurance);
			tracer.addTag("InsuranceXuzhouService.getMedicalAccount 医疗保险个人账户信息", "医疗保险个人账户信息已入库!");
			tracer.addTag("InsuranceXuzhouService.getMedicalAccount:SUCCESS", insuranceRequestParameters.getTaskId());
			
			InsuranceXuzhouHtml insuranceXuzhouHtml = new InsuranceXuzhouHtml();
			insuranceXuzhouHtml.setDescription("medicalAccountInfo");
			insuranceXuzhouHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceXuzhouHtml.setUrl(webParam.getUrl());
			insuranceXuzhouHtml.setHtml(webParam.getHtml());
			xuzhouHtmlRepository.save(insuranceXuzhouHtml);
			tracer.addTag("InsuranceXuzhouService.getMedicalAccount","医疗账户信息源码表入库!");
			insuranceService.changeCrawlerStatus("数据采集中，【医疗保险信息】已采集完成", "CRAWLER_YILIAO_MSG", 200, taskInsurance);
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}else{
			tracer.addTag("InsuranceXuzhouService.getMedicalAccount","认证失败，请确认身份证号码和个人代码");
			tracer.addTag("InsuranceXuzhouService.getMedicalAccount:医疗保险认证失败", insuranceRequestParameters.getTaskId());
		}
	}


}
