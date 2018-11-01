package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.heihe.HousingHeihePaydetails;
import com.microservice.dao.entity.crawler.housing.heihe.HousingHeiheUserInfo;


@Component
public class HousingFundHeiheParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundHeiheParser.class);
	// 解析用户信息
	public HousingHeiheUserInfo htmlUserInfoParser(String html, TaskHousing taskHousing) {
		HousingHeiheUserInfo userInfo = new HousingHeiheUserInfo();
		if (null != html && html.contains("基本信息")) {
			Document doc = Jsoup.parse(html, "utf-8");
			Element table2 = doc.select("table").get(2);
			Elements tds = table2.select("tr").select("td");
			 String username=tds.get(1).text();
			 String accountNum =tds.get(3).text();
			 String companyAccount=tds.get(5).text();		
			 String companyName=tds.get(7).text();
			 String idnum=tds.get(9).text();
			 String openTime=tds.get(11).text();
			  String accountState=tds.get(13).text();
				
			 String crrountBalance =tds.get(15).text();
			 String lastPaytime =tds.get(17).text();
			 String monthpay =tds.get(19).text();
			 String companyPay =tds.get(21).text();
			 String personPay =tds.get(23).text();
			 String hasCard=tds.get(25).text();
			 String drawAmount=tds.get(27).text();	
			userInfo.setUsername(username);
			userInfo.setAccountNum(accountNum);
			userInfo.setCompanyAccount(companyAccount);
			userInfo.setCompanyName(companyName);
			userInfo.setIdnum(idnum);
			userInfo.setAccountState(accountState);
			userInfo.setCrrountBalance(crrountBalance);
			userInfo.setOpenTime(openTime);
			userInfo.setLastPaytime(lastPaytime);
			userInfo.setHasCard(hasCard);
			userInfo.setCompanyPay(companyPay);
			userInfo.setPersonPay(personPay);
			userInfo.setMonthpay(monthpay);
			userInfo.setDrawAmount(drawAmount);
			userInfo.setTaskid(taskHousing.getTaskid());
		}
		return userInfo;
	}
	// 解析缴存明细信息
	public List<HousingHeihePaydetails> htmlPaydetailsParser(String html, TaskHousing taskHousing) {
		List<HousingHeihePaydetails> paydetails = new ArrayList<HousingHeihePaydetails>();
		if (null != html && html.contains("prtable")) {
			Document doc = Jsoup.parse(html, "utf-8"); 
			Element table= doc.getElementById("prtable");
			 Elements trs=table.select("tbody").select("tr");	
			 for (int i = 0; i < trs.size(); i++) {
				 Elements tds = trs.get(i).select("td");
				 String accountDate=tds.get(0).text();
				 String accountAmount=tds.get(1).text();
				 String summary=tds.get(2).text();
				 String balance=tds.get(3).text();
				 String payTime=tds.get(4).text();
				 HousingHeihePaydetails paydetail=new HousingHeihePaydetails();
				 paydetail.setAccountDate(accountDate);
				 paydetail.setAccountAmount(accountAmount);
				 paydetail.setSummary(summary);
				 paydetail.setBalance(balance);
				 paydetail.setPayTime(payTime);
				 paydetail.setTaskid(taskHousing.getTaskid());
				 paydetails.add(paydetail);
			}
		}
		return paydetails;
	}
}
