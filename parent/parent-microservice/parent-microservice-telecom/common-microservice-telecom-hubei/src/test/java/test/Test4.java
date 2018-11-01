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

import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiCallrecords;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiPointrecords;

public class Test4 {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\111.html"),"UTF-8");
		List<TelecomHubeiCallrecords> callrecords = new ArrayList<TelecomHubeiCallrecords>();
		if (null != html && html.contains("xd01")) {
			Document doc = Jsoup.parse(html, "utf-8");			
			Element div= doc.getElementById("xd01");	
			if (null !=div) {
				Element table=div.select("table").get(1);			
				Elements trs=table.getElementsByAttributeValue("class","hovergray1");
				if (trs.size()>=3) {
					for (int i = 0; i < trs.size()-2; i++) {
						String startDate=trs.get(i).select("td").get(0).text();
						String calledNum=trs.get(i).select("td").get(1).text();
						String duration=trs.get(i).select("td").get(2).text();
						String calledType=trs.get(i).select("td").get(3).text();
						String type=trs.get(i).select("td").get(4).text();
						String dataNum=trs.get(i).select("td").get(5).text();
						String calledArea=trs.get(i).select("td").get(6).text();					
						String callChangeNum=trs.get(i).select("td").get(7).text();
						String feeType=trs.get(i).select("td").get(8).text();
						String feeTotal=trs.get(i).select("td").get(9).text();
						TelecomHubeiCallrecords callrecord=new TelecomHubeiCallrecords();
						//callrecord.setCycle(month);
						callrecord.setStartDate(startDate);
						callrecord.setCalledNum(calledNum);
						callrecord.setDuration(duration);				
						callrecord.setCalledType(calledType);
						callrecord.setType(type);
						callrecord.setDataNum(dataNum);
						callrecord.setCalledArea(calledArea);
						callrecord.setCalledNum(calledNum);
						callrecord.setCallChangeNum(callChangeNum);
						callrecord.setFeeType(feeType);				
						callrecord.setFeeTotal(feeTotal);
						//callrecord.setTaskid(taskMobile.getTaskid());
						callrecords.add(callrecord);			
					}
				}	
			}			
		}
		for (int i = 0; i < callrecords.size(); i++) {
			System.out.println(callrecords.get(i).toString());
			
		}
		
	}

}
