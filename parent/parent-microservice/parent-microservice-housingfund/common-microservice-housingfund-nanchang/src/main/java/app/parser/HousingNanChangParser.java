package app.parser;

import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.nanchang.HousingNanChangPay;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingNanChangParser {
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	public WebParam login(WebClient webClient, MessageLoginForHousing messageLoginForHousing,
			TaskHousing taskHousing) throws Exception{
		tracer.addTag("parser.login.parser.login.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		
		String url = "http://www.ncgjj.com.cn:8081/wt-web/login";
		tracer.addTag("parser.login.parser.login.url", url);
		webParam.setUrl(url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage loginPage = webClient.getPage(webRequest);
		tracer.addTag("parser.login.parser.login.page1", "<xmp>"+loginPage.asXml()+"</xmp>");
		if(loginPage.asXml().contains("立即登录")){
			HtmlImage cert = loginPage.getFirstByXPath("//img[@style='cursor: pointer;']");
			HtmlTextInput username = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='username']");
			HtmlPasswordInput in_password = (HtmlPasswordInput) loginPage.getFirstByXPath("//input[@id='in_password']");
			HtmlTextInput captcha = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='captcha']");
			HtmlButton loginButton = loginPage.getFirstByXPath("//button[@id='gr_login']");
			
			username.setText(messageLoginForHousing.getNum());
			in_password.setText(messageLoginForHousing.getPassword());
			captcha.setText(chaoJiYingOcrService.getVerifycode(cert, "1004"));
			HtmlPage loginedPage = (HtmlPage)loginButton.click();
			tracer.addTag("parser.login.parser.login.loginedPage", "<xmp>"+loginedPage.asXml()+"</xmp>");
			webParam.setHtmlPage(loginedPage);
			webParam.setWebClient(webClient);
		}
		return webParam;
	}
	
	public WebParam getPayInfo(WebClient webClient, TaskHousing taskHousing, int i) throws Exception{
		tracer.addTag("parser.crawler.parser.getPayInfo"+i+".taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		String url = "http://www.ncgjj.com.cn:8081/wt-web/person/jczqmxqc";
		tracer.addTag("parser.crawler.parser.getPayInfo"+i+".url", url);
		webParam.setUrl(url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.ncgjj.com.cn:8081");
		webRequest.setAdditionalHeader("Origin", "http://www.ncgjj.com.cn:8081");
		webRequest.setAdditionalHeader("Referer", "http://www.ncgjj.com.cn:8081/wt-web/home?logintype=1");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36");
		
		webRequest.setRequestParameters(new ArrayList<NameValuePair>());
		if(i == 0){
			webRequest.getRequestParameters().add(new NameValuePair("lsnd", "当前年度"));
		}else{
			webRequest.getRequestParameters().add(new NameValuePair("lsnd", getDateBefore(i)));
		}
		webRequest.getRequestParameters().add(new NameValuePair("pageNum", "1"));
		webRequest.getRequestParameters().add(new NameValuePair("pageSize", "1000"));
		webRequest.setCharset(Charset.forName("UTF-8"));
		Page payinfoPage = webClient.getPage(webRequest);
		String payJson = payinfoPage.getWebResponse().getContentAsString();
		webParam.setHtml(payJson);
		webParam.setWebClient(webClient);
		tracer.addTag("parser.crawler.parser.getPayInfo"+i+".page", payinfoPage.getWebResponse().getContentAsString());
		if(null != payinfoPage && 200 == payinfoPage.getWebResponse().getStatusCode()){
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(payJson); // 创建JsonObject对象
			JsonArray results = object.get("results").getAsJsonArray();
			if(null != results && results.size() > 0){
				List<HousingNanChangPay> nanChangPays = new ArrayList<HousingNanChangPay>();
				for (JsonElement jsonel : results) {
					JsonObject result = jsonel.getAsJsonObject();
					String occurDate = result.get("rq").getAsString();
					String mark = result.get("zy").getAsString();
					String incomeMoney = result.get("dfje").getAsString();
					String payMoney = result.get("jfje").getAsString();
					String balance = result.get("ye").getAsString();
					
					HousingNanChangPay nanChangPay = new HousingNanChangPay();
					nanChangPay.setOccurDate(occurDate);
					nanChangPay.setMark(mark);
					nanChangPay.setIncomeMoney(incomeMoney);
					nanChangPay.setPayMoney(payMoney);
					nanChangPay.setBalance(balance);
					
					if(null != nanChangPay){
						nanChangPay.setTaskid(taskHousing.getTaskid());
						nanChangPays.add(nanChangPay);
					}
				}
				if(null != nanChangPays && nanChangPays.size() > 0){
					webParam.setList(nanChangPays);
				}
			}
		}
		return webParam;
	}
	
	public static String getDateBefore(int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -i);
        Date y = c.getTime();
        String year1 = format.format(y);
       
        c.setTime(new Date());
        c.add(Calendar.YEAR, -(i-1));
        Date y2 = c.getTime();
        String year2 = format.format(y2);
        return year1+"-"+year2;
	}
	
	public String getLoginFailMsg(String asXml) throws Exception{
		Document document = Jsoup.parse(asXml);
		Element usernamee = document.getElementById("gr_error_1");
		Element passworde = document.getElementById("in_error");
		Element valicodee = document.getElementById("captcha_error");
		
		String username = usernamee.text();
		String password = passworde.text();
		String valicode = valicodee.text();
		String errorMsg = username+""+password+""+valicode;
		return errorMsg;
	}
}
