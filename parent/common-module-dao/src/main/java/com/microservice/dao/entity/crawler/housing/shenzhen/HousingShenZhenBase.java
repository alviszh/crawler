package com.microservice.dao.entity.crawler.housing.shenzhen;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description:
 * @author: qzb
 * @date: 2017年9月29日 上午9:58:45
 */
@Entity
@Table(name = "housing_shenzhen_base")
public class HousingShenZhenBase extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;

	@Column(name = "taskid")
	private String taskid;
	// 个人公积金账户
	@Column(name = "person_number")
	private String person_number;
	// 状态
	@Column(name = "status")
	private String status;
	// 账户余额
	@Column(name = "yue")
	private String yue;
	// 社保移交金额
	@Column(name = "move_money")
	private String move_money;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPerson_number() {
		return person_number;
	}
	public void setPerson_number(String person_number) {
		this.person_number = person_number;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getYue() {
		return yue;
	}
	public void setYue(String yue) {
		this.yue = yue;
	}
	public String getMove_money() {
		return move_money;
	}
	public void setMove_money(String move_money) {
		this.move_money = move_money;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
