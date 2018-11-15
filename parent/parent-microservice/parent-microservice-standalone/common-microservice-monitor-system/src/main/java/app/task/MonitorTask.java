package app.task;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import app.eureka.MonitorEurekaService;
import app.rancher.MonitorRancherService;

/**
 * 暂定该任务每隔1小时执行一次，若是监控到网站变化，会发送邮件
 * @author sln
 *
 */
@Component
public class MonitorTask {
	@Autowired
	private MonitorEurekaService taskerEurekaService;
	@Autowired
	private MonitorRancherService taskerRancherService;
	//=================================================================
	//监测eureka上微服务存在与否
	@Async
	@Scheduled(cron = "${eurekacron}")
	public void eurekaTask() {
		taskerEurekaService.getRancherMicroServicesAndMonitorEureka();
	}
	//监测rancher各主机网络连接、内存、硬盘指标
	@Async
	@Scheduled(cron = "${ranchercron}")
	public void rancherTask(){
		taskerRancherService.rancherTasker();
	}
}
