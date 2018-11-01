package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.haikou.HousingHaiKouHtml;
import com.microservice.dao.repository.crawler.housing.haikou.HousingHaiKouHtmlRepository;
import com.microservice.dao.repository.crawler.housing.haikou.HousingHaiKouPayRepository;
import com.microservice.dao.repository.crawler.housing.haikou.HousingHaiKouUserinfoRepository;

import app.common.WebParam;
import app.parser.HousingHaiKouParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.haikou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.haikou")
public class GetDataService extends HousingBasicService{

	@Autowired
	private HousingHaiKouPayRepository housingHaiKouPayRepository;
	@Autowired
	private HousingHaiKouUserinfoRepository housingHaiKouUserinfoRepository;
	@Autowired
	private HousingHaiKouHtmlRepository housingHaiKouHtmlRepository;
	@Autowired
	private HousingHaiKouParser housingHaiKouParser;

	@Async
	public void getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		tracer.addTag("housing.haikou.GetDataService.getUserInfo.taskid", taskHousing.getTaskid());
		try {
			WebParam webParam = housingHaiKouParser.getUserInfo(messageLoginForHousing, taskHousing);
			HousingHaiKouHtml html = new HousingHaiKouHtml();
			html.setUrl(webParam.getUrl());
			html.setPageCount(1);
			html.setTaskid(taskHousing.getTaskid());
			html.setType("userinfo");
			html.setHtml(webParam.getHtml());
			housingHaiKouHtmlRepository.save(html);
			if(null != webParam.getList() && webParam.getList().size() > 0){
				housingHaiKouUserinfoRepository.saveAll(webParam.getList());
				tracer.addTag("housing.haikou.GetDataService.getUserInfo.success", "用户信息数据采集成功");
				updateUserInfoStatusByTaskid("数据采集中，【用户信息】采集成功。。。", 200, taskHousing.getTaskid());
			}else{
				tracer.addTag("housing.haikou.GetDataService.getUserInfo.fail", "用户信息数据采集失败");
				updateUserInfoStatusByTaskid("数据采集中，【用户信息】采集完成。。。", 201, taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("housing.haikou.GetDataService.getUserInfo.Exception", e.toString());
			updateUserInfoStatusByTaskid("数据采集中，【用户信息】采集完成。。。", 404, taskHousing.getTaskid());
		}
		updateTaskHousing(taskHousing.getTaskid());
	}

	@Async
	public void getPayInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		tracer.addTag("housing.haikou.GetDataService.getPayInfo.taskid", taskHousing.getTaskid());
		try {
			WebParam webParam = housingHaiKouParser.getPayInfo(messageLoginForHousing, taskHousing);
			HousingHaiKouHtml html = new HousingHaiKouHtml();
			html.setUrl(webParam.getUrl());
			html.setPageCount(1);
			html.setTaskid(taskHousing.getTaskid());
			html.setType("payinfo");
			html.setHtml(webParam.getHtmlPage().asXml());
			housingHaiKouHtmlRepository.save(html);
			if(null != webParam.getList() && webParam.getList().size() > 0){
				housingHaiKouUserinfoRepository.saveAll(webParam.getList());
				tracer.addTag("housing.haikou.GetDataService.getPayInfo.success", "缴费信息数据采集成功");
				updatePayStatusByTaskid("数据采集中，【缴费信息】采集成功。。。", 200, taskHousing.getTaskid());
			}else{
				tracer.addTag("housing.haikou.GetDataService.getPayInfo.fail", "缴费信息数据采集失败");
				updatePayStatusByTaskid("数据采集中，【缴费信息】采集完成。。。", 201, taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("housing.haikou.GetDataService.getPayInfo.Exception", e.toString());
			updatePayStatusByTaskid("数据采集中，【缴费信息】采集完成。。。", 404, taskHousing.getTaskid());
		}
		updateTaskHousing(taskHousing.getTaskid());
	}
	
}
