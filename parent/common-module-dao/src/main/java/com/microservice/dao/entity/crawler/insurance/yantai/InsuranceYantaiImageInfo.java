package com.microservice.dao.entity.crawler.insurance.yantai;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 烟台社保 验证码信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="insurance_yantai_image_info")
public class InsuranceYantaiImageInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/**主界面信息链接中对应的请求参数*/
	@Column(name="requestParameter")
	private String requestParameter;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getRequestParameter() {
		return requestParameter;
	}

	public void setRequestParameter(String requestParameter) {
		this.requestParameter = requestParameter;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}