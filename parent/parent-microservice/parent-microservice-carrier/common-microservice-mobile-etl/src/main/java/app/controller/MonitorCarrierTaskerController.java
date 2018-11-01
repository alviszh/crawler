/**
 * 
 */
package app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.bean.MonitorCarrierTempBean;
import app.service.tasker.MonitorCarrierTaskerResultService;


/**
 * @author sln
 * @date 2018年9月12日下午3:13:18
 * @Description: 用于监测运营商爬取任务的执行结果
 */
@RestController
@RequestMapping("/etl/carrier")
public class MonitorCarrierTaskerController {
	@Autowired
	private MonitorCarrierTaskerResultService carrierTaskerResultService;
	//获取24小时之内的运行结果
	@RequestMapping(path = "/onedaycarrier",method = {RequestMethod.GET,RequestMethod.POST})
	public List<MonitorCarrierTempBean> oneDayCarrier(){
		return carrierTaskerResultService.carrierEtlResultForOneDay();
	}
	//获取10天的运行结果
	@RequestMapping(path = "/moredaycarrier",method = {RequestMethod.GET,RequestMethod.POST})
	public List<MonitorCarrierTempBean> moreDayCarrier(){
		return carrierTaskerResultService.carrierEtlResultForMoreDay();
	}
}
