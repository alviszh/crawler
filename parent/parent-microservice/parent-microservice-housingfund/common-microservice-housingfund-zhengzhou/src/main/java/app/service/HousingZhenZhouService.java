package app.service;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.zhengzhou.HousingZhengZhouPay;
import com.microservice.dao.repository.crawler.housing.zhengzhou.HousingZhengZhouPayRepository;

import app.crawler.htmlparse.HousingZhengZhouParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.zhengzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.zhengzhou")
public class HousingZhenZhouService extends HousingBasicService{
	
	@Autowired
	private HousingZhengZhouPayRepository housingZhengZhouPayRepository;
	@Async
	public  Future<String> getResult(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing,String html){
		HousingZhengZhouPay userinfo = HousingZhengZhouParse.userinfo_parse(html);
		userinfo.setUserid(messageLoginForHousing.getUser_id());
		userinfo.setTaskid(taskHousing.getTaskid());
		save(userinfo);
		return null;
		
	}
	private void save(HousingZhengZhouPay result){
		housingZhengZhouPayRepository.save(result);
	}
}
