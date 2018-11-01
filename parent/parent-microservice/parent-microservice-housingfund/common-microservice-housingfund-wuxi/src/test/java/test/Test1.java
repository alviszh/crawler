package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test1 {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\666.html"),"UTF-8");
		Document doc = Jsoup.parse(html, "utf-8"); 
		 Element table= doc.getElementById("listView");
		 Elements tds=table.select("tr").select("td");
		 String name2=tds.get(2).text();
		 System.out.println(name2);
		 String name4=tds.get(4).text();
		 System.out.println(name4);
		 String name6=tds.get(6).text();
		 System.out.println(name6);
		 String name8=tds.get(8).text();
		 System.out.println(name8);
		 String name10=tds.get(10).text();
		 System.out.println(name10);
		 String name14=tds.get(14).text();
		 System.out.println(name14);
		 String name18=tds.get(18).text();
		 System.out.println(name18);
		 String name22=tds.get(22).text();
		 System.out.println(name22);
		 String name26=tds.get(26).text();
		 System.out.println(name26);
		 String name30=tds.get(30).toString();
		 String[] str = name30.split(";");					
		 String[] astr = str[0].split("\"");					
		 String value1=astr[1];
		 System.out.println(value1.substring(0,4)+ "年"+ value1.substring(4,6)+"月");

		 String name34=tds.get(34).toString();
		 String[] arrstr = name34.split(";");					
		 String[] attstr = arrstr[0].split("\"");					
		 String value=attstr[1];
		 System.out.println(value.substring(0,4)+ "年"+ value.substring(4,6)+"月"+ value.substring(6,8)+"日");
	}

}
