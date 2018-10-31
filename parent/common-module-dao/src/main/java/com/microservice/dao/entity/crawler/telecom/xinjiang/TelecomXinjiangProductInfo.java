package com.microservice.dao.entity.crawler.telecom.xinjiang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_xinjiang_productinfo" ,indexes = {@Index(name = "index_telecom_xinjiang_productinfo_taskid", columnList = "taskid")})
public class TelecomXinjiangProductInfo extends IdEntity {
	private String area; // 区号
	private String proNumber; // 产品号码
	private String proAddress; // 产品所属地址
	private String proType; // 产品类型
	private String proAccount; // 账号
	private String taskid;
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getProNumber() {
		return proNumber;
	}
	public void setProNumber(String proNumber) {
		this.proNumber = proNumber;
	}
	public String getProAddress() {
		return proAddress;
	}
	public void setProAddress(String proAddress) {
		this.proAddress = proAddress;
	}
	public String getProType() {
		return proType;
	}
	public void setProType(String proType) {
		this.proType = proType;
	}
	public String getProAccount() {
		return proAccount;
	}
	public void setProAccount(String proAccount) {
		this.proAccount = proAccount;
	}
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "TelecomXinjiangProductInfo [area=" + area + ", proNumber=" + proNumber + ", proAddress=" + proAddress
				+ ", proType=" + proType + ", proAccount=" + proAccount + ", taskid=" + taskid + "]";
	}
	
}