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
import com.microservice.dao.entity.crawler.housing.datong.HousingDaTongPaydetails;
import com.microservice.dao.entity.crawler.housing.datong.HousingDaTongUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Component
public class HousingFundDaTongParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundDaTongParser.class);
	// 解析用户信息
	public HousingDaTongUserInfo htmlUserInfoParser(String html, TaskHousing taskHousing) {
		HousingDaTongUserInfo userInfo = new HousingDaTongUserInfo();			                                   			
		if (null != html && html.contains("user_info_table")) {
			Document doc = Jsoup.parse(html, "utf-8"); 
			Element userinfoTable=doc.getElementById("user_info_table");
			if (null !=userinfoTable) {
				 String companyName=userinfoTable.getElementById("corpcode2").text();//单位名称
				 String companyAccount=userinfoTable.getElementById("corpcode").text();;//单位账号
				 String staffName=userinfoTable.getElementById("depname").text();;//缴存管理部
				 String bankname=userinfoTable.getElementById("bkname").text();;//缴存银行
				 String personalRatio=userinfoTable.getElementById("perperscale").text();;//缴存比例（个人）
				 String companyalRatio=userinfoTable.getElementById("percorpscale").text();;//缴存比例（单位）
				 String salaryBase=userinfoTable.getElementById("bkname").text();;//工资基数
				 String perdepmny=userinfoTable.getElementById("perdepmny").text();;//月汇缴额个人
				 String corpdepmny=userinfoTable.getElementById("corpdepmny").text();;//月汇缴额单位
				 String depmny=userinfoTable.getElementById("depmny").text();;//月汇缴额合计
				 String balance=userinfoTable.getElementById("accbal").text();;//缴存余额
				 String state=userinfoTable.getElementById("accstate").text();;//账户状态
				 String opendate=userinfoTable.getElementById("regtime").text();;//开户日期
				 String lastmonth=userinfoTable.getElementById("payendmnh").text();;//缴至年月
				 String bindingBankname=userinfoTable.getElementById("bkcardname").text();;//绑定银行
				 String bindingCardnum=userinfoTable.getElementById("bkcard").text();;//绑定银行卡号
				 userInfo=new HousingDaTongUserInfo( companyName,  companyAccount,  staffName,  bankname,
							 personalRatio,  companyalRatio,  salaryBase,  perdepmny,  corpdepmny,
							 depmny,  balance,  state,  opendate,  lastmonth,  bindingBankname,
							 bindingCardnum,  taskHousing.getTaskid());
			}			
		}
		return userInfo;
	}
	// 解析缴费信息
	public List<HousingDaTongPaydetails> htmlPaydetailsParser(String html, TaskHousing taskHousing) {
		List<HousingDaTongPaydetails> paydetails = new ArrayList<HousingDaTongPaydetails>();
		if (null != html && html.contains("dataList")) {
			JSONObject list1ArrayObjs = JSONObject.fromObject(html);
			String 	codeStr=list1ArrayObjs.getString("success");
			if ("true".endsWith(codeStr)) {
				String listStr = list1ArrayObjs.getString("lists");
				JSONObject dateObjs = JSONObject.fromObject(listStr);		
				String dataStr=dateObjs.getString("dataList");			
				JSONObject listObjs = JSONObject.fromObject(dataStr);
				String  listObjsStr=listObjs.getString("list");
				JSONArray listArray = JSONArray.fromObject(listObjsStr);							
				for (int i = 0; i < listArray.size(); i++) {
					 JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(i));
					  String paydate=listArrayObjs.getString("accmnh");
					  String accountdate=listArrayObjs.getString("acctime");
					  String companyName=listArrayObjs.getString("corpname");
					  String increaseAmount=listArrayObjs.getString("income");
					  String reduceAmount=listArrayObjs.getString("outcome");
					  String balance=listArrayObjs.getString("accbal");
					  String type=listArrayObjs.getString("remark");
					  HousingDaTongPaydetails paydetail=new HousingDaTongPaydetails();
					  paydetail.setPaydate(paydate);
					  paydetail.setAccountdate(accountdate);
					  paydetail.setCompanyName(companyName);					 
					  paydetail.setIncreaseAmount(increaseAmount);
					  paydetail.setReduceAmount(reduceAmount);
					  paydetail.setBalance(balance);
					  paydetail.setType(type);
					  paydetail.setTaskid(taskHousing.getTaskid());
					  paydetails.add(paydetail);
				}
			}		
		}
		return paydetails;
	}
}
