package com.microservice.dao.entity.crawler.insurance.leshan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_leshan_yiliao")
public class InsuranceLeShanYiLiao extends IdEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String taskid;
	
	private String dwname;
	
	private String paydate;
	
	private String cardinality;
	
	private String dwpay;
	
	private String grpay;
	
	private String state;
	
	private String ginseng_to;
	
	private String hrgzje;
	
	private String type;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDwname() {
		return dwname;
	}

	public void setDwname(String dwname) {
		this.dwname = dwname;
	}

	public String getPaydate() {
		return paydate;
	}

	public void setPaydate(String paydate) {
		this.paydate = paydate;
	}

	public String getCardinality() {
		return cardinality;
	}

	public void setCardinality(String cardinality) {
		this.cardinality = cardinality;
	}

	public String getDwpay() {
		return dwpay;
	}

	public void setDwpay(String dwpay) {
		this.dwpay = dwpay;
	}

	public String getGrpay() {
		return grpay;
	}

	public void setGrpay(String grpay) {
		this.grpay = grpay;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getGinseng_to() {
		return ginseng_to;
	}

	public void setGinseng_to(String ginseng_to) {
		this.ginseng_to = ginseng_to;
	}

	public String getHrgzje() {
		return hrgzje;
	}

	public void setHrgzje(String hrgzje) {
		this.hrgzje = hrgzje;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public InsuranceLeShanYiLiao(String taskid, String dwname, String paydate, String cardinality, String dwpay,
			String grpay, String state, String ginseng_to, String hrgzje, String type) {
		super();
		this.taskid = taskid;
		this.dwname = dwname;
		this.paydate = paydate;
		this.cardinality = cardinality;
		this.dwpay = dwpay;
		this.grpay = grpay;
		this.state = state;
		this.ginseng_to = ginseng_to;
		this.hrgzje = hrgzje;
		this.type = type;
	}

	public InsuranceLeShanYiLiao() {
		super();
	}

	@Override
	public String toString() {
		return "InsuranceLeShanYanglao [taskid=" + taskid + ", dwname=" + dwname + ", paydate=" + paydate
				+ ", cardinality=" + cardinality + ", dwpay=" + dwpay + ", grpay=" + grpay + ", state=" + state
				+ ", ginseng_to=" + ginseng_to + ", hrgzje=" + hrgzje + ", type=" + type + "]";
	}
	
	
}
