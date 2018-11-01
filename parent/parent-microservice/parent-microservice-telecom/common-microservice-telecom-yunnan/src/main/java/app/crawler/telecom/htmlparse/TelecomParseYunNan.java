package app.crawler.telecom.htmlparse;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.telecom.yunnan.TelecomYunNanBalanceResult;
import com.microservice.dao.entity.crawler.telecom.yunnan.TelecomYunNanBusinessResult;
import com.microservice.dao.entity.crawler.telecom.yunnan.TelecomYunNanCallThremResult;
import com.microservice.dao.entity.crawler.telecom.yunnan.TelecomYunNanIntegraChangeResult;
import com.microservice.dao.entity.crawler.telecom.yunnan.TelecomYunNanPayResult;
import com.microservice.dao.entity.crawler.telecom.yunnan.TelecomYunNanSMSThremResult;
import com.microservice.dao.entity.crawler.telecom.yunnan.TelecomYunNanUserInfoResult;

import app.bean.WebParamTelecom;

public class TelecomParseYunNan {
	public static WebParamTelecom<TelecomYunNanUserInfoResult> userinfo_parse(String html) {
		WebParamTelecom<TelecomYunNanUserInfoResult> webParam = new WebParamTelecom<TelecomYunNanUserInfoResult>();
		List<TelecomYunNanUserInfoResult> list = new ArrayList<>();

		System.out.println(html);
		System.out.println("==============userinfo_parse================");
		try {
			Document doc = Jsoup.parse(html);
			String username = doc.select("tr:contains(" + "客户名称" + ")").select("td").last().text();

			String usertype = doc.select("tr:contains(" + "客户类型" + ")").select("td").last().text();

			String useridcard = doc.select("tr:contains(" + "证件号码" + ")").select("td").last().text();

			String userpostcode = doc.select("tr:contains(" + "邮政编码" + ")").select("td").last().text();

			String address = doc.select("tr:contains(" + "通信地址" + ")").select("td").last().text();

			String useremail = doc.select("tr:contains(" + "E-mail" + ")").select("td").last().text();

			String userstardate = doc.select("tr:contains(" + "创建日期" + ")").select("td").last().text();
			TelecomYunNanUserInfoResult telecomYunNanUserInfoResult = new TelecomYunNanUserInfoResult();
			telecomYunNanUserInfoResult.setUsername(username);
			telecomYunNanUserInfoResult.setUsertype(usertype);
			telecomYunNanUserInfoResult.setUseridcard(useridcard);
			telecomYunNanUserInfoResult.setUserpostcode(userpostcode);
			telecomYunNanUserInfoResult.setUseremail(useremail);
			telecomYunNanUserInfoResult.setUserstardate(userstardate);
			telecomYunNanUserInfoResult.setAddress(address);
			list.add(telecomYunNanUserInfoResult);
		} catch (Exception e) {
			webParam.setErrormessage(e.getMessage());
			webParam.setHtml(html);
		}

		webParam.setList(list);
		return webParam;

	}

	/*
	 * public static List<TelecomQingHaiBillResult> bill_parse(String html) {
	 * 
	 * Document doc = Jsoup.parse(html, "utf-8");
	 * 
	 * List<TelecomQingHaiBillResult> result = new ArrayList<>();
	 * 
	 * Elements treles =
	 * doc.select("table.transact_tab").select("tbody").select("tr");
	 * 
	 * int i = 0; for (Element trele : treles) { i++; if (i == 1) { continue; }
	 * TelecomQingHaiBillResult telecomQingHaiBillResult; try {
	 * telecomQingHaiBillResult = new TelecomQingHaiBillResult();
	 * 
	 * telecomQingHaiBillResult.setDate(trele.select("td").get(0).text());
	 * telecomQingHaiBillResult.setNum(trele.select("td").get(1).text());
	 * result.add(telecomQingHaiBillResult); } catch (Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * try { telecomQingHaiBillResult = new TelecomQingHaiBillResult();
	 * telecomQingHaiBillResult.setDate(trele.select("td").get(5).text());
	 * telecomQingHaiBillResult.setNum(trele.select("td").get(6).text());
	 * result.add(telecomQingHaiBillResult);
	 * System.out.println(telecomQingHaiBillResult.toString()); } catch
	 * (Exception e) { e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * return result;
	 * 
	 * }
	 */

	public static WebParamTelecom<TelecomYunNanPayResult> payResult_parse(String html) {
		System.out.println(html);
		System.out.println("==============payResult_parse================");
		WebParamTelecom<TelecomYunNanPayResult> webParam = new WebParamTelecom<TelecomYunNanPayResult>();

		List<TelecomYunNanPayResult> list = new ArrayList<>();

		Document doc = Jsoup.parse(html, "utf-8");
		Elements trEles = doc.select("table.numList").select("tbody").select("tr");
		for (Element trEle : trEles) {
			try {
				TelecomYunNanPayResult telecomYunNanPayResult = new TelecomYunNanPayResult();
				telecomYunNanPayResult.setPhoneid(trEle.select("td").get(0).text());
				telecomYunNanPayResult.setBookedtime(trEle.select("td").get(1).text());
				telecomYunNanPayResult.setNum(trEle.select("td").get(2).text());
				telecomYunNanPayResult.setPaymethod(trEle.select("td").get(3).text());

				list.add(telecomYunNanPayResult);
			} catch (Exception e) {
				webParam.setErrormessage(e.getMessage());
				webParam.setHtml(html);
			}

		}

		webParam.setList(list);
		return webParam;
	}

