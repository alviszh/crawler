package com.microservice.dao.entity.crawler.standalone.bank.ccbchina;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "js_excel_jianshe_trans_total")
@IdClass(CcbChinaDebitCardBillDetailsId.class)
public class CcbChinaDebitCardBillDetails {

	/**
	 * 【序号】 上传数据库中序号规则是按照顺序增加，且第一个序号由人工填写
	 */
	private String num;

	/**
	 * 交易日期 【存款日期】 1），存款日期为下载日记账中的“交易日期”列
	 */
	private String deal_date;

	/**
	 * 【入账银行】 1），下载的哪个账号的数据，入账银行就填写哪个银行账号， 账号分别为 
	 * 1，6227000016510033763
	 * 2，6214880016868683
	 * 
	 */
	private String account;
	/**
	 * 收入 【金额】 1）金额列为下载数据中的收入金额
	 */
	private String income;
	/**
	 * 对方户名 【存款人】 【实际存管人】列取值逻辑顺序如下： 
	 * 1）、【对方户名】为空，则【实际存管人】保存为“无”；
	 * 2）、【对方户名】前3个字符=“支付宝”，则【实际存管人】 = 【交易地点】去掉“支付宝转账”后的字符串 
	 * 3）、【实际存管人】=【对方户名】
	 */
	private String name;
	/**
	 * 【查账状态】 1），此列为恒定值，填写“未查账”字样
	 */
	private String status;
	/**
	 * 【合同编号】 1），此列无需填写任何，为空值
	 */
	private String contract_num;
	/**
	 * 【查账日期】 1），此列无需填写任何，为空值
	 */
	private String audit_date;
	/**
	 * 【备注】 1），【交易地点】&【对方户名】&【交易时间】
	 */
	private String remark;

	/**
	 * 卡号后四位
	 */
	private String card_no;
	
	/**
	 * 余额
	 */
	private String balance;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date create_time = new Date();

	@Column(length = 50)
	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	@Id
	@Column(length = 50)
	public String getDeal_date() {
		return deal_date;
	}

	public void setDeal_date(String deal_date) {
		this.deal_date = deal_date;
	}

	@Id
	@Column(length = 50)
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Id
	@Column(length = 50)
	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	@Id
	@Column(length = 40)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 20)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(length = 50)
	public String getContract_num() {
		return contract_num;
	}

	public void setContract_num(String contract_num) {
		this.contract_num = contract_num;
	}

	@Column(length = 50)
	public String getAudit_date() {
		return audit_date;
	}

	public void setAudit_date(String audit_date) {
		this.audit_date = audit_date;
	}

	@Id
	@Column(length = 200)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(length = 50)
	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	@Id
	@Column(length = 10)
	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public CcbChinaDebitCardBillDetails(String num, String deal_date, String account, String income, String name,
			String status, String contract_num, String audit_date, String remark, String card_no, String balance) {
		super();
		this.num = num;
		this.deal_date = deal_date;
		this.account = account;
		this.income = income;
		this.name = name;
		this.status = status;
		this.contract_num = contract_num;
		this.audit_date = audit_date;
		this.remark = remark;
		this.card_no = card_no;
		this.balance = balance;
	}

	public CcbChinaDebitCardBillDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
