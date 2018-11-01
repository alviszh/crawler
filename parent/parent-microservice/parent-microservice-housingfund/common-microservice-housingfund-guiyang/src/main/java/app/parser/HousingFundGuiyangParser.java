package app.parser;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.guiyang.HousingGuiyangDetailAccount;
import com.microservice.dao.entity.crawler.housing.guiyang.HousingGuiyangUserinfo;

@Component
public class HousingFundGuiyangParser {
	

	/**
	 * @Des 解析用户信息
	 * @param page
	 * @param taskHousing
	 */
	public HousingGuiyangUserinfo parserUserInfo(HtmlPage page, TaskHousing taskHousing) {
		
		HousingGuiyangUserinfo housingGuiyangUserinfo = new HousingGuiyangUserinfo();
		Document doc = Jsoup.parse(page.asXml());
		
		String companyName = getElementText(doc,"td:contains(单位名称)").replace("单位名称：", "");
		String companyAccount = getElementText(doc,"td:contains(单位帐号)").replace("单位帐号：", "");	
		Element e = doc.getElementsByClass("table-whole").first();
		String personalAccount = getNextElementText(e,"td:contains(个人公积金帐号)");
		String name = getNextElementText(e,"td:contains(姓名)");
		String idnum = getNextElementText(e,"td:contains(身份证号)");
		String sex = getNextElementText(e,"td:contains(性别)");
		String phoneNum = getNextElementText(e,"td:contains(手机号)");
		String cardNum = getNextElementText(e,"td:contains(卡号)");
		String wageBase = getNextElementText(e,"td:contains(工资基数)").replace("￥", "").replace("元", "");
		String depositAmount = getNextElementText(e,"td:contains(月应缴额)").replace("￥", "").replace("元", "");
		String companyPayPercent = getNextElementText(e,"td:contains(单位缴存比例)");
		String employeePayPercent = getNextElementText(e,"td:contains(职工缴存比例)");
		String companyDepositAmount = getNextElementText(e,"td:contains(单位月应缴存额)").replace("￥", "").replace("元", "");
		String employeeDepositAmount = getNextElementText(e,"td:contains(职工月应缴额)").replace("￥", "").replace("元", "");
		String openDate = getNextElementText(e,"td:contains(开户日期)");
		String startYear = getNextElementText(e,"td:contains(起缴年月)");
		String employeePayYear = getNextElementText(e,"td:contains(职工汇缴年月)");
		String remitStatus = getNextElementText(e,"td:contains(汇缴状态)");
		String managementDepartment = getNextElementText(e,"td:contains(所属管理部)");
		String closingAccountDate = getNextElementText(e,"td:contains(汇缴销户时间)");
		String isFreeze = getNextElementText(e,"td:contains(是否冻结)");
		String balance = getNextElementText(e,"td:contains(当前余额)").replace("￥", "").replace("元", "");
		String isLoans = getNextElementText(e,"td:contains(是否贷款)");
		String companyOperator = getNextElementText(e,"td:contains(单位经办人)");
		String companyLegalPerson = getNextElementText(e,"td:contains(单位法人)");
		String companyAddress = getNextElementText(e,"td:contains(单位地址)");
		
		housingGuiyangUserinfo.setBalance(balance);
		housingGuiyangUserinfo.setCardNum(cardNum);
		housingGuiyangUserinfo.setClosingAccountDate(closingAccountDate);
		housingGuiyangUserinfo.setCompanyAccount(companyAccount);
		housingGuiyangUserinfo.setCompanyAddress(companyAddress);
		housingGuiyangUserinfo.setCompanyDepositAmount(companyDepositAmount);
		housingGuiyangUserinfo.setCompanyLegalPerson(companyLegalPerson);
		housingGuiyangUserinfo.setCompanyName(companyName);
		housingGuiyangUserinfo.setCompanyOperator(companyOperator);
		housingGuiyangUserinfo.setCompanyPayPercent(companyPayPercent);
		housingGuiyangUserinfo.setDepositAmount(depositAmount);
		housingGuiyangUserinfo.setEmployeeDepositAmount(employeeDepositAmount);
		housingGuiyangUserinfo.setEmployeePayPercent(employeePayPercent);
		housingGuiyangUserinfo.setEmployeePayYear(employeePayYear);
		housingGuiyangUserinfo.setIdnum(idnum);
		housingGuiyangUserinfo.setIsFreeze(isFreeze);
		housingGuiyangUserinfo.setIsLoans(isLoans);
		housingGuiyangUserinfo.setManagementDepartment(managementDepartment);
		housingGuiyangUserinfo.setName(name);
		housingGuiyangUserinfo.setOpenDate(openDate);
		housingGuiyangUserinfo.setPersonalAccount(personalAccount);
		housingGuiyangUserinfo.setPhoneNum(phoneNum);
		housingGuiyangUserinfo.setRemitStatus(remitStatus);
		housingGuiyangUserinfo.setSex(sex);
		housingGuiyangUserinfo.setStartYear(startYear);
		housingGuiyangUserinfo.setWageBase(wageBase);
		housingGuiyangUserinfo.setTaskid(taskHousing.getTaskid());
			
		return housingGuiyangUserinfo;
		
	}
	
	/**
	 * @Des 根据规则获取当前标签的文本
	 * @param doc
	 * @param rule
	 * @return
	 */
	public String getElementText(Document doc, String rule){
		
		Elements es = doc.select(rule);
		if(null != es && es.size()>0){
			String text = es.first().text();
			return text;
		}else{
			return null;
		}			
	}

	/**
	 * @Des 根据规则获取当前标签的下一个兄弟标签的文本
	 * @param doc
	 * @param rule
	 * @return
	 */
	public String getNextElementText(Element doc, String rule){
		
		Elements es = doc.select(rule);
		if(null != es && es.size()>0){
			Element text = es.first().nextElementSibling();
			return text.text();
		}
	
		return null;
	}

	/**
	 * @Des 解析账户信息
	 * @param page
	 * @param taskHousing
	 * @return
	 */
	public List<HousingGuiyangDetailAccount> parserAccount(XmlPage page, TaskHousing taskHousing) {
		List<HousingGuiyangDetailAccount> accounts = new ArrayList<HousingGuiyangDetailAccount>();
		//因为此page是xml格式的，用jsoup转的话会出问题。这是对xml做处理。
		Document doc = Jsoup.parse(page.asXml());
		String html = doc.toString().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
		Document doc1 = Jsoup.parse(html);
		Elements trs = doc1.getElementsByClass("grid-tr-data");
		if(null != trs && trs.size()>0){
			for(Element tr : trs){
				String accountDate = tr.child(0).text();
				String processType = tr.child(1).text();
				String payYear = tr.child(2).text();
				String income = tr.child(3).text();
				String expend = tr.child(4).text();
				String balance = tr.child(5).text();
				
				HousingGuiyangDetailAccount housingGuiyangDetailAccount = new HousingGuiyangDetailAccount();
				housingGuiyangDetailAccount.setAccountDate(accountDate);
				housingGuiyangDetailAccount.setBalance(balance);
				housingGuiyangDetailAccount.setExpend(expend);
				housingGuiyangDetailAccount.setIncome(income);
				housingGuiyangDetailAccount.setPayYear(payYear);
				housingGuiyangDetailAccount.setProcessType(processType);
				housingGuiyangDetailAccount.setTaskid(taskHousing.getTaskid());
				accounts.add(housingGuiyangDetailAccount);
			}
		}else{
			return null;
		}
		
		return accounts;
	}

}
