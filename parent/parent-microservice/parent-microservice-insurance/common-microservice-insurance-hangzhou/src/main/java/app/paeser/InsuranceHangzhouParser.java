package app.paeser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.beijing.InsuranceBeijingMedical;
import com.microservice.dao.entity.crawler.insurance.beijing.InsuranceBeijingPension;
import com.microservice.dao.entity.crawler.insurance.hangzhou.InsuranceHangzhouInjury;
import com.microservice.dao.entity.crawler.insurance.hangzhou.InsuranceHangzhouMaternity;
import com.microservice.dao.entity.crawler.insurance.hangzhou.InsuranceHangzhouMedical;
import com.microservice.dao.entity.crawler.insurance.hangzhou.InsuranceHangzhouUnemployment;
import com.microservice.dao.entity.crawler.insurance.hangzhou.InsuranceHangzhouUserInfo;
import com.microservice.dao.entity.crawler.insurance.hangzhou.InsurancehangzhouPension;
import com.module.htmlunit.WebCrawler;


@Component
public class InsuranceHangzhouParser {
	
	@Autowired
	private InsuranceService insuranceService;
	
	//个人信息
	public InsuranceHangzhouUserInfo htmlParser(String html,TaskInsurance taskInsurance) {
		
		InsuranceHangzhouUserInfo insuranceHangzhouUserInfo = new InsuranceHangzhouUserInfo();
		Document doc = Jsoup.parse(html);

		String name = null;
		String idNum = null;
		String personalNumber = null;
		String sex = null;
		String nation = null;
		String birthdate = null;
		String workDate = null;
		String personnelStatus = null;
		String personnelType =  null;
		String politicalOutlook = null;
		String category = null;
		String employmentForm = null;
		String education = null;
		String hkadr = null;
		Elements element = doc.select("table.form td");
		if (element.size()>0){
			for(int k = 0;k<element.size();k++){
				String string = element.get(k).text().trim();
				if (string.contains("姓名：")){
					k = k +1;
					name = element.get(k).text().trim();
				}
				if (string.contains("身份证号码：")){
					k = k +1;
					idNum = element.get(k).text().trim();
				}
				if (string.contains("个人编号：")){
					k = k +1;
					personalNumber = element.get(k).text().trim();
				}
				if (string.contains("性别：")){
					k = k +1;
					sex = element.get(k).text().trim();
				}
				if (string.contains("民族：")){
					k = k +1;
					nation = element.get(k).text().trim();
				}
				if (string.contains("出生日期：")){
					k = k +1;
					birthdate = element.get(k).text().trim();
				}
				if (string.contains("参加工作日期：")){
					k = k +1;
					workDate = element.get(k).text().trim();
				}
				if (string.contains("人员状态：")){
					k = k +1;
					personnelStatus = element.get(k).text().trim();
				}
				if (string.contains("人员类别：")){
					k = k +1;
					personnelType = element.get(k).text().trim();
				}
				if (string.contains("政治面貌：")){
					k = k +1;
					politicalOutlook = element.get(k).text().trim();
				}
				if (string.contains("户口性质：")){
					k = k +1;
					category = element.get(k).text().trim();
				}
				if (string.contains("用工形式：")){
					k = k +1;
					employmentForm = element.get(k).text().trim();
				}
				if (string.contains("文化程度：")){
					k = k +1;
					education = element.get(k).text().trim();
				}
				if (string.contains("户口所在地：")){
					k = k +1;
					hkadr = element.get(k).text().trim();
				}
			}
		}
		
		Elements ele = doc.select("table.grid tr:nth-child(2) td:nth-child(1)");
		String unitNumber = null;
		if(ele.size()>0){
			unitNumber = ele.text().trim();
		}
		String pensionStatus=null;                   //养老保险状态
		String pensionthisTime=null;                  //本次参保时间
		String pensionfirstTime=null;                 //首次时间
		String unemploymentStatus=null;                    //失业保险状态
		String unemploymentthisTime=null;                  //本次参保时间
		String unemploymentfirstTime=null;                 //首次时间
		String medicalStatus=null;                   //医疗保险状态
		String medicalthisTime=null;                  //本次参保时间
		String medicalfirstTime=null;                 //首次时间
		String injuryStatus=null;                    //工伤保险状态
		String injurythisTime=null;                  //本次参保时间
		String injuryfirstTime=null;                //首次时间
		String birthStatus=null;                   //生育保险状态
		String birththisTime=null;                  //本次参保时间
		String birthfirstTime=null;                 //首次时间 
		for(int i = 2;i<7;i++){
			Elements ele1 = doc.select("table.grid tr:nth-child("+i+") td");
			if(ele1.size()>0){
				String xianzhong = ele1.get(1).text().trim();
				if(xianzhong.contains("养老")){
					pensionStatus = ele1.get(2).text().trim();
					pensionthisTime = ele1.get(3).text().trim();
					pensionfirstTime = ele1.get(4).text().trim();
				}
				if(xianzhong.contains("失业")){
					unemploymentStatus = ele1.get(2).text().trim();
					unemploymentthisTime = ele1.get(3).text().trim();
					unemploymentfirstTime = ele1.get(4).text().trim();
				}
				if(xianzhong.contains("医保")){
					medicalStatus = ele1.get(2).text().trim();
					medicalthisTime = ele1.get(3).text().trim();
					medicalfirstTime = ele1.get(4).text().trim();
				}
				if(xianzhong.contains("工伤")){
					injuryStatus = ele1.get(2).text().trim();
					injurythisTime = ele1.get(3).text().trim();
					injuryfirstTime = ele1.get(4).text().trim();
				}
				if(xianzhong.contains("生育")){
					birthStatus = ele1.get(2).text().trim();
					birththisTime = ele1.get(3).text().trim();
					birthfirstTime = ele1.get(4).text().trim();
				}
			}
		}
		
		insuranceHangzhouUserInfo.setName(name);
		insuranceHangzhouUserInfo.setIdNum(idNum);
		insuranceHangzhouUserInfo.setPersonalNumber(personalNumber);
		insuranceHangzhouUserInfo.setSex(sex);
		insuranceHangzhouUserInfo.setNation(nation);
		insuranceHangzhouUserInfo.setBirthdate(birthdate);
		insuranceHangzhouUserInfo.setWorkDate(workDate);
		insuranceHangzhouUserInfo.setPersonnelStatus(personnelStatus);
		insuranceHangzhouUserInfo.setPersonnelType(personnelType);
		insuranceHangzhouUserInfo.setPoliticalOutlook(politicalOutlook);
		insuranceHangzhouUserInfo.setCategory(category);
		insuranceHangzhouUserInfo.setEmploymentForm(employmentForm);
		insuranceHangzhouUserInfo.setEducation(education);
		insuranceHangzhouUserInfo.setHkadr(hkadr);
		insuranceHangzhouUserInfo.setUnitNumber(unitNumber);
		insuranceHangzhouUserInfo.setPensionStatus(pensionStatus);
		insuranceHangzhouUserInfo.setPensionthisTime(pensionthisTime);
		insuranceHangzhouUserInfo.setPensionfirstTime(pensionfirstTime);
		insuranceHangzhouUserInfo.setUnemploymentStatus(unemploymentStatus);
		insuranceHangzhouUserInfo.setUnemploymentthisTime(unemploymentthisTime);
		insuranceHangzhouUserInfo.setUnemploymentfirstTime(unemploymentfirstTime);
		insuranceHangzhouUserInfo.setMedicalStatus(medicalStatus);
		insuranceHangzhouUserInfo.setMedicalthisTime(medicalthisTime);
		insuranceHangzhouUserInfo.setMedicalfirstTime(medicalfirstTime);
		insuranceHangzhouUserInfo.setInjuryStatus(injuryStatus);
		insuranceHangzhouUserInfo.setInjurythisTime(injurythisTime);
		insuranceHangzhouUserInfo.setInjuryfirstTime(injuryfirstTime);
		insuranceHangzhouUserInfo.setBirthStatus(birthStatus);
		insuranceHangzhouUserInfo.setBirththisTime(birththisTime);
		insuranceHangzhouUserInfo.setBirthfirstTime(birthfirstTime);
		insuranceHangzhouUserInfo.setTaskid(taskInsurance.getTaskid());
		return insuranceHangzhouUserInfo;
	}
	
	
	/**
	 * @Des 解析养老保险
	 * @param html
	 * @param taskInsurance
	 * @return 
	 * @return
	 */
	public List<InsurancehangzhouPension> parser(String html,TaskInsurance taskInsurance) {
		List<InsurancehangzhouPension> pensions = new ArrayList<InsurancehangzhouPension>();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("#conditionform1 > table > tbody > tr");
		if(ele.size()>0){
			for(int i = 2;i<=11;i++){
				Elements ele1 = doc.select("table.grid tr:nth-child("+i+") td");
				String payMonth = ele1.get(0).text().trim();					//年月
				String insurance = ele1.get(1).text().trim();;					//险种类型
				String base = ele1.get(2).text().trim();;				        //缴费基数
				String payPerson = ele1.get(3).text().trim();;					//个人缴
				String unitNumber = ele1.get(4).text().trim();;				    //单位编号
				String companyName = ele1.get(5).text().trim();;                 //单位名称
				String type = ele1.get(6).text().trim();;			            //缴费类型
				if (payMonth.equals("")||payMonth.equals(null)){
					break;
				}else{
					InsurancehangzhouPension insurancehangzhouPension = new InsurancehangzhouPension();
					insurancehangzhouPension.setPayMonth(payMonth);
					insurancehangzhouPension.setInsurance(insurance);
					insurancehangzhouPension.setBase(base);
					insurancehangzhouPension.setPayPerson(payPerson);
					insurancehangzhouPension.setUnitNumber(unitNumber);
					insurancehangzhouPension.setCompanyName(companyName);
					insurancehangzhouPension.setType(type);
					insurancehangzhouPension.setTaskid(taskInsurance.getTaskid());
					pensions.add(insurancehangzhouPension);
				}	
				
			}
			return pensions;
		}else{
			return null;
		}
		
		
		
	}
	

	
	/**
	 * @Des 解析养老保险
	 * @param html
	 * @param taskInsurance
	 * @return 
	 * @return
	 */
	public List<InsuranceHangzhouUnemployment> unemployment(String html,TaskInsurance taskInsurance) {
		List<InsuranceHangzhouUnemployment> unempl = new ArrayList<InsuranceHangzhouUnemployment>();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("#conditionform1 > table > tbody > tr");
		if(ele.size()>0){
			for(int i = 2;i<=11;i++){
				Elements ele1 = doc.select("table.grid tr:nth-child("+i+") td");
				String payMonth = ele1.get(0).text().trim();					//年月
				String insurance = ele1.get(1).text().trim();;					//险种类型
				String base = ele1.get(2).text().trim();;				        //缴费基数
				String payPerson = ele1.get(3).text().trim();;					//个人缴
				String unitNumber = ele1.get(4).text().trim();;				    //单位编号
				String companyName = ele1.get(5).text().trim();;                 //单位名称
				String type = ele1.get(6).text().trim();;			            //缴费类型
				if (payMonth.equals("")||payMonth.equals(null)){
					break;
				}else{
					InsuranceHangzhouUnemployment insuranceHangzhouUnemployment = new InsuranceHangzhouUnemployment();
					insuranceHangzhouUnemployment.setPayMonth(payMonth);
					insuranceHangzhouUnemployment.setInsurance(insurance);
					insuranceHangzhouUnemployment.setBase(base);
					insuranceHangzhouUnemployment.setPayPerson(payPerson);
					insuranceHangzhouUnemployment.setUnitNumber(unitNumber);
					insuranceHangzhouUnemployment.setCompanyName(companyName);
					insuranceHangzhouUnemployment.setType(type);
					insuranceHangzhouUnemployment.setTaskid(taskInsurance.getTaskid());
					unempl.add(insuranceHangzhouUnemployment);
				}	
				
			}
			return unempl;
		}else{
			return null;
		}
		
		
		
	}
	
	

/**
 * @Des 解析医疗保险
 * @param html
 * @param taskInsurance
 * @return
 */
	public List<InsuranceHangzhouMedical> medical(String html,TaskInsurance taskInsurance) {
		List<InsuranceHangzhouMedical> medicals = new ArrayList<InsuranceHangzhouMedical>();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("#conditionform1 > table > tbody > tr");
		if(ele.size()>0){
			for(int i = 2;i<=11;i++){
				Elements ele1 = doc.select("table.grid tr:nth-child("+i+") td");
				String payMonth = ele1.get(0).text().trim();					//年月
				String insurance = ele1.get(1).text().trim();;					//险种类型
				String base = ele1.get(2).text().trim();;				        //缴费基数
				String payPerson = ele1.get(3).text().trim();;					//个人缴
				String unitNumber = ele1.get(4).text().trim();;				    //单位编号
				String companyName = ele1.get(5).text().trim();;                 //单位名称
				String type = ele1.get(6).text().trim();;			            //缴费类型
				if (payMonth.equals("")||payMonth.equals(null)){
					break;
				}else{
					InsuranceHangzhouMedical insuranceHangzhouMedical = new InsuranceHangzhouMedical();
					insuranceHangzhouMedical.setPayMonth(payMonth);
					insuranceHangzhouMedical.setInsurance(insurance);
					insuranceHangzhouMedical.setBase(base);
					insuranceHangzhouMedical.setPayPerson(payPerson);
					insuranceHangzhouMedical.setUnitNumber(unitNumber);
					insuranceHangzhouMedical.setCompanyName(companyName);
					insuranceHangzhouMedical.setType(type);
					insuranceHangzhouMedical.setTaskid(taskInsurance.getTaskid());
					medicals.add(insuranceHangzhouMedical);
				}	
				
			}
			return medicals;
		}else{
			return null;
		}
	}

	
	/**
	 * @Des 解析医疗保险
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	public List<InsuranceHangzhouInjury> injury(String html,TaskInsurance taskInsurance) {
		List<InsuranceHangzhouInjury> injurys = new ArrayList<InsuranceHangzhouInjury>();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("#conditionform1 > table > tbody > tr");
		if(ele.size()>0){
			for(int i = 2;i<=11;i++){
				Elements ele1 = doc.select("table.grid tr:nth-child("+i+") td");
				String payMonth = ele1.get(0).text().trim();					//年月
				String insurance = ele1.get(1).text().trim();;					//险种类型
				String base = ele1.get(2).text().trim();;				        //缴费基数
				String payPerson = ele1.get(3).text().trim();;					//个人缴
				String unitNumber = ele1.get(4).text().trim();;				    //单位编号
				String companyName = ele1.get(5).text().trim();;                 //单位名称
				String type = ele1.get(6).text().trim();;			            //缴费类型
				if (payMonth.equals("")||payMonth.equals(null)){
					break;
				}else{
					InsuranceHangzhouInjury insuranceHangzhouInjury = new InsuranceHangzhouInjury();
					insuranceHangzhouInjury.setPayMonth(payMonth);
					insuranceHangzhouInjury.setInsurance(insurance);
					insuranceHangzhouInjury.setBase(base);
					insuranceHangzhouInjury.setPayPerson(payPerson);
					insuranceHangzhouInjury.setUnitNumber(unitNumber);
					insuranceHangzhouInjury.setCompanyName(companyName);
					insuranceHangzhouInjury.setType(type);
					insuranceHangzhouInjury.setTaskid(taskInsurance.getTaskid());
					injurys.add(insuranceHangzhouInjury);
				}	
				
			}
			return injurys;
		}else{
			return null;
		}
	}	
	
	
	
	/**
	 * @Des 解析医疗保险
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	public List<InsuranceHangzhouMaternity> maternity(String html,TaskInsurance taskInsurance) {
		List<InsuranceHangzhouMaternity> maternitys = new ArrayList<InsuranceHangzhouMaternity>();
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("#conditionform1 > table > tbody > tr");
		if(ele.size()>0){
			for(int i = 2;i<=11;i++){
				Elements ele1 = doc.select("table.grid tr:nth-child("+i+") td");
				String payMonth = ele1.get(0).text().trim();					//年月
				String insurance = ele1.get(1).text().trim();;					//险种类型
				String base = ele1.get(2).text().trim();;				        //缴费基数
				String payPerson = ele1.get(3).text().trim();;					//个人缴
				String unitNumber = ele1.get(4).text().trim();;				    //单位编号
				String companyName = ele1.get(5).text().trim();;                 //单位名称
				String type = ele1.get(6).text().trim();;			            //缴费类型
				if (payMonth.equals("")||payMonth.equals(null)){
					break;
				}else{
					InsuranceHangzhouMaternity insuranceHangzhouMaternity = new InsuranceHangzhouMaternity();
					insuranceHangzhouMaternity.setPayMonth(payMonth);
					insuranceHangzhouMaternity.setInsurance(insurance);
					insuranceHangzhouMaternity.setBase(base);
					insuranceHangzhouMaternity.setPayPerson(payPerson);
					insuranceHangzhouMaternity.setUnitNumber(unitNumber);
					insuranceHangzhouMaternity.setCompanyName(companyName);
					insuranceHangzhouMaternity.setType(type);
					insuranceHangzhouMaternity.setTaskid(taskInsurance.getTaskid());
					maternitys.add(insuranceHangzhouMaternity);
				}	
				
			}
			return maternitys;
		}else{
			return null;
		}
	}	
}
