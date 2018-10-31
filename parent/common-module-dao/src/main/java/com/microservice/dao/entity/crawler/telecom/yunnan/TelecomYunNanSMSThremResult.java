package com.microservice.dao.entity.crawler.telecom.yunnan;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_yunnan_smsresult")
public class TelecomYunNanSMSThremResult extends IdEntity {

	private String xuhao;//序号
	
	private String callphone;// 主叫号码

	private String callphoneother;// 被叫号码

	private String date;// 通话开始时间
	
	private String callcosts;// 通话基本漫游费用
		
	private String calltype;// 通话类型

	private Integer userid;

	private String taskid;

	public String getXuhao() {
		return xuhao;
	}

	public void setXuhao(String xuhao) {
		this.xuhao = xuhao;
	}

	public String getCallphone() {
		return callphone;
	}

	public void setCallphone(String callphone) {
		this.callphone = callphone;
	}

	public String getCallphoneother() {
		return callphoneother;
	}

	public void setCallphoneother(String callphoneother) {
		this.callphoneother = callphoneother;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCallcosts() {
		return callcosts;
	}

	public void setCallcosts(String callcosts) {
		this.callcosts = callcosts;
	}

	public String getCalltype() {
		return calltype;
	}

	public void setCalltype(String calltype) {
		this.calltype = calltype;
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
		return "TelecomYunNanSMSThremResult [xuhao=" + xuhao + ", callphone=" + callphone + ", callphoneother="
				+ callphoneother + ", date=" + date + ", callcosts=" + callcosts + ", calltype=" + calltype
				+ ", userid=" + userid + ", taskid=" + taskid + "]";
	}
	
	

}