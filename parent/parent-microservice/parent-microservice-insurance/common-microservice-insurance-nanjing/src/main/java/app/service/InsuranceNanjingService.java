package app.service;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.nanjing.InsuranceNanJingParams;
import com.microservice.dao.entity.crawler.insurance.nanjing.InsuranceNanjingHtml;
import com.microservice.dao.entity.crawler.insurance.nanjing.InsuranceNanjingUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.nanjing.InsuranceNanJingParamsRepository;
import com.microservice.dao.repository.crawler.insurance.nanjing.InsuranceNanjingHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.nanjing.InsuranceNanjingUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceNanjingParser;
import app.service.aop.InsuranceLogin;
import net.sf.json.JSONObject;


/**
 * @description: 社保登录逻辑判断
 * @author: sln 
 * @date: 2017年9月26日 下午6:23:52 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.nanjing"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.nanjing"})
public class InsuranceNanjingService  implements InsuranceLogin{
	public static final Logger log = LoggerFactory.getLogger(InsuranceNanjingService.class);
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceFiveInsurChangeStatus fiveInsurChangeStatus;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceNanjingHtmlRepository insuranceNanjingHtmlRepository;
	@Autowired
	private InsuranceNanjingUserInfoRepository insuranceNanjingUserInfoRepository;
	//登陆成功后获取的token，用于用户信息的爬取
	@Autowired
	private InsuranceNanJingParamsRepository nanJingParamsRepository;
	@Autowired
	private InsuranceNanjingParser insuranceNanjingParser;
	@Autowired
	private InsuranceNanjingGetAllDataService nanjingGetAllDataService;

	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters){
		TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			String url="https://m.mynj.cn:11097/";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
//			webClient.getOptions().setJavaScriptEnabled(false);   //避免执行js耗费很长时间
			HtmlPage searchPage = webClient.getPage(webRequest);
			if(null!=searchPage){
				url="https://m.mynj.cn:11097/validateByPassword";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				//用户名输错，status是2,密码输错，响应的是1，都输错，响应的是2，登录成功，响应的是0
				String requestBody="formdata={\"UserName\"=\"1884967402\",\"PassWord\"=\"24851954\"}";  
				webRequest.setRequestBody(requestBody);
				Page page = webClient.getPage(webRequest);
				if(null!=page){
					String html = page.getWebResponse().getContentAsString();
					tracer.addTag("验证用户名和密码响应的内容是：",html);
					if(html.contains("status")){  //响应的页面中有响应状态
						String status = JSONObject.fromObject(html).getString("status");
						if(status.equals("0")){  //登录成功
							//获取登录成功之后的token
							taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
							taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
							taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
							String cookies = CommonUnit.transcookieToJson(webClient);
							taskInsurance.setCookies(cookies);
							taskInsurance = taskInsuranceRepository.save(taskInsurance);
							Thread.sleep(1000);
							String token = JSONObject.fromObject(html).getString("token");   //获取的token用于爬取用户信息
							InsuranceNanJingParams nanJingParams=new InsuranceNanJingParams();
							nanJingParams.setTaskid(insuranceRequestParameters.getTaskId());
							nanJingParams.setToken(token);
							nanJingParamsRepository.save(nanJingParams);
							tracer.addTag("登陆成功之后，获取的用于爬取数据的参数已经入库", token);
						}else if(status.equals("1")){  //密码错误
							insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
						}else if(status.equals("2")){ //用户名错误
							insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
						}else{
							insuranceService.changeLoginStatusException(taskInsurance);
						}
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
	
	
	
	/**
	 * 后期测试的过程中发现，要是将cookie存储到数据库中再取出来，就不管用了，
	 * 故经过后期测试，改为直接将cookies作为参数爬取五险信息
	 */
	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			/**
			 * 爬取用户信息之后，直接爬取其他信息，通过测试过程中遇到的问题，发现：
			 * 不能用异步爬取五险信息，不能将用户信息爬取成功之后获取的cookie重新
			 * 从库中取出，或者是直接传参组装成新的webClient对象，否则有时候不起作用，
			 * 故直接用请求用户信息之后获取的webClient即可
			 */
			getUserInfo(taskInsurance);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);	
			//如果爬取用户信息的过程中出现异常，则需要将其他信息的状态一并更新，方式爬取数据的状态卡住
			fiveInsurChangeStatus.changeFiveCrawlerStatusTrue(taskInsurance, 201);
			tracer.addTag("爬取用户信息过程中出现异常，导致无法提供爬取五险信息需要的cookie", "故将五险信息的爬取状态更新为201，以防无法更新最终状态");
		}
		//最终状态的更新
		insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		return taskInsurance;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		return null;
	}
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 5000,multiplier = 1))
	public String getUserInfo(TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setTimeout(30000);
		String cookies=null;
		try {
			InsuranceNanJingParams params = nanJingParamsRepository.findTopByTaskidOrderByCreatetimeDesc(taskInsurance.getTaskid());
			String token = null;
			if(null!=params){
				token = params.getToken();
			}
			String url="https://m.mynj.cn:11096/njwsbs/index.do?method=show&token="+token+"";   //请求连接中拼接着token
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
					Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html = page.getWebResponse().getContentAsString();
				//存储用户信息源码页
				InsuranceNanjingHtml insuranceNanjingHtml = new InsuranceNanjingHtml();
				insuranceNanjingHtml.setTaskid(taskInsurance.getTaskid());
				insuranceNanjingHtml.setType("用户信息源码页");
				insuranceNanjingHtml.setPageCount(1);
				insuranceNanjingHtml.setUrl(url);
				insuranceNanjingHtml.setHtml(html);
				insuranceNanjingHtmlRepository.save(insuranceNanjingHtml);
				tracer.addTag("action.crawler.getUserinfo.html", "个人信息源码页已入库");
				if(html.contains("上次登录时间")){  //包含这个字段，说明正确获取了用户登录信息
					cookies = CommonUnit.transcookieToJson(webClient);
					taskInsurance.setCookies(cookies);
					//将保存后的taskInsurance进行响应，存储用户信息页面正常响应之后的cookie,用户信息页面正确响应之后获取的cookie用于爬取
					taskInsurance = taskInsuranceRepository.save(taskInsurance);
					//获取用户信息解析返回值
					InsuranceNanjingUserInfo insuranceNanjingUserInfo=insuranceNanjingParser.userInfoParser(taskInsurance,html);
					if(null != insuranceNanjingUserInfo){
						insuranceNanjingUserInfoRepository.save(insuranceNanjingUserInfo);
						tracer.addTag("action.crawler.getUserinfo", "个人信息已入库");
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 
								InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
								 200, taskInsurance);
					}
					//成功解析用户信息之后，直接爬取剩余五险信息，用webclient
					try {
						nanjingGetAllDataService.getPension(taskInsurance, webClient);
					} catch (Exception e) {
						tracer.addTag("action.crawler.getPension.e", e.toString());
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
								InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
								201, taskInsurance);		
					}
					try {
						nanjingGetAllDataService.getMedical(taskInsurance, webClient);
					} catch (Exception e) {
						tracer.addTag("action.crawler.getMedical.e", e.toString());
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
								InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
								 201, taskInsurance);
					}
					try {
						nanjingGetAllDataService.getInjury(taskInsurance, webClient);
					} catch (Exception e) {
						tracer.addTag("action.crawler.getInjury.e", e.toString());
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(), 
								InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
								201, taskInsurance);		}
					try {
						nanjingGetAllDataService.getBear(taskInsurance, webClient);
					} catch (Exception e) {
						tracer.addTag("action.crawler.getBear.e", e.toString());
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(), 
								InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
								201, taskInsurance);		}
					try {
						nanjingGetAllDataService.getUnemployment(taskInsurance, webClient);
					} catch (Exception e) {
						tracer.addTag("action.crawler.getUnemployment.e", e.toString());
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
								InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
								201, taskInsurance);
					}
				}else if(html.contains("token无效")){ //token失效      
					insuranceService.changeCrawlerStatus(
							"数据采集中，爬取用户信息过程中token失效，请重新爬取~~~",
							InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
							201, taskInsurance);
					tracer.addTagWrap("爬取用户信息过程中token失效，响应的页面是：", html);
				}else{  
					tracer.addTag("用户基本信息爬取过程中未响应预期页面", "接下来进行请求重试");
					throw new RuntimeException("用户基本信息爬取过程中未响应预期页面，接下来进行重试");
				}
			}
		} catch (Exception e) {
			tracer.addTag("解析用户信息的过程中出现异常：", e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
			//如果爬取用户信息的过程中出现异常，则需要将其他信息的状态一并更新，方式爬取数据的状态卡住
			fiveInsurChangeStatus.changeFiveCrawlerStatusTrue(taskInsurance, 201);
		}
		return cookies;
	}
}
