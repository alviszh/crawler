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
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.xuzhou.HousingXuzhouAccount;
import com.microservice.dao.entity.crawler.housing.xuzhou.HousingXuzhouDetail;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingFundXuzhouParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	

	//身份证登陆
	public WebParam loginByIdCard(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		//登陆界面
        String url="http://www.xzxxhm.com/xz_fund/fundbill/service/Fundbill.submitHandler.do?source=pc&idCode="+messageLoginForHousing.getNum()+"&fundCode="+messageLoginForHousing.getHosingFundNumber();
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setTimeout(30000);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);		
		HtmlPage page = webClient.getPage(webRequest);
		int status = page.getWebResponse().getStatusCode();
		webParam.setCode(page.getWebResponse().getStatusCode());
		webParam.setPage(page);
		webParam.setWebClient(page.getWebClient());
		return webParam;
	}


	public WebParam<HousingXuzhouAccount> getAccountInfo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing)throws Exception  {
		WebParam webParam= new WebParam();	
		String url = "http://www.xzxxhm.com/xz_fund/fundbill/service/fundbill.queryResult.do?"+taskHousing.getPassword();
		
		List<Page> pageList = new ArrayList<Page>();
		
		
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient.getOptions().setJavaScriptEnabled(false);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webRequest.setAdditionalHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("Accept-Encoding","gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Cache-Control","max-age=0");
			webRequest.setAdditionalHeader("Connection","keep-alive");
			webRequest.setAdditionalHeader("Host","www.xzxxhm.com");
			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests","1");
			webRequest.setAdditionalHeader("User-Agent","Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Mobile Safari/537.36");
			Page page = webClient.getPage(webRequest);
			int statusCode = page.getWebResponse().getStatusCode();
			List<HousingXuzhouAccount> infoList  = new ArrayList<HousingXuzhouAccount>();
			if(200== statusCode){
				String html  =page.getWebResponse().getContentAsString();
				tracer.addTag("HousingXuzhouParser.getAccountInfo 账户信息" + messageLoginForHousing.getTask_id(),
						"<xmp>" + html + "</xmp>");
				System.out.println(html);
				pageList.add(page);
				HousingXuzhouAccount	accountList= htmlParserAccount(html,messageLoginForHousing);
				if(null != infoList){
					webParam.setHousingXuzhouAccount(accountList);
					webParam.setHtml(html);
					webParam.setUrl(url);
					return webParam;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public WebParam<HousingXuzhouDetail> getDetailInfo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing)throws Exception   {
		WebParam webParam= new WebParam();	
		String url = "http://www.xzxxhm.com/xz_fund/fundbill/service/fundbill.queryResult.do?"+taskHousing.getPassword();
		
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient.getOptions().setJavaScriptEnabled(false);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webRequest.setAdditionalHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("Accept-Encoding","gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Cache-Control","max-age=0");
			webRequest.setAdditionalHeader("Connection","keep-alive");
			webRequest.setAdditionalHeader("Host","www.xzxxhm.com");
			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests","1");
			webRequest.setAdditionalHeader("User-Agent","Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Mobile Safari/537.36");
			List<HtmlPage> pageList = new ArrayList<HtmlPage>();
			Page page = webClient.getPage(webRequest);
			int statusCode = page.getWebResponse().getStatusCode();
			List<HousingXuzhouDetail> infoList = new ArrayList<HousingXuzhouDetail>();
			if(200== statusCode){
				String html  =page.getWebResponse().getContentAsString();
				tracer.addTag("HousingXuzhouParser.getDetailInfo 公积金明细" + messageLoginForHousing.getTask_id(),
						"<xmp>" + html + "</xmp>");
				infoList = htmlParserDetail(html,messageLoginForHousing,infoList);
				if(null !=infoList){
	    			webParam.setList(infoList);
	    			webParam.setUrl(url);
	    			webParam.setHtml(html);
	    			return webParam;
	    		}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	private List<HousingXuzhouDetail> htmlParserDetail(String html, MessageLoginForHousing messageLoginForHousing, List<HousingXuzhouDetail> infoList) {
		Document doc = Jsoup.parse(html);
		Elements baseInfo = doc.getElementById("detailBillTable").select("tr");
		int num =2;
		for(int i =num;i<baseInfo.size();i++){
			Elements tds = baseInfo.get(i).select("td");
			List<String> lists= new ArrayList<>();
			for (Element element : tds) {
				lists.add(element.text().trim());
			}
			if(null != lists){
				HousingXuzhouDetail xuzhouDetail = new HousingXuzhouDetail();
				xuzhouDetail.setDate(lists.get(0));
				xuzhouDetail.setAbstracts(lists.get(1));
				xuzhouDetail.setWithdrawalAmount(lists.get(2));
				xuzhouDetail.setIncomeAmount(lists.get(3));
				xuzhouDetail.setBalance(lists.get(4));
				xuzhouDetail.setTaskid(messageLoginForHousing.getTask_id());
				infoList.add(xuzhouDetail);
			}
		}
		return infoList;
	}


	private HousingXuzhouAccount htmlParserAccount(String html, MessageLoginForHousing messageLoginForHousing) {
		Document documentHtml = Jsoup.parse(html);
		String zgxm =getNextLabelByKeyword(documentHtml,"职工姓名","th");
		String sfzh =getNextLabelByKeyword(documentHtml,"身份证号","th");
		String zgzh =getNextLabelByKeyword(documentHtml,"职工账号","th");
		String szdw =getNextLabelByKeyword(documentHtml,"所在单位","th");
		String ssgjd =getNextLabelByKeyword(documentHtml,"所属归集点","th");
		String khrq =getNextLabelByKeyword(documentHtml,"开户日期","th");
		String dqzt =getNextLabelByKeyword(documentHtml,"当前状态","th");
		String dwyjyf =getNextLabelByKeyword(documentHtml,"单位应缴月份","th");
		String gryj =getNextLabelByKeyword(documentHtml,"个人月缴","th");
		String dwyj = getNextLabelByKeyword(documentHtml,"单位月缴","th");
		String yjje =getNextLabelByKeyword(documentHtml,"月缴金额","th");
		String snye =getNextLabelByKeyword(documentHtml,"上年余额","th");
		String bnbj =getNextLabelByKeyword(documentHtml,"本年补缴","th");
		String bnjj =getNextLabelByKeyword(documentHtml,"本年缴交","th");
		String bnzq =getNextLabelByKeyword(documentHtml,"本年支取","th");
		String bjye =getNextLabelByKeyword(documentHtml,"本金余额","th");
		HousingXuzhouAccount xuzhouAccount = new HousingXuzhouAccount();
		xuzhouAccount.setName(zgxm);
		xuzhouAccount.setIdNumber(sfzh);
		xuzhouAccount.setWorkerNumber(zgzh);
		xuzhouAccount.setUnitName(szdw);
		xuzhouAccount.setHomecat(ssgjd);
		xuzhouAccount.setOpeningDate(khrq);
		xuzhouAccount.setCurrentState(dqzt);
		xuzhouAccount.setShouldPay(dwyjyf);
		xuzhouAccount.setIndividualMonthPay(gryj);
		xuzhouAccount.setUnitMonthPay(dwyj);
		xuzhouAccount.setMonthPayNumber(yjje);
		xuzhouAccount.setLastYearBalance(snye);
		xuzhouAccount.setNowYearPay(bnbj);
		xuzhouAccount.setNowYearDue(bnjj);
		xuzhouAccount.setNowYearDraw(bnzq);
		xuzhouAccount.setPrincipalBalance(bjye);
		xuzhouAccount.setTaskid(messageLoginForHousing.getTask_id());
		return xuzhouAccount;
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
