package app.parser;

import java.net.URL;
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
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.xian.HousingXianBasic;
import com.microservice.dao.entity.crawler.housing.xian.HousingXianDetail;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingFundXianParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	

	public static String getNextLabelByKeyword(Elements baseInfo, String keyword, String tag){ 
		Elements es = baseInfo.select(tag+":contains("+keyword+")"); 
		if(null != es && es.size()>0){ 
		Element element = es.first(); 
		Element nextElement = element.nextElementSibling(); 
			if(null != nextElement){ 
				return nextElement.text(); 
			} 
		} 
		return null; 
	}


	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing)throws Exception  {
		String url = "http://query.xazfgjj.gov.cn/";
		
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);		
		HtmlPage page = webClient.getPage(webRequest);
		int status = page.getWebResponse().getStatusCode();
		if(status == 200){
			HtmlTextInput num = (HtmlTextInput)page.getFirstByXPath("//input[@id='wbidcard']");
			HtmlImage image = page.getFirstByXPath("//img[@onclick='change_imgcode199871(this)']");
			HtmlPasswordInput pwd = page.getFirstByXPath("//input[@id='wbmima']");
			String code = chaoJiYingOcrService.getVerifycode(image, "5000");
			HtmlTextInput verifyCode = (HtmlTextInput)page.getFirstByXPath("//input[@id='yanzheng199871']");
			HtmlTextInput username = page.getFirstByXPath("//input[@id='wbzhigongname']");
			username.setText(messageLoginForHousing.getUsername());
			num.setText(messageLoginForHousing.getNum());
			verifyCode.setText(code);
			pwd.setText(messageLoginForHousing.getPassword());
			HtmlElement button = (HtmlElement)page.getFirstByXPath("//input[@value='登录']");
			
			HtmlPage loadPage = button.click();
			
			webParam.setCode(loadPage.getWebResponse().getStatusCode());
			webParam.setPage(loadPage);
			webParam.setWebClient(webClient);
			return webParam;
			
		}
		
		return null;
	}


	public WebParam<HousingXianBasic> getAccountInfo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing)throws Exception  {
		WebParam webParam= new WebParam();
		String cookies = taskHousing.getCookies(); 
		WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies); 
		for (Cookie cookie : cookies1) { 
		webClient.getCookieManager().addCookie(cookie); 
		}
		String url = "http://query.xazfgjj.gov.cn/wsbs/gjjcx_gjjxxcx.jsp?urltype=tree.TreeTempUrl&wbtreeid=1078";
						
		
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

			HtmlPage page = (HtmlPage)webClient.getPage(webRequest); 
			String html3 = page.getWebResponse().getContentAsString();
			if(html3.contains("月缴基数")){
			HousingXianBasic basic =htmlParserAccount(html3,messageLoginForHousing);
			webParam.setCode(page.getWebResponse().getStatusCode());
			webParam.setHousingXianBasic(basic);
			webParam.setUrl(url);
			webParam.setHtml(html3);
			webParam.setPage(page);
			return webParam;
			}
			return null;
	}



	private HousingXianBasic htmlParserAccount(String html3, MessageLoginForHousing messageLoginForHousing) {
		Document documentHtml = Jsoup.parse(html3);
		Elements baseInfo  = documentHtml.getElementsByAttributeValue("name","a199848a");
		
		String zgzh= getNextLabelByKeyword(baseInfo,"职工账号","td");
		String yjjs= getNextLabelByKeyword(baseInfo,"月缴基数","td");
		String dwzh= getNextLabelByKeyword(baseInfo,"单位账号","td");
		String ssbsc= getNextLabelByKeyword(baseInfo,"所属办事处","td");
		String szdw= getNextLabelByKeyword(baseInfo,"所在单位","td");
		String khyh= getNextLabelByKeyword(baseInfo,"开户银行","td");
		String khrq= getNextLabelByKeyword(baseInfo,"开户日期","td");
		String dqzt= getNextLabelByKeyword(baseInfo,"当前状态","td");
		String grjcbl= getNextLabelByKeyword(baseInfo,"个人缴存比例","td");
		String dwjcbl= getNextLabelByKeyword(baseInfo,"单位缴存比例","td");
		String snye= getNextLabelByKeyword(baseInfo,"上年余额","td");
		String bnjj= getNextLabelByKeyword(baseInfo,"本年缴交","td");
		String wbzr= getNextLabelByKeyword(baseInfo,"外部转入","td");
		String bnzq= getNextLabelByKeyword(baseInfo,"本年支取","td");
		String snlx= getNextLabelByKeyword(baseInfo,"上年利息","td");
		String ye= getNextLabelByKeyword(baseInfo,"余额","td");
		HousingXianBasic  xianBasic = new HousingXianBasic();
		
		xianBasic.setWorkNum(zgzh);
		xianBasic.setMonthBase(yjjs);
		xianBasic.setCompanyNum(dwzh);
		xianBasic.setOffice(ssbsc);
		xianBasic.setCompany(szdw);
		xianBasic.setBank(khyh);;
		xianBasic.setDate(khrq);
		xianBasic.setNowState(dqzt);
		xianBasic.setPersonPay(grjcbl);
		xianBasic.setCompanyPay(dwjcbl);
		xianBasic.setLastYearBalance(snye);
		xianBasic.setYearPay(bnjj);
		xianBasic.setExternalTransfer(wbzr);
		xianBasic.setYearDraw(bnzq);
		xianBasic.setLastYearInterest(snlx);
		xianBasic.setBalance(ye);
		xianBasic.setTaskid(messageLoginForHousing.getTask_id());
		return xianBasic;
	}


	public WebParam<HousingXianDetail> getDetailInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		WebParam webParam= new WebParam();	
		
		String url = "http://query.xazfgjj.gov.cn/wsbs/gjjcx_gjjmxcx.jsp?urltype=tree.TreeTempUrl&wbtreeid=1079";
		try {
			String cookies = taskHousing.getCookies(); 
			WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
			Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies); 
			for (Cookie cookie : cookies1) { 
			webClient.getCookieManager().addCookie(cookie); 
			}
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			List<HtmlPage> pageList = new ArrayList<HtmlPage>();
			Page page = webClient.getPage(webRequest);
			int statusCode = page.getWebResponse().getStatusCode();
			List<HousingXianDetail>  infoList = new ArrayList<HousingXianDetail>();
			if(200== statusCode){
				String html  =page.getWebResponse().getContentAsString();
				tracer.addTag("HousingXianParser.getDetailInfo 公积金明细" + messageLoginForHousing.getTask_id(),
						"<xmp>" + html + "</xmp>");
				if(html.contains("摘要")){
				infoList = htmlParserDetail(html,messageLoginForHousing,infoList);
				if(null !=infoList){
	    			webParam.setList(infoList);
	    			webParam.setUrl(url);
	    			webParam.setHtml(html);
	    			return webParam;
	    		}
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}


	private List<HousingXianDetail> htmlParserDetail(String html, MessageLoginForHousing messageLoginForHousing,List<HousingXianDetail> infoList) {
		Document doc = Jsoup.parse(html);
		Elements baseInfo = doc.getElementsByAttributeValue("name","a199849a").select("tr");
		
		for(int i =2;i<baseInfo.size();i++){
			Elements tds = baseInfo.get(i).select("td");
			List<String> lists= new ArrayList<>();
			for (Element element : tds) {
				lists.add(element.text().trim());
			}
			if(null != lists){
				HousingXianDetail xianDetail =  new HousingXianDetail();
				xianDetail.setDate(lists.get(0));
				xianDetail.setRemark(lists.get(1));
				xianDetail.setIncome(lists.get(2));
				xianDetail.setTaskid(messageLoginForHousing.getTask_id());
				infoList.add(xianDetail);
			}
		}
		return infoList;
	}

	
}
