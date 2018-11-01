package app.task;



import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import app.commontracerlog.TracerLog;
import app.pbccrc.TaskerPbccrcService;
import app.tasker.etlmail.MonitorETLMailService;
import app.taskerbank.TaskerBankService;
import app.taskercarrier.MonitorTelecomService;
import app.taskerecommerce.TaskerEcommerceService;
import app.taskerhousingfund.TaskerFundService;
import app.taskersocialinsurance.TaskerSocialInsurService;
import app.webchange.MonitorWebChangeService;
import app.webusable.MonitorWebUsableService;

/**
 * 暂定该任务每隔1小时执行一次，若是监控到网站变化，会发送邮件
 * @author sln
 *
 */
@Component
public class MonitorTask {
	@Autowired
	private TracerLog tracer;
	@Autowired
	public MonitorWebChangeService taskerWebChangeService;
	@Autowired
	public MonitorWebUsableService taskerWebUsableService;
	@Autowired
	private MonitorTelecomService taskerTelecomService;
	@Autowired
	private TaskerFundService taskerFundService;
	@Autowired
	private TaskerSocialInsurService taskerInsuranceService;
	@Autowired
	private TaskerBankService taskerBankService;
	@Autowired
	private TaskerPbccrcService taskerPbccrcService;
	@Autowired
	private MonitorETLMailService eTLMailService;
	@Autowired
	private TaskerEcommerceService taskerEcommerceService;
	//定义监控任务执行的时间间隔(网站改版情况)
	public final static long TIMEINTERVAL =6 * 60 * 60 * 1000; 
	public final static long initialDelay =6 * 60 * 60 * 1000 ;  //延时执行6小时
	//================================================================
	//网站可用性监测
	@Async
	@Scheduled(fixedDelay = TIMEINTERVAL,initialDelay=initialDelay)
	public void webUsableTask() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String currentTime = df.format(System.currentTimeMillis());
		tracer.addTag("网站监测---定时任务---网站可用性监测---已经按时启动，本次启动时间为：",currentTime);
		//监控网站变化
		taskerWebUsableService.webUsableTasker();
	}
	//监测网站改版情况
//	@Async
//	@Scheduled(fixedDelay = TIMEINTERVAL,initialDelay=initialDelay)
//	public void webChangeTask() {
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
//		String currentTime = df.format(System.currentTimeMillis());
//		tracer.addTag("网站监测---定时任务---网站源码变化监测---已经按时启动，本次启动时间为：",currentTime);
//		//监控网站变化
//		taskerWebChangeService.webChangeTasker();
//	}
	//=================================================================
	//运营商、公积金、社保、银行每日定时爬取任务
	//在指定节点上，每天11半执行一次，爬取不需要短信验证码的电信网站
	@Async
	@Scheduled(cron = "${telecomcron}")
	public void telecomTask(){
		taskerTelecomService.telecomTasker();
	}
	@Async
	@Scheduled(cron = "${housingcron}")
	public void housingFundTask(){
		taskerFundService.housingTasker();
	}	
	@Async
	@Scheduled(cron = "${insurcron}")
	public void insuranceTask(){
		taskerInsuranceService.insuranceTasker();
	}
	@Async
	@Scheduled(cron = "${bankcron}")
	public void bankTask(){
		taskerBankService.bankTasker();
	}
	@Async
	@Scheduled(cron = "${pbccrccron}")
	public void pbccrcTask(){
		taskerPbccrcService.PbccrcTasker();
	}
	@Async
	@Scheduled(cron = "${ecomcron}")
	public void eComTask(){
		taskerEcommerceService.eComTasker();
	}
	//====================================================================
	//将所有的定时任务发送邮件
	@Async
	@Scheduled(cron = "${etlmailcron}")
	public void etlMailTask(){
		eTLMailService.getAllWebTaskResultAndSendMail();
	}
}
