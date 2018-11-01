package app.parser;

import java.net.URL;
import java.nio.charset.Charset;
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
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.bozhou.HousingBoZhouDetail;
import com.microservice.dao.entity.crawler.housing.bozhou.HousingBoZhouUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingFundBozhouParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	
	
	
	
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
	
	public WebParam<HousingBoZhouUserInfo> getUserInfo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing)throws Exception {
		try {
			WebParam webParam= new WebParam();
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient.getOptions().setJavaScriptEnabled(false);
			String url = "http://60.174.83.177:8098/wjbzh5/accumulation/listResult.do?type=1&userName="+messageLoginForHousing.getUsername()+"&accountNo="+messageLoginForHousing.getHosingFundNumber();
			WebRequest  requestSettings = new WebRequest(new URL(url), HttpMethod.GET); 
			
			requestSettings.setCharset(Charset.forName("UTF-8"));
			HtmlPage page = webClient.getPage(requestSettings);
			List<HousingBoZhouUserInfo>  infoList = new ArrayList<HousingBoZhouUserInfo>();
			String html = page.getWebResponse().getContentAsString();
			if(html.contains("工作单位")){
				infoList = htmlParserAccount(html,messageLoginForHousing,infoList);
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

	
	
	public WebParam<HousingBoZhouDetail> getDetail(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing) {
		try {
			WebParam webParam= new WebParam();
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient.getOptions().setJavaScriptEnabled(false);
			String url = "http://60.174.83.177:8098/wjbzh5/accumulation/listResult.do?type=1&userName="+messageLoginForHousing.getUsername()+"&accountNo="+messageLoginForHousing.getHosingFundNumber();
			WebRequest  requestSettings = new WebRequest(new URL(url), HttpMethod.GET); 
			
			requestSettings.setCharset(Charset.forName("UTF-8"));
			HtmlPage page = webClient.getPage(requestSettings);
			List<HousingBoZhouDetail>  infoList = new ArrayList<HousingBoZhouDetail>();
			String html = page.getWebResponse().getContentAsString();
			if(html.contains("工作单位")){
				infoList =htmlParserDetail(html,messageLoginForHousing,infoList);
				webParam.setList(infoList);
				webParam.setHtml(html);
				return webParam;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return null;
	}

	private List<HousingBoZhouDetail> htmlParserDetail(String html, MessageLoginForHousing messageLoginForHousing,List<HousingBoZhouDetail> infoList) {
		try {
			WebParam webParam = new WebParam();
			Document doc = Jsoup.parse(html);
			Elements baseInfo = doc.getElementsByClass("accumulationContent-contentUl").select("li");
			for(int i =0;i<baseInfo.size();i++){
				Elements tds = baseInfo.get(i).select("div");
				List<String> lists= new ArrayList<>();
				for (Element element : tds) {
					lists.add(element.text().trim());
				}
				if(null!=lists){
					HousingBoZhouDetail detail = new HousingBoZhouDetail();
					detail.setAbs(lists.get(1));
					detail.setTime(lists.get(2));
					detail.setDeposit(lists.get(3));
					detail.setCurrentBalance(lists.get(4));
					detail.setTaskid(messageLoginForHousing.getTask_id());
					infoList.add(detail);
				}
			}
			return infoList;
		} catch (Exception e){
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	private List<HousingBoZhouUserInfo> htmlParserAccount(String html, MessageLoginForHousing messageLoginForHousing, List<HousingBoZhouUserInfo> infoList) {
		WebParam webParam = new WebParam();
		Document doc = Jsoup.parse(html);
		Elements baseInfo = doc.getElementsByClass("loadsearch-titleTxtColor");
		for(int i =0;i<baseInfo.size();i++){
			List<String> lists= new ArrayList<>();
			for (Element element : baseInfo) {
				lists.add(element.text().trim());
			}
		if(null!=lists){
			HousingBoZhouUserInfo userInfo = new HousingBoZhouUserInfo();
			userInfo.setName(lists.get(0));
			userInfo.setNum(lists.get(1));
			userInfo.setUnitaccname(lists.get(2));
			userInfo.setTaskid(messageLoginForHousing.getTask_id());
			infoList.add(userInfo);
			return infoList;
		}
	}
		return null;
	}
	
}
