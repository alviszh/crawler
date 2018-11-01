package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.zhuhai.HousingZhuHaiHtml;
import com.microservice.dao.repository.crawler.housing.zhuhai.HousingZhuHaiHtmlRepository;
import com.microservice.dao.repository.crawler.housing.zhuhai.HousingZhuHaiPayRepository;
import com.microservice.dao.repository.crawler.housing.zhuhai.HousingZhuHaiUserinfoRepository;

import app.common.WebParam;
import app.parser.HousingZhuHaiParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.zhuhai")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.zhuhai")
public class GetDataService extends HousingBasicService{

	@Autowired
	private HousingZhuHaiUserinfoRepository housingZhuHaiUserinfoRepository;
	@Autowired
	private HousingZhuHaiPayRepository housingZhuHaiPayRepository;
	@Autowired
	private HousingZhuHaiHtmlRepository zhuHaiHtmlRepository;
	@Autowired
	private HousingZhuHaiParser housingZhuHaiParser;
	
	@Async
	public TaskHousing getUserInfo(TaskHousing taskHousing) {
		tracer.addTag("housingFund.crawler.service.getUserInfo.taskid", taskHousing.getTaskid());
		try {
			WebParam webParam =  housingZhuHaiParser.getUserInfo(taskHousing);
			HousingZhuHaiHtml html = new HousingZhuHaiHtml();
			html.setHtml(webParam.getPage().getWebResponse().getContentAsString());
			html.setPageCount(1);
			html.setTaskid(taskHousing.getTaskid());
			html.setType("userinfo");
			html.setUrl(webParam.getUrl());
			zhuHaiHtmlRepository.save(html);
			if(null != webParam.getList()){
				housingZhuHaiUserinfoRepository.saveAll(webParam.getList());
				updateUserInfoStatusByTaskid("数据采集中，【用户信息】采集成功！", 200, taskHousing.getTaskid());
				tracer.addTag("housingFund.crawler.service.getUserInfo.success", "数据采集中，【用户信息】采集成功！");
			}else{
				updateUserInfoStatusByTaskid("数据采集中，【用户信息】采集完成！", 201, taskHousing.getTaskid());
				tracer.addTag("housingFund.crawler.service.getUserInfo.fail", "数据采集中，【用户信息】采集完成！无数据！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			updateUserInfoStatusByTaskid("数据采集中，【用户信息】采集完成！", 404, taskHousing.getTaskid());
			tracer.addTag("housingFund.crawler.service.getUserInfo.Exception", e.toString());
		}
		taskHousing = updateTaskHousing(taskHousing.getTaskid());
		return taskHousing;
	}

	@Async
	public TaskHousing getPayInfo(TaskHousing taskHousing) {
		try {
			tracer.addTag("housingFund.crawler.service.getPayInfo.taskid", taskHousing.getTaskid());
			WebParam webParam =  housingZhuHaiParser.getPayInfo(taskHousing);
			HousingZhuHaiHtml html = new HousingZhuHaiHtml();
			html.setHtml(webParam.getPage().getWebResponse().getContentAsString());
			html.setPageCount(1);
			html.setTaskid(taskHousing.getTaskid());
			html.setType("payInfo");
			html.setUrl(webParam.getUrl());
			zhuHaiHtmlRepository.save(html);
			if(null != webParam.getList() && webParam.getList().size() > 0){
				housingZhuHaiPayRepository.saveAll(webParam.getList());
				updatePayStatusByTaskid("数据采集中，【缴费信息】采集成功！", 200, taskHousing.getTaskid());
				tracer.addTag("housingFund.crawler.service.getPayInfo.success", "数据采集中，【缴费信息】采集成功！");
			}else{
				updatePayStatusByTaskid("数据采集中，【缴费信息】采集完成！", 201, taskHousing.getTaskid());
				tracer.addTag("housingFund.crawler.service.getPayInfo.fail", "数据采集中，【缴费信息】采集完成！无数据！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			updatePayStatusByTaskid("数据采集中，【缴费信息】采集完成！", 404, taskHousing.getTaskid());
			tracer.addTag("housingFund.crawler.service.getPayInfo.Exception", e.toString());
		}
		taskHousing = updateTaskHousing(taskHousing.getTaskid());
		return taskHousing;
	}
}
