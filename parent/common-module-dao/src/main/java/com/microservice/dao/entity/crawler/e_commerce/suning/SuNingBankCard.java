package com.microservice.dao.entity.crawler.e_commerce.suning;

import com.microservice.dao.entity.IdEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "e_commerce_suning_bank_card" ,indexes = {@Index(name = "index_e_commerce_suning_bank_card_taskid", columnList = "taskid")})
public class SuNingBankCard extends IdEntity implements Serializable {

    private String taskid;
    private String cardType;				//绑定卡类型
    private String bindTime;				//开通时间
    private String status;					//当前状态
    private String lastNum;					//尾号
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getBindTime() {
		return bindTime;
	}
	public void setBindTime(String bindTime) {
		this.bindTime = bindTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLastNum() {
		return lastNum;
	}
	public void setLastNum(String lastNum) {
		this.lastNum = lastNum;
	}
	@Override
	public String toString() {
		return "SuNingBankCard [taskid=" + taskid + ", cardType=" + cardType + ", bindTime=" + bindTime + ", status="
				+ status + ", lastNum=" + lastNum + "]";
	}
    
}
