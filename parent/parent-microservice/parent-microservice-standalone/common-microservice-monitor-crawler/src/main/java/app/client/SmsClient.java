/**
 * 
 */
package app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.microservice.dao.entity.crawler.sms.SmsRecv;

/**
 * @author sln
 * @date 2018年9月28日下午4:12:24
 * @Description: 获取猫池截取到的短信，获取正确的短信（对于分条读取到的短信，例如河南电信，数据库查询操作已经用了like关键字）
 */
@FeignClient("MONITOR-SMS")
public interface SmsClient {
	@GetMapping(path = "/monitor/getsms")
	public SmsRecv getsms(@RequestParam(name = "phonenum") String phonenum,@RequestParam(name = "smskey") String smskey);
	
	@GetMapping(path = "/monitor/updatesms")
	public void updateSmsEffective(@RequestParam(name = "taskid") String taskid,@RequestParam(name = "id") Integer id);
	
	@GetMapping(path = "/monitor/excludesms")
	public void updateSmsIneffective(@RequestParam(name = "phonenum") String phonenum);
}
