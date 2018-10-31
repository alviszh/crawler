package com.microservice.dao.entity.crawler.bank.cmbchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 招商银行信用卡积分信息
 * 
 * @author tz
 *
 */
@Entity
@Table(name = "cmbchina_creditcard_integral" ,indexes = {@Index(name = "index_cmbchina_creditcard_integral_taskid", columnList = "taskid")})
public class CmbChinaCreditCardIntegral extends IdEntity implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5736001553548082742L;

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 积分名称
	 */
	private String name;
	/**
	 * 积分管理模式
	 */
	private String model;
	/**
	 * 当期刷卡积分
	 */
	private String payCardIntegral;
	/**
	 * 当期调整积分
	 */
	private String adjustedIntegral;
	/**
	 * 当期奖励积分
	 */
	private String rewardIntegral;
	/**
	 * 当期新增积分
	 */
	private String addIntegral;
	/**
	 * 当期兑换积分
	 */
	private String exchangeIntegral;
	/**
	 * 当前可用积分
	 */
	private String useIntegral;
	/**
	 * 最近即将失效积分
	 */
	private String invalidIntegral;
	/**
	 * 失效日期
	 */
	private String invalidDate;
	
	/**
	 * 年月日期
	 */
	private String yearmonth;

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

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getPayCardIntegral() {
		return payCardIntegral;
	}

	public void setPayCardIntegral(String payCardIntegral) {
		this.payCardIntegral = payCardIntegral;
	}

	public String getAdjustedIntegral() {
		return adjustedIntegral;
	}

	public void setAdjustedIntegral(String adjustedIntegral) {
		this.adjustedIntegral = adjustedIntegral;
	}

	public String getRewardIntegral() {
		return rewardIntegral;
	}

	public void setRewardIntegral(String rewardIntegral) {
		this.rewardIntegral = rewardIntegral;
	}

	public String getAddIntegral() {
		return addIntegral;
	}

	public void setAddIntegral(String addIntegral) {
		this.addIntegral = addIntegral;
	}

	public String getExchangeIntegral() {
		return exchangeIntegral;
	}

	public void setExchangeIntegral(String exchangeIntegral) {
		this.exchangeIntegral = exchangeIntegral;
	}

	public String getUseIntegral() {
		return useIntegral;
	}

	public void setUseIntegral(String useIntegral) {
		this.useIntegral = useIntegral;
	}

	public String getInvalidIntegral() {
		return invalidIntegral;
	}

	public void setInvalidIntegral(String invalidIntegral) {
		this.invalidIntegral = invalidIntegral;
	}

	public String getInvalidDate() {
		return invalidDate;
	}

	public void setInvalidDate(String invalidDate) {
		this.invalidDate = invalidDate;
	}

	public String getYearmonth() {
		return yearmonth;
	}

	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}

	public CmbChinaCreditCardIntegral(String taskid, String name, String model, String payCardIntegral,
			String adjustedIntegral, String rewardIntegral, String addIntegral, String exchangeIntegral,
			String useIntegral, String invalidIntegral, String invalidDate, String yearmonth) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.model = model;
		this.payCardIntegral = payCardIntegral;
		this.adjustedIntegral = adjustedIntegral;
		this.rewardIntegral = rewardIntegral;
		this.addIntegral = addIntegral;
		this.exchangeIntegral = exchangeIntegral;
		this.useIntegral = useIntegral;
		this.invalidIntegral = invalidIntegral;
		this.invalidDate = invalidDate;
		this.yearmonth = yearmonth;
	}

	public CmbChinaCreditCardIntegral() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
}
