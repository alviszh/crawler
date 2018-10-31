package com.microservice.dao.entity.crawler.insurance.sz.shanxi;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_sz_shanxi_userinfo",indexes = {@Index(name = "index_insurance_sz_shanxi_userinfo_taskid", columnList = "taskid")})
public class InsuranceSZShanXiUserInfo extends IdEntity{
	private String name;
	private String IDNum;
	private String birth;
	private String taskid;
	private String cardNum;
	@Override
	public String toString() {
		return "InsuranceSZShanXiUserInfo [name=" + name + ", IDNum=" + IDNum + ", birth=" + birth + ", taskid="
				+ taskid + ", cardNum=" + cardNum + "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIDNum() {
		return IDNum;
	}
	public void setIDNum(String iDNum) {
		IDNum = iDNum;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	
	
}
