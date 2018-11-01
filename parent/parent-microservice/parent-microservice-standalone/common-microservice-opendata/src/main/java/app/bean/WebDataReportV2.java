package app.bean;

import java.util.Date;
import java.util.List;

import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportBaseinfo;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportCallDetailStatistics;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportCallSumStatistics;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportCallTimeDetailStatistics;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportCarrierConsume;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportConsumeinfo;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportContactsTop;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportLocationTop;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportPeriodStatistics;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportSocialAnalysisSummary;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportStability;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportVitality;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportVitalityAnalysisSummary;


public class WebDataReportV2 {
	
	public RequestParam param;
	public List<ProMobileReportBaseinfo> proMobileReportBaseinfos;
	public List<ProMobileReportSocialAnalysisSummary> proMobileReportSocialAnalysisSummarys;
	public List<ProMobileReportContactsTop> proMobileReportContactsTops;
	public List<ProMobileReportLocationTop> proMobileReportLocationTops;
	public List<ProMobileReportVitalityAnalysisSummary> proMobileReportVitalityAnalysisSummarys;
	public List<ProMobileReportConsumeinfo> proMobileReportConsumeinfos;
	public List<ProMobileReportCarrierConsume> proMobileReportCarrierConsumes;
	public List<ProMobileReportCallSumStatistics> proMobileReportCallSumStatisticss;
	public List<ProMobileReportCallDetailStatistics> proMobileReportCallDetailStatistics3ms;
	public List<ProMobileReportCallDetailStatistics> proMobileReportCallDetailStatistics6ms;
	public List<ProMobileReportPeriodStatistics> proMobileReportPeriodStatisticss;
	public List<ProMobileReportCallTimeDetailStatistics> proMobileReportCallTimeDetailStatisticss;
	public List<ProMobileReportStability> proMobileReportStabilitys;
	public List<ProMobileReportVitality> proMobileReportVitalitys;
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
	public List<ProMobileReportBaseinfo> getProMobileReportBaseinfos() {
		return proMobileReportBaseinfos;
	}
	public void setProMobileReportBaseinfos(List<ProMobileReportBaseinfo> proMobileReportBaseinfos) {
		this.proMobileReportBaseinfos = proMobileReportBaseinfos;
	}
	public List<ProMobileReportSocialAnalysisSummary> getProMobileReportSocialAnalysisSummarys() {
		return proMobileReportSocialAnalysisSummarys;
	}
	public void setProMobileReportSocialAnalysisSummarys(
			List<ProMobileReportSocialAnalysisSummary> proMobileReportSocialAnalysisSummarys) {
		this.proMobileReportSocialAnalysisSummarys = proMobileReportSocialAnalysisSummarys;
	}
	public List<ProMobileReportContactsTop> getProMobileReportContactsTops() {
		return proMobileReportContactsTops;
	}
	public void setProMobileReportContactsTops(List<ProMobileReportContactsTop> proMobileReportContactsTops) {
		this.proMobileReportContactsTops = proMobileReportContactsTops;
	}
	public List<ProMobileReportLocationTop> getProMobileReportLocationTops() {
		return proMobileReportLocationTops;
	}
	public void setProMobileReportLocationTops(List<ProMobileReportLocationTop> proMobileReportLocationTops) {
		this.proMobileReportLocationTops = proMobileReportLocationTops;
	}
	public List<ProMobileReportVitalityAnalysisSummary> getProMobileReportVitalityAnalysisSummarys() {
		return proMobileReportVitalityAnalysisSummarys;
	}
	public void setProMobileReportVitalityAnalysisSummarys(
			List<ProMobileReportVitalityAnalysisSummary> proMobileReportVitalityAnalysisSummarys) {
		this.proMobileReportVitalityAnalysisSummarys = proMobileReportVitalityAnalysisSummarys;
	}
	public List<ProMobileReportConsumeinfo> getProMobileReportConsumeinfos() {
		return proMobileReportConsumeinfos;
	}
	public void setProMobileReportConsumeinfos(List<ProMobileReportConsumeinfo> proMobileReportConsumeinfos) {
		this.proMobileReportConsumeinfos = proMobileReportConsumeinfos;
	}
	public List<ProMobileReportCarrierConsume> getProMobileReportCarrierConsumes() {
		return proMobileReportCarrierConsumes;
	}
	public void setProMobileReportCarrierConsumes(List<ProMobileReportCarrierConsume> proMobileReportCarrierConsumes) {
		this.proMobileReportCarrierConsumes = proMobileReportCarrierConsumes;
	}
	public List<ProMobileReportCallSumStatistics> getProMobileReportCallSumStatisticss() {
		return proMobileReportCallSumStatisticss;
	}
	public void setProMobileReportCallSumStatisticss(
			List<ProMobileReportCallSumStatistics> proMobileReportCallSumStatisticss) {
		this.proMobileReportCallSumStatisticss = proMobileReportCallSumStatisticss;
	}
	
