package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ch.qos.logback.core.net.SyslogOutputStream;

public class Test2 {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\777.html"),"UTF-8");
		Document doc = Jsoup.parse(html, "utf-8"); 
		 Elements div= doc.getElementsByAttributeValue("class","scrollDiv");	
		 //System.out.println(div);
		 Element table=div.select("table").get(0);
		 if (null !=table) {
			 Elements trs=table.select("tr");
			 int trs_size=trs.size();
			 if(trs_size>1){
				 for (int i = 1; i < trs_size; i++) {
				 Elements tds=trs.get(i).select("td");
//					 String name1=tds.get(0).text();
//					 System.out.println(name1);
					 String name2=tds.get(1).toString();					
					 String[] arrstr = name2.split(";");					
					 String[] attstr = arrstr[0].split("\"");					
					 String value=attstr[1];
					 System.out.println(value.substring(0,4)+ "."+value.substring(4,6)+"."+value.substring(6,8));
					
					 String name3=tds.get(2).toString();
					 String[] arrstr2 = name3.split(";");					
					 String[] attstr2 = arrstr2[0].split("\"");					
					 String value2=attstr2[1];
					 System.out.println(value2.substring(0,4)+ "."+value.substring(4,6));
//					 
					 String name4=tds.get(3).text();
					 System.out.println(name4);
					 String name5=tds.get(4).text();
					 System.out.println(name5);
					 String name6=tds.get(5).text();
					 System.out.println(name6);
					 String name7=tds.get(6).text();
					 System.out.println(name7);
//					 System.out.println("-----------");
			  	}
			 }
			 
		}
		 

	}

}
