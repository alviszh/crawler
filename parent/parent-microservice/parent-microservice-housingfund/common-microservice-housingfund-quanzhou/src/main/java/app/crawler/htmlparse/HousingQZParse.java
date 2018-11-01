package app.crawler.htmlparse;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.quanzhou.HousingQuanZhouPay;
import com.microservice.dao.entity.crawler.housing.quanzhou.HousingQuanZhouPayDetails;



public class HousingQZParse {
	//缴费信息
		public static HousingQuanZhouPay pay_parse(String html){
			HousingQuanZhouPay pay = new HousingQuanZhouPay();
			Document doc = Jsoup.parse(html);
			Elements ele = doc.select("#grid_id tbody td");
			if(ele.size()>0){
				String name = ele.get(0).text().trim();              //姓名
				String idNumber = ele.get(1).text().trim();          //身份证号
				String perAccount = ele.get(2).text().trim();        //个人账号
				String state = ele.get(3).text().trim();             //状态
				String monthSubscr = ele.get(4).text().trim();       //月缴额	
				String balance = ele.get(5).text().trim();           //余额
				String dueDay = ele.get(6).text().trim();            //最后缴交日期
				String base = ele.get(7).text().trim();              //缴交基数
				String proportion = ele.get(8).text().trim();        //缴交比例
				String unitAccount = ele.get(9).text().trim();       //单位账号
				String unitName = ele.get(10).text().trim();          //单位名称
				String date = ele.get(11).text().trim();              //开户日期
				pay.setName(name);
				pay.setIdNumber(idNumber);
				pay.setPerAccount(perAccount);
				pay.setState(state);
				pay.setMonthSubscr(monthSubscr);
				pay.setBalance(balance);
				pay.setDueDay(dueDay);
				pay.setBase(base);
				pay.setProportion(proportion);
				pay.setUnitAccount(unitAccount);
				pay.setUnitName(unitName);
				pay.setDate(date);
				System.out.println(pay.toString());
				return pay;
			}
			return null;
			
		}
		//缴费信息明细
		public static List<HousingQuanZhouPayDetails> paydetails_parse(String html,TaskHousing taskHousing){
			List<HousingQuanZhouPayDetails> listresult = new ArrayList<>();
			
			Document doc = Jsoup.parse(html);
			Elements ele = doc.select("#grid_id tbody tr");
			if (ele.size()>0){
				for(Element trele : ele){
					String perAccount = trele.select("td").get(0).text().trim();       //个人账号
					String date = trele.select("td").get(1).text().trim();              //日期
					String abstrac = trele.select("td").get(2).text().trim();           //摘要
					String withdrSum = trele.select("td").get(3).text().trim();         //支取金额
					String depoBalance = trele.select("td").get(4).text().trim();       //缴存余额	
					String balance = trele.select("td").get(5).text().trim();          //余额
					HousingQuanZhouPayDetails paydetails = new HousingQuanZhouPayDetails();
					paydetails.setPerAccount(perAccount);
					paydetails.setDate(date);
					paydetails.setAbstrac(abstrac);
					paydetails.setWithdrSum(withdrSum);
					paydetails.setDepoBalance(depoBalance);
					paydetails.setBalance(balance);
					paydetails.setTaskid(taskHousing.getTaskid());
					System.out.println(paydetails.toString());
					listresult.add(paydetails);
				}
				return listresult;
			}
			
			return null;
		}
}
