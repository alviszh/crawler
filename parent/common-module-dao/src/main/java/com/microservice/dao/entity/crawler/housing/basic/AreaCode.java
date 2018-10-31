package com.microservice.dao.entity.crawler.housing.basic;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="area_code")
public class AreaCode {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long regionId;
	
	private String regionCode;
	
	private String regionName;
	
	private String regionNameEn;
	
	private String regionShortnameEn;
	
	private Integer regionLevel;
	
	private Integer regionOrder;
	
	private Integer isInsuranceFinished;				//0:测试，1：开发完成，2：网站维护
	
	private Integer isHousingfundFinished;
	
	private String parentRegionName;
	
	private Integer parentId;
	
	private String regionNameInitial;					//地区首字母
	
	private Integer nodenum;   //节点数
	
	private String taskname;   //任务名
	
	public String getRegionNameInitial() {
		return regionNameInitial;
	}

	public void setRegionNameInitial(String regionNameInitial) {
		this.regionNameInitial = regionNameInitial;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getRegionNameEn() {
		return regionNameEn;
	}

	public void setRegionNameEn(String regionNameEn) {
		this.regionNameEn = regionNameEn;
	}

	public String getRegionShortnameEn() {
		return regionShortnameEn;
	}

	public void setRegionShortnameEn(String regionShortnameEn) {
		this.regionShortnameEn = regionShortnameEn;
	}

	public String getParentRegionName() {
		return parentRegionName;
	}

	public void setParentRegionName(String parentRegionName) {
		this.parentRegionName = parentRegionName;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getRegionLevel() {
		return regionLevel;
	}

	public void setRegionLevel(Integer regionLevel) {
		this.regionLevel = regionLevel;
	}

	public Integer getRegionOrder() {
		return regionOrder;
	}

	public void setRegionOrder(Integer regionOrder) {
		this.regionOrder = regionOrder;
	}

	public Integer getIsInsuranceFinished() {
		return isInsuranceFinished;
	}

	public void setIsInsuranceFinished(Integer isInsuranceFinished) {
		this.isInsuranceFinished = isInsuranceFinished;
	}

	public Integer getIsHousingfundFinished() {
		return isHousingfundFinished;
	}

	public void setIsHousingfundFinished(Integer isHousingfundFinished) {
		this.isHousingfundFinished = isHousingfundFinished;
	}

	public Integer getNodenum() {
		return nodenum;
	}

	public void setNodenum(Integer nodenum) {
		this.nodenum = nodenum;
	}

	public String getTaskname() {
		return taskname;
	}

	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	@Override
	public String toString() {
		return "AreaCode [regionId=" + regionId + ", regionCode=" + regionCode + ", regionName=" + regionName
				+ ", regionNameEn=" + regionNameEn + ", regionShortnameEn=" + regionShortnameEn + ", regionLevel="
				+ regionLevel + ", regionOrder=" + regionOrder + ", isInsuranceFinished=" + isInsuranceFinished
				+ ", isHousingfundFinished=" + isHousingfundFinished + ", parentRegionName=" + parentRegionName
				+ ", parentId=" + parentId + ", regionNameInitial=" + regionNameInitial + ", nodenum=" + nodenum
				+ ", taskname=" + taskname + "]";
	}


	
}
