package com.microservice.dao.entity.crawler.insurance.weihai;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_weihai_image_info")
public class InsuranceWeiHaiImageInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 图片流 */
	@Column(name="imageCurrent")
	private String imageCurrent;
	
	/**主界面信息链接中对应的请求参数*/
	@Column(name="requestParameter")
	private String requestParameter;
	
	/** 版本号 */
	@Column(name="appservion")
	private String appservion;

	public String getRequestParameter() {
		return requestParameter;
	}

	public void setRequestParameter(String requestParameter) {
		this.requestParameter = requestParameter;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getImageCurrent() {
		return imageCurrent;
	}

	public void setImageCurrent(String imageCurrent) {
		this.imageCurrent = imageCurrent;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAppservion() {
		return appservion;
	}

	public void setAppservion(String appservion) {
		this.appservion = appservion;
	}

	

}