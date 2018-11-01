package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.phone.json.PhoneTaskBean;

import app.commontracerlog.TracerLog;
import app.service.ItemCode;
import app.service.ProMobile;

@RestController
@Configuration
@RequestMapping("/api-service/phone")
public class ProMobileController {
	@Autowired 
	private TracerLog tracerLog;
	@Autowired 
	private ProMobile proMobile;
	@Autowired 
	private ItemCode itemCode;
//	@RequestMapping(value = "/proMobile",method = RequestMethod.GET)
	@PostMapping(value = "/proMobile")
	public PhoneTaskBean getInquireProMobile(@RequestParam(name = "taskid",required=false) String taskid){
		System.out.println("taskid"+taskid);
		tracerLog.addTag("ProMobileController","开始查询pro_mobile_call_info表");
		PhoneTaskBean pm = null;
		try {
			pm = proMobile.getPhoneMobile(taskid);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.addTag("ProMobileController.Error",e.getMessage());
		}
		return pm;
		
	}
	
	@PostMapping(value = "/dictionary")
	public String getPhoneDictionary(){
//		itemCode.addTo();
		proMobile.getProMobile();
		return null;
		
	}
}
