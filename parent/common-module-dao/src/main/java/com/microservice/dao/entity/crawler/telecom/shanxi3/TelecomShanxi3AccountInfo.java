package com.microservice.dao.entity.crawler.telecom.shanxi3;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description  账户信息实体
 * @author sln
 * @date 2017年8月24日 上午10:18:55
 */
@Entity
@Table(name = "telecom_shanxi3_accountinfo",indexes = {@Index(name = "index_telecom_shanxi3_accountinfo_taskid", columnList = "taskid")})
public class TelecomShanxi3AccountInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = -240346264074579819L;
	private String taskid;
//	星级
	private String starlevel;
//	成长值
	private Integer growthpoint;
	public TelecomShanxi3AccountInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getStarlevel() {
		return starlevel;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setStarlevel(String starlevel) {
		this.starlevel = starlevel;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	public Integer getGrowthpoint() {
		return growthpoint;
	}
	public void setGrowthpoint(Integer growthpoint) {
		this.growthpoint = growthpoint;
	}
	public TelecomShanxi3AccountInfo(String taskid, String starlevel, Integer growthpoint) {
		super();
		this.taskid = taskid;
		this.starlevel = starlevel;
		this.growthpoint = growthpoint;
	}
	
}

