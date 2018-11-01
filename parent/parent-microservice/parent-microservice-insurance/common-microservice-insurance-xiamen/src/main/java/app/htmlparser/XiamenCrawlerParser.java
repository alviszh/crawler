package app.htmlparser;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.enums.InsuranceXiamenCrawlerResult;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.xiamen.InsuranceXiamenDetailsInfo;
import com.microservice.dao.entity.crawler.insurance.xiamen.InsuranceXiamenPaymentSummaryInfo;
import com.microservice.dao.entity.crawler.insurance.xiamen.InsuranceXiamenBaseInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 厦门社保爬取 HTML解析
 * 
 * @author kaixu
 *
 */
@Component
public class XiamenCrawlerParser {

	public static final Logger log = LoggerFactory.getLogger(XiamenCrawlerParser.class);
	@Autowired
	private TracerLog tracer;

	/**
	 * 解析爬取的基本信息
	 * 
	 * @param baseInfoPage
	 * @return
	 */
	public WebParam<InsuranceXiamenBaseInfo> parserBaseInfo(HtmlPage baseInfoPage, HtmlPage canbaoInfoPage) {
		WebParam<InsuranceXiamenBaseInfo> webParam = new WebParam<>();
		InsuranceXiamenBaseInfo baseInfo = null;
		try {
			// 个人资料
			Document doc = Jsoup.parse(baseInfoPage.getWebResponse().getContentAsString());
			/** 姓名 */
			String name = doc
					.select("body > div.bg > div.zwgk_cen.mar_t20 > div.con_bg > div.con_r.pad_t20 > table > tbody > tr:nth-child(1) > td:nth-child(2)")
					.text();
			/** 保险号 */
			String insuranceNo = doc
					.select("body > div.bg > div.zwgk_cen.mar_t20 > div.con_bg > div.con_r.pad_t20 > table > tbody > tr:nth-child(2) > td:nth-child(2)")
					.text();
			/** 社会保障卡卡号 */
			String shbzCardNo = doc
					.select("body > div.bg > div.zwgk_cen.mar_t20 > div.con_bg > div.con_r.pad_t20 > table > tbody > tr:nth-child(3) > td:nth-child(2)")
					.text();
			/** 社会保障卡状态 */
			String shbzCardStatus = doc
					.select("body > div.bg > div.zwgk_cen.mar_t20 > div.con_bg > div.con_r.pad_t20 > table > tbody > tr:nth-child(4) > td:nth-child(2)")
					.text();
			/** 单位名称 */
			String companyName = doc
					.select("body > div.bg > div.zwgk_cen.mar_t20 > div.con_bg > div.con_r.pad_t20 > table > tbody > tr:nth-child(5) > td:nth-child(2)")
					.text();
			/** 单位编号 */
			String companyNo = doc
					.select("body > div.bg > div.zwgk_cen.mar_t20 > div.con_bg > div.con_r.pad_t20 > table > tbody > tr:nth-child(6) > td:nth-child(2)")
					.text();
			/** 单位类型 */
			String companyType = doc
					.select("body > div.bg > div.zwgk_cen.mar_t20 > div.con_bg > div.con_r.pad_t20 > table > tbody > tr:nth-child(7) > td:nth-child(2)")
					.text();
			/** 个人身份 */
			String identity = doc
					.select("body > div.bg > div.zwgk_cen.mar_t20 > div.con_bg > div.con_r.pad_t20 > table > tbody > tr:nth-child(8) > td:nth-child(2)")
					.text();
			/** 工作状态 */
			String workStatus = doc
					.select("body > div.bg > div.zwgk_cen.mar_t20 > div.con_bg > div.con_r.pad_t20 > table > tbody > tr:nth-child(9) > td:nth-child(2)")
					.text();
			baseInfo = new InsuranceXiamenBaseInfo();
			baseInfo.setName(name);
			baseInfo.setInsuranceNo(insuranceNo);
			baseInfo.setShbzCardNo(shbzCardNo);
			baseInfo.setShbzCardStatus(shbzCardStatus);
			baseInfo.setCompanyName(companyName);
			baseInfo.setCompanyNo(companyNo);
			baseInfo.setCompanyType(companyType);
			baseInfo.setIdentity(identity);
			baseInfo.setWorkStatus(workStatus);
			// 参保信息
			Document canbaoDoc = Jsoup.parse(canbaoInfoPage.getWebResponse().getContentAsString());
			/** 养老保险参保日期 */
			String agedInsuranceCanbaodate = canbaoDoc
					.select("body > div.bg > div.zwgk_cen.mar_t20 > div.con_bg > div.con_r.pad_t20 > table > tbody > tr:nth-child(1) > td:nth-child(2)")
					.text();
			/** 养老缴费工资 */
			String agedInsurancePayBase = canbaoDoc
					.select("body > div.bg > div.zwgk_cen.mar_t20 > div.con_bg > div.con_r.pad_t20 > table > tbody > tr:nth-child(2) > td:nth-child(2)")
					.text();
			/** 医疗参保日期 */
			String medicalInsuranceCanbaodate = canbaoDoc
					.select("body > div.bg > div.zwgk_cen.mar_t20 > div.con_bg > div.con_r.pad_t20 > table > tbody > tr:nth-child(3) > td:nth-child(2)")
					.text();
			/** 医疗缴费工资 */
			String medicalInsurancePayBase = canbaoDoc
					.select("body > div.bg > div.zwgk_cen.mar_t20 > div.con_bg > div.con_r.pad_t20 > table > tbody > tr:nth-child(4) > td:nth-child(2)")
					.text();
			/** 工伤参保日期 */
			String injuryInsuranceCanbaodate = canbaoDoc
					.select("body > div.bg > div.zwgk_cen.mar_t20 > div.con_bg > div.con_r.pad_t20 > table > tbody > tr:nth-child(5) > td:nth-child(2)")
					.text();
			/** 工伤缴费工资 */
			String injuryInsurancePayBase = canbaoDoc
					.select("body > div.bg > div.zwgk_cen.mar_t20 > div.con_bg > div.con_r.pad_t20 > table > tbody > tr:nth-child(6) > td:nth-child(2)")
					.text();
			/** 失业参保日期 */
			String unemploymentInsuranceCanbaodate = canbaoDoc
					.select("body > div.bg > div.zwgk_cen.mar_t20 > div.con_bg > div.con_r.pad_t20 > table > tbody > tr:nth-child(7) > td:nth-child(2)")
					.text();
			/** 失业缴费工资 */
			String unemploymentInsurancePayBase = canbaoDoc
					.select("body > div.bg > div.zwgk_cen.mar_t20 > div.con_bg > div.con_r.pad_t20 > table > tbody > tr:nth-child(8) > td:nth-child(2)")
					.text();
			/** 生育参保日期 */
			String birthInsuranceCanbaodate = canbaoDoc
					.select("body > div.bg > div.zwgk_cen.mar_t20 > div.con_bg > div.con_r.pad_t20 > table > tbody > tr:nth-child(9) > td:nth-child(2)")
					.text();
			/** 生育缴费工资 */
			String birthInsurancePayBase = canbaoDoc
					.select("body > div.bg > div.zwgk_cen.mar_t20 > div.con_bg > div.con_r.pad_t20 > table > tbody > tr:nth-child(10) > td:nth-child(2)")
					.text();
			baseInfo.setAgedInsuranceCanbaodate(agedInsuranceCanbaodate);
			baseInfo.setAgedInsurancePayBase(agedInsurancePayBase);
			baseInfo.setMedicalInsuranceCanbaodate(medicalInsuranceCanbaodate);
			baseInfo.setMedicalInsurancePayBase(medicalInsurancePayBase);
			baseInfo.setInjuryInsuranceCanbaodate(injuryInsuranceCanbaodate);
			baseInfo.setInjuryInsurancePayBase(injuryInsurancePayBase);
			baseInfo.setUnemploymentInsuranceCanbaodate(unemploymentInsuranceCanbaodate);
			baseInfo.setUnemploymentInsurancePayBase(unemploymentInsurancePayBase);
			baseInfo.setBirthInsuranceCanbaodate(birthInsuranceCanbaodate);
			baseInfo.setBirthInsurancePayBase(birthInsurancePayBase);
			webParam.setData(baseInfo);
			webParam.setCode(InsuranceXiamenCrawlerResult.SUCCESS.getCode());
		} catch (Exception e) {
			e.printStackTrace();
			webParam.setData(baseInfo);
			webParam.setCode(InsuranceXiamenCrawlerResult.EXCEPTION.getCode());
		}

		return webParam;
	}

