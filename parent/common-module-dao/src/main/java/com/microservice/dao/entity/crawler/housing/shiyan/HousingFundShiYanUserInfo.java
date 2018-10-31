package com.microservice.dao.entity.crawler.housing.shiyan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_shiyan_userinfo")
public class HousingFundShiYanUserInfo extends IdEntity implements Serializable{

	private String name;//姓名
	
	private String accout;//公积金账号
	
	private String balance;//余额（元）
	
	private String status;//账户状态
	
	private String taskid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccout() {
		return accout;
	}

	public void setAccout(String accout) {
		this.accout = accout;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "HousingFundShiYanUserInfo [name=" + name + ", accout=" + accout + ", balance=" + balance + ", status="
				+ status + ", taskid=" + taskid + "]";
	}

}
