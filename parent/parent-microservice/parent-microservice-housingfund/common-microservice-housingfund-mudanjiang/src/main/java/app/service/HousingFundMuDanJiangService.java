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
import com.microservice.dao.entity.crawler.housing.mudanjiang.HousingFundMuDanJiangAccount;
import com.microservice.dao.entity.crawler.housing.mudanjiang.HousingFundMuDanJiangHtml;
import com.microservice.dao.entity.crawler.housing.mudanjiang.HousingFundMuDanJiangUserInfo;
import com.microservice.dao.repository.crawler.housing.mudanjiang.HousingFundRepositoryMuDanJiangAccount;
import com.microservice.dao.repository.crawler.housing.mudanjiang.HousingFundRepositoryMuDanJiangHtml;
import com.microservice.dao.repository.crawler.housing.mudanjiang.HousingFundRepositoryMuDanJiangUserInfo;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.HousingFundMuDanJiangParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.mudanjiang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.mudanjiang")
public class HousingFundMuDanJiangService extends HousingBasicService{

	@Autowired
	private HousingFundMuDanJiangParser housingFundMuDanJiangParser;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundRepositoryMuDanJiangHtml housingFundRepositoryMuDanJiangHtml;
	@Autowired
	private HousingFundRepositoryMuDanJiangAccount housingFundRepositoryMuDanJiangAccount;
	@Autowired
	private HousingFundRepositoryMuDanJiangUserInfo housingFundRepositoryMuDanJiangUserInfo;
	
	//登陆
	public void login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam webParam = housingFundMuDanJiangParser.login(messageLoginForHousing,taskHousing);
			if(null != webParam)
			{
				if(webParam.getUrl()=="")
				{
					tracer.addTag("action.login.SUCCESS", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskHousing.setCookies(cookieString);
					taskHousing.setLogintype(messageLoginForHousing.getLogintype());
					save(taskHousing);
				}
				else if(webParam.getHtml().contains("交易程序[yd310504]不能执行"))
				{
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status("FAIL");
					taskHousing.setDescription("牡丹江公积金网站异常请关注公告");
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					save(taskHousing);
				}
				else
				{
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




	public void crawlerUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
			try {
				WebParam<HousingFundMuDanJiangUserInfo> webParam = housingFundMuDanJiangParser.crawlerUserInfo(messageLoginForHousing,taskHousing);
				if(null != webParam)
				{
					if(null != webParam.getHousingFundMuDanJiangUserInfo())
					{
						tracer.addTag("action.crawlerUserInfo.SUCCESS", taskHousing.getTaskid());
						HousingFundMuDanJiangHtml h = new HousingFundMuDanJiangHtml();
						h.setHtml(webParam.getHtml());
						h.setTaskid(taskHousing.getTaskid());
						h.setType("crawlerUserInfo");
						housingFundRepositoryMuDanJiangHtml.save(h);
						housingFundRepositoryMuDanJiangUserInfo.save(webParam.getHousingFundMuDanJiangUserInfo());
						taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
						taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
						taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
						taskHousing.setUserinfoStatus(200);
						taskHousing.setCrawlerPort(webParam.getUrl());
						updateTaskHousing(taskHousing.getTaskid());
					}
					else
					{
						tracer.addTag("action.crawlerUserInfo.ERROR1", taskHousing.getTaskid());
						taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
						taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
						taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
						taskHousing.setUserinfoStatus(201);
						updateTaskHousing(taskHousing.getTaskid());
					}
				}
				else
				{
					tracer.addTag("action.crawlerUserInfo.ERROR2", taskHousing.getTaskid());

					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskHousing.setUserinfoStatus(201);
					updateTaskHousing(taskHousing.getTaskid());
				}
			} catch (Exception e) {
				tracer.addTag("action.crawlerUserInfo.TIMEOUT", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
				taskHousing.setUserinfoStatus(201);
				updateTaskHousing(taskHousing.getTaskid());
				e.printStackTrace();
			}
		
	}


	public void crawlerAccount(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		int time =0;
		for (int i = 1; i < 10; i++) {
			try {
				WebParam<HousingFundMuDanJiangAccount> webParam = housingFundMuDanJiangParser.crawlerAccount(messageLoginForHousing,taskHousing,i);
				if(null != webParam)
				{
					if(null != webParam.getList())
					{
						tracer.addTag("action.crawlerAccount.SUCCESS", taskHousing.getTaskid());
						HousingFundMuDanJiangHtml h = new HousingFundMuDanJiangHtml();
						h.setHtml(webParam.getHtml());
						h.setTaskid(taskHousing.getTaskid());
						h.setType("crawlerUserInfo");
						housingFundRepositoryMuDanJiangHtml.save(h);
						housingFundRepositoryMuDanJiangAccount.saveAll(webParam.getList());
						taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
						taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
						taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
						taskHousing.setPaymentStatus(200);
						save(taskHousing);
						time++;
					}
					else
					{
						tracer.addTag("action.crawlerAccount.ERROR1", taskHousing.getTaskid());

						taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
						taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
						taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
						taskHousing.setPaymentStatus(201);
						save(taskHousing);
					}
				}
				else
				{
					tracer.addTag("action.crawlerAccount.ERROR2", taskHousing.getTaskid());

					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
					taskHousing.setPaymentStatus(201);
					save(taskHousing);
				}
			} catch (Exception e) {
				tracer.addTag("action.crawlerAccount.TIMEOUT", taskHousing.getTaskid());

				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
				taskHousing.setPaymentStatus(201);
				save(taskHousing);
				e.printStackTrace();
			}
		}
		if(time>0)
		{
			tracer.addTag("action.crawlerAccount.SUCCESS", taskHousing.getTaskid());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
			taskHousing.setPaymentStatus(200);
			updateTaskHousing(taskHousing.getTaskid()); 
		}
		else
		{
			tracer.addTag("action.crawlerAccount.ERROR3", taskHousing.getTaskid());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
			taskHousing.setPaymentStatus(201);
			updateTaskHousing(taskHousing.getTaskid());
		}
	}

}
