package app.controller.developer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.client.CarrierETLClient;

@RestController
@RequestMapping(value = "/developer/app")
public class AppCarrierRestController {

    @Autowired
    private CarrierETLClient carrierETLClient;
	/**
	 * 运营商 -运营商报告
	 */
	@RequestMapping(value = "/carrier/v2/report/{taskid}", method = {RequestMethod.GET,RequestMethod.POST})
	public String report(@PathVariable String taskid) {
		System.out.println("report");
		String  reportResult=carrierETLClient.getDataReportdata(taskid);	
		return reportResult;
	}
	/**
	 * 运营商 -运营商报告原始数据
	 */
	@RequestMapping(value = "/carrier/v2/rawreport/{taskid}", method = {RequestMethod.GET,RequestMethod.POST})
	public String rawreport(@PathVariable String taskid) {
		System.out.println("report");
		String  reportResult=carrierETLClient.getDataReportrawdata(taskid);
		return reportResult;
	}
}
