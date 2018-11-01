package app.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.microservice.dao.entity.crawler.insurance.shenzhen.InsuranceShenzhenPayDetail;
import com.microservice.dao.entity.crawler.insurance.shenzhen.InsuranceShenzhenBaseInfo;
import com.microservice.dao.entity.crawler.insurance.shenzhen.InsuranceShenzhenCompany;

/**
 * 深圳社保爬取 HTML解析
 * @author rongshengxu
 *
 */
@Component
public class ShenzhenCrawlerParser {
	
	public static final Logger log = LoggerFactory.getLogger(ShenzhenCrawlerParser.class);
	
	/**
	 * 解析爬取的登录信息
	 * @param loginPage
	 * @return
	 */
	public String parserLogin(HtmlPage loginPage){
		//无法获取网页内容,超时
		if(loginPage == null || loginPage.getWebResponse() == null || loginPage.getWebResponse().getContentAsString() == null){
			return InsuranceShenzhenCrawlerResult.TIMEOUT.getCode();
		}
		String contextString = loginPage.getWebResponse().getContentAsString();
		
		if(contextString.contains("用户名、密码不正确")){
			return InsuranceShenzhenCrawlerResult.USER_OR_PASSWORD_ERROR.getCode();
		}
		if(contextString.contains("用户名、密码验证未通过")){
			return InsuranceShenzhenCrawlerResult.USER_OR_PASSWORD_ERROR.getCode();
		}
		if(contextString.contains("验证码不正确")){
			return InsuranceShenzhenCrawlerResult.IMAGE_ERROR.getCode();
		}
		if(contextString.contains("用户名不符合规则")){
			return InsuranceShenzhenCrawlerResult.USER_ERROR.getCode();
		}
		if(contextString.contains("MM_swapImage('Image1','','images/login2.gif',1)")){
			return InsuranceShenzhenCrawlerResult.SUCCESS.getCode();
		}else{
			return InsuranceShenzhenCrawlerResult.EXCEPTION.getCode();
		}
	}

	/**
	 * 解析爬取的基本信息
	 * @param baseInfoPage
	 * @return
	 */
	public WebParam<InsuranceShenzhenBaseInfo> parserBaseInfo(HtmlPage baseInfoPage){
		WebParam<InsuranceShenzhenBaseInfo> webParam = new WebParam<InsuranceShenzhenBaseInfo>();
		InsuranceShenzhenBaseInfo baseInfo = null;
		try {
			List<HtmlElement> elements =baseInfoPage.getByXPath("//input[@class='textReadonly']");
			if(elements != null && !elements.isEmpty()){
				baseInfo = new InsuranceShenzhenBaseInfo();
				baseInfo.setName(elements.get(0).getAttribute("value"));
				baseInfo.setInsuranceCode(elements.get(1).getAttribute("value"));
				baseInfo.setCardId(elements.get(2).getAttribute("value"));
				baseInfo.setSex(elements.get(3).getAttribute("value"));
				baseInfo.setBirthDate(elements.get(4).getAttribute("value"));
				baseInfo.setCensusRegisterType(elements.get(5).getAttribute("value"));
				baseInfo.setMedicalCardId(elements.get(6).getAttribute("value"));
				baseInfo.setCardState(elements.get(7).getAttribute("value"));
				baseInfo.setEmployeeType(elements.get(8).getAttribute("value"));
				baseInfo.setHealthObject(elements.get(9).getAttribute("value"));
				baseInfo.setPisitionLevel(elements.get(10).getAttribute("value"));
				baseInfo.setRemark(elements.get(11).getAttribute("value"));
				baseInfo.setInsuredCompany(elements.get(12).getAttribute("value"));
				baseInfo.setCurrentPayInfo(elements.get(13).getAttribute("value"));
				baseInfo.setPayQuota(elements.get(14).getAttribute("value"));
				baseInfo.setAgedInsuredInfo(elements.get(15).getAttribute("value"));
				baseInfo.setMedicalInsuredInfo(elements.get(16).getAttribute("value"));
				baseInfo.setInjuryInsuredInfo(elements.get(17).getAttribute("value"));
				baseInfo.setUnemploymentInsuredInfo(elements.get(18).getAttribute("value"));
				baseInfo.setMaternityInsuredInfo(elements.get(19).getAttribute("value"));
			}
			webParam.setData(baseInfo);
			webParam.setCode(InsuranceShenzhenCrawlerResult.SUCCESS.getCode());
		} catch (Exception e) {
			e.printStackTrace();
			webParam.setData(baseInfo);
			webParam.setCode(InsuranceShenzhenCrawlerResult.EXCEPTION.getCode());
		}
		
		return webParam;
	}
	
