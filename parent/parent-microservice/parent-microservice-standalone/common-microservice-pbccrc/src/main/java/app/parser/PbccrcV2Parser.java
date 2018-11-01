package app.parser;

import app.bean.*;
import app.bean.PublicInformationDetail;
import app.bean.PublicRecord;
import com.crawler.pbccrc.json.*;
import com.microservice.dao.entity.crawler.pbccrc.*;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * Created by zmy on 2017/12/28.
 */
@Component
public class PbccrcV2Parser extends AbstractParser{
    private String reportId;

    /**
     * 解析人行征信报文（旧版）
     * @param reportData
     * @return
     * @throws IOException
     */
    public PbcCreditReport parserReportData(ReportData reportData, PlainPbccrcJson plainPbccrcJson) throws IOException {
        PbcCreditReport pbcCreditReport = new PbcCreditReport();
        String uuid = UUID.randomUUID().toString();  //唯一标识

        PbcCreditReportFeed report = reportData.getReport();

        if (report == null) {
            return pbcCreditReport;
        }

        //人行征信报告基本信息
        CreditBaseInfo creditBaseInfo = getCreditBaseInfo(report, uuid);
        pbcCreditReport.setCreditBaseInfo(creditBaseInfo);

        ReportBase reportBase = report.getReportBase();
        if (reportBase!=null) {
            String reportId = reportBase.getReportId();
            plainPbccrcJson.setMappingId(uuid);
            plainPbccrcJson.setReport_no(reportId);
        }

        //信息概要
        List<CreditRecordSummary> creditRecordSummaryList = getCreditRecordSummaries(report,uuid);
        pbcCreditReport.setCreditRecordSummaries(creditRecordSummaryList);

        //信贷记录详细信息（1.信用卡 2.住房贷款 3.其它贷款 4.为他人担保 5.保证人代偿）
        pbcCreditReport = getCreditRecordDetails(report, uuid,pbcCreditReport);
//        System.out.println("解析信用卡详细信息："+pbcCreditReport.getCreditCardRecordDetailAnalyzes());

        //查询记录
        List<QueryInformationDetail> queryInformationDetailList = getQueryInformationDetails(report, uuid);
        pbcCreditReport.setQueryInformationDetails(queryInformationDetailList);

        //公共记录（1.欠税记录 2.民事判决记录 3.强制执行记录 4.行政处罚记录 5.电信欠费记录）
        List<PublicInformationDetail> publicInformationDetails = new ArrayList<>();
        app.bean.PublicRecord publicRecords = getPublicRecords(report, uuid);

        PublicInformationDetail publicInformationDetail = new PublicInformationDetail();
        publicInformationDetail.setReport_no(creditBaseInfo.getReport_no());
        publicInformationDetail.setMapping_id(uuid);
        //公共记录-行政处罚记录
        List<PublicAdministrativePunishmen> publicAdministrativePunishmens = publicRecords.getPublicAdministrativePunishmens();
        if (publicAdministrativePunishmens != null && !publicAdministrativePunishmens.isEmpty()) {
            publicInformationDetail.setType("4");
            publicInformationDetail.setContent(publicAdministrativePunishmens);
            publicInformationDetails.add(publicInformationDetail);
        }

        //公共记录-强制执行记录
        List<PublicEnforcedRecord> publicEnforcedRecords = publicRecords.getPublicEnforcedRecords();
        if (publicEnforcedRecords != null && !publicEnforcedRecords.isEmpty()) {
            publicInformationDetail.setType("3");
            publicInformationDetail.setContent(publicEnforcedRecords);
            publicInformationDetails.add(publicInformationDetail);
        }

        //公共记录-欠税记录
        List<PublicTaxesOwed> publicTaxesOweds = publicRecords.getPublicTaxesOweds();
        if (publicTaxesOweds != null && !publicTaxesOweds.isEmpty()) {
            publicInformationDetail.setType("1");
            publicInformationDetail.setContent(publicTaxesOweds);
            publicInformationDetails.add(publicInformationDetail);
        }

        //公共记录-电信欠费信息
        List<PublicTeleArrearsRecord> publicTeleArrearsRecords = publicRecords.getPublicTeleArrearsRecords();
        if (publicTeleArrearsRecords != null && !publicTeleArrearsRecords.isEmpty()) {
            publicInformationDetail.setType("5");
            publicInformationDetail.setContent(publicTeleArrearsRecords);
            publicInformationDetails.add(publicInformationDetail);
        }
        pbcCreditReport.setPublicInformationDetails(publicInformationDetails);//公共记录
        return pbcCreditReport;
    }



    /**
     * 人行征信报告基本信息
     * @param report
     * @param uuid
     * @return
     */
    public CreditBaseInfo getCreditBaseInfo(PbcCreditReportFeed report, String uuid){
        CreditBaseInfo creditBaseInfo = new CreditBaseInfo();
        ReportBase reportBase = report.getReportBase();
        reportId = reportBase.getReportId();
        creditBaseInfo.setMapping_id(uuid);
        creditBaseInfo.setReport_no(reportId);
        creditBaseInfo.setUser_name(reportBase.getRealname());
        creditBaseInfo.setIdCard_type(reportBase.getCertificateType());
        creditBaseInfo.setIdCard_no(reportBase.getCertificateNum());
        creditBaseInfo.setQuery_time(reportBase.getQueryTime());
        creditBaseInfo.setReport_time(reportBase.getReportTime());
        creditBaseInfo.setMarital_status(reportBase.getMarriageStatus());
        return creditBaseInfo;
    }