	/**
	 * 解析厦门五险汇总页面
	 * 
	 * @param fiveInsurance
	 * @return
	 */
	public WebParam<List<InsuranceXiamenPaymentSummaryInfo>> parserFiveInsuranceSummary(HtmlPage fiveInsurance,
			int pageNum) {
		WebParam<List<InsuranceXiamenPaymentSummaryInfo>> webParam = new WebParam<>();
		List<InsuranceXiamenPaymentSummaryInfo> insurancePaymentSummaryInfos = new ArrayList<>();
		InsuranceXiamenPaymentSummaryInfo summaryInfo = null;
		try {
			String html = fiveInsurance.getWebResponse().getContentAsString();
			Document doc = Jsoup.parse(html);
			// 获取列表集合
			Elements elements = doc.select(
					"body > div.bg > div.zwgk_cen.mar_t20 > div.con_bg > div.con_r.pad_t20 > table.tab5 > tbody > tr");
			for (int i = 0; i < elements.size(); i++) {
				Element ele = elements.get(i);
				/** 保险险种 */
				String insuranceType = ele.select("td:nth-child(1) > a").text();
				/** 账目类型 */
				String accountType = ele.select("td:nth-child(2)").text();
				/** 业务标志 */
				String serviceMark = ele.select("td:nth-child(3)").text();
				/** 起始年月 */
				String startDate = ele.select("td:nth-child(4)").text();
				/** 截至年月 */
				String endDate = ele.select("td:nth-child(5)").text();
				/** 缴费总额 */
				String totalPayment = ele.select("td:nth-child(6)").text();
				/** 划拨金额 */
				String allottedAmount = ele.select("td:nth-child(7)").text();
				/** 缴费基数 */
				String baseAmount = ele.select("td:nth-child(8)").text();
				/** 是否缴费 */
				String isPayment = ele.select("td:nth-child(9)").text();
				summaryInfo = new InsuranceXiamenPaymentSummaryInfo();
				summaryInfo.setInsuranceType(insuranceType);
				summaryInfo.setAccountType(accountType);
				summaryInfo.setServiceMark(serviceMark);
				summaryInfo.setStartDate(startDate);
				summaryInfo.setEndDate(endDate);
				summaryInfo.setTotalPayment(totalPayment);
				summaryInfo.setAllottedAmount(allottedAmount);
				summaryInfo.setBaseAmount(baseAmount);
				summaryInfo.setIsPayment(isPayment);
				// page1-1
				summaryInfo.setPageId("page" + pageNum + "-" + (i + 1));
				insurancePaymentSummaryInfos.add(summaryInfo);
				System.out.println("五险汇总第" + pageNum + "页 =========第" + (i + 1) + "条成功！");
				tracer.addTag("XiamenCrawlerParser.parserFiveInsuranceSummary 解析厦门五险汇总页面",
						"五险汇总第" + pageNum + "页 =========第" + (i + 1) + "条成功！");
			}
			webParam.setData(insurancePaymentSummaryInfos);
			webParam.setCode(InsuranceXiamenCrawlerResult.SUCCESS.getCode());
			if (insurancePaymentSummaryInfos.size() == 15) {
				System.out.println("??");
			}
		} catch (Exception e) {
			e.printStackTrace();
			webParam.setData(insurancePaymentSummaryInfos);
			webParam.setCode(InsuranceXiamenCrawlerResult.EXCEPTION.getCode());
		}
		return webParam;
	}

