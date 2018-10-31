package com.microservice.dao.entity.crawler.bank.cebchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="cebchina_creditcard_consumptionmonth")
public class CebChinaCreditCardConsumptionMonth extends IdEntity implements Serializable {

	
	private String taskid;
	
	private String tradedate;//交易日期
	
	private String recordeddate;//入账日期
	
	private String card;//卡号后四位
	
	private String explain;//说明
	
	private String money;//交易金额

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getTradedate() {
		return tradedate;
	}

	public void setTradedate(String tradedate) {
		this.tradedate = tradedate;
	}

	public String getRecordeddate() {
		return recordeddate;
	}

	public void setRecordeddate(String recordeddate) {
		this.recordeddate = recordeddate;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public CebChinaCreditCardConsumptionMonth(String taskid, String tradedate, String recordeddate, String card,
			String explain, String money) {
		super();
		this.taskid = taskid;
		this.tradedate = tradedate;
		this.recordeddate = recordeddate;
		this.card = card;
		this.explain = explain;
		this.money = money;
	}

	public CebChinaCreditCardConsumptionMonth() {
		super();
	}

	@Override
	public String toString() {
		return "CebChinaCreditCardConsumptionMonth [taskid=" + taskid + ", tradedate=" + tradedate + ", recordeddate="
				+ recordeddate + ", card=" + card + ", explain=" + explain + ", money=" + money + "]";
	}
	
	
}
