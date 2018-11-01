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

import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiServices;


public class Test2 {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\222.html"),"UTF-8");
    	List<TelecomHubeiServices> serviceinfos = new ArrayList<TelecomHubeiServices>();
		if (null != html && html.contains("main_biao")) {
			Document doc = Jsoup.parse(html, "utf-8");			
			Elements div= doc.getElementsByAttributeValue("class","main_biao");		
			if (null !=div) {
				Elements  table=div.select("table");				
				if (null !=table) {
					Element	table1=table.get(0);					
					if (null !=table1) {
						Elements  trs=table1.getElementsByAttributeValue("class","hovergray");	
						System.out.println("trs"+trs.size());
						for (int i = 0; i < trs.size(); i++) {			
							Elements tds=trs.get(i).select("td");
							String itemName=tds.get(0).text();
							String amount=tds.get(1).text();
							String startDate=tds.get(2).text();
							String operate=tds.get(3).text();
							TelecomHubeiServices  serviceinfo=new  TelecomHubeiServices();
							serviceinfo.setItemName(itemName);
							serviceinfo.setAmount(amount);
							serviceinfo.setStartDate(startDate);
							serviceinfo.setOperate(operate);
							serviceinfo.setType("1");
							//serviceinfo.setTaskid(taskMobile.getTaskid());
							serviceinfos.add(serviceinfo);
						}		
					}				
					Element  table2=table.get(1);
					if (null !=table2) {
						Elements  trs2=table2.getElementsByAttributeValue("class","hovergray");	
						System.out.println("trs2"+trs2.size());
						for (int i = 0; i < trs2.size(); i++) {
							Elements tds=trs2.get(i).select("td");
							String itemName=tds.get(0).text();
							String startDate=tds.get(1).text();
							String operate=tds.get(2).text();
							TelecomHubeiServices  serviceinfo=new  TelecomHubeiServices();
							serviceinfo.setItemName(itemName);
								serviceinfo.setStartDate(startDate);
							serviceinfo.setOperate(operate);
							serviceinfo.setType("0");
							//serviceinfo.setTaskid(taskMobile.getTaskid());
							serviceinfos.add(serviceinfo);
						}
					}				
				}			
			}		
		}
		for (int i = 0; i < serviceinfos.size(); i++) {
			System.out.println("---"+i);
			System.out.println(serviceinfos.get(i).toString());
		}
	}

}
