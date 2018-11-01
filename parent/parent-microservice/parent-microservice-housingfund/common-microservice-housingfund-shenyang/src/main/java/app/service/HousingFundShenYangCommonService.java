package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.shenyang.HousingShenYangHtml;
import com.microservice.dao.entity.crawler.housing.shenyang.HousingShenYangPay;
import com.microservice.dao.entity.crawler.housing.shenyang.HousingShenYangUserInfo;
import com.microservice.dao.repository.crawler.housing.shenyang.HousingShenYangHtmlRepository;
import com.microservice.dao.repository.crawler.housing.shenyang.HousingShenYangPayRepository;
import com.microservice.dao.repository.crawler.housing.shenyang.HousingShenYangUserInfoRepository;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.HousingFundShenYangParser;
import app.service.common.HousingBasicService;
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.shenyang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.shenyang")
public class HousingFundShenYangCommonService extends HousingBasicService{

	@Autowired
	private HousingFundShenYangParser housingFundShenYangParser;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingShenYangHtmlRepository housingShenYangHtmlRepository;
	@Autowired 
	private HousingShenYangUserInfoRepository housingShenYangUserInfoRepository;
	@Autowired
	private HousingShenYangPayRepository housingShenYangPayRepository;
	//登陆
	public void login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam webParam = housingFundShenYangParser.login(messageLoginForHousing,taskHousing);
			if(null != webParam)
			{
				 if(webParam.getHtml().contains("listtable"))
				{
					tracer.addTag("parser.login.SUCCESS", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskHousing.setCookies(cookieString);
					taskHousing.setError_message(webParam.getUrl());//验证码
					save(taskHousing);
				}
				 else if(webParam.getHtml().contains("登陆失败"))
					{
						tracer.addTag("parser.login.ERROR.pwd", taskHousing.getTaskid());
						taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
						taskHousing.setPhase_status("FAIL");
						taskHousing.setDescription("登陆失败,密码或者卡号错误，请您重新输入");
						taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
						taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
						save(taskHousing);
					}
				 else if(webParam.getHtml().contains("验证码错误"))
				{
					tracer.addTag("parser.login.ERROR.img", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getPhase());
					taskHousing.setPhase_status("FAIL");
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getDescription());
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getError_code());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					save(taskHousing);
				}
				
				
			}
			else
			{
				tracer.addTag("parser.login.ERROR", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status("FAIL");
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				save(taskHousing);
			}
		} catch (Exception e) {
			tracer.addTag("parser.login.TIMEOUT", taskHousing.getTaskid());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status("FAIL");
			taskHousing.setDescription("登陆超时，请重新登陆");
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
			save(taskHousing);
			e.printStackTrace();
		}
	}
	
	//个人信息
	public void getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingShenYangUserInfo> webParam = housingFundShenYangParser.getUserInfo(messageLoginForHousing,taskHousing);
			if(null != webParam.getHousingShenYangUserInfo())
			{
				tracer.addTag("parser.getUserInfo.SUCCESS", taskHousing.getTaskid());
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200, taskHousing.getTaskid());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setUserinfoStatus(200);
				HousingShenYangHtml housingShenYangHtml = new HousingShenYangHtml();
				housingShenYangHtml.setHtml(webParam.getHtml());
				housingShenYangHtml.setPagenumber(1);
				housingShenYangHtml.setTaskid(taskHousing.getTaskid());
				housingShenYangHtml.setUrl(webParam.getUrl());
				housingShenYangHtmlRepository.save(housingShenYangHtml);
				housingShenYangUserInfoRepository.save(webParam.getHousingShenYangUserInfo());
				String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
				taskHousing.setCookies(cookieString);
				updateTaskHousing(taskHousing.getTaskid()); 
			}
			else
			{
				tracer.addTag("parser.getUserInfo.ERROR1", taskHousing.getTaskid());
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 201, taskHousing.getTaskid());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setUserinfoStatus(201);
				updateTaskHousing(taskHousing.getTaskid()); 
			}
		} catch (Exception e) {
			tracer.addTag("parser.getUserInfo.ERROR2", taskHousing.getTaskid());
			updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 201, taskHousing.getTaskid());
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
			taskHousing.setUserinfoStatus(201);
			updateTaskHousing(taskHousing.getTaskid()); 
			e.printStackTrace();
			
		}
		
	}
	
	//流水
	public void getAccount(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingShenYangPay> webParam = housingFundShenYangParser.getAccount(messageLoginForHousing,taskHousing);
			if(null != webParam.getList())
			{
				tracer.addTag("parser.getAccount.SUCCESS", taskHousing.getTaskid());
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 200, taskHousing.getTaskid());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(200);
				HousingShenYangHtml housingShenYangHtml = new HousingShenYangHtml();
				housingShenYangHtml.setHtml(webParam.getHtml());
				housingShenYangHtml.setPagenumber(1);
				housingShenYangHtml.setTaskid(taskHousing.getTaskid());
				housingShenYangHtml.setUrl(webParam.getUrl());
				housingShenYangHtmlRepository.save(housingShenYangHtml);
				housingShenYangPayRepository.saveAll(webParam.getList());
				updateTaskHousing(taskHousing.getTaskid()); 
			}
			else
			{
				tracer.addTag("parser.getAccount.ERROR1", taskHousing.getTaskid());
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 201, taskHousing.getTaskid());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(201);
				updateTaskHousing(taskHousing.getTaskid()); 
			}
		} catch (Exception e) {
			tracer.addTag("parser.getAccount.ERROR2", taskHousing.getTaskid());
			updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 201, taskHousing.getTaskid());
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
			taskHousing.setPaymentStatus(201);
			updateTaskHousing(taskHousing.getTaskid());
			e.printStackTrace();
		}
		
	}

}