    /**
     * 信息概要
     * @param report
     * @param uuid
     * @return
     */
    public List<CreditRecordSummary> getCreditRecordSummaries(PbcCreditReportFeed report, String uuid) {
        List<CreditRecordSummary> creditRecordSummaryList = new ArrayList<>();
        CreditRecord creditRecord = report.getCreditRecord();

        if (creditRecord == null) {
            return creditRecordSummaryList;
        }

        CreditSummary creditSummary = creditRecord.getCreditSummary();
        Map<String, String> creditCards = creditSummary.getCreditCards();//信用卡
        Map<String, String> housingLoans = creditSummary.getHousingLoans();//住房贷款
        Map<String, String> otherLoans = creditSummary.getOtherLoans();//其他贷款

        CreditRecordSummary creditCardSummary = new CreditRecordSummary();
        creditCardSummary.setMapping_id(uuid);
        creditCardSummary.setReport_no(reportId);
        creditCardSummary.setCredit_type("1");
        creditCardSummary.setAccount_num(creditCards.get("accountNum"));
        creditCardSummary.setUnSettle_unCancel(creditCards.get("activeNum"));
        creditCardSummary.setOverdue_account(creditCards.get("overdueNum"));
        creditCardSummary.setOverdue_ninety(creditCards.get("overdue90Num"));
        creditCardSummary.setGuarantee(creditCards.get("guaranteeNum"));
        creditRecordSummaryList.add(creditCardSummary);

        CreditRecordSummary housingLoanSummary = new CreditRecordSummary();
        housingLoanSummary.setMapping_id(uuid);
        housingLoanSummary.setReport_no(reportId);
        housingLoanSummary.setCredit_type("2");
        housingLoanSummary.setAccount_num(housingLoans.get("accountNum"));
        housingLoanSummary.setUnSettle_unCancel(housingLoans.get("activeNum"));
        housingLoanSummary.setOverdue_account(housingLoans.get("overdueNum"));
        housingLoanSummary.setOverdue_ninety(housingLoans.get("overdue90Num"));
        housingLoanSummary.setGuarantee(housingLoans.get("guaranteeNum"));
        creditRecordSummaryList.add(housingLoanSummary);

        CreditRecordSummary otherLoanSummary = new CreditRecordSummary();
        otherLoanSummary.setMapping_id(uuid);
        otherLoanSummary.setReport_no(reportId);
        otherLoanSummary.setCredit_type("3");
        otherLoanSummary.setAccount_num(otherLoans.get("accountNum"));
        otherLoanSummary.setUnSettle_unCancel(otherLoans.get("activeNum"));
        otherLoanSummary.setOverdue_account(otherLoans.get("overdueNum"));
        otherLoanSummary.setOverdue_ninety(otherLoans.get("overdue90Num"));
        otherLoanSummary.setGuarantee(otherLoans.get("guaranteeNum"));
        creditRecordSummaryList.add(otherLoanSummary);
        return creditRecordSummaryList;
    }

    /**
     * 查询记录
     * @param report
     * @param uuid
     * @return
     */
    public  List<QueryInformationDetail> getQueryInformationDetails(PbcCreditReportFeed report, String uuid){
        List<QueryInformationDetail> queryInformationDetailList = new ArrayList<>();
        List<QueryRecord> queryRecords = report.getQueryRecords();
        for (QueryRecord queryRecord : queryRecords) {
            QueryInformationDetail qid = new QueryInformationDetail();
            qid.setMapping_id(uuid);
            qid.setReport_no(reportId);
            String queryDate = queryRecord.getQueryDate();
            if (queryDate.contains("年")) {
                queryDate = queryDate.replaceAll("\u5e74|\u6708", "-").replace("\u65e5", "");
                qid.setQuery_dateTime(queryDate);
            }
            qid.setQuery_operator(queryRecord.getOperator());
            qid.setQuery_reason(queryRecord.getQueryCause());
            queryInformationDetailList.add(qid);
        }
        return queryInformationDetailList;
    }

    /**
     * 公共记录
     * 公共记录类型：1.欠税记录 2.民事判决记录 3.强制执行记录 4.行政处罚记录 5.电信欠费记录
     * @param report
     * @param uuid
     * @return
     */
    public app.bean.PublicRecord getPublicRecords(PbcCreditReportFeed report, String uuid){
        PublicRecord publicRecord = new PublicRecord();
        com.crawler.pbccrc.json.PublicRecord pbccrcPublicRecord = report.getPublicRecord();

        if (pbccrcPublicRecord == null) {
            return publicRecord;
        }

        List<AdministrativePunishmenRecord> administrativePunishmenRecords = pbccrcPublicRecord.getAdministrativePunishmenRecords();//行政处罚记录
        List<CivilJudgment> civilJudgments = pbccrcPublicRecord.getCivilJudgments();//民事判决（没有样本，暂时不做）
        List<EnforcedRecord> enforcedRecords = pbccrcPublicRecord.getEnforcedRecords(); //强制执行记录
        List<TaxesOwed> taxesOweds = pbccrcPublicRecord.getTaxesOweds();//欠税记录
        List<TeleArrearsRecord> teleArrearsRecords = pbccrcPublicRecord.getTeleArrearsRecords();//电信欠费信息

        //行政处罚记录
        List<PublicAdministrativePunishmen> publicAPList = new ArrayList<>();
        if (administrativePunishmenRecords != null && !administrativePunishmenRecords.isEmpty()) {
            for (AdministrativePunishmenRecord ap : administrativePunishmenRecords) {
                PublicAdministrativePunishmen publicAP = new PublicAdministrativePunishmen();
                publicAP.setMapping_id(uuid);
                publicAP.setReport_no(reportId);
                publicAP.setType("4");

                publicAP.setOrganization(ap.getOrganization());
                publicAP.setDocNo(ap.getDocNo());
                publicAP.setContent(ap.getContent());
                publicAP.setMoney(ap.getMoney());
                publicAP.setEffectiveTime(ap.getEffectiveTime());
                publicAP.setIsReview(ap.getIsReview());
                publicAP.setReviewResult(ap.getReviewResult());
                publicAP.setDeadline(ap.getDeadline());
                publicAPList.add(publicAP);
            }
            publicRecord.setPublicAdministrativePunishmens(publicAPList);
        }

        //强制执行记录
        List<PublicEnforcedRecord> publicERList = new ArrayList<>();
        if (enforcedRecords != null && !enforcedRecords.isEmpty()) {
            for (EnforcedRecord er : enforcedRecords) {
                PublicEnforcedRecord publicER = new PublicEnforcedRecord();
                publicER.setMapping_id(uuid);
                publicER.setReport_no(reportId);
                publicER.setType("3");

                publicER.setCourt(er.getCourt());
                publicER.setCaseNum(er.getCaseNum());
                publicER.setCaseType(er.getCaseType());
                publicER.setClosedMode(er.getClosedMode());
                publicER.setFilingTime(er.getFilingTime());
                publicER.setCaseStatus(er.getCaseStatus());
                publicER.setApplyExecuteTarget(er.getApplyExecuteTarget());
                publicER.setExecutedTarget(er.getExecutedTarget());
                publicER.setApplyExecuteTargetMoney(er.getApplyExecuteTargetMoney());
                publicER.setExecutedTargetMoney(er.getExecutedTargetMoney());
                publicER.setClosedTime(er.getClosedTime());
                publicERList.add(publicER);
            }
            publicRecord.setPublicEnforcedRecords(publicERList);
        }

        //欠税记录
        List<PublicTaxesOwed> publicTOList = new ArrayList<>();
        if (taxesOweds != null && !taxesOweds.isEmpty()) {
            for (TaxesOwed to : taxesOweds) {
                PublicTaxesOwed publicTO = new PublicTaxesOwed();
                publicTO.setMapping_id(uuid);
                publicTO.setReport_no(reportId);
                publicTO.setType("1");

                publicTO.setTaxAuthorities(to.getTaxAuthorities());
                publicTO.setTaxTimeStatistics(to.getTaxTimeStatistics());
                publicTO.setTotalTaxes(to.getTotalTaxes());
                publicTO.setTaxpayerIdNum(to.getTaxpayerIdNum());
                publicTOList.add(publicTO);
            }
            publicRecord.setPublicTaxesOweds(publicTOList);
        }

        //电信欠费信息
        List<PublicTeleArrearsRecord> publicTARList = new ArrayList<>();
        if (teleArrearsRecords != null && !teleArrearsRecords.isEmpty()) {
            for (TeleArrearsRecord tar : teleArrearsRecords) {
                PublicTeleArrearsRecord publicTAR = new PublicTeleArrearsRecord();
                publicTAR.setMapping_id(uuid);
                publicTAR.setReport_no(reportId);
                publicTAR.setType("5");

                publicTAR.setOperator(tar.getOperator());
                publicTAR.setBusinessType(tar.getBusinessType());
                publicTAR.setAccountingDate(tar.getAccountingDate());
                publicTAR.setBusinessOpeningDate(tar.getBusinessOpeningDate());
                publicTAR.setArrearsAmount(tar.getArrearsAmount());
                publicTARList.add(publicTAR);
            }
            publicRecord.setPublicTeleArrearsRecords(publicTARList);
        }

        return publicRecord;
    }

