package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.taxation.json.TaxationRequestParameters;
import com.crawler.taxation.json.TaxationStatusCode;
import com.microservice.dao.entity.crawler.taxation.basic.TaskTaxation;
import com.microservice.dao.entity.crawler.taxation.beijing.TaxationBeiJingAccount;
import com.microservice.dao.entity.crawler.taxation.beijing.TaxationBeiJingHtml;
import com.microservice.dao.entity.crawler.taxation.beijing.TaxationBeiJingUserInfo;
import com.microservice.dao.repository.crawler.taxation.basic.TaskTaxationRepository;
import com.microservice.dao.repository.crawler.taxation.beijing.TaxationRepositoryBeiJingAccount;
import com.microservice.dao.repository.crawler.taxation.beijing.TaxationRepositoryBeiJingHtml;
import com.microservice.dao.repository.crawler.taxation.beijing.TaxationRepositoryBeiJingUserInfo;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.TaxationBeiJingParser;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = {"com.microservice.dao.entity.crawler.taxation.beijing","com.microservice.dao.entity.crawler.taxation.basic"})
@EnableJpaRepositories(basePackages = {"com.microservice.dao.repository.crawler.taxation.beijing","com.microservice.dao.repository.crawler.taxation.basic"} )
public class TaxationBeiJingCommonService {

	@Autowired
	private TaxationBeiJingParser taxationBeiJingParser;
	@Autowired
	private TaskTaxationRepository taskTaxationRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaxationRepositoryBeiJingAccount taxationRepositoryBeiJingAccount;
	@Autowired
	private TaxationRepositoryBeiJingUserInfo taxationRepositoryBeiJingUserInfo;
	@Autowired
	private TaxationRepositoryBeiJingHtml taxationRepositoryBeiJingHtml;
	@Autowired
	private TaxationService taxationService;
	
