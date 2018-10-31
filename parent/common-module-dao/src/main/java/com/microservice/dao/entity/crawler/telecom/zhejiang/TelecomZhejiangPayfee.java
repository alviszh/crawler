package com.microservice.dao.entity.crawler.telecom.zhejiang;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

/**
 * 浙江电信-缴费信息
 * @author zz
 *
 */
@Entity
@Table(name="telecom_zhejiang_payfee")
public class TelecomZhejiangPayfee extends IdEntity {
	
	private String taskid;
	private String certificateType;					//凭证类型
	private String feeType;							//交费方式
	private String payType;							//付款类型
	private String paySite;							//付款地点
	private String payTime;							//付款时间
	private String payMoney;						//付款金额
	
	@Override
	public String toString() {
		return "TelecomZhejiangPayfee [taskid=" + taskid + ", certificateType=" + certificateType + ", feeType="
				+ feeType + ", payType=" + payType + ", paySite=" + paySite + ", payTime=" + payTime + ", payMoney="
				+ payMoney + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCertificateType() {
		return certificateType;
	}
	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPaySite() {
		return paySite;
	}
	public void setPaySite(String paySite) {
		this.paySite = paySite;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}
	
	

}
