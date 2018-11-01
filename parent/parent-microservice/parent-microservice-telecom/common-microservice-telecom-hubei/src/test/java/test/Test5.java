package test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiPaymonths;

public class Test5 {

	public static void main(String[] args) throws IOException {
		
		for (int i = 1; i <=6; i++) {
			LocalDate today = LocalDate.now();
			// 本月的第一天
			LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(), 1).plusMonths(-i);
			LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
			if (i == 0) {
				enddate = today;
			}
			String monthint = stardate.getMonthValue() + "";
			if (monthint.length() < 2) {
				monthint = "0" + monthint;
			}
			String month = stardate.getYear() + monthint;
			System.out.println(month);
		}
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\111.html"),"UTF-8");

		List<TelecomHubeiPaymonths> paymonths = new ArrayList<TelecomHubeiPaymonths>();
		if (null != html && html.contains("lszd_month") && html.contains("月基本费")) {
			Document doc = Jsoup.parse(html, "utf-8");			
			Elements div= doc.getElementsByAttributeValue("align","left");
			Elements eles =div.select("tr:contains(月基本费)"); 
			if (null !=eles && eles.size()>0) {
				Element ele1 = eles.get(0).nextElementSibling(); 
				Elements trs1=ele1.select("tr");
				for (int i = 0; i < trs1.size(); i++) {				
					String itemName=trs1.get(i).select("td").get(0).text();
					String amount=trs1.get(i).select("td").get(1).text();
					TelecomHubeiPaymonths  paymonth=new TelecomHubeiPaymonths();
					//paymonth.setCycle(month);
					paymonth.setItemName(itemName);
					paymonth.setAmount(amount);
					//paymonth.setTaskid(taskMobile.getTaskid());
					paymonths.add(paymonth);
				}
			}			
		
			Elements eles2 =div.select("tr:contains(代收费)"); 
		  	if (null !=eles2 && eles2.size() >0) { 
				Element ele12 = eles2.get(0).nextElementSibling(); 
				if (null !=ele12) {
					Elements trs2=ele12.select("tr");
					for (int i = 0; i < trs2.size(); i++) {				
						String itemName=trs2.get(i).select("td").get(0).text();
						String amount=trs2.get(i).select("td").get(1).text();
						TelecomHubeiPaymonths  paymonth=new TelecomHubeiPaymonths();
						//paymonth.setCycle(month);
						paymonth.setItemName(itemName);
						paymonth.setAmount(amount);
						//paymonth.setTaskid(taskMobile.getTaskid());
						paymonths.add(paymonth);
					}	
				}	
			}
							
			Elements eles3 =div.select("tr:contains(语音通信费)"); 
			if (null !=eles3  && eles3.size() >0) {
				Element ele13 = eles3.get(0).nextElementSibling(); 
				if (null !=ele13) {
					Elements trs3=ele13.select("tr");
					for (int i = 0; i < trs3.size(); i++) {				
						String itemName=trs3.get(i).select("td").get(0).text();
						String amount=trs3.get(i).select("td").get(1).text();
						TelecomHubeiPaymonths  paymonth=new TelecomHubeiPaymonths();
						//paymonth.setCycle(month);
						paymonth.setItemName(itemName);
						paymonth.setAmount(amount);
						//paymonth.setTaskid(taskMobile.getTaskid());
						paymonths.add(paymonth);
					}	
				}
			}	
			
			Elements eles4 =div.select("tr:contains(语音通信费)"); 
			if (null !=eles4  && eles4.size() >0) {
				Element eles14 = eles4.get(0).nextElementSibling(); 
				if (null !=eles14) {
					Elements trs4=eles14.select("tr");
					for (int i = 0; i < trs4.size(); i++) {				
						String itemName=trs4.get(i).select("td").get(0).text();
						String amount=trs4.get(i).select("td").get(1).text();
						TelecomHubeiPaymonths  paymonth=new TelecomHubeiPaymonths();
						//paymonth.setCycle(month);
						paymonth.setItemName(itemName);
						paymonth.setAmount(amount);
						//paymonth.setTaskid(taskMobile.getTaskid());
						paymonths.add(paymonth);
					}	
				}
			}		
			

			Elements eles5 =div.select("tr:contains(短信彩信费)"); 
			if (null !=eles5  && eles5.size() >0) {
				Element eles15 = eles5.get(0).nextElementSibling(); 
				if (null !=eles15) {
					Elements trs5=eles15.select("tr");
					for (int i = 0; i < trs5.size(); i++) {				
						String itemName=trs5.get(i).select("td").get(0).text();
						String amount=trs5.get(i).select("td").get(1).text();
						TelecomHubeiPaymonths  paymonth=new TelecomHubeiPaymonths();
						//paymonth.setCycle(month);
						paymonth.setItemName(itemName);
						paymonth.setAmount(amount);
						//paymonth.setTaskid(taskMobile.getTaskid());
						paymonths.add(paymonth);
					}	
				}
			}	
		}
		
	for (int i = 0; i < paymonths.size(); i++) {
		System.out.println(paymonths.get(i).toString());
	}
	}

}
