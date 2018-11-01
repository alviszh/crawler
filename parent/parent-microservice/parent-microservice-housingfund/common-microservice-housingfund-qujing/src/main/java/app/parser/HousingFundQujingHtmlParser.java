package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.qujing.HousingQujingPayDetails;
import com.microservice.dao.entity.crawler.housing.qujing.HousingQujingUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class HousingFundQujingHtmlParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundQujingHtmlParser.class);
	// 解析用户信息
	public HousingQujingUserInfo htmlUserInfoParser(String html, TaskHousing taskHousing) {
		HousingQujingUserInfo userInfo = new HousingQujingUserInfo();
		if (null != html && html.contains("result")) {
			JSONObject jsonObject = JSONObject.fromObject(html);
			String result = jsonObject.getString("result");
			JSONArray listArray = JSONArray.fromObject(result);
			String staffAccount = JSONObject.fromObject(listArray.get(0)).getString("info");// 个人公积金账号
			String username = JSONObject.fromObject(listArray.get(1)).getString("info");// 姓名
		
			String idnum = JSONObject.fromObject(listArray.get(2)).getString("info");// 证件号码
			String phone = JSONObject.fromObject(listArray.get(3)).getString("info");// 手机号码
			String lastpaydate = JSONObject.fromObject(listArray.get(4)).getString("info");// 最后汇缴月
			String lastdrawdate = JSONObject.fromObject(listArray.get(5)).getString("info");// 上次提取日
			String payBase = JSONObject.fromObject(listArray.get(6)).getString("info");// 缴存基数
			String companyProportion = JSONObject.fromObject(listArray.get(7)).getString("info");// 合计单位比例
			String persionProportion = JSONObject.fromObject(listArray.get(8)).getString("info");// 合计个人比例
			String monthpayAmount = JSONObject.fromObject(listArray.get(9)).getString("info");// 月缴额
			String balance = JSONObject.fromObject(listArray.get(10)).getString("info");// 当前余额

			String state = JSONObject.fromObject(listArray.get(11)).getString("info");// 账户状态
			String companyName = JSONObject.fromObject(listArray.get(12)).getString("info");// 单位名称
			String companyAccount = JSONObject.fromObject(listArray.get(13)).getString("info");// 单位账号
			String institutionName = JSONObject.fromObject(listArray.get(14)).getString("info");// 机构
			userInfo = new HousingQujingUserInfo(staffAccount, username, idnum, phone, lastpaydate,
					lastdrawdate, payBase, companyProportion, persionProportion, monthpayAmount, balance, state,
					companyName, companyAccount, institutionName, taskHousing.getTaskid());
		}
		return userInfo;
	}
	// 解析缴存明细信息
	public List<HousingQujingPayDetails> htmlPaydetailsParser(String html, TaskHousing taskHousing) {
		List<HousingQujingPayDetails> paydetails = new ArrayList<HousingQujingPayDetails>();
		if (null != html && html.contains("result")) {			
			JSONObject jsonObject = JSONObject.fromObject(html);
			String result= jsonObject.getString("result");
			JSONArray listArray = JSONArray.fromObject(result);
			for (int i = 0; i < listArray.size(); i++) {
				JSONArray listArrayList = JSONArray.fromObject(listArray.get(i));
				String dealDate = JSONObject.fromObject(listArrayList.get(0)).getString("info");
				String summary = JSONObject.fromObject(listArrayList.get(1)).getString("info");
				String increaseAmount = JSONObject.fromObject(listArrayList.get(2)).getString("info");
				String reduceAmount = JSONObject.fromObject(listArrayList.get(3)).getString("info");
				String balance = JSONObject.fromObject(listArrayList.get(4)).getString("info");
				HousingQujingPayDetails HousingQujingPayDetail = new HousingQujingPayDetails();
				HousingQujingPayDetail.setDealDate(dealDate);
				HousingQujingPayDetail.setSummary(summary);
				HousingQujingPayDetail.setIncreaseAmount(increaseAmount);
				HousingQujingPayDetail.setReduceAmount(reduceAmount);
				HousingQujingPayDetail.setBalance(balance);
				HousingQujingPayDetail.setTaskid(taskHousing.getTaskid());
				paydetails.add(HousingQujingPayDetail);
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
