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
@Table(name="insurance_xiamen_details_info",indexes = {@Index(name = "index_insurance_xiamen_details_info_taskid", columnList = "taskId")})
public class InsuranceXiamenDetailsInfo extends IdEntity implements Serializable {
    private static final long serialVersionUID = -7225639204374657354L;

    /** 爬取批次号 */
    @Column(name="task_id")
    private String taskId;

    /**个人编号*/
    @Column(name = "personal_number")
    private String personalNumber;

    /**单位名称*/
    @Column(name = "company_name")
    private String companyName;

    /**帐目名称*/
    @Column(name = "account_name")
    private String accountName;

    /**实际划拨日期*/
    @Column(name = "actual_allotted_date")
    private String actualAllottedDate;

    /**业务标志*/
    @Column(name = "service_mark")
    private String serviceMark;

    /**保险险种*/
    @Column(name = "insurance_coverage")
    private String insuranceCoverage;

    /**起始帐目年月*/
    @Column(name = "start_account_date")
    private String startAccountDate;

    /**截止帐目年月*/
    @Column(name = "end_account_date")
    private String endAccountDate;

    /**建帐年月*/
    @Column(name = "create_account_date")
    private String createAccountDate;

    /**缴费月数*/
    @Column(name = "payment_month")
    private String paymentMonth;

    /**利息*/
    @Column(name = "interest")
    private String interest;

    /**滞纳金*/
    @Column(name = "late_fee")
    private String lateFee;

    /**单位缴费比例*/
    @Column(name = "company_contribution_ratio")
    private String companyContributionRatio;

    /**单位缴费金额*/
    @Column(name = "company_payment_amount")
    private String companyPaymentAmount;


    /**个人缴费比例*/
    @Column(name = "individual_payment_ratio")
    private String individualPaymentRatio;

    /**个人缴费金额*/
    @Column(name = "individual_payment_amount")
    private String individualPaymentAmount;


    /**单位缴费基数*/
    @Column(name = "company_account_base")
    private String companyAccountBase;


    /**个人缴费基数*/
    @Column(name = "individual_account_base")
    private String individualAccountBase;

    /**单位划入帐户*/
    @Column(name = "company_into_account")
    private String companyIntoAccount;

    /**个人划入帐户*/
    @Column(name = "individual_into_account")
    private String individualIntoAccount;

    /**缴费日期*/
    @Column(name = "payment_date")
    private String paymentDate;

    /**缴费总金额*/
    @Column(name = "total_amount_payment")
    private String totalAmountPayment;

    /**冲销标志*/
    @Column(name = "write_down_sign")
    private String writeDownSign;

    /**页码 - 第n条 与详细表关联*/
    @Column(name = "page_id")
    private String pageId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getActualAllottedDate() {
        return actualAllottedDate;
    }

    public void setActualAllottedDate(String actualAllottedDate) {
        this.actualAllottedDate = actualAllottedDate;
    }

    public String getServiceMark() {
        return serviceMark;
    }

    public void setServiceMark(String serviceMark) {
        this.serviceMark = serviceMark;
    }

    public String getInsuranceCoverage() {
        return insuranceCoverage;
    }

    public void setInsuranceCoverage(String insuranceCoverage) {
        this.insuranceCoverage = insuranceCoverage;
    }

    public String getStartAccountDate() {
        return startAccountDate;
    }

    public void setStartAccountDate(String startAccountDate) {
        this.startAccountDate = startAccountDate;
    }

    public String getEndAccountDate() {
        return endAccountDate;
    }

    public void setEndAccountDate(String endAccountDate) {
        this.endAccountDate = endAccountDate;
    }

    public String getCreateAccountDate() {
        return createAccountDate;
    }

    public void setCreateAccountDate(String createAccountDate) {
        this.createAccountDate = createAccountDate;
    }

    public String getPaymentMonth() {
        return paymentMonth;
    }

