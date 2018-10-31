/**
  * Copyright 2017 bejson.com 
  */
package com.microservice.dao.entity.crawler.bank.bocchina;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2017-11-28 16:15:43
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
@Table(name = "bocchina_cebitcard_scoreinfolist",indexes = {@Index(name = "index_bocchina_cebitcard_scoreinfolist_taskid", columnList = "taskid")})
public class BocchinaCebitCardCrcdScoreInfoList extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String bonusId;// 编号
	private String bonusFromLastTerm;// 上月积分余额
	private String currTermTotalBonus;// 本期累计积分
	private String currTermWinBonus;// 本期赢取积分
	private String currTermExchangeBonus;// 本期兑换积分
	private String bonusToNextTerm;// 本期积分余额
	private String deadline;// 积分到期日

	private String cardNo;// 信用卡卡号

	@Override
	public String toString() {
		return "BocchinaCebitCardCrcdScoreInfoList [bonusId=" + bonusId + ", bonusFromLastTerm=" + bonusFromLastTerm
				+ ", currTermTotalBonus=" + currTermTotalBonus + ", currTermWinBonus=" + currTermWinBonus
				+ ", currTermExchangeBonus=" + currTermExchangeBonus + ", bonusToNextTerm=" + bonusToNextTerm
				+ ", deadline=" + deadline + ", cardNo=" + cardNo + ", taskid=" + taskid + "]";
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	private String taskid;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public void setBonusId(String bonusId) {
		this.bonusId = bonusId;
	}

	public String getBonusId() {
		return bonusId;
	}

	public void setBonusFromLastTerm(String bonusFromLastTerm) {
		this.bonusFromLastTerm = bonusFromLastTerm;
	}

	public String getBonusFromLastTerm() {
		return bonusFromLastTerm;
	}

	public void setCurrTermTotalBonus(String currTermTotalBonus) {
		this.currTermTotalBonus = currTermTotalBonus;
	}

	public String getCurrTermTotalBonus() {
		return currTermTotalBonus;
	}

	public void setCurrTermWinBonus(String currTermWinBonus) {
		this.currTermWinBonus = currTermWinBonus;
	}

	public String getCurrTermWinBonus() {
		return currTermWinBonus;
	}

	public void setCurrTermExchangeBonus(String currTermExchangeBonus) {
		this.currTermExchangeBonus = currTermExchangeBonus;
	}

	public String getCurrTermExchangeBonus() {
		return currTermExchangeBonus;
	}

	public void setBonusToNextTerm(String bonusToNextTerm) {
		this.bonusToNextTerm = bonusToNextTerm;
	}

	public String getBonusToNextTerm() {
		return bonusToNextTerm;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getDeadline() {
		return deadline;
	}

}