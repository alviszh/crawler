package com.microservice.dao.entity.crawler.bank.citicchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="citicchina_debitcard_regular",indexes = {@Index(name = "index_citicchina_debitcard_regular_taskid", columnList = "taskid")}) 
public class CiticChinaDebitCardRegular extends IdEntity implements Serializable{

	
	private String type;//类型
	
	private String currency;//币种
	
	private String ratio;//钞/汇
	
	private String time;//期限
	
	private String doRatio;//执行利率
	
	private String startDate;//开户日期 
	
	private String endDate;//到期日期
	
	private  String useFee;//可用余额
	
	private String accountFee;//账户余额
	
	private String status;//状态
	
	private String cardNum;//卡号
	private String taskid;
	@Override
	public String toString() {
		return "CiticChinaDebitCardRegular [type=" + type + ", currency=" + currency + ", ratio=" + ratio + ", time="
				+ time + ", doRatio=" + doRatio + ", startDate=" + startDate + ", endDate=" + endDate + ", useFee="
				+ useFee + ", accountFee=" + accountFee + ", status=" + status + ", cardNum=" + cardNum + ", taskid="
				+ taskid + "]";
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getRatio() {
		return ratio;
	}
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDoRatio() {
		return doRatio;
	}
	public void setDoRatio(String doRatio) {
		this.doRatio = doRatio;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getUseFee() {
		return useFee;
	}
	public void setUseFee(String useFee) {
		this.useFee = useFee;
	}
	public String getAccountFee() {
		return accountFee;
	}
	public void setAccountFee(String accountFee) {
		this.accountFee = accountFee;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	
	
	
	
}
