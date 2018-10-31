package com.microservice.dao.entity.crawler.insurance.weifang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 潍坊社保信息用户
 * @author zcx
 *
 */
@Entity
@Table(name = "insurance_weifang_userinfo",indexes = {@Index(name = "index_insurance_weifang_userinfo_taskid", columnList = "taskid")})
public class InsuranceWeiFangUserInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 3499962561560605116L;
	private String taskid;
//	姓名
	private String username;
//	人员类别
	private String personalType;
//	经办机构
	private String institution;
//	单位名称
	private String companyName;

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
	public String getPersonalType() {
		return personalType;
	}
	public void setPersonalType(String personalType) {
		this.personalType = personalType;
	}
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	@Override
	public String toString() {
		return "InsuranceWeiFangUserInfo [taskid=" + taskid + ", username=" + username + ", personalType="
				+ personalType + ", institution=" + institution + ", companyName=" + companyName + "]";
	}
	public InsuranceWeiFangUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
