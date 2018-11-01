package app.parser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.bank.bocom.creditcard.BocomCreditcardBalance;
import com.microservice.dao.entity.crawler.bank.bocom.creditcard.BocomCreditcardBillNow;
import com.microservice.dao.entity.crawler.bank.bocom.creditcard.BocomCreditcardTransFlow;

import app.bean.BalanceResult;
import app.bean.CodeMsgBean;
import app.bean.JsonRootBeanBocom;

/**
 * 
 * 项目名称：common-microservice-bank-bocom 类名称：BocomCreditcardParse 类描述： 创建人：hyx
 * 创建时间：2017年11月17日 上午11:17:43
 * 
 * @version
 */
public class BocomCreditcardParse {

	private static Gson gs = new Gson();

	public static void main(String[] args) throws Exception {

		// balance_parse(null);

		String html = "{\"user\" :{\"userName\" : \"王孟广\",\"nickName\" :"
				+ " \"\",\"mobile\" : \"186****7920\",\"email\" : \"\"}"
				+ ",\"modelNickName\":\"\",\"isCRS\" : \"\",\"wechat\" :"
				+ "[{\"contactType\" : \"WECHAT\",\"contactName\" : \"行者\",\"verified\" : \"true\",\"isAutoBind\" : \"\"}],"
				+ "\"qq\" :[]," + "\"passwdStatus\" : \"\",\"modelCard\" : \"5218 **** **** 8975\",\"error\" : "
				+ "\"\",\"hasCard\":\"true\"}";
		userinfo_parse(html);
	}

	public static BalanceResult balance_parse(String html) {
		BalanceResult balanceResult = new BalanceResult();
		Document doc = Jsoup.parse(html);

		Element tableCurrentAccount = doc.select("table.bill-list").first();
		BocomCreditcardBalance bocomCreditcardBalance = new BocomCreditcardBalance();

		balanceResult.setBocomCreditcardBalance(bocomCreditcardBalance);

		try {
			String payDate = tableCurrentAccount.select("tr:contains(到期还款日)").select("td").text();

			bocomCreditcardBalance.setPayDate(payDate);
		} catch (Exception e) {

		}

		try {
			String currentRmb = tableCurrentAccount.select("tr:contains(本期应还款额)").select("td").first().text();
			bocomCreditcardBalance.setCurrentRmb(currentRmb);
		} catch (Exception e) {

		}

		try {
			String currentDollar = tableCurrentAccount.select("tr:contains(本期应还款额)").select("td").last().text();
			bocomCreditcardBalance.setCurrentDollar(currentDollar);

		} catch (Exception e) {

		}

		try {
			String minimalPayRmb = tableCurrentAccount.select("tr:contains(最低还款额)").select("td").first().text();
			bocomCreditcardBalance.setMinimalPayRmb(minimalPayRmb);
		} catch (Exception e) {

		}

		try {
			String minimalPayDollar = tableCurrentAccount.select("tr:contains(最低还款额)").select("td").last().text();
			bocomCreditcardBalance.setMinimalPayDollar(minimalPayDollar);
		} catch (Exception e) {

		}

		try {
			String creditRmb = tableCurrentAccount.select("tr:contains(信用额度)").select("td").first().text();
			bocomCreditcardBalance.setCreditRmb(creditRmb);
		} catch (Exception e) {

		}

		try {
			String creditDollar = tableCurrentAccount.select("tr:contains(信用额度)").select("td").last().text();
			bocomCreditcardBalance.setCreditDollar(creditDollar);
		} catch (Exception e) {

		}
		try {
			String amountRmb = tableCurrentAccount.select("tr:contains(取现额度)").select("td").first().text();
			bocomCreditcardBalance.setAmountRmb(amountRmb);
		} catch (Exception e) {

		}

		try {
			String amountDollar = tableCurrentAccount.select("tr:contains(取现额度)").select("td").last().text();
			bocomCreditcardBalance.setAmountDollar(amountDollar);
		} catch (Exception e) {

		}

		System.out.println("bocomCreditcardBalance==" + bocomCreditcardBalance.toString());
		Elements ddlist = doc.select("div#bill-1").select("dd");

		List<BocomCreditcardTransFlow> list = new ArrayList<>();
		for (Element dd : ddlist) {
			BocomCreditcardTransFlow bocomCreditcardTransFlow = new BocomCreditcardTransFlow();
			String tradeDate = dd.select("span").get(0).text();
			String accountDate = dd.select("span").get(1).text();
			String explain = dd.select("span").get(2).text();
			String tradeCurrencyAmount = dd.select("span").get(3).text();
			String clearingCurrencyAmount = dd.select("span").get(4).text();
			bocomCreditcardTransFlow.setTradeDate(tradeDate);
			bocomCreditcardTransFlow.setAccountDate(accountDate);
			bocomCreditcardTransFlow.setExplain(explain);
			bocomCreditcardTransFlow.setTradeCurrencyAmount(tradeCurrencyAmount);
			bocomCreditcardTransFlow.setClearingCurrencyAmount(clearingCurrencyAmount);
			System.out.println("bocomCreditcardTransFlow==" + bocomCreditcardTransFlow.toString());
			list.add(bocomCreditcardTransFlow);
		}

		balanceResult.setList(list);
		return balanceResult;
	}

