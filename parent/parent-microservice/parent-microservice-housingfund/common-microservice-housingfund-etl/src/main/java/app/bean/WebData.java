package app.bean;

import java.util.Date;
import java.util.List;

import com.microservice.dao.entity.crawler.housing.etl.HousingDetail;
import com.microservice.dao.entity.crawler.housing.etl.HousingUserInfo;



public class WebData {

	public RequestParam param;
	public List<HousingUserInfo> housingUserInfo;
	public List<HousingDetail> housingDetail;
	public String message;
	public Date createtime = new Date();
	public Integer errorCode;
	public String profile;
	@Override
	public String toString() {
		return "WebData [param=" + param + ", housingUserInfo=" + housingUserInfo + ", housingDetail=" + housingDetail
				+ ", message=" + message + ", createtime=" + createtime + ", errorCode=" + errorCode + "]";
	}
	public RequestParam getParam() {
		return param;
	}
	public void setParam(RequestParam param) {
		this.param = param;
	}
	public List<HousingUserInfo> getHousingUserInfo() {
		return housingUserInfo;
	}
	public void setHousingUserInfo(List<HousingUserInfo> housingUserInfo) {
		this.housingUserInfo = housingUserInfo;
	}
	public List<HousingDetail> getHousingDetail() {
		return housingDetail;
	}
	public void setHousingDetail(List<HousingDetail> housingDetail) {
		this.housingDetail = housingDetail;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	
}
