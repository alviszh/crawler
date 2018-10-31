package com.microservice.dao.entity.crawler.housing.huaian;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_huaian_userinfo")
public class HousingHuaiAnUserinfo  extends IdEntity implements Serializable {
		
	private String taskid;
	private String status;						//账户状态
	private String companyName;					//单位名称
	private String companyNum;					//单位账号
	private String userName;					//姓名
	private String idNum;						//身份证号码
	private String personalNum;					//个人账号
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyNum() {
		return companyNum;
	}
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getPersonalNum() {
		return personalNum;
	}
	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}
	@Override
	public String toString() {
		return "HousingHuaiAnUserinfo [taskid=" + taskid + ", status=" + status + ", companyName=" + companyName
				+ ", companyNum=" + companyNum + ", userName=" + userName + ", idNum=" + idNum + ", personalNum="
				+ personalNum + "]";
	}
}
