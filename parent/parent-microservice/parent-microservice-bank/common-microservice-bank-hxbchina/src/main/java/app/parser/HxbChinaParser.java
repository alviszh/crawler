package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.hxbchina.HxbChinaCreditCardTransFlow;
import com.microservice.dao.entity.crawler.bank.hxbchina.HxbChinaCreditCardUserInfo;
import com.microservice.dao.entity.crawler.bank.hxbchina.HxbChinaDebitCardTransFlow;
import com.microservice.dao.entity.crawler.bank.hxbchina.HxbChinaDebitCardUserInfo;

/**
 * @description:
 * @author: sln
 * @date: 2017年11月06日
 */
@Component
public class HxbChinaParser {
	// 解析用户信息
	public HxbChinaDebitCardUserInfo userParser(String html, TaskBank taskBank) {
		Document doc = Jsoup.parse(html);
		HxbChinaDebitCardUserInfo hxbChinaDebitCardUserInfo = new HxbChinaDebitCardUserInfo();
		hxbChinaDebitCardUserInfo.setAccOtherName(doc.select("td:contains(账户别名)+td").first().text());
		hxbChinaDebitCardUserInfo.setAccoutNo(doc.select("td:contains(账号)+td").first().text());
		hxbChinaDebitCardUserInfo.setAvailableBal(doc.select("td:contains(可用余额)+td").first().text());
		// hxbChinaDebitCardUserInfo.setBalance(doc.select("td:contains(余额)+td").first().text());
		// //这样获取的是余额时间的值
		hxbChinaDebitCardUserInfo.setBalance(doc.getElementsByClass("inputStyleTable").get(0).getElementsByTag("tbody")
				.get(0).getElementsByTag("tr").get(6).getElementsByTag("td").get(1).text());
		hxbChinaDebitCardUserInfo.setBalTime(doc.select("td:contains(余额时间)+td").first().text());
		hxbChinaDebitCardUserInfo.setBelongCard(doc.select("td:contains(所属卡/存折/主账号)+td").first().text());
		hxbChinaDebitCardUserInfo.setCurrency(doc.select("td:contains(币种)+td").first().text());
		hxbChinaDebitCardUserInfo.setCustName(doc.select("td:contains(客户姓名)+td").first().text());
		hxbChinaDebitCardUserInfo.setOpenDate(doc.select("td:contains(开户日期)+td").first().text());
		hxbChinaDebitCardUserInfo.setOpenOrganization(doc.select("td:contains(开户机构)+td").first().text());
		hxbChinaDebitCardUserInfo.setRate(doc.select("td:contains(利率)+td").first().text());
		hxbChinaDebitCardUserInfo.setState(doc.select("td:contains(状态)+td").first().text());
		hxbChinaDebitCardUserInfo.setStoreType(doc.select("td:contains(储种)+td").first().text());
		hxbChinaDebitCardUserInfo.setTaskid(taskBank.getTaskid().trim());
		return hxbChinaDebitCardUserInfo;
	}

	public List<HxbChinaDebitCardTransFlow> transflowParser(TaskBank taskBank, Document doc, String monthAgoDate,
			String monthLaterDate, String cardno) {
		List<HxbChinaDebitCardTransFlow> list = new ArrayList<HxbChinaDebitCardTransFlow>();
		HxbChinaDebitCardTransFlow hxbChinaDebitCardTransFlow = null;
		Elements trs = doc.getElementById("paccountQueryAccountDetailsList_row").getElementsByTag("tbody").get(0)
				.getElementsByTag("tr");
		for (Element element : trs) {
			hxbChinaDebitCardTransFlow = new HxbChinaDebitCardTransFlow();
			hxbChinaDebitCardTransFlow.setAcctBal(element.getElementsByTag("td").get(6).text());
			hxbChinaDebitCardTransFlow.setCurrency(element.getElementsByTag("td").get(3).text());
			hxbChinaDebitCardTransFlow.setDisPlayNo(element.getElementsByTag("td").get(0).text());
			hxbChinaDebitCardTransFlow.setNote(element.getElementsByTag("td").get(11).text());
			hxbChinaDebitCardTransFlow.setOtherAcctNo(element.getElementsByTag("td").get(7).text());
			hxbChinaDebitCardTransFlow.setOtherName(element.getElementsByTag("td").get(8).text());
			hxbChinaDebitCardTransFlow.setOtherOpenBankName(element.getElementsByTag("td").get(9).text());
			hxbChinaDebitCardTransFlow.setPrintHDFlag(element.getElementsByTag("td").get(12).text());
			hxbChinaDebitCardTransFlow.setQryDateRange(monthAgoDate + "-" + monthLaterDate);
			hxbChinaDebitCardTransFlow.setSummary(element.getElementsByTag("td").get(10).text());
			hxbChinaDebitCardTransFlow.setTaskid(taskBank.getTaskid().trim());
			hxbChinaDebitCardTransFlow.setTransAmtIncome(element.getElementsByTag("td").get(4).text());
			hxbChinaDebitCardTransFlow.setTransAmtPayOut(element.getElementsByTag("td").get(5).text());
			hxbChinaDebitCardTransFlow.setTransChannel(element.getElementsByTag("td").get(2).text());
			hxbChinaDebitCardTransFlow.setTransDate(element.getElementsByTag("td").get(1).text());
			hxbChinaDebitCardTransFlow.setCardno(cardno);
			list.add(hxbChinaDebitCardTransFlow);
		}
		return list;
	}

