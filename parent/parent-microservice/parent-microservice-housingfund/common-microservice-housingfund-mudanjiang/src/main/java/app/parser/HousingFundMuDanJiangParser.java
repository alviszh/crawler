package app.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.mudanjiang.HousingFundMuDanJiangAccount;
import com.microservice.dao.entity.crawler.housing.mudanjiang.HousingFundMuDanJiangUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class HousingFundMuDanJiangParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		tracer.addTag("action.login.start", messageLoginForHousing.getTask_id());
		String url="http://www.mdjzfgjj.cn/mdjweb/grcxdl.jhtml";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam = new WebParam();
		HtmlPage page = webClient.getPage(url);	
		messageLoginForHousing.setLogintype("IDNUM");
		if(messageLoginForHousing.getLogintype().contains("IDNUM"))
		{
			HtmlSelect select = (HtmlSelect) page.getElementById("selet");
	        HtmlOption option = select.getOptionByText("身份证号");
	        option.click();
			
	        HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//input[@id='certinum']");
			id_card.reset();
			id_card.setText(messageLoginForHousing.getNum());

		}
		else
		{
			HtmlSelect select = (HtmlSelect) page.getElementById("selet");
	        HtmlOption option = select.getOptionByText("公积金账号");
	        option.click();
	        
			HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//input[@id='accnum']");
			id_card.reset();
			id_card.setText(messageLoginForHousing.getNum());
		}
		
		HtmlPasswordInput searchpwd = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='password']");
		searchpwd.reset();
		searchpwd.setText(messageLoginForHousing.getPassword());
		
		HtmlImage img = page.getFirstByXPath("//img[@id='image']");
		
		String verifycode = chaoJiYingOcrService.getVerifycode(img, "2005");
		
		HtmlTextInput identifying = (HtmlTextInput)page.getFirstByXPath("//input[@id='verify']");
		identifying.reset();
		identifying.setText(verifycode);
		
		HtmlElement button = page.getFirstByXPath("//input[@id='submit_button']");
		HtmlPage page2 = button.click();
		Thread.sleep(1000);
		String string2 = page2.getWebResponse().getContentAsString();
		System.out.println(page2.getWebResponse().getContentAsString());
		Thread.sleep(2000);
		String alertMsg = WebCrawler.getAlertMsg();
		System.out.println("==============================================="+alertMsg);
		if(null != page2)
		{
			int code = page2.getWebResponse().getStatusCode();
			if(code==200)
			{
				if(string2.contains("欢迎您"))
				{
					String string = page2.getWebResponse().getContentAsString();
					webParam.setCode(code);
					webParam.setHtml(string);
					webParam.setHtmlPage(page2);
					webParam.setUrl(alertMsg);
					webParam.setWebClient(page2.getWebClient());
					return webParam;
				}
				else
				{
					webParam.setCode(code);
					webParam.setHtml(alertMsg);
					webParam.setHtmlPage(page2);
					//webParam.setWebClient(page2.getWebClient());
					return webParam;
				}
			}
		}
		return null;
	}

	//爬取个人信息
	public WebParam<HousingFundMuDanJiangUserInfo> crawlerUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		String cookies = taskHousing.getCookies();
		JSONArray from = JSONArray.fromObject(cookies);
		String string="";
		for (Object object2 : from) {
			if(object2.toString().contains("gjjaccnum"))
			{
				JSONObject fromObject2 = JSONObject.fromObject(object2);
			    string = fromObject2.getString("value");
			    break;
			}
		}
		String urlInfo ="http://www.mdjzfgjj.cn/mdjweb/website/trans/gjjquery.do?className=TRC311014&accnum="+string+"&time="+System.currentTimeMillis();
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies);
		for (Cookie cookie : cookies1) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		WebParam<HousingFundMuDanJiangUserInfo> webParam = new WebParam<HousingFundMuDanJiangUserInfo>();
		Page page = webClient.getPage(urlInfo);
		if(null != page)
		{
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			HousingFundMuDanJiangUserInfo h = new HousingFundMuDanJiangUserInfo();
			JSONObject fromObject = JSONObject.fromObject(doc.text().subSequence(1, doc.text().length()-1));
			System.out.println(fromObject);
			String lastpaydate = fromObject.getString("lastpaydate");
			String indisum = fromObject.getString("indisum");
			String unitprop = fromObject.getString("unitprop");
			String unitsum = fromObject.getString("unitsum");
			String unitaccname = fromObject.getString("unitaccname");
			String unitaccnum = fromObject.getString("unitaccnum");
			String peraccstate = fromObject.getString("peraccstate");
			String indiprop = fromObject.getString("indiprop");
			String monpaysum = fromObject.getString("monpaysum");
			String transdate = fromObject.getString("transdate");
			String opendate = fromObject.getString("opendate");
			String basenumber = fromObject.getString("basenumber");
			String balance = fromObject.getString("balance");
			h.setBalance(balance);
			h.setBasenumber(basenumber);
			h.setIndiprop(indiprop);
			h.setIndisnum(indisum);
			h.setLastpaydate(lastpaydate);
			h.setMonpaysum(monpaysum);
			h.setOpendate(opendate);
			h.setPeraccstate(peraccstate);
			h.setTransdate(transdate);
			h.setUnitaccname(unitaccname);
			h.setUnitaccnum(unitaccnum);
			h.setUnitprop(unitprop);
			h.setUnitsum(unitsum);
			h.setTaskid(taskHousing.getTaskid());
			System.out.println(h);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setHousingFundMuDanJiangUserInfo(h);
			webParam.setUrl(string);
			return webParam;
		}
		return null;
	}

	
	//爬取流水
	public WebParam<HousingFundMuDanJiangAccount> crawlerAccount(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,int i) throws Exception{
		String url="";
		if(messageLoginForHousing.getLogintype().contains("IDNUM"))
		{		          
			 url ="http://www.mdjzfgjj.cn/mdjweb/website/trans/gjjquery.do?className=TRC311015&time="+System.currentTimeMillis()+"&accnum="+taskHousing.getCrawlerPort();
		}          
		else
		{
			 url ="http://www.mdjzfgjj.cn/mdjweb/website/trans/gjjquery.do?className=TRC311015&time="+System.currentTimeMillis()+"&accnum=&certinum="+messageLoginForHousing.getNum()+"&password="+messageLoginForHousing.getPassword()+"&mark=0&txt=1&verify="+messageLoginForHousing.getSms_code()+"&CI_Pagenum="+i;
		}
		WebParam<HousingFundMuDanJiangAccount> webParam = new WebParam<HousingFundMuDanJiangAccount>();
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
				    System.out.println(doc.text());
				    JSONArray fromObject = JSONArray.fromObject(doc.text());
					System.out.println(fromObject.size());
					List<HousingFundMuDanJiangAccount> list = new ArrayList<HousingFundMuDanJiangAccount>();
					for (int j = 1; j < fromObject.size(); j++) {
						HousingFundMuDanJiangAccount h = new HousingFundMuDanJiangAccount();
						JSONObject fromObject2 = JSONObject.fromObject(fromObject.get(j).toString());
						System.out.println(i);
						h.setCompany(fromObject2.getString("unitaccname"));
						h.setCompanyNum(fromObject2.getString("unitaccnum"));
						h.setMoney(fromObject2.getString("busiamt"));
						h.setPayDate(fromObject2.getString("transdate"));
						if(fromObject2.getString("summary").contains("1001"))
						{
							h.setBussType("汇缴");
						}
						h.setTaskid(taskHousing.getTaskid());
						list.add(h);
					}
					webParam.setList(list);
					webParam.setHtml(doc.text());
				 return webParam;
			 }
		 }
		 return null;
		
	}

	

}
