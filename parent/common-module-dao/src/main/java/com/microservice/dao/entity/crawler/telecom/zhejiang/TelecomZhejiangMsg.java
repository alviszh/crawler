package com.microservice.dao.entity.crawler.telecom.zhejiang;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 浙江电信-短信详情表
 * @author zz
 *
 */
@Entity
@Table(name="telecom_zhejiang_msg")
public class TelecomZhejiangMsg extends IdEntity{
	
	private String taskid;
	private String otherNum;					//对方号码
	private String businessType;				//业务类型
	private String beginTime;					//发送开始日期时间
	private String fee;							//通信费
	private String remission;					//减免
	private String totalFee;					//费用小计
	
	@Override
	public String toString() {
		return "TelecomZhejiangMsg [taskid=" + taskid + ", otherNum=" + otherNum + ", businessType=" + businessType
				+ ", beginTime=" + beginTime + ", fee=" + fee + ", remission=" + remission + ", totalFee=" + totalFee
				+ "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getOtherNum() {
		return otherNum;
	}
	public void setOtherNum(String otherNum) {
		this.otherNum = otherNum;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getRemission() {
		return remission;
	}
	public void setRemission(String remission) {
		this.remission = remission;
	}
	public String getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

}
