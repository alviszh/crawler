package com.microservice.dao.entity.crawler.car.insurance.ygbx;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntityAndCookie;

/**
 * 车险-阳光车险-被投保人信息表
 * @author zz
 *
 */
@Entity
@Table(name="car_insurance_ygbx_assured_result")
public class CarInsuranceYGbxAssuredResult extends IdEntityAndCookie implements Serializable {
	
	private static final long serialVersionUID = -7601637293254927953L;
	
	private String taskid;//uuid 前端通过uuid访问状态结果
	
	private String applicant;		//投保人姓名
	
	private String policyNo;	//保险单号
	
	private String theApplicant;	//被投保人姓名
	
	private String idNum;		//身份证号
	
	private String address;		//家庭住址
	
	private String phone;		//联系方式
	
	private String carOwner;	//行驶证车主

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	@Override
	public String toString() {
		return "CarInsuranceYGbxAssuredResult [taskid=" + taskid + ", applicant=" + applicant + ", policyNo=" + policyNo
				+ ", theApplicant=" + theApplicant + ", idNum=" + idNum + ", address=" + address + ", phone=" + phone
				+ ", carOwner=" + carOwner + "]";
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getTheApplicant() {
		return theApplicant;
	}

	public void setTheApplicant(String theApplicant) {
		this.theApplicant = theApplicant;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public CarInsuranceYGbxAssuredResult(String taskid, String applicant, String policyNo, String theApplicant,
			String idNum, String address, String phone, String carOwner) {
		super();
		this.taskid = taskid;
		this.applicant = applicant;
		this.policyNo = policyNo;
		this.theApplicant = theApplicant;
		this.idNum = idNum;
		this.address = address;
		this.phone = phone;
		this.carOwner = carOwner;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCarOwner() {
		return carOwner;
	}

	public void setCarOwner(String carOwner) {
		this.carOwner = carOwner;
	}
	
	
	
	

}
