package app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.huaian.HousingHuaiAnHtml;
import com.microservice.dao.entity.crawler.housing.lianyungang.HousingLianYunGangHtml;
import com.microservice.dao.repository.crawler.housing.huaian.HousingHuaiAnHtmlRepository;
import com.microservice.dao.repository.crawler.housing.lianyungang.HousingLianYunGangHtmlRepository;

import app.common.WebParam;
import app.parser.HousingHuaiAnParser;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.huaian")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.huaian")
public class HousingHuaiAnService extends HousingBasicService implements ICrawler, ICrawlerLogin{

	@Autowired
	private HousingHuaiAnHtmlRepository housingHuaiAnHtmlRepository;
	@Autowired
	private HousingHuaiAnParser housingHuaiAnParser;
	@Autowired
	private GetDataService getDataService;

	@Override
	@Async
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		tracer.addTag("login.service.begin", taskHousing.getTaskid());
		String messageLoginJson = gs.toJson(messageLoginForHousing);
		taskHousing.setLoginMessageJson(messageLoginJson);
		save(taskHousing);
		
		try {
			WebParam webParam = housingHuaiAnParser.login(messageLoginForHousing);
			if(null != webParam.getHtmlPage()){
				tracer.addTag("login.service.login.success", "登陆成功");
				HousingHuaiAnHtml html = new HousingHuaiAnHtml();
				html.setUrl(webParam.getUrl());
				html.setTaskid(taskHousing.getTaskid());
				html.setHtml(webParam.getHtmlPage().asXml());
				html.setPageCount(1);
				html.setType("loginedPage");
				housingHuaiAnHtmlRepository.save(html);
				
				changeLoginStatusSuccess(taskHousing, webParam.getWebClient());
			}else if(null != webParam.getHtml()){
				tracer.addTag("login.service.login.fail", webParam.getHtml());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription(webParam.getHtml());
				taskHousingRepository.save(taskHousing);
			}else{
				tracer.addTag("login.service.login.fail2", "异常错误，登陆失败");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
				taskHousingRepository.save(taskHousing);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("login.service.Exception", e.toString());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getError_code());
			save(taskHousing);
		}
		
		return taskHousing;
	}
	
	@Override
	@Async
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		tracer.addTag("crawler.service.begin", taskHousing.getTaskid());
		
		getDataService.getUserinfo(taskHousing);
		getDataService.getPayinfo(taskHousing);
		
		return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}