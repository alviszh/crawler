package app.service;

import java.net.URL;

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
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.nanping.HousingNanPingUserInfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.nanping.HousingNanPingUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundNanPingParser;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.nanping"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.nanping"})
public class HousingfundNanPingService extends HousingBasicService implements ICrawlerLogin{

	@Autowired
	private TracerLog tracer;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TaskHousingRepository taskHousingRepository;
	@Autowired
	private HousingFundNanPingParser housingFundNanPingParser;
	@Autowired
	private HousingNanPingUserInfoRepository housingNanPingUserInfoRepository;
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		tracer.addTag("action.NanPing.login", taskHousing.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		try {

			String url = "http://103.37.61.29/saving/login.aspx";
			HtmlPage pag = (HtmlPage) getHtml(url, webClient);
			HtmlButtonInput button = pag.getFirstByXPath("//input[@type='button']");
			HtmlPage page = button.click();
			Thread.sleep(2000);
			HtmlTextInput user = (HtmlTextInput) page.getElementById("ctl00_cphMain_tbxUserId");
			HtmlPasswordInput pass = (HtmlPasswordInput) page.getElementById("ctl00_cphMain_tbxPasswd");

			HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@id='imgCheckCode']");//验证码
			String img = chaoJiYingOcrService.getVerifycode(image, "1005");
			HtmlTextInput yzm = (HtmlTextInput) page.getElementById("ctl00_cphMain_tbxCheck");
			user.setText(messageLoginForHousing.getNum());
			pass.setText(messageLoginForHousing.getPassword());
			yzm.setText(img);
			HtmlSubmitInput sub = (HtmlSubmitInput) page.getElementById("ctl00_cphMain_Button1");
			HtmlPage page2 = sub.click();
			String asString = page2.getWebResponse().getContentAsString();
			if(asString.indexOf("公积金查询_福建省南平市住房公积金管理中心")==-1){
				String url2 = "http://103.37.61.29/saving/Main.aspx";
				HtmlPage page3 = (HtmlPage) getHtml(url2, webClient);
				String html2 = page3.getWebResponse().getContentAsString();
				System.out.println(html2);
				if(html2.indexOf("退出")!=-1){
					System.out.println("登录成功");
					String cookies = CommonUnit.transcookieToJson(webClient);
					taskHousing.setCity(messageLoginForHousing.getCity());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setCookies(cookies);
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
					save(taskHousing);

					HousingNanPingUserInfo housingNanPingUserInfo = housingFundNanPingParser.crawler(html2);

					if(housingNanPingUserInfo!=null){
						housingNanPingUserInfo.setTaskid(taskHousing.getTaskid());
						housingNanPingUserInfoRepository.save(housingNanPingUserInfo);
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
				}else{
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getDescription());
					save(taskHousing);
				}
			}else{
				String asText = page2.getElementById("ctl00_cphMain_lblMsg").asText();
				System.out.println("登录失败"+asText);
				if(asText.equals("错误: 验证码错")){
					asText = "网络异常请重新登录";
					taskHousing.setError_message("错误: 验证码错");
				}
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhasestatus());
				taskHousing.setDescription(asText);
				save(taskHousing);
			}

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("action.NanPing.login", "登录页错误："+e.toString());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_INVALID.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_INVALID.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_INVALID.getDescription());
			save(taskHousing);
		}
		return taskHousing;
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
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
