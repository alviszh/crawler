/**
  * Copyright 2017 bejson.com 
  */
package com.microservice.dao.entity.crawler.unicom;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "unicom_integraresult",indexes = {@Index(name = "index_unicom_integraresult_taskid", columnList = "taskid")})
public class UnicomIntegraThemlResult extends IdEntity {

	private Date date;// 日期
	private String type;// 新增
	private String calltype;// 积分类型
	private String integral;// 积分数量

	private String formatscorecreatdate;
	private String formatscoreinvaliddate;
	private String fusionscore;
	
	private String scorecreatdate;
	private String scoreinvaliddate;//积分增长日期
	private String scoretype;//积分增长类型
	private String scorevalue;//积分增长数量

	public String getFormatscorecreatdate() {
		return formatscorecreatdate;
	}

	public void setFormatscorecreatdate(String formatscorecreatdate) {
		this.formatscorecreatdate = formatscorecreatdate;
	}

	public String getFormatscoreinvaliddate() {
		return formatscoreinvaliddate;
	}

	public void setFormatscoreinvaliddate(String formatscoreinvaliddate) {
		this.formatscoreinvaliddate = formatscoreinvaliddate;
	}

	public String getFusionscore() {
		return fusionscore;
	}

	public void setFusionscore(String fusionscore) {
		this.fusionscore = fusionscore;
	}

	public String getScorecreatdate() {
		return scorecreatdate;
	}

	public void setScorecreatdate(String scorecreatdate) {
		this.scorecreatdate = scorecreatdate;
	}

	public String getScoreinvaliddate() {
		return scoreinvaliddate;
	}

	public void setScoreinvaliddate(String scoreinvaliddate) {
		this.scoreinvaliddate = scoreinvaliddate;
	}

	public String getScoretype() {
		return scoretype;
	}

	public void setScoretype(String scoretype) {
		this.scoretype = scoretype;
	}

	public String getScorevalue() {
		return scorevalue;
	}

	public void setScorevalue(String scorevalue) {
		this.scorevalue = scorevalue;
	}

	private Integer userid;

	private String taskid;

	@com.fasterxml.jackson.annotation.JsonIgnore
	private UnicomIntegralTotalResult result;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "integraltegral_id")
	@JsonIgnore
	@com.fasterxml.jackson.annotation.JsonIgnore
	public UnicomIntegralTotalResult getResult() {
		return result;
	}

	public void setResult(UnicomIntegralTotalResult result) {
		this.result = result;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCalltype() {
		return calltype;
	}

	public void setCalltype(String calltype) {
		this.calltype = calltype;
	}

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

}