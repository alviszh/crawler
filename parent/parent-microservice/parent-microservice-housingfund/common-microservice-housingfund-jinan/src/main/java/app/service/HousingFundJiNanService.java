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

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.jinan"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.jinan"})
public class HousingFundJiNanService extends HousingShiJiaZhuangTemplateCommonService  implements ICrawlerLogin{
	@Value("${loginhost}") 
	public String loginHost;
	@Value("${filesavepath}") 
	public String fileSavePath;
	@Autowired
	private HousingFundJiNanCrawlerService housingFundJiNanCrawlerService;
	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		housingFundJiNanCrawlerService.getUserInfo(taskHousing);
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
		String loginUrl="http://"+loginHost+"/jnwt/indexPerson.jsp";
		String vericodeUrl="http://"+loginHost+"/jnwt/vericode.jsp";
		login(messageLoginForHousing, taskHousing,loginUrl, vericodeUrl,fileSavePath);
		return taskHousing;
	}
}
