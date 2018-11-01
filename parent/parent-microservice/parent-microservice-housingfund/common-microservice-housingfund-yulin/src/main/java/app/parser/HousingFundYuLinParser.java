package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
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
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.yulin.HousingYuLinBasic;
import com.microservice.dao.entity.crawler.housing.yulin.HousingYuLinDetail;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;

@Component
public class HousingFundYuLinParser {
	@Autowired
	private TracerLog tracer;
	
	
	

	public WebParam<HousingYuLinDetail> getDetailInfo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing)throws Exception {

		WebParam webParam= new WebParam();
		Base64 base64 = new Base64();
		String sid =messageLoginForHousing.getNum()+" "+messageLoginForHousing.getPassword();
	    byte[] textByte = sid.getBytes("UTF-8");
		String encodedText = base64.encodeToString(textByte);
		String url ="http://www.gxylgjj.com/base_info3.asp?sid="+encodedText;
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		
		String html = page.getWebResponse().getContentAsString();
		if(html.contains("您填写的用户名或密码有误，请重新填写准确的信息！")) {
				tracer.addTag("HousingDalibaizuParser.getDetailInfo 输入的信息不正确！" + messageLoginForHousing.getTask_id(),
						"<xmp>" + html + "</xmp>");
				return null;
		}else{
			List<HousingYuLinDetail> infoList = new ArrayList<HousingYuLinDetail>();
			tracer.addTag("HousingYulinParser.getDetailInfo 个人公积金明细" + messageLoginForHousing.getTask_id(),
					"<xmp>" + html + "</xmp>");
			infoList = htmlParserDetail(html,messageLoginForHousing,infoList);
			if(null!=infoList){
				webParam.setList(infoList);
				webParam.setUrl(url);
				webParam.setHtml(html);
				return webParam;
			}
		}
		return null;
	}



	private List<HousingYuLinDetail> htmlParserDetail(String html, MessageLoginForHousing messageLoginForHousing,List<HousingYuLinDetail> infoList) {
		Document doc = Jsoup.parse(html);
		Element baseInfo = doc.getElementsByAttributeValue("width","546").first();
		Elements trs = baseInfo.select("tr");
		for (int i = 1; i < trs.size(); i++) {
				Elements tds = trs.get(i).select("td");
				HousingYuLinDetail detail = new HousingYuLinDetail();
				detail.setPayYaer(tds.get(0).text());
				detail.setBusinessType(tds.get(1).text());
				detail.setBackMoney(tds.get(2).text());
				detail.setPersonPay(tds.get(3).text());
				detail.setCompanyPay(tds.get(4).text());
				detail.setTaskid(messageLoginForHousing.getTask_id());
				infoList.add(detail);
		}
		return infoList;
	}



	public WebParam<HousingYuLinBasic> getBasicInfo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing)throws Exception  {
		WebParam webParam= new WebParam();
		Base64 base64 = new Base64();
		String sid =messageLoginForHousing.getNum()+" "+messageLoginForHousing.getPassword();
	    byte[] textByte = sid.getBytes("UTF-8");
		String encodedText = base64.encodeToString(textByte);
		String url ="http://www.gxylgjj.com/base_info3.asp?sid="+encodedText;
		List<Page> pageList = new ArrayList<Page>();
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient.getOptions().setJavaScriptEnabled(false);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			int statusCode = page.getWebResponse().getStatusCode();
			List<HousingYuLinBasic> infoList = new ArrayList<HousingYuLinBasic>();
			if(200==statusCode){
				String html  =page.getWebResponse().getContentAsString();
				tracer.addTag("HousingYuLinParser.getBasicInfo 账户基本信息" + messageLoginForHousing.getTask_id(),
						"<xmp>" + html + "</xmp>");
				pageList.add(page);
				HousingYuLinBasic  basicList = htmlParserBasic(html,messageLoginForHousing);
				if(null!=basicList){
					webParam.setYulinBasic(basicList);
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



	private HousingYuLinBasic htmlParserBasic(String html, MessageLoginForHousing messageLoginForHousing) {
		Document documentHtml = Jsoup.parse(html);
		Elements doc = documentHtml.getElementsByAttributeValue("width", "470");
		if(null != doc && doc.size() > 0){
			Element document = doc.get(0);
			String zgzh = getNextLabelByKeyword(document,"职工账号","td");
			String zgxm = getNextLabelByKeyword(document,"职工姓名","td");
			String dwmc = getNextLabelByKeyword(document,"单位名称","td");
			String sfzhm =getNextLabelByKeyword(document,"身份证号码","td");
			String khsj = getNextLabelByKeyword(document,"开户时间","td");
			String ycje = getNextLabelByKeyword(document,"月存金额","td");
			String gryce= getNextLabelByKeyword(document,"个人月存额","td");
			String dwyce= getNextLabelByKeyword(document,"单位月存额","td");
			String bnzq = getNextLabelByKeyword(document,"本年支取","td");
			String bjye = getNextLabelByKeyword(document,"本金余额","td");
			String zgzt = getNextLabelByKeyword(document,"职工状态","td");
			HousingYuLinBasic basic = new HousingYuLinBasic();
			basic.setWorkNum(zgzh);
			basic.setName(zgxm);
			basic.setCompany(dwmc);
			basic.setNum(sfzhm);
			basic.setDate(khsj);
			basic.setMonthPay(ycje);
			basic.setPerMonthPay(gryce);
			basic.setCompanyMonthPay(dwyce);
			basic.setYearDraw(bnzq);
			basic.setThisYearBalance(bjye);
			basic.setWorkStatus(zgzt);
			basic.setTaskid(messageLoginForHousing.getTask_id());
			return basic;
		}
		return null;
	}
	
	
	public static String getNextLabelByKeyword(Element document, String keyword, String tag){ 
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
