package com.microservice.dao.entity.crawler.insurance.sanming;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

/**
 * @author zz
 * @create 2018-3-14
 * @Desc 社保-三明市 养老信息表
 */
@Entity
@Table(name = "insurance_sanming_pension")
public class InsuranceSanmingPension extends IdEntity {
	
    private String taskid;
    private String accountMonthly;					//到账年月
    private String declarationDate;					//申报日期
    private String companyCode;						//单位编号
    private String payMonth;						//缴费月数
    private String beginDate;						//费款所属起始日期
    private String endDate;							//费款所属截止日期
    private String companyPayCardinal;				//单位缴费基数
    private String companyPayScale;					//单位缴费比例
    private String ordinatingMoney;					//统筹金额
    private String personPayCardinal;				//个人缴费基数
    private String personPayScale;					//个人缴费比例
    private String personAccountMoney;				//个人账户金额
    private String Ashare;							//个帐比例
    private String companyPartMoney;				//单位划拨部分金额
    private String totalMoney;						//实缴总金额
    
    
    public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getAccountMonthly() {
		return accountMonthly;
	}
	@Override
	public String toString() {
		return "InsuranceSanmingPension [taskid=" + taskid + ", accountMonthly=" + accountMonthly + ", declarationDate="
				+ declarationDate + ", companyCode=" + companyCode + ", payMonth=" + payMonth + ", beginDate="
				+ beginDate + ", endDate=" + endDate + ", companyPayCardinal=" + companyPayCardinal
				+ ", companyPayScale=" + companyPayScale + ", ordinatingMoney=" + ordinatingMoney
				+ ", personPayCardinal=" + personPayCardinal + ", personPayScale=" + personPayScale
				+ ", personAccountMoney=" + personAccountMoney + ", Ashare=" + Ashare + ", companyPartMoney="
				+ companyPartMoney + ", totalMoney=" + totalMoney + "]";
	}
	public void setAccountMonthly(String accountMonthly) {
		this.accountMonthly = accountMonthly;
	}
	public String getDeclarationDate() {
		return declarationDate;
	}
	public void setDeclarationDate(String declarationDate) {
		this.declarationDate = declarationDate;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getPayMonth() {
		return payMonth;
	}
	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCompanyPayCardinal() {
		return companyPayCardinal;
	}
	public void setCompanyPayCardinal(String companyPayCardinal) {
		this.companyPayCardinal = companyPayCardinal;
	}
	public String getCompanyPayScale() {
		return companyPayScale;
	}
	public void setCompanyPayScale(String companyPayScale) {
		this.companyPayScale = companyPayScale;
	}
	public String getOrdinatingMoney() {
		return ordinatingMoney;
	}
	public void setOrdinatingMoney(String ordinatingMoney) {
		this.ordinatingMoney = ordinatingMoney;
	}
	public String getPersonPayCardinal() {
		return personPayCardinal;
	}
	public void setPersonPayCardinal(String personPayCardinal) {
		this.personPayCardinal = personPayCardinal;
	}
	public String getPersonPayScale() {
		return personPayScale;
	}
	public void setPersonPayScale(String personPayScale) {
		this.personPayScale = personPayScale;
	}
	public String getPersonAccountMoney() {
		return personAccountMoney;
	}
	public void setPersonAccountMoney(String personAccountMoney) {
		this.personAccountMoney = personAccountMoney;
	}
	public String getAshare() {
		return Ashare;
	}
	public void setAshare(String ashare) {
		Ashare = ashare;
	}
	public String getCompanyPartMoney() {
		return companyPartMoney;
	}
	public void setCompanyPartMoney(String companyPartMoney) {
		this.companyPartMoney = companyPartMoney;
	}
	public String getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

}
