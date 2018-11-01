/**
 * 
 */
package app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crawler.housingfund.json.TaskHousingfund;

/**
 * @author sln
 * @date 2018年9月11日上午10:21:56
 * @Description: 银行定时任务
 */
@FeignClient("BANK-H5")    //微服务的名字
public interface BankClient {
	@PostMapping(value = "/h5/bank/loginD",headers={"content-type=application/x-www-form-urlencoded"}) 
	public @ResponseBody
	TaskHousingfund login(@RequestParam("taskid") String taskid,
			@RequestParam("bankType") String bankType,
            @RequestParam("loginType") String loginType,
            @RequestParam("loginName") String loginName,
            @RequestParam("cardType") String cardType,
            @RequestParam("password") String password,
            @RequestParam("cardNumber") String cardNumber);
	
	@PostMapping(path = "/h5/bank/crawlerD",headers={"content-type=application/x-www-form-urlencoded"})
	TaskHousingfund crawler(@RequestParam("taskid") String taskid,
			@RequestParam("bankType") String bankType,
            @RequestParam("loginType") String loginType,
            @RequestParam("loginName") String loginName,
            @RequestParam("cardType") String cardType,
            @RequestParam("password") String password,
            @RequestParam("cardNumber") String cardNumber);
}
