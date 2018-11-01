package app.parser.pbccrc;

import app.bean.pbccrc.PbcCreditReport;
import com.crawler.report.json.jiemo.pbccrc.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.pbccrc.*;
import com.microservice.dao.entity.crawler.pbccrc.CreditRecordSummary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析报告
 * Created by zmy on 2018/6/23.
 */
@Component
public class PbccrcReportParser extends AbstractParser{

    /**
     * 借么-征信报告json
     * @param pbcCreditReport
     * @return
     */
    public PbccrcCreditReportJiemo getReportJiemoParser(PbcCreditReport pbcCreditReport){
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        PbccrcCreditReportJiemo report = null;
        if (pbcCreditReport == null) {
            return null;
        }
        report = new PbccrcCreditReportJiemo();
        //基本信息
        CreditBaseInfo creditBaseInfo = pbcCreditReport.getCreditBaseInfo();
        CreditReportSimple creditReportSimple = new CreditReportSimple();
        String queryTimeStr = creditBaseInfo.getQuery_time();
        String queryTime = "";
        if (queryTimeStr != null) {
            queryTime = queryTimeStr.replaceAll("\\.", "-");
        }
        creditReportSimple.setQueryTime(queryTime);
        creditReportSimple.setName(creditBaseInfo.getUser_name());
        creditReportSimple.setIdCard(creditBaseInfo.getIdCard_no());
        String marriage = creditBaseInfo.getMarital_status();
        creditReportSimple.setMarriage(getMarriage(marriage));
        String reportTime = creditBaseInfo.getReport_time();
        String generatedTime = "";
        if (reportTime != null) {
            generatedTime = reportTime.replaceAll("\\.", "-");
        }
        creditReportSimple.setGeneratedTime(generatedTime);
        creditReportSimple.setLoginIp("");
        report.setCreditReportSimple(creditReportSimple);

        //信贷记录信息
        List<CreditRecordSummary> creditRecordSummaries = pbcCreditReport.getCreditRecordSummaries();
        List<com.crawler.report.json.jiemo.pbccrc.CreditRecordSummary> creditRecordSummariesJiemo = new ArrayList<>();
        for (CreditRecordSummary crsummary : creditRecordSummaries) {
            com.crawler.report.json.jiemo.pbccrc.CreditRecordSummary crsummaryJiemo = new com.crawler.report.json.jiemo.pbccrc.CreditRecordSummary();
            String creditType = crsummary.getCredit_type();
            crsummaryJiemo.setCreditType(getCreditType(creditType));
            crsummaryJiemo.setAccountNum(crsummary.getAccount_num());
            crsummaryJiemo.setUnsettleUncancel(crsummary.getUnSettle_unCancel());
            crsummaryJiemo.setOverdueAccount(crsummary.getOverdue_account());
            crsummaryJiemo.setOverdueNinety(crsummary.getOverdue_ninety());
            crsummaryJiemo.setGuarantee(crsummary.getGuarantee());
            creditRecordSummariesJiemo.add(crsummaryJiemo);
        }
        report.setCreditRecordSummaries(creditRecordSummariesJiemo);

        //信用卡明细
        List<CreditCardRecordDetail> creditCardRecordDetails = pbcCreditReport.getCreditCardRecordDetailAnalyzes();
        List<CreditCardInfo> creditCardInfos = new ArrayList<>();
        for (CreditCardRecordDetail ccrdetails : creditCardRecordDetails) {
            CreditCardInfo creditCardInfo = new CreditCardInfo();
            creditCardInfo.setQueryId("");   //编号 ??
            String status = ccrdetails.getStatus();
            creditCardInfo.setStatus(getStatus(status));   //账户状态
            String accountType = ccrdetails.getAccount_currency();
            creditCardInfo.setAccountType(getAccountType(accountType));   //币种
            creditCardInfo.setIsDelq(parserFlag(ccrdetails.getIs_overdue()));   //是否发生过逾期
            String is_closed = ccrdetails.getIs_closed();

            creditCardInfo.setIsClosed(parserFlag(is_closed));   //该账户是否已销户
            creditCardInfo.setIsActived(parserFlag(ccrdetails.getIs_actived()));   //该账户是否激活过

            String cardType = ccrdetails.getCard_type();
            creditCardInfo.setCardType(getCardType(cardType));   //信用卡类型
            creditCardInfo.setBankName(ccrdetails.getBank_name());   //发放信用卡银行的名称
            creditCardInfo.setBadDebts(ccrdetails.getBad_debts());   //是否已变成呆账
            creditCardInfo.setReleaseDate(toDateFormat(ccrdetails.getGrant_date(),"yyyy-MM-dd","yyyy-MM-dd"));   //发放日期
            creditCardInfo.setDueDate(toDateFormat(ccrdetails.getCutoff_date(),"yyyy-MM","yyyy-MM-dd"));   //截至年月
            creditCardInfo.setCreditAmt(ccrdetails.getCredit_limit());   //额度
            creditCardInfo.setUsedAmt(ccrdetails.getUsed_credit_line());   //已使用额度
            creditCardInfo.setOverDueAmount(ccrdetails.getOverdue_amount());   //逾期金额
            creditCardInfo.setDelqL5yAmt(ccrdetails.getDelqL5yAmt());   //最近五年逾期次数 ??
            creditCardInfo.setIsSixtydaysOverdraft(ccrdetails.getIs_sixtydays_overdraft());   //准贷记卡账户是否有大于60天逾期记录
            creditCardInfo.setSixtydaysOverdraftMonth(ccrdetails.getSixtydays_overdraft_month());   //准记卡账户有超过60天逾期记录月数
            creditCardInfo.setDelqL5y90dayAmt(ccrdetails.getDelqL5y90dayAmt());   //最近五年90天以上逾期次数(数据类型为整型，无字典)
            creditCardInfo.setCancellDate(toDateFormat(ccrdetails.getCancellDate(),"yyyy-MM-dd","yyyy-MM-dd"));   //销户年月 ??
            creditCardInfos.add(creditCardInfo);
        }
        report.setCreditCardInfo(creditCardInfos);

        //贷款明细（1:住房贷款、2:其他贷款）
        List<HousingLoanRecordDetail> housingLoanRecordDetails = pbcCreditReport.getHousingLoanRecordDetailAnalyzes();
        List<OtherLoanRecordDetail> otherLoanRecordDetails = pbcCreditReport.getOtherLoanRecordDetailAnalyzes();
        List<CreditLoanInfo> creditLoanInfoJiemoList = new ArrayList<>();
        for (HousingLoanRecordDetail housing : housingLoanRecordDetails) {
            CreditLoanInfo creditLoanInfoJiemo = new CreditLoanInfo();
            creditLoanInfoJiemo.setQueryID("");   //编号 ??
            String status = housing.getStatus();
            creditLoanInfoJiemo.setStatus(getLoanStatus(status));   //账户状态
            creditLoanInfoJiemo.setLoanType(getLoanType("个人住房贷款"));   //贷款种类
            creditLoanInfoJiemo.setFinanceCorporationBankName(housing.getBank_name());   //发放贷款机构名称，发放贷款银行的名称
            creditLoanInfoJiemo.setIsDelq(parserFlag(housing.getIs_overdue()));   //是否发生过逾期
            creditLoanInfoJiemo.setIsCloseout(parserFlag(housing.getIs_closeout()));   //贷款是否结清，购房贷款是否已结清
            creditLoanInfoJiemo.setTransfer(housing.getTransfer());   //是否已转出
            creditLoanInfoJiemo.setBadDebts("false");   //是否已变成呆账（其他贷款中的字段）
            creditLoanInfoJiemo.setReleasedate(toDateFormat(housing.getGrant_date(),"yyyy-MM-dd","yyyy-MM-dd"));   //发放日期
            creditLoanInfoJiemo.setDeadline(toDateFormat(housing.getExpiration_date(),"yyyy-MM-dd","yyyy-MM-dd"));   //到期日期
            creditLoanInfoJiemo.setDueDate(toDateFormat(housing.getCutoff_date(),"yyyy-MM","yyyy-MM-dd"));   //截至年月 ??
            creditLoanInfoJiemo.setReleaseCount(housing.getLoan_amount());   //贷款合同金额 ??
            creditLoanInfoJiemo.setBalance(housing.getRemain_balance());   //贷款余额
            creditLoanInfoJiemo.setOverDueAmount(housing.getOverdue_amount());   //逾期金额
            creditLoanInfoJiemo.setDelqL5yAmt(housing.getDelqL5yAmt());   //最近五年逾期次数 ??
            creditLoanInfoJiemo.setDelqL5y90dayAmt(housing.getDelqL5y90dayAmt());   //最近五年90天以上逾期次数(数据类型为整型，无字典)
            creditLoanInfoJiemo.setSettleDate(toDateFormat(housing.getSettleDate(),"yyyy-MM","yyyy-MM-dd"));//结清年月
            creditLoanInfoJiemoList.add(creditLoanInfoJiemo);
        }
        for (OtherLoanRecordDetail other : otherLoanRecordDetails) {
            CreditLoanInfo creditLoanInfoJiemo = new CreditLoanInfo();
            creditLoanInfoJiemo.setQueryID("");   //
            String status = other.getStatus();
            creditLoanInfoJiemo.setStatus(getLoanStatus(status));   //账户状态
            creditLoanInfoJiemo.setLoanType(getLoanType("其他贷款"));   //贷款种类
            creditLoanInfoJiemo.setFinanceCorporationBankName(other.getFinance_corporation());   //发放贷款机构名称，发放贷款银行的名称
            creditLoanInfoJiemo.setIsDelq(parserFlag(other.getIs_overdue()));   //是否发生过逾期
            creditLoanInfoJiemo.setIsCloseout(parserFlag(other.getIs_closeout()));   //贷款是否结清，购房贷款是否已结清
            creditLoanInfoJiemo.setTransfer("false");   //是否已转出 (住房贷款中的字段)
            creditLoanInfoJiemo.setBadDebts(other.getBad_debts());   //是否已变成呆账
            creditLoanInfoJiemo.setReleasedate(toDateFormat(other.getGrant_date(), "yyyy-MM-dd", "yyyy-MM-dd"));   //发放日期
            creditLoanInfoJiemo.setDeadline(toDateFormat(other.getExpiration_date(),"yyyy-MM-dd","yyyy-MM-dd"));   //到期日期
            creditLoanInfoJiemo.setDueDate(toDateFormat(other.getCutoff_date(),"yyyy-MM","yyyy-MM-dd"));   //截至年月
            creditLoanInfoJiemo.setReleaseCount(other.getLoan_amount());   //贷款合同金额
            creditLoanInfoJiemo.setBalance(other.getRemain_balance());   //贷款余额
            creditLoanInfoJiemo.setOverDueAmount(other.getOverdue_amount());   //逾期金额
            creditLoanInfoJiemo.setDelqL5yAmt(other.getDelqL5yAmt());   //最近五年逾期次数
            creditLoanInfoJiemo.setDelqL5y90dayAmt(other.getDelqL5y90dayAmt());   //最近五年90天以上逾期次数(数据类型为整型，无字典)
            creditLoanInfoJiemo.setSettleDate(toDateFormat(other.getSettleDate(),"yyyy-MM","yyyy-MM-dd"));//结清年月
            creditLoanInfoJiemoList.add(creditLoanInfoJiemo);
        }
        report.setCreditLoanInfo(creditLoanInfoJiemoList);

        //查询信息
        List<QueryInformationDetail> queryInformationDetails = pbcCreditReport.getQueryInformationDetails();
        List<CreditQueryRecord> queryRecordList = new ArrayList<>();
        for (QueryInformationDetail queryInformationDetail : queryInformationDetails) {
            CreditQueryRecord queryRecord = new CreditQueryRecord();
            queryRecord.setQueryId(""); //编号 ??
            queryRecord.setQueryTime(toDateFormat(queryInformationDetail.getQuery_dateTime(),"yyyy-MM-dd","yyyy-MM-dd"));
            String query_reason = queryInformationDetail.getQuery_reason();
            queryRecord.setQueryReason(getQueryReason(query_reason));
            queryRecordList.add(queryRecord);
        }
        report.setCreditQueryRecord(queryRecordList);

        return report;
//        return gson.toJson(report);
    }

    /**
     * 贷乎-征信报告json
     * @param pbcCreditReport
     * @return
     */
    public String getReportDaihuParser(PbcCreditReport pbcCreditReport){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        PbccrcCreditReportJiemo report = null;
        if (pbcCreditReport == null) {
            return null;
        }
        report = new PbccrcCreditReportJiemo();
        //基本信息
        return gson.toJson(report);
    }
}
