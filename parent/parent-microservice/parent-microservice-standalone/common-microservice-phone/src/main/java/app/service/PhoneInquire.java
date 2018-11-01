package app.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.aws.json.HttpProxyBean;
import com.crawler.aws.json.HttpProxyRes;
import com.crawler.phone.json.PhoneTaskBean;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.telecom.phone.inquire.InquirePhone;
import com.microservice.dao.repository.crawler.telecom.phone.inquire.InquirePhoneRepository;
import com.module.htmlunit.WebCrawler;

import app.client.aws.AwsApiClient;
import app.client.proxy.HttpProxyClient;
import app.commontracerlog.TracerLog;
@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.phone.inquire")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.phone.inquire")
public class PhoneInquire {
	@Autowired
    private InquirePhoneRepository inquirePhoneRepository;
//	@Autowired
//    private AwsApiClient awsApiClient;
//	private HttpProxyBean httpProxyBean = null;
	
	@Autowired 
	private TracerLog tracerLog;
	
	
	public WebDriver driver;
	public String cid = null;
	
	@Value("${webdriver.chrome.driver.path}")
	public String driverPathChrome;
	@Value("${phoneMarkType}")
	public String phoneMarkType;
//	//获取代理IP、端口
//    public HttpProxyBean getProxy(){
//        httpProxyBean = awsApiClient.getProxy();
//        return httpProxyBean;
//    }
    
   
	
	public List<PhoneTaskBean> getPhone(String phnes,String taskid) throws Exception{
		Set<String> set = new HashSet<>(Arrays.asList(phnes.split(",")));
		tracerLog.addTag("taskid",taskid);
		tracerLog.addTag("电话数量",set.size()+"个");

		List<PhoneTaskBean> lists = new ArrayList<PhoneTaskBean>();
		PhoneTaskBean p = new PhoneTaskBean();
		for (String str : set) {  
			System.out.println("phone :"+str);
			Pattern patt = Pattern.compile("[^0-9]");        //得到字符串中的数字
	        Matcher m = patt.matcher(str);
	        String repickStr = m.replaceAll("");
	        if(repickStr==null||repickStr.equals("")){
	        	continue;
	        }
			List<InquirePhone> list1 = new ArrayList<InquirePhone>();
			list1 = inquirePhoneRepository.findByPhone(repickStr);//查询数据库是否有相同手机号
			if(list1.size()>0){
				System.out.println("有重复数据");
//				tracerLog.addTag("电话数量",set.size()+"个");
			}else{
				System.out.println("无重复数据");
				InquirePhone ph = new InquirePhone();
				ph.setPhone(repickStr);
				ph.setTaskId(taskid);
//				ph.setInquireType(0);
				inquirePhoneRepository.save(ph);
			}
			
			p.setPhone(repickStr);
			p.setTaskid(taskid);
			lists.add(p);
		}


		return lists;
	}
	
