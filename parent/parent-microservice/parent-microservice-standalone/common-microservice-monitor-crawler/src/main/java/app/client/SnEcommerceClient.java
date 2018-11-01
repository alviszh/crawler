/**
 * 
 */
package app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crawler.e_commerce.json.E_CommerceTask;
/**
 * @author sln 
 * @Description: 电商h5页面的调用——苏宁
 */
@FeignClient("ECOMMERCE-SUNING-H5")
public interface SnEcommerceClient {
	@PostMapping(value = "/h5/sn/login",headers={"content-type=application/x-www-form-urlencoded"}) 
	public @ResponseBody
	E_CommerceTask login(@RequestParam("taskid") String taskid,
			@RequestParam("idnum") String idnum,
			@RequestParam("username") String username,
			@RequestParam("passwd") String passwd,			
			@RequestParam("logintype") String logintype);
	
}
