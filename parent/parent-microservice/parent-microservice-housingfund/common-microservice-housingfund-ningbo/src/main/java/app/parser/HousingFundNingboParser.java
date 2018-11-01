package app.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.ningbo.HousingNingboPay;
import com.microservice.dao.entity.crawler.housing.ningbo.HousingNingboUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class HousingFundNingboParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	

	//身份证登陆
	public WebParam loginByIdCard(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		WebParam webParam = new WebParam();
	    WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	    tracer.addTag("parser.housing.idlogin.start", taskHousing.getTaskid());
		//登陆界面
        String url="http://gjjwt.nbjs.gov.cn:7001/gjj-wsyyt/personNotRegisterLogin.html";
        HtmlPage htmlPage = webClient.getPage(url);
        HtmlTextInput cardno = htmlPage.getFirstByXPath("//input[@id='sfzhdiv']");
		cardno.reset();
		cardno.setText(messageLoginForHousing.getNum());
		
		
		HtmlPasswordInput perpwd = htmlPage.getFirstByXPath("//input[@id='password']");
		perpwd.reset();
		perpwd.setText(messageLoginForHousing.getPassword());
		
		
		HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//img[@id='veryCodeImg']");
		HtmlElement verify = htmlPage.getFirstByXPath("//*[@id='notRegisterLogin']/div[5]/div/input");
		String verifycode = chaoJiYingOcrService.getVerifycode(valiCodeImg, "1902");
		verify.setTextContent(verifycode);
		
		
		HtmlElement Button = htmlPage.getFirstByXPath("//button[@id='sub']");
		HtmlPage htmlPage1 = Button.click();
		WebClient webClient2 = htmlPage1.getWebClient();
		
		
		//验证登陆
		String loginurl="http://www.nbgjj.com/GJJQuery?tranCode=142501&task=&accnum=&certinum="+messageLoginForHousing.getNum()+"&pwd="+messageLoginForHousing.getPassword()+"&verify="+verifycode;
	
		Page page = webClient2.getPage(loginurl);
		if(null != page)
		{
			int i = page.getWebResponse().getStatusCode();
			if(i==200)
			{
				tracer.addTag("parser.housing.login.success", taskHousing.getTaskid());
				webParam.setWebClient(webClient2);
				webParam.setHtml(page.getWebResponse().getContentAsString());
				return webParam;
			}
		}
		return null;
		  
	}

	//公积金登陆
	public WebParam loginByHousingFundCard(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		WebParam webParam = new WebParam();
	    WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	    tracer.addTag("parser.housing.fundLogin.start", taskHousing.getTaskid());
		//登陆界面
        String url="http://www.nbgjj.com/perlogin.jhtml";
        HtmlPage htmlPage = webClient.getPage(url);
        HtmlTextInput cardno = htmlPage.getFirstByXPath("//input[@id='accnum']");
		cardno.reset();
		cardno.setText(messageLoginForHousing.getNum());
		
		
		HtmlPasswordInput perpwd = htmlPage.getFirstByXPath("//input[@id='perpwd']");
		perpwd.reset();
		perpwd.setText(messageLoginForHousing.getPassword());
		
		
		HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//img[@id='guestbookCaptcha']");
		HtmlElement verify = htmlPage.getFirstByXPath("//input[@id='verify']");
		String verifycode = chaoJiYingOcrService.getVerifycode(valiCodeImg, "1902");
		verify.setTextContent(verifycode);
		
		
		HtmlElement Button = htmlPage.getFirstByXPath("//button[@id='sub']");
		HtmlPage htmlPage1 = Button.click();
		WebClient webClient2 = htmlPage1.getWebClient();
		
		
		//验证登陆
		String loginurl="http://www.nbgjj.com/GJJQuery?tranCode=142501&task=&accnum=&certinum="+messageLoginForHousing.getNum()+"&pwd="+messageLoginForHousing.getPassword()+"&verify="+verifycode;
	
		Page page = webClient2.getPage(loginurl);
		if(null != page)
		{
			int i = page.getWebResponse().getStatusCode();
			if(i==200)
			{
				tracer.addTag("parser.housing.fundLogin.success", taskHousing.getTaskid());
				webParam.setWebClient(webClient2);
				webParam.setHtml(page.getWebResponse().getContentAsString());
				return webParam;
			}
		}
		return webParam;
	}
	
	//个人信息
	public WebParam<HousingNingboUserInfo> getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		//主界面
		WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();
		WebParam<HousingNingboUserInfo> webParam = new WebParam<HousingNingboUserInfo>();
		
		tracer.addTag("parser.housing.getUserInfo.start", taskHousing.getTaskid());
		
		String pageurl="http://www.nbgjj.com/perquery.jhtml";
		String cookies2 = taskHousing.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		String string=null;
		for (Cookie cookie : cookies) {
			if("gjjaccnum".equals(cookie.getName())){
				 string = cookie.getValue();
			}
			webClient2.getCookieManager().addCookie(cookie);
		}
		
		Page pagepage = webClient2.getPage(pageurl);
		System.out.println("-=-==-========================="+pagepage.getWebResponse().getContentAsString());
		System.out.println(string);
		String url1="http://www.nbgjj.com/GJJQuery?tranCode=142503&task=&accnum="+string;
		webParam.setUrl(url1);
		Page page1 = webClient2.getPage(url1);
		if(null != page1)
		{
			webParam.setHtml(page1.getWebResponse().getContentAsString());
			int code = page1.getWebResponse().getStatusCode();
			if(code==200)
			{
				webParam.setCode(code);
				HousingNingboUserInfo housingNingboUserInfo = new HousingNingboUserInfo();
				String string2 = page1.getWebResponse().getContentAsString();
				JSONObject reqjson  = JSONObject.fromObject(string2);
				housingNingboUserInfo.setAccname(reqjson.getString("accname"));//姓名
				housingNingboUserInfo.setCardtype(reqjson.getString("certype"));//证件类型
				housingNingboUserInfo.setDisposableHousing(reqjson.getString("amt8"));//一次性住房补贴余额
				housingNingboUserInfo.setDisposableMonth(reqjson.getString("monintamt"));// 一次性住房补贴月缴存额
				housingNingboUserInfo.setFee(reqjson.getString("bal"));//余额
				housingNingboUserInfo.setFreezeMoney(reqjson.getString("frzamt"));//冻结金额
				housingNingboUserInfo.setFreezeStatus(reqjson.getString("actmp200"));//冻结状态
				housingNingboUserInfo.setHouseFee(reqjson.getString("amt7"));//住房货币补贴（暂）余额
				housingNingboUserInfo.setHousingFundFee(reqjson.getString("amt"));//公积金余额
				housingNingboUserInfo.setHousingFundMonth(reqjson.getString("monpaysum"));//公积金月缴存额
				housingNingboUserInfo.setMoneySubsidy(reqjson.getString("amt7"));//住房货币补贴（暂）月缴存额
				housingNingboUserInfo.setMonthFee(reqjson.getString("subintamt"));//按月补贴余额
				housingNingboUserInfo.setNowDate(reqjson.getString("lpaym"));//缴至年月
				housingNingboUserInfo.setOpenDate(reqjson.getString("begdate"));// 开户日期
				housingNingboUserInfo.setOrganization(reqjson.getString("unitaccname"));//账户机构
				housingNingboUserInfo.setPersonalId(reqjson.getString("accnum"));//个人公积金账号
				housingNingboUserInfo.setSaveBase(reqjson.getString("basenum"));//缴存基数
				housingNingboUserInfo.setSubsidy(reqjson.getString("subintamt"));//按月补贴月缴存额
				housingNingboUserInfo.setTaskid(taskHousing.getTaskid());
				webParam.setHousingNingboUserInfo(housingNingboUserInfo);
				return webParam;
			}
		}
		return null;
	}

	// 当前时间
	public String getTime(String fmt) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(fmt);// 可以方便地修改日期格式
		String hehe = dateFormat.format(now);
		return hehe;
	}
	
	/*
	 * @Des 获取当前月 的前i个月的 时间
	 */
	public String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}
	
	//公积金明细
	public WebParam<HousingNingboPay> getAccount(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		WebClient webClient2 = WebCrawler.getInstance().getNewWebClient();
		WebParam<HousingNingboPay> webParam = new WebParam<HousingNingboPay>();
		String cookies2 = taskHousing.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		String string=null;
		for (Cookie cookie : cookies) {
			if("gjjaccnum".equals(cookie.getName())){
				 string = cookie.getValue();
			}
			webClient2.getCookieManager().addCookie(cookie);
		}
		String url ="http://www.nbgjj.com/perdetail.jhtml";
		HtmlPage page = webClient2.getPage(url);
		tracer.addTag("parser.housing.getFund.start", taskHousing.getTaskid());
		String gjjurl="http://www.nbgjj.com/GJJQuery?tranCode=142504&task=ftp&accnum="+string+"&begdate="+getDateBefore("yyyy-MM-dd", 3)+"&enddate="+getTime("yyyy-MM-dd")+"&indiacctype=1";
		webParam.setUrl(gjjurl);
		Page page2 = webClient2.getPage(gjjurl);
		if(null != page2)
		{
			webParam.setHtml(page2.getWebResponse().getContentAsString());
			int code = page2.getWebResponse().getStatusCode();
			if(code==200)
			{
				webParam.setCode(code);
				JSONArray fromObject = JSONArray.fromObject(page2.getWebResponse().getContentAsString());
				HousingNingboPay housingNingboPay=null;
				List<HousingNingboPay> list = new ArrayList<HousingNingboPay>();
				for (Object object : fromObject) {
					JSONObject fromObject2 = JSONObject.fromObject(object);
					housingNingboPay = new HousingNingboPay();
					housingNingboPay.setCompany(fromObject2.getString("unitaccname"));
					housingNingboPay.setDatea(fromObject2.getString("trandate"));
					housingNingboPay.setFee(fromObject2.getString("amt"));
					housingNingboPay.setMoney(fromObject2.getString("bal"));
					housingNingboPay.setType(fromObject2.getString("ywtype"));
					housingNingboPay.setTaskid(taskHousing.getTaskid());
					list.add(housingNingboPay);
				}
				webParam.setList(list);
				return webParam;
			}
		}
		return null;
		
		
		
		
		
		
		
		
		
		
	}

}
