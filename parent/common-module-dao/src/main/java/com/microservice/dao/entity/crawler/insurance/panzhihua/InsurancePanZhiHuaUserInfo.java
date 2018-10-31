package com.microservice.dao.entity.crawler.insurance.panzhihua;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_panzhihua_userinfo")
public class InsurancePanZhiHuaUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	private String taskid;
	
	private String name;
	
	private String num;
	
	private String dwnum;
	
	private String dwname;
	
	private String idcard;
	
	private String brithday;
	
	private String gzdate;
	
	private String tel;
	
	private String domicile_place;

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

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
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

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getBrithday() {
		return brithday;
	}

	public void setBrithday(String brithday) {
		this.brithday = brithday;
	}

	public String getGzdate() {
		return gzdate;
	}

	public void setGzdate(String gzdate) {
		this.gzdate = gzdate;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getDomicile_place() {
		return domicile_place;
	}

	public void setDomicile_place(String domicile_place) {
		this.domicile_place = domicile_place;
	}

	public InsurancePanZhiHuaUserInfo(String taskid, String name, String num, String dwnum, String dwname,
			String idcard, String brithday, String gzdate, String tel, String domicile_place) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.num = num;
		this.dwnum = dwnum;
		this.dwname = dwname;
		this.idcard = idcard;
		this.brithday = brithday;
		this.gzdate = gzdate;
		this.tel = tel;
		this.domicile_place = domicile_place;
	}

	public InsurancePanZhiHuaUserInfo() {
		super();
	}

	@Override
	public String toString() {
		return "InsurancePanZhiHuaUserInfo [taskid=" + taskid + ", name=" + name + ", num=" + num + ", dwnum=" + dwnum
				+ ", dwname=" + dwname + ", idcard=" + idcard + ", brithday=" + brithday + ", gzdate=" + gzdate
				+ ", tel=" + tel + ", domicile_place=" + domicile_place + "]";
	}
	
	
}
