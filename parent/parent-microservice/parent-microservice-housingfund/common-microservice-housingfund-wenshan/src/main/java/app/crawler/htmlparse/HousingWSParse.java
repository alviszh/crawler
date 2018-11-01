package app.crawler.htmlparse;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.wenshan.HousingWenShanPay;
import com.microservice.dao.entity.crawler.housing.wenshan.HousingWenShanUserinfo;

public class HousingWSParse {
	//个人信息
	public static  HousingWenShanUserinfo userinfo_parse(String html){
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		String staffName = object.get("xingming").toString().replaceAll("\"", "");					//姓名
		String idNum = object.get("zjhm").toString().replaceAll("\"", "");							//证件号码
		String staffNum = object.get("grzh").toString().replaceAll("\"", "");						//个人账号
		String companyName = object.get("dwmc").toString().replaceAll("\"", "");					//单位名称
		String base = object.get("grjcjs").toString().replaceAll("\"", "");                         //个人缴存基数
		String personalAmount = object.get("dwyjce").toString().replaceAll("\"", "");               //个人月缴存额
		String companyAmount = object.get("gryjce").toString().replaceAll("\"", "");                //单位月缴存额
		String monthPay = object.get("yjce").toString().replaceAll("\"", "");						//月缴存额
		String balance = object.get("grzhye").toString().replaceAll("\"", "");						//个人账户余额
		String date = object.get("jzny").toString().replaceAll("\"", "");					        //缴至年月
		String openDate = object.get("khrq").toString().replaceAll("\"", "");                       //开户日期
		String state = object.get("password").toString().replaceAll("\"", "");						//个人账户状态
		String companyPercent = object.get("unitprop").toString().replaceAll("\"", "");				//单位比例
		String personalPercent = object.get("indinorprop").toString().replaceAll("\"", "");         //个人比例
		String phone = object.get("sjhm").toString().replaceAll("\"", "");                          //手机号码
		HousingWenShanUserinfo userinfo = new HousingWenShanUserinfo();
		userinfo.setStaffName(staffName);
		userinfo.setIdNum(idNum);
		userinfo.setStaffNum(staffNum);
		userinfo.setCompanyName(companyName);
		userinfo.setBase(base);
		userinfo.setPersonalAmount(personalAmount);
		userinfo.setCompanyAmount(companyAmount);
		userinfo.setMonthPay(monthPay);
		userinfo.setBalance(balance);
		userinfo.setDate(date);
		userinfo.setOpenDate(openDate);
		userinfo.setState(state);
		userinfo.setCompanyPercent(companyPercent);
		userinfo.setPersonalPercent(personalPercent);
		userinfo.setPhone(phone);
		return userinfo;
		
	}
	
	//缴费流水
	public static List<HousingWenShanPay> paydetails_parse(String html,TaskHousing taskHousing){
		List<HousingWenShanPay> list =new ArrayList<HousingWenShanPay>();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		JsonArray accountCardList = object.get("results").getAsJsonArray();
		for (JsonElement acc : accountCardList) {
			JsonObject account = acc.getAsJsonObject();
			String name = account.get("xingming").toString().replaceAll("\"", "");					//姓名
			String type = account.get("ywlxmc").toString().replaceAll("\"", "");					//业务类型
			String money = account.get("fse").toString().replaceAll("\"", "");					 	//发生额
			String balance = account.get("zhye").toString().replaceAll("\"", "");					//账户余额
			String payDate = account.get("jzrq").toString().replaceAll("\"", "");					//记账日期
			HousingWenShanPay pay = new HousingWenShanPay();
			pay.setName(name);
			pay.setType(type);
			pay.setMoney(money);
			pay.setBalance(balance);
			pay.setPayDate(payDate);
			pay.setTaskid(taskHousing.getTaskid());
			list.add(pay);
		}
		return list;
		
	}
}
