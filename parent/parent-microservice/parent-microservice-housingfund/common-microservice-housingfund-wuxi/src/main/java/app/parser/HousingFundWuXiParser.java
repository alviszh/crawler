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
import com.microservice.dao.entity.crawler.housing.wuxi.HousingWuxiPaydetails;
import com.microservice.dao.entity.crawler.housing.wuxi.HousingWuxiUserInfo;


@Component
public class HousingFundWuXiParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundWuXiParser.class);
	// 解析用户信息
	public HousingWuxiUserInfo htmlUserInfoParser(String html, TaskHousing taskHousing) {
		Document doc = Jsoup.parse(html, "utf-8");
		HousingWuxiUserInfo userInfo = new HousingWuxiUserInfo();
		if (null != html && html.contains("listView")) {		
			 Element table= doc.getElementById("listView");
			if (null != table) {
			    Elements tds=table.select("tr").select("td");
				String accountNum = tds.get(2).text();
				String username = tds.get(4).text();
				String idnum = tds.get(6).text();
				String companyName = tds.get(8).text();
				String monthpay = tds.get(10).text();
				String currentBalance = tds.get(14).text();
				String monthpayBase = tds.get(18).text();
				String personalPay = tds.get(22).text();
				String companyPay = tds.get(26).text();
				String lastTimeStr=tds.get(30).toString();
				String[] str = lastTimeStr.split(";");					
				String[] astr = str[0].split("\"");					
				String value1=astr[1];
				String lastTime =value1.substring(0,4)+ "年"+ value1.substring(4,6)+"月";			
				String openTimeStr=tds.get(34).toString();
				String[] str2 = openTimeStr.split(";");					
				String[] astr2 = str2[0].split("\"");					
				String value2=astr2[1];
				String openTime =value2.substring(0,4)+ "年"+ value2.substring(4,6)+"月"+ value2.substring(6,8)+"日";
				
				userInfo.setAccountNum(accountNum);
				userInfo.setUsername(username);
				userInfo.setIdnum(idnum);
				userInfo.setCompanyName(companyName);
				userInfo.setMonthpay(monthpay);
				userInfo.setCurrentBalance(currentBalance);
				userInfo.setMonthpayBase(monthpayBase);
				userInfo.setPersonalPay(personalPay);
				userInfo.setCompanyPay(companyPay);
				userInfo.setLastTime(lastTime);
				userInfo.setOpenTime(openTime);				
				userInfo.setTaskid(taskHousing.getTaskid());
			}
		}
		return userInfo;
	}
	// 解析缴存明细信息
	public List<HousingWuxiPaydetails> htmlPaydetailsParser(String html, TaskHousing taskHousing) {
		Document doc = Jsoup.parse(html, "utf-8");
		List<HousingWuxiPaydetails> paydetails = new ArrayList<HousingWuxiPaydetails>();
		Element div = doc.getElementById("scrollDiv");
		if (null != div) {
			Element table = div.select("table").get(0);
			if (null != table) {
				Elements trs = table.select("tbody").select("tr");
				int trs_size = trs.size();
				if (trs_size > 0) {
					for (int i = 1; i < trs_size; i++) {
						HousingWuxiPaydetails paydetail = new HousingWuxiPaydetails();
						Elements tds = trs.get(i).select("td");
						String companyName = tds.get(0).text();
						String recheckDateStr = tds.get(1).toString();
						String[] arrstr = recheckDateStr.split(";");
						String[] attstr = arrstr[0].split("\"");
						String value = attstr[1];
						String recheckDate = value.substring(0, 4) + "." + value.substring(4, 6) + "."
								+ value.substring(6, 8);

						String serviceMonthStr = tds.get(2).toString();
						String[] arrstr2 = serviceMonthStr.split(";");
						String[] attstr2 = arrstr2[0].split("\"");
						String value2 = attstr2[1];
						String serviceMonth = value2.substring(0, 4) + "." + value.substring(4, 6);
						String remark = tds.get(3).text();
						String income = tds.get(4).text();
						String outcome = tds.get(5).text();
						String currentBalance = tds.get(6).text();
						paydetail.setCompanyName(companyName);
						paydetail.setRecheckDate(recheckDate);
						paydetail.setServiceMonth(serviceMonth);
						paydetail.setRemark(remark);
						paydetail.setIncome(income);
						paydetail.setOutcome(outcome);
						paydetail.setCurrentBalance(currentBalance);
						paydetail.setTaskid(taskHousing.getTaskid());
						paydetails.add(paydetail);
					}
				}
			}

		}

		return paydetails;
	}
}