	/**
	 * 解析爬取的单位信息
	 * @param agedPage
	 * @return
	 */
	public WebParam<List<InsuranceShenzhenCompany>> parserInsuranceCompanys(HtmlPage agedPage,String taskId){
		WebParam<List<InsuranceShenzhenCompany>> webParam = new WebParam<List<InsuranceShenzhenCompany>>();
		
		List<InsuranceShenzhenCompany> insuranceCompanys = new ArrayList<InsuranceShenzhenCompany>();
		try {
			HtmlElement pEle = (HtmlElement)agedPage.getFirstByXPath("//p");
			String[] pline = pEle.asText().split("\n");
			for (String p : pline) {
				String[] empty = p.split("\\s+");
				if(StringUtils.isNotEmpty(empty[0])){
					//过滤标题
					if(empty[0].trim().equals("单位编号")){
						continue;
					}
					InsuranceShenzhenCompany isc = new InsuranceShenzhenCompany();
					isc.setTaskId(taskId);
					isc.setCode(empty[0]);
					isc.setName(empty[1]);
					insuranceCompanys.add(isc);
				}
			}
			webParam.setData(insuranceCompanys);
			webParam.setCode(InsuranceShenzhenCrawlerResult.SUCCESS.getCode());
		} catch (Exception e) {
			e.printStackTrace();
			webParam.setData(insuranceCompanys);
			webParam.setCode(InsuranceShenzhenCrawlerResult.EXCEPTION.getCode());
		}
		return webParam;
	}
	
	/**
	 * 解析爬取的养老保险缴费信息
	 * @param agedPage
	 * @return
	 */
	public WebParam<List<InsuranceShenzhenPayDetail>> parserAgedPayDetailsInsurance(HtmlPage agedPage,String taskId){
		WebParam<List<InsuranceShenzhenPayDetail>> webParam = new WebParam<List<InsuranceShenzhenPayDetail>>();
		
		List<InsuranceShenzhenPayDetail> payDetails = new ArrayList<InsuranceShenzhenPayDetail>();
		try {
			List<HtmlTableRow> rowList = agedPage.getByXPath("//tr[@name='TR0']");
			for (int i = 0; i < rowList.size(); i++) {
				HtmlTableRow row = rowList.get(i);
				List<HtmlTableCell> cells = row.getCells();
				
				if(cells == null || cells.isEmpty()){
					continue;
				}
				
				InsuranceShenzhenPayDetail payDetail = new InsuranceShenzhenPayDetail();
				payDetail.setInsuredType(InsuranceShenzhenCrawlerType.AGED_INSURANCE.getCode());
				payDetail.setTaskId(taskId);
				payDetail.setPayCompany(cells.get(0).asText());
				payDetail.setInsuredDate(cells.get(1).asText());
				payDetail.setPayQuota(cells.get(2).asText());
				payDetail.setPersonalPay(cells.get(3).asText());
				payDetail.setCompanyPay(cells.get(4).asText());
				payDetail.setPayTotal(cells.get(5).asText());
				payDetail.setCutinPersonal(cells.get(6).asText());
				payDetail.setRemark(cells.get(7).asText());
				
				payDetails.add(payDetail);
			}
			webParam.setData(payDetails);
			webParam.setCode(InsuranceShenzhenCrawlerResult.SUCCESS.getCode());
		} catch (Exception e) {
			e.printStackTrace();
			webParam.setData(payDetails);
			webParam.setCode(InsuranceShenzhenCrawlerResult.EXCEPTION.getCode());
		}
		
		return webParam;
	}
	
	/**
	 * 解析爬取的医疗保险缴费信息
	 * @param medicalPage
	 * @return
	 */
	public WebParam<List<InsuranceShenzhenPayDetail>> parserMedicalPayDetailsInsurance(HtmlPage medicalPage,String taskId){
		WebParam<List<InsuranceShenzhenPayDetail>> webParam = new WebParam<List<InsuranceShenzhenPayDetail>>();
		
		List<InsuranceShenzhenPayDetail> payDetails = new ArrayList<InsuranceShenzhenPayDetail>();
		try {
			List<HtmlTableRow> rowList =medicalPage.getByXPath("//tr[@name='TR0']");
			for (int i = 0; i < rowList.size(); i++) {
				HtmlTableRow row = rowList.get(i);
				List<HtmlTableCell> cells = row.getCells();
				
				if(cells == null || cells.isEmpty()){
					continue;
				}
				
				InsuranceShenzhenPayDetail payDetail = new InsuranceShenzhenPayDetail();
				payDetail.setInsuredType(InsuranceShenzhenCrawlerType.MEDICAL_INSURANCE.getCode());
				payDetail.setTaskId(taskId);
				payDetail.setPayCompany(cells.get(0).asText());
				payDetail.setInsuredDate(cells.get(1).asText());
				payDetail.setPayQuota(cells.get(2).asText());
				payDetail.setPersonalPay(cells.get(3).asText());
				payDetail.setCompanyPay(cells.get(4).asText());
				payDetail.setPayTotal(cells.get(5).asText());
				payDetail.setCutinPersonal(cells.get(6).asText());
				payDetail.setRemark(cells.get(7).asText());
				
				payDetails.add(payDetail);
			}
			webParam.setData(payDetails);
			webParam.setCode(InsuranceShenzhenCrawlerResult.SUCCESS.getCode());
		} catch (Exception e) {
			e.printStackTrace();
			webParam.setData(payDetails);
			webParam.setCode(InsuranceShenzhenCrawlerResult.EXCEPTION.getCode());
		}
		
		return webParam;
	}
	
