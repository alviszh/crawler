package com.microservice.dao.entity.crawler.insurance.qingdao;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * @Description: 青岛用户信息实体
 * @author sln
 * @date 2017年8月9日
 */
@Entity
@Table(name = "insurance_qingdao_userinfo",indexes = {@Index(name = "index_insurance_qingdao_userinfo_taskid", columnList = "taskid")})
public class InsuranceQingdaoUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5463733733681486246L;
	private String taskid;
//	职工编号
	private String worknum;
//	姓名
	private String name;
//	身份证号
	private String idnum;
//	性别
	private String gender;
//	参加工作日期
	private String startworkdate;
//	出生日期
	private String birthday;
//	人员状态
	private String personstatus;
//	民族
	private String nation;
//	联系电话
	private String phonenum;
//	通讯地址
	private String contactaddr;
//	邮政编码
	private String postalode;
	//单位编号
	private String compnum;
	//单位名称
	private String compname;
	//行政区划
	private String comparea;
//	户口性质
	private String householdtype;
//	制卡状态
	private String cardstatus;
//	发卡银行
	private String bank;
//	银行地址
	private String bankaddr;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getWorknum() {
		return worknum;
	}
	public void setWorknum(String worknum) {
		this.worknum = worknum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getStartworkdate() {
		return startworkdate;
	}
	public void setStartworkdate(String startworkdate) {
		this.startworkdate = startworkdate;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getPersonstatus() {
		return personstatus;
	}
	public void setPersonstatus(String personstatus) {
		this.personstatus = personstatus;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	public String getContactaddr() {
		return contactaddr;
	}
	public void setContactaddr(String contactaddr) {
		this.contactaddr = contactaddr;
	}
	public String getPostalode() {
		return postalode;
	}
	public void setPostalode(String postalode) {
		this.postalode = postalode;
	}
	public String getCompnum() {
		return compnum;
	}
	public void setCompnum(String compnum) {
		this.compnum = compnum;
	}
	public String getCompname() {
		return compname;
	}
	public void setCompname(String compname) {
		this.compname = compname;
	}
	public String getComparea() {
		return comparea;
	}
	public void setComparea(String comparea) {
		this.comparea = comparea;
	}
	public String getHouseholdtype() {
		return householdtype;
	}
	public void setHouseholdtype(String householdtype) {
		this.householdtype = householdtype;
	}
	public String getCardstatus() {
		return cardstatus;
	}
	public void setCardstatus(String cardstatus) {
		this.cardstatus = cardstatus;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getBankaddr() {
		return bankaddr;
	}
	public void setBankaddr(String bankaddr) {
		this.bankaddr = bankaddr;
	}
	public InsuranceQingdaoUserInfo() {
		super();
	}
	public InsuranceQingdaoUserInfo(String taskid, String worknum, String name, String idnum, String gender,
			String startworkdate, String birthday, String personstatus, String nation, String phonenum,
			String contactaddr, String postalode, String compnum, String compname, String comparea,
			String householdtype, String cardstatus, String bank, String bankaddr) {
		super();
		this.taskid = taskid;
		this.worknum = worknum;
		this.name = name;
		this.idnum = idnum;
		this.gender = gender;
		this.startworkdate = startworkdate;
		this.birthday = birthday;
		this.personstatus = personstatus;
		this.nation = nation;
		this.phonenum = phonenum;
		this.contactaddr = contactaddr;
		this.postalode = postalode;
		this.compnum = compnum;
		this.compname = compname;
		this.comparea = comparea;
		this.householdtype = householdtype;
		this.cardstatus = cardstatus;
		this.bank = bank;
		this.bankaddr = bankaddr;
	}
}
