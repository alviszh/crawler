package app.crawler.htmlparse;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.luzhou.HousingLuZhouPay;
import com.microservice.dao.entity.crawler.housing.luzhou.HousingLuZhouUserInfo;

public class HousingLZParse {
	//个人信息
	public static HousingLuZhouUserInfo userinfo_parse(String html){
		HousingLuZhouUserInfo userInfo = new HousingLuZhouUserInfo();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		//System.out.println(object);
		JsonArray accountCardList = object.get("DATA").getAsJsonArray();
		for (JsonElement acc : accountCardList) {
			JsonObject account = acc.getAsJsonObject();
			String personalAccount =  account.get("GRZH").toString().replaceAll("\"", "");      //个人帐号
			String name =  account.get("XINGMING").toString().replaceAll("\"", "");             //姓名
			String idCard =  account.get("ZJHM").toString().replaceAll("\"", "");               //证件号码
			String sex =  account.get("XINGBIEDISP").toString().replaceAll("\"", "");           //性别
			String birth =  account.get("CSNY").toString().replaceAll("\"", "");                //出生年月
			String fee =  account.get("TOTALBALANCE").toString().replaceAll("\"", "");          //个人账户余额
			String base =  account.get("GRJCJS").toString().replaceAll("\"", "");               //缴存基数
			String companyAmount =  account.get("DWYJCE").toString().replaceAll("\"", "");      //单位月缴存额
			String personalAmount =  account.get("GRYJCE").toString().replaceAll("\"", "");     //个人月缴存额
			String state =  account.get("CURSTATUSDISP").toString().replaceAll("\"", "");       //个人账户状态
			String date =  account.get("KHRQ").toString().replaceAll("\"", "");                 //开户日期
			String phone =  account.get("SJHM").toString().replaceAll("\"", "");                //电话号码
			String company =  account.get("DWMC").toString().replaceAll("\"", "");              //单位名称
			
			userInfo.setPersonalAccount(personalAccount);
			userInfo.setName(name);
			userInfo.setIdCard(idCard);
			userInfo.setSex(sex);
			userInfo.setBirth(birth);
			userInfo.setFee(fee);
			userInfo.setBase(base);
			userInfo.setCompanyAmount(companyAmount);
			userInfo.setPersonalAmount(personalAmount);
			userInfo.setState(state);
			userInfo.setDate(date);
			userInfo.setPhone(phone);
			userInfo.setCompany(company);
		}
		return userInfo;
	
	}
	
	//缴费流水
	public static List<HousingLuZhouPay> paydetails_parse(String html,TaskHousing taskHousing){
		List<HousingLuZhouPay> list = new ArrayList<HousingLuZhouPay>();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		//System.out.println(object);
		JsonArray accountCardList = object.get("rows").getAsJsonArray();
		for (JsonElement acc : accountCardList) {
			JsonObject account = acc.getAsJsonObject();
			String years =  account.get("CALINTERDATE").toString().replaceAll("\"", "");            //日期
			String type =  account.get("PAYNAME").toString().replaceAll("\"", "");             //业务类型
			String abstra =  account.get("ABSTRACT").toString().replaceAll("\"", "");           //摘要
			String income =  account.get("PAYMONEYIN").toString().replaceAll("\"", "");           //收入(元)
			String expenditure =  account.get("PAYMONEYOUT").toString().replaceAll("\"", "");      //支出(元)
			String balance =  account.get("TOTALBALANCE").toString().replaceAll("\"", "");          //余额(元)
			if(income.equals("null")){
				income = "0";
			}
			if(expenditure.equals("null")){
				expenditure = "0";
			}
			if(balance.equals("null")){
				balance = "0";
			}
			HousingLuZhouPay pay = new HousingLuZhouPay();
			pay.setYears(years);
			pay.setType(type);
			pay.setAbstra(abstra);
			pay.setIncome(income);
			pay.setExpenditure(expenditure);
			pay.setBalance(balance);
			pay.setTaskid(taskHousing.getTaskid());
			list.add(pay);
		}
		return list;
		
	}	
}
