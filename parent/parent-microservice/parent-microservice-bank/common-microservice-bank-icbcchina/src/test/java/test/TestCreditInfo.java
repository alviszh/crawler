package test;

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
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class TestCreditInfo {

	public static void main(String[] args) throws Exception {
//		getinfo("CLDZGIFVHSDEAGCMAIHQIAAHHTAEHIGLEJBSAQIB");
		gettrans("CLDZGIFVHSDEAGCMAIHQIAAHHTAEHIGLEJBSAQIB");
//		getPayStages("EDIMHEBTEQAAITGHHXFHIYESILJMHDBOBYCOBSGS");
	}
	
	public static void getinfo(String sessionId) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://mybank.icbc.com.cn/servlet/ICBCINBSReqServlet?dse_operationName=per_CardMyCreditCardOp&CardNum=4135190018996339&doFlag=1&cardType=007&cardFlag=0&dse_sessionId="+sessionId;
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		String cardnum = doc.getElementsByClass("cardNumberDivide").first().text();
		Elements es = doc.select("nobr:contains(账单日)");
		String billDay = es.first().text();
		Elements es2 = doc.select("nobr:contains(还款日 :)");
		String repayDay = es2.first().text();
		/*Elements es3 = doc.select("td:contains(有效期)");
		String endDay = es3.first().text();*/
		Elements es4 = doc.select("div:contains(人民币)");
		String balance = es4.first().text();
		System.out.println("--------------->"+cardnum);
		System.out.println("--------------->"+billDay.substring(billDay.indexOf(":")+1).trim());
		System.out.println("--------------->"+repayDay.substring(repayDay.indexOf(":")+1).trim());
//		System.out.println("--------------->"+endDay);
		System.out.println("--------------->"+balance.substring(balance.indexOf("：")+1).trim());
	}
	
	public static void gettrans(String sessionId) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url1 = "https://mybank.icbc.com.cn/servlet/ICBCINBSReqServlet?dse_sessionId="+sessionId+"&dse_operationName=per_CardMyRepayOp&doFlag=1";
		String url2 = "https://mybank.icbc.com.cn/servlet/ICBCINBSReqServlet?dse_sessionId="+sessionId+"&dse_operationName=per_AccountQueryCheckbillListOp&cardNo=6229100019646447&cardType=007";
		
		String url = "https://mybank.icbc.com.cn/servlet/ICBCINBSReqServlet?dse_sessionId="+sessionId+"&dse_operationName=per_AccountQueryCheckbillOp&cardNo=6229100019646447&acctIndex=0&Tran_flag=0&Sel_flag=0&newOldFlag=0&queryType=4&cardNum1=6229100019646447&interCurrType=&changeFlag=0&WORKMON=201710&currtypeR=001&currtypeF=001&cardType=007&dcrFlag=5&currFlag=1";
