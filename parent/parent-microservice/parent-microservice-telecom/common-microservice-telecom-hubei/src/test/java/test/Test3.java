package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiPaymonths;

public class Test3 {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\test.html"),"UTF-8");
		
		List<TelecomHubeiPaymonths> paymonths = new ArrayList<TelecomHubeiPaymonths>();
		String month="201709";
			Document doc = Jsoup.parse(html, "utf-8");			
			Elements div= doc.getElementsByAttributeValue("align","left");
			Elements eles =div.select("tr:contains(月基本费)"); 
			Element ele1 = eles.get(0).nextElementSibling(); 
			Elements trs1=ele1.select("tr");
			for (int i = 0; i < trs1.size(); i++) {				
				String itemName=trs1.get(i).select("td").get(0).text();
				String amount=trs1.get(i).select("td").get(1).text();
				TelecomHubeiPaymonths  paymonth=new TelecomHubeiPaymonths();
				paymonth.setCycle(month);
				paymonth.setItemName(itemName);
				paymonth.setAmount(amount);
			//	paymonth.setTaskid(taskMobile.getTaskid());
				paymonths.add(paymonth);
			}
			Elements eles2 =div.select("tr:contains(代收费)"); 
			Element ele12 = eles2.get(0).nextElementSibling(); 
			Elements trs2=ele12.select("tr");
			for (int i = 0; i < trs2.size(); i++) {				
				String itemName=trs2.get(i).select("td").get(0).text();
				String amount=trs2.get(i).select("td").get(1).text();
				TelecomHubeiPaymonths  paymonth=new TelecomHubeiPaymonths();
				paymonth.setCycle(month);
				paymonth.setItemName(itemName);
				paymonth.setAmount(amount);
				//paymonth.setTaskid(taskMobile.getTaskid());
				paymonths.add(paymonth);
			}			
			Elements eles3 =div.select("tr:contains(优惠费用)"); 
			Element ele13 = eles3.get(1).nextElementSibling(); 
			Elements trs3=ele13.select("tr");
			for (int i = 0; i < trs3.size(); i++) {				
				String itemName=trs3.get(i).select("td").get(0).text();
				String amount=trs3.get(i).select("td").get(1).text();
				TelecomHubeiPaymonths  paymonth=new TelecomHubeiPaymonths();
				paymonth.setCycle(month);
				paymonth.setItemName(itemName);
				paymonth.setAmount(amount);
				//paymonth.setTaskid(taskMobile.getTaskid());
				paymonths.add(paymonth);
			}			
	
		for (int i = 0; i < paymonths.size(); i++) {
		System.out.println(paymonths.get(i).toString());
		
		}
	}

}
