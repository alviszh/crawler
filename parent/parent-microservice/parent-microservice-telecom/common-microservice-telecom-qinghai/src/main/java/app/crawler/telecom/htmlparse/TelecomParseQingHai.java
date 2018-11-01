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
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.telecom.qinghai.TelecomQingHaiIntergentResult;
import com.microservice.dao.entity.crawler.telecom.qinghai.TelecomQingHaiPayResult;
import com.microservice.dao.entity.crawler.telecom.qinghai.TelecomQingHaiBillResult;
import com.microservice.dao.entity.crawler.telecom.qinghai.TelecomQingHaiCallThremResult;
import com.microservice.dao.entity.crawler.telecom.qinghai.TelecomQingHaiSMSThremResult;
import com.microservice.dao.entity.crawler.telecom.qinghai.TelecomQingHaiUserInfoResult;
import com.module.htmlunit.WebCrawler;

import app.bean.TelecomQingHaiIntergentRootBean;
import app.bean.TelecomQingHaiUserHandledBizDataRootBean;
import app.bean.TelecomQingHaiUserRootBean;
import app.bean.TelecomQingHaiUserUseablePointRootBean;
import app.bean.WebParamTelecom;

public class TelecomParseQingHai {
	private static Gson gs = new Gson();


	public static TelecomQingHaiUserInfoResult userinfo_parse(String html, int type,
			TelecomQingHaiUserInfoResult telecomQingHaiUserInfoResult) {
		try {
			if (type == 2) {

				TelecomQingHaiUserHandledBizDataRootBean jsonObject = gs.fromJson(html,
						TelecomQingHaiUserHandledBizDataRootBean.class);
				telecomQingHaiUserInfoResult.setOfferName(jsonObject.getData().getOfferName());
				telecomQingHaiUserInfoResult.setValueAddedSize(jsonObject.getData().getValueAddedSize());
				return telecomQingHaiUserInfoResult;

			}
			if (type == 3) {
				TelecomQingHaiUserUseablePointRootBean jsonObject = gs.fromJson(html,
						TelecomQingHaiUserUseablePointRootBean.class);
				telecomQingHaiUserInfoResult.setCurrentAddPoint(jsonObject.getData().getCurrentAddPoint());
				telecomQingHaiUserInfoResult.setCurrentAvPoint(jsonObject.getData().getCurrentAvPoint());
				telecomQingHaiUserInfoResult.setCurrentCusPoint(jsonObject.getData().getCurrentCusPoint());
				telecomQingHaiUserInfoResult.setCurrentYearCusPoint(jsonObject.getData().getCurrentYearCusPoint());
				return telecomQingHaiUserInfoResult;
			} else {
				TelecomQingHaiUserRootBean<?> jsonObject = gs.fromJson(html, TelecomQingHaiUserRootBean.class);
				telecomQingHaiUserInfoResult.setBalance(jsonObject.getData() + "");

				return telecomQingHaiUserInfoResult;
			}
		} catch (Exception e) {
			return telecomQingHaiUserInfoResult;
		}

	}

	public static String userinfo_parseName(String html) {
		Document doc = Jsoup.parse(html, "utf-8");

		Elements treles = doc.select("h4.uinfoname").select("strong");

		return treles.text();
	}

