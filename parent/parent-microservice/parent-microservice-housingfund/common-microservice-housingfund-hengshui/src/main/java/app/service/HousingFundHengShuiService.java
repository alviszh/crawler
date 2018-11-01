package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.service.common.HousingShiJiaZhuangTemplateCommonService;
import app.service.common.aop.ICrawlerLogin;

/**
 * @description: 衡水市公积金登录service
 * @author: sln 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.hengshui"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.hengshui"})
public class HousingFundHengShuiService extends HousingShiJiaZhuangTemplateCommonService  implements ICrawlerLogin {
	@Value("${loginhost}") 
	public String loginHost;
	@Value("${filesavepath}") 
	public String fileSavePath;
	@Autowired
	private HousingFundHengShuiCrawlerService housingFundHengShuiCrawlerService;
	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		housingFundHengShuiCrawlerService.getUserInfo(taskHousing);
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
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		String loginUrl="http://"+loginHost+"/wsyyt/";
		String vericodeUrl="http://"+loginHost+"/wsyyt/vericode.jsp";
		login(messageLoginForHousing, taskHousing,loginUrl, vericodeUrl,fileSavePath);
		return taskHousing;
	}
}
