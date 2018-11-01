package app.parser;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.crawler.bank.json.BankJsonBean;
import com.microservice.dao.entity.crawler.bank.ccbchina.CcbChinaCreditcardAccountType;
import com.microservice.dao.entity.crawler.bank.ccbchina.CcbChinaCreditcardTransFlow;
import app.bean.WebData;
import app.commontracerlog.TracerLog;

@Component
public class CcbChinaCreditcardParser {
	
	@Autowired
	private TracerLog tracerLog;

	public WebData parser(List<String> pageSources, BankJsonBean bankJsonBean) {
		
		WebData data = new WebData();
		List<CcbChinaCreditcardTransFlow> transFlows = new ArrayList<CcbChinaCreditcardTransFlow>();
		List<CcbChinaCreditcardAccountType> accountTypes = new ArrayList<CcbChinaCreditcardAccountType>();
		
		for(String pageSource : pageSources){
			try{

				Document doc = Jsoup.parse(pageSource);
				Elements table = doc.getElementsByClass("t_data");
				Elements trs = table.get(0).select("tr");
				if(trs.size()<2){
					tracerLog.output("CcbChinaCreditcardAccountType。parser.", "当前账户信息无数据！");
					continue;
				}else if(trs.size()>2){
					for(int i = 1;i<trs.size();i++){
						Element tr = trs.get(i);
						Elements tds = tr.select("td");
						
						CcbChinaCreditcardAccountType accountType = new CcbChinaCreditcardAccountType();
						accountType.setCardNum(bankJsonBean.getLoginName());
						accountType.setCashAmount(tds.get(4).text());
						accountType.setCreditLine(tds.get(3).text());
						accountType.setCurrency(tds.get(0).text());
						accountType.setCurrentBalance(tds.get(5).text());
						accountType.setCurrentMinBalance(tds.get(6).text());
						accountType.setDisputeCount(tds.get(7).text());
						accountType.setDueDate(tds.get(2).text());
//				accountType.setPreviousBalance(tds.get(8).text());
						accountType.setTallyDate(tds.get(1).text());
						accountType.setTaskid(bankJsonBean.getTaskid());
						
						accountTypes.add(accountType);				
					}
				}
				
				Elements mingxiTRs = table.get(2).select("tr");
				
				if(pageSource.contains("无交易明细记录！")){
					tracerLog.output("CcbChinaCreditcardTransFlow。parser.", "当前明细无数据！");
					continue;
				}else{
					for(int i=1;i<mingxiTRs.size();i++){
						Element tr = mingxiTRs.get(i);
						Elements tds = tr.select("td");
						
						CcbChinaCreditcardTransFlow transFlow = new CcbChinaCreditcardTransFlow();
						
						transFlow.setCardNum(bankJsonBean.getLoginName());
						transFlow.setCloseCurrency(tds.get(6).text());
						transFlow.setCloseMoney(tds.get(7).text());
						transFlow.setDealCurrency(tds.get(4).text());
						transFlow.setDealDate(tds.get(0).text());
						transFlow.setDealDescription(tds.get(3).text());
						transFlow.setDealMoney(tds.get(5).text());
						transFlow.setFourCardNum(tds.get(2).text());
						transFlow.setTallyDate(tds.get(1).text());
						transFlow.setTaskid(bankJsonBean.getTaskid());
						
						transFlows.add(transFlow);
						
					}
				}
			}catch(Exception e){
				tracerLog.output("ccbchina.Creditcard.parser", "error");
				tracerLog.output("建设银行信用卡数据解析出错", e.getMessage());
				continue;
			}			
		}
		
		data.setAccountTypes(accountTypes);
		data.setTransFlows(transFlows);	
		return data;
	
	}

}
