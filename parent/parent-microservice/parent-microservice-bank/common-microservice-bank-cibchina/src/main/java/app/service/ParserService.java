package app.service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.bank.cibchina.CibChinaRegular;
import com.microservice.dao.entity.crawler.bank.cibchina.CibChinaTransFlow;
import com.microservice.dao.entity.crawler.bank.cibchina.CibChinaUserInfo;
import com.microservice.dao.entity.crawler.bank.cibchina.CibCreditcardBill;
import com.microservice.dao.entity.crawler.bank.cibchina.CibCreditcardInstallment;
import com.microservice.dao.entity.crawler.bank.cibchina.CibCreditcardTransFlow;
import com.microservice.dao.entity.crawler.bank.cibchina.CibCreditcardUserInfo;

@Component
public class ParserService {
	//储蓄定期存款
	public List<CibChinaRegular> parserRegular(String html,String taskid){
		List<CibChinaRegular> list = new ArrayList<CibChinaRegular>();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("#form > div > span:nth-child(3)");
		int el = 0;
		for (int j = 0;j<ele.size();j++){
			el=el+2;
			Elements eles = doc.select("#form > table:nth-child("+el+") > tbody > tr");
			String str = ele.get(j).text().trim(); 
			String accNumber = str.substring(str.indexOf("：")+1);
			System.out.println("el:"+el);
			System.out.println("accNumber:"+accNumber);
			if(eles!=null&&eles.size()>0){
				
				for(int i = 0;i<eles.size();i++){
					String kindof = eles.get(i).select("td").eq(1).text().trim();//储种
					String currency = eles.get(i).select("td").eq(2).text().trim();//币种
					String exchange = eles.get(i).select("td").eq(3).text().trim();//钞汇
					String balance = eles.get(i).select("td").eq(4).text().trim();//余额
					String avaBalance = eles.get(i).select("td").eq(5).text().trim();//可用余额
					String opening = eles.get(i).select("td").eq(6).text().trim();//开户日期
					String period = eles.get(i).select("td").eq(7).text().trim();//存期
					String expiraDate = eles.get(i).select("td").eq(8).text().trim();//到期日期
					String continued = eles.get(i).select("td").eq(9).text().trim();//续存存期
					String state = eles.get(i).select("td").eq(10).text().trim();//状态
					if (kindof.contains("人民币定期")){
						CibChinaRegular cibChinaRegular = new CibChinaRegular();
						cibChinaRegular.setKindof(kindof);
						cibChinaRegular.setCurrency(currency);
						cibChinaRegular.setExchange(exchange);
						cibChinaRegular.setBalance(balance);
						cibChinaRegular.setAvaBalance(avaBalance);
						cibChinaRegular.setOpening(opening);
						cibChinaRegular.setPeriod(period);
						cibChinaRegular.setExpiraDate(expiraDate);
						cibChinaRegular.setContinued(continued);
						cibChinaRegular.setState(state);
						cibChinaRegular.setTaskid(taskid);
						cibChinaRegular.setAccNumber(accNumber);
						list.add(cibChinaRegular);
					}
				}
			}
		}
		
		return list;
		
	}
	//储蓄账户信息
	public List<CibChinaUserInfo> parserUser(String html,String taskid,String accNumber1,String opening,String balance){
		List<CibChinaUserInfo> list = null;
		
		Document doc = Jsoup.parse(html);
		Elements eles = doc.select("tr.ui-widget-content");
		if(eles!=null&&eles.size()>0){
			list = new ArrayList<CibChinaUserInfo>();
			for(int i = 0;i<eles.size();i++){
				String alias = eles.get(i).select("td").eq(0).text();//账户别名
				String name = eles.get(i).select("td").eq(1).text();//账户户名
				String accNumber = eles.get(i).select("td").eq(2).text();//账户账号
				String type = eles.get(i).select("td").eq(3).text();//账户类型
				String mechanism = eles.get(i).select("td").eq(4).text();//开户机构
				if (type.contains("借记卡")&&accNumber.contains(accNumber1.trim())){
					CibChinaUserInfo cibChinaUserInfo= new CibChinaUserInfo();
					cibChinaUserInfo.setAlias(alias);
					cibChinaUserInfo.setName(name);
					cibChinaUserInfo.setAccNumber(accNumber);
					cibChinaUserInfo.setType(type);
					cibChinaUserInfo.setMechanism(mechanism);
					cibChinaUserInfo.setTaskid(taskid);
					cibChinaUserInfo.setOpening(opening);
					cibChinaUserInfo.setBalance(balance);
					list.add(cibChinaUserInfo);
					
				}
				
			}
			return list;
			
			
		}
		
		return null;
		
	}
	//储蓄明细
	public List<CibChinaTransFlow> parserTransFlow(String html,String taskid,String accNumber){
		List<CibChinaTransFlow> transFlows = new ArrayList<CibChinaTransFlow>();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		JsonArray accountCardList = object.get("rows").getAsJsonArray();
		for (JsonElement acc : accountCardList) {
			JsonObject account = acc.getAsJsonObject();
			String cardNum = account.get("cell").toString().replace("[", "").replace("]", "");
			String [] str1 = cardNum.split("\",");
			String dealTime = str1[0].replaceAll("\"", "");//交易时间
			String billingDay = str1[1].replaceAll("\"", "");//记账日
			String shiftTo = str1[2].replaceAll("\"", "");//收入
			String rollOut = str1[3].replaceAll("\"", "");//支出
			String yue = str1[4].replaceAll("\"", "");//账户余额
			String digest = str1[5].replaceAll("\"", "");//摘要
			String oppName = str1[6].replaceAll("\"", "");//对方户名
			String oppBank = str1[7].replaceAll("\"", "");//对方银行
			String oppNumber = str1[8].replaceAll("\"", "");//对方账号
			String purpose = str1[9].replaceAll("\"", "");//用途
			String channel = str1[10].replaceAll("\"", "");//交易渠道
			
			CibChinaTransFlow cibChinaTransFlow = new CibChinaTransFlow();
			cibChinaTransFlow.setDealTime(dealTime);
			cibChinaTransFlow.setBillingDay(billingDay);
			cibChinaTransFlow.setShiftTo(shiftTo);
			cibChinaTransFlow.setRollOut(rollOut);
			cibChinaTransFlow.setYue(yue);
			cibChinaTransFlow.setDigest(digest);
			cibChinaTransFlow.setOppName(oppName);
			cibChinaTransFlow.setOppBank(oppBank);
			cibChinaTransFlow.setOppNumber(oppNumber);
			cibChinaTransFlow.setPurpose(purpose);
			cibChinaTransFlow.setChannel(channel);
			cibChinaTransFlow.setTaskid(taskid);
			cibChinaTransFlow.setAccNumber(accNumber);
			transFlows.add(cibChinaTransFlow);
			//System.out.println(str1[0].replaceAll("\"", ""));
			
		}
		
		return transFlows;
		
	}
	
