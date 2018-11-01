package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.sz.shanxi3.InsuranceShanXi3Injury;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi3.InsuranceShanXi3InjuryPerson;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi3.InsuranceShanXi3Medical;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi3.InsuranceShanXi3MedicalPerson;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi3.InsuranceShanXi3Pension;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi3.InsuranceShanXi3PensionPerson;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi3.InsuranceShanXi3Unemployment;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi3.InsuranceShanXi3UnemploymentPerson;

import app.commontracerlog.TracerLog;
import app.service.InsuranceService;

@Component
public class InsuranceSZShanxiParser {
	
	@Autowired
	private TracerLog tracer; 
	@Autowired
	private InsuranceService insuranceService;

	/**
	 * 解析养老个人信息
	 * @param asXml
	 * @return
	 */
	public InsuranceShanXi3PensionPerson parserPensionPerson(String asXml) {
		InsuranceShanXi3PensionPerson insuranceShanXi3PensionPerson = new InsuranceShanXi3PensionPerson();
		Document doc = Jsoup.parse(asXml);
		
		String uname = insuranceService.getNextLabelByKeyword(doc,"姓名");
		String statePersonnel = insuranceService.getNextLabelByKeyword(doc,"人员状态");
		String paymentStatus = insuranceService.getNextLabelByKeyword(doc,"缴费状态");
		String organizationDate = insuranceService.getNextLabelByKeyword(doc,"参保日期");
		String cumulativeBalance = insuranceService.getNextLabelByKeyword(doc,"当前个人账户累积储存额");
		String organizationName = insuranceService.getNextLabelByKeyword(doc,"经办机构名称");
		
		tracer.addTag("姓名：", uname);
		tracer.addTag("人员状态：", statePersonnel);
		tracer.addTag("缴费状态：", paymentStatus);
		tracer.addTag("参保日期：", organizationDate);
		tracer.addTag("当前个人账户累积储存额：", cumulativeBalance);
		tracer.addTag("经办机构名称：", organizationName);
		
		insuranceShanXi3PensionPerson.setCumulativeBalance(cumulativeBalance);
		insuranceShanXi3PensionPerson.setOrganizationDate(organizationDate);
		insuranceShanXi3PensionPerson.setOrganizationName(organizationName);
		insuranceShanXi3PensionPerson.setPaymentStatus(paymentStatus);
		insuranceShanXi3PensionPerson.setStatePersonnel(statePersonnel);
		insuranceShanXi3PensionPerson.setUname(uname);

		return insuranceShanXi3PensionPerson;
	}

	/**
	 * 解析养老保险明细
	 * @param asXml
	 * @param taskid
	 * @return
	 */
	public List<InsuranceShanXi3Pension> parserPension(String asXml, String taskid) {
		List<InsuranceShanXi3Pension> list = new ArrayList<InsuranceShanXi3Pension>();
		Document doc = Jsoup.parse(asXml);
		Element tb1 = doc.getElementById("tb1");
		if(null != tb1){
			Elements trs = tb1.select("tr");
			if(null != trs && trs.size()>0){
				for(Element tr : trs){
					InsuranceShanXi3Pension insuranceShanXi3Pension = new InsuranceShanXi3Pension();
					String year = tr.child(0).text();
					String yearPayMonthSum = tr.child(1).text();
					String payPotency = tr.child(2).text();
					String payPersonCount = tr.child(3).text();
					String payUnitCount = tr.child(4).text();
					String allPayMonthSum = tr.child(5).text();
					
					tracer.addTag("年度：",year);
					tracer.addTag("当年缴费月数：",yearPayMonthSum);
					tracer.addTag("当年实缴缴费基数：",payPotency);
					tracer.addTag("当年个人缴划账户：",payPersonCount);
					tracer.addTag("当年单位缴划个人账户：",payUnitCount);
					tracer.addTag("截止上年末累计缴费月数：",allPayMonthSum);
					
					insuranceShanXi3Pension.setAllPayMonthSum(allPayMonthSum);
					insuranceShanXi3Pension.setPayPersonCount(payPersonCount);
					insuranceShanXi3Pension.setPayPotency(payPotency);
					insuranceShanXi3Pension.setPayUnitCount(payUnitCount);
					insuranceShanXi3Pension.setTaskid(taskid);
					insuranceShanXi3Pension.setYear(year);
					insuranceShanXi3Pension.setYearPayMonthSum(yearPayMonthSum);
					
					list.add(insuranceShanXi3Pension);

				}
			}
		}
		return list;
	}

