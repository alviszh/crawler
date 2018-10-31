package com.microservice.dao.entity.crawler.car.insurance;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.microservice.dao.entity.IdEntityAndCookie;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "task_car_insurance" ,indexes = {@Index(name = "index_task_car_insurance_taskid", columnList = "taskid")})
public class TaskCarInsurance extends IdEntityAndCookie implements Serializable {
	
	private static final long serialVersionUID = -7601637293254927953L;

	private String taskid;//uuid 前端通过uuid访问状态结果
	private String phase;//当前步骤
	private String phase_status;//步骤状态
	private String description;
	private String companyName; //保险公司名称
	private Boolean finished;//爬虫任务是否全部完成
	private Date updateTime = new Date();
	private String param;		//爬取中需要的参数
//	public String idnum;		//身份证号
//	public String policyNum;	//保单号
	private Date etltime;
	private Date reportTime;
	private String reportStatus;
	private Date pushtime; //推送前置规则的时间
	private String owner;			//所属公司，例：天曦、汇城、汇金
	private String requestJson;		//请求所需要的参数json
	
	/*public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}

	public String getPolicyNum() {
		return policyNum;
	}

	public void setPolicyNum(String policyNum) {
		this.policyNum = policyNum;
	}
*/
	
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getPhase_status() {
		return phase_status;
	}

	public void setPhase_status(String phase_status) {
		this.phase_status = phase_status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Boolean getFinished() {
		return finished;
	}

	public void setFinished(Boolean finished) {
		this.finished = finished;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public Date getEtltime() {
		return etltime;
	}

	public void setEtltime(Date etltime) {
		this.etltime = etltime;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public String getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}

	public Date getPushtime() {
		return pushtime;
	}

	public void setPushtime(Date pushtime) {
		this.pushtime = pushtime;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getRequestJson() {
		return requestJson;
	}

	public void setRequestJson(String requestJson) {
		this.requestJson = requestJson;
	}

	
	

}
