package com.microservice.dao.entity.crawler.car.insurance.ygbx;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntityAndCookie;

/**
 * 车险-阳光车险-源码表
 * @author zz
 *
 */
@Entity
@Table(name="car_insurance_ygbx_html")
public class CarInsuranceYGbxHtml extends IdEntityAndCookie implements Serializable {
	
	private static final long serialVersionUID = -7601637293254927953L;
	
	private String taskid;//uuid 前端通过uuid访问状态结果
	
	public CarInsuranceYGbxHtml(String taskid, String html) {
		super();
		this.taskid = taskid;
		this.html = html;
	}

	private String html;	//源码

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Column(columnDefinition="text")
	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

}
