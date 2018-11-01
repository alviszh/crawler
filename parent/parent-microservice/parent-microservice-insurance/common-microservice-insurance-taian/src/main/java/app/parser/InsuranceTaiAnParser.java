package app.parser;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.taian.InsuranceTaiAnChargeDetail;
import com.microservice.dao.entity.crawler.insurance.taian.InsuranceTaiAnUserInfo;


@Component
public class InsuranceTaiAnParser {
	public static final Logger log = LoggerFactory.getLogger(InsuranceTaiAnParser.class);
	public InsuranceTaiAnUserInfo userInfoParser(TaskInsurance taskInsurance, String html) {
		InsuranceTaiAnUserInfo insuranceTaiAnUserInfo=new InsuranceTaiAnUserInfo();
		Document doc = Jsoup.parse(html);
		insuranceTaiAnUserInfo.setBirthday(doc.getElementById("csrq").val());
		insuranceTaiAnUserInfo.setContactnum(doc.getElementById("lxdh").val());
		insuranceTaiAnUserInfo.setGender(doc.getElementById("xbmc").val());
		insuranceTaiAnUserInfo.setHomeaddress(doc.getElementById("jtzz").val());
		insuranceTaiAnUserInfo.setHouseholdregister(doc.getElementById("hkszd").val());
		insuranceTaiAnUserInfo.setIdnum(doc.getElementById("sfzhm").val());
		insuranceTaiAnUserInfo.setInsurunit(doc.getElementById("dwmc").val());
		insuranceTaiAnUserInfo.setLinkaddress(doc.getElementById("txdz").val());
		insuranceTaiAnUserInfo.setLinkman(doc.getElementById("lxr").val());
		insuranceTaiAnUserInfo.setName(doc.getElementById("xm").val());
		insuranceTaiAnUserInfo.setPostalcode(doc.getElementById("yzbm").val());
		insuranceTaiAnUserInfo.setTaskid(taskInsurance.getTaskid().trim());
		//民族
		insuranceTaiAnUserInfo.setNation(doc.getElementById("mzmc").val());
		//婚姻状况
		insuranceTaiAnUserInfo.setMarriage(doc.getElementById("hyzkmc").val());
		//行政职务
		insuranceTaiAnUserInfo.setAdministrativepost(doc.getElementById("xzzwmc").val());
		//文化程度
		insuranceTaiAnUserInfo.setEducationdegree(doc.getElementById("whcdmc").val());
		//户口性质
		insuranceTaiAnUserInfo.setHouseholdtype(doc.getElementById("hkxzmc").val());
		return insuranceTaiAnUserInfo;
	}
	//养老保险的解析方式
	public List<InsuranceTaiAnChargeDetail> pensionParser(TaskInsurance taskInsurance, String html) {
		List<InsuranceTaiAnChargeDetail> list=new ArrayList<InsuranceTaiAnChargeDetail>();
		InsuranceTaiAnChargeDetail insuranceTaiAnChargeDetail=null;
		Document doc = Jsoup.parse(html);
		Elements trs = doc.getElementsByAttributeValue("ondblclick", "null");
		int size=trs.size();  //计算总记录数（源代码中标题行也占用了一个tr）
		for(int i=1;i<size;i++){
			insuranceTaiAnChargeDetail=new InsuranceTaiAnChargeDetail();
			Elements tds = trs.get(i).getElementsByTag("td");
			insuranceTaiAnChargeDetail.setChargeyearmonth(tds.get(0).getElementsByTag("input").get(0).val());
			insuranceTaiAnChargeDetail.setChargebasenum(tds.get(1).getElementsByTag("input").get(0).val());
			insuranceTaiAnChargeDetail.setUnitcharge(tds.get(2).getElementsByTag("input").get(0).val());
			insuranceTaiAnChargeDetail.setPerchargebasenum(tds.get(3).getElementsByTag("input").get(0).val());
			insuranceTaiAnChargeDetail.setPercharge(tds.get(4).getElementsByTag("input").get(0).val());
			insuranceTaiAnChargeDetail.setTaskid(taskInsurance.getTaskid().trim());
			insuranceTaiAnChargeDetail.setInsurtype("养老保险");
			list.add(insuranceTaiAnChargeDetail);
		}
		return list;
	}
	//医疗保险的解析方式
	public List<InsuranceTaiAnChargeDetail> medicalParser(TaskInsurance taskInsurance, String html) {
		List<InsuranceTaiAnChargeDetail> list=new ArrayList<InsuranceTaiAnChargeDetail>();
		InsuranceTaiAnChargeDetail insuranceTaiAnChargeDetail=null;
		Document doc = Jsoup.parse(html);
		Elements trs = doc.getElementsByAttributeValue("ondblclick", "null");
		int size=trs.size();
		for(int i=1;i<size;i++){
			insuranceTaiAnChargeDetail=new InsuranceTaiAnChargeDetail();
			Elements tds = trs.get(i).getElementsByTag("td");
			insuranceTaiAnChargeDetail.setChargeyearmonth(tds.get(0).getElementsByTag("input").get(0).val());
			insuranceTaiAnChargeDetail.setInsurtype(tds.get(1).getElementsByTag("input").get(0).val());
			insuranceTaiAnChargeDetail.setUnitchargebasenum(tds.get(2).getElementsByTag("input").get(0).val());
			insuranceTaiAnChargeDetail.setUnitcharge(tds.get(3).getElementsByTag("input").get(0).val());
			insuranceTaiAnChargeDetail.setPerchargebasenum(tds.get(4).getElementsByTag("input").get(0).val());
			insuranceTaiAnChargeDetail.setPercharge(tds.get(5).getElementsByTag("input").get(0).val());
			insuranceTaiAnChargeDetail.setTaskid(taskInsurance.getTaskid().trim());
			list.add(insuranceTaiAnChargeDetail);
		}
		return list;
	}
	//工伤和生育
	public List<InsuranceTaiAnChargeDetail> injuryAndBearParser(TaskInsurance taskInsurance, String html, String englishDescription) {
		List<InsuranceTaiAnChargeDetail> list=new ArrayList<InsuranceTaiAnChargeDetail>();
		InsuranceTaiAnChargeDetail insuranceTaiAnChargeDetail=null;
		Document doc = Jsoup.parse(html);
		Elements trs = doc.getElementsByAttributeValue("ondblclick", "null");
		int size=trs.size();
		for(int i=1;i<size;i++){
			insuranceTaiAnChargeDetail=new InsuranceTaiAnChargeDetail();
			Elements tds = trs.get(i).getElementsByTag("td");
			insuranceTaiAnChargeDetail.setChargeyearmonth(tds.get(0).getElementsByTag("input").get(0).val());
			insuranceTaiAnChargeDetail.setUnitname(tds.get(1).getElementsByTag("input").get(0).val());
			insuranceTaiAnChargeDetail.setChargebasenum(tds.get(2).getElementsByTag("input").get(0).val());
			insuranceTaiAnChargeDetail.setUnitcharge(tds.get(3).getElementsByTag("input").get(0).val());
			insuranceTaiAnChargeDetail.setTaskid(taskInsurance.getTaskid().trim());
			if(englishDescription.equals("getInjury")){
				insuranceTaiAnChargeDetail.setInsurtype("工伤保险");
			}else{
				insuranceTaiAnChargeDetail.setInsurtype("生育保险");
			}
			list.add(insuranceTaiAnChargeDetail);
		}
		return list;
	}
	//失业
	public List<InsuranceTaiAnChargeDetail> unemploymentParser(TaskInsurance taskInsurance, String html) {
		List<InsuranceTaiAnChargeDetail> list=new ArrayList<InsuranceTaiAnChargeDetail>();
		InsuranceTaiAnChargeDetail insuranceTaiAnChargeDetail=null;
		Document doc = Jsoup.parse(html);
		Elements trs = doc.getElementsByAttributeValue("ondblclick", "null");
		int size=trs.size();
		for(int i=1;i<size;i++){
			insuranceTaiAnChargeDetail=new InsuranceTaiAnChargeDetail();
			Elements tds = trs.get(i).getElementsByTag("td");
			insuranceTaiAnChargeDetail.setChargeyearmonth(tds.get(0).getElementsByTag("input").get(0).val());
			insuranceTaiAnChargeDetail.setUnitname(tds.get(1).getElementsByTag("input").get(0).val());
			insuranceTaiAnChargeDetail.setChargebasenum(tds.get(2).getElementsByTag("input").get(0).val());
			insuranceTaiAnChargeDetail.setUnitcharge(tds.get(3).getElementsByTag("input").get(0).val());
			insuranceTaiAnChargeDetail.setPercharge(tds.get(4).getElementsByTag("input").get(0).val());
			insuranceTaiAnChargeDetail.setTaskid(taskInsurance.getTaskid().trim());
			insuranceTaiAnChargeDetail.setInsurtype("失业保险");
			list.add(insuranceTaiAnChargeDetail);
		}
		return list;
	}
}
