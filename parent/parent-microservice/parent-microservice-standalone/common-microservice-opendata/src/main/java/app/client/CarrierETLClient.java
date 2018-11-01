package app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crawler.PageInfo;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.bean.WebDataReportV2;
import app.bean.WebRawDataReportV2;

@FeignClient("mobile-etl")
//@FeignClient(name = "mobile-etl", url = "http://10.167.202.241:1212")
public interface CarrierETLClient {
	@RequestMapping(value = "/etl/carrier/v2/reportdata", method = { RequestMethod.GET})
	public String getDataReportdata(@RequestParam(value = "taskid") String taskid);
	
	
	@RequestMapping(value = "/etl/carrier/v2/reportdata", method = { RequestMethod.GET })
	public WebDataReportV2 getDataReport(@RequestParam(value = "taskid") String taskid);

	@RequestMapping(value = "/etl/carrier/v2/reportrawdata", method = { RequestMethod.GET})
	public String getDataReportrawdata(@RequestParam(value = "taskid") String taskid);
	
	@RequestMapping(value = "/etl/carrier/v2/reportrawdata", method = { RequestMethod.GET })
	public WebRawDataReportV2 getReportRawdata(@RequestParam(value = "taskid") String taskid);

	@RequestMapping(value = "/etl/carrier/tasks/debitcard/findAll", method = RequestMethod.GET)  
	public @ResponseBody PageInfo<TaskMobile> findAll(
			@RequestParam(value = "currentPage") int currentPage,
			@RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "owner") String appId,
			@RequestParam(value = "environmentId") String environmentId,
			@RequestParam(value = "beginTime") String beginTime,
			@RequestParam(value = "endTime") String endTime,
			@RequestParam(value = "taskid") String taskid,
			@RequestParam(value = "loginName") String loginNumber,
			@RequestParam(value = "userId") String userId
			);
	
	
}