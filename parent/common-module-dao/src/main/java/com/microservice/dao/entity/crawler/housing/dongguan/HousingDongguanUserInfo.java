package com.microservice.dao.entity.crawler.housing.dongguan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * 用户信息
 * @author tz
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_dongguan_userinfo" ,indexes = {@Index(name = "index_housing_dongguan_userinfo_taskid", columnList = "taskid")})
public class HousingDongguanUserInfo extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 公积金账号
	 */
	private String psnAcc;

	/**
	 * 姓名
	 */
	private String psnName;

	/**
	 * 证件号码
	 */
	private String certNo;

	/**
	 * 单位名称
	 */
	private String orgName;

	/**
	 * 个人账户状态
	 */
	private String psnAccSt;

	/**
	 * 余额
	 */
	private String bal;

	/**
	 * 上次汇缴年月
	 */
	private String orgEndPayTime;

	/**
	 * 月缴存额
	 */
	private String pay;

	/**
	 * 缴存基数
	 */
	private String originalBase;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPsnAcc() {
		return psnAcc;
	}

	public void setPsnAcc(String psnAcc) {
		this.psnAcc = psnAcc;
	}

	public String getPsnName() {
		return psnName;
	}

	public void setPsnName(String psnName) {
		this.psnName = psnName;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getPsnAccSt() {
		return psnAccSt;
	}

	public void setPsnAccSt(String psnAccSt) {
		this.psnAccSt = psnAccSt;
	}

	public String getBal() {
		return bal;
	}

	public void setBal(String bal) {
		this.bal = bal;
	}

	public String getOrgEndPayTime() {
		return orgEndPayTime;
	}

	public void setOrgEndPayTime(String orgEndPayTime) {
		this.orgEndPayTime = orgEndPayTime;
	}

	public String getPay() {
		return pay;
	}

	public void setPay(String pay) {
		this.pay = pay;
	}

	public String getOriginalBase() {
		return originalBase;
	}

	public void setOriginalBase(String originalBase) {
		this.originalBase = originalBase;
	}

	@Override
	public String toString() {
		return "HousingDongguanUserInfo [taskid=" + taskid + ", psnAcc=" + psnAcc + ", psnName=" + psnName + ", certNo="
				+ certNo + ", orgName=" + orgName + ", psnAccSt=" + psnAccSt + ", bal=" + bal + ", orgEndPayTime="
				+ orgEndPayTime + ", pay=" + pay + ", originalBase=" + originalBase + "]";
	}

	public HousingDongguanUserInfo(String taskid, String psnAcc, String psnName, String certNo, String orgName,
			String psnAccSt, String bal, String orgEndPayTime, String pay, String originalBase) {
		super();
		this.taskid = taskid;
		this.psnAcc = psnAcc;
		this.psnName = psnName;
		this.certNo = certNo;
		this.orgName = orgName;
		this.psnAccSt = psnAccSt;
		this.bal = bal;
		this.orgEndPayTime = orgEndPayTime;
		this.pay = pay;
		this.originalBase = originalBase;
	}

	public HousingDongguanUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

}
