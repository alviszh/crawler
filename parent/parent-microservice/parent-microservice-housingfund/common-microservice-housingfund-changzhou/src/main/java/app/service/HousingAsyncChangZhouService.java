package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.service.common.HousingBasicService;

/**   
*    
* 项目名称：common-microservice-housingfund-changzhou   
* 类名称：HousingAsyncChangZhouService   
* 类描述：   
* 创建人：Administrator   
* 创建时间：2018年7月27日 上午10:56:33   
* @version        
*/
@Component
@EnableAsync
public class HousingAsyncChangZhouService extends HousingBasicService {


	@Autowired
	private HousingChangZhouService housingChangZhouService;

	@Async
	public void crawler(MessageLoginForHousing messageLoginForHousing){
		
		TaskHousing taskHousing = housingChangZhouService.login(messageLoginForHousing);
		

		if(taskHousing.getPhase().indexOf(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase())!=-1&&
				taskHousing.getPhase_status().indexOf(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus())!=-1){
			housingChangZhouService.getAllData(messageLoginForHousing);
		}
		
	}
	

}
