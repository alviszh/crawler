package com.microservice.dao.entity.crawler.insurance.nanjing;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * @Description: 南京社保用户信息实体
 * @author sln
 * @date 2017年9月26日
 */
@Entity
@Table(name = "insurance_nanjing_userinfo",indexes = {@Index(name = "index_insurance_nanjing_userinfo_taskid", columnList = "taskid")})
public class InsuranceNanjingUserInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 8405318726410507885L;
	private String taskid;
//	社保卡号
	private String laborcardnum;
//	姓名
	private String name;
//	身份证号
	private String idnum;
//	人员状态
	private String perstatus;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getLaborcardnum() {
		return laborcardnum;
	}
	public void setLaborcardnum(String laborcardnum) {
		this.laborcardnum = laborcardnum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getPerstatus() {
		return perstatus;
	}
	public void setPerstatus(String perstatus) {
		this.perstatus = perstatus;
	}
	public InsuranceNanjingUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
