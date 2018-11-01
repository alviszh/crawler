package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import app.bean.WebDataReport;
import app.bean.RequestParam;
import app.service.MobileETLReportService;

@Controller
@RequestMapping("/etl/carrier")
public class MobileReportController {
	@Autowired
	private MobileETLReportService mobileETLReportService;
	
	@RequestMapping(path = "/reportdetail",method = {RequestMethod.GET,RequestMethod.POST})
//	@ResponseBody   //支持get
    public String getRepostJson(Model model,String taskid,String mobilenum){  
		RequestParam rp=new RequestParam();		
		rp.setTaskid(taskid);
		rp.setMobileNum(mobilenum);
		WebDataReport allData = mobileETLReportService.getAllData(rp);
//		String jsonString = JSON.toJSONString(allData);	
//		System.out.println("响应的json串是："+jsonString);
		model.addAttribute("reportModel", allData);
		return "report";	
	}
}
