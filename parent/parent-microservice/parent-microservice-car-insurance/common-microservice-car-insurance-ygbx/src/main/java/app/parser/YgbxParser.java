package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.car.insurance.ygbx.CarInsuranceYGbxAssuredResult;
import com.microservice.dao.entity.crawler.car.insurance.ygbx.CarInsuranceYGbxCoverageResult;
import com.microservice.dao.entity.crawler.car.insurance.ygbx.CarInsuranceYGbxHtml;
import com.microservice.dao.entity.crawler.car.insurance.ygbx.CarInsuranceYGbxInsurerResult;

@Component
public class YgbxParser {
	
	private Document doc = null;
	
	/**
	 * 解析被投保人信息
	 * @param text
	 */
	public CarInsuranceYGbxAssuredResult parserCarInsuranceYGbxAssuredResult(String text,String taskid) throws Exception{
		doc = Jsoup.parse(text);
		String applicant = doc.select("td:contains(投保人：)").first().nextElementSibling().text();
		String policyNo = doc.select("td:contains(保险单号码：)").first().nextElementSibling().text();
		String theApplicant = doc.select("td:contains(名称)").first().nextElementSibling().text();
		String idNum = doc.select("td:contains(证件号码)").first().nextElementSibling().text();
		String address = doc.select("td:contains(地址)").first().nextElementSibling().text();
		String phone = doc.select("td:contains(联系方式)").first().nextElementSibling().text();
		String carOwner = doc.select("td:contains(行驶证车主)").first().nextElementSibling().text();
		CarInsuranceYGbxAssuredResult carInsuranceYGbxAssuredResult = new CarInsuranceYGbxAssuredResult(taskid, applicant, policyNo, theApplicant, idNum, address, phone, carOwner);
		return carInsuranceYGbxAssuredResult;
	}
	
	
	/**
	 * 解析投保人信息
	 * @param text
	 */
	public CarInsuranceYGbxInsurerResult parserCarInsuranceYGbxInsurerResult(String text, String taskid) throws Exception{
		String acknowledgementDate = doc.select("td:contains(收款确认：)").first().text().replace("收款确认：", "");
		String generatePolicyDate = doc.select("td:contains(生成保单：)").first().text().replace("生成保单：", "");
		String electronPolicyPipelineNo = doc.select("td:contains(电子保单流水号：)").first().text().replace("电子保单流水号：", "");
		String electronPolicyPipelineDate = doc.select("td:contains(电子保单生成时间：)").first().text().replace("电子保单生成时间：", "");
		String confirmationCode = doc.select("td:contains(确认码：)").first().text().replace("确认码：", "");
		String contractDisputeSolveType = doc.select("td:contains(保险合同争议解决方式)").first().nextElementSibling().text();
		String insuranceDate = doc.select("td:contains(保险期间：)").first().text().replace("保险期间：", "");
		String insuranceCompanyName = doc.select("td:contains(公司名称：)").first().text().replace("公司名称：", "");
		String insuranceCompanySite = doc.select("td:contains(公司网址：)").first().text().replace("公司网址：", "");
		String insuranceCompanyCode = doc.select("td:contains(邮政编码：)").first().text().replace("邮政编码：", "");
		String insuranceCompanyPhone = doc.select("td:contains(联系电话：)").first().text().replace("联系电话：", "");
		String insuranceCompanyAddress = doc.select("td:contains(公司地址：)").first().text().replace("公司地址：", "");
		String signingDate = doc.select("td:contains(签单日期：)").first().text().replace("签单日期：", "");
		String underwriter = doc.select("td:contains(核保：)").first().text().replace("核保：", "");
		String touching = doc.select("td:contains(制单：)").first().text().replace("制单：", "");
		String handle = doc.select("td:contains(经办：)").first().text().replace("经办：", "");
		String nationalUnifiedCallService = doc.select("td:contains(全国统一客户服务和客户维权电话：)").first().text().replace("全国统一客户服务和客户维权电话：", "");
		String ygbxInsuranceCallService = doc.select("td:contains(阳光保险电话车险：)").first().text().replace("阳光保险电话车险：", "");
		String ygbxSite = doc.select("td:contains(阳光网上车险：)").first().text().replace("阳光网上车险：", "");
		String specialAgreement = doc.select("td:contains(特别约定)").first().nextElementSibling().text();
		String importantNote = doc.select("td:contains(重要提示)").first().nextElementSibling().text();
		
		CarInsuranceYGbxInsurerResult carInsuranceYGbxInsurerResult = new CarInsuranceYGbxInsurerResult("111", acknowledgementDate, generatePolicyDate, electronPolicyPipelineNo, electronPolicyPipelineDate, confirmationCode, contractDisputeSolveType, insuranceDate, insuranceCompanyName, insuranceCompanySite, insuranceCompanyCode, insuranceCompanyPhone, insuranceCompanyAddress, signingDate, underwriter, touching, handle, nationalUnifiedCallService, ygbxInsuranceCallService, ygbxSite, specialAgreement, importantNote);
		return carInsuranceYGbxInsurerResult;
	}


