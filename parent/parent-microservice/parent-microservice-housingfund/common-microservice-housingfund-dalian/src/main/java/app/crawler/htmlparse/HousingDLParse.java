package app.crawler.htmlparse;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.housing.dalian.HousingDaLianPay;
import com.microservice.dao.entity.crawler.housing.dalian.HousingDaLianUserinfo;

public class HousingDLParse {
	//个人信息
	public static HousingDaLianUserinfo userinfo_parse(String html,String html1) {
		HousingDaLianUserinfo userinfo = new HousingDaLianUserinfo();
		Document doc = Jsoup.parse(html);
		Elements ele1 = doc.select("div.col-md-3 input");
		String personalAccount = null;         //个人账号
		String name = null;                    //个人姓名
		String idCard = null;                  //证件号码
		String unitAccount = null;             //单位账号
		String unitName = null;                //单位名称
		String mechanism = null;               //所属机构
		String jointNameCard = null;           //联名卡卡号
		String state = null;                   //个人账户状态描述
		String balance = null;                 //公积金可用余额
		String openAnAccount = null;           //开户日期
		String monthlyRemittance = null;       //公积金月汇缴额
		String base = null;                    //公积金缴存基数
		String annualRemittance = null;        //公积金年汇缴额
		String extract = null;                 //公积金年提取额
		
		if (ele1.size()>0){
			personalAccount = ele1.get(1).attr("value").trim();
			name = ele1.get(2).attr("value").trim();
			idCard = ele1.get(4).attr("value").trim();
			unitAccount = ele1.get(5).attr("value").trim();
			unitName = ele1.get(6).attr("value").trim();
			mechanism = ele1.get(7).attr("value").trim();
			jointNameCard = ele1.get(9).attr("value").trim();
			state = ele1.get(10).attr("value").trim();
			balance = ele1.get(11).attr("value").trim();
			openAnAccount = ele1.get(23).attr("value").trim();
			monthlyRemittance = ele1.get(25).attr("value").trim();
			base = ele1.get(27).attr("value").trim();
			annualRemittance = ele1.get(49).attr("value").trim();
			extract = ele1.get(55).attr("value").trim();
		}
		
		
		Document doc1 = Jsoup.parse(html1);
		Elements ele = doc1.select("div.form-group");
		String sex = null;               //性别
		String homeAddress = null;       //家庭住址
		String birth = null;             //出生日期   
		String maritalStatus = null;     //婚姻状况
		String phone = null;             //手机号码
	    String email = null;             //电子邮箱
	    
		if(ele.size()>0){
			sex = ele.get(1).text().trim();
			if (sex.length()>3){
				sex = sex.substring(sex.indexOf("：")+1);
				//System.out.println("sex"+sex);
			}else{
				sex = null;
			}
			
			homeAddress = ele.get(3).text().trim();
			if (homeAddress.length()>5){
				homeAddress = homeAddress.substring(homeAddress.indexOf("：")+1);
				//System.out.println("homeAddress"+homeAddress);
			}else{
				homeAddress = null;
			}
			
			
			birth = ele.get(7).text().trim();
			if (birth.length()>5){
				birth = birth.substring(birth.indexOf("：")+1);
				//System.out.println("birth"+birth);
			}else{
				birth = null;
			}
			
			
			maritalStatus = ele.get(8).text().trim();
			if (maritalStatus.length()>5){
				maritalStatus = maritalStatus.substring(maritalStatus.indexOf("：")+1);
				//System.out.println("maritalStatus"+maritalStatus);
			}else{
				maritalStatus = null;
			}
			
			phone  = ele.get(12).text().trim();
			if (phone.length()>5){
				phone = phone.substring(phone.indexOf("：")+1);
				//System.out.println("phone"+phone);
			}else{
				phone = null;
			}
			
			email  = ele.get(13).text().trim();
			if (email.length()>5){
				email = email.substring(email.indexOf("：")+1);
				System.out.println("email"+email);
			}else{
				email = null;
			}
		}
		userinfo.setPersonalAccount(personalAccount);
		userinfo.setName(name);
		userinfo.setIdCard(idCard);
		userinfo.setUnitAccount(unitAccount);
		userinfo.setUnitName(unitName);
		userinfo.setMechanism(mechanism);
		userinfo.setJointNameCard(jointNameCard);
		userinfo.setState(state);
		userinfo.setBalance(balance);
		userinfo.setOpenAnAccount(openAnAccount);
		userinfo.setMonthlyRemittance(monthlyRemittance);
		userinfo.setBase(base);
		userinfo.setAnnualRemittance(annualRemittance);
		userinfo.setExtract(extract);
		userinfo.setSex(sex);
	    userinfo.setHomeAddress(homeAddress);
	    userinfo.setBirth(birth);
	    userinfo.setMaritalStatus(maritalStatus);
	    userinfo.setPhone(phone);
	    userinfo.setEmail(email);
		return userinfo;
	}
	
	public static List<HousingDaLianPay> paydetails_parse(String html){
		List<HousingDaLianPay> list = new ArrayList<HousingDaLianPay>();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		JsonArray accountCardList = object.get("data").getAsJsonArray();
		for (JsonElement acc : accountCardList) {
			JsonObject account = acc.getAsJsonObject();
//			String jyrq = account.get("jyrq").toString().replaceAll("\"", "");
//			String jyms = account.get("jyms").toString().replaceAll("\"", "");
//			String jffse = account.get("jffse").toString().replaceAll("\"", "");
//			String dffse = account.get("dffse").toString().replaceAll("\"", "");
//			String yue = account.get("yue").toString().replaceAll("\"", "");
//			String dwmc1 = account.get("dwmc1").toString().replaceAll("\"", "");
//			String qsrq = account.get("qsrq").toString().replaceAll("\"", "");
//			String zzrq = account.get("zzrq").toString().replaceAll("\"", "");
//			String jylxms = account.get("jylxms").toString().replaceAll("\"", "");
//			String qdlyms = account.get("qdlyms").toString().replaceAll("\"", "");
			
			String paytime = account.get("jyrq").toString().replaceAll("\"", "");                 //交易日期
			String remark = account.get("jyms").toString().replaceAll("\"", "");                  //摘要
			String debit = account.get("jffse").toString().replaceAll("\"", "");                   //借方发生额
			String credit = account.get("dffse").toString().replaceAll("\"", "");                  //贷方发生额
			String balance = account.get("yue").toString().replaceAll("\"", "");                 //余额
			String unitName = account.get("dwmc1").toString().replaceAll("\"", "");                //单位名称
			String startDate = account.get("qsrq").toString().replaceAll("\"", "");               //起始日期
			String terminationDate = account.get("zzrq").toString().replaceAll("\"", "");        //终止日期
			String tradingState = account.get("jylxms").toString().replaceAll("\"", "");           //交易状态
			String handle = account.get("qdlyms").toString().replaceAll("\"", "");                  //办理方式
			HousingDaLianPay pay = new HousingDaLianPay();
			pay.setPaytime(paytime);
			pay.setRemark(remark);
			pay.setDebit(debit);
			pay.setCredit(credit);
			pay.setBalance(balance);
			pay.setUnitName(unitName);
			pay.setStartDate(startDate);
			pay.setTerminationDate(terminationDate);
			pay.setTradingState(tradingState);
			pay.setHandle(handle);
			list.add(pay);
		}
		return list;
		
	}
}