	/**
	 * 解析城镇医疗保险个人信息
	 * @param asXml
	 * @return
	 */
	public InsuranceShanXi3MedicalPerson parserMedicalPerson(String asXml) {
		
		InsuranceShanXi3MedicalPerson insuranceShanXi3MedicalPerson = new InsuranceShanXi3MedicalPerson();
		Document doc = Jsoup.parse(asXml);
		
		String uname = insuranceService.getNextLabelByKeyword(doc,"姓名");
		String idnum = insuranceService.getNextLabelByKeyword(doc,"公民身份号码");
		String personType = insuranceService.getNextLabelByKeyword(doc,"人员类别");
		String sex = insuranceService.getNextLabelByKeyword(doc,"性别");
		String nation = insuranceService.getNextLabelByKeyword(doc,"民族");
		String birthdate = insuranceService.getNextLabelByKeyword(doc,"出生日期");
		String phone = insuranceService.getNextLabelByKeyword(doc,"联系电话");
		String zipCode = insuranceService.getNextLabelByKeyword(doc,"邮政编码");
		String address = insuranceService.getNextLabelByKeyword(doc,"地址");
		String firstParticipationDate = insuranceService.getNextLabelByKeyword(doc,"首次参保日期");
		String participationStatus = insuranceService.getNextLabelByKeyword(doc,"参保状态");
		String accountBalance = insuranceService.getNextLabelByKeyword(doc,"当前个人帐户余额");
		String usageAmount = insuranceService.getNextLabelByKeyword(doc,"个人账户使用金额");
		String asAmount = insuranceService.getNextLabelByKeyword(doc,"个人账户划入金额");
		
		insuranceShanXi3MedicalPerson.setAccountBalance(accountBalance);
		insuranceShanXi3MedicalPerson.setAddress(address);
		insuranceShanXi3MedicalPerson.setAsAmount(asAmount);
		insuranceShanXi3MedicalPerson.setBirthdate(birthdate);
		insuranceShanXi3MedicalPerson.setFirstParticipationDate(firstParticipationDate);
		insuranceShanXi3MedicalPerson.setIdnum(idnum);
		insuranceShanXi3MedicalPerson.setNation(nation);
		insuranceShanXi3MedicalPerson.setParticipationStatus(participationStatus);
		insuranceShanXi3MedicalPerson.setPersonType(personType);
		insuranceShanXi3MedicalPerson.setPhone(phone);
		insuranceShanXi3MedicalPerson.setSex(sex);
		insuranceShanXi3MedicalPerson.setUname(uname);
		insuranceShanXi3MedicalPerson.setUsageAmount(usageAmount);
		insuranceShanXi3MedicalPerson.setZipCode(zipCode);

		return insuranceShanXi3MedicalPerson;
	}

