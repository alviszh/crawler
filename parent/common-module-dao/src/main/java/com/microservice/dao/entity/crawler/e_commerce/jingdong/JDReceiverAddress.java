package com.microservice.dao.entity.crawler.e_commerce.jingdong;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**   
*    
* 项目名称：common-module-dao   
* 类名称：JDShippingAddress   
* 类描述：   
* 创建人：hyx  
* 创建时间：2017年12月14日 下午4:06:47   
* @version        
*/

@Entity
@Table(name = "e_commerce_jd_receiveraddress",indexes = {@Index(name = "index_e_commerce_jd_receiveraddress_taskid", columnList = "taskid")})
public class JDReceiverAddress  extends IdEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name; //姓名
	private String area; //地区
	private String address; //地址
	private String cellphone; //电话
	private String telephone; //手机号
	private String email; //邮箱
	private String taskid; //唯一标识
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Override
	public String toString() {
		return "JDShippingAddress [name=" + name + ", area=" + area + ", address=" + address + ", cellphone="
				+ cellphone + ", telephone=" + telephone + ", email=" + email + ", taskid=" + taskid + "]";
	}
	
	
}
