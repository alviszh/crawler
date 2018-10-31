package com.microservice.dao.entity.crawler.telecom.sichuan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_sichuan_userinfo",indexes = {@Index(name = "index_telecom_sichuan_userinfo_taskid", columnList = "taskid")})
public class TelecomSiChuanUserInfo extends IdEntity{

	private String username;//机主名称
	
	private String tel;//电话帐号
	
	private String productname;//产品名称
	
	private String address;//安装地址
	
	private String contractnumber;//合同号
	
	private String createdtime;//安装日期
	
	private String status;//状态
	
	private String email;//邮箱
	
	private String stars;//星级
	
	private String starsfen;//星级成长值
	
	private String accountexpense;//余额
	
	private String jifen;//积分
	
	private String taskid;
	
	

	
	public String getStars() {
		return stars;
	}

	public void setStars(String stars) {
		this.stars = stars;
	}

	public String getStarsfen() {
		return starsfen;
	}

	public void setStarsfen(String starsfen) {
		this.starsfen = starsfen;
	}

	public String getAccountexpense() {
		return accountexpense;
	}

	public void setAccountexpense(String accountexpense) {
		this.accountexpense = accountexpense;
	}

	public String getJifen() {
		return jifen;
	}

	public void setJifen(String jifen) {
		this.jifen = jifen;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContractnumber() {
		return contractnumber;
	}

	public void setContractnumber(String contractnumber) {
		this.contractnumber = contractnumber;
	}

	public String getCreatedtime() {
		return createdtime;
	}

	public void setCreatedtime(String createdtime) {
		this.createdtime = createdtime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "TelecomSiChuanUserInfo [username=" + username + ", tel=" + tel + ", productname=" + productname
				+ ", address=" + address + ", contractnumber=" + contractnumber + ", createdtime=" + createdtime
				+ ", status=" + status + ", email=" + email + ", stars=" + stars + ", starsfen=" + starsfen
				+ ", accountexpense=" + accountexpense + ", jifen=" + jifen + ", taskid=" + taskid + "]";
	}

	

}
