/**
  * Copyright 2017 bejson.com 
  */
package com.microservice.dao.entity.crawler.e_commerce.jingdong;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2017-12-13 17:56:55
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

@Entity
@Table(name = "e_commerce_jd_indent",indexes = {@Index(name = "index_e_commerce_jd_indent_taskid", columnList = "taskid")})
public class JDIndent extends IdEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String indentDealtime ;//处理时间
	private String indentNum ;//订单号
	
	private String indentOperate;//说明
	
	private String indentDesignation;//商品名称
	private String indentName;//用户名
	private String indentImgurl;
	private String indentAddress;//订单收货地址
	private String indentPhone;//联系电话
	private String indentMoney;//金额
	private String indentPayStatus;//支付状态
	private String indentStatus;//订单状态
	private String indentJingdou;//京豆
	private String indentOrderwareid;//商品标签分类关联id
		
	public String getIndentOrderwareid() {
		return indentOrderwareid;
	}
	public void setIndentOrderwareid(String indentOrderwareid) {
		this.indentOrderwareid = indentOrderwareid;
	}
	private String taskid; //唯一标识
		
	public String getIndentImgurl() {
		return indentImgurl;
	}
	public void setIndentImgurl(String indentImgurl) {
		this.indentImgurl = indentImgurl;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getIndentDealtime() {
		return indentDealtime;
	}
	public void setIndentDealtime(String indentDealtime) {
		this.indentDealtime = indentDealtime;
	}
	public String getIndentNum() {
		return indentNum;
	}
	public void setIndentNum(String indentNum) {
		this.indentNum = indentNum;
	}
	public String getIndentOperate() {
		return indentOperate;
	}
	public void setIndentOperate(String indentOperate) {
		this.indentOperate = indentOperate;
	}
	
	@Column(columnDefinition="text")
	public String getIndentDesignation() {
		return indentDesignation;
	}
	public void setIndentDesignation(String indentDesignation) {
		this.indentDesignation = indentDesignation;
	}
	public String getIndentName() {
		return indentName;
	}
	public void setIndentName(String indentName) {
		this.indentName = indentName;
	}
	@Column(columnDefinition="text")
	public String getIndentAddress() {
		return indentAddress;
	}
	public void setIndentAddress(String indentAddress) {
		this.indentAddress = indentAddress;
	}
	public String getIndentPhone() {
		return indentPhone;
	}
	public void setIndentPhone(String indentPhone) {
		this.indentPhone = indentPhone;
	}
	public String getIndentMoney() {
		return indentMoney;
	}
	public void setIndentMoney(String indentMoney) {
		this.indentMoney = indentMoney;
	}
	public String getIndentPayStatus() {
		return indentPayStatus;
	}
	public void setIndentPayStatus(String indentPayStatus) {
		this.indentPayStatus = indentPayStatus;
	}
	public String getIndentStatus() {
		return indentStatus;
	}
	public void setIndentStatus(String indentStatus) {
		this.indentStatus = indentStatus;
	}
	public String getIndentJingdou() {
		return indentJingdou;
	}
	public void setIndentJingdou(String indentJingdou) {
		this.indentJingdou = indentJingdou;
	}
	@Override
	public String toString() {
		return "JDIndent [indentDealtime=" + indentDealtime + ", indentNum=" + indentNum + ", indentOperate="
				+ indentOperate + ", indentDesignation=" + indentDesignation + ", indentName=" + indentName
				+ ", indentImgurl=" + indentImgurl + ", indentAddress=" + indentAddress + ", indentPhone=" + indentPhone
				+ ", indentMoney=" + indentMoney + ", indentPayStatus=" + indentPayStatus + ", indentStatus="
				+ indentStatus + ", indentJingdou=" + indentJingdou + ", indentOrderwareid=" + indentOrderwareid
				+ ", taskid=" + taskid + "]";
	}
	
	

}