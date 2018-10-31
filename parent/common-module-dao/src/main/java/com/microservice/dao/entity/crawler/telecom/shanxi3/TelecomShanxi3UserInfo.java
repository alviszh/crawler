package com.microservice.dao.entity.crawler.telecom.shanxi3;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description 用户信息实体
 * @author sln
 * @date 2017年8月23日 下午12:36:41
 */
@Entity
@Table(name = "telecom_shanxi3_userinfo",indexes = {@Index(name = "index_telecom_shanxi3_userinfo_taskid", columnList = "taskid")})
public class TelecomShanxi3UserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = -6312622549509050396L;
	private String taskid;
//	客户名称
	private String cusname;
//	所在地市
	private String city;
//	邮政编码
	private String postalcode;
//	email
	private String email;
//	联系电话
	private String contactnum;
//	地址
	private String detailaddress;
//	纳税登记号（国）
	private String taxnum;
//	是否一般纳税
	private String isgeneraltax;
//	单位地址
	private String compaddress;
//	单位联系电话
	private String compcontactnum;
//	账号
	private String taxaccountnum;
//	开户银行
	private String openbank;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCusname() {
		return cusname;
	}
	public void setCusname(String cusname) {
		this.cusname = cusname;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostalcode() {
		return postalcode;
	}
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContactnum() {
		return contactnum;
	}
	public void setContactnum(String contactnum) {
		this.contactnum = contactnum;
	}
	public String getDetailaddress() {
		return detailaddress;
	}
	public void setDetailaddress(String detailaddress) {
		this.detailaddress = detailaddress;
	}
	public String getTaxnum() {
		return taxnum;
	}
	public void setTaxnum(String taxnum) {
		this.taxnum = taxnum;
	}
	public String getIsgeneraltax() {
		return isgeneraltax;
	}
	public void setIsgeneraltax(String isgeneraltax) {
		this.isgeneraltax = isgeneraltax;
	}
	public String getCompaddress() {
		return compaddress;
	}
	public void setCompaddress(String compaddress) {
		this.compaddress = compaddress;
	}
	public String getCompcontactnum() {
		return compcontactnum;
	}
	public void setCompcontactnum(String compcontactnum) {
		this.compcontactnum = compcontactnum;
	}
	public String getTaxaccountnum() {
		return taxaccountnum;
	}
	public void setTaxaccountnum(String taxaccountnum) {
		this.taxaccountnum = taxaccountnum;
	}
	public String getOpenbank() {
		return openbank;
	}
	public void setOpenbank(String openbank) {
		this.openbank = openbank;
	}
	public TelecomShanxi3UserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TelecomShanxi3UserInfo(String taskid, String cusname, String city, String postalcode, String email,
			String contactnum, String detailaddress, String taxnum, String isgeneraltax, String compaddress,
			String compcontactnum, String taxaccountnum, String openbank) {
		super();
		this.taskid = taskid;
		this.cusname = cusname;
		this.city = city;
		this.postalcode = postalcode;
		this.email = email;
		this.contactnum = contactnum;
		this.detailaddress = detailaddress;
		this.taxnum = taxnum;
		this.isgeneraltax = isgeneraltax;
		this.compaddress = compaddress;
		this.compcontactnum = compcontactnum;
		this.taxaccountnum = taxaccountnum;
		this.openbank = openbank;
	}
	
}