    /**
     * 信贷记录详细信息
     * @param report
     * @param uuid
     * @return
     */
    public PbcCreditReport getCreditRecordDetails(PbcCreditReportFeed report, String uuid,PbcCreditReport pbcCreditReport){
        List<CreditRecordDetail> creditRecordDetailList = new ArrayList<>();
        CreditRecord creditRecord = report.getCreditRecord();

        if (creditRecord == null) {
            return pbcCreditReport;
        }

        List<CreditCardDetail> creditCardDetails = creditRecord.getCreditCardDetails();//信用卡
        List<HousingLoanDetail> housingLoanDetails = creditRecord.getHousingLoanDetails();//住房贷款
        List<OtherLoanDetail> otherLoanDetails = creditRecord.getOtherLoanDetails();//其它贷款
        Map<String, String> guaranteeInfoDetails = creditRecord.getGuaranteeInfoDetails();//为他人担保
        Map<String, String> guarantorCompensatoryinfoDetails = creditRecord.getGuarantorCompensatoryinfoDetails();//保证人代偿信息

        List<CreditCardRecordDetail> ccrdList = new ArrayList<>();
        List<HousingLoanRecordDetail> hlrdList = new ArrayList<>();
        List<OtherLoanRecordDetail> olrdList = new ArrayList<>();
        List<GuaranteeInfoDetail> gidList = new ArrayList<>();
        List<GuarantorCompensatoryDetail> gcd = new ArrayList<>();

        CreditRecordDetail creditRecordDetail = null;
        //信用卡
        if (creditCardDetails != null) {
            for (CreditCardDetail ccd : creditCardDetails) {
                Map<String, String> details = ccd.getDetails();
                String type = ccd.getType();
                for (Map.Entry<String, String> entry : details.entrySet()) {
                    creditRecordDetail = new CreditRecordDetail();
                    String detailItem = entry.getValue();
                    creditRecordDetail.setMapping_id(uuid);
                    creditRecordDetail.setReport_no(reportId);
                    creditRecordDetail.setCredit_type("1");
                    creditRecordDetail.setAccount_type(type);
                    creditRecordDetail.setContent(detailItem);
                    creditRecordDetailList.add(creditRecordDetail);

                    //解析信用卡记录详细信息
                    CreditCardRecordDetail creditCardRecordDetail = new CreditCardRecordDetail();
                    creditCardRecordDetail.setReport_no(reportId);
                    creditCardRecordDetail.setMapping_id(uuid);
                    creditCardRecordDetail.setCreditRecordDetail(creditRecordDetail);
                    creditCardRecordDetail = parseCreditCardDetails(detailItem, creditCardRecordDetail);
                    ccrdList.add(creditCardRecordDetail);

                }
            }
        }
        //住房贷款
        if (housingLoanDetails != null) {
            for (HousingLoanDetail hld : housingLoanDetails) {
                Map<String, String> details = hld.getDetails();
                String type = hld.getType();
                for (Map.Entry<String, String> entry : details.entrySet()) {
                    creditRecordDetail = new CreditRecordDetail();
                    String detailItem = entry.getValue();
                    creditRecordDetail.setMapping_id(uuid);
                    creditRecordDetail.setReport_no(reportId);
                    creditRecordDetail.setCredit_type("2");
                    creditRecordDetail.setAccount_type(type);
                    creditRecordDetail.setContent(detailItem);
                    creditRecordDetailList.add(creditRecordDetail);

                    //解析住房贷款明细
                    HousingLoanRecordDetail housingLoanRecordDetail = new HousingLoanRecordDetail();
                    housingLoanRecordDetail.setReport_no(reportId);
                    housingLoanRecordDetail.setMapping_id(uuid);
                    housingLoanRecordDetail.setCreditRecordDetail(creditRecordDetail);
                    parseHousingLoanRecordDetails(detailItem, housingLoanRecordDetail);
                    hlrdList.add(housingLoanRecordDetail);
                }
            }
        }
        //其它贷款
        if (otherLoanDetails != null) {
            for (OtherLoanDetail old : otherLoanDetails) {
                Map<String, String> details = old.getDetails();
                String type = old.getType();
                for (Map.Entry<String, String> entry : details.entrySet()) {
                    creditRecordDetail = new CreditRecordDetail();
                    String detailItem = entry.getValue();
                    creditRecordDetail.setMapping_id(uuid);
                    creditRecordDetail.setReport_no(reportId);
                    creditRecordDetail.setCredit_type("3");
                    creditRecordDetail.setAccount_type(type);
                    creditRecordDetail.setContent(detailItem);
                    creditRecordDetailList.add(creditRecordDetail);

                    //解析其他贷款明细
                    OtherLoanRecordDetail otherLoanRecordDetail = new OtherLoanRecordDetail();
                    otherLoanRecordDetail.setReport_no(reportId);
                    otherLoanRecordDetail.setMapping_id(uuid);
                    otherLoanRecordDetail.setCreditRecordDetail(creditRecordDetail);
                    parseOtherLoanRecordDetails(detailItem, otherLoanRecordDetail);
                    olrdList.add(otherLoanRecordDetail);
                }
            }
        }
        //为他人担保
        if (guaranteeInfoDetails != null) {
            for (Map.Entry<String, String> entry : guaranteeInfoDetails.entrySet()) {
                creditRecordDetail = new CreditRecordDetail();
                String detailItem = entry.getValue();
                creditRecordDetail.setMapping_id(uuid);
                creditRecordDetail.setReport_no(reportId);
                creditRecordDetail.setCredit_type("4");
                creditRecordDetail.setContent(detailItem);
                creditRecordDetailList.add(creditRecordDetail);

                //解析为他人担保
                GuaranteeInfoDetail guaranteeInfoDetail = new GuaranteeInfoDetail();
                guaranteeInfoDetail.setReport_no(reportId);
                guaranteeInfoDetail.setMapping_id(uuid);
                guaranteeInfoDetail.setCreditRecordDetail(creditRecordDetail);
                parseGuaranteeInfoDetails(detailItem, guaranteeInfoDetail);
                gidList.add(guaranteeInfoDetail);
            }
        }

        //保证人代偿信息
        /*        guarantorCompensatoryinfoDetails = new HashMap<>();
        guarantorCompensatoryinfoDetails.put("1","2016年4月5日华安财产保险股份有限公司进行最近一次代偿，累计代偿金额500,000。最近一次还款日期为2016年8月31日，余额0。");
*/
        if (guarantorCompensatoryinfoDetails != null) {
            for (Map.Entry<String, String> entry : guarantorCompensatoryinfoDetails.entrySet()) {
                creditRecordDetail = new CreditRecordDetail();
                String detailItem = entry.getValue();
                creditRecordDetail.setMapping_id(uuid);
                creditRecordDetail.setReport_no(reportId);
                creditRecordDetail.setCredit_type("5");
                creditRecordDetail.setContent(detailItem);
                creditRecordDetailList.add(creditRecordDetail);

                //解析保证人代偿明细
                GuarantorCompensatoryDetail guarantorCompensatoryDetail = new GuarantorCompensatoryDetail();
                guarantorCompensatoryDetail.setReport_no(reportId);
                guarantorCompensatoryDetail.setMapping_id(uuid);
                guarantorCompensatoryDetail.setCreditRecordDetail(creditRecordDetail);
                parseGuarantorCompensatoryDetails(detailItem, guarantorCompensatoryDetail);
                gcd.add(guarantorCompensatoryDetail);
            }
        }

        pbcCreditReport.setCreditRecordDetails(creditRecordDetailList);//信贷记录详细信息
        pbcCreditReport.setCreditCardRecordDetailAnalyzes(ccrdList); //信用卡记录详细信息解析记录
        pbcCreditReport.setHousingLoanRecordDetailAnalyzes(hlrdList);//购房贷款记录详细信息解析记录
        pbcCreditReport.setOtherLoanRecordDetailAnalyzes(olrdList);//其他贷款记录详细信息解析记录
        pbcCreditReport.setGuaranteeInfoDetailAnalyzes(gidList); //为他人担保信息解析记录
        pbcCreditReport.setGuarantorCompensatoryDetailAnalyzes(gcd); //保证人代偿信息解析记录
        return pbcCreditReport;
    }