	public static WebParamTelecom<TelecomYunNanBalanceResult> balance_parse(String html) {
		System.out.println(html);
		System.out.println("==============balance_parse================");
		WebParamTelecom<TelecomYunNanBalanceResult> webParam = new WebParamTelecom<TelecomYunNanBalanceResult>();
		Document doc = Jsoup.parse(html, "utf-8");

		List<TelecomYunNanBalanceResult> result = new ArrayList<>();

		try {
			Elements treles = doc.select("table.cxListBox").select("tbody").select("tr");

			TelecomYunNanBalanceResult telecomYunNanBalanceResult = new TelecomYunNanBalanceResult();

			telecomYunNanBalanceResult
					.setGeneralbalance(treles.select("tr:contains(" + "通用余额" + ")").select("td").last().text());

			telecomYunNanBalanceResult
					.setSpecialbalance(treles.select("tr:contains(" + "专用余额" + ")").select("td").last().text());
			telecomYunNanBalanceResult
					.setBalance(treles.select("tr:contains(" + "余额：" + ")").select("td").select("b").text());

			telecomYunNanBalanceResult.setResidualcreditline(doc.select("div:contains(" + "剩余信用额度-" + ")+div")
					.select("div:contains(" + "剩余信用额度" + ")+div").text());
			result.add(telecomYunNanBalanceResult);
			webParam.setList(result);
		} catch (Exception e) {
			e.printStackTrace();
			webParam.setErrormessage(e.getMessage());
			webParam.setHtml(html);
		}

		return webParam;
	}

	public static WebParamTelecom<TelecomYunNanIntegraChangeResult> integraChangeResult_parse(String html) {

		System.out.println(html);
		System.out.println("==============integraChangeResult_parse================");
		WebParamTelecom<TelecomYunNanIntegraChangeResult> webParam = new WebParamTelecom<TelecomYunNanIntegraChangeResult>();
		Document doc = Jsoup.parse(html, "utf-8");

		Elements tableeles = doc.select("table#bonus_addition").select("tbody");
		if (tableeles.text().isEmpty() || tableeles.text() == null) {
			webParam.setHtml(html);
			webParam.setErrormessage("==========无数据==========");
			return webParam;
		}
		try {
			Elements trEles = tableeles.select("tr");
			List<TelecomYunNanIntegraChangeResult> result = new ArrayList<>();

			int i = 0;

			for (Element trEle : trEles) {
				if (i == 0) {
					i++;
					continue;
				}
				i++;
				try {
					TelecomYunNanIntegraChangeResult telecomYunNanIntegraChangeResult = new TelecomYunNanIntegraChangeResult();

					telecomYunNanIntegraChangeResult.setMonth(trEle.select("td").get(0).text());
					telecomYunNanIntegraChangeResult.setConsumpoints(trEle.select("td").get(1).text());
					telecomYunNanIntegraChangeResult.setRewardpoints(trEle.select("td").get(2).text());
					telecomYunNanIntegraChangeResult.setTotal(trEle.select("td").get(3).text());
					result.add(telecomYunNanIntegraChangeResult);
				} catch (Exception e) {
					e.printStackTrace();
					webParam.setErrormessage(e.getMessage());
					webParam.setHtml(html);
				}

			}
			webParam.setList(result);
		} catch (Exception e) {
			e.printStackTrace();
			webParam.setHtml(html);
			webParam.setErrormessage(e.getMessage());
		}

		return webParam;
	}