	//信用卡分期
	public List<CibCreditcardInstallment> parserTranslment(String html,String taskid,String accNumber){
		List<CibCreditcardInstallment> transFlows = new ArrayList<CibCreditcardInstallment>();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		JsonArray accountCardList = object.get("rows").getAsJsonArray();
		if (accountCardList.size()>0){
			for (JsonElement acc : accountCardList) {
				JsonObject account = acc.getAsJsonObject();
				String cardNum = account.get("cell").toString().replace("[", "").replace("]", "");
				String [] str1 = cardNum.split("\",");
				String date = str1[0].replaceAll("\"", "");//分期日期
				String currency = str1[1].replaceAll("\"", "");//币种
				String amount = str1[2].replaceAll("\"", "");//分期金额
				String number = str1[3].replaceAll("\"", "");//分期期数
				String feeCollection = str1[4].replaceAll("\"", "");//手续费收取方式
				String principal = str1[5].replaceAll("\"", "");//每月应还本金
				String remaining = str1[6].replaceAll("\"", "");//剩余未还本金
				String state = str1[7].replaceAll("\"", "");//分期状态
				String abstracts = str1[8].replaceAll("\"", "");//摘要
				CibCreditcardInstallment cibCreditcardInstallment = new CibCreditcardInstallment();
				cibCreditcardInstallment.setDate(date);
				cibCreditcardInstallment.setCurrency(currency);
				cibCreditcardInstallment.setAmount(amount);
				cibCreditcardInstallment.setNumber(number);
				cibCreditcardInstallment.setFeeCollection(feeCollection);
				cibCreditcardInstallment.setPrincipal(principal);
				cibCreditcardInstallment.setRemaining(remaining);
				cibCreditcardInstallment.setState(state);
				cibCreditcardInstallment.setAbstracts(abstracts);
				cibCreditcardInstallment.setCardNumber(accNumber);
				cibCreditcardInstallment.setTaskid(taskid);
				transFlows.add(cibCreditcardInstallment);
			}
			return transFlows;
		}
		return null;
		
	}
	//信用卡基本信息
	public CibCreditcardUserInfo parserCredUser(String html,String taskid){
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("#infoDiv span");
		Elements eles = doc.select("table:nth-child(2) > tbody > tr > td > div");
		if(eles!=null&&eles.size()>0){
			String str = ele.get(0).text().trim();
			String cardNumber = str.substring(str.indexOf("：")+1);//卡号
			String str1 = ele.get(1).text().trim();
			String cardHolder = str1.substring(str1.indexOf("：")+1);//姓名
			String creditLine = eles.get(2).text().trim();//信用额度
			String creditLineDollar = eles.get(3).text().trim();//信用额度(美元)
			String availableLimit = eles.get(4).text().trim();//可用余额
			String availableLimitDollar = eles.get(5).text().trim();//可用余额(美元)
			String cashLimit = eles.get(6).text().trim();//预借现金额度
			String cashLimitDollar = eles.get(7).text().trim();//预借现金额度(美元)
			String creditLimit = eles.get(2).text().trim();//信用卡额度
			//String bill_date ;//还款日
			CibCreditcardUserInfo cibCreditcardUserInfo = new CibCreditcardUserInfo();
			cibCreditcardUserInfo.setCardNumber(cardNumber);
			cibCreditcardUserInfo.setCardHolder(cardHolder);
			cibCreditcardUserInfo.setCreditLine(creditLine);
			cibCreditcardUserInfo.setCreditLineDollar(creditLineDollar);
			cibCreditcardUserInfo.setAvailableLimit(availableLimit);
			cibCreditcardUserInfo.setAvailableLimitDollar(availableLimitDollar);
			cibCreditcardUserInfo.setCashLimit(cashLimit);
			cibCreditcardUserInfo.setCashLimitDollar(cashLimitDollar);
			cibCreditcardUserInfo.setCreditLimit(creditLimit);
			cibCreditcardUserInfo.setTaskid(taskid);
			return cibCreditcardUserInfo;
			
		}
		return null;	
		
	}
	
