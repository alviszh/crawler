package com.microservice.dao.entity.crawler.bank.spdbc;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 账单分期记录
 * Created by zmy on 2017/12/5.
 */
@Entity
@Table(name="spdb_creditcard_newinstallment")
public class SpdbCreditCardInstallmentNew extends IdEntity implements Serializable{
    private static final long serialVersionUID = -39073961778943057L;

    private String taskid;
    private String tradingTime;           //交易时间
    private String dateTime;              //记账日期
    private String abstracts;             //交易摘要
    private String installmentNum;        //总分期数
    private String current;               //当前分期数
    private String money;                 //交易金额
    
    

    
    public String getTaskid() {
		return taskid;
	}




	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}




	public String getTradingTime() {
		return tradingTime;
	}




	public void setTradingTime(String tradingTime) {
		this.tradingTime = tradingTime;
	}




	public String getDateTime() {
		return dateTime;
	}




	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}




	public String getAbstracts() {
		return abstracts;
	}




	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}




	public String getInstallmentNum() {
		return installmentNum;
	}




	public void setInstallmentNum(String installmentNum) {
		this.installmentNum = installmentNum;
	}




	public String getCurrent() {
		return current;
	}




	public void setCurrent(String current) {
		this.current = current;
	}




	public String getMoney() {
		return money;
	}




	public void setMoney(String money) {
		this.money = money;
	}




	public static long getSerialversionuid() {
		return serialVersionUID;
	}




	@Override
    public String toString() {
        return "SpdbCreditCardInstallment{" +
                "taskid='" + taskid + '\'' +
                ", tradingTime='" + tradingTime + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", money='" + money + '\'' +
                ", installmentNum='" + installmentNum + '\'' +
                ", abstracts='" + abstracts + '\'' +
                ", current='" + current + '\'' +
                '}';
    }
}
