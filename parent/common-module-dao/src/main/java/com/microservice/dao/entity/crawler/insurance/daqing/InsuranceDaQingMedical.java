package com.microservice.dao.entity.crawler.insurance.daqing;

import com.microservice.dao.entity.IdEntity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "insurance_daqing_medical",indexes = {@Index(name = "index_insurance_daqing_medical_taskid", columnList = "taskid")})
public class InsuranceDaQingMedical extends IdEntity implements Serializable{
	private static final long serialVersionUID = 3845870656982563673L;
	private String taskid;
//	个人编号
	private String pernum;
//	对应所属期
	private String belongdate;
//	月进账金额
	private String monthincome;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPernum() {
		return pernum;
	}
	public void setPernum(String pernum) {
		this.pernum = pernum;
	}
	public String getBelongdate() {
		return belongdate;
	}
	public void setBelongdate(String belongdate) {
		this.belongdate = belongdate;
	}
	public String getMonthincome() {
		return monthincome;
	}
	public void setMonthincome(String monthincome) {
		this.monthincome = monthincome;
	}
}
