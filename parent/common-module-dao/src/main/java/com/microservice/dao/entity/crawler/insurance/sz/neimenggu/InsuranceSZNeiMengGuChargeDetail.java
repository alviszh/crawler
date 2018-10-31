package com.microservice.dao.entity.crawler.insurance.sz.neimenggu;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 省直内蒙古 所有的缴费信息
 * @author sln
 *
 */
@Entity
@Table(name = "insurance_sz_neimenggu_chargedetail",indexes = {@Index(name = "index_insurance_sz_neimenggu_chargedetail_taskid", columnList = "taskid")})
public class InsuranceSZNeiMengGuChargeDetail extends IdEntity implements Serializable{
	private static final long serialVersionUID = 5273473947864101643L;
	private String taskid;
//	结算期
	private String accountdate;
//	费款所属期
	private String belongdate;
//	险种类型
	private String insurtype;
//	缴费基数
	private String chargebasenum;
//	单位实缴
	private String unitcharge;
//	个人实缴
	private String percharge;
//	账户划拨
	private String accountallocation;
//	参保地点代号
	private String citycode;
//	参保地名称
	private String insurplace;
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getAccountdate() {
		return accountdate;
	}

	public void setAccountdate(String accountdate) {
		this.accountdate = accountdate;
	}

	public String getBelongdate() {
		return belongdate;
	}

	public void setBelongdate(String belongdate) {
		this.belongdate = belongdate;
	}

	public String getInsurtype() {
		return insurtype;
	}

	public void setInsurtype(String insurtype) {
		this.insurtype = insurtype;
	}

	public String getChargebasenum() {
		return chargebasenum;
	}

	public void setChargebasenum(String chargebasenum) {
		this.chargebasenum = chargebasenum;
	}

	public String getUnitcharge() {
		return unitcharge;
	}

	public void setUnitcharge(String unitcharge) {
		this.unitcharge = unitcharge;
	}

	public String getPercharge() {
		return percharge;
	}

	public void setPercharge(String percharge) {
		this.percharge = percharge;
	}

	public String getAccountallocation() {
		return accountallocation;
	}

	public void setAccountallocation(String accountallocation) {
		this.accountallocation = accountallocation;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getInsurplace() {
		return insurplace;
	}

	public void setInsurplace(String insurplace) {
		this.insurplace = insurplace;
	}

	public InsuranceSZNeiMengGuChargeDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
