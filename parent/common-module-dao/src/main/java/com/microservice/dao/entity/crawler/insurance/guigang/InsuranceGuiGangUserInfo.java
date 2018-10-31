package com.microservice.dao.entity.crawler.insurance.guigang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_guigang_userinfo",indexes = {@Index(name = "index_insurance_guigang_userinfo_taskid", columnList = "taskid")})
public class InsuranceGuiGangUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = -7225639204374657354L;
	private String taskid;
	private String name;
	private String dw_name;
	private String state;//状态
	private String idcard;
	private String tel;
	private String yanglaobalance;
	private String yibaobalance;
	private String endpaytime;
	private String gr_num;
	private String sex;
	private String nation;//民族
	private String birthday;//出生日期
	private String insuredtime;//首次参保时间
	private String householdtype;//户口性质
	private String registered;//户口所在地
	private String ry_state;//人员状态
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
	public String getDw_name() {
		return dw_name;
	}
	public void setDw_name(String dw_name) {
		this.dw_name = dw_name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getYanglaobalance() {
		return yanglaobalance;
	}
	public void setYanglaobalance(String yanglaobalance) {
		this.yanglaobalance = yanglaobalance;
	}
	public String getYibaobalance() {
		return yibaobalance;
	}
	public void setYibaobalance(String yibaobalance) {
		this.yibaobalance = yibaobalance;
	}
	public String getEndpaytime() {
		return endpaytime;
	}
	public void setEndpaytime(String endpaytime) {
		this.endpaytime = endpaytime;
	}
	public String getGr_num() {
		return gr_num;
	}
	public void setGr_num(String gr_num) {
		this.gr_num = gr_num;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getInsuredtime() {
		return insuredtime;
	}
	public void setInsuredtime(String insuredtime) {
		this.insuredtime = insuredtime;
	}
	public String getHouseholdtype() {
		return householdtype;
	}
	public void setHouseholdtype(String householdtype) {
		this.householdtype = householdtype;
	}
	public String getRegistered() {
		return registered;
	}
	public void setRegistered(String registered) {
		this.registered = registered;
	}
	public String getRy_state() {
		return ry_state;
	}
	public void setRy_state(String ry_state) {
		this.ry_state = ry_state;
	}
	@Override
	public String toString() {
		return "InsuranceGuiGangUserInfo [taskid=" + taskid + ", name=" + name + ", dw_name=" + dw_name + ", state="
				+ state + ", idcard=" + idcard + ", tel=" + tel + ", yanglaobalance=" + yanglaobalance
				+ ", yibaobalance=" + yibaobalance + ", endpaytime=" + endpaytime + ", gr_num=" + gr_num + ", sex="
				+ sex + ", nation=" + nation + ", birthday=" + birthday + ", insuredtime=" + insuredtime
				+ ", householdtype=" + householdtype + ", registered=" + registered + ", ry_state=" + ry_state + "]";
	}
	public InsuranceGuiGangUserInfo(String taskid, String name, String dw_name, String state, String idcard, String tel,
			String yanglaobalance, String yibaobalance, String endpaytime, String gr_num, String sex, String nation,
			String birthday, String insuredtime, String householdtype, String registered, String ry_state) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.dw_name = dw_name;
		this.state = state;
		this.idcard = idcard;
		this.tel = tel;
		this.yanglaobalance = yanglaobalance;
		this.yibaobalance = yibaobalance;
		this.endpaytime = endpaytime;
		this.gr_num = gr_num;
		this.sex = sex;
		this.nation = nation;
		this.birthday = birthday;
		this.insuredtime = insuredtime;
		this.householdtype = householdtype;
		this.registered = registered;
		this.ry_state = ry_state;
	}
	public InsuranceGuiGangUserInfo() {
		super();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	 
}