	public static List<TelecomQingHaiBillResult> bill_parse(String html) {

		Document doc = Jsoup.parse(html, "utf-8");

		List<TelecomQingHaiBillResult> result = new ArrayList<>();

		Elements treles = doc.select("table.transact_tab").select("tbody").select("tr");

		int i = 0;
		for (Element trele : treles) {
			i++;
			if (i == 1) {
				continue;
			}
			TelecomQingHaiBillResult telecomQingHaiBillResult;
			try {
				telecomQingHaiBillResult = new TelecomQingHaiBillResult();

				telecomQingHaiBillResult.setDate(trele.select("td").get(0).text());
				telecomQingHaiBillResult.setNum(trele.select("td").get(1).text());
				result.add(telecomQingHaiBillResult);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				telecomQingHaiBillResult = new TelecomQingHaiBillResult();
				telecomQingHaiBillResult.setDate(trele.select("td").get(5).text());
				telecomQingHaiBillResult.setNum(trele.select("td").get(6).text());
				result.add(telecomQingHaiBillResult);
				System.out.println(telecomQingHaiBillResult.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return result;

	}

	public static List<TelecomQingHaiPayResult> payResult_parse(String html) {

		Document doc = Jsoup.parse(html, "utf-8");
		List<TelecomQingHaiPayResult> result = new ArrayList<>();
		Elements trEles = doc.select("tbody.tc").select("tr");
		for (Element trEle : trEles) {

			if (trEle.toString().indexOf("无交费信息查询记录") != -1) {
				continue;
			}
			try {
				TelecomQingHaiPayResult telecomQingHaiPayResult = new TelecomQingHaiPayResult();
				telecomQingHaiPayResult.setPhoneid(trEle.select("td").get(0).text());
				telecomQingHaiPayResult.setBookedtime(trEle.select("td").get(1).text());
				telecomQingHaiPayResult.setNum(trEle.select("td").get(2).text());
				telecomQingHaiPayResult.setPaymethod(trEle.select("td").get(3).text());
				telecomQingHaiPayResult.setPayaddress(trEle.select("td").get(4).text());

				result.add(telecomQingHaiPayResult);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return result;
	}

	public static WebParamTelecom<TelecomQingHaiIntergentResult> integraResult_parse(String html) {
		html = "{'telecomQingHaiIntergentResultlist':" + html + "}";
		WebParamTelecom<TelecomQingHaiIntergentResult> webParam = new WebParamTelecom<TelecomQingHaiIntergentResult>();
		try {
			TelecomQingHaiIntergentRootBean jsonObject = gs.fromJson(html, TelecomQingHaiIntergentRootBean.class);
			webParam.setList(jsonObject.getIntergent());
			return webParam;
		} catch (Exception e) {
			webParam.setErrormessage(e.toString());
			return webParam;
		}
	}

	public static WebParamTelecom<TelecomQingHaiCallThremResult> callThrem_parse(String html) {
		WebParamTelecom<TelecomQingHaiCallThremResult> webParam = new WebParamTelecom<TelecomQingHaiCallThremResult>();
		Document doc = Jsoup.parse(html, "utf-8");
		if (doc.select("strong.red").text().indexOf("无话单记录") != -1) {
			webParam.setHtml(html);
			webParam.setErrormessage("无话单记录");
			System.out.println("=========无话单记录==============");
			return webParam;
		}
		if (doc.select("strong.red").text().indexOf("  对不起，您查询失败，失败原因") != -1) {
			System.out.println("=========  对不起，您查询失败，失败原因==============");
			webParam.setHtml(html);
			webParam.setErrormessage(" 对不起，您查询失败");

			return webParam;
		}
		List<TelecomQingHaiCallThremResult> result = new ArrayList<>();
		try {
			Elements trEles = doc.select("table.transact_tab").select("tbody").first().select("tr");
			int trnum = 0;
			for (Element trEle : trEles) {
				trnum++;
				if (trnum == 1) {
					continue;
				}

				try {
					TelecomQingHaiCallThremResult telecomQingHaiCallThremResult = new TelecomQingHaiCallThremResult();

					telecomQingHaiCallThremResult.setCallarea(trEle.select("td").get(0).text());

					telecomQingHaiCallThremResult.setCallphone(trEle.select("td").get(1).text());

					telecomQingHaiCallThremResult.setCallareaother(trEle.select("td").get(2).text());

					telecomQingHaiCallThremResult.setCallphoneother(trEle.select("td").get(3).text());

					telecomQingHaiCallThremResult.setDate(trEle.select("td").get(4).text());

					telecomQingHaiCallThremResult.setCalltime(trEle.select("td").get(5).text());

					telecomQingHaiCallThremResult.setCalltype(trEle.select("td").get(6).text());

					telecomQingHaiCallThremResult.setCallcosts(trEle.select("td").get(7).text());

					telecomQingHaiCallThremResult.setType(trEle.select("td").get(8).text());

					result.add(telecomQingHaiCallThremResult);
				} catch (Exception e) {
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

	public static WebParamTelecom<TelecomQingHaiSMSThremResult> SMSThrem_parse(String html) {
		WebParamTelecom<TelecomQingHaiSMSThremResult> webParam = new WebParamTelecom<TelecomQingHaiSMSThremResult>();
		Document doc = Jsoup.parse(html, "utf-8");
		if (doc.select("strong.red").text().indexOf("无话单记录") != -1) {
			webParam.setHtml(html);
			webParam.setErrormessage("无话单记录");
			System.out.println("=========无话单记录==============");
			return webParam;
		}
		if (doc.select("strong.red").text().indexOf("  对不起，您查询失败，失败原因") != -1) {
			System.out.println("=========  对不起，您查询失败，失败原因==============");
			webParam.setHtml(html);
			webParam.setErrormessage(" 对不起，您查询失败");

			return webParam;
		}
		List<TelecomQingHaiSMSThremResult> result = new ArrayList<>();
		try {
			Elements trEles = doc.select("table.transact_tab").select("tbody").first().select("tr");
			int trnum = 0;
			for (Element trEle : trEles) {
				trnum++;
				if (trnum == 1) {
					System.out.println("=========第一个===========");
					continue;
				}

				try {
					TelecomQingHaiSMSThremResult telecomQingHaiSMSThremResult = new TelecomQingHaiSMSThremResult();

					telecomQingHaiSMSThremResult.setCallarea(trEle.select("td").get(0).text());

					telecomQingHaiSMSThremResult.setCallphone(trEle.select("td").get(1).text());

					telecomQingHaiSMSThremResult.setCallareaother(trEle.select("td").get(2).text());

					telecomQingHaiSMSThremResult.setCallphoneother(trEle.select("td").get(3).text());

					telecomQingHaiSMSThremResult.setDate(trEle.select("td").get(4).text());

					telecomQingHaiSMSThremResult.setCalltime(trEle.select("td").get(5).text());

					telecomQingHaiSMSThremResult.setCalltype(trEle.select("td").get(6).text());

					telecomQingHaiSMSThremResult.setCallcosts(trEle.select("td").get(7).text());

					telecomQingHaiSMSThremResult.setType(trEle.select("td").get(8).text());

					result.add(telecomQingHaiSMSThremResult);
				} catch (Exception e) {
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

	public static void main(String[] args) throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://login.189.cn/login";
		HtmlPage html = getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
		username.setText("13389757201");
		passwordInput.setText("270822");

		HtmlPage htmlpage2 = button.click();

		if (htmlpage2.asXml().indexOf("登录失败") != -1) {
			System.out.println("=======失败==============");
		}

		url = "http://www.189.cn/dqmh/my189/initMy189home.do";
		HtmlPage html3 = getHtml(url, webClient);
		// System.out.println(html3.asXml());

		url = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00900906";
		html3 = getHtml(url, webClient);
		System.out.println("===============" + html3.asXml());

		URL url_url = new URL("http://qh.189.cn/service/account/handledBiz.parser?rnd=0.021569039347768992");
		WebRequest requestSettings = new WebRequest(url_url, HttpMethod.POST);

		Page page = html3.getWebClient().getPage(requestSettings);
		System.out.println("===============" + page.getWebResponse().getContentAsString());

		// System.out.println(page.getWebResponse().getContentAsString());
		/*
		 * Set<Cookie> cookies4 = webClient.getCookieManager().getCookies(); for
		 * (Cookie cookie4 : cookies4) { System.out.println("发送请求的cookie：" +
		 * cookie4.toString()); }
		 */
		System.out.println("===============" + page.getWebResponse().getContentAsString());

		url_url = new URL(
				"http://qh.189.cn/service/account/init.parser?csrftoken=QQ_OPEN_TOKEN&fastcode=00900906&cityCode=qh&SSOURL=http://qh.189.cn/service/account/init.parser?csrftoken=QQ_OPEN_TOKEN&fastcode=00900906&cityCode=qh");
		requestSettings = new WebRequest(url_url, HttpMethod.GET);

		page = html3.getWebClient().getPage(requestSettings);
		System.out.println("===============" + page.getWebResponse().getContentAsString());

		userinfo_parseName(page.getWebResponse().getContentAsString());

	}

}
