package com.microservice.dao.entity.crawler.housing.wuxi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 无锡公积金用户信息
 */
@Entity
@Table(name="housing_wuxi_paydetails")
public class HousingWuxiPaydetails extends IdEntity implements Serializable {
	private static final long serialVersionUID = -758773159050632619L;
	private String companyName;//	单位名称
	private String recheckDate;//	业务确认日期
	private String serviceMonth;//	业务对应年月
	private String remark;//	摘要
	private String income;//	收入
	private String outcome;//	支出
	private String currentBalance;//	余额
	private String taskid;
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getRecheckDate() {
		return recheckDate;
	}
	public void setRecheckDate(String recheckDate) {
		this.recheckDate = recheckDate;
	}
	public String getServiceMonth() {
		return serviceMonth;
	}
	public void setServiceMonth(String serviceMonth) {
		this.serviceMonth = serviceMonth;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
	}
	public String getOutcome() {
		return outcome;
	}
	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}
	public String getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@Override
	public String toString() {
		return "HousingWuxiPaydetails [companyName=" + companyName + ", recheckDate=" + recheckDate + ", serviceMonth="
				+ serviceMonth + ", remark=" + remark + ", income=" + income + ", outcome=" + outcome
				+ ", currentBalance=" + currentBalance + ", taskid=" + taskid + "]";
	}
	
	public HousingWuxiPaydetails(String companyName, String recheckDate, String serviceMonth, String remark,
			String income, String outcome, String currentBalance, String taskid) {
		super();
		this.companyName = companyName;
		this.recheckDate = recheckDate;
		this.serviceMonth = serviceMonth;
		this.remark = remark;
		this.income = income;
		this.outcome = outcome;
		this.currentBalance = currentBalance;
		this.taskid = taskid;
	}
	public HousingWuxiPaydetails() {
		super();
		// TODO Auto-generated constructor stub
	}	
}