	// 信用卡用户基本信息解析
	public HxbChinaCreditCardUserInfo userCreditParser(String html, TaskBank taskBank) {
		Document doc = Jsoup.parse(html);
		HxbChinaCreditCardUserInfo hxbChinaCreditCardUserInfo = new HxbChinaCreditCardUserInfo();
		hxbChinaCreditCardUserInfo.setAccOtherName(doc.select("td:contains(账户别名)+td").first().text());
		hxbChinaCreditCardUserInfo.setAvailableLimit(doc.select("td:contains(可用额度)+td").first().text());
		hxbChinaCreditCardUserInfo.setCardConvexWordName(doc.select("td:contains(卡片凸字姓名)+td").first().text());
		hxbChinaCreditCardUserInfo.setCardNo(doc.select("td:contains(信用卡卡号)+td").first().text());
		hxbChinaCreditCardUserInfo.setCardOwnerName(doc.select("td:contains(持卡人姓名)+td").first().text());
		hxbChinaCreditCardUserInfo.setCardType(doc.select("td:contains(卡片种类描述)+td").first().text());
		hxbChinaCreditCardUserInfo.setCardValidDate(doc.select("td:contains(卡片有效期)+td").first().text());
		hxbChinaCreditCardUserInfo.setCreditLimit(doc.select("td:contains(信用额度)+td").first().text());
		hxbChinaCreditCardUserInfo.setMainFlag(doc.select("td:contains(主附卡标志)+td").first().text());
		hxbChinaCreditCardUserInfo.setTaskid(taskBank.getTaskid());
		hxbChinaCreditCardUserInfo.setUsePwdFlag(doc.select("td:contains(消费是否使用密码)+td").first().text());
		return hxbChinaCreditCardUserInfo;
	}

	// 信用卡账单信息解析
	public List<HxbChinaCreditCardTransFlow> transflowCreditParser(TaskBank taskBank, Document doc,
			String monthAgoDate) {
		List<HxbChinaCreditCardTransFlow> list = new ArrayList<HxbChinaCreditCardTransFlow>();
		HxbChinaCreditCardTransFlow hxbChinaCreditCardTransFlow = null;
		Elements trs = doc.getElementById("paccountQueryCreditCardDetailsList_row").getElementsByTag("tbody").get(0)
				.getElementsByTag("tr");
		for (Element element : trs) {
			hxbChinaCreditCardTransFlow = new HxbChinaCreditCardTransFlow();
			hxbChinaCreditCardTransFlow.setCurrency(element.getElementsByTag("td").get(3).text());
			hxbChinaCreditCardTransFlow.setQryDateRange(monthAgoDate);
			hxbChinaCreditCardTransFlow.setTaskid(taskBank.getTaskid().trim());
			hxbChinaCreditCardTransFlow.setAccountDate(element.getElementsByTag("td").get(2).text());
			hxbChinaCreditCardTransFlow.setCardEnd(element.getElementsByTag("td").get(6).text());
			hxbChinaCreditCardTransFlow.setRemark(element.getElementsByTag("td").get(4).text());
			hxbChinaCreditCardTransFlow.setSortNum(element.getElementsByTag("td").get(0).text());
			hxbChinaCreditCardTransFlow.setTransAmount(element.getElementsByTag("td").get(5).text());
			hxbChinaCreditCardTransFlow.setTransDate(element.getElementsByTag("td").get(1).text());
			list.add(hxbChinaCreditCardTransFlow);
		}
		return list;
	}

}
