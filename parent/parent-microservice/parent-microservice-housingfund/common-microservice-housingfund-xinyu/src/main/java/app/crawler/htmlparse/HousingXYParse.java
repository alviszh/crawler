package app.crawler.htmlparse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.xinyu.HousingXinYuUserInfo;

public class HousingXYParse {
	//个人信息
		public static HousingXinYuUserInfo userinfo_parse(String html){
			HousingXinYuUserInfo userinfo = new HousingXinYuUserInfo();
			Document doc = Jsoup.parse(html);
			Elements ele = doc.select("#column1 > div >table >tbody >tr");
			if(ele.size()>0){
				String unitCode = ele.get(0).select("td").eq(1).text().trim();        //公积金账号
				String companyName = ele.get(1).select("td").eq(1).text().trim();     //单位
				String username = ele.get(2).select("td").eq(1).text().trim();        //姓名
				String balance = ele.get(3).select("td").eq(1).text().trim();         //公积金余额
				String monthlyPayment = ele.get(4).select("td").eq(1).text().trim();  //月缴纳数
				
				userinfo.setUnitCode(unitCode);
				userinfo.setCompanyName(companyName);
				userinfo.setUsername(username);
				userinfo.setBalance(balance);
				userinfo.setMonthlyPayment(monthlyPayment);
				return userinfo;
			}
			return null;
		}
}
