package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.aws.json.HttpProxyBean;
import com.crawler.aws.json.HttpProxyRes;

import app.commontracerlog.TracerLog;
import app.service.IpApiService;

@RestController
@RequestMapping("/api-service") 
public class IpApiController {
	
	@Autowired
	private TracerLog tracer;
	@Autowired
	private IpApiService ipApiService;
	
	/**
	 * 极光代理获取IP  (暂时只支持省级的IP，不支持市级)
	 * @param num		获取ip数量
	 * @param pro		省份
	 * @param city		城市
	 * @return
	 */
	@GetMapping(value="/proxy/get")
	public HttpProxyRes getProxy(String num, String pro,String useCache){
		tracer.addTag("获取IP的参数：", "数量："+num+" ;省份："+pro);
		System.out.println("获取IP的数量： "+num);
		System.out.println("获取IP的省份： "+pro);
		HttpProxyRes httpProxyRes= null;
		if(num==null){
			num = "1";
		}
		if(useCache==null){
			useCache = "true";
		}
		System.out.println("获取IP是否用缓存： "+useCache);
			
		httpProxyRes =ipApiService.getAwsIp(num);

		if(httpProxyRes == null){
			System.out.println("获取aws代理IP、端口出错。");
	        httpProxyRes=ipApiService.getIP(num,pro,useCache);
//			System.out.println("httpProxyRes： "+httpProxyRes);
			httpProxyRes=ipApiService.getIpExamination(httpProxyRes,num,pro,useCache);
//			tracer.addTag("获取aws代理IP、端口出错。", httpProxyRes.toString());
		}else{
			System.out.println("获取aws代理IP"+httpProxyRes.toString());
			tracer.addTag("获取aws代理IP", httpProxyRes.toString());
		}
		
		return httpProxyRes;		
	}

	//删除代理ip并获取新的代理ip
	@GetMapping(value="/proxy/del")
	public HttpProxyRes getDelProxy(String num, String pro,String useCache){
		tracer.addTag("删除", "删除代理ip");
		if(num==null){
			num = "1";
		}
		ipApiService.delete(num,pro);
		tracer.addTag("获取IP的参数：", "数量："+num+" ;省份："+pro);
		return ipApiService.getIP(num,pro,useCache);
		
	}
	
	
	
	
//	//定时任务检查缓存代理ip是否可用
//	@Scheduled(fixedDelay=60000*5)
//	public String getIp(){
//		tracer.addTag("定时任务", "定时任务检查缓存代理ip是否可用");
//		ipApiService.getIpExamination();
//		return null;
//		
//	}
	
	
}