	/**
	 * 解析医疗保险缴费明细
	 * @param asXml
	 * @param taskid
	 * @return
	 */
	public List<InsuranceShanXi3Medical> parserMedical(String asXml, String taskid) {
		
		List<InsuranceShanXi3Medical> list = new ArrayList<InsuranceShanXi3Medical>();
		Document doc = Jsoup.parse(asXml);
		Element tb1 = doc.getElementById("tb1");
		if(null != tb1){
			Elements trs = tb1.select("tr");
			if(null != trs && trs.size()>0){
				for(Element tr : trs){
					if(tr.select("td").size()>8){
						String year = tr.child(0).text();
						String insuranceType = tr.child(1).text();
						String payType = tr.child(2).text();
						String paymentBase = tr.child(3).text();
						String payScale = tr.child(4).text();
						String payMoney = tr.child(5).text();
						String asAccountScale = tr.child(6).text();
						String asAccountMoney = tr.child(7).text();
						String receivedDate = tr.child(8).text();
						
						InsuranceShanXi3Medical insuranceShanXi3Medical = new InsuranceShanXi3Medical();
						insuranceShanXi3Medical.setAsAccountMoney(asAccountMoney);
						insuranceShanXi3Medical.setAsAccountScale(asAccountScale);
						insuranceShanXi3Medical.setInsuranceType(insuranceType);
						insuranceShanXi3Medical.setPaymentBase(paymentBase);
						insuranceShanXi3Medical.setPayMoney(payMoney);
						insuranceShanXi3Medical.setPayScale(payScale);
						insuranceShanXi3Medical.setPayType(payType);
						insuranceShanXi3Medical.setReceivedDate(receivedDate);
						insuranceShanXi3Medical.setTaskid(taskid);
						insuranceShanXi3Medical.setYear(year);
						
						list.add(insuranceShanXi3Medical);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 解析失业保险
	 * @param asXml
	 * @return
	 */
	public InsuranceShanXi3UnemploymentPerson parserUnemploymentPerson(String asXml) {
		InsuranceShanXi3UnemploymentPerson insuranceShanXi3UnemploymentPerson = new InsuranceShanXi3UnemploymentPerson();
		Document doc = Jsoup.parse(asXml);
		
		String uname = insuranceService.getNextLabelByKeyword(doc,"姓名");
		String personStatus = insuranceService.getNextLabelByKeyword(doc,"人员状态");
		String payType = insuranceService.getNextLabelByKeyword(doc,"缴费状态");
		String registerDate = insuranceService.getNextLabelByKeyword(doc,"失业登记时间");
		String getDate = insuranceService.getNextLabelByKeyword(doc,"待遇开始领取时间");
		String enjoyMonth = insuranceService.getNextLabelByKeyword(doc,"应享受月数");
		String enjoyedMonth = insuranceService.getNextLabelByKeyword(doc,"已享受月数");
		String residueEnjoyMonth = insuranceService.getNextLabelByKeyword(doc,"剩余享受月数");
		String grantStatus = insuranceService.getNextLabelByKeyword(doc,"发放状态");
		String participationDate = insuranceService.getNextLabelByKeyword(doc,"参保日期");
		String organization = insuranceService.getNextLabelByKeyword(doc,"经办机构名称");
		
		insuranceShanXi3UnemploymentPerson.setEnjoyedMonth(enjoyedMonth);
		insuranceShanXi3UnemploymentPerson.setGetDate(getDate);
		insuranceShanXi3UnemploymentPerson.setGrantStatus(grantStatus);
		insuranceShanXi3UnemploymentPerson.setOrganization(organization);
		insuranceShanXi3UnemploymentPerson.setParticipationDate(participationDate);
		insuranceShanXi3UnemploymentPerson.setPayType(payType);
		insuranceShanXi3UnemploymentPerson.setPersonStatus(personStatus);
		insuranceShanXi3UnemploymentPerson.setRegisterDate(registerDate);
		insuranceShanXi3UnemploymentPerson.setResidueEnjoyMonth(residueEnjoyMonth);
		insuranceShanXi3UnemploymentPerson.setUname(uname);
		insuranceShanXi3UnemploymentPerson.setEnjoyMonth(enjoyMonth);
		
		return insuranceShanXi3UnemploymentPerson;
	}

	/**
	 * 解析失业保险
	 * @param asXml
	 * @param taskid
	 * @return
	 */
	public List<InsuranceShanXi3Unemployment> parserUnemployment(String asXml, String taskid) {
		List<InsuranceShanXi3Unemployment> list = new ArrayList<InsuranceShanXi3Unemployment>();
		Document doc = Jsoup.parse(asXml);
		Element tb1 = doc.getElementById("tb1");
		if(null != tb1){
			Elements trs = tb1.select("tr");
			if(null != trs && trs.size()>0){
				for(Element tr : trs){
					if(tr.select("td").size()>6){
						String year = tr.child(0).text();
						String payType = tr.child(1).text();
						String paymentBase = tr.child(2).text();
						String personPayScale = tr.child(3).text();
						String organizationPayScale = tr.child(4).text();
						String payMoney = tr.child(5).text();
						String accountDate = tr.child(6).text();
						
						InsuranceShanXi3Unemployment insuranceShanXi3Unemployment = new InsuranceShanXi3Unemployment();
						insuranceShanXi3Unemployment.setAccountDate(accountDate);
						insuranceShanXi3Unemployment.setOrganizationPayScale(organizationPayScale);
						insuranceShanXi3Unemployment.setPaymentBase(paymentBase);
						insuranceShanXi3Unemployment.setPayMoney(payMoney);
						insuranceShanXi3Unemployment.setPayType(payType);
						insuranceShanXi3Unemployment.setPersonPayScale(personPayScale);
						insuranceShanXi3Unemployment.setTaskid(taskid);
						insuranceShanXi3Unemployment.setYear(year);
						list.add(insuranceShanXi3Unemployment);
						
					}
				}
			}
		}
		return list;
	}

	/**
	 * 解析工伤保险个人信息
	 * @param asXml
	 * @return
	 */
	public InsuranceShanXi3InjuryPerson parserInjuryPerson(String asXml) {
		InsuranceShanXi3InjuryPerson insuranceShanXi3InjuryPerson = new InsuranceShanXi3InjuryPerson();
		Document doc = Jsoup.parse(asXml);
		
		String uname = insuranceService.getNextLabelByKeyword(doc,"姓名");
		String personStatus = insuranceService.getNextLabelByKeyword(doc,"人员状态");
		String payType = insuranceService.getNextLabelByKeyword(doc,"缴费状态");
		String participationDate = insuranceService.getNextLabelByKeyword(doc,"参保日期");
		String organization = insuranceService.getNextLabelByKeyword(doc,"经办机构名称");
		
		insuranceShanXi3InjuryPerson.setOrganization(organization);
		insuranceShanXi3InjuryPerson.setParticipationDate(participationDate);
		insuranceShanXi3InjuryPerson.setPayType(payType);
		insuranceShanXi3InjuryPerson.setPersonStatus(personStatus);
		insuranceShanXi3InjuryPerson.setUname(uname);
		return insuranceShanXi3InjuryPerson;
	}

	/**
	 * 解析工伤保险明细
	 * @param asXml
	 * @param taskid
	 * @return
	 */
	public List<InsuranceShanXi3Injury> parserInjury(String asXml, String taskid) {
		List<InsuranceShanXi3Injury> list = new ArrayList<InsuranceShanXi3Injury>();
		Document doc = Jsoup.parse(asXml);
		Element tb1 = doc.getElementById("tb1");
		if(null != tb1){
			Elements trs = tb1.select("tr");
			if(null != trs && trs.size()>0){
				for(Element tr : trs){
					if(tr.select("td").size()>4){
						String year = tr.child(0).text();
						String payType = tr.child(1).text();
						String paymentBase = tr.child(2).text();
						String organizationPayScale = tr.child(3).text();
						String accountDate = tr.child(4).text();
						
						InsuranceShanXi3Injury insuranceShanXi3Injury = new InsuranceShanXi3Injury();
						insuranceShanXi3Injury.setAccountDate(accountDate);
						insuranceShanXi3Injury.setOrganizationPayScale(organizationPayScale);
						insuranceShanXi3Injury.setPaymentBase(paymentBase);
						insuranceShanXi3Injury.setPayType(payType);
						insuranceShanXi3Injury.setTaskid(taskid);
						insuranceShanXi3Injury.setYear(year);
						list.add(insuranceShanXi3Injury);					
					}
				}
			}
		}
		return list;
	}

}
