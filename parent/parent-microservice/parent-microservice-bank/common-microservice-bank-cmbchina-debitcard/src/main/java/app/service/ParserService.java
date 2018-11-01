package app.service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.bank.cmbchina.CmbChinaDebitCardTransFlow;
import com.microservice.dao.entity.crawler.bank.cmbchina.CmbChinaDebitCardUserInfo;

import app.commontracerlog.TracerLog;


@Component
public class ParserService {
	
	@Autowired 
	private TracerLog tracerLog; 
	
	
	//解析账户信息
	public List<CmbChinaDebitCardUserInfo> parserUserinfo(String html,String taskid){
		List<CmbChinaDebitCardUserInfo> transFlows = null;
		if(html==null){
			tracerLog.addTag("用户信息为空", taskid);
		}else{
			Document doc = Jsoup.parse(html);
			Elements eles = doc.select("#dgSubAccRecSet > tbody tr");
			if(eles!=null&&eles.size()>0){
				transFlows = new ArrayList<CmbChinaDebitCardUserInfo>();
				for(int i=1;i<eles.size();i++){//表单中由于第一行是表头，数据从第二航开始，因此i=1
					Element ele = eles.get(i); 
					tracerLog.addTag("一条客户信息", ele.html()); 
					String cardNo = ele.select("td").eq(1).text();//交易时间
					String username = ele.select("td").eq(2).text();//支出
					String branch = ele.select("td").eq(3).text();//存入
					
					CmbChinaDebitCardUserInfo transFlow = new CmbChinaDebitCardUserInfo();
					transFlow.setCardNo(cardNo);
					transFlow.setUsername(username);
					transFlow.setBranch(branch);
					transFlow.setTaskid(taskid);
					transFlows.add(transFlow); 
				}
				if(transFlows.isEmpty()){
					tracerLog.addTag("没有客户信息",html);
				}else{
					tracerLog.addTag("共解析出客户信息", transFlows.size()+"条");
				}
				
				
			}
		}
		return transFlows;
	}
	
	//解析账号流水信息
	public List<CmbChinaDebitCardTransFlow> parserTransFlow(String html,String taskid){
		
		List<CmbChinaDebitCardTransFlow> transFlows = null;
		if(html==null){
			tracerLog.addTag("流水信息为空", taskid);
		}else{
			Document doc = Jsoup.parse(html);
			Elements eles = doc.select("table.dgMain>tbody tr");
			 
			String cardNo = "";
			Elements cardeles = doc.select("#ddlDebitCardList option[selected=selected]");
			if(cardeles!=null&&cardeles.size()>0){
				Element cardele = cardeles.get(0);
				cardNo = cardele.ownText();
			}else{
				tracerLog.addTag("银行卡号未解析出来","#ddlDebitCardList option[selected=selected]");
			}
			
			tracerLog.addTag("卡号：",cardNo);
			
			if(eles!=null&&eles.size()>0){
				transFlows = new ArrayList<CmbChinaDebitCardTransFlow>();
				for(int i=1;i<eles.size();i++){//表单中由于第一行是表头，数据从第二航开始，因此i=1
					Element ele = eles.get(i);
					
					tracerLog.addTag("一条交易流水", ele.html());
					
					String date = ele.select("td").eq(0).text();//交易日期
					String time = ele.select("td").eq(1).text();//交易时间
					String pay = ele.select("td").eq(2).text();//支出
					String deposit = ele.select("td").eq(3).text();//存入
					String balance = ele.select("td").eq(4).text();//存入
					String type = ele.select("td").eq(5).text();//交易类型
					String remarks = ele.select("td").eq(6).text();//交易备注
					
					CmbChinaDebitCardTransFlow transFlow = new CmbChinaDebitCardTransFlow();
					transFlow.setDate(date);
					transFlow.setTime(time);
					transFlow.setPay(pay);
					transFlow.setDeposit(deposit);
					transFlow.setBalance(balance);
					transFlow.setType(type);
					transFlow.setRemarks(remarks);
					transFlow.setTaskid(taskid);
					transFlow.setCardNo(cardNo);
					transFlows.add(transFlow);
					
					tracerLog.addTag("流水bean",transFlow.toString());
				}
			}
			if(transFlows.isEmpty()){
				tracerLog.addTag("没有解析出流水",html);
			}else{
				tracerLog.addTag("共解析出交易流水", transFlows.size()+"条");
			}
			
			
		}
		
		
		
		return transFlows;
	}
	

}
