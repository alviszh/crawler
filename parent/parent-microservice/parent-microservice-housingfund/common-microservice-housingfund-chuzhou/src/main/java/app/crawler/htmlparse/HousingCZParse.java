package app.crawler.htmlparse;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.chuzhou.HousingChuZhouPay;
import com.microservice.dao.entity.crawler.housing.chuzhou.HousingChuZhouUserinfo;

public class HousingCZParse {
	//个人信息
	public static HousingChuZhouUserinfo userinfo_parse(String html){
		HousingChuZhouUserinfo userInfo = new HousingChuZhouUserinfo();
		Document doc = Jsoup.parse(html);
		Elements ele1 = doc.select("#Panel1 > table:nth-child(3) > tbody > tr >td >span");
		if(ele1.size()>0){
			String unitAccount = ele1.get(0).text().trim();             //单位账号
			String unitName = ele1.get(1).text().trim();                //单位名称
			String personalAccount = ele1.get(2).text().trim();         //个人账号
		    String name = ele1.get(3).text().trim();                    //姓名
			String idCard = ele1.get(4).text().trim();                  //身份证号
			String monthlyRemittance = ele1.get(5).text().trim();       //月 缴 额
			String balance = ele1.get(6).text().trim();                 //余额
			userInfo.setUnitAccount(unitAccount);
			userInfo.setUnitName(unitName);
			userInfo.setPersonalAccount(personalAccount);
			userInfo.setName(name);
			userInfo.setIdCard(idCard);
			userInfo.setMonthlyRemittance(monthlyRemittance);
			userInfo.setBalance(balance);
			return userInfo;
		}
		return null;
	}
	
	//缴费流水
	public static List<HousingChuZhouPay> paydetails_parse(String html,TaskHousing taskHousing){
		List<HousingChuZhouPay> list = new ArrayList<HousingChuZhouPay>();
		Document doc = Jsoup.parse(html);
		Elements ele1 = doc.select("#GridView1 > tbody > tr");
		if(ele1.size()>0){
			for(int i = 1;i <ele1.size();i++){
				String remark = ele1.get(i).select("td").eq(0).text().trim();                  //摘要
				String paytime = ele1.get(i).select("td").eq(1).text().trim();                 //记帐日期
				String debit = ele1.get(i).select("td").eq(2).text().trim();                   //借方金额
				String credit = ele1.get(i).select("td").eq(3).text().trim();                  //贷方金额
				String balance = ele1.get(i).select("td").eq(4).text().trim();                 //余额
				HousingChuZhouPay pay = new HousingChuZhouPay();
				pay.setRemark(remark);
				pay.setPaytime(paytime);
				pay.setDebit(debit);
				pay.setCredit(credit);
				pay.setBalance(balance);
				pay.setTaskid(taskHousing.getTaskid());
				list.add(pay);
			}
			return list;
		}
		return null;
	}
}
