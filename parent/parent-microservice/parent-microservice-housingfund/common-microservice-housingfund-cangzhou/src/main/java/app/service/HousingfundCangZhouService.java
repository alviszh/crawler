package app.service;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import app.commontracerlog.TracerLog;
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
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.cangzhou.HousingCangZhouUserInfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.cangzhou.HousingCangZhouUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.parser.HousingFundCangZhouParser;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.cangzhou"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.cangzhou"})
public class HousingfundCangZhouService extends HousingBasicService implements ICrawlerLogin{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TaskHousingRepository taskHousingRepository;
	@Autowired
	private HousingFundCangZhouParser housingFundCangZhouParser;
	@Autowired
	private HousingCangZhouUserInfoRepository housingCangZhouUserInfoRepository;
	String html2 = null;
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		tracer.addTag("action.cangzhou.login", messageLoginForHousing.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://www.czgjj.net/";
		try {
			HtmlPage page = (HtmlPage)getHtml(url, webClient);
			HtmlTextInput xm = (HtmlTextInput) page.getElementById("xm");//真实姓名
			HtmlTextInput sfzh = (HtmlTextInput) page.getElementById("sfzh");//身份证
			//input_02
			HtmlTextInput yzm = (HtmlTextInput) page.getElementByName("CheckCodex");
			HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@alt='点击更换验证码']");
			String img = chaoJiYingOcrService.getVerifycode(image, "1902");

			xm.setText(messageLoginForHousing.getUsername());
			sfzh.setText(messageLoginForHousing.getNum());
			yzm.setText(img);
			HtmlSubmitInput submit = (HtmlSubmitInput)page.getElementByName("button");
			Page page2 = submit.click();
			Thread.sleep(2000);
			html2 = page2.getWebResponse().getContentAsString();
			if(html2.indexOf("查询结果")!=-1){
				System.out.println("登录成功");
				String cookies = CommonUnit.transcookieToJson(webClient);
				
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setCookies(cookies);
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
				save(taskHousing);
				
			}
			else{
				System.out.println("登录失败");
				Document doc = Jsoup.parse(html2);
				String text = doc.getElementsByClass("box").get(0).text();
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhasestatus());
				taskHousing.setDescription(text);
				save(taskHousing);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("action.cangzhou.login", "登录页错误："+e.toString());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_INVALID.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_INVALID.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_INVALID.getDescription());
			save(taskHousing);
		}finally {
			if(webClient!=null){
				webClient.close();
			}
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
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		
		HousingCangZhouUserInfo housingCangZhouUserInfo = housingFundCangZhouParser.crawler(html2);
		
		if(housingCangZhouUserInfo!=null){
			housingCangZhouUserInfo.setTaskid(taskHousing.getTaskid());
			housingCangZhouUserInfo.setUserid(messageLoginForHousing.getUser_id());
			housingCangZhouUserInfoRepository.save(housingCangZhouUserInfo);
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
			taskHousing.setUserinfoStatus(200);
			taskHousing.setPaymentStatus(201);
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
		TaskHousing updateTaskHousing = updateTaskHousing(messageLoginForHousing.getTask_id());
		return updateTaskHousing;
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
