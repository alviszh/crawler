package com.microservice.dao.entity.crawler.telecom.guangdong;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_guangdong_userinfo",indexes = {@Index(name = "index_telecom_guangdong_userinfo_taskid", columnList = "taskid")})
public class TelecomGuangDongUserInfo extends IdEntity{

	private String username;//客户姓名
	
	private String indentNbrType;//证件类型
	
	private String indentCode;//证件号码
	
	private String sex;//性别
	
	private String userAddress;//地址
	
	private String email;//邮箱
	
	private String moblie;//手机
	
	private String stars;//星级
	
	private String starsfen;//星级成长值
	
	private String accountexpense;//余额
	
	private String jifen;//积分
	
	private String post_code;//邮政编码
	
	private String post_addr;//通信地址
	
	private String rela_man;//联系人
	
	private String dhNumber;//固话
	
	private String qqNumber;//QQ
	
	private String education;//教育程度
	
	private String cal;//工作行业
	
	private String zell;//职业类别
	
	private String moneyy;//月收入
	
	private String px;//报销方式
	   
	
	private Integer userid;
	
	private String taskid;
	
	
	
	

	@Override
	public String toString() {
		return "TelecomGuangDongUserInfo [username=" + username + ", indentNbrType=" + indentNbrType + ", indentCode="
				+ indentCode + ", sex=" + sex + ", userAddress=" + userAddress + ", email=" + email + ", moblie="
				+ moblie + ", stars=" + stars + ", starsfen=" + starsfen + ", accountexpense=" + accountexpense
				+ ", jifen=" + jifen + ", post_code=" + post_code + ", post_addr=" + post_addr + ", rela_man="
				+ rela_man + ", dhNumber=" + dhNumber + ", qqNumber=" + qqNumber + ", education=" + education + ", cal="
				+ cal + ", zell=" + zell + ", moneyy=" + moneyy + ", px=" + px + ", userid=" + userid + ", taskid="
				+ taskid + "]";
	}

	public String getPost_code() {
		return post_code;
	}

	public void setPost_code(String post_code) {
		this.post_code = post_code;
	}

	public String getPost_addr() {
		return post_addr;
	}

	public void setPost_addr(String post_addr) {
		this.post_addr = post_addr;
	}

	public String getRela_man() {
		return rela_man;
	}

	public void setRela_man(String rela_man) {
		this.rela_man = rela_man;
	}

	public String getDhNumber() {
		return dhNumber;
	}

	public void setDhNumber(String dhNumber) {
		this.dhNumber = dhNumber;
	}

	public String getQqNumber() {
		return qqNumber;
	}

	public void setQqNumber(String qqNumber) {
		this.qqNumber = qqNumber;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getCal() {
		return cal;
	}

	public void setCal(String cal) {
		this.cal = cal;
	}

	public String getZell() {
		return zell;
	}

	public void setZell(String zell) {
		this.zell = zell;
	}

	public String getMoneyy() {
		return moneyy;
	}

	public void setMoneyy(String moneyy) {
		this.moneyy = moneyy;
	}

	public String getPx() {
		return px;
	}

	public void setPx(String px) {
		this.px = px;
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMoblie() {
		return moblie;
	}

	public void setMoblie(String moblie) {
		this.moblie = moblie;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIndentNbrType() {
		return indentNbrType;
	}

	public void setIndentNbrType(String indentNbrType) {
		this.indentNbrType = indentNbrType;
	}

	public String getIndentCode() {
		return indentCode;
	}

	public void setIndentCode(String indentCode) {
		this.indentCode = indentCode;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	
	
}
