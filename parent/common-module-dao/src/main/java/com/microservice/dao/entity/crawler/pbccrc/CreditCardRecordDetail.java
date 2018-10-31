package com.microservice.dao.entity.crawler.pbccrc;

import org.codehaus.jackson.annotate.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 信用卡记录详细信息解析记录
 * Created by zmy on 2017/12/26.
 */
@Entity
@Table(name="creditcard_record_detail")
//@JsonSerialize(using=MySerializer.class)
public class CreditCardRecordDetail extends AbstractEntity implements Serializable{
    private static final long serialVersionUID = 8075909127234222120L;

    private String mapping_id;  //uuid 唯一标识
    private String report_no;   //人行征信报告编号

    @JsonBackReference
    private CreditRecordDetail creditRecordDetail;

    private Long recordDetail_autoId;  //对应于creditRecordDetails中的auto_id

    private String grant_date;   //信用卡发放的时间
    private String bank_name;   //发放信用卡银行的名称
    private String card_type;   //信用卡类型
    private String account_currency;   //账户的币种
    private String cutoff_date;   //本征信报告获取本信用卡信息的最后时间
    private String credit_limit;   //信用额度
    private String used_credit_line;   //已使用额度
    private String overdraft_balance;   //透支余额
    private String overdue_amount;   //逾期金额
    private String is_actived;   //该账户是否激活过
    private String is_closed;   //该账户是否已销户
    private String is_overdue;   //该账户是否有逾期记录
    private String overdue_month;   //有过逾期记录的月数
    private String is_sixtydays_overdraft;   //准贷记卡账户是否有大于60天逾期记录
    private String sixtydays_overdraft_month;   //准记卡账户有超过60天逾期记录月数
    private String is_nintydays_overdue;   //该账户是否有超过90天
    private String nintydays_overdue_month;   //该账户有超过90天逾期记录月数
    private String bad_debts;   //是否已变成呆账
    private String remaining_sum;   //余额(人民币)

    private String status;      //账户状态
    private String delqL5yAmt;  //最近五年逾期次数(月数)
    private String delqL5y90dayAmt; //最近五年90天以上逾期次数（月数）
    private String cancellDate; //销户年月

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

