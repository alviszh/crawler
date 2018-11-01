package app.crawler.htmlparse;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.huizhou.HousingHuiZhouPay;
import com.microservice.dao.entity.crawler.housing.huizhou.HousingHuiZhouUserInfo;

public class HousingHZParse {
	//个人信息
	public static HousingHuiZhouUserInfo userinfo_parse(String html){
		HousingHuiZhouUserInfo userinfo = new HousingHuiZhouUserInfo();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("body > table:nth-child(5) > tbody >tr > td");
		if (ele.size()>0){
			String name = ele.get(1).text().trim();               //姓名
			String idNum = ele.get(3).text().trim();			   //证件号码
			String accountNum = ele.get(5).text().trim();         //个人住房公积金账号
			String accountOldNum = ele.get(7).text().trim();      //个人住房公积金旧账号
			String companyNum = ele.get(9).text().trim();         //单位住房公积金账号
			String companyOldNum = ele.get(11).text().trim();      //单位住房公积金旧账号
			String unitName = ele.get(13).text().trim();           //缴存单位名称
			String state = ele.get(15).text().trim();			   //个人现缴存状态
			String base = ele.get(17).text().trim();               //个人当前缴存基数
			String monthlyPay = ele.get(19).text().trim();         //个人当前月缴额
			String balance = ele.get(21).text().trim();            //个人当前账户余额
			userinfo.setName(name);
			userinfo.setIdNum(idNum);
			userinfo.setAccountNum(accountNum);
			userinfo.setAccountOldNum(accountOldNum);
			userinfo.setCompanyNum(companyNum);
			userinfo.setCompanyOldNum(companyOldNum);
			userinfo.setUnitName(unitName);
			userinfo.setState(state);
			userinfo.setBase(base);
			userinfo.setMonthlyPay(monthlyPay);
			userinfo.setBalance(balance);
			return userinfo;
		}
		return null;
	}
	
	//缴费信息
	public static List<HousingHuiZhouPay> paydetails_parse(String html,TaskHousing taskHousing){
		List<HousingHuiZhouPay> list = new ArrayList<HousingHuiZhouPay>();
		Document doc1 = Jsoup.parse(html);
		Elements ele1 = doc1.select("table.listtable >tbody >tr");
		if (ele1.size()>0){
			for (int i = 0;i < ele1.size();i++){
				String jndate = ele1.get(i).select("td").eq(0).text().trim();             //记账日期
				String abstracts = ele1.get(i).select("td").eq(1).text().trim();          //摘要
				String occurrence = ele1.get(i).select("td").eq(2).text().trim();         //发生额（元）
				String balance = ele1.get(i).select("td").eq(3).text().trim();            //余额（元）
				String payDate = ele1.get(i).select("td").eq(4).text().trim();			   //汇缴年月
				HousingHuiZhouPay pay = new HousingHuiZhouPay();
				pay.setJndate(jndate);
				pay.setAbstracts(abstracts);
				pay.setOccurrence(occurrence);
				pay.setBalance(balance);
				pay.setPayDate(payDate);
				pay.setTaskid(taskHousing.getTaskid());
				list.add(pay);
			}
			return list;
		}
		return null;
	}
}
