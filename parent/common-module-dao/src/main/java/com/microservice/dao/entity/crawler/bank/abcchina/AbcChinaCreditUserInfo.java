package com.microservice.dao.entity.crawler.bank.abcchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="abcchinaCredit_userinfo",indexes = {@Index(name = "index_abcchinaCredit_userinfo_taskid", columnList = "taskid")})
public class AbcChinaCreditUserInfo extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**姓名 */
	@Column(name="name")
	private String name;
	
	/**积分 */
	@Column(name="integral")
	private String integral;
	
	/**用户类别 */
	@Column(name="userType")
	private String userType;
	
	/**卡号*/
	@Column(name="cardNumber")
	private String cardNumber;

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

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
