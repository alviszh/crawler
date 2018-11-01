package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.ningbo.HousingNingboHtml;
import com.microservice.dao.entity.crawler.housing.ningbo.HousingNingboPay;
import com.microservice.dao.entity.crawler.housing.ningbo.HousingNingboUserInfo;
import com.microservice.dao.repository.crawler.housing.ningbo.HousingNingboAccountRepository;
import com.microservice.dao.repository.crawler.housing.ningbo.HousingNingboHtmlRepository;
import com.microservice.dao.repository.crawler.housing.ningbo.HousingNingboUserInfoRepository;

import app.common.WebParam;
import app.parser.HousingFundNingboParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.ningbo")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.ningbo")
public class HousingFundNingboCommonService extends HousingBasicService{

	@Autowired
	private HousingNingboAccountRepository housingNingboAccountRepository;
	@Autowired
	private HousingNingboUserInfoRepository housingNingboUserInfoRepository;
	@Autowired
	private HousingNingboHtmlRepository housingNingboHtmlRepository;
	@Autowired
	private HousingFundNingboParser housingFundNingboParser;
	
	public void loginByIdCard(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing){
		
		try {
			WebParam webParam = housingFundNingboParser.loginByIdCard(messageLoginForHousing,taskHousing);
			if(null != webParam)
			{
				if(null ==webParam.getHtml())
				{
					tracer.addTag("parser.login.ERROR1", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status("FAIL");
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					taskHousing.setPaymentStatus(104);
					save(taskHousing); 
				}	
				else if(webParam.getHtml().contains("验证码")){
					tracer.addTag("parser.login.img", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getPhase());
					taskHousing.setPhase_status("FAIL");
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getDescription());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					taskHousing.setPaymentStatus(102);
					save(taskHousing);
				}
				else if(webParam.getHtml().contains("您输入的身份证号码未在业务系统中开户。请就近咨询公积金网点业务专柜或拔打公积金热线12329"))
				{
					tracer.addTag("parser.login.ERROR.IDCARD", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status("FAIL");
					taskHousing.setDescription("您输入的身份证号码未在业务系统中开户。请就近咨询公积金网点业务专柜或拔打公积金热线12329");
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					taskHousing.setPaymentStatus(103);
					save(taskHousing);
				}
				else if(webParam.getHtml().contains("success"))
				{
					tracer.addTag("parser.login.SUCCESS", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskHousing.setCookies(cookieString);
					taskHousing.setPaymentStatus(200);
					save(taskHousing);
				}
				else
				{
					tracer.addTag("parser.login.ERROR2", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status("FAIL");
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					taskHousing.setPaymentStatus(104);
					save(taskHousing);
				}
			}
			else
			{
				tracer.addTag("parser.login.TimeOUT1", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status("FAIL");
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(104);
				save(taskHousing);
			}
		} catch (Exception e) {
			tracer.addTag("parser.login.TimeOUT2", taskHousing.getTaskid());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status("FAIL");
			taskHousing.setDescription("登陆超时，请核对信息及其网络");
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
			taskHousing.setPaymentStatus(104);
			save(taskHousing);
			e.printStackTrace();
		}
	}

public void loginByHousingFundCard(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing){
		
		try {
			WebParam webParam = housingFundNingboParser.loginByHousingFundCard(messageLoginForHousing,taskHousing);
			if(null != webParam)
			{
				if(null ==webParam.getHtml())
				{
					tracer.addTag("parser.login.ERROR1", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status("FAIL");
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					taskHousing.setPaymentStatus(104);
					save(taskHousing); 
				}	
				else if(webParam.getHtml().contains("验证码")){
					tracer.addTag("parser.login.img", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getPhase());
					taskHousing.setPhase_status("FAIL");
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getDescription());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					taskHousing.setPaymentStatus(102);
					save(taskHousing);
				}
				else if(webParam.getHtml().contains("0"))
				{
					tracer.addTag("parser.login.SUCCESS", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskHousing.setCookies(cookieString);
					taskHousing.setPaymentStatus(200);
					save(taskHousing);
				}
				else{
					tracer.addTag("parser.login.ERROR2", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status("FAIL");
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					taskHousing.setPaymentStatus(104);
					save(taskHousing);
				}
			}
			else
			{
				tracer.addTag("parser.login.TimeOUT1", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status("FAIL");
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(104);
				save(taskHousing);
			}
		} catch (Exception e) {
			tracer.addTag("parser.login.TimeOUT2", taskHousing.getTaskid());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status("FAIL");
			taskHousing.setDescription("网络超时，请核对信息及其网络");
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
			taskHousing.setPaymentStatus(104);
			save(taskHousing);
			e.printStackTrace();
		}
	}
	
	
	//个人信息
	@Async
	public void getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingNingboUserInfo> webParam = housingFundNingboParser.getUserInfo(messageLoginForHousing,taskHousing);
			if(null != webParam.getHousingNingboUserInfo())
			{
				tracer.addTag("parser.getUserInfo.SUCCESS", taskHousing.getTaskid());
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200, taskHousing.getTaskid());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setUserinfoStatus(200);
				HousingNingboHtml housingNingboHtml = new HousingNingboHtml();
				housingNingboHtml.setHtml(webParam.getHtml());
				housingNingboHtml.setPagenumber(1);
				housingNingboHtml.setTaskid(messageLoginForHousing.getTask_id());
				housingNingboHtml.setUrl(webParam.getUrl());
				housingNingboHtmlRepository.save(housingNingboHtml);
				housingNingboUserInfoRepository.save(webParam.getHousingNingboUserInfo());
//				String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
//				taskHousing.setCookies(cookieString);
				updateTaskHousing(taskHousing.getTaskid());
			}
			else
			{
				tracer.addTag("parser.getUserInfo.ERROR", taskHousing.getTaskid());
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 201, taskHousing.getTaskid());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setUserinfoStatus(201);
//				String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
//				taskHousing.setCookies(cookieString);
				updateTaskHousing(taskHousing.getTaskid());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	//公积金
	@Async
	public void getAccount(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingNingboPay> webParam = housingFundNingboParser.getAccount(messageLoginForHousing,taskHousing);
			if(null != webParam.getList())
			{
				tracer.addTag("parser.getAccount.SUCCESS", taskHousing.getTaskid());
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 200, taskHousing.getTaskid());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(200);
				HousingNingboHtml housingNingboHtml = new HousingNingboHtml();
				housingNingboHtml.setHtml(webParam.getHtml());
				housingNingboHtml.setPagenumber(1);
				housingNingboHtml.setTaskid(messageLoginForHousing.getTask_id());
				housingNingboHtml.setUrl(webParam.getUrl());
				housingNingboHtmlRepository.save(housingNingboHtml);
				housingNingboAccountRepository.saveAll(webParam.getList());
//				String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
//				taskHousing.setCookies(cookieString);
				updateTaskHousing(taskHousing.getTaskid());
			}
			else
			{
				tracer.addTag("parser.getAccount.ERROR", taskHousing.getTaskid());
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 201, taskHousing.getTaskid());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(201);
//				String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
//				taskHousing.setCookies(cookieString);
				updateTaskHousing(taskHousing.getTaskid());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