	public List<ProMobileReportCallDetailStatistics> getProMobileReportCallDetailStatistics3ms() {
		return proMobileReportCallDetailStatistics3ms;
	}
	public void setProMobileReportCallDetailStatistics3ms(
			List<ProMobileReportCallDetailStatistics> proMobileReportCallDetailStatistics3ms) {
		this.proMobileReportCallDetailStatistics3ms = proMobileReportCallDetailStatistics3ms;
	}
	
	public List<ProMobileReportCallDetailStatistics> getProMobileReportCallDetailStatistics6ms() {
		return proMobileReportCallDetailStatistics6ms;
	}
	public void setProMobileReportCallDetailStatistics6ms(
			List<ProMobileReportCallDetailStatistics> proMobileReportCallDetailStatistics6ms) {
		this.proMobileReportCallDetailStatistics6ms = proMobileReportCallDetailStatistics6ms;
	}
	public List<ProMobileReportPeriodStatistics> getProMobileReportPeriodStatisticss() {
		return proMobileReportPeriodStatisticss;
	}
	public void setProMobileReportPeriodStatisticss(
			List<ProMobileReportPeriodStatistics> proMobileReportPeriodStatisticss) {
		this.proMobileReportPeriodStatisticss = proMobileReportPeriodStatisticss;
	}
	public List<ProMobileReportCallTimeDetailStatistics> getProMobileReportCallTimeDetailStatisticss() {
		return proMobileReportCallTimeDetailStatisticss;
	}
	public void setProMobileReportCallTimeDetailStatisticss(
			List<ProMobileReportCallTimeDetailStatistics> proMobileReportCallTimeDetailStatisticss) {
		this.proMobileReportCallTimeDetailStatisticss = proMobileReportCallTimeDetailStatisticss;
	}
	public List<ProMobileReportStability> getProMobileReportStabilitys() {
		return proMobileReportStabilitys;
	}
	public void setProMobileReportStabilitys(List<ProMobileReportStability> proMobileReportStabilitys) {
		this.proMobileReportStabilitys = proMobileReportStabilitys;
	}
	public List<ProMobileReportVitality> getProMobileReportVitalitys() {
		return proMobileReportVitalitys;
	}
	public void setProMobileReportVitalitys(List<ProMobileReportVitality> proMobileReportVitalitys) {
		this.proMobileReportVitalitys = proMobileReportVitalitys;
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
		return "WebDataReportV2 [param=" + param + ", proMobileReportBaseinfos=" + proMobileReportBaseinfos
				+ ", proMobileReportSocialAnalysisSummarys=" + proMobileReportSocialAnalysisSummarys
				+ ", proMobileReportContactsTops=" + proMobileReportContactsTops + ", proMobileReportLocationTops="
				+ proMobileReportLocationTops + ", proMobileReportVitalityAnalysisSummarys="
				+ proMobileReportVitalityAnalysisSummarys + ", proMobileReportConsumeinfos="
				+ proMobileReportConsumeinfos + ", proMobileReportCarrierConsumes=" + proMobileReportCarrierConsumes
				+ ", proMobileReportCallSumStatisticss=" + proMobileReportCallSumStatisticss
				+ ", proMobileReportCallDetailStatistics3ms=" + proMobileReportCallDetailStatistics3ms
				+ ", proMobileReportCallDetailStatistics6ms=" + proMobileReportCallDetailStatistics6ms
				+ ", proMobileReportPeriodStatisticss=" + proMobileReportPeriodStatisticss
				+ ", proMobileReportCallTimeDetailStatisticss=" + proMobileReportCallTimeDetailStatisticss
				+ ", proMobileReportStabilitys=" + proMobileReportStabilitys + ", proMobileReportVitalitys="
				+ proMobileReportVitalitys + ", message=" + message + ", createtime=" + createtime + ", errorCode="
				+ errorCode + ", profile=" + profile + "]";
	}
	
	
}
