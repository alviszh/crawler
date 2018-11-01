package app.crawler.htmlparse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.xianyang.HousingXianYangUserInfo;

public class HousingXYParse {
	//个人信息
	public static HousingXianYangUserInfo userinfo_parse(String html) {
		HousingXianYangUserInfo userinfo = new HousingXianYangUserInfo();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("table:nth-child(4) > tbody > tr> td");
		if (ele.size()>0){
			System.out.println("sssssssssssss");
			String companyName = ele.get(2).text().trim();     //单位名称
			String unitCode = ele.get(4).text().trim();        //单位账号
			String username = ele.get(6).text().trim();        //姓名
			String personalCode = ele.get(8).text().trim();    //个人账号
			String idCard = ele.get(10).text().trim();          //身份证号
			String monthlyPayment = ele.get(12).text().trim();  //月缴存额
			String balance = ele.get(14).text().trim();         //余额(元)
			String years = ele.get(16).text().trim();           //缴至年月
			String state = ele.get(18).text().trim();           //账户状态
			
			userinfo.setUnitCode(unitCode);
			userinfo.setCompanyName(companyName);
			userinfo.setPersonalCode(personalCode);
			userinfo.setUsername(username);
			userinfo.setIdCard(idCard);
			userinfo.setMonthlyPayment(monthlyPayment);
			userinfo.setBalance(balance);
			userinfo.setYears(years);
			userinfo.setState(state);
		}
		return userinfo;
	}
}
