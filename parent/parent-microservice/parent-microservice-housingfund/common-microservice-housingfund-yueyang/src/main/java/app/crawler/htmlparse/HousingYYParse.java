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
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.yueyang.HousingYueYangPay;
import com.microservice.dao.entity.crawler.housing.yueyang.HousingYueYangUserinfo;

public class HousingYYParse {
	//个人信息
	public static HousingYueYangUserinfo userinfo_parse(String html){
		HousingYueYangUserinfo userinfo = new HousingYueYangUserinfo();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		JsonObject accountCard = object.get("dataset").getAsJsonObject();
		JsonArray accountCardList = accountCard.get("rows").getAsJsonArray();
		for (JsonElement acc : accountCardList) {
			JsonObject account = acc.getAsJsonObject();    
			String companyNum = account.get("DWZH").toString().replaceAll("\"", "");             //单位账号
			String companyName = account.get("DWMC").toString().replaceAll("\"", "");			 //单位名称
			String personalNum  = account.get("GRZH").toString().replaceAll("\"", "");           //个人账号
			String name = account.get("XM").toString().replaceAll("\"", "");				     //姓名
			String idNum = account.get("ZJHM").toString().replaceAll("\"", "");				     //证件号码
			String telephone = account.get("GDDHHM").toString().replaceAll("\"", "");            //固定电话
			String phone = account.get("SJH").toString().replaceAll("\"", "");                   //手机号码
			String address = account.get("JTDZ").toString().replaceAll("\"", "");                //家庭住址
			String pay = account.get("YJE").toString().replaceAll("\"", "");                     //月缴存额
			String balance = account.get("GRZHYE").toString().replaceAll("\"", "");				 //个人账户余额
			String lastPaytime = account.get("HJNY").toString().replaceAll("\"", "");            //缴至年月
			String base = account.get("GZJS").toString().replaceAll("\"", "");		             //个人缴存基数
			String personalRatio = account.get("ZGJJL").toString().replaceAll("\"", "");         //个人缴存比例
			String companyRatio = account.get("DWJJL").toString().replaceAll("\"", "");          //单位缴存比例
			String state = account.get("GRZHZTMC").toString().replaceAll("\"", "");              //个人账户状态
			String openDate = account.get("KHRQ").toString().replaceAll("\"", "");               //开户日期
			String loan = account.get("SFDK").toString().replaceAll("\"", "");                   //是否贷款
			userinfo.setCompanyNum(companyNum);
			userinfo.setCompanyName(companyName);
			userinfo.setPersonalNum(personalNum);
			userinfo.setName(name);
			userinfo.setIdNum(idNum);
			userinfo.setTelephone(telephone);
			userinfo.setPhone(phone);
			userinfo.setAddress(address);
			userinfo.setPay(pay);
			userinfo.setBalance(balance);
			userinfo.setLastPaytime(lastPaytime);
			userinfo.setBase(base);
			userinfo.setPersonalRatio(personalRatio);
			userinfo.setCompanyRatio(companyRatio);
			userinfo.setState(state);
			userinfo.setOpenDate(openDate);
			userinfo.setLoan(loan);

		}		
		return userinfo;
//		Document doc = Jsoup.parse(html);
//		Elements ele = doc.select("#listView > tbody > tr > td");
//		if(ele.size()>0){
//			String companyNum = ele.get(1).text().trim();             //职工帐号
//			String name = ele.get(3).text().trim();				   //职工姓名
//			String idNum = ele.get(5).text().trim();				   //证件号码
//			String companyName = ele.get(7).text().trim();			   //单位名称
//			String pay = ele.get(9).text().trim();                    //公积金月缴存额
//			String subsidyPay = ele.get(11).text().trim();             //'新职工'补贴月缴存额
//			String balance = ele.get(13).text().trim();				   //公积金余额
//			String subsidyBalance = ele.get(15).text().trim();		   //'新职工'补贴余额
//			String lastPaytime = ele.get(17).text().trim();            //公积金已缴年月
//			String subsidyPaytime = ele.get(19).text().trim();         //'新职工'补贴已缴年月
//			userinfo.setCompanyNum(companyNum);
//			userinfo.setName(name);
//			userinfo.setIdNum(idNum);
//			userinfo.setCompanyName(companyName);
//			userinfo.setPay(pay);
//			userinfo.setSubsidyPay(subsidyPay);
//			userinfo.setBalance(balance);
//			userinfo.setSubsidyBalance(subsidyBalance);
//			userinfo.setLastPaytime(lastPaytime);
//			userinfo.setSubsidyPaytime(subsidyPaytime);
//			return userinfo;
//		}
//		return null;
	}
	
