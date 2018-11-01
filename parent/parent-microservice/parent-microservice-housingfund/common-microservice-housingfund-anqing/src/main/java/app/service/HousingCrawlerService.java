package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

/**   
*    
* 项目名称：common-microservice-housingfund-anqing   
* 类名称：HousingCrawlerService   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年7月26日 上午10:33:01   
* @version        
*/
@Component
public class HousingCrawlerService {
	
	@Autowired
	private HousingFutureService housingFutureService;

	@Async
	public TaskHousing crawler(MessageLoginForHousing messageLoginForHousing){
		
		TaskHousing taskHousing = housingFutureService.login(messageLoginForHousing);

		if(taskHousing.getPhase().indexOf(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase())!=-1&&
				taskHousing.getPhase_status().indexOf(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus())!=-1){
			housingFutureService.getAllData(messageLoginForHousing);
		}
		
		return null;
	}
}
