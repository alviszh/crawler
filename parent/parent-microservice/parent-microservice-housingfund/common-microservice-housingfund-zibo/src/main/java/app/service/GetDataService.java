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
import com.microservice.dao.entity.crawler.housing.zibo.HousingZiBoHtml;
import com.microservice.dao.repository.crawler.housing.zibo.HousingZiBoHtmlRepository;
import com.microservice.dao.repository.crawler.housing.zibo.HousingZiBoUserinfoRepository;

import app.common.WebParam;
import app.parser.HousingZiBoParser;
import app.service.common.HousingBasicService;
import app.service.common.aop.impl.CrawlerImpl;

@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.zibo")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.zibo")
public class GetDataService extends HousingBasicService{

	@Autowired
	private HousingZiBoUserinfoRepository housingZiBoUserinfoRepository;
	@Autowired
	private HousingZiBoHtmlRepository housingZiBoHtmlRepository;
	@Autowired
	private HousingZiBoParser housingZiBoParser;
	@Autowired
	private CrawlerImpl crawlerImpl;

	public WebParam crawler(WebClient webClient, MessageLoginForHousing messageLoginForHousing,
			TaskHousing taskHousing) {
		tracer.addTag("parser.GetDataService.crawler.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		try {
			webParam = housingZiBoParser.crawler(webClient, messageLoginForHousing, taskHousing);
			if(null != webParam.getHtmlPage()){
				HousingZiBoHtml html = new HousingZiBoHtml();
				html.setUrl(webParam.getUrl());
				html.setType("userinfo");
				html.setPageCount(1);
				html.setHtml(webParam.getHtmlPage().asXml());
				html.setTaskid(taskHousing.getTaskid());
				housingZiBoHtmlRepository.save(html);
				tracer.addTag("parser.GetDataService.crawler.page.status", "用户信息页面已经入库");
			}
			if(null != webParam.getList()){						//判断是否有用户信息
				housingZiBoUserinfoRepository.saveAll(webParam.getList());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getDescription());
				taskHousing.setUserinfoStatus(200);
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getError_code());
				taskHousing.setFinished(true);
				save(taskHousing);
				tracer.addTag("parser.GetDataService.crawler.userinfo.success", "用户信息已经入库");
			}else if(null != webParam.getHtml()){				//webParam.getHtml()中存放的是错误信息
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
				taskHousing.setDescription(webParam.getHtml());
				taskHousing.setUserinfoStatus(201);
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getError_code());
				taskHousing.setFinished(true);
				save(taskHousing);
				tracer.addTag("parser.GetDataService.crawler.userinfo.fail", webParam.getHtml());
			}else{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());
				taskHousing.setUserinfoStatus(201);
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getError_code());
				taskHousing.setFinished(true);
				save(taskHousing);
				tracer.addTag("parser.GetDataService.crawler.userinfo.fail", "用户信息为空");
			}
		} catch (Exception e) {
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());
			taskHousing.setUserinfoStatus(404);
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getError_code());
			taskHousing.setFinished(true);
			save(taskHousing);
			tracer.addTag("parser.GetDataService.crawler.userinfo.fail", e.toString());
		}
		crawlerImpl.getAllDataDone(taskHousing.getTaskid());
		return webParam;
	}
	
	
}
