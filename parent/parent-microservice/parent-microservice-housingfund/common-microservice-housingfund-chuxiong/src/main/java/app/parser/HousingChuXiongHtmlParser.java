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
import com.microservice.dao.entity.crawler.housing.chuxiong.HousingChuXiongPaydetails;
import com.microservice.dao.entity.crawler.housing.chuxiong.HousingChuXiongUserInfo;

@Component
public class HousingChuXiongHtmlParser {
	public static final Logger log = LoggerFactory.getLogger(HousingChuXiongHtmlParser.class);
	// 解析用户信息
	public HousingChuXiongUserInfo htmlUserInfoParser(String html, TaskHousing taskHousing) {
		HousingChuXiongUserInfo userInfo = new HousingChuXiongUserInfo();
		if (null != html && html.contains("查询结果")) {
			Document doc = Jsoup.parse(html, "utf-8");
			Element div = doc.getElementById("UpdatePanel4");
			Element table = div.select("table").first();
			String username = table.select("font").get(0).text();// 姓名
			String companyName = table.select("font").get(2).text();// 证件类型
			String newAccount = table.select("span").get(1).text();// 证件号码
			String oldAccount = table.select("font").get(3).text();// 性别
			String balance = table.select("span").get(2).text();// 个人账户余额
			userInfo = new HousingChuXiongUserInfo(username, companyName, newAccount, oldAccount, balance,
					taskHousing.getTaskid());
		}
		return userInfo;
	}
	// 解析缴存明细信息
	public List<HousingChuXiongPaydetails> htmlPaydetailsParser(String html, TaskHousing taskHousing) {
		List<HousingChuXiongPaydetails> paydetails = new ArrayList<HousingChuXiongPaydetails>();
		if (null != html && html.contains("查询结果")) {
			Document doc = Jsoup.parse(html, "utf-8");
			Element div = doc.getElementById("UpdatePanel4");
			Element table = div.select("table").last();
			if (null != table) {
				Elements trs = table.select("tr");
				int trs_size = trs.size();
				if (trs_size > 1) {
					for (int i = 1; i < trs_size; i++) {
						Elements tds = trs.get(i).select("td");
						HousingChuXiongPaydetails paydetail = new HousingChuXiongPaydetails();
						String date = tds.get(0).text();
						String summary = tds.get(1).text();
						String drawAmount = tds.get(2).text();
						String payAmount = tds.get(3).text();
						String balance = tds.get(4).text();
						paydetail.setDate(date);
						paydetail.setSummary(summary);
						paydetail.setDrawAmount(drawAmount);
						paydetail.setPayAmount(payAmount);
						paydetail.setBalance(balance);
						paydetail.setTaskid(taskHousing.getTaskid());
						paydetails.add(paydetail);
					}
				}
			}
		}
		return paydetails;
	}
}
