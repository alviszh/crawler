package com.microservice.dao.entity.crawler.telecom.tianjin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * @Description:  账户信息
 * @author sln
 * @date 2017年9月13日
 */
@Entity
@Table(name = "telecom_tianjin_accountinfo",indexes = {@Index(name = "index_telecom_tianjin_accountinfo_taskid", columnList = "taskid")})
public class TelecomTianjinAccountInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 2633107303204106603L;
	private String taskid;
//	当前星级
	private String starlevel;
//	号码
	private String phonenum;
//	当前可用积分
	private String usefulintegra;
//	年底到期积分
	private String maturityintegra;
//	星级积分倍数
	private String starmultiple;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getStarlevel() {
		return starlevel;
	}
	public void setStarlevel(String starlevel) {
		this.starlevel = starlevel;
	}
	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	public String getUsefulintegra() {
		return usefulintegra;
	}
	public void setUsefulintegra(String usefulintegra) {
		this.usefulintegra = usefulintegra;
	}
	public String getMaturityintegra() {
		return maturityintegra;
	}
	public void setMaturityintegra(String maturityintegra) {
		this.maturityintegra = maturityintegra;
	}
	
	public String getStarmultiple() {
		return starmultiple;
	}
	public void setStarmultiple(String starmultiple) {
		this.starmultiple = starmultiple;
	}
	public TelecomTianjinAccountInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TelecomTianjinAccountInfo(String taskid, String starlevel, String phonenum, String usefulintegra,
			String maturityintegra, String starmultiple) {
		super();
		this.taskid = taskid;
		this.starlevel = starlevel;
		this.phonenum = phonenum;
		this.usefulintegra = usefulintegra;
		this.maturityintegra = maturityintegra;
		this.starmultiple = starmultiple;
	}
	
}
