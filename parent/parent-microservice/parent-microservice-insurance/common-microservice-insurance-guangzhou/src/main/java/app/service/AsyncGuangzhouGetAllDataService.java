package app.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.guangzhou.InsuranceGuangzhouHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.guangzhou.InsuranceGuangzhouHtmlRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceGuangzhouParser;
import app.service.aop.InsuranceCrawler;
import app.service.aop.InsuranceLogin;
import app.service.aop.InsuranceSms;


@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.guangzhou"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.guangzhou"})
public class AsyncGuangzhouGetAllDataService implements InsuranceLogin, InsuranceSms, InsuranceCrawler{

	@Autowired
	private InsuranceGuangzhouHtmlRepository insuranceGuangzhouHtmlRepository;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceGuangzhouService insuranceGuangzhouService;
	@Autowired
	private InsuranceGuangzhouParser insuranceGuangzhouParser;
	@Autowired
	private TracerLog tracer;
	
	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 */
	@Override
	@Async
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("taskid", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		if(null != taskInsurance){
			
			for (int i = 0; i < 3; i++) {
				taskInsurance = loginReal(insuranceRequestParameters, taskInsurance);
				if(taskInsurance.getDescription().equals("图片验证码有误！")){
					tracer.addTag("Error==>", "图片验证码有误！"+i);
					if(i == 2){
						tracer.addTag("Error==>", "验证码错误三次");
						insuranceService.changeLoginStatusCaptError(taskInsurance);
					}
				}else{
					break;
				}
			}
			
		}
		return taskInsurance;
	}
	
