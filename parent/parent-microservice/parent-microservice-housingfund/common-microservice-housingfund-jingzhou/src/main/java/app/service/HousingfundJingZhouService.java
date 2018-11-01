package app.service;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.jingzhou.HousingJingZhouPay;
import com.microservice.dao.entity.crawler.housing.jingzhou.HousingJingZhouUserinfo;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.housing.jingzhou.HousingJingZhouPayRepository;
import com.microservice.dao.repository.crawler.housing.jingzhou.HousingJingZhouUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.parser.HousingfundJingZhouParser;
import app.service.common.HousingBasicService;
import app.service.common.LoginAndGetCommon;
import app.service.common.aop.ICrawler;
import app.service.common.aop.ICrawlerLogin;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.jingzhou"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.jingzhou"})
public class HousingfundJingZhouService extends HousingBasicService implements ICrawler,ICrawlerLogin{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private HousingfundJingZhouParser housingfundJingZhouParser;
	@Autowired
	private HousingJingZhouPayRepository housingJingZhouPayRepository;
	@Autowired
	private HousingJingZhouUserInfoRepository housingJingZhouUserInfoRepository;
	/***
	 * 登录
	 * @param messageLoginForHousing
	 * @param taskHousing
	 */
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		String messageLoginJson = gs.toJson(messageLoginForHousing);
		taskHousing.setLoginMessageJson(messageLoginJson);
		System.out.println(messageLoginForHousing.toString());
		tracer.addTag("parser.crawler.taskid", taskHousing.getTaskid());
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getDescription());
		save(taskHousing);
		
		String url = "http://58.54.135.133/wt-web/login";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		try{
			HtmlPage page = (HtmlPage) LoginAndGetCommon.getHtml(url, webClient);
			HtmlTextInput username = (HtmlTextInput) page.getElementById("username");//身份证
			HtmlPasswordInput pass = (HtmlPasswordInput) page.getElementById("in_password");//密码
			HtmlTextInput captcha = (HtmlTextInput) page.getElementById("captcha");//验证码

			HtmlImage img = (HtmlImage) page.getFirstByXPath("//img[@src='/wt-web/captcha']");//验证码

			String image = chaoJiYingOcrService.getVerifycode(img, "1902");
			username.setText(messageLoginForHousing.getNum());//身份证
			pass.setText(messageLoginForHousing.getPassword());//密码
			captcha.setText(image);

			HtmlButton login = (HtmlButton) page.getFirstByXPath("//button[@onclick='individualSubmitForm();']");
			HtmlPage page3 = login.click();
			
			String url2 = "http://58.54.135.133/wt-web/home?logintype=1";
			Page page2 = LoginAndGetCommon.getHtml(url2, webClient);
			String html = page2.getWebResponse().getContentAsString();

			System.out.println(html);
			if(html.indexOf("加载中 ...")!=-1){
				System.out.println("登录成功");
				String cookies = CommonUnit.transcookieToJson(webClient);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setCookies(cookies);
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
				save(taskHousing);
			}else{
				String error = "用户名、密码是否正确？请重试";
				if(page3.asText().contains("身份证不能为空")){
					error = "身份证不能为空";
				}else if(page3.asText().contains("密码不能为空")){
					error = "密码不能为空";
				}else if(page3.asText().contains("密码不正确")){
					error = "密码不正确";
				}else if(page3.asText().contains("密码格式不正确")){
					error = "密码格式不正确";
				}else if(page3.asText().contains("身份证格式错误")){
					error = "身份证格式错误";
				}else if(page3.asText().contains("验证码格式不正确")){
					error = "网络繁忙，请一分钟后重试！";
				}else if(page3.asText().contains("验证码不能为空")){
					error = "网络有延迟，请重新登录";
				}else if(page3.asText().contains("验证码错误")){
					error = "网络繁忙，请重试！";
				}
				System.out.println("登录失败:"+error);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhasestatus());
				taskHousing.setDescription(error);
				save(taskHousing);
			}

		}catch (Exception e) {
			tracer.addTag("parser.login.taskid", e.getMessage());
			System.out.println("登录失败");
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhase());
			save(taskHousing);
		}
		return taskHousing;
	}
	/**
	 * 爬取
	 * @param messageLogin
	 * @param taskHousing
	 * @param webClient 
	 */
	public TaskHousing getcrawler(MessageLoginForHousing messageLogin, TaskHousing taskHousing) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getDescription());
		save(taskHousing);
		String cookies = taskHousing.getCookies();
		Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
		Iterator<Cookie> i = set.iterator();
		while(i.hasNext()){
			webClient.getCookieManager().addCookie(i.next());
		}

		String date = new Date().toLocaleString().substring(0, 9);
		String url= "http://58.54.135.133/wt-web/personal/jcmxlist?"
					+ "UserId=1"
					+ "&beginDate=2000-01-01"
					+ "&endDate="+date
					+ "&userId=1"
					+ "&pageNum=1"
					+ "&pageSize=1000";
		try {
			Page page3 = gethtmlPost(webClient, null, url);
			String html = page3.getWebResponse().getContentAsString();
			if(html.indexOf("success")!=-1){
				List<HousingJingZhouPay> list = housingfundJingZhouParser.getcrawler(html,messageLogin.getTask_id());
				if(null!=list){
					housingJingZhouPayRepository.saveAll(list);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
					taskHousing.setPaymentStatus(200);
					save(taskHousing);
				}else{
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getDescription());
					taskHousing.setPaymentStatus(201);
					save(taskHousing);
				}
			}else{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getDescription());
				taskHousing.setPaymentStatus(500);
				save(taskHousing);
			}
		} catch (Exception e) {
			tracer.addTag("parser.crawler.taskid", e.getMessage());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getDescription());
			taskHousing.setPaymentStatus(404);
			save(taskHousing);
		}

		return taskHousing;
	}
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}
	/**
	 * 个人信息
	 * @param messageLogin
	 * @param taskHousing
	 */
	public TaskHousing getuserinfo(MessageLoginForHousing messageLogin, TaskHousing taskHousing) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies = taskHousing.getCookies();
		Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
		Iterator<Cookie> i = set.iterator();
		while(i.hasNext()){
			webClient.getCookieManager().addCookie(i.next());
		}
		try {
			String userurl = "http://58.54.135.133/wt-web/person/bgcx";
			Page page = gethtmlPost(webClient, null, userurl);
			String html = page.getWebResponse().getContentAsString();

			if(html.indexOf("success")!=-1){
				HousingJingZhouUserinfo user = housingfundJingZhouParser.getuserinfo(html);
				if(user!=null){
					user.setTaskid(messageLogin.getTask_id());
					housingJingZhouUserInfoRepository.save(user);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskHousing.setUserinfoStatus(200);
					save(taskHousing);
				}else{
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());
					taskHousing.setUserinfoStatus(201);
					save(taskHousing);
				}
			}else{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());
				taskHousing.setUserinfoStatus(500);
				save(taskHousing);
			}

		} catch (Exception e) {
			tracer.addTag("parser.crawler.taskid", e.getMessage());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());
			taskHousing.setUserinfoStatus(404);
			save(taskHousing);
		}
		return taskHousing;
	}
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		getcrawler(messageLoginForHousing, taskHousing);
		getuserinfo(messageLoginForHousing, taskHousing);
		TaskHousing updateTaskHousing = updateTaskHousing(messageLoginForHousing.getTask_id());
		return updateTaskHousing;
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		return null; 
	}
}
