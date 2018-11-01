package app.bean;

import java.util.Date;
import java.util.List;

import com.microservice.dao.entity.crawler.mobile.etl.MobileReportBaseInfo;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportCallStatistics;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportCallStatisticsSixMonth;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportCallStatisticsThreeMonth;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportConsumption;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportConsumptionSixMonth;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportConsumptionThreeMonth;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportContactsStatisticsSixMonth;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportContactsStatisticsThreeMonth;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportFamilyDetail;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportFamilyInfo;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportLocationStatistics;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportPayment;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportSMS;


public class WebDataReport {
	
	public RequestParam param;
	public List<MobileReportBaseInfo> mobileReportBaseInfos;
	public List<MobileReportCallStatistics> mobileReportCallStatisticss;
	public List<MobileReportCallStatisticsSixMonth> mobileReportCallStatisticsSixMonths;
	public List<MobileReportCallStatisticsThreeMonth> mobileReportCallStatisticsThreeMonths;
	public List<MobileReportConsumption> mobileReportConsumptions;
	public List<MobileReportConsumptionSixMonth> mobileReportConsumptionSixMonths;
	public List<MobileReportConsumptionThreeMonth> mobileReportConsumptionThreeMonths;
	public List<MobileReportContactsStatisticsSixMonth> mobileReportContractsStatisticsSixMonths;
	public List<MobileReportContactsStatisticsThreeMonth> mobileReportContractsStatisticsThreeMonths;
	public List<MobileReportFamilyDetail> mobileReportFamilyDetails;
	public List<MobileReportFamilyInfo> mobileReportFamilyInfos;
	public List<MobileReportLocationStatistics> mobileReportLocationStatisticss;
	public List<MobileReportPayment> mobileReportPayments;
	public List<MobileReportSMS> mobileReportSMSs;
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
	public List<MobileReportBaseInfo> getMobileReportBaseInfos() {
		return mobileReportBaseInfos;
	}
	public void setMobileReportBaseInfos(List<MobileReportBaseInfo> mobileReportBaseInfos) {
		this.mobileReportBaseInfos = mobileReportBaseInfos;
	}
	public List<MobileReportCallStatistics> getMobileReportCallStatisticss() {
		return mobileReportCallStatisticss;
	}
	public void setMobileReportCallStatisticss(List<MobileReportCallStatistics> mobileReportCallStatisticss) {
		this.mobileReportCallStatisticss = mobileReportCallStatisticss;
	}
	public List<MobileReportCallStatisticsSixMonth> getMobileReportCallStatisticsSixMonths() {
		return mobileReportCallStatisticsSixMonths;
	}
	public void setMobileReportCallStatisticsSixMonths(
			List<MobileReportCallStatisticsSixMonth> mobileReportCallStatisticsSixMonths) {
		this.mobileReportCallStatisticsSixMonths = mobileReportCallStatisticsSixMonths;
	}
	public List<MobileReportCallStatisticsThreeMonth> getMobileReportCallStatisticsThreeMonths() {
		return mobileReportCallStatisticsThreeMonths;
	}
	public void setMobileReportCallStatisticsThreeMonths(
			List<MobileReportCallStatisticsThreeMonth> mobileReportCallStatisticsThreeMonths) {
		this.mobileReportCallStatisticsThreeMonths = mobileReportCallStatisticsThreeMonths;
	}
	public List<MobileReportConsumption> getMobileReportConsumptions() {
		return mobileReportConsumptions;
	}
	public void setMobileReportConsumptions(List<MobileReportConsumption> mobileReportConsumptions) {
		this.mobileReportConsumptions = mobileReportConsumptions;
	}
	public List<MobileReportConsumptionSixMonth> getMobileReportConsumptionSixMonths() {
		return mobileReportConsumptionSixMonths;
	}
	public void setMobileReportConsumptionSixMonths(
			List<MobileReportConsumptionSixMonth> mobileReportConsumptionSixMonths) {
		this.mobileReportConsumptionSixMonths = mobileReportConsumptionSixMonths;
	}
	public List<MobileReportConsumptionThreeMonth> getMobileReportConsumptionThreeMonths() {
		return mobileReportConsumptionThreeMonths;
	}
	public void setMobileReportConsumptionThreeMonths(
			List<MobileReportConsumptionThreeMonth> mobileReportConsumptionThreeMonths) {
		this.mobileReportConsumptionThreeMonths = mobileReportConsumptionThreeMonths;
	}
	public List<MobileReportContactsStatisticsSixMonth> getMobileReportContractsStatisticsSixMonths() {
		return mobileReportContractsStatisticsSixMonths;
	}
	public void setMobileReportContractsStatisticsSixMonths(
			List<MobileReportContactsStatisticsSixMonth> mobileReportContractsStatisticsSixMonths) {
		this.mobileReportContractsStatisticsSixMonths = mobileReportContractsStatisticsSixMonths;
	}
	public List<MobileReportContactsStatisticsThreeMonth> getMobileReportContractsStatisticsThreeMonths() {
		return mobileReportContractsStatisticsThreeMonths;
	}
	public void setMobileReportContractsStatisticsThreeMonths(
			List<MobileReportContactsStatisticsThreeMonth> mobileReportContractsStatisticsThreeMonths) {
		this.mobileReportContractsStatisticsThreeMonths = mobileReportContractsStatisticsThreeMonths;
	}
	public List<MobileReportFamilyDetail> getMobileReportFamilyDetails() {
		return mobileReportFamilyDetails;
	}
	public void setMobileReportFamilyDetails(List<MobileReportFamilyDetail> mobileReportFamilyDetails) {
		this.mobileReportFamilyDetails = mobileReportFamilyDetails;
	}
	public List<MobileReportFamilyInfo> getMobileReportFamilyInfos() {
		return mobileReportFamilyInfos;
	}
	public void setMobileReportFamilyInfos(List<MobileReportFamilyInfo> mobileReportFamilyInfos) {
		this.mobileReportFamilyInfos = mobileReportFamilyInfos;
	}
	public List<MobileReportLocationStatistics> getMobileReportLocationStatisticss() {
		return mobileReportLocationStatisticss;
	}
	public void setMobileReportLocationStatisticss(List<MobileReportLocationStatistics> mobileReportLocationStatisticss) {
		this.mobileReportLocationStatisticss = mobileReportLocationStatisticss;
	}
	public List<MobileReportPayment> getMobileReportPayments() {
		return mobileReportPayments;
	}
	public void setMobileReportPayments(List<MobileReportPayment> mobileReportPayments) {
		this.mobileReportPayments = mobileReportPayments;
	}
	public List<MobileReportSMS> getMobileReportSMSs() {
		return mobileReportSMSs;
	}
	public void setMobileReportSMSs(List<MobileReportSMS> mobileReportSMSs) {
		this.mobileReportSMSs = mobileReportSMSs;
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
		return "WebDataReport [param=" + param + ", mobileReportBaseInfos=" + mobileReportBaseInfos
				+ ", mobileReportCallStatisticss=" + mobileReportCallStatisticss
				+ ", mobileReportCallStatisticsSixMonths=" + mobileReportCallStatisticsSixMonths
				+ ", mobileReportCallStatisticsThreeMonths=" + mobileReportCallStatisticsThreeMonths
				+ ", mobileReportConsumptions=" + mobileReportConsumptions + ", mobileReportConsumptionSixMonths="
				+ mobileReportConsumptionSixMonths + ", mobileReportConsumptionThreeMonths="
				+ mobileReportConsumptionThreeMonths + ", mobileReportContractsStatisticsSixMonths="
				+ mobileReportContractsStatisticsSixMonths + ", mobileReportContractsStatisticsThreeMonths="
				+ mobileReportContractsStatisticsThreeMonths + ", mobileReportFamilyDetails="
				+ mobileReportFamilyDetails + ", mobileReportFamilyInfos=" + mobileReportFamilyInfos
				+ ", mobileReportLocationStatisticss=" + mobileReportLocationStatisticss + ", mobileReportPayments="
				+ mobileReportPayments + ", mobileReportSMSs=" + mobileReportSMSs + ", message=" + message
				+ ", createtime=" + createtime + ", errorCode=" + errorCode + ", profile=" + profile + "]";
	}
	
}