    @OneToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="recordDetail_autoId")
    public CreditRecordDetail getCreditRecordDetail() {
        return creditRecordDetail;
    }

    public void setCreditRecordDetail(CreditRecordDetail creditRecordDetail) {
        this.creditRecordDetail = creditRecordDetail;
    }
    public String getGrant_date() {
        return grant_date;
    }

    public void setGrant_date(String grant_date) {
        this.grant_date = grant_date;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getAccount_currency() {
        return account_currency;
    }

    public void setAccount_currency(String account_currency) {
        this.account_currency = account_currency;
    }

    public String getCutoff_date() {
        return cutoff_date;
    }

    public void setCutoff_date(String cutoff_date) {
        this.cutoff_date = cutoff_date;
    }

    public String getCredit_limit() {
        return credit_limit;
    }

    public void setCredit_limit(String credit_limit) {
        this.credit_limit = credit_limit;
    }

    public String getUsed_credit_line() {
        return used_credit_line;
    }

    public void setUsed_credit_line(String used_credit_line) {
        this.used_credit_line = used_credit_line;
    }

    public String getOverdraft_balance() {
        return overdraft_balance;
    }

    public void setOverdraft_balance(String overdraft_balance) {
        this.overdraft_balance = overdraft_balance;
    }

    public String getOverdue_amount() {
        return overdue_amount;
    }

    public void setOverdue_amount(String overdue_amount) {
        this.overdue_amount = overdue_amount;
    }

    public String getIs_actived() {
        return is_actived;
    }

    public void setIs_actived(String is_actived) {
        this.is_actived = is_actived;
    }

    public String getIs_closed() {
        return is_closed;
    }

    public void setIs_closed(String is_closed) {
        this.is_closed = is_closed;
    }

    public String getIs_overdue() {
        return is_overdue;
    }

    public void setIs_overdue(String is_overdue) {
        this.is_overdue = is_overdue;
    }

    public String getOverdue_month() {
        return overdue_month;
    }

    public void setOverdue_month(String overdue_month) {
        this.overdue_month = overdue_month;
    }

    public String getIs_sixtydays_overdraft() {
        return is_sixtydays_overdraft;
    }

    public void setIs_sixtydays_overdraft(String is_sixtydays_overdraft) {
        this.is_sixtydays_overdraft = is_sixtydays_overdraft;
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

    public String getSixtydays_overdraft_month() {
        return sixtydays_overdraft_month;
    }

    public void setSixtydays_overdraft_month(String sixtydays_overdraft_month) {
        this.sixtydays_overdraft_month = sixtydays_overdraft_month;
    }

    public String getIs_nintydays_overdue() {
        return is_nintydays_overdue;
    }

    public void setIs_nintydays_overdue(String is_nintydays_overdue) {
        this.is_nintydays_overdue = is_nintydays_overdue;
    }

    public String getNintydays_overdue_month() {
        return nintydays_overdue_month;
    }

    public void setNintydays_overdue_month(String nintydays_overdue_month) {
        this.nintydays_overdue_month = nintydays_overdue_month;
    }

    public String getBad_debts() {
        return bad_debts;
    }

    public void setBad_debts(String bad_debts) {
        this.bad_debts = bad_debts;
    }

    public String getRemaining_sum() {
        return remaining_sum;
    }

    public void setRemaining_sum(String remaining_sum) {
        this.remaining_sum = remaining_sum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelqL5yAmt() {
        return delqL5yAmt;
    }

    public void setDelqL5yAmt(String delqL5yAmt) {
        this.delqL5yAmt = delqL5yAmt;
    }

    public String getDelqL5y90dayAmt() {
        return delqL5y90dayAmt;
    }

    public void setDelqL5y90dayAmt(String delqL5y90dayAmt) {
        this.delqL5y90dayAmt = delqL5y90dayAmt;
    }

    public String getCancellDate() {
        return cancellDate;
    }

    public void setCancellDate(String cancellDate) {
        this.cancellDate = cancellDate;
    }

    @Override
    public String toString() {
        return "CreditCardRecordDetail{" +
                "mapping_id='" + mapping_id + '\'' +
                ", report_no='" + report_no + '\'' +
                ", creditRecordDetail=" + creditRecordDetail +
                ", recordDetail_autoId=" + recordDetail_autoId +
                ", grant_date='" + grant_date + '\'' +
                ", bank_name='" + bank_name + '\'' +
                ", card_type='" + card_type + '\'' +
                ", account_currency='" + account_currency + '\'' +
                ", cutoff_date='" + cutoff_date + '\'' +
                ", credit_limit='" + credit_limit + '\'' +
                ", used_credit_line='" + used_credit_line + '\'' +
                ", overdraft_balance='" + overdraft_balance + '\'' +
                ", overdue_amount='" + overdue_amount + '\'' +
                ", is_actived='" + is_actived + '\'' +
                ", is_closed='" + is_closed + '\'' +
                ", is_overdue='" + is_overdue + '\'' +
                ", overdue_month='" + overdue_month + '\'' +
                ", is_sixtydays_overdraft='" + is_sixtydays_overdraft + '\'' +
                ", sixtydays_overdraft_month='" + sixtydays_overdraft_month + '\'' +
                ", is_nintydays_overdue='" + is_nintydays_overdue + '\'' +
                ", nintydays_overdue_month='" + nintydays_overdue_month + '\'' +
                ", bad_debts='" + bad_debts + '\'' +
                ", remaining_sum='" + remaining_sum + '\'' +
                ", status='" + status + '\'' +
                ", delqL5yAmt='" + delqL5yAmt + '\'' +
                ", delqL5y90dayAmt='" + delqL5y90dayAmt + '\'' +
                ", cancellDate='" + cancellDate + '\'' +
                '}';
    }
}
