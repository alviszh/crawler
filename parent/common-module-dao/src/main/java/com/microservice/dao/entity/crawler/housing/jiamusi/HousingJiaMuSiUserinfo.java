package com.microservice.dao.entity.crawler.housing.jiamusi;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_jiamusi_userinfo")
public class HousingJiaMuSiUserinfo  extends IdEntity implements Serializable {
		
	private String taskid;
	private String companyNum;						//单位账号
	private String companyName;						//单位名称
	private String staffNum;						//职工账号
	private String staffName;						//职工姓名
	private String state;							//账户状态
	private String cardNum;							//证件号码
	private String openDate;						//开户日期
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCompanyNum() {
		return companyNum;
	}
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
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
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	@Override
	public String toString() {
		return "HousingJiaMuSiUserinfo [taskid=" + taskid + ", companyNum=" + companyNum + ", companyName="
				+ companyName + ", staffNum=" + staffNum + ", staffName=" + staffName + ", state=" + state
				+ ", cardNum=" + cardNum + ", openDate=" + openDate + "]";
	}
	
	
	

}
