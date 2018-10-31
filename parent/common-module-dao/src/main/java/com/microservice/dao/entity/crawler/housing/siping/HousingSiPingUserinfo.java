package com.microservice.dao.entity.crawler.housing.siping;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "housing_siping_userinfo",indexes = {@Index(name = "index_housing_siping_userinfo_taskid", columnList = "taskid")})
public class HousingSiPingUserinfo extends IdEntity implements Serializable{

	private String taskid;
	
	private String dw_num;
	
	private String dw_name;
	
	private String zg_num;
	
	private String zg_name;
	
	private String zjtype;
	
	private String zjnum;
	
	private String phone;
	
	private String address;//地址
	
	private String bank_num;
	
	private String bank_name;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBank_num() {
		return bank_num;
	}

	public void setBank_num(String bank_num) {
		this.bank_num = bank_num;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public HousingSiPingUserinfo(String taskid, String dw_num, String dw_name, String zg_num, String zg_name,
			String zjtype, String zjnum, String phone, String address, String bank_num, String bank_name) {
		super();
		this.taskid = taskid;
		this.dw_num = dw_num;
		this.dw_name = dw_name;
		this.zg_num = zg_num;
		this.zg_name = zg_name;
		this.zjtype = zjtype;
		this.zjnum = zjnum;
		this.phone = phone;
		this.address = address;
		this.bank_num = bank_num;
		this.bank_name = bank_name;
	}

	public HousingSiPingUserinfo() {
		super();
	}

	@Override
	public String toString() {
		return "HousingSiPingUserinfo [taskid=" + taskid + ", dw_num=" + dw_num + ", dw_name=" + dw_name + ", zg_num="
				+ zg_num + ", zg_name=" + zg_name + ", zjtype=" + zjtype + ", zjnum=" + zjnum + ", phone=" + phone
				+ ", address=" + address + ", bank_num=" + bank_num + ", bank_name=" + bank_name + "]";
	}
	
	
}
