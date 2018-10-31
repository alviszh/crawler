package com.microservice.dao.entity.crawler.telecom.hainan;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_hainan_integra")
public class TelecomHaiNanIntegraChangeResult extends IdEntity {

	private String xuhao;//序号
		
	private String date;//月份
		
	private String newintegra;//当月新增积分

	private String consumptionintegra ;//当月消费积分
	
	private String bonusintegra ;//当月奖励积分
	
	private String usableintegra ;//当月奖励积分
	
	private Integer userid;

	private String taskid;

	public String getXuhao() {
		return xuhao;
	}

	public void setXuhao(String xuhao) {
		this.xuhao = xuhao;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getNewintegra() {
		return newintegra;
	}

	public void setNewintegra(String newintegra) {
		this.newintegra = newintegra;
	}

	public String getConsumptionintegra() {
		return consumptionintegra;
	}

	public void setConsumptionintegra(String consumptionintegra) {
		this.consumptionintegra = consumptionintegra;
	}

	public String getBonusintegra() {
		return bonusintegra;
	}

	public void setBonusintegra(String bonusintegra) {
		this.bonusintegra = bonusintegra;
	}

	public String getUsableintegra() {
		return usableintegra;
	}

	public void setUsableintegra(String usableintegra) {
		this.usableintegra = usableintegra;
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
		return "TelecomHaiNanIntegraChangeResult [xuhao=" + xuhao + ", date=" + date + ", newintegra=" + newintegra
				+ ", consumptionintegra=" + consumptionintegra + ", bonusintegra=" + bonusintegra + ", usableintegra="
				+ usableintegra + ", userid=" + userid + ", taskid=" + taskid + "]";
	}

	
	
}