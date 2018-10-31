package com.microservice.dao.entity.crawler.housing.jingzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name = "housing_jingzhou_userinfo",indexes = {@Index(name = "index_housing_jingzhou_userinfo_taskid", columnList = "taskid")})
public class HousingJingZhouUserinfo extends IdEntity implements Serializable {

	private static final long serialVersionUID = -6616664279708476893L;
	private String taskid;

	private String dw_num;

	private String dw_name;

	private String zg_num;

	private String zg_name;

	private String zjtype;

	private String zjnum;

	private String phone;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDw_num() {
		return dw_num;
	}

	public void setDw_num(String dw_num) {
		this.dw_num = dw_num;
	}

	public String getDw_name() {
		return dw_name;
	}

	public void setDw_name(String dw_name) {
		this.dw_name = dw_name;
	}

	public String getZg_num() {
		return zg_num;
	}

	public void setZg_num(String zg_num) {
		this.zg_num = zg_num;
	}

	public String getZg_name() {
		return zg_name;
	}

	public void setZg_name(String zg_name) {
		this.zg_name = zg_name;
	}

	public String getZjtype() {
		return zjtype;
	}

	public void setZjtype(String zjtype) {
		this.zjtype = zjtype;
	}

	public String getZjnum() {
		return zjnum;
	}

	public void setZjnum(String zjnum) {
		this.zjnum = zjnum;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public HousingJingZhouUserinfo(String taskid, String dw_num, String dw_name, String zg_num, String zg_name,
			String zjtype, String zjnum, String phone) {
		super();
		this.taskid = taskid;
		this.dw_num = dw_num;
		this.dw_name = dw_name;
		this.zg_num = zg_num;
		this.zg_name = zg_name;
		this.zjtype = zjtype;
		this.zjnum = zjnum;
		this.phone = phone;
	}

	public HousingJingZhouUserinfo() {
		super();
	}

	@Override
	public String toString() {
		return "HousingJingZhouUserinfo [taskid=" + taskid + ", dw_num=" + dw_num + ", dw_name=" + dw_name + ", zg_num="
				+ zg_num + ", zg_name=" + zg_name + ", zjtype=" + zjtype + ", zjnum=" + zjnum + ", phone=" + phone
				+ "]";
	}
	
	
}
