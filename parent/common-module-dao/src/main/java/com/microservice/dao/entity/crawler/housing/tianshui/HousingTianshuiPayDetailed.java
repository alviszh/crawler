package com.microservice.dao.entity.crawler.housing.tianshui;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 帐户明细查询
 * @author tz
 *
 */
@Entity
@Table(name = "housing_tianshui_paydetailed" ,indexes = {@Index(name = "index_housing_tianshui_paydetailed_taskid", columnList = "taskid")})
public class HousingTianshuiPayDetailed extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;
	
	/**
	 * 单位编号
	 */
	private String corpcode;

	/**
	 * 缴款单位名称
	 */
	private String corpname;

	/**
	 * 交易日期
	 */
	private String acctime;

	/**
	 * 代理机构
	 */
	private String depname;

	/**
	 * 摘要
	 */
	private String remark;

	/**
	 * 发生金额
	 */
	private String depbal;
	
	/**
	 * 余额
	 */
	private String accbal;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCorpcode() {
		return corpcode;
	}

	public void setCorpcode(String corpcode) {
		this.corpcode = corpcode;
	}

	public String getCorpname() {
		return corpname;
	}

	public void setCorpname(String corpname) {
		this.corpname = corpname;
	}

	public String getAcctime() {
		return acctime;
	}

	public void setAcctime(String acctime) {
		this.acctime = acctime;
	}

	public String getDepname() {
		return depname;
	}

	public void setDepname(String depname) {
		this.depname = depname;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDepbal() {
		return depbal;
	}

	public void setDepbal(String depbal) {
		this.depbal = depbal;
	}

	public String getAccbal() {
		return accbal;
	}

	public void setAccbal(String accbal) {
		this.accbal = accbal;
	}

	public HousingTianshuiPayDetailed(String taskid, String corpcode, String corpname, String acctime, String depname,
			String remark, String depbal, String accbal) {
		super();
		this.taskid = taskid;
		this.corpcode = corpcode;
		this.corpname = corpname;
		this.acctime = acctime;
		this.depname = depname;
		this.remark = remark;
		this.depbal = depbal;
		this.accbal = accbal;
	}

	public HousingTianshuiPayDetailed() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
