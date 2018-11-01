package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.zhuhai.InsuranceZhuhaiUnify;
import com.microservice.dao.entity.crawler.insurance.zhuhai.InsuranceZhuhaiUserinfo;

import app.commontracerlog.TracerLog;

@Component
public class InsuranceZhuhaiParser {
	
	@Autowired
	private TracerLog tracer; 

	/**
	 * 解析用户信息数据
	 * @param html
	 * @return
	 */
	public InsuranceZhuhaiUserinfo parserUserinfo(HtmlPage html) {
		InsuranceZhuhaiUserinfo insuranceZhuhaiUserinfo = new InsuranceZhuhaiUserinfo();
		try{
			String page = html.getWebResponse().getContentAsString();
			String SSN = getMsgByKeyword(page,"社会保障号码");
			System.out.println("社会保障号码："+SSN);
			String name = getMsgByKeyword(page,"姓名");
			System.out.println("姓名："+name);
			String certificate = getMsgByKeyword(page,"法人代码");
			System.out.println("法人代码："+certificate);
			String companyName = getMsgByKeyword(page,"单位名称");
			System.out.println("单位名称："+companyName);
			String insuranceKind = getMsgByKeyword(page,"参保险种");
			System.out.println("参保险种："+insuranceKind);
			String declareSalary = getMsgByKeyword(page,"最新申报工资");
			System.out.println("最新申报工资："+declareSalary);
			String pensionPayMonthTotal = getMsgByKeyword(page,"养老保险累计缴费月数");
			System.out.println("养老保险累计缴费月数："+pensionPayMonthTotal);
			String pensionCapital = getMsgByKeyword(page,"养老保险个人帐户本金");
			System.out.println("养老保险个人帐户本金："+pensionCapital);
			String pensionPayCapital = getMsgByKeyword(page,"养老保险个人缴费本金");
			System.out.println("养老保险个人缴费本金："+pensionPayCapital);
			String householdType = getMsgByKeyword(page,"户口类型");
			System.out.println("户口类型："+householdType);
			String employmentForm = getMsgByKeyword(page,"用工形式");
			System.out.println("用工形式："+employmentForm);
			String InsuranceType = getMsgByKeyword(page,"社保状态");
			System.out.println("社保状态："+InsuranceType);
			String companySSN = getMsgByKeyword(page,"单位社保号");
			System.out.println("单位社保号："+companySSN);
			String socialSecurityNum = getMsgByKeyword(page,"社保卡号");
			System.out.println("社保卡号："+socialSecurityNum);
			String socialSecurityNumPerson = getMsgByKeyword(page,"个人社保号");
			System.out.println("个人社保号："+socialSecurityNumPerson);
			
			insuranceZhuhaiUserinfo.setCertificate(certificate);
			insuranceZhuhaiUserinfo.setCompanyName(companyName);
			insuranceZhuhaiUserinfo.setCompanySSN(companySSN);
			insuranceZhuhaiUserinfo.setDeclareSalary(declareSalary);
			insuranceZhuhaiUserinfo.setEmploymentForm(employmentForm);
			insuranceZhuhaiUserinfo.setHouseholdType(householdType);
			insuranceZhuhaiUserinfo.setInsuranceKind(insuranceKind);
			insuranceZhuhaiUserinfo.setInsuranceType(InsuranceType);
			insuranceZhuhaiUserinfo.setName(companyName);
			insuranceZhuhaiUserinfo.setPensionCapital(pensionCapital);
			insuranceZhuhaiUserinfo.setPensionPayCapital(pensionPayCapital);
			insuranceZhuhaiUserinfo.setPensionPayMonthTotal(pensionPayMonthTotal);
			insuranceZhuhaiUserinfo.setSocialSecurityNum(socialSecurityNumPerson);
			insuranceZhuhaiUserinfo.setSocialSecurityNumPerson(socialSecurityNumPerson);
			insuranceZhuhaiUserinfo.setSSN(SSN);
			
			return insuranceZhuhaiUserinfo;
			
		}catch(Exception e){
			tracer.addTag("解析用户信息出错了！", e.getMessage());
			return null;
		}
		
	}
	
	 public static String getMsgByKeyword(String html, String keyword) {
		 	Document document = Jsoup.parse(html);
	        Elements es = document.select("label:contains(" + keyword + ")");
	        if (null != es && es.size() > 0) {
	            Element element = es.first();
	            String text = element.nextElementSibling().attr("value");
	            return text;
	        }
	        return null;
	    }

	/**
	 * 五险统一解析方法
	 * @param html
	 * @param string
	 * @param taskid
	 * @return
	 */
	public List<InsuranceZhuhaiUnify> parserInsurance(HtmlPage html, String type, String taskid) {
		
		List<InsuranceZhuhaiUnify> list = new ArrayList<InsuranceZhuhaiUnify>();
		String page = html.getWebResponse().getContentAsString();
		Document doc = Jsoup.parse(page);
		Elements fieldsets = doc.getElementsByTag("fieldset");
		if(null != fieldsets && fieldsets.size()>2){
			for(int i = 1; i<fieldsets.size();i++){
				Element fieldset = fieldsets.get(i);
				
				String month = fieldset.child(0).child(0).text();
				String companyName = fieldset.child(0).child(1).text();
				String payType = fieldset.child(0).child(2).text();
				
				System.out.println("缴纳月份："+month);
				System.out.println("单位名称："+companyName);
				System.out.println("缴费类型："+payType);
				
				
				String paySalary = getMsgByKeyword1(page,"缴费工资");
				System.out.println("缴费工资："+paySalary);
				String payPerson = getMsgByKeyword1(page,"个人缴费");
				System.out.println("个人缴费："+payPerson);
				String payCompany = getMsgByKeyword1(page,"单位缴费");
				System.out.println("单位缴费："+payCompany);
				String companyClassified = getMsgByKeyword1(page,"单位划入");
				System.out.println("单位划入："+companyClassified);
				String dealDate = getMsgByKeyword1(page,"处理日期");
				System.out.println("处理日期:"+dealDate);
				String capitalSource = getMsgByKeyword1(page,"资金来源");
				System.out.println("资金来源:"+capitalSource);
				
				InsuranceZhuhaiUnify insuranceZhuhaiUnify = new InsuranceZhuhaiUnify();
				insuranceZhuhaiUnify.setCapitalSource(capitalSource);
				insuranceZhuhaiUnify.setCompanyClassified(companyClassified);
				insuranceZhuhaiUnify.setCompanyName(companyName);
				insuranceZhuhaiUnify.setDealDate(dealDate);
				insuranceZhuhaiUnify.setMonth(month);
				insuranceZhuhaiUnify.setPayCompany(payCompany);
				insuranceZhuhaiUnify.setPayPerson(payPerson);
				insuranceZhuhaiUnify.setPaySalary(paySalary);
				insuranceZhuhaiUnify.setPayType(payType);
				insuranceZhuhaiUnify.setTaskid(taskid);
				insuranceZhuhaiUnify.setType(type);
				list.add(insuranceZhuhaiUnify);
			}
		}else{
			return null;
		}
		
		return list;
	}
	
	 public static String getMsgByKeyword1(String html, String keyword) {
		 	Document document = Jsoup.parse(html);
	        Elements es = document.select("label:contains(" + keyword + ")");
	        if (null != es && es.size() > 0) {
	            Element element = es.first();
	            String text = element.nextElementSibling().text();
	            return text;
	        }
	        return null;
	    }

}
