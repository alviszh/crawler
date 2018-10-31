package com.microservice.dao.entity.crawler.telecom.shanghai;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

/**
 * 上海电信-用户信息表
 * @author zz
 *
 */
@Entity
@Table(name="telecom_shanghai_userinfo")
public class TelecomShanghaiUserInfo extends IdEntity{
	
	private String taskid;
	private String CustGroup;				//客户分组
	private String CustNAME;				//客户名称
	private String CustNumber;	
	private String CustSinceDT;				//注册日期
	private String CustStatus;				//账户当前状态
	private String CustType;				//客户类型
	private String MainEmailAddr;			//邮箱
	private String MainIdenNumber;			//证件号 
	private String MainIdenType;			//证件类型
	private String MainPhNum;				//手机号
	private String PrAddrName;				//地址
	private String PrAddrZipCode;			//邮编
	private String SubBureauName;			//例  ： 非本地
	
	@Override
	public String toString() {
		return "TelecomShanghaiUserInfo [taskid=" + taskid + ", CustGroup=" + CustGroup + ", CustNAME=" + CustNAME
				+ ", CustNumber=" + CustNumber + ", CustSinceDT=" + CustSinceDT + ", CustStatus=" + CustStatus
				+ ", CustType=" + CustType + ", MainEmailAddr=" + MainEmailAddr + ", MainIdenNumber=" + MainIdenNumber
				+ ", MainIdenType=" + MainIdenType + ", MainPhNum=" + MainPhNum + ", PrAddrName=" + PrAddrName
				+ ", PrAddrZipCode=" + PrAddrZipCode + ", SubBureauName=" + SubBureauName + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCustGroup() {
		return CustGroup;
	}
	public void setCustGroup(String custGroup) {
		CustGroup = custGroup;
	}
	public String getCustNAME() {
		return CustNAME;
	}
	public void setCustNAME(String custNAME) {
		CustNAME = custNAME;
	}
	public String getCustNumber() {
		return CustNumber;
	}
	public void setCustNumber(String custNumber) {
		CustNumber = custNumber;
	}
	public String getCustSinceDT() {
		return CustSinceDT;
	}
	public void setCustSinceDT(String custSinceDT) {
		CustSinceDT = custSinceDT;
	}
	public String getCustStatus() {
		return CustStatus;
	}
	public void setCustStatus(String custStatus) {
		CustStatus = custStatus;
	}
	public String getCustType() {
		return CustType;
	}
	public void setCustType(String custType) {
		CustType = custType;
	}
	public String getMainEmailAddr() {
		return MainEmailAddr;
	}
	public void setMainEmailAddr(String mainEmailAddr) {
		MainEmailAddr = mainEmailAddr;
	}
	public String getMainIdenNumber() {
		return MainIdenNumber;
	}
	public void setMainIdenNumber(String mainIdenNumber) {
		MainIdenNumber = mainIdenNumber;
	}
	public String getMainIdenType() {
		return MainIdenType;
	}
	public void setMainIdenType(String mainIdenType) {
		MainIdenType = mainIdenType;
	}
	public String getMainPhNum() {
		return MainPhNum;
	}
	public void setMainPhNum(String mainPhNum) {
		MainPhNum = mainPhNum;
	}
	public String getPrAddrName() {
		return PrAddrName;
	}
	public void setPrAddrName(String prAddrName) {
		PrAddrName = prAddrName;
	}
	public String getPrAddrZipCode() {
		return PrAddrZipCode;
	}
	public void setPrAddrZipCode(String prAddrZipCode) {
		PrAddrZipCode = prAddrZipCode;
	}
	public String getSubBureauName() {
		return SubBureauName;
	}
	public void setSubBureauName(String subBureauName) {
		SubBureauName = subBureauName;
	}
}
