package app.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.yibin.HousingYibinUserInfo;


@Component
public class HousingFundYinbinParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundYinbinParser.class);
	// 解析用户信息
	public HousingYibinUserInfo htmlUserInfoParser(String html, TaskHousing taskHousing) {
		HousingYibinUserInfo userInfo = new HousingYibinUserInfo();
		if (null != html && html.contains("tablejieguo")) {
			Document doc = Jsoup.parse(html, "utf-8");
			Elements tr = doc.getElementsByClass("tablejieguo");
			String staffAccount = tr.select("td").get(0).text();// 公积金账号
			String username = tr.select("td").get(1).text();// 姓名
			String companyMonthlyPay = tr.select("td").get(2).text();// 单位月缴
			String persionalMonthlyPay = tr.select("td").get(3).text();// 个人月缴
			String monthlyPay = tr.select("td").get(4).text();// 月缴额
			String balance = tr.select("td").get(5).text();// 个人余额
			String companyPayMonth = tr.select("td").get(6).text();// 单位缴至年月
			String persionalPayMonth = tr.select("td").get(7).text();// 个人缴至年月
			String accountState = tr.select("td").get(8).text();// 单位月缴存额
			userInfo = new HousingYibinUserInfo(staffAccount, username, companyMonthlyPay, persionalMonthlyPay,
					monthlyPay, balance, companyPayMonth, persionalPayMonth, accountState, taskHousing.getTaskid());
		}
		return userInfo;
	}
}
