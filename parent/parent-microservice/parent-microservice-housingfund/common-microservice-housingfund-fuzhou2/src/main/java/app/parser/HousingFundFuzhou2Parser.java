package app.parser;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import java.util.concurrent.TimeUnit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.common.base.Function;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.fuzhou2.HousingFuzhou2Account;
import com.microservice.dao.entity.crawler.housing.fuzhou2.HousingFuzhou2Basic;
import com.microservice.dao.entity.crawler.housing.yichun.HousingYiChunBase;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingFundFuzhou2Parser {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private  ChaoJiYingOcrService chaoJiYingOcrService;
	


	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing, WebDriver driver)throws Exception {
		WebParam webParam = new WebParam();
		WebDriverWait wait=new WebDriverWait(driver, 10);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		String baseUrl = "http://219.132.4.6:6012/web/ggfw/app/index.html";
		tracer.addTag("parser.login.url", baseUrl);
		driver.get(baseUrl);
		WebElement username= wait.until(new ExpectedCondition<WebElement>() {  
            public WebElement apply(WebDriver driver) {  
                return driver.findElement(By.id("username"));  
            } 
        });
		WebElement password = driver.findElement(By.id("password"));
		WebElement button = driver.findElement(By.id("personalLoginbtn"));
		
		
		username.sendKeys(messageLoginForHousing.getUsername());
		password.sendKeys(messageLoginForHousing.getPassword());
		button.click();
		Alert alert = null;
		try {
			alert = wait.until(new Function<WebDriver, Alert>() {  
	            public Alert apply(WebDriver driver) {  
	                return driver.switchTo().alert(); 
	            }
	        });
		} catch (Exception e) {
			tracer.addTag("parser.login.alert.none", "无登陆成功提示弹框。");
		}
		
		if(null != alert){
			String alertText = alert.getText();
			tracer.addTag("parser.login.alert.text", alertText);
			alert.accept();
			if(alertText.contains("用户名或密码错误")){
				webParam.setHtml(driver.getPageSource());
			}
		}
		Thread.sleep(1000);
		tracer.addTag("parser.login.loginedPage", driver.getPageSource());
		webParam.setDriver(driver);
		return webParam;
	}
	
	public WebParam<HousingFuzhou2Basic> getBasicInfo(String pageSource, String taskid) {
		WebParam webParam = new WebParam();
		HousingFuzhou2Basic base = new HousingFuzhou2Basic();
		Document doc = Jsoup.parse(pageSource);
		Elements trs = doc.select("tr");
		Elements tds1 = trs.get(1).select("td");
		Elements tds2 = trs.get(2).select("td");
		Elements tds3 = trs.get(3).select("td");
		Elements tds4 = trs.get(4).select("td");
		Elements tds5 = trs.get(5).select("td");
		Elements tds6 = trs.get(6).select("td");
		Elements tds7 = trs.get(7).select("td");
		Elements tds8 = trs.get(8).select("td");
		Elements tds9 = trs.get(9).select("td");
		Elements tds10 = trs.get(10).select("td");
		Elements tds11 = trs.get(11).select("td");
		Elements tds12 = trs.get(12).select("td");
		Elements tds13 = trs.get(13).select("td");
		Elements tds14 = trs.get(14).select("td");
		Elements tds15 = trs.get(15).select("td");
		Elements tds16 = trs.get(16).select("td");
		Elements tds17 = trs.get(17).select("td");
		Elements tds18 = trs.get(18).select("td");
		Elements tds19 = trs.get(19).select("td");
		Elements tds20 = trs.get(20).select("td");
		base.setCardNum(tds2.get(3).getElementsByTag("input").val());
		base.setName(tds1.get(1).val());
		base.setBirthday(tds1.get(3).val());
		base.setCardType(tds2.get(1).val());
		base.setAccountType(tds3.get(1).val());
		base.setMobilePhone(tds3.get(3).val());
		base.setHomePhone(tds4.get(1).val());
		base.setHomeAddress(tds4.get(3).val());
		base.setCompanyPhone(tds5.get(1).val());
		base.setPostOfficeCode(tds5.get(3).val());
		base.setJointCardCode(tds6.get(1).val());
		base.setAccumulatedOverTheYears(tds6.get(3).val());
		base.setAnnualBorrowerTotal(tds7.get(1).val());
		base.setLastYearBalance(tds7.get(3).val());
		base.setYearLendersTotal(tds8.get(1).val());
		base.setLiAnnualBorrowerTotal(tds8.get(3).val());
		base.setPayYear(tds9.get(1).val());
		base.setRealIncome(tds9.get(3).val());
		base.setCompanyPay(tds10.get(1).val());
		base.setPersonPay(tds10.get(3).val());
		base.setCompanyPayable(tds11.get(1).val());
		base.setPersonPayable(tds11.get(3).val());
		base.setMonthPayable(tds12.get(1).val());
		base.setDataUpdateDate(tds12.get(3).val());
		base.setHousingFundNum(tds13.get(1).val());
		base.setOpenAccountDate(tds13.get(3).val());
		base.setHousingAccountStatus(tds14.get(1).val());
		base.setHousingAccountBalance(tds14.get(3).val());
		base.setPersonSubsidyNum(tds15.get(1).val());
		base.setCompanyHousingNum(tds15.get(3).val());
		base.setSubsidyAccountStatus(tds16.get(1).val());
		base.setDataUpdateDateAccount(tds16.get(3).val());
		base.setSubsidyAccountBalance(tds17.get(1).val());
		base.setHousingMonthWages(tds17.get(3).val());
		base.setSubsidyMonthWages(tds18.get(1).val());
		base.setHousingMonthPay(tds18.get(3).val());
		base.setSubsidyMonthPay(tds19.get(1).val());
		base.setFreezeMark(tds19.get(3).val());
		
		webParam.setHtml(pageSource);
		return webParam;
	}

	public List<HousingFuzhou2Account> getFuzhou2Account(String pageSource, String taskid, List<HousingFuzhou2Account> list) {
		Document doc = Jsoup.parse(pageSource);
		Elements trs = doc.getElementById("grdz").select("tr");
		for(int i =1;i<trs.size();i++){
			Elements tds = trs.get(i).select("td");
			List<String> lists= new ArrayList<>();
			for (Element element : tds) {
				lists.add(element.text().trim());
			}
			if(null != lists){
				HousingFuzhou2Account account = new HousingFuzhou2Account();
				account.setFlowNumber(lists.get(0));
				account.setAccountType(lists.get(1));
				account.setPersonNum(lists.get(2));
				account.setDateOfOccurrence(lists.get(3));
				account.setSummaryNote(lists.get(4));
				account.setAmountOfOccurrence(lists.get(5));
				account.setBalance(lists.get(6));
				list.add(account);
			}
	}
		return list;
	}

}
