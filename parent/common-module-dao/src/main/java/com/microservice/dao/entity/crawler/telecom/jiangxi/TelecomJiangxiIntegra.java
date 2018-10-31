package com.microservice.dao.entity.crawler.telecom.jiangxi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description 积分生成记录实体
 * @author sln
 * @date 2017年9月16日
 */
@Entity
@Table(name = "telecom_jiangxi_integra",indexes = {@Index(name = "index_telecom_jiangxi_integra_taskid", columnList = "taskid")})
public class TelecomJiangxiIntegra extends IdEntity implements Serializable {
	private static final long serialVersionUID = -8924603691957866946L;
	private String taskid;
//	积分周期
	private String qrymonth;
//	当月积分
	private String monthintegra;
//	奖励积分
	private String bonusintegra;
//	在网积分
	private String networkintegra;
//	信用积分
	private String creditintegra;
//	倍增积分
	private String multipintegra;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getQrymonth() {
		return qrymonth;
	}
	public void setQrymonth(String qrymonth) {
		this.qrymonth = qrymonth;
	}
	public String getMonthintegra() {
		return monthintegra;
	}
	public void setMonthintegra(String monthintegra) {
		this.monthintegra = monthintegra;
	}
	public String getBonusintegra() {
		return bonusintegra;
	}
	public void setBonusintegra(String bonusintegra) {
		this.bonusintegra = bonusintegra;
	}
	public String getNetworkintegra() {
		return networkintegra;
	}
	public void setNetworkintegra(String networkintegra) {
		this.networkintegra = networkintegra;
	}
	public String getCreditintegra() {
		return creditintegra;
	}
	public void setCreditintegra(String creditintegra) {
		this.creditintegra = creditintegra;
	}
	public String getMultipintegra() {
		return multipintegra;
	}
	public void setMultipintegra(String multipintegra) {
		this.multipintegra = multipintegra;
	}
	public TelecomJiangxiIntegra() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TelecomJiangxiIntegra(String taskid, String qrymonth, String monthintegra, String bonusintegra,
			String networkintegra, String creditintegra, String multipintegra) {
		super();
		this.taskid = taskid;
		this.qrymonth = qrymonth;
		this.monthintegra = monthintegra;
		this.bonusintegra = bonusintegra;
		this.networkintegra = networkintegra;
		this.creditintegra = creditintegra;
		this.multipintegra = multipintegra;
	}

}

