package app.crawler.htmlparse;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.hangzhou.HousingHangZhouPay;
import com.microservice.dao.entity.crawler.housing.hangzhou.HousingHangZhouPayDetails;
import com.microservice.dao.entity.crawler.housing.hangzhou.HousingHangZhouRecoDetails;
import com.microservice.dao.entity.crawler.housing.hangzhou.HousingHangZhouUserinfo;


public class HousingHZParse {
	//个人信息
	public static HousingHangZhouUserinfo userinfo_parse(String html) {

		HousingHangZhouUserinfo userinfo = new HousingHangZhouUserinfo();

		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("td.TD_data div input");
		Elements eles = doc.select("td.TD_data");
		String cusNumber = eles.get(0).text().trim(); //个人客户号
		String name = eles.get(1).text().trim();      //姓名
		String iDNumber = eles.get(2).text().trim();   //注册人身份证号
		String userName = ele.get(0).attr("value").trim();  //用户名
		String phoneNumber = ele.get(1).attr("value").trim();//手机号码
		String email = ele.get(4).attr("value").trim();      //E-mail
		String address = ele.get(5).attr("value").trim();    //通讯地址
		String postalCode = ele.get(6).attr("value").trim(); //邮政编码	
		userinfo.setCusNumber(cusNumber);
		userinfo.setName(name);
		userinfo.setiDNumber(iDNumber);
		userinfo.setUserName(userName);
		userinfo.setPhoneNumber(phoneNumber);
		userinfo.setEmail(email);
		userinfo.setAddress(address);
		userinfo.setPostalCode(postalCode);
		System.out.println(userinfo.toString());
		return userinfo;
	}
	//缴费信息
	public static HousingHangZhouPay pay_parse(String html){
		HousingHangZhouPay pay = new HousingHangZhouPay();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("table.BStyle_TB td");
		String fundAccount = ele.get(1).text().trim();     //资金账号
		String natureFunds =  ele.get(2).text().trim();     //资金性质
		String depositUnit =  ele.get(3).text().trim();     //缴存单位
		String company =  ele.get(4).text().trim();         //月缴存额单位
		String personal =  ele.get(5).text().trim();        //月缴存额个人
		String total =  ele.get(6).text().trim();           //月缴存额合计
		String depositStatus =  ele.get(7).text().trim();   //缴存状态
		pay.setFundAccount(fundAccount);
		pay.setNatureFunds(natureFunds);
		pay.setDepositUnit(depositUnit);
		pay.setCompany(company);
		pay.setPersonal(personal);
		pay.setTotal(total);
		pay.setDepositStatus(depositStatus);
		System.out.println(pay.toString());
		return pay;
		
	}
	//缴费信息明细
	public static List<HousingHangZhouPayDetails> paydetails_parse(String html){
		List<HousingHangZhouPayDetails> listresult = new ArrayList<>();
		
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("table.BStyle_TB tr");
		int i=0;
		for(Element trele : ele){
			i++;
			if(i==1||i==2){
				continue;
			}
			String date = trele.select("td").get(1).text().trim();        //业务年月
			String type = trele.select("td").get(2).text().trim();       //缴存类型
			String company = trele.select("td").get(3).text().trim();         //月缴存额单位
			String personal = trele.select("td").get(4).text().trim();        //月缴存额个人
			String total = trele.select("td").get(5).text().trim();          //月缴存额合计
			String money = trele.select("td").get(6).text().trim();        //补缴金额
			String accounted = trele.select("td").get(7).text().trim();    //是否入账
			HousingHangZhouPayDetails paydetails = new HousingHangZhouPayDetails();
			paydetails.setDate(date);
			paydetails.setType(type);
			paydetails.setCompany(company);
			paydetails.setPersonal(personal);
			paydetails.setTotal(total);
			paydetails.setMoney(money);
			paydetails.setAccounted(accounted);
			System.out.println(paydetails.toString());
			listresult.add(paydetails);
			
		}
		return listresult;
		
	}
	//个人对账明细
		public static List<HousingHangZhouRecoDetails> recodetails_parse(String html){
			List<HousingHangZhouRecoDetails> listresult = new ArrayList<>();
			
			Document doc = Jsoup.parse(html);
			if (doc.equals("无个人账务信息")){
				return listresult;
			}else{
				Elements ele = doc.select("table.BStyle_TB tr");
				int i=0;
				for(Element trele : ele){
					i++;
					if(i==1){
						continue;
					}
					String date = trele.select("td").get(1).text().trim();        //记账日期
					String abstrac = trele.select("td").get(2).text().trim();     //摘要
					String increase = trele.select("td").get(3).text().trim();    //增加
					String reduce = trele.select("td").get(4).text().trim();      //减少
					String balance = trele.select("td").get(5).text().trim();     //余额
					HousingHangZhouRecoDetails recodetails = new HousingHangZhouRecoDetails();
					recodetails.setDate(date);
					recodetails.setAbstrac(abstrac);
					recodetails.setIncrease(increase);
					recodetails.setReduce(reduce);
					recodetails.setBalance(balance);
					System.out.println(recodetails.toString());
					listresult.add(recodetails);
					
				}
				return listresult;
				
			}
			}
			
	
}
