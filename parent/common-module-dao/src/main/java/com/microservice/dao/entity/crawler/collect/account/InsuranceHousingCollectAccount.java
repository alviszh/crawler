package com.microservice.dao.entity.crawler.collect.account;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

/**
 * 针对一些未开发网站的账户收集
 * @author zz
 *
 */
@Entity
@Table(name="insurance_housing_collect_account")
public class InsuranceHousingCollectAccount extends IdEntity{
	
	private String city;
	private String type;				//社保还是公积金
	private String taskid;
	private String username;			//用户名
	private String password;			//密码
	private String loginType;			//登录方式
	
	@Override
	public String toString() {
		return "InsuranceHousingCollectAccount [city=" + city + ", type=" + type + ", taskid=" + taskid + ", username="
				+ username + ", password=" + password + ", loginType=" + loginType + "]";
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

}
