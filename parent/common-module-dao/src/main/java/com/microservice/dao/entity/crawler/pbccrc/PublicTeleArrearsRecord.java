package com.microservice.dao.entity.crawler.pbccrc;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

//公共记录 -- 电信欠费信息
@Entity
@Table(name="public_teleArrears_record")
public class PublicTeleArrearsRecord extends AbstractEntity implements Serializable{

	private static final long serialVersionUID = -6491046896163931871L;
	private String mapping_id;  //uuid 唯一标识
	private String report_no;   //人行征信报告编号
	private String type;  //1.欠税记录 2.民事判决记录 3.强制执行记录 4.行政处罚记录 5.电信欠费记录

	private String operator;//运营商
	
	private String businessType;//业务类型
	
	private String accountingDate;//记账年月
	
	private String businessOpeningDate;//业务开通时间
	
	private String arrearsAmount;//欠费金额

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getAccountingDate() {
		return accountingDate;
	}

	public void setAccountingDate(String accountingDate) {
		this.accountingDate = accountingDate;
	}

	public String getBusinessOpeningDate() {
		return businessOpeningDate;
	}

	public void setBusinessOpeningDate(String businessOpeningDate) {
		this.businessOpeningDate = businessOpeningDate;
	}

	public String getArrearsAmount() {
		return arrearsAmount;
	}

	public void setArrearsAmount(String arrearsAmount) {
		this.arrearsAmount = arrearsAmount;
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

	@Override
	public String toString() {
		return "PublicTeleArrearsRecord{" +
				"mapping_id='" + mapping_id + '\'' +
				", report_no='" + report_no + '\'' +
				", type='" + type + '\'' +
				", operator='" + operator + '\'' +
				", businessType='" + businessType + '\'' +
				", accountingDate='" + accountingDate + '\'' +
				", businessOpeningDate='" + businessOpeningDate + '\'' +
				", arrearsAmount='" + arrearsAmount + '\'' +
				'}';
	}
}
