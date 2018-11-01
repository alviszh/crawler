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
import com.microservice.dao.entity.crawler.housing.lijiang.HousingFundLiJiangAccount;
import com.microservice.dao.entity.crawler.housing.lijiang.HousingFundLiJiangHtml;
import com.microservice.dao.repository.crawler.housing.lijiang.HousingFundLiJiangRepositoryAccount;
import com.microservice.dao.repository.crawler.housing.lijiang.HousingFundLiJiangRepositoryHtml;

import app.common.WebParam;
import app.parser.HousingFundLiJiangParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.lijiang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.lijiang")
public class HousingFundLiJiangCommonService extends HousingBasicService{

	@Autowired
	private HousingFundLiJiangRepositoryAccount housingFundLiJiangRepositoryAccount;
	@Autowired
	private HousingFundLiJiangRepositoryHtml housingFundLiJiangRepositoryHtml;
	@Autowired
	private HousingFundLiJiangParser housingFundLiJiangParser;
	public void login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam webParam = housingFundLiJiangParser.login(messageLoginForHousing,taskHousing);
			if(webParam !=null)
			{
				
			    if(webParam.getHtml().contains("丽江市住房公积金管理中心"))
				{
					tracer.addTag("parser.login.SUCCESS", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim()+messageLoginForHousing.getUsername());
					taskHousing.setCookies(webParam.getHtml());
					save(taskHousing);
				}
				 else
				{
					tracer.addTag("parser.login.ERROR.idNum", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status("FAIL");
					taskHousing.setDescription("系统超时系统问题，请重新登陆");
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim()+messageLoginForHousing.getUsername());
					save(taskHousing);
				}
					
			}
			else
			{
				tracer.addTag("parser.login.ERROR.TIMEOUT1", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status("FAIL");
				taskHousing.setDescription("登陆超时，请核对信息及其网络");
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim()+messageLoginForHousing.getUsername());
				save(taskHousing);
			}
		} catch (Exception e) {
			tracer.addTag("parser.login.ERROR.TIMEOUT2", taskHousing.getTaskid());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status("FAIL");
			taskHousing.setDescription("登陆超时，请核对信息及其网络");
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim()+messageLoginForHousing.getUsername());
			save(taskHousing);
		}
		
	}
	
	
	
	public void crawlerAccount(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingFundLiJiangAccount> webParam = housingFundLiJiangParser.crawlerAccount(messageLoginForHousing,taskHousing);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.crawlerAccount.SUCCESS", taskHousing.getTaskid());
					HousingFundLiJiangHtml h = new HousingFundLiJiangHtml();
					h.setHtml(webParam.getHtml());
					h.setTaskid(taskHousing.getTaskid());
					h.setType("crawlerAccount");
					housingFundLiJiangRepositoryHtml.save(h);
					housingFundLiJiangRepositoryAccount.saveAll(webParam.getList());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
					taskHousing.setPaymentStatus(200);
					save(taskHousing);
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
		updateTaskHousing(taskHousing.getTaskid()); 
	}



	public void crawlerUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		taskHousing.setUserinfoStatus(200);
		save(taskHousing);
		updateTaskHousing(taskHousing.getTaskid());
	}

}
