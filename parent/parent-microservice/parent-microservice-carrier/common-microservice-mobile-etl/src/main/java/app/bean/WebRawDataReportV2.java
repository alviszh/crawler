package app.bean;

import java.util.Date;
import java.util.List;

import com.microservice.dao.entity.crawler.mobile.etl.ProMobileBillinfo;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileCallInfo;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobilePayInfo;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileServiceInfo;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileSmsInfo;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileUserInfo;


public class WebRawDataReportV2 {
	
	public RequestParam param;	
	public List<ProMobileUserInfo> proMobileUserInfos;
	public List<ProMobilePayInfo> proMobilePayInfos;
	public List<ProMobileSmsInfo> proMobileSmsInfos;
	public List<ProMobileServiceInfo> proMobileServiceInfos;
	public List<ProMobileBillinfo> proMobileBillinfos;
	public List<ProMobileCallInfo> proMobileCallInfos;
	public String message;
	public Date createtime = new Date();
	public Integer errorCode;
	public String profile;
	
	public RequestParam getParam() {
		return param;
	}
	public void setParam(RequestParam param) {
		this.param = param;
	}

	public List<ProMobileUserInfo> getProMobileUserInfos() {
		return proMobileUserInfos;
	}
	public void setProMobileUserInfos(List<ProMobileUserInfo> proMobileUserInfos) {
		this.proMobileUserInfos = proMobileUserInfos;
	}
	public List<ProMobilePayInfo> getProMobilePayInfos() {
		return proMobilePayInfos;
	}
	public void setProMobilePayInfos(List<ProMobilePayInfo> proMobilePayInfos) {
		this.proMobilePayInfos = proMobilePayInfos;
	}
	public List<ProMobileSmsInfo> getProMobileSmsInfos() {
		return proMobileSmsInfos;
	}
	public void setProMobileSmsInfos(List<ProMobileSmsInfo> proMobileSmsInfos) {
		this.proMobileSmsInfos = proMobileSmsInfos;
	}
	public List<ProMobileServiceInfo> getProMobileServiceInfos() {
		return proMobileServiceInfos;
	}
	public void setProMobileServiceInfos(List<ProMobileServiceInfo> proMobileServiceInfos) {
		this.proMobileServiceInfos = proMobileServiceInfos;
	}
	public List<ProMobileBillinfo> getProMobileBillinfos() {
		return proMobileBillinfos;
	}
	public void setProMobileBillinfos(List<ProMobileBillinfo> proMobileBillinfos) {
		this.proMobileBillinfos = proMobileBillinfos;
	}
	public List<ProMobileCallInfo> getProMobileCallInfos() {
		return proMobileCallInfos;
	}
	public void setProMobileCallInfos(List<ProMobileCallInfo> proMobileCallInfos) {
		this.proMobileCallInfos = proMobileCallInfos;
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
	@Override
	public String toString() {
		return "WebRawDataReportV2 [param=" + param + ", proMobileUserInfos=" + proMobileUserInfos
				+ ", proMobilePayInfos=" + proMobilePayInfos + ", proMobileSmsInfos=" + proMobileSmsInfos
				+ ", proMobileServiceInfos=" + proMobileServiceInfos + ", proMobileBillinfos=" + proMobileBillinfos
				+ ", proMobileCallInfos=" + proMobileCallInfos + ", message=" + message + ", createtime=" + createtime
				+ ", errorCode=" + errorCode + ", profile=" + profile + "]";
	}	
}
