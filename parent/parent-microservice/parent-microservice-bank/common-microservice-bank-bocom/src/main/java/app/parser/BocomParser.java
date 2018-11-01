package app.parser;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import com.gargoylesoftware.htmlunit.Page;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.bocom.BocomDebitcardMsg;
import com.microservice.dao.entity.crawler.bank.bocom.BocomDebitcardTransFlow;

@Component
public class BocomParser {
	

	public List<BocomDebitcardTransFlow> parserDate(Page page, TaskBank taskBank, String selectCardNo) throws Exception {
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		List<BocomDebitcardTransFlow> transFlows = new ArrayList<BocomDebitcardTransFlow>();
		
		if(page.getWebResponse().getContentAsString().contains("未查询到相关条件下的账户明细数据")){
			return null;
		}
		Element tbody = doc.getElementById("recordtbody");
		if(null == tbody){
			return null;
		}
		Elements trs = tbody.select("tr");
		if(null == trs || trs.size()<=0){
			return null;
		}else{
			for(Element tr : trs){
				BocomDebitcardTransFlow bocomDebitcardTransFlow = new BocomDebitcardTransFlow();
				
				String transDate = tr.child(0).text();
				String transType = tr.child(1).text();
				String currency = tr.child(2).text();
				String expend = tr.child(3).child(0).text();
				String income = tr.child(4).child(0).text();
				String balance = tr.child(5).child(0).text();	
				String transSite = tr.child(6).text();
				
				bocomDebitcardTransFlow.setBalance(balance);
				bocomDebitcardTransFlow.setCurrency(currency);
				bocomDebitcardTransFlow.setExpend(expend);
				bocomDebitcardTransFlow.setIncome(income);
				bocomDebitcardTransFlow.setTransDate(transDate);
				bocomDebitcardTransFlow.setTransSite(transSite);
				bocomDebitcardTransFlow.setTransType(transType);
				bocomDebitcardTransFlow.setTaskid(taskBank.getTaskid());
				bocomDebitcardTransFlow.setCardNum(selectCardNo);
				
				transFlows.add(bocomDebitcardTransFlow);
				
//				System.out.println("交易时间    ：    "+transDate);
//				System.out.println("交易方式    ：    "+transType);
//				System.out.println("币种    ：    "+currency);
//				System.out.println("支出金额    ：    "+expend);
//				System.out.println("收入金额    ：    "+income);
//				System.out.println("余额    ：    "+balance);
//				System.out.println("交易地点    ：    "+transSite);
							
			}
		}
		return transFlows;
	}

	/**
	 * 解析卡号信息
	 * @param pageSource
	 * @param taskid
	 * @return
	 */
	public List<BocomDebitcardMsg> parserCardMsg(String pageSource, String taskid) {
		List<BocomDebitcardMsg> msgs = new ArrayList<BocomDebitcardMsg>();
		Document doc = Jsoup.parse(pageSource);
		Elements trs = doc.select("table.form-table>tbody>tr");
		if(null != trs && trs.size()>0){
			for(Element tr:trs){
				Elements tds = tr.select("td");
				if(null != tds && tds.size()>0){
					try{
						BocomDebitcardMsg bocomDebitcardMsg = new BocomDebitcardMsg();
						bocomDebitcardMsg.setCardNum(tds.get(0).text());
						bocomDebitcardMsg.setUsername(tds.get(1).text());
						bocomDebitcardMsg.setAlias(tds.get(2).text());
						bocomDebitcardMsg.setAccountType(tds.get(3).text());
						bocomDebitcardMsg.setCurrency(tds.get(4).text());
						bocomDebitcardMsg.setBalance(tds.get(5).text());
						bocomDebitcardMsg.setAvailableBalance(tds.get(6).text());	
						bocomDebitcardMsg.setTaskid(taskid);
						msgs.add(bocomDebitcardMsg);					
					}catch(Exception e){
						return null;
					}
				}
			}
		}
		
		return msgs;
	}

}
