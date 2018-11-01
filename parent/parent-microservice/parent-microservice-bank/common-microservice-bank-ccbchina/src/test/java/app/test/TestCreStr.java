package app.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.bank.ccbchina.CcbChinaCreditcardAccountType;
import com.microservice.dao.entity.crawler.bank.ccbchina.CcbChinaCreditcardTransFlow;

public class TestCreStr {
	
	public static void main(String[] args) {
		
		String text = readTxtFile("C:\\home\\tieba.txt");
//		System.out.println(text);
//		parser(text);
		parserTime(text);
		
	}
	
	 private static void parserTime(String text) {
		 Document doc = Jsoup.parse(text);
			Elements divs = doc.select("#j_p_postlist>div");
			if(null != divs && divs.size()>0){
				for(Element div : divs){
					Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}\\s*\\d{2}\\:\\d{2}");
					Matcher m = pattern.matcher(div.toString());
					if(m.find()){
						System.out.println(m.group());
					}
				}
			}
	}
		private static String parserParam(Element div, String rule) {
			Elements elements = div.select(rule);
			if(null != elements && elements.size()>0){
				String param = elements.first().text();
				return param;
			}else{
				return null;
			}
		}

	private static void parser(String pageSource) {
		 Document doc = Jsoup.parse(pageSource);
			Elements table = doc.getElementsByClass("t_data");
			Elements trs = table.get(0).select("tr");
			if(trs.size()<2){
				
			}else if(trs.size()>2){
				for(int i = 1;i<trs.size();i++){
					Element tr = trs.get(i);
					Elements tds = tr.select("td");
					
					CcbChinaCreditcardAccountType accountType = new CcbChinaCreditcardAccountType();
					accountType.setCashAmount(tds.get(4).text());
					accountType.setCreditLine(tds.get(3).text());
					accountType.setCurrency(tds.get(0).text());
					accountType.setCurrentBalance(tds.get(5).text());
					accountType.setCurrentMinBalance(tds.get(6).text());
					accountType.setDisputeCount(tds.get(7).text());
					accountType.setDueDate(tds.get(2).text());
//				accountType.setPreviousBalance(tds.get(8).text());
					accountType.setTallyDate(tds.get(1).text());	
					System.out.println(accountType.toString());
				}
			}
			
			Elements mingxiTRs = table.get(2).select("tr");
			
			if(pageSource.contains("无交易明细记录！")){
			
			}else{
				for(int i=1;i<mingxiTRs.size();i++){
					Element tr = mingxiTRs.get(i);
					Elements tds = tr.select("td");
					
					CcbChinaCreditcardTransFlow transFlow = new CcbChinaCreditcardTransFlow();
					
					transFlow.setCloseCurrency(tds.get(6).text());
					transFlow.setCloseMoney(tds.get(7).text());
					transFlow.setDealCurrency(tds.get(4).text());
					transFlow.setDealDate(tds.get(0).text());
					transFlow.setDealDescription(tds.get(3).text());
					transFlow.setDealMoney(tds.get(5).text());
					transFlow.setFourCardNum(tds.get(2).text());
					transFlow.setTallyDate(tds.get(1).text());
					System.out.println(transFlow.toString());

					
				}
				
			}	
//		System.out.println(table.toString());
//		Elements trs = table.get(0).select("tr"); 
//		if(trs.size()<2){
//			System.out.println("当前用户无数据");
//		}else if(trs.size()>2){
//			Element tr = trs.get(1);
//			System.out.println(tr.toString());
//			Elements tds = tr.select("td");
//			
//			System.out.println("币种  : "+tds.get(0).text());
//			System.out.println("账单日  : "+tds.get(1).text());
//			System.out.println("到期还款日  : "+tds.get(2).text());
//			System.out.println("信用额度 : "+tds.get(3).text());
//			System.out.println("取现额度  : "+tds.get(4).text());
//			System.out.println("本期全部应还款额  : "+tds.get(5).text());
//			System.out.println("本期最低还款额  : "+tds.get(6).text());
//			System.out.println("争议款/笔数  : "+tds.get(7).text());
//			System.out.println("上期账单余额  : "+tds.get(8).text());
//			
//		}
		
		
//		Elements mingxiTRs = table.get(2).select("tr");
//		
//		if(mingxiTRs.size()<2){
//			System.out.println("当前用户无数据");
//		}else{
//			Element tr = mingxiTRs.get(1);
//			System.out.println(tr.toString());
//			Elements tds = tr.select("td");
//			
//			System.out.println("交易日  : "+tds.get(0).text());
//			System.out.println("记账日 : "+tds.get(1).text());
//			System.out.println("卡号后四位  : "+tds.get(2).text());
//			System.out.println("交易描述 : "+tds.get(3).text());
//			System.out.println("交易币种  : "+tds.get(4).text());
//			System.out.println("交易金额  : "+tds.get(5).text());
//			System.out.println("结算币种  : "+tds.get(6).text());
//			System.out.println("结算金额 : "+tds.get(7).text());
//			
//		}
//		
		
	}
	 
	 

	/**
	 * @param filePath
	 * @return
	 */
	public static String readTxtFile(String filePath) {
		 String text = "";
	     try {
	         File file = new File(filePath);
	         if (file.isFile() && file.exists()) {
	             InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
	             BufferedReader br = new BufferedReader(isr);
	             String lineTxt;
				while ((lineTxt = br.readLine()) != null) {
					text = text+lineTxt;
	             }
	             br.close();
	         } else {
	             System.out.println("文件不存在!");
	         }
	     } catch (Exception e) {
	         System.out.println("文件读取错误!");
	     }
		return text;

	    }

}
