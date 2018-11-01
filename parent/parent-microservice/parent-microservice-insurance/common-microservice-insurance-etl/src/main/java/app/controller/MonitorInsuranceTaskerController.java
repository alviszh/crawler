/**
 * 
 */
package app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.bean.MonitorInsuranceTaskerBean;
import app.service.tasker.MonitorInsuranceTaskerResultService;


/**
 * @author sln
 * @Description: 用于监测社保爬取任务的执行结果
 */
@RestController
@Configuration
@RequestMapping("/etl/insurance")
public class MonitorInsuranceTaskerController {
	@Autowired
	private MonitorInsuranceTaskerResultService taskerResultService;
	//获取24小时之内的运行结果
	@RequestMapping(path = "/onedayinsurance",method = {RequestMethod.GET,RequestMethod.POST})
	public List<MonitorInsuranceTaskerBean> oneDayInsurance(){
		return taskerResultService.InsuranceEtlResultForOneDay();
	}
}