	public static WebParamTelecom<TelecomYunNanCallThremResult> callThrem_parse(String html) {

		System.out.println(html);
		System.out.println("==============callThrem_parse================");
		WebParamTelecom<TelecomYunNanCallThremResult> webParam = new WebParamTelecom<TelecomYunNanCallThremResult>();
		Document doc = Jsoup.parse(html, "utf-8");
		if (doc.select("div.bill_content").text().indexOf("尊敬的客户，您所查询的条件内没有相应的记录") != -1) {
			webParam.setHtml(html);
			webParam.setErrormessage("无话单记录");
			return webParam;
		}
		List<TelecomYunNanCallThremResult> result = new ArrayList<>();
		try {
			Elements trEles = doc.select("table#details_table").first().select("tr");
			int i = 0;
			for (Element trEle : trEles) {

				if (i == 0) {
					i++;
					continue;
				}
				i++;
				try {
					TelecomYunNanCallThremResult telecomYunNanCallThremResult = new TelecomYunNanCallThremResult();

					telecomYunNanCallThremResult.setXuhao(trEle.select("td").get(0).text());

					telecomYunNanCallThremResult.setCalllocation(trEle.select("td").get(1).text());

					telecomYunNanCallThremResult.setCallphone(trEle.select("td").get(2).text());

					telecomYunNanCallThremResult.setCallphoneother(trEle.select("td").get(3).text());

					telecomYunNanCallThremResult.setDate(trEle.select("td").get(4).text());

					telecomYunNanCallThremResult.setCalltime(trEle.select("td").get(5).text());

					telecomYunNanCallThremResult.setCallcosts(trEle.select("td").get(6).text());

					telecomYunNanCallThremResult.setCalllongcosts(trEle.select("td").get(7).text());

					telecomYunNanCallThremResult.setCalltype(trEle.select("td").get(8).text());

					result.add(telecomYunNanCallThremResult);
				} catch (Exception e) {
					e.printStackTrace();
					webParam.setErrormessage(e.getMessage());
					webParam.setHtml(html);
				}

			}
			webParam.setList(result);
		} catch (Exception e) {
			webParam.setErrormessage(e.getMessage());
			webParam.setHtml(html);
		}

		return webParam;

	}

	public static WebParamTelecom<TelecomYunNanSMSThremResult> SMSThrem_parse(String html) {

		System.out.println(html);
		System.out.println("==============SMSThrem_parse================");
		WebParamTelecom<TelecomYunNanSMSThremResult> webParam = new WebParamTelecom<TelecomYunNanSMSThremResult>();
		Document doc = Jsoup.parse(html, "utf-8");
		if (doc.select("div.bill_content").text().indexOf("尊敬的客户，您所查询的条件内没有相应的记录") != -1) {
			webParam.setHtml(html);
			webParam.setErrormessage("无话单记录");
			return webParam;
		}
		List<TelecomYunNanSMSThremResult> result = new ArrayList<>();
		
		try {
			Elements trEles = doc.select("table#details_table").select("tbody").first().select("tr");
			int i = 0;
			for (Element trEle : trEles) {
				if (i == 0) {
					i++;
					continue;
				}
				i++;
				try {
					TelecomYunNanSMSThremResult telecomYunNanSMSThremResult = new TelecomYunNanSMSThremResult();

					telecomYunNanSMSThremResult.setXuhao(trEle.select("td").get(0).text());

					telecomYunNanSMSThremResult.setCallphone(trEle.select("td").get(1).text());

					telecomYunNanSMSThremResult.setCallphoneother(trEle.select("td").get(2).text());

					telecomYunNanSMSThremResult.setDate(trEle.select("td").get(3).text());

					telecomYunNanSMSThremResult.setCallcosts(trEle.select("td").get(4).text());

					telecomYunNanSMSThremResult.setCalltype(trEle.select("td").get(5).text());

					result.add(telecomYunNanSMSThremResult);
				} catch (Exception e) {
					e.printStackTrace();
					webParam.setErrormessage(e.getMessage());
					webParam.setHtml(html);
				}

			}
			webParam.setList(result);
		} catch (Exception e) {
			webParam.setErrormessage(e.getMessage());
			webParam.setHtml(html);
		}

		return webParam;

	}

	public static WebParamTelecom<TelecomYunNanBusinessResult> business_parse(String html) {
		System.out.println(html);
		System.out.println("==============business_parse================");
		WebParamTelecom<TelecomYunNanBusinessResult> webParam = new WebParamTelecom<TelecomYunNanBusinessResult>();
		List<TelecomYunNanBusinessResult> list = new ArrayList<>();
		try {
			Document doc = Jsoup.parse(html);
			// <table name="tabs_tcinfo
			Elements tableeles = doc.select("table.table");
			for (Element tableele : tableeles) {
				TelecomYunNanBusinessResult telecomYunNanBusinessResult = new TelecomYunNanBusinessResult();
				telecomYunNanBusinessResult
						.setBusinessname(tableele.select("tr:contains(" + "套餐名称" + ")").select("td").last().text());
				telecomYunNanBusinessResult.setBusinessstardate(
						tableele.select("tr:contains(" + "套餐生效日期" + ")").select("td").last().text());
				telecomYunNanBusinessResult.setBusinessenddate(
						tableele.select("tr:contains(" + "套餐失效日期" + ")").select("td").last().text());
				telecomYunNanBusinessResult.setBusinessdescription(
						tableele.select("tr:contains(" + "套餐描述" + ")").select("td").last().text());
				if (telecomYunNanBusinessResult.getBusinessname() == null
						|| telecomYunNanBusinessResult.getBusinessname().isEmpty()) {
					continue;
				}
				list.add(telecomYunNanBusinessResult);
			}
		} catch (Exception e) {
			e.printStackTrace();
			webParam.setErrormessage(e.getMessage());
			webParam.setHtml(html);
		}
		webParam.setList(list);
		return webParam;
	}

	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}

	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

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

	public static void main(String[] args) {
		try {
			balance_parse(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