    //信用卡流水
	public List<CibCreditcardTransFlow> parserCredFlow(String html,String taskid,String accNumber){
		List<CibCreditcardTransFlow> list = new ArrayList<CibCreditcardTransFlow>();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		JsonArray accountCardList = object.get("rows").getAsJsonArray();
		if (accountCardList.size()>0){
			for (JsonElement acc : accountCardList) {
				JsonObject account = acc.getAsJsonObject();
				String cardNum = account.get("cell").toString().replace("[", "").replace("]", "");
				String [] str1 = cardNum.split("\",");
				String tranDate = str1[0].replaceAll("\"", "");//交易日期
				String chargeDate = str1[1].replaceAll("\"", "");//记账日期
				String currency = str1[2].replaceAll("\"", "");//币种
				String fee = str1[3].replaceAll("\"", "");//花费
				String abstracts = str1[4].replaceAll("\"", "");//摘要
				String lastNumber = str1[6].replaceAll("\"", "");//卡号后四位
				CibCreditcardTransFlow cibCreditcardTransFlow = new CibCreditcardTransFlow();
				cibCreditcardTransFlow.setTranDate(tranDate);
				cibCreditcardTransFlow.setChargeDate(chargeDate);
				cibCreditcardTransFlow.setCurrency(currency);
				cibCreditcardTransFlow.setFee(fee);
				cibCreditcardTransFlow.setAbstracts(abstracts);
				cibCreditcardTransFlow.setLastNumber(lastNumber);
				cibCreditcardTransFlow.setTaskid(taskid);
				cibCreditcardTransFlow.setCardNumber(accNumber);
				list.add(cibCreditcardTransFlow);
			}
			return list;
		}
		return null;
	}
	
	//信用卡账单信息
	public CibCreditcardBill parserCredBill(String html,String cardNumber,String trimAmount,String integral,String taskid){
		Document doc = Jsoup.parse(html);
		Elements eles = doc.select("table.table-v td");
		if(eles!=null&&eles.size()>0){
			String billDate = eles.get(0).text().trim();//账单日期
			String billEndDate = eles.get(1).text().trim();//账单到期日期
			String billShouldPay = eles.get(2).text().trim();//本期应还金额
			String billLeastPay = eles.get(3).text().trim();//本期最低还款额
			CibCreditcardBill cibCreditcardBill =new CibCreditcardBill();
			cibCreditcardBill.setBillDate(billDate);
			cibCreditcardBill.setBillEndDate(billEndDate);
			cibCreditcardBill.setBillShouldPay(billShouldPay);
			cibCreditcardBill.setBillLeastPay(billLeastPay);
			cibCreditcardBill.setCardNumber(cardNumber);
			cibCreditcardBill.setTrimAmount(trimAmount);
			cibCreditcardBill.setBillPay(billShouldPay);
			cibCreditcardBill.setIntegral(integral);
//			cibCreditcardBill.setIntegralAdd(integralAdd);
//			cibCreditcardBill.setIntegralExchange(integralExchange);
			cibCreditcardBill.setTaskid(taskid);
			return cibCreditcardBill;
		}else{
			CibCreditcardBill cibCreditcardBill =new CibCreditcardBill();
			cibCreditcardBill.setCardNumber(cardNumber);
			cibCreditcardBill.setTrimAmount(trimAmount);
			cibCreditcardBill.setIntegral(integral);
//			cibCreditcardBill.setIntegralAdd(integralAdd);
//			cibCreditcardBill.setIntegralExchange(integralExchange);
			cibCreditcardBill.setTaskid(taskid);
			return cibCreditcardBill;
		}
		
		
	}
}
