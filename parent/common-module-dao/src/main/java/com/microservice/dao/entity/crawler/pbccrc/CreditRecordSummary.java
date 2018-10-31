package com.microservice.dao.entity.crawler.pbccrc;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 人行征信报告信贷记录 - 信息概要
 * Created by zmy on 2017/12/26.
 */
@Entity
@Table(name="credit_record_summary")
public class CreditRecordSummary extends AbstractEntity implements Serializable{
    private static final long serialVersionUID = -8718620900396966439L;

    private String mapping_id;  //uuid 唯一标识
    private String report_no;   //人行征信报告编号
    private String credit_type; //账户类型（1.信用卡 2.住房贷款 3.其它贷款）
    private String account_num; //账户数
    private String unSettle_unCancel;   //未结清、未销户账户数
    private String overdue_account;     //发生过逾期的账户数
    private String overdue_ninety;      //发生过90天以上逾期的账户数
    private String guarantee;           //为他人担保笔数

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

    public String getCredit_type() {
        return credit_type;
    }

    public void setCredit_type(String credit_type) {
        this.credit_type = credit_type;
    }

    public String getAccount_num() {
        return account_num;
    }

    public void setAccount_num(String account_num) {
        this.account_num = account_num;
    }

    public String getUnSettle_unCancel() {
        return unSettle_unCancel;
    }

    public void setUnSettle_unCancel(String unSettle_unCancel) {
        this.unSettle_unCancel = unSettle_unCancel;
    }

    public String getOverdue_account() {
        return overdue_account;
    }

    public void setOverdue_account(String overdue_account) {
        this.overdue_account = overdue_account;
    }

    public String getOverdue_ninety() {
        return overdue_ninety;
    }

    public void setOverdue_ninety(String overdue_ninety) {
        this.overdue_ninety = overdue_ninety;
    }

    public String getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(String guarantee) {
        this.guarantee = guarantee;
    }

    @Override
    public String toString() {
        return "CreditRecordSummary{" +
                "mapping_id='" + mapping_id + '\'' +
                ", report_no='" + report_no + '\'' +
                ", credit_type='" + credit_type + '\'' +
                ", account_num='" + account_num + '\'' +
                ", unSettle_unCancel='" + unSettle_unCancel + '\'' +
                ", overdue_account='" + overdue_account + '\'' +
                ", overdue_ninety='" + overdue_ninety + '\'' +
                ", guarantee='" + guarantee + '\'' +
                '}';
    }
}
