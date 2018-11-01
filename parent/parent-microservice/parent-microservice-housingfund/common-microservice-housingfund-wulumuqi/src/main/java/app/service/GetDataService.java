package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.wulumuqi.HousingWuLuMuQiHtml;
import com.microservice.dao.entity.crawler.housing.zhuhai.HousingZhuHaiHtml;
import com.microservice.dao.repository.crawler.housing.wulumuqi.HousingWuLuMuQiHtmlRepository;
import com.microservice.dao.repository.crawler.housing.wulumuqi.HousingWuLuMuQiPayRepository;
import com.microservice.dao.repository.crawler.housing.wulumuqi.HousingWuLuMuQiUserinfoRepository;
import com.microservice.dao.repository.crawler.housing.zhuhai.HousingZhuHaiHtmlRepository;
import com.microservice.dao.repository.crawler.housing.zhuhai.HousingZhuHaiPayRepository;
import com.microservice.dao.repository.crawler.housing.zhuhai.HousingZhuHaiUserinfoRepository;

import app.common.WebParam;
import app.parser.HousingWuLuMuQiParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.wulumuqi")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.wulumuqi")
public class GetDataService extends HousingBasicService{

	@Autowired
	private HousingWuLuMuQiUserinfoRepository housingWuLuMuQiUserinfoRepository;
	@Autowired
	private HousingWuLuMuQiHtmlRepository housingWuLuMuQiHtmlRepository;
	@Autowired
	private HousingWuLuMuQiPayRepository housingWuLuMuQiPayRepository;
	@Autowired
	private HousingWuLuMuQiParser housingZhuHaiParser;
	
	@Async
	public TaskHousing getUserInfo(TaskHousing taskHousing) {
		tracer.addTag("housingFund.crawler.service.getUserInfo.taskid", taskHousing.getTaskid());
		try {
			WebParam webParam =  housingZhuHaiParser.getUserInfo(taskHousing);
			HousingWuLuMuQiHtml html = new HousingWuLuMuQiHtml();
			html.setHtml(webParam.getPage().getWebResponse().getContentAsString());
			html.setPageCount(1);
			html.setTaskid(taskHousing.getTaskid());
			html.setType("userinfo");
			html.setUrl(webParam.getUrl());
			housingWuLuMuQiHtmlRepository.save(html);
			if(null != webParam.getList()){
				housingWuLuMuQiUserinfoRepository.saveAll(webParam.getList());
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
			HousingWuLuMuQiHtml html = new HousingWuLuMuQiHtml();
			html.setHtml(webParam.getPage().getWebResponse().getContentAsString());
			html.setPageCount(1);
			html.setTaskid(taskHousing.getTaskid());
			html.setType("payInfo");
			html.setUrl(webParam.getUrl());
			housingWuLuMuQiHtmlRepository.save(html);
			if(null != webParam.getList() && webParam.getList().size() > 0){
				housingWuLuMuQiPayRepository.saveAll(webParam.getList());
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
