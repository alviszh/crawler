package com.microservice.dao.entity.crawler.telecom.jiangsu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 余额变动明细
 * 
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_jiangsu_balance" ,indexes = {@Index(name = "index_telecom_jiangsu_balance_taskid", columnList = "taskid")})
public class TelecomJiangsuBalance extends IdEntity {

	/**
	 * taskid
	 */
	private String taskid;

	/**
	 * 时间 				
	 * 状态时间（变动时间）
	 */
	private String dccStateDate;

	/**
	 * 余额类型 
	 * 余额类型标识0：通用余额 1：专用余额 2：用户级 3：用户帐目组级 4：帐户帐目组级
	 */
	private String aocUnit;

	/**
	 * 入帐（元） 
	 * 本期入帐
	 */
	private String dccPaymentAmount;

	/**
	 * 支出（元） 
	 * 本期支出
	 */
	private String dccBalUsedAmount;

	/**
	 * 变动类型 
	 * 0-------现金充值
	 * 1-------预存返还
	 * 2-------赠费
	 * 3-------退费
	 * 4-------积分兑换
	 * 5-------话费支出"
	 * 6-------代收费
	 * 7-------余额失效
	 * 8-------其它支出
	 * 9-------转坏帐
	 */
	private String dccBalUnitTypeId;

	/**
	 * 余额（元）
	 * 本期末余额
	 */
	private String balanceAmount;

	/**
	 * 变动号码 
	 * 号码信息描述
	 */
	private String dccCounts;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDccStateDate() {
		return dccStateDate;
	}

	public void setDccStateDate(String dccStateDate) {
		this.dccStateDate = dccStateDate;
	}

	public String getAocUnit() {
		return aocUnit;
	}

	public void setAocUnit(String aocUnit) {
		this.aocUnit = aocUnit;
	}

	public String getDccPaymentAmount() {
		return dccPaymentAmount;
	}

	public void setDccPaymentAmount(String dccPaymentAmount) {
		this.dccPaymentAmount = dccPaymentAmount;
	}

	public String getDccBalUsedAmount() {
		return dccBalUsedAmount;
	}

	public void setDccBalUsedAmount(String dccBalUsedAmount) {
		this.dccBalUsedAmount = dccBalUsedAmount;
	}

	public String getDccBalUnitTypeId() {
		return dccBalUnitTypeId;
	}

	public void setDccBalUnitTypeId(String dccBalUnitTypeId) {
		this.dccBalUnitTypeId = dccBalUnitTypeId;
	}

	public String getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(String balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public String getDccCounts() {
		return dccCounts;
	}

	public void setDccCounts(String dccCounts) {
		this.dccCounts = dccCounts;
	}

	@Override
	public String toString() {
		return "TelecomJiangsuBalance [taskid=" + taskid + ", dccStateDate=" + dccStateDate + ", aocUnit=" + aocUnit
				+ ", dccPaymentAmount=" + dccPaymentAmount + ", dccBalUsedAmount=" + dccBalUsedAmount
				+ ", dccBalUnitTypeId=" + dccBalUnitTypeId + ", balanceAmount=" + balanceAmount + ", dccCounts="
				+ dccCounts + "]";
	}

	public TelecomJiangsuBalance(String taskid, String dccStateDate, String aocUnit, String dccPaymentAmount,
			String dccBalUsedAmount, String dccBalUnitTypeId, String balanceAmount, String dccCounts) {
		super();
		this.taskid = taskid;
		this.dccStateDate = dccStateDate;
		this.aocUnit = aocUnit;
		this.dccPaymentAmount = dccPaymentAmount;
		this.dccBalUsedAmount = dccBalUsedAmount;
		this.dccBalUnitTypeId = dccBalUnitTypeId;
		this.balanceAmount = balanceAmount;
		this.dccCounts = dccCounts;
	}

	public TelecomJiangsuBalance() {
		super();
		// TODO Auto-generated constructor stub
	}


}