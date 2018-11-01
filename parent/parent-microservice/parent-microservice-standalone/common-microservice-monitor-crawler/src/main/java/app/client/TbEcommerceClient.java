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
 * @Description: 电商h5页面的调用——淘宝
 */
@FeignClient("ECOMMERCE-TAOBAO-H5")
public interface TbEcommerceClient {
	@PostMapping(value = "/h5/tb/login",headers={"content-type=application/x-www-form-urlencoded"}) 
	public @ResponseBody
	E_CommerceTask login(@RequestParam("taskid") String taskid,
			@RequestParam("idnum") String idnum,
			@RequestParam("username") String username,
			@RequestParam("passwd") String passwd,			
			@RequestParam("logintype") String logintype);
}
