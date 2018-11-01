/**
 * 
 */
package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.dao.entity.crawler.sms.SmsRecv;

import app.service.MonitorSmsService;

/**
 * @author sln
 * @date 2018年9月26日上午10:09:04
 * @Description:  用于将猫池存储到数据库的内容进行进行读取
 */
@EnableFeignClients
@RestController
@RequestMapping("/monitor")
public class MonitorSmsController {
	@Autowired
	private MonitorSmsService smsService;
	@RequestMapping(value = "/getsms",method = {RequestMethod.POST, RequestMethod.GET})
	public SmsRecv getSms(@RequestParam(name = "phonenum") String phonenum,@RequestParam(name = "smskey") String smskey){
		SmsRecv smsBean = smsService.getSmsBean(phonenum, smskey);
		return smsBean;
	}
	//更新有效的已经使用过的短信，更新taskid为实际任务对应的taskid
	@RequestMapping(value = "/updatesms",method = {RequestMethod.POST, RequestMethod.GET})
	public void updateSmsEffective(@RequestParam(name = "taskid") String taskid,@RequestParam(name = "id") Integer id){
		smsService.updateSmsEffective(taskid, id);
	}
	//排除指定手机号码下无效的短信，数据库中更新taskid为"exclude"
	@RequestMapping(value = "/excludesms",method = {RequestMethod.POST, RequestMethod.GET})
	public void updateSmsIneffective(@RequestParam(name = "phonenum") String phonenum){
		smsService.updateSmsIneffective(phonenum);
	}
}
