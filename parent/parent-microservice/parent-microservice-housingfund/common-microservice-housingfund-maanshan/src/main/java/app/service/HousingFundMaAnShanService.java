package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

/**
 * @author: sln 
 * 
 * 
 * 改版前用的是石家庄模板，改版后用的是安徽省直，但是爬取需要重新开发
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.maanshan"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.maanshan"})
public class HousingFundMaAnShanService extends HousingBasicService  implements ICrawlerLogin{
	@Autowired
	private HousingFundMaAnShanCrawlerService housingFundMaAnShanCrawlerService;
	/*@Value("${loginhost}") 
	public String loginHost;
	@Value("${filesavepath}") 
	public String fileSavePath;
	public void login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing){
		String loginUrl="http://"+loginHost+"/maswt/?sc=ff80808160c5f6f20160cb3c0e511061&ac=340500000000";
		String vericodeUrl="http://"+loginHost+"/maswt/vericode.jsp";
		login(messageLoginForHousing, taskHousing,loginUrl, vericodeUrl,fileSavePath);
	}*/
	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		housingFundMaAnShanCrawlerService.getUserInfo(taskHousing);
		return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Async
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		return null;
	}
}
