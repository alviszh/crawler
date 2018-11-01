package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.chongqing.HousingChongqingAccountInfo;

public class Test3 {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\555.html"),"UTF-8");
		Document doc = Jsoup.parse(html, "utf-8"); 
		 Elements divs= doc.getElementsByAttributeValue("class","listinfo");	
		 HousingChongqingAccountInfo accountInfo=new HousingChongqingAccountInfo();
	        if (null !=divs ) {
	        	Elements tds=divs.select("tbody").select("td");
	        	String idnum=tds.get(1).text();
	        	accountInfo.setIdnum(idnum);
	        	String	openTime =tds.get(3).text();
	        	accountInfo.setOpenTime(openTime);
	        }
	        
	        System.out.println(accountInfo.toString());
	}



}
