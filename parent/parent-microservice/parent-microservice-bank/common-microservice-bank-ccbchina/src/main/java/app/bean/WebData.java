package app.bean;

import java.util.List;
import com.microservice.dao.entity.crawler.bank.ccbchina.CcbChinaCreditcardAccountType;
import com.microservice.dao.entity.crawler.bank.ccbchina.CcbChinaCreditcardTransFlow;

public class WebData {

	public List<CcbChinaCreditcardTransFlow> transFlows;
	public List<CcbChinaCreditcardAccountType> accountTypes;
	
	public List<CcbChinaCreditcardTransFlow> getTransFlows() {
		return transFlows;
	}
	public void setTransFlows(List<CcbChinaCreditcardTransFlow> transFlows) {
		this.transFlows = transFlows;
	}
	public List<CcbChinaCreditcardAccountType> getAccountTypes() {
		return accountTypes;
	}
	public void setAccountTypes(List<CcbChinaCreditcardAccountType> accountTypes) {
		this.accountTypes = accountTypes;
	}
	
}
