package com.microservice.dao.entity.crawler.e_commerce.jingdong;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**   
*    
* 项目名称：common-module-dao   
* 类名称：JDRenZhengInfo   
* 类描述：   
* 创建人：hyx  
* 创建时间：2017年12月13日 下午4:50:49   
* @version        
*/

@Entity
@Table(name = "e_commerce_jd_renzheng",indexes = {@Index(name = "index_e_commerce_jd_renzheng_taskid", columnList = "taskid")})
public class JDRenZhengInfo extends IdEntity implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name; //姓名
	private String idcard; //身份证
	private String cerdate; //认证时间
	private String sfz; //认证手机号
	private String cerChannels; //认证渠道
	private String hpfs; //金融服务
	private String taskid; //唯一标识

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getCerdate() {
		return cerdate;
	}
	public void setCerdate(String cerdate) {
		this.cerdate = cerdate;
	}
	public String getSfz() {
		return sfz;
	}
	public void setSfz(String sfz) {
		this.sfz = sfz;
	}
	public String getCerChannels() {
		return cerChannels;
	}
	public void setCerChannels(String cerChannels) {
		this.cerChannels = cerChannels;
	}
	public String getHpfs() {
		return hpfs;
	}
	public void setHpfs(String hpfs) {
		this.hpfs = hpfs;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "JDRenZhengInfo [name=" + name + ", idcard=" + idcard + ", cerdate=" + cerdate + ", sfz=" + sfz
				+ ", cerChannels=" + cerChannels + ", hpfs=" + hpfs + ", taskid=" + taskid + "]";
	}
	
	
	
}
