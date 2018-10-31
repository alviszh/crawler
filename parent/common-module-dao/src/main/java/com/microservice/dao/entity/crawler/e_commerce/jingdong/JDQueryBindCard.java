package com.microservice.dao.entity.crawler.e_commerce.jingdong;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**   
*    
* 项目名称：common-module-dao   
* 类名称：JDQueryBindCard   
* 类描述：   
* 创建人：hyx  
* 创建时间：2017年12月19日 下午1:53:26   
* @version        
*/

@Entity
@Table(name = "e_commerce_jd_querybindcard",indexes = {@Index(name = "index_e_commerce_jd_querybindcard_taskid", columnList = "taskid")})
public class JDQueryBindCard extends IdEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String bank;//银行
	private String tailnum;//尾号
	private String banktye;//卡类型
	
	private String name;//姓名
	private String phone;//手机号
	private String taskid; //唯一标识
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getTailnum() {
		return tailnum;
	}
	public void setTailnum(String tailnum) {
		this.tailnum = tailnum;
	}
	public String getBanktye() {
		return banktye;
	}
	public void setBanktye(String banktye) {
		this.banktye = banktye;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "JDQueryBindCard [bank=" + bank + ", tailnum=" + tailnum + ", banktye=" + banktye + ", name=" + name
				+ ", phone=" + phone + ", taskid=" + taskid + "]";
	}
	
	
	
}