//		&dse_applicationId=-1&acctIndex=0&Sel_flag=0&newOldFlag=0&dcrFlag=5&currFlag=1
//		String url = "https://mybank.icbc.com.cn/servlet/ICBCINBSReqServlet?dse_sessionId="+sessionId+"&dse_operationName=per_AccountQueryCheckbillOp&cardNo=6229100019646447&Tran_flag=0&queryType=4&changeFlag=0&WORKMON=201710&currtypeR=001&currtypeF=001&cardType=007";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
//		webRequest.setAdditionalHeader("Host", "mybank.icbc.com.cn");
//		webRequest.setAdditionalHeader("Connection", "Keep-Alive");
//		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest.setAdditionalHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E; InfoPath.3)");
		webClient.getPage(url1);
		webClient.getPage(url2);
		Page page = webClient.getPage(webRequest);
		
		System.out.println("-789789789");
		System.out.println(page.getWebResponse().getContentAsString());
		System.out.println("789789789");
		Document document = Jsoup.parse(page.getWebResponse().getContentAsString());
		Elements bs = document.select("b:contains(账单周期)");
		if(null != bs && bs.size() > 0){
			String billstr = bs.get(0).text();
			System.out.println("账单周期："+billstr.substring(billstr.indexOf("2")));
		}
		Elements bs2 = document.select("b:contains(对账单生成日)");
		if(null != bs2 && bs2.size() > 0){
			String text = bs2.get(0).text();
			String substring = text.substring(text.indexOf("2"));
			System.out.println("对账单生成日："+substring);
		}
		Elements bs3 = document.select("b:contains(贷记卡到期还款日)");
		if(null != bs3 && bs3.size() > 0){
			String text = bs3.get(0).parent().parent().text();
			System.out.println("贷记卡到期还款日："+text.substring(text.indexOf("2")));
		}
		//账单信息
		Elements trss = document.select("div:contains(卡号后四位)");
		if(null != trss && trss.size() > 0){
			System.out.println("有账单"+trss.toString());
			Element trr = trss.first();
			Element tr = trr.parent().parent();
			Element nextElement = tr.nextElementSibling();
			if(null != nextElement){
				System.out.println("billData---》");
				Elements tds = nextElement.children();
				for (Element td : tds) {
					System.out.println("-->"+td.text());
				}
			}
		}
		//积分信息 
		Elements trss1 = document.select("b:contains(个人综合积分)");
		if(null != trss1 && trss1.size() > 0){
			System.out.println("有积分"+trss1.toString());
			Element trr = trss1.first();
			Element tr = trr.parent().parent();
			Element nextElement = tr.nextElementSibling();
			if(null != nextElement){
				String jifen = nextElement.text();
				System.out.println("jifen---》"+jifen.substring(jifen.indexOf(" ")+1).trim());
			}
		}
		webRequest = new WebRequest(new URL("https://mybank.icbc.com.cn/icbc/newperbank/account/account_query_checkbill_loan_detail_index.jsp?dse_sessionId="+sessionId+"&cardNo=6229100019646447"), HttpMethod.GET);
		HtmlPage page2 = webClient.getPage(webRequest);
		System.out.println("-456654456654");
//		System.out.println(page2.getWebResponse().getContentAsString());
		System.out.println("456654456654");
		List<String> strs = new ArrayList<>();
		gettrans2(page2, strs);
		System.out.println("asdasd->"+strs.toString());
	}
	
	public static void gettrans2(HtmlPage page, List<String> strs) throws Exception{
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		Element byId = doc.getElementById("主交易区");
		Elements trs = byId.select("[bgcolor=white]");
//		System.out.println(trs.toString());
		System.out.println("*+-+-+--+-+--++--++-+-*");
		System.out.println("*///////*"+trs.size());
		for (Element tr : trs) {
			Elements tds = tr.select("td");
			if(tds.size() > 1){
				System.out.println("*--------------------------------------*");
				String txt = "";
				for (Element td : tds) {
					System.out.println("--->"+td.text());
					txt += td.text()+"-";
				}
				strs.add(txt);
			}
		}
		DomNodeList<DomElement> as = page.getElementsByTagName("a");
		for (DomElement a : as) {
			if(a.getTextContent().contains("下一页")){
				HtmlPage click = a.click();
				gettrans2(click, strs);
			}
		}
		/*HtmlSelect select = (HtmlSelect) page.getElementsByTagName("a");
		HtmlOption option = select.getOptionByText("【下一页】");
		if(null != option){
			HtmlPage click = option.click();
			gettrans2(click);
		}*/
		 
		/*DomNodeList<DomNode> doms = page.querySelectorAll("a:contains(下一页)");
		if(null != doms && doms.size() > 0){
			
		}
		Elements es = doc.select("a:contains(下一页)");*/
		
	}
	
	public static void getPayStages(String sessionId) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://mybank.icbc.com.cn/servlet/ICBCINBSReqServlet?dse_sessionId="+sessionId+"&dse_operationName=per_CardDebtPayOp&CardNum=6229100019646447&doFlag=1&subPage=1&acctSelList=5&currType=001&partPayType=2&STARTDATE=2012-12-07&OVERDATE=2017-12-06";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		System.out.println("*--------------*");
		System.out.println(page.getWebResponse().getContentAsString());
		System.out.println("1--------------1");
		if(page.getWebResponse().getContentAsString().contains("无符合条件的记录")){
			System.out.println("无记录");
		}else{
			System.out.println("有记录");
		}
		
	}
}
