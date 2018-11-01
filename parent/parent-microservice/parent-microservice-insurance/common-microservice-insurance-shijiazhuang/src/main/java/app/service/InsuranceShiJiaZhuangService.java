package app.service;

import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.shijiazhuang.HtmlStoreShiJiaZhuang;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.shijiazhuang.HtmlStoreShiJiaZhuangRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.shijiazhuang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.shijiazhuang"})
public class InsuranceShiJiaZhuangService implements InsuranceLogin {
	public static final Logger log = LoggerFactory.getLogger(InsuranceShiJiaZhuangService.class);
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private BasicUserShiJiaZhuangService basicUserShiJiaZhuangService;
	@Autowired
	private StreamMedicalShiJiaZhuangService streamMedicalShiJiaZhuangService;
	@Autowired
	private StreamAgedShiJiaZhuangService streamAgedShiJiaZhuangService;
	@Autowired
	private StreamGeneralShiJiaZhuangService streamGeneralShiJiaZhuangService;
	@Autowired
	private StreamLostWorkShiJiaZhuangService streamLostWorkShiJiaZhuangService;
	@Autowired
	private  HtmlStoreShiJiaZhuangRepository htmlStoreShiJiaZhuangRepository;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	private static int errCount=0;
	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		Map<String, Future<String>> listfuture = new HashMap<>();   //判断异步爬取是否完成
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId().trim());
		try {
			//爬取用户基本信息
			Future<String> future=basicUserShiJiaZhuangService.getUserInfo(insuranceRequestParameters,taskInsurance);
			listfuture.put("getUserInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
			//没有生育险和工伤险，为了最后更改状态成功，此处设置为定值
			insuranceService.changeCrawlerStatus("【个人社保-生育保险】无可采集数据！", InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
					 200, taskInsurance);
			insuranceService.changeCrawlerStatus("【个人社保-工伤保险】无可采集数据！", InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
					 200, taskInsurance);
		}
		try {
			//流水公共信息爬取
			Future<String> future=streamGeneralShiJiaZhuangService.getStreamGeneral(insuranceRequestParameters,taskInsurance);
			listfuture.put("getStreamGeneral", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getStreamGeneral.e", taskInsurance.getTaskid()+"===>"+e.toString());
		}
		try{
			//爬取医疗保险
			Future<String> future=streamMedicalShiJiaZhuangService.getStreamMedicalDetail(insuranceRequestParameters,taskInsurance);
			listfuture.put("getStreamMedicalDetail", future);
		}catch(Exception e){
			tracer.addTag("action.crawler.getStreamMedicalDetail.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		try {
			//爬取失业保险
			Future<String> future=streamLostWorkShiJiaZhuangService.getStreamLostWorkDetail(insuranceRequestParameters,taskInsurance);
			listfuture.put("getStreamLostWorkDetail", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getStreamLostWorkDetail.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		try {
			//爬取养老保险
			int thisYear=Calendar.getInstance().get(Calendar.YEAR);
			//养老保险  页面只显示一年的，但是通过改变参数可以得到10年的
			for(int year=thisYear-10;year<=thisYear-1;year++){
				Future<String> future=streamAgedShiJiaZhuangService.getStreamAgedDetail(year,insuranceRequestParameters,taskInsurance); 
				listfuture.put("getStreamAgedDetail"+year, future);
				tracer.addTag("action.crawler.getPension"+year, year+"年养老保险年度信息已经采集完成！"+insuranceRequestParameters.getTaskId());
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getPension.e", insuranceRequestParameters.getTaskId()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		//最终状态的更新
		try {
			while (true) {
				for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
					if (entry.getValue().isDone()) { // 判断是否执行完毕
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
			e.printStackTrace();
			tracer.addTag("listfuture--ERROR", taskInsurance.getTaskid() + "---ERROR:" + e);
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());

		}
		return taskInsurance;
	}
	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			String url = "http://grsbcx.sjz12333.gov.cn/login.do?method=begin";
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage searchPage = webClient.getPage(webRequest);		
			if(null!= searchPage){
				HtmlImage image = searchPage.getFirstByXPath("//img[@id='jcaptcha']");
				String code = chaoJiYingOcrService.getVerifycode(image, "1006");
				//验证登陆信息
				url="http://grsbcx.sjz12333.gov.cn/j_unieap_security_check.do";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				String requestBody=""
						+ "Method=P"
						+ "&pid=1373174326875"
						+ "&j_username="+insuranceRequestParameters.getUsername().trim()+""
						+ "&j_password="+insuranceRequestParameters.getPassword().trim()+""
						+ "&jcaptcha_response="+code.trim()+"";
				webRequest.setRequestBody(requestBody);
				HtmlPage page = webClient.getPage(webRequest);
				if(null!=page){
					String html=page.getWebResponse().getContentAsString();
					HtmlStoreShiJiaZhuang htmlStoreShiJiaZhuang = new HtmlStoreShiJiaZhuang();
					htmlStoreShiJiaZhuang.setPageCount(1);
					htmlStoreShiJiaZhuang.setType("登陆信息验证结果源码页");
					htmlStoreShiJiaZhuang.setTaskid(insuranceRequestParameters.getTaskId());
					htmlStoreShiJiaZhuang.setUrl(url);
					htmlStoreShiJiaZhuang.setHtml(html);
					htmlStoreShiJiaZhuangRepository.save(htmlStoreShiJiaZhuang);			
			    	tracer.addTag("登陆信息验证结果源码页：","已经入库");
					if(html.contains("mianFrame")){
						System.out.println("登陆成功");
						taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance,page);
					}else if(html.contains("si_LoginErrMsg")){ //登陆失败
						if(html.contains("您录入的社会保障号码错误或该号码")){
							taskInsurance = insuranceService.changeLoginStatusIdnumNotExistError(taskInsurance);
							return taskInsurance;
						}else if(html.contains("您输入的密码与您的用户名不匹配")){
							taskInsurance = insuranceService.changeLoginStatusPwdError(taskInsurance);
							return taskInsurance;
						}else if(html.contains("错误的验证码")){
							errCount++;
							tracer.addTag(taskInsurance.getTaskid()+"登录验证码解析错误的次数为：",errCount+"次");
							if (errCount<3){
								tracer.addTag("action.login.captchaErrorCount","解析图片验证码失败"+errCount+"次，重新执行登录方法");
								login(insuranceRequestParameters);
							} else {
								errCount=0;
								taskInsurance = insuranceService.changeLoginStatusCaptError(taskInsurance);
								return taskInsurance;
							}
						}else{  //出现了其他登陆情况
							tracer.addTag("登陆失败，失败原因在调研时没有出现：", "请根据taskid到数据库中查看登陆源码，继续补充优化该程序");
							taskInsurance.setDescription("访问官网过于频繁，请稍后再试~");
							taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
							taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
							taskInsurance = taskInsuranceRepository.save(taskInsurance);
						}
					}else{  //为了方式登陆信息验证后响应回来的页面调研的不充分，出现其他情况，此处添加日志和状态更新处理
						tracer.addTag("登陆失败，为了方式登陆信息验证后响应回来的页面调研的不充分，出现其他情况，此处添加日志和状态更新处理：", "请根据taskid到数据库中查看登陆源码，继续补充优化该程序");
						taskInsurance.setDescription("访问官网过于频繁，请稍后再试~");
						taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
						taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
						taskInsurance = taskInsuranceRepository.save(taskInsurance);
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("登录发生异常，异常信息是：", e.toString());
			taskInsurance.setDescription("访问官网过于频繁，请稍后再试~");
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
		}
		return taskInsurance;
	}
}
