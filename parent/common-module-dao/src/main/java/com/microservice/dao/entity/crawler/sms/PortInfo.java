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
 * @date 2018年9月27日下午5:35:38
 * @Description: 
DROP TABLE IF EXISTS `port_info`;
CREATE TABLE IF NOT EXISTS `port_info` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `PortNum` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '端口号',
  `IMSI` varchar(255) NOT NULL DEFAULT '' COMMENT '用户识别码(IMSI)',
  `ICCID` varchar(255) DEFAULT '' COMMENT '卡识别码(ICCID)',
  `PhoNum` varchar(255) DEFAULT NULL COMMENT '手机号',
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

 *
 *   虽然新酷卡数据库字段用的都是大写，但是转成小写，也不影响，经过测试，可以将内容存储到数据表
 *
 */
@Entity
@Table(name = "port_info")
public class PortInfo {
	private Integer id;
	private Integer portnum;
	private String imsi;
	private String iccid;
	private String phonum;
	
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
	public String getPhonum() {
		return phonum;
	}
	public void setPhonum(String phonum) {
		this.phonum = phonum;
	}
	
}
