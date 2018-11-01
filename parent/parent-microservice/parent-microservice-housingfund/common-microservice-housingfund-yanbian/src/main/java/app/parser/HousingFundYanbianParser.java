package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.yanbian.HousingYanbianPaydetails;
import com.microservice.dao.entity.crawler.housing.yanbian.HousingYanbianUserInfo;


@Component
public class HousingFundYanbianParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundYanbianParser.class);
	// 解析用户信息
	public HousingYanbianUserInfo htmlUserInfoParser(String html, TaskHousing taskHousing) {
		HousingYanbianUserInfo userInfo = new HousingYanbianUserInfo();
		if (null != html && html.contains("data")) {
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject
			String success = object.get("success").getAsString();
			if ("true".equals(success)) {
				JsonObject data = object.get("data").getAsJsonObject();
				String username = data.get("姓名").getAsString();
				String idnum = data.get("身份证号码").getAsString();
				String accountNum = data.get("个人账号").getAsString();
				String companyCode = data.get("单位代码").getAsString();
				String sex = data.get("性别").getAsString();
				String lastmonthpay = data.get("上年月平均工资额").getAsString();
				String monthpay = data.get("月应缴额").getAsString();
				String balance = data.get("公积金余额").getAsString();
				String companyPay = data.get("单位缴存额").getAsString();
				String personPay = data.get("个人缴存额").getAsString();
				String companyRatio = data.get("单位缴存比例").getAsString();
				String personRatio = data.get("个人缴存比例").getAsString();
				String depositSituation = data.get("缴存情况").getAsString();
				String companyDepositSituation = data.get("单位缴存情况2").getAsString();
				String openTime = data.get("开户时间").getAsString();
				String lastPaytime = data.get("最后缴交时间").getAsString();
				String maxDefaultperiod = data.get("最大连续违约期数").getAsString();
				String area = data.get("地区名").getAsString();
				String companyName = data.get("单位名称").getAsString();
				userInfo.setUsername(username);
				userInfo.setIdnum(idnum);
				userInfo.setAccountNum(accountNum);
				userInfo.setCompanyCode(companyCode);
				userInfo.setSex(sex);
				userInfo.setLastmonthpay(lastmonthpay);
				userInfo.setMonthpay(monthpay);
				userInfo.setBalance(balance);
				userInfo.setCompanyPay(companyPay);
				userInfo.setPersonPay(personPay);
				userInfo.setCompanyRatio(companyRatio);
				userInfo.setPersonRatio(personRatio);
				userInfo.setDepositSituation(depositSituation);
				userInfo.setCompanyDepositSituation(companyDepositSituation);
				userInfo.setOpenTime(openTime);
				userInfo.setLastPaytime(lastPaytime);
				userInfo.setMaxDefaultperiod(maxDefaultperiod);
				userInfo.setArea(area);
				userInfo.setCompanyName(companyName);
				userInfo.setTaskid(taskHousing.getTaskid());
			}
		}
		return userInfo;
	}
	// 解析缴存明细信息
	public List<HousingYanbianPaydetails> htmlPaydetailsParser(String html, TaskHousing taskHousing) {
		List<HousingYanbianPaydetails> paydetails = new ArrayList<HousingYanbianPaydetails>();
		if (null != html && html.contains("root")) {
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对
			JsonArray jsonArray = object.get("root").getAsJsonArray();
			for (int i = 0; i < jsonArray.size(); i++) {
				HousingYanbianPaydetails paydetail=new HousingYanbianPaydetails();
				JsonObject subObject = jsonArray.get(i).getAsJsonObject();
				String areaName=subObject.get("地区名称").getAsString();
				String payTime=subObject.get("年月").getAsString();
				String companyName=subObject.get("单位名称").getAsString();
				String username=subObject.get("姓名").getAsString();
				String idnum=subObject.get("身份证号码").getAsString();
				String accountNum=subObject.get("个人账号").getAsString();
				String monthpay=subObject.get("月缴存额").getAsString();
				String personPay=subObject.get("个人缴存额").getAsString();
				String companyPay=subObject.get("单位缴存额").getAsString();
				paydetail.setAreaName(areaName);
				paydetail.setPayTime(payTime);
				paydetail.setCompanyName(companyName);
				paydetail.setUsername(username);
				paydetail.setIdnum(idnum);
				paydetail.setAccountNum(accountNum);
				paydetail.setMonthpay(monthpay);
				paydetail.setCompanyPay(companyPay);
				paydetail.setPersonPay(personPay);
				paydetail.setTaskid(taskHousing.getTaskid());
				paydetails.add(paydetail);
			}
		}
		return paydetails;
	}
}
