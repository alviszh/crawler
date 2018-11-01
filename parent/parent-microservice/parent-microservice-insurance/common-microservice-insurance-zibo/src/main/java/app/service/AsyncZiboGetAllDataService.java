package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.zibo.InsuranceZiboHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.zibo.InsuranceZiboHtmlRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceZiboParser;
import app.service.aop.InsuranceCrawler;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.zibo"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.zibo"})
public class AsyncZiboGetAllDataService implements InsuranceLogin, InsuranceCrawler{

	@Autowired
	private InsuranceZiboHtmlRepository insuranceZiboHtmlRepository;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceZiboParser insuranceZiboParser;
	@Autowired
	private GetUserInfoService getUserInfoService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	
	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 */
	@Override
	@Async
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters){
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		
		if(null != taskInsurance){

			WebParam webParam = null;
			try {
				webParam = insuranceZiboParser.login(insuranceRequestParameters);
			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("login.Exception ==>", e.getMessage());
			}
			if(null == webParam){
				tracer.addTag("ERROR ==>","登录页获取超时！");
				insuranceService.changeLoginStatusTimeOut(taskInsurance);			
			}else{
				HtmlPage page = webParam.getPage();

				String html = page.asXml();
				if(html.contains("您输入的密码不正确!")){
					insuranceService.changeLoginStatusPwdError(taskInsurance);
					tracer.addTag("ERROR ==>","您输入的密码不正确!");
				}else if(html.contains("查不到用户信息，请先注册。")){
					insuranceService.changeLoginStatusIdnumError(taskInsurance);
					tracer.addTag("ERROR ==>","查不到用户信息，请先注册。");
				}else if(html.contains("验证码不正确")){
					insuranceService.changeLoginStatusCaptError(taskInsurance);
					tracer.addTag("ERROR ==>","验证码不正确");
				}else if(!html.contains("dw-laneContainer-mainLane")){
					insuranceService.changeLoginStatusCaptError(taskInsurance);
					tracer.addTag("ERROR ==>","验证码不正确2");
				}else{
					insuranceService.changeLoginStatusSuccess(taskInsurance,page);
					tracer.addTag("parser.login.taskInsurance", taskInsurance.toString());
					InsuranceZiboHtml insuranceZiboHtml = new InsuranceZiboHtml();
					insuranceZiboHtml.setTaskid(taskInsurance.getTaskid());
					insuranceZiboHtml.setPageCount(1);
					insuranceZiboHtml.setType("logined");
					insuranceZiboHtml.setUrl(webParam.getUrl());
					insuranceZiboHtml.setHtml(html);
					insuranceZiboHtmlRepository.save(insuranceZiboHtml);
					tracer.addTag("parser.login.save-insuranceZiboHtml", insuranceZiboHtml.toString());
				}
					
			}
						
		}
		return taskInsurance;
	}
	
	
	/**
	 * @Des 更新taskInsurance
	 * @param insuranceRequestParameters
	 */
	public TaskInsurance updateTaskInsurance(InsuranceRequestParameters insuranceRequestParameters) {	
		TaskInsurance taskInsurance = insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
		return taskInsurance;
	}
	
	
	/**
	 * @Des 爬取总接口
	 * @param insuranceRequestParameters
	 * @throws Exception 
	 */
	@Override
	@Async
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("parser.crawler.getAllData", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		//爬取个人信息
		try {
			tracer.addTag("parser.crawler.getUserinfo", taskInsurance.getTaskid());
			getUserInfoService.getUserInfo(taskInsurance);
		} catch (Exception e) {
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
