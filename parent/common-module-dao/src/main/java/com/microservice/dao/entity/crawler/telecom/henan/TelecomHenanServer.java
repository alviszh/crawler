package com.microservice.dao.entity.crawler.telecom.henan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "telecom_henan_server", indexes = {@Index(name = "index_telecom_henan_server_taskid", columnList = "taskid")})
public class TelecomHenanServer extends IdEntity {

	private String taskid;
	private String serverName;							//业务名称
	private String startDate;							//生效时间
	private String endDate;								//失效时间
	private String cancel;								//取消指令
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCancel() {
		return cancel;
	}
	public void setCancel(String cancel) {
		this.cancel = cancel;
	}
	@Override
	public String toString() {
		return "TelecomHenanServer [taskid=" + taskid + ", serverName=" + serverName + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", cancel=" + cancel + "]";
	}
	
	

}