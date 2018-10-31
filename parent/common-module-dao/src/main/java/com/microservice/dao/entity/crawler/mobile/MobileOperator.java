package com.microservice.dao.entity.crawler.mobile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

/**
 * @Description: 三大手机运营商表
 * @author zzhen
 * @date 2017年6月16日 下午2:23:24
 */
@Entity
@Table(name="mobile_operator")
public class MobileOperator extends IdEntity{
	
	private String type;		//1.移动；2.联通；3.电信；4.虚拟运营商
	@Column(name="mobile_num")
	private String mobileNum;	//手机号前三位
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMobileNum() {
		return mobileNum;
	}
	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}
	
	@Override
	public String toString() {
		return "MobileOperator [type=" + type + ", mobileNum=" + mobileNum + "]";
	}
	
	
}
