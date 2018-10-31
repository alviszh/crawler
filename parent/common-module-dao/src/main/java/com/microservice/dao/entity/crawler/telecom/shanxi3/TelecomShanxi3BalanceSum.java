package com.microservice.dao.entity.crawler.telecom.shanxi3;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description   余额汇总实体
 * @author sln
 * @date 2017年9月7日 下午3:29:28
 */
@Entity
@Table(name = "telecom_shanxi3_balancesum",indexes = {@Index(name = "index_telecom_shanxi3_balancesum_taskid", columnList = "taskid")})
public class TelecomShanxi3BalanceSum extends IdEntity implements Serializable {
	private static final long serialVersionUID = 308185458603609466L;
//	余额类型
	private String balancetype;
//	上期末余额
	private String lastbalance;
//	本期入账
	private String thisbalancein;
//	本期支出
	private String thisbalanceout;
//	本期末余额
	private String thisbalance;
//	查询周期
	private String querycycle;
	private String taskid;
	public String getBalancetype() {
		return balancetype;
	}
	public void setBalancetype(String balancetype) {
		this.balancetype = balancetype;
	}
	public String getLastbalance() {
		return lastbalance;
	}
	public void setLastbalance(String lastbalance) {
		this.lastbalance = lastbalance;
	}
	public String getThisbalancein() {
		return thisbalancein;
	}
	public void setThisbalancein(String thisbalancein) {
		this.thisbalancein = thisbalancein;
	}
	public String getThisbalanceout() {
		return thisbalanceout;
	}
	public void setThisbalanceout(String thisbalanceout) {
		this.thisbalanceout = thisbalanceout;
	}
	public String getThisbalance() {
		return thisbalance;
	}
	public void setThisbalance(String thisbalance) {
		this.thisbalance = thisbalance;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public TelecomShanxi3BalanceSum() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getQuerycycle() {
		return querycycle;
	}
	public void setQuerycycle(String querycycle) {
		this.querycycle = querycycle;
	}
	public TelecomShanxi3BalanceSum(String balancetype, String lastbalance, String thisbalancein, String thisbalanceout,
			String thisbalance, String querycycle, String taskid) {
		super();
		this.balancetype = balancetype;
		this.lastbalance = lastbalance;
		this.thisbalancein = thisbalancein;
		this.thisbalanceout = thisbalanceout;
		this.thisbalance = thisbalance;
		this.querycycle = querycycle;
		this.taskid = taskid;
	}
	
}

