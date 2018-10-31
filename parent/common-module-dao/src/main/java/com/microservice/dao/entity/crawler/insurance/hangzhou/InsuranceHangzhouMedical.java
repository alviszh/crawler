package com.microservice.dao.entity.crawler.insurance.hangzhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Des 杭州社保-医疗缴费表
 * @author zh
 *
 */
@Entity
@Table(name="insurance_hangzhou_medical",indexes = {@Index(name = "index_insurance_hangzhou_medical_taskid", columnList = "taskid")})
public class InsuranceHangzhouMedical extends IdEntity {
	private String taskid;
	private String payMonth;					//年月
	private String insurance;					//险种类型
	private String base;				        //缴费基数
	private String payPerson;					//个人缴
	private String unitNumber;				    //单位编号
	private String companyName;                 //单位名称
	private String type;			            //缴费类型
	
	@Override
	public String toString() {
		return "InsurancehangzhouPension [taskid=" + taskid +", payMonth=" + payMonth 
				+ ", insurance=" +insurance + ", base=" + base + ", unitNumber=" + unitNumber
				+ ", companyName=" +companyName + ", type=" + type + ", payPerson=" + payPerson+"]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPayMonth() {
		return payMonth;
	}

	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getPayPerson() {
		return payPerson;
	}

	public void setPayPerson(String payPerson) {
		this.payPerson = payPerson;
	}

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