	/**
	 * 初始化网页源码表
	 * @param text
	 * @param taskid
	 */
	public CarInsuranceYGbxHtml parserCarInsuranceYGbxHtml(String text, String taskid) {
		CarInsuranceYGbxHtml carInsuranceYGbxHtml = new CarInsuranceYGbxHtml(taskid, text);
		return carInsuranceYGbxHtml;
	}
	
	
	/**
	 * 解析保险金额缴费明细表
	 * @param text
	 */
	public List<CarInsuranceYGbxCoverageResult> parserCarInsuranceYGbxCoverageResult(String text, 
			String taskid) throws Exception{
		List<CarInsuranceYGbxCoverageResult> list = new ArrayList<CarInsuranceYGbxCoverageResult>();
		
		String amountCovered1 = doc.select("td:contains(机动车损失保险)").first().nextElementSibling().text();
		String deductibleExcess1 = doc.select("td:contains(机动车损失保险)").first().nextElementSibling().nextElementSibling().text();
		String insurance1 = doc.select("td:contains(机动车损失保险)").first().nextElementSibling().nextElementSibling().nextElementSibling().text();
		String type1 = "机动车损失保险";
		
		CarInsuranceYGbxCoverageResult carInsuranceYGbxCoverageResult1 = new CarInsuranceYGbxCoverageResult(taskid, amountCovered1, insurance1, deductibleExcess1, type1);
		list.add(carInsuranceYGbxCoverageResult1);
		
		String amountCovered2 = doc.select("td:contains(第三者责任保险)").first().nextElementSibling().text();
		String deductibleExcess2 = doc.select("td:contains(第三者责任保险)").first().nextElementSibling().nextElementSibling().text();
		String insurance2 = doc.select("td:contains(第三者责任保险)").first().nextElementSibling().nextElementSibling().nextElementSibling().text();
		String type2 = "第三者责任保险";
		
		CarInsuranceYGbxCoverageResult carInsuranceYGbxCoverageResult2 = new CarInsuranceYGbxCoverageResult(taskid, amountCovered2, insurance2, deductibleExcess2, type2);
		list.add(carInsuranceYGbxCoverageResult2);
		
		String amountCovered3 = doc.select("td:contains(全车盗抢保险)").first().nextElementSibling().text();
		String deductibleExcess3 = doc.select("td:contains(全车盗抢保险)").first().nextElementSibling().nextElementSibling().text();
		String insurance3 = doc.select("td:contains(全车盗抢保险)").first().nextElementSibling().nextElementSibling().nextElementSibling().text();
		String type3 = "全车盗抢保险";
		
		CarInsuranceYGbxCoverageResult carInsuranceYGbxCoverageResult3 = new CarInsuranceYGbxCoverageResult(taskid, amountCovered3, insurance3, deductibleExcess3, type3);
		list.add(carInsuranceYGbxCoverageResult3);
		
		String amountCovered4 = doc.select("td:contains(不计免赔率险)").first().nextElementSibling().text();
		String deductibleExcess4 = doc.select("td:contains(不计免赔率险)").first().nextElementSibling().nextElementSibling().text();
		String insurance4 = doc.select("td:contains(不计免赔率险)").first().nextElementSibling().nextElementSibling().nextElementSibling().text();
		String type4 = "不计免赔率险";
		
		CarInsuranceYGbxCoverageResult carInsuranceYGbxCoverageResult4 = new CarInsuranceYGbxCoverageResult(taskid, amountCovered4, insurance4, deductibleExcess4, type4);
		list.add(carInsuranceYGbxCoverageResult4);
		
		String amountCovered5 = doc.select("td:contains(机动车损失保险无法找到第三方特约险)").first().nextElementSibling().text();
		String deductibleExcess5 = doc.select("td:contains(机动车损失保险无法找到第三方特约险)").first().nextElementSibling().nextElementSibling().text();
		String insurance5 = doc.select("td:contains(机动车损失保险无法找到第三方特约险)").first().nextElementSibling().nextElementSibling().nextElementSibling().text();
		String type5 = "机动车损失保险无法找到第三方特约险";
		
		CarInsuranceYGbxCoverageResult carInsuranceYGbxCoverageResult5 = new CarInsuranceYGbxCoverageResult(taskid, amountCovered5, insurance5, deductibleExcess5, type5);
		list.add(carInsuranceYGbxCoverageResult5);
		
		String amountCovered6 = doc.select("td:contains(玻璃单独破碎险)").first().nextElementSibling().text();
		String deductibleExcess6 = doc.select("td:contains(玻璃单独破碎险)").first().nextElementSibling().nextElementSibling().text();
		String insurance6 = doc.select("td:contains(玻璃单独破碎险)").first().nextElementSibling().nextElementSibling().nextElementSibling().text();
		String type6 = "玻璃单独破碎险";
		
		CarInsuranceYGbxCoverageResult carInsuranceYGbxCoverageResult6 = new CarInsuranceYGbxCoverageResult(taskid, amountCovered6, insurance6, deductibleExcess6, type6);
		list.add(carInsuranceYGbxCoverageResult6);
		
		String amountCovered7 = doc.select("td:contains(指定修理厂险)").first().nextElementSibling().text();
		String deductibleExcess7 = doc.select("td:contains(指定修理厂险)").first().nextElementSibling().nextElementSibling().text();
		String insurance7 = doc.select("td:contains(指定修理厂险)").first().nextElementSibling().nextElementSibling().nextElementSibling().text();
		String type7 = "指定修理厂险";
		
		CarInsuranceYGbxCoverageResult carInsuranceYGbxCoverageResult7 = new CarInsuranceYGbxCoverageResult(taskid, amountCovered7, insurance7, deductibleExcess7, type7);
		list.add(carInsuranceYGbxCoverageResult7);
		
		String amountCovered8 = doc.select("td:contains(车身划痕损失险)").first().nextElementSibling().text();
		String deductibleExcess8 = doc.select("td:contains(车身划痕损失险)").first().nextElementSibling().nextElementSibling().text();
		String insurance8 = doc.select("td:contains(车身划痕损失险)").first().nextElementSibling().nextElementSibling().nextElementSibling().text();
		String type8 = "车身划痕损失险";
		
		CarInsuranceYGbxCoverageResult carInsuranceYGbxCoverageResult8 = new CarInsuranceYGbxCoverageResult(taskid, amountCovered8, insurance8, deductibleExcess8, type8);
		list.add(carInsuranceYGbxCoverageResult8);
		
		return list;
	}

}
