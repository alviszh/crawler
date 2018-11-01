/**
 * 
 */
package app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crawler.insurance.json.TaskInsurance;

/**
 * @author sln
 * @date 2018年9月6日上午10:58:53
 * @Description: 社保定时任务爬取
 */
@FeignClient(name = "h5-proxy", url = "${task-insurance-proxy}")
public interface InsuranceClient {
	@PostMapping(value = "/h5/insur/login",headers={"content-type=application/x-www-form-urlencoded"}) 
	public @ResponseBody
	TaskInsurance login(@RequestParam("taskId") String taskId,
			@RequestParam("name") String name,
			@RequestParam("idnum") String idnum,
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam("city") String city,
			@RequestParam("loginType") String loginType,
			@RequestParam("area") String area) ;
	
	@PostMapping(path = "/h5/insur/getAllData",headers={"content-type=application/x-www-form-urlencoded"})
	TaskInsurance crawler(@RequestParam("taskId") String taskId,
			@RequestParam("name") String name,
            @RequestParam("idnum") String idnum,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("city") String city,
            @RequestParam("loginType") String loginType,
            @RequestParam("area") String area) ;
	
	
}
