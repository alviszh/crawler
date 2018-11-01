package app.service.common.aop;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

public interface ICrawlerLogin extends ICrawler{
	
	/**
	 * 登录接口
	 * @param taskHousing 
	 * 
	 * 
	 * */
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing);

}
