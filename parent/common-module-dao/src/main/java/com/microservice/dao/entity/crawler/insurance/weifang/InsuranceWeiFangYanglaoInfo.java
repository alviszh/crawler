package com.microservice.dao.entity.crawler.insurance.weifang;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 济南社保 养老信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="insurance_weifang_yanglao_info")
public class InsuranceWeiFangYanglaoInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;

	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 年月 */
	@Column(name="year_month")
	private String yearMonth;
	
	/**单位缴费基数*/
	@Column(name="unint_cardinal")
	private String unint_cardinal;
	
	/**单位缴费额*/
	@Column(name="unint_charge")
	private String unint_charge;
	
	/**个人缴费基数*/
	@Column(name="persion_cardinal")
	private String persion_cardinal;
	
	/**个人缴费额*/
	@Column(name="persion_charge")
	private String persion_charge;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public String getUnint_cardinal() {
		return unint_cardinal;
	}

	public void setUnint_cardinal(String unint_cardinal) {
		this.unint_cardinal = unint_cardinal;
	}

	public String getUnint_charge() {
		return unint_charge;
	}

	public void setUnint_charge(String unint_charge) {
		this.unint_charge = unint_charge;
	}

	public String getPersion_cardinal() {
		return persion_cardinal;
	}

	public void setPersion_cardinal(String persion_cardinal) {
		this.persion_cardinal = persion_cardinal;
	}

	public String getPersion_charge() {
		return persion_charge;
	}

	public void setPersion_charge(String persion_charge) {
		this.persion_charge = persion_charge;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}