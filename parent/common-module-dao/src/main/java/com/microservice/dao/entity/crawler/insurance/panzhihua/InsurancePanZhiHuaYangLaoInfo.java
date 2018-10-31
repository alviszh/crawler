package com.microservice.dao.entity.crawler.insurance.panzhihua;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_panzhihua_yanglaoinfo")
public class InsurancePanZhiHuaYangLaoInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	private String taskid;
	
	private String num;
	
	private String name;
	
	private String idcard;
	
	private String dwnum;
	
	private String dwname;
	
	private String cbdate;
	
	private String creattime;
	
	private String fristpaytime;
	
	private String lastpaytime;
	
	private String dwpaysign;
	
	private String cardinalnum;
	
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

	public String getCbdate() {
		return cbdate;
	}

	public void setCbdate(String cbdate) {
		this.cbdate = cbdate;
	}

	public String getCreattime() {
		return creattime;
	}

	public void setCreattime(String creattime) {
		this.creattime = creattime;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public InsurancePanZhiHuaYangLaoInfo(String taskid, String num, String name, String idcard, String dwnum,
			String dwname, String cbdate, String creattime, String fristpaytime, String lastpaytime, String dwpaysign,
			String cardinalnum, String type) {
		super();
		this.taskid = taskid;
		this.num = num;
		this.name = name;
		this.idcard = idcard;
		this.dwnum = dwnum;
		this.dwname = dwname;
		this.cbdate = cbdate;
		this.creattime = creattime;
		this.fristpaytime = fristpaytime;
		this.lastpaytime = lastpaytime;
		this.dwpaysign = dwpaysign;
		this.cardinalnum = cardinalnum;
		this.type = type;
	}

	public InsurancePanZhiHuaYangLaoInfo() {
		super();
	}

	@Override
	public String toString() {
		return "InsurancePanZhiHuaYangLaoInfo [taskid=" + taskid + ", num=" + num + ", name=" + name + ", idcard="
				+ idcard + ", dwnum=" + dwnum + ", dwname=" + dwname + ", cbdate=" + cbdate + ", creattime=" + creattime
				+ ", fristpaytime=" + fristpaytime + ", lastpaytime=" + lastpaytime + ", dwpaysign=" + dwpaysign
				+ ", cardinalnum=" + cardinalnum + ", type=" + type + "]";
	}
	
	
	
}
