package com.microservice.dao.entity.crawler.telecom.shanxi3;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description  余额明细信息
 * @author sln
 * @date 2017年9月7日 下午3:38:16
 */
@Entity
@Table(name = "telecom_shanxi3_balancedetail",indexes = {@Index(name = "index_telecom_shanxi3_balancedetail_taskid", columnList = "taskid")})
public class TelecomShanxi3BalanceDetail extends IdEntity implements Serializable {
	private static final long serialVersionUID = 6273301796076937929L;
	private String taskid;
//	时间
	private String month;
//	余额类型
	private String balancetype;
//	入账
	private String balancein;
//	支出
	private String balanceout;
//	变动类型
	private String changetype;
//	余额
	private String balance;
//	使用范围
	private String usescope;
//	查询周期
	private String querycycle;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getBalancetype() {
		return balancetype;
	}
	public void setBalancetype(String balancetype) {
		this.balancetype = balancetype;
	}
	public String getChangetype() {
		return changetype;
	}
	public void setChangetype(String changetype) {
		this.changetype = changetype;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getUsescope() {
		return usescope;
	}
	public void setUsescope(String usescope) {
		this.usescope = usescope;
	}
	public String getBalancein() {
		return balancein;
	}
	public void setBalancein(String balancein) {
		this.balancein = balancein;
	}
	public String getBalanceout() {
		return balanceout;
	}
	public void setBalanceout(String balanceout) {
		this.balanceout = balanceout;
	}
	public TelecomShanxi3BalanceDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getQuerycycle() {
		return querycycle;
	}
	public void setQuerycycle(String querycycle) {
		this.querycycle = querycycle;
	}
	public TelecomShanxi3BalanceDetail(String taskid, String month, String balancetype, String balancein,
			String balanceout, String changetype, String balance, String usescope, String querycycle) {
		super();
		this.taskid = taskid;
		this.month = month;
		this.balancetype = balancetype;
		this.balancein = balancein;
		this.balanceout = balanceout;
		this.changetype = changetype;
		this.balance = balance;
		this.usescope = usescope;
		this.querycycle = querycycle;
	}
	
}

