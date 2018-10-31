package com.microservice.dao.entity.crawler.telecom.jiangsu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 充值缴费记录
 * 
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_jiangsu_pay" ,indexes = {@Index(name = "index_telecom_jiangsu_pay_taskid", columnList = "taskid")})
public class TelecomJiangsuPay extends IdEntity {

	/**
	 * taskid
	 */
	private String taskid;

	/**
	 * 流水号
	 */
	private String dccPaySerialNbr;

	/**
	 * 被充值号码
	 */
	private String dccPaymentNbr;

	/**
	 * 入帐时间
	 */
	private String dccPaidTime;

	/**
	 * 交费渠道
	 * 0-----------营业厅
	 * 1-----------网厅
	 * 2-----------欢GO客户端 
	 * 3-----------翼支付
	 * 4-----------第三方支付 
	 * 5-----------自助缴费 
	 * 6-----------银行 
	 * 7-----------其它
	 */
	private String dccChargeSourceId;

	/**
	 * 交费方式
	 * 11------------现金
	 * 12------------支票
	 * 14------------代缴
	 * 15------------充值
	 * 16------------套餐促销费用
	 * 17------------托收
	 * 18------------空中充值
	 * 19------------银行卡
	 * 20------------充值卡
	*/
	private String dccPaymentMethod;

	/**
	 * 入帐金额（元）
	 */
	private String dccPaymentAmount;

	/**
	 * 使用范围
	 * 0-----------------通用
	 * 1-----------------专用
	 * 2-----------------用户
	 * 3-----------------用户帐目组
	 * 4-----------------帐户帐目组
	 */
	private String tBalanceTypeId;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDccPaySerialNbr() {
		return dccPaySerialNbr;
	}

	public void setDccPaySerialNbr(String dccPaySerialNbr) {
		this.dccPaySerialNbr = dccPaySerialNbr;
	}

	public String getDccPaymentNbr() {
		return dccPaymentNbr;
	}

	public void setDccPaymentNbr(String dccPaymentNbr) {
		this.dccPaymentNbr = dccPaymentNbr;
	}

	public String getDccPaidTime() {
		return dccPaidTime;
	}

	public void setDccPaidTime(String dccPaidTime) {
		this.dccPaidTime = dccPaidTime;
	}

	public String getDccChargeSourceId() {
		return dccChargeSourceId;
	}

	public void setDccChargeSourceId(String dccChargeSourceId) {
		this.dccChargeSourceId = dccChargeSourceId;
	}

	public String getDccPaymentMethod() {
		return dccPaymentMethod;
	}

	public void setDccPaymentMethod(String dccPaymentMethod) {
		this.dccPaymentMethod = dccPaymentMethod;
	}

	public String getDccPaymentAmount() {
		return dccPaymentAmount;
	}

	public void setDccPaymentAmount(String dccPaymentAmount) {
		this.dccPaymentAmount = dccPaymentAmount;
	}

	public String gettBalanceTypeId() {
		return tBalanceTypeId;
	}

	public void settBalanceTypeId(String tBalanceTypeId) {
		this.tBalanceTypeId = tBalanceTypeId;
	}

	@Override
	public String toString() {
		return "TelecomJiangsuPay [taskid=" + taskid + ", dccPaySerialNbr=" + dccPaySerialNbr + ", dccPaymentNbr="
				+ dccPaymentNbr + ", dccPaidTime=" + dccPaidTime + ", dccChargeSourceId=" + dccChargeSourceId
				+ ", dccPaymentMethod=" + dccPaymentMethod + ", dccPaymentAmount=" + dccPaymentAmount
				+ ", tBalanceTypeId=" + tBalanceTypeId + "]";
	}

	public TelecomJiangsuPay(String taskid, String dccPaySerialNbr, String dccPaymentNbr, String dccPaidTime,
			String dccChargeSourceId, String dccPaymentMethod, String dccPaymentAmount, String tBalanceTypeId) {
		super();
		this.taskid = taskid;
		this.dccPaySerialNbr = dccPaySerialNbr;
		this.dccPaymentNbr = dccPaymentNbr;
		this.dccPaidTime = dccPaidTime;
		this.dccChargeSourceId = dccChargeSourceId;
		this.dccPaymentMethod = dccPaymentMethod;
		this.dccPaymentAmount = dccPaymentAmount;
		this.tBalanceTypeId = tBalanceTypeId;
	}

	public TelecomJiangsuPay() {
		super();
		// TODO Auto-generated constructor stub
	}
	


}