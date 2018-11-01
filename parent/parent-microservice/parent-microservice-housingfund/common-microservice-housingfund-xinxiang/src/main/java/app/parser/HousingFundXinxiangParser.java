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
import com.microservice.dao.entity.crawler.housing.xinxiang.HousingXinxiangPaydetails;
import com.microservice.dao.entity.crawler.housing.xinxiang.HousingXinxiangUserInfo;


@Component
public class HousingFundXinxiangParser {
	public static final Logger log = LoggerFactory.getLogger(HousingFundXinxiangParser.class);
	// 解析用户信息
	public HousingXinxiangUserInfo htmlUserInfoParser(String html, TaskHousing taskHousing) {
		HousingXinxiangUserInfo userInfo = new HousingXinxiangUserInfo();
		if (html.contains("新乡市住房公积金个人信息查询")) {
			Document doc = Jsoup.parse(html,"UTF-8");
			String companyNumber = getNextLabelByKeyword(doc, "单位编号");	
			String accountNumber= getNextLabelByKeyword(doc, "个人编号");	
			String username = getNextLabelByKeyword(doc, "姓名");	
			String companyName= getNextLabelByKeyword(doc, "单位名称");	
			String idnum = getNextLabelByKeyword(doc, "身份证号");	
			String state= getNextLabelByKeyword(doc, "账户状态");	
			String personPayAmount = getNextLabelByKeyword(doc, "个人月缴额");	
			String companyPayAmount= getNextLabelByKeyword(doc, "单位月缴额");	
			String payBase = getNextLabelByKeyword(doc, "工资基数");	
			String balance= getNextLabelByKeyword(doc, "余额");	
			String companyLastMonth = getNextLabelByKeyword(doc, "单位缴至月份");	
			String personLastMonth= getNextLabelByKeyword(doc, "个人缴至月份");
			userInfo.setCompanyNumber(companyNumber);
			userInfo.setAccountNumber(accountNumber);
			userInfo.setUsername(username);
			userInfo.setCompanyName(companyName);
			userInfo.setIdnum(idnum);
			userInfo.setState(state);
			userInfo.setPersonPayAmount(personPayAmount);
			userInfo.setCompanyPayAmount(companyPayAmount);
			userInfo.setPayBase(payBase);
			userInfo.setBalance(balance);
			userInfo.setCompanyLastMonth(companyLastMonth);
			userInfo.setPersonLastMonth(personLastMonth);
			userInfo.setTaskid(taskHousing.getTaskid());
		}
		return userInfo;
	}
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("th:contains(" + keyword + ")");
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
	public List<HousingXinxiangPaydetails> htmlPaydetailsParser(String html, TaskHousing taskHousing) {
		List<HousingXinxiangPaydetails> paydetails = new ArrayList<HousingXinxiangPaydetails>();
		if (null != html && html.contains("新乡市住房公积金个人明细查询")) {
			Document doc = Jsoup.parse(html,"UTF-8");
			 Element table= doc.select("table").last();
			 Elements trs=table.select("tbody").select("tr");
			 if (trs.size()>2) {
				 for (int i = 2; i < trs.size(); i++) {
					 Elements tds = trs.get(i).select("td");
					 String companyNumber=tds.get(0).text();
					 String accountNumber= tds.get(1).text();
					 String incomeAmount= tds.get(2).text();
					 String expendAmount=tds.get(3).text();
					 String balance= tds.get(4).text();
					 String type= tds.get(5).text();
					 String payForMonth= tds.get(6).text();
					 String payDate= tds.get(7).text();
					 HousingXinxiangPaydetails paydetail=new HousingXinxiangPaydetails();
					 paydetail.setCompanyNumber(companyNumber);
					 paydetail.setAccountNumber(accountNumber);
					 paydetail.setIncomeAmount(incomeAmount);
					 paydetail.setExpendAmount(expendAmount);
					 paydetail.setBalance(balance);
					 paydetail.setType(type);
					 paydetail.setPayForMonth(payForMonth);
					 paydetail.setPayDate(payDate);
					 paydetail.setTaskid(taskHousing.getTaskid());
					 paydetails.add(paydetail);
				 }
			}
		}
		return paydetails;
	}
}
