package com.microservice.dao.entity.crawler.insurance.wuzhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_wuzhou_pension",indexes = {@Index(name = "index_insurance_wuzhou_pension_taskid", columnList = "taskid")})
public class InsuranceWuZhouPension extends IdEntity{
	private String type;                            //参保险种
	private String year;                            //费款期号
	private String base;                            //缴费工资
	private String unitContriRatio;                 //单位缴费比例
	private String unitAmount;                      //单位缴费
	private String personalContriRatio;				//个人缴费比例
	private String personalAmount;                  //个人缴费
	private String company;						    //参保单位
	private String sign;                            //缴费标志
	private String paymentType;				        //缴费类型
	private String payMonth;                        //到账日期
	
    private String taskid;
    
    @Override
	public String toString() {
		return "InsuranceWuZhouPension [unitContriRatio=" + unitContriRatio + ", personalContriRatio=" + personalContriRatio
				+ ", company=" + company+ ", type=" + type+ ", year=" + year
				+ ", payMonth=" + payMonth+ ", base=" + base+ ", unitAmount=" + unitAmount
				+ ", personalAmount=" + personalAmount+ ", paymentType=" + paymentType+ ", sign=" + sign
				+ ", taskid=" + taskid + "]";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getUnitContriRatio() {
		return unitContriRatio;
	}

	public void setUnitContriRatio(String unitContriRatio) {
		this.unitContriRatio = unitContriRatio;
	}

	public String getUnitAmount() {
		return unitAmount;
	}

	public void setUnitAmount(String unitAmount) {
		this.unitAmount = unitAmount;
	}

	public String getPersonalContriRatio() {
		return personalContriRatio;
	}

	public void setPersonalContriRatio(String personalContriRatio) {
		this.personalContriRatio = personalContriRatio;
	}

	public String getPersonalAmount() {
		return personalAmount;
	}

	public void setPersonalAmount(String personalAmount) {
		this.personalAmount = personalAmount;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPayMonth() {
		return payMonth;
	}

	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
}
