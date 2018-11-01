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
import com.microservice.dao.entity.crawler.housing.zhaoqing.HousingZhaoQingPay;
import com.microservice.dao.entity.crawler.housing.zhaoqing.HousingZhaoQingUserinfo;

public class HousingZQParse {
	//个人信息
	public static HousingZhaoQingUserinfo userinfo_parse(String html){
		HousingZhaoQingUserinfo userinfo = new HousingZhaoQingUserinfo();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("#container-4e1e90b7f2af4327b64f9700b3fa54fa > tbody >tr > td >input");
		if(ele.size()>0){
			String companyNum = ele.get(0).attr("value").trim();             //单位账号
			String companyName = ele.get(1).attr("value").trim();			 //单位名称
			String accountNum = ele.get(2).attr("value").trim();             //个人账号
			String name = ele.get(3).attr("value").trim();				     //姓名
			String idNum = ele.get(4).attr("value").trim();				     //证件号码
			String balance = ele.get(19).attr("value").trim();				 //个人账户余额
			String companyRatio = ele.get(11).attr("value").trim();          //单位缴存比例
			String personRatio = ele.get(12).attr("value").trim();           //个人缴存比例
			String companyPay = ele.get(13).attr("value").trim();            //单位月缴存额
			String personPay = ele.get(14).attr("value").trim();             //个人月缴存额
//			System.out.println(name);
//			String state = ele.get(1).attr("value").trim();				     //个人账户状态
//			String openTime = ele.get(1).attr("value").trim();               //开户日期
//		    String lastPaytime = ele.get(1).attr("value").trim();            //最后缴存年月
			if(companyRatio!=null&&!companyRatio.equals("")&&companyPay!=null&&!companyPay.equals("")){
				float   a=Float.parseFloat(companyRatio);
				float   b=Float.parseFloat(companyPay);
//				System.out.println(a);
//				System.out.println(b);
			    String base = String.valueOf(a*b);	                             //个人缴存基数
//			    System.out.println(base);
			    userinfo.setBase(base+"0");
			}
			
		    userinfo.setCompanyNum(companyNum);
		    userinfo.setCompanyName(companyName);
		    userinfo.setAccountNum(accountNum);
		    userinfo.setName(name);
		    userinfo.setIdNum(idNum);
		    userinfo.setBalance(balance);
		    userinfo.setCompanyRatio(companyRatio+"%");
		    userinfo.setPersonRatio(personRatio+"%");
		    userinfo.setCompanyPay(companyPay);
		    userinfo.setPersonPay(personPay);
		   
		    return userinfo;
		}
		return null;
	}
	
	//缴费信息明细
	public static List<HousingZhaoQingPay> paydetails_parse(String html,TaskHousing taskHousing){
		List<HousingZhaoQingPay> listresult = new ArrayList<HousingZhaoQingPay>();
		if(html.contains("accname1")){
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
			JsonObject accountCard = object.get("data").getAsJsonObject();
			JsonArray accountCardList = accountCard.get("data").getAsJsonArray();
			for (JsonElement acc : accountCardList) {
				JsonObject account = acc.getAsJsonObject();
				String jndate = account.get("transdate").toString().replaceAll("\"", "");                  //交易日期
				String companyNum = account.get("unitaccnum1").toString().replaceAll("\"", "");             //单位账号
				String companyName = account.get("unitaccname").toString().replaceAll("\"", "");			   //单位名称
				String accountNum = account.get("accnum1").toString().replaceAll("\"", "");             //个人账号
				String name = account.get("accname1").toString().replaceAll("\"", "");				   //姓名
				String loan = account.get("fundsouflag").toString().replaceAll("\"", "");                   //借贷标志	
				String amount = account.get("amt1").toString().replaceAll("\"", "");                 //发生额	
				String balance = account.get("amt2").toString().replaceAll("\"", "");				   //个人账户余额
				String abstracts = account.get("accname2").toString().replaceAll("\"", "");              //摘要
			
				if(loan.contains("1")){
					loan = "借";
				}else if (loan.contains("2")){
					loan = "贷";
				}
				
				if(abstracts.contains("1000")){
					abstracts = "开户";
				}else if(abstracts.contains("1001")){
					abstracts = "汇缴";
				}else if(abstracts.contains("1002")){
					abstracts = "补缴";
				}else if(abstracts.contains("1003")){
					abstracts = "提取";
				}else if(abstracts.contains("1004")){
					abstracts = "结息";
				}else if(abstracts.contains("1005")){
					abstracts = "封存";
				}else if(abstracts.contains("1006")){
					abstracts = "启封";
				}else if(abstracts.contains("1007")){
					abstracts = "转出";
				}else if(abstracts.contains("1008")){
					abstracts = "转入";
				}else if(abstracts.contains("1009")){
					abstracts = "托管转入";
				}else if(abstracts.contains("1010")){
					abstracts = "托管转出";
				}else if(abstracts.contains("1011")){
					abstracts = "基数调动";
				}else if(abstracts.contains("1012")){
					abstracts = "比例调整";
				}else if(abstracts.contains("1013")){
					abstracts = "销户";
				}else if(abstracts.contains("1014")){
					abstracts = "预缴";
				}else if(abstracts.contains("1015")){
					abstracts = "退款销户";
				}else if(abstracts.contains("1016")){
					abstracts = "差额补缴";
				}else if(abstracts.contains("1900")){
					abstracts = "";
				}
				
				HousingZhaoQingPay pay = new HousingZhaoQingPay();
				pay.setJndate(jndate);
				pay.setCompanyNum(companyNum);
				pay.setCompanyName(companyName);
				pay.setAccountNum(accountNum);
				pay.setName(name);
				pay.setLoan(loan);
				pay.setAmount(amount);
				pay.setBalance(balance);
				pay.setAbstracts(abstracts);
				pay.setTaskid(taskHousing.getTaskid());
				listresult.add(pay);
			}
			return listresult;
		}
		return null;
	}
}
