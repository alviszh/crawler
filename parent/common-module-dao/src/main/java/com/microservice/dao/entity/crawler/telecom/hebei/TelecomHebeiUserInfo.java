package com.microservice.dao.entity.crawler.telecom.hebei;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

/**
 * 河北电信-用户信息表
 * @author zz
 *
 */
@Entity
@Table(name="telecom_hebei_userinfo")
public class TelecomHebeiUserInfo extends IdEntity{
	
	private String taskid;
	private String name;						//用户名
	private String starLevel;					//星级
	private String city;						//所属城市
	private String serviceStatus;				//服务状态	
	private String customerLevel;				//客户级别
	private String certificateType;				//证件类型
	private String certificateNum;				//证件号码
	private String address;						//客户地址
	
	@Override
	public String toString() {
		return "TelecomHebeiUserInfo [taskid=" + taskid + ", name=" + name + ", starLevel=" + starLevel + ", city="
				+ city + ", serviceStatus=" + serviceStatus + ", customerLevel=" + customerLevel + ", certificateType="
				+ certificateType + ", certificateNum=" + certificateNum + ", address=" + address + "]";
	}
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStarLevel() {
		return starLevel;
	}
	public void setStarLevel(String starLevel) {
		this.starLevel = starLevel;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getServiceStatus() {
		return serviceStatus;
	}
	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
	public String getCustomerLevel() {
		return customerLevel;
	}
	public void setCustomerLevel(String customerLevel) {
		this.customerLevel = customerLevel;
	}
	public String getCertificateType() {
		return certificateType;
	}
	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}
	public String getCertificateNum() {
		return certificateNum;
	}
	public void setCertificateNum(String certificateNum) {
		this.certificateNum = certificateNum;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

}