	@Cacheable(value="mycache",key="#phoneTaskBean.taskid + 'getFindByPhone'")    
	public PhoneTaskBean getFindByPhone(String phone , String taskid) {
		tracerLog.addTag("缓存数据的phone：",phone);
		System.out.println("缓存数据的phone："+phone);
		PhoneTaskBean phoneTaskBean = new PhoneTaskBean();
		phoneTaskBean.setPhone(phone);
		phoneTaskBean.setTaskid(taskid);
		return phoneTaskBean;	
	}
//	@CacheEvict(value="mycache",key="#phoneTaskBean.taskid + 'getFindByPhone'")
//	public String delete(PhoneTaskBean phoneTaskBean) {
//		return "删除成功";
//	}

	
//	@Async
	public String phoneIn(String p,String taskid,HttpProxyRes httpProxyRes) throws Exception{
//		Thread.sleep(5000);
		String url = "https://haoma.baidu.com/query";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		if (httpProxyRes != null) {
			ProxyConfig proxyConfig = webClient.getOptions().getProxyConfig();
			System.err.println("代理："+httpProxyRes.getIp()+Integer.parseInt(httpProxyRes.getPort()));
			proxyConfig.setProxyHost(httpProxyRes.getIp()); 
			proxyConfig.setProxyPort(Integer.parseInt(httpProxyRes.getPort()));

        }
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage1= webClient.getPage(webRequest);
		String html = searchPage1.getWebResponse().getContentAsString();
//		System.err.println(html);
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("#queryForm > input");
		Elements ele1 = doc.select("#id_captcha_0");
//		Elements ele2 = doc.select("#id_cid");
		String csrfmiddlewaretoken = null;
		String captcha_0 = null;
//		String cid = null;
		if(ele.size()>0&&ele1.size()>0){
			csrfmiddlewaretoken = ele.get(0).attr("value").trim();
			captcha_0 = ele1.attr("value").trim();
		}
//		String cid = cid;
		System.out.println("cid:"+cid);
		String url1 = "https://haoma.baidu.com/user_code_info?cid="+cid+"";
		WebRequest webRequest2 = new WebRequest(new URL(url1), HttpMethod.GET);
		HtmlPage searchPage2= webClient.getPage(webRequest2);
		String html5 = searchPage2.getWebResponse().getContentAsString();
		System.err.println("未查询到号码html5："+html5);
		WebRequest webRequest1 = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest1.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest1.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
		webRequest1.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest1.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest1.setAdditionalHeader("Connection", "keep-alive");
		webRequest1.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest1.setAdditionalHeader("Host", "haoma.baidu.com");
		webRequest1.setAdditionalHeader("Origin", "https://haoma.baidu.com");
		webRequest1.setAdditionalHeader("Referer", "https://haoma.baidu.com/query");
		webRequest1.setAdditionalHeader("Upgrade-Insecure-Requests:", "1");
		webRequest1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		String requestBody = "csrfmiddlewaretoken="+csrfmiddlewaretoken+"&phone="+p+"&captcha_0="+captcha_0+"&captcha_1=1&cid="+cid+"";
		webRequest1.setRequestBody(requestBody);
		Page searchPage = webClient.getPage(webRequest1);
		
		String html4 = searchPage.getWebResponse().getContentAsString();
		Document doc1 = Jsoup.parse(html4);
		Elements ele3 = doc1.select("div.category h2");
		Elements ele4 = doc1.select("div.category > span");
		String type = null;
		String phone = null;
		String mark = null;
		String markType = null;
		if(ele3.size()>0){
			
			if(ele4.size()==0){
				type = ele3.text().trim();
			}else if(ele4.size()==1){
				type = ele3.text().trim();
				phone = ele4.text().trim();
			}else if(ele4.size()==2){
				markType = ele3.text().trim();
				mark = ele4.get(0).text().trim();
				phone = ele4.get(1).text().trim();
			}else if(ele4.size()==3){
				markType = ele3.text().trim();
				mark = ele4.get(0).text().trim();
				phone = ele4.get(1).text().trim();
				type = ele4.get(2).text().trim();
			}else{
				ele3 = doc1.select("div.category h2");
				ele4 = doc1.select("div.category  span");
				markType = ele3.text().trim();
				type = ele4.text().trim();
			}
			tracerLog.addTag("Phone",phone);
			System.out.println("type :"+type);
			System.out.println("markType :"+markType);
			System.out.println("mark :"+mark);
			System.out.println("phone :"+phone);
//			List<String> result = Arrays.asList(phoneMarkType.split(","));  
			List<InquirePhone> list = inquirePhoneRepository.findByInquireTypeAndTaskIdAndPhone(null,taskid, p);
			for (InquirePhone ph : list) {  
				System.out.println("sssssssssssssssssssssssss :"+phone);
				Date currentTime = new Date();
			    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    String updateTime = formatter.format(currentTime);
				if(markType!=null){
					byte[] iso8859 = phoneMarkType.getBytes("ISO-8859-1"); 
		            String n = new String(iso8859,"ISO-8859-1"); 
		            n = new String(iso8859,"UTF-8"); 
					System.out.println("phoneMarkType:"+n);
					if(n.contains(markType)){
						ph.setMarkTimes(mark);
						ph.setMarkType(markType);
//						ph.setPhoneType(type);
						ph.setInquireType("1");
						ph.setUpdateTime(updateTime);
						if(type!=null){
//							if(type.equals("抱歉,无该号码信息")||type.equals("移动")||type.equals("联通")||type.equals("电信")){
//								ph.setPhoneType(type);
//							}
							if(type.contains("移动")){
								ph.setPhoneType(type);
								type = null;
							}else if(type.contains("联通")){
								ph.setPhoneType(type);
								type = null;
							}else if(type.contains("电信")){
								ph.setPhoneType(type);
								type = null;
							}else if(type.contains("抱歉,无该号码信息")){
								ph.setPhoneType(type);
								type = null;
							}
							ph.setPhonenumFlag(type);
							
							
						}
						inquirePhoneRepository.save(ph);
					}else{
						ph.setMarkTimes(mark);
//						ph.setMarkType(markType);
//						ph.setPhoneType(markType);
						ph.setInquireType("1");
						ph.setUpdateTime(updateTime);
//						if(markType.equals("抱歉,无该号码信息")||markType.equals("移动")||markType.equals("联通")||markType.equals("电信")){
//							ph.setPhoneType(markType);
//						}
						if(markType!=null&&!markType.equals("抱歉,无该号码信息")){
							if(markType.contains("移动")){
								ph.setPhoneType(markType);
								markType = null;
							}else if(markType.contains("联通")){
								ph.setPhoneType(markType);
								markType = null;
							}else if(markType.contains("电信")){
								ph.setPhoneType(markType);
								markType = null;
							}else if(markType.contains("抱歉,无该号码信息")){
								ph.setPhoneType(markType);
								markType = null;
							}
							ph.setPhonenumFlag(markType);
						}
						
						inquirePhoneRepository.save(ph);
					}
				}else{
					ph.setMarkTimes(mark);
					ph.setMarkType(markType);
					
					ph.setInquireType("1");
					ph.setUpdateTime(updateTime);
					if(type!=null){
//						if(type.equals("抱歉,无该号码信息")||type.equals("移动")||type.equals("联通")||type.equals("电信")){
//							ph.setPhoneType(type);
//						}
						if(type.contains("移动")){
							ph.setPhoneType(type);
							type = null;
						}else if(type.contains("联通")){
							ph.setPhoneType(type);
							type = null;
						}else if(type.contains("电信")){
							ph.setPhoneType(type);
							type = null;
						}else if(type.contains("抱歉,无该号码信息")){
							ph.setPhoneType(type);
							type = null;
						}
						ph.setPhonenumFlag(type);
						
					}
					
					inquirePhoneRepository.save(ph);
				}
				
				
			}
			
//			inquirePhoneRepository.updatePhoneInquire(1, type, markType, mark, taskid, phone);
		}else{
			tracerLog.addTag("未获取到数据",phone);
			System.err.println("未获取到数据");
		}
		
		return null;
	}