	//缴费信息明细
	public static List<HousingYueYangPay> paydetails_parse(String html,TaskHousing taskHousing){
		List<HousingYueYangPay> list = new ArrayList<HousingYueYangPay>();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		JsonObject accountCard = object.get("dataset").getAsJsonObject();
		JsonArray accountCardList = accountCard.get("rows").getAsJsonArray();
		for (JsonElement acc : accountCardList) {
			JsonObject account = acc.getAsJsonObject();    
			String jndate = account.get("QRRQ").toString().replaceAll("\"", "");                 //记账日期
			String abstracts = account.get("CLLXMC").toString().replaceAll("\"", "");            //归集和提取业务明细类型
			String payDate = account.get("JZNY").toString().replaceAll("\"", "");				 //汇补缴年月
			String pay = account.get("SR").toString().replaceAll("\"", "");                      //收入
			String extract = account.get("ZC").toString().replaceAll("\"", "");			         //支出
			String interest = account.get("LX").toString().replaceAll("\"", "");                 //利息
			String balance = account.get("GRZHYE").toString().replaceAll("\"", "");				 //个人账户余额
			String companyName = account.get("DWMC").toString().replaceAll("\"", "");            //单位名称
			HousingYueYangPay pays = new HousingYueYangPay();
			pays.setJndate(jndate);
			pays.setAbstracts(abstracts);
			pays.setPayDate(payDate);
			pays.setPay(pay);
			pays.setExtract(extract);
			pays.setInterest(interest);
			pays.setBalance(balance);
			pays.setCompanyName(companyName);
			pays.setTaskid(taskHousing.getTaskid());
			list.add(pays);
		}
		return list;
//		Document doc = Jsoup.parse(html);
//		Elements ele = doc.select("#listView > tbody > tr");
//		if(ele.size()>0){
//			for(int i = 2;i<(ele.size()-1);i++){
//				String jndate = ele.get(i).select("td").eq(0).text().trim();                 //日期
//				String abstracts = ele.get(i).select("td").eq(1).text().trim();              //摘要
//				String payDate = ele.get(i).select("td").eq(2).text().trim();				   //汇缴月份
//				String pay = ele.get(i).select("td").eq(3).text().trim();                    //缴存
//				String extract = ele.get(i).select("td").eq(4).text().trim();			       //提取
//				String interest = ele.get(i).select("td").eq(5).text().trim();               //利息
//				String balance = ele.get(i).select("td").eq(6).text().trim();				   //余额
//				String subsidyPay = ele.get(i).select("td").eq(7).text().trim();             //补贴缴存
//				String subsidyExtract = ele.get(i).select("td").eq(8).text().trim();		   //补贴提取
//				String subsidyInterest = ele.get(i).select("td").eq(9).text().trim();		   //补贴利息
//				String subsidyBalance = ele.get(i).select("td").eq(10).text().trim();         //补贴余额
//				String companyNum = ele.get(i).select("td").eq(11).text().trim();             //汇缴单位
//				HousingYueYangPay pays = new HousingYueYangPay();
//				pays.setJndate(jndate);
//				pays.setAbstracts(abstracts);
//				pays.setPayDate(payDate);
//				pays.setPay(pay);
//				pays.setExtract(extract);
//				pays.setInterest(interest);
//				pays.setBalance(balance);
//				pays.setSubsidyPay(subsidyPay);
//				pays.setSubsidyExtract(subsidyExtract);
//				pays.setSubsidyInterest(subsidyInterest);
//				pays.setSubsidyBalance(subsidyBalance);
//				pays.setCompanyNum(companyNum);
//				pays.setTaskid(taskHousing.getTaskid());
//				list.add(pays);
//			}
//			return list;
//		}
//		return null;
	}
}
