package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouPoint;


public class Test2 {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\111.html"),"UTF-8");
		
		Document doc = Jsoup.parse(html, "utf-8");		
		TelecomGuizhouPoint point = new TelecomGuizhouPoint();
		Elements div= doc.getElementsByAttributeValue("class","details");
		System.out.println(div.toString());
		if (null !=div && div.toString().contains("details")) {
			Elements trps = div.select("p");
			String status=trps.get(0).text();
			String canuse=trps.get(1).text();
			String expires=trps.get(2).text();
			
			String statusPoint[] = status.split("：");
			String canuseIntegral[] = canuse.split("：");
			String expiresIntegral[] = expires.split("：");
			point.setStatus(statusPoint[1]);
			point.setCanuseIntegral(canuseIntegral[1]);
			point.setExpiresIntegral(expiresIntegral[1]);
			//taskMobile.setTaskid(taskMobile.getTaskid());
		}
	   System.out.println(point.toString());
 	}

}
