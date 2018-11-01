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
import com.microservice.dao.entity.crawler.housing.jinhua.HousingJinHuaPaydetails;
import com.microservice.dao.entity.crawler.housing.jinhua.HousingJinHuaUserInfo;

@Component
public class HousingFundJinhuaHtmlParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundJinhuaHtmlParser.class);
	// 解析用户信息
	public List<HousingJinHuaUserInfo> htmlUserInfoParser(String html, TaskHousing taskHousing) {
		List<HousingJinHuaUserInfo> userInfoList=new ArrayList<HousingJinHuaUserInfo>();
		if (null != html && html.contains("公积金账户信息")) {
			Document doc = Jsoup.parse(html);	
			Elements tbody=doc.select("tbody");
			int size=tbody.size();
			for (int i = 0; i < size; i++) {
				String userAccount=tbody.get(i).select("td").get(1).text();//	公积金账号
				String type=tbody.get(i).select("td").get(3).text();//	账号类型
			    String username=tbody.get(i).select("td").get(5).text();//	姓名
			    String idnum=tbody.get(i).select("td").get(7).text();//	身份证号
			    String companyName=tbody.get(i).select("td").get(9).text();//	单位名称
				String staffName=tbody.get(i).select("td").get(11).text();//	缴存机构
				String personalAmount=tbody.get(i).select("td").get(13).text();//	合计单位比例
				String companyAmount=tbody.get(i).select("td").get(15).text();//	合计个人比例
			    String totalAmount=tbody.get(i).select("td").get(17).text();//	合计月缴额
				String balance=tbody.get(i).select("td").get(19).text();//	账户余额
			
				String state=tbody.get(i).select("td").get(21).text();//	账户状态
				String lastPaymonth=getNextLabelByKeyword(doc,"最后缴交月份");//最后缴交月份
				HousingJinHuaUserInfo userInfo = new HousingJinHuaUserInfo(userAccount, type, username, idnum,
						companyName, staffName, personalAmount, companyAmount, totalAmount, balance, state,
						lastPaymonth, taskHousing.getTaskid());	
				userInfoList.add(userInfo);
			}
		}
		return userInfoList;
	}
	// 解析缴存明细信息
	public List<HousingJinHuaPaydetails> htmlPaydetailsParser(String html,String account, TaskHousing taskHousing) {
		List<HousingJinHuaPaydetails> paydetails = new ArrayList<HousingJinHuaPaydetails>();
		if (null != html && html.contains("grid")) {
			Document doc = Jsoup.parse(html, "utf-8"); 
			Element table = doc.select("table").first();
			if (null != table) {	
				Elements  trs=table.select("tbody").select("tr");
				int trs_size = trs.size();
				if (trs_size > 1) {
					for (int i = 1; i < trs_size; i++) {
						Elements tds = trs.get(i).select("td");
						String dealDate = tds.get(0).text();//记账日期
						String type = tds.get(1).text();//业务类型
						String dealAmount = tds.get(2).text();//发生额
						String balance = tds.get(3).text();//账户余额
						String summary = tds.get(4).text();//摘要
						
						HousingJinHuaPaydetails paydetail = new HousingJinHuaPaydetails();
						paydetail.setDealDate(dealDate);
						paydetail.setType(type);
						paydetail.setDealAmount(dealAmount);
						paydetail.setBalance(balance);
						paydetail.setSummary(summary);
						paydetail.setUserAccount(account);
						paydetail.setTaskid(taskHousing.getTaskid());
						paydetails.add(paydetail);
					}
				}
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
