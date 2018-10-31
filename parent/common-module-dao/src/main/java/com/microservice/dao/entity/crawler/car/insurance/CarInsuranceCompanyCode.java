package com.microservice.dao.entity.crawler.car.insurance;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 车险公司标准代码表
 * @author zz
 *
 */
@Entity
@Table(name="car_insurance_company_code")
public class CarInsuranceCompanyCode {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long companyId;
	private String companyName;
	private String companyEn;
	private String companyShortnameEn;
	private String companyShortname;
	private Integer isFlag;			//状态：0.维护中 1.可用
	
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyEn() {
		return companyEn;
	}
	public void setCompanyEn(String companyEn) {
		this.companyEn = companyEn;
	}
	public String getCompanyShortnameEn() {
		return companyShortnameEn;
	}
	public void setCompanyShortnameEn(String companyShortnameEn) {
		this.companyShortnameEn = companyShortnameEn;
	}
	public String getCompanyShortname() {
		return companyShortname;
	}
	public void setCompanyShortname(String companyShortname) {
		this.companyShortname = companyShortname;
	}
	public Integer getIsFlag() {
		return isFlag;
	}
	public void setIsFlag(Integer isFlag) {
		this.isFlag = isFlag;
	}

}
