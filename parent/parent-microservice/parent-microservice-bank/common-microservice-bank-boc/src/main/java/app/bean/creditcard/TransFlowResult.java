/**
  * Copyright 2017 bejson.com 
  */
package app.bean.creditcard;

import java.util.List;

import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaCebitCardCrcdAccountInfoList;
import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaCebitCardCrcdCustomerInfo;
import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaCebitCardCrcdScoreInfoList;

/**
 * Auto-generated: 2017-11-28 16:15:43
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class TransFlowResult {

	private String billDate;
	private BocchinaCebitCardCrcdCustomerInfo crcdCustomerInfo;
	private List<BocchinaCebitCardCrcdAccountInfoList> crcdAccountInfoList;
	private List<BocchinaCebitCardCrcdScoreInfoList> crcdScoreInfoList;
	private List<String> crcdBillInfoList;

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setCrcdCustomerInfo(BocchinaCebitCardCrcdCustomerInfo crcdCustomerInfo) {
		this.crcdCustomerInfo = crcdCustomerInfo;
	}

	public BocchinaCebitCardCrcdCustomerInfo getCrcdCustomerInfo() {
		return crcdCustomerInfo;
	}

	public void setCrcdAccountInfoList(List<BocchinaCebitCardCrcdAccountInfoList> crcdAccountInfoList) {
		this.crcdAccountInfoList = crcdAccountInfoList;
	}

	public List<BocchinaCebitCardCrcdAccountInfoList> getCrcdAccountInfoList() {
		return crcdAccountInfoList;
	}

	public void setCrcdScoreInfoList(List<BocchinaCebitCardCrcdScoreInfoList> crcdScoreInfoList) {
		this.crcdScoreInfoList = crcdScoreInfoList;
	}

	public List<BocchinaCebitCardCrcdScoreInfoList> getCrcdScoreInfoList() {
		return crcdScoreInfoList;
	}

	public void setCrcdBillInfoList(List<String> crcdBillInfoList) {
		this.crcdBillInfoList = crcdBillInfoList;
	}

	public List<String> getCrcdBillInfoList() {
		return crcdBillInfoList;
	}

	@Override
	public String toString() {
		return "TransFlowResult [billDate=" + billDate + ", crcdCustomerInfo=" + crcdCustomerInfo
				+ ", crcdAccountInfoList=" + crcdAccountInfoList + ", crcdScoreInfoList=" + crcdScoreInfoList
				+ ", crcdBillInfoList=" + crcdBillInfoList + "]";
	}

}