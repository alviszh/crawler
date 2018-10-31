package com.microservice.dao.entity.crawler.bank.spdbc;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 浦发银行信用卡
 * 账户基本信息
 * Created by zmy on 2017/12/5.
 */
@Entity
@Table(name="spdb_creditcard_newgeneralinfo")
public class SpdbCreditCardGeneralInfoNew extends IdEntity implements Serializable{
    private static final long serialVersionUID = 9132819721834208833L;

    private String taskid;
    private String name;              //账户姓名 
    private String account;           //账户
    private String cardNbr;           //卡号
    private String maFlag;            //银行卡类型
    private String credit;            //最高信用额度
    private String creditLimit;       //日常信用额度
    private String essayLimit;        //取现额度

    

    public String getTaskid() {
		return taskid;
	}



	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getAccount() {
		return account;
	}



	public void setAccount(String account) {
		this.account = account;
	}



	public String getCardNbr() {
		return cardNbr;
	}



	public void setCardNbr(String cardNbr) {
		this.cardNbr = cardNbr;
	}



	public String getMaFlag() {
		return maFlag;
	}



	public void setMaFlag(String maFlag) {
		this.maFlag = maFlag;
	}



	public String getCredit() {
		return credit;
	}



	public void setCredit(String credit) {
		this.credit = credit;
	}



	public String getCreditLimit() {
		return creditLimit;
	}



	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}



	public String getEssayLimit() {
		return essayLimit;
	}



	public void setEssayLimit(String essayLimit) {
		this.essayLimit = essayLimit;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	@Override
    public String toString() {
        return "SpdbCreditCardGeneralInfo{" +
                "taskid='" + taskid + '\'' +
                ", name='" + name + '\'' +
                ", cardNbr='" + cardNbr + '\'' +
                ", account='" + account + '\'' +
                ", creditLimit='" + creditLimit + '\'' +
                ", essayLimit='" + essayLimit + '\'' +
                ", maFlag='" + maFlag + '\'' +
                ", credit='" + credit + '\'' +
                '}';
    }
}
