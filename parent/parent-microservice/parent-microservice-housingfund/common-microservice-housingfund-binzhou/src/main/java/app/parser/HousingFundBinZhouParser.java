package app.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.binzhou.HousingBinZhouUserInfo;


@Component
public class HousingFundBinZhouParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundBinZhouParser.class);
	// 解析用户信息
	public HousingBinZhouUserInfo htmlUserInfoParser(String html, TaskHousing taskHousing) {
		Document doc = Jsoup.parse(html, "utf-8");
		HousingBinZhouUserInfo userInfo = new HousingBinZhouUserInfo();		
		if (null != html && html.contains("职工帐号")) {		
			Element table=doc.select("table").last();
			if (null != table) {
			    Elements tds=table.select("tr").select("td");
			    String userAccount = tds.get(1).text();				
				String username = tds.get(3).text();			
				String companyAccount = tds.get(5).text();
				String companyName = tds.get(7).text();
				String idnum = tds.get(9).text();
				String state = tds.get(11).text();
				String lastPaymonth = tds.get(13).text();
				String basemny = tds.get(15).text();
				String payAmount = tds.get(17).text();
				String balance=tds.get(19).text();
				String isFreeze=tds.get(21).text();
				userInfo.setUserAccount(userAccount);
				userInfo.setUsername(username);
				userInfo.setCompanyAccount(companyAccount);
				userInfo.setCompanyName(companyName);
				userInfo.setIdnum(idnum);
				userInfo.setState(state);
				userInfo.setLastPaymonth(lastPaymonth);
				userInfo.setBasemny(basemny);
				userInfo.setPayAmount(payAmount);
				userInfo.setBalance(balance);
				userInfo.setIsFreeze(isFreeze);			
				userInfo.setTaskid(taskHousing.getTaskid());
			}
		}
		return userInfo;
	}
}
