package com.microservice.dao.entity.crawler.bank.abcchina;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="abcchinaCredit_transflow",indexes = {@Index(name = "index_abcchinaCredit_transflow_taskid", columnList = "taskid")})
public class AbcChinaCreditTransFlow  extends IdEntity{
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	
	/**交易日期*/ 
	@Column(name="trDate")
	private String trDate ;
	
	/**入账日期*/ 
	@Column(name="postDate")
	private String postDate ;
	
	/**交易摘要*/ 
	@Column(name="trTxt")
	private String trTxt ;
	
	/**交易地点*/ 
	@Column(name="trAddress")
	private String trAddress ;
	
	/**入账金额(元)*/ 
	@Column(name="postAmt")
	private String postAmt ;
	/**入账币种*/ 
	@Column(name="postCod")
	private String postCod ;

	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getTrDate() {
		return trDate;
	}

	public void setTrDate(String trDate) {
		this.trDate = trDate;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public String getTrTxt() {
		return trTxt;
	}

	public void setTrTxt(String trTxt) {
		this.trTxt = trTxt;
	}

	public String getTrAddress() {
		return trAddress;
	}

	public void setTrAddress(String trAddress) {
		this.trAddress = trAddress;
	}

	public String getPostAmt() {
		return postAmt;
	}

	public void setPostAmt(String postAmt) {
		this.postAmt = postAmt;
	}

	public String getPostCod() {
		return postCod;
	}

	public void setPostCod(String postCod) {
		this.postCod = postCod;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
