package app.controller;

import app.commontracerlog.TracerLog;
import app.service.AwsApiService;
import app.service.SanWangEsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.aws.json.HttpProxyBean;
import com.crawler.aws.json.HttpProxyRes;
import com.crawler.domain.json.IdAuthBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api-service/proxy/aws")
@EnableScheduling
public class AwsApiController {


	@Autowired
	private TracerLog tracer;
	@Autowired
	private AwsApiService awsApiService;

	@Autowired
	private SanWangEsService sanWangEsService;
	
	/**
	 * 52.80.126.61   supervisor3
	 * 52.80.89.122	  supervisor4
	 * 52.80.166.231  supervisor5
	 * */

	
	@GetMapping(value="/get")
	public HttpProxyRes getProxy(){
		System.out.println("-----request getProxy()-----");
		HttpProxyRes httpProxyRes = awsApiService.getProxyIps();
		
		List<HttpProxyBean> proxys = awsApiService.getProxyIps().getHttpProxyBeanSet();
		
		List<HttpProxyBean> proxyIps = new ArrayList<HttpProxyBean>();
		for(HttpProxyBean httpProxyBean:proxys){
			if(httpProxyBean.getIp()!=null){
				proxyIps.add(httpProxyBean);
			}
		}
		int random = (int) ( Math.random () * proxyIps.size() ); 
		HttpProxyBean hpb = proxyIps.get(random); 
		
		httpProxyRes.setIp(hpb.getIp());
		httpProxyRes.setName(hpb.getName());
		httpProxyRes.setPort(hpb.getPort());
		httpProxyRes.setInstanceId(hpb.getInstanceId());
		
		tracer.addTag("ip", hpb.getIp());
		tracer.addTag("port", hpb.getPort()); 
		
		System.out.println(hpb);
		tracer.addTag("HttpProxyBean",hpb.toString()); 

		return httpProxyRes;
	}

	@Scheduled(cron = "0 00 18 ? * ?")
//	@Scheduled(cron = "0 11 15 ? * ?")
	public void changeIP(){
		System.out.println("开始切换ip");
		tracer.addTag("changeip", "开始切换ip");
		awsApiService.changeIP();
	}
	
	// 改变三网联查es服务器的配置大小，从周一到周五每天上午的9点00分触发 (系统是格林威治时间，北京时间需要在表达式基础上加8小时)
	@Scheduled(cron = "0 00 1 ? * MON-FRI")
	//@Scheduled(cron = "0 57 19 ? * MON-FRI")
	public void changeEsSizeTo32g() throws InterruptedException{
		System.out.println("changeEsSizeTo32g---start-----");
		sanWangEsService.stop();
		Thread.sleep(60000L);
		sanWangEsService.update2xlarge();
		Thread.sleep(10000L);
		sanWangEsService.start();
		System.out.println("changeEsSizeTo32g---end-----");
	}
	
	// 改变三网联查es服务器的配置大小，从周一到周五每天上午的18点00分触发 (系统是格林威治时间，北京时间需要在表达式基础上加8小时)
	@Scheduled(cron = "0 00 10 ? * MON-FRI")
	//@Scheduled(cron = "0 54 19 ? * MON-FRI")
	public void changeEsSizeTo8g() throws InterruptedException{
		System.out.println("changeEsSizeTo8g---start-----");
		sanWangEsService.stop();
		Thread.sleep(60000L);
		sanWangEsService.updatet2large();
		Thread.sleep(10000L);
		sanWangEsService.start();
		System.out.println("changeEsSizeTo8g---end-----");
	}
	 
}

