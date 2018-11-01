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
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.foshan.HousingFoshanHtml;
import com.microservice.dao.repository.crawler.housing.foshan.HousingFoShanHtmlRepository;
import com.microservice.dao.repository.crawler.housing.foshan.HousingFoShanPayRepository;
import com.microservice.dao.repository.crawler.housing.foshan.HousingFoShanUserinfoRepository;

import app.common.WebParam;
import app.parser.HousingFoShanParser;
import app.service.common.HousingBasicService;

@EnableAsync
@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.foshan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.foshan")
public class GetDataService extends HousingBasicService{

	@Autowired
	private HousingFoShanUserinfoRepository housingFoShanUserinfoRepository;
	@Autowired
	private HousingFoShanPayRepository housingFoShanPayRepository;
	@Autowired
	private HousingFoShanHtmlRepository housingFoShanHtmlRepository;
	@Autowired
	private HousingFoShanParser housingFoShanParser;
	
	public WebParam login(WebClient webClient, MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		tracer.addTag("parser.GetDataService.login.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		webParam.setCode(0);
		try {
			webParam = housingFoShanParser.login(webClient, messageLoginForHousing, taskHousing);
			if(null != webParam.getHtmlPage()){
				if(webParam.getHtmlPage().asXml().contains("当前用户")){
					tracer.addTag("parser.GetDataService.login.success", "登陆成功");
					taskHousing = changeLoginStatusSuccess(taskHousing, webParam.getWebClient());
					webParam.setCode(1);
				}else{
					tracer.addTag("parser.GetDataService.login.fail", "登陆失败");
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
					save(taskHousing);
				}
			}else{
				tracer.addTag("parser.GetDataService.login.fail2", "登录时页面发生异常");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription("登录异常，请您稍后重试。");
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
				save(taskHousing);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.GetDataService.login.error", e.toString());
			tracer.addTag("parser.GetDataService.login.fail3", "登录异常");
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription("登录异常，请您稍后重试。");
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
			save(taskHousing);
		}
		
		return webParam;
	}

	@Async
	public WebParam getUserInfo(TaskHousing taskHousing) {
		tracer.addTag("parser.HousingFoShanService.crawler.getuserinfo.taskid", taskHousing.getTaskid());
		WebClient webClient = taskHousing.getClient(taskHousing.getCookies());
		try {
			WebParam webParam = housingFoShanParser.getUseInfo(webClient, taskHousing);
			if(null != webParam.getHtmlPage()){
				HousingFoshanHtml foshanHtml = new HousingFoshanHtml();
				foshanHtml.setUrl(webParam.getUrl());
				foshanHtml.setType("userinfo");
				foshanHtml.setPageCount(1);
				foshanHtml.setHtml(webParam.getHtmlPage().asXml());
				foshanHtml.setTaskid(taskHousing.getTaskid());
				housingFoShanHtmlRepository.save(foshanHtml);
				tracer.addTag("parser.GetDataService.crawler.getuserinfo.page.status", "用户信息页面已经入库");
			}
			if(null != webParam.getList()){
				housingFoShanUserinfoRepository.saveAll(webParam.getList());
				tracer.addTag("parser.GetDataService.crawler.getuserinfo.success", "用户信息已入库");
				taskHousing.setUserinfoStatus(200);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_READ.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getError_code());
				save(taskHousing);
			}else{
				tracer.addTag("parser.GetDataService.crawler.getuserinfo.fail", "用户信息获取失败");
				taskHousing.setUserinfoStatus(201);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_READ.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getError_code());
				save(taskHousing);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.GetDataService.crawler.getuserinfo.error", e.toString());
			tracer.addTag("parser.GetDataService.crawler.getuserinfo.fail2", "获取用户信息出现异常");
			taskHousing.setUserinfoStatus(404);
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_READ.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getError_code());
			save(taskHousing);
		}
		updateTaskHousing(taskHousing.getTaskid());
		return null;
	}

	@Async
	public WebParam getPayInfo(TaskHousing taskHousing) {
		tracer.addTag("parser.HousingFoShanService.crawler.getPayInfo.taskid", taskHousing.getTaskid());
		WebClient webClient = taskHousing.getClient(taskHousing.getCookies());
		try {
			WebParam webParam = housingFoShanParser.getPayInfo(webClient, taskHousing);
			if(null != webParam.getHtmlPage()){
				HousingFoshanHtml foshanHtml = new HousingFoshanHtml();
				foshanHtml.setUrl(webParam.getUrl());
				foshanHtml.setType("payinfo");
				foshanHtml.setPageCount(1);
				foshanHtml.setHtml(webParam.getHtmlPage().asXml());
				foshanHtml.setTaskid(taskHousing.getTaskid());
				housingFoShanHtmlRepository.save(foshanHtml);
				tracer.addTag("parser.GetDataService.crawler.getPayInfo.page.status", "流水信息页面已经入库");
			}
			if(null != webParam.getList()){
				housingFoShanPayRepository.saveAll(webParam.getList());
				tracer.addTag("parser.GetDataService.crawler.getPayInfo.success", "流水信息已入库");
				taskHousing.setPaymentStatus(200);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_READ.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getError_code());
				save(taskHousing);
			}else{
				tracer.addTag("parser.GetDataService.crawler.getPayInfo.fail", "流水信息获取失败");
				taskHousing.setPaymentStatus(201);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_READ.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getDescription());
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getError_code());
				save(taskHousing);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.GetDataService.crawler.getPayInfo.error", e.toString());
			tracer.addTag("parser.GetDataService.crawler.getPayInfo.fail2", "获取流水信息出现异常");
			taskHousing.setPaymentStatus(404);
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_READ.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getDescription());
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getError_code());
			save(taskHousing);
		}
		updateTaskHousing(taskHousing.getTaskid());
		return null;
	}
	
}
