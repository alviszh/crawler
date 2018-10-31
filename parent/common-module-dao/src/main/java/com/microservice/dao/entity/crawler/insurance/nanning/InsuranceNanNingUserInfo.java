package com.microservice.dao.entity.crawler.insurance.nanning;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_nanning_userinfo",indexes = {@Index(name = "index_insurance_nanning_userinfo_taskid", columnList = "taskid")}) 
public class InsuranceNanNingUserInfo extends IdEntity{

	private String IDCard;//身份证
	
	private String num;//社保卡号
	
	private String name;
	
	
	
	private String taskid;



	@Override
	public String toString() {
		return "InsuranceNanNingUserInfo [IDCard=" + IDCard + ", num=" + num + ", name=" + name + ", taskid=" + taskid
				+ "]";
	}



	public String getIDCard() {
		return IDCard;
	}



	public void setIDCard(String iDCard) {
		IDCard = iDCard;
	}



	public String getNum() {
		return num;
	}



	public void setNum(String num) {
		this.num = num;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getTaskid() {
		return taskid;
	}



	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	
}