	/**
	 * 解析爬取的工伤保险缴费信息
	 * @param medicalPage
	 * @return
	 */
	public WebParam<List<InsuranceShenzhenPayDetail>> parserInjuryPayDetailsInsurance(HtmlPage injuryPage,String taskId){
		WebParam<List<InsuranceShenzhenPayDetail>> webParam = new WebParam<List<InsuranceShenzhenPayDetail>>();
		
		List<InsuranceShenzhenPayDetail> payDetails = new ArrayList<InsuranceShenzhenPayDetail>();
		try {
			List<HtmlTableRow> rowList = injuryPage.getByXPath("//tr[@name='TR0']");
			for (int i = 0; i < rowList.size(); i++) {
				HtmlTableRow row = rowList.get(i);
				List<HtmlTableCell> cells = row.getCells();
				
				if(cells == null || cells.isEmpty()){
					continue;
				}
				
				InsuranceShenzhenPayDetail payDetail = new InsuranceShenzhenPayDetail();
				payDetail.setInsuredType(InsuranceShenzhenCrawlerType.INJURY_INSURANCE.getCode());
				payDetail.setTaskId(taskId);
				payDetail.setPayCompany(cells.get(0).asText());
				payDetail.setInsuredDate(cells.get(1).asText());
				payDetail.setPayQuota(cells.get(2).asText());
				payDetail.setCompanyPay(cells.get(3).asText());
				payDetail.setRemark(cells.get(4).asText());
				
				payDetails.add(payDetail);
			}
			webParam.setData(payDetails);
			webParam.setCode(InsuranceShenzhenCrawlerResult.SUCCESS.getCode());
		} catch (Exception e) {
			e.printStackTrace();
			webParam.setData(payDetails);
			webParam.setCode(InsuranceShenzhenCrawlerResult.EXCEPTION.getCode());
		}
		
		return webParam;
	}
	
	/**
	 * 解析爬取的失业保险缴费信息
	 * @param medicalPage
	 * @return
	 */
	public WebParam<List<InsuranceShenzhenPayDetail>> parserUnemploymentPayDetailsInsurance(HtmlPage injuryPage,String taskId){
		WebParam<List<InsuranceShenzhenPayDetail>> webParam = new WebParam<List<InsuranceShenzhenPayDetail>>();
		
		List<InsuranceShenzhenPayDetail> payDetails = new ArrayList<InsuranceShenzhenPayDetail>();
		try {
			List<HtmlTableRow> rowList =injuryPage.getByXPath("//tr[@name='TR0']");
			for (int i = 0; i < rowList.size(); i++) {
				HtmlTableRow row = rowList.get(i);
				List<HtmlTableCell> cells = row.getCells();
				
				if(cells == null || cells.isEmpty()){
					continue;
				}
				
				InsuranceShenzhenPayDetail payDetail = new InsuranceShenzhenPayDetail();
				payDetail.setInsuredType(InsuranceShenzhenCrawlerType.UNEMPLOYMENT_INSURANCE.getCode());
				payDetail.setTaskId(taskId);
				payDetail.setPayCompany(cells.get(0).asText());
				payDetail.setInsuredDate(cells.get(1).asText());
				payDetail.setPayQuota(cells.get(2).asText());
				payDetail.setPersonalPay(cells.get(3).asText());
				payDetail.setCompanyPay(cells.get(4).asText());
				payDetail.setRemark(cells.get(5).asText());
				
				payDetails.add(payDetail);
			}
			webParam.setData(payDetails);
			webParam.setCode(InsuranceShenzhenCrawlerResult.SUCCESS.getCode());
		} catch (Exception e) {
			e.printStackTrace();
			webParam.setData(payDetails);
			webParam.setCode(InsuranceShenzhenCrawlerResult.EXCEPTION.getCode());
		}
		
		return webParam;
	}

}
