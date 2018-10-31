package com.crawler.pbccrc.json;

//公共记录 -- 行政处罚记录
public class AdministrativePunishmenRecord {

	private String organization;//处罚机构
	
	private String docNo;//文书编号
	
	private String content;//处罚内容
	
	private String money;//处罚金额
	
	private String effectiveTime;  //生效时间
	
	private String isReview; //是否行政复议
	
	private String reviewResult;//复议结果
	
	private String deadline;//处罚截止时间

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getDocNo() {
		return docNo;
	}

	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public String getIsReview() {
		return isReview;
	}

	public void setIsReview(String isReview) {
		this.isReview = isReview;
	}

	public String getReviewResult() {
		return reviewResult;
	}

	public void setReviewResult(String reviewResult) {
		this.reviewResult = reviewResult;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	@Override
	public String toString() {
		return "AdministrativePunishmenRecords [organization=" + organization + ", docNo=" + docNo + ", content="
				+ content + ", money=" + money + ", effectiveTime=" + effectiveTime + ", isReview=" + isReview
				+ ", reviewResult=" + reviewResult + ", deadline=" + deadline + "]";
	}
	
	
	
	
	
	
	
}
