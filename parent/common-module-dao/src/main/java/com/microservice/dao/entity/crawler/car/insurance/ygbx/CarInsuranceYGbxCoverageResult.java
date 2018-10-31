package com.microservice.dao.entity.crawler.car.insurance.ygbx;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntityAndCookie;

/**
 * 车险-阳光保险-保险金额缴费明细表
 * @author zz
 *
 */
@Entity
@Table(name="car_insurance_ygbx_coverage_result")
public class CarInsuranceYGbxCoverageResult extends IdEntityAndCookie implements Serializable {

	private static final long serialVersionUID = -7601637293254927953L;
	
	private String taskid;//uuid 前端通过uuid访问状态结果
	
	private String amountCovered;		//保险金额/责任限额(元)
	
	private String insurance;			//保险费
	
	private String deductibleExcess;	//每次事故绝对免赔额
	
	private String type;				//险种
	
	public CarInsuranceYGbxCoverageResult(String taskid, String amountCovered, String insurance,
			String deductibleExcess, String type) {
		super();
		this.taskid = taskid;
		this.amountCovered = amountCovered;
		this.insurance = insurance;
		this.deductibleExcess = deductibleExcess;
		this.type = type;
	}

	@Override
	public String toString() {
		return "CarInsuranceYGbxCoverageResult [taskid=" + taskid + ", amountCovered=" + amountCovered + ", insurance="
				+ insurance + ", deductibleExcess=" + deductibleExcess + ", type=" + type + "]";
	}

	
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getAmountCovered() {
		return amountCovered;
	}

	public void setAmountCovered(String amountCovered) {
		this.amountCovered = amountCovered;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	public String getDeductibleExcess() {
		return deductibleExcess;
	}

	public void setDeductibleExcess(String deductibleExcess) {
		this.deductibleExcess = deductibleExcess;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
