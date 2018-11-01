package app.crawler.htmlparse;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.wuzhou.HousingWuZhouPay;
import com.microservice.dao.entity.crawler.housing.wuzhou.HousingWuZhouUserinfo;

public class HousingTZParse {
	//个人信息
	public static HousingWuZhouUserinfo userinfo_parse(String html,String html1){
		HousingWuZhouUserinfo userinfo = new HousingWuZhouUserinfo();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("body > table:nth-child(3) > tbody > tr:nth-child(2) > td > font:nth-child(1) ");
		Elements eles = doc.select("#zgbh > option");
		Document doc1 = Jsoup.parse(html1);
		Elements eles1 = doc1.select("table.footcollapse > thead > tr");
		if(ele.size()>0&&eles.size()>0&&eles1.size()>0){
			String s = eles.text().trim();
			//System.out.println(s);
			String bian = s.substring(0,s.indexOf("]"));
			String dan = s.substring(s.indexOf("单位:"),s.indexOf("状态:"));
			String zhuang = s.substring(s.indexOf("状态:"));
			
			String staffName = ele.text().trim();						                            //姓名
			String staffNum = bian.substring(bian.indexOf("[")+1);						            //职工编号
			String companyName = dan.substring(dan.indexOf("[")+1,dan.indexOf("]"));	            //单位
			String state = zhuang.substring(zhuang.indexOf("[")+1,zhuang.indexOf("]"));           //状态
			String idNum = eles1.get(1).select("td").text().trim();							//实际身份证号
			String code = eles1.get(2).select("td").text().trim();                            //公积金联名卡号
			String openDate = eles1.get(4).select("td").text().trim();                        //开户日期
			String monthPay = eles1.get(5).select("td").text().trim();						//月存金额
			String personalAmount = eles1.get(6).select("td").text().trim();                  //个人月存额
			String companyAmount = eles1.get(7).select("td").text().trim();                   //单位月存额
			String prevYearBalances = eles1.get(8).select("td").text().trim();                //上年结存金额
			String thisYear = eles1.get(9).select("td").text().trim();                        //本年缴存金额
			String draw = eles1.get(10).select("td").text().trim();                            //本年支取金额
			String principal = eles1.get(11).select("td").text().trim();						//本金金额
			String companyPercent = eles1.get(12).select("td").text().trim();					//单位存缴比例
			String personalPercent = eles1.get(13).select("td").text().trim();				//个人缴存比例
			String companyDate = eles1.get(14).select("td").text().trim();					 //公积金单位部分缴至年月
			String personalDate = eles1.get(15).select("td").text().trim();                    //公积金个人部分缴至年月
			userinfo.setStaffName(staffName);
			userinfo.setStaffNum(staffNum);
			userinfo.setCompanyName(companyName);
			userinfo.setState(state);
			userinfo.setIdNum(idNum);
			userinfo.setCode(code);
			userinfo.setOpenDate(openDate);
			userinfo.setMonthPay(monthPay);
			userinfo.setPersonalAmount(personalAmount);
			userinfo.setCompanyAmount(companyAmount);
			userinfo.setPrevYearBalances(prevYearBalances);
			userinfo.setThisYear(thisYear);
			userinfo.setDraw(draw);
			userinfo.setPrincipal(principal);
			userinfo.setCompanyPercent(companyPercent);
			userinfo.setPersonalPercent(personalPercent);
			userinfo.setCompanyDate(companyDate);
			userinfo.setPersonalDate(personalDate);
			
			return userinfo;
		}
		return null;
		
	}
	
	//缴费信息明细
	public static List<HousingWuZhouPay> paydetails_parse(String html,TaskHousing taskHousing){
		List<HousingWuZhouPay> listresult = new ArrayList<HousingWuZhouPay>();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("table.footcollapse > tbody > tr");
		if(ele.size()>0){
			for(Element trele : ele){
				String payDate = trele.select("td").get(0).text().trim();						//日期
				String mark = trele.select("td").get(1).text().trim();						//摘要
				String markDate = trele.select("td").get(2).text().trim();					//汇交年月
				String money = trele.select("td").get(3).text().trim();					 	//金额
				String balance = trele.select("td").get(4).text().trim();						//余额
				String remarks = trele.select("td").get(5).text().trim();					    //备注
				HousingWuZhouPay pay = new HousingWuZhouPay();
				pay.setPayDate(payDate);
				pay.setMark(mark);
				pay.setMarkDate(markDate);
				pay.setMoney(money);
				pay.setBalance(balance);
				pay.setRemarks(remarks);
				pay.setTaskid(taskHousing.getTaskid());
				listresult.add(pay);
			}
			return listresult;
		}
		return null;
		
	}
}
