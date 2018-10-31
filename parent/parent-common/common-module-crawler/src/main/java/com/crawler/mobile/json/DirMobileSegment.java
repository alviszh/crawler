package com.crawler.mobile.json;

import java.io.Serializable;

public class DirMobileSegment implements Serializable{
    private String id;		//id

    private String prefix;		//手机号前7位

    private String province;		//归属地（省份）

    private String city;		//归属地（城市）

    private String catname;		//所属运营商

    private String code;		//地区代码 例如北京 010

    private String unknown;		//（含义未知）

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUnknown() {
        return unknown;
    }

    public void setUnknown(String unknown) {
        this.unknown = unknown;
    }

    @Override
    public String toString() {
        return "DirMobileSegment{" +
                "id='" + id + '\'' +
                ", prefix='" + prefix + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", catname='" + catname + '\'' +
                ", code='" + code + '\'' +
                ", unknown='" + unknown + '\'' +
                '}';
    }
}
