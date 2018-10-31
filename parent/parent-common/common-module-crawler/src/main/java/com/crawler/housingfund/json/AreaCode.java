package com.crawler.housingfund.json;

import java.io.Serializable;

public class AreaCode implements Serializable{

    private Long regionId;

    private String regionCode;

    private String regionName;

    private String regionNameEn;

    private String regionShortnameEn;

    private Integer regionLevel;

    private Integer regionOrder;

    private Integer isHousingFundFinished;

    private Integer isHousingfundFinished;

    private String parentRegionName;

    private Integer parentId;

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

    public Integer getIsHousingFundFinished() {
        return isHousingfundFinished;
    }

    public void setIsHousingFundFinished(Integer isHousingfundFinished) {
        this.isHousingfundFinished = isHousingfundFinished;
    }

    public Integer getIsHousingfundFinished() {
        return isHousingfundFinished;
    }

    public void setIsHousingfundFinished(Integer isHousingfundFinished) {
        this.isHousingfundFinished = isHousingfundFinished;
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

    @Override
    public String toString() {
        return "AreaCode{" +
                "regionId=" + regionId +
                ", regionCode='" + regionCode + '\'' +
                ", regionName='" + regionName + '\'' +
                ", regionNameEn='" + regionNameEn + '\'' +
                ", regionShortnameEn='" + regionShortnameEn + '\'' +
                ", regionLevel=" + regionLevel +
                ", regionOrder=" + regionOrder +
                ", isHousingfundFinished=" + isHousingfundFinished +
                ", isHousingfundFinished=" + isHousingfundFinished +
                ", parentRegionName='" + parentRegionName + '\'' +
                ", parentId=" + parentId +
                '}';
    }
}
