package com.microservice.dao.entity.crawler.telecom.ningxia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 宁夏电信基本信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="telecom_ningxia_userinfo",indexes = {@Index(name = "index_telecom_ningxia_userinfo_taskid", columnList = "taskid")})
public class TelecomNingxiaUserInfo  extends IdEntity{
	@Column(name="userid")
	private Integer userid;
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
    ////////////////////////////////////我的资料组////////////////////////////////////
	/**证件类型*/  //存在   我的资料组
	@Column(name="papers_type")
	private String paperstype;
	
	/**身份证号*/   //存在  我的资料组
	@Column(name="card_id")
	private String cardid;
	
	/**注册该号码地址*/   //空的  我的资料组
	@Column(name="addr")
	private String addr;
	
	/**姓名*/   //存在  我的资料组
	@Column(name="name")
	private String name;
	
	/**运营商*/   //电信
	@Column(name="operator")
	private String operator ;
	
    //////////////////////////////////我的资料组/////////////////////////////////////
	/**归属省份*/  //空 
	@Column(name="province")
	private String province ;
	
	/**性别*/    //空    
	@Column(name="sex")
	private String sex ;
	
	
	///////////////////////////////////我的产品组/////////////////////////////////////
	/**归属城市*/   //存在    我的产品组
	@Column(name="city")
	private String city ;
	/**电话号码*/   //存在    我的产品组
	@Column(name="phone")
	private String phone ;

	/**邮箱*/      //存在    我的产品组
	@Column(name="email")
	private String email ;
	/**账户状态*/   //存在      我的产品组
	@Column(name="accountstatus")
	private String accountstatus;
	
	/**入网时间*/   //存在      我的产品组
	@Column(name="netintime")
	private String netintime;
	
	/**在网时长*/   //存在   我的产品组（当前时间-入网时间）
	@Column(name="netingtime")
	private String netingtime;
	
    //////////////////////////////////星级客服经理组//////////////////////////////////////
	
	/**账户星级*/  //存在       星级客服经理组
	@Column(name="star")
	private String star;
	
    ///////////////////////////////汇总积分查询组//////////////////////////////////////
	
	
	/**可用积分*/  //存在     汇总积分查询组
	@Column(name="usable_integral")
	private String usableintegral ;
	
    //////////////////////////////////// 话费及余额组//////////////////////////////////////
	
	/**当前账户余额*/  //存在     话费及余额组
	@Column(name="remaining")
	private String remaining ;
	
////////////////////////////////////不存在//////////////////////////////////////
	/**积分时间*/  //不存在     
	@Column(name="integral_time")
	private String integraltime ;
	
	/**实名制信息*/   //不存在
	@Column(name="realnamesystem")
	private String realnamesystem;
	
	
	/**星级有效期*/    //不存在
	@Column(name="starvalidity")
	private String starvalidity;
	
	/**账户冻结余额*/    //不存在
	@Column(name="freezemoney")
	private String freezemoney;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPaperstype() {
		return paperstype;
	}

	public void setPaperstype(String paperstype) {
		this.paperstype = paperstype;
	}

	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAccountstatus() {
		return accountstatus;
	}

	public void setAccountstatus(String accountstatus) {
		this.accountstatus = accountstatus;
	}

	public String getNetintime() {
		return netintime;
	}

	public void setNetintime(String netintime) {
		this.netintime = netintime;
	}

	public String getNetingtime() {
		return netingtime;
	}

	public void setNetingtime(String netingtime) {
		this.netingtime = netingtime;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public String getIntegraltime() {
		return integraltime;
	}

	public void setIntegraltime(String integraltime) {
		this.integraltime = integraltime;
	}

	public String getUsableintegral() {
		return usableintegral;
	}

	public void setUsableintegral(String usableintegral) {
		this.usableintegral = usableintegral;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRemaining() {
		return remaining;
	}

	public void setRemaining(String remaining) {
		this.remaining = remaining;
	}

	public String getRealnamesystem() {
		return realnamesystem;
	}

	public void setRealnamesystem(String realnamesystem) {
		this.realnamesystem = realnamesystem;
	}

	public String getStarvalidity() {
		return starvalidity;
	}

	public void setStarvalidity(String starvalidity) {
		this.starvalidity = starvalidity;
	}

	public String getFreezemoney() {
		return freezemoney;
	}

	public void setFreezemoney(String freezemoney) {
		this.freezemoney = freezemoney;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}


	
}
