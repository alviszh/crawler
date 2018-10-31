package com.microservice.dao.entity.crawler.insurance.jiyuan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_jiyuan_userinfo",indexes = {@Index(name = "index_insurance_jiyuan_userinfo_taskid", columnList = "taskid")})
public class InsuranceJiYuanUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = -7225639204374657354L;
	private String taskid;
	
	private String birthday;//出生日期
	
	private String nation;//民族
	
	private String insuredtime;//首次参保时间
	
	private String dw_num;
	
	private String dw_name;
	
	private String gr_num;
	
	private String idcard;
	
	private String name;
	
	private String sex;
	
	private String state;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getInsuredtime() {
		return insuredtime;
	}

	public void setInsuredtime(String insuredtime) {
		this.insuredtime = insuredtime;
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

	public String getGr_num() {
		return gr_num;
	}

	public void setGr_num(String gr_num) {
		this.gr_num = gr_num;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public InsuranceJiYuanUserInfo(String taskid, String birthday, String nation, String insuredtime, String dw_num,
			String dw_name, String gr_num, String idcard, String name, String sex, String state) {
		super();
		this.taskid = taskid;
		this.birthday = birthday;
		this.nation = nation;
		this.insuredtime = insuredtime;
		this.dw_num = dw_num;
		this.dw_name = dw_name;
		this.gr_num = gr_num;
		this.idcard = idcard;
		this.name = name;
		this.sex = sex;
		this.state = state;
	}

	public InsuranceJiYuanUserInfo() {
		super();
	}

	@Override
	public String toString() {
		return "InsuranceJiYuanUserInfo [taskid=" + taskid + ", birthday=" + birthday + ", nation=" + nation
				+ ", insuredtime=" + insuredtime + ", dw_num=" + dw_num + ", dw_name=" + dw_name + ", gr_num=" + gr_num
				+ ", idcard=" + idcard + ", name=" + name + ", sex=" + sex + ", state=" + state + "]";
	}
	
	
	 
}
