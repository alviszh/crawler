package com.microservice.dao.entity.crawler.telecom.sichuan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


@Entity
@Table(name = "telecom_sichuan_callresult",indexes = {@Index(name = "index_telecom_sichuan_callresult_taskid", columnList = "taskid")})
public class TelecomSiChuanCallThremResult extends IdEntity{

	private String callType;// 呼叫类型
	
	private String counterArea;//对方地点
	
	private String counterNumber;//对方号码
	
	private String callDate;// 呼叫时间
	
	private String duration;// 时长(秒)
	
	private String type;// 通话类型
	
	private String favour;//优惠减免
	
	private String billing;//通话地点
	
	private String baseFee;// 基本费
	
	private String total;//总计费用

	private String tollAdd;//长途费

	private String otherFee;// 其他费用

	private Integer userid;

	private String taskid;

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getCounterArea() {
		return counterArea;
	}

	public void setCounterArea(String counterArea) {
		this.counterArea = counterArea;
	}

	public String getCounterNumber() {
		return counterNumber;
	}

	public void setCounterNumber(String counterNumber) {
		this.counterNumber = counterNumber;
	}

	public String getCallDate() {
		return callDate;
	}

	public void setCallDate(String callDate) {
		this.callDate = callDate;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFavour() {
		return favour;
	}

	public void setFavour(String favour) {
		this.favour = favour;
	}

	public String getBilling() {
		return billing;
	}

	public void setBilling(String billing) {
		this.billing = billing;
	}

	public String getBaseFee() {
		return baseFee;
	}

	public void setBaseFee(String baseFee) {
		this.baseFee = baseFee;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getTollAdd() {
		return tollAdd;
	}

	public void setTollAdd(String tollAdd) {
		this.tollAdd = tollAdd;
	}

	public String getOtherFee() {
		return otherFee;
	}

	public void setOtherFee(String otherFee) {
		this.otherFee = otherFee;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "TelecomLiaoNingCallThremResult [callType=" + callType + ", counterArea=" + counterArea
				+ ", counterNumber=" + counterNumber + ", callDate=" + callDate + ", duration=" + duration + ", type="
				+ type + ", favour=" + favour + ", billing=" + billing + ", baseFee=" + baseFee + ", total=" + total
				+ ", tollAdd=" + tollAdd + ", otherFee=" + otherFee + ", userid=" + userid + ", taskid=" + taskid + "]";
	}

	

}
