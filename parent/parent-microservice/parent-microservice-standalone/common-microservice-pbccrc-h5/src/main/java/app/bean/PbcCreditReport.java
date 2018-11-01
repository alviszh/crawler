package app.bean;

import com.microservice.dao.entity.crawler.pbccrc.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zmy on 2017/12/26.
 */
public class PbcCreditReport implements Serializable{
    private static final long serialVersionUID = -3351340695378238181L;

    private CreditBaseInfo creditBaseInfo;  //基本信息
    private List<CreditRecordSummary> creditRecordSummaries; //信息概要
    private List<CreditRecordDetail> creditRecordDetails; //信贷记录详细信息
    private List<PublicInformationDetail> publicInformationDetails; //公共记录
    private List<QueryInformationDetail> queryInformationDetails; //查询记录
    private List<CreditCardRecordDetail> creditCardRecordDetailAnalyzes; //信用卡记录详细信息解析记录
    private List<HousingLoanRecordDetail> housingLoanRecordDetailAnalyzes; //购房贷款记录详细信息解析记录
    private List<OtherLoanRecordDetail> otherLoanRecordDetailAnalyzes; //其他贷款记录详细信息解析记录
    private List<GuaranteeInfoDetail> guaranteeInfoDetailAnalyzes;  //为他人担保信息解析记录
    private List<GuarantorCompensatoryDetail> guarantorCompensatoryDetailAnalyzes; //保证人代偿信息解析记录
   // private PublicRecord publicInformationDetails; //公共记录 这部分包含您最近5年内的欠税记录、民事判决记录、强制执行记录、行政处罚记录及电信欠费记录。 金额类数据均以人民币计算， 精确到元。

    public CreditBaseInfo getCreditBaseInfo() {
        return creditBaseInfo;
    }

    public void setCreditBaseInfo(CreditBaseInfo creditBaseInfo) {
        this.creditBaseInfo = creditBaseInfo;
    }

    public List<CreditRecordSummary> getCreditRecordSummaries() {
        return creditRecordSummaries;
    }

    public void setCreditRecordSummaries(List<CreditRecordSummary> creditRecordSummaries) {
        this.creditRecordSummaries = creditRecordSummaries;
    }

    public List<CreditRecordDetail> getCreditRecordDetails() {
        return creditRecordDetails;
    }

    public void setCreditRecordDetails(List<CreditRecordDetail> creditRecordDetails) {
        this.creditRecordDetails = creditRecordDetails;
    }

    public List<PublicInformationDetail> getPublicInformationDetails() {
        return publicInformationDetails;
    }

    public void setPublicInformationDetails(List<PublicInformationDetail> publicInformationDetails) {
        this.publicInformationDetails = publicInformationDetails;
    }

    public List<QueryInformationDetail> getQueryInformationDetails() {
        return queryInformationDetails;
    }

    public void setQueryInformationDetails(List<QueryInformationDetail> queryInformationDetails) {
        this.queryInformationDetails = queryInformationDetails;
    }

    public List<CreditCardRecordDetail> getCreditCardRecordDetailAnalyzes() {
        return creditCardRecordDetailAnalyzes;
    }

    public void setCreditCardRecordDetailAnalyzes(List<CreditCardRecordDetail> creditCardRecordDetailAnalyzes) {
        this.creditCardRecordDetailAnalyzes = creditCardRecordDetailAnalyzes;
    }

    public List<HousingLoanRecordDetail> getHousingLoanRecordDetailAnalyzes() {
        return housingLoanRecordDetailAnalyzes;
    }

    public void setHousingLoanRecordDetailAnalyzes(List<HousingLoanRecordDetail> housingLoanRecordDetailAnalyzes) {
        this.housingLoanRecordDetailAnalyzes = housingLoanRecordDetailAnalyzes;
    }

    public List<OtherLoanRecordDetail> getOtherLoanRecordDetailAnalyzes() {
        return otherLoanRecordDetailAnalyzes;
    }

    public void setOtherLoanRecordDetailAnalyzes(List<OtherLoanRecordDetail> otherLoanRecordDetailAnalyzes) {
        this.otherLoanRecordDetailAnalyzes = otherLoanRecordDetailAnalyzes;
    }

    public List<GuaranteeInfoDetail> getGuaranteeInfoDetailAnalyzes() {
        return guaranteeInfoDetailAnalyzes;
    }

    public void setGuaranteeInfoDetailAnalyzes(List<GuaranteeInfoDetail> guaranteeInfoDetailAnalyzes) {
        this.guaranteeInfoDetailAnalyzes = guaranteeInfoDetailAnalyzes;
    }

    public List<GuarantorCompensatoryDetail> getGuarantorCompensatoryDetailAnalyzes() {
        return guarantorCompensatoryDetailAnalyzes;
    }

    public void setGuarantorCompensatoryDetailAnalyzes(List<GuarantorCompensatoryDetail> guarantorCompensatoryDetailAnalyzes) {
        this.guarantorCompensatoryDetailAnalyzes = guarantorCompensatoryDetailAnalyzes;
    }
}
