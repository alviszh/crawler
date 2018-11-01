package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.zhuhai.HousingZhuHaiHtml;
import com.microservice.dao.repository.crawler.housing.zhuhai.HousingZhuHaiHtmlRepository;

import app.common.WebParam;
import app.parser.HousingZhuHaiParser;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.zhuhai")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.zhuhai")
public class HousingZhuHaiService extends HousingBasicService implements ICrawlerLogin{

	@Autowired
	private HousingZhuHaiHtmlRepository zhuHaiHtmlRepository;
	@Autowired
	private HousingZhuHaiParser housingZhuHaiParser;
	@Autowired
	private GetDataService getDataService;

	@Override
	@Async
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("housingFund.login.service.login.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		try {
			WebParam webParam = housingZhuHaiParser.login(messageLoginForHousing, taskHousing);
			HousingZhuHaiHtml html = new HousingZhuHaiHtml();
			html.setHtml(webParam.getPage().getWebResponse().getContentAsString());
			html.setPageCount(1);
			html.setTaskid(taskHousing.getTaskid());
			html.setType("login");
			html.setUrl(webParam.getUrl());
			zhuHaiHtmlRepository.save(html);
			if(null != webParam.getWebClient()){
				taskHousing.setPassword(webParam.getParam());
				changeLoginStatusSuccess(taskHousing, webParam.getWebClient());
				tracer.addTag("housingFund.login.service.login.success", "登录成功！");
			}else{
				taskHousing.setDescription(webParam.getHtml());
				taskHousing.setPhase("LOGIN");
				taskHousing.setPhase_status("FAIL");
				save(taskHousing);
				tracer.addTag("housingFund.login.service.login.fail", webParam.getHtml());
			}
		} catch (Exception e) {
			e.printStackTrace();
			taskHousing.setDescription("网络异常，请您稍后重试！");
			taskHousing.setPhase("LOGIN");
			taskHousing.setPhase_status("FAIL");
			save(taskHousing);
			tracer.addTag("housingFund.login.service.login.Exception", e.toString());
		}
		return taskHousing;
	}

	@Override
	@Async
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("housingFund.crawler.service.crawler.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		if(null != taskHousing){
			taskHousing = getDataService.getUserInfo(taskHousing);
			taskHousing = getDataService.getPayInfo(taskHousing);
		}
		return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}


}