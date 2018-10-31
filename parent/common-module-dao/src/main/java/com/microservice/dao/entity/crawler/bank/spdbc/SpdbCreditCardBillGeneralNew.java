package com.microservice.dao.entity.crawler.bank.spdbc;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

/**
 * 账单信息概要
 * Created by zmy on 2017/12/5.
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name="spdb_creditcard_newbillgeneral")
public class SpdbCreditCardBillGeneralNew extends IdEntity implements Serializable{
    private static final long serialVersionUID = -7023133529303996560L;

    private String taskid;
    private String billMonth;          //账单月份
    private String lastBackDate;       //到期还款日
    private String accountDay;         //账单日
    private String stmtAmt;            //本期应还款余额
    private String minPay;             //本期最低还款额
    private String lastMoney;          //上期账单金额
    private String backMoney;          //已还款金额/其他入账
    private String newCharges;         //新签金额/其他费用
    private String creditLimit;        //信用卡额度
    private String cashLimit;          //预借现金额度
    private String integral;           //积分
    private String addIntegral;        //新增积分 
    private String adjustmentIntegral; //本期调整积分 
    private String exchangeIntegral;   //本期兑换积分
    
    
    public String getTaskid() {
		return taskid;
	}



	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}



	public String getBillMonth() {
		return billMonth;
	}



	public void setBillMonth(String billMonth) {
		this.billMonth = billMonth;
	}



	public String getLastBackDate() {
		return lastBackDate;
	}



	public void setLastBackDate(String lastBackDate) {
		this.lastBackDate = lastBackDate;
	}



	public String getAccountDay() {
		return accountDay;
	}



	public void setAccountDay(String accountDay) {
		this.accountDay = accountDay;
	}



	public String getStmtAmt() {
		return stmtAmt;
	}



	public void setStmtAmt(String stmtAmt) {
		this.stmtAmt = stmtAmt;
	}



	public String getMinPay() {
		return minPay;
	}



	public void setMinPay(String minPay) {
		this.minPay = minPay;
	}



	public String getBackMoney() {
		return backMoney;
	}



	public void setBackMoney(String backMoney) {
		this.backMoney = backMoney;
	}



	public String getLastMoney() {
		return lastMoney;
	}



	public void setLastMoney(String lastMoney) {
		this.lastMoney = lastMoney;
	}



	public String getCreditLimit() {
		return creditLimit;
	}



	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}



	public String getCashLimit() {
		return cashLimit;
	}



	public void setCashLimit(String cashLimit) {
		this.cashLimit = cashLimit;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public String getNewCharges() {
		return newCharges;
	}



	public void setNewCharges(String newCharges) {
		this.newCharges = newCharges;
	}



	public String getIntegral() {
		return integral;
	}



	public void setIntegral(String integral) {
		this.integral = integral;
	}



	public String getAddIntegral() {
		return addIntegral;
	}



	public void setAddIntegral(String addIntegral) {
		this.addIntegral = addIntegral;
	}



	public String getAdjustmentIntegral() {
		return adjustmentIntegral;
	}



	public void setAdjustmentIntegral(String adjustmentIntegral) {
		this.adjustmentIntegral = adjustmentIntegral;
	}



	public String getExchangeIntegral() {
		return exchangeIntegral;
	}



	public void setExchangeIntegral(String exchangeIntegral) {
		this.exchangeIntegral = exchangeIntegral;
	}



	@Override
    public String toString() {
        return "SpdbCreditCardBillGeneral{" +
                "taskid='" + taskid + '\'' +
                ", billMonth='" + billMonth + '\'' +
                ", lastBackDate='" + lastBackDate + '\'' +
                ", accountDay='" + accountDay + '\'' +
                ", stmtAmt='" + stmtAmt + '\'' +
                ", minPay='" + minPay + '\'' +
                ", backMoney='" + backMoney + '\'' +
                ", lastMoney='" + lastMoney + '\'' +
                ", creditLimit='" + creditLimit + '\'' +
                ", cashLimit='" + cashLimit + '\'' +
                ", newCharges='" + newCharges + '\'' +
                ", integral='" + integral + '\'' +
                ", addIntegral='" + addIntegral + '\'' +
                ", adjustmentIntegral='" + adjustmentIntegral + '\'' +
                ", exchangeIntegral='" + exchangeIntegral + '\'' +
                '}';
    }
}
