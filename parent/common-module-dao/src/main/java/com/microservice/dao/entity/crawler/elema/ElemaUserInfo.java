package com.microservice.dao.entity.crawler.elema;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="elema_userinfo",indexes = {@Index(name = "index_elema_userinfo_taskid", columnList = "taskid")})
public class ElemaUserInfo extends IdEntity{
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**账户名 */
	@Column(name="username")
	private String username;
	
	/**账户Id */
	@Column(name="user_id")
	private String user_id;
	
	/**余额 */
	@Column(name="balance")
	private String balance;
	
	/**号码 */
	@Column(name="mobile")
	private String mobile;
	
	/**金币个数 */
	@Column(name="point")
	private String point;

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

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
}
