package app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crawler.PageInfo;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;



/**
 *
 */
//@FeignClient("bank-etl")
 @FeignClient(name = "bank-etl", url = "http://10.167.211.156:1256")
public interface BankETLClient {

	@RequestMapping(value = "/etl/bank/tasks/debitcard/findAll", method = RequestMethod.GET)  
	public @ResponseBody PageInfo<TaskBank> findAll(
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