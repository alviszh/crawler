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
import com.microservice.dao.entity.crawler.housing.panjin.HousingPanjinPaydetails;
import com.microservice.dao.entity.crawler.housing.panjin.HousingPanjinUserInfo;


@Component
public class HousingFundPanjinParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundPanjinParser.class);
	// 解析用户信息
	public HousingPanjinUserInfo htmlUserInfoParser(String html, TaskHousing taskHousing) {
		HousingPanjinUserInfo userInfo = new HousingPanjinUserInfo();
		if (null != html && html.contains("基本信息")) {
			Document doc = Jsoup.parse(html, "utf-8"); 
			 Element table2= doc.select("table").get(2);
			 Elements tds=table2.select("tr").select("td");
			 String username=tds.get(1).text();
			 String accountNum =tds.get(3).text();
			 String companyAccount=tds.get(5).text();			 
			 String companyName=tds.get(7).text();
			 String idnum=tds.get(9).text();
			 String queryCardnum=tds.get(11).text();
			 String accountState=tds.get(13).text();
			 String crrountBalance=tds.get(15).text();
			 String lastBalance=tds.get(17).text();
			 String yearBalance=tds.get(19).text();
			 String openTime=tds.get(21).text();
			 String lastPaytime=tds.get(23).text();
			 String companyRatio=tds.get(25).text();
			 String personRatio=tds.get(27).text();
			 String companyPay=tds.get(29).text();
			 String personPay=tds.get(31).text();
			 String monthPayBase=tds.get(33).text();
			 String monthpay=tds.get(35).text();
			 String drawTimes=tds.get(37).text();
			 String drawAmount=tds.get(39).text();
			 String lastInterest=tds.get(41).text();		
			 userInfo.setUsername(username);
			 userInfo.setAccountNum(accountNum);
			 userInfo.setCompanyAccount(companyAccount);
			 userInfo.setCompanyName(companyName);
			 userInfo.setIdnum(idnum);
			 userInfo.setQueryCardnum(queryCardnum);
			 userInfo.setAccountState(accountState);
			 userInfo.setCrrountBalance(crrountBalance);
			 userInfo.setLastBalance(lastBalance);
			 userInfo.setYearBalance(yearBalance);
			 userInfo.setOpenTime(openTime);
			 userInfo.setLastPaytime(lastPaytime);
			 userInfo.setCompanyRatio(companyRatio);
			 userInfo.setPersonRatio(personRatio);
			 userInfo.setCompanyPay(companyPay);
			 userInfo.setPersonPay(personPay);
			 userInfo.setMonthPayBase(monthPayBase);
			 userInfo.setMonthpay(monthpay);
			 userInfo.setDrawTimes(drawTimes);
			 userInfo.setDrawAmount(drawAmount);
			 userInfo.setLastInterest(lastInterest);
			 userInfo.setTaskid(taskHousing.getTaskid());
		}
		return userInfo;
	}
	// 解析缴存明细信息
	public List<HousingPanjinPaydetails> htmlPaydetailsParser(String html, TaskHousing taskHousing) {
		List<HousingPanjinPaydetails> paydetails = new ArrayList<HousingPanjinPaydetails>();
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
				 HousingPanjinPaydetails paydetail=new HousingPanjinPaydetails();
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
