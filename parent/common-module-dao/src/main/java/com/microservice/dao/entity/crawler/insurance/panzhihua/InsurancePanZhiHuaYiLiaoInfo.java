package com.microservice.dao.entity.crawler.insurance.panzhihua;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_panzhihua_yiliaoinfo")
public class InsurancePanZhiHuaYiLiaoInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	private String taskid;

	private String num;

	private String name;

	private String idcard;

	private String dwnum;

	private String dwname;
	
	private String fristpaytime;

	private String lastpaytime;
	
	private String paymonth;
	
	private String dwpaysign;
	
	private String cardinalnum;
	
	private String remote;
	
	private String type;
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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

	public String getDwnum() {
		return dwnum;
	}

	public void setDwnum(String dwnum) {
		this.dwnum = dwnum;
	}

	public String getDwname() {
		return dwname;
	}

	public void setDwname(String dwname) {
		this.dwname = dwname;
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

	public String getPaymonth() {
		return paymonth;
	}

	public void setPaymonth(String paymonth) {
		this.paymonth = paymonth;
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

	public String getRemote() {
		return remote;
	}

	public void setRemote(String remote) {
		this.remote = remote;
	}


	public InsurancePanZhiHuaYiLiaoInfo(String taskid, String num, String name, String idcard, String dwnum,
			String dwname, String fristpaytime, String lastpaytime, String paymonth, String dwpaysign,
			String cardinalnum, String remote, String type) {
		super();
		this.taskid = taskid;
		this.num = num;
		this.name = name;
		this.idcard = idcard;
		this.dwnum = dwnum;
		this.dwname = dwname;
		this.fristpaytime = fristpaytime;
		this.lastpaytime = lastpaytime;
		this.paymonth = paymonth;
		this.dwpaysign = dwpaysign;
		this.cardinalnum = cardinalnum;
		this.remote = remote;
		this.type = type;
	}

	public InsurancePanZhiHuaYiLiaoInfo() {
		super();
	}

	@Override
	public String toString() {
		return "InsurancePanZhiHuaYiLiaoInfo [taskid=" + taskid + ", num=" + num + ", name=" + name + ", idcard="
				+ idcard + ", dwnum=" + dwnum + ", dwname=" + dwname + ", fristpaytime=" + fristpaytime
				+ ", lastpaytime=" + lastpaytime + ", paymonth=" + paymonth + ", dwpaysign=" + dwpaysign
				+ ", cardinalnum=" + cardinalnum + ", remote=" + remote + "]";
	}
	
	
}
