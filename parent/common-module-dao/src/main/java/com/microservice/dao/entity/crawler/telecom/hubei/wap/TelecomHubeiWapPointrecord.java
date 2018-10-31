package com.microservice.dao.entity.crawler.telecom.hubei.wap;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_hubei_wap_pointrecord" ,indexes = {@Index(name = "index_telecom_hubei_wap_pointrecord_taskid", columnList = "taskid")})
public class TelecomHubeiWapPointrecord extends IdEntity {

	private String nowPoint;// 当前可用积分
	private String addPointMonth;// 当月新增积分
	private String yearFailPoint;// 年末到期积分
	private String changePointMonth;// 当月兑换积分
	private String taskid;
  
    
	public String getNowPoint() {
		return nowPoint;
	}
	public void setNowPoint(String nowPoint) {
		this.nowPoint = nowPoint;
	}
	public String getAddPointMonth() {
		return addPointMonth;
	}
	public void setAddPointMonth(String addPointMonth) {
		this.addPointMonth = addPointMonth;
	}
	public String getYearFailPoint() {
		return yearFailPoint;
	}
	public void setYearFailPoint(String yearFailPoint) {
		this.yearFailPoint = yearFailPoint;
	}
	public String getChangePointMonth() {
		return changePointMonth;
	}
	public void setChangePointMonth(String changePointMonth) {
		this.changePointMonth = changePointMonth;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@Override
	public String toString() {
		return "TelecomHubeiWapPointrecords [nowPoint=" + nowPoint + ", addPointMonth=" + addPointMonth
				+ ", yearFailPoint=" + yearFailPoint + ", changePointMonth=" + changePointMonth + ", taskid=" + taskid
				+ "]";
	}
	
	public TelecomHubeiWapPointrecord(String nowPoint, String addPointMonth, String yearFailPoint,
			String changePointMonth, String taskid) {
		super();
		this.nowPoint = nowPoint;
		this.addPointMonth = addPointMonth;
		this.yearFailPoint = yearFailPoint;
		this.changePointMonth = changePointMonth;
		this.taskid = taskid;
	}
	public TelecomHubeiWapPointrecord() {
		super();
		
	}
}