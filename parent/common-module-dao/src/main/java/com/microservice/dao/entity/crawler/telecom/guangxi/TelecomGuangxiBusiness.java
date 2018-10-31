package com.microservice.dao.entity.crawler.telecom.guangxi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="telecomGuangxi_business",indexes = {@Index(name = "index_telecomGuangxi_business_taskid", columnList = "taskid")}) 
public class TelecomGuangxiBusiness extends IdEntity{
    private String name;//业务名称
	
	private String startTime; //生效时间
	
	private String outTime;//失效时间
	
	
	private String taskid;

	private String packageTime;//套餐名称
	
	private String buyTime;//订购时间
	
	private String favourable;//优惠时间
	
	private String introduce;//套餐介绍

	@Override
	public String toString() {
		return "TelecomGuangxiBusiness [name=" + name + ", startTime=" + startTime + ", outTime=" + outTime
				+ ", taskid=" + taskid + ", packageTime=" + packageTime + ", buyTime=" + buyTime + ", favourable="
				+ favourable + ", introduce=" + introduce + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPackageTime() {
		return packageTime;
	}

	public void setPackageTime(String packageTime) {
		this.packageTime = packageTime;
	}

	public String getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}

	public String getFavourable() {
		return favourable;
	}

	public void setFavourable(String favourable) {
		this.favourable = favourable;
	}

	@Column(columnDefinition="text")
	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	

	
	
}
