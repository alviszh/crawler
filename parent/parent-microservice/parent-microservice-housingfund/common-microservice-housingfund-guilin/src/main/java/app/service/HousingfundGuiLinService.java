package app.service;

import java.io.IOException;
import java.net.URL;
import java.util.List;

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
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.guilin.HousingGuiLinPay;
import com.microservice.dao.repository.crawler.housing.guilin.HousingGuiLinPayRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundGuiLinParser;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.guilin"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.guilin"})
public class HousingfundGuiLinService extends HousingBasicService implements ICrawlerLogin{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingGuiLinPayRepository housingGuiLinPayRepository;
	@Autowired
	private HousingFundGuiLinParser housingFundGuiLinParser;
	String html;
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {

		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		tracer.addTag("action.guilin.login", taskHousing.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		try {
			String url = "http://www.glzfgjj.cn:8136/";
			HtmlPage page = getHtml(url, webClient);
			HtmlTextInput username = (HtmlTextInput) page.getElementById("ctl00_ContentPlaceHolder1_TextBox1");
			HtmlPasswordInput pass = (HtmlPasswordInput) page.getElementById("ctl00_ContentPlaceHolder1_TextBox2");
			HtmlSubmitInput login = (HtmlSubmitInput) page.getElementById("ctl00_ContentPlaceHolder1_btnSearch");

			username.setText(messageLoginForHousing.getUsername());//450324199303132249
			pass.setText(messageLoginForHousing.getPassword());//132249
			Page page2 = login.click();
			html = page2.getWebResponse().getContentAsString();
			System.out.println(html);
			if(html.indexOf("桂林市职工住房公积金缴存情况")!=-1){
				System.out.println("成功");
				String cookies = CommonUnit.transcookieToJson(webClient);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setCookies(cookies);
				taskHousing.setPassword(messageLoginForHousing.getPassword());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
				save(taskHousing);
			}else{
				System.out.println("失败");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhasestatus());
				taskHousing.setDescription("抱歉！身份证或密码错误！");
				taskHousing.setError_message("密码与身份证登录不符");
				save(taskHousing);
			}
		} catch (Exception e) {
			tracer.addTag("action.guilin.login", "登录页错误："+e.toString());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhasestatus());
			taskHousing.setDescription("抱歉！系统繁忙，请稍后再试！");
			taskHousing.setError_message("网站登录有问题");
			save(taskHousing);
		}
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
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		List<HousingGuiLinPay> list = housingFundGuiLinParser.getpay(messageLoginForHousing.getTask_id(),html);
		if(list!=null){
			housingGuiLinPayRepository.saveAll(list);
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
			taskHousing.setUserinfoStatus(201);
			taskHousing.setPaymentStatus(200);
			save(taskHousing);
		}else{
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
			taskHousing.setUserinfoStatus(201);
			taskHousing.setPaymentStatus(201);
			save(taskHousing);
		}
		
		taskHousing = updateTaskHousing(taskHousing.getTaskid());
		
		return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}


}