    /**
     * 解析信用卡明细
     * @param detailItem
     * @param creditCardDetail
     * @return
     */
    public static CreditCardRecordDetail parseCreditCardDetails(String detailItem,CreditCardRecordDetail creditCardDetail){
        List<String> matchResultList = null;
        String matchResultStr = "";
        //信用卡发放的时间
        matchResultList = getSubStringByRegex(detailItem, "^([12]\\d{3}\u5e74\\d\\d?\u6708\\d\\d?\u65e5)");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u5e74|\u6708", "-").replace("\u65e5", "");
            creditCardDetail.setGrant_date(matchResultStr);
        } else {
            creditCardDetail.setGrant_date("");
        }

        //发放信用卡银行的名称
        matchResultList = getSubStringByRegex(detailItem, "\u65e5\\D+?\u53d1\u653e\u7684");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u65e5|\u53d1\u653e\u7684", "");
            creditCardDetail.setBank_name(matchResultStr);
        } else {
            creditCardDetail.setBank_name("");
        }

        //信用卡类型
        matchResultList = getSubStringByRegex(detailItem, "\u53d1\u653e\u7684\\D+?\\uff08");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u53d1\u653e\u7684|\uff08", "");
            creditCardDetail.setCard_type(matchResultStr);
        } else {
            creditCardDetail.setCard_type("");
        }

        //账户的币种
        matchResultList = getSubStringByRegex(detailItem, "\uff08(?!.*\uff08)[\u4e00-\u9fa5].*?\uff09");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0);
            matchResultStr = matchResultStr.replaceAll("\uff08|\uff09", "");
            creditCardDetail.setAccount_currency(matchResultStr);
        } else {
            creditCardDetail.setAccount_currency("其他");
        }
        //本征信报告获取本信用卡信息的最后时间
        matchResultList = getSubStringByRegex(detailItem, "\u622a\u81f3[12]\\d{3}\u5e74\\d\\d?\u6708");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replace("\u622a\u81f3", "").replace("\u5e74", "-").replace("\u6708", "");
            creditCardDetail.setCutoff_date(matchResultStr);
        } else {
            creditCardDetail.setCutoff_date("");
        }
        //信用额度
        matchResultList = getSubStringByRegex(detailItem, "\u4fe1\u7528\u989d\u5ea6(\u6298\u5408\u4eba\u6c11\u5e01)?\\d+(,\\d{3})+");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u4fe1\u7528\u989d\u5ea6(\u6298\u5408\u4eba\u6c11\u5e01)?|,", "");
            creditCardDetail.setCredit_limit(matchResultStr);
        } else {
            creditCardDetail.setCredit_limit("0");
        }
        //已使用额度
        matchResultList = getSubStringByRegex(detailItem, "\u5df2\u4f7f\u7528\u989d\u5ea6\\d+(,\\d{3})+");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u5df2\u4f7f\u7528\u989d\u5ea6|,", "");
            creditCardDetail.setUsed_credit_line(matchResultStr);
        } else {
            creditCardDetail.setUsed_credit_line("0");
        }
        //透支余额
        matchResultList = getSubStringByRegex(detailItem, "\u900f\u652f\u4f59\u989d\\d+(,\\d{3})+");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u900f\u652f\u4f59\u989d|,", "");
            creditCardDetail.setOverdraft_balance(matchResultStr);
        } else {
            creditCardDetail.setOverdraft_balance("0");
        }

        //逾期金额
        matchResultList = getSubStringByRegex(detailItem, "\u903e\u671f\u91d1\u989d\\d+(,\\d{3})+");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u903e\u671f\u91d1\u989d|,", "");
            creditCardDetail.setOverdue_amount(matchResultStr);
        }else {
            creditCardDetail.setOverdue_amount("0");
        }
        //该账户是否激活过
        if (detailItem.contains("未激活")) {
            creditCardDetail.setIs_actived("false");
        } else {
            creditCardDetail.setIs_actived("true");
        }
        //该账户是否已销户
        if (detailItem.contains("已销户")) {
            creditCardDetail.setIs_closed("true");
        } else {
            creditCardDetail.setIs_closed("false");
        }

        //有过逾期记录的月数
        matchResultList = getSubStringByRegex(detailItem, "\\d\\d?\u4e2a\u6708\u5904\u4e8e\u903e\u671f\u72b6\u6001");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u4e2a\u6708\u5904\u4e8e\u903e\u671f\u72b6\u6001", "");
            creditCardDetail.setOverdue_month(matchResultStr);
            creditCardDetail.setIs_overdue("true");//该账户是否有逾期记录
        } else {
            creditCardDetail.setOverdue_month("0");
            creditCardDetail.setIs_overdue("false");
        }

        //准记卡账户有超过60天逾期记录月数
        if (detailItem.contains("准贷记卡")) {
            matchResultList = getSubStringByRegex(detailItem, "\u6709\\d\\d?\u4e2a\u6708\u900f\u652f\u8d85\u8fc760\u5929");
            if (matchResultList.size() > 0) {
                matchResultStr = matchResultList.get(0).replaceAll("\u6709|\u4e2a\u6708\u900f\u652f\u8d85\u8fc760\u5929", "");
                creditCardDetail.setSixtydays_overdraft_month(matchResultStr);
                creditCardDetail.setIs_sixtydays_overdraft("true");//准贷记卡账户是否有大于60天逾期记录
            } else {
                creditCardDetail.setSixtydays_overdraft_month("0");
                creditCardDetail.setIs_sixtydays_overdraft("false");
            }
        } else {
            creditCardDetail.setSixtydays_overdraft_month("0");
            creditCardDetail.setIs_sixtydays_overdraft("false");
        }

        //该账户有超过90天逾期记录月数
        matchResultList = getSubStringByRegex(detailItem, "\u5176\u4e2d\\d\\d?\u4e2a\u6708\u903e\u671f\u8d85\u8fc790\u5929");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u5176\u4e2d|\u4e2a\u6708\u903e\u671f\u8d85\u8fc790\u5929", "");
            creditCardDetail.setNintydays_overdue_month(matchResultStr);
            creditCardDetail.setIs_nintydays_overdue("true"); //该账户是否有超过90天
        } else {
            creditCardDetail.setNintydays_overdue_month("0");
            creditCardDetail.setIs_nintydays_overdue("false");
        }
        //是否已变成呆账
        if (detailItem.contains("已变成呆账")) {
            creditCardDetail.setBad_debts("true");
        } else {
            creditCardDetail.setBad_debts("false");
        }
        //余额(人民币)
        matchResultList = getSubStringByRegex(detailItem, "\uff0c\u4f59\u989d\\d+(,\\d{3})+");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\uff0c\u4f59\u989d|,", "");
            creditCardDetail.setRemaining_sum(matchResultStr);
        } else {
            creditCardDetail.setRemaining_sum("0");
        }

        //账户状态
        String status = "";
        if(detailItem.contains("逾期金额")){ //update 按照汇城要求，增加“逾期金额”判断，如有有该关键词，账号状态设置为“逾期” 2018-01-30
            status = "逾期";
        }else if (detailItem.contains("已销户")) {
            status = "销户";
        } else if (detailItem.contains("尚未激活")) {
            status = "未激活";
        } else if (detailItem.contains("止付")) {
            status = "止付";
        } else if (detailItem.contains("冻结")) {
            status = "冻结";
        } else if (detailItem.contains("呆账")) {
            status = "呆账";
        } else {
            status = "正常";
        }
        creditCardDetail.setStatus(status);

        // 最近五年逾期次数
        matchResultList = getSubStringByRegex(detailItem, "\u6700\u8fd15\u5e74\u5185\u6709\\d\\d?\u4e2a\u6708\u5904\u4e8e\u903e\u671f\u72b6\u6001");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0);
            matchResultStr = matchResultStr.replaceAll("\u6700\u8fd15\u5e74\u5185\u6709|\u4e2a\u6708\u5904\u4e8e\u903e\u671f\u72b6\u6001", "");
            creditCardDetail.setDelqL5yAmt(matchResultStr);
        } else {
            creditCardDetail.setDelqL5yAmt("0");
        }

        //最近5年内90天以上的逾期次数 OVERDUE_FOR_NO
        matchResultList = getSubStringByRegex(detailItem, "\u5176\u4e2d\\d\\d?\u4e2a\u6708\u903e\u671f\u8d85\u8fc790\u5929");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u5176\u4e2d|\u4e2a\u6708\u903e\u671f\u8d85\u8fc790\u5929", "");
            creditCardDetail.setDelqL5y90dayAmt(matchResultStr);
        } else {
            creditCardDetail.setDelqL5y90dayAmt("0");
        }

        //销户年月 CANCELLATION_DAY
        matchResultList = getSubStringByRegex(detailItem, "[12]\\d{3}\u5e74\\d\\d?\u6708\u5df2\u9500\u6237");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replace("\u5e74", ".").replace("\u6708\u5df2\u9500\u6237", "");
            creditCardDetail.setCancellDate(matchResultStr);
        } else {
            creditCardDetail.setCancellDate("");
        }

        return creditCardDetail;
    }

    /**
     * 解析购房贷款明细
     * @param detailItem
     * @param housingLoanRecordDetail
     * @return
     */
    public static HousingLoanRecordDetail parseHousingLoanRecordDetails(String detailItem, HousingLoanRecordDetail housingLoanRecordDetail){
        List<String> matchResultList = null;
        String matchResultStr = "";
        //贷款发放的时间
        matchResultList = getSubStringByRegex(detailItem, "^([12]\\d{3}\u5e74\\d\\d?\u6708\\d\\d?\u65e5)");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u5e74|\u6708", "-").replace("\u65e5", "");
            housingLoanRecordDetail.setGrant_date(matchResultStr);
        } else {
            housingLoanRecordDetail.setGrant_date("");
        }
        //发放贷款银行的名称
        matchResultList = getSubStringByRegex(detailItem, "\u65e5\\D+?\u53d1\u653e\u7684");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u65e5|\u53d1\u653e\u7684", "");
            housingLoanRecordDetail.setBank_name(matchResultStr);
        }else {
            housingLoanRecordDetail.setBank_name("");
        }
        //贷款币种
        matchResultList = getSubStringByRegex(detailItem, "\uff08(?!.*\u5143\uff08)[\u4e00-\u9fa5].*?\uff09");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0);
            matchResultStr = matchResultStr.replaceAll("\uff08|\uff09", "");
            housingLoanRecordDetail.setCurrency(matchResultStr);
        } else {
            housingLoanRecordDetail.setCurrency("其他");
        }

        //购房贷款总金额
        matchResultList = getSubStringByRegex(detailItem, "\u53d1\u653e\u7684\\d+(,\\d{3})+");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u53d1\u653e\u7684|,", "");
            housingLoanRecordDetail.setLoan_amount(matchResultStr);
        }else {
            housingLoanRecordDetail.setLoan_amount("0");
        }
        //贷款对象
        matchResultList = getSubStringByRegex(detailItem, "\uff09\\D+?\uff0c");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceFirst("\uff09", "").replaceAll("\uff0c","");
            housingLoanRecordDetail.setLoan_item(matchResultStr);
        } else {
            housingLoanRecordDetail.setLoan_item("");
        }
        //购房贷款到期日
        matchResultList = getSubStringByRegex(detailItem, "[12]\\d{3}\u5e74\\d\\d?\u6708\\d\\d?\u65e5\u5230\u671f");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u5e74|\u6708", "-").replace("\u65e5\u5230\u671f", "");
            housingLoanRecordDetail.setExpiration_date(matchResultStr);
        } else {
            housingLoanRecordDetail.setExpiration_date("");
        }
        //本征信报告获取本该购房贷款信息的最后时间
        matchResultList = getSubStringByRegex(detailItem, "\u622a\u81f3[12]\\d{3}\u5e74\\d\\d?\u6708");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replace("\u622a\u81f3", "").replace("\u5e74", "-").replace("\u6708", "");
            housingLoanRecordDetail.setCutoff_date(matchResultStr);
        } else {
            housingLoanRecordDetail.setCutoff_date("");
        }
        //购房贷款是否已结清
        if (detailItem.contains("已结清")) {
            housingLoanRecordDetail.setIs_closeout("true");
        } else {
            housingLoanRecordDetail.setIs_closeout("false");
        }
        //购房贷款余额
        matchResultList = getSubStringByRegex(detailItem, "\uff0c\u4f59\u989d\\d+(,\\d{3})+");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\uff0c\u4f59\u989d|,", "");
            housingLoanRecordDetail.setRemain_balance(matchResultStr);
        } else {
            housingLoanRecordDetail.setRemain_balance("0");
        }

        //购房贷款有逾期的月数
        matchResultList = getSubStringByRegex(detailItem, "\\d\\d?\u4e2a\u6708\u5904\u4e8e\u903e\u671f\u72b6\u6001");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u4e2a\u6708\u5904\u4e8e\u903e\u671f\u72b6\u6001", "");
            housingLoanRecordDetail.setOverdue_month(matchResultStr);
            housingLoanRecordDetail.setIs_overdue("true");//购房贷款是否有逾期
        } else {
            housingLoanRecordDetail.setOverdue_month("0");
            housingLoanRecordDetail.setIs_overdue("false");
        }
        //逾期金额
        matchResultList = getSubStringByRegex(detailItem, "\u903e\u671f\u91d1\u989d\\d+(,\\d{3})+");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u903e\u671f\u91d1\u989d|,", "");
            housingLoanRecordDetail.setOverdue_amount(matchResultStr);
        } else {
            housingLoanRecordDetail.setOverdue_amount("0");
        }
        //购房贷款是否有超过90天的逾期
        //购房逾期超过90天的月数
        matchResultList = getSubStringByRegex(detailItem, "\u5176\u4e2d\\d\\d?\u4e2a\u6708\u903e\u671f\u8d85\u8fc790\u5929");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u5176\u4e2d|\u4e2a\u6708\u903e\u671f\u8d85\u8fc790\u5929", "");
            housingLoanRecordDetail.setNintydays_oversue_month(matchResultStr);
            housingLoanRecordDetail.setIs_nintydays_overdue("true"); //该账户是否有超过90天
        } else {
            housingLoanRecordDetail.setNintydays_oversue_month("0");
            housingLoanRecordDetail.setIs_nintydays_overdue("false");
        }
        //是否已转出
        if (detailItem.contains("已转出")) {
            housingLoanRecordDetail.setTransfer("true");
        } else {
            housingLoanRecordDetail.setTransfer("false");
        }
        //转出日期
        matchResultList = getSubStringByRegex(detailItem, "[12]\\d{3}\u5e74\\d\\d?\u6708\u5df2\u8f6c\u51fa");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u5e74", "-").replace("\u6708\u5df2\u8f6c\u51fa", "");
            housingLoanRecordDetail.setTransfer_date(matchResultStr);
        } else {
            housingLoanRecordDetail.setTransfer_date("");
        }

        //账户状态
        String status = "正常";
        if (detailItem.contains("逾期金额")) {// update 按照汇城要求，将“逾期”改为“逾期金额” 2018-01-30
            status = "逾期";
        } else if (detailItem.contains("结清")) {
            status = "结清";
        } else if (detailItem.contains("转出")) {
            status = "转出";
        } else if (detailItem.contains("呆账")) {
            status = "呆账";
        }
        housingLoanRecordDetail.setStatus(status);

        //最近五年逾期次数
        matchResultList = getSubStringByRegex(detailItem, "\u6700\u8fd15\u5e74\u5185\u6709\\d\\d?\u4e2a\u6708\u5904\u4e8e\u903e\u671f\u72b6\u6001");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0);
            matchResultStr = matchResultStr.replaceAll("\u6700\u8fd15\u5e74\u5185\u6709|\u4e2a\u6708\u5904\u4e8e\u903e\u671f\u72b6\u6001", "");
            housingLoanRecordDetail.setDelqL5yAmt(matchResultStr);
        } else {
            housingLoanRecordDetail.setDelqL5yAmt("0");
        }

        //最近5年内90天以上的逾期次数
        matchResultList = getSubStringByRegex(detailItem, "\u5176\u4e2d\\d\\d?\u4e2a\u6708\u903e\u671f\u8d85\u8fc790\u5929");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u5176\u4e2d|\u4e2a\u6708\u903e\u671f\u8d85\u8fc790\u5929", "");
            housingLoanRecordDetail.setDelqL5y90dayAmt(matchResultStr);
        } else {
            housingLoanRecordDetail.setDelqL5y90dayAmt("0");
        }

        //结清年月
        matchResultList = getSubStringByRegex(detailItem, "[12]\\d{3}\u5e74\\d\\d?\u6708\u5df2\u7ed3\u6e05");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replace("\u5e74", "-").replace("\u6708\u5df2\u7ed3\u6e05", "");
            housingLoanRecordDetail.setSettleDate(matchResultStr);
        } else {
            housingLoanRecordDetail.setSettleDate("");
        }
        return housingLoanRecordDetail;
    }

    /**
     * 解析其他贷款明细
     * @param detailItem
     * @param otherLoanRecordDetail
     * @return
     */
    public static OtherLoanRecordDetail parseOtherLoanRecordDetails(String detailItem, OtherLoanRecordDetail otherLoanRecordDetail) {
        List<String> matchResultList = null;
        String matchResultStr = "";
        //贷款发放的时间
        matchResultList = getSubStringByRegex(detailItem, "^([12]\\d{3}\u5e74\\d\\d?\u6708\\d\\d?\u65e5)");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u5e74|\u6708", "-").replace("\u65e5", "");
            otherLoanRecordDetail.setGrant_date(matchResultStr);
        } else {
            otherLoanRecordDetail.setGrant_date("");
        }
        //发放贷款机构的名称
        matchResultList = getSubStringByRegex(detailItem, "\u65e5\\D+?\u53d1\u653e\u7684");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u65e5|\u53d1\u653e\u7684", "");
            otherLoanRecordDetail.setFinance_corporation(matchResultStr);
        } else {
            otherLoanRecordDetail.setFinance_corporation("");
        }
        //贷款币种
        matchResultList = getSubStringByRegex(detailItem, "\uff08(?!.*\uff08)[\u4e00-\u9fa5].*?\uff09");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0);
            matchResultStr = matchResultStr.replaceAll("\uff08|\uff09", "");
            otherLoanRecordDetail.setCurrency(matchResultStr);
        } else {
            otherLoanRecordDetail.setCurrency("其他");
        }
        //贷款总金额
        matchResultList = getSubStringByRegex(detailItem, "\u53d1\u653e\u7684\\d+(,\\d{3})+");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u53d1\u653e\u7684|,", "");
            otherLoanRecordDetail.setLoan_amount(matchResultStr);
        } else {
            otherLoanRecordDetail.setLoan_amount("0");
        }
        //贷款对象
        matchResultList = getSubStringByRegex(detailItem, "\uff09\\D+?\\uff0c");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\uff09|\\uff0c", "");
            otherLoanRecordDetail.setLoan_item(matchResultStr);
        } else {
            otherLoanRecordDetail.setLoan_item("");
        }
        //贷款到期日
        matchResultList = getSubStringByRegex(detailItem, "[12]\\d{3}\u5e74\\d\\d?\u6708\\d\\d?\u65e5\u5230\u671f");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u5e74|\u6708", "-").replace("\u65e5\u5230\u671f", "");
            otherLoanRecordDetail.setExpiration_date(matchResultStr);
        } else {
            otherLoanRecordDetail.setExpiration_date("");
        }
        //本征信报告获取本该贷款信息的最后时间
        matchResultList = getSubStringByRegex(detailItem, "\u622a\u81f3[12]\\d{3}\u5e74\\d\\d?\u6708");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replace("\u622a\u81f3", "").replace("\u5e74", "-").replace("\u6708", "");
            otherLoanRecordDetail.setCutoff_date(matchResultStr);
        } else {
            otherLoanRecordDetail.setCutoff_date("");
        }
        //贷款是否已结清
        if (detailItem.contains("结清")) {
            otherLoanRecordDetail.setIs_closeout("true");
        } else {
            otherLoanRecordDetail.setIs_closeout("false");
        }
        //贷款余额
        matchResultList = getSubStringByRegex(detailItem, "\uff0c\u4f59\u989d\\d+(,\\d{3})+");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\uff0c\u4f59\u989d|,", "");
            otherLoanRecordDetail.setRemain_balance(matchResultStr);
        } else {
            otherLoanRecordDetail.setRemain_balance("0");
        }
        //逾期金额
        matchResultList = getSubStringByRegex(detailItem, "\u903e\u671f\u91d1\u989d\\d+(,\\d{3})+");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u903e\u671f\u91d1\u989d|,", "");
            otherLoanRecordDetail.setOverdue_amount(matchResultStr);
        } else {
            otherLoanRecordDetail.setOverdue_amount("0");
        }
        //贷款有逾期的月数
        matchResultList = getSubStringByRegex(detailItem, "\\d\\d?\u4e2a\u6708\u5904\u4e8e\u903e\u671f\u72b6\u6001");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u4e2a\u6708\u5904\u4e8e\u903e\u671f\u72b6\u6001", "");
            otherLoanRecordDetail.setOverdue_month(matchResultStr);
            otherLoanRecordDetail.setIs_overdue("true");//贷款是否有逾期
        } else {
            otherLoanRecordDetail.setOverdue_month("0");
            otherLoanRecordDetail.setIs_overdue("false");
        }
        //贷款是否有超过90天的逾期
        //逾期超过90天的月数
        matchResultList = getSubStringByRegex(detailItem, "\u5176\u4e2d\\d\\d?\u4e2a\u6708\u903e\u671f\u8d85\u8fc790\u5929");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u5176\u4e2d|\u4e2a\u6708\u903e\u671f\u8d85\u8fc790\u5929", "");
            otherLoanRecordDetail.setNintydays_oversue_month(matchResultStr);
            otherLoanRecordDetail.setIs_nintydays_overdue("true"); //该账户是否有超过90天
        } else {
            otherLoanRecordDetail.setNintydays_oversue_month("0");
            otherLoanRecordDetail.setIs_nintydays_overdue("false");
        }
        //是否已变成呆账
        if (detailItem.contains("已变成呆账")) {
            otherLoanRecordDetail.setBad_debts("true");
            //余额(人民币) (区分贷款余额)
            matchResultList = getSubStringByRegex(detailItem, "\uff0c\u4f59\u989d\\d+(,\\d{3})+");
            if (matchResultList.size()>0) {
                matchResultStr = matchResultList.get(0).replaceAll("\uff0c\u4f59\u989d|,", "");
                otherLoanRecordDetail.setRemaining_sum(matchResultStr);
            } else {
                otherLoanRecordDetail.setRemaining_sum("0");
            }
        } else {
            otherLoanRecordDetail.setRemaining_sum("0");
            otherLoanRecordDetail.setBad_debts("false");
        }

        //账户状态
        String status = "正常";
        if (detailItem.contains("逾期金额")) {// update 按照汇城要求，将“逾期”改为“逾期金额” 2018-01-30
            status = "逾期";
        } else if (detailItem.contains("结清")) {
            status = "结清";
        } else if (detailItem.contains("转出")) {
            status = "转出";
        } else if (detailItem.contains("呆账")) {
            status = "呆账";
        }
        otherLoanRecordDetail.setStatus(status);

        //最近五年逾期次数
        matchResultList = getSubStringByRegex(detailItem, "\u6700\u8fd15\u5e74\u5185\u6709\\d\\d?\u4e2a\u6708\u5904\u4e8e\u903e\u671f\u72b6\u6001");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0);
            matchResultStr = matchResultStr.replaceAll("\u6700\u8fd15\u5e74\u5185\u6709|\u4e2a\u6708\u5904\u4e8e\u903e\u671f\u72b6\u6001", "");
            otherLoanRecordDetail.setDelqL5yAmt(matchResultStr);
        } else {
            otherLoanRecordDetail.setDelqL5yAmt("0");
        }

        //最近5年内90天以上的逾期次数
        matchResultList = getSubStringByRegex(detailItem, "\u5176\u4e2d\\d\\d?\u4e2a\u6708\u903e\u671f\u8d85\u8fc790\u5929");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u5176\u4e2d|\u4e2a\u6708\u903e\u671f\u8d85\u8fc790\u5929", "");
            otherLoanRecordDetail.setDelqL5y90dayAmt(matchResultStr);
        } else {
            otherLoanRecordDetail.setDelqL5y90dayAmt("0");
        }

        //结清年月
        matchResultList = getSubStringByRegex(detailItem, "[12]\\d{3}\u5e74\\d\\d?\u6708\u5df2\u7ed3\u6e05");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replace("\u5e74", "-").replace("\u6708\u5df2\u7ed3\u6e05", "");
            otherLoanRecordDetail.setSettleDate(matchResultStr);
        } else {
            otherLoanRecordDetail.setSettleDate("");
        }
        return otherLoanRecordDetail;
    }

    /**
     * 解析为他人担保
     * @param detailItem
     * @param guaranteeInfoDetail
     * @return
     */
    public static GuaranteeInfoDetail parseGuaranteeInfoDetails(String detailItem, GuaranteeInfoDetail guaranteeInfoDetail) {
        List<String> matchResultList = null;
        String matchResultStr = null;
        //被担保人姓名
        matchResultList = getSubStringByRegex(detailItem, "\u4e3a[\u4e00-\u9fa5]+\uff08\u8bc1\u4ef6\u7c7b\u578b");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u4e3a|\uff08\u8bc1\u4ef6\u7c7b\u578b", "");
            guaranteeInfoDetail.setGuaranteedPerson(matchResultStr);
        } else {
            guaranteeInfoDetail.setGuaranteedPerson("");
        }

        //被担保人身份证号  GUARANTEED_PERSON_ID_NUM
        matchResultList = getSubStringByRegex(detailItem, "\u8eab\u4efd\u8bc1\uff0c\u8bc1\u4ef6\u53f7\u7801\uff1a(\\*+|\\d+)\\d{4}");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replace("\u8eab\u4efd\u8bc1\uff0c\u8bc1\u4ef6\u53f7\u7801\uff1a", "");
            guaranteeInfoDetail.setGuaranteedPersonIdNum(matchResultStr);
        } else {
            guaranteeInfoDetail.setGuaranteedPersonIdNum("");
        }

        //为他人贷款合同担保金额  OTHER_GUARANTEE_AMOUNT
        matchResultList = getSubStringByRegex(detailItem, "\u62c5\u4fdd\u8d37\u6b3e\u5408\u540c\u91d1\u989d\\d+(,\\d{3})*");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u62c5\u4fdd\u8d37\u6b3e\u5408\u540c\u91d1\u989d|,", "");
            guaranteeInfoDetail.setOtherGuaranteeAmount(matchResultStr);
        } else {
            guaranteeInfoDetail.setOtherGuaranteeAmount("0");
        }

        //被担保贷款实际本金余额  REAL_PRINCIPAL
        matchResultList = getSubStringByRegex(detailItem, "\u62c5\u4fdd\u8d37\u6b3e(\u672c\u91d1)?\u4f59\u989d\\d+(,\\d{3})*");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u62c5\u4fdd\u8d37\u6b3e(\u672c\u91d1)?\u4f59\u989d|,", "");
            guaranteeInfoDetail.setRealPrincipal(matchResultStr);
        } else {
            guaranteeInfoDetail.setRealPrincipal("0");
        }

        //截至年月  ACTUAL_DAY
        matchResultList = getSubStringByRegex(detailItem, "\u622a\u81f3[12]\\d{3}\u5e74\\d\\d?\u6708\\d\\d?\u65e5");
        if (matchResultList.size()>0) {
            matchResultStr = matchResultList.get(0).replaceAll("\u622a\u81f3|\u65e5", "").replaceAll("\u5e74|\u6708", "-");
            guaranteeInfoDetail.setActualDay(matchResultStr);
        } else {
            guaranteeInfoDetail.setActualDay("");
        }
        return guaranteeInfoDetail;
    }

    /**
     * 解析保证人代偿信息
     * @param detailItem
     * @param guarantorCompensatoryDetail
     * @return
     */
    public static GuarantorCompensatoryDetail parseGuarantorCompensatoryDetails(String detailItem, GuarantorCompensatoryDetail guarantorCompensatoryDetail){
        int indexDate =  detailItem.indexOf("日")+1;
        int indexRecent =  detailItem.indexOf("进行最近一次代偿");
        int indexMount =  detailItem.indexOf("累计代偿金额")+6;
        int indexjh =  detailItem.indexOf("。");
        int indexbalance =  detailItem.indexOf("余额")+2;
        int indexEndjh =  detailItem.lastIndexOf("。");
        int indexRecentDate =  detailItem.indexOf("最近一次还款日期为")+9;
        int indexEndRecentDate =  detailItem.indexOf("，余额");
        //最近一次代偿时间
        if(indexEndRecentDate!=-1){
            String recentRepaymentDate = detailItem.substring(indexRecentDate, indexEndRecentDate);
            if (recentRepaymentDate.contains("年")) {
                recentRepaymentDate = recentRepaymentDate.replaceAll("\u5e74|\u6708", "-").replace("\u65e5", "");
                guarantorCompensatoryDetail.setRecentRepaymentDate(recentRepaymentDate);//最近一次代偿时间
            } else {
                guarantorCompensatoryDetail.setRecentRepaymentDate("");
            }
        } else {
            guarantorCompensatoryDetail.setRecentRepaymentDate("");
        }

        //代偿机构
        String compensatoryAgency = detailItem.substring(indexDate, indexRecent);
        if (compensatoryAgency != null) {
            guarantorCompensatoryDetail.setCompensatoryAgency(compensatoryAgency);
        } else {
            guarantorCompensatoryDetail.setCompensatoryAgency("");
        }

        //最近一次还款日期
        String recentCompensatoryTime = detailItem.substring(0, indexDate);
        if (recentCompensatoryTime.contains("年")) {
            recentCompensatoryTime = recentCompensatoryTime.replaceAll("\u5e74|\u6708", "-").replace("\u65e5", "");
            guarantorCompensatoryDetail.setRecentCompensatoryTime(recentCompensatoryTime);
        } else {
            guarantorCompensatoryDetail.setRecentCompensatoryTime("");
        }

        //累计代偿金
        String accumulativeCompensatoryAmount = detailItem.substring(indexMount, indexjh);
        if (accumulativeCompensatoryAmount != null) {
            accumulativeCompensatoryAmount = accumulativeCompensatoryAmount.replaceAll(",", "");
            guarantorCompensatoryDetail.setAccumulativeCompensatoryAmount(accumulativeCompensatoryAmount);
        } else {
            guarantorCompensatoryDetail.setAccumulativeCompensatoryAmount("0");
        }

        //余额
        String balance = detailItem.substring(indexbalance, indexEndjh);
        if (balance != null) {
            balance = balance.replaceAll(",", "");
            guarantorCompensatoryDetail.setBalance(balance);
        } else {
            guarantorCompensatoryDetail.setBalance("0");
        }

        return guarantorCompensatoryDetail;
    }

    //授权码是否可用
    public boolean isOverdueTradeCodeParser(String html){
        boolean flag = false;
        if (!StringUtils.isEmpty(html)) {
            Document doc = Jsoup.parse(html);
            Elements inputs = doc.select("input[name=reportformat]");
            String val = "";
            for (Element input : inputs) {
                System.out.println("======val===" + input.val());
                if (input.toString().contains("disabled")) {
                    val += input.val();
                }
            }

            if ("252421".equals(val)) {
                return true;
            }
        }
        return flag;
    }
}
