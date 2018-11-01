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
import com.microservice.dao.entity.crawler.housing.xiaogan.HousingXiaoGanPay;
import com.microservice.dao.entity.crawler.housing.xiaogan.HousingXiaoGanUserinfo;

public class HousingXGParse {
    //个人信息
	public static HousingXiaoGanUserinfo userinfo_parse(String html){
		HousingXiaoGanUserinfo userinfo = new HousingXiaoGanUserinfo();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html);
		JsonObject accountCardList = object.get("dataGrxx").getAsJsonObject();
//		if(accountCardList.equals("dwmc")){
			String staffName = accountCardList.get("xingming").toString().replaceAll("\"", "");		//姓名
			String idNum = accountCardList.get("zjhm").toString().replaceAll("\"", "");				//身 份 证
			String companyName = accountCardList.get("dwmc").toString().replaceAll("\"", "");		//单位名称
			String staffNum = accountCardList.get("grzh").toString().replaceAll("\"", "");			//个人公积金帐号
			String balance = accountCardList.get("grzhye").toString().replaceAll("\"", "");			//公积金余额
			String date = accountCardList.get("grjzny").toString().replaceAll("\"", "");			//缴至年月
			userinfo.setStaffName(staffName);
			userinfo.setIdNum(idNum);
			userinfo.setCompanyName(companyName);
			userinfo.setStaffNum(staffNum);
			userinfo.setBalance(balance);
			userinfo.setDate(date);
			return userinfo;
//		}
		
//		return null;
		
	}
	
	
	//缴费信息
	public static List<HousingXiaoGanPay> paydetails_parse(String html){
		List<HousingXiaoGanPay> list = new ArrayList<HousingXiaoGanPay>();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		JsonObject accountCard = object.get("dataYwlsKjny").getAsJsonObject();
		JsonArray accountCardList = accountCard.get("datalist").getAsJsonArray();
//		if(accountCardList.equals("jzrq")){
			for (JsonElement acc : accountCardList) {
				JsonObject account = acc.getAsJsonObject();
				String payDate = account.get("jzrq").toString().replaceAll("\"", "");		//日期
				String type = account.get("GJHTQYWLXMX").toString().replaceAll("\"", "");	//业务类别
				String money = account.get("fse").toString().replaceAll("\"", "");			//金额
				String mark = account.get("zy").toString().replaceAll("\"", "");			//摘要
				HousingXiaoGanPay pay = new HousingXiaoGanPay();
				pay.setPayDate(payDate);
				pay.setType(type);
				pay.setMoney(money);
				pay.setMark(mark);
				list.add(pay);
					
			}	
			return list;
//		}
		
//		return null;
		
	}
}
