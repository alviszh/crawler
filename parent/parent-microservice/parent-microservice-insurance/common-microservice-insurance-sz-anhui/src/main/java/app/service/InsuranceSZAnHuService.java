package app.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sz.anhui.InsuranceSZAnHuiHtml;
import com.microservice.dao.entity.crawler.insurance.sz.anhui.InsuranceSZAnHuiUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.sz.anhui.InsuranceSZAnHuiHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.sz.anhui.InsuranceSZAnHuiUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceSZAnHuiHtmlunit;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sz.anhui"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sz.anhui"})
public class InsuranceSZAnHuService implements InsuranceLogin{
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceSZAnHuiHtmlunit insuranceSZAnHuiHtmlunit;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceSZAnHuiUserInfoRepository insuranceSZAnHuiUserInfoRepository;
	@Autowired
	private InsuranceSZAnHuiHtmlRepository  insuranceSZAnHuiHtmlRepository;
	@Autowired
	private InsuranceSZAnHuUserInfoService  insuranceSZAnHuUserInfoService;
	@Autowired
	private TracerLog tracer;	
	/**
	 * @Des 登录 医疗
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceSZAnHuService.login", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			WebParam webParam = insuranceSZAnHuiHtmlunit.login(insuranceRequestParameters);
			String html = webParam.getHtml();
			tracer.addTag("InsuranceSZAnHuService.login",
					insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
			if (html.contains("社保卡基本信息")) {
				tracer.addTag(insuranceRequestParameters.getTaskId(),"登陆成功");
				String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
				taskInsurance.setCookies(cookies);
				taskInsuranceRepository.save(taskInsurance);
				taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance);
				return taskInsurance;
			}else if(html.contains("用户名或密码错误")){
				tracer.addTag(insuranceRequestParameters.getTaskId(),"用户名或密码错误");
				taskInsurance = insuranceService.changeLoginStatusPwdError(taskInsurance);					
				return taskInsurance;
			}else if(html.contains("用户名或密码错误")){
				tracer.addTag("InsuranceSZAnHuService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");						
				taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
				return taskInsurance;
			}
		} catch (Exception e) {
			tracer.addTag("InsuranceSZAnHuService.login.Exception", e.toString());	
			e.printStackTrace();
		}		
		return taskInsurance;
	}
	/**
	 * @Des 获取个人信息
	 * @param insuranceRequestParameters
	 * @throws Exception 
	 */
	public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("getUserInfo", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		WebParam webParam = insuranceSZAnHuiHtmlunit.getUserInfo(insuranceRequestParameters, cookies);
		String html=webParam.getHtml();
		tracer.addTag("getUserInfo",
				insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
		InsuranceSZAnHuiHtml insuranceSZAnHuiHtml = new InsuranceSZAnHuiHtml();
		insuranceSZAnHuiHtml.setPageCount(1);
		insuranceSZAnHuiHtml.setType("userInfo");
		insuranceSZAnHuiHtml.setTaskid(insuranceRequestParameters.getTaskId());
		insuranceSZAnHuiHtml.setUrl(webParam.getUrl());
		insuranceSZAnHuiHtml.setHtml(html);
		insuranceSZAnHuiHtmlRepository.save(insuranceSZAnHuiHtml);	
		InsuranceSZAnHuiUserInfo  userInfo=webParam.getInsuranceSZAnHuiUserInfo();
		if (null !=userInfo) {
			insuranceSZAnHuiUserInfoRepository.save(userInfo);
			tracer.addTag("getUserinfo", "安徽省直个人信息已入库");
			taskInsurance=insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, taskInsurance);
			insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());
		}else if("请至少完成2个中级认证".contains(webParam.getMsgAlert())){
			tracer.addTag("getUserinfo", "安徽省直个人信息  用户未认证，未爬取到数据");	
			taskInsurance.setUserInfoStatus(201);
			taskInsuranceRepository.save(taskInsurance);	
			insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());
			taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
			taskInsurance.setDescription("用户未认证，未爬取到个人信息");
			taskInsuranceRepository.save(taskInsurance);	
		}else{
			tracer.addTag("getUserinfo", "安徽省直个人信息失败");	
			taskInsurance.setUserInfoStatus(201);
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
			insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());		
		}
	}
	
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance.setShiyeStatus(201);
		taskInsurance.setShengyuStatus(201);
		taskInsurance.setGongshangStatus(201);
		taskInsurance.setYanglaoStatus(201);
		taskInsurance.setYiliaoStatus(201);
		taskInsuranceRepository.save(taskInsurance);
		insuranceSZAnHuUserInfoService.getUserInfo(insuranceRequestParameters);
		taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		return taskInsurance;
	}
	@Override
	public TaskInsurance getAllDataDone(String taskId) {	
		return null;
	}	
}
