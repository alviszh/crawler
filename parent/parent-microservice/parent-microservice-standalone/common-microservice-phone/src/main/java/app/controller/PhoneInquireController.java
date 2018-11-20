package app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.aws.json.HttpProxyRes;
import com.crawler.phone.json.PhoneTaskBean;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.telecom.phone.inquire.InquirePhone;
import com.microservice.dao.repository.crawler.telecom.phone.inquire.InquirePhoneRepository;
import com.module.htmlunit.WebCrawler;

import app.client.proxy.HttpProxyClient;
import app.commontracerlog.TracerLog;
import app.service.PhoneInquire;

@RequestMapping("/inquire") 
@RestController
@Configuration
public class PhoneInquireController {
	@Autowired
    private PhoneInquire phoneInquire; 
	@Autowired 
	private TracerLog tracerLog;
	@Autowired
    private InquirePhoneRepository inquirePhoneRepository;
	@Autowired
    private HttpProxyClient httpProxyClient;
	private HttpProxyRes httpProxyRes = null;
	@Value("${isHttpProxy}")
    String isHttpProxy;
	@Scheduled(fixedDelay=12000)
	public List<InquirePhone> getInquire(){
		tracerLog.addTag("PhoneInquireController","-----------系统定时开始------------");
		System.out.println("-----------系统定时开始------------");
		List<InquirePhone> list = null;
		try {
			list = inquirePhoneRepository.findFirst10ByInquireType(null);
			System.out.println("list:"+list.size());
			if(list.size()>0){
				if (isHttpProxy.equals("1")) {  //使用HTTP代理
					try {
						httpProxyRes = getProxyClient("2","北京市","");
		            } catch (Exception ex) {
		                System.out.println("获取代理IP、端口出错。");
		            }
				}
//				
				
				for (InquirePhone p : list) {  
					System.out.println("phone :"+p);
					phoneInquire.phoneIn(p.getPhone(),p.getTaskId(),httpProxyRes);
				}
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracerLog.addTag("PhoneInquireController.Error",e.getMessage());
		}
		return list;
		
	}
	
	//获取代理IP、端口
    public HttpProxyRes getProxyClient(String num, String pro,String useCache){
    	httpProxyRes = httpProxyClient.getProxy(num,pro,useCache);
        return httpProxyRes;
    }
}
