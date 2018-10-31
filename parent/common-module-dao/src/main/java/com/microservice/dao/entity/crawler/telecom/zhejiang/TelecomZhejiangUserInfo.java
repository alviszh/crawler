package com.microservice.dao.entity.crawler.telecom.zhejiang;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 浙江电信-用户信息表
 * @author zz
 *
 */
@Entity
@Table(name="telecom_zhejiang_userinfo")
public class TelecomZhejiangUserInfo extends IdEntity {
	
	private String taskid;
	private String productName;					//套餐
	private String starLevel;					//星级
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getStarLevel() {
		return starLevel;
	}
	public void setStarLevel(String starLevel) {
		this.starLevel = starLevel;
	}
}
