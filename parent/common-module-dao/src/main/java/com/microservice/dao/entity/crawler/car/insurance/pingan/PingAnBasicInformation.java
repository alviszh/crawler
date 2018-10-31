package com.microservice.dao.entity.crawler.car.insurance.pingan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntityAndCookie;

@Entity
@Table(name="car_insurance_pingan_basic_information")
public class PingAnBasicInformation extends IdEntityAndCookie implements Serializable{

	private String taskid;
	
	private String policyNumber;//保单号
	
	private String insuranceInception;//保险起期
	
	private String insuranceEnddate;//保险止期
	
	private String vehicleType;//品牌型号
	
	private String insuranceName;//险种名称
	
	private String ljnmdvVin;//车架号

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getInsuranceInception() {
		return insuranceInception;
	}

	public void setInsuranceInception(String insuranceInception) {
		this.insuranceInception = insuranceInception;
	}

	public String getInsuranceEnddate() {
		return insuranceEnddate;
	}

	public void setInsuranceEnddate(String insuranceEnddate) {
		this.insuranceEnddate = insuranceEnddate;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getInsuranceName() {
		return insuranceName;
	}

	public void setInsuranceName(String insuranceName) {
		this.insuranceName = insuranceName;
	}

	public String getLjnmdvVin() {
		return ljnmdvVin;
	}

	public void setLjnmdvVin(String ljnmdvVin) {
		this.ljnmdvVin = ljnmdvVin;
	}

	@Override
	public String toString() {
		return "PingAnBasicInformation [taskid=" + taskid + ", policyNumber=" + policyNumber + ", insuranceInception="
				+ insuranceInception + ", insuranceEnddate=" + insuranceEnddate + ", vehicleType=" + vehicleType
				+ ", insuranceName=" + insuranceName + ", ljnmdvVin=" + ljnmdvVin + "]";
	}
	
	
	
	
}
