package com.microservice.dao.entity.crawler.honesty.judicialwrit;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.crawler.executor.IdEntityExecutor;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "judicial_writ_list",indexes = {@Index(name = "judicial_writ_list_taskid", columnList = "taskid")})
public class JudicialWritList extends IdEntityExecutor implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String taskid;

	private String gist;//裁判要旨段原文
	
	private String reason;//不公开理由
	
	private String type;//案件类型
	
	private String judicialdate;//裁判日期
	
	private String casename;//案件名称
	
	private String writid;//文书ID
	
	private String judgeprocedure;//审判程序
	
	private String casenum;//案号
	
	private String courtname;//法院名称
	
	private String keyword;//搜索关键词
	
	private String courtid;//法院id
	
	private String casebasic;//案件基本情况段原文
	
	private String fujia;//附加原文
	
	private String prefectural;//法院地市

	private String province;//省份
	
	private String preludetext;//文本首部段落原文
	
	private String courtdistrict;//法院区域
	
	private String county;//法院区县;
	
	private String amendment;//补正文书
	
	private String fulltype;//全文类型
	
	private String lawsuitrecord;//诉讼记录段原文
	
	private String result_of_judgment;//判决结果段原文
	
	private String text;//原文内容
	
	private String htmltext;//原文html
	public String getCourtid() {
		return courtid;
	}

	public void setCourtid(String courtid) {
		this.courtid = courtid;
	}
	@Column(columnDefinition="text")
	public String getCasebasic() {
		return casebasic;
	}

	public void setCasebasic(String casebasic) {
		this.casebasic = casebasic;
	}
	@Column(columnDefinition="text")
	public String getFujia() {
		return fujia;
	}

	public void setFujia(String fujia) {
		this.fujia = fujia;
	}

	public String getPrefectural() {
		return prefectural;
	}

	public void setPrefectural(String prefectural) {
		this.prefectural = prefectural;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	@Column(columnDefinition="text")
	public String getPreludetext() {
		return preludetext;
	}

	public void setPreludetext(String preludetext) {
		this.preludetext = preludetext;
	}

	public String getCourtdistrict() {
		return courtdistrict;
	}

	public void setCourtdistrict(String courtdistrict) {
		this.courtdistrict = courtdistrict;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getAmendment() {
		return amendment;
	}

	public void setAmendment(String amendment) {
		this.amendment = amendment;
	}

	public String getFulltype() {
		return fulltype;
	}

	public void setFulltype(String fulltype) {
		this.fulltype = fulltype;
	}
	@Column(columnDefinition="text")
	public String getLawsuitrecord() {
		return lawsuitrecord;
	}

	public void setLawsuitrecord(String lawsuitrecord) {
		this.lawsuitrecord = lawsuitrecord;
	}
	@Column(columnDefinition="text")
	public String getResult_of_judgment() {
		return result_of_judgment;
	}

	public void setResult_of_judgment(String result_of_judgment) {
		this.result_of_judgment = result_of_judgment;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Column(columnDefinition="text")
	public String getGist() {
		return gist;
	}

	public void setGist(String gist) {
		this.gist = gist;
	}
	@Column(columnDefinition="text")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getJudicialdate() {
		return judicialdate;
	}

	public void setJudicialdate(String judicialdate) {
		this.judicialdate = judicialdate;
	}
	@Column(columnDefinition="text")
	public String getCasename() {
		return casename;
	}

	public void setCasename(String casename) {
		this.casename = casename;
	}

	public String getWritid() {
		return writid;
	}

	public void setWritid(String writid) {
		this.writid = writid;
	}

	public String getJudgeprocedure() {
		return judgeprocedure;
	}

	public void setJudgeprocedure(String judgeprocedure) {
		this.judgeprocedure = judgeprocedure;
	}

	public String getCasenum() {
		return casenum;
	}

	public void setCasenum(String casenum) {
		this.casenum = casenum;
	}

	public String getCourtname() {
		return courtname;
	}

	public void setCourtname(String courtname) {
		this.courtname = courtname;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Column(columnDefinition="text")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	@Column(columnDefinition="text")
	public String getHtmltext() {
		return htmltext;
	}

	public void setHtmltext(String htmltext) {
		this.htmltext = htmltext;
	}

	public JudicialWritList(String taskid, String gist, String reason, String type, String judicialdate,
			String casename, String writid, String judgeprocedure, String casenum, String courtname, String keyword) {
		super();
		this.taskid = taskid;
		this.gist = gist;
		this.reason = reason;
		this.type = type;
		this.judicialdate = judicialdate;
		this.casename = casename;
		this.writid = writid;
		this.judgeprocedure = judgeprocedure;
		this.casenum = casenum;
		this.courtname = courtname;
		this.keyword = keyword;
	}

	public JudicialWritList() {
		super();
	}


	public JudicialWritList(String courtid, String casebasic, String fujia, String prefectural, String province,
			String preludetext, String courtdistrict, String county, String amendment, String fulltype,
			String lawsuitrecord, String result_of_judgment, String text, String htmltext) {
		super();
		this.courtid = courtid;
		this.casebasic = casebasic;
		this.fujia = fujia;
		this.prefectural = prefectural;
		this.province = province;
		this.preludetext = preludetext;
		this.courtdistrict = courtdistrict;
		this.county = county;
		this.amendment = amendment;
		this.fulltype = fulltype;
		this.lawsuitrecord = lawsuitrecord;
		this.result_of_judgment = result_of_judgment;
		this.text = text;
		this.htmltext = htmltext;
	}

	@Override
	public String toString() {
		return "JudicialWritList [taskid=" + taskid + ", gist=" + gist + ", reason=" + reason + ", type=" + type
				+ ", judicialdate=" + judicialdate + ", casename=" + casename + ", writid=" + writid
				+ ", judgeprocedure=" + judgeprocedure + ", casenum=" + casenum + ", courtname=" + courtname
				+ ", keyword=" + keyword + ", courtid=" + courtid + ", casebasic=" + casebasic + ", fujia=" + fujia
				+ ", prefectural=" + prefectural + ", province=" + province + ", preludetext=" + preludetext
				+ ", courtdistrict=" + courtdistrict + ", county=" + county + ", amendment=" + amendment + ", fulltype="
				+ fulltype + ", lawsuitrecord=" + lawsuitrecord + ", result_of_judgment=" + result_of_judgment
				+ ", text=" + text + ", htmltext=" + htmltext + "]";
	}

	
	
	
}
