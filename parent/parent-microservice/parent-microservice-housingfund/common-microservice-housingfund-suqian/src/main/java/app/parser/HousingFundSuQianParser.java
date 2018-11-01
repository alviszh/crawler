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
import com.microservice.dao.entity.crawler.housing.suqian.HousingSuQianPaydetails;
import com.microservice.dao.entity.crawler.housing.suqian.HousingSuQianUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Component
public class HousingFundSuQianParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundSuQianParser.class);
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
	public HousingSuQianUserInfo htmlUserInfoParser(String html, TaskHousing taskHousing) {
		HousingSuQianUserInfo userInfo = new HousingSuQianUserInfo();			                                   			
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
				
				String companyTel ="";
				if (!listArrayObjs.getString("DWDH").equals("null")) {
					companyTel=	listArrayObjs.getString("DWDH");
				}			
				String companyAccount = listArrayObjs.getString("DWZH");
				String staffName = listArrayObjs.getString("ORGNAME");
				String userAccount = listArrayObjs.getString("GRZH");
				String username= listArrayObjs.getString("XM");
				String cardbankName="";
				if (!listArrayObjs.getString("LMKYH").equals("null")) {
					cardbankName=listArrayObjs.getString("LMKYH");
				}							
				String cardbankNum="";				
				if (!listArrayObjs.getString("LMKH").equals("null")) {
					cardbankNum=listArrayObjs.getString("LMKH");//联名卡号
				}
				String monthpayAmount = listArrayObjs.getString("YJE");
				String balance = listArrayObjs.getString("GRZHYE");
				String openDate = listArrayObjs.getString("KHRQ");
				String firstpayMonth= listArrayObjs.getString("TQLAST");
				String lastPaymonth = listArrayObjs.getString("QJNY");
				String payBase= listArrayObjs.getString("GZJS");
				String state = listArrayObjs.getString("GRZHZTMC");
				String companyRatio = listArrayObjs.getString("DWJJL");
				String personRatio = listArrayObjs.getString("ZGJJL");
				String supplyRatio = listArrayObjs.getString("BTBL");
				String isLoan = listArrayObjs.getString("SFDK");// 是否贷款
				String isMortgage= listArrayObjs.getString("SFDY");// 是否抵押
				String idtype= listArrayObjs.getString("SFDY");
				String idnum= listArrayObjs.getString("ZJHM");
				
				String telephone="";
				if (!listArrayObjs.getString("SJH") .equals("null")) {
					telephone=listArrayObjs.getString("SJH");//手机号
				}				
				String isFreeze= listArrayObjs.getString("SFDJ");// 是否冻结
				String isDeduction= listArrayObjs.getString("SFAYDK");// 是否按月抵扣
				String manageBank= listArrayObjs.getString("JBYH");// 经办银行
				String familyAddress=listArrayObjs.getString("JTZZ");// 家庭住址
				String workAddress=listArrayObjs.getString("DWDZ");// 单位地址		
				userInfo = new HousingSuQianUserInfo(username, userAccount, idtype, idnum, telephone, companyTel,
						companyAccount, companyName, cardbankName, cardbankNum, staffName, manageBank, state, openDate,
						firstpayMonth, lastPaymonth, companyRatio, personRatio, supplyRatio, payBase, monthpayAmount,
						balance, isLoan, isMortgage, isFreeze, isDeduction, familyAddress, workAddress,
						taskHousing.getTaskid());
			}			
		}
		return userInfo;
	}
	// 解析缴费信息
	public List<HousingSuQianPaydetails> htmlPaydetailsParser(String html, TaskHousing taskHousing) {
		List<HousingSuQianPaydetails> paydetails = new ArrayList<HousingSuQianPaydetails>();
		if (null != html && html.contains("dataset")) {
			JSONObject list1ArrayObjs = JSONObject.fromObject(html);
			String codeStr = list1ArrayObjs.getString("code");
			if ("0".endsWith(codeStr)) {
				String listStr = list1ArrayObjs.getString("dataset");
				JSONObject dateObjs = JSONObject.fromObject(listStr);
				String dataStr = dateObjs.getString("rows");
				JSONArray listArray = JSONArray.fromObject(dataStr);
				for (int i = 0; i < listArray.size(); i++) {
					JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(i));
					String rnum = listArrayObjs.getString("ROWNUM_");
					String confirmDate = listArrayObjs.getString("QRRQ");
					String type = listArrayObjs.getString("YWLXMC");
					String accountDate = listArrayObjs.getString("HJNY");
					String increaseAmount = "";
					if (!listArrayObjs.getString("SR").equals("null")) {
						increaseAmount = listArrayObjs.getString("SR");
					}
					String reduceAmount = "";
					if (!listArrayObjs.getString("ZC").equals("null")) {
						reduceAmount = listArrayObjs.getString("ZC");
					}
					String balance = listArrayObjs.getString("GRZHYE");
					HousingSuQianPaydetails paydetail = new HousingSuQianPaydetails();
					paydetail.setRnum(rnum);
					paydetail.setConfirmDate(confirmDate);
					paydetail.setType(type);
					paydetail.setAccountDate(accountDate);
					if (increaseAmount != null) {
						paydetail.setIncreaseAmount(increaseAmount);
					} else {
						paydetail.setIncreaseAmount("");
					}
					if (reduceAmount != null) {
						paydetail.setReduceAmount(reduceAmount);
					} else {
						paydetail.setReduceAmount("");
					}
					paydetail.setBalance(balance);
					paydetail.setTaskid(taskHousing.getTaskid());
					paydetails.add(paydetail);
				}
			}
		}
		return paydetails;
	}
}
