package com.microservice.dao.entity.crawler.soical.weibo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="social_weiboinfo_keyword")
public class SoicalWeiboInfo_Keyword  extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;
	/** 爬取批次号 */
	@Column(name="uuid")
	private String uuid;
	
	/**etltime */
	@Column(name="etltime")
	private String etltime;
	
	/** 关键字*/
	@Column(name="keyword")
	private String keyword;
	
	/** 频率*/
	@Column(name="frequency")
	private String frequency;
	
	/** 备注*/
	@Column(name="remark")
	private String remark;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getEtltime() {
		return etltime;
	}

	public void setEtltime(String etltime) {
		this.etltime = etltime;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
