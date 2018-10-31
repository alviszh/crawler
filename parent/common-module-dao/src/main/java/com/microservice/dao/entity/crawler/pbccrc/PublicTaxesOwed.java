package com.microservice.dao.entity.crawler.pbccrc;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

// 公共记录 -- 欠税记录
@Entity
@Table(name="public_taxes_owed")
public class PublicTaxesOwed extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = -8012765685543584501L;
	private String mapping_id;  //uuid 唯一标识
	private String report_no;   //人行征信报告编号
	private String type;  //1.欠税记录 2.民事判决记录 3.强制执行记录 4.行政处罚记录 5.电信欠费记录

	private String taxAuthorities;//主管税务机关
	
	private String taxTimeStatistics;//欠税统计时间
	
	private String totalTaxes;//欠税总额
	
	private String taxpayerIdNum;//纳税人识别号

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTaxAuthorities() {
		return taxAuthorities;
	}

	public void setTaxAuthorities(String taxAuthorities) {
		this.taxAuthorities = taxAuthorities;
	}

	public String getTaxTimeStatistics() {
		return taxTimeStatistics;
	}

	public void setTaxTimeStatistics(String taxTimeStatistics) {
		this.taxTimeStatistics = taxTimeStatistics;
	}

	public String getTotalTaxes() {
		return totalTaxes;
	}

	public void setTotalTaxes(String totalTaxes) {
		this.totalTaxes = totalTaxes;
	}

	public String getTaxpayerIdNum() {
		return taxpayerIdNum;
	}

	public void setTaxpayerIdNum(String taxpayerIdNum) {
		this.taxpayerIdNum = taxpayerIdNum;
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

	@Override
	public String toString() {
		return "PublicTaxesOwed{" +
				"mapping_id='" + mapping_id + '\'' +
				", report_no='" + report_no + '\'' +
				", type='" + type + '\'' +
				", taxAuthorities='" + taxAuthorities + '\'' +
				", taxTimeStatistics='" + taxTimeStatistics + '\'' +
				", totalTaxes='" + totalTaxes + '\'' +
				", taxpayerIdNum='" + taxpayerIdNum + '\'' +
				'}';
	}
}
