/*package app.crawler.htmlparse;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.crawler.bank.json.WebParamBank;
import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaDebitCardTransFlow;
import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaDebitCardUserinfo2;



public class BocParse {

	public static WebParamBank<BocchinaDebitCardUserinfo2> userinfo_parse(String html) {
		WebParamBank<BocchinaDebitCardUserinfo2> webParamBank = new WebParamBank<>();
		List<BocchinaDebitCardUserinfo2> list = new ArrayList<>();
		BocchinaDebitCardUserinfo2 userinfo = new BocchinaDebitCardUserinfo2();
		Document doc = Jsoup.parse(StringEscapeUtils.unescapeHtml(html), "utf-8");

		String name = doc.select("span#welcome-customerName").text();
		System.out.println("用户姓名："+name);
		userinfo.setName(name);
		Elements debitCards = doc.select("span#div_accountnumber_740994").first().select("span");
		
		String debitName = debitCards.select("span").get(1).text();
		System.out.println("借记卡名称："+debitName);
		userinfo.setDebitName(debitName);

		String debitAccount = debitCards.select("span").get(2).text();
		System.out.println("借记卡账户："+debitAccount);
		userinfo.setDebitAccount(debitAccount);
		String debitCity = debitCards.select("span").get(3).text();
		System.out.println("借记卡开户城市："+debitCity);
		userinfo.setDebitCity(debitCity);
		
		String debitState = doc.select("span:contains(" + "账户状态" + ")+div").first().text();
		System.out.println("账户状态："+debitState);
		String debitOpening  = doc.select("span:contains(" + "开户网点" + ")+div").first().text();
		System.out.println("开户网点："+debitOpening);

		String debitDate  = doc.select("span:contains(" + "开户日期" + ")+div").first().text();
		System.out.println("开户日期："+debitDate);
		userinfo.setDebitDate(debitDate);

		String debitArea  = doc.select("span:contains(" + "当前账户所属地区" + ")+div").first().text();
		System.out.println("当前账户所属地区："+debitArea);
		userinfo.setDebitArea(debitArea);
		
		Elements balanceEles = doc.select("table.tb").first().select("tbody").select("tr").select("td");
		
		String  debitCurrency = balanceEles.get(0).text();//币种
		System.out.println("币种："+debitCurrency);
		userinfo.setDebitCurrency(debitCurrency);
		String  debitMoneysinks = balanceEles.get(1).text();//钞/汇
		System.out.println("钞/汇："+debitMoneysinks);
		userinfo.setDebitMoneysinks(debitMoneysinks);
		String  accountBalance = balanceEles.get(2).text();//账户余额
		System.out.println("账户余额："+accountBalance);
		userinfo.setAccountBalance(accountBalance);
		String  usableBalance = balanceEles.get(3).text();//可用余额
		System.out.println("可用余额："+usableBalance);
		userinfo.setUsableBalance(usableBalance);
		Elements elecAccountEles = doc.select("span#div_accountnumber_740994").last().children();
		
		String elecName = elecAccountEles.select("span").get(0).text();//电子现金账户名称
		System.out.println("电子现金账户名称："+elecName);
		userinfo.setElecName(elecName);
		String elecAccount = elecAccountEles.select("span").get(1).text();//电子现金账户
		System.out.println("电子现金账户："+elecAccount);
		userinfo.setElecAccount(elecAccount);
		String elecCity = elecAccountEles.select("span").get(2).text();//电子现金账户城市
		System.out.println("电子现金账户城市："+elecCity);
		userinfo.setElecCity(elecCity);
		String elecState = doc.select("span:contains(" + "账户状态" + ")+div").last().text();//电子现金账户状态
		System.out.println("电子现金账户状态："+elecState);
		userinfo.setElecState(elecState);
		
        Elements elecEles = doc.select("table.tb").last().select("tbody").select("tr").select("td");
		
        String  elecUpper = elecEles.get(0).text();//电子现金卡片余额上限
        System.out.println("电子现金卡片余额上限："+elecUpper);
        userinfo.setElecUpper(elecUpper);
		String  elecTransaction  = elecEles.get(1).text();//电子现金脱机消费单笔交易限额
        System.out.println("电子现金脱机消费单笔交易限额："+elecTransaction);
        userinfo.setElecTransaction(elecTransaction);
		String  elecCurrency = elecEles.get(2).text();//币种
		System.out.println("电子现金币种："+elecCurrency);
		userinfo.setElecCurrency(elecCurrency);
		String  elecMoneysinks = elecEles.get(3).text();//钞/汇
		System.out.println("电子现金钞/汇："+elecMoneysinks);
		userinfo.setElecMoneysinks(elecMoneysinks);
		String  elecFillalance = elecEles.get(3).text();//补登余额
		System.out.println("电子现金补登余额："+elecFillalance);
		userinfo.setElecFillalance(elecFillalance);
		
		list.add(userinfo);
		webParamBank.setList(list);
		return webParamBank;
	}

	public static WebParamBank<BocchinaDebitCardTransFlow> pay_parse(String html) {

		//List<HousingBeiJingPay> listresult = new ArrayList<>();
		WebParamBank<BocchinaDebitCardTransFlow> webParamBank = new WebParamBank<>();
		Document doc = Jsoup.parse(StringEscapeUtils.unescapeHtml(html), "utf-8");
		List<BocchinaDebitCardTransFlow> list = new ArrayList<>();
		Elements treles = doc.select("div#debitCardTransDetail_table").select("tbody").select("tr");
		for(Element trele : treles){
			BocchinaDebitCardTransFlow bocchinaDebitCardTransFlow = new BocchinaDebitCardTransFlow();
			String transflowDate = trele.select("td").get(0).text().trim();//交易日期
			String transflowType = trele.select("td").get(1).text().trim();//业务摘要
			String transflowOthername = trele.select("td").get(2).text().trim();//对方姓名
			String transflowOtheraccount = trele.select("td").get(3).text().trim();//对方账户
			String transflowCurrency = trele.select("td").get(4).text().trim();//币种
			String transflowMoneysinks = trele.select("td").get(5).text().trim();//钞汇
			String transflowIncome = trele.select("td").get(6).text().trim();//收入金额
			String transflowPaid= trele.select("td").get(7).text().trim();//支出金额
			String transflowBalance= trele.select("td").get(8).text().trim();//余额
			String transflowChannel= trele.select("td").get(9).text().trim();//交易渠道
			String transflowPostscript= trele.select("td").get(10).text().trim();//附言
			
			bocchinaDebitCardTransFlow.setTransflowDate(transflowDate);
			bocchinaDebitCardTransFlow.setTransflowType(transflowType);
			bocchinaDebitCardTransFlow.setTransflowOthername(transflowOthername);
			bocchinaDebitCardTransFlow.setTransflowOtheraccount(transflowOtheraccount);
			bocchinaDebitCardTransFlow.setTransflowCurrency(transflowCurrency);
			bocchinaDebitCardTransFlow.setTransflowMoneysinks(transflowMoneysinks);
			bocchinaDebitCardTransFlow.setTransflowIncome(transflowIncome);
			bocchinaDebitCardTransFlow.setTransflowPaid(transflowPaid);
			bocchinaDebitCardTransFlow.setTransflowBalance(transflowBalance);
			bocchinaDebitCardTransFlow.setTransflowChannel(transflowChannel);
			bocchinaDebitCardTransFlow.setTransflowPostscript(transflowPostscript);
			System.out.println("============"+bocchinaDebitCardTransFlow.toString());
			list.add(bocchinaDebitCardTransFlow);
		}
		
		return webParamBank;
	}

}
*/