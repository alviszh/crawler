package app.htmlparser;

import com.crawler.domain.json.Result;
import com.crawler.pbccrc.json.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

@Component
public class PbcCreditFeedParser extends AbstractPbccrcParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(PbcCreditFeedParser.class);
	private static final Map<String, String> creditCardDetailTypeMap;
	private static final Map<String, String> housingLoanDetailTypeMap;
	private static final Map<String, String> otherLoanDetailTypeMap;
	
	static {
		creditCardDetailTypeMap = new HashMap<String, String>();
		creditCardDetailTypeMap.put("透支超过60天的准贷记卡账户明细如下：", "2");
		creditCardDetailTypeMap.put("发生过逾期的贷记卡账户明细如下：", "1");
		creditCardDetailTypeMap.put("从未逾期过的贷记卡及透支未超过60天的准贷记卡账户明细如下：", "3");
		
		housingLoanDetailTypeMap = new HashMap<String, String>();
		housingLoanDetailTypeMap.put("从未逾期过的账户明细如下：", "2");
		housingLoanDetailTypeMap.put("发生过逾期账户明细如下：", "1");
		
		otherLoanDetailTypeMap = new HashMap<String, String>();
		otherLoanDetailTypeMap.put("发生过逾期的账户明细如下：", "1");
		otherLoanDetailTypeMap.put("从未逾期过的账户明细如下：", "2");
	}
	
	
	
	public PbcCreditReportFeed getPbcCreditFeed(String html) throws IOException{
		if (StringUtils.isEmpty(html)) { //html is null <==> Exception in storm
			return null;
		}
		
		LOGGER.info("=================PbcCreditFeedParser.getPbcCreditFeed begin=================");
		PbcCreditReportFeed pbcCreditReportFeed = new PbcCreditReportFeed();
		Elements eles = Jsoup.parseBodyFragment(html).select("body");
		if (eles==null || eles.isEmpty()) {
			return pbcCreditReportFeed;
		}
		Element ele = eles.get(0);
		if (getfirstChildElement(ele, "th div.h1 strong:contains(个人信用报告)")==null || getfirstChildElement(ele, "tr>td>strong:contains(报告编号：)")==null) {
			return pbcCreditReportFeed;
		}

		 /*
		 * 报告表头
		 */
		ReportBase reportBase = new ReportBase();
		Element ele_report_id = getfirstChildElement(ele, "tr>td>strong:contains(报告编号：)"); //报告编号
		String report_id = getElementText(ele_report_id);
		report_id = report_id.replaceAll("报告编号：", "").trim();
		reportBase.setReportId(report_id);
		Element ele_query_time = getfirstChildElement(ele, "tr>td>strong.p:contains(查询时间：)"); //查询时间
		String query_time = getElementText(ele_query_time);
		query_time = query_time.replaceAll("查询时间：", "").trim();
		reportBase.setQueryTime(query_time);
		Element ele_report_time = getfirstChildElement(ele, "tr>td>strong.p:contains(报告时间：)"); //报告时间
		String report_time = getElementText(ele_report_time);
		report_time = report_time.replaceAll("报告时间：", "").trim();
		reportBase.setReportTime(report_time);
		Element ele_realname = getfirstChildElement(ele, "tr>td>strong.p:contains(姓名：)"); //姓名
		String realname = getElementText(ele_realname);
		realname = realname.replaceAll("姓名：", "").trim();
		reportBase.setRealname(realname);
		Element ele_certificate_type = getfirstChildElement(ele, "tr>td>strong.p:contains(证件类型：)"); //证件类型
		String certificate_type = getElementText(ele_certificate_type);
		certificate_type = certificate_type.replaceAll("证件类型：", "").trim();
		reportBase.setCertificateType(certificate_type);
		Element ele_certificate_num = getfirstChildElement(ele, "tr>td>strong.p:contains(证件号码：)"); //证件号码
		String certificate_num = getElementText(ele_certificate_num);
		certificate_num = certificate_num.replaceAll("证件号码：", "").trim();
		reportBase.setCertificateNum(certificate_num);
		Element ele_is_married = getfirstChildElement(ele, "tr>td>strong.p:contains(未婚),tr>td>strong.p:contains(已婚)"); //婚否
		String marriage_status_str = getElementText(ele_is_married);
		String marriage_status = "";
		if (ele_is_married==null) {
			marriage_status = "未知";
		} else {
			marriage_status = "已婚".equals(marriage_status_str) ? "已婚" : "未婚";
		}
		reportBase.setMarriageStatus(marriage_status);
		pbcCreditReportFeed.setReportBase(reportBase);

		/*
		 * 通过字段个数和字段名找到相应的表格
		 */
		Element ele_table_creditSummery = null;		//信息概要
		Element ele_table_queryRecord_org = null; 	//机构查询记录明细
		Element ele_table_queryRecord_per = null; 	//个人查询记录明细
		Elements ele_tables = getElements(ele, "table");
		for (int i=0; i<ele_tables.size(); i++) {
			Elements ele_tds = getElements(ele_tables.get(i), "tbody>tr:eq(0)>td");
			int size = ele_tds.size();
			StringBuffer sb = new StringBuffer("--");
			for (Element ele_tb : ele_tds) {
				sb.append(getElementText(ele_tb) + "--");
			}
			LOGGER.info("size-------"+size+"--------sb.toString()------>>"+sb.toString()+"<<");
			if (size==4 && "-- --信用卡--购房贷款--其他贷款--".equals(sb.toString())) {
				ele_table_creditSummery = ele_tables.get(i);
				continue;
			}
			if (size==1 && ("--机构查询记录明细--".equals(sb.toString()))) {
				ele_table_queryRecord_org = ele_tables.get(i);
				continue;
			}
			if (size==1 && ("--个人查询记录明细--".equals(sb.toString()))) {
				ele_table_queryRecord_per = ele_tables.get(i);
				continue;
			}
		}
		
		
		 /*
		 * 信贷记录
		 */
		CreditRecord creditRecord = new CreditRecord();
		//信息概要
		CreditSummary creditSummary = new CreditSummary();
		Map<String, String> creditCards = new HashMap<String, String>();  //信用卡
		Map<String, String> housingLoans = new HashMap<String, String>(); //住房贷款
		Map<String, String> otherLoans = new HashMap<String, String>();	  //其他贷款
		String[] creditSummaryItem = {"accountNum", "activeNum", "overdueNum", "overdue90Num", "guaranteeNum"};
		Elements ele_table_trs_creditSummery = getElements(ele_table_creditSummery, "tbody tr:gt(0)"); 
		if (ele_table_trs_creditSummery!=null) {
			for (int i = 0; i < 5; i++) {
				Element ele_creditCards_item = getfirstChildElement(ele_table_trs_creditSummery.get(i), "td:eq(1)");
				Element ele_housingLoans_item = getfirstChildElement(ele_table_trs_creditSummery.get(i), "td:eq(2)");
				Element ele_otherLoans_item = getfirstChildElement(ele_table_trs_creditSummery.get(i), "td:eq(3)");
				creditCards.put(creditSummaryItem[i], getElementText(ele_creditCards_item).trim());
				housingLoans.put(creditSummaryItem[i], getElementText(ele_housingLoans_item).trim());
				otherLoans.put(creditSummaryItem[i], getElementText(ele_otherLoans_item).trim());
			}
		}
		creditSummary.setCreditCards(creditCards);
		creditSummary.setHousingLoans(housingLoans);
		creditSummary.setOtherLoans(otherLoans);
		creditRecord.setCreditSummary(creditSummary); //
		
		//信用卡明细
		Element ele_creditCardDetail_ol = getfirstChildElement(ele, "ol.p.olstyle:contains(透支超过60天的准贷记卡账户明细如下),ol.p.olstyle:contains(发生过逾期的贷记卡账户明细如下),ol.p.olstyle:contains(从未逾期过的贷记卡及透支未超过60天的准贷记卡账户明细如下)");
		Elements ele_creditCardDetail_li_spans = getElements(ele_creditCardDetail_ol, "li,span");
		int creditCardDetailSpanNum = 0;
		if (ele_creditCardDetail_li_spans!=null && !ele_creditCardDetail_li_spans.isEmpty()) {
			Set<CreditCardDetail> creditCardDetails = new LinkedHashSet<CreditCardDetail>();
			CreditCardDetail creditCardDetail = null;
			for (int i = 0; i < ele_creditCardDetail_li_spans.size(); i++) {
				Element ele_creditCardDetail_li_span = ele_creditCardDetail_li_spans.get(i);
				if (ele_creditCardDetail_li_span.toString().contains("<span")) { //标题
					creditCardDetailSpanNum++;
					String creditCardDetailType = creditCardDetailTypeMap.get(getElementText(ele_creditCardDetail_li_span).trim());
					creditCardDetail = new CreditCardDetail();
					creditCardDetail.setType(creditCardDetailType);
					creditCardDetail.setDetails(new LinkedHashMap<String, String>());
				} else if (ele_creditCardDetail_li_span.toString().contains("<li")) { //列表
					creditCardDetail.getDetails().put((i+1-creditCardDetailSpanNum)+"", getElementText(ele_creditCardDetail_li_span).trim());
				}
				creditCardDetails.add(creditCardDetail);
			}
			creditRecord.setCreditCardDetails(new ArrayList<CreditCardDetail>(creditCardDetails));
		}
		
		//住房贷款明细
		Element ele_housingLoanDetail_ol = getfirstChildElement(ele, "span.h1:contains(购房贷款) + ol.p.olstyle:contains(账户明细如下)");
		Elements ele_housingLoanDetail_li_spans = getElements(ele_housingLoanDetail_ol, "li,span");
		int housingLoanDetailSpanNum = 0;
		if (ele_housingLoanDetail_li_spans!=null && !ele_housingLoanDetail_li_spans.isEmpty()) {
			Set<HousingLoanDetail> housingLoanDetails = new LinkedHashSet<HousingLoanDetail>();
			HousingLoanDetail housingLoanDetail = null;
			for (int i = 0; i < ele_housingLoanDetail_li_spans.size(); i++) {
				Element ele_housingLoanDetail_li_span = ele_housingLoanDetail_li_spans.get(i);
				if (ele_housingLoanDetail_li_span.toString().contains("<span")) { //标题
					housingLoanDetailSpanNum++;
					String housingLoanDetailType = housingLoanDetailTypeMap.get(getElementText(ele_housingLoanDetail_li_span).trim());
					if ("发生过逾期账户明细如下:".equals(getElementText(ele_housingLoanDetail_li_span).trim())) {
						housingLoanDetailType = "1";
					}
					housingLoanDetail = new HousingLoanDetail();
					housingLoanDetail.setType(housingLoanDetailType);
					housingLoanDetail.setDetails(new LinkedHashMap<String, String>());
				} else if (ele_housingLoanDetail_li_span.toString().contains("<li")) { //列表
					housingLoanDetail.getDetails().put((i+1-housingLoanDetailSpanNum)+"", getElementText(ele_housingLoanDetail_li_span).trim());
				}
				housingLoanDetails.add(housingLoanDetail);
			}
			creditRecord.setHousingLoanDetails(new ArrayList<HousingLoanDetail>(housingLoanDetails));
		}
		
		//其他贷款
		Element ele_otherLoanDetail_ol = getfirstChildElement(ele, "span.h1:contains(其他贷款) + ol.p.olstyle:contains(账户明细如下)");
		Elements ele_otherLoanDetail_li_spans = getElements(ele_otherLoanDetail_ol, "li,span");
		int otherLoanDetailSpanNum = 0;
		if (ele_otherLoanDetail_li_spans!=null && !ele_otherLoanDetail_li_spans.isEmpty()) {
			Set<OtherLoanDetail> otherLoanDetails = new LinkedHashSet<OtherLoanDetail>();
			OtherLoanDetail otherLoanDetail = null;
			for (int i = 0; i < ele_otherLoanDetail_li_spans.size(); i++) {
				Element ele_otherLoanDetail_li_span = ele_otherLoanDetail_li_spans.get(i);
				if (ele_otherLoanDetail_li_span.toString().contains("<span")) { //标题
					otherLoanDetailSpanNum++;
					String otherLoanDetailType = otherLoanDetailTypeMap.get(getElementText(ele_otherLoanDetail_li_span).trim());
					otherLoanDetail = new OtherLoanDetail();
					otherLoanDetail.setType(otherLoanDetailType);
					otherLoanDetail.setDetails(new LinkedHashMap<String, String>());
				} else if (ele_otherLoanDetail_li_span.toString().contains("<li")) { //列表
					otherLoanDetail.getDetails().put((i+1-otherLoanDetailSpanNum)+"", getElementText(ele_otherLoanDetail_li_span).trim());
				}
				otherLoanDetails.add(otherLoanDetail);
			}
			creditRecord.setOtherLoanDetails(new ArrayList<OtherLoanDetail>(otherLoanDetails));
		}
		
		//为他人担保信息
//		LOGGER.info("ele--------"+ele.html());
		Element ele_guaranteeInfoDetail_ol = getfirstChildElement(ele, "span.h1:contains(为他人担保信息) + ol.p.olstyle");
		Elements ele_guaranteeInfoDetail_lis = getElements(ele_guaranteeInfoDetail_ol, "li");
		if (ele_guaranteeInfoDetail_lis!=null && !ele_guaranteeInfoDetail_lis.isEmpty()) {
			Map<String, String> guaranteeInfoDetails = new LinkedHashMap<String, String>();
			for (int i = 0; i < ele_guaranteeInfoDetail_lis.size(); i++) {
				Element ele_guaranteeInfoDetail_li = ele_guaranteeInfoDetail_lis.get(i);
				guaranteeInfoDetails.put((i+1)+"", getElementText(ele_guaranteeInfoDetail_li));
			}
			creditRecord.setGuaranteeInfoDetails(guaranteeInfoDetails);
		}
		pbcCreditReportFeed.setCreditRecord(creditRecord);
		
		//保证人代偿信息 
		Element ele_guarantorCompensatoryDetail_ol = getfirstChildElement(ele, "span.h1:contains(保证人代偿信息) + br + ol.p.olstyle");
		Elements ele_guarantorCompensatoryDetail_lis = getElements(ele_guarantorCompensatoryDetail_ol, "li");
		if (ele_guarantorCompensatoryDetail_lis!=null && !ele_guarantorCompensatoryDetail_lis.isEmpty()) {
			Map<String, String> guarantorCompensatoryDetails = new LinkedHashMap<String, String>();
			for (int i = 0; i < ele_guarantorCompensatoryDetail_lis.size(); i++) {
				Element ele_guarantorCompensatoryDetail_li = ele_guarantorCompensatoryDetail_lis.get(i);
				guarantorCompensatoryDetails.put((i+1)+"", getElementText(ele_guarantorCompensatoryDetail_li));
				//LOGGER.info((i+1)+"======="+getElementText(ele_guarantorCompensatoryDetail_li));
			}
			creditRecord.setGuarantorCompensatoryinfoDetails(guarantorCompensatoryDetails);
		}
		pbcCreditReportFeed.setCreditRecord(creditRecord);
		
		
		
		 /*
		 * 公共记录
		 */ 
		Element ele_public_record = getfirstChildElement(ele, "strong.p:contains(这部分包含您最近5年内的欠税记录)");  
		
		if(ele_public_record!=null){
			PublicRecord publicRecord = new PublicRecord();    
			/*
			 * 欠税记录
			 */ 
			Element ele_flag_taxesOwed = getfirstChildElement(ele, "span.h1 > strong:contains(欠税记录)"); 
			if(ele_flag_taxesOwed!=null){
				List<TaxesOwed> taxesOweds = new ArrayList<TaxesOwed>();
				Elements ele_taxAuthorities = getElements(ele, "td.p:contains(主管税务机关：)"); 
				Elements ele_taxTimeStatistics = getElements(ele, "td.p:contains(欠税统计时间：)"); 
				Elements ele_totalTaxes = getElements(ele, "td.p:contains(欠税总额：)"); 
				Elements ele_taxpayerIdNums = getElements(ele, "td.p:contains(纳税人识别号：)"); 
				
				if(ele_taxAuthorities!=null&&ele_taxAuthorities.size()>0){ 
					for(int i=0;i<ele_taxAuthorities.size();i++){
						TaxesOwed taxesOwed = new TaxesOwed();
						//主管税务机关
						Element ele_taxAuthoritie = ele_taxAuthorities.get(i);
						String taxAuthorities = ele_taxAuthoritie.text().replace("主管税务机关：", "").trim(); 
						taxesOwed.setTaxAuthorities(taxAuthorities); 
						//欠税统计时间
						Element ele_taxTimeStatistic = ele_taxTimeStatistics.get(i);
						String taxTimeStatistics = ele_taxTimeStatistic.text().replace("欠税统计时间： ", "").trim(); 
						taxesOwed.setTaxTimeStatistics(taxTimeStatistics); 
						//欠税总额
						Element ele_totalTaxe = ele_totalTaxes.get(i);
						String totalTaxes = ele_totalTaxe.text().replace("欠税总额：", "").trim(); 
						taxesOwed.setTotalTaxes(totalTaxes);
						//纳税人识别号
						Element ele_taxpayerIdNum = ele_taxpayerIdNums.get(i);
						String taxpayerIdNum = ele_taxpayerIdNum.text().replace("纳税人识别号：", "").trim(); 
						taxesOwed.setTaxpayerIdNum(taxpayerIdNum);
						
						taxesOweds.add(taxesOwed);
					} 
				} 
				publicRecord.setTaxesOweds(taxesOweds);
			}
			
			/*
			 * 电信欠费记录
			 */ 
			Element ele_flag_teleArrearsRecord = getfirstChildElement(ele, "span.h1 > strong:contains(电信欠费信息)"); 
			
			if(ele_flag_teleArrearsRecord!=null){
				List<TeleArrearsRecord> teleArrearsRecords = new ArrayList<TeleArrearsRecord>();
				Elements ele_operators = getElements(ele, "td.p:contains(电信运营商：)"); 
				Elements ele_businessTypes = getElements(ele, "td.p:contains(业务类型：)"); 
				Elements ele_accountingDates = getElements(ele, "td.p:contains(记账年月：)"); 
				Elements ele_businessOpeningDates = getElements(ele, "td.p:contains(业务开通时间：)"); 
				Elements ele_arrearsAmounts = getElements(ele, "td.p:contains(欠费金额：)"); 
				if(ele_operators!=null&&ele_operators.size()>0){ 
					for(int i=0;i<ele_operators.size();i++){
						TeleArrearsRecord teleArrearsRecord = new TeleArrearsRecord();
						//主管税务机关
						Element ele_operator = ele_operators.get(i);
						String operator = ele_operator.text().replace("电信运营商：", "").trim(); 
						teleArrearsRecord.setOperator(operator);
						
						Element ele_businessType = ele_businessTypes.get(i);
						String businessType = ele_businessType.text().replace("业务类型：", "").trim(); 
						teleArrearsRecord.setBusinessType(businessType);
						
						Element ele_accountingDate = ele_accountingDates.get(i);
						String accountingDate = ele_accountingDate.text().replace("记账年月：", "").trim(); 
						teleArrearsRecord.setAccountingDate(accountingDate);
						
						Element ele_businessOpeningDate = ele_businessOpeningDates.get(i);
						String businessOpeningDate = ele_businessOpeningDate.text().replace("业务开通时间：", "").trim(); 
						teleArrearsRecord.setBusinessOpeningDate(businessOpeningDate);
						
						Element ele_arrearsAmount = ele_arrearsAmounts.get(i);
						String arrearsAmount = ele_arrearsAmount.text().replace("欠费金额：", "").trim(); 
						teleArrearsRecord.setArrearsAmount(arrearsAmount);
						
						
						teleArrearsRecords.add(teleArrearsRecord);
					}
				}
				
				publicRecord.setTeleArrearsRecords(teleArrearsRecords);
			}
			
			/*
			 * 行政处罚记录
			 */ 
			Element ele_flag_administrativePunishmenRecord = getfirstChildElement(ele, "span.h1 > strong:contains(行政处罚记录)"); 
			
			if(ele_flag_administrativePunishmenRecord!=null){
				List<AdministrativePunishmenRecord> administrativePunishmenRecords = new ArrayList<AdministrativePunishmenRecord>();
				Elements ele_organizations = getElements(ele, "td.p:contains(处罚机构：)");  
				Elements ele_docNos = getElements(ele, "td.p:contains(文书编号：)"); 
				Elements ele_moneys = getElements(ele, "td.p:contains(处罚金额：)"); 
				Elements ele_effectiveTimes = getElements(ele, "td.p:contains(处罚生效时间：)"); 
				Elements ele_isReviews = getElements(ele, "td.p:contains(是否行政复议：)"); 
				Elements ele_reviewResults = getElements(ele, "td.p:contains(行政复议结果：)"); 
				Elements ele_deadlines = getElements(ele, "td.p:contains(处罚截止时间：)"); 
				if(ele_organizations!=null&&ele_organizations.size()>0){ 
					for(int i=0;i<ele_organizations.size();i++){
						AdministrativePunishmenRecord administrativePunishmenRecord = new AdministrativePunishmenRecord();
						//处罚机构
						Element ele_organization= ele_organizations.get(i);
						String organization = ele_organization.text().replace("处罚机构：", "").trim(); 
						administrativePunishmenRecord.setOrganization(organization); 
						
						//文书编号
						Element ele_docNo= ele_docNos.get(i);
						String docNo = ele_docNo.text().replace("文书编号：", "").trim(); 
						administrativePunishmenRecord.setDocNo(docNo);
						
						//处罚金额
						Element ele_money = ele_moneys.get(i);
						String money = ele_money.text().replace("处罚金额：", "").trim(); 
						administrativePunishmenRecord.setMoney(money);
						
						//处罚生效时间
						Element ele_effectiveTime = ele_effectiveTimes.get(i);
						String effectiveTime = ele_effectiveTime.text().replace("处罚生效时间：", "").trim(); 
						administrativePunishmenRecord.setEffectiveTime(effectiveTime);
						
						//是否行政复议
						Element ele_isReview = ele_isReviews.get(i);
						String isReview = ele_isReview.text().replace("是否行政复议：", "").trim(); 
						administrativePunishmenRecord.setIsReview(isReview); 
						
						//行政复议结果
						Element ele_reviewResult = ele_reviewResults.get(i);
						String reviewResult = ele_reviewResult.text().replace("行政复议结果：", "").trim(); 
						administrativePunishmenRecord.setReviewResult(reviewResult); 
						
						//处罚截止时间
						Element ele_deadline = ele_deadlines.get(i);
						String deadline = ele_deadline.text().replace("处罚截止时间：", "").trim(); 
						administrativePunishmenRecord.setDeadline(deadline); 
						
						administrativePunishmenRecords.add(administrativePunishmenRecord);
					}
				}
				
				publicRecord.setAdministrativePunishmenRecords(administrativePunishmenRecords);
			}
			
			/*
			 * 行政处罚记录
			 */ 
			Element ele_flag_enforcedRecord = getfirstChildElement(ele, "span.h1 > strong:contains(强制执行记录)"); 
			
			if(ele_flag_enforcedRecord!=null){
				List<EnforcedRecord> enforcedRecords = new ArrayList<EnforcedRecord>();
				Elements ele_courts = getElements(ele, "td.p:contains(执行法院：)");  
				Elements ele_caseNums = getElements(ele, "td.p:contains(案号：)"); 
				Elements ele_caseTypes = getElements(ele, "td.p:contains(执行案由：)"); 
				Elements ele_closedModes = getElements(ele, "td.p:contains(结案方式：)"); 
				Elements ele_filingTimes = getElements(ele, "td.p:contains(立案时间：)"); 
				Elements ele_caseStatuss = getElements(ele, "td.p:contains(案件状态：)"); 
				Elements ele_applyExecuteTargets = getElements(ele, "td.p:contains(申请执行标的：)"); 
				Elements ele_executedTargets = getElements(ele, "td.p:contains(已执行标的：)"); 
				Elements ele_applyExecuteTargetMoneys = getElements(ele, "td.p:contains(申请执行标的金额：)"); 
				Elements ele_executedTargetMoneys = getElements(ele, "td.p:contains(已执行标的金额：)"); 
				Elements ele_closedTimes = getElements(ele, "td.p:contains(结案时间：)"); 
				if(ele_courts!=null&&ele_courts.size()>0){ 
					for(int i=0;i<ele_courts.size();i++){
						EnforcedRecord enforcedRecord = new EnforcedRecord();
						//执行法院
						Element ele_court= ele_courts.get(i);
						String court = ele_court.text().replace("执行法院：", "").trim(); 
						enforcedRecord.setCourt(court); 
						
						//案号
						Element ele_caseNum= ele_caseNums.get(i);
						String caseNum = ele_caseNum.text().replace("案号：", "").trim(); 
						enforcedRecord.setCaseNum(caseNum);
						
						//执行案由
						Element ele_caseType = ele_caseTypes.get(i);
						String caseType = ele_caseType.text().replace("执行案由：", "").trim(); 
						enforcedRecord.setCaseType(caseType);
						
						//结案方式
						Element ele_closedMode = ele_closedModes.get(i);
						String closedMode = ele_closedMode.text().replace("结案方式：", "").trim(); 
						enforcedRecord.setClosedMode(closedMode);
						
						//立案时间
						Element ele_filingTime = ele_filingTimes.get(i);
						String filingTime = ele_filingTime.text().replace("立案时间：", "").trim(); 
						enforcedRecord.setFilingTime(filingTime);
						
						//案件状态
						Element ele_caseStatus = ele_caseStatuss.get(i);
						String caseStatus = ele_caseStatus.text().replace("案件状态：", "").trim(); 
						enforcedRecord.setCaseStatus(caseStatus);
						
						//申请执行标的
						Element ele_applyExecuteTarget = ele_applyExecuteTargets.get(i);
						String applyExecuteTarget = ele_applyExecuteTarget.text().replace("申请执行标的：", "").trim(); 
						enforcedRecord.setApplyExecuteTarget(applyExecuteTarget);
						
						//已执行标的
						Element ele_executedTarget = ele_executedTargets.get(i);
						String executedTarget = ele_executedTarget.text().replace("已执行标的：", "").trim(); 
						enforcedRecord.setExecutedTarget(executedTarget);
						
						//申请执行标的金额
						Element ele_applyExecuteTargetMoney = ele_applyExecuteTargetMoneys.get(i);
						String applyExecuteTargetMoney = ele_applyExecuteTargetMoney.text().replace("申请执行标的金额：", "").trim(); 
						enforcedRecord.setApplyExecuteTargetMoney(applyExecuteTargetMoney);
						
						//已执行标的金额
						Element ele_executedTargetMoney = ele_executedTargetMoneys.get(i);
						String executedTargetMoney = ele_executedTargetMoney.text().replace("已执行标的金额：", "").trim(); 
						enforcedRecord.setExecutedTargetMoney(executedTargetMoney);
						
						//结案时间
						Element ele_closedTime = ele_closedTimes.get(i);
						String closedTime = ele_closedTime.text().replace("结案时间：", "").trim(); 
						enforcedRecord.setClosedTime(closedTime);
						
						enforcedRecords.add(enforcedRecord);
					}
				}
				
				publicRecord.setEnforcedRecords(enforcedRecords);
			}
			
			
			
			pbcCreditReportFeed.setPublicRecord(publicRecord);
			
		}
		
		
		
		
		
		 /*
		 * 查询记录
		 */
		List<QueryRecord> queryRecords =new ArrayList<QueryRecord>(); 
		int index = 0;
		if (ele_table_queryRecord_org!=null) { //机构查询记录明细
			Elements ele_table_trs_queryRecord_org = getElements(ele_table_queryRecord_org, "tbody>tr");
			for (int i=0; i<ele_table_trs_queryRecord_org.size(); i++) {
				Element ele_table_tr_queryRecord_org = ele_table_trs_queryRecord_org.get(i);
				Elements ele_table_tds_queryRecord = getElements(ele_table_tr_queryRecord_org, "td");
				if (i==2 || ele_table_tds_queryRecord.size()!=4) {
					continue;
				}
				QueryRecord queryRecord = new QueryRecord();
				String num = ele_table_tds_queryRecord.get(0).text().trim();
				String queryDate = ele_table_tds_queryRecord.get(1).text().trim();
				String operator = ele_table_tds_queryRecord.get(2).text().trim();
				String queryCause = ele_table_tds_queryRecord.get(3).text().trim();
				queryRecord.setNum(num);
				queryRecord.setQueryDate(queryDate);
				queryRecord.setOperator(operator);
				queryRecord.setQueryCause(queryCause);
				queryRecord.setQueryType("机构查询");
				queryRecords.add(queryRecord);
				index++;
			}
		}
		if (ele_table_queryRecord_per!=null) { //个人查询记录明细
			Elements ele_table_trs_queryRecord_per = getElements(ele_table_queryRecord_per, "tbody>tr");
			for (int i=0; i<ele_table_trs_queryRecord_per.size(); i++) {
				Element ele_table_tr_queryRecord_per = ele_table_trs_queryRecord_per.get(i);
				Elements ele_table_tds_queryRecord = getElements(ele_table_tr_queryRecord_per, "td");
				if (i==2 || ele_table_tds_queryRecord.size()!=4) {
					continue;
				}
				QueryRecord queryRecord = new QueryRecord();
				String num = ele_table_tds_queryRecord.get(0).text().trim();
				String queryDate = ele_table_tds_queryRecord.get(1).text().trim();
				String operator = ele_table_tds_queryRecord.get(2).text().trim();
				String queryCause = ele_table_tds_queryRecord.get(3).text().trim();
				queryRecord.setNum((Integer.parseInt(num)+index)+"");
				queryRecord.setQueryDate(queryDate);
				queryRecord.setOperator(operator);
				queryRecord.setQueryCause(queryCause);
				queryRecord.setQueryType("个人查询");
				queryRecords.add(queryRecord);
			}
		}
		pbcCreditReportFeed.setQueryRecords(queryRecords);
		
		
		
		
		
		
		
		return pbcCreditReportFeed;
	}
	
	
	
	
	//解析信用卡明细部分字符串解析
	private static final String IS_QUASI_CREDIT_CARD = "isQuasiCreditCard"; //是否为准贷记卡
	private static final String ACCOUNT_STATUS = "accountStatus";			//账户状态
	private static final String CURRENCY = "currency";						//币种
	private static final String IS_OVERDUE = "isOverdue";					//是否发生过逾期
	private static final String ISSUE_DAY = "issueDay";						//发放日期
	private static final String ABORT_DAY = "abortDay";						//截至年月
	private static final String LIMIT = "limit";							//额度
	private static final String USED_LIMIT = "usedLimit";					//已使用额度
	private static final String OVERDUE_AMOUNT = "overdueAmount";			//逾期金额
	private static final String OVERDUE_NO = "overdueNo";					//最近5年内逾期次数
	private static final String OVERDUE_FOR_NO = "overdueForNo";			//最近5年内90天以上的逾期次数
	private static final String CANCELLATION_DAY = "cancellationDay";		//销户年月
	public PbcCreditReportFeed parseCreditCardDetails(PbcCreditReportFeed reportFeed) {
		List<CreditCardDetail> creditCardDetails = reportFeed.getCreditRecord().getCreditCardDetails();
		
		//System.out.println("creditCardDetails.size()------------"+creditCardDetails.size());
		if (creditCardDetails==null || creditCardDetails.isEmpty()) {
			return reportFeed;
		}
		
		List<String> matchResultList = null;
		String matchResultStr = null;
		List<Map<String,Object>> parsedCreditCardDetails = new ArrayList<Map<String,Object>>();
		for (CreditCardDetail creditCardDetail : creditCardDetails) {
			Map<String, String> details = creditCardDetail.getDetails();
			//System.out.println("details.size()----------------"+details.size());
			Set<Entry<String, String>> detailsEntrySet = details.entrySet();
			for (Entry<String, String> detailEntry : detailsEntrySet) {
				Map<String,Object> parsedCreditCardDetail = new LinkedHashMap<String, Object>();
				
				//
				//String num = detailEntry.getKey();
				String detailItem = detailEntry.getValue();
				//System.out.println("detailItem222-------"+detailItem);
				//是否为准贷记卡 若是则不解析
				if (detailItem.contains("准贷记卡")) {
					parsedCreditCardDetail.put(IS_QUASI_CREDIT_CARD, "是");
					parsedCreditCardDetails.add(parsedCreditCardDetail);
					//continue;
				}
				parsedCreditCardDetail.put(IS_QUASI_CREDIT_CARD, "否");
				
				//System.out.println("detailItem-------"+detailItem);
				//账户状态  
				if(detailItem.contains("逾期金额")){ //update 按照汇城要求，增加“逾期金额”判断，如有有该关键词，账号状态设置为“逾期” 2018-01-30
					parsedCreditCardDetail.put(ACCOUNT_STATUS, "逾期");
				}else if (detailItem.contains("已销户")) {
					parsedCreditCardDetail.put(ACCOUNT_STATUS, "销户");
				} else if (detailItem.contains("尚未激活")) {
					parsedCreditCardDetail.put(ACCOUNT_STATUS, "未激活");
				} else if (detailItem.contains("止付")) {
					parsedCreditCardDetail.put(ACCOUNT_STATUS, "止付");
				} else if (detailItem.contains("冻结")) {
					parsedCreditCardDetail.put(ACCOUNT_STATUS, "冻结");
				} else if (detailItem.contains("呆账")) {
					parsedCreditCardDetail.put(ACCOUNT_STATUS, "呆账");
				} else {
					parsedCreditCardDetail.put(ACCOUNT_STATUS, "正常");
				}
				
				//币种
				matchResultList = getSubStringByRegex(detailItem, "\uff08(?!.*\uff08)[\u4e00-\u9fa5].*?\u8d26\u6237\uff09");
				if (matchResultList.size()>0) {
					matchResultStr = matchResultList.get(0);
					matchResultStr = matchResultStr.replaceAll("\uff08|\u8d26\u6237\uff09", "");
					parsedCreditCardDetail.put(CURRENCY, matchResultStr);
				} else {
					parsedCreditCardDetail.put(CURRENCY, "其他");
				}
				
				//是否发生过逾期  & 逾期次数
				matchResultList = getSubStringByRegex(detailItem, "\u6700\u8fd15\u5e74\u5185\u6709\\d\\d?\u4e2a\u6708\u5904\u4e8e\u903e\u671f\u72b6\u6001");
				if (matchResultList.size()>0) {
					matchResultStr = matchResultList.get(0);
					matchResultStr = matchResultStr.replaceAll("\u6700\u8fd15\u5e74\u5185\u6709|\u4e2a\u6708\u5904\u4e8e\u903e\u671f\u72b6\u6001", "");
					parsedCreditCardDetail.put(OVERDUE_NO, matchResultStr);
					matchResultStr = Integer.parseInt(matchResultStr)>0 ? "是" : "否";
					parsedCreditCardDetail.put(IS_OVERDUE, matchResultStr);
				} else {
					parsedCreditCardDetail.put(OVERDUE_NO, "");
					parsedCreditCardDetail.put(IS_OVERDUE, "");
				}
				
				//发放日期
				matchResultList = getSubStringByRegex(detailItem, "^([12]\\d{3}\u5e74\\d\\d?\u6708\\d\\d?\u65e5)");
				if (matchResultList.size()>0) {
					matchResultStr = matchResultList.get(0).replaceAll("\u5e74|\u6708", ".").replace("\u65e5", "");
					parsedCreditCardDetail.put(ISSUE_DAY, matchResultStr);
				} else {
					parsedCreditCardDetail.put(ISSUE_DAY, "");
				}
				
				//截至年月
				matchResultList = getSubStringByRegex(detailItem, "\u622a\u81f3[12]\\d{3}\u5e74\\d\\d?\u6708");
				if (matchResultList.size()>0) {
					matchResultStr = matchResultList.get(0).replace("\u622a\u81f3", "").replace("\u5e74", ".").replace("\u6708", "");
					parsedCreditCardDetail.put(ABORT_DAY, matchResultStr);
				} else {
					parsedCreditCardDetail.put(ABORT_DAY, "");
				}
				
				//信用额度
				matchResultList = getSubStringByRegex(detailItem, "\u4fe1\u7528\u989d\u5ea6(\u6298\u5408\u4eba\u6c11\u5e01)?\\d+(,\\d{3})?");
				if (matchResultList.size()>0) {
					matchResultStr = matchResultList.get(0).replaceAll("\u4fe1\u7528\u989d\u5ea6(\u6298\u5408\u4eba\u6c11\u5e01)?|,", "");
					parsedCreditCardDetail.put(LIMIT, matchResultStr);
				} else {
					parsedCreditCardDetail.put(LIMIT, "");
				}
				
				//已使用额度
				matchResultList = getSubStringByRegex(detailItem, "\u5df2\u4f7f\u7528\u989d\u5ea6\\d+(,\\d{3})?");
				if (matchResultList.size()>0) {
					matchResultStr = matchResultList.get(0).replaceAll("\u5df2\u4f7f\u7528\u989d\u5ea6|,", "");
					parsedCreditCardDetail.put(USED_LIMIT, matchResultStr);
				} else {
					parsedCreditCardDetail.put(USED_LIMIT, "");
				}
				
				//逾期金额 OVERDUE_AMOUNT 
				matchResultList = getSubStringByRegex(detailItem, "\u903e\u671f\u91d1\u989d\\d+(,\\d{3})?");
				if (matchResultList.size()>0) {
					matchResultStr = matchResultList.get(0).replaceAll("\u903e\u671f\u91d1\u989d|,", "");
					parsedCreditCardDetail.put(OVERDUE_AMOUNT, matchResultStr);
				} else {
					parsedCreditCardDetail.put(OVERDUE_AMOUNT, "");	
				}
				
				//最近5年内90天以上的逾期次数 OVERDUE_FOR_NO	
				matchResultList = getSubStringByRegex(detailItem, "\u5176\u4e2d\\d\\d?\u4e2a\u6708\u903e\u671f\u8d85\u8fc790\u5929");
				if (matchResultList.size()>0) {
					matchResultStr = matchResultList.get(0).replaceAll("\u5176\u4e2d|\u4e2a\u6708\u903e\u671f\u8d85\u8fc790\u5929", "");
					parsedCreditCardDetail.put(OVERDUE_FOR_NO, matchResultStr);
				} else {
					parsedCreditCardDetail.put(OVERDUE_FOR_NO, "0");
				}
				
				//销户年月 CANCELLATION_DAY
				matchResultList = getSubStringByRegex(detailItem, "[12]\\d{3}\u5e74\\d\\d?\u6708\u5df2\u9500\u6237");
				if (matchResultList.size()>0) {
					matchResultStr = matchResultList.get(0).replace("\u5e74", ".").replace("\u6708\u5df2\u9500\u6237", "");
					parsedCreditCardDetail.put(CANCELLATION_DAY, matchResultStr);
				} else {
					parsedCreditCardDetail.put(CANCELLATION_DAY, "");
				}
				
				//System.out.println("parsedCreditCardDetail----------"+parsedCreditCardDetail);
				parsedCreditCardDetails.add(parsedCreditCardDetail);
			}
		}
		
		reportFeed.getCreditRecord().setParsedCreditCardDetails(parsedCreditCardDetails);
		return reportFeed;
	}
	
	
	//贷款明细部分字符串解析
	private static final String LOAN_ACCOUNT_STATUS = "accountStatus"; 	//账户状态*
	private static final String LOAN_CURRENCY = "currency";				//贷款种类		
	private static final String LOAN_IS_OVERDUE = "isOverdue"; 			//是否发生过逾期* 	
	private static final String LOAN_ISSUE_DAY = "issueDay"; 			//发放日期*		
	private static final String LOAN_ABORT_DAY = "abortDay"; 			//到期日期*		
	private static final String LOAN_ACTUAL_DAY = "actualDay"; 			//截至年月*		
	private static final String LOAN_CONTEACT_AMOUNT = "conteactAmount";//贷款合同金额*	
	private static final String LOAN_LOAN_BALANCE = "loanBalance"; 		//贷款余额*		
	private static final String LOAN_OVERDUE_AMOUNT = "overdueAmount";	//逾期金额*		
	private static final String LOAN_OVERDUE_NO = "overdueNo";			//最近5年内逾期次数* 
	private static final String LOAN_OVERDUE_FOR_NO = "overdueForNo";	//最近5年内90天以上的逾期次数  
	private static final String LOAN_SETTLE_DAY = "settleDay";			//结清年月		
	
	private Map<String, Object> parseCommonPartOfLoadDetails(String detailItem, Map<String, Object> parsedLoanDetail) {
		List<String> matchResultList = null;
		String matchResultStr = null;
		//贷款种类
		matchResultList = getSubStringByRegex(detailItem, "[\u5e01\u5143\u9551]{1}\uff09[\u4e00-\u9fa5\uff08\uff09]+\uff0c");
		if (matchResultList.size()>0) {
			matchResultStr = matchResultList.get(0);
			matchResultStr = matchResultStr.replaceAll("[\u5e01\u5143\u9551]{1}\uff09|\uff0c", "");
			parsedLoanDetail.put(LOAN_CURRENCY, matchResultStr);
		}
		
		//账户状态
		if (detailItem.contains("逾期金额")) {// update 按照汇城要求，将“逾期”改为“逾期金额” 2018-01-30
			parsedLoanDetail.put(LOAN_ACCOUNT_STATUS, "逾期");
		} else if (detailItem.contains("结清")) {
			parsedLoanDetail.put(LOAN_ACCOUNT_STATUS, "结清");
		} else if (detailItem.contains("转出")) {
			parsedLoanDetail.put(LOAN_ACCOUNT_STATUS, "转出");
		} else if (detailItem.contains("呆账")) {
			parsedLoanDetail.put(LOAN_ACCOUNT_STATUS, "呆账");
		} else {
			parsedLoanDetail.put(LOAN_ACCOUNT_STATUS, "正常");
		}
		
		//是否发生过逾期* 		LOAN_IS_OVERDUE	& LOAN_OVERDUE_NO
		matchResultList = getSubStringByRegex(detailItem, "\u6700\u8fd15\u5e74\u5185\u6709\\d\\d?\u4e2a\u6708\u5904\u4e8e\u903e\u671f\u72b6\u6001");
		if (matchResultList.size()>0) {
			matchResultStr = matchResultList.get(0);
			matchResultStr = matchResultStr.replaceAll("\u6700\u8fd15\u5e74\u5185\u6709|\u4e2a\u6708\u5904\u4e8e\u903e\u671f\u72b6\u6001", "");
			parsedLoanDetail.put(LOAN_OVERDUE_NO, matchResultStr);
			matchResultStr = Integer.parseInt(matchResultStr)>0 ? "是" : "否";
			parsedLoanDetail.put(LOAN_IS_OVERDUE, matchResultStr);
		} else {
			parsedLoanDetail.put(LOAN_OVERDUE_NO, "");
			parsedLoanDetail.put(LOAN_IS_OVERDUE, "");
		}
		
		//发放日期*			LOAN_ISSUE_DAY 
		matchResultList = getSubStringByRegex(detailItem, "^([12]\\d{3}\u5e74\\d\\d?\u6708\\d\\d?\u65e5)");
		if (matchResultList.size()>0) {
			matchResultStr = matchResultList.get(0).replaceAll("\u5e74|\u6708", ".").replace("\u65e5", "");
			parsedLoanDetail.put(LOAN_ISSUE_DAY, matchResultStr);
		} else {
			parsedLoanDetail.put(LOAN_ISSUE_DAY, "");
		}
		
		//到期日期*			LOAN_ABORT_DAY 
		matchResultList = getSubStringByRegex(detailItem, "[12]\\d{3}\u5e74\\d\\d?\u6708\\d\\d?\u65e5\u5230\u671f");
		if (matchResultList.size()>0) {
			matchResultStr = matchResultList.get(0).replaceAll("\u5e74|\u6708", ".").replace("\u65e5\u5230\u671f", "");
			parsedLoanDetail.put(LOAN_ABORT_DAY, matchResultStr);
		} else {
			parsedLoanDetail.put(LOAN_ABORT_DAY, "");
		}
		
		//截至年月*			LOAN_ACTUAL_DAY 
		matchResultList = getSubStringByRegex(detailItem, "\u622a\u81f3[12]\\d{3}\u5e74\\d\\d?\u6708");
		if (matchResultList.size()>0) {
			matchResultStr = matchResultList.get(0).replaceAll("\u622a\u81f3|\u6708", "").replace("\u5e74", ".");
			parsedLoanDetail.put(LOAN_ACTUAL_DAY, matchResultStr);
		} else {
			parsedLoanDetail.put(LOAN_ACTUAL_DAY, "");
		}
		
		//贷款合同金额*			LOAN_CONTEACT_AMOUNT
		matchResultList = getSubStringByRegex(detailItem, "\u53d1\u653e\u7684\\d+(,\\d{3})*");
		if (matchResultList.size()>0) {
			matchResultStr = matchResultList.get(0).replaceAll("\u53d1\u653e\u7684|,", "");
			parsedLoanDetail.put(LOAN_CONTEACT_AMOUNT, matchResultStr);
		} else {
			parsedLoanDetail.put(LOAN_CONTEACT_AMOUNT, "");
		}
		
		//贷款余额*			LOAN_LOAN_BALANCE 
		matchResultList = getSubStringByRegex(detailItem, "\u4f59\u989d\\d+(,\\d{3})*");
		if (matchResultList.size()>0) {
			matchResultStr = matchResultList.get(0).replaceAll("\u4f59\u989d|,", "");
			parsedLoanDetail.put(LOAN_LOAN_BALANCE, matchResultStr);
		} else {
			parsedLoanDetail.put(LOAN_LOAN_BALANCE, "");
		}
		
		//逾期金额*			LOAN_OVERDUE_AMOUNT 	
		matchResultList = getSubStringByRegex(detailItem, "\u903e\u671f\u91d1\u989d\\d+(,\\d{3})*");
		if (matchResultList.size()>0) {
			matchResultStr = matchResultList.get(0).replaceAll("\u903e\u671f\u91d1\u989d|,", "");
			parsedLoanDetail.put(LOAN_OVERDUE_AMOUNT, matchResultStr);
		} else {
			parsedLoanDetail.put(LOAN_OVERDUE_AMOUNT, "");
		}
		
		//最近5年内90天以上的逾期次数 	LOAN_OVERDUE_FOR_NO
		matchResultList = getSubStringByRegex(detailItem, "\u5176\u4e2d\\d\\d?\u4e2a\u6708\u903e\u671f\u8d85\u8fc790\u5929");
		if (matchResultList.size()>0) {
			matchResultStr = matchResultList.get(0).replaceAll("\u5176\u4e2d|\u4e2a\u6708\u903e\u671f\u8d85\u8fc790\u5929", "");
			parsedLoanDetail.put(LOAN_OVERDUE_FOR_NO, matchResultStr);
		} else {
			parsedLoanDetail.put(LOAN_OVERDUE_FOR_NO, "0");
		}
		
		//结清年月				LOAN_SETTLE_DAY	
		matchResultList = getSubStringByRegex(detailItem, "[12]\\d{3}\u5e74\\d\\d?\u6708\u5df2\u7ed3\u6e05");
		if (matchResultList.size()>0) {
			matchResultStr = matchResultList.get(0).replace("\u5e74", ".").replace("\u6708\u5df2\u7ed3\u6e05", "");
			parsedLoanDetail.put(LOAN_SETTLE_DAY, matchResultStr);
		} else {
			parsedLoanDetail.put(LOAN_SETTLE_DAY, "");
		}
		return parsedLoanDetail;
	}
	
	public PbcCreditReportFeed parseLoanDetails(PbcCreditReportFeed reportFeed) {
		List<Map<String,Object>> parsedLoanDetails = new ArrayList<Map<String,Object>>();
		
		//购房贷款明细
		List<HousingLoanDetail> housingLoanDetails = reportFeed.getCreditRecord().getHousingLoanDetails();
		if (housingLoanDetails!=null && !housingLoanDetails.isEmpty()) {
			for (HousingLoanDetail housingLoanDetail : housingLoanDetails) {
				Map<String, String> details = housingLoanDetail.getDetails();
				Set<Entry<String, String>> detailsEntrySet = details.entrySet();
				for (Entry<String, String> detailEntry : detailsEntrySet) {
					Map<String,Object> parsedLoanDetail = new LinkedHashMap<String, Object>();
					//
					//String num = detailEntry.getKey();
					String detailItem = detailEntry.getValue();
					
					//贷款种类
					parsedLoanDetail.put(LOAN_CURRENCY, "个人住房贷款");
					//解析除了贷款种类以外的字段
					parseCommonPartOfLoadDetails(detailItem, parsedLoanDetail);
					parsedLoanDetails.add(parsedLoanDetail);
				}
			}
		}
		
		//其他贷款明细
		List<OtherLoanDetail> otherLoanDetails = reportFeed.getCreditRecord().getOtherLoanDetails();
		if (otherLoanDetails!=null && !otherLoanDetails.isEmpty()) {
			for (OtherLoanDetail otherLoanDetail : otherLoanDetails) {
				Map<String, String> details = otherLoanDetail.getDetails();
				Set<Entry<String, String>> detailsEntrySet = details.entrySet();
				for (Entry<String, String> detailEntry : detailsEntrySet) {
					Map<String,Object> parsedLoanDetail = new LinkedHashMap<String, Object>();
					//
					//String num = detailEntry.getKey();
					String detailItem = detailEntry.getValue();
					
					//贷款种类
					parsedLoanDetail.put(LOAN_CURRENCY, "其他");
					//解析除了贷款种类以外的字段
					parseCommonPartOfLoadDetails(detailItem, parsedLoanDetail);
					parsedLoanDetails.add(parsedLoanDetail);
				}
			}
		}
		
		reportFeed.getCreditRecord().setParsedLoanDetails(parsedLoanDetails);
		return reportFeed;
	}
	
	
	
	//为他人担保信息
	private static final String GUARANTEED_PERSON = "guaranteedPerson"; 			//被担保人姓名
	private static final String GUARANTEED_PERSON_ID_NUM = "guaranteedPersonIdNum"; //被担保人身份证号
	private static final String OTHER_GUARANTEE_AMOUNT = "otherGuaranteeAmount";	//为他人贷款合同担保金额
	private static final String REAL_PRINCIPAL = "realPrincipal"; 					//被担保贷款实际本金余额
	private static final String ACTUAL_DAY = "actualDay"; 							//截至年月
	public PbcCreditReportFeed parseGuaranteeInfoDetails(PbcCreditReportFeed reportFeed) {
		List<Map<String,Object>> parsedGuaranteeInfoDetails = new ArrayList<Map<String,Object>>();
		List<String> matchResultList = null;
		String matchResultStr = null;
		
		Map<String, String> guaranteeInfoDetails = reportFeed.getCreditRecord().getGuaranteeInfoDetails();
		if (guaranteeInfoDetails==null || guaranteeInfoDetails.isEmpty()) {
			return reportFeed;
		}
		
		Set<Entry<String, String>> guaranteeInfoDetailsEntrySet = guaranteeInfoDetails.entrySet();
		for (Entry<String, String> guaranteeInfoDetailEntry : guaranteeInfoDetailsEntrySet) {
			Map<String, Object> parsedGuaranteeInfoDetail = new LinkedHashMap<String, Object>();
			
			//String num = guaranteeInfoDetailEntry.getKey();
			String detailItem = guaranteeInfoDetailEntry.getValue();
			
			//被担保人姓名
			matchResultList = getSubStringByRegex(detailItem, "\u4e3a[\u4e00-\u9fa5]+\uff08\u8bc1\u4ef6\u7c7b\u578b");
			if (matchResultList.size()>0) {
				matchResultStr = matchResultList.get(0).replaceAll("\u4e3a|\uff08\u8bc1\u4ef6\u7c7b\u578b", "");
				parsedGuaranteeInfoDetail.put(GUARANTEED_PERSON, matchResultStr);
			} else {
				parsedGuaranteeInfoDetail.put(GUARANTEED_PERSON, "");
			}
				
			//被担保人身份证号  GUARANTEED_PERSON_ID_NUM
			matchResultList = getSubStringByRegex(detailItem, "\u8eab\u4efd\u8bc1\uff0c\u8bc1\u4ef6\u53f7\u7801\uff1a(\\*+|\\d+)\\d{4}");
			if (matchResultList.size()>0) {
				matchResultStr = matchResultList.get(0).replace("\u8eab\u4efd\u8bc1\uff0c\u8bc1\u4ef6\u53f7\u7801\uff1a", "");
				parsedGuaranteeInfoDetail.put(GUARANTEED_PERSON_ID_NUM, matchResultStr);
			} else {
				parsedGuaranteeInfoDetail.put(GUARANTEED_PERSON_ID_NUM, "");
			}
			
			//为他人贷款合同担保金额  OTHER_GUARANTEE_AMOUNT
			matchResultList = getSubStringByRegex(detailItem, "\u62c5\u4fdd\u8d37\u6b3e\u5408\u540c\u91d1\u989d\\d+(,\\d{3})*");
			if (matchResultList.size()>0) {
				matchResultStr = matchResultList.get(0).replaceAll("\u62c5\u4fdd\u8d37\u6b3e\u5408\u540c\u91d1\u989d|,", "");
				parsedGuaranteeInfoDetail.put(OTHER_GUARANTEE_AMOUNT, matchResultStr);
			} else {
				parsedGuaranteeInfoDetail.put(OTHER_GUARANTEE_AMOUNT, "");
			}
			
			//被担保贷款实际本金余额  REAL_PRINCIPAL
			matchResultList = getSubStringByRegex(detailItem, "\u62c5\u4fdd\u8d37\u6b3e(\u672c\u91d1)?\u4f59\u989d\\d+(,\\d{3})*");
			if (matchResultList.size()>0) {
				matchResultStr = matchResultList.get(0).replaceAll("\u62c5\u4fdd\u8d37\u6b3e(\u672c\u91d1)?\u4f59\u989d|,", "");
				parsedGuaranteeInfoDetail.put(REAL_PRINCIPAL, matchResultStr);
			} else {
				parsedGuaranteeInfoDetail.put(REAL_PRINCIPAL, "");
			}
			
			//截至年月  ACTUAL_DAY
			matchResultList = getSubStringByRegex(detailItem, "\u622a\u81f3[12]\\d{3}\u5e74\\d\\d?\u6708\\d\\d?\u65e5");
			if (matchResultList.size()>0) {
				matchResultStr = matchResultList.get(0).replaceAll("\u622a\u81f3|\u65e5", "").replaceAll("\u5e74|\u6708", ".");
				parsedGuaranteeInfoDetail.put(ACTUAL_DAY, matchResultStr);
			} else {
				parsedGuaranteeInfoDetail.put(ACTUAL_DAY, "");
			}
			parsedGuaranteeInfoDetails.add(parsedGuaranteeInfoDetail);
		}
		
		reportFeed.getCreditRecord().setParsedGuaranteeInfoDetails(parsedGuaranteeInfoDetails);
		return reportFeed;
	}
	
	//保证人代偿信息  基于报告编号： 2016071200003006791339  编写解析规则，如有修改，需回归测试  2016071200003006791339
	private static final String RECENT_COMPENSATORY_TIME = "recentCompensatoryTime"; //最近一次代偿时间
	private static final String COMPENSATORY_AGENCY = "compensatoryAgency"; //代偿机构
	private static final String ACCUMULATIVE_COMPENSATORY_AMOUNT = "accumulativeCompensatoryAmount";	//累计代偿金
	private static final String BALANCE = "balance"; 					//余额 
	private static final String RECENT_REPAYMENT_DATE = "recentRepaymentDate"; 					//最近一次还款日期
	public PbcCreditReportFeed parseGuarantorCompensatoryinfoDetails(PbcCreditReportFeed reportFeed) {
		List<Map<String,Object>> parsedGuarantorCompensatoryinfoDetails = new ArrayList<Map<String,Object>>();
		
		Map<String, String> guarantorCompensatoryinfoDetails = reportFeed.getCreditRecord().getGuarantorCompensatoryinfoDetails();
		if (guarantorCompensatoryinfoDetails==null || guarantorCompensatoryinfoDetails.isEmpty()) {
			return reportFeed;
		}
		
		Set<Entry<String, String>> guarantorCompensatoryinfoDetailsEntrySet = guarantorCompensatoryinfoDetails.entrySet();
		for (Entry<String, String> guarantorCompensatoryinfoDetailsEntry : guarantorCompensatoryinfoDetailsEntrySet) {
			Map<String, Object> parsedGuarantorCompensatoryinfoDetail = new LinkedHashMap<String, Object>();
			 
			String detailItem = guarantorCompensatoryinfoDetailsEntry.getValue();
			LOGGER.info("detailItem--------"+detailItem);
			
			int indexDate =  detailItem.indexOf("日")+1;
			LOGGER.info("indexDate-------"+indexDate);
			int indexRecent =  detailItem.indexOf("进行最近一次代偿");
			LOGGER.info("indexRecent-------"+indexRecent);
			int indexMount =  detailItem.indexOf("累计代偿金额")+6;
			LOGGER.info("indexMount-------"+indexMount);
			int indexjh =  detailItem.indexOf("。");
			LOGGER.info("indexjh-------"+indexjh);
			int indexbalance =  detailItem.indexOf("余额")+2;
			LOGGER.info("indexbalance-------"+indexbalance);
			
			int indexEndjh =  detailItem.lastIndexOf("。");
			LOGGER.info("indexEndjh-------"+indexEndjh);
			
			int indexRecentDate =  detailItem.indexOf("最近一次还款日期为")+9;
			LOGGER.info("indexRecentDate-------"+indexRecentDate);
			
			int indexEndRecentDate =  detailItem.indexOf("，余额");
			if(indexEndRecentDate!=-1){
				LOGGER.info("indexEndRecentDate-------"+indexEndRecentDate); 
				String recentRepaymentDate = detailItem.substring(indexRecentDate, indexEndRecentDate);
				LOGGER.info("recentRepaymentDate---------"+recentRepaymentDate);
				parsedGuarantorCompensatoryinfoDetail.put(RECENT_REPAYMENT_DATE, recentRepaymentDate);
			}else{
				LOGGER.info("没有'最近一次还款日期'这项");
				parsedGuarantorCompensatoryinfoDetail.put(RECENT_REPAYMENT_DATE, "");
			}
			
			
			String compensatoryAgency = detailItem.substring(indexDate, indexRecent);
			LOGGER.info("compensatoryAgency---------"+compensatoryAgency);
			
			String recentCompensatoryTime = detailItem.substring(0, indexDate);
			LOGGER.info("recentCompensatoryTime---------"+recentCompensatoryTime);
			
			String accumulativeCompensatoryAmount = detailItem.substring(indexMount, indexjh);
			LOGGER.info("accumulativeCompensatoryAmount---------"+accumulativeCompensatoryAmount);
			
			String balance = detailItem.substring(indexbalance, indexEndjh);
			LOGGER.info("balance---------"+balance);
			 
			parsedGuarantorCompensatoryinfoDetail.put(COMPENSATORY_AGENCY, compensatoryAgency);
			parsedGuarantorCompensatoryinfoDetail.put(RECENT_COMPENSATORY_TIME, recentCompensatoryTime);
			parsedGuarantorCompensatoryinfoDetail.put(ACCUMULATIVE_COMPENSATORY_AMOUNT, accumulativeCompensatoryAmount.replaceAll(",", ""));
			parsedGuarantorCompensatoryinfoDetail.put(BALANCE, balance.replaceAll(",", ""));
			
		
			parsedGuarantorCompensatoryinfoDetails.add(parsedGuarantorCompensatoryinfoDetail);
		}
		
		reportFeed.getCreditRecord().setParsedGuarantorCompensatoryinfoDetails(parsedGuarantorCompensatoryinfoDetails);
		return reportFeed;
	}
	
	public PbcCreditReportFeed parseTaxesOwed(PbcCreditReportFeed reportFeed) {
		
		
		
		
		
		return reportFeed;
	}
	
	
	
	public static void main(String[] args) throws IOException {
		PbcCreditFeedParser pfp = new PbcCreditFeedParser();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		//String html = FileUtils.readFileToString(new File("D:/img/个人征信报告简版-姚艳青.htm"), "UTF-8");
		//String html = FileUtils.readFileToString(new File("D:/pbccrc/pbccrc_保证人代偿3.html"), "UTF-8");
		
		//String html = FileUtils.readFileToString(new File("D:/pbccrc/pbccrc_电信欠费信息.html"), "UTF-8"); 
		//String html = FileUtils.readFileToString(new File("D:/pbccrc/pbccrc_欠税记录.html"), "UTF-8");
		//String html = FileUtils.readFileToString(new File("D:/pbccrc/pbccrc_行政处罚记录.html"), "UTF-8"); 
		
		String html = FileUtils.readFileToString(new File("D:/pbccrc/王璞莹.html"), "UTF-8"); 
		
		PbcCreditReportFeed reportFeed = pfp.getPbcCreditFeed(html);
		Result<ReportData> reportResult = new Result<ReportData>();
		//LOGGER.info("==============================解析前===================================");
		System.out.println(gson.toJson(reportFeed));
		
		//解析信用卡明细
		reportFeed = pfp.parseCreditCardDetails(reportFeed);
		//解析贷款明细（住房贷款 和 其他贷款）
		reportFeed = pfp.parseLoanDetails(reportFeed);
		//解析担保信息明细
		reportFeed = pfp.parseGuaranteeInfoDetails(reportFeed); 
		//解析保证人代偿信息
		reportFeed = pfp.parseGuarantorCompensatoryinfoDetails(reportFeed);
		//解析公共信息 -- 欠税记录
		reportFeed = pfp.parseTaxesOwed(reportFeed);
		
		
		
		System.out.println("\n\n==============================解析后===================================");
		ReportData reportData = new ReportData("0", "查询成功", reportFeed, null);
		reportResult.setData(reportData);
		System.out.println(gson.toJson(reportResult));
	}
	
	/**
	 * 字符串转换unicode
	 */
	/*public static String string2Unicode(String string) {
	 
	    StringBuffer unicode = new StringBuffer();
	 
	    for (int i = 0; i < string.length(); i++) {
	 
	        // 取出每一个字符
	        char c = string.charAt(i);
	 
	        // 转换为unicode
	        unicode.append("\\u" + Integer.toHexString(c));
	    }
	 
	    return unicode.toString();
	}*/
}
