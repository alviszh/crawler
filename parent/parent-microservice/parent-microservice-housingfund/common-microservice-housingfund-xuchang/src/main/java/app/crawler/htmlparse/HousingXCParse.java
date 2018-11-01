package app.crawler.htmlparse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.housing.xuchang.HousingXuChangUserInfo;

public class HousingXCParse {
	//个人信息
	public static HousingXuChangUserInfo userinfo_parse(String html) {
		HousingXuChangUserInfo userinfo = new HousingXuChangUserInfo();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		String accountCard = object.get("custom").toString().replaceAll("\\\\", "");
		accountCard = accountCard.substring(accountCard.lastIndexOf("[")+1,accountCard.lastIndexOf("]"));
		JsonObject object1 = (JsonObject) parser.parse(accountCard); // 创建JsonObject对象
		String unitCode = object1.get("DWDM").toString().replaceAll("\"", "");           //单位代码
		String companyName = object1.get("DWNAME").toString().replaceAll("\"", "");      //单位名称
		String personalCode = object1.get("GRDM").toString().replaceAll("\"", "");       //个人代码
		String username = object1.get("XM").toString().replaceAll("\"", "");             //姓名
		String idCard = object1.get("SFZID").toString().replaceAll("\"", "");            //身份证号
		String monthlyPayment = object1.get("YJE").toString().replaceAll("\"", "");      //月缴额(元)
		String balance = object1.get("YE").toString().replaceAll("\"", "");              //余额(元)
		String years = object1.get("GRJZNY").toString().replaceAll("\"", "");            //缴至年月
		String state = object1.get("GRZT").toString().replaceAll("\"", "");              //账户状态
		
		userinfo.setUnitCode(unitCode);
		userinfo.setCompanyName(companyName);
		userinfo.setPersonalCode(personalCode);
		userinfo.setUsername(username);
		userinfo.setIdCard(idCard);
		userinfo.setMonthlyPayment(monthlyPayment);
		userinfo.setBalance(balance);
		userinfo.setYears(years);
		userinfo.setState(state);
		return userinfo;
		
	}
}
