package com.microservice.dao.entity.crawler.unicom;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "unicom_userinfo",indexes = {@Index(name = "index_unicom_userinfo_taskid", columnList = "taskid")})
public class UnicomUserInfo extends IdEntity {

	private String password;//用户密码 加密

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private String packageName;//套餐名
	private String provincecode;
	private String usernumber;//手机号
	private String expireTime;
	private String nettype;
	private String areaCode;
	private String certnum;
	private String opendate;//开户日期
	private String citycode;
	private String paytype;
	private String productId;
	private String custName;//用户名
	private String brand;
	private String productType;
	private String packageID;
	private String currentID;
	private String customid;
	private String custlvl;//用户星级
	private String loginType;
	private String nickName;//用户名
	private String subscrbstat;//开通或有效期
	private String laststatdate;
	private String brand_name;
	private String is_wo;
	private boolean is_20;
	private boolean is_36;

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getProvincecode() {
		return provincecode;
	}

	public void setProvincecode(String provincecode) {
		this.provincecode = provincecode;
	}

	public String getUsernumber() {
		return usernumber;
	}

	public void setUsernumber(String usernumber) {
		this.usernumber = usernumber;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public String getNettype() {
		return nettype;
	}

	public void setNettype(String nettype) {
		this.nettype = nettype;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getCertnum() {
		return certnum;
	}

	public void setCertnum(String certnum) {
		this.certnum = certnum;
	}

	public String getOpendate() {
		return opendate;
	}

	public void setOpendate(String opendate) {
		this.opendate = opendate;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getPackageID() {
		return packageID;
	}

	public void setPackageID(String packageID) {
		this.packageID = packageID;
	}

	public String getCurrentID() {
		return currentID;
	}

	public void setCurrentID(String currentID) {
		this.currentID = currentID;
	}

	public String getCustomid() {
		return customid;
	}

	public void setCustomid(String customid) {
		this.customid = customid;
	}

	public String getCustlvl() {
		return custlvl;
	}

	public void setCustlvl(String custlvl) {
		this.custlvl = custlvl;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSubscrbstat() {
		return subscrbstat;
	}

	public void setSubscrbstat(String subscrbstat) {
		this.subscrbstat = subscrbstat;
	}

	public String getLaststatdate() {
		return laststatdate;
	}

	public void setLaststatdate(String laststatdate) {
		this.laststatdate = laststatdate;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	public String getIs_wo() {
		return is_wo;
	}

	public void setIs_wo(String is_wo) {
		this.is_wo = is_wo;
	}

	public boolean isIs_20() {
		return is_20;
	}

	public void setIs_20(boolean is_20) {
		this.is_20 = is_20;
	}

	public boolean isIs_36() {
		return is_36;
	}

	public void setIs_36(boolean is_36) {
		this.is_36 = is_36;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	private Integer userid;

	private String taskid;

	@Override
	public String toString() {
		return "UnicomUserInfo [password=" + password + ", packageName=" + packageName + ", provincecode="
				+ provincecode + ", usernumber=" + usernumber + ", expireTime=" + expireTime + ", nettype=" + nettype
				+ ", areaCode=" + areaCode + ", certnum=" + certnum + ", opendate=" + opendate + ", citycode="
				+ citycode + ", paytype=" + paytype + ", productId=" + productId + ", custName=" + custName + ", brand="
				+ brand + ", productType=" + productType + ", packageID=" + packageID + ", currentID=" + currentID
				+ ", customid=" + customid + ", custlvl=" + custlvl + ", loginType=" + loginType + ", nickName="
				+ nickName + ", subscrbstat=" + subscrbstat + ", laststatdate=" + laststatdate + ", brand_name="
				+ brand_name + ", is_wo=" + is_wo + ", is_20=" + is_20 + ", is_36=" + is_36 + ", userid=" + userid
				+ ", taskid=" + taskid + "]";
	}

}