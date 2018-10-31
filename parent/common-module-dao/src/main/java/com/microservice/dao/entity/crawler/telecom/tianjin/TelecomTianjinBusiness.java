package com.microservice.dao.entity.crawler.telecom.tianjin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description  业务信息实体
 * @author sln
 * @date 2017年9月11日
 */
@Entity
@Table(name = "telecom_tianjin_business",indexes = {@Index(name = "index_telecom_tianjin_business_taskid", columnList = "taskid")})
public class TelecomTianjinBusiness extends IdEntity implements Serializable {
	private static final long serialVersionUID = -4969108718212569861L;
	private String taskid;
//  业务名称
	private String businessname;
//	生效时间
	private String effectivetime;
//	失效时间
	private String expireTime;
//	业务类型
	private String businesstype;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getBusinessname() {
		return businessname;
	}
	public void setBusinessname(String businessname) {
		this.businessname = businessname;
	}
	public String getEffectivetime() {
		return effectivetime;
	}
	public void setEffectivetime(String effectivetime) {
		this.effectivetime = effectivetime;
	}
	public String getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}
	public TelecomTianjinBusiness() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getBusinesstype() {
		return businesstype;
	}
	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}
	public TelecomTianjinBusiness(String taskid, String businessname, String effectivetime, String expireTime,
			String businesstype) {
		super();
		this.taskid = taskid;
		this.businessname = businessname;
		this.effectivetime = effectivetime;
		this.expireTime = expireTime;
		this.businesstype = businesstype;
	}
}

