package com.crawler.pbccrc.json;

import java.io.Serializable;
import java.util.List;

public class PbcCreditReportFeed implements Serializable {
	private static final long serialVersionUID = -8489599136031950805L;
	private ReportBase reportBase; 			//表头
	private CreditRecord creditRecord; 		//信用卡，贷款，保证人代偿信息
	private List<QueryRecord> queryRecords; //查询记录
	private PublicRecord publicRecord; //公共记录 这部分包含您最近5年内的欠税记录、民事判决记录、强制执行记录、行政处罚记录及电信欠费记录。 金额类数据均以人民币计算， 精确到元。
	
	public ReportBase getReportBase() {
		return reportBase;
	}
	public void setReportBase(ReportBase reportBase) {
		this.reportBase = reportBase;
	}
	public CreditRecord getCreditRecord() {
		return creditRecord;
	}
	public void setCreditRecord(CreditRecord creditRecord) {
		this.creditRecord = creditRecord;
	}
	public List<QueryRecord> getQueryRecords() {
		return queryRecords;
	}
	public void setQueryRecords(List<QueryRecord> queryRecords) {
		this.queryRecords = queryRecords;
	}
	public PublicRecord getPublicRecord() {
		return publicRecord;
	}
	public void setPublicRecord(PublicRecord publicRecord) {
		this.publicRecord = publicRecord;
	}
	@Override
	public String toString() {
		return "PbcCreditReportFeed [reportBase=" + reportBase + ", creditRecord=" + creditRecord + ", queryRecords="
				+ queryRecords + ", publicRecord=" + publicRecord + "]";
	}
	
	
	
}
