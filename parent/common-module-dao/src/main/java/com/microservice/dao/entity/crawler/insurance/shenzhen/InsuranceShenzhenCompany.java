package com.microservice.dao.entity.crawler.insurance.shenzhen;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 深圳社保 单位信息
 * @author rongshengxu
 *
 */
@Entity
@Table(name="insurance_shenzhen_company")
public class InsuranceShenzhenCompany extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 5752802241266503742L;
	
	/** 登录名 */
	@Column(name="login_name")
	private String loginName;
	
	/** 爬取批次号 */
	@Column(name="task_id")
	private String taskId;
	
	/** 单位编号 */
	@Column(name="code")
	private String code;
	
	/** 单位姓名 */
	@Column(name="name")
	private String name;
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
