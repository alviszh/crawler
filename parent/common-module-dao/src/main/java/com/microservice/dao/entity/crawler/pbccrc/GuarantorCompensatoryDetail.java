package com.microservice.dao.entity.crawler.pbccrc;

import com.microservice.dao.entity.IdEntity;
import org.codehaus.jackson.annotate.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 保证人代偿信息
 * Created by zmy on 2018/1/3.
 */
@Entity
@Table(name="guarantor_compensatory_detail")
public class GuarantorCompensatoryDetail extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 644089502363078638L;

    private String mapping_id;  //uuid 唯一标识
    private String report_no;   //人行征信报告编号
    private Long recordDetail_autoId;  //对应于creditRecordDetails中的auto_id

    @JsonBackReference
    private CreditRecordDetail creditRecordDetail;

    private String recentCompensatoryTime;  //最近一次代偿时间
    private String compensatoryAgency; //代偿机构
    private String accumulativeCompensatoryAmount;  //累计代偿金
    private String balance; //余额
    private String recentRepaymentDate; //最近一次还款日期

    public String getMapping_id() {
        return mapping_id;
    }

    public void setMapping_id(String mapping_id) {
        this.mapping_id = mapping_id;
    }

    public String getReport_no() {
        return report_no;
    }

    public void setReport_no(String report_no) {
        this.report_no = report_no;
    }

    public String getRecentCompensatoryTime() {
        return recentCompensatoryTime;
    }

    public void setRecentCompensatoryTime(String recentCompensatoryTime) {
        this.recentCompensatoryTime = recentCompensatoryTime;
    }

    public String getCompensatoryAgency() {
        return compensatoryAgency;
    }

    public void setCompensatoryAgency(String compensatoryAgency) {
        this.compensatoryAgency = compensatoryAgency;
    }

    public String getAccumulativeCompensatoryAmount() {
        return accumulativeCompensatoryAmount;
    }

    public void setAccumulativeCompensatoryAmount(String accumulativeCompensatoryAmount) {
        this.accumulativeCompensatoryAmount = accumulativeCompensatoryAmount;
    }

    @OneToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="recordDetail_autoId")
    public CreditRecordDetail getCreditRecordDetail() {
        return creditRecordDetail;
    }

    public void setCreditRecordDetail(CreditRecordDetail creditRecordDetail) {
        this.creditRecordDetail = creditRecordDetail;
    }

    @Transient
    public Long getRecordDetail_autoId() {
        if (creditRecordDetail != null) {
            return creditRecordDetail.getAuto_id();
        }
        return recordDetail_autoId;
    }
    public void setRecordDetail_autoId(Long recordDetail_autoId) {
        this.recordDetail_autoId = recordDetail_autoId;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRecentRepaymentDate() {
        return recentRepaymentDate;
    }

    public void setRecentRepaymentDate(String recentRepaymentDate) {
        this.recentRepaymentDate = recentRepaymentDate;
    }

    @Override
    public String toString() {
        return "GuarantorCompensatoryDetail{" +
                "mapping_id='" + mapping_id + '\'' +
                ", report_no='" + report_no + '\'' +
                ", recordDetail_autoId=" + recordDetail_autoId +
                ", creditRecordDetail=" + creditRecordDetail +
                ", recentCompensatoryTime='" + recentCompensatoryTime + '\'' +
                ", compensatoryAgency='" + compensatoryAgency + '\'' +
                ", accumulativeCompensatoryAmount='" + accumulativeCompensatoryAmount + '\'' +
                ", balance='" + balance + '\'' +
                ", recentRepaymentDate='" + recentRepaymentDate + '\'' +
                '}';
    }
}
