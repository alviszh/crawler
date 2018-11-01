package app.service.crawler.telecom.htmlparse;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.telecom.beijing.TelecomBeijingCallThremResult;
import com.microservice.dao.entity.crawler.telecom.beijing.TelecomBeijingChargesResult;
import com.microservice.dao.entity.crawler.telecom.beijing.TelecomBeijingIntegraResult;
import com.microservice.dao.entity.crawler.telecom.beijing.TelecomBeijingPayResult;
import com.microservice.dao.entity.crawler.telecom.beijing.TelecomBeijingPhoneBill;
import com.microservice.dao.entity.crawler.telecom.beijing.TelecomBeijingSMSThremResult;
import com.microservice.dao.entity.crawler.telecom.beijing.TelecomBeijingUserInfo;
import app.bean.CallThremBean;
import app.bean.SMSThremBean;
import app.commontracerlog.TracerLog;

@Service
public class TelecomParseBeijingService {
	@Autowired
	private TracerLog tracerLog;
	
	public  TelecomBeijingUserInfo userinfo_parse(String html) {
		TelecomBeijingUserInfo result = new TelecomBeijingUserInfo();
		Document doc = Jsoup.parse(html, "utf-8");

		String name = doc.select("th:contains(客户名称)+td").first().text();
		result.setName(name);
		String type = doc.select("th:contains(客户类型)+td").first().text();

		result.setType(type);
		Elements contactnumberEles = doc.select("th:contains(联系电话)+td");
		String contactnumber = null;
		for (Element contactnumberEle : contactnumberEles) {
			if (contactnumber == null) {
				contactnumber = contactnumberEle.text();
			} else {
				contactnumber = contactnumber + ";" + contactnumberEle.text();
			}

		}
		result.setContactnumber(contactnumber);

		String address = doc.select("th:contains(通讯地址)+td").first().text();

		result.setAddress(address);

		String postcode = doc.select("th:contains(邮政编码)+td").first().text();

		result.setPostcode(postcode);

		String email = doc.select("th:contains(E-mail)+td").first().text();
		result.setEmail(email);

		String nettime = doc.select("th:contains(入网时间)+td").first().text();
		result.setNettime(nettime);

		return result;

	}

	public  List<TelecomBeijingPhoneBill> phoneBill_parse(String html) {

		Document doc = Jsoup.parse(html, "utf-8");

		List<TelecomBeijingPhoneBill> result = new ArrayList<>();

		Elements treles = doc.select("tbody#userBill").select("tr");

		for (Element trele : treles) {
			TelecomBeijingPhoneBill telecomBeijingPhoneBill = new TelecomBeijingPhoneBill();
			telecomBeijingPhoneBill.setDate(trele.select("td").get(0).text());

			telecomBeijingPhoneBill.setBillNumber(trele.select("td").get(1).text());

			telecomBeijingPhoneBill.setBillCustom(trele.select("td").get(2).text());

			telecomBeijingPhoneBill.setOther(trele.select("td").get(3).text());
			result.add(telecomBeijingPhoneBill);
		}

		return result;

	}