    public void setPaymentMonth(String paymentMonth) {
        this.paymentMonth = paymentMonth;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getLateFee() {
        return lateFee;
    }

    public void setLateFee(String lateFee) {
        this.lateFee = lateFee;
    }

    public String getCompanyContributionRatio() {
        return companyContributionRatio;
    }

    public void setCompanyContributionRatio(String companyContributionRatio) {
        this.companyContributionRatio = companyContributionRatio;
    }

    public String getCompanyPaymentAmount() {
        return companyPaymentAmount;
    }

    public void setCompanyPaymentAmount(String companyPaymentAmount) {
        this.companyPaymentAmount = companyPaymentAmount;
    }

    public String getIndividualPaymentRatio() {
        return individualPaymentRatio;
    }

    public void setIndividualPaymentRatio(String individualPaymentRatio) {
        this.individualPaymentRatio = individualPaymentRatio;
    }

    public String getCompanyAccountBase() {
        return companyAccountBase;
    }

    public void setCompanyAccountBase(String companyAccountBase) {
        this.companyAccountBase = companyAccountBase;
    }

    public String getIndividualAccountBase() {
        return individualAccountBase;
    }

    public void setIndividualAccountBase(String individualAccountBase) {
        this.individualAccountBase = individualAccountBase;
    }

    public String getCompanyIntoAccount() {
        return companyIntoAccount;
    }

    public void setCompanyIntoAccount(String companyIntoAccount) {
        this.companyIntoAccount = companyIntoAccount;
    }

    public String getIndividualIntoAccount() {
        return individualIntoAccount;
    }

    public void setIndividualIntoAccount(String individualIntoAccount) {
        this.individualIntoAccount = individualIntoAccount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getTotalAmountPayment() {
        return totalAmountPayment;
    }

    public void setTotalAmountPayment(String totalAmountPayment) {
        this.totalAmountPayment = totalAmountPayment;
    }

    public String getWriteDownSign() {
        return writeDownSign;
    }

    public void setWriteDownSign(String writeDownSign) {
        this.writeDownSign = writeDownSign;
    }

    public String getIndividualPaymentAmount() {
        return individualPaymentAmount;
    }

    public void setIndividualPaymentAmount(String individualPaymentAmount) {
        this.individualPaymentAmount = individualPaymentAmount;
    }
    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    @Override
    public String toString() {
        return "InsuranceXiamenDetailsInfo{" +
                "taskId='" + taskId + '\'' +
                ", personalNumber='" + personalNumber + '\'' +
                ", companyName='" + companyName + '\'' +
                ", accountName='" + accountName + '\'' +
                ", actualAllottedDate='" + actualAllottedDate + '\'' +
                ", serviceMark='" + serviceMark + '\'' +
                ", insuranceCoverage='" + insuranceCoverage + '\'' +
                ", startAccountDate='" + startAccountDate + '\'' +
                ", endAccountDate='" + endAccountDate + '\'' +
                ", createAccountDate='" + createAccountDate + '\'' +
                ", paymentMonth='" + paymentMonth + '\'' +
                ", interest='" + interest + '\'' +
                ", lateFee='" + lateFee + '\'' +
                ", companyContributionRatio='" + companyContributionRatio + '\'' +
                ", companyPaymentAmount='" + companyPaymentAmount + '\'' +
                ", individualPaymentRatio='" + individualPaymentRatio + '\'' +
                ", individualPaymentAmount='" + individualPaymentAmount + '\'' +
                ", companyAccountBase='" + companyAccountBase + '\'' +
                ", individualAccountBase='" + individualAccountBase + '\'' +
                ", companyIntoAccount='" + companyIntoAccount + '\'' +
                ", individualIntoAccount='" + individualIntoAccount + '\'' +
                ", paymentDate='" + paymentDate + '\'' +
                ", totalAmountPayment='" + totalAmountPayment + '\'' +
                ", writeDownSign='" + writeDownSign + '\'' +
                ", pageId='" + pageId + '\'' +
                '}';
    }
}
