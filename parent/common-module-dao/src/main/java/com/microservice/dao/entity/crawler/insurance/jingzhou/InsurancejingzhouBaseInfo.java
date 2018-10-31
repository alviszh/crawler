package com.microservice.dao.entity.crawler.insurance.jingzhou;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 荆州社保 参保基本信息
 * @author DougeChow
 *
 */
@Entity
@Table(name="insurance_jingzhou_baseinfo")
public class InsurancejingzhouBaseInfo extends InsuranceJingzhouBasicBean implements Serializable {
	
	private static final long serialVersionUID = -7225639204374657354L;

//	"aac004": "1",
	private String aac004;
//			"aac003": "徐超贵",
	private String aac003;
//			"aac006": "1987-08-05 00:00:00",
	private String aac006;
//			"aac005": "01",
	private String aac005;
//			"aac014": null,
	private String aac014;
//			"aac008": "1",
	private String aac008;
//			"sbaac001": "1027344034",
	private String sbaac001;
//			"aac007": "2009-08-10 00:00:00",
	private String aac007;
//			"aac009": "2",
	private String aac009;
//			"aac002_1": "421022198708056610",
	private String aac002_1;
//			"yac005": "D12971407",
	private String yac005;
//			"gaac001": "1000375125",
	private String gaac001;
//			"aae006": "荆州",
	private String aae006;
//			"gaac003": "徐超贵",
	private String gaac003;
//			"aae005": "8254503",
	private String aae005;
//			"gaac002": "421022198708056610",
	private String gaac002;
//			"aac012": "1",
	private String aac012;
//			"aac003_1": "徐超贵",
	private String aac003_1;
//			"aac010": null,
	private String aac010;
//			"aac001": "1000375125",
	private String aac001;
//			"aac011": "31"
	private String aac011;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getAac004() {
		return aac004;
	}

	public void setAac004(String aac004) {
		this.aac004 = aac004;
	}

	public String getAac003() {
		return aac003;
	}

	public void setAac003(String aac003) {
		this.aac003 = aac003;
	}

	public String getAac006() {
		return aac006;
	}

	public void setAac006(String aac006) {
		this.aac006 = aac006;
	}

	public String getAac005() {
		return aac005;
	}

	public void setAac005(String aac005) {
		this.aac005 = aac005;
	}

	public String getAac014() {
		return aac014;
	}

	public void setAac014(String aac014) {
		this.aac014 = aac014;
	}

	public String getAac008() {
		return aac008;
	}

	public void setAac008(String aac008) {
		this.aac008 = aac008;
	}

	public String getSbaac001() {
		return sbaac001;
	}

	public void setSbaac001(String sbaac001) {
		this.sbaac001 = sbaac001;
	}

	public String getAac007() {
		return aac007;
	}

	public void setAac007(String aac007) {
		this.aac007 = aac007;
	}

	public String getAac009() {
		return aac009;
	}

	public void setAac009(String aac009) {
		this.aac009 = aac009;
	}

	public String getAac002_1() {
		return aac002_1;
	}

	public void setAac002_1(String aac002_1) {
		this.aac002_1 = aac002_1;
	}

	public String getYac005() {
		return yac005;
	}

	public void setYac005(String yac005) {
		this.yac005 = yac005;
	}

	public String getGaac001() {
		return gaac001;
	}

	public void setGaac001(String gaac001) {
		this.gaac001 = gaac001;
	}

	public String getAae006() {
		return aae006;
	}

	public void setAae006(String aae006) {
		this.aae006 = aae006;
	}

	public String getGaac003() {
		return gaac003;
	}

	public void setGaac003(String gaac003) {
		this.gaac003 = gaac003;
	}

	public String getAae005() {
		return aae005;
	}

	public void setAae005(String aae005) {
		this.aae005 = aae005;
	}

	public String getGaac002() {
		return gaac002;
	}

	public void setGaac002(String gaac002) {
		this.gaac002 = gaac002;
	}

	public String getAac012() {
		return aac012;
	}

	public void setAac012(String aac012) {
		this.aac012 = aac012;
	}

	public String getAac003_1() {
		return aac003_1;
	}

	public void setAac003_1(String aac003_1) {
		this.aac003_1 = aac003_1;
	}

	public String getAac010() {
		return aac010;
	}

	public void setAac010(String aac010) {
		this.aac010 = aac010;
	}

	public String getAac001() {
		return aac001;
	}

	public void setAac001(String aac001) {
		this.aac001 = aac001;
	}

	public String getAac011() {
		return aac011;
	}

	public void setAac011(String aac011) {
		this.aac011 = aac011;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
}