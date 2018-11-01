package app.service;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import app.commontracerlog.TracerLog;

@Component
public class RetryService {
	@Autowired
	private TracerLog tracer;

	/**
	 * 获取详情数据，增加重试机制
	 */
	@Retryable(value = {
			RuntimeException.class, }, maxAttempts = 3, backoff = @Backoff(delay = 1500l, multiplier = 1.5))
	public HtmlPage retry(String url, WebClient webClient, String month) {
		HtmlPage htmlpage = null;
		WebRequest requestSettings;
		try {
			requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
			htmlpage = webClient.getPage(requestSettings);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String asXml = htmlpage.asXml();
		if (asXml.contains("没有查询到相关数据")) {
			return htmlpage;
		} else if (asXml.contains("对不起您的号码未通过实名校验，请先完成实名核对，并审核通过后再来访问此功能！")) {
			System.out.println("对不起您的号码未通过实名校验，请先完成实名核对，并审核通过后再来访问此功能！");
			tracer.addTag("重试失败！", asXml);
			throw new RuntimeException("重试机制触发！");
		} else {
			return htmlpage;
		}
	}

}
