package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public HttpProxyRes getProxy(String num, String pro){
		tracer.addTag("获取IP的参数：", "数量："+num+" ;省份："+pro);
		System.out.println("获取IP的数量： "+num);
		System.out.println("获取IP的省份： "+pro);
//		System.out.println("获取IP的城市： "+city);	
		if(num==null){
			num = "1";
		}
		return ipApiService.getIP(num,pro);		
	}

	@GetMapping(value="/proxy/del")
	public HttpProxyRes getDelProxy(String num, String pro){
		tracer.addTag("删除", "删除代理ip");
		if(num==null){
			num = "1";
		}
		ipApiService.delete(num,pro);
		tracer.addTag("获取IP的参数：", "数量："+num+" ;省份："+pro);
		return ipApiService.getIP(num,pro);
		
	}
}
