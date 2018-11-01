/**
  * Copyright 2017 bejson.com 
  */
package app.bean.creditcard;

import java.util.List;

import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaCebitCardCrcdTransList;

/**
 * Auto-generated: 2017-11-29 16:41:24
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class TransListResult {

	private String sumNo;
	private String pageNo;
	private String primary;
	private String dealCount;
	private List<BocchinaCebitCardCrcdTransList> transList;

	public void setSumNo(String sumNo) {
		this.sumNo = sumNo;
	}

	public String getSumNo() {
		return sumNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPrimary(String primary) {
		this.primary = primary;
	}

	public String getPrimary() {
		return primary;
	}

	public void setDealCount(String dealCount) {
		this.dealCount = dealCount;
	}

	public String getDealCount() {
		return dealCount;
	}

	public void setTransList(List<BocchinaCebitCardCrcdTransList> transList) {
		this.transList = transList;
	}

	public List<BocchinaCebitCardCrcdTransList> getTransList() {
		return transList;
	}

	@Override
	public String toString() {
		return "TransListResult [sumNo=" + sumNo + ", pageNo=" + pageNo + ", primary=" + primary + ", dealCount="
				+ dealCount + ", transList=" + transList + "]";
	}
	
	

}