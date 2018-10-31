package com.microservice.dao.entity.crawler.car.insurance.pingan;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntityAndCookie;
@Entity
@Table(name="car_insurance_pingan_userinfo")
public class PingAnUserInfo extends IdEntityAndCookie implements Serializable{

	private String taskid;
	
	private String theInsured;//被保险人
	
	private String licensePlateNumber;//车牌号
	
	private String applicant;//投保人
	
	private String deadline;//保险期限
	
	private String enginenum;//发动机号
	
	private String framenum;//车架号
	
	private String ratifiedLoadCapacity;//核定载质量
	
	private String seat;//核定座位
	
	private String type;//车辆类型
	
	private String function;//使用性质
	
	private String sfyp;//标准保费
	
	private String state;//保单状态
	
	private String SourceOfBusiness;//业务来源
	
	private String subdivide;//业务来源细分
	
	private String agent;//代理人
	
	private String forwarderCode;//代理人代码
	
	private String payment;//支付方式
	
	private String objectOfPayment;//支付对象
	
	private String amount;//应缴保费金额
	
	private String paid_in;//已缴保费金额
	
	private String arrears;//欠缴保费金额
	
	private String mode;//缴费方式
	
	private String paydate;//缴费日期
	
	private String fudong;//浮动比率
	
	private String chechuan;//车船税打印码
	
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getTheInsured() {
		return theInsured;
	}

	public void setTheInsured(String theInsured) {
		this.theInsured = theInsured;
	}

	public String getLicensePlateNumber() {
		return licensePlateNumber;
	}

	public void setLicensePlateNumber(String licensePlateNumber) {
		this.licensePlateNumber = licensePlateNumber;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getEnginenum() {
		return enginenum;
	}

	public void setEnginenum(String enginenum) {
		this.enginenum = enginenum;
	}

	public String getFramenum() {
		return framenum;
	}

	public void setFramenum(String framenum) {
		this.framenum = framenum;
	}

	public String getRatifiedLoadCapacity() {
		return ratifiedLoadCapacity;
	}

	public void setRatifiedLoadCapacity(String ratifiedLoadCapacity) {
		this.ratifiedLoadCapacity = ratifiedLoadCapacity;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getSfyp() {
		return sfyp;
	}

	public void setSfyp(String sfyp) {
		this.sfyp = sfyp;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSourceOfBusiness() {
		return SourceOfBusiness;
	}

	public void setSourceOfBusiness(String sourceOfBusiness) {
		SourceOfBusiness = sourceOfBusiness;
	}

	public String getSubdivide() {
		return subdivide;
	}

	public void setSubdivide(String subdivide) {
		this.subdivide = subdivide;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getForwarderCode() {
		return forwarderCode;
	}

	public void setForwarderCode(String forwarderCode) {
		this.forwarderCode = forwarderCode;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getObjectOfPayment() {
		return objectOfPayment;
	}

	public void setObjectOfPayment(String objectOfPayment) {
		this.objectOfPayment = objectOfPayment;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPaid_in() {
		return paid_in;
	}

	public void setPaid_in(String paid_in) {
		this.paid_in = paid_in;
	}

	public String getArrears() {
		return arrears;
	}

	public void setArrears(String arrears) {
		this.arrears = arrears;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getPaydate() {
		return paydate;
	}

	public void setPaydate(String paydate) {
		this.paydate = paydate;
	}
	@Column(columnDefinition="text")
	public String getFudong() {
		return fudong;
	}

	public void setFudong(String fudong) {
		this.fudong = fudong;
	}
	@Column(columnDefinition="text")
	public String getChechuan() {
		return chechuan;
	}

	public void setChechuan(String chechuan) {
		this.chechuan = chechuan;
	}

	@Override
	public String toString() {
		return "PingAnUserInfo [taskid=" + taskid + ", theInsured=" + theInsured + ", licensePlateNumber="
				+ licensePlateNumber + ", applicant=" + applicant + ", deadline=" + deadline + ", enginenum="
				+ enginenum + ", framenum=" + framenum + ", ratifiedLoadCapacity=" + ratifiedLoadCapacity + ", seat="
				+ seat + ", type=" + type + ", function=" + function + ", sfyp=" + sfyp + ", state=" + state
				+ ", SourceOfBusiness=" + SourceOfBusiness + ", subdivide=" + subdivide + ", agent=" + agent
				+ ", forwarderCode=" + forwarderCode + ", payment=" + payment + ", objectOfPayment=" + objectOfPayment
				+ ", amount=" + amount + ", paid_in=" + paid_in + ", arrears=" + arrears + ", mode=" + mode
				+ ", paydate=" + paydate + ", fudong=" + fudong + ", chechuan=" + chechuan + "]";
	}

	public PingAnUserInfo(String taskid, String theInsured, String licensePlateNumber, String applicant,
			String deadline, String enginenum, String framenum, String ratifiedLoadCapacity, String seat, String type,
			String function, String sfyp, String state, String sourceOfBusiness, String subdivide, String agent,
			String forwarderCode, String payment, String objectOfPayment, String amount, String paid_in, String arrears,
			String mode, String paydate, String fudong, String chechuan) {
		super();
		this.taskid = taskid;
		this.theInsured = theInsured;
		this.licensePlateNumber = licensePlateNumber;
		this.applicant = applicant;
		this.deadline = deadline;
		this.enginenum = enginenum;
		this.framenum = framenum;
		this.ratifiedLoadCapacity = ratifiedLoadCapacity;
		this.seat = seat;
		this.type = type;
		this.function = function;
		this.sfyp = sfyp;
		this.state = state;
		SourceOfBusiness = sourceOfBusiness;
		this.subdivide = subdivide;
		this.agent = agent;
		this.forwarderCode = forwarderCode;
		this.payment = payment;
		this.objectOfPayment = objectOfPayment;
		this.amount = amount;
		this.paid_in = paid_in;
		this.arrears = arrears;
		this.mode = mode;
		this.paydate = paydate;
		this.fudong = fudong;
		this.chechuan = chechuan;
	}

	public PingAnUserInfo() {
		super();
	}
	
	
}
