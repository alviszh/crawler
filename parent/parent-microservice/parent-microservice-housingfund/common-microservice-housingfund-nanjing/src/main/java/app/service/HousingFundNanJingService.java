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
 * @description:
 * @author: sln 
 * @date: 2017年10月26日 下午4:11:20 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.nanjing"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.nanjing"})
public class HousingFundNanJingService extends HousingShiJiaZhuangTemplateCommonService  implements ICrawlerLogin{
	@Value("${filesavepath}") 
	public String fileSavePath;
	@Autowired
	private HousingFundNanJingCrawlerService nanJingCrawlerService;
	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		nanJingCrawlerService.getUserInfo(taskHousing);
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
		String loginUrl="http://www.njgjj.com/login-per.jsp?flg=1";
		String vericodeUrl="http://www.njgjj.com/vericode.jsp";
		login(messageLoginForHousing, taskHousing,loginUrl, vericodeUrl,fileSavePath);
		return taskHousing;
	}
}
