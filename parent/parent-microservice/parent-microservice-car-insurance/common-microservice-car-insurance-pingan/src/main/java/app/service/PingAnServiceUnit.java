package app.service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.crawler.car.insurance.bean.CarInsuranceRequestBean;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.car.insurance.TaskCarInsurance;
import com.microservice.dao.entity.crawler.car.insurance.pingan.PingAnBasicInformation;
import com.microservice.dao.entity.crawler.car.insurance.pingan.PingAnHtml;
import com.microservice.dao.entity.crawler.car.insurance.pingan.PingAnPayInfo;
import com.microservice.dao.entity.crawler.car.insurance.pingan.PingAnUserInfo;
import com.microservice.dao.repository.crawler.car.insurance.pingan.PingAnBasicInformationRepository;
import com.microservice.dao.repository.crawler.car.insurance.pingan.PingAnHtmlRepository;
import com.microservice.dao.repository.crawler.car.insurance.pingan.PingAnPayInfoRepository;
import com.microservice.dao.repository.crawler.car.insurance.pingan.PingAnUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.parser.PingAnParser;
import app.unit.PingAnUnit;
import net.sf.json.JSONObject;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.car.insurance.pingan"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.car.insurance.pingan"})
public class PingAnServiceUnit {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private PingAnParser pingAnParser;
	@Autowired
	private PingAnBasicInformationRepository pingAnBasicInformationRepository;
	@Autowired
	private PingAnUserInfoRepository pingAnUserInfoRepository;
	@Autowired
	private PingAnPayInfoRepository pingAnPayInfoRepository;
	@Autowired
	private PingAnHtmlRepository pingAnHtmlRepository;
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 1500l,multiplier = 1.5))
	public PingAnUnit verify(CarInsuranceRequestBean carInsuranceRequestBean) {
		String state = null;
		PingAnUnit pingAnUnit = new PingAnUnit();
		PingAnHtml pingAnHtml = new PingAnHtml();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://www.pingan.com/property_insurance/pa18AutoInquiry/login.screen";
		HtmlPage page = null;
		try {
			page = (HtmlPage) getHtml(url, webClient);
		} catch (Exception e) {
			throw new RuntimeException("获取验证界面出错！"+e.getMessage());
		}
		HtmlTextInput vehiclePolicyNo = (HtmlTextInput) page.getElementById("vehiclePolicyNo");//保单号
		tracer.addTag("保单号输入框:", vehiclePolicyNo.asXml());
		HtmlTextInput vehicleIdNo = (HtmlTextInput) page.getElementById("vehicleIdNo");//证件号
		tracer.addTag("身份证号输入框:", vehicleIdNo.asXml());
		HtmlTextInput vehicleVerifyCode = (HtmlTextInput) page.getElementById("vehicleVerifyCode");//验证码输入
		tracer.addTag("验证码输入框:", vehicleVerifyCode.asXml());
		HtmlImage image = (HtmlImage)page.getFirstByXPath("//img[@src='./rand.do']");//验证码
		tracer.addTag("验证码:", image.asXml());
		HtmlAnchor but = (HtmlAnchor) page.getElementById("search");
		tracer.addTag("登录按钮:", but.asXml());
		String code = null;
		try {
			code = chaoJiYingOcrService.getVerifycode(image, "4004");
		} catch (Exception e) {
			throw new RuntimeException("超级鹰解析验证码失败！"+e.getMessage());
		}		
		tracer.addTag("超级鹰解析验证码后结果：", code);
		pingAnUnit.setVerifyCode(code);
		vehiclePolicyNo.setText(carInsuranceRequestBean.getPolicyNum());
		vehicleIdNo.setText(carInsuranceRequestBean.getIdnum());
		vehicleVerifyCode.setText(code);
		HtmlPage page2 = null;
		try {
			page2 = but.click();
		} catch (IOException e) {
			throw new RuntimeException("获取验证后页面失败！"+e.getMessage());
		}
		String contentAsString = page2.getWebResponse().getContentAsString();
		if(contentAsString.indexOf("<title>车险承保理赔信息查询</title>")!=-1){
			tracer.addTag("保单号及其证件号正确，开始发送短信", contentAsString);
			state = "保单号及其证件号正确，开始发送短信";
		}else{
			if(page2.asText().contains("保单号不能为空")){
				tracer.addTag("保单号为空", contentAsString);
				state = "保单号不能为空";
			}else if(page2.asText().contains("保单号不合规范")){
				tracer.addTag("保单号不合规范", contentAsString);
				state = "保单号不合规范";
			}else if(page2.asText().contains("证件号不能为空")){
				System.out.println("证件号不能为空");
				tracer.addTag("证件号为空", contentAsString);
				state = "证件号不能为空";
			}else if(page2.asText().contains("验证码不能为空")){
				tracer.addTag("验证码为空", contentAsString);
				state = "网络异常，请稍后再试！";
			}else if(page2.asText().contains("请输入正确的验证码")){
				tracer.addTag("验证码错误", contentAsString);
				state = "网络异常，请等待几分钟再试！";
			}else if(page2.asText().contains("您查询的车辆未在我公司投保，如有疑问请留言或联系本公司。")){
				tracer.addTag("您查询的车辆未在我公司投保，如有疑问请留言或联系本公司。", contentAsString);
				state = "您查询的车辆未在平安公司投保，如有疑问请留言或联系平安公司。";
			}else if(page2.asText().contains("保单号与证件号不匹配，请核实后重新输入！")){
				tracer.addTag("保单号与证件号不匹配，请核实后重新输入！", contentAsString);
				state = "保单号与证件号不匹配，请核实后重新输入！";
			}else{
				tracer.addTag("未知错误", contentAsString);
				state = "网络繁忙";
			}
		}
		pingAnHtml.setUrl(url);
		pingAnHtml.setHtmlText(contentAsString);
		pingAnHtml.setMessagesText("平安车险登录页");
		pingAnHtml.setTaskid(carInsuranceRequestBean.getTaskid());
		pingAnHtmlRepository.save(pingAnHtml);
		pingAnUnit.setPage(page2);
		pingAnUnit.setState(state);
		return pingAnUnit;
	}


	public PingAnUnit sendSms(CarInsuranceRequestBean carInsuranceRequestBean, TaskCarInsurance taskCarInsurance) throws Exception {
		PingAnUnit pingAnUnit = new PingAnUnit();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskCarInsurance.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		String url = "http://www.pingan.com/property_insurance/pa18AutoInquiry/smbjsSend.do?"
				+ "mobileNo="+carInsuranceRequestBean.getPhoneNum();
		Page page = getHtml(url, webClient);
		String contentAsString = page.getWebResponse().getContentAsString();
		//errorCode
		int errorCode = JSONObject.fromObject(contentAsString).getInt("errorCode");
		if(errorCode==0){
			pingAnUnit.setState("短信发送成功");
			System.out.println("发送成功");
		}else{
			String errorMsg = JSONObject.fromObject(contentAsString).getString("errorMsg");
			pingAnUnit.setState(errorMsg);
		}
		pingAnUnit.setPage(page);
		return pingAnUnit;

	}

	//验证短信
	public PingAnUnit verifySms(CarInsuranceRequestBean carInsuranceRequestBean, TaskCarInsurance taskCarInsurance) throws Exception {
		PingAnUnit pingAnUnit = new PingAnUnit();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskCarInsurance.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		String url = "http://www.pingan.com/property_insurance/pa18AutoInquiry/autoClaimQueryValidate.do?"
				+ "mobileNo="+carInsuranceRequestBean.getPhoneNum()
				+ "&checkNo="+carInsuranceRequestBean.getCode();

		Page page = getHtml(url, webClient);
		String contentAsString = page.getWebResponse().getContentAsString();
		System.out.println(contentAsString);

		if(contentAsString.indexOf("退出登录")!=-1){
			pingAnUnit.setState("验证成功");
			System.out.println("短信验证成功");
		}else{
			pingAnUnit.setState("短信验证码已过期或者输入错误,请检查后重试。");
		}
		return pingAnUnit;


	}

	//投保车辆详情
	public void getBasicInformation(CarInsuranceRequestBean carInsuranceRequestBean,
			TaskCarInsurance taskCarInsurance, WebClient webClient) throws Exception {
		String url = "http://www.pingan.com/property_insurance/pa18AutoInquiry/policyList.do";
		PingAnHtml pingAnHtml = new PingAnHtml();
		Page page = getHtml(url, webClient);
		String contentAsString = page.getWebResponse().getContentAsString();
		System.out.println(contentAsString);
		tracer.addTag("投保车辆详情页面", contentAsString);
		if (contentAsString!=null) {
			List<PingAnBasicInformation> list = pingAnParser.getBasicInformation(contentAsString,taskCarInsurance);
			if(list==null){
				tracer.addTag("投保车辆详情没有数据", contentAsString);
				System.out.println("111");
			}
			tracer.addTag("投保车辆详情", list.toString());
			for (PingAnBasicInformation pingAnBasicInformation : list) {
				pingAnBasicInformationRepository.save(pingAnBasicInformation);
			}
			pingAnHtml.setUrl(url);
			pingAnHtml.setHtmlText(contentAsString);
			pingAnHtml.setMessagesText("投保车辆详情页");
			pingAnHtml.setTaskid(carInsuranceRequestBean.getTaskid());
			pingAnHtmlRepository.save(pingAnHtml);
		}else{
			tracer.addTag("投保车辆详情首页未获取到", contentAsString);
		}
	}

	//投保人详情
	public void getUserInfo(CarInsuranceRequestBean carInsuranceRequestBean, 
			TaskCarInsurance taskCarInsurance, WebClient webClient) throws Exception {
		String url = "http://www.pingan.com/property_insurance/pa18AutoInquiry/policyDetail.do?"
				+ "policyNo="+carInsuranceRequestBean.getPolicyNum();
		PingAnHtml pingAnHtml = new PingAnHtml();
		Page page = getHtml(url, webClient);
		String contentAsString = page.getWebResponse().getContentAsString();
		System.out.println(contentAsString);
		tracer.addTag("投保人详情", contentAsString);
		if(contentAsString!=null){
			PingAnUserInfo pingAnUserInfo = pingAnParser.getUserInfo(contentAsString,taskCarInsurance);
			tracer.addTag("投保人详情数据", pingAnUserInfo.toString());
			if (pingAnUserInfo!=null) {
				pingAnUserInfoRepository.save(pingAnUserInfo);
			}
			List<PingAnPayInfo> PingAnPayInfo = pingAnParser.getPayInfo(contentAsString,taskCarInsurance);
			if (PingAnPayInfo!=null) {
				pingAnPayInfoRepository.saveAll(PingAnPayInfo);
			}else{
				tracer.addTag("投保人缴费详情数据未获取到", PingAnPayInfo.toString());
			}
			
		}else{
			tracer.addTag("投保人详情未获取到", contentAsString);
		}
		pingAnHtml.setUrl(url);
		pingAnHtml.setHtmlText(contentAsString);
		pingAnHtml.setMessagesText("投保人详情页");
		pingAnHtml.setTaskid(carInsuranceRequestBean.getTaskid());
		pingAnHtmlRepository.save(pingAnHtml);
	}

	//出险详情
	public void getAccidentInfo(CarInsuranceRequestBean carInsuranceRequestBean, 
			TaskCarInsurance taskCarInsurance, WebClient webClient) throws Exception {
		String url = "http://www.pingan.com/property_insurance/pa18AutoInquiry/reportCaseCount.do?"
				+ "policyNo="+carInsuranceRequestBean.getPolicyNum();
		PingAnHtml pingAnHtml = new PingAnHtml();
		Page page = getHtml(url, webClient);
		String contentAsString = page.getWebResponse().getContentAsString();
		pingAnHtml.setHtmlText(contentAsString);
		pingAnHtml.setUrl(url);
		pingAnHtml.setMessagesText("出险详情页");
		pingAnHtml.setTaskid(carInsuranceRequestBean.getTaskid());
		pingAnHtmlRepository.save(pingAnHtml);
		System.out.println(contentAsString);
		tracer.addTag("出险详情", contentAsString);
	}

	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	public HtmlPage gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		//		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		//		webRequest.setAdditionalHeader("Accept-Encoding","gzip, deflate");
		//		webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.9");
		//		webRequest.setAdditionalHeader("Cache-Control","max-age=0");
		//		webRequest.setAdditionalHeader("Connection","keep-alive");
		//		webRequest.setAdditionalHeader("Host", "www.pingan.com");
		//		webRequest.setAdditionalHeader("Origin", "http://www.pingan.com");
		//		webRequest.setAdditionalHeader("Referer", "http://www.pingan.com/property_insurance/pa18AutoInquiry/login.screen");
		//		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
		//		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");		
		//		webRequest.setAdditionalHeader("Content-Length","92");
		//		webRequest.setAdditionalHeader("Content-Type","application/x-www-form-urlencoded");
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		HtmlPage searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}
}