	public String getPhoneCid() throws Exception{
//		if (isHttpProxy.equals("1")) {  //使用HTTP代理
////			ProxyConfig proxyConfig = webClient.getOptions().getProxyConfig();
////			proxyConfig.setProxyHost("110.19.191.72"); 
////			proxyConfig.setProxyPort(4599);
//			try {
//				httpProxyRes = getProxyClient("1","北京市");
//            } catch (Exception ex) {
//                System.out.println("获取代理IP、端口出错。");
//            }
//		}
		WebDriver webDriver = openloginCmbChina(); 
		String url = "https://haoma.baidu.com/query";
		try {
			webDriver.get(url);
		} catch (Exception e) {
			// TODO: handle exception
			webDriver.get(url);
		}
		Thread.sleep(10000);
		String html = webDriver.getPageSource();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("#id_cid");
		if(ele.size()>0){
			cid = ele.attr("value").trim();
		}
		tracerLog.addTag("cid",cid);
		webDriver.quit();
		return cid;
	}

    public WebDriver openloginCmbChina()throws Exception{ 
    	//driver.manage().window().maximize(); webdriver.ie.driver  webdriver.chrome.driver
    	System.out.println("launching chrome browser");
    	try {
    		ChromeOptions chromeOptions = new ChromeOptions();

//            if (httpProxyRes != null) {
//                Proxy proxy = new Proxy();
//                String PROXY = ""+httpProxyRes.getIp()+":"+httpProxyRes.getPort()+"";
//                proxy.setHttpProxy(PROXY);
//                proxy.setFtpProxy(PROXY);
//                proxy.setSslProxy(PROXY);
//                chromeOptions.setCapability(CapabilityType.PROXY, proxy);
//            }
        	System.setProperty("webdriver.chrome.driver", driverPathChrome);
    	
    		
        	chromeOptions.addArguments("disable-gpu"); 

        	driver = new ChromeDriver(chromeOptions);
        	 
        	driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        	driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
        	
        	
            driver.manage().window().maximize();
    		
    		return driver;
    	} catch (Exception e) {
    		System.out.println("网络超时,重启");
    		tracerLog.addTag("WebDriver.Error网络超时,重启",e.getMessage());
//    		openloginCmbChina();
    	}
    		return null;
		
   } 
	
}
