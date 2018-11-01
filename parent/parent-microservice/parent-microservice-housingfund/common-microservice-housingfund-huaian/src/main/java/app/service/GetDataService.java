package app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.huaian.HousingHuaiAnHtml;
import com.microservice.dao.repository.crawler.housing.huaian.HousingHuaiAnHtmlRepository;
import com.microservice.dao.repository.crawler.housing.huaian.HousingHuaiAnPayRepository;
import com.microservice.dao.repository.crawler.housing.huaian.HousingHuaiAnUserinfoRepository;
import com.microservice.dao.repository.crawler.housing.lianyungang.HousingLianYunGangPayRepository;
import com.microservice.dao.repository.crawler.housing.lianyungang.HousingLianYunGangUserinfoRepository;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.parser.HousingHuaiAnParser;
import app.service.common.HousingBasicService;
import app.service.common.LoginAndGetCommon;

@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.huaian")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.huaian")
public class GetDataService extends HousingBasicService{

	@Autowired
	private HousingHuaiAnUserinfoRepository housingHuaiAnUserinfoRepository;
	@Autowired
	private HousingHuaiAnPayRepository housingHuaiAnPayRepository;
	@Autowired
	private HousingHuaiAnHtmlRepository housingHuaiAnHtmlRepository;
	@Autowired
	private HousingHuaiAnParser housingHuaiAnParser;

	@Async
	public void getUserinfo(TaskHousing taskHousing){
		tracer.addTag("crawler.GetDataService.getUserinfo.begin", taskHousing.getTaskid());
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient = LoginAndGetCommon.addcookie(webClient, taskHousing);
			WebParam webParam = housingHuaiAnParser.getUserinfo(taskHousing, webClient);
			
			HousingHuaiAnHtml html = new HousingHuaiAnHtml();
			html.setTaskid(taskHousing.getTaskid());
			html.setPageCount(0);
			html.setType("userinfo");
			html.setUrl(webParam.getUrl());
			html.setHtml(webParam.getHtmlPage().asXml());
			housingHuaiAnHtmlRepository.save(html);
			
			if(null != webParam.getList()){
				housingHuaiAnUserinfoRepository.saveAll(webParam.getList());
				tracer.addTag("crawler.GetDataService.getUserinfo.success", "用户信息入库成功");
				updateUserInfoStatusByTaskid("数据采集中，用户信息采集成功！", 200, taskHousing.getTaskid());
			}else{
				tracer.addTag("crawler.GetDataService.getUserinfo.fail", "用户信息入库失败");
				updateUserInfoStatusByTaskid("数据采集中，用户信息采集完成！", 201, taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.GetDataService.getUserinfo.exception", e.toString());
			updateUserInfoStatusByTaskid("数据采集中，用户信息采集完成！", 404, taskHousing.getTaskid());
		}
		updateTaskHousing(taskHousing.getTaskid());
	}

	@Async
	public void getPayinfo(TaskHousing taskHousing){
		tracer.addTag("crawler.GetDataService.getPayinfo.begin", taskHousing.getTaskid());
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient = LoginAndGetCommon.addcookie(webClient, taskHousing);
			WebParam webParam = housingHuaiAnParser.getPayinfo(taskHousing, webClient);
			
			HousingHuaiAnHtml html = new HousingHuaiAnHtml();
			html.setTaskid(taskHousing.getTaskid());
			html.setPageCount(0);
			html.setType("payinfo");
			html.setUrl(webParam.getUrl());
			html.setHtml(webParam.getHtmlPage().asXml());
			housingHuaiAnHtmlRepository.save(html);
			
			if(webParam.getList().size() > 0){
				housingHuaiAnPayRepository.saveAll(webParam.getList());
				tracer.addTag("crawler.GetDataService.getPayinfo.success", "缴费信息入库成功");
				updatePayStatusByTaskid("数据采集中，缴费信息采集成功！", 200, taskHousing.getTaskid());
			}else{
				tracer.addTag("crawler.GetDataService.getPayinfo.fail", "缴费信息入库失败");
				updatePayStatusByTaskid("数据采集中，缴费信息采集完成！", 201, taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.GetDataService.getPayinfo.exception", e.toString());
			updatePayStatusByTaskid("数据采集中，缴费信息采集完成！", 404, taskHousing.getTaskid());
		}
		updateTaskHousing(taskHousing.getTaskid());
	}
	
}
