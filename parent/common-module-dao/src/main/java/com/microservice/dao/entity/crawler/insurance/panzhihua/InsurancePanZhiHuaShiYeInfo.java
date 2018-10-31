package com.microservice.dao.entity.crawler.insurance.panzhihua;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_panzhihua_shiyeinfo")
public class InsurancePanZhiHuaShiYeInfo extends IdEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	private String taskid;

	private String num;

	private String name;

	private String idcard;
	
	private String nmgidentifying;
	
	private String fristpaytime;

	private String lastpaytime;
	
	private String dwpaysign;
	
	private String cardinalnum;
	
	private String enjoymonths;
	
	private String surplusmonths;
	
	private String type;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
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

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getNmgidentifying() {
		return nmgidentifying;
	}

	public void setNmgidentifying(String nmgidentifying) {
		this.nmgidentifying = nmgidentifying;
	}

	public String getFristpaytime() {
		return fristpaytime;
	}

	public void setFristpaytime(String fristpaytime) {
		this.fristpaytime = fristpaytime;
	}

	public String getLastpaytime() {
		return lastpaytime;
	}

	public void setLastpaytime(String lastpaytime) {
		this.lastpaytime = lastpaytime;
	}

	public String getDwpaysign() {
		return dwpaysign;
	}

	public void setDwpaysign(String dwpaysign) {
		this.dwpaysign = dwpaysign;
	}

	public String getCardinalnum() {
		return cardinalnum;
	}

	public void setCardinalnum(String cardinalnum) {
		this.cardinalnum = cardinalnum;
	}

	public String getEnjoymonths() {
		return enjoymonths;
	}

	public void setEnjoymonths(String enjoymonths) {
		this.enjoymonths = enjoymonths;
	}

	public String getSurplusmonths() {
		return surplusmonths;
	}

	public void setSurplusmonths(String surplusmonths) {
		this.surplusmonths = surplusmonths;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public InsurancePanZhiHuaShiYeInfo(String taskid, String num, String name, String idcard, String nmgidentifying,
			String fristpaytime, String lastpaytime, String dwpaysign, String cardinalnum, String enjoymonths,
			String surplusmonths, String type) {
		super();
		this.taskid = taskid;
		this.num = num;
		this.name = name;
		this.idcard = idcard;
		this.nmgidentifying = nmgidentifying;
		this.fristpaytime = fristpaytime;
		this.lastpaytime = lastpaytime;
		this.dwpaysign = dwpaysign;
		this.cardinalnum = cardinalnum;
		this.enjoymonths = enjoymonths;
		this.surplusmonths = surplusmonths;
		this.type = type;
	}

	public InsurancePanZhiHuaShiYeInfo() {
		super();
	}

	@Override
	public String toString() {
		return "InsurancePanZhiHuaShiYeInfo [taskid=" + taskid + ", num=" + num + ", name=" + name + ", idcard="
				+ idcard + ", nmgidentifying=" + nmgidentifying + ", fristpaytime=" + fristpaytime + ", lastpaytime="
				+ lastpaytime + ", dwpaysign=" + dwpaysign + ", cardinalnum=" + cardinalnum + ", enjoymonths="
				+ enjoymonths + ", surplusmonths=" + surplusmonths + ", type=" + type + "]";
	}
	
	
}
