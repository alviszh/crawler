package app.service;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;
import net.sf.json.JSONObject;

/**
 * @description:    最开始登录方式：根据测试类，决定登录方式用的是driver浏览器和htmlunit相结合(selenium方式获取登录所需的验证码，
 * 					htmlunit将相关信息进行统一校验，因为只能通过那个校验链接响应相关的登录错误或者是失败的信息)
 * 
 * 					后在泰安社保的开发过程中，发现用流的方式可以将page对象承载的图片验证码进行保存，故决定将衡水的登录代码重构
 * @author: sln 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.hengshui"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.hengshui"})
public class InsuranceHengShuiService implements InsuranceLogin{
	public static final Logger log = LoggerFactory.getLogger(InsuranceHengShuiService.class);
	@Autowired
	private TracerLog tracer;
	@Value("${loginhost}")
	public String loginhost;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceHengShuiCrawlerService insuranceHengShuiCrawlerService;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	private static int captchaErrorCount=0;   //验证码识别错误次数计数器
	public static String pernum;   //个人编号，爬取所有的信息都需要将其作为参数
	public static String idcard;   //身份证号
	public static String name;   //姓名
	public static String insurnum;   //社保卡号
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try{
			String url="http://"+loginhost+"/ggfwweb/";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				//此处请求图片验证码链接
				url="http://"+loginhost+"/ggfwweb/captchaimg?tm=1514277408596";
				webRequest = new WebRequest(new URL(url), HttpMethod.GET);
				page = webClient.getPage(webRequest);
				String imgagePath = InsuranceHengShuiImageService.getImagePath(page);
				Thread.sleep(1000);
				String code = chaoJiYingOcrService.callChaoJiYingService(imgagePath, "1902");   
				//在此处用请求链接的方式来登录，因为此链接会将错误信息响应回来
				url="http://"+loginhost+"/ggfwweb/app/login?"
						+ "j_username="+insuranceRequestParameters.getUsername().trim()+""
						+ "&j_password="+insuranceRequestParameters.getPassword().trim()+""
						+ "&j_captcha="+code+"";
				webRequest=new WebRequest(new URL(url), HttpMethod.POST);
				page = webClient.getPage(webRequest);
				if(null!=page){
					String html=page.getWebResponse().getContentAsString();
					tracer.addTag(taskInsurance.getTaskid()+"用链接进行登录信息的校验，返回的校验信息是", html);
					if(html.contains("0000")){  //登录成功
						taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
						taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
						taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
						String logincookies = CommonUnit.transcookieToJson(webClient);
					    taskInsurance.setCookies(logincookies);
					    taskInsuranceRepository.save(taskInsurance);				    
					    Thread.sleep(1000);  
					}else if(html.contains("验证码错误")){
						System.out.println("验证码错误");
						captchaErrorCount++;
						tracer.addTag(taskInsurance.getTaskid()+"登录验证码解析错误的次数为：",captchaErrorCount+"次");
						if (captchaErrorCount<=2){
							tracer.addTag("action.login.captchaErrorCount","解析图片验证码失败"+captchaErrorCount+"次，重新执行登录方法");
							login(insuranceRequestParameters);
						} else {
							captchaErrorCount=0;
							insuranceService.changeLoginStatusCaptError(taskInsurance);
							return taskInsurance;
						}
					}else if(html.contains("用户名或密码错误")){
						System.out.println("用户名或密码错误");
						insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase(),
								InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus(),
								InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getDescription(),taskInsurance);
					}else{
						System.out.println("出现了其他登录错误");
						insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
								InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
								"系统繁忙，请稍后再试！",taskInsurance);
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("登录过程出现异常，异常信息是：", e.toString());
			System.out.println("登录过程出现异常，异常信息是："+e.toString());
			insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
					InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
					"系统繁忙，请稍后再试！",taskInsurance);
		}
		return taskInsurance;
	}
	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance=taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			//获取相关信息的请求必须带参数——个人编号，该参数如下链接请求：
			String url="http://"+loginhost+"/ggfwweb/app/curuser?_=1513735589511";
			WebRequest webRequest=new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			Page page = webClient.getPage(webRequest);  
			if(page!=null){
				String html=page.getWebResponse().getContentAsString();
				if(!html.equals("")){
					//个人编号
					pernum = JSONObject.fromObject(html).getJSONArray("userbussList").getJSONObject(0).getString("grbh");
					name=JSONObject.fromObject(html).getString("name");
				    idcard=JSONObject.fromObject(html).getString("idcard");
					insurnum=JSONObject.fromObject(html).getString("sbkh");
					
					//根据如上参数，爬取所需要的社保信息
					Map<String, Future<String>> listfuture = new HashMap<>();   //判断异步爬取是否完成
					//由于该网站并没有提供生育保险的缴费信息，故在爬取数据的最初将生育保险的爬取状态更新为完成
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
							 201, taskInsurance);
					///////////////////////////////////////////////////////////////////////////////
					try {
						Future<String> future=insuranceHengShuiCrawlerService.getUserInfo(taskInsurance,pernum,name,idcard,insurnum);
						listfuture.put("getUserInfo", future);
					} catch (Exception e) {
						tracer.addTag("action.crawler.getUserInfo.e", taskInsurance.getTaskid()+"===>"+e.toString());
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
								 201, taskInsurance);
					}
//					如下是除了生育险之外的其他险缴费信息的爬取
					try {
						Future<String> future=insuranceHengShuiCrawlerService.getPension(taskInsurance,pernum);
						listfuture.put("getPension", future);
					} catch (Exception e) {
						tracer.addTag("action.crawler.getPension.e", taskInsurance.getTaskid()+"===>"+e.toString());
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
								InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
								201, taskInsurance);
					}
					try {
						Future<String> future=insuranceHengShuiCrawlerService.getMedical(taskInsurance,pernum);
						listfuture.put("getMedical", future);
					} catch (Exception e) {
						tracer.addTag("action.crawler.getMedical.e", taskInsurance.getTaskid()+"===>"+e.toString());
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
								InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
								201, taskInsurance);
					}
					try {
						Future<String> future=insuranceHengShuiCrawlerService.getInjury(taskInsurance,pernum);
						listfuture.put("getInjury", future);
					} catch (Exception e) {
						tracer.addTag("action.crawler.getInjury.e", taskInsurance.getTaskid()+"===>"+e.toString());
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(), 
								InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
								201, taskInsurance);
					}
					try {
						Future<String> future=insuranceHengShuiCrawlerService.getUnemployment(taskInsurance,pernum);
						listfuture.put("getUnemployment", future);
					} catch (Exception e) {
						System.out.println("action.crawler.getUnemployment.e:"+e.toString());
						tracer.addTag("action.crawler.getUnemployment.e", taskInsurance.getTaskid()+"===>"+e.toString());
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
								InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
								201, taskInsurance);
					}
				
					
//					最终状态的更新
					try {
						while (true) {
							for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
//								 判断是否执行完毕
								if (entry.getValue().isDone()) { 
									tracer.addTag(taskInsurance.getTaskid() + entry.getKey() + "---get", entry.getValue().get());
									tracer.addTag(taskInsurance.getTaskid() + entry.getKey() + "---isDone", entry.getValue().get());
									listfuture.remove(entry.getKey());
									break;
								}
							}
							if (listfuture.size() == 0) {
								break;
							}
						}
						insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
					} catch (Exception e) {
						System.out.println("更新最终状态时候出现了异常："+e.toString());
						tracer.addTag("listfuture--ERROR", taskInsurance.getTaskid() + "---ERROR:" + e);
						insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
					}
				}else{
					tracer.addTagWrap("获取爬取所有数据需要的参数时请求链接返回为空,故更新爬取状态为完成", html);
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getDescription());
			        taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getPhase());
			        taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getPhasestatus());
			        taskInsurance.setFinished(true);
			        taskInsuranceRepository.save(taskInsurance);
			        insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
				}
			}
		} catch (Exception e) {
			tracer.addTag("爬取所有信息的时候需从此请求中获取部分参数，但是出现了异常，故此处决定将数据爬取信息为完成。异常信息是：", e.toString());
			System.out.println("爬取所有信息的时候需从此请求中获取部分参数，但是出现了异常，故此处决定将数据爬取信息为完成。异常信息是："+ e.toString());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getDescription());
	        taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getPhase());
	        taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getPhasestatus());
	        taskInsurance.setFinished(true);
	        taskInsuranceRepository.save(taskInsurance);
	        insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
		return taskInsurance;
	}
	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
