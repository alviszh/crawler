package app.crawler.htmlparse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.baotou.HousingBaoTouUserinfo;

public class HousingBTParse {
	//个人信息
	public static HousingBaoTouUserinfo userinfo_parse(String html){
		HousingBaoTouUserinfo userinfo = new HousingBaoTouUserinfo();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("td.tdright");
		if (ele.size()>0){
			String name = ele.get(0).text().trim();                    //姓名
			String account = ele.get(1).text().trim();                 //账号
			String unitName = ele.get(2).text().trim();                //单位名称
			String unitAccount = ele.get(3).text().trim();             //单位账号
			String monthlyRemittance = ele.get(4).text().trim();       //月 缴 额
			String payDate = ele.get(5).text().trim();					//缴存至
			String base = ele.get(6).text().trim();                    //缴存基数
			String balance = ele.get(7).text().trim();                 //公积金余额
			String ratio = ele.get(8).text().trim();                   //缴存比例
			String state = ele.get(9).text().trim();					//帐号状态
			String interest = ele.get(10).text().trim();				//利息
			userinfo.setName(name);
			userinfo.setAccount(account);
			userinfo.setUnitName(unitName);
			userinfo.setUnitAccount(unitAccount);
			userinfo.setMonthlyRemittance(monthlyRemittance);
			userinfo.setPayDate(payDate);
			userinfo.setBase(base);
			userinfo.setBalance(balance);
			userinfo.setRatio(ratio);
			userinfo.setState(state);
			userinfo.setInterest(interest);
			return userinfo;
		}
		return null;
	}
}
