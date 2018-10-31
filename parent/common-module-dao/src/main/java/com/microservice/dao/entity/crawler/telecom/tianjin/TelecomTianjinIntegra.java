package com.microservice.dao.entity.crawler.telecom.tianjin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description 积分生成记录实体
 * @author sln
 * @date 2017年9月11日
 */
@Entity
@Table(name = "telecom_tianjin_integra",indexes = {@Index(name = "index_telecom_tianjin_integra_taskid", columnList = "taskid")})
public class TelecomTianjinIntegra extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1411272316245356765L;
	private String taskid;
//	查询开始时间
	private String startdate;
//	查询结束时间
	private String enddate;
//	发放时间
	private String senddate;
//	积分值
	private String integra;
//	来源类型
	private String type;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getSenddate() {
		return senddate;
	}
	public void setSenddate(String senddate) {
		this.senddate = senddate;
	}
	public String getIntegra() {
		return integra;
	}
	public void setIntegra(String integra) {
		this.integra = integra;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public TelecomTianjinIntegra() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TelecomTianjinIntegra(String taskid, String startdate, String enddate, String senddate, String integra,
			String type) {
		super();
		this.taskid = taskid;
		this.startdate = startdate;
		this.enddate = enddate;
		this.senddate = senddate;
		this.integra = integra;
		this.type = type;
	}
}

