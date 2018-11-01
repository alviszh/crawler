package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.xiamen.HousingXiamenPaydetails;
import com.microservice.dao.entity.crawler.housing.xiamen.HousingXiamenUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Component
public class HousingFundXiamenParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundXiamenParser.class);
	// 解析用户信息
	public HousingXiamenUserInfo htmlUserInfoParser(String html, TaskHousing taskHousing) {
		HousingXiamenUserInfo userInfo = new HousingXiamenUserInfo();
		if (null != html && html.contains("list")) {
			JSONObject list1ArrayObjs = JSONObject.fromObject(html);
			String listStr = list1ArrayObjs.getString("list");
			JSONArray listArray = JSONArray.fromObject(listStr);
			JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(0));
			String accountnum = listArrayObjs.getString("custAcct");
			String openbank = listArrayObjs.getString("aaa103");
			String username = listArrayObjs.getString("custName");
			String bankname = listArrayObjs.getString("bankOrgName");
			String opendate = listArrayObjs.getString("openDate");
			String compname = listArrayObjs.getString("compName");
			String acctstatus = listArrayObjs.getString("acctStatus");
			String balance = listArrayObjs.getString("bal");
			userInfo.setAccountnum(accountnum);
			userInfo.setOpenbank(openbank);
			userInfo.setUsername(username);
			userInfo.setBankname(bankname);
			userInfo.setOpendate(opendate);
			userInfo.setCompname(compname);
			userInfo.setAcctstatus(acctstatus);
			userInfo.setBalance(balance);
			userInfo.setTaskid(taskHousing.getTaskid());
		}
		return userInfo;
	}
	// 解析缴存明细信息
	public List<HousingXiamenPaydetails> htmlPaydetailsParser(String html, TaskHousing taskHousing) {
		List<HousingXiamenPaydetails> paydetails = new ArrayList<HousingXiamenPaydetails>();
		if (null != html && html.contains("list")) {
			JSONObject list1ArrayObjs = JSONObject.fromObject(html);
			String listStr=list1ArrayObjs.getString("list");
			JSONArray listArray=JSONArray.fromObject(listStr); 
			for (int i = 0; i < listArray.size(); i++) {
				 JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(i));
				  String bankdate=listArrayObjs.getString("bankAcctDate");
				  String banksumy=listArrayObjs.getString("bankSumy");
				  String fixamount=listArrayObjs.getString("creditFixAmt");
				  String saveamount=listArrayObjs.getString("creditSaveAmt");
				  String balance=listArrayObjs.getString("bal");
				  HousingXiamenPaydetails paydetail=new HousingXiamenPaydetails();
				  paydetail.setBankdate(bankdate);
				  paydetail.setBanksumy(banksumy);
				  paydetail.setFixamount(fixamount);
				  paydetail.setSaveamount(saveamount);
				  paydetail.setBalance(balance);
				  paydetail.setTaskid(taskHousing.getTaskid());
				  paydetails.add(paydetail);
			}
		}
		return paydetails;
	}
}
