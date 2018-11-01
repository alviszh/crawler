package app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.eureka.listen.EurekaListenbean;
import com.crawler.housingfund.json.HousingFundJsonBean;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.AreaCode;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.service.HousingFundTaskService;

@RestController
@RequestMapping("/housingfund")
public class HousingFundTaskController extends HousingBasicController {

	public static final Logger log = LoggerFactory.getLogger(HousingFundTaskController.class);

	@Autowired
	private HousingFundTaskService housingFundTaskService;

	@Autowired
	private DiscoveryClient discoveryClient;

	public List<ServiceInstance> showServiceInfo(String microname) {
		return this.discoveryClient.getInstances(microname);
	}

	@PostMapping(path = "/check")
	public TaskHousing createTask(@RequestBody HousingFundJsonBean housingFundJsonBean) {

		log.info("housingFundTaskService =======>>" + housingFundJsonBean.toString());
		System.out.println("housingFundTaskService =======>>" + housingFundJsonBean.toString());

		return housingFundTaskService.createTask(housingFundJsonBean);

	}
	
	@PostMapping(path = "/savecitys")
	public TaskHousing saveCitys(@RequestBody MessageLoginForHousing messageLoginForHousing) {

		log.info("messageLoginForHousing  saveCitys=======>>" + messageLoginForHousing.toString());
		System.out.println("messageLoginForHousing  saveCitys =======>>" + messageLoginForHousing.toString());
		TaskHousing taskHousing = housingFundTaskService.getTaskHousing(messageLoginForHousing.getTask_id());
		taskHousing.setCity(taskHousing.getCity());
		return housingFundTaskService.saveTaskHousing(taskHousing);

	}

	/**
	 * 服务接口
	 * 
	 * @param instanceInfo
	 * @return
	 */
	@PostMapping(path = "/eurekalisten")
	public void eurekalistenCanceled(@RequestBody EurekaListenbean eurekaListenbean) {
		log.info("instanceInfo =======>>" + eurekaListenbean.toString());
		AreaCode areaCode = housingFundTaskService.getCitysByTaskName(eurekaListenbean.getAppName());
		if (areaCode != null) {
			System.out.println("==============查询到的城市记录==========" + areaCode.toString());
			List<ServiceInstance> list = showServiceInfo(eurekaListenbean.getAppName().trim());
			System.out.println(list.size());
			if (list.size() <= 0) {
				System.out.println("============查询到的记录小于等于0===========");
				areaCode.setNodenum(list.size());
				areaCode.setIsHousingfundFinished(2);
			} else {
				System.out.println("============查询到的记录为"+list.size()+"===========");
				areaCode.setNodenum(list.size());
				areaCode.setIsHousingfundFinished(0);
			}
			housingFundTaskService.save(areaCode);
		}

	}

/*	*//**
	 * 服务注册接口
	 * 
	 * @param instanceInfo
	 * @return
	 *//*
	@PostMapping(path = "/eurekalisten/registered")
	public void eurekalistenRegistered(@RequestBody EurekaListenbean eurekaListenbean) {

		log.info("eurekaListenbean =======>>" + eurekaListenbean.toString());
		AreaCode areaCode = housingFundTaskService.getCitysByTaskName(eurekaListenbean.getAppName());
		if (areaCode != null) {
			System.out.println("==============查询到的城市记录==========" + areaCode.toString());
			if (areaCode.getNodenum() != null) {
				areaCode.setNodenum(areaCode.getNodenum() + 1);
			} else {
				areaCode.setNodenum(1);
			}
			housingFundTaskService.save(areaCode);
		}

	}*/

	@GetMapping(path = "/tasks/{taskid}/status")
	public TaskHousing taskStatus(@PathVariable String taskid) {

		TaskHousing taskHousing = housingFundTaskService.getTaskHousing(taskid);
		tracer.addTag("Request task status", "taskid:" + taskid);
		return taskHousing;

	}

	/**
	 * @Description: 获取所有公积金已开发完成的城市
	 * @return
	 */
	@GetMapping(path = "/citys")
	public List<AreaCode> getCitys() {

		tracer.addTag("getCitys ==>","start");

		return housingFundTaskService.getCitys();

	}

}
