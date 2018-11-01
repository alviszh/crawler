package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.baishan.HousingBaishanPaydetails;
import com.microservice.dao.entity.crawler.housing.baishan.HousingBaishanUserInfo;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.commontracerlog.TracerLog;


@Component
public class HousingFundBaishanParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundBaishanParser.class);
	@Autowired
	private TracerLog tracer;
	/**
	 * 解析用户信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	public HousingBaishanUserInfo htmlUserInfoParser(TaskHousing taskHousing, String html) {
		tracer.addTag("HousingFundBaishanParser---info:" + taskHousing.getTaskid(),
				"<xmp>" + html + "</xmp>");
		try {
			Document doc = Jsoup.parse(html);			
			String username = getNextLabelByKeyword(doc, "职工姓名");	
			String companyNum = getNextLabelByKeyword(doc, "单位账号");
			String idnum = getNextLabelByKeyword(doc, "身份证号");
			String accountNum = getNextLabelByKeyword(doc, "职工账号");
			String companyName = getNextLabelByKeyword(doc, "所在单位");
			String staffName = getNextLabelByKeyword(doc, "所属办事处");
			String openTime = getNextLabelByKeyword(doc, "开户日期");
			String accountState = getNextLabelByKeyword(doc, "当前状态");
			String payBase = getNextLabelByKeyword(doc, "月缴基数");
			String payAmount = getNextLabelByKeyword(doc, "月缴金额");
			String payRatio = getNextLabelByKeyword(doc, "个人/单位");// 缴存比例		
			String suppleRatio = getNextLabelByKeyword(doc, "补充缴存率");
			HousingBaishanUserInfo housingBaishanUserInfo = new HousingBaishanUserInfo();
			housingBaishanUserInfo.setUsername(username);
			housingBaishanUserInfo.setCompanyNum(companyNum);
			housingBaishanUserInfo.setCompanyName(companyName);
			housingBaishanUserInfo.setIdnum(idnum);
			housingBaishanUserInfo.setAccountNum(accountNum);
			housingBaishanUserInfo.setStaffName(staffName);
			housingBaishanUserInfo.setOpenTime(openTime);
			housingBaishanUserInfo.setAccountState(accountState);
			housingBaishanUserInfo.setPayBase(payBase);
			housingBaishanUserInfo.setPayAmount(payAmount);
			housingBaishanUserInfo.setPayRatio(payRatio);
			housingBaishanUserInfo.setSuppleRatio(suppleRatio);
			housingBaishanUserInfo.setTaskid(taskHousing.getTaskid());
			return housingBaishanUserInfo;
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingFundBaishanParser---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
		}
		return null;

	}

	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("tr[class=jtpsoft] td:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}
	// 解析缴存明细信息
	/**
	 * 解析缴费信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	public List<HousingBaishanPaydetails> htmlPayParser(String html, TaskHousing taskHousing) {
		List<HousingBaishanPaydetails> list = new ArrayList<>();
		try {
			Document doc = Jsoup.parse(html);
			Elements tr = doc.getElementsByTag("tr");
			for (Element element : tr) {
				Elements td = element.getElementsByTag("td");
				if (td.size() == 6) {
					String accountDate = td.get(0).text();
					if (!"日期".equals(accountDate)) {						
						String debtAmount = td.get(1).text();
						String creditAmount = td.get(2).text();
						String balance = td.get(3).text();
						String lendingdirection = td.get(4).text();
						String explanation = td.get(5).text();						
						HousingBaishanPaydetails housingBaishanPaydetail= new HousingBaishanPaydetails();
						housingBaishanPaydetail.setAccountDate(accountDate);
						housingBaishanPaydetail.setDebtAmount(debtAmount);
						housingBaishanPaydetail.setCreditAmount(creditAmount);
						housingBaishanPaydetail.setBalance(balance);
						housingBaishanPaydetail.setLendingdirection(lendingdirection);
						housingBaishanPaydetail.setExplanation(explanation);
						housingBaishanPaydetail.setTaskid(taskHousing.getTaskid());
						list.add(housingBaishanPaydetail);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingSZHunanParse.htmlPayParser---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
		}

		return list;

	}
}
