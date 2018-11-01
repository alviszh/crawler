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

import com.microservice.dao.entity.crawler.housing.chongqing.HousingChongqingPaydetails;

public class Test1 {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\111.html"),"UTF-8");
		Document doc = Jsoup.parse(html, "utf-8"); 
		 List<HousingChongqingPaydetails> paydetails=new  ArrayList<HousingChongqingPaydetails>();
		 Elements divs= doc.getElementsByAttributeValue("class","listinfo");	
		 if (null !=divs) {
				Element  table=divs.select("table").get(0);
				if (null !=table) {
					Elements  trs=table.select("tbody").select("tr");
					int trs_size=trs.size();
					if (trs_size>0) {
						for (int i = 0; i < trs.size(); i++) {
							HousingChongqingPaydetails paydetail=new HousingChongqingPaydetails();
							Elements tds=trs.get(i).select("td");
							String paytime=tds.get(0).text();
							String remark=tds.get(1).text();
							String personalPay=tds.get(2).text();
							String companyPay=tds.get(3).text();
							String currentBalance=tds.get(4).text();
							paydetail.setPaytime(paytime);
							paydetail.setRemark(remark);
							paydetail.setPersonalPay(personalPay);
							paydetail.setCompanyPay(companyPay);
							paydetail.setCurrentBalance(currentBalance);
							paydetails.add(paydetail);
						}
					}
				}
		}
		
			
		 for (int i = 0; i < paydetails.size(); i++) {
			System.out.println(paydetails.get(i).toString());
		}
	
	}



}
