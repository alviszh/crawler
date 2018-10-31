package com.microservice.dao.entity.crawler.telecom.tianjin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description 用户信息实体
 * @author sln
 * @date 2017年9月11日
 */
@Entity
@Table(name = "telecom_tianjin_userinfo",indexes = {@Index(name = "index_telecom_tianjin_userinfo_taskid", columnList = "taskid")})
public class TelecomTianjinUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 5051959924273465329L;
	private String taskid;
//	机主姓名
	private String cusname;
//	邮政编码
	private String postalcode;
//	证件号码
	private String idnum;
//	入网时间
	private String accesstime;
//	联系电话
	private String contactnum;
//	通信地址
	private String linkaddress;
//	电子邮箱
	private String email;
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
	public String getPostalcode() {
		return postalcode;
	}
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getAccesstime() {
		return accesstime;
	}
	public void setAccesstime(String accesstime) {
		this.accesstime = accesstime;
	}
	public String getContactnum() {
		return contactnum;
	}
	public void setContactnum(String contactnum) {
		this.contactnum = contactnum;
	}
	public String getLinkaddress() {
		return linkaddress;
	}
	public void setLinkaddress(String linkaddress) {
		this.linkaddress = linkaddress;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public TelecomTianjinUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TelecomTianjinUserInfo(String taskid, String cusname, String postalcode, String idnum, String accesstime,
			String contactnum, String linkaddress, String email) {
		super();
		this.taskid = taskid;
		this.cusname = cusname;
		this.postalcode = postalcode;
		this.idnum = idnum;
		this.accesstime = accesstime;
		this.contactnum = contactnum;
		this.linkaddress = linkaddress;
		this.email = email;
	}
	
}

