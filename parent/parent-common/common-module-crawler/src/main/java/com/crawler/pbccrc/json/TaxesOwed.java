package com.crawler.pbccrc.json;

import java.io.Serializable;

// 公共记录 -- 欠税记录
public class TaxesOwed implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8115706136432743521L;
	
	private String taxAuthorities;//主管税务机关
	
	private String taxTimeStatistics;//欠税统计时间
	
	private String totalTaxes;//欠税总额
	
	private String taxpayerIdNum;//纳税人识别号

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

	@Override
	public String toString() {
		return "TaxesOwed [taxAuthorities=" + taxAuthorities + ", taxTimeStatistics=" + taxTimeStatistics
				+ ", totalTaxes=" + totalTaxes + ", taxpayerIdNum=" + taxpayerIdNum + "]";
	}
	
	
	
	
	
	
	
	 

}
