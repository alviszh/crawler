package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.bank.cebchina.CebChinaCreditCardBilling;
import com.microservice.dao.entity.crawler.bank.cebchina.CebChinaCreditCardConsumption;
import com.microservice.dao.entity.crawler.bank.cebchina.CebChinaCreditCardConsumptionMonth;
import com.microservice.dao.entity.crawler.bank.cebchina.CebChinaCreditCardUserInfo;

import app.commontracerlog.TracerLog;

@Component
public class CebChinaParser {

	@Autowired 
	private TracerLog tracerLog;

	/*public CebChinaCreditCardUserInfo getUserInfo(String html, String html2, String html3, String html4, String html5, String html6, String html7) {
		CebChinaCreditCardUserInfo cebChinaCreditCardUserInfo =new CebChinaCreditCardUserInfo();
		try {
			//信用卡信息
			Document doc = Jsoup.parse(html);
			Element class1 = doc.getElementsByClass("tab99").get(0);
			cebChinaCreditCardUserInfo.setNum(class1.getElementsByTag("td").get(0).text());//卡号
			cebChinaCreditCardUserInfo.setNumname(class1.getElementsByTag("td").get(1).text());//卡名称
			cebChinaCreditCardUserInfo.setCurrency(class1.getElementsByTag("td").get(2).text());//币种

			//信用卡额度
			Document doc2 = Jsoup.parse(html2);
			String text = doc2.getElementsByTag("body").text();
			JSONObject obj = JSONObject.fromObject(text);
			cebChinaCreditCardUserInfo.setLine_of_credit(obj.getString("limit"));

			//还款详情
			Document doc3 = Jsoup.parse(html3);
			String text3 = doc3.getElementsByTag("body").text();
			JSONObject obj2 = JSONObject.fromObject(text3);
			cebChinaCreditCardUserInfo.setAccountant_bill_date(obj2.getString("statementDate"));//账单日
			cebChinaCreditCardUserInfo.setRmb_money(obj2.getString("RMBStatementBalance"));//RMB
			cebChinaCreditCardUserInfo.setDollar_money(obj2.getString("USDStatementBalance"));//US
			String eur = obj2.getString("EURStatementBalance");//EUR
			if(eur.length()<1){
				cebChinaCreditCardUserInfo.setEuro_money("0.00");
			}else{
				cebChinaCreditCardUserInfo.setEuro_money(obj2.getString("EURStatementBalance"));
			}
			cebChinaCreditCardUserInfo.setRepayment_date(obj2.getString("paymentDueDay"));//还款日

			//积分
			Document doc4 = Jsoup.parse(html4);
			Element element = doc4.getElementsByClass("tab_one").get(0);
			Element element2 = element.getElementsByTag("tr").get(1);
			cebChinaCreditCardUserInfo.setIntegral(element2.getElementsByTag("td").get(0).text());//积分
			cebChinaCreditCardUserInfo.setEndintegral(element2.getElementsByTag("td").get(1).text());//即将过期的积分

			//当月欠款金额
			String[] split = html5.split(":");
			String s = split[split.length-1];
			String[] split2 = s.split("<");
			cebChinaCreditCardUserInfo.setDept(split2[0]);

			//剩余额度
			String[] split6 =html6.split(":");
			String s2 = split6[split6.length-1];
			String[] split3 = s2.split("<");
			cebChinaCreditCardUserInfo.setEnd_of_credit(split3[0]);//剩余额度

			//卡所有人姓名
			Document doc7 = Jsoup.parse(html7);
			Element element7 = doc7.getElementById("logined");
			cebChinaCreditCardUserInfo.setName(element7.getElementsByTag("strong").text());

			return cebChinaCreditCardUserInfo;

		} catch (Exception e) {
			tracerLog.output("信用卡信息解析错误：", e.getMessage());
			return cebChinaCreditCardUserInfo;
		}
	}
*/


