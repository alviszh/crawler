package app.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.aws.json.HttpProxyBean;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;

import app.client.aws.AwsApiClient;
import app.commontracerlog.TracerLog;

@Component
public class GetProxyService {

	private HttpProxyBean httpProxyBean = null;
	
	@Autowired
	private AwsApiClient awsApiClient;
	@Autowired
    private TracerLog tracerLog;
	
	//将代理IP置入IE浏览器
	public DesiredCapabilities setProxy(DesiredCapabilities ieCapabilities){
		if (httpProxyBean != null) {
	        Proxy proxy = new Proxy();
	        String PROXY = ""+httpProxyBean.getIp()+":"+httpProxyBean.getPort()+"";
	        proxy.setHttpProxy(PROXY);
	        proxy.setFtpProxy(PROXY);
	        proxy.setSslProxy(PROXY);
	        ieCapabilities.setCapability(CapabilityType.PROXY, proxy);
	        return ieCapabilities;
	    }
		return null;
	}
	
	
	//获取代理IP、端口
    public HttpProxyBean getProxy(){
        httpProxyBean = awsApiClient.getProxy();
        return httpProxyBean;
    }
    
    //处理爬取数据业务请求的时候必须是登陆的IP
    public WebRequest getLoginProxy(String url,HttpProxyBean httpProxyBean){
        WebRequest webRequest=null;
		try {
			webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (httpProxyBean != null) {
	            //代理ip，端口
	 		 webRequest.setProxyHost(httpProxyBean.getIp());
	 		 webRequest.setProxyPort(Integer.parseInt(httpProxyBean.getPort()));
	        }
		} catch (MalformedURLException e) {
			e.printStackTrace();
            tracerLog.addTag("getProxt.exception", e.getMessage());
		}
	    return webRequest;
    }
    
	
}
