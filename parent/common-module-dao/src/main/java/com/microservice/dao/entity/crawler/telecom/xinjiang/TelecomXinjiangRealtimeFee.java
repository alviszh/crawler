package com.microservice.dao.entity.crawler.telecom.xinjiang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_xinjiang_realtimefee" ,indexes = {@Index(name = "index_telecom_xinjiang_realtimefee_taskid", columnList = "taskid")})
public class TelecomXinjiangRealtimeFee extends IdEntity {

	private String feeName;//费用名称
	private String amount;//已消费金额
	private String type;//类型  1号码实时费用 0账户实时费用
	private String taskid;
   
	public String getFeeName() {
		return feeName;
	}

	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
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

	@Override
	public String toString() {
		return "TelecomXinjiangRealtimeFee [feeName=" + feeName + ", amount=" + amount + ", type=" + type + ", taskid="
				+ taskid + "]";
	}
}