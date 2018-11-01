package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.mianyang.HousingMianYangPaydetails;
import com.microservice.dao.entity.crawler.housing.mianyang.HousingMianYangUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Component
public class HousingFundMianYangParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundMianYangParser.class);
	// 解析用户信息
	public HousingMianYangUserInfo htmlUserInfoParser(String html, TaskHousing taskHousing) {
		HousingMianYangUserInfo userInfo = new HousingMianYangUserInfo();
		if (null != html && html.contains("DATA")) {
			JSONObject list1ArrayObjs = JSONObject.fromObject(html);
			String listStr = list1ArrayObjs.getString("DATA");
			JSONArray listArray = JSONArray.fromObject(listStr);
			JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(0));
			String personalAccount = listArrayObjs.getString("GRZH");
			String username = listArrayObjs.getString("XINGMING");
			String idtype = listArrayObjs.getString("ZJLXDISP");
			String idnum = listArrayObjs.getString("ZJHM");
			String sex = listArrayObjs.getString("XINGBIEDISP");
			String birthDate = listArrayObjs.getString("CSNY");
			String balance = listArrayObjs.getString("TOTALBALANCE");
			String payBase = listArrayObjs.getString("GRJCJS");
			String companyMonthpayAmount = listArrayObjs.getString("DWYJCE");// 单位月缴存额
			String personMonthpayAmount = listArrayObjs.getString("GRYJCE");// 个人月缴存额
			String financeMonthpayAmount = listArrayObjs.getString("CZYJCE");// 财政月缴存额
			String monthpaySum = listArrayObjs.getString("HJYJCE");// 月缴存额合计
			String state = listArrayObjs.getString("CURSTATUSDISP");// 个人账户状态
			String loanState = listArrayObjs.getString("LOANSTTXT");// 贷款状态
			String freezeState = listArrayObjs.getString("FREEZESTATEDISP");// 冻结状态
			String openDate = listArrayObjs.getString("KHRQ");// 开户日期
			String sealDate = listArrayObjs.getString("FCDATE");// 封存日期
			String startDate = listArrayObjs.getString("BEGINPAYDATE");// 个人起缴年月
			String telphone = listArrayObjs.getString("SJHM");// 手机号码
			userInfo = new HousingMianYangUserInfo(personalAccount, username, idtype, idnum, sex, birthDate, balance,
					payBase, companyMonthpayAmount, personMonthpayAmount, financeMonthpayAmount, monthpaySum, state,
					loanState, freezeState, openDate, sealDate, startDate, telphone, taskHousing.getTaskid());
		}
		return userInfo;
	}
	// 解析缴存明细信息
	public List<HousingMianYangPaydetails> htmlPaydetailsParser(String html, TaskHousing taskHousing) {
		List<HousingMianYangPaydetails> paydetails = new ArrayList<HousingMianYangPaydetails>();
		if (null != html && html.contains("rows")) {
			JSONObject list1ArrayObjs = JSONObject.fromObject(html);
			String listStr=list1ArrayObjs.getString("rows");
			JSONArray listArray=JSONArray.fromObject(listStr); 
			for (int i = 0; i < listArray.size(); i++) {
				 JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(i));
				  String date=listArrayObjs.getString("CALINTERDATE");
				  String type=listArrayObjs.getString("PAYNAME");
				  String summary=listArrayObjs.getString("ABSTRACT");
				  String increaseAmount=listArrayObjs.getString("PAYMONEYIN");
				  String reduceAmount=listArrayObjs.getString("FINANCEMONEY");
				  String balance=listArrayObjs.getString("TOTALBALANCE");
				  HousingMianYangPaydetails paydetail=new HousingMianYangPaydetails();
				  paydetail.setDate(date);
				  paydetail.setType(type);
				  paydetail.setSummary(summary);
				  paydetail.setIncreaseAmount(increaseAmount);
				  paydetail.setReduceAmount(reduceAmount);
				  paydetail.setBalance(balance);
				  paydetail.setTaskid(taskHousing.getTaskid());
				  paydetails.add(paydetail);
			}
		}
		return paydetails;
	}
}
