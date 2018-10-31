package com.microservice.dao.entity.crawler.taxation.basic;

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
	
	private Integer isTaxationFinished;
	
	private String parentRegionName;
	
	private Integer parentId;
	
	private String regionNameInitial;					//地区首字母

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

	public Integer getIsTaxationFinished() {
		return isTaxationFinished;
	}

	public void setIsTaxationFinished(Integer isTaxationFinished) {
		this.isTaxationFinished = isTaxationFinished;
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

	public String getRegionNameInitial() {
		return regionNameInitial;
	}

	public void setRegionNameInitial(String regionNameInitial) {
		this.regionNameInitial = regionNameInitial;
	}
	
	

	


}
