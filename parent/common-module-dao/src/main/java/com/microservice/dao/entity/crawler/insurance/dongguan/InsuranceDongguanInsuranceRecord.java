package com.microservice.dao.entity.crawler.insurance.dongguan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

/**
 * 东莞社保参保记录
 * @author Administrator
 *
 */
@Entity
@Table(name = "insurance_dongguan_insurancerecord" ,indexes = {@Index(name = "index_insurance_dongguan_insurancerecord_taskid", columnList = "taskid")})
public class InsuranceDongguanInsuranceRecord extends IdEntity{
	
	/**
	 * uuid 前端通过uuid访问状态结果	
	 */
	private String taskid;
	
	/**
	 * 医疗险:医疗险种名称-----其他险：险种类型   
	 */
	private String insuranceType;				
	/**
	 * 医疗险:待遇状态-----其他险：参保状态   
	 */
	private String insuranceState;								
	/**
	 * 医疗险:无-----其他险: 首次参保日期
	 */
	private String firstInsuranceDate;
	/**
	 * 医疗险:无-----其他险: 参保月数
	 */
	private String insuranceMonth;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getInsuranceType() {
		return insuranceType;
	}
	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}
	public String getInsuranceState() {
		return insuranceState;
	}
	public void setInsuranceState(String insuranceState) {
		this.insuranceState = insuranceState;
	}
	public String getFirstInsuranceDate() {
		return firstInsuranceDate;
	}
	public void setFirstInsuranceDate(String firstInsuranceDate) {
		this.firstInsuranceDate = firstInsuranceDate;
	}
	public String getInsuranceMonth() {
		return insuranceMonth;
	}
	public void setInsuranceMonth(String insuranceMonth) {
		this.insuranceMonth = insuranceMonth;
	}
	@Override
	public String toString() {
		return "InsuranceDongguanInsuranceRecord [taskid=" + taskid + ", insuranceType=" + insuranceType
				+ ", insuranceState=" + insuranceState + ", firstInsuranceDate=" + firstInsuranceDate
				+ ", insuranceMonth=" + insuranceMonth + "]";
	}
	public InsuranceDongguanInsuranceRecord(String taskid, String insuranceType, String insuranceState,
			String firstInsuranceDate, String insuranceMonth) {
		super();
		this.taskid = taskid;
		this.insuranceType = insuranceType;
		this.insuranceState = insuranceState;
		this.firstInsuranceDate = firstInsuranceDate;
		this.insuranceMonth = insuranceMonth;
	}
	public InsuranceDongguanInsuranceRecord() {
		super();
		// TODO Auto-generated constructor stub
	}	
	
}
