package com.microservice.dao.entity.crawler.insurance.nanjing;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 
 * @author sln
 * @date 2018年8月3日下午7:12:58
 * @Description: 该实体类用于存储登陆成功之后获取的参数，参数用于爬取
 */

@Entity
@Table(name = "insurance_nanjing_params",indexes = {@Index(name = "index_insurance_nanjing_params_taskid", columnList = "taskid")})
public class InsuranceNanJingParams extends IdEntity implements Serializable {
	private static final long serialVersionUID = 5879953430328446204L;
	private String taskid;						
	private String token;
	private String ex;  //扩展字段
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getEx() {
		return ex;
	}
	public void setEx(String ex) {
		this.ex = ex;
	}
}
