package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.nanchang.HousingNanChangHtml;
import com.microservice.dao.repository.crawler.housing.nanchang.HousingNanChangHtmlRepository;
import com.microservice.dao.repository.crawler.housing.nanchang.HousingNanChangPayRepository;

import app.common.WebParam;
import app.parser.HousingNanChangParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.nanchang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.nanchang")
public class GetDataService extends HousingBasicService{

	@Autowired
	private HousingNanChangHtmlRepository housingNanChangHtmlRepository;
	@Autowired
	private HousingNanChangPayRepository housingNanChangPayRepository;
	@Autowired
	private HousingNanChangParser housingNanChangParser;
	
	public WebParam login(WebClient webClient, MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		tracer.addTag("parser.GetDataService.login.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		webParam.setCode(0);
		try {
			webParam = housingNanChangParser.login(webClient, messageLoginForHousing, taskHousing);
			if(null != webParam.getHtmlPage()){
				if(webParam.getHtmlPage().getBaseURI().contains("http://www.ncgjj.com.cn:8081/wt-web/home?logintype=1")){
					tracer.addTag("parser.GetDataService.login.success", "登陆成功");
					taskHousing = changeLoginStatusSuccess(taskHousing, webParam.getWebClient());
					webParam.setCode(1);
				}else if(webParam.getHtmlPage().getBaseURI().contains("http://www.ncgjj.com.cn:8081/wt-web/login")){
					String msg = housingNanChangParser.getLoginFailMsg(webParam.getHtmlPage().asXml());
					tracer.addTag("parser.GetDataService.login.fail", msg);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription(msg);
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
					save(taskHousing);
				}else{
					tracer.addTag("parser.GetDataService.login.fail1", "登录时页面发生异常");
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription("登录异常，请您稍后重试。");
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


	public TaskHousing getPayInfo(TaskHousing taskHousing) {
		tracer.addTag("parser.HousingFoShanService.crawler.getPayInfo.taskid", taskHousing.getTaskid());
		int a = 0;		//获取到数据的计数器
		int b = 0;		//页面出现异常的计数器
		WebClient webClient = taskHousing.getClient(taskHousing.getCookies());
		for (int i = 0; i < 10; i++) {
			try {
				WebParam webParam = housingNanChangParser.getPayInfo(webClient, taskHousing, i);
				if (null != webParam.getHtml()) {
					HousingNanChangHtml nanChangHtml = new HousingNanChangHtml();
					nanChangHtml.setUrl(webParam.getUrl());
					nanChangHtml.setType("payinfo"+i);
					nanChangHtml.setPageCount(1);
					nanChangHtml.setHtml(webParam.getHtml());
					nanChangHtml.setTaskid(taskHousing.getTaskid());
					housingNanChangHtmlRepository.save(nanChangHtml);
					tracer.addTag("parser.GetDataService.crawler.getPayInfo.page"+i+".status", "流水信息"+i+"页面已经入库");
				}
				if (null != webParam.getList()) {
					housingNanChangPayRepository.saveAll(webParam.getList());
					tracer.addTag("parser.GetDataService.crawler.getPayInfo"+i+".success", "流水信息"+i+"已入库");
					a++;
				} else {
					tracer.addTag("parser.GetDataService.crawler.getPayInfo"+i+".fail", "流水信息"+i+"获取失败");
				}
			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("parser.GetDataService.crawler.getPayInfo"+i+".error", e.toString());
				tracer.addTag("parser.GetDataService.crawler.getPayInfo"+i+".fail2", "获取流水信息"+i+"出现异常");
				b++;
			} 
		}
		
		if(a > 0){
			taskHousing.setUserinfoStatus(201);
			taskHousing.setPaymentStatus(200);
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_READ.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getError_code());
			save(taskHousing);
		}else if(b == 10){
			taskHousing.setUserinfoStatus(201);
			taskHousing.setPaymentStatus(404);
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getDescription());
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getError_code());
			save(taskHousing);
		}else{
			taskHousing.setUserinfoStatus(201);
			taskHousing.setPaymentStatus(201);
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getDescription());
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getError_code());
			save(taskHousing);
		}
		return taskHousing;
	}
	
}
