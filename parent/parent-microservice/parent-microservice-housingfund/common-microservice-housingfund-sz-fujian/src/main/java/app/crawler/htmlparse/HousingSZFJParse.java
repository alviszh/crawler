package app.crawler.htmlparse;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.sz.fujian.HousingSZFuJianPay;
import com.microservice.dao.entity.crawler.housing.sz.fujian.HousingSZFuJianUserInfo;

public class HousingSZFJParse {
	//个人信息
	public static HousingSZFuJianUserInfo userinfo_parse(String html){
		HousingSZFuJianUserInfo userinfo = new HousingSZFuJianUserInfo();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("input.srk");
		Elements ele1 = doc.select("table > tbody > tr > td");
		if (ele.size()>0&&ele1.size()>0){
			String name = ele.get(0).attr("value").trim();             //姓名
			String birth = ele.get(1).attr("value").trim();            //出生年月
			String idCard = ele.get(2).attr("value").trim();           //个人证件号
			String phone = ele.get(3).attr("value").trim();            //移动电话
			String comPhone = ele.get(6).attr("value").trim();         //单位电话
			String creditAccumula = ele.get(9).attr("value").trim();   //历年贷方累计
			String fee = ele.get(11).attr("value").trim();              //上年结转余额
			String payDate = ele.get(14).attr("value").trim();			 //缴至年月
			String companyRatio = ele.get(16).attr("value").trim();     //单位缴交率
			String personRatio = ele.get(17).attr("value").trim();      //个人缴交率
			String companyAmount = ele.get(18).attr("value").trim();    //单位应缴额
			String personalAmount = ele.get(19).attr("value").trim();   //职工应缴额
			String monthpay = ele.get(20).attr("value").trim();         //月应缴额
			String personalCard = ele.get(22).attr("value").trim();     //个人公积金账号
			String date = ele.get(23).attr("value").trim();             //开户日期
			String state = ele1.get(53).text().trim();                   //公积金账户状态
			String balance = ele.get(24).attr("value").trim();          //公积金账户余额
			String companyCard = ele.get(26).attr("value").trim();      //单位公积金账号
		    String base = ele.get(29).attr("value").trim();             //缴存基数
			String company = ele1.get(77).text().trim();                //单位名称
			userinfo.setName(name);
			userinfo.setBirth(birth);
			userinfo.setIdCard(idCard);
			userinfo.setPhone(phone);
			userinfo.setComPhone(comPhone);
			userinfo.setCreditAccumula(creditAccumula);
			userinfo.setFee(fee);
			userinfo.setPayDate(payDate);
			userinfo.setCompanyRatio(companyRatio);
			userinfo.setPersonRatio(personRatio);
			userinfo.setCompanyAmount(companyAmount);
			userinfo.setPersonalAmount(personalAmount);
			userinfo.setMonthpay(monthpay);
			userinfo.setPersonalCard(personalCard);
			userinfo.setDate(date);
			userinfo.setState(state);
			userinfo.setBalance(balance);
			userinfo.setCompanyCard(companyCard);
			userinfo.setBase(base);
			userinfo.setCompany(company);
			return userinfo;
		}
		return null;
	}
	
	//缴费信息明细
	public static List<HousingSZFuJianPay> paydetails_parse(String html,TaskHousing taskHousing){
		List<HousingSZFuJianPay> list = new ArrayList<HousingSZFuJianPay>();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("#grdz > tbody >tr");
		if(ele.size()>0){
			for(int i = 1;i <ele.size();i++){
				String number = ele.get(i).select("td").eq(0).text().trim();           //流水号
				String type = ele.get(i).select("td").eq(1).text().trim();             //账户类型
				String account = ele.get(i).select("td").eq(2).text().trim();          //个人账号
				String years = ele.get(i).select("td").eq(3).text().trim();            //发生日期
				String abstra = ele.get(i).select("td").eq(4).text().trim();           //摘要备注
				String occurrence = ele.get(i).select("td").eq(5).text().trim();       //发生金额
				String balance = ele.get(i).select("td").eq(6).text().trim();          //余额
				HousingSZFuJianPay pay = new HousingSZFuJianPay();
				pay.setNumber(number);
				pay.setType(type);
				pay.setAccount(account);
				pay.setYears(years);
				pay.setAbstra(abstra);
				pay.setOccurrence(occurrence);
				pay.setBalance(balance);
				pay.setTaskid(taskHousing.getTaskid());
				list.add(pay);
			}
			return list;
		}
		return null;
	}
	
}
