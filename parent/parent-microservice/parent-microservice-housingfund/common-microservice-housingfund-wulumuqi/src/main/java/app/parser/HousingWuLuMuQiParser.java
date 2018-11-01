package app.parser;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.wulumuqi.HousingWuLuMuQiPay;
import com.microservice.dao.entity.crawler.housing.wulumuqi.HousingWuLuMuQiUserinfo;
import com.module.htmlunit.WebCrawler;

import app.common.RSAUtil;
import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingWuLuMuQiParser {
	
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		tracer.addTag("housingFund.login.parser.login.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		String loginUrl = "http://www.wlmqgjj.com:18080/wt-web-gr/grlogin";
		webParam.setUrl(loginUrl);
		tracer.addTag("housingFund.login.parser.login.loginUrl", loginUrl);
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		HtmlPage loginPage = webClient.getPage(webRequest);
		tracer.addTag("housingFund.login.parser.login.loginPage", "<xmp>"+loginPage.asXml()+"</xmp>");
		webParam.setPage(loginPage);
		String asXml = loginPage.asXml();
		Document doc = Jsoup.parse(asXml);
		String modulus = doc.getElementById("modulus").val();
		String exponent = doc.getElementById("exponent").val();
		String pwd=messageLoginForHousing.getPassword();
		RSAUtil rsaUtil = new RSAUtil();
		String pwdEncode = rsaUtil.encrypt(pwd, modulus, exponent);
		HtmlImage captcha_img = (HtmlImage)loginPage.getFirstByXPath("//img[@id='captcha_img']");
		if(null != captcha_img){
			String imgcode = chaoJiYingOcrService.getVerifycode(captcha_img, "1004");
			String url = "http://www.wlmqgjj.com:18080/wt-web-gr/grlogin";
			String reqBody = "grloginDxyz=0&username="+messageLoginForHousing.getNum()+"&password="+pwdEncode+"&force=&captcha="+imgcode.trim()+"&sliderCaptcha=";
			tracer.addTag("housingFund.login.parser.login.loginedurl", url+"?"+reqBody);
			webParam.setUrl(url+"?"+reqBody);
			webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setRequestBody(reqBody);
			Page loginedPage = webClient.getPage(webRequest);
			Thread.sleep(5000);
			webParam.setPage(loginedPage);
			String loginedString = loginedPage.getWebResponse().getContentAsString();
			tracer.addTag("housingFund.login.parser.login.loginedPage", "<xmp>"+loginedString+"</xmp>");
			if(loginedString.contains("var grzh='")){
				int staIndex = loginedString.indexOf("var grzh='");
				int endIndex = loginedString.indexOf("';", staIndex);
				String grzh = loginedString.substring(staIndex+10, endIndex);
				if(null != grzh && !grzh.trim().equals("")){
					webParam.setParam(grzh);
					webParam.setWebClient(webClient);
				}else{
					webParam.setHtml("登陆失败，用户名或密码错误！");
				}
			}else{
				webParam.setHtml("登陆失败，用户名或密码错误！");
			}
		}else{
			webParam.setHtml("网络异常，请您稍后重试！");
		}
		
		return webParam;
	}

	public WebParam getUserInfo(TaskHousing taskHousing) throws Exception{
		tracer.addTag("housingFund.crawler.parser.getUserInfo.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = taskHousing.getClient(taskHousing.getCookies());
		String userUrl = "http://www.wlmqgjj.com:18080/wt-web-gr/jcr/jcrkhxxcx_mh.service";
		String reqBody = "grxx=grbh&ffbm=01&ywfl=01&ywlb=99&cxlx=01";
		webParam.setUrl(userUrl+"?"+reqBody);
		tracer.addTag("housingFund.crawler.parser.getUserInfo.url", userUrl+"?"+reqBody);
		WebRequest webRequest = new WebRequest(new URL(userUrl), HttpMethod.POST);
		webRequest.setRequestBody(reqBody);
		Page userPage = webClient.getPage(webRequest);
		webParam.setPage(userPage);
		tracer.addTag("housingFund.crawler.parser.getUserInfo.userPage", userPage.getWebResponse().getContentAsString());
		if(userPage.getWebResponse().getContentAsString().contains("success")){
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(userPage.getWebResponse().getContentAsString());
			String success = object.get("success").getAsString();
			if(success.contains("true")){
				JsonObject data = object.get("data").getAsJsonObject();
				String name = data.get("xingming").getAsString();
				String cardType = data.get("zjlx").getAsString();
				String idNum = data.get("zjhm").getAsString();
				String birthday = data.get("csny").getAsString();
				String gender = data.get("xingbie").getAsString();
				String isMarry = data.get("hyzk").getAsString();
				String postNum = data.get("yzbm").getAsString();
				String telNum = data.get("gddhhm").getAsString();
				String familyMonthIncome = data.get("jtysr").getAsString();
				String address = data.get("jtzz").getAsString();
				String phoneNum = data.get("sjhm").getAsString();
				String accountNum = data.get("grzh").getAsString();
				String accountStatus = data.get("grzhzt").getAsString();
				String accountBalance = data.get("grzhye").getAsString();
				String openDate = data.get("djrq").getAsString();
				String companyName = data.get("dwmc").getAsString();
				String loanStatus = data.get("sfyzfdk").getAsString();
				String payPercent = data.get("jcbl").getAsString();
				String payBase = data.get("grjcjs").getAsString();
				String monthPay = data.get("yjce").getAsString();
				String bankName = data.get("grckzhkhyhmc").getAsString();
				String bankNum = data.get("grckzhhm").getAsString();
				
				List<HousingWuLuMuQiUserinfo> userinfos = new ArrayList<HousingWuLuMuQiUserinfo>();
				HousingWuLuMuQiUserinfo userinfo = new HousingWuLuMuQiUserinfo();
				userinfo.setTaskid(taskHousing.getTaskid());
				userinfo.setName(name);
				userinfo.setCardType(cardType);
				userinfo.setIdNum(idNum);
				userinfo.setBirthday(birthday);
				userinfo.setGender(gender);
				userinfo.setIsMarry(isMarry);
				userinfo.setPostNum(postNum);
				userinfo.setTelNum(telNum);
				userinfo.setFamilyMonthIncome(familyMonthIncome);
				userinfo.setAddress(address);
				userinfo.setPhoneNum(phoneNum);
				userinfo.setAccountNum(accountNum);
				userinfo.setAccountStatus(accountStatus);
				userinfo.setAccountBalance(accountBalance);
				userinfo.setOpenDate(openDate);
				userinfo.setCompanyName(companyName);
				userinfo.setLoanStatus(loanStatus);
				userinfo.setPayPercent(payPercent);
				userinfo.setPayBase(payBase);
				userinfo.setMonthPay(monthPay);
				userinfo.setBankName(bankName);
				userinfo.setBankNum(bankNum);
				userinfos.add(userinfo);
				webParam.setList(userinfos);
			}
		}
		return webParam;
	}

	public WebParam getPayInfo(TaskHousing taskHousing) throws Exception{
		tracer.addTag("housingFund.crawler.parser.getPayInfo.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = taskHousing.getClient(taskHousing.getCookies());
		String payUrl = "http://www.wlmqgjj.com:18080/wt-web-gr/jcr/jcrxxcxzhmxcx.service?ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq=2000-01-01&jsrq="+getDate("yyyy-MM-dd")+"&grxx="+taskHousing.getPassword()+"&fontSize=13px&pageNum=1&pageSize=500";
		webParam.setUrl(payUrl);
		tracer.addTag("housingFund.crawler.parser.getPayInfo.payUrl", payUrl);
		WebRequest webRequest = new WebRequest(new URL(payUrl), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		webParam.setPage(page);
		tracer.addTag("housingFund.crawler.parser.getPayInfo.page", page.getWebResponse().getContentAsString());
		if(page.getWebResponse().getContentAsString().contains("success")){
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(page.getWebResponse().getContentAsString());
			String success = object.get("success").getAsString();
			if(success.contains("true")){
				JsonArray results = object.get("results").getAsJsonArray();
				if(null != results && results.size() > 0){
					List<HousingWuLuMuQiPay> pays = new ArrayList<HousingWuLuMuQiPay>();
					for (JsonElement result : results) {
						JsonObject obj = result.getAsJsonObject();
						String payDate = obj.get("jzrq").getAsString();
						String payType = obj.get("gjhtqywlx").getAsString();
						String fee = obj.get("fse").getAsString();
						String interest = obj.get("fslxe").getAsString();
						String getReason = obj.get("tqyy").getAsString();
						String getType = obj.get("tqfs").getAsString();
						
						HousingWuLuMuQiPay pay = new HousingWuLuMuQiPay();
						pay.setPayDate(payDate);
						pay.setPayType(payType);
						pay.setFee(fee);
						pay.setInterest(interest);
						pay.setGetReason(getReason);
						pay.setGetType(getType);
						pay.setTaskid(taskHousing.getTaskid());
						pays.add(pay);
					}
					webParam.setList(pays);
				}
			}
		}
		return webParam;
	}
	
	/*
	 * @Des 获取当前的 时间
	 */
	public String getDate(String fmt) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		String date = format.format(new Date());
		return date;
	}
	
}
