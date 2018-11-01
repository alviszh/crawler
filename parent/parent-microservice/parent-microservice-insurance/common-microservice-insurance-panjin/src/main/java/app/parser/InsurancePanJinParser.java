package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.panjin.InsurancePanJinChargeDetail;
import com.microservice.dao.entity.crawler.insurance.panjin.InsurancePanJinUserInfo;

/**
 * @description:
 * @author: sln 
 */
@Component
public class InsurancePanJinParser {
	//解析用户信息
	public InsurancePanJinUserInfo userInfoParser(TaskInsurance taskInsurance, String html) {
		InsurancePanJinUserInfo insurancePanJinUserInfo=new InsurancePanJinUserInfo();
		Document doc = Jsoup.parse(html);
		Elements tds =doc.getElementById("queryResult").getElementsByClass("grid").get(0).getElementsByTag("tr").get(1).getElementsByTag("td");
		insurancePanJinUserInfo.setBirthday(tds.get(7).text());
		insurancePanJinUserInfo.setGender(tds.get(5).text());
		insurancePanJinUserInfo.setInsurcardno(tds.get(1).text());
		insurancePanJinUserInfo.setInsurno(tds.get(3).text());
		insurancePanJinUserInfo.setMedicalretire(tds.get(9).text());
		insurancePanJinUserInfo.setName(tds.get(4).text());
		insurancePanJinUserInfo.setNation(tds.get(6).text());
		insurancePanJinUserInfo.setPersonalnum(tds.get(2).text());
		insurancePanJinUserInfo.setRetirestate(tds.get(8).text());
		insurancePanJinUserInfo.setTaskid(taskInsurance.getTaskid());
		return insurancePanJinUserInfo;
	}
	//解析个人缴费明细信息(返回的字段有很多，但是个人信息中有的此处不再存储)
	public List<InsurancePanJinChargeDetail> chargeDetailParser(TaskInsurance taskInsurance, String html, String tabId) {
		List<InsurancePanJinChargeDetail> list=new ArrayList<InsurancePanJinChargeDetail>();
		InsurancePanJinChargeDetail insurancePanJinChargeDetail=null;
		Document doc = Jsoup.parse(html);
		//从隐藏域中获取本页个人缴费明细信息的总记录数
		String val = doc.getElementById("tab"+tabId).getElementsByClass("page_bottom").get(0).getElementsByClass("page_num").get(0).getElementsByTag("input").val();
		int totalRecord = Integer.parseInt(val);
		Elements elementsByTag =doc.getElementById("tab"+tabId).getElementsByClass("grid").get(0).getElementsByTag("tr");
		for(int i=1;i<=totalRecord;i++){
			Elements tds = elementsByTag.get(i).getElementsByTag("td");
			insurancePanJinChargeDetail=new InsurancePanJinChargeDetail();
			insurancePanJinChargeDetail.setAccountdate(tds.get(14).text());
			insurancePanJinChargeDetail.setChargebasenum(tds.get(11).text());
			insurancePanJinChargeDetail.setChargeflag(tds.get(10).text());
			insurancePanJinChargeDetail.setChargetype(tds.get(8).text());
			insurancePanJinChargeDetail.setChargeyearmonth(tds.get(9).text());
			insurancePanJinChargeDetail.setInsurtype(tds.get(2).text());
			insurancePanJinChargeDetail.setPercharge(tds.get(12).text());
			insurancePanJinChargeDetail.setTaskid(taskInsurance.getTaskid());
			insurancePanJinChargeDetail.setUnitcharge(tds.get(13).text());
			insurancePanJinChargeDetail.setUnitname(tds.get(7).text());
			insurancePanJinChargeDetail.setUnitnum(tds.get(6).text());
			list.add(insurancePanJinChargeDetail);
		}
		return list;
	}
}
