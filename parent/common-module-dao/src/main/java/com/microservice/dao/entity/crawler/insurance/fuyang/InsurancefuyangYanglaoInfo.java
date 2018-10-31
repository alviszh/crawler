package com.microservice.dao.entity.crawler.insurance.fuyang;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 德州社保 养老信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="insurance_fuyang_yanglao_info")
public class InsurancefuyangYanglaoInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;

	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 记帐年月 */
	@Column(name="jzny")
	private String jzny;
	
	/** 对应费款所属期*/
	@Column(name="dyfkssq")
	private String dyfkssq;
	
	/** 缴费基数*/
	@Column(name="jfjs")
	private String jfjs;
	
	/** 个人缴纳划入账户金额*/
	@Column(name="grjnhrzhye")
	private String grjnhrzhye;
	
	/** 单位划拨金额*/
	@Column(name="dwhbje")
	private String dwhbje;
	
	/**注销标志*/
	@Column(name="zxbz")
	private String zxbz;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getJzny() {
		return jzny;
	}

	public void setJzny(String jzny) {
		this.jzny = jzny;
	}

	public String getDyfkssq() {
		return dyfkssq;
	}

	public void setDyfkssq(String dyfkssq) {
		this.dyfkssq = dyfkssq;
	}

	public String getJfjs() {
		return jfjs;
	}

	public void setJfjs(String jfjs) {
		this.jfjs = jfjs;
	}

	public String getGrjnhrzhye() {
		return grjnhrzhye;
	}

	public void setGrjnhrzhye(String grjnhrzhye) {
		this.grjnhrzhye = grjnhrzhye;
	}

	public String getDwhbje() {
		return dwhbje;
	}

	public void setDwhbje(String dwhbje) {
		this.dwhbje = dwhbje;
	}

	public String getZxbz() {
		return zxbz;
	}

	public void setZxbz(String zxbz) {
		this.zxbz = zxbz;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}