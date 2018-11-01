package app.crawler.htmlparse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.jian.HousingJiAnPay;
import com.microservice.dao.entity.crawler.housing.jian.HousingJiAnUserInfo;

public class HousingJAParse {
	//个人信息
	public static HousingJiAnUserInfo userinfo_parse(String html){
		HousingJiAnUserInfo userinfo = new HousingJiAnUserInfo();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("td.bgContent");
		if (ele.size()>0){
			String name = ele.get(0).text().trim();               //姓名
			String idNum = ele.get(1).text().trim();			   //身份证号
			String accountCode = ele.get(2).text().trim();        //个人代码
			String companyCode = ele.get(3).text().trim();        //单位代码
			String unitName = ele.get(4).text().trim();           //单位名称
			String state = ele.get(6).text().trim();			   //状态
			String balance = ele.get(7).text().trim();            //余额
			String wangdian = ele.get(8).text().trim();           //所属网点
			userinfo.setName(name);
			userinfo.setIdNum(idNum);
			userinfo.setAccountCode(accountCode);
			userinfo.setCompanyCode(companyCode);
			userinfo.setUnitName(unitName);
			userinfo.setState(state);
			userinfo.setBalance(balance);
			userinfo.setWangdian(wangdian);
			return userinfo;
		}
		return null;
		
	}
	
	//缴费信息
	public static List<HousingJiAnPay> paydetails_parse(String html,TaskHousing taskHousing){
		List<HousingJiAnPay> list = new ArrayList<HousingJiAnPay>();
		Document doc1 = Jsoup.parse(html);
		Elements ele1 = doc1.select("tbody.tableBody > tr");
		if (ele1.size()>0){
			for (int i = 0;i < ele1.size()-1;i++){
				String jndate = ele1.get(i).select("td").eq(0).text().trim();             //业务日期
				String abstracts = ele1.get(i).select("td").eq(1).text().trim();          //摘要
				String income = ele1.get(i).select("td").eq(2).text().trim();             //收入金额
				String expenditure = ele1.get(i).select("td").eq(3).text().trim();        //支出金额
				HousingJiAnPay pay = new HousingJiAnPay();
				if(jndate.contains(",")&&!jndate.contains("-")){
					String x = jndate;
					SimpleDateFormat sdf1 = new SimpleDateFormat ("MMM dd, yyyy", Locale.UK);
					Date date = null;
					try {
						date = sdf1.parse(x);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
					jndate=sdf.format(date);
				}
				pay.setJndate(jndate);
				pay.setAbstracts(abstracts);
				pay.setIncome(income);
				pay.setExpenditure(expenditure);
				pay.setTaskid(taskHousing.getTaskid());
				list.add(pay);
			}
			return list;
		}
		return null;
		
	}
}
