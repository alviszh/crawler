package com.microservice.dao.entity.crawler.pbccrc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.microservice.dao.entity.IdEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 信贷记录详细信息
 * Created by zmy on 2017/12/26.
 */
@Entity
@Table(name="credit_record_detail")
public class CreditRecordDetail implements Serializable{
    private static final long serialVersionUID = -3967815147801228527L;

    private Long auto_id;    //这个记录的唯一ID
    private String mapping_id;  //uuid 唯一标识
    private String report_no;    //人行征信报告编号
    private String credit_type;    //信贷类型（1.信用卡 2.住房贷款 3.其它贷款 4.为他人担保 5.保证人代偿）
    private String account_type;    //账户类型
    private String content;         //账户明细

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createtime = new Date();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getAuto_id() {
        return auto_id;
    }

    public void setAuto_id(Long auto_id) {
        this.auto_id = auto_id;
    }

    public String getCredit_type() {
        return credit_type;
    }

    public void setCredit_type(String credit_type) {
        this.credit_type = credit_type;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CreditRecordDetail{" +
                "auto_id=" + auto_id +
                ", mapping_id='" + mapping_id + '\'' +
                ", report_no='" + report_no + '\'' +
                ", credit_type=" + credit_type +
                ", account_type=" + account_type +
                ", content='" + content + '\'' +
                '}';
    }
}
