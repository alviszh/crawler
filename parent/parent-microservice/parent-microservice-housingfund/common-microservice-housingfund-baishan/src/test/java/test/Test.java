package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.baishan.HousingBaishanPaydetails;

public class Test {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\777.html"),"UTF-8");
		//System.out.println(html);
		Document doc = Jsoup.parse(html, "utf-8");
		Elements tr = doc.getElementsByTag("tr");
		for (Element element : tr) {
			Elements td = element.getElementsByTag("td");
			if (td.size() == 6) {
				String accountDate = td.get(0).text();
				if (!"日期".equals(accountDate)) {						
					String debtAmount = td.get(1).text();
					String creditAmount = td.get(2).text();
					String balance = td.get(3).text();
					String lendingdirection = td.get(4).text();
					String explanation = td.get(5).text();
				//	System.out.println(accountDate);
					System.out.println("debtAmount="+debtAmount);
					System.out.println("creditAmount="+creditAmount);
					System.out.println("balance="+balance);
					//System.out.println(lendingdirection);
					//System.out.println(explanation);
				
//					HousingBaishanPaydetails housingBaishanPaydetail= new HousingBaishanPaydetails(accountDate, debtAmount,creditAmount, balance,lendingdirection, explanation,"111");
//				   System.out.println(housingBaishanPaydetail.toString());
				   
				   
					HousingBaishanPaydetails housingBaishanPaydetail2= new HousingBaishanPaydetails();
					housingBaishanPaydetail2.setAccountDate(accountDate);
					housingBaishanPaydetail2.setDebtAmount(debtAmount);
					housingBaishanPaydetail2.setCreditAmount(creditAmount);
					housingBaishanPaydetail2.setBalance(balance);
					housingBaishanPaydetail2.setLendingdirection(lendingdirection);
					housingBaishanPaydetail2.setExplanation(explanation);
					System.out.println(housingBaishanPaydetail2.toString());
					System.out.println("---------------------"+tr.size());
				}
			}
		}
	}

}
