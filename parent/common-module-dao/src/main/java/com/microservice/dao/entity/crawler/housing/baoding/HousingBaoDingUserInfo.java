package com.microservice.dao.entity.crawler.housing.baoding;

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
@Table(name = "housing_baoding_userinfo",indexes = {@Index(name = "index_housing_baoding_userinfo_taskid", columnList = "taskid")})
public class HousingBaoDingUserInfo extends IdEntity implements Serializable {

	private String taskid;
	
	private String dwnum;//单位账号
	
	private String dename;//单位名称
	
	private String num;//职工账号
	
	private String name;//职工姓名
	
	private String ztai;//账户状态
	
	private String idcard;//证件号码
	
	private String kdate;//开户日期

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDwnum() {
		return dwnum;
	}

	public void setDwnum(String dwnum) {
		this.dwnum = dwnum;
	}

	public String getDename() {
		return dename;
	}

	public void setDename(String dename) {
		this.dename = dename;
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

	public String getZtai() {
		return ztai;
	}

	public void setZtai(String ztai) {
		this.ztai = ztai;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getKdate() {
		return kdate;
	}

	public void setKdate(String kdate) {
		this.kdate = kdate;
	}

	public HousingBaoDingUserInfo(String taskid, String dwnum, String dename, String num, String name, String ztai,
			String idcard, String kdate) {
		super();
		this.taskid = taskid;
		this.dwnum = dwnum;
		this.dename = dename;
		this.num = num;
		this.name = name;
		this.ztai = ztai;
		this.idcard = idcard;
		this.kdate = kdate;
	}

	public HousingBaoDingUserInfo() {
		super();
	}
	
	
}
