package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;

import app.bean.RequestParam;
import app.bean.WebDataReportV2;
import app.service.MobileETLReportV2Service;

@Controller
@RequestMapping("/etl/carrier/v2")
public class MobileReportV2Controller {
	@Autowired
	private MobileETLReportV2Service mobileETLReportV2Service;
	
	@RequestMapping(path = "/reportdetail",method = {RequestMethod.GET,RequestMethod.POST})
//	@ResponseBody   //支持get
    public String getRepostJson(Model model,String taskid,String mobilenum){  
		RequestParam rp=new RequestParam();		
		rp.setTaskid(taskid);
		rp.setMobileNum(mobilenum);
		WebDataReportV2 allData = mobileETLReportV2Service.getAllData(rp);
//		String jsonString = JSON.toJSONString(allData);	
//		System.out.println("响应的json串是："+jsonString);		
		model.addAttribute("reportModel", allData);
		return "reportv2";	
	}
}
