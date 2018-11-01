package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

/**
 * @description: 石家庄公积金登录service    (没改版之前用的是登陆模板，以石家庄命名的模板)
 * @author: sln 
 * @date: 2017年10月19日 下午2:38:19 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.shijiazhuang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.shijiazhuang"})
public class HousingFundShiJiaZhuangService extends HousingBasicService implements ICrawlerLogin{
	@Autowired
	private HousingFundShiJiaZhuangCrawlerService housingFundShiJiaZhuangCrawlerService;
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		try {
			housingFundShiJiaZhuangCrawlerService.getUserInfo(taskHousing);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		return null;
	}

}
