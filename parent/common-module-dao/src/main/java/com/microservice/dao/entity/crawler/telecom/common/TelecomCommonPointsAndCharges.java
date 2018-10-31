package com.microservice.dao.entity.crawler.telecom.common;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_common_pointsandcharges")
public class TelecomCommonPointsAndCharges extends IdEntity {

	private String isMustLoggedFlg;
	private String redirectFlg;
	private String authority;
	private String redirectUrl;
	private String code;
	private String status;
	private String msg;
	private String shopId;
	private String salesProCode;
	private String salesProId;
	private String salesProProvinceCode;
	private String provinceCode;
	private String cityCode;
	private String cityCodeCd;
	private String netProvinceCode;
	private String netCityCode;
	private String orderId;
	private String usertype;
	private String loginUserName;
	private String reWriteCookieShopIdFlg;
	private String queryString;
	private String dsitChannel;
	private String pidType;
	private String dataType;
	private String menuType;
	private String logistics;
	private String month_charge;
	private String my_money;
	private String my189datarebean;
	private String remain_date;
	private String now_date;
	private String myjifen;
	private String iszhilian;
	private String phone;
	private String province;
	private String flowFlag;
	private String userresourcequeryfor189home;
	private String infiniteFlowFlag;
	private String multipartRequestHandler;

	private Integer userid;

	private String taskid;

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

	public void setIsMustLoggedFlg(String isMustLoggedFlg) {
		this.isMustLoggedFlg = isMustLoggedFlg;
	}

	public String getIsMustLoggedFlg() {
		return isMustLoggedFlg;
	}

	public void setRedirectFlg(String redirectFlg) {
		this.redirectFlg = redirectFlg;
	}

	public String getRedirectFlg() {
		return redirectFlg;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getAuthority() {
		return authority;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopId() {
		return shopId;
	}

	public void setSalesProCode(String salesProCode) {
		this.salesProCode = salesProCode;
	}

	public String getSalesProCode() {
		return salesProCode;
	}

	public void setSalesProId(String salesProId) {
		this.salesProId = salesProId;
	}

	public String getSalesProId() {
		return salesProId;
	}

	public void setSalesProProvinceCode(String salesProProvinceCode) {
		this.salesProProvinceCode = salesProProvinceCode;
	}

	public String getSalesProProvinceCode() {
		return salesProProvinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCodeCd(String cityCodeCd) {
		this.cityCodeCd = cityCodeCd;
	}

	public String getCityCodeCd() {
		return cityCodeCd;
	}

	public void setNetProvinceCode(String netProvinceCode) {
		this.netProvinceCode = netProvinceCode;
	}

	public String getNetProvinceCode() {
		return netProvinceCode;
	}

	public void setNetCityCode(String netCityCode) {
		this.netCityCode = netCityCode;
	}

	public String getNetCityCode() {
		return netCityCode;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}

	public String getLoginUserName() {
		return loginUserName;
	}

	public void setReWriteCookieShopIdFlg(String reWriteCookieShopIdFlg) {
		this.reWriteCookieShopIdFlg = reWriteCookieShopIdFlg;
	}

	public String getReWriteCookieShopIdFlg() {
		return reWriteCookieShopIdFlg;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setDsitChannel(String dsitChannel) {
		this.dsitChannel = dsitChannel;
	}

	public String getDsitChannel() {
		return dsitChannel;
	}

	public void setPidType(String pidType) {
		this.pidType = pidType;
	}

	public String getPidType() {
		return pidType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataType() {
		return dataType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setLogistics(String logistics) {
		this.logistics = logistics;
	}

	public String getLogistics() {
		return logistics;
	}

	public void setMonth_charge(String month_charge) {
		this.month_charge = month_charge;
	}

	public String getMonth_charge() {
		return month_charge;
	}

	public void setMy_money(String my_money) {
		this.my_money = my_money;
	}

	public String getMy_money() {
		return my_money;
	}

	public void setMy189datarebean(String my189datarebean) {
		this.my189datarebean = my189datarebean;
	}

	public String getMy189datarebean() {
		return my189datarebean;
	}

	public void setRemain_date(String remain_date) {
		this.remain_date = remain_date;
	}

	public String getRemain_date() {
		return remain_date;
	}

	public void setNow_date(String now_date) {
		this.now_date = now_date;
	}

	public String getNow_date() {
		return now_date;
	}

	public void setMyjifen(String myjifen) {
		this.myjifen = myjifen;
	}

	public String getMyjifen() {
		return myjifen;
	}

	public void setIszhilian(String iszhilian) {
		this.iszhilian = iszhilian;
	}

	public String getIszhilian() {
		return iszhilian;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProvince() {
		return province;
	}

	public void setFlowFlag(String flowFlag) {
		this.flowFlag = flowFlag;
	}

	public String getFlowFlag() {
		return flowFlag;
	}

	public void setUserresourcequeryfor189home(String userresourcequeryfor189home) {
		this.userresourcequeryfor189home = userresourcequeryfor189home;
	}

	public String getUserresourcequeryfor189home() {
		return userresourcequeryfor189home;
	}

	public void setInfiniteFlowFlag(String infiniteFlowFlag) {
		this.infiniteFlowFlag = infiniteFlowFlag;
	}

	public String getInfiniteFlowFlag() {
		return infiniteFlowFlag;
	}

	public void setMultipartRequestHandler(String multipartRequestHandler) {
		this.multipartRequestHandler = multipartRequestHandler;
	}

	public String getMultipartRequestHandler() {
		return multipartRequestHandler;
	}

	@Override
	public String toString() {
		return "TelecomBeijingPointsAndCharges [isMustLoggedFlg=" + isMustLoggedFlg + ", redirectFlg=" + redirectFlg
				+ ", authority=" + authority + ", redirectUrl=" + redirectUrl + ", code=" + code + ", status=" + status
				+ ", msg=" + msg + ", shopId=" + shopId + ", salesProCode=" + salesProCode + ", salesProId="
				+ salesProId + ", salesProProvinceCode=" + salesProProvinceCode + ", provinceCode=" + provinceCode
				+ ", cityCode=" + cityCode + ", cityCodeCd=" + cityCodeCd + ", netProvinceCode=" + netProvinceCode
				+ ", netCityCode=" + netCityCode + ", orderId=" + orderId + ", usertype=" + usertype
				+ ", loginUserName=" + loginUserName + ", reWriteCookieShopIdFlg=" + reWriteCookieShopIdFlg
				+ ", queryString=" + queryString + ", dsitChannel=" + dsitChannel + ", pidType=" + pidType
				+ ", dataType=" + dataType + ", menuType=" + menuType + ", logistics=" + logistics + ", month_charge="
				+ month_charge + ", my_money=" + my_money + ", my189datarebean=" + my189datarebean + ", remain_date="
				+ remain_date + ", now_date=" + now_date + ", myjifen=" + myjifen + ", iszhilian=" + iszhilian
				+ ", phone=" + phone + ", province=" + province + ", flowFlag=" + flowFlag
				+ ", userresourcequeryfor189home=" + userresourcequeryfor189home + ", infiniteFlowFlag="
				+ infiniteFlowFlag + ", multipartRequestHandler=" + multipartRequestHandler + ", userid=" + userid
				+ ", taskid=" + taskid + "]";
	}

}