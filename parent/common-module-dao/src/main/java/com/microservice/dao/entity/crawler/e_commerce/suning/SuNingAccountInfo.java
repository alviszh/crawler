package com.microservice.dao.entity.crawler.e_commerce.suning;

import com.microservice.dao.entity.IdEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "e_commerce_suning_account_info" ,indexes = {@Index(name = "index_e_commerce_suning_account_info_taskid", columnList = "taskid")})
public class SuNingAccountInfo extends IdEntity implements Serializable {

    private String taskid;
    private String name;						//真实姓名			
    private String username;					//账户名			
    private String idNum;						//证件号码
    private String phoneNum;					//手机号码
    private String email;						//联系邮箱
    private String occupation;					//职业
    private String address;						//联系地址
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "SuNingAccountInfo [taskid=" + taskid + ", name=" + name + ", username=" + username + ", idNum=" + idNum
				+ ", phoneNum=" + phoneNum + ", email=" + email + ", occupation=" + occupation + ", address=" + address
				+ "]";
	}
    
    
   }
