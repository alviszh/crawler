/**
 * 
 */
package com.microservice.dao.entity.crawler.sms;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author sln
 * @date 2018年9月27日下午5:36:05
 * @Description: 
DROP TABLE IF EXISTS `sms_recv`;
CREATE TABLE IF NOT EXISTS `sms_recv` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `PortNum` int(11) unsigned DEFAULT '0' COMMENT '接收短信的端口号',
  `PhoNum` varchar(255) DEFAULT '' COMMENT '手机号',
  `IMSI` varchar(255) DEFAULT NULL COMMENT '用户识别码(IMSI)',
  `ICCID` varchar(255) DEFAULT NULL COMMENT '卡识别码(ICCID)',
  `smsDate` varchar(255) NOT NULL DEFAULT '' COMMENT '短信日期，注意是smsDate而不是smsData',
  `smsNumber` varchar(255) NOT NULL DEFAULT '' COMMENT '短信号码',
  `smsContent` varchar(255) NOT NULL DEFAULT '' COMMENT '短信内容',
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
 */
@Entity
@Table(name = "sms_recv")
public class SmsRecv {
	private Integer id;
	private Integer portnum;
	private String phonum;
	private String imsi;
	private String iccid;
	private String smsdate;
	private String smsnumber;
	private String smscontent;
	private String taskid;   //监测任务的taskid
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPortnum() {
		return portnum;
	}
	public void setPortnum(Integer portnum) {
		this.portnum = portnum;
	}
	public String getPhonum() {
		return phonum;
	}
	public void setPhonum(String phonum) {
		this.phonum = phonum;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getIccid() {
		return iccid;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	public String getSmsdate() {
		return smsdate;
	}
	public void setSmsdate(String smsdate) {
		this.smsdate = smsdate;
	}
	public String getSmsnumber() {
		return smsnumber;
	}
	public void setSmsnumber(String smsnumber) {
		this.smsnumber = smsnumber;
	}
	public String getSmscontent() {
		return smscontent;
	}
	public void setSmscontent(String smscontent) {
		this.smscontent = smscontent;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "SmsRecv [id=" + id + ", portnum=" + portnum + ", phonum=" + phonum + ", imsi=" + imsi + ", iccid="
				+ iccid + ", smsdate=" + smsdate + ", smsnumber=" + smsnumber + ", smscontent=" + smscontent
				+ ", taskid=" + taskid + "]";
	}
}
