package com.microservice.dao.entity.crawler.housing.xiaogan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_xiaogan_userinfo",indexes = {@Index(name = "index_housing_xiaogan_userinfo_taskid", columnList = "taskid")})
public class HousingXiaoGanUserinfo extends IdEntity implements Serializable{
	private String taskid;
	private String staffName;						//姓名
	private String idNum;							//身 份 证
	private String companyName;						//单位名称
	private String staffNum;						//个人公积金帐号
	private String balance;						    //公积金余额
	private String date;					        //缴至年月
	
	@Override
	public String toString() {
		return "HousingXiaoGanUserinfo [taskid=" + taskid + ", staffName=" + staffName + ", idNum=" + idNum
				+ ", companyName=" + companyName + ", staffNum=" + staffNum + ", balance=" + balance
				+ ", date=" + date + "]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getStaffNum() {
		return staffNum;
	}

	public void setStaffNum(String staffNum) {
		this.staffNum = staffNum;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	

}
