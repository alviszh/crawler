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
import com.microservice.dao.entity.crawler.housing.zhanjiang.HousingFundZhanJiangAccount;
import com.microservice.dao.entity.crawler.housing.zhanjiang.HousingFundZhanJiangHtml;
import com.microservice.dao.entity.crawler.housing.zhanjiang.HousingFundZhanJiangUserInfo;
import com.microservice.dao.repository.crawler.housing.zhanjiang.HousingFundZhanJiangRepositoryAccount;
import com.microservice.dao.repository.crawler.housing.zhanjiang.HousingFundZhanJiangRepositoryHtml;
import com.microservice.dao.repository.crawler.housing.zhanjiang.HousingFundZhanJiangRepositoryUserInfo;

import app.common.WebParam;
import app.parser.HousingFundZhanJiangParser;
import app.service.common.HousingBasicService;
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.zhanjiang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.zhanjiang")
public class HousingFundZhanJiangService extends HousingBasicService{

	@Autowired
	private HousingFundZhanJiangParser housingFundZhanJiangParser;
	@Autowired
	private HousingFundZhanJiangRepositoryUserInfo housingFundZhanJiangRepositoryUserInfo;
	@Autowired
	private HousingFundZhanJiangRepositoryHtml housingFundZhanJiangRepositoryHtml;
	@Autowired
	private HousingFundZhanJiangRepositoryAccount housingFundZhanJiangRepositoryAccount;
	public void login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam webParam = housingFundZhanJiangParser.login(messageLoginForHousing,taskHousing);
			if(null != webParam)
			{
				if(webParam.getHtml().contains("欢迎光临湛江市住房公积金管理中心官方网站"))
				{
					tracer.addTag("action.login.SUCCESS", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskHousing.setCookies(cookieString);
					taskHousing.setLoginMessageJson(webParam.getHtml());
					taskHousing.setLogintype(messageLoginForHousing.getLogintype());
					save(taskHousing);
				}
				else{
					tracer.addTag("action.login.ERROR1", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status("FAIL");
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					save(taskHousing);
				}
				
			}
			else
			{
				tracer.addTag("action.login.ERROR2", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status("FAIL");
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				save(taskHousing);
			}
		} catch (Exception e) {
			tracer.addTag("action.login.ERROR.TIMEOUT2", taskHousing.getTaskid());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status("FAIL");
			taskHousing.setDescription("登陆超时，请核对信息及其网络");
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
			save(taskHousing);
			e.printStackTrace();
		}		
	}
	
	//个人信息
	public void crawlerUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingFundZhanJiangUserInfo> webParam = housingFundZhanJiangParser.crawlerUserInfo(messageLoginForHousing,taskHousing);
			if(null != webParam)
			{
				if(null != webParam.getHousingZhanJiangUserInfo())
				{
					tracer.addTag("action.crawlerUserInfo.SUCCESS", taskHousing.getTaskid());
					HousingFundZhanJiangHtml h = new HousingFundZhanJiangHtml();
					h.setHtml(webParam.getHtml());
					h.setTaskid(taskHousing.getTaskid());
					h.setType("crawlerUserInfo");
					housingFundZhanJiangRepositoryHtml.save(h);
					housingFundZhanJiangRepositoryUserInfo.save(webParam.getHousingZhanJiangUserInfo());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskHousing.setUserinfoStatus(200);
					String a = webParam.getHousingZhanJiangUserInfo().getFundNum().trim().concat(webParam.getHousingZhanJiangUserInfo().getCompanyNum().trim());
					String b =a.substring(1);
					b =b.substring(0, 19) + b.substring(19 + 1);
//					System.out.println(b);
					taskHousing.setWebdriverHandle("");
					taskHousing.setWebdriverHandle(b);
					save(taskHousing);
				}
				else
				{
					tracer.addTag("action.crawlerUserInfo.ERROR1", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskHousing.setUserinfoStatus(201);
					save(taskHousing);
				}
			}
			else
			{
				tracer.addTag("action.crawlerUserInfo.ERROR2", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
				taskHousing.setUserinfoStatus(201);
				save(taskHousing);
			}
		} catch (Exception e) {
			tracer.addTag("action.crawlerUserInfo.TIMEOUT", taskHousing.getTaskid());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
			taskHousing.setUserinfoStatus(201);
			save(taskHousing);
			e.printStackTrace();
		}
		updateTaskHousing(taskHousing.getTaskid());
	}
	
	//流水
	public void crawlerAccount(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			int a=0;
			for (int i = 0; i < 3; i++) {
				WebParam<HousingFundZhanJiangAccount> webParam = housingFundZhanJiangParser.crawlerAccount(messageLoginForHousing,taskHousing,i);
				if(null != webParam)
				{
					if(null != webParam.getList())
					{
						tracer.addTag("action.crawlerAccount.SUCCESS", taskHousing.getTaskid());
						HousingFundZhanJiangHtml h = new HousingFundZhanJiangHtml();
						h.setHtml(webParam.getHtml());
						h.setTaskid(taskHousing.getTaskid());
						h.setType("crawlerAccount");
						housingFundZhanJiangRepositoryHtml.save(h);
						housingFundZhanJiangRepositoryAccount.saveAll(webParam.getList());
//						taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
//						taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
//						taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
//						taskHousing.setPaymentStatus(200);
//						updateTaskHousing(taskHousing.getTaskid());
						a++;
						save(taskHousing);
					}
					else
					{
						tracer.addTag("action.crawlerAccount.ERROR1", taskHousing.getTaskid());
//						taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
//						taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
//						taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
//						taskHousing.setPaymentStatus(201);
//						updateTaskHousing(taskHousing.getTaskid());
//						save(taskHousing);
					}
				}
				else
				{
					tracer.addTag("action.crawlerAccount.ERROR2", taskHousing.getTaskid());
//					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
//					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
//					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
//					taskHousing.setPaymentStatus(201);
//					updateTaskHousing(taskHousing.getTaskid());
//					save(taskHousing);
				}
			}
			if(a>0)
			{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
				taskHousing.setPaymentStatus(200);
				save(taskHousing);
//				updateTaskHousing(taskHousing.getTaskid());
			}
			else
			{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
				taskHousing.setPaymentStatus(201);
				save(taskHousing);
//				updateTaskHousing(taskHousing.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("action.crawlerUserInfo.TIMEOUT", taskHousing.getTaskid());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
			taskHousing.setPaymentStatus(201);
			save(taskHousing);
//			updateTaskHousing(taskHousing.getTaskid());
			e.printStackTrace();
		}		
		updateTaskHousing(taskHousing.getTaskid());
	}

}
