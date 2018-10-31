package com.microservice.dao.entity.crawler.insurance.huzhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;
/**
 * 湖州社保个人信息
 * @author zcx
 *
 */
@Entity
@Table(name="insurance_huzhou_userinfo",indexes = {@Index(name = "index_insurance_huzhou_userinfo_taskid", columnList = "taskid")})
public class InsuranceHuzhouUserInfo extends IdEntity {
	private String useraccount; // 人员编号
	private String username; // 姓名
	private String type; // 证件类型
	private String idnum; // 身份证
	private String sex; // 性别
	private String birthdate; // 出生日期
	private String companyaccount; // 单位编码
	private String companyname; // 单位名称
	private String firstdate; //开始参保时间
	private String state; //参保状态
	private String contactnum; // 联系电话
	private String telphone; // 手机号码
	private String planarea; // 统筹区
	private String address; // 地址
	private String taskid; // 任务ID
    
	public String getUseraccount() {
		return useraccount;
	}
	public void setUseraccount(String useraccount) {
		this.useraccount = useraccount;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public String getCompanyaccount() {
		return companyaccount;
	}
	public void setCompanyaccount(String companyaccount) {
		this.companyaccount = companyaccount;
	}

	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getFirstdate() {
		return firstdate;
	}
	public void setFirstdate(String firstdate) {
		this.firstdate = firstdate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getContactnum() {
		return contactnum;
	}
	public void setContactnum(String contactnum) {
		this.contactnum = contactnum;
	}
	public String getTelphone() {
		return telphone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}
	public String getPlanarea() {
		return planarea;
	}
	public void setPlanarea(String planarea) {
		this.planarea = planarea;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@Override
	public String toString() {
		return "InsuranceHuzhouUserInfo [useraccount=" + useraccount + ", username=" + username + ", type=" + type
				+ ", idnum=" + idnum + ", sex=" + sex + ", birthdate=" + birthdate + ", companyaccount="
				+ companyaccount + ", companyname=" + companyname + ", firstdate=" + firstdate + ", state=" + state
				+ ", contactnum=" + contactnum + ", telphone=" + telphone + ", planarea=" + planarea + ", address="
				+ address + ", taskid=" + taskid + "]";
	}
	public InsuranceHuzhouUserInfo() {
		super();
	}
	public InsuranceHuzhouUserInfo(String useraccount, String username, String type, String idnum, String sex,
			String birthdate, String companyaccount, String companyname, String firstdate, String state,
			String contactnum, String telphone, String planarea, String address, String taskid) {
		super();
		this.useraccount = useraccount;
		this.username = username;
		this.type = type;
		this.idnum = idnum;
		this.sex = sex;
		this.birthdate = birthdate;
		this.companyaccount = companyaccount;
		this.companyname = companyname;
		this.firstdate = firstdate;
		this.state = state;
		this.contactnum = contactnum;
		this.telphone = telphone;
		this.planarea = planarea;
		this.address = address;
		this.taskid = taskid;
	}
}
