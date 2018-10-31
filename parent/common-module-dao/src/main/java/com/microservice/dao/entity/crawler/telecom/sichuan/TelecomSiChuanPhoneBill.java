package com.microservice.dao.entity.crawler.telecom.sichuan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_sichuan_phonebill",indexes = {@Index(name = "index_telecom_sichuan_phonebill_taskid", columnList = "taskid")})
public class TelecomSiChuanPhoneBill extends IdEntity{

	private String level_id;//级别
	
	private String acc_item_type;//级别名称
	
	private String acc_item_detail;//费用名称
	
	private String fee;//费用
	
	private String total;//总花费
	
	private String belongsDate;//所属月

	private String taskid;
	
	private Integer userid;
	
	
	public String getBelongsDate() {
		return belongsDate;
	}

	public void setBelongsDate(String belongsDate) {
		this.belongsDate = belongsDate;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getLevel_id() {
		return level_id;
	}

	public void setLevel_id(String level_id) {
		this.level_id = level_id;
	}

	public String getAcc_item_type() {
		return acc_item_type;
	}

	public void setAcc_item_type(String acc_item_type) {
		this.acc_item_type = acc_item_type;
	}

	public String getAcc_item_detail() {
		return acc_item_detail;
	}

	public void setAcc_item_detail(String acc_item_detail) {
		this.acc_item_detail = acc_item_detail;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "TelecomSiChuanPhoneBill [level_id=" + level_id + ", acc_item_type=" + acc_item_type
				+ ", acc_item_detail=" + acc_item_detail + ", fee=" + fee + ", total=" + total + "]";
	}
	
	
}