	public List<CebChinaCreditCardConsumptionMonth> getgetConsumptionMonth(String html, String taskid) {
		List<CebChinaCreditCardConsumptionMonth> list = new ArrayList<>();
		try {
			Document doc = Jsoup.parse(html);
			Element element = doc.getElementsByClass("tab_one1").get(0);
			Elements elementsByTag = element.getElementsByTag("tr");
			if(elementsByTag.size()>1){
				CebChinaCreditCardConsumptionMonth cebChinaCreditCardConsumptionMonth = null;
				for(int i=0;i<elementsByTag.size();i++){

					String text1 = elementsByTag.get(i+1).getElementsByTag("td").get(0).text();
					String text2 = elementsByTag.get(i+1).getElementsByTag("td").get(1).text();
					String text3 = elementsByTag.get(i+1).getElementsByTag("td").get(2).text();
					String text4 = elementsByTag.get(i+1).getElementsByTag("td").get(3).text();
					String text5 = elementsByTag.get(i+1).getElementsByTag("td").get(4).text();
					cebChinaCreditCardConsumptionMonth = new CebChinaCreditCardConsumptionMonth(
							taskid,text1,text2,text3,text4,text5);
					list.add(cebChinaCreditCardConsumptionMonth);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			tracerLog.output("信用卡信息解析错误：", e.getMessage());
		}
		return list;
	}

	public List<CebChinaCreditCardConsumption> getConsumption(String taskid, String html, String html2) {
		List<CebChinaCreditCardConsumption> list = new ArrayList<>();
		try {
			Document doc1 = Jsoup.parse(html2);
			Element class1 = doc1.getElementsByClass("tab99").get(0);
			String num = class1.getElementsByTag("td").get(0).text();//卡号

			Document doc = Jsoup.parse(html);
			Elements byTag = doc.getElementsByClass("tab_one").get(0).getElementsByTag("tr");
			CebChinaCreditCardConsumption cebChinaCreditCardConsumption =null;
			for(int i=0;i<byTag.size();i++){

				String a1 = byTag.get(i+1).getElementsByTag("td").get(0).text();


				String a2 = byTag.get(i+1).getElementsByTag("td").get(1).text();


				String a3 = byTag.get(i+1).getElementsByTag("td").get(2).text();


				String a4 = byTag.get(i+1).getElementsByTag("td").get(3).text();


				String a5 = byTag.get(i+1).getElementsByTag("td").get(4).text();
				cebChinaCreditCardConsumption = new CebChinaCreditCardConsumption(
						taskid, a1, a2, a3, a4, a5,num);
				list.add(cebChinaCreditCardConsumption);
			}

		} catch (Exception e) {
			tracerLog.output("信用卡信息解析错误：", e.getMessage());
		}

		return list;
	}



	public List<CebChinaCreditCardBilling> getbilling(String html, String month1, String taskid) {
		List<CebChinaCreditCardBilling> list = null;
		try {
			Document doc = Jsoup.parse(html);
			Elements elementsByTag = doc.getElementsByTag("table");
			Element element = elementsByTag.get(7);
			list = new ArrayList<CebChinaCreditCardBilling>();
			Elements elementsByTag2 = element.getElementsByTag("tr");
			for(int i = 0;i<elementsByTag2.size();i++){
				CebChinaCreditCardBilling cebChinaCreditCardBilling = new CebChinaCreditCardBilling();
				if(i==0){
					String a = elementsByTag2.get(0).getElementsByTag("span").get(0).text();//kahao
					String a2 = elementsByTag2.get(0).getElementsByTag("span").get(1).text();//Gale
					String a3 = elementsByTag2.get(0).getElementsByTag("span").get(2).text();//上期欠款
					cebChinaCreditCardBilling.setAccount_num(a);
					cebChinaCreditCardBilling.setLei(a2);
					cebChinaCreditCardBilling.setShangdept(a3);
					cebChinaCreditCardBilling.setDatetime(month1);
					cebChinaCreditCardBilling.setTaskid(taskid);
					list.add(cebChinaCreditCardBilling);
				}
				if(i>=2){
					String text = elementsByTag2.get(i).getElementsByTag("td").get(0).text().trim();
					String text2 = elementsByTag2.get(i).getElementsByTag("td").get(1).text().trim();
					String text3 = elementsByTag2.get(i).getElementsByTag("td").get(2).text().trim();
					String text4 = elementsByTag2.get(i).getElementsByTag("td").get(3).text().trim();
					String text5 = elementsByTag2.get(i).getElementsByTag("td").get(4).text().trim();
					cebChinaCreditCardBilling.setTrade_date(text);
					cebChinaCreditCardBilling.setAccounting_date(text2);
					cebChinaCreditCardBilling.setNum(text3);
					cebChinaCreditCardBilling.setState(text4);
					cebChinaCreditCardBilling.setMoney(text5);
					cebChinaCreditCardBilling.setDatetime(month1);
					cebChinaCreditCardBilling.setTaskid(taskid);
					list.add(cebChinaCreditCardBilling);
				}
			}
			return list;
		} catch (Exception e) {
			tracerLog.output("信用卡账单信息解析错误：", e.getMessage());
			return list;
		}
	}



	public List<CebChinaCreditCardUserInfo> getUserInfo1(String html2, String taskid, String html4, String html7, String numname) {
		List<CebChinaCreditCardUserInfo> list = null;
		try {
			
			Document doc7 = Jsoup.parse(html7);
			Element element7 = doc7.getElementById("logined");
			String text17 = element7.getElementsByTag("strong").text();//持卡人姓名
			
			Document doc4 = Jsoup.parse(html4);
			Element element = doc4.getElementsByClass("tab_one").get(0);
			Element element2 = element.getElementsByTag("tr").get(1);
			String text15 = element2.getElementsByTag("td").get(0).text();//积分
			String text16 = element2.getElementsByTag("td").get(1).text();//即将过期的积分
			
			
			Document parse = Jsoup.parse(html2);
			Element elementsByClass = parse.getElementsByClass("tab_one1").get(0);
			Elements elementsByTag = elementsByClass.getElementsByTag("th");
			list = new ArrayList<CebChinaCreditCardUserInfo>();
			if(elementsByTag.size()>2)//除去人民币 包含其他货币额度
			{
				
				for (int i = 1; i < 3; i++) {
				CebChinaCreditCardUserInfo cebChinaCreditCardUserInfo =new CebChinaCreditCardUserInfo();
				System.out.println(111);
				String text = elementsByClass.getElementsByTag("tr").get(0).getElementsByTag("th").get(i).text();//货币名称
				String text1 = elementsByClass.getElementsByTag("tr").get(1).getElementsByTag("td").get(i).text();//卡号
				String text2 = elementsByClass.getElementsByTag("tr").get(2).getElementsByTag("td").get(i).text();//查询时间
				String text3 = elementsByClass.getElementsByTag("tr").get(3).getElementsByTag("td").get(i).text();//信用额度
				String text4 = elementsByClass.getElementsByTag("tr").get(4).getElementsByTag("td").get(i).text();//临时额度
				String text5 = elementsByClass.getElementsByTag("tr").get(5).getElementsByTag("td").get(i).text();//预借现金额度
				String text6 = elementsByClass.getElementsByTag("tr").get(6).getElementsByTag("td").get(i).text();//实际可用额度
				String text7 = elementsByClass.getElementsByTag("tr").get(7).getElementsByTag("td").get(i).text();//当前账户余额
				String text8 = elementsByClass.getElementsByTag("tr").get(8).getElementsByTag("td").get(1).text();//账单日
				String text9 = elementsByClass.getElementsByTag("tr").get(9).getElementsByTag("td").get(1).text();//到期还款日
				String tex = elementsByClass.getElementsByTag("tr").get(10).getElementsByTag("td").get(i).text();//本期账单应还款额
				String[] split = tex.split("账单分期");
				String text10 = split[0];
				String text11 = elementsByClass.getElementsByTag("tr").get(11).getElementsByTag("td").get(i).text();//本期最小还款额
				String text12 = elementsByClass.getElementsByTag("tr").get(12).getElementsByTag("td").get(i).text();//本期未还款金额
				String text13 = elementsByClass.getElementsByTag("tr").get(13).getElementsByTag("td").get(i).text();//当前透支利率（消费类）
				String text14 = elementsByClass.getElementsByTag("tr").get(14).getElementsByTag("td").get(i).text();//当前透支利率（现金类）
				cebChinaCreditCardUserInfo = new CebChinaCreditCardUserInfo(taskid,text,text1,text2,text3,text4,text5,text6,text7,text8,text9,text10,text11,text12,text13,text14,text15,text16,text17,numname);
				list.add(cebChinaCreditCardUserInfo);
				}
				
			}else{
				String text = elementsByClass.getElementsByTag("tr").get(0).getElementsByTag("th").get(1).text();//货币名称
				String text1 = elementsByClass.getElementsByTag("tr").get(1).getElementsByTag("td").get(1).text();//卡号
				String text2 = elementsByClass.getElementsByTag("tr").get(2).getElementsByTag("td").get(1).text();//查询时间
				String text3 = elementsByClass.getElementsByTag("tr").get(3).getElementsByTag("td").get(1).text();//信用额度
				String text4 = elementsByClass.getElementsByTag("tr").get(4).getElementsByTag("td").get(1).text();//临时额度
				String text5 = elementsByClass.getElementsByTag("tr").get(5).getElementsByTag("td").get(1).text();//预借现金额度
				String text6 = elementsByClass.getElementsByTag("tr").get(6).getElementsByTag("td").get(1).text();//实际可用额度
				String text7 = elementsByClass.getElementsByTag("tr").get(7).getElementsByTag("td").get(1).text();//当前账户余额
				String text8 = elementsByClass.getElementsByTag("tr").get(8).getElementsByTag("td").get(1).text();//账单日
				String text9 = elementsByClass.getElementsByTag("tr").get(9).getElementsByTag("td").get(1).text();//到期还款日
				String text10 = elementsByClass.getElementsByTag("tr").get(10).getElementsByTag("td").get(1).text();//本期账单应还款额
				String text11 = elementsByClass.getElementsByTag("tr").get(11).getElementsByTag("td").get(1).text();//本期最小还款额
				String text12 = elementsByClass.getElementsByTag("tr").get(12).getElementsByTag("td").get(1).text();//本期未还款金额
				String text13 = elementsByClass.getElementsByTag("tr").get(13).getElementsByTag("td").get(1).text();//当前透支利率（消费类）
				String text14 = elementsByClass.getElementsByTag("tr").get(14).getElementsByTag("td").get(1).text();//当前透支利率（现金类）
				CebChinaCreditCardUserInfo cebChinaCreditCardUserInfo = new CebChinaCreditCardUserInfo(taskid,text,text1,text2,text3,text4,text5,text6,text7,text8,text9,text10,text11,text12,text13,text14,text15,text16,text17,numname);
				list.add(cebChinaCreditCardUserInfo);
			}
		}catch (Exception e) {
			tracerLog.output("信用卡信息解析错误：", taskid);
			return list;
		}

		return list;
	}

}
