package app.crawler.htmlparse;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.beijing.HousingBeiJingPay;
import com.microservice.dao.entity.crawler.housing.beijing.HousingBeiJingUserinfo;

public class HousingBJParse {

	public static HousingBeiJingUserinfo userinfo_parse(String html) {

		HousingBeiJingUserinfo userinfo = new HousingBeiJingUserinfo();

		Document doc = Jsoup.parse(StringEscapeUtils.unescapeHtml(html), "utf-8");

		String name = doc.select("td:contains(" + "姓名" + ")+td").text();
		String personnum = doc.select("td:contains(" + "个人登记号" + ")+td").text();
		String idtype = doc.select("td:contains(" + "证件类型" + ")+td").text();
		String idnum = doc.select("td:contains(" + "证件号" + ")+td").text();
		String companynum = doc.select("td:contains(" + "单位登记号" + ")+td").text();
		String companyname = doc.select("td:contains(" + "单位名称" + ")+td").text();
		String managementnum = doc.select("td:contains(" + "所属管理部编号" + ")+td").text();
		String managementname = doc.select("td:contains(" + "所属管理部名称" + ")+td").text();
		String balance = doc.select("td:contains(" + "当前余额" + ")+td").text();
		String accountstatus = doc.select("td:contains(" + "帐户状态" + ")+td").text();
		String payyear = doc.select("td:contains(" + "当年缴存金额" + ")+td").text();
		String fetchyear = doc.select("td:contains(" + "当年提取金额" + ")+td").text();
		String payoldyear = doc.select("td:contains(" + "上年结转余额" + ")+td").text();
		String enddate = doc.select("td:contains(" + "最后业务日期" + ")+td").text();
		String transfer = doc.select("td:contains(" + "转出金额" + ")+td").text();

		userinfo.setName(name);
		userinfo.setPersonnum(personnum);
		userinfo.setIdtype(idtype);
		userinfo.setIdnum(idnum);
		userinfo.setCompanynum(companynum);
		userinfo.setCompanyname(companyname);
		userinfo.setManagementnum(managementnum);
		userinfo.setManagementname(managementname);
		userinfo.setBalance(balance);
		userinfo.setAccountstatus(accountstatus);
		userinfo.setPayyear(payyear);
		userinfo.setFetchyear(fetchyear);
		userinfo.setPayoldyear(payoldyear);
		userinfo.setEnddate(enddate);
		userinfo.setTransfer(transfer);
		System.out.println(userinfo.toString());

		return userinfo;
	}

	public static List<HousingBeiJingPay> pay_parse(String html) {

		List<HousingBeiJingPay> listresult = new ArrayList<>();
		
		Document doc = Jsoup.parse(StringEscapeUtils.unescapeHtml(html), "utf-8");

		Elements treles = doc.select("table#new-mytable3").select("tbody").select("tr");
		int i=0;
		for(Element trele : treles){
			i++;
			if(i==1){
				continue;
			}
			System.out.println(trele);
			String receiveddate = trele.select("td").get(0).text().trim();
			String settlementdate = trele.select("td").get(1).text().trim();
			String businesstype = trele.select("td").get(2).text().trim();
			String addnum = trele.select("td").get(3).text().trim();
			String lessennum = trele.select("td").get(4).text().trim();
			String balance = trele.select("td").get(5).text().trim();
			HousingBeiJingPay pay = new HousingBeiJingPay();
			pay.setReceiveddate(receiveddate);
			pay.setSettlementdate(settlementdate);
			pay.setBusinesstype(businesstype);
			pay.setAddnum(addnum);
			pay.setLessennum(lessennum);
			pay.setBalance(balance);
			System.out.println(pay.toString());
			listresult.add(pay);
		}
		return listresult;
	}

}