	public void login(TaxationRequestParameters taxationRequestParameters, TaskTaxation taskTaxation) {
		try {
			WebParam webParam = taxationBeiJingParser.login(taxationRequestParameters,taskTaxation);
			if(null != webParam)
			{
				if(webParam.getHtml().contains("个人纳税信息查询"))
				{
					tracer.addTag("action.login.SUCCESS", taskTaxation.getTaskid());
					taskTaxation.setPhase(TaxationStatusCode.TAXATION_LOGIN_SUCCESS.getPhase());
					taskTaxation.setPhase_status(TaxationStatusCode.TAXATION_LOGIN_SUCCESS.getPhasestatus());
					taskTaxation.setDescription(TaxationStatusCode.TAXATION_LOGIN_SUCCESS.getDescription());
					taskTaxation.setPassword(taxationRequestParameters.getPassword().trim());
					String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskTaxation.setCookies(cookieString);
					taskTaxation.setTesthtml(webParam.getHtml());
					taskTaxationRepository.save(taskTaxation);
				}
				else
				{
					tracer.addTag("action.login.ERROR", taskTaxation.getTaskid());
					taskTaxation.setPhase(TaxationStatusCode.TAXATION_LOGIN_EXCEPTION.getPhase());
					taskTaxation.setPhase_status(TaxationStatusCode.TAXATION_LOGIN_EXCEPTION.getPhasestatus());
					taskTaxation.setDescription(TaxationStatusCode.TAXATION_LOGIN_EXCEPTION.getDescription());
					taskTaxation.setPassword(taxationRequestParameters.getPassword().trim());
					taskTaxationRepository.save(taskTaxation);
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.login.TIMEOUT", taskTaxation.getTaskid());
			taskTaxation.setPhase(TaxationStatusCode.TAXATION_LOGIN_TIMEOUT.getPhase());
			taskTaxation.setPhase_status(TaxationStatusCode.TAXATION_LOGIN_TIMEOUT.getPhasestatus());
			taskTaxation.setDescription(TaxationStatusCode.TAXATION_LOGIN_TIMEOUT.getDescription());
			taskTaxation.setPassword(taxationRequestParameters.getPassword().trim());
			taskTaxationRepository.save(taskTaxation);
			e.printStackTrace();
		}
		
	}

	//个人信息
	public void crawlerUserInfo(TaxationRequestParameters taxationRequestParameters, TaskTaxation taskTaxation) {
		try {
			WebParam<TaxationBeiJingUserInfo> webParam = taxationBeiJingParser.crawlerUserInfo(taxationRequestParameters,taskTaxation);
			if(null != webParam)
			{
				if(null != webParam.getTaxationBeiJingUserInfos())
				{
					tracer.addTag("action.getUserInfo.success",taskTaxation.getTaskid());
					TaxationBeiJingHtml i = new TaxationBeiJingHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getUserInfo");
					i.setTaskid(taskTaxation.getTaskid());
					i.setUrl(webParam.getUrl());
					taxationRepositoryBeiJingHtml.save(i);
					taxationRepositoryBeiJingUserInfo.save(webParam.getTaxationBeiJingUserInfos());
					taxationService.changeTaskTaxationStatusCrawler(taskTaxation, TaxationStatusCode.TAXATION_CRAWLER_USER_MSG_SUCCESS);
				}
				else
				{
					tracer.addTag("action.getUserInfo.ERROR",taskTaxation.getTaskid());
					TaxationBeiJingHtml i = new TaxationBeiJingHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getUserInfo");
					i.setTaskid(taskTaxation.getTaskid());
					i.setUrl(webParam.getUrl());
					taxationRepositoryBeiJingHtml.save(i);
					taxationRepositoryBeiJingUserInfo.save(webParam.getTaxationBeiJingUserInfos());
					taxationService.changeTaskTaxationStatusCrawler(taskTaxation, TaxationStatusCode.TAXATION_CRAWLER_USER_MSG_ERROR);
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.getUserInfo.TIMEOUT",taskTaxation.getTaskid());
			taxationService.changeTaskTaxationStatusCrawler(taskTaxation, TaxationStatusCode.TAXATION_CRAWLER_USER_MSG_TIMEOUT);
			e.printStackTrace();
		}
	}

	
	//税务流水
	public void crawlerAccount(TaxationRequestParameters taxationRequestParameters, TaskTaxation taskTaxation) {
		try {
			WebParam<TaxationBeiJingAccount> webParam = taxationBeiJingParser.crawlerAccount(taxationRequestParameters,taskTaxation);
			if(null != webParam)
			{
				if(null != webParam.getList())
				{
					tracer.addTag("action.getAccount.success",taskTaxation.getTaskid());
					TaxationBeiJingHtml i = new TaxationBeiJingHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getAccount");
					i.setTaskid(taskTaxation.getTaskid());
					i.setUrl(webParam.getUrl());
					taxationRepositoryBeiJingHtml.save(i);
					taxationRepositoryBeiJingAccount.saveAll(webParam.getList());
					taxationService.changeTaskTaxationStatusCrawler(taskTaxation,TaxationStatusCode.TAXATION_CRAWLER_ACCOUNT_MSG_SUCCESS);
				}
				else
				{
					tracer.addTag("action.getAccount.ERROR",taskTaxation.getTaskid());
					TaxationBeiJingHtml i = new TaxationBeiJingHtml();
					i.setHtml(webParam.getHtml());
					i.setPageCount(1);
					i.setType("getUserInfo");
					i.setTaskid(taskTaxation.getTaskid());
					i.setUrl(webParam.getUrl());
					taxationRepositoryBeiJingHtml.save(i);
					taxationService.changeTaskTaxationStatusCrawler(taskTaxation, TaxationStatusCode.TAXATION_CRAWLER_ACCOUNT_MSG_ERROR);
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.getAccount.TIMEOUT",taskTaxation.getTaskid());
			taxationService.changeTaskTaxationStatusCrawler(taskTaxation, TaxationStatusCode.TAXATION_CRAWLER_ACCOUNT_MSG_TIMEOUT);
			e.printStackTrace();
		}
		
	}

}
