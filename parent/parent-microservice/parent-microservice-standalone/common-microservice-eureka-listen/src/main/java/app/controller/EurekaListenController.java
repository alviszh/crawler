package app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.eureka.listen.EurekaListenbean;

import app.client.HousingfundTaskClient;

@Controller
@RequestMapping("/listen") 
@RestController
@Configuration
public class EurekaListenController {
	
	@Autowired
	private HousingfundTaskClient housingfundTaskClient;
	
	/**
	 * 服务接口
	 * 
	 * @param instanceInfo
	 * @return
	 */
	@PostMapping(path = "/eurekalisten")
	public void eurekalisten(@RequestBody EurekaListenbean eurekaListenbean) {
		System.out.println("'======================eurekalisten====================="+eurekaListenbean.getEventType());
		if(eurekaListenbean.getAppName().indexOf("HOUSINGFUND")!=-1){
			System.out.println("=======公积金服务接口事件============");
			housingfundTaskClient.eurekalisten(eurekaListenbean);
		}
		
		/*log.info("instanceInfo =======>>" + eurekaListenbean.toString());
		AreaCode areaCode = housingFundTaskService.getCitysByTaskName(eurekaListenbean.getAppName());
		if (areaCode != null) {
			System.out.println("==============查询到的城市记录==========" + areaCode.toString());
			List<ServiceInstance> list = showServiceInfo(eurekaListenbean.getAppName());
			if (list.size() <= 0) {

				areaCode.setNodenum(list.size());
				areaCode.setIsHousingfundFinished(2);
			} else {
				areaCode.setNodenum(list.size());
				areaCode.setIsHousingfundFinished(0);
			}
			housingFundTaskService.save(areaCode);
		}*/

	}
	

}
