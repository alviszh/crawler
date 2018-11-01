package app.bean;

import java.util.Date;
import java.util.List;
import com.microservice.dao.entity.crawler.mobile.etl.MobileAccountInfo;
import com.microservice.dao.entity.crawler.mobile.etl.MobileBillInfoMonth;
import com.microservice.dao.entity.crawler.mobile.etl.MobileBusinessInfo;
import com.microservice.dao.entity.crawler.mobile.etl.MobileCallRecordDetail;
import com.microservice.dao.entity.crawler.mobile.etl.MobileCallRecordStatistics;
import com.microservice.dao.entity.crawler.mobile.etl.MobileFamilyNetInfo;
import com.microservice.dao.entity.crawler.mobile.etl.MobileGroupNetInfo;
import com.microservice.dao.entity.crawler.mobile.etl.MobilePaymentInfo;
import com.microservice.dao.entity.crawler.mobile.etl.MobilePointsInfo;
import com.microservice.dao.entity.crawler.mobile.etl.MobileRelationshipInfo;
import com.microservice.dao.entity.crawler.mobile.etl.MobileSmsRecordDetail;
import com.microservice.dao.entity.crawler.mobile.etl.MobileUserInfo;

public class WebData {
	
	public RequestParam param;
	public List<MobileUserInfo> mobileUserInfos;
	public List<MobileCallRecordDetail> mobileCallRecordDetails;
	public List<MobileCallRecordStatistics> mobileCallRecordStatistics;
	public List<MobileAccountInfo> mobileAccountInfos;
	public List<MobileBillInfoMonth> mobileBillInfoMonths;
	public List<MobileBusinessInfo> mobileBusinessInfos;
	public List<MobileFamilyNetInfo> mobileFamilyNetInfos;
	public List<MobileGroupNetInfo> mobileGroupNetInfos;
	public List<MobilePaymentInfo> mobilePaymentInfos;
	public List<MobilePointsInfo> mobilePointsInfos;
	public List<MobileRelationshipInfo> mobileRelationshipInfos;
	public List<MobileSmsRecordDetail> mobileSmsRecordDetails;
	public String message;
	public Date createtime = new Date();
	public Integer errorCode;
	public String profile;
	
	
	public List<MobileCallRecordDetail> getMobileCallRecordDetails() {
		return mobileCallRecordDetails;
	}
	public void setMobileCallRecordDetails(List<MobileCallRecordDetail> mobileCallRecordDetails) {
		this.mobileCallRecordDetails = mobileCallRecordDetails;
	}	
	public List<MobileCallRecordStatistics> getMobileCallRecordStatistics() {
		return mobileCallRecordStatistics;
	}
	public void setMobileCallRecordStatistics(List<MobileCallRecordStatistics> mobileCallRecordStatistics) {
		this.mobileCallRecordStatistics = mobileCallRecordStatistics;
	}
	
	public List<MobileBillInfoMonth> getMobileBillInfoMonths() {
		return mobileBillInfoMonths;
	}
	public void setMobileBillInfoMonths(List<MobileBillInfoMonth> mobileBillInfoMonths) {
		this.mobileBillInfoMonths = mobileBillInfoMonths;
	}
	
	public List<MobileUserInfo> getMobileUserInfos() {
		return mobileUserInfos;
	}
	public void setMobileUserInfos(List<MobileUserInfo> mobileUserInfos) {
		this.mobileUserInfos = mobileUserInfos;
	}
	public List<MobileAccountInfo> getMobileAccountInfos() {
		return mobileAccountInfos;
	}
	public void setMobileAccountInfos(List<MobileAccountInfo> mobileAccountInfos) {
		this.mobileAccountInfos = mobileAccountInfos;
	}
	public List<MobileBusinessInfo> getMobileBusinessInfos() {
		return mobileBusinessInfos;
	}
	public void setMobileBusinessInfos(List<MobileBusinessInfo> mobileBusinessInfos) {
		this.mobileBusinessInfos = mobileBusinessInfos;
	}
	public List<MobileFamilyNetInfo> getMobileFamilyNetInfos() {
		return mobileFamilyNetInfos;
	}
	public void setMobileFamilyNetInfos(List<MobileFamilyNetInfo> mobileFamilyNetInfos) {
		this.mobileFamilyNetInfos = mobileFamilyNetInfos;
	}
	public List<MobileGroupNetInfo> getMobileGroupNetInfos() {
		return mobileGroupNetInfos;
	}
	public void setMobileGroupNetInfos(List<MobileGroupNetInfo> mobileGroupNetInfos) {
		this.mobileGroupNetInfos = mobileGroupNetInfos;
	}
	public List<MobilePaymentInfo> getMobilePaymentInfos() {
		return mobilePaymentInfos;
	}
	public void setMobilePaymentInfos(List<MobilePaymentInfo> mobilePaymentInfos) {
		this.mobilePaymentInfos = mobilePaymentInfos;
	}
	public List<MobilePointsInfo> getMobilePointsInfos() {
		return mobilePointsInfos;
	}
	public void setMobilePointsInfos(List<MobilePointsInfo> mobilePointsInfos) {
		this.mobilePointsInfos = mobilePointsInfos;
	}
	public List<MobileRelationshipInfo> getMobileRelationshipInfos() {
		return mobileRelationshipInfos;
	}
	public void setMobileRelationshipInfos(List<MobileRelationshipInfo> mobileRelationshipInfos) {
		this.mobileRelationshipInfos = mobileRelationshipInfos;
	}
	public List<MobileSmsRecordDetail> getMobileSmsRecordDetails() {
		return mobileSmsRecordDetails;
	}
	public void setMobileSmsRecordDetails(List<MobileSmsRecordDetail> mobileSmsRecordDetails) {
		this.mobileSmsRecordDetails = mobileSmsRecordDetails;
	}
	
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public RequestParam getParam() {
		return param;
	}
	public void setParam(RequestParam param) {
		this.param = param;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	
	@Override
	public String toString() {
		return "WebData [param=" + param + ", mobileUserInfos=" + mobileUserInfos + ", mobileCallRecordDetails="
				+ mobileCallRecordDetails + ", mobileCallRecordStatistics=" + mobileCallRecordStatistics
				+ ", mobileAccountInfos=" + mobileAccountInfos + ", mobileBillInfoMonths=" + mobileBillInfoMonths
				+ ", mobileBusinessInfos=" + mobileBusinessInfos + ", mobileFamilyNetInfos=" + mobileFamilyNetInfos
				+ ", mobileGroupNetInfos=" + mobileGroupNetInfos + ", mobilePaymentInfos=" + mobilePaymentInfos
				+ ", mobilePointsInfos=" + mobilePointsInfos + ", mobileRelationshipInfos=" + mobileRelationshipInfos
				+ ", mobileSmsRecordDetails=" + mobileSmsRecordDetails + ", message=" + message + ", createtime="
				+ createtime + ", errorCode=" + errorCode + ", profile=" + profile + "]";
	}
	
	
}
