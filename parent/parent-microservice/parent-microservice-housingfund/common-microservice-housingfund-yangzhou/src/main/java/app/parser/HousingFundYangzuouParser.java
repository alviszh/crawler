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

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.yangzhou.HousingYangzhouPaydetails;
import com.microservice.dao.entity.crawler.housing.yangzhou.HousingYangzhouUserInfo;


@Component
public class HousingFundYangzuouParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundYangzuouParser.class);
	// 解析用户信息
	public HousingYangzhouUserInfo htmlUserInfoParser(String html, TaskHousing taskHousing) {
		HousingYangzhouUserInfo userInfo = new HousingYangzhouUserInfo();
		Document doc = Jsoup.parse(html, "UTF-8");
		Element  table=doc.getElementById("sample-table-sortable");
		if (null !=table) {
			String username=getNextLabelByKeyword(doc,"职工姓名");
			String companyName=getNextLabelByKeyword(doc,"单位名称");
			String useraccount=getNextLabelByKeyword(doc,"职工账号");
			String idnum=getNextLabelByKeyword(doc,"身份证号");
			String state=getNextLabelByKeyword(doc,"缴存状态");
			String payrate=getNextLabelByKeyword(doc,"缴存比例");;
			String payamount=getNextLabelByKeyword(doc,"公积金月缴额");
			String subsidyamount=getNextLabelByKeyword(doc,"补贴月缴额");
			String balance=getNextLabelByKeyword(doc,"公积金余额");
			String subsidybalance=getNextLabelByKeyword(doc,"补贴余额");
			String totalamount=getNextLabelByKeyword(doc,"余额合计");
			String lastmonth=getNextLabelByKeyword(doc,"缴至年月");
			userInfo=new HousingYangzhouUserInfo(username, companyName, useraccount, idnum, state,
					payrate, payamount, subsidyamount, balance, subsidybalance,
					totalamount, lastmonth, taskHousing.getTaskid());
		}
		return userInfo;
	}
	// 解析缴存明细信息
	public List<HousingYangzhouPaydetails> htmlPaydetailsParser(String html, TaskHousing taskHousing) {
		List<HousingYangzhouPaydetails> paydetails = new ArrayList<HousingYangzhouPaydetails>();
		if (null != html && html.contains("缴存明细账")) {
			Document doc = Jsoup.parse(html, "UTF-8");
			Element table = doc.getElementById("sample-table-sortable");
			Elements trs = table.select("tbody").select("tr");
			if (null != trs) {
				int trs_size = trs.size();
				if (trs_size > 0) {
					for (int i = 0; i < trs_size; i++) {
						Elements tds = trs.get(i).select("td");
						String confirmDate = tds.get(0).text();
						String type = tds.get(1).text();
						String paymonth = tds.get(2).text();
						String incomeAmount = tds.get(3).text();
						String expendAmount = tds.get(4).text();
						String interests = tds.get(5).text();
						String balance = tds.get(6).text();
						String subsidyIncome = tds.get(7).text();
						String subsidyExpend = tds.get(8).text();
						String subsidyInterests = tds.get(9).text();
						String subsidyBalance = tds.get(10).text();
						String totalBalance = tds.get(11).text();
						HousingYangzhouPaydetails paydetail = new HousingYangzhouPaydetails(confirmDate, type, paymonth,
								incomeAmount, expendAmount, interests, balance, subsidyIncome, subsidyExpend,
								subsidyInterests, subsidyBalance, totalBalance, taskHousing.getTaskid());
						paydetails.add(paydetail);
					}
				}
			}
		}
		return paydetails;
	}
    /**
     * @param document
     * @param keyword
     * @return
     * @Des 获取目标标签的下一个兄弟标签的内容
     */
    public String getNextLabelByKeyword(Document document, String keyword) {
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
