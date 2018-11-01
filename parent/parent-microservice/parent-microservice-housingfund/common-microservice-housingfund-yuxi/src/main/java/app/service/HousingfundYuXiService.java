package app.service;

import java.net.URL;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.yuxi.HousingYuXiPay;
import com.microservice.dao.entity.crawler.housing.yuxi.HousingYuXiUserinfo;
import com.microservice.dao.repository.crawler.housing.yuxi.HousingYuXiPayRepository;
import com.microservice.dao.repository.crawler.housing.yuxi.HousingYuXiUserinfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundYuXiParser;
import app.service.common.HousingBasicService;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.yuxi"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.yuxi"})
public class HousingfundYuXiService extends HousingBasicService{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundYuXiParser housingFundYuXiParser;
	@Autowired
	private HousingYuXiUserinfoRepository housingYuXiUserinfoRepository;
	@Autowired
	private HousingYuXiPayRepository housingYuXiPayRepository;
	public void login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		tracer.addTag("action.yuxi.login", taskHousing.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://www.yxgjj.com/";
		HtmlPage page = getHtml(url, webClient);
		String contentAsString = page.getWebResponse().getContentAsString();
		System.out.println(contentAsString);
		HtmlCheckBoxInput sle = (HtmlCheckBoxInput)page.getElementById("#");
		sle.click();
		
		HtmlTextInput xm = (HtmlTextInput)page.getElementByName("xm");
		HtmlTextInput id_card_input = (HtmlTextInput)page.getElementByName("id_card_input");
		HtmlSubmitInput submit = (HtmlSubmitInput) page.getFirstByXPath("//input[@class='login-submit']");
		xm.setText(messageLoginForHousing.getUsername());
		id_card_input.setText(messageLoginForHousing.getNum());
		
		Page page2 = submit.click();
		String html = page2.getWebResponse().getContentAsString();
		System.out.println(html);
		
		if(html.indexOf("开始年份：")!=-1){
			System.out.println("登录成功");
			String cookies = CommonUnit.transcookieToJson(webClient);
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
			taskHousing.setCookies(cookies);
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
			save(taskHousing);
			
			HousingYuXiUserinfo userinfo = housingFundYuXiParser.getuserinfo(html,messageLoginForHousing.getTask_id());
			if(userinfo!=null){
				housingYuXiUserinfoRepository.save(userinfo);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
				taskHousing.setUserinfoStatus(200);
			}else{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
				taskHousing.setUserinfoStatus(201);
			}
			
			List<HousingYuXiPay> list = housingFundYuXiParser.getpay(html,messageLoginForHousing.getTask_id());
			if(list!=null){
				housingYuXiPayRepository.saveAll(list);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
				taskHousing.setPaymentStatus(200);
			}else{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
				taskHousing.setPaymentStatus(201);
			}
			updateTaskHousing(messageLoginForHousing.getTask_id());
		}else{
			Document doc = Jsoup.parse(html);
			String error = "网络繁忙";
			Elements elementsByTag = doc.getElementsByTag("td");
			if(elementsByTag.size()>2){
				error = elementsByTag.get(2).text();
			}
			if(elementsByTag.size()<3){
				error = elementsByTag.get(1).text();
			}
			System.out.println("登录失败:"+error);
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhasestatus());
			taskHousing.setDescription(error);
			save(taskHousing);
		}
		
	}
	
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	
}