	public  List<TelecomBeijingPayResult> payResult_parse(String html) {

		Document doc = Jsoup.parse(html, "utf-8");
		List<TelecomBeijingPayResult> result = new ArrayList<>();
		Elements trEles = doc.select("tbody").select("tr");
		if (trEles.toString().indexOf("无交费信息查询记录") != -1) {
			tracerLog.addTag("==============>中国电信抓取客户北京用户余额和积<===============", "无交费信息查询记录");
			return null;
		}
		for (Element trEle : trEles) {

			if (trEle.toString().indexOf("无交费信息查询记录") != -1) {
				tracerLog.addTag("中国电信抓取客户北京用户余额和积", "无交费信息查询记录");
				tracerLog.addTag("中国电信抓取客户北京用户余额和积","<xmp>"+trEle+"</xmp>");
				continue;
			}
			if(trEle.text()=="无"||trEles.text().isEmpty()||trEles.text()==null){
				tracerLog.addTag("==============>中国电信抓取客户北京用户余额和积<===============","<xmp>"+trEle+"</xmp>");
				continue;
			}
			try {
				TelecomBeijingPayResult telecomBeijingPayResult = new TelecomBeijingPayResult();
				telecomBeijingPayResult.setSerialnumber(trEle.select("td").get(0).text());
				telecomBeijingPayResult.setBookedtime(trEle.select("td").get(1).text());
				telecomBeijingPayResult.setPhone(trEle.select("td").get(2).text());
				telecomBeijingPayResult.setNum(trEle.select("td").get(3).text());
				telecomBeijingPayResult.setPaychannels(trEle.select("td").get(4).text());

				telecomBeijingPayResult.setPaymethod(trEle.select("td").get(5).text());

				telecomBeijingPayResult.setApplicable(trEle.select("td").get(6).text());
				result.add(telecomBeijingPayResult);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return result;
	}

	public  List<TelecomBeijingIntegraResult> integraResult_parse(String html) {

		Document doc = Jsoup.parse(html, "utf-8");
		// System.out.println(doc);
		List<TelecomBeijingIntegraResult> result = new ArrayList<>();
		Elements trEles = doc.select("tbody").first().select("tr.phBean");
		for (Element trEle : trEles) {
			TelecomBeijingIntegraResult telecomBeijingIntegraResult = new TelecomBeijingIntegraResult();
			telecomBeijingIntegraResult.setDate(trEle.select("td").get(0).text());
			telecomBeijingIntegraResult.setType(trEle.select("td").get(1).text());
			telecomBeijingIntegraResult.setNum(trEle.select("td").get(2).text());
			telecomBeijingIntegraResult.setPhone(trEle.select("td").get(3).text());

			result.add(telecomBeijingIntegraResult);
		}

		return result;

	}

	public  TelecomBeijingChargesResult chargesResult_parse(String html) {

		Document doc = Jsoup.parse(html, "utf-8");
		Elements trEles = doc.select("div:contains(欠费)").select("ul.clearfix");
		TelecomBeijingChargesResult result = new TelecomBeijingChargesResult();
		result.setArrears1(trEles.select("li").get(0).text());
		result.setArrears2(trEles.select("li").get(1).text());
		trEles = doc.select("li:contains(余额)").select("span.color-6");
		result.setCharges1(trEles.get(0).text());
		result.setCharges2(trEles.get(1).text());
		result.setCharges3(trEles.get(2).text());
		result.setCharges4(trEles.get(3).text());

		return result;

	}

	public  CallThremBean callThrem_parse(String html) {
		CallThremBean callThremBean = new CallThremBean();
		if (html.indexOf("系统正忙，请稍后再试") != -1) {
			callThremBean.setPagenum(0);
			return callThremBean;
		}
		
		Document doc = Jsoup.parse(html, "utf-8");
		List<TelecomBeijingCallThremResult> result = new ArrayList<>();
		try{
			Elements trEles = doc.select("table.ued-table").select("tbody").first().select("tr");
			int trnum = 0;
			for (Element trEle : trEles) {
				trnum++;
				if (trnum == 1) {
					continue;
				}
				
				if(trEle.toString().indexOf("共")!=-1 && trEle.toString().indexOf("首页")!=-1){
					continue;
				}
				if(trEle.toString().indexOf("下载详单")!=-1 ){
					continue;
				}

				TelecomBeijingCallThremResult telecomBeijingCallThremResult = new TelecomBeijingCallThremResult();
				try {
					telecomBeijingCallThremResult.setCallid(trEle.select("td").get(0).text());

					telecomBeijingCallThremResult.setCalltype1(trEle.select("td").get(1).text());

					telecomBeijingCallThremResult.setCalltype2(trEle.select("td").get(2).text());

					telecomBeijingCallThremResult.setAddress(trEle.select("td").get(3).text());

					telecomBeijingCallThremResult.setOthernum(trEle.select("td").get(4).text());

					telecomBeijingCallThremResult.setStartdate(trEle.select("td").get(5).text());

					telecomBeijingCallThremResult.setCallcosts(trEle.select("td").get(6).text());

					telecomBeijingCallThremResult.setOthercosts(trEle.select("td").get(7).text());

					telecomBeijingCallThremResult.setCalltime(trEle.select("td").get(8).text());

					telecomBeijingCallThremResult.setCosts(trEle.select("td").get(9).text());

					result.add(telecomBeijingCallThremResult);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			callThremBean.setResult(result);

			try {
				int pagenum = Integer.parseInt(doc.select("label.fr").select("a.on").last().text());
				callThremBean.setPagenum(pagenum);
			} catch (Exception e) {
				callThremBean.setPagenum(0);
			}

			return callThremBean;
		}catch(Exception e){
			e.printStackTrace();
			
			return null;
		}
		

	}

	public  SMSThremBean sms_parse(String html) {
		SMSThremBean smsThremBean = new SMSThremBean();
		if (html.indexOf("系统正忙，请稍后再试") != -1) {
			return smsThremBean;
		}
		
		Document doc = Jsoup.parse(html, "utf-8");
		List<TelecomBeijingSMSThremResult> result = new ArrayList<>();
		if(doc.select("table.ued-table") == null){
			return null;
		}
		if(doc.select("table.ued-table").select("tbody")== null){
			return null;
		}
		Elements trEles = doc.select("table.ued-table").select("tbody").first().select("tr");
		int trnum = 0;
		for (Element trEle : trEles) {
			trnum++;
			if (trnum == 1) {

				continue;
			}
			
			if(trEle.toString().indexOf("共")!=-1 && trEle.toString().indexOf("条")!=-1){
				continue;
			}
			if(trEle.toString().indexOf("下载详单")!=-1 ){
				continue;
			}

			TelecomBeijingSMSThremResult telecomBeijingSMSThremResult = new TelecomBeijingSMSThremResult();
			try {
				telecomBeijingSMSThremResult.setCallid(trEle.select("td").get(0).text());

				telecomBeijingSMSThremResult.setType(trEle.select("td").get(1).text());

				telecomBeijingSMSThremResult.setSmstype(trEle.select("td").get(2).text());

				telecomBeijingSMSThremResult.setOthernum(trEle.select("td").get(3).text());

				telecomBeijingSMSThremResult.setDate(trEle.select("td").get(4).text());

				telecomBeijingSMSThremResult.setCosts(trEle.select("td").get(5).text());

				result.add(telecomBeijingSMSThremResult);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		smsThremBean.setResult(result);

		try{
			int pagenums = Integer.parseInt(doc.select("label.fr").select("a.on").last().text());
			smsThremBean.setPagenum(pagenums);
		}catch(Exception e){
			smsThremBean.setPagenum(0);
		}
		
		return smsThremBean;

	}

	public  HtmlPage getHtml(String url, WebClient webClient) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}

	public  Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

}
