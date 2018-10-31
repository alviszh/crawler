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
 * @date 2018年9月27日下午5:36:27
 * @Description: 
DROP TABLE IF EXISTS `sms_send`;
CREATE TABLE IF NOT EXISTS `sms_send` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `PortNum` int(11) DEFAULT '-1' COMMENT '大于0表示指定端口号发送',
  `smsNumber` varchar(255) NOT NULL DEFAULT '' COMMENT '接收号码',
  `smsSubject` varchar(255) DEFAULT '' COMMENT '彩信标题，如果发送彩信不能为空',
  `smsContent` varchar(255) NOT NULL DEFAULT '' COMMENT '发送内容',
  `smsType` int(11) unsigned DEFAULT '0' COMMENT '0:短信 1:彩信',
  `PhoNum` varchar(255) DEFAULT NULL COMMENT '手机号',
  `smsState` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '0:未发送 1:已在发送队列 2:发送成功 3:发送失败',
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
 */
@Entity
@Table(name = "sms_send")
public class SmsSend {
	private Integer id;
	private Integer phonum;
	private String portnum;
	private String smscontent;
	private String smsnumber;
	private Integer smsstate;
	private String smssubject;
	private Integer smstype;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPhonum() {
		return phonum;
	}
	public void setPhonum(Integer phonum) {
		this.phonum = phonum;
	}
	public String getPortnum() {
		return portnum;
	}
	public void setPortnum(String portnum) {
		this.portnum = portnum;
	}
	public String getSmscontent() {
		return smscontent;
	}
	public void setSmscontent(String smscontent) {
		this.smscontent = smscontent;
	}
	public String getSmsnumber() {
		return smsnumber;
	}
	public void setSmsnumber(String smsnumber) {
		this.smsnumber = smsnumber;
	}
	public Integer getSmsstate() {
		return smsstate;
	}
	public void setSmsstate(Integer smsstate) {
		this.smsstate = smsstate;
	}
	public String getSmssubject() {
		return smssubject;
	}
	public void setSmssubject(String smssubject) {
		this.smssubject = smssubject;
	}
	public Integer getSmstype() {
		return smstype;
	}
	public void setSmstype(Integer smstype) {
		this.smstype = smstype;
	}
	
	
	
}
