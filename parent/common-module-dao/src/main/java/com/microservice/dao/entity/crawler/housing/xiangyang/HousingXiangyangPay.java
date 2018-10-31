package com.microservice.dao.entity.crawler.housing.xiangyang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * 缴存明细
 * @author tz
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_xiangyang_pay" ,indexes = {@Index(name = "index_housing_xiangyang_pay_taskid", columnList = "taskid")})
public class HousingXiangyangPay extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;
	
	/**
	 * 记账日期
	 */
	private String jzrq;

	/**
	 * 归集和提取业务类型
	 */
	private String gjhtqywlx;

	/**
	 * 发生额
	 */
	private String fse;

	/**
	 * 发生利息额
	 */
	private String fslxe;

	/**
	 * 个人余额
	 */
	private String grzhye;
	

	/**
	 * 提取原因
	 */
	private String tqyy;
	
	/**
	 * 提取方式
	 */
	private String tqfs;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getJzrq() {
		return jzrq;
	}

	public void setJzrq(String jzrq) {
		this.jzrq = jzrq;
	}

	public String getGjhtqywlx() {
		return gjhtqywlx;
	}

	public void setGjhtqywlx(String gjhtqywlx) {
		this.gjhtqywlx = gjhtqywlx;
	}

	public String getFse() {
		return fse;
	}

	public void setFse(String fse) {
		this.fse = fse;
	}

	public String getFslxe() {
		return fslxe;
	}

	public void setFslxe(String fslxe) {
		this.fslxe = fslxe;
	}

	public String getGrzhye() {
		return grzhye;
	}

	public void setGrzhye(String grzhye) {
		this.grzhye = grzhye;
	}

	public String getTqyy() {
		return tqyy;
	}

	public void setTqyy(String tqyy) {
		this.tqyy = tqyy;
	}

	public String getTqfs() {
		return tqfs;
	}

	public void setTqfs(String tqfs) {
		this.tqfs = tqfs;
	}

	public HousingXiangyangPay() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HousingXiangyangPay(String taskid, String jzrq, String gjhtqywlx, String fse, String fslxe, String grzhye,
			String tqyy, String tqfs) {
		super();
		this.taskid = taskid;
		this.jzrq = jzrq;
		this.gjhtqywlx = gjhtqywlx;
		this.fse = fse;
		this.fslxe = fslxe;
		this.grzhye = grzhye;
		this.tqyy = tqyy;
		this.tqfs = tqfs;
	}

	

}
