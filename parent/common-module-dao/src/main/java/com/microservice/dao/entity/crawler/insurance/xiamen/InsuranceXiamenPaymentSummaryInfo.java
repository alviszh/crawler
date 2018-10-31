package com.microservice.dao.entity.crawler.insurance.xiamen;

/**
 * Created by kaixu on 2017/9/26.
 */

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 深圳社保 缴费汇总信息
 * @author rongshengxu
 *
 */
@Entity
@Table(name="insurance_xiamen_payment_summary_info",indexes = {@Index(name = "index_insurance_xiamen_payment_summary_info_taskid", columnList = "taskId")})
public class InsuranceXiamenPaymentSummaryInfo extends IdEntity implements Serializable {
    private static final long serialVersionUID = -7225639204374657354L;

    /** 爬取批次号 */
    @Column(name="task_id")
    private String taskId;

    /**保险险种*/
    @Column(name = "insurance_type")
    private String insuranceType;

    /**账目类型*/
    @Column(name = "account_type")
    private String accountType;

    /**业务标志*/
    @Column(name = "service_mark")
    private String serviceMark;

    /**起始年月*/
    @Column(name = "start_date")
    private String startDate;

    /**截至年月*/
    @Column(name = "end_date")
    private String endDate;

    /**缴费总额*/
    @Column(name = "total_payment")
    private String totalPayment;

    /**划拨金额*/
    @Column(name = "allotted_amount")
    private String allottedAmount;

    /**缴费基数*/
    @Column(name = "base_amount")
    private String baseAmount;

    /**是否缴费*/
    @Column(name = "is_payment")
    private String isPayment;

    /**页码 - 第n条 与详细表关联*/
    @Column(name = "page_id")
    private String pageId;

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getServiceMark() {
        return serviceMark;
    }

    public void setServiceMark(String serviceMark) {
        this.serviceMark = serviceMark;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getAllottedAmount() {
        return allottedAmount;
    }

    public void setAllottedAmount(String allottedAmount) {
        this.allottedAmount = allottedAmount;
    }

    public String getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(String baseAmount) {
        this.baseAmount = baseAmount;
    }

    public String getIsPayment() {
        return isPayment;
    }

    public void setIsPayment(String isPayment) {
        this.isPayment = isPayment;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }
}
