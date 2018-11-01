package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.eureka.MonitorEurekaService;
import app.rancher.MonitorRancherService;



/**
 * @description:   用于测试调用，打包发布后直接走定时任务
 * @author: sln 
 * @date: 2018年2月5日 上午11:05:59 
 */
@EnableFeignClients
@RestController
@EnableScheduling
@RequestMapping("/monitor")
public class MonitorController {
	@Autowired
	private MonitorEurekaService taskerEurekaService;
	@Autowired
	private MonitorRancherService taskerRancherService;
//	=================================================================
	//监测eureka上微服务和节点变化
	@GetMapping(path = "/eurekainfo")
	public void eurekaTasker() { 
		taskerEurekaService.eurekaTasker();
	}
	//监测rancher上主机相关信息
	@GetMapping(path = "/rancherinfo")
	public void rancherTasker() { 
		taskerRancherService.rancherTasker();
	}
}
