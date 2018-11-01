package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.lianyungang.HousingLianYunGangPay;
import com.microservice.dao.entity.crawler.housing.lianyungang.HousingLianYunGangUserinfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingLianYunGangParser {
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	
	public WebParam login(MessageLoginForHousing messageLoginForHousing) throws Exception{
		// TODO Auto-generated method stub
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		tracer.addTag("login.parser.taskid", messageLoginForHousing.getTask_id());
		WebParam webParam = new WebParam();
		
		String url = "https://12329.lygzfgjj.com.cn/BSS_GR/Gjjmxcx/Gjjmxcx?MenuID=1006";
		tracer.addTag("login.parser.url", url);
		webParam.setUrl(url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage loginPage = webClient.getPage(webRequest);
		tracer.addTag("login.parser.loginPage", "<xmp>"+loginPage.asXml()+"</xmp>");
		
		HtmlButton btnminlogin = (HtmlButton)loginPage.getFirstByXPath("//button[@id='btnminlogin']");
		if(null != btnminlogin){
			HtmlTextInput usercode = (HtmlTextInput)loginPage.getFirstByXPath("//input[@id='usercode']");
			HtmlPasswordInput password = (HtmlPasswordInput)loginPage.getFirstByXPath("//input[@id='password']");
			HtmlTextInput checkCode = (HtmlTextInput)loginPage.getFirstByXPath("//input[@id='checkCode']");
			HtmlImage imgVerifyCode = (HtmlImage)loginPage.getFirstByXPath("//img[@id='imgVerifyCode']");
			
			String verifycode = chaoJiYingOcrService.getVerifycode(imgVerifyCode, "6001");
			
			usercode.setText(messageLoginForHousing.getNum());
			password.setText(messageLoginForHousing.getPassword());
			checkCode.setText(verifycode);
			HtmlPage loginedPage = btnminlogin.click();
			tracer.addTag("login.parser.loginedPage", "<xmp>"+loginedPage.asXml()+"</xmp>");
			
			HtmlElement error = loginedPage.getFirstByXPath("//*[@id='longinerror']");
			if(null != error){
				if(error.asText().equals(" ") && loginedPage.asXml().contains("身份证号后四位")){
					tracer.addTag("login.parser.success", "登录成功，可以抓取数据！");
					webParam.setHtmlPage(loginedPage);
				}else{
					tracer.addTag("login.parser.fail", "登陆失败，失败原因:"+error.asText());
					webParam.setHtml(error.asText());
				}
			}else{
				tracer.addTag("login.parser.fail2", "登陆失败，失败原因:网站页面不正确。");
				webParam.setHtml("网络异常，请您稍后重试！");
			}
		}else{
			webParam.setHtml("网络异常，请您稍后重试！");
		}
		return webParam;
	}

	public WebParam getUserinfo(String html, TaskHousing taskHousing) throws Exception{
		tracer.addTag("login.parser.getUserinfo.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		Document document = Jsoup.parse(html);
		String personalNum = document.getElementById("AcntNo8").text();
		String companyNum = document.getElementById("AcntNo6").text();
		String companyName = document.getElementById("DWName").text();
		String remark = document.getElementById("Remark").text();
		
		List<HousingLianYunGangUserinfo> userinfos = new ArrayList<HousingLianYunGangUserinfo>();
		HousingLianYunGangUserinfo userinfo = new HousingLianYunGangUserinfo();
		if(null != personalNum && !personalNum.trim().equals("")){
			userinfo.setTaskid(taskHousing.getTaskid());
			userinfo.setPersonalNum(personalNum);
			userinfo.setCompanyNum(companyNum);
			userinfo.setCompanyName(companyName);
			userinfo.setMark(remark);
			userinfos.add(userinfo);
		}
		webParam.setList(userinfos);
		return webParam;
	}

	public WebParam getPayinfo(String html, TaskHousing taskHousing) throws Exception{
		tracer.addTag("login.parser.getPayinfo.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		Document document = Jsoup.parse(html);
		Element tablelist = document.getElementById("tablelist");
		Elements trs = tablelist.select("tr");
		if(null != trs && trs.size() > 1){
			List<HousingLianYunGangPay> pays = new ArrayList<HousingLianYunGangPay>();
			for (int i = 1; i < trs.size(); i++) {
				Element tr = trs.get(i);
				Elements tds = tr.select("td");
				
				HousingLianYunGangPay pay = new HousingLianYunGangPay();
				String date = tds.get(0).text();
				String remark = tds.get(1).text();
				String outMoney = tds.get(2).text();
				String getMoney = tds.get(3).text();
				String balance = tds.get(4).text();
				String mark = tds.get(5).text();
				
				pay.setTaskid(taskHousing.getTaskid());
				pay.setDate(date);
				pay.setRemark(remark);
				pay.setOutMoney(outMoney);
				pay.setGetMoney(getMoney);
				pay.setBalance(balance);
				pay.setMark(mark);
				pays.add(pay);
			}
			webParam.setList(pays);
		}
		return webParam;
	}
}
