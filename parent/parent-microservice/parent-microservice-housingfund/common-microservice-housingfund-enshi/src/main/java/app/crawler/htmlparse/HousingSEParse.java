package app.crawler.htmlparse;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.housing.enshi.HousingEnShiPay;
import com.microservice.dao.entity.crawler.housing.enshi.HousingEnShiUserInfo;

public class HousingSEParse {
	//个人信息
	public static HousingEnShiUserInfo userinfo_parse(String html){
		HousingEnShiUserInfo userInfo = new HousingEnShiUserInfo();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
//		System.out.println(object);
		JsonArray accountCardList = object.get("JBXX").getAsJsonArray();
		
		for (JsonElement acc : accountCardList) {
			JsonObject account = acc.getAsJsonObject();
			String name =  account.get("ZGXM").toString().replaceAll("\"", "").trim();             //职工姓名
			String personalAccount =  account.get("GRZH").toString().replaceAll("\"", "").trim();  //个人帐号
			String company =  account.get("GZDW").toString().replaceAll("\"", "").trim();          //单位名称
			String companyAccount =  account.get("DWZH").toString().replaceAll("\"", "").trim();   //单位帐号
			String fee =  account.get("ZHYE").toString().replaceAll("\"", "").trim();              //账户余额（元）
			String companyAmount =  account.get("DWJCE").toString().replaceAll("\"", "").trim();    //单位缴存额（元）
			String personalAmount =  account.get("GRJCE").toString().replaceAll("\"", "").trim();   //个人缴存额（元）
			String mode =  account.get("DJFS").toString().replaceAll("\"", "").trim();             //冻结方式
			String guarantee =  account.get("DBJE").toString().replaceAll("\"", "").trim();        //担保金额（元）
			String state =  account.get("ZHZT").toString().replaceAll("\"", "").trim();            //账户状态
			String date =  account.get("KHNY").toString().replaceAll("\"", "").trim();             //开户年月
			String lastDate =  account.get("JZNY").toString().replaceAll("\"", "").trim();         //缴至年月
			userInfo.setName(name);
			userInfo.setPersonalAccount(personalAccount);
			userInfo.setCompany(company);
			userInfo.setCompanyAccount(companyAccount);
			userInfo.setFee(fee);
			userInfo.setCompanyAmount(companyAmount);
			userInfo.setPersonalAmount(personalAmount);
			userInfo.setMode(mode);
			userInfo.setGuarantee(guarantee);
			userInfo.setState(state);
			userInfo.setDate(date);
			userInfo.setLastDate(lastDate);
		}
		return userInfo;
		
	}
	
	//缴费流水
	public static List<HousingEnShiPay> paydetails_parse(String html){
		List<HousingEnShiPay> list = new ArrayList<HousingEnShiPay>();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
//		System.out.println(object);
		JsonArray accountCardList = object.get("JCMX").getAsJsonArray();
		
		for (JsonElement acc : accountCardList) {
			JsonObject account = acc.getAsJsonObject();
			String years =  account.get("RQ").toString().replaceAll("\"", "").trim();            //日期
			String abstra =  account.get("ZY").toString().replaceAll("\"", "").trim();           //摘要
			String expenditure =  account.get("FSE").toString().replaceAll("\"", "").trim();      //发生额（元）
			String balance =  account.get("YE").toString().replaceAll("\"", "").trim();          //余额(元)
			HousingEnShiPay pay = new HousingEnShiPay();
			pay.setYears(years);
			pay.setAbstra(abstra);
			pay.setExpenditure(expenditure);
			pay.setBalance(balance);
			list.add(pay);
		}
		return list;
		
	}
}
