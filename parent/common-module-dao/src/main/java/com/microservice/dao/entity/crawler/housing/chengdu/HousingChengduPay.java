package com.microservice.dao.entity.crawler.housing.chengdu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * 缴存明细
 * @author Administrator
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_chengdu_pay" ,indexes = {@Index(name = "index_housing_chengdu_pay_taskid", columnList = "taskid")})
public class HousingChengduPay extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 单位名称
	 */
	private String corpname;

	/**
	 * 缴存年月起
	 */
	private String paybmnh;

	/**
	 * 缴存年月止
	 */
	private String payemnh;

	/**
	 * 缴款时间
	 */
	private String acctime;

	/**
	 * 单位缴存额(元)
	 */
	private String corpdepmny;

	/**
	 * 个人缴存额(元)
	 */
	private String perdepmny;

	/**
	 * 合计(元)
	 */
	private String depmny;

	/**
	 * 业务类型 
	 * '1':'汇缴'
	 * '2':'补缴'
	 */
	private String bustype;

	/**
	 * 单位账号
	 */
	private String corpcode;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCorpname() {
		return corpname;
	}

	public void setCorpname(String corpname) {
		this.corpname = corpname;
	}

	public String getPaybmnh() {
		return paybmnh;
	}

	public void setPaybmnh(String paybmnh) {
		this.paybmnh = paybmnh;
	}

	public String getPayemnh() {
		return payemnh;
	}

	public void setPayemnh(String payemnh) {
		this.payemnh = payemnh;
	}

	public String getAcctime() {
		return acctime;
	}

	public void setAcctime(String acctime) {
		this.acctime = acctime;
	}

	public String getCorpdepmny() {
		return corpdepmny;
	}

	public void setCorpdepmny(String corpdepmny) {
		this.corpdepmny = corpdepmny;
	}

	public String getPerdepmny() {
		return perdepmny;
	}

	public void setPerdepmny(String perdepmny) {
		this.perdepmny = perdepmny;
	}

	public String getDepmny() {
		return depmny;
	}

	public void setDepmny(String depmny) {
		this.depmny = depmny;
	}

	public String getBustype() {
		return bustype;
	}

	public void setBustype(String bustype) {
		this.bustype = bustype;
	}

	public String getCorpcode() {
		return corpcode;
	}

	public void setCorpcode(String corpcode) {
		this.corpcode = corpcode;
	}

	@Override
	public String toString() {
		return "HousingChengduPay [taskid=" + taskid + ", corpname=" + corpname + ", paybmnh=" + paybmnh + ", payemnh="
				+ payemnh + ", acctime=" + acctime + ", corpdepmny=" + corpdepmny + ", perdepmny=" + perdepmny
				+ ", depmny=" + depmny + ", bustype=" + bustype + ", corpcode=" + corpcode + "]";
	}

	public HousingChengduPay(String taskid, String corpname, String paybmnh, String payemnh, String acctime,
			String corpdepmny, String perdepmny, String depmny, String bustype, String corpcode) {
		super();
		this.taskid = taskid;
		this.corpname = corpname;
		this.paybmnh = paybmnh;
		this.payemnh = payemnh;
		this.acctime = acctime;
		this.corpdepmny = corpdepmny;
		this.perdepmny = perdepmny;
		this.depmny = depmny;
		this.bustype = bustype;
		this.corpcode = corpcode;
	}

	public HousingChengduPay() {
		super();
		// TODO Auto-generated constructor stub
	}

}
