package com.microservice.dao.entity.crawler.bank.cmbchina;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

//招商银行用户信息
@Entity
@Table(name = "cmbchina_debitcard_userinfo" ,indexes = {@Index(name = "index_cmbchina_debitcard_userinfo_taskid", columnList = "taskid")})
public class CmbChinaDebitCardUserInfo extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -706565097429868753L;

	// 任务id
	private String taskid;
	
	private String cardNo;
	
	private String username;
	
	private String branch;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	@Override
	public String toString() {
		return "CmbChinaDebitCardUserInfo [taskid=" + taskid + ", cardNo=" + cardNo + ", username=" + username
				+ ", branch=" + branch + "]";
	}
	
	

}
