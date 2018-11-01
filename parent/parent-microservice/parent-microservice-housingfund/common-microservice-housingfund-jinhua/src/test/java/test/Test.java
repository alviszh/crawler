package test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {

	public static void main(String[] args) throws IOException {
		//http://wsbs.jhgjj.gov.cn/PubWeb/GR/GRZHMX_List.aspx
		//http://wsbs.jhgjj.gov.cn/PubWeb/GR/GRZHMX_List.aspx
		String ss="GRZHMX.aspx?jgh=3300701&zhlx=01&grzh=101000000035592";
		//tbxJgh=3300701&tbxGrzh=101000000035592&tbxZhlx=01&tbxStart=2017-02-06&tbxEnd=2018-02-06&pageIndex=0&pageSize=20&btnSubmit=%E6%9F%A5+%E8%AF%A2
		String[] str=ss.split("=");
		for (int i = 0; i < str.length; i++) {
			System.out.println(str[i]);
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		Calendar c = Calendar.getInstance();  
		String tbxEnd=format.format(c.getTime());
		System.out.println(tbxEnd);
		c.add(Calendar.YEAR, -3); //年份减3  
		String tbxStart=format.format(c.getTime());
		System.out.println(tbxStart);
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\111.html"),"UTF-8");
		Document doc = Jsoup.parse(html, "utf-8"); 
		Elements tbody=doc.select("tbody");
		int size=tbody.size();
		for (int i = 0; i < size; i++) {
			String userAccount=tbody.get(i).select("td").get(1).text();
			String type=tbody.get(i).select("td").get(3).text();//	账号类型
		    String username=tbody.get(i).select("td").get(5).text();//	姓名
		    String idnum=tbody.get(i).select("td").get(7).text();//	身份证号
		    String companyName=tbody.get(i).select("td").get(9).text();//	单位名称
			String staffName=tbody.get(i).select("td").get(11).text();//	缴存机构
			String personalAmount=tbody.get(i).select("td").get(13).text();//	合计单位比例
			String companyAmount=tbody.get(i).select("td").get(15).text();//	合计个人比例
		    String totalAmount=tbody.get(i).select("td").get(17).text();//	合计月缴额
			String balance=tbody.get(i).select("td").get(19).text();//	账户余额
		
			String state=tbody.get(i).select("td").get(21).text();//	账户状态
			System.out.println(userAccount);
			System.out.println(type);
			System.out.println(username);
			System.out.println(idnum);
			System.out.println(companyName);
			System.out.println(staffName);
			
			System.out.println(personalAmount);
			System.out.println(companyAmount);
			System.out.println(totalAmount);
			System.out.println(balance);
			System.out.println(state);
			
			String href=tbody.get(i).select("a").attr("href");
			System.out.println(href);
			System.out.println("------------");
		}
      
	}
	public static String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("td:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}
}
