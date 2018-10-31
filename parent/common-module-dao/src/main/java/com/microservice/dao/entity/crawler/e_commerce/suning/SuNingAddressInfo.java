package com.microservice.dao.entity.crawler.e_commerce.suning;

import com.microservice.dao.entity.IdEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "e_commerce_suning_address_info" ,indexes = {@Index(name = "index_e_commerce_suning_address_info_taskid", columnList = "taskid")})
public class SuNingAddressInfo extends IdEntity implements Serializable {

    private String taskid;
    private String name;			//收货人
    private String telNum;			//联系电话
    private String address;			//收货地址
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTelNum() {
		return telNum;
	}
	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "SuNingAddressInfo [taskid=" + taskid + ", name=" + name + ", telNum=" + telNum + ", address=" + address
				+ "]";
	}
    
}
