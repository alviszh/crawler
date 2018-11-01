package app.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.suizhou.HousingSuiZhouPaydetails;
import com.microservice.dao.entity.crawler.housing.suizhou.HousingSuiZhouUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Component
public class HousingFundSuiZhouParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundSuiZhouParser.class);
	//用正则表达式取括号内的内容
	public  String dateListParser(String textStr) {
		String text=null;
		String regex = "(?<=\\().*(?=\\))";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(textStr);
        while (m.find()) {
        	text=m.group();
        }
        return text;
	}
	// 解析用户信息
	public HousingSuiZhouUserInfo htmlUserInfoParser(String html, TaskHousing taskHousing) {
		HousingSuiZhouUserInfo userInfo = new HousingSuiZhouUserInfo();
		if (null != html && html.contains("grjbxxResult")) {
			JSONObject list1ArrayObjs = JSONObject.fromObject(html);
			String codeStr = list1ArrayObjs.getString("success");
			if ("true".endsWith(codeStr)) {
				String dataStr = list1ArrayObjs.getString("grjbxxResult");
				JSONArray listArray = JSONArray.fromObject(dataStr);
				JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(0));		
				String username = listArrayObjs.getString("xingming");
				String idnum = listArrayObjs.getString("zjhm");
				String companyName = listArrayObjs.getString("dwmc");
				String fundNum = listArrayObjs.getString("grzh");
				String banlance = listArrayObjs.getString("grzhye");
				String state = listArrayObjs.getString("grzhztc");
				String incomeBase = listArrayObjs.getString("grjcjs");
				String lastMonth = listArrayObjs.getString("grjzny");
				String telephone = listArrayObjs.getString("sjhm");
				userInfo = new HousingSuiZhouUserInfo(username, idnum, companyName, fundNum, banlance, state,
						incomeBase, lastMonth, telephone, taskHousing.getTaskid());
			}
		}
		return userInfo;
	}
	// 解析缴费信息
	public List<HousingSuiZhouPaydetails> htmlPaydetailsParser(String html, TaskHousing taskHousing) {
		List<HousingSuiZhouPaydetails> paydetails = new ArrayList<HousingSuiZhouPaydetails>();
		if (null != html && html.contains("datalist")) {
			JSONObject list1ArrayObjs = JSONObject.fromObject(html);
			String codeStr = list1ArrayObjs.getString("success");
			if ("true".endsWith(codeStr)) {
				String listStr = list1ArrayObjs.getString("grmxResult");
				JSONObject dateObjs = JSONObject.fromObject(listStr);
				String dataStr = dateObjs.getString("datalist");
				JSONArray listArray = JSONArray.fromObject(dataStr);
				for (int i = 0; i < listArray.size(); i++) {
					JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(i));				
					String date = listArrayObjs.getString("jzrq");
					String type = listArrayObjs.getString("gjhtqywlxc");
					String amount = listArrayObjs.getString("fse");
					String summary = listArrayObjs.getString("zy");
					
					HousingSuiZhouPaydetails paydetail = new HousingSuiZhouPaydetails();
					paydetail.setDate(date);
					paydetail.setType(type);
					paydetail.setAmount(amount);
					paydetail.setSummary(summary);
					paydetail.setTaskid(taskHousing.getTaskid());
					paydetails.add(paydetail);
				}
			}
		}
		return paydetails;
	}
}
