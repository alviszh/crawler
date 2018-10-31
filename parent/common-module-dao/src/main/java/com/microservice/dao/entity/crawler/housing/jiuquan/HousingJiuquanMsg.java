package com.microservice.dao.entity.crawler.housing.jiuquan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * 我的消息
 * @author tz
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_jiuquan_msg" ,indexes = {@Index(name = "index_housing_jiuquan_msg_taskid", columnList = "taskid")})
public class HousingJiuquanMsg extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 消息ID
	 */
	private String msgId;

	/**
	 * 业务名称
	 */
	private String ywmc;

	/**
	 * 消息内容
	 */
	private String xxnr;

	/**
	 * 页面展示推送时间
	 */
	private String ago;

	/**
	 * 推送渠道
	 */
	private String blqd;

	/**
	 * 接口json字段，感觉像推送时间
	 */
	private String tssj;

	/**
	 * 不知道啥.....
	 */
	private String xxczy;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getYwmc() {
		return ywmc;
	}

	public void setYwmc(String ywmc) {
		this.ywmc = ywmc;
	}

	public String getXxnr() {
		return xxnr;
	}

	public void setXxnr(String xxnr) {
		this.xxnr = xxnr;
	}

	public String getAgo() {
		return ago;
	}

	public void setAgo(String ago) {
		this.ago = ago;
	}

	public String getBlqd() {
		return blqd;
	}

	public void setBlqd(String blqd) {
		this.blqd = blqd;
	}

	public String getTssj() {
		return tssj;
	}

	public void setTssj(String tssj) {
		this.tssj = tssj;
	}

	public String getXxczy() {
		return xxczy;
	}

	public void setXxczy(String xxczy) {
		this.xxczy = xxczy;
	}

	public HousingJiuquanMsg(String taskid, String msgId, String ywmc, String xxnr, String ago, String blqd,
			String tssj, String xxczy) {
		super();
		this.taskid = taskid;
		this.msgId = msgId;
		this.ywmc = ywmc;
		this.xxnr = xxnr;
		this.ago = ago;
		this.blqd = blqd;
		this.tssj = tssj;
		this.xxczy = xxczy;
	}

	public HousingJiuquanMsg() {
		super();
		// TODO Auto-generated constructor stub
	}

}