	public static BocomCreditcardBillNow billnow_parse(String html) {
		Type type = new TypeToken<BocomCreditcardBillNow>() {
		}.getType();
		BocomCreditcardBillNow jsonObject = gs.fromJson(html, type);

		System.out.println(jsonObject.toString());

		return jsonObject;

	}

	public static BocomCreditcardBillNow billnow_parse2(String html, BocomCreditcardBillNow bocomCreditcardBillNow) {

		Document doc = Jsoup.parse(html);

		try {
			String available_credit_line_ren = doc.select("li").first().ownText().replaceAll("￥", "")
					.replaceAll("￥", "＄").split(",")[0];
			bocomCreditcardBillNow.setAvailable_credit_line_ren(available_credit_line_ren);
		} catch (Exception e) {
			e.printStackTrace();

		}

		try {
			String available_credit_line_mei = doc.select("li").first().ownText().replaceAll("￥", "")
					.replaceAll("￥", "＄").split(",")[1];
			bocomCreditcardBillNow.setAvailable_credit_line_mei(available_credit_line_mei);

		} catch (Exception e) {
			e.printStackTrace();

		}

		try {
			String available_withdrawal_limits_ren = doc.select("li").last().ownText().replaceAll("￥", "")
					.replaceAll("￥", "＄").split(",")[0];

			bocomCreditcardBillNow.setAvailable_withdrawal_limits_ren(available_withdrawal_limits_ren);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			String available_withdrawal_limits_mei = doc.select("li").last().ownText().replaceAll("￥", "")
					.replaceAll("￥", "＄").split(",")[1];
			bocomCreditcardBillNow.setAvailable_withdrawal_limits_mei(available_withdrawal_limits_mei);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bocomCreditcardBillNow;
	}

	public static JsonRootBeanBocom userinfo_parse(String html) {
		// JsonRootBean<Userinfo> resultroot = new JsonRootBean<Userinfo>();
		Type type = new TypeToken<JsonRootBeanBocom>() {
		}.getType();
		JsonRootBeanBocom jsonObject = gs.fromJson(html, type);

		System.out.println(jsonObject.toString());

		return jsonObject;
	}

	public static CodeMsgBean codeMsg_parse(String html) {
		// JsonRootBean<Userinfo> resultroot = new JsonRootBean<Userinfo>();
		Type type = new TypeToken<CodeMsgBean>() {
		}.getType();
		CodeMsgBean jsonObject = gs.fromJson(html, type);

		System.out.println(jsonObject.toString());

		return jsonObject;
	}

	public static String getCardnum(String html) {
		Document doc = Jsoup.parse(html);
		String cardnum = doc.select("div.title").first().text().trim();
		return cardnum;
	}

}
