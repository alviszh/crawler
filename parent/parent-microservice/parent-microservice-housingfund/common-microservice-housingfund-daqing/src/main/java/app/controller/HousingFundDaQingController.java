package app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.service.HousingDaQingService;


/**   
*    
* 项目名称：common-microservice-housingfund-daqing   
* 类名称：HousingFundDaQingController   
* 类描述：   
* 创建人：hyx  
* 创建时间：2017年11月8日 下午3:28:21   
* @version        
*/
@RestController
@Configuration
@RequestMapping("/housing/daqing") 
public class HousingFundDaQingController extends HousingBasicController {

	public static final Logger log = LoggerFactory.getLogger(HousingFundDaQingController.class);
	
	@Autowired
	private HousingDaQingService housingDaQingService;
	
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public TaskHousing telecom(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		
		housingDaQingService.login(messageLoginForHousing);
		
		return taskHousing;
	}
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public TaskHousing crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());	
		
		housingDaQingService.getAllData(messageLoginForHousing);
		
		return taskHousing;
	}
}
