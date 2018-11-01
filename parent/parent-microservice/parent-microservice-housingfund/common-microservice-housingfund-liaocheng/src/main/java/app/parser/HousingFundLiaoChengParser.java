package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.liaocheng.HousingLiaoChengPaydetails;
import com.microservice.dao.entity.crawler.housing.liaocheng.HousingLiaoChengUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Component
public class HousingFundLiaoChengParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundLiaoChengParser.class);
	// 解析获取用户的grzh，用于用户信息及明细查询
	public String htmlForGzhParser(String html) {
		Document doc = Jsoup.parse(html, "utf-8");
		Element grzhDiv = doc.getElementById("grzh");
		String grzh = "";
		if (grzhDiv != null) {
			grzh = grzhDiv.text();
		}
		return grzh;
	}
	// 解析用户信息
	public HousingLiaoChengUserInfo htmlUserInfoParser(String html, TaskHousing taskHousing) {
		HousingLiaoChengUserInfo userInfo = new HousingLiaoChengUserInfo();			                                   			
		if (null != html && html.contains("dataset")) {
			JSONObject jsonObject = JSONObject.fromObject(html);
			String codeStr = jsonObject.getString("code");
			if ("0".endsWith(codeStr)) {
				String dataStr = jsonObject.getString("dataset");
				JSONObject dateObjs = JSONObject.fromObject(dataStr);
				String dataRows = dateObjs.getString("rows");
				JSONArray listArray = JSONArray.fromObject(dataRows);
				JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(0));
				String companyName = listArrayObjs.getString("DWMC");
				String companyAccount = listArrayObjs.getString("DWZH");
				String staffName = listArrayObjs.getString("ORGNAME");
				String userAccount = listArrayObjs.getString("GRZH");
				String username= listArrayObjs.getString("XM");
				String monthpayAmount = listArrayObjs.getString("YJE");
				String balance = listArrayObjs.getString("GRZHYE");
				String openDate = listArrayObjs.getString("HJNY");
				String lastPaymonth = listArrayObjs.getString("HJNY");
				String payBase= listArrayObjs.getString("GZJS");
				String state = listArrayObjs.getString("GRZHZTMC");
				String companyRatio = listArrayObjs.getString("DWJJL");
				String personRatio = listArrayObjs.getString("ZGJJL");
				userInfo.setCompanyName(companyName);
				userInfo.setCompanyAccount(companyAccount);
				userInfo.setStaffName(staffName);
				userInfo.setUserAccount(userAccount);
				userInfo.setUsername(username);
				userInfo.setMonthpayAmount(monthpayAmount);
				userInfo.setBalance(balance);
				userInfo.setOpenDate(openDate);
				userInfo.setLastPaymonth(lastPaymonth);
				userInfo.setPayBase(payBase);
				userInfo.setState(state);
				userInfo.setCompanyRatio(companyRatio);
				userInfo.setPersonRatio(personRatio);
				userInfo.setTaskid(taskHousing.getTaskid());
			}			
		}
		return userInfo;
	}
	// 解析缴费信息
	public List<HousingLiaoChengPaydetails> htmlPaydetailsParser(String html, TaskHousing taskHousing) {
		List<HousingLiaoChengPaydetails> paydetails = new ArrayList<HousingLiaoChengPaydetails>();
		if (null != html && html.contains("result")) {
			JSONObject list1ArrayObjs = JSONObject.fromObject(html);
			String 	codeStr=list1ArrayObjs.getString("code");
			if ("0".endsWith(codeStr)) {
				String listStr = list1ArrayObjs.getString("dataset");
				JSONObject dateObjs = JSONObject.fromObject(listStr);
				String dataStr=dateObjs.getString("rows");
				JSONArray listArray = JSONArray.fromObject(dataStr);
				for (int i = 0; i < listArray.size(); i++) {
					 JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(i));
					  String rnum=listArrayObjs.getString("RNUM");
					  String confirmDate=listArrayObjs.getString("QRRQ");
					  String type=listArrayObjs.getString("YWLXMC");
					  String accountDate=listArrayObjs.getString("HJNY");
					  String increaseAmount=listArrayObjs.getString("SR");
					  String reduceAmount=listArrayObjs.getString("ZC");
					  String balance=listArrayObjs.getString("GRZHYE");
					  HousingLiaoChengPaydetails paydetail=new HousingLiaoChengPaydetails();
					  paydetail.setRnum(rnum);
					  paydetail.setConfirmDate(confirmDate);
					  paydetail.setType(type);
					  paydetail.setAccountDate(accountDate);
					  paydetail.setIncreaseAmount(increaseAmount);
					  paydetail.setReduceAmount(reduceAmount);
					  paydetail.setBalance(balance);
					  paydetail.setTaskid(taskHousing.getTaskid());
					  paydetails.add(paydetail);
				}
			}		
		}
		return paydetails;
	}
}
