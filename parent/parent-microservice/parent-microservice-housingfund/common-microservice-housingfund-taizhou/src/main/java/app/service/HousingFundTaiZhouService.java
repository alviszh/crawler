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
import com.microservice.dao.entity.crawler.housing.taizhou.HousingFundTaiZhouAccount;
import com.microservice.dao.entity.crawler.housing.taizhou.HousingFundTaiZhouHtml;
import com.microservice.dao.entity.crawler.housing.tonghua.HousingFundTongHuaHtml;
import com.microservice.dao.repository.crawler.housing.taizhou.HousingFundTaiZhouRepositoryAccount;
import com.microservice.dao.repository.crawler.housing.taizhou.HousingFundTaiZhouRepositoryHtml;
import com.microservice.dao.repository.crawler.housing.taizhou.HousingFundTaiZhouRepositoryUserInfo;

import app.common.WebParam;
import app.parser.HousingFundTaiZhouParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.taizhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.taizhou")
public class HousingFundTaiZhouService extends HousingBasicService{

	@Autowired
	private HousingFundTaiZhouParser housingFundTaiZhouParser;
	@Autowired
	private HousingFundTaiZhouRepositoryAccount housingFundTaiZhouRepositoryAccount;
	@Autowired
	private HousingFundTaiZhouRepositoryHtml housingFundTaiZhouRepositoryHtml;
	@Autowired
	private HousingFundTaiZhouRepositoryUserInfo fundTaiZhouRepositoryUserInfo;
	public void login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam webParam = housingFundTaiZhouParser.login(messageLoginForHousing,taskHousing);
			if(null != webParam)
			{
				if(webParam.getHtml().contains("欢迎访问台州市住房公积金网站"))
				{
					tracer.addTag("action.login.SUCCESS", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					taskHousing.setCookies(webParam.getHtml());
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
	
	
	public void crawler(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
		WebParam webParam = housingFundTaiZhouParser.crawler(messageLoginForHousing,taskHousing);
		if(null != webParam)
		{
			if(null != webParam.getHousingFundTaiZhouUserInfo())
			{
				tracer.addTag("action.crawlerUserInfo.SUCCESS", taskHousing.getTaskid());
				HousingFundTaiZhouHtml h = new HousingFundTaiZhouHtml();
				h.setHtml(webParam.getHtml());
				h.setTaskid(taskHousing.getTaskid());
				h.setType("crawlerUserInfo");
				housingFundTaiZhouRepositoryHtml.save(h);
				fundTaiZhouRepositoryUserInfo.save(webParam.getHousingFundTaiZhouUserInfo());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
				taskHousing.setUserinfoStatus(200);
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
	try {
		WebParam<HousingFundTaiZhouAccount> webParam = housingFundTaiZhouParser.crawlerAccount(messageLoginForHousing,taskHousing);
		if(null != webParam)
		{
			if(null != webParam.getList())
			{
				tracer.addTag("action.crawlerAccount.SUCCESS", taskHousing.getTaskid());
				HousingFundTaiZhouHtml h = new HousingFundTaiZhouHtml();
				h.setHtml(webParam.getHtml());
				h.setTaskid(taskHousing.getTaskid());
				h.setType("crawlerAccount");
				housingFundTaiZhouRepositoryHtml.save(h);
				housingFundTaiZhouRepositoryAccount.saveAll(webParam.getList());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
				taskHousing.setPaymentStatus(200);
				updateTaskHousing(taskHousing.getTaskid());				}
			else
			{
				tracer.addTag("action.crawlerAccount.ERROR1", taskHousing.getTaskid());

				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
				taskHousing.setPaymentStatus(201);
				updateTaskHousing(taskHousing.getTaskid());				}
		}
		else
		{
			tracer.addTag("action.crawlerAccount.ERROR2", taskHousing.getTaskid());

			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
			taskHousing.setPaymentStatus(201);
			updateTaskHousing(taskHousing.getTaskid());			
		}
	} catch (Exception e) {
		tracer.addTag("action.crawlerAccount.TIMEOUT", taskHousing.getTaskid());

		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
		taskHousing.setPaymentStatus(201);
		updateTaskHousing(taskHousing.getTaskid());
		e.printStackTrace();
	}
	}

}
