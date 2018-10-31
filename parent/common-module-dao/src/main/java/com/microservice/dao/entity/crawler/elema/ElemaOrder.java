package com.microservice.dao.entity.crawler.elema;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="elema_order",indexes = {@Index(name = "index_elema_order_taskid", columnList = "taskid")})
public class ElemaOrder extends IdEntity{
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	private static final long serialVersionUID = -7225639204374657354L;
	
	/**订单id */
	@Column(name="order_id")
	private String order_id;
	
	/**订单状态 */
	@Column(name="order_status")
	private String order_status;
	
	/**收货地址 */
	@Column(name="address")
	private String address;
	
	/**联系人 */
	@Column(name="consignee")
	private String consignee;
	
	/**联系电话 */
	@Column(name="phone")
	private String phone;
	
	/**订单创建时间 */
	@Column(name="active_at")
	private String active_at;
	
	/**店铺名称 */
	@Column(name="restaurant_name")
	private String restaurant_name;
	
	/**店铺Id */
	@Column(name="restaurant_id")
	private String restaurant_id;
	
	/**总价*/
	@Column(name="total_amount")
	private String total_amount;
	
	/**配送服务公司*/
	@Column(name="delivery_company")
	private String delivery_company;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getActive_at() {
		return active_at;
	}

	public void setActive_at(String active_at) {
		this.active_at = active_at;
	}

	public String getRestaurant_name() {
		return restaurant_name;
	}

	public void setRestaurant_name(String restaurant_name) {
		this.restaurant_name = restaurant_name;
	}

	public String getRestaurant_id() {
		return restaurant_id;
	}

	public void setRestaurant_id(String restaurant_id) {
		this.restaurant_id = restaurant_id;
	}

	public String getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}

	public String getDelivery_company() {
		return delivery_company;
	}

	public void setDelivery_company(String delivery_company) {
		this.delivery_company = delivery_company;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
