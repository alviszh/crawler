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

import app.bean.MonitorBankTaskerBean;
import app.service.tasker.MonitorBankTaskerResultService;


/**
 * @author sln
 * @Description: 用于监测银行爬取任务的执行结果
 */
@RestController
@Configuration
@RequestMapping("/etl/bank")
public class MonitorBankTaskerController {
	@Autowired
	private MonitorBankTaskerResultService taskerResultService;
	//获取24小时之内的运行结果
	@RequestMapping(path = "/onedaybank",method = {RequestMethod.GET,RequestMethod.POST})
	public List<MonitorBankTaskerBean> oneDayBank(){
		return taskerResultService.BankEtlResultForOneDay();
	}
}
