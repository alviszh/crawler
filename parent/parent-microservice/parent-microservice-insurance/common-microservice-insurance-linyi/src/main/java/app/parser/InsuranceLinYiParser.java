package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.linyi.InsuranceLinYiChargeDetail;
import com.microservice.dao.entity.crawler.insurance.linyi.InsuranceLinYiChargeInfo;
import com.microservice.dao.entity.crawler.insurance.linyi.InsuranceLinYiUserInfo;

/**
 * @description:
 * @author: sln 
 * @date: 2017年12月7日 下午5:34:35 
 */
@Component
public class InsuranceLinYiParser {
	//解析用户信息
	public InsuranceLinYiUserInfo userInfoParser(TaskInsurance taskInsurance, String html) {
		InsuranceLinYiUserInfo insuranceLinYiUserInfo=new InsuranceLinYiUserInfo();
		Document doc = Jsoup.parse(html);
		insuranceLinYiUserInfo.setBirthday(doc.getElementById("aac006").val());
		insuranceLinYiUserInfo.setGender(doc.getElementById("aac004").getElementsByAttribute("selected").get(0).text());
		insuranceLinYiUserInfo.setIdnum(doc.getElementById("aac002").val());
		insuranceLinYiUserInfo.setJoinworkdate(doc.getElementById("aac007").val());
		insuranceLinYiUserInfo.setName(doc.getElementById("aac003").val());
		insuranceLinYiUserInfo.setNation(doc.getElementById("aac005").getElementsByAttribute("selected").get(0).text());
		insuranceLinYiUserInfo.setPersonalnum(doc.getElementById("akc020").val());
		insuranceLinYiUserInfo.setPersoncategory(doc.getElementById("aac004").getElementsByAttribute("selected").get(0).text());
		insuranceLinYiUserInfo.setTaskid(taskInsurance.getTaskid());
		insuranceLinYiUserInfo.setUnitname(doc.getElementById("aab004").val());
		return insuranceLinYiUserInfo;
	}
	//解析个人参保信息
	public List<InsuranceLinYiChargeInfo> chargeInfoParser(TaskInsurance taskInsurance, String html) {
		List<InsuranceLinYiChargeInfo> list=new ArrayList<InsuranceLinYiChargeInfo>();
		InsuranceLinYiChargeInfo insuranceLinYiChargeInfo=null;
		Document doc = Jsoup.parse(html);
		//获取本页参保信息的总记录数
		String val = doc.getElementById("queryform1").getElementsByTag("table").get(0).getElementsByClass("page_bottom").get(0).getElementsByClass("page_num").get(0).getElementsByTag("input").val();
		int totalRecord = Integer.parseInt(val);
		System.out.println("获取的记录总数是："+totalRecord);
		if(totalRecord>0){
			Elements elementsByTag =doc.getElementById("queryform1").getElementById("queryResult").getElementsByClass("grid").get(0).getElementsByTag("tr");
			for(int i=1;i<=totalRecord;i++){
				Elements tds = elementsByTag.get(i).getElementsByTag("td");
				insuranceLinYiChargeInfo=new InsuranceLinYiChargeInfo();
				insuranceLinYiChargeInfo.setInsurdate(tds.get(6).text());
				insuranceLinYiChargeInfo.setInsurstatus(tds.get(4).text());
				insuranceLinYiChargeInfo.setInsurtype(tds.get(3).text());
				insuranceLinYiChargeInfo.setPersonalnum(tds.get(2).text());
				insuranceLinYiChargeInfo.setTaskid(taskInsurance.getTaskid().trim());
				insuranceLinYiChargeInfo.setThisunitinsurdate(tds.get(5).text());
				insuranceLinYiChargeInfo.setUnitname(tds.get(1).text());
				insuranceLinYiChargeInfo.setUnitnum(tds.get(0).text());
				list.add(insuranceLinYiChargeInfo);
			}
		}else{
			list=null;
		}
		return list;
	}
	//解析个人缴费明细信息
	public List<InsuranceLinYiChargeDetail> chargeDetailParser(TaskInsurance taskInsurance, String html) {
		List<InsuranceLinYiChargeDetail> list=new ArrayList<InsuranceLinYiChargeDetail>();
		InsuranceLinYiChargeDetail insuranceLinYiChargeDetail=null;
		Document doc = Jsoup.parse(html);
		//获取本页个人缴费明细信息的总记录数
		String val = doc.getElementById("queryform1").getElementsByTag("table").get(0).getElementsByClass("page_bottom").get(0).getElementsByClass("page_num").get(0).getElementsByTag("input").val();
		int totalRecord = Integer.parseInt(val);
		System.out.println("获取的记录总数是："+totalRecord);
		Elements elementsByTag =doc.getElementById("queryform1").getElementById("queryResult").getElementsByClass("grid").get(0).getElementsByTag("tr");
		for(int i=1;i<=totalRecord;i++){
			Elements tds = elementsByTag.get(i).getElementsByTag("td");
			insuranceLinYiChargeDetail=new InsuranceLinYiChargeDetail();
			insuranceLinYiChargeDetail.setChargebasenum(tds.get(5).text());
			insuranceLinYiChargeDetail.setChargeflag(tds.get(4).text());
			insuranceLinYiChargeDetail.setChargetype(tds.get(3).text());
			insuranceLinYiChargeDetail.setChargeyearmonth(tds.get(1).text());
			insuranceLinYiChargeDetail.setInsurtype(tds.get(2).text());
			insuranceLinYiChargeDetail.setPercharge(tds.get(7).text());
			insuranceLinYiChargeDetail.setTaskid(taskInsurance.getTaskid());
			insuranceLinYiChargeDetail.setUnitcharge(tds.get(6).text());
			list.add(insuranceLinYiChargeDetail);
		}
		return list;
	}

}
