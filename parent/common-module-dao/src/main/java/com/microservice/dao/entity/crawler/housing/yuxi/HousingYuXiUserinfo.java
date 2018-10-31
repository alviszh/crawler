package com.microservice.dao.entity.crawler.housing.yuxi;

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
@Table(name = "housing_yuxi_userinfo",indexes = {@Index(name = "index_housing_yuxi_userinfo_taskid", columnList = "taskid")})
public class HousingYuXiUserinfo  extends IdEntity implements Serializable {
		
	private String taskid;
	private String name;							//姓名
	private String companyName;						//单位名称
	private String personMonthPay;					//个人月缴存额
	private String balance;							//公积金余额
	private String num;                             //帐号
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
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPersonMonthPay() {
		return personMonthPay;
	}
	public void setPersonMonthPay(String personMonthPay) {
		this.personMonthPay = personMonthPay;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public HousingYuXiUserinfo(String taskid, String name, String companyName, String personMonthPay, String balance,
			String num) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.companyName = companyName;
		this.personMonthPay = personMonthPay;
		this.balance = balance;
		this.num = num;
	}
	public HousingYuXiUserinfo() {
		super();
	}
	@Override
	public String toString() {
		return "HousingYuXiUserinfo [taskid=" + taskid + ", name=" + name + ", companyName=" + companyName
				+ ", personMonthPay=" + personMonthPay + ", balance=" + balance + ", num=" + num + "]";
	}

}