	/**
	 * @Des 登录方法
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception 
	 */
	public TaskInsurance loginReal(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance){
		try {
			WebParam webParam = insuranceGuangzhouParser.login(insuranceRequestParameters);
			if(null == webParam){
				tracer.addTag("parser.login.Error", "登录页获取超时！");
				taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
			}else{
				tracer.addTag("parser.login.webParam", webParam.toString());
				
				InsuranceGuangzhouHtml guangzhouHtml = new InsuranceGuangzhouHtml();
				guangzhouHtml.setTaskid(taskInsurance.getTaskid());
				guangzhouHtml.setUrl(webParam.getUrl());
				guangzhouHtml.setType("login");
				guangzhouHtml.setPageCount(1);
				guangzhouHtml.setHtml(webParam.getPage().asXml());
				insuranceGuangzhouHtmlRepository.save(guangzhouHtml);
				
				String html = webParam.getPage().asXml();
				tracer.addTag("parser.login.html", html);
				System.out.println(html);
				Document obj = Jsoup.parse(html);
				Elements username = obj.getElementsByAttributeValue("color", "blue");
				Element errors = obj.getElementById("*.errors");
				if(null != username && !username.text().trim().equals("")){
					taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance,webParam.getPage());
					tracer.addTag("parser.login.taskInsurance", taskInsurance.toString());
					//不需要发送短信，可以直接采集数据
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_HAVESMSCODE_NO.getDescription());
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_HAVESMSCODE_NO.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_HAVESMSCODE_NO.getPhasestatus());
					taskInsurance = taskInsuranceRepository.save(taskInsurance);
				}else if(html.contains("您当前可能存在异地登录风险，为确保安全，请输入手机验证码")){
					tracer.addTag("parser.login.haveSMSValidate", "需要短信验证");
					taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance,webParam.getPage());
					//需要发送短信
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_HAVESMSCODE_YES.getDescription());
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_HAVESMSCODE_YES.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_HAVESMSCODE_YES.getPhasestatus());
					taskInsurance = taskInsuranceRepository.save(taskInsurance);
				}else if(null != errors && !errors.text().trim().equals("")){
					tracer.addTag("parser.login.fail", errors.text());
					String error = errors.text();
					//账号或密码有误
					if(error.contains("The credentials you provided cannot be determined to be authentic.")){
						taskInsurance = insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
					}
					//账号或密码有误
					if(error.contains("error.authentication.systemerror")){
						taskInsurance = insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
					}
					//验证码错误
					if(error.contains("error.authentication.vialidatecode.bad")){
						taskInsurance = insuranceService.changeLoginStatusCaptError(taskInsurance);
					}
				}else{
					tracer.addTag("parser.login.fail2", "登录异常");
					taskInsurance = insuranceService.changeLoginStatusException(taskInsurance);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.login.Exception", e.toString());
			taskInsurance = insuranceService.changeLoginStatusIdnumError(taskInsurance);
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
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.addTag("parser.crawler.getAllData", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		insuranceGuangzhouService.getUserInfo(taskInsurance);
		insuranceGuangzhouService.getMedicalInfo(taskInsurance);
		insuranceGuangzhouService.getGeneralInfo(taskInsurance);
		return taskInsurance;
	}

	@Override
	@Async
	public TaskInsurance sendSms(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("parser.sendSMS.taskid", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			WebParam webParam = insuranceGuangzhouParser.sendSMS(taskInsurance);
			InsuranceGuangzhouHtml guangzhouHtml = new InsuranceGuangzhouHtml();
			guangzhouHtml.setTaskid(taskInsurance.getTaskid());
			guangzhouHtml.setUrl(webParam.getUrl());
			guangzhouHtml.setType("sendSMS");
			guangzhouHtml.setPageCount(1);
			guangzhouHtml.setHtml(webParam.getPage2().getWebResponse().getContentAsString());
			insuranceGuangzhouHtmlRepository.save(guangzhouHtml);
			
			if(null != webParam.getCode()){
				tracer.addTag("parser.sendSMS.service.success", "验证码发送成功！");
				insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_SUCCESS.getPhase(),
						InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_SUCCESS.getPhasestatus(),
						InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_SUCCESS.getDescription(), taskInsurance);
			}else{
				tracer.addTag("parser.sendSMS.service.success", "验证码发送失败！");
				insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_FAILUE.getPhase(),
						InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_FAILUE.getPhasestatus(),
						InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_FAILUE.getDescription(), taskInsurance);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.sendSMS.Exception", e.toString());
			insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_FAILUE.getPhase(),
					InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_FAILUE.getPhasestatus(),
					InsuranceStatusCode.INSURANCE_LOGIN_VALIDATE_FAILUE.getDescription(), taskInsurance);
		}
		return taskInsurance;
	}

	@Override
	@Async
	public TaskInsurance verifySms(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("crawler.checkSMS.service.taskid", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			WebParam webParam = insuranceGuangzhouParser.checkSMS(insuranceRequestParameters, taskInsurance);
			InsuranceGuangzhouHtml guangzhouHtml = new InsuranceGuangzhouHtml();
			guangzhouHtml.setTaskid(taskInsurance.getTaskid());
			guangzhouHtml.setUrl(webParam.getUrl());
			guangzhouHtml.setType("checkSMS");
			guangzhouHtml.setPageCount(1);
			guangzhouHtml.setHtml(webParam.getPage().getWebResponse().getContentAsString());
			insuranceGuangzhouHtmlRepository.save(guangzhouHtml);
			
			if(null != webParam.getHtml()){
				tracer.addTag("crawler.checkSMS.service.fail", "短信验证码错误。");
				insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_SMS_VALIDATE_FAILUE.getPhase(),
						InsuranceStatusCode.INSURANCE_SMS_VALIDATE_FAILUE.getPhasestatus(),
						InsuranceStatusCode.INSURANCE_SMS_VALIDATE_FAILUE.getDescription(), taskInsurance);
			}else{
				tracer.addTag("crawler.checkSMS.service.fail", "短信验证码错误。");
				insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_SMS_VALIDATE_SUCCESS.getPhase(),
						InsuranceStatusCode.INSURANCE_SMS_VALIDATE_SUCCESS.getPhasestatus(),
						InsuranceStatusCode.INSURANCE_SMS_VALIDATE_SUCCESS.getDescription(), taskInsurance);
				String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
				taskInsurance.setCookies(cookies);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.checkSMS.service.Exception", e.toString());
			insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_SMS_VALIDATE_FAILUE.getPhase(),
					InsuranceStatusCode.INSURANCE_SMS_VALIDATE_FAILUE.getPhasestatus(),
					InsuranceStatusCode.INSURANCE_SMS_VALIDATE_FAILUE.getDescription(), taskInsurance);
		}
		return taskInsurance;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
