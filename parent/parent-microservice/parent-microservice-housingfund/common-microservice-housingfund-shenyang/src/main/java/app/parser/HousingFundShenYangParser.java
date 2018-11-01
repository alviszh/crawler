package app.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.shenyang.HousingShenYangPay;
import com.microservice.dao.entity.crawler.housing.shenyang.HousingShenYangUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;
@Component
public class HousingFundShenYangParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	
	//登陆
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		tracer.addTag("parser.login.start", taskHousing.getTaskid());
		String url="http://personal.sygjj.com/login.html";
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		HtmlTextInput idCard = (HtmlTextInput)page.getFirstByXPath("//input[@id='loginname']");
		idCard.reset();
		idCard.setText(messageLoginForHousing.getNum().trim());
		HtmlPasswordInput pafCard = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='pass']");
		pafCard.reset();
		pafCard.setText(messageLoginForHousing.getPassword().trim());
		HtmlImage img = page.getFirstByXPath("/html/body/div[1]/div/div[2]/div[2]/div[4]/div[2]/div[2]/div");
		
		String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		HtmlTextInput identifying = (HtmlTextInput)page.getFirstByXPath("//input[@id='yzm']");
		identifying.reset();
		identifying.setText(verifycode);
		HtmlElement button = page.getFirstByXPath("/html/body/div[1]/div/div[2]/div[2]/div[5]/div[1]/input");
		HtmlPage page2 = button.click();
		Thread.sleep(2000);
		System.out.println(page2.getWebResponse().getContentAsString());
		if(null != page2)
		{
			int code = page2.getWebResponse().getStatusCode();
			if(code==200)
			{
				webParam.setHtml(page2.getWebResponse().getContentAsString());
				webParam.setWebClient(page2.getWebClient());
				webParam.setUrl(verifycode);
				return webParam;
			}
		}
		
		return null;
	}


	//个人信息
	public WebParam<HousingShenYangUserInfo> getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		tracer.addTag("parser.start.getuserinfo", taskHousing.getTaskid());
		 String url="http://www.sygjj.com/cxxt/personalDetail/personalDetailLogin.parser?idCard="+messageLoginForHousing.getNum()+"&pafCard="+messageLoginForHousing.getPassword()+"&identifying="+taskHousing.getError_message();
		 WebParam<HousingShenYangUserInfo> webParam = new WebParam<HousingShenYangUserInfo>();
		 WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		 String cookies2 = taskHousing.getCookies();
		 Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		 for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		 }
		 HtmlPage page = webClient.getPage(url);
		 if(null != page)
		 {
			 int code = page.getWebResponse().getStatusCode();
			 if(code==200)
			 {
				 Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
				 Elements elementsByClass = doc.getElementsByClass("listtable");
				 HousingShenYangUserInfo housingShenYangUserInfo = new HousingShenYangUserInfo();
					housingShenYangUserInfo.setName(getNextLabelByKeywordTwo(elementsByClass, "姓名", "td")); 
					housingShenYangUserInfo.setPersonal(getNextLabelByKeywordTwo(elementsByClass, "个人账号", "td"));
					housingShenYangUserInfo.setIDCard(getNextLabelByKeywordTwo(elementsByClass, "身份证号", "td"));
					housingShenYangUserInfo.setDayFee(getNextLabelByKeywordTwo(elementsByClass, "查询日余额", "td"));
					housingShenYangUserInfo.setCardNum(getNextLabelByKeywordTwo(elementsByClass, "磁卡卡号", "td"));
					housingShenYangUserInfo.setRegularFee(getNextLabelByKeywordTwo(elementsByClass, "定期余额", "td"));
					housingShenYangUserInfo.setSaveStatus(getNextLabelByKeywordTwo(elementsByClass, "缴存状态", "td"));
					housingShenYangUserInfo.setCurrentFee(getNextLabelByKeywordTwo(elementsByClass, "活期余额", "td"));
					housingShenYangUserInfo.setSaveDate(getNextLabelByKeywordTwo(elementsByClass, "缴至年月", "td"));
					housingShenYangUserInfo.setGetMoney(getNextLabelByKeywordTwo(elementsByClass, "本年提取额", "td"));
					housingShenYangUserInfo.setCompanyRatio(getNextLabelByKeywordTwo(elementsByClass, "单位缴存比例", "td"));
					housingShenYangUserInfo.setSaveBase(getNextLabelByKeywordTwo(elementsByClass, "缴存基数", "td"));
					housingShenYangUserInfo.setPersonalRatio(getNextLabelByKeywordTwo(elementsByClass, "个人缴存比例", "td"));
					housingShenYangUserInfo.setMonthSave(getNextLabelByKeywordTwo(elementsByClass, "月缴存额", "td"));
					housingShenYangUserInfo.setTaskid(taskHousing.getTaskid());
					webParam.setHtml(page.getWebResponse().getContentAsString());
					webParam.setPage(page);
					webParam.setHousingShenYangUserInfo(housingShenYangUserInfo);
					webParam.setUrl(url);
					webParam.setWebClient(page.getWebClient());
					return webParam;
			 }
		 }
		 return null;
	}


	//流水
	public WebParam<HousingShenYangPay> getAccount(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		tracer.addTag("parser.start.getAccount", taskHousing.getTaskid());
		 String url="http://www.sygjj.com/cxxt/personalDetail/personalAccountsList.parser";
		 WebParam<HousingShenYangPay> webParam = new WebParam<HousingShenYangPay>();
		 WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		 String cookies2 = taskHousing.getCookies();
		 Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		 for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		 }
		 HtmlPage page = webClient.getPage(url);
		 if(null != page)
		 {
			 int code = page.getWebResponse().getStatusCode();
			 if(code==200)
			 {
				 	Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
					Elements elementsByClass = doc.getElementsByClass("listtable");
					HousingShenYangPay housingShenYangPay = null;
					Elements elementsByClass2 = elementsByClass.get(0).getElementsByClass("td_title2");
					List<HousingShenYangPay> list = new ArrayList<HousingShenYangPay>();
						for (int i = 0; i < elementsByClass2.size(); i=i+6) {
								housingShenYangPay = new HousingShenYangPay();
								housingShenYangPay.setSaveDate(elementsByClass2.get(i).text());
								housingShenYangPay.setMoney(elementsByClass2.get(i+1).text());
								housingShenYangPay.setType(elementsByClass2.get(i+2).text());
								housingShenYangPay.setFlag(elementsByClass2.get(i+3).text());
								housingShenYangPay.setFee(elementsByClass2.get(i+4).text());
								housingShenYangPay.setDatea(elementsByClass2.get(i+5).text());
								housingShenYangPay.setTaskid(taskHousing.getTaskid());
							    list.add(housingShenYangPay);
					   }
					webParam.setCode(code);
					webParam.setHtml(page.getWebResponse().getContentAsString());
					webParam.setUrl(url);
					webParam.setList(list);
				return webParam;
			 }
		 }
		return null;
	}
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容2
	 * @param document
	 * @param keyword
	 * @return
	 */
	public  String getNextLabelByKeywordTwo(Elements element,  String keyword, String tag) {
		Elements es = element.select(tag + ":contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element1 = es.first();
			Element nextElement = element1.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}

}
