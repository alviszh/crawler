package app.crawler.htmlparse;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.fuzhou.HousingFuZhouPay;
import com.microservice.dao.entity.crawler.housing.fuzhou.HousingFuZhouUserinfo;

public class HousingFZParse {
	//个人信息
	public static HousingFuZhouUserinfo userinfo_parse(String html,String html1){
		HousingFuZhouUserinfo userinfo = new HousingFuZhouUserinfo();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("span.hidden-phone");
		Document doc1 = Jsoup.parse(html1);
		Elements ele1 = doc1.select("#detail > tr > td");
		if (ele.size()>0&&ele1.size()>0){
			String personalAccount = ele1.get(0).text().trim();         //个人客户号
			String unitAccount = ele1.get(1).text().trim();             //单位客户号
			String unitName = ele1.get(2).text().trim();                //单位名称
			String unitMonth = ele1.get(3).text().trim();               //单位缴至月份
			String personalMonth = ele1.get(4).text().trim();           //个人缴至月份
			String base = ele1.get(5).text().trim();                    //缴存基数
			String personPay = ele1.get(6).text().trim();               //个人缴存额
			String companyPay = ele1.get(7).text().trim();              //单位缴存额
			String interest = ele1.get(8).text().trim();                //年度利息
			String balance = ele1.get(9).text().trim();                 //账户余额
			String state = ele1.get(10).text().trim();                  //缴存状态
		    String name = ele.get(1).text().trim();                     //姓名
		    
		    name = name.substring(0, name.indexOf(",")).trim();
		    userinfo.setPersonalAccount(personalAccount);
		    userinfo.setUnitAccount(unitAccount);
		    userinfo.setUnitName(unitName);
		    userinfo.setUnitMonth(unitMonth);
		    userinfo.setPersonalMonth(personalMonth);
		    userinfo.setBase(base);
		    userinfo.setPersonPay(personPay);
		    userinfo.setCompanyPay(companyPay);
		    userinfo.setInterest(interest);
		    userinfo.setBalance(balance);
		    userinfo.setState(state);
		    userinfo.setName(name);
		    return userinfo;
		}
		return null;
		
	}
	
	
	public static List<HousingFuZhouPay> paydetails_parse(String html,TaskHousing taskHousing){
		List<HousingFuZhouPay> list = new ArrayList<HousingFuZhouPay>();
		Document doc1 = Jsoup.parse(html);
		Elements ele1 = doc1.select("#detail > tr");
		if (ele1.size()>0){
			for (int i = 0;i < ele1.size();i++){
				String businessTime = ele1.get(i).select("td").eq(0).text().trim();            //业务时间
				String paytime = ele1.get(i).select("td").eq(1).text().trim();                 //入账时间
				String type = ele1.get(i).select("td").eq(2).text().trim();                    //业务类型
				String amouOccurrence = ele1.get(i).select("td").eq(3).text().trim();          //发生金额
				String balance = ele1.get(i).select("td").eq(4).text().trim();                 //实时余额
				String describe = ele1.get(i).select("td").eq(5).text().trim();                //业务描述
				
				HousingFuZhouPay pay = new HousingFuZhouPay();
				pay.setBusinessTime(businessTime);
				pay.setPaytime(paytime);
				pay.setType(type);
				pay.setAmouOccurrence(amouOccurrence);
				pay.setBalance(balance);
				pay.setDescribe(describe);
				pay.setTaskid(taskHousing.getTaskid());
				list.add(pay);
				
			}
			return list;
		}
		return null;
		
	}
}
