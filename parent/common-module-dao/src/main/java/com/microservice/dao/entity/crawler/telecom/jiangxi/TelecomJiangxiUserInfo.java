package com.microservice.dao.entity.crawler.telecom.jiangxi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description 用户信息实体
 * @author sln
 * @date 2017年9月16日
 */
@Entity
@Table(name = "telecom_jiangxi_userinfo",indexes = {@Index(name = "index_telecom_jiangxi_userinfo_taskid", columnList = "taskid")})
public class TelecomJiangxiUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 203649217105736034L;
	private String taskid;
//	客户名称
	private String cusname;
//	证件类型
	private String idtype;
//	证件号码
	private String idnum;
//	客户地址
	private String cusaddress;
//	联系人姓名
	private String contactname;
//	联系电话
	private String contactnum;
//	电子邮箱
	private String email;
//	邮政编码
	private String postalcode;
//	通信地址
	private String linkaddress;
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
	public String getIdtype() {
		return idtype;
	}
	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getCusaddress() {
		return cusaddress;
	}
	public void setCusaddress(String cusaddress) {
		this.cusaddress = cusaddress;
	}
	public String getContactname() {
		return contactname;
	}
	public void setContactname(String contactname) {
		this.contactname = contactname;
	}
	public String getContactnum() {
		return contactnum;
	}
	public void setContactnum(String contactnum) {
		this.contactnum = contactnum;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPostalcode() {
		return postalcode;
	}
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	public String getLinkaddress() {
		return linkaddress;
	}
	public void setLinkaddress(String linkaddress) {
		this.linkaddress = linkaddress;
	}
	public TelecomJiangxiUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TelecomJiangxiUserInfo(String taskid, String cusname, String idtype, String idnum, String cusaddress,
			String contactname, String contactnum, String email, String postalcode, String linkaddress) {
		super();
		this.taskid = taskid;
		this.cusname = cusname;
		this.idtype = idtype;
		this.idnum = idnum;
		this.cusaddress = cusaddress;
		this.contactname = contactname;
		this.contactnum = contactnum;
		this.email = email;
		this.postalcode = postalcode;
		this.linkaddress = linkaddress;
	}
}

