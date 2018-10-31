package com.microservice.dao.entity.crawler.housing.haikou;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_haikou_userinfo")
public class HousingHaiKouUserinfo  extends IdEntity implements Serializable {
		
	private String taskid;
	private String name;								//姓名
	private String idNum;								//证件号码
	private String idType;								//证件类型
	private String phoneNum;							//手机号码
	private String balance;								//个人账户余额（元）
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return "HousingHaiKouUserinfo [taskid=" + taskid + ", name=" + name + ", idNum=" + idNum + ", idType=" + idType
				+ ", phoneNum=" + phoneNum + ", balance=" + balance + "]";
	}
	
}
