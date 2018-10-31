package com.microservice.dao.entity.crawler.telecom.shanghai;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 上海电信-缴费信息
 * @author zz
 *
 */
@Entity
@Table(name="telecom_shanghai_payfee")
public class TelecomShanghaiPayfee extends IdEntity{
	
	private String taskid;
	private String acctNo;
	private String amount;
	private String bankTempSeq;				//流水号
	private String billCycle;				
	private String devNo;					//手机号
	private String officeName;				
	private String operationType;
	private String partnerTransDate;		//充值时间
	private String queType;					//充值类型
	private String reserve1;
	private String reserve2;
	private String storeInAmount;			//充值金额
	
	@Override
	public String toString() {
		return "TelecomShanghaiPayfee [taskid=" + taskid + ", acctNo=" + acctNo + ", amount=" + amount
				+ ", bankTempSeq=" + bankTempSeq + ", billCycle=" + billCycle + ", devNo=" + devNo + ", officeName="
				+ officeName + ", operationType=" + operationType + ", partnerTransDate=" + partnerTransDate
				+ ", queType=" + queType + ", reserve1=" + reserve1 + ", reserve2=" + reserve2 + ", storeInAmount="
				+ storeInAmount + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getAcctNo() {
		return acctNo;
	}
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getBankTempSeq() {
		return bankTempSeq;
	}
	public void setBankTempSeq(String bankTempSeq) {
		this.bankTempSeq = bankTempSeq;
	}
	public String getBillCycle() {
		return billCycle;
	}
	public void setBillCycle(String billCycle) {
		this.billCycle = billCycle;
	}
	public String getDevNo() {
		return devNo;
	}
	public void setDevNo(String devNo) {
		this.devNo = devNo;
	}
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public String getPartnerTransDate() {
		return partnerTransDate;
	}
	public void setPartnerTransDate(String partnerTransDate) {
		this.partnerTransDate = partnerTransDate;
	}
	public String getQueType() {
		return queType;
	}
	public void setQueType(String queType) {
		this.queType = queType;
	}
	public String getReserve1() {
		return reserve1;
	}
	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}
	public String getReserve2() {
		return reserve2;
	}
	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}
	public String getStoreInAmount() {
		return storeInAmount;
	}
	public void setStoreInAmount(String storeInAmount) {
		this.storeInAmount = storeInAmount;
	}

}
