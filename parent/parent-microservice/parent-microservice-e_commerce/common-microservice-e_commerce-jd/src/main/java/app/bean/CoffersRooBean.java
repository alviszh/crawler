/**
  * Copyright 2017 bejson.com 
  */
package app.bean;

import com.microservice.dao.entity.crawler.e_commerce.jingdong.JDCoffers;

/**
 * Auto-generated: 2017-12-15 17:8:22
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class CoffersRooBean {

	private JDCoffers accountResult;
	private String incomeAward;

	public void setAccountResult(JDCoffers accountResult) {
		this.accountResult = accountResult;
	}

	public JDCoffers getAccountResult() {
		return accountResult;
	}

	public void setIncomeAward(String incomeAward) {
		this.incomeAward = incomeAward;
	}

	public String getIncomeAward() {
		return incomeAward;
	}

	@Override
	public String toString() {
		return "GoldRooBean [accountResult=" + accountResult + ", incomeAward=" + incomeAward + "]";
	}
	
	

}