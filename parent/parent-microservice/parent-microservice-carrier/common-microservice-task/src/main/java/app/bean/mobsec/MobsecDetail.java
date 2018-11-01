package app.bean.mobsec;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MobsecDetail {
    private List<Area> area;
    private String province; //省份
    private String type;    //类型（domestic）
    private String operator; //运营商
    private String mobile; //手机号

    public List<Area> getArea() {
        return area;
    }

    public void setArea(List<Area> area) {
        this.area = area;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "MobsecDetail{" +
                "area=" + area +
                ", province='" + province + '\'' +
                ", type='" + type + '\'' +
                ", operator='" + operator + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
