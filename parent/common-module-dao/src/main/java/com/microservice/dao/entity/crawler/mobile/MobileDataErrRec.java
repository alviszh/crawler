package com.microservice.dao.entity.crawler.mobile;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

/**
 * 运营商数据错误记录表（例：请求失败404，当前月无数据）
 * @author zz
 *
 */
@Entity
@Table(name="mobile_data_error_record")
public class MobileDataErrRec extends IdEntity{
	
	@Override
	public String toString() {
		return "MobileDataErrRec [taskid=" + taskid + ", type=" + type + ", month=" + month + ", carrier=" + carrier
				+ ", city=" + city + ", status=" + status + "]";
	}

	private String taskid;
	
	private String type;		//例：通话记录、短信记录
	
	private String month;		//月份
	
	private String carrier;		//所属运行商

	private String city;		//城市
	
	private String status;		//失败错误类型
	
	private String errorMsg;	//错误描述
	
	private Integer currentPage;	//页数
	
	public MobileDataErrRec(String taskid, String type, String month, String carrier, String city, String status,
			String errorMsg, Integer currentPage) {
		super();
		this.taskid = taskid;
		this.type = type;
		this.month = month;
		this.carrier = carrier;
		this.city = city;
		this.status = status;
		this.errorMsg = errorMsg;
		this.currentPage = currentPage;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
