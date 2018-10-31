/**
  * Copyright 2017 bejson.com 
  */
package com.microservice.dao.entity.crawler.e_commerce.jingdong;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2017-12-15 17:8:22
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

@Entity
@Table(name = "e_commerce_jd_coffers",indexes = {@Index(name = "index_e_commerce_jd_coffers_taskid", columnList = "taskid")})
public class JDCoffers extends IdEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String total;// 总金额
	private String frozen;// 冻结总金额
	private String available;// 可用金额
	private String consumable;
	private String allIncome;// 总收益的累计收益
	private String preIncome;// 推测为总收益的今日收益
	private String baiTiaoFrozenAmt;// 总收益的白条冻结金额
	private String otherFrozenAmt;// 总收益的其他冻结
	private String billFrozenAmt;// 推测为总收益的预约理财冻结金额
	private String consumeTotal;// 零用钱总金额
	private String consumeFrozen;// 零用钱的冻结金额
	private String consumeAvailable;// 理财可用金额
	private String consumeConsumable;
	private String consumeAllIncome;
	private String consumePreIncome;
	private String financeTotal;// 理财总金额
	private String financeFrozen;// 理财冻结总金额
	private String financeAvailable;// 理财可用金额
	private String financeConsumable;
	private String financeAllIncome;// 推测为理财金的累计收益
	private String financePreIncome;// 推测为理财金的今日收益
	private String extendInfo;
	private String consumeBaiTiaoFrozenAmt;// 零用钱白条冻结总金额
	private String consumeOtherFrozenAmt;// 推测为零用钱的其他冻结
	private String consumeBillFrozenAmt;// 推测为零用钱的预约理财冻结金额
	private String financeBillFrozenAmt;// 推测为理财金的预约理财冻结金额
	private String redPackageCollectedAmt;

	private String taskid; // 唯一标识

	public String getTaskid() {
		return taskid;
	}

	
	public String getTotal() {
		return total;
	}


	public void setTotal(String total) {
		this.total = total;
	}


	public String getFrozen() {
		return frozen;
	}


	public void setFrozen(String frozen) {
		this.frozen = frozen;
	}


	public String getAvailable() {
		return available;
	}


	public void setAvailable(String available) {
		this.available = available;
	}


	public String getConsumable() {
		return consumable;
	}


	public void setConsumable(String consumable) {
		this.consumable = consumable;
	}


	public String getAllIncome() {
		return allIncome;
	}


	public void setAllIncome(String allIncome) {
		this.allIncome = allIncome;
	}


	public String getPreIncome() {
		return preIncome;
	}


	public void setPreIncome(String preIncome) {
		this.preIncome = preIncome;
	}


	public String getBaiTiaoFrozenAmt() {
		return baiTiaoFrozenAmt;
	}


	public void setBaiTiaoFrozenAmt(String baiTiaoFrozenAmt) {
		this.baiTiaoFrozenAmt = baiTiaoFrozenAmt;
	}


	public String getOtherFrozenAmt() {
		return otherFrozenAmt;
	}


	public void setOtherFrozenAmt(String otherFrozenAmt) {
		this.otherFrozenAmt = otherFrozenAmt;
	}


	public String getBillFrozenAmt() {
		return billFrozenAmt;
	}


	public void setBillFrozenAmt(String billFrozenAmt) {
		this.billFrozenAmt = billFrozenAmt;
	}


	public String getConsumeTotal() {
		return consumeTotal;
	}


	public void setConsumeTotal(String consumeTotal) {
		this.consumeTotal = consumeTotal;
	}


	public String getConsumeFrozen() {
		return consumeFrozen;
	}


	public void setConsumeFrozen(String consumeFrozen) {
		this.consumeFrozen = consumeFrozen;
	}


	public String getConsumeAvailable() {
		return consumeAvailable;
	}


	public void setConsumeAvailable(String consumeAvailable) {
		this.consumeAvailable = consumeAvailable;
	}


	public String getConsumeConsumable() {
		return consumeConsumable;
	}


	public void setConsumeConsumable(String consumeConsumable) {
		this.consumeConsumable = consumeConsumable;
	}


	public String getConsumeAllIncome() {
		return consumeAllIncome;
	}


	public void setConsumeAllIncome(String consumeAllIncome) {
		this.consumeAllIncome = consumeAllIncome;
	}


	public String getConsumePreIncome() {
		return consumePreIncome;
	}


	public void setConsumePreIncome(String consumePreIncome) {
		this.consumePreIncome = consumePreIncome;
	}


	public String getFinanceTotal() {
		return financeTotal;
	}


	public void setFinanceTotal(String financeTotal) {
		this.financeTotal = financeTotal;
	}


	public String getFinanceFrozen() {
		return financeFrozen;
	}


	public void setFinanceFrozen(String financeFrozen) {
		this.financeFrozen = financeFrozen;
	}


	public String getFinanceAvailable() {
		return financeAvailable;
	}


	public void setFinanceAvailable(String financeAvailable) {
		this.financeAvailable = financeAvailable;
	}


	public String getFinanceConsumable() {
		return financeConsumable;
	}


	public void setFinanceConsumable(String financeConsumable) {
		this.financeConsumable = financeConsumable;
	}


	public String getFinanceAllIncome() {
		return financeAllIncome;
	}


	public void setFinanceAllIncome(String financeAllIncome) {
		this.financeAllIncome = financeAllIncome;
	}


	public String getFinancePreIncome() {
		return financePreIncome;
	}


	public void setFinancePreIncome(String financePreIncome) {
		this.financePreIncome = financePreIncome;
	}


	public String getExtendInfo() {
		return extendInfo;
	}


	public void setExtendInfo(String extendInfo) {
		this.extendInfo = extendInfo;
	}


	public String getConsumeBaiTiaoFrozenAmt() {
		return consumeBaiTiaoFrozenAmt;
	}


	public void setConsumeBaiTiaoFrozenAmt(String consumeBaiTiaoFrozenAmt) {
		this.consumeBaiTiaoFrozenAmt = consumeBaiTiaoFrozenAmt;
	}


	public String getConsumeOtherFrozenAmt() {
		return consumeOtherFrozenAmt;
	}


	public void setConsumeOtherFrozenAmt(String consumeOtherFrozenAmt) {
		this.consumeOtherFrozenAmt = consumeOtherFrozenAmt;
	}


	public String getConsumeBillFrozenAmt() {
		return consumeBillFrozenAmt;
	}


	public void setConsumeBillFrozenAmt(String consumeBillFrozenAmt) {
		this.consumeBillFrozenAmt = consumeBillFrozenAmt;
	}


	public String getFinanceBillFrozenAmt() {
		return financeBillFrozenAmt;
	}


	public void setFinanceBillFrozenAmt(String financeBillFrozenAmt) {
		this.financeBillFrozenAmt = financeBillFrozenAmt;
	}


	public String getRedPackageCollectedAmt() {
		return redPackageCollectedAmt;
	}


	public void setRedPackageCollectedAmt(String redPackageCollectedAmt) {
		this.redPackageCollectedAmt = redPackageCollectedAmt;
	}


	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}


	@Override
	public String toString() {
		return "JDGold [total=" + total + ", frozen=" + frozen + ", available=" + available + ", consumable="
				+ consumable + ", allIncome=" + allIncome + ", preIncome=" + preIncome + ", baiTiaoFrozenAmt="
				+ baiTiaoFrozenAmt + ", otherFrozenAmt=" + otherFrozenAmt + ", billFrozenAmt=" + billFrozenAmt
				+ ", consumeTotal=" + consumeTotal + ", consumeFrozen=" + consumeFrozen + ", consumeAvailable="
				+ consumeAvailable + ", consumeConsumable=" + consumeConsumable + ", consumeAllIncome="
				+ consumeAllIncome + ", consumePreIncome=" + consumePreIncome + ", financeTotal=" + financeTotal
				+ ", financeFrozen=" + financeFrozen + ", financeAvailable=" + financeAvailable + ", financeConsumable="
				+ financeConsumable + ", financeAllIncome=" + financeAllIncome + ", financePreIncome="
				+ financePreIncome + ", extendInfo=" + extendInfo + ", consumeBaiTiaoFrozenAmt="
				+ consumeBaiTiaoFrozenAmt + ", consumeOtherFrozenAmt=" + consumeOtherFrozenAmt
				+ ", consumeBillFrozenAmt=" + consumeBillFrozenAmt + ", financeBillFrozenAmt=" + financeBillFrozenAmt
				+ ", redPackageCollectedAmt=" + redPackageCollectedAmt + ", taskid=" + taskid + "]";
	}

	
}