	/**
	 * 解析五险详细信息
	 * 
	 * @param fiveInsurance
	 * @return
	 */
	public WebParam<InsuranceXiamenDetailsInfo> parserFiveInsuranceDetails(HtmlPage fiveInsurance) {
		WebParam<InsuranceXiamenDetailsInfo> webParam = new WebParam<>();
		InsuranceXiamenDetailsInfo insuranceXiamenDetailsInfo = null;
		try {
			String html = fiveInsurance.getWebResponse().getContentAsString();
			Document doc = Jsoup.parse(html);
			// 获取列表集合
			Elements elements = doc.select(
					"body > div.bg > div.zwgk_cen.mar_t20 > div.con_bg > div.con_r.pad_t20 > table.tab > tbody");
			insuranceXiamenDetailsInfo = new InsuranceXiamenDetailsInfo();
			/** 个人编号 */
			String personalNumber = elements.select("tr:nth-child(1) > td:nth-child(2)").text();
			/** 单位名称 */
			String companyName = elements.select("tr:nth-child(1) > td:nth-child(4)").text();
			/** 帐目名称 */
			String accountName = elements.select("tr:nth-child(2) > td:nth-child(2)").text();
			/** 实际划拨日期 */
			String actualAllottedDate = elements.select("tr:nth-child(2) > td:nth-child(4)").text();
			/** 业务标志 */
			String serviceMark = elements.select("tr:nth-child(3) > td:nth-child(2)").text();
			/** 保险险种 */
			String insuranceCoverage = elements.select("tr:nth-child(3) > td:nth-child(4)").text();
			/** 起始帐目年月 */
			String startAccountDate = elements.select("tr:nth-child(4) > td:nth-child(2)").text();
			/** 截止帐目年月 */
			String endAccountDate = elements.select("tr:nth-child(4) > td:nth-child(4)").text();
			/** 建帐年月 */
			String createAccountDate = elements.select("tr:nth-child(5) > td:nth-child(2)").text();
			/** 缴费月数 */
			String paymentMonth = elements.select("tr:nth-child(5) > td:nth-child(4)").text();
			/** 利息 */
			String interest = elements.select("tr:nth-child(6) > td:nth-child(2)").text();
			/** 滞纳金 */
			String lateFee = elements.select("tr:nth-child(6) > td:nth-child(4)").text();
			/** 单位缴费比例 */
			String companyContributionRatio = elements.select("tr:nth-child(7) > td:nth-child(2)").text();
			/** 单位缴费金额 */
			String companyPaymentAmount = elements.select("tr:nth-child(7) > td:nth-child(4)").text();
			/** 个人缴费比例 */
			String individualPaymentRatio = elements.select("tr:nth-child(8) > td:nth-child(2)").text();
			/** 个人缴费金额 */
			String individualPaymentAmount = elements.select("tr:nth-child(8) > td:nth-child(4)").text();
			/** 单位缴费基数 */
			String companyAccountBase = elements.select("tr:nth-child(9) > td:nth-child(2)").text();
			/** 个人缴费基数 */
			String individualAccountBase = elements.select("tr:nth-child(9) > td:nth-child(4)").text();
			/** 单位划入帐户 */
			String companyIntoAccount = elements.select("tr:nth-child(10) > td:nth-child(2)").text();
			/** 个人划入帐户 */
			String individualIntoAccount = elements.select("tr:nth-child(10) > td:nth-child(4)").text();
			/** 缴费日期 */
			String paymentDate = elements.select("tr:nth-child(11) > td:nth-child(2)").text();
			/** 缴费总金额 */
			String totalAmountPayment = elements.select("tr:nth-child(11) > td:nth-child(4)").text();
			/** 冲销标志 */
			String writeDownSign = elements.select("tr:nth-child(12) > td:nth-child(2)").text();
			insuranceXiamenDetailsInfo.setPersonalNumber(personalNumber);
			insuranceXiamenDetailsInfo.setCompanyName(companyName);
			insuranceXiamenDetailsInfo.setAccountName(accountName);
			insuranceXiamenDetailsInfo.setActualAllottedDate(actualAllottedDate);
			insuranceXiamenDetailsInfo.setServiceMark(serviceMark);
			insuranceXiamenDetailsInfo.setInsuranceCoverage(insuranceCoverage);
			insuranceXiamenDetailsInfo.setStartAccountDate(startAccountDate);
			insuranceXiamenDetailsInfo.setEndAccountDate(endAccountDate);
			insuranceXiamenDetailsInfo.setCreateAccountDate(createAccountDate);
			insuranceXiamenDetailsInfo.setPaymentMonth(paymentMonth);
			insuranceXiamenDetailsInfo.setInterest(interest);
			insuranceXiamenDetailsInfo.setLateFee(lateFee);
			insuranceXiamenDetailsInfo.setCompanyContributionRatio(companyContributionRatio);
			insuranceXiamenDetailsInfo.setCompanyPaymentAmount(companyPaymentAmount);
			insuranceXiamenDetailsInfo.setIndividualPaymentRatio(individualPaymentRatio);
			insuranceXiamenDetailsInfo.setIndividualPaymentAmount(individualPaymentAmount);
			insuranceXiamenDetailsInfo.setCompanyAccountBase(companyAccountBase);
			insuranceXiamenDetailsInfo.setIndividualAccountBase(individualAccountBase);
			insuranceXiamenDetailsInfo.setCompanyIntoAccount(companyIntoAccount);
			insuranceXiamenDetailsInfo.setIndividualIntoAccount(individualIntoAccount);
			insuranceXiamenDetailsInfo.setPaymentDate(paymentDate);
			insuranceXiamenDetailsInfo.setTotalAmountPayment(totalAmountPayment);
			insuranceXiamenDetailsInfo.setWriteDownSign(writeDownSign);

			webParam.setData(insuranceXiamenDetailsInfo);
			webParam.setCode(InsuranceXiamenCrawlerResult.SUCCESS.getCode());
		} catch (Exception e) {
			e.printStackTrace();
			webParam.setData(insuranceXiamenDetailsInfo);
			webParam.setCode(InsuranceXiamenCrawlerResult.EXCEPTION.getCode());
		}
		return webParam;
	}
}
