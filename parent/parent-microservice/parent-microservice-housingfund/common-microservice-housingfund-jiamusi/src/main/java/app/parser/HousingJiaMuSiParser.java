package app.parser;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.jiamusi.HousingJiaMuSiPay;
import com.microservice.dao.entity.crawler.housing.jiamusi.HousingJiaMuSiUserinfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;
import app.service.common.LoginAndGetCommon;

@Component
public class HousingJiaMuSiParser {
	@Autowired
	public ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	protected TaskHousingRepository taskHousingRepository;
	@Autowired
	private TracerLog tracer;
	
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		tracer.addTag("parser.login.parser.taskid", taskHousing.getTaskid());
		taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		String loginUrl = "http://www.jmsgjj.org.cn:8080/wt-web/login";
		tracer.addTag("parser.login.parser.url", loginUrl);
		
		WebParam webParam = new WebParam();
		webParam.setUrl(loginUrl);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		HtmlPage loginPage1 = webClient.getPage(webRequest);
		HtmlImage vc1 = loginPage1.getFirstByXPath("//img[@style='cursor: pointer;']");
		if(null != vc1){
			HtmlPage loginPage = (HtmlPage) vc1.click();
			if(loginPage.asXml().contains("立即登录")){
				HtmlImage vc = loginPage.getFirstByXPath("//img[@style='cursor: pointer;']");
				HtmlButton loginbtn = loginPage.getFirstByXPath("//button[@class='btn btn-primary']");
				HtmlTextInput username = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='username']");
				HtmlPasswordInput password = (HtmlPasswordInput) loginPage.getFirstByXPath("//input[@id='password']");
				HtmlTextInput captcha = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='captcha']");
				
				username.setText(messageLoginForHousing.getNum());
				password.setText(messageLoginForHousing.getPassword());
				captcha.setText(chaoJiYingOcrService.getVerifycode(vc, "1004"));
				HtmlPage loginedPage = (HtmlPage) loginbtn.click();
				webParam.setHtmlPage(loginedPage);
			}
		}
		webParam.setWebClient(webClient);
		return webParam;
	}
	

	public WebParam getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		tracer.addTag("crawler.getUserInfo.parser.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = LoginAndGetCommon.addcookie(webClient, taskHousing);
		String userUrl = "http://www.jmsgjj.org.cn:8080/wt-web/person/jbxx";
		webParam.setUrl(userUrl);
		tracer.addTag("crawler.getUserInfo.parser.url", userUrl);
		WebRequest webRequest = new WebRequest(new URL(userUrl), HttpMethod.POST);
		Page page = webClient.getPage(webRequest);
		webParam.setHtml(page.getWebResponse().getContentAsString());
		tracer.addTag("crawler.getUserInfo.parser.userPage", page.getWebResponse().getContentAsString());
		if(page.getWebResponse().getContentAsString().contains("true")){
			String userJson = page.getWebResponse().getContentAsString();
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(userJson); // 创建JsonObject对象
			JsonArray results = object.get("results").getAsJsonArray();
			if(results.size() > 0){
				JsonElement jsonElement = results.get(0);
				JsonObject jsonObject = jsonElement.getAsJsonObject();
				String companyNum = jsonObject.get("a003").getAsString();
				String companyName = jsonObject.get("a004").getAsString();
				String staffNum = jsonObject.get("a001").getAsString();
				String staffName = jsonObject.get("a002").getAsString();
				String state = jsonObject.get("mc").getAsString();
				String cardNum = jsonObject.get("a008").getAsString();
				String openDate = jsonObject.get("a013").getAsString();
				
				HousingJiaMuSiUserinfo userinfo = new HousingJiaMuSiUserinfo();
				userinfo.setTaskid(taskHousing.getTaskid());
				userinfo.setCompanyNum(companyNum);
				userinfo.setCompanyName(companyName);
				userinfo.setStaffNum(staffNum);
				userinfo.setStaffName(staffName);
				userinfo.setState(state);
				userinfo.setCardNum(cardNum);
				userinfo.setOpenDate(openDate);
				
				List<HousingJiaMuSiUserinfo> userinfos = new ArrayList<HousingJiaMuSiUserinfo>();
				userinfos.add(userinfo);
				webParam.setList(userinfos);
			}
		}
		return webParam;
	}


	public WebParam getTrans(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		tracer.addTag("crawler.getTrans.parser.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = LoginAndGetCommon.addcookie(webClient, taskHousing);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String today = format.format(new Date());
		
		String transUrl = "http://www.jmsgjj.org.cn:8080/wt-web/personal/jcmxlist?beginDate="+getDateBefore()+"&endDate="+today+"&UserId=1&pageNum=1&pageSize=500";
		webParam.setUrl(transUrl);
		tracer.addTag("crawler.getTrans.parser.url", transUrl);
		WebRequest webRequest = new WebRequest(new URL(transUrl), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		webParam.setHtml(page.getWebResponse().getContentAsString());
		tracer.addTag("crawler.getTrans.parser.transPage", page.getWebResponse().getContentAsString());
		if(page.getWebResponse().getContentAsString().contains("true")){
			String transJson = page.getWebResponse().getContentAsString();
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(transJson); // 创建JsonObject对象
			JsonArray results = object.get("results").getAsJsonArray();
			if(results.size() > 0){
				List<HousingJiaMuSiPay> pays = new ArrayList<HousingJiaMuSiPay>();
				for (JsonElement jsonElement : results) {
					JsonObject jsonObject = jsonElement.getAsJsonObject();
					String date = jsonObject.get("rq").getAsString();
					String getMoney = jsonObject.get("jfje").getAsString();
					String setMoney = jsonObject.get("dfje").getAsString();
					String balance = jsonObject.get("ye").getAsString();
					String mark = jsonObject.get("zy").getAsString();
					
					HousingJiaMuSiPay pay = new HousingJiaMuSiPay();
					pay.setTaskid(taskHousing.getTaskid());
					pay.setDate(date);
					pay.setGetMoney(getMoney);
					pay.setSetMoney(setMoney);
					pay.setBalance(balance);
					pay.setMark(mark);
					
					pays.add(pay);
				}
				webParam.setList(pays);
			}
		}
		return webParam;
	}
	
	//获取 距今 三年前 的时间
	public String getDateBefore() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -3);
        Date y = c.getTime();
        String year1 = format.format(y);
        return year1;
	}
}
