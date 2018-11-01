package app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crawler.PageInfo;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;



/**
 *
 */
 @FeignClient("housingfund-etl")
public interface HousingfundETLClient {

	@RequestMapping(value = "/etl/housing/tasks/debitcard/findAll", method = RequestMethod.GET)  
	public @ResponseBody PageInfo<TaskHousing> findAll(
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