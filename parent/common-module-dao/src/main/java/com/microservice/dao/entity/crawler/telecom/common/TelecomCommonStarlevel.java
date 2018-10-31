package com.microservice.dao.entity.crawler.telecom.common;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * 山西用户星级服务信息
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_common_starlevel")
public class TelecomCommonStarlevel extends IdEntity {
	
	/**
	 * taskid
	 */
	private String taskid;
	
	/**
	 * 姓名（加了星号）
	 */
	private String custName;
	
	/**
	 * 等级
	 * '0' : '普通用户'
	 * '11': '1星客户',
	 * '12': '2星客户',
	 * '13': '3星客户',
	 * '14': '4星客户',
	 * '15': '5星客户',
	 * '16': '6星客户',
	 * '17': '7星客户'
	 */
	private String membershipLevel;
	
	/**
	 * 成长值
	 * 1星[600,1000)
	 * 2星[1000,1500)
	 * 3星[1500,2300)
	 * 4星[2300,4000)
	 * 5星[4000,6000)
	 * 6星[6000,8000)
	 * 7星8000及以上
	 */
	private String growthpoint;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getMembershipLevel() {
		return membershipLevel;
	}

	public void setMembershipLevel(String membershipLevel) {
		this.membershipLevel = membershipLevel;
	}

	public String getGrowthpoint() {
		return growthpoint;
	}

	public void setGrowthpoint(String growthpoint) {
		this.growthpoint = growthpoint;
	}

	@Override
	public String toString() {
		return "TelecomShanxi1Starlevel [taskid=" + taskid + ", custName=" + custName + ", membershipLevel="
				+ membershipLevel + ", growthpoint=" + growthpoint + "]";
	}
	
	public TelecomCommonStarlevel(String taskid, String custName, String membershipLevel, String growthpoint) {
		super();
		this.taskid = taskid;
		this.custName = custName;
		this.membershipLevel = membershipLevel;
		this.growthpoint = growthpoint;
	}

	public TelecomCommonStarlevel() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}