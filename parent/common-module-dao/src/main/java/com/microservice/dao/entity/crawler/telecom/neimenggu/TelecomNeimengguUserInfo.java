package com.microservice.dao.entity.crawler.telecom.neimenggu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 呼和浩特电信基本信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="telecom_neimenggu_userinfo",indexes = {@Index(name = "index_telecom_neimenggu_userinfo_taskid", columnList = "taskid")})
public class TelecomNeimengguUserInfo  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**证件类型*/  
	@Column(name="papers_type")
	private String paperstype;
	
	/**身份证号*/   
	@Column(name="card_id")
	private String cardid;
	
	/**注册该号码地址*/   
	@Column(name="addr")
	private String addr;
	
	/**姓名*/   
	@Column(name="name")
	private String name;
	
	/**运营商*/   
	@Column(name="operator")
	private String operator ;
	
	
	/**电话号码*/   
	@Column(name="phone")
	private String phone ;

	
	/**账户状态*/   
	@Column(name="accountstatus")
	private String accountstatus;
	
	
	/**入网时间*/   
	@Column(name="nettime")
	private String nettime;
	
//
//	/**产品id*/   
//	@Column(name="prodid")
//	private String prodid;
	
	/**产品名称*/   
	@Column(name="proname")
	private String proname;
	
	
	/**消费积分*/ 
	@Column(name="consume_integral")
	private String consumeintegral ;
	
	/**倍增积分*/ 
	@Column(name="double_integral")
	private String doubleintegral ;
	
	
	/**当前账户余额*/  
	@Column(name="remaining")
	private String remaining ;
	
	
//
//	public String getProdid() {
//		return prodid;
//	}
//
//	public void setProdid(String prodid) {
//		this.prodid = prodid;
//	}

	public String getProname() {
		return proname;
	}

	public void setProname(String proname) {
		this.proname = proname;
	}

	public String getNettime() {
		return nettime;
	}

	public void setNettime(String nettime) {
		this.nettime = nettime;
	}

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


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getAccountstatus() {
		return accountstatus;
	}

	public void setAccountstatus(String accountstatus) {
		this.accountstatus = accountstatus;
	}




	public String getConsumeintegral() {
		return consumeintegral;
	}

	public void setConsumeintegral(String consumeintegral) {
		this.consumeintegral = consumeintegral;
	}

	public String getDoubleintegral() {
		return doubleintegral;
	}

	public void setDoubleintegral(String doubleintegral) {
		this.doubleintegral = doubleintegral;
	}

	public String getRemaining() {
		return remaining;
	}

	public void setRemaining(String remaining) {
		this.remaining = remaining;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	
}
