package app.parser;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
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
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.suzhou.HousingSuzhouAccountBasic;
import com.microservice.dao.entity.crawler.housing.suzhou.HousingSuzhouAccountDetail;
import com.microservice.dao.entity.crawler.housing.xuzhou.HousingXuzhouAccount;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class HousingFundSuzhouParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	

	//身份证登陆
	public WebParam loginByIdCard(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		//index界面
        String url="https://gr.szgjj.gov.cn/retail/index.jsp";
		WebParam webParam= new WebParam();
	    WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	    HtmlPage htmlPage = webClient.getPage(url);	
	    Document doc = Jsoup.parse(htmlPage.getWebResponse().getContentAsString());
	    String val = doc.getElementsByAttributeValue("name", "sid").val();
	    //登录页面
	    String url1 = "https://gr.szgjj.gov.cn/retail/internet?sid="+val+"&service=com.jbsoft.i2hf.retail.services.UserLogon.logturnpage&turntype=0";
	    WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
	    HtmlPage page1 = webClient.getPage(webRequest);
	    HtmlImage valiCodeImg = page1.getFirstByXPath("//img[@id='validatePicture0']");
//	   
	    String verifycode = chaoJiYingOcrService.getVerifycode(valiCodeImg, "4004");
	    //post请求登录
	    String url2="https://gr.szgjj.gov.cn/retail/service?service=com.jbsoft.i2hf.retail.services.UserLogon.unRegUserLogon&custacno="+messageLoginForHousing.getHosingFundNumber()+"&paperid="+messageLoginForHousing.getNum()+"&paperkind=A&logontype=1&validateCode="+verifycode;
		WebParam webParam1= new WebParam();
		WebClient webClient1 = WebCrawler.getInstance().getNewWebClient();
		webClient1.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest1 = new WebRequest(new URL(url2), HttpMethod.POST);		
		HtmlPage page = webClient.getPage(webRequest1);
	    String  html = page.getWebResponse().getContentAsString();
	    System.out.println(html);
		webParam.setHtml(html);
		webParam.setWebClient(webClient);
		webParam.setUrl(val);
		return webParam;
	}


	public WebParam<HousingXuzhouAccount> getBasicInfo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing)throws Exception  {
		WebParam webParam= new WebParam();	
		String url = "https://gr.szgjj.gov.cn/retail/internet";
		List<Page> pageList = new ArrayList<Page>();
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String cookies = taskHousing.getCookies();
			Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
			Iterator<Cookie> i = set.iterator();
			while(i.hasNext()){
				webClient.getCookieManager().addCookie(i.next());
			}
			webClient.getOptions().setJavaScriptEnabled(false);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setRequestBody("sid="+taskHousing.getPassword()+"&service=com.jbsoft.i2hf.retail.services.UserAccService.getBaseAccountInfo");
			Page page = webClient.getPage(webRequest);
			int statusCode = page.getWebResponse().getStatusCode();
			List<HousingSuzhouAccountBasic> infoList  = new ArrayList<HousingSuzhouAccountBasic>();
			if(200== statusCode){
				String html  =page.getWebResponse().getContentAsString();
				tracer.addTag("HousingSuzhouParser.getBasicInfo 账户基本信息" + messageLoginForHousing.getTask_id(),
						"<xmp>" + html + "</xmp>");
				System.out.println(html);
				pageList.add(page);
//				HousingSuzhouAccountBasic	accountList= htmlParserAccount(html,messageLoginForHousing);
				// sln 临时注释
				/*HousingSuzhouAccountBasic	accountList= htmlParserAccount(html,messageLoginForHousing);
				if(null != infoList){
					webParam.setHousingSuzhouAccount(accountList);
					webParam.setHtml(html);
					webParam.setUrl(url);
					return webParam;
				}*/
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	public WebParam<HousingSuzhouAccountDetail> getDetailInfo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing)throws Exception   {
		WebParam webParam= new WebParam();	
		String url = "https://gr.szgjj.gov.cn/retail/internet";
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String cookies = taskHousing.getCookies();
			Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
			Iterator<Cookie> i = set.iterator();
			while(i.hasNext()){
				webClient.getCookieManager().addCookie(i.next());
			}
			webClient.getOptions().setJavaScriptEnabled(false);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setRequestBody("sid="+taskHousing.getPassword()+"&service=com.jbsoft.i2hf.retail.services.UserAccService.getDetailAccountInfoJSON");
			webRequest.setCharset(Charset.forName("UTF-8"));

			List<HtmlPage> pageList = new ArrayList<HtmlPage>();
			Page page = webClient.getPage(webRequest);
			int statusCode = page.getWebResponse().getStatusCode();
			List<HousingSuzhouAccountDetail> infoList = new ArrayList<HousingSuzhouAccountDetail>();
			if(200== statusCode){
				String html  =page.getWebResponse().getContentAsString();
				tracer.addTag("InsuranceXuzhouiParser.getDetailInfo 公积金明细" + messageLoginForHousing.getTask_id(),
						"<xmp>" + html + "</xmp>");
				infoList = htmlParserDetail(html,messageLoginForHousing,infoList);
				if(null !=infoList){
	    			webParam.setList(infoList);
	    			webParam.setHtml(html);
	    			return webParam;
	    		}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	
	private List<HousingSuzhouAccountDetail> htmlParserDetail(String html, MessageLoginForHousing messageLoginForHousing, List<HousingSuzhouAccountDetail> infoList) {
		JSONObject obj = JSONObject.fromObject(html);
		String obj1 = obj.getString("recoreds"); 
		JSONArray fromObject2 = JSONArray.fromObject(obj1); 
		for (int i = 0; i < fromObject2.size(); i++) { 
				JSONArray fromObject3 = JSONArray.fromObject(fromObject2.toString()); 
				Object obj2 = fromObject3.get(i); 
				JSONObject fromObject4 = JSONObject.fromObject(obj2); 
				HousingSuzhouAccountDetail suzhouDetail = new HousingSuzhouAccountDetail();
				suzhouDetail.setHousingNum(fromObject4.getString("custacno"));
				suzhouDetail.setName(fromObject4.getString("custname"));
				suzhouDetail.setPostingDate(fromObject4.getString("acdate"));
				suzhouDetail.setMonth(fromObject4.getString("savemonth"));
				suzhouDetail.setBusinessType(fromObject4.getString("busidetailtype"));
				suzhouDetail.setDirection(fromObject4.getString("flag"));
				suzhouDetail.setOccurrenceAmount(fromObject4.getString("amt"));
				suzhouDetail.setBalance(fromObject4.getString("bal"));
				suzhouDetail.setFilingOffice(fromObject4.getString("bankname"));
				infoList.add(suzhouDetail);
		}
		return infoList;
	}


	public static String getNextLabelByKeyword(Document document, String keyword, String tag){ 
		Elements es = document.select(tag+":contains("+keyword+")"); 
		if(null != es && es.size()>0){ 
		Element element = es.first(); 
		Element nextElement = element.nextElementSibling(); 
			if(null != nextElement){ 
				return nextElement.text(); 
			} 
		} 
		return null; 
	}


	
}
