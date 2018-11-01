package app.service;

import java.io.IOException;
import java.net.URL;
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
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.liuzhou.HousingFundLiuZhouPay;
import com.microservice.dao.entity.crawler.housing.liuzhou.HousingFundLiuZhouUserinfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.liuzhou.HousingFundLiuZhouRepositoryHtml;
import com.microservice.dao.repository.crawler.housing.liuzhou.HousingFundLiuZhouRepositoryPay;
import com.microservice.dao.repository.crawler.housing.liuzhou.HousingFundLiuZhouRepositoryUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundLiuZhouParser;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.liuzhou"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.liuzhou"})
public class HousingfundLiuZhouService extends HousingBasicService implements ICrawlerLogin{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TaskHousingRepository taskHousingRepository;
	@Autowired
	private HousingFundLiuZhouParser housingFundLiuZhouParser;
	@Autowired
	private HousingFundLiuZhouRepositoryUserInfo housingFundLiuZhouRepositoryUserInfo;
	@Autowired
	private HousingFundLiuZhouRepositoryPay housingFundLiuZhouRepositoryPay;
	@Autowired
	private HousingFundLiuZhouRepositoryHtml housingFundLiuZhouRepositoryHtml;
	/**
	 * 登录
	 * @param messageLoginForHousing
	 * @param taskHousing
	 */
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String url = "http://www.lzzfgjj.com/login.jspx";
			HtmlPage page = getHtml(url, webClient);

			HtmlTextInput username = (HtmlTextInput) page.getElementById("username");
			HtmlPasswordInput pass = (HtmlPasswordInput) page.getElementById("password");
			HtmlTextInput captcha = (HtmlTextInput) page.getElementById("captcha");
			HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@src='/captcha.svl']");//验证码
			HtmlSubmitInput submit = (HtmlSubmitInput) page.getElementById("submit");

			String verifycode = chaoJiYingOcrService.getVerifycode(image, "1902");
			username.setText(messageLoginForHousing.getNum());//452225198306020021
			pass.setText(messageLoginForHousing.getPassword());//0781065778
			captcha.setText(verifycode);
			submit.click();
			String url2 = "http://www.lzzfgjj.com/grcx/grcx_grjbqk.jspx";
			Page page3 = getHtml1(url2, webClient);
			String html2 = page3.getWebResponse().getContentAsString();
			if(html2.indexOf("退出")!=-1){
				System.out.println("登录成功");
				String cookies = CommonUnit.transcookieToJson(webClient);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setCookies(cookies);
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
				taskHousing.setPassword(messageLoginForHousing.getPassword());
				taskHousing.setCity(messageLoginForHousing.getCity());
				save(taskHousing);
			}else{
				String alertMsg = WebCrawler.getAlertMsg();
				System.out.println("登录失败:"+alertMsg);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription(alertMsg);
				save(taskHousing);
			}
		} catch (Exception e) {
			tracer.addTag("action.liuzhou.login", "登录页错误："+e.toString());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getDescription());
			save(taskHousing);
		}
		return taskHousing;
	}
	/***
	 * 爬取
	 * @param messageLogin
	 * @param taskHousing
	 */
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies = taskHousing.getCookies();
		Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
		Iterator<Cookie> i = set.iterator();
		while(i.hasNext()){
			webClient.getCookieManager().addCookie(i.next());
		}
		//个人信息
		String url = "http://www.lzzfgjj.com/grcx/grcx_grjbqk.jspx";
		try {
			Page page = getHtml1(url, webClient);
			String html = page.getWebResponse().getContentAsString();
			if(html!=null){
				HousingFundLiuZhouUserinfo userinfo = housingFundLiuZhouParser.getuserinfo(html,taskHousing.getTaskid());
				if(userinfo!=null){
					housingFundLiuZhouRepositoryUserInfo.save(userinfo);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskHousing.setUserinfoStatus(200);
					save(taskHousing);
				}else{
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskHousing.setError_message("个人信息解析有误！");
					taskHousing.setUserinfoStatus(201);
					save(taskHousing);
				}
			}else{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
				taskHousing.setError_message("个人信息网页有误！");
				taskHousing.setUserinfoStatus(500);
				save(taskHousing);
			}
			//流水
			String url2 = "http://www.lzzfgjj.com/grcx/grcx_grzmmx.jspx";
			Page page2 = getHtml1(url2, webClient);
			String html2 = page2.getWebResponse().getContentAsString();
			if(html2!=null){
				List<HousingFundLiuZhouPay> list = housingFundLiuZhouParser.getpay(html2,taskHousing.getTaskid());
				if(list!=null){
					housingFundLiuZhouRepositoryPay.saveAll(list);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
					taskHousing.setPaymentStatus(200);
					save(taskHousing);
				}else{
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
					taskHousing.setPaymentStatus(201);
					save(taskHousing);
				}
			}else{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
				taskHousing.setPaymentStatus(500);
				save(taskHousing);
			}
		} catch (Exception e) {
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_INVALID.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_INVALID.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_INVALID.getDescription());
			save(taskHousing);
		}
		taskHousing = updateTaskHousing(taskHousing.getTaskid());
		return taskHousing;
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	public static Page getHtml1(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
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
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
