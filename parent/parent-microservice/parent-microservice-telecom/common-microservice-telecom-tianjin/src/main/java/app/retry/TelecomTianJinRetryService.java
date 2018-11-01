package app.retry;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.mobile.MobileDataErrRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.MobileDataErrRecRepository;

import app.commontracerlog.TracerLog;
import app.service.common.LoginAndGetCommonService;
/**
 * 经过尝试，此类并没有起到作用，和官网的访问频率有关
 * @author sln
 *
 */
@Component
public class TelecomTianJinRetryService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private LoginAndGetCommonService loginAndGetCommonService;
	@Autowired
	private MobileDataErrRecRepository mobileDataErrRecRepository;
	
	public static int eachCallRecordRetryCount;
	public static int eachSmsRecordRetryCount;
	
	/**
	 * 获取详情数据，增加重试机制(貌似并没有起到作用，还是受官网访问频率的影响)
	 */ 
	@Retryable(value={RuntimeException.class,},maxAttempts=3,backoff = @Backoff(delay = 2000l,multiplier = 1.5))
	//maxAttempts表示重试次数，multiplier即指定延迟倍数，比如delay=5000l,multiplier=2,则第一次重试为5秒，第二次为10秒，第三次为20秒
	//backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为1000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，
	//如果把multiplier设置为1.5，则第一次重试为1秒，第二次为1.5秒，第三次为2.25秒。
	public String retry(String initUrl, String eachPageUrl, WebClient webClient, String firstMonthdate, String lastMonthdate,int curPageNo) throws FailingHttpStatusCodeException, IOException {
		String html=null;
		WebRequest webRequest = new WebRequest(new URL(eachPageUrl), HttpMethod.GET);
		webRequest.setCharset(Charset.forName("UTF-8"));
		webRequest.setAdditionalHeader("Accept", "text/html, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "tj.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://tj.189.cn");
		webRequest.setAdditionalHeader("Referer", ""+initUrl+"");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page page = webClient.getPage(webRequest);
		if(page!=null){
			int statusCode = page.getWebResponse().getStatusCode();
			html = page.getWebResponse().getContentAsString();
			if(200 == statusCode){
				return html;
			}else if(503 == statusCode){
				tracer.addTag(firstMonthdate+"至"+lastMonthdate+"第"+curPageNo+"页详单数据"+System.currentTimeMillis(), statusCode+" 重试机制触发 URL: "+eachPageUrl);
				throw new RuntimeException("状态码="+statusCode+"页面访问频率过高，无法正常响应相关记录，重试机制触发！");
			}else{
				tracer.addTag(firstMonthdate+"至"+lastMonthdate+"第"+curPageNo+"页详单数据"+System.currentTimeMillis(), statusCode+" 重试机制触发 URL: "+eachPageUrl);
				throw new RuntimeException("状态码="+statusCode+"页面访问频率过高，无法正常响应相关记录，重试机制触发！");
			}
		}
		return html;
	}
	
	
	//通话分页爬取
	public String getEachCallPageSource(String initUrl, String eachPageUrl, TaskMobile taskMobile, String firstMonthdate, String lastMonthdate,int curPageNo) throws Exception{
		WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
		String html="初始化变量";
		WebRequest webRequest = new WebRequest(new URL(eachPageUrl), HttpMethod.GET);
		webRequest.setCharset(Charset.forName("UTF-8"));
		webRequest.setAdditionalHeader("Accept", "text/html, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "tj.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://tj.189.cn");
		webRequest.setAdditionalHeader("Referer", ""+initUrl+"");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page page = webClient.getPage(webRequest);
		if(page!=null){
			int statusCode = page.getWebResponse().getStatusCode();
			html = page.getWebResponse().getContentAsString();
			if(200 == statusCode){
				eachCallRecordRetryCount=0;  //将数据归0,避免影响下个循环的使用
				return html;
			}else if(503 == statusCode){
				eachCallRecordRetryCount++;
				if(eachCallRecordRetryCount>2){
					eachCallRecordRetryCount=0; //将数据归0,避免影响下个循环的使用
					tracer.addTag(firstMonthdate+"至"+lastMonthdate+"通话详单暂无法正常响应数据页面", "原因：请求频率过高或因处于月初或月末，相关源码已存入数据库");
					MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), 
							"通话记录",firstMonthdate+"-"+lastMonthdate,taskMobile.getCarrier(),taskMobile.getCity(),
							"page temporarily unavailable", "请求频率过高或因处于月初或月末，该查询区间通话详单页面暂时无法响应", curPageNo);		
					mobileDataErrRecRepository.save(m);
					return html;
				}else{
					Thread.sleep(1000);
					html=getEachCallPageSource(initUrl,eachPageUrl,taskMobile,firstMonthdate,lastMonthdate,curPageNo);
					tracer.addTag(firstMonthdate+"至"+lastMonthdate+"第"+curPageNo+"页短信详单暂无法正常响应数据页面，请求重试次数：", eachCallRecordRetryCount+"");
				}
			}else{  //出现其他状况
				return html;
			}
		}
		return html;
	}


	//短信分页爬取
	public String getEachSMSPageSource(String initUrl, String eachPageUrl, TaskMobile taskMobile, String firstMonthdate,
			String lastMonthdate, int curPageNo) throws Exception {
		WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
		String html="初始化变量";
		WebRequest webRequest = new WebRequest(new URL(eachPageUrl), HttpMethod.GET);
		webRequest.setCharset(Charset.forName("UTF-8"));
		webRequest.setAdditionalHeader("Accept", "text/html, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "tj.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://tj.189.cn");
		webRequest.setAdditionalHeader("Referer", ""+initUrl+"");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page page = webClient.getPage(webRequest);
		if(page!=null){
			int statusCode = page.getWebResponse().getStatusCode();
			html = page.getWebResponse().getContentAsString();
			if(200 == statusCode){
				eachCallRecordRetryCount=0;  //将数据归0,避免影响下个循环的使用
				return html;
			}else if(503 == statusCode){
				eachSmsRecordRetryCount++;
				if(eachSmsRecordRetryCount>2){
					eachSmsRecordRetryCount=0; //将数据归0,避免影响下个循环的使用
					tracer.addTag(firstMonthdate+"至"+lastMonthdate+"短信详单暂无法正常响应数据页面", "原因：请求频率过高或因处于月初或月末，相关源码已存入数据库");
					MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), 
							"短信记录",firstMonthdate+"-"+lastMonthdate,taskMobile.getCarrier(),taskMobile.getCity(),
							"page temporarily unavailable", "请求频率过高或因处于月初或月末，该查询区间短信详单页面暂时无法响应", curPageNo);		
					mobileDataErrRecRepository.save(m);
					return html;
				}else{
					Thread.sleep(1000);
					html=getEachSMSPageSource(initUrl,eachPageUrl,taskMobile,firstMonthdate,lastMonthdate,curPageNo);
					tracer.addTag(firstMonthdate+"至"+lastMonthdate+"第"+curPageNo+"页短信详单暂无法正常响应数据页面，请求重试次数：", eachSmsRecordRetryCount+"");
				}
			}else{  //出现其他状况
				return html;
			}
		}
		return html;
	}
}
