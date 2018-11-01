package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.chenzhou.HousingChenZhouPaydetails;
import com.microservice.dao.entity.crawler.housing.chenzhou.HousingChenZhouUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Component
public class HousingFundChenZhouParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundChenZhouParser.class);
	// 解析用户信息
	public HousingChenZhouUserInfo htmlUserInfoParser(String html, TaskHousing taskHousing) {
		HousingChenZhouUserInfo userInfo =new  HousingChenZhouUserInfo();                             			
		if (null != html && html.contains("个人缴存信息")) {
			Document doc = Jsoup.parse(html, "utf-8");
			String companyname = getNextLabelByKeyword(doc, "单位名称");// 单位名称
			String username = getNextLabelByKeyword(doc, "姓名");// 姓名
			String idnum = getNextLabelByKeyword(doc, "身份证号码");// 身份证号
			String useraccount = getNextLabelByKeyword(doc, "个人账号");// 个人账号
			String companyaccount = getNextLabelByKeyword(doc, "单位账号");// 单位账号
			String monthamount = getNextLabelByKeyword(doc, "月缴额");// 月缴额
			String paymonth = getNextLabelByKeyword(doc, "汇缴年月");// 汇缴年月
			String balance = getNextLabelByKeyword(doc, "公积金金额");// 公积金金额
			String payRatio = getNextLabelByKeyword(doc, "汇缴比例");// 汇缴比例
			String state = getNextLabelByKeyword(doc, "账户状态");// 账户状态
			userInfo.setCompanyname(companyname);
			userInfo.setUsername(username);
			userInfo.setIdnum(idnum);
			userInfo.setUseraccount(useraccount);
			userInfo.setCompanyaccount(companyaccount);
			userInfo.setMonthamount(monthamount);
			userInfo.setPaymonth(paymonth);
			userInfo.setBalance(balance);
			userInfo.setPayRatio(payRatio);
			userInfo.setState(state);
			userInfo.setTaskid(taskHousing.getTaskid());
		}
		return userInfo;
	}
	// 解析缴费信息
	public List<HousingChenZhouPaydetails> htmlPaydetailsParser(String html, TaskHousing taskHousing) {
		List<HousingChenZhouPaydetails> paydetails = new ArrayList<HousingChenZhouPaydetails>();
		if (null != html && html.contains("rows")) {		
				JSONObject dateObjs = JSONObject.fromObject(html);
				String dataStr=dateObjs.getString("rows");
				JSONArray listArray = JSONArray.fromObject(dataStr);
				for (int i = 0; i < listArray.size(); i++) {
					 JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(i));
					  String useraccount=listArrayObjs.getString("spcode");//	个人账号
					  String paymentDate=listArrayObjs.getString("qrrq");//	汇缴日期
					  String type=listArrayObjs.getString("dename");
					  String paymentMonth=listArrayObjs.getString("hjny");
					  String income=listArrayObjs.getString("sr");
					  String expend=listArrayObjs.getString("zc");
					  String balance=listArrayObjs.getString("ye");
					  HousingChenZhouPaydetails paydetail=new HousingChenZhouPaydetails();
					  paydetail.setUseraccount(useraccount);
					  paydetail.setPaymentDate(paymentDate);
					  paydetail.setType(type);
					  paydetail.setPaymentMonth(paymentMonth);
					  paydetail.setIncome(income);
					  paydetail.setExpend(expend);
					  paydetail.setBalance(balance);
					  paydetail.setTaskid(taskHousing.getTaskid());
					  paydetails.add(paydetail);
				}			
		}
		return paydetails;
	}
	
	public static String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("td:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}
}
