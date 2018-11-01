package app.crawler.htmlparse;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.tieling.HousingTieLingPay;
import com.microservice.dao.entity.crawler.housing.tieling.HousingTielingUserInfo;

public class HousingTLParse {

	//个人信息
	public static HousingTielingUserInfo userinfo_parse(String html){
		HousingTielingUserInfo userinfo = new HousingTielingUserInfo();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("td.data");
		if(ele.size()>0){
			String companyName = ele.get(0).text().trim();     //	单位名称
			String companyCode = ele.get(1).text().trim();     //	单位帐号
			String accountNum = ele.get(2).text().trim();      //   个人公积金账号
			String username = ele.get(3).text().trim();        //   姓名
			String mechanism = ele.get(4).text().trim();       //   所属机构
			String state = ele.get(5).text().trim();           //   账户状态
		    String base = ele.get(6).text().trim();            //   缴存基数
		    String companyRatio = ele.get(7).text().trim();    //	单位缴存比例
			String personRatio = ele.get(8).text().trim();     //	个人缴存比例
			String monthpay = ele.get(9).text().trim();        //	月应缴额
			String balance = ele.get(10).text().trim();         //	当前余额
			String deposit = ele.get(11).text().trim();         //   当年缴存额
			String extract = ele.get(12).text().trim();         //   当年提取额
			String monthly = ele.get(13).text().trim();         //   最后汇缴月	
			userinfo.setCompanyName(companyName);
			userinfo.setCompanyCode(companyCode);
			userinfo.setAccountNum(accountNum);
			userinfo.setUsername(username);
			userinfo.setMechanism(mechanism);
			userinfo.setState(state);
			userinfo.setBase(base);
			userinfo.setCompanyRatio(companyRatio);
			userinfo.setPersonRatio(personRatio);
			userinfo.setMonthpay(monthpay);
			userinfo.setBalance(balance);
			userinfo.setDeposit(deposit);
			userinfo.setExtract(extract);
			userinfo.setMonthly(monthly);
			
			
		}
		return userinfo;
		
	}
	//缴费信息明细
	public static List<HousingTieLingPay> recodetails_parse(String html){
		List<HousingTieLingPay> listresult = new ArrayList<HousingTieLingPay>();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("#dataList tr");
		for(int i = 1;i<ele.size();i++){
			String date = ele.get(i).select("td").get(0).text().trim();          //交易日期
			String stract =ele.get(i).select("td").get(1).text().trim();        //摘要
//			if(stract.equals("1001")){
//				stract = "汇缴";
//			}else if(stract.equals("1004")){
//				stract = "结息";
//			}else if(stract.equals("1900")){
//				stract = "年度结转";
//			}
			String forehead = ele.get(i).select("td").get(2).text().trim();      //发生额
			String balance = ele.get(i).select("td").get(3).text().trim();       //余额
			HousingTieLingPay pay = new HousingTieLingPay();
			pay.setDate(date);
			pay.setStract(stract);
			pay.setForehead(forehead);
			pay.setBalance(balance);
			listresult.add(pay);
		}
		return listresult;
		
	}
}
