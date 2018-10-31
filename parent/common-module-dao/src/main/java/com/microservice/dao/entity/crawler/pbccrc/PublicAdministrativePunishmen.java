package com.microservice.dao.entity.crawler.pbccrc;

//公共记录 -- 行政处罚记录

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="public_administrative_punishmen")
public class PublicAdministrativePunishmen extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = -4660626669290986142L;

	private String mapping_id;  //uuid 唯一标识
	private String report_no;   //人行征信报告编号
	private String type;  //1.欠税记录 2.民事判决记录 3.强制执行记录 4.行政处罚记录 5.电信欠费记录

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

	public String getMapping_id() {
		return mapping_id;
	}

	public void setMapping_id(String mapping_id) {
		this.mapping_id = mapping_id;
	}

	public String getReport_no() {
		return report_no;
	}

	public void setReport_no(String report_no) {
		this.report_no = report_no;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "PublicAdministrativePunishmen{" +
				"mapping_id='" + mapping_id + '\'' +
				", report_no='" + report_no + '\'' +
				", type='" + type + '\'' +
				", organization='" + organization + '\'' +
				", docNo='" + docNo + '\'' +
				", content='" + content + '\'' +
				", money='" + money + '\'' +
				", effectiveTime='" + effectiveTime + '\'' +
				", isReview='" + isReview + '\'' +
				", reviewResult='" + reviewResult + '\'' +
				", deadline='" + deadline + '\'' +
				'}';
	}
}
