package com.microservice.dao.entity.crawler.telecom.jiangsu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 账单本月合计
 * 
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_jiangsu_billsum" ,indexes = {@Index(name = "index_telecom_jiangsu_billsum_taskid", columnList = "taskid")})
public class TelecomJiangsuBillSum extends IdEntity {

	/**
	 * taskid
	 */
	private String taskid;

	/**
	 * 客户名称
	 */
	private String user_name;
	/**
	 * 帐单周期
	 */
	private String zhangqi;
	/**
	 * 费用提示
	 */
	private String fee_new;
	/**
	 * 本期存入金额
	 */
	private String into_fee;
	/**
	 * 本期返还金额
	 */
	private String re_fee;
	/**
	 * 本期末可用积分
	 */
	private String ben_use_score;
	/**
	 * 上期末可用积分
	 */
	private String last_use_score;
	/**
	 * 当期使用积分
	 */
	private String use_score;
	/**
	 * 本期新增积分
	 */
	private String add_score;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getZhangqi() {
		return zhangqi;
	}

	public void setZhangqi(String zhangqi) {
		this.zhangqi = zhangqi;
	}

	public String getFee_new() {
		return fee_new;
	}

	public void setFee_new(String fee_new) {
		this.fee_new = fee_new;
	}

	public String getInto_fee() {
		return into_fee;
	}

	public void setInto_fee(String into_fee) {
		this.into_fee = into_fee;
	}

	public String getRe_fee() {
		return re_fee;
	}

	public void setRe_fee(String re_fee) {
		this.re_fee = re_fee;
	}

	public String getBen_use_score() {
		return ben_use_score;
	}

	public void setBen_use_score(String ben_use_score) {
		this.ben_use_score = ben_use_score;
	}

	public String getLast_use_score() {
		return last_use_score;
	}

	public void setLast_use_score(String last_use_score) {
		this.last_use_score = last_use_score;
	}

	public String getUse_score() {
		return use_score;
	}

	public void setUse_score(String use_score) {
		this.use_score = use_score;
	}

	public String getAdd_score() {
		return add_score;
	}

	public void setAdd_score(String add_score) {
		this.add_score = add_score;
	}

	public TelecomJiangsuBillSum(String taskid, String user_name, String zhangqi, String fee_new, String into_fee,
			String re_fee, String ben_use_score, String last_use_score, String use_score, String add_score) {
		super();
		this.taskid = taskid;
		this.user_name = user_name;
		this.zhangqi = zhangqi;
		this.fee_new = fee_new;
		this.into_fee = into_fee;
		this.re_fee = re_fee;
		this.ben_use_score = ben_use_score;
		this.last_use_score = last_use_score;
		this.use_score = use_score;
		this.add_score = add_score;
	}

	public TelecomJiangsuBillSum() {
		super();
		// TODO Auto-generated constructor stub
	}

}