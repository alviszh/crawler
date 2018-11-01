package app.crawler.htmlparse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.jiangmen.HousingJiangMenUserInfo;

public class HousingJMParse {
	public static HousingJiangMenUserInfo userinfo_parse(String html){
		HousingJiangMenUserInfo userinfo = new HousingJiangMenUserInfo();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("td.righttd > span");
		if(ele.size()>0){
			String name = ele.get(0).text().trim();               //姓名
			String accumulationFund = ele.get(1).text().trim();   //公积金账号
			String number = ele.get(2).text().trim();             //单位登记号
			String unitName = ele.get(3).text().trim();           //单位名称
			String accountNum = ele.get(4).text().trim();         //个人编号
			String balance = ele.get(5).text().trim();            //个人账号余额
			String state = ele.get(6).text().trim();			   //个人账号状态
			String base = ele.get(7).text().trim();               //个人缴存基数
			String companyRatio = ele.get(8).text().trim();       //单位缴存比例
			String companyMonthlyPay = ele.get(9).text().trim();  //月缴存额单位部分
			String personRatio = ele.get(10).text().trim();        //个人缴存比例
			String persionalMonthlyPay = ele.get(11).text().trim();//月缴存额个人部分
			
			userinfo.setName(name);
			userinfo.setAccumulationFund(accumulationFund);
			userinfo.setNumber(number);
			userinfo.setUnitName(unitName);
			userinfo.setAccountNum(accountNum);
			userinfo.setBalance(balance);
			userinfo.setState(state);
			userinfo.setBase(base);
			userinfo.setCompanyRatio(companyRatio);
			userinfo.setCompanyMonthlyPay(companyMonthlyPay);
			userinfo.setPersonRatio(personRatio);
			userinfo.setPersionalMonthlyPay(persionalMonthlyPay);
			
			return userinfo;
		}
		return null;
	}
}
