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
import com.microservice.dao.entity.crawler.housing.luoyang.HousingLuoYangDetailed;
import com.microservice.dao.entity.crawler.housing.luoyang.HousingLuoYangPay;
import com.microservice.dao.entity.crawler.housing.luoyang.HousingLuoYangUserInfo;

public class HousingLYParse {
	//个人信息
	public static HousingLuoYangUserInfo userinfo_parse(String html) {
		HousingLuoYangUserInfo userinfo = new HousingLuoYangUserInfo();
		Document doc = Jsoup.parse(html);
		
		String name = null;             //姓名
		String idCard = null;           //身份证号
		String personalAccount = null;  //个人帐号
		String phone = null;            //电话号码
		String company = null;          //单位名称
		String unitAccount = null;      //单位帐号
		String administration = null;   //缴存管理部
		String depositBank = null;      //缴存银行
		String proportion = null;       //缴存比例
		String base = null;             //工资基数
		String amountPaid = null;       //月汇缴额	
		String fee = null;              //缴存余额
		String state = null;            //账户状态
		String date = null;             //开户日期
		String years = null;            //缴至年月
		String bank = null;             //绑定银行
		String cardNumber = null;       //绑定银行卡号
		Elements ele = doc.select("div.glname");
		if (ele.size()>0){
			name = ele.text().trim();
		}
		Elements ele1 = doc.select("div.glitem div");
		if (ele1.size()>0){
			idCard = ele1.get(0).text().trim();
			personalAccount = ele1.get(1).text().trim();
			phone = ele1.get(2).text().trim();
		}
		Elements ele2 = doc.select("#user_info_table > tbody > tr > td");
		if (ele2.size()>0){
			company = ele2.get(1).text().trim();          //单位名称
			unitAccount = ele2.get(3).text().trim();      //单位帐号
			administration = ele2.get(5).text().trim();   //缴存管理部
			depositBank = ele2.get(7).text().trim();      //缴存银行
			proportion = ele2.get(11).text().trim();       //缴存比例
			base = ele2.get(13).text().trim();             //工资基数
			amountPaid = ele2.get(15).text().trim();       //月汇缴额	
			fee = ele2.get(17).text().trim();              //缴存余额
			state = ele2.get(19).text().trim();            //账户状态
			date = ele2.get(21).text().trim();             //开户日期
			years = ele2.get(23).text().trim();            //缴至年月
			bank = ele2.get(25).text().trim();             //绑定银行
			cardNumber = ele2.get(27).text().trim();       //绑定银行卡号
		}
		userinfo.setName(name);
		userinfo.setIdCard(idCard);
		userinfo.setPersonalAccount(personalAccount);
		userinfo.setPhone(phone);
		userinfo.setCompany(company);
		userinfo.setUnitAccount(unitAccount);
		userinfo.setAdministration(administration);
		userinfo.setDepositBank(depositBank);
		userinfo.setProportion(proportion);
		userinfo.setBase(base);
		userinfo.setAmountPaid(amountPaid);
		userinfo.setFee(fee);
		userinfo.setState(state);
		userinfo.setDate(date);
		userinfo.setYears(years);
		userinfo.setBank(bank);
		userinfo.setCardNumber(cardNumber);
		return userinfo;
	}
	
	//个人明细
	public static List<HousingLuoYangDetailed> userinfo_detailed(String html){
		List<HousingLuoYangDetailed> list = new ArrayList<HousingLuoYangDetailed>();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		JsonObject accountCard = object.get("lists").getAsJsonObject();
		JsonObject accountCard1 = accountCard.get("dataList").getAsJsonObject();
		JsonArray accountCardList = accountCard1.get("list").getAsJsonArray();
		for (JsonElement acc : accountCardList) {
			JsonObject account = acc.getAsJsonObject();
			//System.out.println(account);
			String years = account.get("accmnh").toString().replaceAll("\"", "");            //缴存年月
			String time = account.get("acctime").toString().replaceAll("\"", "");             //入账时间
		    String companyName = account.get("corpname").toString().replaceAll("\"", "");      //单位名称
		    String income = account.get("income").toString().replaceAll("\"", "");           //收入(元)
			String expenditure = account.get("outcome").toString().replaceAll("\"", "");      //支出(元)
			String balance = account.get("accbal").toString().replaceAll("\"", "");          //当前余额(元)
		    String type = account.get("remark").toString().replaceAll("\"", "");             //业务类型
		    System.out.println(years);
		    HousingLuoYangDetailed detailed = new HousingLuoYangDetailed();
		    detailed.setYears(years);
		    detailed.setTime(time);
		    detailed.setCompanyName(companyName);
		    detailed.setIncome(income);
		    detailed.setExpenditure(expenditure);
		    detailed.setBalance(balance);
		    detailed.setType(type);
		    list.add(detailed);
		}
		return list;
		
	}
	
	
	//缴存明细
	public static List<HousingLuoYangPay> paydetails_parse(String html){
		List<HousingLuoYangPay> list = new ArrayList<HousingLuoYangPay>();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		JsonObject accountCard = object.get("lists").getAsJsonObject();
		JsonObject accountCard1 = accountCard.get("datalist").getAsJsonObject();
		JsonArray accountCardList = accountCard1.get("list").getAsJsonArray();
		for (JsonElement acc : accountCardList) {
			JsonObject account = acc.getAsJsonObject();
			System.out.println(account);
			String years = account.get("paybmnh").toString().replaceAll("\"", "");            //缴存年月
			String time = account.get("acctime").toString().replaceAll("\"", "");             //入账时间
		    String companyName = account.get("corpname").toString().replaceAll("\"", "");      //单位名称
		    String type = account.get("bustype").toString().replaceAll("\"", "");             //业务类型
		    if (type.contains("1")){
		    	type = "汇缴";
		    }else if(type.contains("2")){
		    	type = "补缴";
		    }
			String personal = account.get("perdepmny").toString().replaceAll("\"", "");         //个人月缴存额(元)
			String company = account.get("corpdepmny").toString().replaceAll("\"", "");          //单位月缴存额(元)
			String total = account.get("depmny").toString().replaceAll("\"", "");              //合计月缴存额(元)
			HousingLuoYangPay pay = new HousingLuoYangPay();
			pay.setYears(years);
			pay.setTime(time);
			pay.setCompanyName(companyName);
			pay.setType(type);
			pay.setPersonal(personal);
			pay.setCompany(company);
			pay.setTotal(total);
			list.add(pay);
		}
		return list;
	}
}
