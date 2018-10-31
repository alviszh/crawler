package com.microservice.dao.entity.crawler.telecom.fujian;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 福建电信基本信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="telecom_fujian_userinfo",indexes = {@Index(name = "index_telecom_fujian_userinfo_taskid", columnList = "taskid")})
public class TelecomFujianUserInfo  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**当前可用积分*/ 
	@Column(name="use_integral")
	private String useintegral ;
	
	
	/**到期积分*/ 
	@Column(name="expire_integral")
	private String expireintegral ;
	
	
	/**账户状态*/   
	@Column(name="accountstatus")
	private String accountstatus;
	/**姓名*/   
	@Column(name="name")
	private String name;
	/**归属城市*/   
	@Column(name="city")
	private String city;
	
	/**余额*/  
	@Column(name="commonremaining")
	private String commonremaining ;

	/**电话号码*/   
	@Column(name="phone")
	private String phone ;

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

	public String getAccountstatus() {
		return accountstatus;
	}

	public void setAccountstatus(String accountstatus) {
		this.accountstatus = accountstatus;
	}


	public String getUseintegral() {
		return useintegral;
	}

	public void setUseintegral(String useintegral) {
		this.useintegral = useintegral;
	}


	public String getExpireintegral() {
		return expireintegral;
	}

	public void setExpireintegral(String expireintegral) {
		this.expireintegral = expireintegral;
	}

	public String getCommonremaining() {
		return commonremaining;
	}

	public void setCommonremaining(String commonremaining) {
		this.commonremaining = commonremaining;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}


	
}
