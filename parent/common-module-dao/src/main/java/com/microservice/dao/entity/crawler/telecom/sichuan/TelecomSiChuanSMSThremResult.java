package com.microservice.dao.entity.crawler.telecom.sichuan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name = "telecom_sichuan_smsresult",indexes = {@Index(name = "index_telecom_sichuan_smsresult_taskid", columnList = "taskid")})
public class TelecomSiChuanSMSThremResult extends IdEntity{

	private String callPhone;//对方号码
	
	private String beginDate;//发送时间
	
	private String feeKind;//短信类型
	
	private String total;//合计(元)
	
	private Integer userid;

	private String taskid;

	public String getCallPhone() {
		return callPhone;
	}

	public void setCallPhone(String callPhone) {
		this.callPhone = callPhone;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getFeeKind() {
		return feeKind;
	}

	public void setFeeKind(String feeKind) {
		this.feeKind = feeKind;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
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
		return "TelecomLiaoNingSMSThremResult [callPhone=" + callPhone + ", beginDate=" + beginDate + ", feeKind="
				+ feeKind + ", total=" + total + ", userid=" + userid + ", taskid=" + taskid + "]";
	}
	
	
	
}
