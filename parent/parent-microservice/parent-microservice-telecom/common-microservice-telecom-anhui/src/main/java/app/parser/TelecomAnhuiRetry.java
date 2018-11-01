package app.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.commontracerlog.TracerLog;

@Component
public class TelecomAnhuiRetry {
	    
	@Autowired
	private TracerLog tracer;
	
	@Retryable(value = {
			RuntimeException.class, }, maxAttempts = 3, backoff = @Backoff(delay = 1500l, multiplier = 1.5))
	public Page getCallRetry(TaskMobile taskMobile,MessageLogin messageLogin,String url,WebClient webClient) {
		Page page = null;
		try {
//			 WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			 
			 page = webClient.getPage(url);
			 Thread.sleep(2000);
		} catch (Exception e) {
			tracer.addTag("retry.getCallRetry.exception",taskMobile.getTaskid());
			e.printStackTrace();
		}
		if(page.getWebResponse().getContentAsString().contains("fmList"))
		{
			tracer.addTag("retry.getCallRetry.successs", page.getWebResponse().getContentAsString()+taskMobile.getTaskid());
			return page;
		}
		else
		{
			tracer.addTag("retry.getCallRetry.error", page.getWebResponse().getContentAsString()+taskMobile.getTaskid());
			throw new RuntimeException("重试机制触发！");
		}
	}
	
	
	@Retryable(value = {
			RuntimeException.class, }, maxAttempts = 3, backoff = @Backoff(delay = 1500l, multiplier = 1.5))
	public Page getMessageRetry(TaskMobile taskMobile,MessageLogin messageLogin,String url,WebClient webClient) {
		Page page = null;
		try {
//			 WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			 
			 page = webClient.getPage(url);
			 Thread.sleep(2000);
		} catch (Exception e) {
			tracer.addTag("retry.getMessageRetry.exception",taskMobile.getTaskid());
			e.printStackTrace();
		}
		if(page.getWebResponse().getContentAsString().contains("fmList"))
		{
			tracer.addTag("retry.getMessageRetry.successs", page.getWebResponse().getContentAsString()+taskMobile.getTaskid());
			return page;
		}
		else
		{
			tracer.addTag("retry.getMessageRetry.error", page.getWebResponse().getContentAsString()+taskMobile.getTaskid());
			throw new RuntimeException("重试机制触发！");
		}
	}
}
