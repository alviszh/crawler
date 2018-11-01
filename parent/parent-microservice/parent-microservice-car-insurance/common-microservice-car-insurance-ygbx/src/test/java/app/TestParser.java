package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.microservice.dao.entity.crawler.car.insurance.ygbx.CarInsuranceYGbxAssuredResult;
import com.microservice.dao.entity.crawler.car.insurance.ygbx.CarInsuranceYGbxInsurerResult;

public class TestParser {
	
	public static void main(String[] args) {
		String text = readTxtFile("C:\\home\\reaule.txt");
//		System.out.println(text);
//		parserCarInsuranceYGbxAssuredResult(text);
//		parserCarInsuranceYGbxInsurerResult(text);
		parserCarInsuranceYGbxCoverageResult(text);
	}

	
	/**
	 * 解析保险金额缴费明细表
	 * @param text
	 */
	private static void parserCarInsuranceYGbxCoverageResult(String text) {
		Document doc = Jsoup.parse(text);
		String amountCovered1 = doc.select("td:contains(机动车损失保险)").first().nextElementSibling().text();
		String deductibleExcess1 = doc.select("td:contains(机动车损失保险)").first().nextElementSibling().nextElementSibling().text();
		String insurance1 = doc.select("td:contains(机动车损失保险)").first().nextElementSibling().nextElementSibling().nextElementSibling().text();
		String type1 = "机动车损失保险";
		
		String amountCovered2 = doc.select("td:contains(第三者责任保险)").first().nextElementSibling().text();
		String deductibleExcess2 = doc.select("td:contains(第三者责任保险)").first().nextElementSibling().nextElementSibling().text();
		String insurance2 = doc.select("td:contains(第三者责任保险)").first().nextElementSibling().nextElementSibling().nextElementSibling().text();
		String type2 = "第三者责任保险";
		
		String amountCovered3 = doc.select("td:contains(全车盗抢保险)").first().nextElementSibling().text();
		String deductibleExcess3 = doc.select("td:contains(全车盗抢保险)").first().nextElementSibling().nextElementSibling().text();
		String insurance3 = doc.select("td:contains(全车盗抢保险)").first().nextElementSibling().nextElementSibling().nextElementSibling().text();
		String type3 = "全车盗抢保险";
		
		String amountCovered4 = doc.select("td:contains(不计免赔率险)").first().nextElementSibling().text();
		String deductibleExcess4 = doc.select("td:contains(不计免赔率险)").first().nextElementSibling().nextElementSibling().text();
		String insurance4 = doc.select("td:contains(不计免赔率险)").first().nextElementSibling().nextElementSibling().nextElementSibling().text();
		String type4 = "不计免赔率险";
		
		String amountCovered5 = doc.select("td:contains(机动车损失保险无法找到第三方特约险)").first().nextElementSibling().text();
		String deductibleExcess5 = doc.select("td:contains(机动车损失保险无法找到第三方特约险)").first().nextElementSibling().nextElementSibling().text();
		String insurance5 = doc.select("td:contains(机动车损失保险无法找到第三方特约险)").first().nextElementSibling().nextElementSibling().nextElementSibling().text();
		String type5 = "机动车损失保险无法找到第三方特约险";
		
		String amountCovered6 = doc.select("td:contains(玻璃单独破碎险)").first().nextElementSibling().text();
		String deductibleExcess6 = doc.select("td:contains(玻璃单独破碎险)").first().nextElementSibling().nextElementSibling().text();
		String insurance6 = doc.select("td:contains(玻璃单独破碎险)").first().nextElementSibling().nextElementSibling().nextElementSibling().text();
		String type6 = "玻璃单独破碎险";
		
		String amountCovered7 = doc.select("td:contains(指定修理厂险)").first().nextElementSibling().text();
		String deductibleExcess7 = doc.select("td:contains(指定修理厂险)").first().nextElementSibling().nextElementSibling().text();
		String insurance7 = doc.select("td:contains(指定修理厂险)").first().nextElementSibling().nextElementSibling().nextElementSibling().text();
		String type7 = "指定修理厂险";
		
		String amountCovered8 = doc.select("td:contains(车身划痕损失险)").first().nextElementSibling().text();
		String deductibleExcess8 = doc.select("td:contains(车身划痕损失险)").first().nextElementSibling().nextElementSibling().text();
		String insurance8 = doc.select("td:contains(车身划痕损失险)").first().nextElementSibling().nextElementSibling().nextElementSibling().text();
		String type8 = "车身划痕损失险";
		
		
	}


	/**
	 * 解析投保人信息
	 * @param text
	 */
	private static void parserCarInsuranceYGbxInsurerResult(String text) {
		Document doc = Jsoup.parse(text);
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
		System.out.println(carInsuranceYGbxInsurerResult.toString());
	}


	/**
	 * 解析被投保人信息
	 * @param text
	 */
	private static void parserCarInsuranceYGbxAssuredResult(String text) {
		Document doc = Jsoup.parse(text);
		String applicant = doc.select("td:contains(投保人：)").first().nextElementSibling().text();
		String policyNo = doc.select("td:contains(保险单号码：)").first().nextElementSibling().text();
		String theApplicant = doc.select("td:contains(名称)").first().nextElementSibling().text();
		String idNum = doc.select("td:contains(证件号码)").first().nextElementSibling().text();
		String address = doc.select("td:contains(地址)").first().nextElementSibling().text();
		String phone = doc.select("td:contains(联系方式)").first().nextElementSibling().text();
		String carOwner = doc.select("td:contains(行驶证车主)").first().nextElementSibling().text();
		CarInsuranceYGbxAssuredResult carInsuranceYGbxAssuredResult = new CarInsuranceYGbxAssuredResult("111", applicant, policyNo, theApplicant, idNum, address, phone, carOwner);
		System.out.println(carInsuranceYGbxAssuredResult.toString());
	}


	/**
	 * @param filePath
	 * @return
	 */
	public static String readTxtFile(String filePath) {
		 String text = "";
	     try {
	         File file = new File(filePath);
	         if (file.isFile() && file.exists()) {
	             InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
	             BufferedReader br = new BufferedReader(isr);
	             String lineTxt;
				while ((lineTxt = br.readLine()) != null) {
					text = text+lineTxt;
	             }
	             br.close();
	         } else {
	             System.out.println("文件不存在!");
	         }
	     } catch (Exception e) {
	         System.out.println("文件读取错误!");
	     }
		return text;

	    }
}
