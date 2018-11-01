/**
 * 
 */
package app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.bean.MonitorHousingTaskerBean;
import app.service.tasker.MonitorHousingTaskerResultService;


/**
 * @author sln
 * @Description: 用于监测公积金爬取任务的执行结果
 */
@RestController
@RequestMapping("/etl/housing")
public class MonitorHousingTaskerController {
	@Autowired
	private MonitorHousingTaskerResultService taskerResultService;
	//获取24小时之内的运行结果
	@RequestMapping(path = "/onedayhousing",method = {RequestMethod.GET,RequestMethod.POST})
	public List<MonitorHousingTaskerBean> oneDayhousing(){
		return taskerResultService.housingEtlResultForOneDay();
	}
}
