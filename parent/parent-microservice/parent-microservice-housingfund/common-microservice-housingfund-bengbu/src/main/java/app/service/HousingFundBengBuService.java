package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.bengbu.HousingFundBengBuAccount;
import com.microservice.dao.entity.crawler.housing.bengbu.HousingFundBengBuHtml;
import com.microservice.dao.entity.crawler.housing.bengbu.HousingFundBengBuUserInfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.bengbu.HousingFundBengBuRepositoryAccount;
import com.microservice.dao.repository.crawler.housing.bengbu.HousingFundBengBuRepositoryHtml;
import com.microservice.dao.repository.crawler.housing.bengbu.HousingFundBengBuRepositoryUserInfo;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.HousingFundBengBuParser;
import app.service.common.HousingBasicService;
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.bengbu")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.bengbu")
public class HousingFundBengBuService{

	@Autowired
	private HousingFundBengBuParser housingFundBengBuParser;
	@Autowired
	private HousingFundBengBuRepositoryUserInfo housingFundBengBuRepositoryUserInfo;
	@Autowired
	private HousingFundBengBuRepositoryAccount housingFundBengBuRepositoryAccount;
	@Autowired
	private HousingFundBengBuRepositoryHtml housingFundBengBuRepositoryHtml;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskHousingRepository taskHousingRepository;
	@Autowired
	private HousingBasicService basicService;
	
	public void login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam webParam = housingFundBengBuParser.login(messageLoginForHousing,taskHousing);
			if(null != webParam)
			{
				if(webParam.getHtml().contains("公积金在线查询结果"))
				{
					tracer.addTag("action.login.SUCCESS", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					taskHousing.setCookies(webParam.getHtml());
					taskHousing.setLogintype(messageLoginForHousing.getLogintype());
					taskHousingRepository.save(taskHousing);
				}
				else if(webParam.getHtml().contains("无法找到资源"))
				{
					tracer.addTag("action.login.PWD.ERROR", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getDescription());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
//					taskHousing.setCookies(webParam.getHtml());
					taskHousing.setLogintype(messageLoginForHousing.getLogintype());
					taskHousingRepository.save(taskHousing);
				}
				else{
					tracer.addTag("action.login.ERROR1", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status("FAIL");
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					taskHousingRepository.save(taskHousing);
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
				taskHousingRepository.save(taskHousing);
			}
		} catch (Exception e) {
			tracer.addTag("action.login.ERROR.TIMEOUT2", taskHousing.getTaskid());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status("FAIL");
			taskHousing.setDescription("登陆超时，请核对信息及其网络");
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
			taskHousingRepository.save(taskHousing);
			e.printStackTrace();
		}		
	}
	
	//个人信息
	public void crawlerUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingFundBengBuUserInfo> webParam = housingFundBengBuParser.crawlerUserInfo(messageLoginForHousing,taskHousing);
			if(null != webParam)
			{
				if(null != webParam.getHousingBengBuUserInfo())
				{
					tracer.addTag("action.crawlerUserInfo.SUCCESS", taskHousing.getTaskid());
					HousingFundBengBuHtml h = new HousingFundBengBuHtml();
					h.setHtml(webParam.getHtml());
					h.setTaskid(taskHousing.getTaskid());
					h.setType("crawlerUserInfo");
					housingFundBengBuRepositoryHtml.save(h);
					housingFundBengBuRepositoryUserInfo.save(webParam.getHousingBengBuUserInfo());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskHousing.setUserinfoStatus(200);
					taskHousingRepository.save(taskHousing);
//					updateTaskHousing(taskHousing.getTaskid());
				}
				else
				{
					tracer.addTag("action.crawlerUserInfo.ERROR1", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskHousing.setUserinfoStatus(201);
					taskHousingRepository.save(taskHousing);
//					updateTaskHousing(taskHousing.getTaskid());
				}
			}
			else
			{
				tracer.addTag("action.crawlerUserInfo.ERROR2", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
				taskHousing.setUserinfoStatus(201);
				taskHousingRepository.save(taskHousing);
//				updateTaskHousing(taskHousing.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("action.crawlerUserInfo.TIMEOUT", taskHousing.getTaskid());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
			taskHousing.setUserinfoStatus(201);
			e.printStackTrace();
			taskHousingRepository.save(taskHousing);
		}
		basicService.updateTaskHousing(taskHousing.getTaskid());
	}

	//流水
	public void crawlerAccount(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingFundBengBuAccount> webParam = housingFundBengBuParser.crawlerAccount(messageLoginForHousing,taskHousing);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.crawlerAccount.SUCCESS", taskHousing.getTaskid());
					HousingFundBengBuHtml h = new HousingFundBengBuHtml();
					h.setHtml(webParam.getHtml());
					h.setTaskid(taskHousing.getTaskid());
					h.setType("crawlerAccount");
					housingFundBengBuRepositoryHtml.save(h);
					housingFundBengBuRepositoryAccount.saveAll(webParam.getList());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
					taskHousing.setPaymentStatus(200);
					taskHousingRepository.save(taskHousing);
//					updateTaskHousing(taskHousing.getTaskid());				
					}
				else
				{
					tracer.addTag("action.crawlerAccount.ERROR1", taskHousing.getTaskid());

					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
					taskHousing.setPaymentStatus(201);
					taskHousingRepository.save(taskHousing);
//					updateTaskHousing(taskHousing.getTaskid());				
				}
			}
			else
			{
				tracer.addTag("action.crawlerAccount.ERROR2", taskHousing.getTaskid());

				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
				taskHousing.setPaymentStatus(201);
				taskHousingRepository.save(taskHousing);
//				updateTaskHousing(taskHousing.getTaskid());			
			}
		} catch (Exception e) {
			tracer.addTag("action.crawlerAccount.TIMEOUT", taskHousing.getTaskid());

			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
			taskHousing.setPaymentStatus(201);
			taskHousingRepository.save(taskHousing);
//			updateTaskHousing(taskHousing.getTaskid());
			e.printStackTrace();
		}
		basicService.updateTaskHousing(taskHousing.getTaskid());
	}

}
