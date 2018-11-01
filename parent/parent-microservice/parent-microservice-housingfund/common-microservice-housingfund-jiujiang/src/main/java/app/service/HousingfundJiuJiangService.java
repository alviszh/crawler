package app.service;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.jiujiang.HousingJiuJiangUserInfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.jiujiang.HousingJiuJiangUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundJiuJiangParser;
import app.service.common.HousingBasicService;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.jiujiang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.jiujiang"})
public class HousingfundJiuJiangService extends HousingBasicService {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TaskHousingRepository taskHousingRepository;
	@Autowired
	private HousingFundJiuJiangParser housingFundJiuJiangParser;
	@Autowired
	private HousingJiuJiangUserInfoRepository housingJiuJiangUserInfoRepository;
	public void login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {

		tracer.addTag("action.JiuJiang.login", taskHousing.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://www.jjzfgjj.com/caxun.aspx";
		try {
			HtmlPage pag = (HtmlPage) getHtml(url, webClient);
			HtmlImage imag = (HtmlImage) pag.getFirstByXPath("//img[@id='captchaimg']");
			HtmlPage page = (HtmlPage) imag.click();
			
			HtmlTextInput username = (HtmlTextInput) page.getElementById("UserCard");
			HtmlPasswordInput pass = (HtmlPasswordInput) page.getElementById("Password");
			
			HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@id='captchaimg']");//验证码
			HtmlTextInput yzm = (HtmlTextInput) page.getElementByName("CaptchaText");
			String img = chaoJiYingOcrService.getVerifycode(image, "1902");

			username.setText(messageLoginForHousing.getNum());
			pass.setText(messageLoginForHousing.getPassword());
			yzm.setText(img);
			
			HtmlSubmitInput button1 = (HtmlSubmitInput) page.getElementById("Button1");
			Page page2 = button1.click();
			String error = WebCrawler.getAlertMsg();
			Thread.sleep(2000);
			String html2 = page2.getWebResponse().getContentAsString();
			if(html2.indexOf("单位名称：")!=-1){
				System.out.println("登录成功");
				String cookies = CommonUnit.transcookieToJson(webClient);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setCookies(cookies);
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
				save(taskHousing);
				
				HousingJiuJiangUserInfo housingJiuJiangUserInfo = housingFundJiuJiangParser.crawler(html2);
				
				if(housingJiuJiangUserInfo!=null){
					housingJiuJiangUserInfo.setTaskid(taskHousing.getTaskid());
					housingJiuJiangUserInfoRepository.save(housingJiuJiangUserInfo);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
					taskHousing.setUserinfoStatus(200);
					taskHousing.setPaymentStatus(200);
					save(taskHousing);
				}
				else{
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
					taskHousing.setError_code(StatusCodeRec.CRAWLER_AccountMsgStatus_ERROR.getCode());
					taskHousing.setError_message(StatusCodeRec.CRAWLER_AccountMsgStatus_ERROR.getMessage());
					taskHousing.setUserinfoStatus(201);
					taskHousing.setPaymentStatus(201);
					save(taskHousing);
					tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
				}
				updateTaskHousing(messageLoginForHousing.getTask_id());
			}
			else{
				System.out.println("登录失败");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhasestatus());
				taskHousing.setDescription(error);
				save(taskHousing);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("action.JiuJiang.login", "登录页错误："+e.toString());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_INVALID.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_INVALID.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_INVALID.getDescription());
			save(taskHousing);
		}
		
	}
	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	public void save(TaskHousing taskHousing){
		taskHousingRepository.save(taskHousing);
	}
	
}
